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

package thothbot.parallax.core.client.renderers;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.enums.CullFaceMode;
import thothbot.parallax.core.client.gl2.enums.EnableCap;
import thothbot.parallax.core.client.gl2.enums.FrontFaceDirection;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.TextureMagFilter;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
import thothbot.parallax.core.client.shaders.DepthRGBAShader;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.cameras.OrthographicCamera;
import thothbot.parallax.core.shared.cameras.PerspectiveCamera;
import thothbot.parallax.core.shared.core.BufferGeometry;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryObject;
import thothbot.parallax.core.shared.core.Gyroscope;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.helpers.CameraHelper;
import thothbot.parallax.core.shared.lights.DirectionalLight;
import thothbot.parallax.core.shared.lights.Light;
import thothbot.parallax.core.shared.lights.ShadowLight;
import thothbot.parallax.core.shared.lights.SpotLight;
import thothbot.parallax.core.shared.lights.VirtualLight;
import thothbot.parallax.core.shared.materials.HasSkinning;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.MeshFaceMaterial;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Frustum;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.objects.SkinnedMesh;
import thothbot.parallax.core.shared.scenes.Scene;

public final class ShadowMap extends Plugin 
{
	private boolean isAutoUpdate = true;
	private boolean isSoft = true;
	private boolean isCullFrontFaces = true;
	private boolean isDebugEnabled = false;
	private boolean isCascade = false;
	
	private ShaderMaterial depthMaterial, depthMaterialMorph, depthMaterialSkin, depthMaterialMorphSkin;

	private Frustum frustum;
	private Matrix4 projScreenMatrix;
	
	private Vector3 matrixPosition;

	private Vector3 min;
	private Vector3 max;
	
	List<WebGLObject> _renderList = new ArrayList<WebGLObject>();
		
	public ShadowMap(WebGLRenderer renderer, Scene scene) 
	{
		super(renderer, scene);

		this.min = new Vector3();
		this.max = new Vector3();
		
		this.frustum = new Frustum();
		this.projScreenMatrix = new Matrix4();
		
		this.matrixPosition = new Vector3();
		
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

	public boolean isAutoUpdate() {
		return isAutoUpdate;
	}

	public void setAutoUpdate(boolean isAutoUpdate) {
		this.isAutoUpdate = isAutoUpdate;
	}

	public boolean isSoft() {
		return isSoft;
	}

	public void setSoft(boolean isSoft) {
		this.isSoft = isSoft;
	}

	public boolean isCullFrontFaces() {
		return isCullFrontFaces;
	}

	public void setCullFrontFaces(boolean isCullFrontFaces) {
		this.isCullFrontFaces = isCullFrontFaces;
	}

	public boolean isDebugEnabled() {
		return isDebugEnabled;
	}

	public void setDebugEnabled(boolean isDebugEnabled) {
		this.isDebugEnabled = isDebugEnabled;
	}

	public boolean isCascade() {
		return isCascade;
	}

	public void setCascade(boolean isCascade) {
		this.isCascade = isCascade;
	}

	@Override
	public TYPE getType() 
	{
		return Plugin.TYPE.PRE_RENDER;
	}

	@Override
	public void render(Camera camera, List<Light> sceneLights, int currentWidth, int currentHeight) 
	{
		if ( ! ( isEnabled() && isAutoUpdate() ) ) return;

		WebGLRenderingContext gl = getRenderer().getGL();
		
		// set GL state for depth map

		gl.clearColor( 1, 1, 1, 1 );
		gl.disable( EnableCap.BLEND );

		gl.enable( EnableCap.CULL_FACE );
		gl.frontFace( FrontFaceDirection.CCW );

		if ( isCullFrontFaces() ) 
		{
			gl.cullFace( CullFaceMode.FRONT );
		} 
		else 
		{
			gl.cullFace( CullFaceMode.BACK );
		}

		getRenderer().setDepthTest( true );
		
		List<Light> lights = new ArrayList<Light>();

		// preprocess lights
		// 	- skip lights that are not casting shadows
		//	- create virtual lights for cascaded shadow maps
		// render depth map
		for ( int i = 0, il = sceneLights.size(); i < il; i ++ ) 
		{
			Light sceneLight = sceneLights.get( i );

			if ( ! sceneLight.isCastShadow() ) continue;
						
			if ( ( sceneLight instanceof DirectionalLight ) && ((DirectionalLight)sceneLight).isShadowCascade() ) 
			{
				DirectionalLight dirLight = (DirectionalLight)sceneLight;

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
			} else {
				lights.add(sceneLight);
			}
		}
		
		// render depth map

		for ( int i = 0, il = lights.size(); i < il; i ++ ) {
			ShadowLight light = (ShadowLight) lights.get(i);
			
			if ( light.getShadowMap() == null ) 
			{
				RenderTargetTexture map = new RenderTargetTexture(light.getShadowMapWidth(), light.getShadowMapHeight());
				map.setMinFilter(TextureMinFilter.NEAREST);
				map.setMagFilter(TextureMagFilter.NEAREST);
				map.setFormat(PixelFormat.RGBA);
				light.setShadowMap(map);

				light.setShadowMapSize( new Vector2( light.getShadowMapWidth(), light.getShadowMapHeight() ) );
				light.setShadowMatrix( new Matrix4() );
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
				getScene().add( light.getCameraHelper() );
			}

			if ( light instanceof VirtualLight && ((VirtualLight)light).getOriginalCamera() == camera ) 
			{
				updateShadowCamera( camera, (VirtualLight)light );
			}

			Camera shadowCamera = light.getShadowCamera();
			
			shadowCamera.getPosition().setFromMatrixPosition( light.getMatrixWorld() );
			this.matrixPosition.setFromMatrixPosition( light.getTarget().getMatrixWorld() );
			shadowCamera.lookAt( this.matrixPosition );
			shadowCamera.updateMatrixWorld(false);
			
			shadowCamera.getMatrixWorldInverse().getInverse( shadowCamera.getMatrixWorld() );
			
			if ( light.getCameraHelper() != null ) 
				light.getCameraHelper().setVisible( light.isShadowCameraVisible() );
			if ( light.isShadowCameraVisible() ) 
				light.getCameraHelper().update();
			
			// compute shadow matrix
			Matrix4 shadowMatrix = light.getShadowMatrix();
			shadowMatrix.set( 0.5, 0.0, 0.0, 0.5,
							  0.0, 0.5, 0.0, 0.5,
							  0.0, 0.0, 0.5, 0.5,
							  0.0, 0.0, 0.0, 1.0 );

			shadowMatrix.multiply( shadowCamera.getProjectionMatrix() );
			shadowMatrix.multiply( shadowCamera.getMatrixWorldInverse() );

			// update camera matrices and frustum
			
			this.projScreenMatrix.multiply( shadowCamera.getProjectionMatrix(), shadowCamera.getMatrixWorldInverse() );
			this.frustum.setFromMatrix( this.projScreenMatrix );

			// render shadow map

			getRenderer().setRenderTarget( light.getShadowMap() );
			getRenderer().clear();
			
			// set object matrices & frustum culling

			this._renderList = new ArrayList<WebGLObject>();

			projectObject( getScene(), getScene(), shadowCamera );


			// render regular objects

			for ( int j = 0, jl = _renderList.size(); j < jl; j ++ ) {

				WebGLObject webglObject = _renderList.get( j );

				GeometryObject object = webglObject.object;
				WebGLGeometry buffer = webglObject.buffer;

				// culling is overriden globally for all objects
				// while rendering depth map

				// need to deal with MeshFaceMaterial somehow
				// in that case just use the first of material.materials for now
				// (proper solution would require to break objects by materials
				//  similarly to regular rendering and then set corresponding
				//  depth materials per each chunk instead of just once per object)

				Material objectMaterial = getObjectMaterial( object );

				boolean useMorphing = ((Geometry)object.getGeometry()).getMorphTargets() != null 
						&& ((Geometry)object.getGeometry()).getMorphTargets().size() > 0 
						&& objectMaterial instanceof HasSkinning && ((HasSkinning)objectMaterial).isMorphTargets();
						
				boolean  useSkinning = object instanceof SkinnedMesh 
						&& objectMaterial instanceof HasSkinning && ((HasSkinning)objectMaterial).isSkinning();

				Material material = null;
				
//				if ( object.customDepthMaterial ) {
//
//					material = object.customDepthMaterial;
//
//				} else 
					if ( useSkinning ) {

					material = useMorphing ? this.depthMaterialMorphSkin : this.depthMaterialSkin;

				} else if ( useMorphing ) {

					material = this.depthMaterialMorph;

				} else {

					material = this.depthMaterial;

				}

				getRenderer().setMaterialFaces( objectMaterial );

				if ( buffer instanceof BufferGeometry ) {

					getRenderer().renderBufferDirect( shadowCamera, sceneLights, null, material, (BufferGeometry)buffer, object );

				} else {

					getRenderer().renderBuffer( shadowCamera, sceneLights, null, material, buffer, object );

				}

			}

			// set matrices and render immediate objects

			for ( int j = 0, jl = getRenderer()._webglObjectsImmediate.size(); j < jl; j ++ ) {

				WebGLObject webglObject = getRenderer()._webglObjectsImmediate.get( j );
				GeometryObject object = webglObject.object;

				if ( object.isVisible() && object.isCastShadow() ) {

					object._modelViewMatrix.multiply( shadowCamera.getMatrixWorldInverse(), object.getMatrixWorld() );

					getRenderer().renderImmediateObject( shadowCamera, sceneLights, null, this.depthMaterial, object );

				}

			}

		}

		// restore GL state

		Color clearColor = getRenderer().getClearColor();
		double clearAlpha = getRenderer().getClearAlpha();

		gl.clearColor( clearColor.getR(), clearColor.getG(), clearColor.getB(), clearAlpha );
		gl.enable( EnableCap.BLEND );

		if ( isCullFrontFaces() ) 
		{
			gl.cullFace( CullFaceMode.BACK );
		}
		
		getRenderer().resetGLState();
	}
	
	private void projectObject( Scene scene, Object3D object, Camera shadowCamera ){

		if ( object.isVisible() ) {

			List<WebGLObject> webglObjects = getRenderer()._webglObjects.get( object.getId() + "" );

			if ( webglObjects != null && object.isCastShadow() && (object.isFrustumCulled() == false || getRenderer()._frustum.isIntersectsObject( (GeometryObject) object ) == true) ) {

				for ( int i = 0, l = webglObjects.size(); i < l; i ++ ) {

					WebGLObject webglObject = webglObjects.get( i );

					object._modelViewMatrix.multiply( shadowCamera.getMatrixWorldInverse(), object.getMatrixWorld() );
					_renderList.add( webglObject );

				}

			}

			for ( int i = 0, l = object.getChildren().size(); i < l; i ++ ) {

				projectObject( scene, object.getChildren().get( i ), shadowCamera );

			}

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
			p.unproject( camera );
			
			p.apply( shadowCamera.getMatrixWorldInverse() );

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
	
	// For the moment just ignore objects that have multiple materials with different animation methods
	// Only the first material will be taken into account for deciding which depth material to use for shadow maps

	private Material getObjectMaterial( GeometryObject object ) {

		return object.getMaterial() instanceof MeshFaceMaterial
			? ((MeshFaceMaterial)object.getMaterial()).getMaterials().get( 0 )
			: object.getMaterial();

	};

}
