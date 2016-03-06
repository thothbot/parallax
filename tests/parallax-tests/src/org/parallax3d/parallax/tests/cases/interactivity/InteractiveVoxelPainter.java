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

package org.parallax3d.parallax.tests.cases.interactivity;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.graphics.core.Raycaster;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.PlaneBufferGeometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.input.*;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.List;

@ThreejsExample("webgl_interactive_voxelpainter")
public final class InteractiveVoxelPainter extends ParallaxTest implements TouchDownHandler, TouchMoveHandler, KeyUpHandler, KeyDownHandler
{

	private static final String texture = "textures/square-outline-textured.png";

	Scene scene;
	List<GeometryObject> objects = new ArrayList<GeometryObject>();

	PerspectiveCamera camera;
	
	Raycaster raycaster;
	
	Mesh rollOverMesh;
	Mesh plane;
	
	BoxGeometry cubeGeo;
	MeshLambertMaterial cubeMaterial;
	
	Vector3 vector;
	
	boolean isShiftDown, isCtrlDown;

	int width = 0, height = 0;
	GLRenderer renderer;

	@Override
	public void onResize(RenderingContext context) {
		renderer = context.getRenderer();
		width = context.getWidth();
		height = context.getHeight();
	}

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				45, // fov
				context.getAspectRation(), // aspect
				1, // near
				10000 // far 
		);
		
		camera.getPosition().set(500, 800, 1300);
		camera.lookAt(new Vector3());
	
		// roll-over helpers

		BoxGeometry rollOverGeo = new BoxGeometry( 50, 50, 50 );
		MeshBasicMaterial rollOverMaterial = new MeshBasicMaterial()
				.setColor( 0xff0000 )
				.setOpacity(0.5)
				.setTransparent(true);
		rollOverMesh = new Mesh( rollOverGeo, rollOverMaterial );
		scene.add( rollOverMesh );

		// cubes

		cubeGeo = new BoxGeometry( 50, 50, 50 );
		cubeMaterial = new MeshLambertMaterial()
				.setColor( 0xfeb74c )
				.setAmbient( 0x00ff80 )
				.setShading(Material.SHADING.FLAT)
				.setMap(new Texture( texture ))
				.setAmbient( 0xfeb74c );

		// grid

		int size = 500, step = 50;

		Geometry geometry = new Geometry();

		for ( int i = - size; i <= size; i += step ) {

			geometry.getVertices().add( new Vector3( - size, 0, i ) );
			geometry.getVertices().add( new Vector3(   size, 0, i ) );

			geometry.getVertices().add( new Vector3( i, 0, - size ) );
			geometry.getVertices().add( new Vector3( i, 0,   size ) );

		}

		LineBasicMaterial material = new LineBasicMaterial()
				.setColor( 0x000000 )
				.setTransparent(true)
				.setOpacity(0.2);
		
		Line line = new Line( geometry, material, Line.MODE.PIECES );
		scene.add( line );
		
		//

		vector = new Vector3();
		raycaster = new Raycaster();

		PlaneBufferGeometry geometry2 = new PlaneBufferGeometry( 1000, 1000 );
		geometry2.applyMatrix( new Matrix4().makeRotationX( - Math.PI / 2 ) );

		plane = new Mesh( geometry2 );
		plane.setVisible( false );
		scene.add( plane );

		objects.add( plane );

		// Lights

		scene.add( new AmbientLight( 0x606060 ) );

		DirectionalLight directionalLight = new DirectionalLight( 0xffffff );
		directionalLight.getPosition().set( 1, 0.75, 0.5 ).normalize();
		scene.add( directionalLight );

		context.getRenderer().setClearColor(0xf0f0f0);
		
		context.getRenderer().render(scene, camera);
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
	}
	
	@Override
	public String getName() {
		return "Voxel painter";
	}

	@Override
	public String getDescription() {
		return "<strong>click</strong>: add voxel, <strong>shift + click</strong>: remove voxel";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}

	@Override
	public void onKeyDown(int keycode) {
		
		if ( keycode == KeyCodes.KEY_SHIFT ) 
		{
			isShiftDown = true;
		} 
		else if ( keycode == KeyCodes.KEY_CTRL ) 
		{
			isCtrlDown = true;
		} 
	}

	@Override
	public void onKeyUp(int keycode) {
		if ( keycode == KeyCodes.KEY_SHIFT ) 
		{
			isShiftDown = false;
		} 
		else if ( keycode == KeyCodes.KEY_CTRL ) 
		{
			isCtrlDown = false;
		} 
	}

	@Override
	public void onTouchDown(int screenX, int screenY, int pointer, int button) {

		vector.set( ( screenX /(double) width ) * 2.0 - 1.0,
				- ( screenY /(double) height ) * 2.0 + 1.0, 0.5 );

		vector.unproject( camera );

		raycaster.getRay().set( camera.getPosition(), vector.sub( camera.getPosition() ).normalize() );

		List<Raycaster.Intersect> intersects = raycaster.intersectObjects( objects, false );

		if ( intersects.size() > 0 ) {

			Raycaster.Intersect intersect = intersects.get(0);

			// delete cube

			if ( isShiftDown ) {

				if ( intersect.object != plane ) {

					scene.remove( intersect.object );

					objects.remove( objects.indexOf( intersect.object ) );

				}

			// create cube

			} else {

				Mesh voxel = new Mesh( cubeGeo, cubeMaterial );
				voxel.getPosition().copy( intersect.point ).add( intersect.face.getNormal() );
				voxel.getPosition().divide( 50.0 ).floor().multiply( 50.0 ).add( 25.0 );
				scene.add( voxel );

				objects.add( voxel );

			}

		}

		renderer.render(scene, camera);
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		vector.set( ( screenX /(double) width ) * 2.0 - 1.0, 
				- ( screenY /(double) height ) * 2.0 + 1.0, 0.5 );
		vector.unproject( camera );

		raycaster.getRay().set( camera.getPosition(), vector.sub( camera.getPosition() ).normalize() );

		List<Raycaster.Intersect> intersects = raycaster.intersectObjects( objects, false );

		if ( intersects.size() > 0 ) {

			Raycaster.Intersect intersect = intersects.get( 0 );

			rollOverMesh.getPosition().copy( intersect.point ).add( intersect.face.getNormal() );
			rollOverMesh.getPosition().divide( 50.0 ).floor().multiply( 50.0 ).add( 25.0 );

		}
		
		renderer.render(scene, camera);
	}

}
