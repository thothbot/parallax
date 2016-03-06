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
import org.parallax3d.parallax.graphics.core.Raycaster;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.List;

@ThreejsExample("webgl_interactive_cubes")
public final class InteractiveCubes extends ParallaxTest 
{
	class Intersect
	{
		public GeometryObject object;
		public int currentHex;
	}

	static final int radius = 100;

	Scene scene;
	PerspectiveCamera camera;
			
	double mouseDeltaX = 0, mouseDeltaY = 0;
	Intersect intersected;

	Raycaster raycaster;
	
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
		
		camera.getPosition().set( 0, 300, 500 );
		
		DirectionalLight light = new DirectionalLight( 0xffffff, 1.0 );
		light.getPosition().set( 1.0 ).normalize();
		scene.add( light );

		BoxGeometry geometry = new BoxGeometry( 20, 20, 20 );

		for ( int i = 0; i < 2000; i ++ ) 
		{
			MeshLambertMaterial material = new MeshLambertMaterial().setColor( (int)(Math.random() * 0xffffff) );
			Mesh object = new Mesh( geometry, material );

			object.getPosition().setX( Math.random() * 800 - 400 );
			object.getPosition().setY( Math.random() * 800 - 400 );
			object.getPosition().setZ( Math.random() * 800 - 400 );

			object.getRotation().setX( Math.random() * 2 * Math.PI );
			object.getRotation().setY( Math.random() * 2 * Math.PI );
			object.getRotation().setZ( Math.random() * 2 * Math.PI );

			object.getScale().setX( Math.random() + 0.5 );
			object.getScale().setY( Math.random() + 0.5 );
			object.getScale().setZ( Math.random() + 0.5 );

			scene.add( object );

		}

		raycaster = new Raycaster();
		context.getRenderer().setClearColor(0xf0f0f0);
		context.getRenderer().setSortObjects(false);
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double duration = context.getDeltaTime();
		camera.getPosition().setX( radius * Math.sin( Mathematics.degToRad(duration / 100)) );
		camera.getPosition().setY( radius * Math.sin( Mathematics.degToRad(duration / 100)) );
		camera.getPosition().setZ( radius * Math.cos( Mathematics.degToRad(duration / 100)) );

		camera.lookAt( scene.getPosition() );

		// find intersections

		Vector3 vector = new Vector3( mouseDeltaX, mouseDeltaY, 1 );
		raycaster.set( camera.getPosition(), vector.sub( camera.getPosition() ).normalize() );
		
		List<Raycaster.Intersect> intersects = raycaster.intersectObjects( scene.getChildren(), false );

		if ( intersects.size() > 0 ) 
		{
			if ( intersected == null || intersected.object != intersects.get( 0 ).object ) 
			{
				if(intersected != null)
				{
					((MeshLambertMaterial)intersected.object.getMaterial()).getColor().setHex( intersected.currentHex );
				}
				
				intersected = new Intersect();
				intersected.object = (GeometryObject) intersects.get( 0 ).object;
				intersected.currentHex = ((MeshLambertMaterial)intersected.object.getMaterial()).getColor().getHex();
				((MeshLambertMaterial)intersected.object.getMaterial()).getColor().setHex( 0xff0000 );
			}
		}
		else 
		{
			if ( intersected != null ) 
				((MeshLambertMaterial)intersected.object.getMaterial()).getColor().setHex( intersected.currentHex );

			intersected = null;
		}
		
		context.getRenderer().render(scene, camera);
	}
		
	@Override
	public String getName() {
		return "Interactive cubes";
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
//		this.renderingPanel.getCanvas().addMouseMoveHandler(new MouseMoveHandler() {
//		      @Override
//		      public void onMouseMove(MouseMoveEvent event)
//		      {
//		    	  	DemoScene rs = (DemoScene) renderingPanel.getAnimatedScene();
//		    	  	rs.mouseDeltaX = (event.getX() / (double)renderingPanel.context.getRenderer().getAbsoluteWidth() ) * 2.0 - 1.0; 
//		    	  	rs.mouseDeltaY = - (event.getY() / (double)renderingPanel.context.getRenderer().getAbsoluteHeight() ) * 2.0 + 1.0;
//		      }
//		});
//	}
}
