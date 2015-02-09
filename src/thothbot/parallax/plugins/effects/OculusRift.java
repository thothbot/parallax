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

package thothbot.parallax.plugins.effects;

import java.util.List;

import thothbot.parallax.core.client.events.ViewportResizeEvent;
import thothbot.parallax.core.client.events.ViewportResizeHandler;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.TextureMagFilter;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.cameras.OrthographicCamera;
import thothbot.parallax.core.shared.cameras.PerspectiveCamera;
import thothbot.parallax.core.shared.geometries.PlaneBufferGeometry;
import thothbot.parallax.core.shared.lights.Light;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Mathematics;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.math.Vector4;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.scenes.Scene;
import thothbot.parallax.plugins.effects.shaders.OculusRiftShader;

public class OculusRift extends Effect {
	
	private static class HMD {
		// DK1
		
//		public int hResolution = 1280;
//		public int vResolution = 800;
//		hScreenSize: 0.14976,
//		vScreenSize: 0.0936,
//		interpupillaryDistance: 0.064,
//		lensSeparationDistance: 0.064,
//		eyeToScreenDistance: 0.041,
//		distortionK : [1.0, 0.22, 0.24, 0.0],
//		chromaAbParameter: [ 0.996, -0.004, 1.014, 0.0]
		
		// DK2
		public int hResolution = 1920;
		public int vResolution = 1080;
		public double hScreenSize = 0.12576;
		public double vScreenSize = 0.07074;
		public double interpupillaryDistance = 0.0635;
		public double lensSeparationDistance = 0.0635;
		public double eyeToScreenDistance = 0.041;
		public double[] distortionK  = new double[]{1.0, 0.22, 0.24, 0.0};
		public double[] chromaAbParameter = new double[]{ 0.996, -0.004, 1.014, 0.0};
	};
	
	public static class Params {
		public Matrix4 proj;
		public Matrix4 tranform;
		public int[] viewport;
		public Vector2 lensCenter;
	};

	// worldFactor indicates how many units is 1 meter
	private double worldFactor = 1.0;
	private OculusRift.HMD hdm;
	
	// Perspective camera
	PerspectiveCamera pCamera = new PerspectiveCamera();
	Vector3 target = new Vector3();

	// Orthographic camera
	OrthographicCamera oCamera = new OrthographicCamera( 2, 2, 1, 1000 );

	Color emptyColor = new Color(0x000000);

	// Render target
	RenderTargetTexture renderTarget;
	ShaderMaterial RTMaterial;

	// Final scene
	Scene finalScene = new Scene();
	
    double distScale = 1.0;
    
    Params left = new Params(), right = new Params();
    
    boolean _oldAutoClear;

	public OculusRift(WebGLRenderer renderer, Scene scene) {
		super(renderer, scene);
		
		_oldAutoClear = renderer.isAutoClear();
		renderer.setAutoClear(false);

		pCamera.setMatrixAutoUpdate(false);
		
		oCamera.getPosition().setZ( 1 );
		
		oCamera.addViewportResizeHandler(new ViewportResizeHandler() {
			
			@Override
			public void onResize(ViewportResizeEvent event) {

				oCamera.setSize( 2, 2 );
				
			}
		});
				
		RTMaterial = new ShaderMaterial(new OculusRiftShader());
		setHMD(new OculusRift.HMD());

		Mesh mesh = new Mesh( new PlaneBufferGeometry( 2, 2 ), RTMaterial );
		
		finalScene.add( oCamera );
		finalScene.add( mesh );		
	}
	
	@Override
	public void onResize(ViewportResizeEvent event) 
	{
		int width = event.getRenderer().getAbsoluteWidth();
		int height = event.getRenderer().getAbsoluteHeight();
		
		left.viewport = new int[]{width/2 - hdm.hResolution/2, height/2 - hdm.vResolution/2, hdm.hResolution/2, hdm.vResolution};
		right.viewport = new int[]{width/2, height/2 - hdm.vResolution/2, hdm.hResolution/2, hdm.vResolution};

		initRenderTargets(width, height);
	}
	
	private void initRenderTargets(int width, int height ) 
	{
		if ( renderTarget != null ) 
			renderTarget.deallocate(this.renderer.getGL());

		renderTarget = new RenderTargetTexture( width, height );
		renderTarget.setMinFilter(TextureMinFilter.LINEAR);
		renderTarget.setMagFilter(TextureMagFilter.NEAREST);
		renderTarget.setFormat(PixelFormat.RGBA);
		
		RTMaterial.getShader().getUniforms().get( "texid" ).setValue( renderTarget );
	}
	
	public OculusRift.HMD getHDM() {
		return this.hdm;
	}
		
	public void setHMD(OculusRift.HMD hdm) {
	
		this.hdm = hdm;

		// Compute aspect ratio and FOV
		double aspect = (double)hdm.hResolution / (double)(2.0 * hdm.vResolution);

		// Fov is normally computed with:
		//   THREE.Math.radToDeg( 2*Math.atan2(HMD.vScreenSize,2*HMD.eyeToScreenDistance) );
		// But with lens distortion it is increased (see Oculus SDK Documentation)
		double r = -1.0 - (4.0 * (hdm.hScreenSize/4.0 - hdm.lensSeparationDistance/2.0) / hdm.hScreenSize);
		distScale = (hdm.distortionK[0] + hdm.distortionK[1] * Math.pow(r,2) + hdm.distortionK[2] * Math.pow(r,4) + hdm.distortionK[3] * Math.pow(r,6));
		double fov = Mathematics.radToDeg(2.0 * Math.atan2(hdm.vScreenSize * distScale, 2.0 * hdm.eyeToScreenDistance));

		// Compute camera projection matrices
		Matrix4 proj = (new Matrix4()).makePerspective( fov, aspect, 0.3, 10000 );
		double h = 4.0 * (hdm.hScreenSize/4.0 - hdm.interpupillaryDistance/2.0) / hdm.hScreenSize;
		left.proj = ((new Matrix4()).makeTranslation( h, 0.0, 0.0 )).multiply(proj);
		right.proj = ((new Matrix4()).makeTranslation( -h, 0.0, 0.0 )).multiply(proj);

		// Compute camera transformation matrices
		left.tranform = (new Matrix4()).makeTranslation( -worldFactor * hdm.interpupillaryDistance/2.0, 0.0, 0.0 );
		right.tranform = (new Matrix4()).makeTranslation( worldFactor * hdm.interpupillaryDistance/2.0, 0.0, 0.0 );

		// Compute Viewport
//		left.viewport = new int[]{0, 0, hdm.hResolution/2, hdm.vResolution};
//		right.viewport = new int[]{hdm.hResolution/2, 0, hdm.hResolution/2, hdm.vResolution};

		int width = renderer.getAbsoluteWidth();
		int height = renderer.getAbsoluteHeight();
		left.viewport = new int[]{width/2 - hdm.hResolution/2, height/2 - hdm.vResolution/2, hdm.hResolution/2, hdm.vResolution};
		right.viewport = new int[]{width/2, height/2 - hdm.vResolution/2, hdm.hResolution/2, hdm.vResolution};

		// Distortion shader parameters
		double lensShift = 4.0 * (hdm.hScreenSize/4.0 - hdm.lensSeparationDistance/2.0) / hdm.hScreenSize;
		left.lensCenter = new Vector2(lensShift, 0.0);
		right.lensCenter = new Vector2(-lensShift, 0.0);

		// Create render target
		initRenderTargets((int)(hdm.hResolution * distScale / 2.0), (int)(hdm.vResolution * distScale));

		RTMaterial.getShader().getUniforms().get( "hmdWarpParam" ).setValue( new Vector4(hdm.distortionK[0], hdm.distortionK[1], hdm.distortionK[2], hdm.distortionK[3]) );
		RTMaterial.getShader().getUniforms().get( "chromAbParam" ).setValue( new Vector4(hdm.chromaAbParameter[0], hdm.chromaAbParameter[1], hdm.chromaAbParameter[2], hdm.chromaAbParameter[3]) );
		RTMaterial.getShader().getUniforms().get( "scaleIn" ).setValue( new Vector2(1.0,1.0/aspect) );
		RTMaterial.getShader().getUniforms().get( "scale" ).setValue( new Vector2(1.0/distScale, 1.0*aspect/distScale) );
	}

	@Override
	public void render(Camera sceneCamera, List<Light> lights, int currentWidth,	int currentHeight) 
	{
		if(!(sceneCamera instanceof PerspectiveCamera))
			return;
		
		PerspectiveCamera camera = (PerspectiveCamera)sceneCamera;
		
		if ( camera.getParent() == null ) 
			camera.updateMatrixWorld(false);
		
		Color cc = renderer.getClearColor().clone();

		// Clear
		renderer.setClearColor(emptyColor);
		renderer.clear();
		renderer.setClearColor(cc);

		// camera parameters
		if (camera.isMatrixAutoUpdate()) 
			camera.updateMatrix();

		// Render left

		pCamera.getProjectionMatrix().copy(left.proj);

		pCamera.getMatrix().copy(camera.getMatrix()).multiply(left.tranform);
		pCamera.setMatrixWorldNeedsUpdate(true);

		renderer.setViewport(left.viewport[0], left.viewport[1], left.viewport[2], left.viewport[3]);

		RTMaterial.getShader().getUniforms().get( "lensCenter" ).setValue(left.lensCenter);
		renderer.render( scene, pCamera, renderTarget, true );

		renderer.render( finalScene, oCamera );

		// Render right

		pCamera.getProjectionMatrix().copy(right.proj);

		pCamera.getMatrix().copy(camera.getMatrix()).multiply(right.tranform);
		pCamera.setMatrixWorldNeedsUpdate(true);

		renderer.setViewport(right.viewport[0], right.viewport[1], right.viewport[2], right.viewport[3]);

		RTMaterial.getShader().getUniforms().get( "lensCenter" ).setValue(right.lensCenter);

		renderer.render( scene, pCamera, renderTarget, true );
		renderer.render( finalScene, oCamera );
	}
	
	@Override
	public void deallocate() {
		super.deallocate();

		int _width = renderer.getAbsoluteWidth();
		int _height = renderer.getAbsoluteHeight();
		
		renderer.setScissor( 0, 0, _width, _height );
		renderer.setViewport( 0, 0, _width, _height );
		
		renderer.setAutoClear(_oldAutoClear);
		
		renderTarget.deallocate(this.renderer.getGL());

		RTMaterial.deallocate(renderer);
	}
}
