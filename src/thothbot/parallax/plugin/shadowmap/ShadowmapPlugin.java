/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.plugin.shadowmap;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.renderers.Plugin;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.shaders.DepthRGBAShader;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.Color;
import thothbot.parallax.core.shared.core.Frustum;
import thothbot.parallax.core.shared.core.Matrix4;
import thothbot.parallax.core.shared.core.Projector;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.lights.DirectionalLight;
import thothbot.parallax.core.shared.lights.Light;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.scenes.Scene;

public final class ShadowmapPlugin extends Plugin 
{

	private ShaderMaterial depthMaterial, depthMaterialMorph, depthMaterialSkin;

	private Frustum frustum;
	private Matrix4 projScreenMatrix;

	private Vector3 min;
	private Vector3 max;
	
	private static Projector projector = new Projector();
	
	public ShadowmapPlugin(WebGLRenderer renderer, Scene scene) 
	{
		super(renderer, scene);
		
		this.min = new Vector3();
		this.max = new Vector3();
		
		this.frustum = new Frustum();
		this.projScreenMatrix = new Matrix4();
		
		DepthRGBAShader depthShader = new DepthRGBAShader();

		this.depthMaterial = new ShaderMaterial( depthShader );
		this.depthMaterialMorph = new ShaderMaterial( depthShader );
		this.depthMaterialMorph.setMorphTargets(true);
		this.depthMaterialSkin = new ShaderMaterial( depthShader );
		this.depthMaterialSkin.setSkinning(true);

//		_depthMaterial._shadowPass = true;
//		_depthMaterialMorph._shadowPass = true;
//		_depthMaterialSkin._shadowPass = true;
	}

	@Override
	public TYPE getType() 
	{
		return Plugin.TYPE.PRE_RENDER;
	}

	@Override
	public void render(Camera camera, int currentWidth, int currentHeight) 
	{
		if ( ! ( getRenderer().isShadowMapEnabled() && getRenderer().isShadowMapAutoUpdate() ) ) return;

		WebGLRenderingContext gl = getRenderer().getGL();
		
//		var i, il, j, jl, n,
//
//		shadowMap, shadowMatrix, shadowCamera,
//		program, buffer, material,
//		webglObject, object, light,
//		renderList,

//		fog = null;

		// set GL state for depth map

		gl.clearColor( 1, 1, 1, 1 );
		gl.disable( GLenum.BLEND.getValue() );

		gl.enable( GLenum.CULL_FACE.getValue() );

		if ( getRenderer().isShadowMapCullFrontFaces() ) 
		{
			gl.cullFace( GLenum.FRONT.getValue() );
		} 
		else 
		{
			gl.cullFace( GLenum.BACK.getValue() );
		}

		getRenderer().setDepthTest( true );

		// preprocess lights
		// 	- skip lights that are not casting shadows
		//	- create virtual lights for cascaded shadow maps

		List<Light> sceneLights = getScene().getLights();
		List<Light> lights = new ArrayList<Light>();
		
		for ( int i = 0, il = sceneLights.size(); i < il; i ++ ) 
		{
			Light light = sceneLights.get( i );

			if ( ! light.isCastShadow() ) continue;

			if ( ( light instanceof DirectionalLight ) && ((DirectionalLight)light).shadowCascade ) 
			{
				for ( int n = 0; n < ((DirectionalLight)light).shadowCascadeCount; n ++ ) 
				{
					DirectionalLight virtualLight;

					if ( ((DirectionalLight)light).shadowCascadeArray.get( n ) == null ) 
					{
						virtualLight = createVirtualLight( light, n );
						virtualLight.originalCamera = camera;

						var gyro = new Gyroscope();
						gyro.position = light.shadowCascadeOffset;

						gyro.add( virtualLight );
						gyro.add( virtualLight.target );

						camera.add( gyro );

						light.shadowCascadeArray[ n ] = virtualLight;

						Log.debug( "Shadowmap plugin: Created virtualLight");
					} 
					else 
					{
						virtualLight = light.shadowCascadeArray.get( n );
					}

					updateVirtualLight( light, n );
					lights.add(virtualLight);
				}
			} 
			else 
			{
				lights.add(light);
			}
		}

		// render depth map
		for ( int i = 0, il = lights.size(); i < il; i ++ ) 
		{
			Light light = lights.get( i );

			if ( ! light.shadowMap ) 
			{
				var pars = { minFilter: THREE.LinearFilter, magFilter: THREE.LinearFilter, format: THREE.RGBAFormat };

				light.shadowMap = new THREE.WebGLRenderTarget( light.shadowMapWidth, light.shadowMapHeight, pars );
				light.shadowMapSize = new THREE.Vector2( light.shadowMapWidth, light.shadowMapHeight );

				light.shadowMatrix = new THREE.Matrix4();
			}

			if ( ! light.shadowCamera ) 
			{
				if ( light instanceof SpotLight ) 
				{
					light.shadowCamera = new PerspectiveCamera( light.shadowCameraFov, light.shadowMapWidth / light.shadowMapHeight, light.shadowCameraNear, light.shadowCameraFar );
				} 
				else if ( light instanceof DirectionalLight ) 
				{
					light.shadowCamera = new OrthographicCamera( light.shadowCameraLeft, light.shadowCameraRight, light.shadowCameraTop, light.shadowCameraBottom, light.shadowCameraNear, light.shadowCameraFar );

				} 
				else 
				{
					Log.error( "Shadowmap plugin: Unsupported light class for shadow: " + light.getClass().getName() );
					continue;
				}

				getScene().add( light.shadowCamera );

				if ( getRenderer().isAutoUpdateScene() ) 
					getScene().updateMatrixWorld(false);
			}

			if ( light.shadowCameraVisible && ! light.cameraHelper ) 
			{
				light.cameraHelper = new THREE.CameraHelper( light.shadowCamera );
				light.shadowCamera.add( light.cameraHelper );
			}

			if ( light.isVirtual && virtualLight.originalCamera == camera ) 
			{
				updateShadowCamera( camera, light );
			}

			shadowMap = light.shadowMap;
			shadowMatrix = light.shadowMatrix;
			shadowCamera = light.shadowCamera;

			shadowCamera.position.copy( light.matrixWorld.getPosition() );
			shadowCamera.lookAt( light.target.matrixWorld.getPosition() );
			shadowCamera.updateMatrixWorld();

			shadowCamera.matrixWorldInverse.getInverse( shadowCamera.matrixWorld );

			if ( light.cameraHelper ) 
				light.cameraHelper.lines.visible = light.shadowCameraVisible;
			if ( light.shadowCameraVisible ) 
				light.cameraHelper.update();

			// compute shadow matrix

			shadowMatrix.set( 0.5, 0.0, 0.0, 0.5,
							  0.0, 0.5, 0.0, 0.5,
							  0.0, 0.0, 0.5, 0.5,
							  0.0, 0.0, 0.0, 1.0 );

			shadowMatrix.multiplySelf( shadowCamera.projectionMatrix );
			shadowMatrix.multiplySelf( shadowCamera.matrixWorldInverse );

			// update camera matrices and frustum

			if ( ! shadowCamera._viewMatrixArray ) shadowCamera._viewMatrixArray = new Float32Array( 16 );
			if ( ! shadowCamera._projectionMatrixArray ) shadowCamera._projectionMatrixArray = new Float32Array( 16 );

			shadowCamera.matrixWorldInverse.flattenToArray( shadowCamera._viewMatrixArray );
			shadowCamera.projectionMatrix.flattenToArray( shadowCamera._projectionMatrixArray );

			_projScreenMatrix.multiply( shadowCamera.projectionMatrix, shadowCamera.matrixWorldInverse );
			_frustum.setFromMatrix( _projScreenMatrix );

			// render shadow map

			getRenderer().setRenderTarget( shadowMap );
			getRenderer().clear(false, false, false);

			// set object matrices & frustum culling

			renderList = scene.__webglObjects;

			for ( int j = 0, jl = renderList.length; j < jl; j ++ ) 
			{
				webglObject = renderList[ j ];
				object = webglObject.object;

				webglObject.render = false;

				if ( object.visible && object.castShadow ) 
				{
					if ( ! ( object instanceof Mesh ) || ! ( object.frustumCulled ) || _frustum.contains( object ) ) 
					{
						object._modelViewMatrix.multiply( shadowCamera.matrixWorldInverse, object.matrixWorld );

						webglObject.render = true;
					}
				}
			}

			// render regular objects

			for ( int j = 0, jl = renderList.length; j < jl; j ++ ) 
			{
				webglObject = renderList[ j ];

				if ( webglObject.render ) {

					object = webglObject.object;
					buffer = webglObject.buffer;

					// culling is overriden globally for all objects
					// while rendering depth map

					if ( object.customDepthMaterial ) 
					{
						material = object.customDepthMaterial;
					} 
					else if ( object.geometry.morphTargets.length ) 
					{
						material = _depthMaterialMorph;
					} 
					else if ( object instanceof THREE.SkinnedMesh ) 
					{
						material = _depthMaterialSkin;
					} 
					else 
					{
						material = _depthMaterial;
					}

					if ( buffer instanceof BufferGeometry ) 
					{
						getRenderer().renderBufferDirect( shadowCamera, scene.__lights, fog, material, buffer, object );

					} 
					else 
					{
						getRenderer().renderBuffer( shadowCamera, scene.__lights, fog, material, buffer, object );
					}
				}
			}

			// set matrices and render immediate objects

			renderList = scene.__webglObjectsImmediate;

			for ( int j = 0, jl = renderList.length; j < jl; j ++ ) 
			{
				webglObject = renderList[ j ];
				object = webglObject.object;

				if ( object.visible && object.castShadow ) 
				{
					object._modelViewMatrix.multiply( shadowCamera.matrixWorldInverse, object.matrixWorld );

					_renderer.renderImmediateObject( shadowCamera, scene.__lights, fog, _depthMaterial, object );
				}
			}
		}

		// restore GL state

		Color clearColor = getRenderer().getClearColor(),
		double clearAlpha = getRenderer().getClearAlpha();

		gl.clearColor( clearColor.getR(), clearColor.getG(), clearColor.getB(), clearAlpha );
		gl.enable( GLenum.BLEND.getValue() );

		if ( getRenderer().isShadowMapCullFrontFaces() ) 
		{
			gl.cullFace( GLenum.BACK.getValue() );
		}
	}

	private DirectionalLight createVirtualLight( Light light, int cascade ) 
	{
		DirectionalLight virtualLight = new DirectionalLight();

		virtualLight.isVirtual = true;

		virtualLight.onlyShadow = true;
		virtualLight.castShadow = true;

		virtualLight.shadowCameraNear = light.shadowCameraNear;
		virtualLight.shadowCameraFar = light.shadowCameraFar;

		virtualLight.shadowCameraLeft = light.shadowCameraLeft;
		virtualLight.shadowCameraRight = light.shadowCameraRight;
		virtualLight.shadowCameraBottom = light.shadowCameraBottom;
		virtualLight.shadowCameraTop = light.shadowCameraTop;

		virtualLight.shadowCameraVisible = light.shadowCameraVisible;

		virtualLight.shadowDarkness = light.shadowDarkness;

		virtualLight.shadowBias = light.shadowCascadeBias[ cascade ];
		virtualLight.shadowMapWidth = light.shadowCascadeWidth[ cascade ];
		virtualLight.shadowMapHeight = light.shadowCascadeHeight[ cascade ];

		virtualLight.pointsWorld = [];
		virtualLight.pointsFrustum = [];

		var pointsWorld = virtualLight.pointsWorld,
			pointsFrustum = virtualLight.pointsFrustum;

		for ( int i = 0; i < 8; i ++ ) 
		{
			pointsWorld[ i ] = new THREE.Vector3();
			pointsFrustum[ i ] = new THREE.Vector3();
		}

		var nearZ = light.shadowCascadeNearZ[ cascade ];
		var farZ = light.shadowCascadeFarZ[ cascade ];

		pointsFrustum[ 0 ].set( -1, -1, nearZ );
		pointsFrustum[ 1 ].set(  1, -1, nearZ );
		pointsFrustum[ 2 ].set( -1,  1, nearZ );
		pointsFrustum[ 3 ].set(  1,  1, nearZ );

		pointsFrustum[ 4 ].set( -1, -1, farZ );
		pointsFrustum[ 5 ].set(  1, -1, farZ );
		pointsFrustum[ 6 ].set( -1,  1, farZ );
		pointsFrustum[ 7 ].set(  1,  1, farZ );

		return virtualLight;
	}
	
	/** 
	 * Synchronize virtual light with the original light
	 */
	private void updateVirtualLight( Light light, int cascade ) 
	{
		var virtualLight = light.shadowCascadeArray[ cascade ];

		virtualLight.position.copy( light.position );
		virtualLight.target.position.copy( light.target.position );
		virtualLight.lookAt( virtualLight.target );

		virtualLight.shadowCameraVisible = light.shadowCameraVisible;
		virtualLight.shadowDarkness = light.shadowDarkness;

		virtualLight.shadowBias = light.shadowCascadeBias[ cascade ];

		var nearZ = light.shadowCascadeNearZ[ cascade ];
		var farZ = light.shadowCascadeFarZ[ cascade ];

		var pointsFrustum = virtualLight.pointsFrustum;

		pointsFrustum[ 0 ].z = nearZ;
		pointsFrustum[ 1 ].z = nearZ;
		pointsFrustum[ 2 ].z = nearZ;
		pointsFrustum[ 3 ].z = nearZ;

		pointsFrustum[ 4 ].z = farZ;
		pointsFrustum[ 5 ].z = farZ;
		pointsFrustum[ 6 ].z = farZ;
		pointsFrustum[ 7 ].z = farZ;

	}
	
	/**
	 * Fit shadow camera's ortho frustum to camera frustum
	 */
	private void updateShadowCamera( Camera camera, Light light ) 
	{
		var shadowCamera = light.shadowCamera,
			pointsFrustum = light.pointsFrustum,
			pointsWorld = light.pointsWorld;

		this.min.set( Double.POSITIVE_INFINITY );
		this.max.set( Double.NEGATIVE_INFINITY );

		for ( int i = 0; i < 8; i ++ ) 
		{
			Vector3 p = pointsWorld[ i ];

			p.copy( pointsFrustum[ i ] );
			ShadowmapPlugin.projector.unprojectVector( p, camera );

			shadowCamera.matrixWorldInverse.multiplyVector3( p );

			if ( p.getX() < this.min.getX() ) this.min.setX( p.getX() );
			if ( p.getX() > this.max.getX() ) this.max.setX( p.getX() );

			if ( p.getY() < this.min.getY() ) this.min.setY( p.getY() );
			if ( p.getY() > this.max.getY() ) this.max.setY( p.getY() );

			if ( p.getZ() < this.min.getZ() ) this.min.setZ( p.getZ() );
			if ( p.getZ() > this.max.getZ() ) this.max.setZ( p.getZ() );

		}

		shadowCamera.left = this.min.getX();
		shadowCamera.right = this.max.getX();
		shadowCamera.top = this.max.getY();
		shadowCamera.bottom = this.min.getY();

		shadowCamera.updateProjectionMatrix();
	}
}
