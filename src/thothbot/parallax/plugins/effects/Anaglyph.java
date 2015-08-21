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
import thothbot.parallax.core.shared.math.Mathematics;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.scenes.Scene;
import thothbot.parallax.plugins.effects.shaders.AnaglyphShader;

public class Anaglyph extends Effect
{
	private Matrix4 eyeRight = new Matrix4();
	private Matrix4 eyeLeft = new Matrix4();
	private double focalLength = 125;
	private double _aspect, _near, _far, _fov;

	private PerspectiveCamera _cameraL = new PerspectiveCamera();
	private PerspectiveCamera _cameraR = new PerspectiveCamera();

	private OrthographicCamera _camera = new OrthographicCamera( 2, 2, 0, 1 );

	private Scene _scene = new Scene();
	
	private RenderTargetTexture _renderTargetL;
	private RenderTargetTexture _renderTargetR;
	
	private ShaderMaterial _material;
		
	public Anaglyph(WebGLRenderer renderer, Scene scene) 
	{
		super(renderer, scene);
				
		_cameraL.setMatrixAutoUpdate(false);
		_cameraR.setMatrixAutoUpdate(false);
		
		_camera.addViewportResizeHandler(new ViewportResizeHandler() {
			
			@Override
			public void onResize(ViewportResizeEvent event) {

				_camera.setSize( 2, 2 );
				
			}
		});

		_material = new ShaderMaterial(new AnaglyphShader());
		initRenderTargets(renderer.getAbsoluteWidth(), renderer.getAbsoluteHeight());
 
		Mesh mesh = new Mesh( new PlaneBufferGeometry( 2, 2 ), _material );
		_scene.add( mesh );
	}
	
	@Override
	public void onResize(ViewportResizeEvent event) 
	{
		int width = event.getRenderer().getAbsoluteWidth();
		int height = event.getRenderer().getAbsoluteHeight();

		initRenderTargets(width, height);
	}
			
	private void initRenderTargets(int width, int height ) 
	{
		if ( _renderTargetL != null ) 
			_renderTargetL.deallocate(this.renderer.getGL());
		if ( _renderTargetR != null ) 
			_renderTargetR.deallocate(this.renderer.getGL());

		_renderTargetL = new RenderTargetTexture( width, height );
		_renderTargetL.setMinFilter(TextureMinFilter.LINEAR);
		_renderTargetL.setMagFilter(TextureMagFilter.NEAREST);
		_renderTargetL.setFormat(PixelFormat.RGBA);
		
		_renderTargetR = new RenderTargetTexture( width, height );
		_renderTargetR.setMinFilter(TextureMinFilter.LINEAR);
		_renderTargetR.setMagFilter(TextureMagFilter.NEAREST);
		_renderTargetR.setFormat(PixelFormat.RGBA);

		_material.getShader().getUniforms().get( "mapLeft" ).setValue( _renderTargetL );
		_material.getShader().getUniforms().get( "mapRight").setValue( _renderTargetR );
	}

	/*
	 * Renderer now uses an asymmetric perspective projection
	 * (http://paulbourke.net/miscellaneous/stereographics/stereorender/).
	 *
	 * Each camera is offset by the eye seperation and its projection matrix is
	 * also skewed asymetrically back to converge on the same projection plane.
	 * Added a focal length parameter to, this is where the parallax is equal to 0.
	 */

	@Override
	public void render( Camera sceneCamera, List<Light> lights, int currentWidth, int currentHeight )
	{
		if(!(sceneCamera instanceof PerspectiveCamera))
			return;
		
		PerspectiveCamera camera = (PerspectiveCamera)sceneCamera;

		scene.updateMatrixWorld(false);
		
		if ( camera.getParent() == null ) 
			camera.updateMatrixWorld(false);

		boolean hasCameraChanged = ( _aspect != camera.getAspect() ) || ( _near != camera.getNear() ) || ( _far != camera.getFar() ) || ( _fov != camera.getFov() );

		if ( hasCameraChanged ) 
		{
			_aspect = camera.getAspect();
			_near = camera.getNear();
			_far = camera.getFar();
			_fov = camera.getFov();

			Matrix4 projectionMatrix = camera.getProjectionMatrix().clone();
			double eyeSep = focalLength / 30.0 * 0.5;
			double eyeSepOnProjection = eyeSep * _near / focalLength;
			double ymax = _near * Math.tan( Mathematics.degToRad( _fov * 0.5 ) );
			double xmin, xmax;

			// translate xOffset

			eyeRight.getArray().set(12, eyeSep);
			eyeLeft.getArray().set(12, -eyeSep);

			// for left eye

			xmin = -ymax * _aspect + eyeSepOnProjection;
			xmax = ymax * _aspect + eyeSepOnProjection;

			projectionMatrix.getArray().set(0, 2.0 * _near / ( xmax - xmin ));
			projectionMatrix.getArray().set(8, ( xmax + xmin ) / ( xmax - xmin ));

			_cameraL.getProjectionMatrix().copy( projectionMatrix );

			// for right eye

			xmin = -ymax * _aspect - eyeSepOnProjection;
			xmax = ymax * _aspect - eyeSepOnProjection;

			projectionMatrix.getArray().set(0, 2.0 * _near / ( xmax - xmin ));
			projectionMatrix.getArray().set(8, ( xmax + xmin ) / ( xmax - xmin ));

			_cameraR.getProjectionMatrix().copy( projectionMatrix );

		}

		_cameraL.getMatrixWorld().copy( camera.getMatrixWorld() ).multiply( eyeLeft );
		_cameraL.getPosition().copy( camera.getPosition() );
		_cameraL.setNear( camera.getNear() );
		_cameraL.setFar( camera.getFar() );

		renderer.render( scene, _cameraL, _renderTargetL, true );

		_cameraR.getMatrixWorld().copy( camera.getMatrixWorld() ).multiply( eyeRight );
		_cameraR.getPosition().copy( camera.getPosition() );
		_cameraR.setNear( camera.getNear() );
		_cameraR.setFar( camera.getFar() );

		renderer.render( scene, _cameraR, _renderTargetR, true );

		renderer.render( _scene, _camera );
	}
	
	@Override
	public void deallocate() {
		super.deallocate();

		_renderTargetL.deallocate(this.renderer.getGL());
		_renderTargetR.deallocate(this.renderer.getGL());

		_material.deallocate(renderer);
	}
}
