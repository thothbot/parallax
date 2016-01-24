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

package org.parallax3d.parallax.tests.animation.geometries;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.cameras.OrthographicCamera;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.extras.helpers.CameraHelper;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.PointCloudMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.objects.PointCloud;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;

public class Cameras extends ParallaxTest
{

	Scene scene;
	Camera activeCamera;
	CameraHelper activeHelper;

	PerspectiveCamera camera;
	PerspectiveCamera cameraPerspective;
	CameraHelper cameraPerspectiveHelper;

	OrthographicCamera cameraOrtho;
	CameraHelper cameraOrthoHelper;

	Object3D cameraRig;

	Mesh mesh;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				50,
				0.5 * context.getRenderer().getAbsoluteAspectRation(),
				1,
				10000 );

		camera.getPosition().setZ(2500);
//		camera.addViewportResizeHandler(new ViewportResizeHandler() {
//
//			@Override
//			public void onResize(ViewportResizeEvent event) {
//				camera.setAspect(0.5 * event.context.getRenderer().getAbsoluteAspectRation());
//
//			}
//		});

		cameraPerspective = new PerspectiveCamera(
				50,
				context.getRenderer().getAbsoluteAspectRation() * 0.5,
				150,
				1000 );
//		cameraPerspective.addViewportResizeHandler(new ViewportResizeHandler() {
//
//			@Override
//			public void onResize(ViewportResizeEvent event) {
//				cameraPerspective.setAspect(0.5 * event.context.getRenderer().getAbsoluteAspectRation());
//
//			}
//		});


		cameraOrtho = new OrthographicCamera( 0.5 * context.getRenderer().getAbsoluteWidth(), context.getRenderer().getAbsoluteHeight(), 150, 1000 );
//		cameraOrtho.addViewportResizeHandler(new ViewportResizeHandler() {
//
//			@Override
//			public void onResize(ViewportResizeEvent event) {
//				cameraOrtho.setSize(0.5 * event.context.getRenderer().getAbsoluteWidth(), event.context.getRenderer().getAbsoluteHeight() );
//
//			}
//		});

		this.cameraPerspectiveHelper = new CameraHelper( this.cameraPerspective );
		scene.add( this.cameraPerspectiveHelper );

		this.cameraOrthoHelper = new CameraHelper( this.cameraOrtho );
		scene.add( this.cameraOrthoHelper );

		//

		this.activeCamera = this.cameraPerspective;
		this.activeHelper = this.cameraPerspectiveHelper;

		// counteract different front orientation of cameras vs rig

		this.cameraOrtho.getRotation().setY(Math.PI);
		this.cameraPerspective.getRotation().setY(Math.PI);

		this.cameraRig = new Object3D();

		this.cameraRig.add( this.cameraPerspective );
		this.cameraRig.add( this.cameraOrtho );

		scene.add( this.cameraRig );

		//

		MeshBasicMaterial bopt0 = new MeshBasicMaterial();
		bopt0.setColor( new Color(0xffffff) );
		bopt0.setWireframe(true);

		this.mesh = new Mesh( new SphereGeometry( 100, 16, 8 ), bopt0);
		scene.add( mesh );

		MeshBasicMaterial  bopt1 = new MeshBasicMaterial();
		bopt1.setColor( new Color(0x00ff00) );
		bopt1.setWireframe(true);
		Mesh mesh2 = new Mesh( new SphereGeometry( 50, 16, 8 ), bopt1);
		mesh2.getPosition().setY(150);
		mesh.add( mesh2 );

		MeshBasicMaterial  bopt2 = new MeshBasicMaterial();
		bopt2.setColor( new Color(0x0000ff) );
		Mesh mesh3 = new Mesh( new SphereGeometry( 5, 16, 8 ), bopt2);
		mesh3.getPosition().setZ(150);
		cameraRig.add( mesh3 );

		//

		Geometry geometry = new Geometry();

		for ( int i = 0; i < 10000; i ++ )
		{
			Vector3 vertex = new Vector3();
			vertex.setX(Mathematics.randFloatSpread( 2000.0 ));
			vertex.setY(Mathematics.randFloatSpread( 2000.0 ));
			vertex.setZ(Mathematics.randFloatSpread( 2000.0 ));

			geometry.getVertices().add( vertex );
		}

		PointCloudMaterial popt = new PointCloudMaterial();
		popt.setColor( new Color(0x888888) );

		PointCloud particles = new PointCloud( geometry, popt );
		scene.add( particles );

		//

		context.getRenderer().setAutoClear(false);
	}

	@Override
	public void onUpdate(RenderingContext context)
	{
		double r = context.getDeltaTime() * 0.0005;

		mesh.getPosition().setX(700 * Math.cos( r ));
		mesh.getPosition().setZ(700 * Math.sin( r ));
		mesh.getPosition().setY(700 * Math.sin( r ));

		mesh.getChildren().get( 0 ).getPosition().setX(70.0 * Math.cos( 2.0 * r ));
		mesh.getChildren().get( 0 ).getPosition().setZ(70.0 * Math.sin( r ));

		if ( activeCamera.equals(cameraPerspective) )
		{
			cameraPerspective.setFov(35.0 + 30.0 * Math.sin( 0.5 * r ));
			cameraPerspective.setFar(mesh.getPosition().length());
			cameraPerspective.updateProjectionMatrix();

			cameraPerspectiveHelper.update();
			cameraPerspectiveHelper.setVisible(true);

			cameraOrthoHelper.setVisible(false);
		}
		else
		{
			cameraOrtho.setFar(mesh.getPosition().length());
			cameraOrtho.updateProjectionMatrix();

			cameraOrthoHelper.update();
			cameraOrthoHelper.setVisible(true);

			cameraPerspectiveHelper.setVisible(false);
		}

		cameraRig.lookAt( mesh.getPosition() );

		context.getRenderer().clear();

		activeHelper.setVisible(false);

		context.getRenderer().setViewport( 0, 0, context.getRenderer().getAbsoluteWidth() / 2, context.getRenderer().getAbsoluteHeight() );

		context.getRenderer().render( scene, activeCamera );

		activeHelper.setVisible(true);

		context.getRenderer().setViewport( context.getRenderer().getAbsoluteWidth() / 2, 0, context.getRenderer().getAbsoluteWidth() / 2, context.getRenderer().getAbsoluteHeight() );
		context.getRenderer().render(scene, camera);
	}

//	@Override
//	public void onAnimationReady(AnimationReadyEvent event)
//	{
//		super.onAnimationReady(event);
//
//		RootPanel.get().addDomHandler(new KeyDownHandler() {
//
//			@Override
//			public void onKeyDown(KeyDownEvent event)
//			{
//				DemoScene rs = (DemoScene) contextPanel.getAnimatedScene();
//				switch(event.getNativeEvent().getKeyCode())
//				{
//				case 79: case 111:/*O*/
//					rs.activeCamera = rs.cameraOrtho;
//					rs.activeHelper = rs.cameraOrthoHelper;
//					break;
//				case 80: case 112:/*P*/
//					rs.activeCamera = rs.cameraPerspective;
//					rs.activeHelper = rs.cameraPerspectiveHelper;
//					break;
//				}
//
//			}
//		}, KeyDownEvent.getType());
//	}

	@Override
	public String getName() {
		return "Cameras";
	}

	@Override
	public String getDescription() {
		return "Here is show how to split viewport to two and use different cameras for each. Use: [O] - orthographic camera,  [P] - perspective camera.";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}

}
