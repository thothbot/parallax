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
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.lights.AmbientLight;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.lights.PointLight;
import org.parallax3d.parallax.graphics.lights.SpotLight;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.RenderTargetTexture;
import org.parallax3d.parallax.graphics.renderers.ShadowMap;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector4;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.system.gl.enums.TextureMagFilter;
import org.parallax3d.parallax.system.gl.enums.TextureMinFilter;
import org.parallax3d.parallax.system.gl.enums.TextureWrapMode;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;
import org.parallax3d.parallax.tests.resources.BeckmannShader;
import org.parallax3d.parallax.tests.resources.SkinSimpleShader;

@ThreejsExample("webgl_materials_bumpmap_skin")
public final class MaterialsBumpmapSkin extends ParallaxTest 
{

	private static final String texture = "./assets/models/obj/leeperrysmith/Infinite-Level_02_Disp_NoSmoothUV-4096.jpg";
	private static final String textureSpec = "./assets/models/obj/leeperrysmith/Map-SPEC.jpg";
	private static final String textureCol = "./assets/models/obj/leeperrysmith/Map-COL.jpg";
	private static final String model = "./assets/models/obj/leeperrysmith/LeePerrySmith.js";

	Scene scene;
	PerspectiveCamera camera;
	
	Mesh mesh;
	
//	Postprocessing composerBeckmann;
	
	int mouseX = 0, mouseY = 0;
	
	boolean firstPass = true;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				27, // fov
				context.getRenderer().getAbsoluteAspectRation(), // aspect 
				1, // near
				10000 // far 
		);
		
		camera.getPosition().setZ(1200);

		// LIGHTS

		scene.add( new AmbientLight( 0x555555 ) );

		PointLight pointLight = new PointLight( 0xffffff, 1.5, 1000 );
		pointLight.getPosition().set( 0, 0, 600 );

		scene.add( pointLight );

		// shadow for PointLight

		SpotLight spotLight = new SpotLight( 0xffffff, 1 );
		spotLight.getPosition().set( 0.05, 0.05, 1 );
		scene.add( spotLight );
		
		spotLight.getPosition().multiply( 700 );

		spotLight.setCastShadow(true);
		spotLight.setOnlyShadow(true);
		//spotLight.shadowCameraVisible = true;

		spotLight.setShadowMapWidth( 2048 );
		spotLight.setShadowMapHeight( 2048 );

		spotLight.setShadowCameraNear( 200 );
		spotLight.setShadowCameraFar( 1500 );

		spotLight.setShadowCameraFov( 40 );

		spotLight.setShadowBias( -0.005 );
		spotLight.setShadowDarkness( 0.15 );

		//

		DirectionalLight directionalLight = new DirectionalLight( 0xffffff, 0.85 );
		directionalLight.getPosition().set( 1, -0.5, 1 );
		directionalLight.getColor().setHSL( 0.6, 1.0, 0.85 );
		scene.add( directionalLight );

		directionalLight.getPosition().multiply( 500 );
		
		directionalLight.setCastShadow(true);
		//directionalLight.shadowCameraVisible = true;

		directionalLight.setShadowMapWidth( 2048 );
		directionalLight.setShadowMapHeight( 2048 );

		directionalLight.setShadowCameraNear( 200 );
		directionalLight.setShadowCameraFar( 1500 );

		directionalLight.setShadowCameraLeft( -500 );
		directionalLight.setShadowCameraRight( 500 );
		directionalLight.setShadowCameraTop( 500 );
		directionalLight.setShadowCameraBottom( -500 );

		directionalLight.setShadowBias( -0.005 );
		directionalLight.setShadowDarkness( 0.15 );

		//

		DirectionalLight directionalLight2 = new DirectionalLight( 0xffffff, 0.85 );
		directionalLight2.getPosition().set( 1, -0.5, -1 );
		scene.add( directionalLight2 );
		
		// COMPOSER BECKMANN

//		ShaderPass effectBeckmann = new ShaderPass( new BeckmannShader() );
//		ShaderPass effectCopy = new ShaderPass( new CopyShader() );
//
//		effectCopy.setRenderToScreen(true);
//
//		RenderTargetTexture target = new RenderTargetTexture( 512, 512 );
//		target.setMinFilter(TextureMinFilter.LINEAR);
//		target.setMagFilter(TextureMagFilter.LINEAR);
//		target.setFormat(PixelFormat.RGB);
//		target.setStencilBuffer(false);
//		composerBeckmann = new Postprocessing( context.getRenderer(), scene, target );
//			composerBeckmann.addPass( effectBeckmann );
//			composerBeckmann.addPass( effectScreen );

		//
		
//		new JsonLoader(model, new XHRLoader.ModelLoadHandler() {
//
//			@Override
//			public void onModelLoaded(XHRLoader loader, AbstractGeometry geometry) {
//				createScene( (Geometry) geometry, 100 );
//			}
//		});

		//

		context.getRenderer().setClearColor(0x4c5159);

		ShadowMap shadowMap = new ShadowMap(context.getRenderer(), scene);
		shadowMap.setCullFrontFaces(false);

		context.getRenderer().setAutoClear(false);
		context.getRenderer().setGammaInput(true);
		context.getRenderer().setGammaOutput(true);
	}
	
	private void createScene(Geometry geometry, double scale )
	{
		Texture mapHeight = new Texture( texture );

		mapHeight.setAnisotropy(4);
		mapHeight.getRepeat().set( 0.998, 0.998 );
		mapHeight.getOffset().set( 0.001, 0.001 );
		mapHeight.setWrapS(TextureWrapMode.REPEAT);
		mapHeight.setWrapT(TextureWrapMode.REPEAT);
		mapHeight.setFormat(PixelFormat.RGB);

		Texture mapSpecular = new Texture( textureSpec );
		mapSpecular.getRepeat().set( 0.998, 0.998 );
		mapSpecular.getOffset().set( 0.001, 0.001 );
		mapSpecular.setWrapS(TextureWrapMode.REPEAT);
		mapSpecular.setWrapT(TextureWrapMode.REPEAT);
		mapSpecular.setFormat(PixelFormat.RGB);

		Texture mapColor = new Texture( textureCol );
		mapColor.getRepeat().set( 0.998, 0.998 );
		mapColor.getOffset().set( 0.001, 0.001 );
		mapColor.setWrapS(TextureWrapMode.REPEAT);
		mapColor.setWrapT(TextureWrapMode.REPEAT);
		mapColor.setFormat(PixelFormat.RGB);

		SkinSimpleShader shader = new SkinSimpleShader();

		Map<String, Uniform> uniforms = shader.getUniforms();
		
		uniforms.get( "enableBump" ).setValue( true );
		uniforms.get( "enableSpecular" ).setValue( true );

//		uniforms.get( "tBeckmann" ).setValue( composerBeckmann.getRenderTarget1() );
		uniforms.get( "tDiffuse" ).setValue( mapColor );

		uniforms.get( "bumpMap" ).setValue( mapHeight );
		uniforms.get( "specularMap" ).setValue( mapSpecular );

		((Color)uniforms.get( "ambient" ).getValue()).setHex( 0xa0a0a0 );
		((Color)uniforms.get( "diffuse" ).getValue()).setHex( 0xa0a0a0 );
		((Color)uniforms.get( "specular" ).getValue()).setHex( 0xa0a0a0 );

		uniforms.get( "uRoughness" ).setValue( 0.145 );
		uniforms.get( "uSpecularBrightness" ).setValue( 0.75 );

		uniforms.get( "bumpScale" ).setValue( 16.0 );

		((Vector4)uniforms.get( "offsetRepeat" ).getValue()).set( 0.001, 0.001, 0.998, 0.998 );

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
		
//			if ( firstPass ) 
//			{
//				composerBeckmann.render();
//				firstPass = false;
//			}

		context.getRenderer().clear();
		context.getRenderer().render(scene, camera);
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
//
//		    	  	rs.mouseX = event.getX() - renderingPanel.context.getRenderer().getAbsoluteWidth() / 2 ; 
//		    	  	rs.mouseY = event.getY() - renderingPanel.context.getRenderer().getAbsoluteHeight() / 2;
//		      }
//		});
//	}
	
}
