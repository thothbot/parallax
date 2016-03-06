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

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.controllers.TrackballControls;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.graphics.core.Raycaster;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.PlaneBufferGeometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.SpotLight;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.renderers.ShadowMap;
import org.parallax3d.parallax.input.TouchDownHandler;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.input.TouchUpHandler;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.List;

@ThreejsExample("webgl_interactive_draggablecubes")
public final class InteractiveDraggableCubes extends ParallaxTest implements TouchDownHandler, TouchUpHandler, TouchMoveHandler
{
	Scene scene;
	PerspectiveCamera camera;
	
	Vector3 offset = new Vector3(10, 10, 10);
	double mouseDeltaX = 0, mouseDeltaY = 0;
	
	List<GeometryObject> objects;
	Mesh plane;
	
	TrackballControls controls;
	
	GeometryObject intersected;
	GeometryObject selected;
	
	FastMap<Integer> currentHex = new FastMap<>();

	int width = 0, height = 0;

	@Override
	public void onResize(RenderingContext context) {
		width = context.getWidth();
		height = context.getHeight();
	}

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				70, // fov
				context.getAspectRation(), // aspect
				1, // near
				10000 // far 
		);
		camera.getPosition().setZ(1000);
		
		controls = new TrackballControls( camera, context );
		controls.setRotateSpeed(1.0);
		controls.setZoomSpeed(1.2);
		controls.setPanSpeed(0.8);
		controls.setZoom(true);
		controls.setPan(true);
		controls.setStaticMoving(true);
		controls.setDynamicDampingFactor(0.3);
		controls.setEnabled(false);

		scene.add( new AmbientLight( 0x505050 ) );

		SpotLight light = new SpotLight( 0xffffff, 1.5 );
		light.getPosition().set( 0, 500, 2000 );
		light.setCastShadow(true);

		light.setShadowCameraNear(200);
		light.setShadowCameraFar(camera.getFar());
		light.setShadowCameraFar(50);

		light.setShadowBias(-0.00022);
		light.setShadowDarkness(0.5);

		light.setShadowMapWidth(2048);
		light.setShadowMapHeight(2048);

		scene.add( light );

		BoxGeometry geometry = new BoxGeometry( 40, 40, 40 );

		objects = new ArrayList<>();
		for ( int i = 0; i < 200; i ++ ) 
		{
			int color = (int)(Math.random() * 0xffffff);
			Mesh object = new Mesh( geometry, new MeshLambertMaterial().setColor( color ).setAmbient( color ) );

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

		MeshBasicMaterial material2 = new MeshBasicMaterial()
				.setColor( 0x000000 )
				.setOpacity(0.25)
				.setTransparent(true);
		plane = new Mesh( new PlaneBufferGeometry( 2000, 2000, 8, 8 ), material2 );
		plane.setVisible(false);
		scene.add( plane );

		context.getRenderer().setClearColor(0xeeeeee);
		context.getRenderer().setSortObjects(false);

		// Init shadow
		new ShadowMap(context.getRenderer(), scene);
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		controls.update();
		context.getRenderer().render(scene, camera);
	}

	@Override
	public void onTouchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 vector = new Vector3( mouseDeltaX, mouseDeltaY, 0.5 ).unproject(camera);

		Raycaster raycaster = new Raycaster( camera.getPosition(), vector.sub( camera.getPosition() ).normalize() );

		List<Raycaster.Intersect> intersects = raycaster.intersectObjects( objects, false );

		if ( intersects.size() > 0 )
		{
			controls.setEnabled(false);

			selected = intersects.get( 0 ).object;

			List<Raycaster.Intersect>  intersects2 = raycaster.intersectObject( plane, false );
			offset.copy( intersects2.get( 0 ).point ).sub( plane.getPosition() );

//			getWidget().getElement().getStyle().setCursor(Cursor.MOVE);
		}
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		mouseDeltaX = (screenX / width ) * 2.0 - 1.0;
		mouseDeltaY = - (screenY / height ) * 2.0 + 1.0;

		//

		Vector3 vector = new Vector3( mouseDeltaX, mouseDeltaY, 0.5 ).unproject( camera );

		Raycaster raycaster = new Raycaster( camera.getPosition(), vector.sub( camera.getPosition() ).normalize() );

		if ( selected != null )
		{
			List<Raycaster.Intersect> intersects = raycaster.intersectObject( plane, false );
			selected.getPosition().copy( intersects.get( 0 ).point.sub( offset ) );
			return;
		}

		List<Raycaster.Intersect> intersects = raycaster.intersectObjects( objects, false );

		if ( intersects.size() > 0 )
		{
			if ( intersected != intersects.get(0).object )
			{
				if ( intersected != null )
				{
					((MeshLambertMaterial)intersected.getMaterial()).getColor().setHex( currentHex.get(intersected.getId() + "") );
				}

				intersected = intersects.get(0).object;
				currentHex.put(intersected.getId() + "", ((MeshLambertMaterial)intersected.getMaterial()).getColor().getHex());

				plane.getPosition().copy( intersected.getPosition() );
				plane.lookAt( camera.getPosition() );
			}

//			getWidget().getElement().getStyle().setCursor(Cursor.POINTER);

		} else {

			if ( intersected != null )
				((MeshLambertMaterial)intersected.getMaterial()).getColor().setHex(  currentHex.get(intersected.getId() + "") );

			intersected = null;
			Log.error('n');
//			getWidget().getElement().getStyle().setCursor(Cursor.AUTO);

		}
	}

	@Override
	public void onTouchUp(int screenX, int screenY, int pointer, int button) {
		controls.setEnabled(true);

		if ( intersected != null )
		{
			plane.getPosition().copy( intersected.getPosition() );

			selected = null;
		}

//		getWidget().getElement().getStyle().setCursor(Cursor.AUTO);

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

}
