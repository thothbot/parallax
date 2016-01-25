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
import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.PlaneBufferGeometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.SpotLight;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.tests.ParallaxTest;

import java.util.ArrayList;
import java.util.List;

public final class InteractiveDraggableCubes extends ParallaxTest
{
	class Intersect
	{
		public GeometryObject object;
		public int currentHex;
	}

	Scene scene;
	PerspectiveCamera camera;
	
	Vector3 offset = new Vector3(10, 10, 10);
	double mouseDeltaX = 0, mouseDeltaY = 0;
	
	List<GeometryObject> objects;
	Mesh plane;
	
//	TrackballControls controls;
	
	GeometryObject intersected;
	GeometryObject selected;
	
	FastMap<Integer> currentHex = new FastMap<>();

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				70, // fov
				context.getRenderer().getAbsoluteAspectRation(), // aspect 
				1, // near
				10000 // far 
		);
		camera.getPosition().setZ(1000);
		
//		controls = new TrackballControls( camera, renderingPanel.getCanvas() );
//		controls.setRotateSpeed(1.0);
//		controls.setZoomSpeed(1.2);
//		controls.setPanSpeed(0.8);
//		controls.setZoom(true);
//		controls.setPan(true);
//		controls.setStaticMoving(true);
//		controls.setDynamicDampingFactor(0.3);
//		controls.setEnabled(false);

		scene.add( new AmbientLight( 0x505050 ) );

		SpotLight light = new SpotLight( 0xffffff, 1.5 );
		light.getPosition().set( 0, 500, 2000 );
		light.setCastShadow(true);

		light.setShadowCameraNear(200);
		light.setShadowCameraFar(((PerspectiveCamera)camera).getFar());
		light.setShadowCameraFar(50);

		light.setShadowBias(-0.00022);
		light.setShadowDarkness(0.5);

		light.setShadowMapWidth(2048);
		light.setShadowMapHeight(2048);

		scene.add( light );

		BoxGeometry geometry = new BoxGeometry( 40, 40, 40 );

		objects = new ArrayList<GeometryObject>();
		for ( int i = 0; i < 200; i ++ ) 
		{
			MeshLambertMaterial material1 = new MeshLambertMaterial();
			material1.setColor(new Color( (int)(Math.random() * 0xffffff) ));
			material1.setAmbient(material1.getColor());
			Mesh object = new Mesh( geometry, material1 );

			object.getPosition().setX( Math.random() * 1000 - 500 );
			object.getPosition().setY( Math.random() * 600 - 300 );
			object.getPosition().setZ( Math.random() * 800 - 400 );

			object.getRotation().setX( Math.random() * 2 * Math.PI );
			object.getRotation().setY( Math.random() * 2 * Math.PI ); 
			object.getRotation().setZ( Math.random() * 2 * Math.PI );

			object.getScale().setX( Math.random() * 2 + 1 );
			object.getScale().setY( Math.random() * 2 + 1 );
			object.getScale().setZ( Math.random() * 2 + 1 );

			object.setCastShadow(true);
			object.setReceiveShadow(true);

			scene.add( object );

			objects.add( object );
		}

		MeshBasicMaterial material2 = new MeshBasicMaterial();
		material2.setColor(new Color(0x000000));
		material2.setOpacity(0.25);
		material2.setTransparent(true);
		plane = new Mesh( new PlaneBufferGeometry( 2000, 2000, 8, 8 ), material2 );
		plane.setVisible(false);
		scene.add( plane );

		context.getRenderer().setClearColor(0xeeeeee);
		context.getRenderer().setSortObjects(false);
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
//		controls.update();
		context.getRenderer().render(scene, camera);
	}
	
	@Override
	public String getName() {
		return "Draggable cubes";
	}

	@Override
	public String getDescription() {
		return "";
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
//		renderingPanel.getCanvas().addDomHandler(this, MouseMoveEvent.getType());
//		renderingPanel.getCanvas().addDomHandler(this, MouseDownEvent.getType());
//		renderingPanel.getCanvas().addDomHandler(this, MouseUpEvent.getType());
//	}
//	
//	@Override
//	public void onMouseUp(MouseUpEvent event) 
//	{
//		event.preventDefault();
//
//		DemoScene rs = (DemoScene) renderingPanel.getAnimatedScene();
//		
//		rs.controls.setEnabled(true);
//
//		if ( rs.intersected != null ) 
//		{
//			rs.plane.getPosition().copy( rs.intersected.getPosition() );
//
//			rs.selected = null;
//		}
//
//		getWidget().getElement().getStyle().setCursor(Cursor.AUTO);	
//	}
//
//	@Override
//	public void onMouseDown(MouseDownEvent event) 
//	{
//		event.preventDefault();
//
//		DemoScene rs = (DemoScene) renderingPanel.getAnimatedScene();
//		
//		Vector3 vector = new Vector3( rs.mouseDeltaX, rs.mouseDeltaY, 0.5 ).unproject(rs.camera);
//
//		Raycaster raycaster = new Raycaster( rs.camera.getPosition(), vector.sub( rs.camera.getPosition() ).normalize() );
//		
//		List<Raycaster.Intersect> intersects = raycaster.intersectObjects( rs.objects, false );
//
//		if ( intersects.size() > 0 ) 
//		{
//			rs.controls.setEnabled(false);
//
//			rs.selected = intersects.get( 0 ).object; 
//
//			List<Raycaster.Intersect>  intersects2 = raycaster.intersectObject( rs.plane, false );
//			rs.offset.copy( intersects2.get( 0 ).point ).sub( rs.plane.getPosition() );
//
//			getWidget().getElement().getStyle().setCursor(Cursor.MOVE);	
//		}
//	}
//
//	@Override
//	public void onMouseMove(MouseMoveEvent event) 
//	{
//		event.preventDefault();
//
//		DemoScene rs = (DemoScene) renderingPanel.getAnimatedScene();
//
//		rs.mouseDeltaX = (event.getX() / (double)renderingPanel.context.getRenderer().getAbsoluteWidth() ) * 2.0 - 1.0; 
//		rs.mouseDeltaY = - (event.getY() / (double)renderingPanel.context.getRenderer().getAbsoluteHeight() ) * 2.0 + 1.0;
//
//		//
//
//		Vector3 vector = new Vector3( rs.mouseDeltaX, rs.mouseDeltaY, 0.5 ).unproject( rs.camera );
//
//		Raycaster raycaster = new Raycaster( rs.camera.getPosition(), vector.sub( rs.camera.getPosition() ).normalize() );
//
//		if ( rs.selected != null ) 
//		{
//			List<Raycaster.Intersect> intersects = raycaster.intersectObject( rs.plane, false );
//			rs.selected.getPosition().copy( intersects.get( 0 ).point.sub( rs.offset ) );
//			return;
//		}
//
//		List<Raycaster.Intersect> intersects = raycaster.intersectObjects( rs.objects, false );
//
//		if ( intersects.size() > 0 ) 
//		{
//			if ( rs.intersected != intersects.get(0).object )
//			{
//				if ( rs.intersected != null )
//				{
//					((MeshLambertMaterial)rs.intersected.getMaterial()).getColor().setHex( rs.currentHex.get(rs.intersected.getId() + "") );
//				}
//
//				rs.intersected = intersects.get(0).object;
//				rs.currentHex.put(rs.intersected.getId() + "", ((MeshLambertMaterial)rs.intersected.getMaterial()).getColor().getHex());
//
//				rs.plane.getPosition().copy( rs.intersected.getPosition() );
//				rs.plane.lookAt( rs.camera.getPosition() );
//			}
//
//			getWidget().getElement().getStyle().setCursor(Cursor.POINTER);
//
//		} else {
//
//			if ( rs.intersected != null ) 
//				((MeshLambertMaterial)rs.intersected.getMaterial()).getColor().setHex(  rs.currentHex.get(rs.intersected.getId() + "") );
//
//			rs.intersected = null;
//
//			getWidget().getElement().getStyle().setCursor(Cursor.AUTO);
//
//		}
//	}
}
