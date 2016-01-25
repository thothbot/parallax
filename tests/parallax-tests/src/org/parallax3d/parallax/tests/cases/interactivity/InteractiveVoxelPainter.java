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
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.List;

@ThreejsExample("webgl_interactive_voxelpainter")
public final class InteractiveVoxelPainter extends ParallaxTest
{

	private static final String texture = "./assets/textures/square-outline-textured.png";

	Scene scene;
	List<GeometryObject> objects = new ArrayList<GeometryObject>();
	
	PerspectiveCamera camera;
	
	Raycaster raycaster;
	
	Mesh rollOverMesh;
	Mesh plane;
	
	BoxGeometry cubeGeo;
	MeshLambertMaterial cubeMaterial;
	
	Vector3 mouse2D;
	Vector3 voxelPosition;
	Vector3 vector;
	
	boolean isShiftDown, isCtrlDown;
	
	double theta = 45;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				45, // fov
				context.getRenderer().getAbsoluteAspectRation(), // aspect 
				1, // near
				10000 // far 
		);
		
		camera.getPosition().set(500, 800, 1300);
		camera.lookAt(new Vector3());
	
		// roll-over helpers

		BoxGeometry rollOverGeo = new BoxGeometry( 50, 50, 50 );
		MeshBasicMaterial rollOverMaterial = new MeshBasicMaterial();
		rollOverMaterial.setColor(new Color(0xff0000));
		rollOverMaterial.setOpacity(0.5);
		rollOverMaterial.setTransparent(true);
		rollOverMesh = new Mesh( rollOverGeo, rollOverMaterial );
		scene.add( rollOverMesh );

		// cubes

		cubeGeo = new BoxGeometry( 50, 50, 50 );
		cubeMaterial = new MeshLambertMaterial();
		cubeMaterial.setColor(new Color(0xfeb74c));
		cubeMaterial.setAmbient(new Color(0x00ff80));
		cubeMaterial.setShading(Material.SHADING.FLAT);
		cubeMaterial.setMap(new Texture( texture ));
		cubeMaterial.setAmbient(cubeMaterial.getColor());

		// grid

		int size = 500, step = 50;

		Geometry geometry = new Geometry();

		for ( int i = - size; i <= size; i += step ) {

			geometry.getVertices().add( new Vector3( - size, 0, i ) );
			geometry.getVertices().add( new Vector3(   size, 0, i ) );

			geometry.getVertices().add( new Vector3( i, 0, - size ) );
			geometry.getVertices().add( new Vector3( i, 0,   size ) );

		}

		LineBasicMaterial material = new LineBasicMaterial();
		material.setColor(new Color(0x000000));
		material.setOpacity(0.2);
		material.setTransparent(true);
		
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
	
//	@Override
//	public void onAnimationReady(AnimationReadyEvent event)
//	{
//		super.onAnimationReady(event);
//  	
//	  	RootPanel.get().addDomHandler(this, KeyDownEvent.getType());
//		RootPanel.get().addDomHandler(this, KeyUpEvent.getType());
//		getWidget().addDomHandler(this, MouseMoveEvent.getType());
//		getWidget().addDomHandler(this, MouseDownEvent.getType());
//	}
//	
//	@Override
//	public void onMouseDown(MouseDownEvent event) 
//	{
//		event.preventDefault();
//
//		DemoScene rs = (DemoScene) renderingPanel.getAnimatedScene();
//		
//		rs.vector.set( ( event.getX() / (double) renderingPanel.context.getRenderer().getAbsoluteWidth() ) * 2.0 - 1.0, 
//				- ( event.getY() / (double) renderingPanel.context.getRenderer().getAbsoluteHeight() ) * 2.0 + 1.0, 0.5 );
//		
//		rs.vector.unproject( rs.camera );
//
//		rs.raycaster.getRay().set( rs.camera.getPosition(), rs.vector.sub( rs.camera.getPosition() ).normalize() );
//
//		List<Intersect> intersects = rs.raycaster.intersectObjects( rs.objects, false );
//
//		if ( intersects.size() > 0 ) {
//
//			Intersect intersect = intersects.get(0);
//
//			// delete cube
//
//			if ( rs.isShiftDown ) {
//
//				if ( intersect.object != rs.plane ) {
//
//					rs.scene.remove( intersect.object );
//
//					rs.objects.remove( rs.objects.indexOf( intersect.object ) );
//
//				}
//
//			// create cube
//
//			} else {
//
//				Mesh voxel = new Mesh( rs.cubeGeo, rs.cubeMaterial );
//				voxel.getPosition().copy( intersect.point ).add( intersect.face.getNormal() );
//				voxel.getPosition().divide( 50.0 ).floor().multiply( 50.0 ).add( 25.0 );
//				rs.scene.add( voxel );
//
//				rs.objects.add( voxel );
//
//			}
//
//		}
//		
//		rs.context.getRenderer().render(rs.scene, rs.camera);
//	}
//
//	@Override
//	public void onMouseMove(MouseMoveEvent event) 
//	{
//		event.preventDefault();
//		
//		DemoScene rs = (DemoScene) renderingPanel.getAnimatedScene();
//
//		rs.vector.set( ( event.getX() /(double) renderingPanel.context.getRenderer().getAbsoluteWidth() ) * 2.0 - 1.0, 
//				- ( event.getY() /(double) renderingPanel.context.getRenderer().getAbsoluteHeight() ) * 2.0 + 1.0, 0.5 );
//		rs.vector.unproject( rs.camera );
//
//		rs.raycaster.getRay().set( rs.camera.getPosition(), rs.vector.sub( rs.camera.getPosition() ).normalize() );
//
//		List<Intersect> intersects = rs.raycaster.intersectObjects( rs.objects, false );
//
//		if ( intersects.size() > 0 ) {
//
//			Intersect intersect = intersects.get( 0 );
//
//			rs.rollOverMesh.getPosition().copy( intersect.point ).add( intersect.face.getNormal() );
//			rs.rollOverMesh.getPosition().divide( 50.0 ).floor().multiply( 50.0 ).add( 25.0 );
//
//		}
//		
//		rs.context.getRenderer().render(rs.scene, rs.camera);
//	}
//
//	@Override
//	public void onKeyUp(KeyUpEvent event) 
//	{
//		DemoScene rs = (DemoScene) renderingPanel.getAnimatedScene();
//		
//		if ( event.getNativeKeyCode() == KeyCodes.KEY_SHIFT ) 
//		{
//			rs.isShiftDown = false;
//		} 
//		else if ( event.getNativeKeyCode() == KeyCodes.KEY_CTRL ) 
//		{
//			rs.isCtrlDown = false;
//		} 
//	}
//
//	@Override
//	public void onKeyDown(KeyDownEvent event) 
//	{
//		DemoScene rs = (DemoScene) renderingPanel.getAnimatedScene();
//		
//		if ( event.getNativeKeyCode() == KeyCodes.KEY_SHIFT ) 
//		{
//			rs.isShiftDown = true;
//		} 
//		else if ( event.getNativeKeyCode() == KeyCodes.KEY_CTRL ) 
//		{
//			rs.isCtrlDown = true;
//		} 
//	}
}
