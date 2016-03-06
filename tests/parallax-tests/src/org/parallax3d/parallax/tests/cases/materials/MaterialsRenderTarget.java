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
import org.parallax3d.parallax.graphics.cameras.OrthographicCamera;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.extras.geometries.PlaneBufferGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.TorusGeometry;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.RenderTargetTexture;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.system.gl.enums.TextureMagFilter;
import org.parallax3d.parallax.system.gl.enums.TextureMinFilter;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_rtt")
public class MaterialsRenderTarget extends ParallaxTest implements TouchMoveHandler
{
	interface ResourcesPass1 extends Shader.DefaultResources
	{
		ResourcesPass1 INSTANCE = SourceBundleProxy.create(ResourcesPass1.class);

		@Source("../../resources/shaders/rtt.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("../../resources/shaders/rtt_pass_1.fs.glsl")
		SourceTextResource getFragmentShader();
	}

	interface ResourcesScreen extends Shader.DefaultResources
	{
		ResourcesScreen INSTANCE = SourceBundleProxy.create(ResourcesScreen.class);

		@Source("../../resources/shaders/rtt.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("../../resources/shaders/rtt_screen.fs.glsl")
		SourceTextResource getFragmentShader();
	}
		
	PerspectiveCamera camera;
	OrthographicCamera cameraRTT;
	Scene scene, sceneRTT, sceneScreen;
	
	RenderTargetTexture rtTexture;
	
	ShaderMaterial material;
	
	Mesh zmesh1, zmesh2;

	int width = 0, height = 0;
	int mouseX;
	int mouseY;
	
	double delta = 0.01;
	
	ShaderMaterial materialScreen;
	
	Mesh quad1;

	@Override
	public void onResize(RenderingContext context) {
		width = context.getWidth();
		height = context.getHeight();
	}

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera( 30,
				context.getAspectRation(),
				1, 
				10000 
		);
		camera.getPosition().setZ(100);
		
		cameraRTT = new OrthographicCamera( context.getRenderer().getAbsoluteWidth(), context.getRenderer().getAbsoluteHeight() , -10000, 10000 );
		cameraRTT.getPosition().setZ(100);

		sceneRTT = new Scene();
		sceneScreen = new Scene();

		DirectionalLight light1 = new DirectionalLight( 0xffffff );
		light1.getPosition().set( 0, 0, 1 ).normalize();
		sceneRTT.add( light1 );

		DirectionalLight light2 = new DirectionalLight( 0xffaaaa, 1.5 );
		light2.getPosition().set( 0, 0, -1 ).normalize();
		sceneRTT.add( light2 );

		rtTexture = new RenderTargetTexture( context.getRenderer().getAbsoluteWidth(), context.getRenderer().getAbsoluteHeight() );
		rtTexture.setMinFilter(TextureMinFilter.LINEAR);
		rtTexture.setMagFilter(TextureMagFilter.NEAREST);
		rtTexture.setFormat(PixelFormat.RGBA);

		material = new ShaderMaterial(ResourcesPass1.INSTANCE);
		material.getShader().addUniform("time", new Uniform(Uniform.TYPE.F, 0.0 ));

		materialScreen = new ShaderMaterial(ResourcesScreen.INSTANCE)
				.setDepthWrite(false);
		materialScreen.getShader().addUniform("tDiffuse", new Uniform(Uniform.TYPE.T, rtTexture ));

		PlaneBufferGeometry plane = new PlaneBufferGeometry(context.getRenderer().getAbsoluteWidth(), context.getRenderer().getAbsoluteHeight() );
		quad1 = new Mesh( plane, material );
		quad1.getPosition().setZ( -100 );
		sceneRTT.add( quad1 );

		MeshPhongMaterial mat2 = new MeshPhongMaterial()
				.setColor(0x550000)
				.setSpecular(0xff2200)
				.setShininess(5.0);
		
		MeshPhongMaterial mat1 = new MeshPhongMaterial()
				.setColor(0x555555)
				.setSpecular(0xffaa00)
				.setShininess(5.0);

		TorusGeometry geometry = new TorusGeometry( 100, 25, 15, 30 );

		zmesh1 = new Mesh( geometry, mat1 );
		zmesh1.getPosition().set( 0, 0, 100 );
		zmesh1.getScale().set( 1.5, 1.5, 1.5 );
		sceneRTT.add( zmesh1 );
		
		zmesh2 = new Mesh( geometry, mat2 );
		zmesh2.getPosition().set( 0, 150, 100 );
		zmesh2.getScale().set( 0.75, 0.75, 0.75 );
		sceneRTT.add( zmesh2 );

		Mesh quad2 = new Mesh( plane, materialScreen );
		quad2.getPosition().setZ( -100 );
		sceneScreen.add( quad2 );

		int n = 5;
		SphereGeometry geometry2 = new SphereGeometry( 10, 64, 32 );
		MeshBasicMaterial material2 = new MeshBasicMaterial()
				.setColor(0xffffff)
				.setMap(rtTexture);

		for( int j = 0; j < n; j ++ ) {

			for( int i = 0; i < n; i ++ ) {

				Mesh mesh = new Mesh( geometry2, material2 );

				mesh.getPosition().setX(  ( i - ( n - 1.0 ) / 2.0 ) * 20.0 );
				mesh.getPosition().setY( ( j - ( n - 1.0 ) / 2.0 ) * 20.0 );
				mesh.getPosition().setZ( 0 );

				mesh.getRotation().setY( - Math.PI / 2 );

				scene.add( mesh );

			}

		}

		context.getRenderer().setAutoClear(false);
	}
			
	@Override
	public void onUpdate(RenderingContext context)
	{

		double duration = context.getDeltaTime();
		camera.getPosition().addX( ( mouseX - camera.getPosition().getX() ) * 0.05 );
		camera.getPosition().addY( ( - mouseY - camera.getPosition().getY() ) * 0.05 );

		camera.lookAt( scene.getPosition() );

		if ( zmesh1 != null && zmesh2 != null ) {

			zmesh1.getRotation().setY( - duration * 0.0015 );
			zmesh2.getRotation().setY(- duration * 0.0015 + Math.PI / 2 );

		}

		if ( (Double)material.getShader().getUniforms().get("time").getValue() > 1.0 
				|| (Double)material.getShader().getUniforms().get("time").getValue() < 0.0 ) {

			delta *= -1;

		}

		material.getShader().getUniforms().get("time").setValue((Double)material.getShader().getUniforms().get("time").getValue() + delta);

		context.getRenderer().clear();

		// Render first scene into texture

		context.getRenderer().render( sceneRTT, cameraRTT, rtTexture, true );

		// Render full screen quad with generated texture

		context.getRenderer().render( sceneScreen, cameraRTT );

		// Render second scene to screen
		// (using first scene as regular texture)

		context.getRenderer().render( scene, camera );
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		mouseX = (screenX - width / 2 );
		mouseY = (screenY - height / 2);
	}


	@Override
	public String getName() {
		return "Render to texture";
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

