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

package thothbot.parallax.core.client.renderers;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.TextureMagFilter;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
import thothbot.parallax.core.client.shaders.DepthRGBAShader;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.cameras.OrthographicCamera;
import thothbot.parallax.core.shared.cameras.PerspectiveCamera;
import thothbot.parallax.core.shared.core.Color;
import thothbot.parallax.core.shared.core.Frustum;
import thothbot.parallax.core.shared.core.GeometryBuffer;
import thothbot.parallax.core.shared.core.Gyroscope;
import thothbot.parallax.core.shared.core.Matrix4;
import thothbot.parallax.core.shared.core.Projector;
import thothbot.parallax.core.shared.core.Vector2;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.helpers.CameraHelper;
import thothbot.parallax.core.shared.lights.AbstractShadowLight;
import thothbot.parallax.core.shared.lights.DirectionalLight;
import thothbot.parallax.core.shared.lights.Light;
import thothbot.parallax.core.shared.lights.SpotLight;
import thothbot.parallax.core.shared.lights.VirtualLight;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.objects.GeometryObject;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.objects.SkinnedMesh;
import thothbot.parallax.core.shared.objects.WebGLObject;
import thothbot.parallax.core.shared.scenes.Scene;

public final class ShadowMap extends Plugin 
{

	private ShaderMaterial depthMaterial, depthMaterialMorph, depthMaterialSkin, depthMaterialMorphSkin;

	private Frustum frustum;
	private Matrix4 projScreenMatrix;

	private Vector3 min;
	private Vector3 max;
	
	private static Projector projector = new Projector();
	
	public ShadowMap(WebGLRenderer renderer, Scene scene) 
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
		this.depthMaterialMorphSkin = new ShaderMaterial( depthShader );
		this.depthMaterialMorphSkin.setMorphTargets(true);
		this.depthMaterialMorphSkin.setSkinning(true);

		this.depthMaterial.setShadowPass(true);
		this.depthMaterialMorph.setShadowPass(true);
		this.depthMaterialSkin.setShadowPass(true);
		this.depthMaterialMorphSkin.setShadowPass(true);
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
		
		// set GL state for depth map

		gl.clearColor( 1, 1, 1, 1 );
		gl.disable( GLenum.BLEND.getValue() );

		gl.enable( GLenum.CULL_FACE.getValue() );
		gl.frontFace( GLenum.CCW.getValue() );

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
		List<AbstractShadowLight> lights = new ArrayList<AbstractShadowLight>();
		
		for ( int i = 0, il = sceneLights.size(); i < il; i ++ ) 
		{
			Light light = sceneLights.get( i );

			if ( ! light.isCastShadow() ) continue;

			if ( ( light instanceof DirectionalLight ) && ((DirectionalLight)light).isShadowCascade() ) 
			{
				DirectionalLight dirLight = (DirectionalLight)light;

				for ( int n = 0; n < dirLight.getShadowCascadeCount(); n ++ ) 
				{
					VirtualLight virtualLight;
					List<VirtualLight> shadowCascadeArray = dirLight.getShadowCascadeArray();

					if ( shadowCascadeArray.size() <= n || shadowCascadeArray.get( n ) == null ) 
					{
						virtualLight = createVirtualLight( dirLight, n );
						virtualLight.setOriginalCamera( camera );

						Gyroscope gyro = new Gyroscope();
						gyro.setPosition( dirLight.getShadowCascadeOffset() );

						gyro.add( virtualLight );
						gyro.add( virtualLight.getTarget() );

						camera.add( gyro );

						shadowCascadeArray.add( n, virtualLight );

						Log.debug( "Shadowmap plugin: Created virtualLight");
					} 
					else 
					{
						virtualLight = (VirtualLight) shadowCascadeArray.get( n );
					}

					updateVirtualLight( dirLight, n );
					lights.add(virtualLight);
				}
			} 
			else 
			{
				lights.add((AbstractShadowLight) light);
			}
		}

		// render depth map
		for ( int i = 0, il = lights.size(); i < il; i ++ ) 
		{
			AbstractShadowLight light = lights.get( i );

			if ( light.getShadowMap() == null ) 
			{
				RenderTargetTexture map = new RenderTargetTexture(light.getShadowMapWidth(), light.getShadowMapHeight());
				map.setMinFilter(TextureMinFilter.LINEAR);
				map.setMagFilter(TextureMagFilter.LINEAR);
				map.setFormat(PixelFormat.RGBA);
				light.setShadowMap(map);
				
				light.setShadowMapSize( new Vector2( light.getShadowMapWidth(), light.getShadowMapHeight() ) );
			}

			if ( light.getShadowCamera() == null ) 
			{
				if ( light instanceof SpotLight )
				{
					light.setShadowCamera(new PerspectiveCamera( 
							((SpotLight)light).getShadowCameraFov(), 
							light.getShadowMapWidth() / light.getShadowMapHeight(), 
							light.getShadowCameraNear(), 
							light.getShadowCameraFar() ));
				} 
				else if ( light instanceof DirectionalLight ) 
				{
					light.setShadowCamera(new OrthographicCamera( 
							((DirectionalLight) light).getShadowCameraLeft(), 
							((DirectionalLight) light).getShadowCameraRight(), 
							((DirectionalLight) light).getShadowCameraTop(), 
							((DirectionalLight) light).getShadowCameraBottom(), 
							light.getShadowCameraNear(), 
							light.getShadowCameraFar() ));
				} 
				else 
				{
					Log.error( "Shadowmap plugin: Unsupported light class for shadow: " + light.getClass().getName() );
					continue;
				}

				getScene().add( light.getShadowCamera() );

				if ( getRenderer().isAutoUpdateScene() ) 
					getScene().updateMatrixWorld(false);
			}

			if ( light.isShadowCameraVisible() && light.getCameraHelper() == null ) 
			{
				light.setCameraHelper( new CameraHelper( light.getShadowCamera() ));
				light.getShadowCamera().add( light.getCameraHelper() );
			}

			if ( light instanceof VirtualLight && ((VirtualLight)light).getOriginalCamera() == camera ) 
			{
				updateShadowCamera( camera, (VirtualLight)light );
			}

			RenderTargetTexture shadowMap = light.getShadowMap();
			Matrix4 shadowMatrix = light.getShadowMatrix();
			Camera shadowCamera = light.getShadowCamera();

			shadowCamera.getPosition().copy( light.getMatrixWorld().getPosition() );
			shadowCamera.lookAt( light.getTarget().getMatrixWorld().getPosition() );
			shadowCamera.updateMatrixWorld(false);

			shadowCamera.getMatrixWorldInverse().getInverse( shadowCamera.getMatrixWorld() );

			if ( light.getCameraHelper() != null ) 
				light.getCameraHelper().setVisible( light.isShadowCameraVisible() );
			if ( light.isShadowCameraVisible() ) 
				light.getCameraHelper().update();

			// compute shadow matrix

			shadowMatrix.set( 0.5, 0.0, 0.0, 0.5,
							  0.0, 0.5, 0.0, 0.5,
							  0.0, 0.0, 0.5, 0.5,
							  0.0, 0.0, 0.0, 1.0 );

			shadowMatrix.multiply( shadowCamera.getProjectionMatrix() );
			shadowMatrix.multiply( shadowCamera.getMatrixWorldInverse() );

			// update camera matrices and frustum

			if ( shadowCamera._viewMatrixArray == null )
				shadowCamera._viewMatrixArray = Float32Array.create( 16 );
			if ( shadowCamera._projectionMatrixArray == null ) 
				shadowCamera._projectionMatrixArray = Float32Array.create( 16 );

			shadowCamera.getMatrixWorldInverse().flattenToArray( shadowCamera._viewMatrixArray );
			shadowCamera.getProjectionMatrix().flattenToArray( shadowCamera._projectionMatrixArray );

			this.projScreenMatrix.multiply( shadowCamera.getProjectionMatrix(), shadowCamera.getMatrixWorldInverse() );
			this.frustum.setFromMatrix( this.projScreenMatrix );

			// render shadow map

			getRenderer().setRenderTarget( shadowMap );
			getRenderer().clear(false, false, false);

			// set object matrices & frustum culling

			List<WebGLObject> renderList = getScene().__webglObjects;

			for ( int j = 0, jl = renderList.size(); j < jl; j ++ ) 
			{
				WebGLObject webglObject = renderList.get( j );
				GeometryObject object = webglObject.object;

				webglObject.render = false;

				if ( object.isVisible() && object.isCastShadow() ) 
				{
					if ( ! ( object instanceof Mesh ) || ! ( object.isFrustumCulled() ) || this.frustum.contains( object ) ) 
					{
						object._modelViewMatrix.multiply( shadowCamera.getMatrixWorldInverse(), object.getMatrixWorld() );

						webglObject.render = true;
					}
				}
			}

			// render regular objects

			for ( int j = 0, jl = renderList.size(); j < jl; j ++ ) 
			{
				WebGLObject webglObject = renderList.get( j );

				if ( webglObject.render ) 
				{
					GeometryObject object = webglObject.object;
					GeometryBuffer buffer = webglObject.buffer;
					Material material;

					// culling is overriden globally for all objects
					// while rendering depth map

					if ( object.getCustomDepthMaterial() != null ) 
					{
						material = object.getCustomDepthMaterial();
					} 
					else if ( object instanceof SkinnedMesh ) 
					{
						material = object.getGeometry().getMorphTargets().size() > 0 ? this.depthMaterialMorphSkin : this.depthMaterialSkin;
					}
					else if ( object.getGeometry().getMorphTargets().size() > 0 ) 
					{
						material = this.depthMaterialMorph;
					}
					else 
					{
						material = this.depthMaterial;
					}

//					if ( buffer instanceof BufferGeometry ) 
//					{
//						getRenderer().renderBufferDirect( shadowCamera, scene.__lights, fog, material, buffer, object );
//
//					} 
//					else 
//					{
						getRenderer().renderBuffer( shadowCamera, getScene().getLights(), null, material, buffer, object );
//					}
				}
			}

			// set matrices and render immediate objects

//			renderList = scene.__webglObjectsImmediate;
//
//			for ( int j = 0, jl = renderList.length; j < jl; j ++ ) 
//			{
//				webglObject = renderList[ j ];
//				object = webglObject.object;
//
//				if ( object.visible && object.castShadow ) 
//				{
//					object._modelViewMatrix.multiply( shadowCamera.matrixWorldInverse, object.matrixWorld );
//
//					_renderer.renderImmediateObject( shadowCamera, scene.__lights, fog, _depthMaterial, object );
//				}
//			}
		}

		// restore GL state

		Color clearColor = getRenderer().getClearColor();
		double clearAlpha = getRenderer().getClearAlpha();

		gl.clearColor( clearColor.getR(), clearColor.getG(), clearColor.getB(), clearAlpha );
		gl.enable( GLenum.BLEND.getValue() );

		if ( getRenderer().isShadowMapCullFrontFaces() ) 
		{
			gl.cullFace( GLenum.BACK.getValue() );
		}
	}

	private VirtualLight createVirtualLight( DirectionalLight light, int cascade ) 
	{
		VirtualLight virtualLight = new VirtualLight(light.getColor().getHex());

		virtualLight.setOnlyShadow(true);
		virtualLight.setCastShadow(true);

		virtualLight.setShadowCameraNear( light.getShadowCameraNear() );
		virtualLight.setShadowCameraFar( light.getShadowCameraFar() );

		virtualLight.setShadowCameraLeft( light.getShadowCameraLeft() );
		virtualLight.setShadowCameraRight( light.getShadowCameraRight() );
		virtualLight.setShadowCameraBottom( light.getShadowCameraBottom() );
		virtualLight.setShadowCameraTop( light.getShadowCameraTop() );

		virtualLight.setShadowCameraVisible( light.isShadowCameraVisible() );

		virtualLight.setShadowDarkness( light.getShadowDarkness() );

		virtualLight.setShadowBias( light.getShadowCascadeBias()[ cascade ] );
		virtualLight.setShadowMapWidth( light.getShadowCascadeWidth()[ cascade ] );
		virtualLight.setShadowMapHeight( light.getShadowCascadeHeight()[ cascade ] );

		double nearZ = light.getShadowCascadeNearZ()[ cascade ];
		double farZ = light.getShadowCascadeFarZ()[ cascade ];

		List<Vector3> pointsFrustum = virtualLight.getPointsFrustum();
		
		pointsFrustum.get( 0 ).set( -1, -1, nearZ );
		pointsFrustum.get( 1 ).set(  1, -1, nearZ );
		pointsFrustum.get( 2 ).set( -1,  1, nearZ );
		pointsFrustum.get( 3 ).set(  1,  1, nearZ );

		pointsFrustum.get( 4 ).set( -1, -1, farZ );
		pointsFrustum.get( 5 ).set(  1, -1, farZ );
		pointsFrustum.get( 6 ).set( -1,  1, farZ );
		pointsFrustum.get( 7 ).set(  1,  1, farZ );

		return virtualLight;
	}
	
	/** 
	 * Synchronize virtual light with the original light
	 */
	private void updateVirtualLight( DirectionalLight light, int cascade ) 
	{
		VirtualLight virtualLight = light.getShadowCascadeArray().get( cascade );

		virtualLight.getPosition().copy( light.getPosition() );
		virtualLight.getTarget().getPosition().copy( light.getTarget().getPosition() );
		virtualLight.lookAt( virtualLight.getTarget().getPosition() );

		virtualLight.setShadowCameraVisible( light.isShadowCameraVisible() );
		virtualLight.setShadowDarkness( light.getShadowDarkness());

		virtualLight.setShadowBias( light.getShadowCascadeBias()[ cascade ] );

		double nearZ = light.getShadowCascadeNearZ()[ cascade ];
		double farZ = light.getShadowCascadeFarZ()[ cascade ];

		List<Vector3> pointsFrustum = virtualLight.getPointsFrustum();

		pointsFrustum.get( 0 ).setZ( nearZ );
		pointsFrustum.get( 1 ).setZ( nearZ );
		pointsFrustum.get( 2 ).setZ( nearZ );
		pointsFrustum.get( 3 ).setZ( nearZ );

		pointsFrustum.get( 4 ).setZ( farZ );
		pointsFrustum.get( 5 ).setZ( farZ );
		pointsFrustum.get( 6 ).setZ( farZ );
		pointsFrustum.get( 7 ).setZ( farZ );
	}
	
	/**
	 * Fit shadow camera's ortho frustum to camera frustum
	 */
	private void updateShadowCamera( Camera camera, VirtualLight light ) 
	{
		OrthographicCamera shadowCamera = (OrthographicCamera) light.getShadowCamera();
		List<Vector3> pointsFrustum = light.getPointsFrustum();
		List<Vector3> pointsWorld = light.getPointsWorld();

		this.min.set( Double.POSITIVE_INFINITY );
		this.max.set( Double.NEGATIVE_INFINITY );

		for ( int i = 0; i < 8; i ++ ) 
		{
			Vector3 p = pointsWorld.get( i );

			p.copy( pointsFrustum.get( i ) );
			ShadowMap.projector.unprojectVector( p, camera );

			shadowCamera.getMatrixWorldInverse().multiplyVector3( p );

			if ( p.getX() < this.min.getX() ) this.min.setX( p.getX() );
			if ( p.getX() > this.max.getX() ) this.max.setX( p.getX() );

			if ( p.getY() < this.min.getY() ) this.min.setY( p.getY() );
			if ( p.getY() > this.max.getY() ) this.max.setY( p.getY() );

			if ( p.getZ() < this.min.getZ() ) this.min.setZ( p.getZ() );
			if ( p.getZ() > this.max.getZ() ) this.max.setZ( p.getZ() );

		}

		shadowCamera.setLeft( this.min.getX() );
		shadowCamera.setRight( this.max.getX() );
		shadowCamera.setTop( this.max.getY() );
		shadowCamera.setBottom( this.min.getY() );

		shadowCamera.updateProjectionMatrix();
	}
}
