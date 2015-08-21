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
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.cameras.PerspectiveCamera;
import thothbot.parallax.core.shared.lights.Light;
import thothbot.parallax.core.shared.math.Mathematics;
import thothbot.parallax.core.shared.math.Quaternion;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.scenes.Scene;

public class Stereo extends Effect {
	
	private double eyeSeparation = 3.0;
	// Distance to the non-parallax or projection plane
	private double focalLength = 15.0;
	
	// internals

	Vector3 _position = new Vector3();
	Quaternion _quaternion = new Quaternion();
	Vector3 _scale = new Vector3();

	PerspectiveCamera _cameraL = new PerspectiveCamera();
	PerspectiveCamera _cameraR = new PerspectiveCamera();

	double _fov;
	double _outer, _inner, _top, _bottom;
	double _ndfl, _halfFocalWidth, _halfFocalHeight;
	double _innerFactor, _outerFactor;
	
	boolean _oldAutoClear;

	public Stereo(WebGLRenderer renderer, Scene scene) {
		super(renderer, scene);
		
		_oldAutoClear = renderer.isAutoClear();
		renderer.setAutoClear(false);
	}
	
	@Override
	public void onResize(ViewportResizeEvent event) 
	{
		
	}

	@Override
	public void render(Camera sceneCamera, List<Light> lights, int currentWidth, int currentHeight) 
	{
		if(!(sceneCamera instanceof PerspectiveCamera))
			return;
		
		PerspectiveCamera camera = (PerspectiveCamera)sceneCamera;
		
		scene.updateMatrixWorld(false);

		if ( camera.getParent() == null ) 
			camera.updateMatrixWorld(false);
	
		camera.getMatrixWorld().decompose( _position, _quaternion, _scale );

		// Effective fov of the camera

		_fov = Mathematics.radToDeg( 2.0 * Math.atan( Math.tan( Mathematics.degToRad( camera.getFov() ) * 0.5 ) / camera.zoom ) );

		_ndfl = camera.getNear() / this.focalLength;
		_halfFocalHeight = Math.tan( Mathematics.degToRad( _fov ) * 0.5 ) * this.focalLength;
		_halfFocalWidth = _halfFocalHeight * 0.5 * camera.getAspect();

		_top = _halfFocalHeight * _ndfl;
		_bottom = -_top;
		_innerFactor = ( _halfFocalWidth + this.eyeSeparation / 2.0 ) / ( _halfFocalWidth * 2.0 );
		_outerFactor = 1.0 - _innerFactor;

		_outer = _halfFocalWidth * 2.0 * _ndfl * _outerFactor;
		_inner = _halfFocalWidth * 2.0 * _ndfl * _innerFactor;

		// left

		_cameraL.getProjectionMatrix().makeFrustum(
			-_outer,
			_inner,
			_bottom,
			_top,
			camera.getNear(),
			camera.getFar()
		);

		_cameraL.getPosition().copy( _position );
		_cameraL.getQuaternion().copy( _quaternion );
		_cameraL.translateX( - this.eyeSeparation / 2.0 );

		// right

		_cameraR.getProjectionMatrix().makeFrustum(
			-_inner,
			_outer,
			_bottom,
			_top,
			camera.getNear(),
			camera.getFar()
		);

		_cameraR.getPosition().copy( _position );
		_cameraR.getQuaternion().copy( _quaternion );
		_cameraR.translateX( this.eyeSeparation / 2.0 );

		//

		renderer.clear();
		renderer.enableScissorTest( true );
		
		int _width = renderer.getAbsoluteWidth() / 2;
		int _height = renderer.getAbsoluteHeight();

		renderer.setScissor( 0, 0, _width, _height );
		renderer.setViewport( 0, 0, _width, _height );
		renderer.render( scene, _cameraL );

		renderer.setScissor( _width, 0, _width, _height );
		renderer.setViewport( _width, 0, _width, _height );
		renderer.render( scene, _cameraR );

		renderer.enableScissorTest( false );

	}
	
	@Override
	public void deallocate() {
		super.deallocate();

		int _width = renderer.getAbsoluteWidth();
		int _height = renderer.getAbsoluteHeight();
		
		renderer.setScissor( 0, 0, _width, _height );
		renderer.setViewport( 0, 0, _width, _height );
		
		renderer.setAutoClear(_oldAutoClear);
	}

}
