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
import org.parallax3d.parallax.graphics.extras.geometries.PlaneBufferGeometry;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshDepthMaterial;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.renderers.RenderTargetTexture;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.input.TouchMoveHandler;
import org.parallax3d.parallax.loaders.JsonLoader;
import org.parallax3d.parallax.loaders.Loader;
import org.parallax3d.parallax.loaders.ModelLoadHandler;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.system.gl.enums.TextureMagFilter;
import org.parallax3d.parallax.system.gl.enums.TextureMinFilter;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;
import org.parallax3d.parallax.tests.resources.GodRaysCombineShader;
import org.parallax3d.parallax.tests.resources.GodRaysGenerateShader;
import org.parallax3d.parallax.tests.resources.GodraysFakeSunShader;

@ThreejsExample("webgl_postprocessing_godrays")
public final class PostprocessingGodrays extends ParallaxTest implements TouchMoveHandler
{

	private static final String model = "models/obj/tree/tree.js";
	private static final double orbitRadius = 200.0;
	
	private static final int bgColor = 0x000511;
	private static final int sunColor = 0xffee00;

	Scene scene;
	PerspectiveCamera camera;

	int width = 0, height = 0;
	int mouseX;
	int mouseY;
	
	Mesh sphereMesh, quad;
	
	Vector3 sunPosition = new Vector3( 0, 1000, -1000 );
	Vector3 screenSpacePosition = new Vector3();
		
	Scene postprocessingScene;
	OrthographicCamera postprocessingCamera;
	RenderTargetTexture rtTextureColors, rtTextureDepth, rtTextureGodRays1, rtTextureGodRays2;
	
	ShaderMaterial materialGodraysGenerate, materialGodraysCombine, materialGodraysFakeSun;
	
	MeshDepthMaterial materialDepth;

	@Override
	public void onResize(RenderingContext context) {
		width = context.getWidth();
		height = context.getHeight();
	}

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera( 70,
				context.getAspectRation(),
				1, 
				3000 
			);
		camera.getPosition().setZ(200);
		
		materialDepth = new MeshDepthMaterial();

		final MeshBasicMaterial materialScene = new MeshBasicMaterial()
				.setColor( 0x000000 )
				.setShading(Material.SHADING.FLAT);

		// tree

		new JsonLoader(model, new ModelLoadHandler() {

			@Override
			public void onModelLoaded(Loader loader, AbstractGeometry geometry) {
				Mesh treeMesh = new Mesh( geometry, materialScene );
				treeMesh.getPosition().set( 0, -150, -150 );

				treeMesh.getScale().set( 400 );

				treeMesh.setMatrixAutoUpdate(false);
				treeMesh.updateMatrix();

				scene.add( treeMesh );
			}
		});

		// sphere

		SphereGeometry geo = new SphereGeometry( 1, 20, 10 );
		this.sphereMesh = new Mesh( geo, materialScene );
		this.sphereMesh.getScale().set( 20 );

		scene.add( this.sphereMesh );

		//
				
		context.getRenderer().setSortObjects(false);
		context.getRenderer().setAutoClear(false);
		context.getRenderer().setClearColor( bgColor, 1 );
		
		//  Postprocessing
		
		postprocessingScene = new Scene();

		postprocessingCamera = new OrthographicCamera(context.getRenderer().getAbsoluteWidth(), context.getRenderer().getAbsoluteHeight(), -10000, 10000 );
		postprocessingCamera.getPosition().setZ( 100 );

		postprocessingScene.add( postprocessingCamera );

		rtTextureColors = new RenderTargetTexture( context.getRenderer().getAbsoluteWidth(), context.getRenderer().getAbsoluteHeight() );
		rtTextureColors.setMinFilter(TextureMinFilter.LINEAR);
		rtTextureColors.setMagFilter(TextureMagFilter.LINEAR);
		rtTextureColors.setFormat(PixelFormat.RGBA);

		// Switching the depth formats to luminance from rgb doesn't seem to work. I didn't
		// investigate further for now.
		// pars.format = THREE.LuminanceFormat;

		// I would have this quarter size and use it as one of the ping-pong render
		// targets but the aliasing causes some temporal flickering

		rtTextureDepth = new RenderTargetTexture(  context.getRenderer().getAbsoluteWidth(), context.getRenderer().getAbsoluteHeight()  );
		rtTextureDepth.setMinFilter(TextureMinFilter.LINEAR);
		rtTextureDepth.setMagFilter(TextureMagFilter.LINEAR);
		rtTextureDepth.setFormat(PixelFormat.RGBA);
		
		// Aggressive downsize god-ray ping-pong render targets to minimize cost

		int w = context.getRenderer().getAbsoluteWidth() / 4;
		int h = context.getRenderer().getAbsoluteHeight() / 4;
		rtTextureGodRays1 = new RenderTargetTexture( w, h );
		rtTextureGodRays1.setMinFilter(TextureMinFilter.LINEAR);
		rtTextureGodRays1.setMagFilter(TextureMagFilter.LINEAR);
		rtTextureGodRays1.setFormat(PixelFormat.RGBA);
		
		rtTextureGodRays2 = new RenderTargetTexture( w, h );
		rtTextureGodRays2.setMinFilter(TextureMinFilter.LINEAR);
		rtTextureGodRays2.setMagFilter(TextureMagFilter.LINEAR);
		rtTextureGodRays2.setFormat(PixelFormat.RGBA);

		// god-ray shaders
		
		materialGodraysGenerate = new ShaderMaterial ( new GodRaysGenerateShader() );
		materialGodraysCombine = new ShaderMaterial ( new GodRaysCombineShader() );
		materialGodraysCombine.getShader().getUniforms().get("fGodRayIntensity").setValue( 0.75 );

		materialGodraysFakeSun = new ShaderMaterial ( new GodraysFakeSunShader() );

		((Color)materialGodraysFakeSun.getShader().getUniforms().get("bgColor").getValue()).setHex( bgColor );
		((Color)materialGodraysFakeSun.getShader().getUniforms().get("sunColor").getValue()).setHex( sunColor );

		quad = new Mesh(
				new PlaneBufferGeometry( context.getRenderer().getAbsoluteWidth(), context.getRenderer().getAbsoluteHeight() ),
				materialGodraysGenerate
		);
		quad.getPosition().setZ( -9900 );
		postprocessingScene.add( quad );
		context.getRenderer().setClearColor(0x000511);
	}
			
	@Override
	public void onUpdate(RenderingContext context)
	{
		double time = context.getDeltaTime() / 4000.0;

		this.sphereMesh.getPosition().setX( orbitRadius * Math.cos( time ) );
		this.sphereMesh.getPosition().setZ( orbitRadius * Math.sin( time ) - 100.0 );

		camera.getPosition().addX( ( mouseX - camera.getPosition().getX() ) * 0.036 );
		camera.getPosition().addY( ( - ( mouseY ) - camera.getPosition().getY() ) * 0.036 );

		camera.lookAt( scene.getPosition() );
		
		// Find the screenspace position of the sun
		screenSpacePosition.copy( sunPosition ).project( camera );

		screenSpacePosition.setX( ( screenSpacePosition.getX() + 1.0 ) / 2.0 );
		screenSpacePosition.setY( ( screenSpacePosition.getY() + 1.0 ) / 2.0 );
		
		// Give it to the god-ray and sun shaders

		((Vector2)materialGodraysGenerate.getShader().getUniforms().get("vSunPositionScreenSpace").getValue())
			.set( screenSpacePosition.getX(), screenSpacePosition.getY() );
		((Vector2)materialGodraysFakeSun.getShader().getUniforms().get("vSunPositionScreenSpace").getValue())
			.set( screenSpacePosition.getX(), screenSpacePosition.getY() );

		// -- Draw sky and sun --

		// Clear colors and depths, will clear to sky color

		context.getRenderer().clearTarget( rtTextureColors, true, true, false );

		// Sun render. Runs a shader that gives a brightness based on the screen
		// space distance to the sun. Not very efficient, so i make a scissor
		// rectangle around the suns position to avoid rendering surrounding pixels.
		
		int width = context.getRenderer().getAbsoluteWidth(); 
		int height = context.getRenderer().getAbsoluteHeight();

		double sunsqH = 0.74 * height; // 0.74 depends on extent of sun from shader
		double sunsqW = 0.74 * height; // both depend on height because sun is aspect-corrected

		screenSpacePosition.setX( screenSpacePosition.getX() * width );
		screenSpacePosition.setY( screenSpacePosition.getY() * height );

		context.getRenderer().setScissor( (int)(screenSpacePosition.getX() - sunsqW / 2.0), (int)(screenSpacePosition.getY() - sunsqH / 2.0), (int)sunsqW, (int)sunsqH );
		context.getRenderer().enableScissorTest( true );

		materialGodraysFakeSun.getShader().getUniforms().get("fAspect").setValue( (double)width / (double)height );

		postprocessingScene.setOverrideMaterial( materialGodraysFakeSun );
		context.getRenderer().render( postprocessingScene, postprocessingCamera, rtTextureColors );

		context.getRenderer().enableScissorTest( false );
		
		// Colors

		scene.setOverrideMaterial( null );
		context.getRenderer().render( scene, camera, rtTextureColors );

		// Depth

		scene.setOverrideMaterial( materialDepth );
		context.getRenderer().render( scene, camera, rtTextureDepth, true );
		
		// -- Render god-rays --

		// Maximum length of god-rays (in texture space [0,1]X[0,1])

		double filterLen = 1.0;

		// Samples taken by filter

		double TAPS_PER_PASS = 6.0;

		// Pass order could equivalently be 3,2,1 (instead of 1,2,3), which
		// would start with a small filter support and grow to large. however
		// the large-to-small order produces less objectionable aliasing artifacts that
		// appear as a glimmer along the length of the beams

		// pass 1 - render into first ping-pong target

		double pass = 1.0;
		double stepLen = filterLen * Math.pow( TAPS_PER_PASS, -pass );

		materialGodraysGenerate.getShader().getUniforms().get("fStepSize" ).setValue( stepLen );
		materialGodraysGenerate.getShader().getUniforms().get("tInput" ).setValue( rtTextureDepth );
		postprocessingScene.setOverrideMaterial( materialGodraysGenerate );
		
		context.getRenderer().render( postprocessingScene, postprocessingCamera, rtTextureGodRays2 );
		
		// pass 2 - render into second ping-pong target

		pass = 2.0;
		stepLen = filterLen * Math.pow( TAPS_PER_PASS, -pass );

		materialGodraysGenerate.getShader().getUniforms().get("fStepSize" ).setValue( stepLen );
		materialGodraysGenerate.getShader().getUniforms().get("tInput" ).setValue( rtTextureGodRays2 );

		context.getRenderer().render( postprocessingScene, postprocessingCamera, rtTextureGodRays1 );
		
		// pass 3 - 1st RT

		pass = 3.0;
		stepLen = filterLen * Math.pow( TAPS_PER_PASS, -pass );

		materialGodraysGenerate.getShader().getUniforms().get("fStepSize" ).setValue( stepLen );
		materialGodraysGenerate.getShader().getUniforms().get("tInput" ).setValue( rtTextureGodRays1 );

		context.getRenderer().render( postprocessingScene, postprocessingCamera, rtTextureGodRays2 );

		// final pass - composite god-rays onto colors

		materialGodraysCombine.getShader().getUniforms().get("tColors" ).setValue( rtTextureColors );
		materialGodraysCombine.getShader().getUniforms().get("tGodRays" ).setValue( rtTextureGodRays2 );

		postprocessingScene.setOverrideMaterial( materialGodraysCombine );

		context.getRenderer().render( postprocessingScene, postprocessingCamera );
		postprocessingScene.setOverrideMaterial( null );


	}

	@Override
	public void onTouchMove(int screenX, int screenY, int pointer) {
		mouseX = (screenX - width / 2 );
		mouseY = (screenY - height / 2);
	}

	@Override
	public String getName() {
		return "God-rays";
	}

	@Override
	public String getDescription() {
		return "Drag mouse to move.";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}

//	@Override
//	protected void loadRenderingPanelAttributes(RenderingPanel renderingPanel) 
//	{
//		super.loadRenderingPanelAttributes(renderingPanel);
//		renderingPanel.getCanvas3dAttributes().setAntialiasEnable(false);
//	}
	
}
