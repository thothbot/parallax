/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.client.context.Canvas3d;
import thothbot.parallax.core.client.gl2.WebGLFramebuffer;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.WebGLUniformLocation;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.enums.BlendEquationMode;
import thothbot.parallax.core.client.gl2.enums.BlendingFactorDest;
import thothbot.parallax.core.client.gl2.enums.BlendingFactorSrc;
import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
import thothbot.parallax.core.client.renderers.plugins.LensFlarePlugin;
import thothbot.parallax.core.client.renderers.plugins.SpritePlugin;
import thothbot.parallax.core.client.shader.Program;
import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.Uniform;
import thothbot.parallax.core.client.textures.CubeTexture;
import thothbot.parallax.core.client.textures.DataTexture;
import thothbot.parallax.core.client.textures.RenderTargetCubeTexture;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.Color3;
import thothbot.parallax.core.shared.core.Frustum;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryBuffer;
import thothbot.parallax.core.shared.core.GeometryGroup;
import thothbot.parallax.core.shared.core.Mathematics;
import thothbot.parallax.core.shared.core.Matrix4;
import thothbot.parallax.core.shared.core.Vector2;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.core.Vector4;
import thothbot.parallax.core.shared.core.WebGLCustomAttribute;
import thothbot.parallax.core.shared.lights.AmbientLight;
import thothbot.parallax.core.shared.lights.DirectionalLight;
import thothbot.parallax.core.shared.lights.Light;
import thothbot.parallax.core.shared.lights.PointLight;
import thothbot.parallax.core.shared.lights.SpotLight;
import thothbot.parallax.core.shared.materials.HasEnvMap;
import thothbot.parallax.core.shared.materials.HasFog;
import thothbot.parallax.core.shared.materials.HasLightMap;
import thothbot.parallax.core.shared.materials.HasMap;
import thothbot.parallax.core.shared.materials.HasSkinning;
import thothbot.parallax.core.shared.materials.HasVertexColors;
import thothbot.parallax.core.shared.materials.HasWireframe;
import thothbot.parallax.core.shared.materials.HasWrap;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.MeshFaceMaterial;
import thothbot.parallax.core.shared.materials.MeshLambertMaterial;
import thothbot.parallax.core.shared.materials.MeshPhongMaterial;
import thothbot.parallax.core.shared.materials.ParticleBasicMaterial;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.objects.GeometryObject;
import thothbot.parallax.core.shared.objects.HasSides;
import thothbot.parallax.core.shared.objects.LensFlare;
import thothbot.parallax.core.shared.objects.Line;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.objects.Object3D;
import thothbot.parallax.core.shared.objects.ParticleSystem;
import thothbot.parallax.core.shared.objects.Ribbon;
import thothbot.parallax.core.shared.objects.SkinnedMesh;
import thothbot.parallax.core.shared.objects.Sprite;
import thothbot.parallax.core.shared.objects.WebGLObject;
import thothbot.parallax.core.shared.scenes.Fog;
import thothbot.parallax.core.shared.scenes.FogExp2;
import thothbot.parallax.core.shared.scenes.Scene;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

/**
 * The WebGL renderer displays your beautifully crafted {@link Scene}s using WebGL, if your device supports it.
 */
public class WebGLRenderer
{
	/**
	 * Sets the Shaders precision value.
	 */
	public static enum PRECISION 
	{
		HIGHP,
		MEDIUMP,
		LOWP
	};

	// The HTML5 Canvas's 'webgl' context obtained from the canvas where the renderer will draw.
	private Canvas3d canvas;
	private WebGLRenderInfo info;

	// shader precision. Can be "highp", "mediump" or "lowp".
	private WebGLRenderer.PRECISION precision = WebGLRenderer.PRECISION.HIGHP;
				
	// Integer, default is Color3(0x000000).
	private Color3 clearColor = new Color3(0x000000);

	// double, default is 0
	private double clearAlpha = 1.0;
	
	// Integer, default is 4
	private int maxLights = 4;
	
	// Properties
	private boolean isAutoClear = true;
	private boolean isAutoClearColor = true;
	private boolean isAutoClearDepth = true;
	private boolean isAutoClearStencil = true;

	// scene graph
	private boolean isSortObjects = true;
	private boolean isAutoUpdateObjects = true;
	private boolean isAutoUpdateScene = true;

	// physically based shading
	private boolean isGammaInput = false;
	private boolean isGammaOutput = false;
	private boolean isPhysicallyBasedShading = false;

	// shadow map
	private boolean isShadowMapEnabled = false;
	private boolean isShadowMapAutoUpdate = true;
	private boolean isShadowMapSoft = true;
	private boolean isShadowMapCullFrontFaces = true;
	private boolean isShadowMapDebug = false;
	private boolean isShadowMapCascade = false;

	// morphs
	private int maxMorphTargets = 8;
	private int maxMorphNormals = 4;

	// flags
	private boolean isAutoScaleCubemaps = true;

	// custom render plugins

	// An array with render plugins to be applied before rendering.
	private List<Plugin> renderPluginsPre;
	
	//An array with render plugins to be applied after rendering.
	private List<Plugin> renderPluginsPost;

	// internal state cache

	private Program cache_currentProgram = null;
	private WebGLFramebuffer cache_currentFramebuffer = null;
	private int cache_currentMaterialId = -1;
	private int cache_currentGeometryGroupHash = -1;
	private Camera cache_currentCamera = null;
	
	// GL state cache

	private Boolean cache_oldDoubleSided = null;
	private Boolean cache_oldFlipSided = null;

	private Material.BLENDING cache_oldBlending = null;
	private BlendEquationMode cache_oldBlendEquation = null;

	private BlendingFactorSrc cache_oldBlendSrc = null;
	private BlendingFactorDest cache_oldBlendDst = null;

	private Boolean cache_oldDepthTest = null;
	private Boolean cache_oldDepthWrite = null;

	private Boolean cache_oldPolygonOffset = null;
	private Double cache_oldPolygonOffsetFactor = null;
	private Double cache_oldPolygonOffsetUnits = null;
			
	private int viewportX = 0;
	private int viewportY = 0;
	private int viewportWidth = 0;
	private int viewportHeight = 0;
	private int _currentWidth = 0;
	private int _currentHeight = 0;
	
	// frustum

	private Frustum frustum;

	 // camera matrices cache
	private Matrix4 cache_projScreenMatrix;
	private Vector4 cache_vector3;

	// light arrays cache
	private Vector3 cache_direction;
	private boolean isLightsNeedUpdate = true;
	private WebGLRenderLights cache_lights;
	
	private Map<String, Program> cache_programs;

	// GPU capabilities
	private int GPUmaxVertexTextures;
	private int GPUmaxTextureSize;
	private int GPUmaxCubemapSize;
	
	/**
	 * The constructor will create renderer for the {@link Canvas3d} widget.
	 * 
	 * @param canvas the {@link Canvas3d} widget
	 */
	public WebGLRenderer(Canvas3d canvas)
	{
		setCanvas(canvas);

		this.setInfo(new WebGLRenderInfo());
		
		this.frustum = new Frustum();
		
		this.cache_projScreenMatrix = new Matrix4();
		this.cache_vector3          = new Vector4();
		this.cache_direction        = new Vector3();
		this.cache_lights           = new WebGLRenderLights();
		this.cache_programs         = new HashMap<String, Program>();
		
		this.GPUmaxVertexTextures = getGL().getParameteri(GLenum.MAX_VERTEX_TEXTURE_IMAGE_UNITS.getValue());
		this.GPUmaxTextureSize    = getGL().getParameteri(GLenum.MAX_TEXTURE_SIZE.getValue());
		this.GPUmaxCubemapSize    = getGL().getParameteri(GLenum.MAX_CUBE_MAP_TEXTURE_SIZE.getValue());

		setViewport(0, 0, getCanvas().getWidth(), getCanvas().getHeight());
		setDefaultGLState();
		
		// default plugins (order is important)
		this.renderPluginsPre = new ArrayList<Plugin>();
		this.renderPluginsPost = new ArrayList<Plugin>();
		
//		this.shadowMapPlugin = new THREE.ShadowMapPlugin();
//		this.addPrePlugin( this.shadowMapPlugin );
	
		addPostPlugin( new SpritePlugin() );
		addPostPlugin( new LensFlarePlugin() );
	}
	
	public void addPostPlugin( Plugin plugin ) 
	{
		plugin.init( this );
		this.renderPluginsPost.add( plugin );
	}

	public void addPrePlugin( Plugin plugin ) 
	{
		plugin.init( this );
		this.renderPluginsPre.add( plugin );
	}

	/**
	 * Gets {@link #setAutoClear(boolean)} flag.
	 */
	public boolean isAutoClear() {
		return isAutoClear;
	}

	/**
	 * Defines whether the renderer should automatically clear its output before rendering.
	 * Default is true.
	 * 
	 * @param isAutoClear false or true
	 */
	public void setAutoClear(boolean isAutoClear) {
		this.isAutoClear = isAutoClear;
	}

	/**
	 * Gets {@link #setAutoClearColor(boolean)} flag.
	 */
	public boolean isAutoClearColor() {
		return isAutoClearColor;
	}

	/**
	 * Defines whether the renderer should clear the color buffer. 
	 * Default is true.
	 * 
	 * @param isAutoClearColor false or true
	 */
	public void setAutoClearColor(boolean isAutoClearColor) {
		this.isAutoClearColor = isAutoClearColor;
	}


	/**
	 * Gets {@link #setAutoClearDepth(boolean)} flag.
	 */
	public boolean isAutoClearDepth() {
		return isAutoClearDepth;
	}

	/**
	 * Defines whether the renderer should clear the depth buffer. 
	 * Default is true.
	 * 
	 * @param isAutoClearDepth false or true
	 */
	public void setAutoClearDepth(boolean isAutoClearDepth) {
		this.isAutoClearDepth = isAutoClearDepth;
	}

	/**
	 * Gets {@link #setAutoClearStencil(boolean)} flag.
	 */
	public boolean isAutoClearStencil() {
		return isAutoClearStencil;
	}

	/**
	 * Defines whether the renderer should clear the stencil buffer. 
	 * Default is true.
	 * 
	 * @param isAutoClearStencil false or true
	 */
	public void setAutoClearStencil(boolean isAutoClearStencil) {
		this.isAutoClearStencil = isAutoClearStencil;
	}

	/**
	 * Gets {@link #setSortObjects(boolean)} flag.
	 */
	public boolean isSortObjects() {
		return isSortObjects;
	}

	/**
	 * Defines whether the renderer should sort objects. 
	 * Default is true.
	 * 
	 * @param isSortObjects false or true
	 */
	public void setSortObjects(boolean isSortObjects) {
		this.isSortObjects = isSortObjects;
	}

	/**
	 * Gets {@link #setAutoUpdateObjects(boolean)} flag.
	 */
	public boolean isAutoUpdateObjects() {
		return isAutoUpdateObjects;
	}

	/**
	 * Defines whether the renderer should auto update objects. 
	 * Default is true.
	 * 
	 * @param isAutoUpdateObjects false or true
	 */
	public void setAutoUpdateObjects(boolean isAutoUpdateObjects) {
		this.isAutoUpdateObjects = isAutoUpdateObjects;
	}

	/**
	 * Gets {@link #setAutoUpdateScene(boolean)} flag.
	 */
	public boolean isAutoUpdateScene() {
		return isAutoUpdateScene;
	}
	
	public boolean isGammaInput() {
		return this.isGammaInput;
	}
	
	public void setGammaInput(boolean isGammaInput) {
		this.isGammaInput = isGammaInput;
	}
	
	public boolean isGammaOutput() {
		return this.isGammaOutput;
	}
	
	public void setGammaOutput(boolean isGammaOutput) {
		this.isGammaOutput = isGammaOutput;
	}

	public boolean isPhysicallyBasedShading() {
		return this.isPhysicallyBasedShading;
	}
	
	public void setPhysicallyBasedShading(boolean isPhysicallyBasedShading) {
		this.isPhysicallyBasedShading = isPhysicallyBasedShading;
	}

	/**
	 * Defines whether the renderer should auto update the scene.
	 * Default is true.
	 * 
	 * @param isAutoUpdateScene false or true
	 */
	public void setAutoUpdateScene(boolean isAutoUpdateScene) {
		this.isAutoUpdateScene = isAutoUpdateScene;
	}

	/**
	 * Gets {@link WebGLRenderInfo} instance with debug information.
	 * 
	 * @return the {@link WebGLRenderInfo} instance
	 */
	public WebGLRenderInfo getInfo() {
		return info;
	}
	
	private void setInfo(WebGLRenderInfo info) {
		this.info = info;
	}
	
	/**
	 * Gets {@link Canvas3d} widget with whom the renderer is associated.
	 * 
	 * @return the {@link Canvas3d} widget.
	 */
	public Canvas3d getCanvas() {
		return this.canvas;
	}
	
	private void setCanvas(Canvas3d canvas) {
		this.canvas = canvas;
	}
	
	/**
	 * Gets the WebGL context from the {@link Canvas3d} widget.
	 * 
	 * @return the underlying context implementation for drawing onto the
	 *         {@link Canvas3d}.
	 */
	public WebGLRenderingContext getGL()
	{
		return getCanvas().getGL();
	}

	private void setDefaultGLState () 
	{
		getGL().clearColor( 0.0, 0.0, 0.0, 1.0 );
		getGL().clearDepth( 1 );
		getGL().clearStencil( 0 );

		getGL().enable( GLenum.DEPTH_TEST.getValue() );
		getGL().depthFunc( GLenum.LEQUAL.getValue() );

		getGL().frontFace( GLenum.CCW.getValue() );
		getGL().cullFace( GLenum.BACK.getValue() );
		getGL().enable( GLenum.CULL_FACE.getValue() );

		getGL().enable( GLenum.BLEND.getValue() );
		getGL().blendEquation( GLenum.FUNC_ADD.getValue() );
		getGL().blendFunc( GLenum.SRC_ALPHA.getValue(), GLenum.ONE_MINUS_SRC_ALPHA.getValue() );
	}

	/**
	 * Return a Boolean true if the context supports vertex textures.
	 */
	public boolean supportsVertexTextures()
	{
		return this.GPUmaxVertexTextures > 0;
	}

	/**
	 * Sets the {@link Canvas3d} and also sets {@link #setViewport(int, int, int, int)} size.
	 * 
	 * @param width the {@link Canvas3d} width.
	 * @param height the {@link Canvas3d} height.
	 */
	public void setSize(int width, int height)
	{
		getCanvas().setSize(width, height);
		setViewport(0, 0, width, height);
	}

	/**
	 * Sets the viewport to render from (X, Y) to (X + width, Y + height).
	 * By default X and Y = 0.
	 */
	public void setViewport(int x, int y, int width, int height)
	{
		this.viewportX = x;
		this.viewportY = y;

		this.viewportWidth = width;
		this.viewportHeight = height;

		getGL().viewport(this.viewportX, this.viewportY, this.viewportWidth, this.viewportHeight);
	}

	/**
	 * Sets the scissor area from (x, y) to (x + width, y + height).
	 */
	public void setScissor(int x, int y, int width, int height)
	{
		getGL().scissor(x, y, width, height);
	}

	/**
	 * Enable the scissor test. When this is enabled, only the pixels 
	 * within the defined scissor area will be affected by further 
	 * renderer actions.
	 */
	public void enableScissorTest(boolean enable)
	{
		if (enable)
			getGL().enable(GLenum.SCISSOR_TEST.getValue());
		else
			getGL().disable(GLenum.SCISSOR_TEST.getValue());
	}
	
	/**
	 * Sets the Shader precision value.
	 * 
	 * @param precision the {@link WebGLRenderer.PRECISION} value.
	 */
	public void setPrecision(WebGLRenderer.PRECISION precision) 
	{
		this.precision = precision; 
	}
	
	/**
	 * Specifies how many total lights are allowed in the scene 
	 * (divided evenly between point & directional lights) By default there are 4.
	 * 
	 * @param maxLights
	 */
	public void setMaxLights(int maxLights) 
	{
		this.maxLights = maxLights;
	}

	/**
	 * Sets the the background color, using hex for the color.<br>
	 * 
	 * @param hex the clear color value.
	 */
	public void setClearColorHex( int hex )
	{
		setClearColorHex(hex, 1.0);
	}

	/**
	 * Sets the the background color, using hex for the color and alpha for the opacity.<br>
	 * Default clear clolor is 0x000000 - black.<br>
	 * Default clear alpha is 1.0 - opaque.<br>
	 * 
	 * @param hex   the clear color value.
	 * @param alpha the opacity of the scene's background color, range 0.0 (invisible) to 1.0 (opaque).
	 */
	public void setClearColorHex( int hex, double alpha ) 
	{
		this.clearColor.setHex( hex );
		this.clearAlpha = alpha;

		getGL().clearColor( this.clearColor.getR(), this.clearColor.getG(), this.clearColor.getB(), this.clearAlpha );
	}
	

	/**
	 * Sets the the background color, using {@link Color3} for the color and alpha for the opacity.
	 * 
	 * @see #setClearColorHex(int, double). 
	 * 
	 * @param color the {@link Color3} instance.
	 * @param alpha the opacity of the scene's background color, range 0.0 (invisible) to 1.0 (opaque).
	 */
	public void setClearColor( Color3 color, double alpha ) 
	{
		this.clearColor.copy(color);
		this.clearAlpha = alpha;

		getGL().clearColor( this.clearColor.getR(), this.clearColor.getG(), this.clearColor.getB(), this.clearAlpha );
	}

	/**
	 * Returns the background color.
	 * 
	 * @return the {@link Color3} instance. 
	 */
	public Color3 getClearColor() 
	{
		return this.clearColor;
	}

	/**
	 * Returns the opacity of the scene's background color, range 0.0 (invisible) to 1.0 (opaque)
	 * 
	 * @return the value in range <0,1>.
	 */
	public double getClearAlpha() 
	{
		return this.clearAlpha;
	}

	/**
	 * Tells the renderer to clear its color, depth or stencil drawing buffer(s).
	 * If no parameters are passed, no buffer will be cleared.
	 * 
	 * @param color   is it should clear color
	 * @param depth   is it should clear depth
	 * @param stencil is it should clear stencil
	 */
	public void clear( boolean color, boolean depth, boolean stencil ) 
	{
		int bits = 0;

		if ( color ) bits |= GLenum.COLOR_BUFFER_BIT.getValue();
		if ( depth ) bits |= GLenum.DEPTH_BUFFER_BIT.getValue();
		if ( stencil ) bits |= GLenum.STENCIL_BUFFER_BIT.getValue();

		getGL().clear( bits );
	}

	/**
	 * Clear {@link RenderTargetTexture} and GL buffers.
	 * @see #clear(boolean, boolean, boolean).
	 */
	public void clearTarget( RenderTargetTexture renderTarget, boolean color, boolean depth, boolean stencil ) 
	{
		setRenderTarget( renderTarget );
		clear( color, depth, stencil );
	}
	
	/**
	 * object — an instance of Object3D
	 * Removes an object from the GL context and releases all the data (geometry, matrices...) 
	 * that the GL context keeps about the object, but it doesn't release textures or affect any 
	 * JavaScript data.
	 */
	private void deallocateObject( GeometryObject object ) 
	{
		if ( ! object.isWebglInit ) return;

		object.isWebglInit = false;

		object._modelViewMatrix = null;
		object._normalMatrix = null;

		object._normalMatrixArray = null;
		object._modelViewMatrixArray = null;
		object._objectMatrixArray = null;

		if ( object.getClass() == Mesh.class )
			for ( GeometryGroup g : object.getGeometry().getGeometryGroups().values() )
				deleteMeshBuffers( g );
					
		else if ( object.getClass() == Ribbon.class )
			deleteRibbonBuffers( object.getGeometry() );

		else if ( object.getClass() == Line.class )
			deleteLineBuffers( object.getGeometry() );

		else if ( object.getClass() == ParticleSystem.class )
			deleteParticleBuffers( object.getGeometry() );
	}

	/**
	 * Releases a texture from the GL context.
	 * texture — an instance of Texture
	 */
	public void deallocateTexture( Texture texture ) 
	{
		if ( texture.getWebGlTexture() == null ) return;

		getGL().deleteTexture( texture.getWebGlTexture() );

		this.getInfo().getMemory().textures--;
	}

	/**
	 * Releases a render target from the GL context.
	 * 
	 * @param renderTarget the instance of {@link RenderTargetTexture}
	 */
	public void deallocateRenderTarget ( RenderTargetTexture renderTarget ) 
	{
		renderTarget.deallocate(getGL());
	}

	/**
	 * Tells the shadow map plugin to update using the passed scene and camera parameters.
	 */
	private void updateShadowMap( Scene scene, Camera camera ) 
	{
		this.cache_currentProgram = null;
		this.cache_oldBlending = null;
		this.cache_oldDepthTest = null;
		this.cache_oldDepthWrite = null;
		this.cache_currentGeometryGroupHash = -1;
		this.cache_currentMaterialId = -1;
		this.isLightsNeedUpdate = true;
		this.cache_oldDoubleSided = null;
		this.cache_oldFlipSided = null;

		//TODO: this is extras 
		//shadowMapPlugin.update( scene, camera );
	}
	
	private void deleteParticleBuffers ( Geometry geometry ) 
	{
		getGL().deleteBuffer( geometry.__webglVertexBuffer );
		getGL().deleteBuffer( geometry.__webglColorBuffer );

		this.getInfo().getMemory().geometries --;
	}
	
	private void deleteLineBuffers (Geometry geometry ) 
	{
		deleteParticleBuffers(geometry);
	};

	private void deleteRibbonBuffers (Geometry geometry ) 
	{
		deleteParticleBuffers(geometry);
	}
	
	private void deleteMeshBuffers ( GeometryGroup geometryGroup ) 
	{
		getGL().deleteBuffer( geometryGroup.__webglVertexBuffer );
		getGL().deleteBuffer( geometryGroup.__webglNormalBuffer );
		getGL().deleteBuffer( geometryGroup.__webglTangentBuffer );
		getGL().deleteBuffer( geometryGroup.__webglColorBuffer );
		getGL().deleteBuffer( geometryGroup.__webglUVBuffer );
		getGL().deleteBuffer( geometryGroup.__webglUV2Buffer );

		getGL().deleteBuffer( geometryGroup.__webglSkinVertexABuffer );
		getGL().deleteBuffer( geometryGroup.__webglSkinVertexBBuffer );
		getGL().deleteBuffer( geometryGroup.__webglSkinIndicesBuffer );
		getGL().deleteBuffer( geometryGroup.__webglSkinWeightsBuffer );

		getGL().deleteBuffer( geometryGroup.__webglFaceBuffer );
		getGL().deleteBuffer( geometryGroup.__webglLineBuffer );

		if ( geometryGroup.numMorphTargets != 0) {

			for ( int m = 0; m < geometryGroup.numMorphTargets; m ++ ) {
				getGL().deleteBuffer( geometryGroup.__webglMorphTargetsBuffers.get( m ) );
			}
		}

		if ( geometryGroup.numMorphNormals != 0 ) {

			for ( int m = 0; m <  geometryGroup.numMorphNormals; m ++ ) {
				getGL().deleteBuffer( geometryGroup.__webglMorphNormalsBuffers.get( m ) );
			}
		}


		if ( geometryGroup.__webglCustomAttributesList != null) {

			for ( WebGLCustomAttribute att : geometryGroup.__webglCustomAttributesList ) {
				getGL().deleteBuffer( att.buffer );
			}
		}

		this.getInfo().getMemory().geometries --;
	}
	
	// Buffer initialization
	
	/**
	 * Morph Targets Buffer initialization
	 */
	private void setupMorphTargets ( Material material, GeometryBuffer geometrybuffer, Mesh object ) 
	{
		// set base
		Map<String, Integer> attributes = material.getProgram().getAttributes();

		if ( object.getMorphTargetBase() != - 1 ) 
		{
			getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphTargetsBuffers.get( object.getMorphTargetBase() ) );
			getGL().vertexAttribPointer( attributes.get("position"), 3, GLenum.FLOAT.getValue(), false, 0, 0 );

		} 
		else if ( attributes.get("position") >= 0 ) 
		{
			getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometrybuffer.__webglVertexBuffer );
			getGL().vertexAttribPointer( attributes.get("position"), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
		}

		if ( object.getMorphTargetForcedOrder().size() > 0 ) 
		{
			// set forced order

			int m = 0;
			List<Integer> order = object.getMorphTargetForcedOrder();
			List<Double> influences = object.getMorphTargetInfluences();

			while ( material instanceof HasSkinning 
					&& m < ((HasSkinning)material).getNumSupportedMorphTargets() 
					&& m < order.size() 
			) {
				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphTargetsBuffers.get( order.get( m ) ) );
				getGL().vertexAttribPointer( attributes.get("morphTarget" + m ), 3, GLenum.FLOAT.getValue(), false, 0, 0 );

				if ( material instanceof HasSkinning && ((HasSkinning)material).isMorphNormals()) 
				{
					getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphNormalsBuffers.get( order.get( m ) ) );
					getGL().vertexAttribPointer( attributes.get("morphNormal" + m ), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
				}

				object.__webglMorphTargetInfluences.set( m , influences.get( order.get( m ) ));

				m ++;
			}
		}
		else 
		{
			// find most influencing

			Map<Integer, Boolean> used = new HashMap<Integer, Boolean>();
			double candidateInfluence = - 1;
			int candidate = 0;
			List<Double> influences = object.getMorphTargetInfluences();			

			if ( object.getMorphTargetBase() != - 1 )
				used.put( object.getMorphTargetBase(), true);

			int m = 0;
			while ( material instanceof HasSkinning 
					&& m < ((HasSkinning)material).getNumSupportedMorphTargets() ) 
			{
				for ( int i = 0; i < influences.size(); i ++ ) 
				{
					if ( !used.containsKey(i) && influences.get( i ) > candidateInfluence ) 
					{
						candidate = i;
						candidateInfluence = influences.get( candidate );
					}
				}

				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphTargetsBuffers.get( candidate ) );
				getGL().vertexAttribPointer( attributes.get( "morphTarget" + m ), 3, GLenum.FLOAT.getValue(), false, 0, 0 );

				if ( material instanceof HasSkinning && ((HasSkinning)material).isMorphNormals() ) 
				{
					getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphNormalsBuffers.get( candidate ) );
					getGL().vertexAttribPointer( attributes.get( "morphNormal" + m ), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
				}

				object.__webglMorphTargetInfluences.set( m, candidateInfluence);

				used.put( candidate, true);
				candidateInfluence = -1;
				m ++;
			}
		}

		// load updated influences uniform
		if( material.getProgram().getUniforms().get("morphTargetInfluences") != null ) 
		{
			Float32Array vals = object.__webglMorphTargetInfluences;
			double[] val2 = new double[vals.getLength()];
			for (int i = 0; i < vals.getLength(); i++) 
			{
			    Double f = vals.get(i);
			    val2[i] = (f != null ? f : Double.NaN); // Or whatever default you want.
			}
			getGL().uniform1fv( material.getProgram().getUniforms().get("morphTargetInfluences"), val2 );
		}
	}

	/**
	 * Rendering. 
	 * @see #render(Scene, Camera, RenderTargetTexture, boolean).
	 */
	public void render( Scene scene, Camera camera)
	{
		render(scene, camera, null);
	}

	/**
	 * Rendering. 
	 * @see #render(Scene, Camera, RenderTargetTexture, boolean).
	 */
	public void render( Scene scene, Camera camera, RenderTargetTexture renderTarget)
	{
		render(scene, camera, renderTarget, false);
	}

	/**
	 * Rendering.
	 * 
	 * @param scene        the {@link Scene} object.
	 * @param camera       the {@link Camera} object
	 * @param renderTarget optional
	 * @param forceClear   optional
	 */
	public void render( Scene scene, Camera camera, RenderTargetTexture renderTarget, boolean forceClear ) 
	{
		Log.debug("Called render()");

		List<Light> lights = scene.getLights();
		Fog fog = scene.getFog();

		// reset caching for this frame
		this.cache_currentMaterialId = -1;
		this.isLightsNeedUpdate = true;

		// update scene graph
		if ( camera.getParent() == null ) 
		{
			Log.warn("DEPRECATED: Camera hasn\'t been added to a Scene. Adding it...");
			scene.addChild( camera );
		}

		Log.debug("render() this.autoUpdateScene=" + this.isAutoUpdateScene());
		if ( this.isAutoUpdateScene() ) 
				scene.updateMatrixWorld(false);

		// update camera matrices and frustum

		if ( camera._viewMatrixArray == null ) 
			camera._viewMatrixArray = Float32Array.create( 16 );

		if ( camera._projectionMatrixArray == null ) 
			camera._projectionMatrixArray = Float32Array.create( 16 );

		camera.getMatrixWorldInverse().getInverse( camera.getMatrixWorld() );
		camera.getMatrixWorldInverse().flattenToArray( camera._viewMatrixArray );
		camera.getProjectionMatrix().flattenToArray( camera._projectionMatrixArray );

		this.cache_projScreenMatrix.multiply( camera.getProjectionMatrix(), camera.getMatrixWorldInverse() );
		this.frustum.setFromMatrix( cache_projScreenMatrix );

		// update WebGL objects
		if ( this.isAutoUpdateObjects() ) 
			initWebGLObjects( scene );

		// custom render plugins (pre pass)
		renderPlugins( this.renderPluginsPre, scene, camera );

		this.getInfo().getRender().calls = 0;
		this.getInfo().getRender().vertices = 0;
		this.getInfo().getRender().faces = 0;
		this.getInfo().getRender().points = 0;

		setRenderTarget( renderTarget );

		if ( this.isAutoClear() || forceClear )
			clear( this.isAutoClearColor(), this.isAutoClearDepth(), this.isAutoClearStencil() );

		// set matrices for regular objects (frustum culled)
		List<WebGLObject> renderList = scene.__webglObjects;
		Log.debug("render(): Render list size is: " + renderList.size());

		for(WebGLObject webglObject: renderList) 
		{
			GeometryObject object = webglObject.object;
			webglObject.render = false;

			if ( object.isVisible() ) 
			{
				if ( ! ( object.getClass() == Mesh.class 
						|| object.getClass() == ParticleSystem.class ) 
						|| ! ( object.isFrustumCulled ) 
						|| frustum.contains( object ) )
				{
					setupMatrices( (Object3D) object, camera );
					unrollBufferMaterial( webglObject );
					webglObject.render = true;

					if ( this.isSortObjects() ) 
					{
						if ( object.renderDepth > 0 ) 
						{
							webglObject.z = object.renderDepth;
						} 
						else 
						{
							this.cache_vector3.copy( object.getMatrixWorld().getPosition() );
							this.cache_projScreenMatrix.multiplyVector3( cache_vector3 );

							webglObject.z = cache_vector3.getZ();
						}
					}
				}
			}
		}

		if ( this.isSortObjects() )
			Collections.sort(renderList);

		// set matrices for immediate objects

		List<WebGLObject> renderListI = scene.__webglObjectsImmediate;

		for(WebGLObject webglObject: renderListI)
		{
			GeometryObject object = webglObject.object;

			if ( object.isVisible() ) 
			{
				Log.debug("render(): set matrices for immediate objects");

				setupMatrices( (Object3D) object, camera );

				unrollImmediateBufferMaterial( webglObject );
			}
		}

		if ( scene.overrideMaterial != null ) 
		{
			Log.error("render(): override material");
			
			Material material = scene.overrideMaterial;

			//TODO: FIX
//			this.setBlending( material.getBlending(), material.getBlendEquation(), material.getBlendSrc(), material.getBlendDst() );
//			this.setDepthTest( material.isDepthTest() );
//			this.setDepthWrite( material.isDepthWrite() );
//			setPolygonOffset( material.isPolygonOffset(), material.getPolygonOffsetFactor(), material.getPolygonOffsetUnits() );
//
//			renderObjects( scene.__webglObjects, false, "", camera, lights, fog, true, material );
//			renderObjectsImmediate( scene.__webglObjectsImmediate, "", camera, lights, fog, false, material );

		} 
		else 
		{
			Log.debug("render(): NON override material");
			// opaque pass (front-to-back order)

			setBlending( Material.BLENDING.NORMAL);

			renderObjects( scene.__webglObjects, true, "opaque", camera, lights, fog, false );
			renderObjectsImmediate( scene.__webglObjectsImmediate, "opaque", camera, lights, fog, false );

			// transparent pass (back-to-front order)

			renderObjects( scene.__webglObjects, false, "transparent", camera, lights, fog, true );
			renderObjectsImmediate( scene.__webglObjectsImmediate, "transparent", camera, lights, fog, true );
		}

		// custom render plugins (post pass)
		renderPlugins( this.renderPluginsPost, scene, camera );


		// Generate mipmap if we're using any kind of mipmap filtering
		if ( renderTarget != null && renderTarget.isGenerateMipmaps() 
				&& renderTarget.getMinFilter() != TextureMinFilter.NEAREST 
				&& renderTarget.getMinFilter() != TextureMinFilter.LINEAR)
		{
			renderTarget.updateRenderTargetMipmap(getGL());
		}

		// Ensure depth buffer writing is enabled so it can be cleared on next render

		this.setDepthTest( true );
		this.setDepthWrite( true );

//		 getGL().finish();
	}

	private void renderPlugins( List<Plugin> plugins, Scene scene, Camera camera ) 
	{
		if ( plugins.size() == 0 ) return;

		for ( int i = 0, il = plugins.size(); i < il; i ++ ) 
		{
			Plugin plugin = plugins.get( i );
			Log.debug("Called renderPlugins(): " + plugin.getClass().getName());

			// reset state for plugin (to start from clean slate)
			this.cache_currentProgram = null;
			this.cache_currentCamera = null;

			this.cache_oldBlending = null;
			this.cache_oldDepthTest = null;
			this.cache_oldDepthWrite = null;
			this.cache_oldDoubleSided = null;
			this.cache_oldFlipSided = null;

			this.cache_currentGeometryGroupHash = -1;
			this.cache_currentMaterialId = -1;

			this.isLightsNeedUpdate = true;

			plugin.render( scene, camera, _currentWidth, _currentHeight );

			// reset state after plugin (anything could have changed)

			this.cache_currentProgram = null;
			this.cache_currentCamera = null;

			this.cache_oldBlending = null;
			this.cache_oldDepthTest = null;
			this.cache_oldDepthWrite = null;
			this.cache_oldDoubleSided = null;
			this.cache_oldFlipSided = null;

			this.cache_currentGeometryGroupHash = -1;
			this.cache_currentMaterialId = -1;

			this.isLightsNeedUpdate = true;
		}
	}
	
	// TODO: CHECK callback
	private void renderImmediateObject( Camera camera, List<Light> lights, Fog fog, Material material, GeometryObject object ) 
	{
		Program program = setProgram( camera, lights, fog, material, object );

		this.cache_currentGeometryGroupHash = -1;

		setObjectFaces( (HasSides) object );

//		if ( object.immediateRenderCallback )
//			object.immediateRenderCallback( program, this._gl, this._frustum );
//
//		else
//			object.render( function( object ) { _this.renderBufferImmediate( object, program, material.shading ); } );
	}
	
	private void renderObjectsImmediate ( List<WebGLObject> renderList, String materialType, Camera camera, List<Light> lights, Fog fog, boolean useBlending) 
	{
		renderObjectsImmediate ( renderList, materialType, camera, lights, fog,  useBlending, null);
	}

	private void renderObjectsImmediate ( List<WebGLObject> renderList, String materialType, Camera camera, List<Light> lights, Fog fog, boolean useBlending, Material overrideMaterial ) 
	{
		for ( int i = 0; i < renderList.size(); i ++ ) 
		{

			WebGLObject webglObject = renderList.get( i );
			GeometryObject object = webglObject.object;

			if ( object.isVisible()) 
			{

				Material material = null;
				if ( overrideMaterial != null)
				{
					material = overrideMaterial;

				} 
				else 
				{
					if(materialType == "opaque")
						material = webglObject.opaque;
					else if(materialType == "transparent")
						material = webglObject.transparent;

					if ( material == null ) continue;

					if ( useBlending ) 
						this.setBlending( material.getBlending(), material.getBlendEquation(), material.getBlendSrc(), material.getBlendDst() );

					this.setDepthTest( material.isDepthTest() );
					this.setDepthWrite( material.isDepthWrite() );
					setPolygonOffset( material.isPolygonOffset(), material.getPolygonOffsetFactor(), material.getPolygonOffsetUnits());
				}

				renderImmediateObject( camera, lights, fog, material, object );
			}
		}
	}

	private void renderObjects ( List<WebGLObject> renderList, boolean reverse, String materialType, Camera camera, List<Light> lights, Fog fog, boolean useBlending) 
	{
		renderObjects ( renderList, reverse, materialType, camera, lights, fog, useBlending, null);
	}

	private void renderObjects ( List<WebGLObject> renderList, boolean reverse, String materialType, Camera camera, List<Light> lights, Fog fog, boolean useBlending, Material overrideMaterial ) 
	{
		Log.debug("Called renderObjects() render list contains = " + renderList.size());
		
		int start = 0;
		int end = 0;
		int delta = 0;
			
		if ( reverse ) 
		{
			start = renderList.size() - 1;
			end = -1;
			delta = -1;

		} 
		else 
		{
			start = 0;
			end = renderList.size();
			delta = 1;
		}

		Material material = null;

		for ( int i = start; i != end; i += delta ) {

			WebGLObject webglObject = renderList.get( i );

			if ( webglObject.render ) 
			{
 
				GeometryObject object = webglObject.object;
				GeometryBuffer buffer = webglObject.buffer;

				if ( overrideMaterial != null ) 
				{
					material = overrideMaterial;

				} 
				else 
				{
					if(materialType == "opaque")
						material = webglObject.opaque;
					else if(materialType == "transparent")
						material = webglObject.transparent;

					if ( material == null ) continue;

					if ( useBlending ) 
						setBlending( material.getBlending(), material.getBlendEquation(), material.getBlendSrc(), material.getBlendDst());

					setDepthTest( material.isDepthTest());
					setDepthWrite( material.isDepthWrite());
					setPolygonOffset( material.isPolygonOffset(), material.getPolygonOffsetFactor(), material.getPolygonOffsetUnits());
				}

				if(object instanceof HasSides)
					setObjectFaces( (HasSides) object );

				// TODO: Extras
//				if ( buffer instanceof THREE.BufferGeometry )
//					_this.renderBufferDirect( camera, lights, fog, material, buffer, object );
//				else
					renderBuffer( camera, lights, fog, material, buffer, (GeometryObject) object );
			}
		}
	}

	
	/**
	 * Buffer rendering.
	 * Render GeometryObject with material.
	 */
	private void renderBuffer( Camera camera, List<Light> lights, Fog fog, Material material, GeometryBuffer geometryBuffer, GeometryObject object ) 
	{
		if ( ! material.isVisible()) 
			return;

		Program program = setProgram( camera, lights, fog, material, object );

		Map<String, Integer> attributes = program.getAttributes();

		boolean updateBuffers = false;
		int wireframeBit = material instanceof HasWireframe && ((HasWireframe)material).isWireframe() ? 1 : 0;

		int geometryGroupHash = ( geometryBuffer.getId() * 0xffffff ) + ( program.getId() * 2 ) + wireframeBit;

//		GWT.log("--- renderBuffer() geometryGroupHash=" + geometryGroupHash 
//				+ ", _currentGeometryGroupHash=" +  this._currentGeometryGroupHash
//				+ ", program.id=" + program.id
//				+ ", geometryGroup.id=" + geometryBuffer.getId()
//				+ ", __webglLineCount=" + geometryBuffer.__webglLineCount
//				+ ", object.id=" + object.getId()
//				+ ", wireframeBit=" + wireframeBit);

		if ( geometryGroupHash != this.cache_currentGeometryGroupHash ) 
		{
			this.cache_currentGeometryGroupHash = geometryGroupHash;
			updateBuffers = true;
		}

		// vertices
		if ( !(material instanceof HasSkinning && ((HasSkinning)material).isMorphTargets()) && attributes.get("position") >= 0 ) 
		{
			if ( updateBuffers ) 
			{
				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglVertexBuffer );
				getGL().vertexAttribPointer( attributes.get("position"), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
			}

		} 
		else if ( object instanceof Mesh/* && ((Mesh)object).getMorphTargetBase() */ ) 
		{
				setupMorphTargets( material, geometryBuffer, (Mesh)object );
		}

		
		if ( updateBuffers ) 
		{
			// custom attributes

			// Use the per-geometryGroup custom attribute arrays which are setup in initMeshBuffers

			if ( geometryBuffer.__webglCustomAttributesList != null ) 
			{
				for ( int i = 0; i < geometryBuffer.__webglCustomAttributesList.size(); i ++ ) 
				{
					WebGLCustomAttribute attribute = geometryBuffer.__webglCustomAttributesList.get( i );

					if( attributes.get( attribute.belongsToAttribute ) >= 0 ) 
					{
						getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), attribute.buffer );
						getGL().vertexAttribPointer( attributes.get( attribute.belongsToAttribute ), attribute.size, GLenum.FLOAT.getValue(), false, 0, 0 );
					}
				}
			}

			// colors
			if ( attributes.get("color") >= 0 ) 
			{
				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglColorBuffer );
				getGL().vertexAttribPointer( attributes.get("color"), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
			}

			// normals
			if ( attributes.get("normal") >= 0 ) 
			{
				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglNormalBuffer );
				getGL().vertexAttribPointer( attributes.get("normal"), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
			}

			// tangents
			if ( attributes.get("tangent") >= 0 ) 
			{
				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglTangentBuffer );
				getGL().vertexAttribPointer( attributes.get("tangent"), 4, GLenum.FLOAT.getValue(), false, 0, 0 );
			}

			// uvs
			if ( attributes.get("uv") >= 0 ) 
			{
				if ( geometryBuffer.__webglUVBuffer != null) 
				{
					getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglUVBuffer );
					getGL().vertexAttribPointer( attributes.get("uv"), 2, GLenum.FLOAT.getValue(), false, 0, 0 );

					getGL().enableVertexAttribArray( attributes.get("uv") );

				} else {
					getGL().disableVertexAttribArray( attributes.get("uv") );
				}
			}

			if ( attributes.get("uv2") >= 0 ) 
			{
				if ( geometryBuffer.__webglUV2Buffer != null) 
				{
					getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglUV2Buffer );
					getGL().vertexAttribPointer( attributes.get("uv2"), 2, GLenum.FLOAT.getValue(), false, 0, 0 );

					getGL().enableVertexAttribArray( attributes.get("uv2") );

				} else {
					getGL().disableVertexAttribArray( attributes.get("uv2") );
				}
			}

			if ( material instanceof HasSkinning && ((HasSkinning)material).isSkinning() &&
				 attributes.get("skinVertexA") >= 0 && attributes.get("skinVertexB") >= 0 &&
				 attributes.get("skinIndex") >= 0 && attributes.get("skinWeight") >= 0 ) 
			{

				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglSkinVertexABuffer );
				getGL().vertexAttribPointer( attributes.get("skinVertexA"), 4, GLenum.FLOAT.getValue(), false, 0, 0 );

				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglSkinVertexBBuffer );
				getGL().vertexAttribPointer( attributes.get("skinVertexB"), 4, GLenum.FLOAT.getValue(), false, 0, 0 );

				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglSkinIndicesBuffer );
				getGL().vertexAttribPointer( attributes.get("skinIndex"), 4, GLenum.FLOAT.getValue(), false, 0, 0 );

				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglSkinWeightsBuffer );
				getGL().vertexAttribPointer( attributes.get("skinWeight"), 4, GLenum.FLOAT.getValue(), false, 0, 0 );
			}
		}

		Log.debug(" -> renderBuffer() ID " + object.getId() + " = " + object.getClass().getName());

		// Render object's buffers
		object.renderBuffer(this, geometryBuffer, updateBuffers);
	}

	private void renderBufferImmediate( Object3D object, Program program, Material.SHADING shading ) 
	{
		if ( object.__webglVertexBuffer == null ) 
			object.__webglVertexBuffer = getGL().createBuffer();

		if ( object.__webglNormalBuffer == null ) 
			object.__webglNormalBuffer = getGL().createBuffer();

		if ( object.hasPos ) 
		{
			getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), object.__webglVertexBuffer );
			getGL().bufferData( GLenum.ARRAY_BUFFER.getValue(), object.positionArray, GLenum.DYNAMIC_DRAW.getValue() );
			getGL().enableVertexAttribArray( program.getAttributes().get("position") );
			getGL().vertexAttribPointer( program.getAttributes().get("position"), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
		}

		if ( object.hasNormal ) 
		{
			getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), object.__webglNormalBuffer );

			if ( shading == Material.SHADING.FLAT ) 
			{
				for(int  i = 0; i < (object.count * 3); i += 9 ) 
				{
					Float32Array normalArray = object.normalArray;

					double nax  = normalArray.get( i );
					double nay  = normalArray.get( i + 1 );
					double naz  = normalArray.get( i + 2 );

					double nbx  = normalArray.get( i + 3 );
					double nby  = normalArray.get( i + 4 );
					double nbz  = normalArray.get( i + 5 );

					double ncx  = normalArray.get( i + 6 );
					double ncy  = normalArray.get( i + 7 );
					double ncz  = normalArray.get( i + 8 );

					double nx = ( nax + nbx + ncx ) / 3.0;
					double ny = ( nay + nby + ncy ) / 3.0;
					double nz = ( naz + nbz + ncz ) / 3.0;

					normalArray.set(i, nx);
					normalArray.set( i + 1, ny);
					normalArray.set( i + 2, nz);

					normalArray.set( i + 3, nx);
					normalArray.set( i + 4, ny);
					normalArray.set( i + 5, nz);

					normalArray.set( i + 6, nx);
					normalArray.set( i + 7, ny);
					normalArray.set( i + 8, nz);
				}
			}

			getGL().bufferData( GLenum.ARRAY_BUFFER.getValue(), object.normalArray, GLenum.DYNAMIC_DRAW.getValue() );
			getGL().enableVertexAttribArray( program.getAttributes().get("normal") );
			getGL().vertexAttribPointer( program.getAttributes().get("normal"), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
		}

		getGL().drawArrays( GLenum.TRIANGLES.getValue(), 0, object.count );
		object.count = 0;
	}
	
//	public void renderBufferDirect ( Camera camera, List<Light> lights, Fog fog, Material material, GeometryGroup geometryGroup, Object3D object ) 
//	{
//		if ( !material.visible ) return;
//
//		Program program = setProgram( camera, lights, fog, material, object );
//		Map<String, Integer> attributes = program.attributes;
//
//		boolean updateBuffers = false;
//		int wireframeBit = material.wireframe ? 1 : 0;
//		int geometryGroupHash = ( geometryGroup.id * 0xffffff ) + ( program.id * 2 ) + wireframeBit;
//
//		if ( geometryGroupHash != this._currentGeometryGroupHash ) {
//			this._currentGeometryGroupHash = geometryGroupHash;
//			updateBuffers = true;
//		}
//
//		// render mesh
//		if ( object instanceof Mesh ) {
//
//			List<Integer> offsets = geometryGroup.offsets;
//
//			for ( int i = 0; i < offsets.size(); ++ i ) {
//				if ( updateBuffers ) {
//
//					// vertices
//
//					getGL().bindBuffer( GLenum.ARRAY_BUFFER, geometryGroup.vertexPositionBuffer );
//					getGL().vertexAttribPointer( attributes.get("position"), 
//							geometryGroup.vertexPositionBuffer.itemSize, 
//							GLenum.FLOAT, false, 0, offsets.get( i ).index * 4 * 3 );
//
//					// normals
//
//					if ( attributes.get("normal") >= 0 && geometryGroup.vertexNormalBuffer != null) {
//						getGL().bindBuffer( GLenum.ARRAY_BUFFER, geometryGroup.vertexNormalBuffer );
//						getGL().vertexAttribPointer( attributes.get("normal"), 
//								geometryGroup.vertexNormalBuffer.itemSize, 
//								GLenum.FLOAT, false, 0, offsets.get( i ).index * 4 * 3 );
//					}
//
//					// uvs
//
//					if ( attributes.uv >= 0 && geometryGroup.vertexUvBuffer != null ) {
//
//						if ( geometryGroup.vertexUvBuffer != null) {
//							getGL().bindBuffer( GLenum.ARRAY_BUFFER, geometryGroup.vertexUvBuffer );
//							getGL().vertexAttribPointer(  attributes.get("uv"), 
//									geometryGroup.vertexUvBuffer.itemSize, 
//									GLenum.FLOAT, false, 0, offsets.get( i ).index * 4 * 2 );
//							getGL().enableVertexAttribArray( attributes.uv );
//						} else {
//							getGL().disableVertexAttribArray( attributes.uv );
//						}
//
//					}
//
//					// colors
//
//					if ( attributes.color >= 0 && geometryGroup.vertexColorBuffer != null ) {
//						getGL().bindBuffer( GLenum.ARRAY_BUFFER, geometryGroup.vertexColorBuffer );
//						getGL().vertexAttribPointer( attributes.get("color"), 
//								geometryGroup.vertexColorBuffer.itemSize, 
//								GLenum.FLOAT, false, 0, offsets.get( i ).index * 4 * 4 );
//					}
//
//					getGL().bindBuffer( GLenum.ELEMENT_ARRAY_BUFFER, geometryGroup.vertexIndexBuffer );
//				}
//
//				// render indexed triangles
//				getGL().drawElements( GLenum.TRIANGLES, offsets.get( i ).count, GLenum.UNSIGNED_SHORT, offsets.get( i ).start * 2 ); // 2 = Uint16
//
//				this.info.render.calls ++;
//				// not really true, here vertices can be shared
//				this.info.render.vertices += offsets.get( i ).count; 
//				this.info.render.faces += offsets.get( i ).count / 3;
//			}
//		}
//	}

	private void unrollImmediateBufferMaterial ( WebGLObject globject ) 
	{
		GeometryObject object = globject.object;
		Material material = object.getMaterial();

		if ( material.isTransparent()) {
			globject.transparent = material;
			globject.opaque = null;

		} else {
			globject.opaque = material;
			globject.transparent = null;
		}
	}

	private void unrollBufferMaterial (  WebGLObject globject ) 
	{
		GeometryObject object = globject.object;

		Material meshMaterial = object.getMaterial();

		if ( meshMaterial instanceof MeshFaceMaterial ) 
		{
			GeometryGroup buffer = (GeometryGroup) globject.buffer;
			int materialIndex = buffer.materialIndex;

			if ( materialIndex >= 0 ) 
			{
				Material material = object.getGeometry().getMaterials().get( materialIndex );

				if ( material.isTransparent() ) 
				{
					globject.transparent = material;
					globject.opaque = null;
					
				} 
				else 
				{
					globject.opaque = material;
					globject.transparent = null;
				}
			}

		} 
		else 
		{

			Material material = meshMaterial;

			if ( material != null) 
			{
				if ( material.isTransparent() ) 
				{
					globject.transparent = material;
					globject.opaque = null;

				} 
				else 
				{
					globject.opaque = material;
					globject.transparent = null;
				}
			}
		}
	}

	/**
	 * Refresh Scene's objects
	 * 
	 * @param scene the Scene with child objects
	 */
	public void initWebGLObjects(Scene scene ) 
	{
		if ( scene.__webglObjects == null ) 
		{
			scene.__webglObjects = new ArrayList<WebGLObject>();
			scene.__webglObjectsImmediate = new ArrayList<WebGLObject>();
			scene.__webglSprites = new ArrayList<Sprite>();
			scene.__webglFlares = new ArrayList<LensFlare>();
		}

		Log.debug("initWebGLObjects() objectsAdded=" + scene.getObjectsAdded().size() 
				+ ", objectsRemoved=" + scene.getObjectsRemoved().size() 
				+ ", update=" + scene.__webglObjects.size());
		
		while ( scene.getObjectsAdded().size() > 0 ) 
		{
			addObject( (Object3D) scene.getObjectsAdded().get( 0 ), scene );
			scene.getObjectsAdded().remove(0);
		}

		while ( scene.getObjectsRemoved().size() > 0 ) 
		{
			removeObject( (GeometryObject) scene.getObjectsRemoved().get( 0 ), scene );
			scene.getObjectsRemoved().remove(0);
		}

		// update must be called after objects adding / removal
		for(WebGLObject object: scene.__webglObjects)
		{
			object.object.setBuffer(this);
		}			
	}

	/**
	 * Adds objects
	 */
	private void addObject ( Object3D object, Scene scene )
	{
		Log.debug("addObject() object=" + object.getClass().getName());

		if ( object instanceof GeometryObject && ! object.isWebglInit ) 
		{
			object.isWebglInit = true;

			Log.debug("addObject() initBuffer()");
			((GeometryObject)object).initBuffer(this);
		}

		if ( ! object.isWebglActive ) 
		{
			object.isWebglActive = true;

			Log.debug("addObject() addObjectAddBuffer()");
			addObjectAddBuffer(object, scene);
		}
	}
	
	private void addObjectAddBuffer(Object3D object, Scene scene)
	{
		if(object instanceof GeometryObject)
		{
			if ( object instanceof Mesh ) 
			{
				Mesh mesh = (Mesh)object;
				Geometry geometry = mesh.getGeometry();
				Log.debug("addObject() add Mesh buffer");
				//			if(geometry instanceof BufferGeometry) 
				//			{
				//				addBuffer( scene.__webglObjects, geometry, object );
				//			}
				//			else {				
				for ( GeometryGroup geometryGroup : geometry.getGeometryGroups().values())
					addBuffer( scene.__webglObjects, geometryGroup, (GeometryObject)object );
				//			}

			} 
			else if ( object.getClass() == Ribbon.class ||
					object.getClass() == Line.class ||
					object.getClass() == ParticleSystem.class ) {

				Geometry geometry = ((GeometryObject)object).getGeometry();
				addBuffer( scene.__webglObjects, geometry, (GeometryObject)object );
			}

			
//		} else if ( object.getClass() instanceof THREE.ImmediateRenderObject || object.immediateRenderCallback ) {
//
//			addBufferImmediate( scene.__webglObjectsImmediate, object );
//
		} 
		else if ( object.getClass() == Sprite.class ) 
		{
			scene.__webglSprites.add( (Sprite) object );

		} 
		else if ( object.getClass() == LensFlare.class ) 
		{
			scene.__webglFlares.add( (LensFlare) object );
		}
	}

	private void addBuffer ( List<WebGLObject> objlist, GeometryBuffer buffer, GeometryObject object ) 
	{
		objlist.add(new WebGLObject(buffer, object, null, null));
	}

//	function addBufferImmediate ( objlist, object ) {
//
//		objlist.push(
//			{
//				object: object,
//				opaque: null,
//				transparent: null
//			}
//		);
//	}

	/*
	 * Objects removal
	 */
	private static void removeObject ( Object3D object, Scene scene ) 
	{
		if ( object instanceof GeometryObject) 
		{
			for ( int o = scene.__webglObjects.size() - 1; o >= 0; o -- )
				if ( scene.__webglObjects.get( o ).object == object )
					scene.__webglObjects.remove(o);

		} 
		else if ( object.getClass() == Sprite.class ) 
		{
			for ( int o = scene.__webglSprites.size() - 1; o >= 0; o -- )
				if ( scene.__webglSprites.get( o ) == object )
					scene.__webglSprites.remove(o);

		} 
		else if ( object.getClass() == LensFlare.class ) 
		{
			for ( int o = scene.__webglFlares.size() - 1; o >= 0; o -- )
				if ( scene.__webglFlares.get( o ) == object )
					scene.__webglFlares.remove(o);

//		} else if ( object instanceof ImmediateRenderObject || object.immediateRenderCallback ) {
//			removeInstances( scene.__webglObjectsImmediate, object );
		}

		object.isWebglActive = false;
	}

	private void initMaterial ( Material material, List<Light> lights, Fog fog, GeometryObject object ) 
	{
		Log.debug("Called initMaterial for material: " + material.getClass().getName() + " and object " + object.getClass().getName());

		Shader shaderPrototype = material.getShaderId(); 

		if ( shaderPrototype != null )
			material.setMaterialShaders(shaderPrototype);

		// heuristics to create shader parameters according to lights in the scene
		// (not to blow over maxLights budget)
		Map<String, Integer> maxLightCount = allocateLights( lights );
		int maxShadows = allocateShadows( lights );
		int maxBones   = allocateBones( object );
		
		Program.ProgramParameters parameters = new Program.ProgramParameters();
		
		parameters.gammaInput  = isGammaInput();
		parameters.gammaOutput = isGammaOutput();
		parameters.physicallyBasedShading = isPhysicallyBasedShading();
		
		parameters.map      = (material instanceof HasMap && ((HasMap)material).getMap() != null);
		parameters.envMap   = (material instanceof HasEnvMap && ((HasEnvMap)material).getEnvMap() != null);
		parameters.lightMap = (material instanceof HasLightMap &&  ((HasLightMap)material).getLightMap() != null);

		parameters.vertexColors = (material instanceof HasVertexColors && ((HasVertexColors)material).isVertexColors() != Material.COLORS.NO);

		parameters.useFog  = (fog != null);
		parameters.useFog2 = (fog != null && fog.getClass() == FogExp2.class);

		parameters.sizeAttenuation = material instanceof ParticleBasicMaterial && ((ParticleBasicMaterial)material).isSizeAttenuation();

		if(material instanceof HasSkinning)
		{
			parameters.skinning     = ((HasSkinning)material).isSkinning();
			parameters.morphTargets = ((HasSkinning)material).isMorphTargets();
			parameters.morphNormals = ((HasSkinning)material).isMorphNormals();
		}

		parameters.maxBones = maxBones;
		
		parameters.maxMorphTargets = this.maxMorphTargets;
		parameters.maxMorphNormals = this.maxMorphNormals;

		parameters.maxDirLights   = maxLightCount.get("directional");
		parameters.maxPointLights = maxLightCount.get("point");
		parameters.maxSpotLights  = maxLightCount.get("spot");
		
		parameters.maxShadows = maxShadows;
		
		parameters.shadowMapEnabled = this.isShadowMapEnabled && object.isReceiveShadow;
		parameters.shadowMapSoft    = this.isShadowMapSoft;
		parameters.shadowMapDebug   = this.isShadowMapDebug;
		parameters.shadowMapCascade = this.isShadowMapCascade;

		parameters.alphaTest = material.getAlphaTest();
		if(material instanceof MeshPhongMaterial)
		{
			parameters.metal = ((MeshPhongMaterial)material).isMetal();
			parameters.perPixel = ((MeshPhongMaterial)material).isPerPixel();
		}
		
		parameters.wrapAround = material instanceof HasWrap && ((HasWrap)material).isWrapAround();

		parameters.doubleSided = object instanceof HasSides && ((HasSides)object).isDoubleSided();

		Log.debug("initMaterial() called new Program");

		material.setProgram(
				buildProgram(this.precision, this.GPUmaxVertexTextures, 
						material.getFragmentShaderSource(), material.getVertexShaderSource(), 
						material.getUniforms(), material.getAttributes(), parameters ));

		Map<String, Integer> attributes = material.getProgram().getAttributes();

		if ( attributes.get("position") >= 0 ) 
			getGL().enableVertexAttribArray( attributes.get("position") );

		if ( attributes.get("color") >= 0 ) 
			getGL().enableVertexAttribArray( attributes.get("color") );

		if ( attributes.get("normal") >= 0 ) 
			getGL().enableVertexAttribArray( attributes.get("normal") );

		if ( attributes.get("tangent") >= 0 ) 
			getGL().enableVertexAttribArray( attributes.get("tangent") );

		if ( material instanceof HasSkinning && ((HasSkinning)material).isSkinning() &&
			 attributes.get("skinVertexA") >=0 && attributes.get("skinVertexB") >= 0 &&
			 attributes.get("skinIndex") >= 0 && attributes.get("skinWeight") >= 0 
		) {
			getGL().enableVertexAttribArray( attributes.get("skinVertexA") );
			getGL().enableVertexAttribArray( attributes.get("skinVertexB") );
			getGL().enableVertexAttribArray( attributes.get("skinIndex") );
			getGL().enableVertexAttribArray( attributes.get("skinWeight") );
		}

		if ( material.getAttributes() != null )
			for ( String a : material.getAttributes().keySet() )
				if( attributes.get( a ) != null && attributes.get( a ) >= 0 ) 
					getGL().enableVertexAttribArray( attributes.get( a ) );

		if(material instanceof HasSkinning)
		{
			if ( ((HasSkinning)material).isMorphTargets()) 
			{
				int numSupportedMorphTargets = 0;
				for ( int i = 0; i < this.maxMorphTargets; i ++ ) 
				{
					String id = "morphTarget" + i;

					if ( attributes.get( id ) >= 0 ) 
					{
						getGL().enableVertexAttribArray( attributes.get( id ) );
						numSupportedMorphTargets ++;
					}
				}
				
				((HasSkinning)material).setNumSupportedMorphTargets(numSupportedMorphTargets);
			}

			if ( ((HasSkinning)material).isMorphNormals() ) 
			{
				int numSupportedMorphNormals = 0;
				for ( int i = 0; i < this.maxMorphNormals; i ++ ) 
				{
					String id = "morphNormal" + i;

					if ( attributes.get( id ) >= 0 ) 
					{
						getGL().enableVertexAttribArray( attributes.get( id ) );
						numSupportedMorphNormals ++;
					}
				}

				((HasSkinning)material).setNumSupportedMorphNormals(numSupportedMorphNormals);
			}
		}
	}

	private Program setProgram( Camera camera, List<Light> lights, Fog fog, Material material, GeometryObject object ) 
	{
		if ( material.getProgram() == null || material.isNeedsUpdate() ) 
		{
			initMaterial( material, lights, fog, object );
			material.setNeedsUpdate(false);
		}

		if ( material instanceof HasSkinning && ((HasSkinning)material).isMorphTargets() ) 
		{
			if ( object instanceof Mesh && ((Mesh)object).__webglMorphTargetInfluences == null ) 
			{
				((Mesh)object).__webglMorphTargetInfluences = Float32Array.create( this.maxMorphTargets );

				for ( int i = 0, il = this.maxMorphTargets; i < il; i ++ )
					((Mesh)object).__webglMorphTargetInfluences.set(i, 0);
			}
		}

		boolean refreshMaterial = false;

		Program program = material.getProgram();
		Map<String, WebGLUniformLocation> p_uniforms = program.getUniforms();
		Map<String, Uniform> m_uniforms = material.getUniforms();

		if ( program != cache_currentProgram ) 
		{
			getGL().useProgram( program.getProgram() );
			this.cache_currentProgram = program;

			refreshMaterial = true;
		}

		if ( material.getId() != this.cache_currentMaterialId ) 
		{
			this.cache_currentMaterialId = material.getId();
			refreshMaterial = true;
		}

		if ( refreshMaterial || camera != this.cache_currentCamera ) 
		{
			getGL().uniformMatrix4fv( p_uniforms.get("projectionMatrix"), false, camera._projectionMatrixArray );

			if ( camera != this.cache_currentCamera ) 
				this.cache_currentCamera = camera;
		}

		if ( refreshMaterial ) 
		{
			// refresh uniforms common to several materials
			if ( fog != null && material instanceof HasFog && ((HasFog)material).isFog())
				fog.refreshUniforms( m_uniforms );

			if ( material.getClass() == MeshPhongMaterial.class ||
				 material.getClass() == MeshLambertMaterial.class ||
				 (material.getClass() == ShaderMaterial.class && ((ShaderMaterial)material).isLights())) 
			{

				if (this.isLightsNeedUpdate ) 
				{
					setupLights( program, lights );
					this.isLightsNeedUpdate = false;
				}

				refreshUniformsLights( m_uniforms, this.cache_lights );
			}

			material.refreshUniforms(getCanvas(), camera, this.isGammaInput);

			// TODO: fix this
//			if ( object.receiveShadow && ! material._shadowPass )
//				refreshUniformsShadow( m_uniforms, lights );

			// load common uniforms
			loadUniformsGeneric( program, material.getUniforms() );

			// load material specific uniforms
			// (shader material also gets them for the sake of genericity)
			if ( material.getClass() == ShaderMaterial.class ||
				 material.getClass() == MeshPhongMaterial.class ||
				 material instanceof HasEnvMap 
			) {

				if ( p_uniforms.get("cameraPosition") != null ) 
				{
					Vector3 position = camera.getMatrixWorld().getPosition();
					getGL().uniform3f( p_uniforms.get("cameraPosition"), position.getX(), position.getY(), position.getZ() );
				}
			}

			if ( material.getClass() == MeshPhongMaterial.class ||
				 material.getClass() == MeshLambertMaterial.class ||
				 material.getClass() == ShaderMaterial.class ||
				 material instanceof HasSkinning && ((HasSkinning)material).isSkinning() 
			) {

				if ( p_uniforms.get("viewMatrix") != null ) 
					getGL().uniformMatrix4fv( p_uniforms.get("viewMatrix"), false, camera._viewMatrixArray );
			}

			// TODO: fix this
//			if ( material.skinning )
//				getGL().uniformMatrix4fv( p_uniforms.get("boneGlobalMatrices"), false, object.boneMatrices );
		}

		loadUniformsMatrices( p_uniforms, object );

		if ( p_uniforms.get("objectMatrix") != null )
			getGL().uniformMatrix4fv( p_uniforms.get("objectMatrix"), false, object.getMatrixWorld().getArray() );

		return program;
	}

	private void refreshUniformsLights ( Map<String, Uniform> uniforms, WebGLRenderLights lights ) 
	{
		uniforms.get("ambientLightColor").setValue( lights.ambient );

		uniforms.get("directionalLightColor").setValue( lights.directional.colors );
		uniforms.get("directionalLightDirection").setValue( lights.directional.positions );

		uniforms.get("pointLightColor").setValue( lights.point.colors );
		uniforms.get("pointLightPosition").setValue( lights.point.positions );
		uniforms.get("pointLightDistance").setValue( lights.point.distances );

		uniforms.get("spotLightColor").setValue( lights.spot.colors );
		uniforms.get("spotLightPosition").setValue( lights.spot.positions );
		uniforms.get("spotLightDistance").setValue( lights.spot.distances );
		uniforms.get("spotLightDirection").setValue( lights.spot.directions );
		uniforms.get("spotLightAngle").setValue( lights.spot.angles );
		uniforms.get("spotLightExponent").setValue( lights.spot.exponents );
	}

	private void refreshUniformsShadow ( Map<String, Uniform> uniforms, WebGLRenderLights lights ) 
	{
		if ( uniforms.containsKey("shadowMatrix") ) 
		{
Log.error("?????????????");
//			var j = 0;
//
//			for ( int i = 0, il = lights.size(); i < il; i ++ ) {
//
//				var light = lights[ i ];
//
//				if ( ! light.castShadow ) continue;
//
//				if ( light instanceof THREE.SpotLight || ( light instanceof THREE.DirectionalLight && ! light.shadowCascade ) ) {
//
//					uniforms.shadowMap.texture[ j ] = light.shadowMap;
//					uniforms.shadowMapSize.value[ j ] = light.shadowMapSize;
//
//					uniforms.shadowMatrix.value[ j ] = light.shadowMatrix;
//
//					uniforms.shadowDarkness.value[ j ] = light.shadowDarkness;
//					uniforms.shadowBias.value[ j ] = light.shadowBias;
//
//					j ++;
//				}
//			}
		}
	}

	// Uniforms (load to GPU)

	private void loadUniformsMatrices ( Map<String, WebGLUniformLocation> uniforms, GeometryObject object ) 
	{
		GeometryObject objectImpl = (GeometryObject) object;
		getGL().uniformMatrix4fv( uniforms.get("modelViewMatrix"), false, objectImpl._modelViewMatrix.getArray() );

		if ( uniforms.containsKey("normalMatrix") )
			getGL().uniformMatrix3fv( uniforms.get("normalMatrix"), false, objectImpl._normalMatrix.getArray() );
	}

	private void loadUniformsGeneric( Program program, Map<String, Uniform> materialUniforms ) 
	{
		for ( String u : materialUniforms.keySet() ) 
		{
			WebGLUniformLocation location = program.getUniforms().get( u );
			if ( location == null ) continue;

			Uniform uniform = (Uniform) materialUniforms.get(u);

			Uniform.TYPE type = uniform.getType();
			Object value = uniform.getValue();
			
			Log.debug("loadUniformsGeneric() u=" + u + ", " + uniform);
			
			switch ( type ) {

				case I: // single integer
					getGL().uniform1i( location, (Integer) value );
					break;

				case F: // single double
					getGL().uniform1f( location, (Double) value );
					break;

				case V2: // single THREE.Vector2
					Vector2 vector2 = (Vector2) value;
					getGL().uniform2f( location, vector2.getX(), vector2.getX() );
					break;

				case V3: // single THREE.Vector3
					Vector3 vector3 = (Vector3) value;
					getGL().uniform3f( location, vector3.getX(), vector3.getY(), vector3.getZ() );
					break;

				case V4: // single THREE.Vector4
					Vector4 vector4 = (Vector4) value;
					getGL().uniform4f( location, vector4.getX(), vector4.getY(), vector4.getZ(), vector4.getW() );
					break;

				case C: // single THREE.Color
					Color3 color = (Color3) value;
					getGL().uniform3f( location, color.getR(), color.getG(), color.getB() );
					break;

				case FV1: // flat array of floats (JS or typed array)
					Float32Array float1 = (Float32Array) value;
					getGL().uniform1fv( location, float1 );
					break;

				case FV: // flat array of floats with 3 x N size (JS or typed array)
					Float32Array float2 = (Float32Array) value;
					getGL().uniform3fv( location, float2 );
					break;

				case V2V: // array of THREE.Vector2
					List<Vector2> listVector2f = (List<Vector2>) value;
					if ( uniform.getCacheArray() == null )
						uniform.setCacheArray( Float32Array.create( 2 * listVector2f.size() ) );

					for ( int i = 0, il = listVector2f.size(); i < il; i ++ ) 
					{
						int offset = i * 2;

						uniform.getCacheArray().set(offset, listVector2f.get(i).getX());
						uniform.getCacheArray().set(offset + 1, listVector2f.get(i).getY());
					}

					getGL().uniform2fv( location, uniform.getCacheArray() );
					break;

				case V3V: // array of THREE.Vector3
					List<Vector3> listVector3f = (List<Vector3>) value;
					if ( uniform.getCacheArray() == null )
						uniform.setCacheArray( Float32Array.create( 3 * listVector3f.size() ) );

					for ( int i = 0, il = listVector3f.size(); i < il; i ++ ) 
					{
						int offset = i * 3;

						uniform.getCacheArray().set(offset, listVector3f.get( i ).getX());
						uniform.getCacheArray().set(offset + 1, listVector3f.get( i ).getY());
						uniform.getCacheArray().set(offset + 2 , listVector3f.get( i ).getZ());
					}

					getGL().uniform3fv( location, uniform.getCacheArray() );
					break;

				case V4V: // array of THREE.Vector4
					List<Vector4> listVector4f = (List<Vector4>) value;
					if ( uniform.getCacheArray() == null)
						uniform.setCacheArray( Float32Array.create( 4 * listVector4f.size() ) );


					for ( int i = 0, il = listVector4f.size(); i < il; i ++ ) 
					{
						int offset = i * 4;

						uniform.getCacheArray().set(offset, listVector4f.get( i ).getX());
						uniform.getCacheArray().set(offset + 1, listVector4f.get( i ).getY());
						uniform.getCacheArray().set(offset + 2, listVector4f.get( i ).getZ());
						uniform.getCacheArray().set(offset + 3, listVector4f.get( i ).getW());
					}

					getGL().uniform4fv( location, uniform.getCacheArray() );
					break;

				case M4: // single THREE.Matrix4
					Matrix4 matrix4 = (Matrix4) value;
					if ( uniform.getCacheArray() == null )
						uniform.setCacheArray( Float32Array.create( 16 ) );

					matrix4.flattenToArray( uniform.getCacheArray() );
					getGL().uniformMatrix4fv( location, false, uniform.getCacheArray() );
					break;

				case M4V: // array of THREE.Matrix4
					List<Matrix4> listMatrix4f = (List<Matrix4>) value;
					if ( uniform.getCacheArray() == null )
						uniform.setCacheArray( Float32Array.create( 16 * listMatrix4f.size() ) );

					for ( int i = 0, il = listMatrix4f.size(); i < il; i ++ )
						listMatrix4f.get( i ).flattenToArray( uniform.getCacheArray(), i * 16 );

					getGL().uniformMatrix4fv( location, false, uniform.getCacheArray() );
					break;

				case T: // single THREE.Texture (2d or cube)
					getGL().uniform1i( location, (Integer)value );

					Texture texture = uniform.getTexture();

					if ( texture == null ) continue;

					if ( texture.getClass() == CubeTexture.class )
						setCubeTexture( (CubeTexture) texture, (Integer)value );

					else if ( texture.getClass() == RenderTargetCubeTexture.class )
						setCubeTextureDynamic( (RenderTargetCubeTexture)texture, (Integer)value );

					else
						setTexture( texture, (Integer)value );

					break;

				case TV: // array of THREE.Texture (2d)
					Log.error("WebGL Render: Fix this");
//					if ( uniform._array == null ) 
//					{
//						uniform._array = (Float32Array) Float32Array.createArray();
//
//						for( int i = 0, il = uniform.texture.length; i < il; i ++ )
//							uniform._array.set( i, (Integer)value + i);
//					}
//
//					getGL().uniform1iv( name, uniform._array );
//
//					for( i = 0, il = uniform.texture.length; i < il; i ++ ) {
//
//						texture = uniform.texture[ i ];
//
//						if ( !texture ) continue;
//
//						_this.setTexture( texture, uniform._array[ i ] );
//
//					}
//
					break;
			}
		}
	}

	private void setupMatrices ( Object3D object, Camera camera ) 
	{
		object._modelViewMatrix.multiply( camera.getMatrixWorldInverse(), object.getMatrixWorld());

		object._normalMatrix.getInverse(object._modelViewMatrix );
		object._normalMatrix.transpose();
	}

	private void setupLights ( Program program, List<Light> lights ) 
	{
		Log.debug("Called setupLights()");

		WebGLRenderLights zlights = this.cache_lights; 

		Float32Array dcolors     = zlights.directional.colors;
		Float32Array dpositions  = zlights.directional.positions;

		Float32Array pcolors     = zlights.point.colors;
		Float32Array ppositions  = zlights.point.positions;
		Float32Array pdistances  = zlights.point.distances;

		Float32Array scolors     = zlights.spot.colors;
		Float32Array spositions  = zlights.spot.positions;
		Float32Array sdistances  = zlights.spot.distances;
		Float32Array sdirections = zlights.spot.directions;
		Float32Array sangles     = zlights.spot.angles;
		Float32Array sexponents  = zlights.spot.exponents;

		int dlength = 0;
		int plength = 0;
		int slength = 0;

		int doffset = 0;
		int poffset = 0;
		int soffset = 0;
		double r = 0, g = 0, b = 0;

		for ( int l = 0, ll = lights.size(); l < ll; l ++ ) 
		{
			Light light = lights.get( l );

			if ( light.isOnlyShadow() ) continue;

			Color3 color = light.getColor();

			if ( light.getClass() == AmbientLight.class ) 
			{
				if ( this.isGammaInput ) 
				{
					r += color.getR() * color.getR();
					g += color.getG() * color.getG();
					b += color.getB() * color.getB();
				} 
				else 
				{
					r += color.getR();
					g += color.getG();
					b += color.getB();
				}

			} 
			else if ( light.getClass() == DirectionalLight.class ) 
			{

				DirectionalLight directionalLight = (DirectionalLight) light;
				double intensity = directionalLight.getIntensity();

				doffset = dlength * 3;

				if ( this.isGammaInput ) 
				{
					dcolors.set( doffset, color.getR() * color.getR() * intensity * intensity);
					dcolors.set( doffset + 1, color.getG() * color.getG() * intensity * intensity);
					dcolors.set( doffset + 2, color.getB() * color.getB() * intensity * intensity);
				} 
				else 
				{
					dcolors.set( doffset, color.getR() * intensity);
					dcolors.set( doffset + 1, color.getG() * intensity);
					dcolors.set( doffset + 2, color.getB() * intensity);
				}

				this.cache_direction.copy( directionalLight.getMatrixWorld().getPosition() );
				this.cache_direction.sub( directionalLight.getTarget().getMatrixWorld().getPosition() );
				this.cache_direction.normalize();

				dpositions.set( doffset, this.cache_direction.getX());
				dpositions.set( doffset + 1, this.cache_direction.getY());
				dpositions.set( doffset + 2, this.cache_direction.getZ());

				dlength += 1;

			} 
			else if( light.getClass() == PointLight.class ) 
			{

				PointLight pointLight = (PointLight) light;
				double intensity = pointLight.getIntensity();
				double distance = pointLight.getDistance();
				poffset = plength * 3;

				if ( this.isGammaInput ) 
				{
					pcolors.set(  poffset, color.getR() * color.getR() * intensity * intensity);
					pcolors.set(  poffset + 1, color.getG() * color.getG() * intensity * intensity);
					pcolors.set(  poffset + 2, color.getB() * color.getB() * intensity * intensity);
				} 
				else 
				{
					pcolors.set(  poffset, color.getR() * intensity);
					pcolors.set(  poffset + 1, color.getG() * intensity);
					pcolors.set(  poffset + 2, color.getB() * intensity);
				}

				Vector3 position = pointLight.getMatrixWorld().getPosition();

				ppositions.set(  poffset, position.getX());
				ppositions.set(  poffset + 1, position.getY());
				ppositions.set(  poffset + 2, position.getZ());

				pdistances.set( plength, distance);

				plength += 1;

			} 
			else if( light.getClass() == SpotLight.class ) 
			{
				SpotLight spotLight = (SpotLight) light;
				double intensity = spotLight.intensity;
				double distance = spotLight.distance;

				soffset = slength * 3;

				if ( this.isGammaInput ) 
				{
					scolors.set(soffset, color.getR() * color.getR() * intensity * intensity);
					scolors.set(soffset + 1, color.getG() * color.getG() * intensity * intensity);
					scolors.set(soffset + 2, color.getB() * color.getB() * intensity * intensity);
				} 
				else 
				{
					scolors.set(soffset, color.getR() * intensity);
					scolors.set(soffset + 1, color.getG() * intensity);
					scolors.set(soffset + 2, color.getB() * intensity);
				}

				Vector3 position = spotLight.getMatrixWorld().getPosition();

				spositions.set(soffset, position.getX());
				spositions.set(soffset + 1, position.getY());
				spositions.set(soffset + 2, position.getZ());

				sdistances.set(slength, distance);

				this.cache_direction.copy( position );
				this.cache_direction.sub( spotLight.target.getMatrixWorld().getPosition() );
				this.cache_direction.normalize();

				sdirections.set(soffset, this.cache_direction.getX());
				sdirections.set(soffset + 1, this.cache_direction.getY());
				sdirections.set(soffset + 2, this.cache_direction.getZ());

				sangles.set(slength, Math.cos( spotLight.angle ));
				sexponents.set( slength, spotLight.exponent);

				slength += 1;
			}
		}

		// null eventual remains from removed lights
		// (this is to avoid if in shader)
		for ( int l = dlength * 3, ll = dcolors.getLength(); l < ll; l ++ ) dcolors.set( l, 0.0 );
		for ( int l = plength * 3, ll = pcolors.getLength(); l < ll; l ++ ) pcolors.set( l, 0.0 );
		for ( int l = slength * 3, ll = scolors.getLength(); l < ll; l ++ ) scolors.set( l, 0.0 );

		zlights.directional.length = dlength;
		zlights.point.length = plength;
		zlights.spot.length = slength;

		zlights.ambient.set( 0, r );
		zlights.ambient.set( 1, g );
		zlights.ambient.set( 2, b );
	}

	// GL state setting
	
	private void setFaceCulling(String frontFace ) 
	{
		getGL().disable( GLenum.CULL_FACE.getValue() );
	}

	private void setFaceCulling(String cullFace, String frontFace) 
	{
		if ( frontFace == null || frontFace.equals("ccw") )
			getGL().frontFace( GLenum.CCW.getValue() );
		else
			getGL().frontFace( GLenum.CW.getValue() );

		if( cullFace.equals("back") )
			getGL().cullFace( GLenum.BACK.getValue() );
			
		else if( cullFace.equals("front") )
			getGL().cullFace( GLenum.FRONT.getValue() );
			
		else
			getGL().cullFace( GLenum.FRONT_AND_BACK.getValue() );

		getGL().enable( GLenum.CULL_FACE.getValue() );
	}

	private void setObjectFaces( HasSides object ) 
	{
		if ( this.cache_oldDoubleSided == null || this.cache_oldDoubleSided != object.isDoubleSided() ) 
		{
			if ( object.isDoubleSided() )
				getGL().disable( GLenum.CULL_FACE.getValue() );
			else
				getGL().enable( GLenum.CULL_FACE.getValue() );

			this.cache_oldDoubleSided = object.isDoubleSided();
		}

		if ( this.cache_oldFlipSided == null || this.cache_oldFlipSided != object.isFlipSided() ) 
		{
			if ( object.isFlipSided() )
				getGL().frontFace( GLenum.CW.getValue() );
			else
				getGL().frontFace( GLenum.CCW.getValue() );

			this.cache_oldFlipSided = object.isFlipSided();
		}
	}

	private void setDepthTest( boolean depthTest ) 
	{
		if ( this.cache_oldDepthTest == null || this.cache_oldDepthTest != depthTest ) 
		{
			if ( depthTest )
				getGL().enable( GLenum.DEPTH_TEST.getValue() );
			else 
				getGL().disable( GLenum.DEPTH_TEST.getValue() );

			this.cache_oldDepthTest = depthTest;
		}
	}

	private void setDepthWrite(boolean depthWrite ) 
	{
		if ( this.cache_oldDepthWrite == null || this.cache_oldDepthWrite != depthWrite ) 
		{
			getGL().depthMask( depthWrite );
			cache_oldDepthWrite = depthWrite;
		}
	}

	private void setPolygonOffset( boolean polygonoffset, double factor, double units ) 
	{
		if ( this.cache_oldPolygonOffset == null || this.cache_oldPolygonOffset != polygonoffset ) 
		{
			if ( polygonoffset )
				getGL().enable( GLenum.POLYGON_OFFSET_FILL.getValue() );
			else
				getGL().disable( GLenum.POLYGON_OFFSET_FILL.getValue() );

			this.cache_oldPolygonOffset = polygonoffset;
		}

		if ( polygonoffset && ( cache_oldPolygonOffsetFactor == null || 
				cache_oldPolygonOffsetUnits == null || 
				cache_oldPolygonOffsetFactor != factor || 
				cache_oldPolygonOffsetUnits != units ) 
		) {
			getGL().polygonOffset( factor, units );

			this.cache_oldPolygonOffsetFactor = factor;
			this.cache_oldPolygonOffsetUnits = units;
		}
	}

	public void setBlending( Material.BLENDING blending) 
	{
		if ( blending != this.cache_oldBlending ) 
		{
			if( blending == Material.BLENDING.NO) 
			{
				getGL().disable( GLenum.BLEND.getValue() );
				
			} 
			else if( blending == Material.BLENDING.ADDITIVE) 
			{
				getGL().enable( GLenum.BLEND.getValue() );
				getGL().blendEquation( GLenum.FUNC_ADD.getValue() );
				getGL().blendFunc( GLenum.SRC_ALPHA.getValue(), GLenum.ONE.getValue() );
				
			// TODO: Find blendFuncSeparate() combination
			} 
			else if( blending == Material.BLENDING.SUBTRACTIVE) 
			{
				getGL().enable( GLenum.BLEND.getValue() );
				getGL().blendEquation( GLenum.FUNC_ADD.getValue() );
				getGL().blendFunc( GLenum.ZERO.getValue(), GLenum.ONE_MINUS_SRC_COLOR.getValue() );

			// TODO: Find blendFuncSeparate() combination
			} 
			else if( blending == Material.BLENDING.MULTIPLY) 
			{
				getGL().enable( GLenum.BLEND.getValue() );
				getGL().blendEquation( GLenum.FUNC_ADD.getValue() );
				getGL().blendFunc( GLenum.ZERO.getValue(), GLenum.SRC_COLOR.getValue() );

			} 
			else if( blending == Material.BLENDING.CUSTOM) 
			{
				getGL().enable( GLenum.BLEND.getValue() );

			} 
			else 
			{
				getGL().enable( GLenum.BLEND.getValue() );
				getGL().blendEquationSeparate( GLenum.FUNC_ADD.getValue(), GLenum.FUNC_ADD.getValue() );
				getGL().blendFuncSeparate( GLenum.SRC_ALPHA.getValue(), GLenum.ONE_MINUS_SRC_ALPHA.getValue(), GLenum.ONE.getValue(), GLenum.ONE_MINUS_SRC_ALPHA.getValue() );
			}

			this.cache_oldBlending = blending;
		}
		
		this.cache_oldBlendEquation = null;
		this.cache_oldBlendSrc = null;
		this.cache_oldBlendDst = null;
	}

	private void setBlending( Material.BLENDING blending, BlendEquationMode blendEquation, BlendingFactorSrc blendSrc, BlendingFactorDest blendDst ) 
	{
		setBlending(blending);

		if ( blending == Material.BLENDING.CUSTOM ) 
		{
			if ( blendEquation != this.cache_oldBlendEquation ) 
			{
				getGL().blendEquation( blendEquation.getValue() );
				this.cache_oldBlendEquation = blendEquation;
			}

			if ( blendSrc != cache_oldBlendSrc || blendDst != cache_oldBlendDst ) 
			{
				getGL().blendFunc( blendSrc.getValue(), blendDst.getValue());

				this.cache_oldBlendSrc = blendSrc;
				this.cache_oldBlendDst = blendDst;
			}
		}
	}

	// Shaders

	private Program buildProgram (WebGLRenderer.PRECISION _precision,
			int _maxVertexTextures, String fragmentShader, String vertexShader,
			Map<String, Uniform> uniforms, Map<String, WebGLCustomAttribute> attributes, Program.ProgramParameters parameters ) 
	{
		String cashKey = fragmentShader + vertexShader + parameters.toString();
		if(this.cache_programs.containsKey(cashKey))
			return this.cache_programs.get(cashKey);

		Program program = new Program(getGL(), this.precision, this.GPUmaxVertexTextures, fragmentShader, vertexShader, uniforms, attributes, parameters);

		program.setId(cache_programs.size());
		this.cache_programs.put(cashKey, program);

		this.getInfo().getMemory().programs = cache_programs.size();

		return program;
	}

	// Textures
	
	private void setCubeTextureDynamic(RenderTargetCubeTexture texture, int slot) 
	{
		getGL().activeTexture( GLenum.TEXTURE0.getValue() + slot );
		getGL().bindTexture( GLenum.TEXTURE_CUBE_MAP.getValue(), texture.getWebGlTexture() );
	}

	public void setTexture( Texture texture, int slot ) 
	{
		if ( texture.isNeedsUpdate()) 
		{
			if ( texture.getWebGlTexture() == null ) 
			{
				texture.setWebGlTexture(getGL().createTexture());

				this.getInfo().getMemory().textures ++;
			}

			getGL().activeTexture( GLenum.TEXTURE0.getValue() + slot );
			getGL().bindTexture( GLenum.TEXTURE_2D.getValue(), texture.getWebGlTexture() );

			getGL().pixelStorei( GLenum.UNPACK_PREMULTIPLY_ALPHA_WEBGL.getValue(), texture.isPremultiplyAlpha() ? -1 : 1 );

			Element image = texture.getImage();
			boolean isImagePowerOfTwo = Mathematics.isPowerOfTwo( image.getOffsetWidth() ) 
					&& Mathematics.isPowerOfTwo( image.getOffsetHeight() );

			texture.setTextureParameters( getGL(), GLenum.TEXTURE_2D.getValue(), isImagePowerOfTwo );

			if ( texture instanceof DataTexture ) 
			{
				Log.error("WebGLRenderer. Todo: fix DataTexture");
//				getGL().texImage2D( GLenum.TEXTURE_2D, 0, texture.getFormat().getValue(), 
//						image.getWidth(), image.getHeight(), 0, texture.getFormat().getValue(), texture.getType().getValue(), image.getdata );
			} 
			else 
			{
				getGL().texImage2D( GLenum.TEXTURE_2D.getValue(), 0, texture.getFormat().getValue(), 
						texture.getFormat().getValue(), texture.getType().getValue(), image);
			}

			if ( texture.isGenerateMipmaps() && isImagePowerOfTwo ) 
				getGL().generateMipmap( GLenum.TEXTURE_2D.getValue() );

			texture.setNeedsUpdate(false);
		} 
		else 
		{
			getGL().activeTexture( GLenum.TEXTURE0.getValue() + slot );
			getGL().bindTexture( GLenum.TEXTURE_2D.getValue(), texture.getWebGlTexture() );
		}
	}

	/**
	 * Warning: Scaling through the canvas will only work with images that use
	 * premultiplied alpha.
	 * 
	 * @param image    the image element
	 * @param maxSize  the max size of width or height
	 * 
	 * @return the image element (Canvas or Image)
	 */
	private Element clampToMaxSize ( Element image, int maxSize ) 
	{
		int imgWidth = image.getOffsetWidth();
		int imgHeight = image.getOffsetHeight();

		if ( imgWidth <= maxSize && imgHeight <= maxSize )
			return image;

		int maxDimension = Math.max( imgWidth, imgHeight );
		int newWidth = (int) Math.floor( imgWidth * maxSize / maxDimension );
		int newHeight = (int) Math.floor( imgHeight * maxSize / maxDimension );

		CanvasElement canvas = Document.get().createElement("canvas").cast();
		canvas.setWidth(newWidth);
		canvas.setHeight(newHeight);
		
		Context2d context = canvas.getContext2d();
		context.drawImage((CanvasElement) image, 0, 0, imgWidth, imgHeight, 0, 0, newWidth, newHeight );

		return canvas;
	}

	private void setCubeTexture ( CubeTexture texture, int slot ) 
	{
		if ( !texture.isValid() )
			return;

		if ( texture.isNeedsUpdate() ) 
		{
			if ( texture.getWebGlTexture() == null )
			{
				texture.setWebGlTexture(getGL().createTexture());
				this.getInfo().getMemory().textures += 6;
			}

			getGL().activeTexture( GLenum.TEXTURE0.getValue() + slot );
			getGL().bindTexture( GLenum.TEXTURE_CUBE_MAP.getValue(), texture.getWebGlTexture() );

			List<Element> cubeImage = new ArrayList<Element>();

			for ( int i = 0; i < 6; i ++ ) 
			{
				if ( this.isAutoScaleCubemaps )
					cubeImage.add(clampToMaxSize( texture.getImage( i ), this.GPUmaxCubemapSize ));

				else
					cubeImage.add(texture.getImage( i ));
			}

			Element image = cubeImage.get( 0 );
			boolean isImagePowerOfTwo = Mathematics.isPowerOfTwo( image.getOffsetWidth() ) 
					&& Mathematics.isPowerOfTwo( image.getOffsetHeight() );

			texture.setTextureParameters( getGL(), GLenum.TEXTURE_CUBE_MAP.getValue(), isImagePowerOfTwo );

			for ( int i = 0; i < 6; i ++ ) 
			{
				getGL().texImage2D( GLenum.TEXTURE_CUBE_MAP_POSITIVE_X.getValue() + i, 0, 
						texture.getFormat().getValue(), texture.getFormat().getValue(), texture.getType().getValue(), cubeImage.get( i ) );
			}

			if ( texture.isGenerateMipmaps() && isImagePowerOfTwo )	
				getGL().generateMipmap( GLenum.TEXTURE_CUBE_MAP.getValue() );

			texture.setNeedsUpdate(false);
		} 
		else 
		{
			getGL().activeTexture( GLenum.TEXTURE0.getValue() + slot );
			getGL().bindTexture( GLenum.TEXTURE_CUBE_MAP.getValue(), texture.getWebGlTexture() );
		}

	}

	/**
	 * Setup render target
	 * 
	 * @param renderTarget the render target
	 */
	private void setRenderTarget( RenderTargetTexture renderTarget ) 
	{
		Log.debug("Called setRenderTarget(params)");
		WebGLFramebuffer framebuffer = null;
		
		if(renderTarget != null) 
		{
			renderTarget.setRenderTarget(getGL());
		    framebuffer = renderTarget.getWebGLFramebuffer();

			this._currentWidth = renderTarget.getWidth();
			this._currentHeight = renderTarget.getHeight();

		} 
		else 
		{
			this._currentWidth = this.viewportWidth;
			this._currentHeight = this.viewportHeight;
		}

		if ( framebuffer != this.cache_currentFramebuffer ) 
		{
			getGL().bindFramebuffer( GLenum.FRAMEBUFFER.getValue(), framebuffer );
			getGL().viewport( 0, 0, this._currentWidth, this._currentHeight );

			this.cache_currentFramebuffer = framebuffer;
		}
	}

	/**
	 * Default for when object is not specified
	 * ( for example when prebuilding shader to be used with multiple objects )
	 * 
	 * - leave some extra space for other uniforms
	 * - limit here is ANGLE's 254 max uniform vectors (up to 54 should be safe)
	 * 
	 * @param object
	 * @return
	 */
	private int allocateBones (GeometryObject object ) 
	{
		int maxBones = 50;

		if ( object !=null && object instanceof SkinnedMesh )
			maxBones = ((SkinnedMesh)object).bones.size();

		return maxBones;
	}

	private Map<String, Integer> allocateLights ( List<Light> lights ) 
	{
		int dirLights = 0;
		int pointLights = 0;
		int spotLights = 0;
		
		int maxDirLights;
		int maxPointLights;
		int maxSpotLights;
		
		for(Light light: lights) 
		{
			if ( light.isOnlyShadow() ) continue;

			if ( light instanceof DirectionalLight ) dirLights ++;
			if ( light instanceof PointLight ) pointLights ++;
			if ( light instanceof SpotLight ) spotLights ++;
		}

		if ( ( pointLights + spotLights + dirLights ) <= this.maxLights ) 
		{
			maxDirLights = dirLights;
			maxPointLights = pointLights;
			maxSpotLights = spotLights;

		} 
		else 
		{
			maxDirLights = (int) Math.ceil( this.maxLights * dirLights / ( pointLights + dirLights ) );
			maxPointLights = this.maxLights - maxDirLights;
			maxSpotLights = maxPointLights; // this is not really correct
		}

		Map<String, Integer> retval = new HashMap<String, Integer>();
		retval.put("directional", maxDirLights);
		retval.put("point", maxPointLights);
		retval.put("spot", maxSpotLights);

		return retval;
	}

	private int allocateShadows ( List<Light> lights ) 
	{
		int maxShadows = 0;

		for (Light light: lights)
			if ( light.isAllocateShadows() )
				maxShadows ++;

		return maxShadows;
	}

	@Deprecated
	public Matrix4 getCache_projScreenMatrix() {
		return cache_projScreenMatrix;
	}

	@Deprecated
	public Vector4 getCache_vector3() {
		return cache_vector3;
	}
}
