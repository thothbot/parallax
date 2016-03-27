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

import java.util.Map;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.GLRenderTarget;
import org.parallax3d.parallax.graphics.renderers.ShadowMap;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.Postprocessing;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.ShaderPass;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.shaders.CopyShader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.loaders.JsonLoader;
import org.parallax3d.parallax.loaders.Loader;
import org.parallax3d.parallax.loaders.ModelLoadHandler;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.system.gl.enums.TextureMagFilter;
import org.parallax3d.parallax.system.gl.enums.TextureMinFilter;
import org.parallax3d.parallax.system.gl.enums.TextureWrapMode;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;
import org.parallax3d.parallax.tests.resources.BeckmannShader;
import org.parallax3d.parallax.tests.resources.SkinSimpleShader;

@ThreejsExample("webgl_materials_bumpmap_skin")
public final class MaterialsBumpmapSkin extends ParallaxTest implements TouchMoveHandler
{

	private static final String texture = "models/obj/leeperrysmith/Infinite-Level_02_Disp_NoSmoothUV-4096.jpg";
	private static final String textureSpec = "models/obj/leeperrysmith/Map-SPEC.jpg";
	private static final String textureCol = "models/obj/leeperrysmith/Map-COL.jpg";
	private static final String model = "models/obj/leeperrysmith/LeePerrySmith.js";

	Scene scene;
	PerspectiveCamera camera;
	
	Mesh mesh;
	
	Postprocessing composerBeckmann;

	int width = 0, height = 0;
	int mouseX;
	int mouseY;
	
	boolean firstPass = true;

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
				27, // fov
				context.getAspectRation(), // aspect
				1, // near
				10000 // far 
		);
		
		camera.getPosition().setZ(1200);

		// LIGHTS

		scene.add( new AmbientLight( 0x555555 ) );

		DirectionalLight directionalLight = new DirectionalLight( 0xffffff, 1 );
		directionalLight.getPosition().set( 500, 0, 500 );

		directionalLight.setCastShadow(true);

		directionalLight.getShadow().getMap().setWidth( 2048 );
		directionalLight.getShadow().getMap().setHeight( 2048 );

		directionalLight.getShadow().getCamera().setNear( 200 );
		directionalLight.getShadow().getCamera().setFar( 1500 );

		directionalLight.getShadow().getCamera().setLeft( -500 );
		directionalLight.getShadow().getCamera().setRight( 500 );
		directionalLight.getShadow().getCamera().setTop( 500 );
		directionalLight.getShadow().getCamera().setBottom( -500 );

		directionalLight.getShadow().setBias( -0.005 );

		scene.add( directionalLight );

		// COMPOSER BECKMANN

		ShaderPass effectBeckmann = new ShaderPass( new BeckmannShader() );
		ShaderPass effectCopy = new ShaderPass( new CopyShader() );

		effectCopy.setRenderToScreen(true);

		GLRenderTarget target = new GLRenderTarget( 512, 512 );
		target.setMinFilter(TextureMinFilter.LINEAR);
		target.setMagFilter(TextureMagFilter.LINEAR);
		target.setFormat(PixelFormat.RGB);
		target.setStencilBuffer(false);
		composerBeckmann = new Postprocessing( context.getRenderer(), scene, target );
		composerBeckmann.addPass( effectBeckmann );
		composerBeckmann.addPass( effectCopy );

		//
		
		new JsonLoader(model, new ModelLoadHandler() {

			@Override
			public void onModelLoaded(Loader loader, AbstractGeometry geometry) {
				createScene( (Geometry) geometry, 100 );
			}
		});

		//

		context.getRenderer().setClearColor(0x242a34);

		ShadowMap shadowMap = new ShadowMap(context.getRenderer(), scene);
		shadowMap.setCullFrontFaces(false);

		context.getRenderer().setAutoClear(false);
		context.getRenderer().setGammaInput(true);
		context.getRenderer().setGammaOutput(true);
	}
	
	private void createScene(Geometry geometry, double scale )
	{
		Texture mapHeight = new Texture( texture )
				.setAnisotropy(4)
				.setWrapS(TextureWrapMode.REPEAT)
				.setWrapT(TextureWrapMode.REPEAT)
				.setFormat(PixelFormat.RGB);

		Texture mapSpecular = new Texture( textureSpec )
				.setAnisotropy(4)
				.setWrapS(TextureWrapMode.REPEAT)
				.setWrapT(TextureWrapMode.REPEAT)
				.setFormat(PixelFormat.RGB);

		Texture mapColor = new Texture( textureCol )
				.setAnisotropy(4)
				.setWrapS(TextureWrapMode.REPEAT)
				.setWrapT(TextureWrapMode.REPEAT)
				.setFormat(PixelFormat.RGB);

		SkinSimpleShader shader = new SkinSimpleShader();

		Map<String, Uniform> uniforms = shader.getUniforms();
		
		uniforms.get( "enableBump" ).setValue( true );
		uniforms.get( "enableSpecular" ).setValue( true );

		uniforms.get( "tBeckmann" ).setValue( composerBeckmann.getRenderTarget1() );
		uniforms.get( "tDiffuse" ).setValue( mapColor );

		uniforms.get( "bumpMap" ).setValue( mapHeight );
		uniforms.get( "specularMap" ).setValue( mapSpecular );

		((Color)uniforms.get( "diffuse" ).getValue()).setHex( 0xa0a0a0 );
		((Color)uniforms.get( "specular" ).getValue()).setHex( 0xa0a0a0 );

		uniforms.get( "uRoughness" ).setValue( 0.2 );
		uniforms.get( "uSpecularBrightness" ).setValue( 0.5 );

		uniforms.get( "bumpScale" ).setValue( 8.0 );

		ShaderMaterial material = new ShaderMaterial( shader )
				.setLights(true);

		mesh = new Mesh( geometry, material );

		mesh.getPosition().setY(- 50 );
		mesh.getScale().set( scale );

		mesh.setCastShadow(true);
		mesh.setReceiveShadow(true);

		scene.add( mesh );
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double targetX = mouseX * .001;
		double targetY = mouseY * .001;

		if ( mesh != null ) 
		{
			mesh.getRotation().addY( 0.05 * ( targetX - mesh.getRotation().getY() ) );
			mesh.getRotation().addX( 0.05 * ( targetY - mesh.getRotation().getX() ) );
		}

//		if ( firstPass )
//		{
//			composerBeckmann.render();
//			firstPass = false;
//		}

		context.getRenderer().clear();
		context.getRenderer().render(scene, camera);
	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		mouseX = (screenX - width / 2 );
		mouseY = (screenY - height / 2);
	}
	
	@Override
	public String getName() {
		return "Single-pass skin material";
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
