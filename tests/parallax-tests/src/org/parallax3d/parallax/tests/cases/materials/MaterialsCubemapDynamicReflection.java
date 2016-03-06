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

package org.parallax3d.parallax.tests.cases.materials;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.CubeCamera;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.TorusKnotGeometry;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.system.gl.enums.TextureMinFilter;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_materials_cubemap_dynamic2")
public final class MaterialsCubemapDynamicReflection extends ParallaxTest 
{

	static final String img = "textures/ruins.jpg";

	Scene scene;
	PerspectiveCamera camera; 
	
	public int onMouseDownMouseX = 0;
	public int onMouseDownMouseY = 0;
	
	public boolean onMouseDown = false;
	
	public double fov = 70;

	public double lat = 0; 
	public double lon = 0;
	public double phi = 0; 
	public double theta = 0;
	
	private Mesh sphere;
	private Mesh cube;
	private Mesh torus;
	
	private CubeCamera cubeCamera;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				this.fov, // fov
				context.getAspectRation(), // aspect
				1, // near
				1000 // far 
		); 
		
		Texture texture = new Texture(img);
		MeshBasicMaterial mbOpt = new MeshBasicMaterial();
		mbOpt.setMap( texture );
		
		Mesh mesh = new Mesh( new SphereGeometry( 500, 60, 40 ), mbOpt );
		mesh.getScale().setX( -1 );
		scene.add( mesh );

		this.cubeCamera = new CubeCamera( 1, 1000, 256 );
		this.cubeCamera.getRenderTarget().setMinFilter( TextureMinFilter.LINEAR_MIPMAP_LINEAR );
		scene.add( cubeCamera );

		MeshBasicMaterial material = new MeshBasicMaterial()
				.setEnvMap( cubeCamera.getRenderTarget() );
		
		sphere = new Mesh( new SphereGeometry( 20, 30, 15 ), material );
		scene.add( sphere );

		cube = new Mesh( new BoxGeometry( 20, 20, 20 ), material );
		scene.add( cube );

		torus = new Mesh( new TorusKnotGeometry( 20, 5, 100, 25 ), material );
		scene.add( torus );
		
		context.getRenderer().render( scene, camera );
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		this.lon += .15;

		double duration = context.getDeltaTime();

		this.lat = Math.max( - 85.0, Math.min( 85.0, this.lat ) );
		this.phi = Mathematics.degToRad( 90 - lat ) ;
		this.theta = Mathematics.degToRad( this.lon );

		this.sphere.getPosition().setX(Math.sin( duration * 0.001 ) * 30.0 );
		this.sphere.getPosition().setY(Math.sin( duration * 0.0011 ) * 30.0 );
		this.sphere.getPosition().setZ(Math.sin( duration * 0.0012 ) * 30.0 );

		this.sphere.getRotation().addX( 0.02 );
		this.sphere.getRotation().addY( 0.03 );

		this.cube.getPosition().setX(Math.sin( duration * 0.001 + 2.0 ) * 30.0 );
		this.cube.getPosition().setY(Math.sin( duration * 0.0011 + 2.0 ) * 30.0 );
		this.cube.getPosition().setZ(Math.sin( duration * 0.0012 + 2.0 ) * 30.0 );

		this.cube.getRotation().addX( 0.02 );
		this.cube.getRotation().addY( 0.03 );

		this.torus.getPosition().setX(Math.sin( duration * 0.001 + 4.0 ) * 30.0 );
		this.torus.getPosition().setY(Math.sin( duration * 0.0011 + 4.0 ) * 30.0 );
		this.torus.getPosition().setZ(Math.sin( duration * 0.0012 + 4.0 ) * 30.0 );

		this.torus.getRotation().addX( 0.02 );
		this.torus.getRotation().addY( 0.03 );

		camera.getPosition().setX(100.0 * Math.sin( phi ) * Math.cos( theta ) );
		camera.getPosition().setY(100.0 * Math.cos( phi ) );
		camera.getPosition().setZ(100.0 * Math.sin( phi ) * Math.sin( theta ) );

		camera.lookAt( scene.getPosition() );

		this.sphere.setVisible(false); // *cough*

		cubeCamera.updateCubeMap( context.getRenderer(), scene );

		this.sphere.setVisible(true); // *cough*
		
		context.getRenderer().render(scene, camera);
	}
	
	@Override
	public String getName() {
		return "Dynamic map cube reflection";
	}

	@Override
	public String getDescription() {
		return "Use mouse to move and zoom.";
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
//		this.renderingPanel.getCanvas().addMouseWheelHandler(new MouseWheelHandler() {
//			
//			@Override
//			public void onMouseWheel(MouseWheelEvent event) {
//				DemoScene rs = (DemoScene) renderingPanel.getAnimatedScene();
//				rs.fov -= event.getDeltaY() * 1.0;
//				rs.camera.getProjectionMatrix().makePerspective(rs.fov, rs.context.getAspectRation(), 1, 1100);
//			}
//		});
//		
//		this.renderingPanel.getCanvas().addMouseDownHandler(new MouseDownHandler() {
//			
//			@Override
//			public void onMouseDown(MouseDownEvent event) {
//				event.preventDefault();
//
//				DemoScene rs = (DemoScene) renderingPanel.getAnimatedScene();
//				rs.onMouseDownMouseX = event.getX();
//				rs.onMouseDownMouseY = event.getY();
//				
//				rs.onMouseDown = true;
//			}
//		});
//		
//		this.renderingPanel.getCanvas().addMouseUpHandler(new MouseUpHandler() {
//			
//			@Override
//			public void onMouseUp(MouseUpEvent event) {
//				DemoScene rs = (DemoScene) renderingPanel.getAnimatedScene();
//				rs.onMouseDown = false;
//			}
//		});
//				
//		this.renderingPanel.getCanvas().addMouseMoveHandler(new MouseMoveHandler() {
//			
//		      @Override
//		      public void onMouseMove(MouseMoveEvent event)
//		      {
//		    	  	DemoScene rs = (DemoScene) renderingPanel.getAnimatedScene();
//		    	  	if(rs.onMouseDown)
//		    	  	{
//		    	  		rs.lon += ( event.getX() - rs.onMouseDownMouseX ) * 0.01; 
//		    	  		rs.lat += ( event.getY() - rs.onMouseDownMouseY ) * 0.01;
//		    	  	}
//		      }
//		});
//	}

}
