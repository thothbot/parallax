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

package org.parallax3d.parallax.tests.cases.misc;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.extras.geometries.CylinderGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshNormalMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.Euler;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("misc_lookat")
public final class MiscLookAt extends ParallaxTest 
{
	Scene scene;
	PerspectiveCamera camera;
	Mesh sphere;
	
	int mouseX = 0, mouseY = 0;

	@Override
	public void onStart(RenderingContext context)
	{
		camera = new PerspectiveCamera(
				50, // fov
				context.getAspectRation(), // aspect
				1, // near
				15000 // far 
		); 
		
		camera.getPosition().setZ(3200);
		
		MeshNormalMaterial material = new MeshNormalMaterial().setShading(Material.SHADING.SMOOTH);
		sphere = new Mesh( new SphereGeometry( 100, 20, 20 ), material );
		scene.add( sphere );

		CylinderGeometry geometry = new CylinderGeometry( 0, 10, 100, 3, 1 );
		geometry.applyMatrix( new Matrix4().makeRotationFromEuler( new Euler( Math.PI / 2.0, Math.PI, 0.0 ) ) );

		MeshNormalMaterial material2 = new MeshNormalMaterial();

		for ( int i = 0; i < 1000; i ++ ) 
		{
			Mesh mesh2 = new Mesh( geometry, material2 );
			mesh2.getPosition().setX( Math.random() * 4000 - 2000 );
			mesh2.getPosition().setY( Math.random() * 4000 - 2000 );
			mesh2.getPosition().setZ( Math.random() * 4000 - 2000 );
			mesh2.getScale().set( Math.random() * 4.0 + 2.0 );
			scene.add( mesh2 );
		}

		scene.setMatrixAutoUpdate(false);
		context.getRenderer().setSortObjects(false);
		context.getRenderer().setClearColor(0xeeeeee);
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double time = context.getDeltaTime() * 0.0005;
		sphere.getPosition().setX( Math.sin( time * 0.7 ) * 2000 );
		sphere.getPosition().setY( Math.cos( time * 0.5 ) * 2000 );
		sphere.getPosition().setZ( Math.cos( time * 0.3 ) * 2000 );

		for ( int i = 1, l = scene.getChildren().size(); i < l; i ++ ) 
		{
			scene.getChildren().get(i).lookAt( sphere.getPosition() );
		}

		camera.getPosition().addX( ( mouseX - camera.getPosition().getX() ) * .05 );
		camera.getPosition().addY( ( - mouseY - camera.getPosition().getY() ) * .05 );
		camera.lookAt( scene.getPosition() );
		
		context.getRenderer().render(scene, camera);
	}

	@Override
	public String getName() {
		return "Object3D.lookAt()";
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
//		    	  	rs.mouseX = (event.getX() - renderingPanel.context.getRenderer().getAbsoluteWidth() / 2 ) * 10; 
//		    	  	rs.mouseY = (event.getY() - renderingPanel.context.getRenderer().getAbsoluteHeight() / 2) * 10;
//		      }
//		});
//	}
	
}
