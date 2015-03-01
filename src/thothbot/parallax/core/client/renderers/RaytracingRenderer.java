/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution 
 * 3.0 Unported License. for more details.
 * 
 * You should have received a copy of the the Creative Commons Attribution 
 * 3.0 Unported License along with Parallax. 
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */

package thothbot.parallax.core.client.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.animation.client.AnimationScheduler.AnimationHandle;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CanvasPixelArray;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.cameras.PerspectiveCamera;
import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.FastMap;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryObject;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.core.Object3D.Traverse;
import thothbot.parallax.core.shared.core.Raycaster;
import thothbot.parallax.core.shared.core.Raycaster.Intersect;
import thothbot.parallax.core.shared.helpers.HasRaytracingPhysicalAttenuation;
import thothbot.parallax.core.shared.lights.HasIntensity;
import thothbot.parallax.core.shared.lights.Light;
import thothbot.parallax.core.shared.materials.HasColor;
import thothbot.parallax.core.shared.materials.HasRaytracingGlass;
import thothbot.parallax.core.shared.materials.HasRaytracingMirror;
import thothbot.parallax.core.shared.materials.HasShading;
import thothbot.parallax.core.shared.materials.HasVertexColors;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.materials.MeshLambertMaterial;
import thothbot.parallax.core.shared.materials.MeshPhongMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Mathematics;
import thothbot.parallax.core.shared.math.Matrix3;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Ray;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.scenes.Scene;

public class RaytracingRenderer extends AbstractRenderer 
{
	
	private static final int maxRecursionDepth = 3;
	private static final int blockSize = 64;
	
	private static class ObjectMatrixes
	{
		public Matrix3 normalMatrix;
		public Matrix4 inverseMatrix;
		
		public ObjectMatrixes() 
		{
			this.normalMatrix = new Matrix3();
			this.inverseMatrix = new Matrix4();
		}
	}
	
	Canvas canvas;
    Context2d context;

	Vector3 origin = new Vector3();
	Vector3 direction = new Vector3();
	
	Vector3 cameraPosition = new Vector3();

	Raycaster raycaster = new Raycaster( origin, direction );
	Raycaster raycasterLight = new Raycaster();
	
	double perspective;
	Matrix4 modelViewMatrix = new Matrix4();	
	Matrix3 cameraNormalMatrix = new Matrix3();

	List<Object3D> objects;
	List<Light> lights = new ArrayList<Light>();
	
	AnimationHandle animationHandler;
	
	Map<String, ObjectMatrixes> cache = GWT.isScript() ? 
			new FastMap<ObjectMatrixes>() : new HashMap<String, ObjectMatrixes>();
			
			
	Canvas canvasBlock;
	ImageData imagedata = null;

	public RaytracingRenderer(int width, int height) {
		canvas = Canvas.createIfSupported();
		canvas.ensureDebugId("canvas2d");
		
		setSize(width, height);
		
	    context = canvas.getContext2d();
	    context.setFillStyle( "#FFFFFF" );
	    
	    
	    canvasBlock = Canvas.createIfSupported();
		canvasBlock.setCoordinateSpaceWidth(blockSize);
		canvasBlock.setCoordinateSpaceHeight(blockSize);

		RootPanel.get().add(canvasBlock, -10000, 0);

		Context2d contextBlock = canvasBlock.getContext2d();
		imagedata = contextBlock.getImageData( 0, 0, blockSize, blockSize );
	}
	
	public Canvas getCanvas() {
		return this.canvas;
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
	}

	@Override
	public void setClearColor(Color color, double alpha) 
	{
		this.clearColor.copy(color);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render( Scene scene, final Camera camera ) {

		if ( isAutoClear() == true ) this.clear();

//		cancelAnimationFrame( animationFrameId );

		// update scene graph

		if ( scene.isAutoUpdate() == true ) scene.updateMatrixWorld(false);

		// update camera matrices

		if ( camera.getParent() == null ) camera.updateMatrixWorld(false);

		camera.getMatrixWorldInverse().getInverse( camera.getMatrixWorld() );
		cameraPosition.setFromMatrixPosition( camera.getMatrixWorld() );

		//

		cameraNormalMatrix.getNormalMatrix( camera.getMatrixWorld() );
		origin.copy( cameraPosition );

		perspective = 0.5 / Math.tan( Mathematics.degToRad( ((PerspectiveCamera)camera).getFov() * 0.5 ) ) * getAbsoluteHeight();

		objects = scene.getChildren();

		// collect lights and set up object matrices

		lights = new ArrayList<Light>();

		scene.traverse(new Traverse() {
			
			@Override
			public void callback(Object3D object) {
				if ( object instanceof Light ) {

					lights.add( (Light) object );

				}

				if ( !cache.containsKey( object.getId() + "" ) ) 
				{
					cache.put( object.getId() + "", new ObjectMatrixes());
				}

				modelViewMatrix.multiply( camera.getMatrixWorldInverse(), object.getMatrixWorld() );

				ObjectMatrixes _object = cache.get( object.getId() + "" );
				
				_object.normalMatrix.getNormalMatrix( modelViewMatrix );
				_object.inverseMatrix.getInverse( object.getMatrixWorld() );

				
			}
		});
		
		renderBlock(0, 0);
	}

	private void renderBlock(int blockX, int blockY) 
	{
		Log.debug("Raytracing -- Render block: " + blockX + ", " + blockY);
		Color pixelColor = new Color();
		
		//
		
		int index = 0;

		for ( int y = 0; y < blockSize; y ++ ) {

			for ( int x = 0; x < blockSize; x ++, index += 4 ) {

				// spawn primary ray at pixel position

				origin.copy( cameraPosition );

				direction.set( x + blockX - getAbsoluteWidth()/2, - ( y + blockY - getAbsoluteHeight()/2 ), - perspective );
				direction.apply( cameraNormalMatrix ).normalize();

				spawnRay( origin, direction, pixelColor, 0 );		

				// convert from linear to gamma

				imagedata.getData().set( index , (int)(Math.sqrt( pixelColor.getR() ) * 255) );
				imagedata.getData().set( index + 1 , (int)(Math.sqrt( pixelColor.getG() ) * 255) );
				imagedata.getData().set( index + 2 , (int)(Math.sqrt( pixelColor.getB() ) * 255) );

				imagedata.getData().set( index + 3 , 255 ); // alpha
			}

		}

		context.putImageData( imagedata, blockX, blockY );

		blockX += blockSize;

		if ( blockX >= getAbsoluteWidth() ) {

			blockX = 0;
			blockY += blockSize;

			if ( blockY >= getAbsoluteHeight() ) return;

		}

		context.fillRect( blockX, blockY, blockSize, blockSize );

		final int _blockX = blockX;
		final int _blockY = blockY;
		
		animationHandler = AnimationScheduler.get().requestAnimationFrame(new AnimationCallback() {
						
			@Override
			public void execute(double timestamp) {
				
				renderBlock( _blockX, _blockY );
								
			}
		});

	}

	Color diffuseColor = new Color();
	Color specularColor = new Color();
	Color lightColor = new Color();
	Color schlick = new Color();

	Color lightContribution = new Color();

	Vector3 eyeVector = new Vector3();
	Vector3 lightVector = new Vector3();
	Vector3 normalVector = new Vector3();
	Vector3 halfVector = new Vector3();

	Vector3 localPoint = new Vector3();
	Vector3 reflectionVector = new Vector3();

	Vector3 tmpVec = new Vector3();

	Color[] tmpColor = null;

	private void spawnRay(Vector3 rayOrigin, Vector3 rayDirection, Color outputColor, int recursionDepth) 
	{
		// Init the tmp array
		if(tmpColor == null)
		{
			tmpColor = new Color[maxRecursionDepth];
			for ( int i = 0; i < maxRecursionDepth; i ++ )
				tmpColor[ i ] = new Color();
		}

		Ray ray = raycaster.getRay();

		ray.setOrigin( rayOrigin );
		ray.setDirection( rayDirection );

		//

		Ray rayLight = raycasterLight.getRay();

		//

		outputColor.setRGB( 0, 0, 0 );

		//

		List<Intersect> intersections = raycaster.intersectObjects( objects, true );

		// ray didn't find anything
		// (here should come setting of background color?)

		if ( intersections.size() == 0 ) {

			return;

		}

		// ray hit

		Intersect intersection = intersections.get( 0 );

		Vector3 point = intersection.point;
		GeometryObject object = intersection.object;
		Material material = object.getMaterial();
		Face3 face = intersection.face;

		List<Vector3> vertices = ((Geometry)object.getGeometry()).getVertices();

		//

		ObjectMatrixes _object = cache.get( object.getId() + "" );

		localPoint.copy( point ).apply( _object.inverseMatrix );
		eyeVector.sub( raycaster.getRay().getOrigin(), point ).normalize();

		// resolve pixel diffuse color

		if ( material instanceof MeshLambertMaterial ||
			 material instanceof MeshPhongMaterial ||
			 material instanceof MeshBasicMaterial ) 
		{
			diffuseColor.copyGammaToLinear( ((HasColor)material).getColor() );
		} 
		else 
		{
			diffuseColor.setRGB( 1, 1, 1 );
		}

		if ( material instanceof HasVertexColors 
				&& ((HasVertexColors)material).isVertexColors() == Material.COLORS.FACE ) 
		{
			diffuseColor.multiply( face.getColor() );
		}
		
		// compute light shading

		rayLight.getOrigin().copy( point );

		if ( material instanceof MeshBasicMaterial ) 
		{
			for ( int i = 0, l = lights.size(); i < l; i ++ ) 
			{

				Light light = lights.get( i );

				lightVector.setFromMatrixPosition( light.getMatrixWorld() );
				lightVector.sub( point );

				rayLight.getDirection().copy( lightVector ).normalize();

				List<Intersect> intersections2 = raycasterLight.intersectObjects( objects, true );

				// point in shadow

				if ( intersections2.size() > 0 ) continue;

				// point visible

				outputColor.add( diffuseColor );

			}

		} else if ( material instanceof MeshLambertMaterial ||
					material instanceof MeshPhongMaterial ) {

			boolean normalComputed = false;

			for ( int i = 0, l = lights.size(); i < l; i ++ ) {

				Light light = lights.get( i );

				lightColor.copyGammaToLinear( light.getColor() );

				lightVector.setFromMatrixPosition( light.getMatrixWorld() );
				lightVector.sub( point );

				rayLight.getDirection().copy( lightVector ).normalize();

				List<Intersect> intersections3 = raycasterLight.intersectObjects( objects, true );

				// point in shadow

				if ( intersections3.size() > 0 ) continue;

				// point lit

				if ( normalComputed == false ) {

					// the same normal can be reused for all lights
					// (should be possible to cache even more)

					computePixelNormal( normalVector, localPoint, ((HasShading)material).getShading(), face, vertices );
					normalVector.apply( _object.normalMatrix ).normalize();

					normalComputed = true;

				}

				// compute attenuation

				double attenuation = 1.0;

				if (light instanceof HasRaytracingPhysicalAttenuation 
						&& ((HasRaytracingPhysicalAttenuation)light).isPhysicalAttenuation() == true ) {

					attenuation = lightVector.length();
					attenuation = 1.0 / ( attenuation * attenuation );

				}

				lightVector.normalize();

				// compute diffuse

				double dot = Math.max( normalVector.dot( lightVector ), 0 );
				double diffuseIntensity = dot * ((HasIntensity)light).getIntensity();

				lightContribution.copy( diffuseColor );
				lightContribution.multiply( lightColor );
				lightContribution.multiply( diffuseIntensity * attenuation );

				outputColor.add( lightContribution );

				// compute specular

				if ( material instanceof MeshPhongMaterial ) 
				{

					halfVector.add( lightVector, eyeVector ).normalize();

					double dotNormalHalf = Math.max( normalVector.dot( halfVector ), 0.0 );
					double specularIntensity = Math.max( Math.pow( dotNormalHalf, ((MeshPhongMaterial) material).getShininess() ), 0.0 ) * diffuseIntensity;

					double specularNormalization = ( ((MeshPhongMaterial) material).getShininess() + 2.0 ) / 8.0;

					specularColor.copyGammaToLinear( ((MeshPhongMaterial) material).getSpecular() );

					double alpha = Math.pow( Math.max( 1.0 - lightVector.dot( halfVector ), 0.0 ), 5.0 );

					schlick.setR( specularColor.getR() + ( 1.0 - specularColor.getR() ) * alpha );
					schlick.setG( specularColor.getG() + ( 1.0 - specularColor.getG() ) * alpha );
					schlick.setB( specularColor.getB() + ( 1.0 - specularColor.getB() ) * alpha );

					lightContribution.copy( schlick );

					lightContribution.multiply( lightColor );
					lightContribution.multiply( specularNormalization * specularIntensity * attenuation );

					outputColor.add( lightContribution );
				}

			}

		}

		// reflection / refraction

		double reflectivity = ((MeshPhongMaterial)material).getReflectivity();

		if ( ( ((HasRaytracingMirror)material).isMirror() || ((HasRaytracingGlass)material).isGlass() ) 
				&& reflectivity > 0 
				&& recursionDepth < maxRecursionDepth ) 
		{

			if ( ((HasRaytracingMirror)material).isMirror() ) 
			{

				reflectionVector.copy( rayDirection );
				reflectionVector.reflect( normalVector );

			} 
			else if ( ((HasRaytracingGlass)material).isGlass() ) 
			{

				double eta = ((MeshPhongMaterial)material).getRefractionRatio();

				double dotNI = rayDirection.dot( normalVector );
				double k = 1.0 - eta * eta * ( 1.0 - dotNI * dotNI );

				if ( k < 0.0 ) {

					reflectionVector.set( 0, 0, 0 );

				} else {

					reflectionVector.copy( rayDirection );
					reflectionVector.multiply( eta );

					double alpha = eta * dotNI + Math.sqrt( k );
					tmpVec.copy( normalVector );
					tmpVec.multiply( alpha );
					reflectionVector.sub( tmpVec );

				}

			}

			double theta = Math.max( eyeVector.dot( normalVector ), 0.0 );
			double rf0 = reflectivity;
			double fresnel = rf0 + ( 1.0 - rf0 ) * Math.pow( ( 1.0 - theta ), 5.0 );

			double weight = fresnel;

			Color zColor = tmpColor[ recursionDepth ];

			spawnRay( point, reflectionVector, zColor, recursionDepth + 1 );

			if ( ((MeshPhongMaterial)material).getSpecular() != null ) {

				zColor.multiply( ((MeshPhongMaterial)material).getSpecular()  );

			}

			zColor.multiply( weight );
			outputColor.multiply( 1.0 - weight );
			outputColor.add( zColor );
		}
		
	}
	
	Vector3 tmpVec1 = new Vector3();
	Vector3 tmpVec2 = new Vector3();
	Vector3 tmpVec3 = new Vector3();

	private void computePixelNormal(Vector3 outputVector, Vector3 point, Material.SHADING shading, Face3 face, List<Vector3> vertices ) 
	{

		Vector3 faceNormal = face.getNormal();
		List<Vector3> vertexNormals = face.getVertexNormals();

		if ( shading == Material.SHADING.FLAT ) 
		{
			outputVector.copy( faceNormal );

		} 
		else if ( shading == Material.SHADING.SMOOTH ) 
		{
			// compute barycentric coordinates

			Vector3 vA = vertices.get( face.getA() );
			Vector3 vB = vertices.get( face.getB() );
			Vector3 vC = vertices.get( face.getC() );

			tmpVec3.cross( tmpVec1.sub( vB, vA ), tmpVec2.sub( vC, vA ) );
			double areaABC = faceNormal.dot( tmpVec3 );

			tmpVec3.cross( tmpVec1.sub( vB, point ), tmpVec2.sub( vC, point ) );
			double areaPBC = faceNormal.dot( tmpVec3 );
			double a = areaPBC / areaABC;

			tmpVec3.cross( tmpVec1.sub( vC, point ), tmpVec2.sub( vA, point ) );
			double areaPCA = faceNormal.dot( tmpVec3 );
			double b = areaPCA / areaABC;

			double c = 1.0 - a - b;

			// compute interpolated vertex normal

			tmpVec1.copy( vertexNormals.get( 0 ) );
			tmpVec1.multiply( a );

			tmpVec2.copy( vertexNormals.get( 1 ) );
			tmpVec2.multiply( b );

			tmpVec3.copy( vertexNormals.get( 2 ) );
			tmpVec3.multiply( c );

			outputVector.add( tmpVec1, tmpVec2 );
			outputVector.add( tmpVec3 );

		}

	}

}
