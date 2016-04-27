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

package org.parallax3d.parallax.graphics.renderers;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.user.client.ui.RootPanel;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.graphics.lights.HasRaytracingPhysicalAttenuation;
import org.parallax3d.parallax.graphics.lights.HasIntensity;
import org.parallax3d.parallax.graphics.lights.Light;
import org.parallax3d.parallax.graphics.materials.*;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.*;
import org.parallax3d.parallax.system.FastMap;

import java.util.ArrayList;
import java.util.List;

public class RaytracingRenderer extends Renderer
{

	private static final int MAX_RECURSION_DEPTH = 3;
	private static final int BLOCK_SIZE = 64;

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

	Vector3 tmpVec1 = new Vector3();
	Vector3 tmpVec2 = new Vector3();
	Vector3 tmpVec3 = new Vector3();

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

	AnimationScheduler.AnimationHandle animationHandler;

	FastMap<ObjectMatrixes> cache = new FastMap<ObjectMatrixes>();

	Canvas canvasBlock;
	ImageData imagedata = null;

	public RaytracingRenderer(int width, int height) {
		canvas = Canvas.createIfSupported();
		canvas.ensureDebugId("canvas2d");
		
		setSize(width, height);
		
	    context = canvas.getContext2d();
	    context.setFillStyle( "#FFFFFF" );
	    
	    
	    canvasBlock = Canvas.createIfSupported();
		canvasBlock.setCoordinateSpaceWidth(BLOCK_SIZE);
		canvasBlock.setCoordinateSpaceHeight(BLOCK_SIZE);

		RootPanel.get().add(canvasBlock, -10000, 0);

		Context2d contextBlock = canvasBlock.getContext2d();
		imagedata = contextBlock.getImageData( 0, 0, BLOCK_SIZE, BLOCK_SIZE );
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
	public void render(Scene scene, final Camera camera ) {

		if (isAutoClear()) {
			this.clear();
		}

		// update scene graph

		if (scene.isAutoUpdate()) {
			scene.updateMatrixWorld(false);
		}

		// update camera matrices

		if ( camera.getParent() == null ) {
			camera.updateMatrixWorld(false);
		}

		camera.getMatrixWorldInverse().getInverse( camera.getMatrixWorld() );
		cameraPosition.setFromMatrixPosition( camera.getMatrixWorld() );

		//

		cameraNormalMatrix.getNormalMatrix( camera.getMatrixWorld() );
		origin.copy( cameraPosition );

		perspective = 0.5 / Math.tan( Mathematics.degToRad( ((PerspectiveCamera)camera).getFov() * 0.5 ) ) * getAbsoluteHeight();

		objects = scene.getChildren();

		// collect lights and set up object matrices

		lights = new ArrayList<Light>();

		scene.traverse(new Object3D.Traverse() {
			
			@Override
			public void callback(Object3D object) {
				if ( object instanceof Light ) {

					lights.add( (Light) object );

				}

				if ( !cache.containsKey(Integer.toString(object.getId()))) 
				{
					cache.put( Integer.toString(object.getId()), new ObjectMatrixes());
				}

				modelViewMatrix.multiply( camera.getMatrixWorldInverse(), object.getMatrixWorld() );

				ObjectMatrixes _object = cache.get(Integer.toString(object.getId()));
				
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

		for ( int y = 0; y < BLOCK_SIZE; y ++ ) {

			for ( int x = 0; x < BLOCK_SIZE; x ++, index += 4 ) {

				// spawn primary ray at pixel position

				origin.copy( cameraPosition );

				direction.set( x + blockX - (double)getAbsoluteWidth()/2, - ( y + blockY - getAbsoluteHeight()/2 ), - perspective );
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

		blockX += BLOCK_SIZE;

		if ( blockX >= getAbsoluteWidth() ) {

			blockX = 0;
			blockY += BLOCK_SIZE;

			if ( blockY >= getAbsoluteHeight() ) {
				return;
			}

		}

		context.fillRect( blockX, blockY, BLOCK_SIZE, BLOCK_SIZE );

		final int _blockX = blockX;
		final int _blockY = blockY;
		
		animationHandler = AnimationScheduler.get().requestAnimationFrame(new AnimationScheduler.AnimationCallback() {
						
			@Override
			public void execute(double timestamp) {
				
				renderBlock( _blockX, _blockY );
								
			}
		});

	}

	private void spawnRay(Vector3 rayOrigin, Vector3 rayDirection, Color outputColor, int recursionDepth) 
	{
		// Init the tmp array
		if(tmpColor == null)
		{
			tmpColor = new Color[MAX_RECURSION_DEPTH];
			for ( int i = 0; i < MAX_RECURSION_DEPTH; i ++ )
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

		List<Raycaster.Intersect> intersections = raycaster.intersectObjects( objects, true );

		// ray didn't find anything
		// (here should come setting of background color?)

		if ( intersections.isEmpty()) {
			return;
		}

		// ray hit

		Raycaster.Intersect intersection = intersections.get( 0 );

		Vector3 point = intersection.point;
		GeometryObject object = intersection.object;
		Material material = object.getMaterial();
		Face3 face = intersection.face;

		List<Vector3> vertices = ((Geometry)object.getGeometry()).getVertices();

		//

		ObjectMatrixes _object = cache.get(Integer.toString(object.getId()));

		localPoint.copy( point ).apply( _object.inverseMatrix );
		eyeVector.sub( raycaster.getRay().getOrigin(), point ).normalize();

		// resolve pixel diffuse color

		if ( material instanceof MeshLambertMaterial ||
			 material instanceof MeshPhongMaterial ||
			 material instanceof MeshBasicMaterial)
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
			for (Light light : lights) {

				lightVector.setFromMatrixPosition(light.getMatrixWorld());
				lightVector.sub(point);

				rayLight.getDirection().copy(lightVector).normalize();

				List<Raycaster.Intersect> intersections2 = raycasterLight.intersectObjects(objects, true);

				// point in shadow

				if (!intersections2.isEmpty()) {
					continue;
				}

				// point visible

				outputColor.add(diffuseColor);

			}

		} else if ( material instanceof MeshLambertMaterial ||
					material instanceof MeshPhongMaterial ) {

			boolean normalComputed = false;

			for (Light light : lights) {

				lightColor.copyGammaToLinear(light.getColor());

				lightVector.setFromMatrixPosition(light.getMatrixWorld());
				lightVector.sub(point);

				rayLight.getDirection().copy(lightVector).normalize();

				List<Raycaster.Intersect> intersections3 = raycasterLight.intersectObjects(objects, true);

				// point in shadow

				if (!intersections3.isEmpty()) {
					continue;
				}

				// point lit

				if (!normalComputed) {

					// the same normal can be reused for all lights
					// (should be possible to cache even more)

					computePixelNormal(normalVector, localPoint, ((HasShading) material).getShading(), face, vertices);
					normalVector.apply(_object.normalMatrix).normalize();

					normalComputed = true;

				}

				// compute attenuation

				double attenuation = 1.0;

				if (light instanceof HasRaytracingPhysicalAttenuation
						&& ((HasRaytracingPhysicalAttenuation) light).isPhysicalAttenuation()) {

					attenuation = lightVector.length();
					attenuation = 1.0 / (attenuation * attenuation);

				}

				lightVector.normalize();

				// compute diffuse

				double dot = Math.max(normalVector.dot(lightVector), 0);
				double diffuseIntensity = dot * ((HasIntensity) light).getIntensity();

				lightContribution.copy(diffuseColor);
				lightContribution.multiply(lightColor);
				lightContribution.multiply(diffuseIntensity * attenuation);

				outputColor.add(lightContribution);

				// compute specular

				if (material instanceof MeshPhongMaterial) {

					halfVector.add(lightVector, eyeVector).normalize();

					double dotNormalHalf = Math.max(normalVector.dot(halfVector), 0.0);
					double specularIntensity = Math.max(Math.pow(dotNormalHalf, ((MeshPhongMaterial) material).getShininess()), 0.0) * diffuseIntensity;

					double specularNormalization = (((MeshPhongMaterial) material).getShininess() + 2.0) / 8.0;

					specularColor.copyGammaToLinear(((MeshPhongMaterial) material).getSpecular());

					double alpha = Math.pow(Math.max(1.0 - lightVector.dot(halfVector), 0.0), 5.0);

					schlick.setR(specularColor.getR() + (1.0 - specularColor.getR()) * alpha);
					schlick.setG(specularColor.getG() + (1.0 - specularColor.getG()) * alpha);
					schlick.setB(specularColor.getB() + (1.0 - specularColor.getB()) * alpha);

					lightContribution.copy(schlick);

					lightContribution.multiply(lightColor);
					lightContribution.multiply(specularNormalization * specularIntensity * attenuation);

					outputColor.add(lightContribution);
				}

			}

		}

		// reflection / refraction

		double reflectivity = ((MeshPhongMaterial)material).getReflectivity();

		if ( ( ((HasRaytracingMirror)material).isMirror() || ((HasRaytracingGlass)material).isGlass() ) 
				&& reflectivity > 0 
				&& recursionDepth < MAX_RECURSION_DEPTH ) 
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

			double weight = reflectivity + ( 1.0 - reflectivity) * Math.pow( 1.0 - theta, 5.0 );

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
