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
import org.parallax3d.parallax.controllers.TrackballControls;
import org.parallax3d.parallax.graphics.cameras.OrthographicCamera;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.geometries.PlaneGeometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.lights.PointLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.objects.MorphAnimMesh;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.graphics.renderers.RenderTargetTexture;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.BloomPass;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.Postprocessing;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.RenderPass;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.ShaderPass;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.shaders.*;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.scenes.Fog;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.input.KeyCodes;
import org.parallax3d.parallax.input.KeyDownHandler;
import org.parallax3d.parallax.loaders.JsonLoader;
import org.parallax3d.parallax.loaders.Loader;
import org.parallax3d.parallax.loaders.ModelLoadHandler;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.Duration;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.SourceTextResource;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.system.gl.enums.TextureMagFilter;
import org.parallax3d.parallax.system.gl.enums.TextureMinFilter;
import org.parallax3d.parallax.system.gl.enums.TextureWrapMode;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.resources.TerrainShader;

import java.util.ArrayList;
import java.util.List;

public final class TerrainDynamic extends ParallaxTest implements KeyDownHandler, Texture.ImageLoadHandler
{

	interface Resources extends Shader.DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("../../resources/shaders/terrain_dynamic_noise.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("../../resources/shaders/terrain_dynamic_noise.fs.glsl")
		SourceTextResource getFragmentShader();
	}
	
	static final String diffuseImage1 = "textures/terrain/grasslight-big.jpg";
	static final String diffuseImage2 = "textures/terrain/backgrounddetailed6.jpg";
	static final String detailImage = "textures/terrain/grasslight-big-nm.jpg";
	
	static final String parrotModel = "models/animated/parrot.js";
	static final String flamingoModel = "models/animated/flamingo.js";
	static final String storkModel = "models/animated/stork.js";
		
	static final int bluriness = 6;

	Scene scene;
	PerspectiveCamera camera;
	
	OrthographicCamera cameraOrtho;
	Scene sceneRenderTarget;
	
	FastMap<ShaderMaterial> mlib;
	List<MorphAnimMesh> morphs;
	
	FastMap<Uniform> uniformsTerrain;
	FastMap<Uniform> uniformsNoise;
	
	RenderTargetTexture heightMap;
	RenderTargetTexture normalMap;
	
	DirectionalLight directionalLight;
	PointLight pointLight;
	
	TrackballControls controls;
	
	ShaderPass hblur, vblur; 
	
	Mesh terrain;
	Mesh quadTarget;
	
	int textureCounter;
	
	double animDelta = 0;
	int animDeltaDir = -1;
	double lightVal = 0;
	int lightDir = 1;
	
	int screenWidth = 1000, screenHeight = 1000;
	
	private double oldTime;
	private boolean updateNoise = true;
	
	@Override
	public void onResize(RenderingContext context) 
	{
		screenWidth = context.getRenderer().getAbsoluteWidth();
		screenHeight = context.getRenderer().getAbsoluteHeight();
		
		hblur.getUniforms().get( "h" ).setValue( bluriness / (double)screenWidth );
		vblur.getUniforms().get( "v" ).setValue( bluriness / (double)screenHeight );
	}

	@Override
	public void onStart(final RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				40, // fov
				context.getAspectRation(), // aspect
				2, // near
				4000 // far 
		); 
					
		screenWidth = context.getRenderer().getAbsoluteWidth();
		screenHeight = context.getRenderer().getAbsoluteHeight();
		cameraOrtho = new OrthographicCamera( screenWidth, screenHeight, -10000, 10000 );
		
		camera.getPosition().set( -1200, 800, 1200 );
		cameraOrtho.getPosition().setZ( 100 );
		
		// SCENE (RENDER TARGET)

		sceneRenderTarget = new Scene();
		sceneRenderTarget.add( cameraOrtho );

		// CAMERA

		controls = new TrackballControls( camera, context );
		controls.getTarget().set( 0 );

		controls.setRotateSpeed(1.0);
		controls.setZoomSpeed(1.2);
		controls.setPanSpeed(0.8);

		controls.setZoom(true);
		controls.setPan(true);

		controls.setStaticMoving(true);
		controls.setDynamicDampingFactor(0.15);

		// SCENE (FINAL)

		scene.setFog( new Fog( 0x050505, 2000, 4000 ) );

		// LIGHTS

		scene.add( new AmbientLight( 0x111111 ) );

		directionalLight = new DirectionalLight( 0xffffff, 1.15 );
		directionalLight.getPosition().set( 500, 2000, 0.0 );
		scene.add( directionalLight );

		pointLight = new PointLight( 0xff4400, 1.5, 0.0 );
		pointLight.getPosition().set( 0.0 );
		scene.add( pointLight );

		// HEIGHT + NORMAL MAPS

		int rx = 256, ry = 256;

		heightMap  = new RenderTargetTexture( rx, ry );
		heightMap.setMinFilter(TextureMinFilter.LINEAR_MIPMAP_LINEAR);
		heightMap.setMagFilter(TextureMagFilter.LINEAR);
		heightMap.setFormat(PixelFormat.RGB);
		heightMap.setGenerateMipmaps(false);
		
		normalMap = new RenderTargetTexture( rx, ry );
		normalMap.setMinFilter(TextureMinFilter.LINEAR_MIPMAP_LINEAR);
		normalMap.setMagFilter(TextureMagFilter.LINEAR);
		normalMap.setFormat(PixelFormat.RGB);
		normalMap.setGenerateMipmaps(false);

		NormalMapShader normalShader = new NormalMapShader();

		FastMap<Uniform> uniformsNormal = normalShader.getUniforms();

		uniformsNormal.get("height").setValue( 0.05 );
		((Vector2)uniformsNormal.get("resolution").getValue()).set( rx, ry );
		uniformsNormal.get("heightMap").setValue( heightMap );

		// TEXTURES

		final RenderTargetTexture specularMap = new RenderTargetTexture( 2048, 2048 );
		specularMap.setMinFilter(TextureMinFilter.LINEAR_MIPMAP_LINEAR);
		specularMap.setMagFilter(TextureMagFilter.LINEAR);
		specularMap.setFormat(PixelFormat.RGB);
		specularMap.setGenerateMipmaps(false);
		specularMap.setWrapS(TextureWrapMode.REPEAT);
		specularMap.setWrapT(TextureWrapMode.REPEAT);

		Texture diffuseTexture1 = new Texture( diffuseImage1, new Texture.ImageLoadHandler() {

			@Override
			public void onImageLoad(Texture texture) {
				TerrainDynamic.this.onImageLoad(texture);
				TerrainDynamic.this.applyShader( context.getRenderer(), new LuminosityShader(), texture, specularMap );
			}
		});

		diffuseTexture1.setWrapS(TextureWrapMode.REPEAT);
		diffuseTexture1.setWrapT(TextureWrapMode.REPEAT);

		Texture diffuseTexture2 = new Texture( diffuseImage2, this);

		diffuseTexture2.setWrapS(TextureWrapMode.REPEAT);
		diffuseTexture2.setWrapT(TextureWrapMode.REPEAT);

		Texture detailTexture = new Texture( detailImage, this );
		detailTexture.setWrapS(TextureWrapMode.REPEAT);
		detailTexture.setWrapT(TextureWrapMode.REPEAT);

		// TERRAIN SHADER

		TerrainShader terrainShader = new TerrainShader();

		uniformsTerrain = terrainShader.getUniforms();

		uniformsTerrain.get( "tNormal" ).setValue( normalMap );
		uniformsTerrain.get( "uNormalScale" ).setValue( 3.5 );

		uniformsTerrain.get( "tDisplacement" ).setValue( heightMap );

		uniformsTerrain.get( "tDiffuse1" ).setValue( diffuseTexture1 );
		uniformsTerrain.get( "tDiffuse2" ).setValue( diffuseTexture2 );
		uniformsTerrain.get( "tSpecular" ).setValue( specularMap );
		uniformsTerrain.get( "tDetail" ).setValue( detailTexture );

		uniformsTerrain.get( "enableDiffuse1" ).setValue( true );
		uniformsTerrain.get( "enableDiffuse2" ).setValue( true );
		uniformsTerrain.get( "enableSpecular" ).setValue( true );

		((Color)uniformsTerrain.get( "diffuse" ).getValue()).setHex( 0xffffff );
		((Color)uniformsTerrain.get( "specular").getValue()).setHex( 0xffffff );
		((Color)uniformsTerrain.get( "ambient" ).getValue()).setHex( 0x111111 );

		uniformsTerrain.get( "shininess" ).setValue( 30.0 );
		uniformsTerrain.get( "uDisplacementScale" ).setValue( 375.0 );

		((Vector2)uniformsTerrain.get( "uRepeatOverlay" ).getValue()).set( 6.0, 6.0 );

		uniformsNoise = new FastMap<>();
		uniformsNoise.put("time", new Uniform(Uniform.TYPE.F, 1.0));
		uniformsNoise.put("scale", new Uniform(Uniform.TYPE.V2, new Vector2( 1.5, 1.5 )));
		uniformsNoise.put("offset", new Uniform(Uniform.TYPE.V2, new Vector2( 0.0, 0.0 )));
		
		mlib = new FastMap<>();
		
		ShaderMaterial materialHeightmap = new ShaderMaterial(Resources.INSTANCE);
		materialHeightmap.getShader().setUniforms(uniformsNoise);
		materialHeightmap.setLights(false);
		materialHeightmap.setFog(true);
		mlib.put("heightmap", materialHeightmap);
		
		ShaderMaterial materialNormal = new ShaderMaterial(normalShader);
		materialNormal.getShader().setUniforms(uniformsNormal);
		materialNormal.setLights(false);
		materialNormal.setFog(true);
		mlib.put("normal", materialNormal);
		
		ShaderMaterial materialTerrain = new ShaderMaterial(terrainShader);
		materialTerrain.getShader().setUniforms(uniformsTerrain);
		materialTerrain.setLights(true);
		materialTerrain.setFog(true);
		mlib.put("terrain", materialTerrain);

		PlaneGeometry plane = new PlaneGeometry( screenWidth, screenHeight );
		MeshBasicMaterial planeMaterial = new MeshBasicMaterial().setColor( 0x000000 );
		quadTarget = new Mesh( plane, planeMaterial );
		quadTarget.getPosition().setZ( -500 );
		sceneRenderTarget.add( quadTarget );

		// TERRAIN MESH

		PlaneGeometry geometryTerrain = new PlaneGeometry( 6000, 6000, 64, 64 );
		geometryTerrain.computeTangents();

		terrain = new Mesh( geometryTerrain, materialTerrain );
		terrain.getPosition().set( 0, -125, 0 );
		terrain.getRotation().setX( -Math.PI / 2 );
		terrain.setVisible(false);
		scene.add( terrain );

		// RENDERER

		context.getRenderer().setClearColor( scene.getFog().getColor(), 1 );
		context.getRenderer().setGammaInput(true);
		context.getRenderer().setGammaOutput(true);

		// COMPOSER
		context.getRenderer().setAutoClear(false);
		
		RenderPass renderModel = new RenderPass( scene, camera );
		
		BloomPass effectBloom = new BloomPass( 0.6 );
		ShaderPass effectBleach = new ShaderPass( new BleachBypassShader() );
		effectBleach.getUniforms().get( "opacity" ).setValue( 0.65 );

		hblur = new ShaderPass( new HorizontalTiltShiftShader() );
		vblur = new ShaderPass( new VerticalTiltShiftShader() );

		hblur.getUniforms().get( "h" ).setValue( bluriness / (double)screenWidth );
		vblur.getUniforms().get( "v" ).setValue( bluriness / (double)screenHeight );
		hblur.getUniforms().get( "r" ).setValue( 0.5 ); 
		vblur.getUniforms().get( "r" ).setValue( 0.5 );
		vblur.setRenderToScreen(true);

		RenderTargetTexture renderTarget = new RenderTargetTexture( screenWidth, screenHeight );
		specularMap.setMinFilter(TextureMinFilter.LINEAR);
		specularMap.setMagFilter(TextureMagFilter.LINEAR);
		specularMap.setFormat(PixelFormat.RGB);
		specularMap.setStencilBuffer(false);
		specularMap.setGenerateMipmaps(false);
		
		Postprocessing composer = new Postprocessing( context.getRenderer(), scene, renderTarget );
		composer.addPass( renderModel );
		composer.addPass( effectBloom );
		composer.addPass( effectBleach );

		composer.addPass( hblur );
		composer.addPass( vblur );
		
		final double startX = -3000;
		morphs = new ArrayList<MorphAnimMesh>();

		new JsonLoader(parrotModel, new ModelLoadHandler() {

			@Override
			public void onModelLoaded(Loader loader, AbstractGeometry geometry) {
				((JsonLoader)loader).morphColorsToFaceColors((Geometry) geometry);
				addMorph( (Geometry)geometry, 500, startX -500, 500, 700 );
				addMorph( (Geometry)geometry, 500, startX - Math.random() * 500, 500, -200 );
				addMorph( (Geometry)geometry, 500, startX - Math.random() * 500, 500, 200 );
				addMorph( (Geometry)geometry, 500, startX - Math.random() * 500, 500, 1000 );

			}
		});

		new JsonLoader(flamingoModel, new ModelLoadHandler() {

			@Override
			public void onModelLoaded(Loader loader, AbstractGeometry geometry) {

				((JsonLoader)loader).morphColorsToFaceColors((Geometry) geometry);
				addMorph( (Geometry)geometry, 1000, startX - Math.random() * 500, 350, 40 );
			}
		});

		new JsonLoader(storkModel, new ModelLoadHandler() {

			@Override
			public void onModelLoaded(Loader loader, AbstractGeometry geometry) {

				((JsonLoader)loader).morphColorsToFaceColors((Geometry) geometry);
				addMorph( (Geometry)geometry, 1000, startX - Math.random() * 500, 350, 340 );
			}
		});
	
		this.oldTime = Duration.currentTimeMillis();
	}
	
	private void addMorph(Geometry geometry, int duration, double x, double y, double z )
	{
		MeshLambertMaterial material = new MeshLambertMaterial()
				.setColor( 0xffaa55 )
				.setMorphTargets(true)
				.setVertexColors(Material.COLORS.FACE);

		MorphAnimMesh meshAnim = new MorphAnimMesh( geometry, material );

		meshAnim.setDuration(duration);
		meshAnim.setTime( (int)(600 * Math.random()) );

		meshAnim.getPosition().set( x, y, z );
		meshAnim.getRotation().setY( Math.PI/2 );

		meshAnim.setCastShadow(true);
		meshAnim.setReceiveShadow(true);

		scene.add( meshAnim );

		morphs.add( meshAnim );

	}
	
	private void applyShader(GLRenderer renderer,  Shader shader, Texture texture, RenderTargetTexture target )
	{
		ShaderMaterial shaderMaterial = new ShaderMaterial(shader);

		shaderMaterial.getShader().getUniforms().get("tDiffuse").setValue(texture);

		Scene sceneTmp = new Scene();

		Mesh meshTmp = new Mesh( new PlaneGeometry( screenWidth, screenHeight ), shaderMaterial );
		meshTmp.getPosition().setZ( -500 );

		sceneTmp.add( meshTmp );

		renderer.render( sceneTmp, cameraOrtho, target, true );
	}

	public void onImageLoad(Texture texture)
	{
		textureCounter += 1;

		if ( textureCounter == 3 )	
		{
			terrain.setVisible(true);
		}
	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double delta = (Duration.currentTimeMillis() - this.oldTime) * 0.001;

		if ( terrain.isVisible() ) 
		{
			controls.update();

			double fLow = 0.4, fHigh = 0.825;

			lightVal = Mathematics.clamp( lightVal + 0.5 * delta * lightDir, fLow, fHigh );
			double valNorm = ( lightVal - fLow ) / ( fHigh - fLow );

			double sat = Mathematics.mapLinear( valNorm, 0, 1, 0.95, 0.25 );
			scene.getFog().getColor().setHSL( 0.1, sat, lightVal );

			context.getRenderer().setClearColor( scene.getFog().getColor(), 1 );

			directionalLight.setIntensity( Mathematics.mapLinear( valNorm, 0, 1, 0.1, 1.15 ) );
			pointLight.setIntensity( Mathematics.mapLinear( valNorm, 0, 1, 0.9, 1.5 ) );

			uniformsTerrain.get( "uNormalScale" ).setValue( Mathematics.mapLinear( valNorm, 0, 1, 0.6, 3.5 ) );

			if ( updateNoise ) 
			{
				animDelta = Mathematics.clamp( animDelta + 0.00075 * animDeltaDir, 0, 0.05 );
				uniformsNoise.get( "time" ).setValue( (Double)uniformsNoise.get( "time" ).getValue() + delta * animDelta );
				((Vector2)uniformsNoise.get( "offset" ).getValue()).addX( delta * 0.05 );

				((Vector2)uniformsTerrain.get( "uOffset" ).getValue()).setX( 4 * ((Vector2)uniformsNoise.get( "offset" ).getValue()).getX() );

				quadTarget.setMaterial( mlib.get( "heightmap" ));
				context.getRenderer().render( sceneRenderTarget, cameraOrtho, heightMap, true );

				quadTarget.setMaterial( mlib.get( "normal" ));
				context.getRenderer().render( sceneRenderTarget, cameraOrtho, normalMap, true );

				updateNoise = false;
			}

			for ( int i = 0; i < morphs.size(); i ++ ) 
			{
				MorphAnimMesh morph = morphs.get( i );

				morph.updateAnimation( (int)(1000 * delta) );

				morph.getPosition().addX( morph.getDuration() * delta );

				if ( morph.getPosition().getX()  > 2000 )  
				{
					morph.getPosition().setX( -1500 - Math.random() * 500 );
				}
			}
			
			context.getRenderer().render( scene, camera );
		}
		
		this.oldTime = Duration.currentTimeMillis();
	}

	@Override
	public void onKeyDown(int keycode) {
		switch(keycode)
		{
			case KeyCodes.KEY_N:
			case KeyCodes.KEY_NUM_PERIOD:
				lightDir *= -1;
				break;
		}
	}

	@Override
	public String getName() {
		return "Dynamic procedural terrain";
	}

	@Override
	public String getDescription() {
		return "Used 3d simplex noise. Options - day / night: [n].";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}

}
