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

package org.parallax3d.parallax.tests.cases.plugins;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.OrthographicCamera;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.geometries.PlaneBufferGeometry;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.RenderTargetTexture;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.*;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.shaders.*;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.loaders.JsonLoader;
import org.parallax3d.parallax.loaders.Loader;
import org.parallax3d.parallax.loaders.ModelLoadHandler;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.system.gl.enums.TextureMagFilter;
import org.parallax3d.parallax.system.gl.enums.TextureMinFilter;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_postprocessing_advanced")
public final class PostprocessingMulti extends ParallaxTest
{

	private static final String model = "models/obj/leeperrysmith/LeePerrySmith.js";
	private static final String texture = "models/obj/leeperrysmith/Infinite-Level_02_Disp_NoSmoothUV-4096.jpg";
	private static final String textureCol = "models/obj/leeperrysmith/Map-COL.jpg";
	private static final String texturebg = "textures/cube/swedishRoyalCastle/pz.jpg";

	Scene scene;
	PerspectiveCamera cameraPerspective;
	OrthographicCamera cameraOrtho;
	
	Scene sceneModel, sceneBG;
	
	Mesh mesh, quadBG, quadMask;
	
	Postprocessing composerScene, composer1, composer2, composer3, composer4;
	TexturePass renderScene;

	@Override
	public void onResize(RenderingContext context) 
	{
/*
		Canvas3d canvas = context.getRenderer().getCanvas();
		int halfWidth = context.getRenderer().getAbsoluteWidth() / 2;
		int halfHeight = context.getRenderer().getAbsoluteHeight() / 2;
		
		cameraPerspective.setAspectRatio( context.getAspectRation() );

		cameraOrtho.setLeft( -halfWidth );
		cameraOrtho.setRight( halfWidth );
		cameraOrtho.setTop( halfHeight );
		cameraOrtho.setBottom( -halfHeight );

		cameraOrtho.updateProjectionMatrix();

		composerScene.reset( new WebGLRenderTarget( halfWidth * 2, halfHeight * 2, rtParameters ) );

		composer1.reset( new WebGLRenderTarget( halfWidth, halfHeight, rtParameters ) );
		composer2.reset( new WebGLRenderTarget( halfWidth, halfHeight, rtParameters ) );
		composer3.reset( new WebGLRenderTarget( halfWidth, halfHeight, rtParameters ) );
		composer4.reset( new WebGLRenderTarget( halfWidth, halfHeight, rtParameters ) );

		renderScene.getMaterial().getShader().getUniforms().get("tDiffuse").setValue( composerScene.getRenderTarget2() );

		quadBG.getScale().set( context.getRenderer().getAbsoluteWidth(), 1, context.getRenderer().getAbsoluteHeight() );
		quadMask.getScale().set( halfWidth, 1, halfHeight );
		*/
	}

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
//		EVENT_BUS.addHandler(ViewportResizeEvent.TYPE, this);

		int width = context.getRenderer().getAbsoluteWidth();
		int height = context.getRenderer().getAbsoluteHeight();
		
		cameraOrtho = new OrthographicCamera( width, height, -10000, 10000 );
		cameraOrtho.getPosition().setZ( 100 );

		cameraPerspective = new PerspectiveCamera( 50, context.getAspectRation(), 1, 10000 );
		cameraPerspective.getPosition().setZ( 900 );

		//

		sceneModel = new Scene();
		sceneBG = new Scene();

		//

		DirectionalLight directionalLight = new DirectionalLight( 0xffffff );
		directionalLight.getPosition().set( 0, -0.1, 1 ).normalize();
		sceneModel.add( directionalLight );

		new JsonLoader(model, new ModelLoadHandler() {

			@Override
			public void onModelLoaded(Loader loader, AbstractGeometry geometry) {
				createMesh( (Geometry) geometry, 100 );
			}
		});
		
		//

		MeshBasicMaterial materialColor = new MeshBasicMaterial()
				.setMap(new Texture(texturebg))
				.setDepthTest(false);

		quadBG = new Mesh( new PlaneBufferGeometry( 1, 1 ), materialColor );
		quadBG.getPosition().setZ( -500 );
		quadBG.getScale().set( width, height, 1 );
		sceneBG.add( quadBG );

		//

		Scene sceneMask = new Scene();
		MeshBasicMaterial maskMaterial = new MeshBasicMaterial().setColor( 0xffaa00 );

		quadMask = new Mesh( new PlaneBufferGeometry( 1, 1 ), maskMaterial );
		quadMask.getPosition().setZ( -300 );
		quadMask.getScale().set( width / 2, height / 2, 1 );
		sceneMask.add( quadMask );

		//

		context.getRenderer().setClearColor( 0x000000, 1 );
		context.getRenderer().setAutoClear(false);
		context.getRenderer().setGammaInput(true);
		context.getRenderer().setGammaOutput(true);

		BleachBypassShader shaderBleach = new BleachBypassShader();
		SepiaShader shaderSepia = new SepiaShader();
		VignetteShader shaderVignette = new VignetteShader();
		CopyShader shaderCopy = new CopyShader();

		ShaderPass effectBleach = new ShaderPass( shaderBleach );
		ShaderPass effectSepia = new ShaderPass( shaderSepia );
		ShaderPass effectVignette = new ShaderPass( shaderVignette );

		effectBleach.getUniforms().get("opacity").setValue( 0.95 );
		effectSepia.getUniforms().get("amount").setValue( 0.9 );
		effectVignette.getUniforms().get("offset").setValue( 0.95 );
		effectVignette.getUniforms().get("darkness").setValue( 1.6 );

		BloomPass effectBloom = new BloomPass( 0.5 );
		FilmPass effectFilmBW = new FilmPass( 0.35, 0.5, 2048, true );
		FilmPass effectFilm = new FilmPass( 0.35, 0.025, 648, false );
		DotScreenPass effectDotScreen = new DotScreenPass( new Vector2( 0, 0 ), 0.5, 0.8 );

		ShaderPass effectHBlur = new ShaderPass( new HorizontalBlurShader() );
		ShaderPass effectVBlur = new ShaderPass( new VerticalBlurShader() );
		effectHBlur.getUniforms().get("h").setValue( 2.0 / ( width / 2.0 ) );
		effectVBlur.getUniforms().get("v").setValue( 2.0 / ( height / 2.0 ) );

		ShaderPass effectColorify1 = new ShaderPass( new ColorifyShader() );
		ShaderPass effectColorify2 = new ShaderPass( new ColorifyShader() );
		((Color)effectColorify1.getUniforms().get("color").getValue()).setRGB( 1, 0.8, 0.8 );
		((Color)effectColorify2.getUniforms().get("color").getValue()).setRGB( 1, 0.75, 0.5 );

		ClearMaskPass clearMask = new ClearMaskPass();
		MaskPass renderMask = new MaskPass( sceneModel, cameraPerspective );
		MaskPass renderMaskInverse = new MaskPass( sceneModel, cameraPerspective );

		renderMaskInverse.setInverse(true);

		//effectFilm.renderToScreen = true;
		//effectFilmBW.renderToScreen = true;
		//effectDotScreen.renderToScreen = true;
		//effectBleach.renderToScreen = true;
		effectVignette.setRenderToScreen(true);
		//effectScreen.renderToScreen = true;

		//
	
		int rtWidth  = width / 2;
		int rtHeight = height / 2;

		RenderTargetTexture rt = new RenderTargetTexture(rtWidth, rtHeight);
		rt.setMinFilter(TextureMinFilter.LINEAR);
		rt.setMagFilter(TextureMagFilter.LINEAR);
		rt.setFormat(PixelFormat.RGB);
		rt.setStencilBuffer(true);

		RenderPass renderBackground = new RenderPass( sceneBG, cameraOrtho );
		RenderPass renderModel = new RenderPass( sceneModel, cameraPerspective );
		renderModel.setClear(false);

		composerScene = new Postprocessing( context.getRenderer(), scene, rt);
		composerScene.setEnabled(false);
		composerScene.addPass( renderBackground );
		composerScene.addPass( renderModel );
		composerScene.addPass( renderMaskInverse );
		composerScene.addPass( effectHBlur );
		composerScene.addPass( effectVBlur );
		composerScene.addPass( clearMask );

		//

		renderScene = new TexturePass( composerScene.getRenderTarget2() );

		//

		composer1 = new Postprocessing( context.getRenderer(), scene, rt.clone() );
		composer1.setEnabled(false);

		composer1.addPass( renderScene );
		//composer1.addPass( renderMask );
		composer1.addPass( effectFilmBW );
		//composer1.addPass( clearMask );
		composer1.addPass( effectVignette );

		//

		composer2 = new Postprocessing( context.getRenderer(), scene, rt.clone() );
		composer2.setEnabled(false);

		composer2.addPass( renderScene );
		composer2.addPass( effectDotScreen );
		composer2.addPass( renderMask );
		composer2.addPass( effectColorify1 );
		composer2.addPass( clearMask );
		composer2.addPass( renderMaskInverse );
		composer2.addPass( effectColorify2 );
		composer2.addPass( clearMask );
		composer2.addPass( effectVignette );

		//

		composer3 = new Postprocessing( context.getRenderer(), scene, rt.clone() );
		composer3.setEnabled(false);

		composer3.addPass( renderScene );
		//composer3.addPass( renderMask );
		composer3.addPass( effectSepia );
		composer3.addPass( effectFilm );
		//composer3.addPass( clearMask );
		composer3.addPass( effectVignette );

		//

		composer4 = new Postprocessing( context.getRenderer(), scene, rt.clone() );
		composer4.setEnabled(false);

		composer4.addPass( renderScene );
		//composer4.addPass( renderMask );
		composer4.addPass( effectBloom );
		composer4.addPass( effectFilm );
		composer4.addPass( effectBleach );
		//composer4.addPass( clearMask );
		composer4.addPass( effectVignette );

		renderScene.getMaterial().getShader().getUniforms().get("tDiffuse").setValue( composerScene.getRenderTarget2() );
	}
	
	private void createMesh(Geometry geometry, double scale )
	{
		geometry.computeTangents();

		MeshLambertMaterial mat2 = new MeshLambertMaterial()
				.setColor( 0x999999 )
				.setAmbient( 0x444444 )
				.setMap(new Texture( textureCol ));

		mesh = new Mesh( geometry, mat2 );
		mesh.getPosition().set( 0, -50, 0 );
		mesh.getScale().set( scale );

		sceneModel.add( mesh );
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		if ( mesh != null )
			mesh.getRotation().addY( -0.04 );

		int halfWidth = context.getRenderer().getAbsoluteWidth() / 2;
		int halfHeight = context.getRenderer().getAbsoluteHeight() / 2;
		
		context.getRenderer().setViewport( 0, 0, 2 * halfWidth, 2 * halfHeight );

		context.getRenderer().clear();
		
		composerScene.setEnabled(true);
		context.getRenderer().render(scene, this.cameraPerspective);
		composerScene.setEnabled(false);

		context.getRenderer().setViewport( 0, 0, halfWidth, halfHeight );
		composer1.setEnabled(true);
		context.getRenderer().render(scene, this.cameraPerspective);
		composer1.setEnabled(false);

		context.getRenderer().setViewport( halfWidth, 0, halfWidth, halfHeight );
		composer2.setEnabled(true);
		context.getRenderer().render(scene, this.cameraPerspective);
		composer2.setEnabled(false);

		context.getRenderer().setViewport( 0, halfHeight, halfWidth, halfHeight );
		composer3.setEnabled(true);
		context.getRenderer().render(scene, this.cameraPerspective);
		composer3.setEnabled(false);

		context.getRenderer().setViewport( halfWidth, halfHeight, halfWidth, halfHeight );
		composer4.setEnabled(true);
		context.getRenderer().render(scene, this.cameraPerspective);
		composer4.setEnabled(false);
	}
		
	@Override
	public String getName() {
		return "Postprocessing";
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
