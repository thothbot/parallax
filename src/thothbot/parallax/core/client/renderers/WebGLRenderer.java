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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.client.context.Canvas3d;
import thothbot.parallax.core.client.events.HasEventBus;
import thothbot.parallax.core.client.events.ViewportResizeEvent;
import thothbot.parallax.core.client.gl2.WebGLConstants;
import thothbot.parallax.core.client.gl2.WebGLExtension;
import thothbot.parallax.core.client.gl2.WebGLFramebuffer;
import thothbot.parallax.core.client.gl2.WebGLProgram;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.WebGLShaderPrecisionFormat;
import thothbot.parallax.core.client.gl2.WebGLUniformLocation;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Uint8Array;
import thothbot.parallax.core.client.gl2.enums.BeginMode;
import thothbot.parallax.core.client.gl2.enums.BlendEquationMode;
import thothbot.parallax.core.client.gl2.enums.BlendingFactorDest;
import thothbot.parallax.core.client.gl2.enums.BlendingFactorSrc;
import thothbot.parallax.core.client.gl2.enums.BufferTarget;
import thothbot.parallax.core.client.gl2.enums.BufferUsage;
import thothbot.parallax.core.client.gl2.enums.ClearBufferMask;
import thothbot.parallax.core.client.gl2.enums.CullFaceMode;
import thothbot.parallax.core.client.gl2.enums.DataType;
import thothbot.parallax.core.client.gl2.enums.DepthFunction;
import thothbot.parallax.core.client.gl2.enums.DrawElementsType;
import thothbot.parallax.core.client.gl2.enums.EnableCap;
import thothbot.parallax.core.client.gl2.enums.FrontFaceDirection;
import thothbot.parallax.core.client.gl2.enums.PixelStoreParameter;
import thothbot.parallax.core.client.gl2.enums.ShaderPrecisionSpecifiedTypes;
import thothbot.parallax.core.client.gl2.enums.Shaders;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
import thothbot.parallax.core.client.gl2.enums.TextureTarget;
import thothbot.parallax.core.client.gl2.enums.TextureUnit;
import thothbot.parallax.core.client.gl2.extension.ExtTextureFilterAnisotropic;
import thothbot.parallax.core.client.gl2.extension.WebGLCompressedTextureS3tc;
import thothbot.parallax.core.client.renderers.WebGLExtensions.Id;
import thothbot.parallax.core.client.shaders.Attribute;
import thothbot.parallax.core.client.shaders.ProgramParameters;
import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.client.shaders.Uniform.TYPE;
import thothbot.parallax.core.client.textures.CompressedTexture;
import thothbot.parallax.core.client.textures.CubeTexture;
import thothbot.parallax.core.client.textures.DataTexture;
import thothbot.parallax.core.client.textures.RenderTargetCubeTexture;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.cameras.HasNearFar;
import thothbot.parallax.core.shared.core.AbstractGeometry;
import thothbot.parallax.core.shared.core.BufferAttribute;
import thothbot.parallax.core.shared.core.BufferGeometry;
import thothbot.parallax.core.shared.core.BufferGeometry.DrawCall;
import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.FastMap;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryGroup;
import thothbot.parallax.core.shared.core.GeometryObject;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.lights.DirectionalLight;
import thothbot.parallax.core.shared.lights.HemisphereLight;
import thothbot.parallax.core.shared.lights.Light;
import thothbot.parallax.core.shared.lights.PointLight;
import thothbot.parallax.core.shared.lights.ShadowLight;
import thothbot.parallax.core.shared.lights.SpotLight;
import thothbot.parallax.core.shared.materials.HasEnvMap;
import thothbot.parallax.core.shared.materials.HasFog;
import thothbot.parallax.core.shared.materials.HasSkinning;
import thothbot.parallax.core.shared.materials.HasWireframe;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.materials.MeshFaceMaterial;
import thothbot.parallax.core.shared.materials.MeshLambertMaterial;
import thothbot.parallax.core.shared.materials.MeshPhongMaterial;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Frustum;
import thothbot.parallax.core.shared.math.Mathematics;
import thothbot.parallax.core.shared.math.Matrix3;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.math.Vector4;
import thothbot.parallax.core.shared.objects.Line;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.objects.PointCloud;
import thothbot.parallax.core.shared.objects.SkinnedMesh;
import thothbot.parallax.core.shared.scenes.AbstractFog;
import thothbot.parallax.core.shared.scenes.FogExp2;
import thothbot.parallax.core.shared.scenes.Scene;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;

/**
 * The WebGL renderer displays your beautifully crafted {@link Scene}s using WebGL, if your device supports it.
 */
public class WebGLRenderer extends AbstractRenderer implements HasEventBus
{
	// The HTML5 Canvas's 'webgl' context obtained from the canvas where the renderer will draw.
	private WebGLRenderingContext gl;

	private WebGlRendererInfo info;
					
	private List<Light> lights = new ArrayList<Light>();
	
	public Map<String, List<WebGLObject>> _webglObjects = GWT.isScript() ? 
			new FastMap<List<WebGLObject>>() : new HashMap<String, List<WebGLObject>>();	

	public List<WebGLObject> _webglObjectsImmediate  = new ArrayList<WebGLObject>();

	public List<WebGLObject> opaqueObjects = new ArrayList<WebGLObject>();
	public List<WebGLObject> transparentObjects = new ArrayList<WebGLObject>();
	
	public double devicePixelRatio = _devicePixelRatio();	
	public final native double _devicePixelRatio() /*-{
    	return self.devicePixelRatio !==null ? self.devicePixelRatio : 1.0;
  	}-*/; 
	
	public boolean _logarithmicDepthBuffer = false;

	// ---- Properties ------------------------------------
	
	public Shader.PRECISION _precision = Shader.PRECISION.HIGHP;
	
	// clearing
	private boolean autoClearColor = true;
	private boolean autoClearDepth = true;
	private boolean autoClearStencil = true;

	// scene graph
	private boolean sortObjects = true;
	
	// physically based shading
	private boolean gammaInput = false;
	private boolean gammaOutput = false;

	// shadow map

//	private boolean shadowMapEnabled = false;
//	shadowMapType = PCFShadowMap;
	private CullFaceMode shadowMapCullFace = CullFaceMode.FRONT;
	private boolean shadowMapDebug = false;
	private boolean shadowMapCascade = false;
	
	// morphs
	private int maxMorphTargets = 8;
	private int maxMorphNormals = 4;

	// flags
	private boolean autoScaleCubemaps = true;

	// ---- Internal properties ----------------------------
	
	public Map<String, Shader> _programs;
	
	private WebGLProgram _currentProgram = null;
	private WebGLFramebuffer _currentFramebuffer = null;
	private int _currentMaterialId = -1;
	private int _currentGeometryGroupHash = -1;
	private Camera _currentCamera = null;	
	
	private int _usedTextureUnits = 0;
	
	// GL state cache
	
	private Material.SIDE _oldDoubleSided = null;
	private Material.SIDE _oldFlipSided = null;
	private Material.SIDE cache_oldMaterialSided = null;

	private Material.BLENDING _oldBlending = null;
	
	private BlendEquationMode _oldBlendEquation = null;
	private BlendingFactorSrc _oldBlendSrc = null;
	private BlendingFactorDest _oldBlendDst = null;
	
	private Boolean _oldDepthTest = null;
	private Boolean _oldDepthWrite = null;

	private Boolean _oldPolygonOffset = null;
	private Double _oldPolygonOffsetFactor = null;
	private Double _oldPolygonOffsetUnits = null;
	
//	_oldLineWidth = null,

	private int _viewportX = 0;
	private int _viewportY = 0;
	private int _viewportWidth = 0;
	private int _viewportHeight = 0;
	private int _currentWidth = 0;
	private int _currentHeight = 0;

	private Uint8Array _newAttributes = Uint8Array.create( 16 );
	private Uint8Array _enabledAttributes = Uint8Array.create( 16 );

	// frustum
	public Frustum _frustum = new Frustum();

	 // camera matrices cache

	public Matrix4 _projScreenMatrix = new Matrix4();
	public Matrix4 _projScreenMatrixPS = new Matrix4();

	public Vector3 _vector3 = new Vector3();
	
	// light arrays cache
	private Vector3 _direction = new Vector3();

	private boolean _lightsNeedUpdate = true;

	private RendererLights _lights;
		
	private List<Plugin> plugins;
		
//	var sprites = [];
//	var lensFlares = [];

	// GPU capabilities
	private int _maxTextures;
	private int _maxVertexTextures;
	private int _maxTextureSize;
	private int _maxCubemapSize;

	private boolean _supportsVertexTextures;
	private boolean _supportsBoneTextures;
	
	private WebGLShaderPrecisionFormat _vertexShaderPrecisionHighpFloat;
	private WebGLShaderPrecisionFormat _vertexShaderPrecisionMediumpFloat;
	private WebGLShaderPrecisionFormat _vertexShaderPrecisionLowpFloat;

	private WebGLShaderPrecisionFormat _fragmentShaderPrecisionHighpFloat;
	private WebGLShaderPrecisionFormat _fragmentShaderPrecisionMediumpFloat;
	private WebGLShaderPrecisionFormat _fragmentShaderPrecisionLowpFloat;
		
//	private OESTextureFloat GLExtensionTextureFloat;
//	private OESStandardDerivatives GLExtensionStandardDerivatives;
//	private ExtTextureFilterAnisotropic GLExtensionTextureFilterAnisotropic;
//	private WebGLCompressedTextureS3tc GLExtensionCompressedTextureS3TC;
	
	// clamp precision to maximum available
	private boolean highpAvailable;
	private boolean mediumpAvailable;
		
	private boolean isAutoUpdateObjects = true;
	private boolean isAutoUpdateScene = true;

	/**
	 * The constructor will create renderer for the {@link Canvas3d} widget.
	 * 
	 * @param gl     the {@link WebGLRenderingContext}
	 * @param width  the viewport width
	 * @param height the viewport height
	 */
	public WebGLRenderer(WebGLRenderingContext gl, int width, int height)
	{
		this.gl = gl;

		this.setInfo(new WebGlRendererInfo());
		
		this._lights           = new RendererLights();
		this._programs         = GWT.isScript() ? 
				new FastMap<Shader>() : new HashMap<String, Shader>();
			
		this._maxTextures       = gl.getParameteri(WebGLConstants.MAX_TEXTURE_IMAGE_UNITS);
		this._maxVertexTextures = gl.getParameteri(WebGLConstants.MAX_VERTEX_TEXTURE_IMAGE_UNITS);
		this._maxTextureSize    = gl.getParameteri(WebGLConstants.MAX_TEXTURE_SIZE);
		this._maxCubemapSize    = gl.getParameteri(WebGLConstants.MAX_CUBE_MAP_TEXTURE_SIZE);

		this._supportsVertexTextures = ( this._maxVertexTextures > 0 ); 
		this._supportsBoneTextures = this._supportsVertexTextures && WebGLExtensions.get(gl, WebGLExtensions.Id.OES_texture_float) != null ;
		
		this._vertexShaderPrecisionHighpFloat = gl.getShaderPrecisionFormat( Shaders.VERTEX_SHADER, ShaderPrecisionSpecifiedTypes.HIGH_FLOAT );
		this._vertexShaderPrecisionMediumpFloat = gl.getShaderPrecisionFormat( Shaders.VERTEX_SHADER, ShaderPrecisionSpecifiedTypes.MEDIUM_FLOAT );
		this._vertexShaderPrecisionLowpFloat = gl.getShaderPrecisionFormat( Shaders.VERTEX_SHADER, ShaderPrecisionSpecifiedTypes.LOW_FLOAT );

		this._fragmentShaderPrecisionHighpFloat = gl.getShaderPrecisionFormat( Shaders.FRAGMENT_SHADER, ShaderPrecisionSpecifiedTypes.HIGH_FLOAT );
		this._fragmentShaderPrecisionMediumpFloat = gl.getShaderPrecisionFormat( Shaders.FRAGMENT_SHADER, ShaderPrecisionSpecifiedTypes.MEDIUM_FLOAT );
		this._fragmentShaderPrecisionLowpFloat = gl.getShaderPrecisionFormat( Shaders.FRAGMENT_SHADER, ShaderPrecisionSpecifiedTypes.LOW_FLOAT );
		
		this.highpAvailable = _vertexShaderPrecisionHighpFloat.getPrecision() > 0 && _fragmentShaderPrecisionHighpFloat.getPrecision() > 0;
		this.mediumpAvailable = _vertexShaderPrecisionMediumpFloat.getPrecision() > 0 && _fragmentShaderPrecisionMediumpFloat.getPrecision() > 0;
		
		if ( this._precision == Shader.PRECISION.HIGHP && ! highpAvailable ) {

			if ( mediumpAvailable ) {

				this._precision = Shader.PRECISION.MEDIUMP;
				Log.warn( "WebGLRenderer: highp not supported, using mediump." );

			} else {

				this._precision = Shader.PRECISION.LOWP;
				Log.warn( "WebGLRenderer: highp and mediump not supported, using lowp." );

			}

		}

		if ( this._precision == Shader.PRECISION.MEDIUMP && ! mediumpAvailable ) {

			this._precision = Shader.PRECISION.LOWP;
			Log.warn( "THREE.WebGLRenderer: mediump not supported, using lowp." );
		}

				
		WebGLExtensions.get(gl, WebGLExtensions.Id.OES_texture_float);
		WebGLExtensions.get(gl, WebGLExtensions.Id.OES_texture_float_linear);
		WebGLExtensions.get(gl, WebGLExtensions.Id.OES_standard_derivatives);
		
		if ( _logarithmicDepthBuffer ) 
		{
			WebGLExtensions.get(gl, WebGLExtensions.Id.EXT_frag_depth);
		}
		
		WebGLCompressedTextureS3tc GLExtensionCompressedTextureS3TC = (WebGLCompressedTextureS3tc) gl.getExtension( "WEBGL_compressed_texture_s3tc" );
		if(GLExtensionCompressedTextureS3TC == null)
			GLExtensionCompressedTextureS3TC = (WebGLCompressedTextureS3tc) gl.getExtension( "MOZ_WEBGL_compressed_texture_s3tc" );
		if(GLExtensionCompressedTextureS3TC == null)
			GLExtensionCompressedTextureS3TC = (WebGLCompressedTextureS3tc) gl.getExtension( "WEBKIT_WEBGL_compressed_texture_s3tc" );
		if(GLExtensionCompressedTextureS3TC == null)
			Log.warn( "WebGLRenderer: S3TC compressed textures not supported." );

		setSize(width, height);
		setDefaultGLState();
		
		// default plugins (order is important)
		this.plugins = new ArrayList<Plugin>();
	}

	public void addPlugin(Plugin plugin)
	{
		deletePlugin(plugin);
		this.plugins.add( plugin );
	}
	
	public void deletePlugin(Plugin plugin)
	{
		if(plugin == null)
			return;

		if(this.plugins.remove( plugin )) {
			plugin.deallocate();
		}
	}

	public boolean supportsVertexTextures()
	{
		return this._maxVertexTextures > 0;
	}
	
	public boolean supportsFloatTextures() 
	{
		return WebGLExtensions.get( this.gl, Id.OES_texture_float ) != null;
	}

	public boolean supportsStandardDerivatives() 
	{
		return WebGLExtensions.get( this.gl, Id.OES_standard_derivatives ) != null;
	}

	public boolean supportsCompressedTextureS3TC() 
	{
		return WebGLExtensions.get( this.gl, Id.WEBGL_compressed_texture_s3tc ) != null;
	}

	public boolean supportsCompressedTexturePVRTC() 
	{
		return WebGLExtensions.get( this.gl, Id.WEBGL_compressed_texture_pvrtc ) != null;
	}

	public boolean supportsBlendMinMax() 
	{
		return WebGLExtensions.get( this.gl, Id.EXT_blend_minmax ) != null;
	}

	public int getMaxAnisotropy() 
	{
		WebGLExtension extension = WebGLExtensions.get( this.gl, Id.EXT_texture_filter_anisotropic ); 

		return extension != null ? getGL().getParameteri(ExtTextureFilterAnisotropic.MAX_TEXTURE_MAX_ANISOTROPY_EXT) : 0;
	}

	public Shader.PRECISION getPrecision() {
		return this._precision;
	}
	
	/**
	 * Gets {@link #setAutoClearColor(boolean)} flag.
	 */
	public boolean isAutoClearColor() {
		return autoClearColor;
	}

	/**
	 * Defines whether the renderer should clear the color buffer. 
	 * Default is true.
	 * 
	 * @param isAutoClearColor false or true
	 */
	public void setAutoClearColor(boolean isAutoClearColor) {
		this.autoClearColor = isAutoClearColor;
	}


	/**
	 * Gets {@link #setAutoClearDepth(boolean)} flag.
	 */
	public boolean isAutoClearDepth() {
		return autoClearDepth;
	}

	/**
	 * Defines whether the renderer should clear the depth buffer. 
	 * Default is true.
	 * 
	 * @param isAutoClearDepth false or true
	 */
	public void setAutoClearDepth(boolean isAutoClearDepth) {
		this.autoClearDepth = isAutoClearDepth;
	}

	/**
	 * Gets {@link #setAutoClearStencil(boolean)} flag.
	 */
	public boolean isAutoClearStencil() {
		return autoClearStencil;
	}

	/**
	 * Defines whether the renderer should clear the stencil buffer. 
	 * Default is true.
	 * 
	 * @param isAutoClearStencil false or true
	 */
	public void setAutoClearStencil(boolean isAutoClearStencil) {
		this.autoClearStencil = isAutoClearStencil;
	}

	/**
	 * Gets {@link #setSortObjects(boolean)} flag.
	 */
	public boolean isSortObjects() {
		return sortObjects;
	}

	/**
	 * Defines whether the renderer should sort objects. 
	 * Default is true.
	 * 
	 * @param isSortObjects false or true
	 */
	public void setSortObjects(boolean isSortObjects) {
		this.sortObjects = isSortObjects;
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
		return this.gammaInput;
	}
	
	public void setGammaInput(boolean isGammaInput) {
		this.gammaInput = isGammaInput;
	}
	
	public boolean isGammaOutput() {
		return this.gammaOutput;
	}
	
	public void setGammaOutput(boolean isGammaOutput) {
		this.gammaOutput = isGammaOutput;
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
	 * Gets {@link WebGlRendererInfo} instance with debug information.
	 * 
	 * @return the {@link WebGlRendererInfo} instance
	 */
	public WebGlRendererInfo getInfo() {
		return info;
	}
	
	private void setInfo(WebGlRendererInfo info) {
		this.info = info;
	}

	/**
	 * Gets the WebGL context from the {@link Canvas3d} widget.
	 * 
	 * @return the underlying context implementation for drawing onto the
	 *         {@link Canvas3d}.
	 */
	public WebGLRenderingContext getGL()
	{
		return this.gl;
	}

	private void setDefaultGLState() 
	{
		getGL().clearColor( 0.0, 0.0, 0.0, 1.0 );
		getGL().clearDepth( 1 );
		getGL().clearStencil( 0 );

		getGL().enable( EnableCap.DEPTH_TEST );
		getGL().depthFunc( DepthFunction.LEQUAL );

		getGL().frontFace( FrontFaceDirection.CCW );
		getGL().cullFace( CullFaceMode.BACK );
		getGL().enable( EnableCap.CULL_FACE );

		getGL().enable( EnableCap.BLEND );
		getGL().blendEquation( BlendEquationMode.FUNC_ADD );
		getGL().blendFunc( BlendingFactorSrc.SRC_ALPHA, BlendingFactorDest.ONE_MINUS_SRC_ALPHA );
		
		getGL().viewport( _viewportX, _viewportY, _viewportWidth, _viewportHeight );
		getGL().clearColor( clearColor.getR(), clearColor.getG(), clearColor.getB(), clearAlpha );
	}

	/**
	 * Return a Boolean true if the context supports vertex textures.
	 */

	/**
	 * Sets the sizes and also sets {@link #setViewport(int, int, int, int)} size.
	 * 
	 * @param width  the {@link Canvas3d} width.
	 * @param height the {@link Canvas3d} height.
	 */
	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height);

		setViewport(0, 0, width, height);
		
		EVENT_BUS.fireEvent(new ViewportResizeEvent(this));
	}

	/**
	 * Sets the viewport to render from (X, Y) to (X + absoluteWidth, Y + absoluteHeight).
	 * By default X and Y = 0.
	 */
	public void setViewport(int x, int y, int width, int height)
	{
		this._viewportX = x;
		this._viewportY = y;

		this._viewportWidth = width;
		this._viewportHeight = height;

		getGL().viewport(this._viewportX, this._viewportY, this._viewportWidth, this._viewportHeight);
	}
	
	/**
	 * Sets the scissor area from (x, y) to (x + absoluteWidth, y + absoluteHeight).
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
			getGL().enable(EnableCap.SCISSOR_TEST);
		else
			getGL().disable(EnableCap.SCISSOR_TEST);
	}

	@Override
	public void setClearColor( Color color, double alpha ) 
	{
		this.clearColor.copy(color);
		this.clearAlpha = alpha;

		getGL().clearColor( this.clearColor.getR(), this.clearColor.getG(), this.clearColor.getB(), this.clearAlpha );
	}

	@Override
	public void clear() 
	{
		clear(true, true, true);
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

		if ( color ) bits |= ClearBufferMask.COLOR_BUFFER_BIT.getValue();
		if ( depth ) bits |= ClearBufferMask.DEPTH_BUFFER_BIT.getValue();
		if ( stencil ) bits |= ClearBufferMask.STENCIL_BUFFER_BIT.getValue();

		getGL().clear( bits );
	}
	
	public void clearColor() 
	{
		getGL().clear( ClearBufferMask.COLOR_BUFFER_BIT.getValue() );
	}

	public void clearDepth() 
	{
		getGL().clear( ClearBufferMask.DEPTH_BUFFER_BIT.getValue() );
	}

	public void clearStencil() 
	{
		getGL().clear( ClearBufferMask.STENCIL_BUFFER_BIT.getValue() );
	}

	/**
	 * Clear {@link RenderTargetTexture} and GL buffers.
	 */
	public void clearTarget( RenderTargetTexture renderTarget, boolean color, boolean depth, boolean stencil ) 
	{
		setRenderTarget( renderTarget );
		clear( color, depth, stencil );
	}
	
	public void resetGLState() 
	{
		_currentProgram = null;
		_currentCamera = null;

		_oldBlending = null;
		_oldDepthTest = null;
		_oldDepthWrite = null;
		_oldDoubleSided = null;
		_oldFlipSided = null;
		_currentGeometryGroupHash = -1;
		_currentMaterialId = -1;

		_lightsNeedUpdate = true;
	}
	
	private void initAttributes() {

		for ( int i = 0, l = _newAttributes.getLength(); i < l; i ++ ) {

			_newAttributes.set( i, 0);

		}

	}

	private void enableAttribute( Integer attribute ) {

		_newAttributes.set( attribute,  1);

		if ( _enabledAttributes.get( attribute ) == 0 ) {

			getGL().enableVertexAttribArray( attribute );
			_enabledAttributes.set( attribute, 1);

		}

	}

	private void  disableUnusedAttributes() {

		for ( int i = 0, l = _enabledAttributes.getLength(); i < l; i ++ ) {

			if ( _enabledAttributes.get( i ) != _newAttributes.get( i ) ) {

				getGL().disableVertexAttribArray( i );
				_enabledAttributes.set( i, 0);

			}

		}

	}

	
	/**
	 * Morph Targets Buffer initialization
	 */
	private void setupMorphTargets ( Material material, WebGLGeometry geometrybuffer, Mesh object ) 
	{

		// set base
		Map<String, Integer> attributes = material.getShader().getAttributesLocations();
		Map<String, Uniform> uniforms = material.getShader().getUniforms();

		if ( object.morphTargetBase != - 1 && attributes.get("position") >= 0) 
		{
			getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, geometrybuffer.__webglMorphTargetsBuffers.get( object.morphTargetBase ) );
			enableAttribute( attributes.get("position") );
			getGL().vertexAttribPointer( attributes.get("position"), 3, DataType.FLOAT, false, 0, 0 );

		} 
		else if ( attributes.get("position") >= 0 ) 
		{
			getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, geometrybuffer.__webglVertexBuffer );
			enableAttribute( attributes.get("position") );
			getGL().vertexAttribPointer( attributes.get("position"), 3, DataType.FLOAT, false, 0, 0 );
		}

		if ( object.morphTargetForcedOrder.size() > 0 ) 
		{
			// set forced order

			int m = 0;
			List<Integer> order = object.morphTargetForcedOrder;
			List<Double> influences = object.morphTargetInfluences;

			while ( material instanceof HasSkinning 
					&& m < ((HasSkinning)material).getNumSupportedMorphTargets() 
					&& m < order.size() 
			) {
				if ( attributes.get("morphTarget" + m )  >= 0 ) 
				{
					gl.bindBuffer( BufferTarget.ARRAY_BUFFER, geometrybuffer.__webglMorphTargetsBuffers.get( order.get( m ) ) );
					enableAttribute( attributes.get("morphTarget" + m ) );
					gl.vertexAttribPointer( attributes.get("morphTarget" + m) , 3, DataType.FLOAT, false, 0, 0 );
					
				}

				if (  attributes.get("morphNormal" + m )  >= 0 && material instanceof HasSkinning && ((HasSkinning)material).isMorphNormals()) 
				{
					gl.bindBuffer( BufferTarget.ARRAY_BUFFER, geometrybuffer.__webglMorphNormalsBuffers.get( order.get( m ) ) );
					enableAttribute( attributes.get("morphNormal" + m ));
					gl.vertexAttribPointer( attributes.get("morphNormal" + m ), 3, DataType.FLOAT, false, 0, 0 );
				}

				object.__webglMorphTargetInfluences.set( m , influences.get( order.get( m ) ));

				m ++;
			}
		}
		else 
		{
			// find most influencing

			List<Double> influences = object.morphTargetInfluences;
			List<Double[]> activeInfluenceIndices = new ArrayList<Double[]>();
			
			for ( int i = 0; i < influences.size(); i ++ ) {

				double influence = influences.get( i );

				if ( influence > 0 ) {

					Double[] tmp = new Double[]{influence, (double)i};
					activeInfluenceIndices.add( tmp );

				}

			}
			
			if ( activeInfluenceIndices.size() > ((HasSkinning)material).getNumSupportedMorphTargets() ) {
			
				Collections.sort(activeInfluenceIndices, new Comparator<Double[]>() {
					   public int compare(Double[] a, Double[] b) {
						   return (int)(b[ 0 ] - a[ 0 ]);
					   }
					});


			} else if ( activeInfluenceIndices.size() > ((HasSkinning)material).getNumSupportedMorphNormals() ) {
			
				Collections.sort(activeInfluenceIndices, new Comparator<Double[]>() {
					   public int compare(Double[] a, Double[] b) {
						   return (int)(b[ 0 ] - a[ 0 ]);
					   }
					});

			} else if ( activeInfluenceIndices.size() == 0 ) {

				activeInfluenceIndices.add(  new Double[]{0.0, 0.0} );

			}

			int influenceIndex;
			int m = 0;

			while ( material instanceof HasSkinning 
					&& m < ((HasSkinning)material).getNumSupportedMorphTargets() ) 
			{
				
				if ( activeInfluenceIndices.size() > m && activeInfluenceIndices.get( m ) != null ) 
				{
					influenceIndex = activeInfluenceIndices.get( m )[ 1 ].intValue();

					if ( attributes.get( "morphTarget" + m ) >= 0 ) {

						gl.bindBuffer( BufferTarget.ARRAY_BUFFER, geometrybuffer.__webglMorphTargetsBuffers.get( influenceIndex ) );
						enableAttribute( attributes.get( "morphTarget" + m ) );
						gl.vertexAttribPointer( attributes.get( "morphTarget" + m ), 3, DataType.FLOAT, false, 0, 0 );

					}

					if ( attributes.get( "morphNormal" + m ) >= 0 && ((HasSkinning)material).isMorphNormals() ) {

						gl.bindBuffer( BufferTarget.ARRAY_BUFFER, geometrybuffer.__webglMorphNormalsBuffers.get( influenceIndex ) );
						enableAttribute( attributes.get( "morphNormal" + m ) );
						gl.vertexAttribPointer( attributes.get( "morphNormal" + m ), 3, DataType.FLOAT, false, 0, 0 );

					}

					object.__webglMorphTargetInfluences.set( m, influences.get( influenceIndex ));

				} else {

					/*
					_gl.vertexAttribPointer( attributes[ "morphTarget" + m ], 3, _gl.FLOAT, false, 0, 0 );

					if ( material.morphNormals ) {

						_gl.vertexAttribPointer( attributes[ "morphNormal" + m ], 3, _gl.FLOAT, false, 0, 0 );

					}
					*/

					object.__webglMorphTargetInfluences.set( m, 0);

				}
				
				m ++;
			}
		}

		// load updated influences uniform
		if( uniforms.get("morphTargetInfluences").getLocation() != null ) 
		{
			Float32Array vals = object.__webglMorphTargetInfluences;
			getGL().uniform1fv( uniforms.get("morphTargetInfluences").getLocation(), vals );
		}
	}
	
	public void renderBufferImmediate( GeometryObject object, Shader program, Material material ) {

		initAttributes();
//
//		if ( object.hasPositions && ! object.__webglVertexBuffer ) object.__webglVertexBuffer = getGL().createBuffer();
//		if ( object.hasNormals && ! object.__webglNormalBuffer ) object.__webglNormalBuffer = getGL().createBuffer();
//		if ( object.hasUvs && ! object.__webglUvBuffer ) object.__webglUvBuffer = getGL().createBuffer();
//		if ( object.hasColors && ! object.__webglColorBuffer ) object.__webglColorBuffer = getGL().createBuffer();
//
//		if ( object.hasPositions )
//		{
//
//			getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, object.__webglVertexBuffer );
//			getGL().bufferData( BufferTarget.ARRAY_BUFFER, object.positionArray, BufferUsage.DYNAMIC_DRAW );
//			enableAttribute( program.attributes.position );
//			getGL().vertexAttribPointer( program.attributes.position, 3, getGL().FLOAT, false, 0, 0 );
//
//		}
//
//		if ( object.hasNormals ) {
//
//			getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, object.__webglNormalBuffer );
//
//			if (((HasShading)material).getShading() == Material.SHADING.FLAT ) {
//
//				var nx, ny, nz,
//					nax, nbx, ncx, nay, nby, ncy, naz, nbz, ncz,
//					normalArray,
//					i, il = object.count * 3;
//
//				for ( int i = 0; i < il; i += 9 ) {
//
//					normalArray = object.normalArray;
//
//					nax  = normalArray[ i ];
//					nay  = normalArray[ i + 1 ];
//					naz  = normalArray[ i + 2 ];
//
//					nbx  = normalArray[ i + 3 ];
//					nby  = normalArray[ i + 4 ];
//					nbz  = normalArray[ i + 5 ];
//
//					ncx  = normalArray[ i + 6 ];
//					ncy  = normalArray[ i + 7 ];
//					ncz  = normalArray[ i + 8 ];
//
//					nx = ( nax + nbx + ncx ) / 3;
//					ny = ( nay + nby + ncy ) / 3;
//					nz = ( naz + nbz + ncz ) / 3;
//
//					normalArray[ i ]   = nx;
//					normalArray[ i + 1 ] = ny;
//					normalArray[ i + 2 ] = nz;
//
//					normalArray[ i + 3 ] = nx;
//					normalArray[ i + 4 ] = ny;
//					normalArray[ i + 5 ] = nz;
//
//					normalArray[ i + 6 ] = nx;
//					normalArray[ i + 7 ] = ny;
//					normalArray[ i + 8 ] = nz;
//
//				}
//
//			}
//
//			getGL().bufferData( BufferTarget.ARRAY_BUFFER, object.normalArray, BufferUsage.DYNAMIC_DRAW );
//			enableAttribute( program.attributes.normal );
//			getGL().vertexAttribPointer( program.attributes.normal, 3, getGL().FLOAT, false, 0, 0 );
//
//		}
//
//		if ( object.hasUvs && material.map ) {
//
//			getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, object.__webglUvBuffer );
//			getGL().bufferData( BufferTarget.ARRAY_BUFFER, object.uvArray, BufferUsage.DYNAMIC_DRAW );
//			enableAttribute( program.attributes.uv );
//			getGL().vertexAttribPointer( program.attributes.uv, 2, DataType.FLOAT, false, 0, 0 );
//
//		}
//
//		if ( object.hasColors && ((HasVertexColors)material).isVertexColors != Material.COLORS.NO ) {
//
//			getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, object.__webglColorBuffer );
//			getGL().bufferData( BufferTarget.ARRAY_BUFFER, object.colorArray, BufferUsage.DYNAMIC_DRAW );
//			enableAttribute( program.attributes.color );
//			getGL().vertexAttribPointer( program.attributes.color, 3, DataType.FLOAT, false, 0, 0 );
//
//		}
//
//		disableUnusedAttributes();
//
//		getGL().drawArrays( BeginMode.TRIANGLES, 0, object.count );
//
//		object.count = 0;

	}
	
	private void setupVertexAttributes( Material material, Shader program, BufferGeometry geometry, int startIndex ) {

		Map<String, BufferAttribute> geometryAttributes = geometry.getAttributes();

		Map<String, Integer> programAttributes = program.getAttributesLocations();

		for ( String key: programAttributes.keySet()) {

			 Integer programAttribute = programAttributes.get(key);
		
			if ( programAttribute >= 0 ) {

				BufferAttribute geometryAttribute = geometryAttributes.get( key );

				if ( geometryAttribute != null ) {

					int size = geometryAttribute.getItemSize();

					gl.bindBuffer( BufferTarget.ARRAY_BUFFER, geometryAttribute.getBuffer() );

					enableAttribute( programAttribute );

					gl.vertexAttribPointer( programAttribute, size, DataType.FLOAT, false, 0, startIndex * size * 4 ); // 4 bytes per Float32

				}
//				else if ( material.defaultAttributeValues != null ) {
//
//					if ( material.defaultAttributeValues[ key ].length === 2 ) {
//
//						gl.vertexAttrib2fv( programAttribute, material.defaultAttributeValues[ key ] );
//
//					} else if ( material.defaultAttributeValues[ key ].length === 3 ) {
//
//						gl.vertexAttrib3fv( programAttribute, material.defaultAttributeValues[ key ] );
//
//					}
//
//				}

			}

		}

		disableUnusedAttributes();

	}

	
	//camera, lights, fog, material, geometry, object
	public void renderBufferDirect( Camera camera, List<Light> lights, AbstractFog fog, Material material, BufferGeometry geometry, GeometryObject object ) 
	{
		if ( ! material.isVisible() ) 
			return;

		Shader program = setProgram( camera, lights, fog, material, object );

		Map<String, Integer> attributes = material.getShader().getAttributesLocations();
		
		boolean updateBuffers = false;
		int wireframeBit = material instanceof HasWireframe && ((HasWireframe)material).isWireframe() ? 1 : 0;

		int geometryGroupHash = ( geometry.getId() * 0xffffff ) + ( material.getShader().getId() * 2 ) + wireframeBit;

		if ( geometryGroupHash != this._currentGeometryGroupHash ) 
		{
			this._currentGeometryGroupHash = geometryGroupHash;
			updateBuffers = true;
		}
		
		if ( updateBuffers ) {

			initAttributes();

		}

		WebGLRenderingContext gl = getGL();
				
		// render mesh

		if ( object instanceof Mesh ) 
		{
			BeginMode mode = material instanceof HasWireframe && ((HasWireframe)material).isWireframe() ? BeginMode.LINES : BeginMode.TRIANGLES;
			
			BufferAttribute index = geometry.getAttribute("index");

			if(index != null)
			{
				DrawElementsType type = DrawElementsType.UNSIGNED_SHORT;
				int size = 2;

				List<BufferGeometry.DrawCall> offsets = geometry.getDrawcalls();

				if ( offsets.size() == 0 ) {

					if ( updateBuffers ) {

						setupVertexAttributes( material, program, geometry, 0 );

						getGL().bindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER, index.getBuffer() );

					}

					getGL().drawElements( mode, index.getArray().getLength(), type, 0 );

					this.info.getRender().calls ++;
					this.info.getRender().vertices += index.getArray().getLength(); // not really true, here vertices can be shared
					this.info.getRender().faces += index.getArray().getLength() / 3;

				} else {
					// if there is more than 1 chunk
					// must set attribute pointers to use new offsets for each chunk
					// even if geometry and materials didn't change

					updateBuffers = true;

					for ( int i = 0, il = offsets.size(); i < il; ++ i ) 
					{

						int startIndex = offsets.get( i ).index;

						if ( updateBuffers ) {

							setupVertexAttributes( material, program, geometry, startIndex );
							getGL().bindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER, index.getBuffer() );

						}

						gl.drawElements( mode,  offsets.get( i ).count, type, offsets.get( i ).start * size  );

						getInfo().getRender().calls ++;
						getInfo().getRender().vertices += offsets.get( i ).count; // not really true, here vertices can be shared
						getInfo().getRender().faces += offsets.get( i ).count / 3;
					}

				}
				
			} 
			else 
			{

				// non-indexed triangles

				if ( updateBuffers ) {

					setupVertexAttributes( material, program, geometry, 0 );

				}

				BufferAttribute position = geometry.getAttribute("position");

				// render non-indexed triangles

				gl.drawArrays( mode, 0, position.getArray().getLength() / 3 );

				this.info.getRender().calls ++;
				this.info.getRender().vertices += position.getArray().getLength() / 3;
				this.info.getRender().faces += position.getArray().getLength() / 9;

			}
			
		} 
		else if ( object instanceof PointCloud ) 
		{
			// render particles

			if ( updateBuffers ) {

				setupVertexAttributes( material, program, geometry, 0 );

			}

			BufferAttribute position = geometry.getAttribute("position");

			// render particles

			gl.drawArrays( BeginMode.POINTS, 0, position.getArray().getLength() / 3 );

			this.info.getRender().calls ++;
			this.info.getRender().points += position.getArray().getLength() / 3;
		} 
		else if ( object instanceof Line ) 
		{

			BeginMode mode = ( ((Line)object).getMode() == Line.MODE.STRIPS ) ? BeginMode.LINE_STRIP : BeginMode.LINES;
			object.setLineWidth(gl, ((LineBasicMaterial)material).getLinewidth());

			BufferAttribute index = geometry.getAttribute("index");

			if ( index != null ) {

				// indexed lines

//				var type, size;

//				if ( index.array instanceof Uint32Array ) {
//
//					type = _gl.UNSIGNED_INT;
//					size = 4;
//
//				} else {

					DrawElementsType type = DrawElementsType.UNSIGNED_SHORT;
					int size = 2;

//				}

				List<DrawCall> drawcalls = geometry.getDrawcalls();

				if ( drawcalls.size() == 0 ) {

					if ( updateBuffers ) {

						setupVertexAttributes( material, program, geometry, 0 );
						gl.bindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER, index.getBuffer() );

					}

					gl.drawElements( mode, index.getArray().getLength(), type, 0 ); // 2 bytes per Uint16Array

					this.info.getRender().calls ++;
					this.info.getRender().vertices += index.getArray().getLength(); // not really true, here vertices can be shared

				} else {

					// if there is more than 1 chunk
					// must set attribute pointers to use new offsets for each chunk
					// even if geometry and materials didn't change

					if ( drawcalls.size() > 1 ) updateBuffers = true;

					for ( int i = 0, il = drawcalls.size(); i < il; i ++ ) {

						int startIndex = drawcalls.get( i ).index;

						if ( updateBuffers ) {

							setupVertexAttributes( material, program, geometry, startIndex );
							gl.bindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER, index.getBuffer() );

						}

						// render indexed lines

						gl.drawElements( mode, drawcalls.get( i ).count, type, drawcalls.get( i ).start * size ); // 2 bytes per Uint16Array

						this.info.getRender().calls ++;
						this.info.getRender().vertices += drawcalls.get( i ).count; // not really true, here vertices can be shared

					}

				}

			} else {

				// non-indexed lines

				if ( updateBuffers ) {

					setupVertexAttributes( material, program, geometry, 0 );

				}

				BufferAttribute position = geometry.getAttribute("position");

				gl.drawArrays( mode, 0, position.getArray().getLength() / 3 );

				this.info.getRender().calls ++;
				this.info.getRender().points += position.getArray().getLength() / 3;

			}

		}
	}
	
	public List<GeometryGroup> makeGroups( Geometry geometry, boolean usesFaceMaterial ) {
		
		long maxVerticesInGroup = WebGLExtensions.get(gl, WebGLExtensions.Id.OES_element_index_uint) != null ? 4294967296L : 65535L;
		
		int numMorphTargets = geometry.getMorphTargets().size();
		int numMorphNormals = geometry.getMorphNormals().size();

		String groupHash;

		Map<String, Integer> hash_map = GWT.isScript() ? 
				new FastMap<Integer>() : new HashMap<String, Integer>(); 

		Map<String, GeometryGroup> groups = GWT.isScript() ? 
				new FastMap<GeometryGroup>() : new HashMap<String, GeometryGroup>();
				
		List<GeometryGroup> groupsList = new ArrayList<GeometryGroup>();

		for ( int f = 0, fl = geometry.getFaces().size(); f < fl; f ++ ) {

			Face3 face = geometry.getFaces().get( f );
			Integer materialIndex = usesFaceMaterial ? face.getMaterialIndex() : 0;

			if ( ! hash_map.containsKey(materialIndex) ) {

				hash_map.put(materialIndex.toString(), 0);

			}

			groupHash = materialIndex + "_" + hash_map.get( materialIndex );

			if ( ! groups.containsKey(groupHash) ) {

				GeometryGroup group = new GeometryGroup(materialIndex, numMorphTargets, numMorphNormals);
				
				groups.put(groupHash, group);
				groupsList.add( group );

			}

			if ( groups.get( groupHash ).getVertices() + 3 > maxVerticesInGroup ) {

				hash_map.put( materialIndex.toString(), hash_map.get( materialIndex ) + 1 );
				groupHash = materialIndex + "_" + hash_map.get( materialIndex );

				if ( ! groups.containsKey(groupHash) ) {

					GeometryGroup group = new GeometryGroup(materialIndex, numMorphTargets, numMorphNormals);
					groups.put(groupHash, group);
					groupsList.add( group );
					
				}

			}

			groups.get( groupHash ).getFaces3().add( f );
			groups.get( groupHash ).setVertices(groups.get( groupHash ).getVertices() + 3);

		}

		return groupsList;

	}
	
	public void initGeometryGroups( Object3D scene, Mesh object, Geometry geometry ) {

		Material material = object.getMaterial();
		boolean addBuffers = false;

		if ( GeometryGroup.geometryGroups.get( geometry.getId() + "" ) == null || geometry.isGroupsNeedUpdate() ) {

			this._webglObjects.put(object.getId() + "", new ArrayList<WebGLObject>());

			GeometryGroup.geometryGroups.put( geometry.getId() + "", makeGroups( geometry, material instanceof MeshFaceMaterial ));

			geometry.setGroupsNeedUpdate( false );

		}

		List<GeometryGroup> geometryGroupsList = GeometryGroup.geometryGroups.get( geometry.getId() + "" );

		// create separate VBOs per geometry chunk

		for ( int i = 0, il = geometryGroupsList.size(); i < il; i ++ ) {

			GeometryGroup geometryGroup = geometryGroupsList.get( i );

			// initialise VBO on the first access

			if ( geometryGroup.__webglVertexBuffer == null ) {

				((Mesh)object).createBuffers(this, geometryGroup);
				((Mesh)object).initBuffers(gl, geometryGroup);

				geometry.setVerticesNeedUpdate( true );
				geometry.setMorphTargetsNeedUpdate( true );
				geometry.setElementsNeedUpdate( true );
				geometry.setUvsNeedUpdate( true );
				geometry.setNormalsNeedUpdate( true );
				geometry.setTangentsNeedUpdate( true );
				geometry.setColorsNeedUpdate( true );

				addBuffers = true;

			} else {

				addBuffers = false;

			}

			if ( addBuffers || !object.__webglActive ) {

				addBuffer( geometryGroup, object );

			}

		}

		object.__webglActive = true;

	}

	private void initObject( Object3D object, Object3D scene ) {

		if ( !object.__webglInit ) {

			object.__webglInit = true;
			object._modelViewMatrix = new Matrix4();
			object._normalMatrix = new Matrix3();

		}

		AbstractGeometry geometry = object instanceof GeometryObject ? ((GeometryObject)object).getGeometry() : null;

		if ( geometry == null ) {

			// ImmediateRenderObject

		} else if ( ! geometry.__webglInit ) {

			geometry.__webglInit = true;
//			geometry.addEventListener( 'dispose', onGeometryDispose );

			if ( geometry instanceof BufferGeometry ) {

				//

			} else if ( object instanceof Mesh ) {

				initGeometryGroups( scene, (Mesh) object, (Geometry)geometry );

			} else if ( object instanceof Line ) {

				if ( geometry.__webglVertexBuffer == null ) {

					((Line)object).createBuffers(this);
					((Line)object).initBuffers(gl);

					geometry.setVerticesNeedUpdate( true );
					geometry.setColorsNeedUpdate( true );
					geometry.setLineDistancesNeedUpdate( true );

				}

			} else if ( object instanceof PointCloud ) {

				if ( geometry.__webglVertexBuffer == null ) {
					
					((PointCloud)object).createBuffers(this);
					((PointCloud)object).initBuffers(gl);

					geometry.setVerticesNeedUpdate( true );
					geometry.setColorsNeedUpdate( true );

				}

			}

		}

		if ( !object.__webglActive ) {

			object.__webglActive = true;

			if ( object instanceof Mesh ) {

				if ( geometry instanceof BufferGeometry ) {

					addBuffer( geometry, (GeometryObject) object );

				} else if ( geometry instanceof Geometry ) {

					List<GeometryGroup> geometryGroupsList = GeometryGroup.geometryGroups.get( geometry.getId() + "" );

					for ( int i = 0,l = geometryGroupsList.size(); i < l; i ++ ) {

						addBuffer( geometryGroupsList.get( i ), (GeometryObject) object );

					}

				}

			} else if ( object instanceof Line || object instanceof PointCloud ) {

				addBuffer( geometry, (GeometryObject) object );

			} /*else if ( object instanceof ImmediateRenderObject || object.immediateRenderCallback ) {

				addBufferImmediate( _webglObjectsImmediate, object );

			}*/

		}

	}
	
	private void addBuffer( WebGLGeometry buffer, GeometryObject object ) {

		int id = object.getId();
		List<WebGLObject> list = _webglObjects.get(id + "");
		if(list == null) {
			list = new ArrayList<WebGLObject>();
			_webglObjects.put(id + "", list);
		}
		
		WebGLObject webGLObject = new WebGLObject(buffer, object);
		webGLObject.id = id;
		list.add(webGLObject);
	}

	private void projectObject( Object3D scene, Object3D object ) {

		if ( object.isVisible() == false ) return;

		if ( object instanceof Scene /*|| object instanceof Group */) {

			// skip

		} else {

			if(!(object instanceof Light))
				initObject( object, scene );

			if ( object instanceof Light ) {

				lights.add( (Light) object );

			} /*else if ( object instanceof Sprite ) {

				sprites.push( object );

			} else if ( object instanceof LensFlare ) {

				lensFlares.push( object );

			} */else {

				List<WebGLObject> webglObjects = this._webglObjects.get( object.getId() + "" );	
			
				if ( webglObjects != null && ( object.isFrustumCulled() == false || _frustum.isIntersectsObject( (GeometryObject) object ) == true ) ) {

					updateObject( (GeometryObject) object, scene );

					for ( int i = 0, l = webglObjects.size(); i < l; i ++ ) {

						WebGLObject webglObject = webglObjects.get(i);

						webglObject.unrollBufferMaterial(this);

						webglObject.render = true;

						if ( this.sortObjects == true ) {

							if ( object.getRenderDepth() > 0 ) {

								webglObject.z = object.getRenderDepth();

							} else {

								_vector3.setFromMatrixPosition( object.getMatrixWorld() );
								_vector3.applyProjection( _projScreenMatrix );

								webglObject.z = _vector3.getZ();

							}

						}

					}

				}

			}

		}

		for ( int i = 0, l = object.getChildren().size(); i < l; i ++ ) {

			projectObject( scene, object.getChildren().get( i ) );

		}

	}
	
	public Material getBufferMaterial( GeometryObject object, GeometryGroup geometryGroup ) 
	{

		return object.getMaterial() instanceof MeshFaceMaterial
			 ? ((MeshFaceMaterial)object.getMaterial()).getMaterials().get( geometryGroup.getMaterialIndex() )
			 : object.getMaterial();

	}
	
	public Material getBufferMaterial( GeometryObject object, Geometry geometry ) {

		return object.getMaterial();

	}

	
	public void updateObject( GeometryObject object, Object3D scene ) 
	{
		AbstractGeometry geometry = object.getGeometry();
		
		Material material = null;

		if ( geometry instanceof BufferGeometry ) {

			((BufferGeometry)geometry).setDirectBuffers(gl);

		} else if ( object instanceof Mesh ) {

			// check all geometry groups

			if ( geometry.isGroupsNeedUpdate() ) {

				initGeometryGroups( scene, (Mesh)object, (Geometry)geometry );

			}

			List<GeometryGroup> geometryGroupsList = GeometryGroup.geometryGroups.get( geometry.getId() + "" );

			for ( int i = 0, il = geometryGroupsList.size(); i < il; i ++ ) {

				GeometryGroup geometryGroup = geometryGroupsList.get( i );

				material = getBufferMaterial( object, geometryGroup );

				if ( geometry.isGroupsNeedUpdate() ) {

					((Mesh)object).initBuffers( gl, geometryGroup );

				}

				boolean customAttributesDirty = (material instanceof ShaderMaterial) && ((ShaderMaterial)material).getShader().areCustomAttributesDirty();

				if ( geometry.isVerticesNeedUpdate() || geometry.isMorphTargetsNeedUpdate() || geometry.isElementsNeedUpdate() ||
					 geometry.isUvsNeedUpdate() || geometry.isNormalsNeedUpdate() ||
					 geometry.isColorsNeedUpdate() || geometry.isTangentsNeedUpdate() || customAttributesDirty ) {

					((Mesh)object).setBuffers( gl, geometryGroup, BufferUsage.DYNAMIC_DRAW, ! ((Geometry)geometry).isDynamic(), material );

				}

			}

			geometry.setVerticesNeedUpdate( false );
			geometry.setMorphTargetsNeedUpdate( false );
			geometry.setElementsNeedUpdate( false );
			geometry.setUvsNeedUpdate( false );
			geometry.setNormalsNeedUpdate( false );
			geometry.setColorsNeedUpdate( false );
			geometry.setTangentsNeedUpdate( false );

			if(material instanceof ShaderMaterial ) {
				((ShaderMaterial)material).getShader().clearCustomAttributes();
			}

		} else if ( object instanceof Line ) {

			material = getBufferMaterial( object, (Geometry)geometry );

			boolean customAttributesDirty = (material instanceof ShaderMaterial) && ((ShaderMaterial)material).getShader().areCustomAttributesDirty();

			if ( geometry.isVerticesNeedUpdate() || geometry.isColorsNeedUpdate() || geometry.isLineDistancesNeedUpdate() || customAttributesDirty ) {

				((Line)object).setBuffers( gl, BufferUsage.DYNAMIC_DRAW );

			}

			geometry.setVerticesNeedUpdate( false );
			geometry.setColorsNeedUpdate( false );
			geometry.setLineDistancesNeedUpdate( false );

			if(material instanceof ShaderMaterial ) {
				((ShaderMaterial)material).getShader().clearCustomAttributes();
			}


		} else if ( object instanceof PointCloud ) {

			material = getBufferMaterial( object, (Geometry)geometry );

			boolean customAttributesDirty = (material instanceof ShaderMaterial) && ((ShaderMaterial)material).getShader().areCustomAttributesDirty();

			if ( geometry.isVerticesNeedUpdate() || geometry.isColorsNeedUpdate() || ((PointCloud)object).isSortParticles() || customAttributesDirty ) {

				((PointCloud)object).setBuffers( this, BufferUsage.DYNAMIC_DRAW );

			}

			geometry.setVerticesNeedUpdate( false );
			geometry.setColorsNeedUpdate( false );

			if(material instanceof ShaderMaterial ) {
				((ShaderMaterial)material).getShader().clearCustomAttributes();
			}

		}

	}

	@Override
	public void render( Scene scene, Camera camera )
	{
		render(scene, camera, null);
	}

	public void render( Scene scene, Camera camera, RenderTargetTexture renderTarget )
	{
		render(scene, camera, renderTarget, false);
	}
	
	/**
	 * Rendering.
	 * 
	 * @param scene        the {@link Scene} object.
	 * @param renderTarget optional
	 * @param forceClear   optional
	 */
	public void render( Scene scene, Camera camera, RenderTargetTexture renderTarget, boolean forceClear ) 
	{		
		// Render basic plugins
		if(renderPlugins( this.plugins, scene, camera, Plugin.TYPE.BASIC_RENDER ))
			return;
		
		Log.debug("Called render()");
				
		AbstractFog fog = scene.getFog();

		// reset caching for this frame
		this._currentGeometryGroupHash = - 1;
		this._currentCamera = null;		
		this._currentMaterialId = -1;
		this._lightsNeedUpdate = true;
		
		if ( this.isAutoUpdateScene() ) 
		{
			scene.updateMatrixWorld(false);
		}

		// update camera matrices and frustum
		if ( camera.getParent() == null ) 
		{
			camera.updateMatrixWorld(false);
		}

		camera.getMatrixWorldInverse().getInverse( camera.getMatrixWorld() );
		
		_projScreenMatrix.multiply( camera.getProjectionMatrix(), camera.getMatrixWorldInverse() );
		_frustum.setFromMatrix( _projScreenMatrix );

		this.lights = new ArrayList<Light>();
		this.opaqueObjects = new ArrayList<WebGLObject>();
		this.transparentObjects = new ArrayList<WebGLObject>();

		projectObject( scene, scene );

		if ( this.isSortObjects() ) {
			
			Collections.sort(opaqueObjects, new Comparator<WebGLObject>() {

				@Override
				public int compare(WebGLObject a, WebGLObject b) {
					if ( a.z != b.z ) {
					
						return (int)(b.z - a.z);
		
					} else {
		
						return a.id - b.id;
		
					}
			
				}
			});
			
			Collections.sort(transparentObjects, new Comparator<WebGLObject>() {

				@Override
				public int compare(WebGLObject a, WebGLObject b) {
					if ( a.material.getId() != b.material.getId() ) {
				
						return a.material.getId() - b.material.getId();
		
					} else if ( a.z != b.z ) {
		
						return (int)(a.z - b.z);
		
					} else {
		
						return a.id - b.id;
		
					}
			
				}
			});

		}

		// custom render plugins (pre pass)
		renderPlugins( this.plugins, scene, camera, Plugin.TYPE.PRE_RENDER );

		this.getInfo().getRender().calls = 0;
		this.getInfo().getRender().vertices = 0;
		this.getInfo().getRender().faces = 0;
		this.getInfo().getRender().points = 0;

		setRenderTarget( renderTarget );

		if ( this.isAutoClear() || forceClear )
		{
			clear( this.isAutoClearColor(), this.isAutoClearDepth(), this.isAutoClearStencil() );
		}
		
		// set matrices for immediate objects

		for ( int i = 0, il = this._webglObjectsImmediate.size(); i < il; i ++ ) 
		{
			WebGLObject webglObject = this._webglObjectsImmediate.get( i );
			Object3D object = webglObject.object;

			if ( object.isVisible() ) {

				setupMatrices( object, camera );

				webglObject.unrollImmediateBufferMaterial();

			}

		}

		Log.debug("  -- render() overrideMaterial : " + (scene.getOverrideMaterial() != null)
				+ ", lights: " + lights.size()
				+ ", opaqueObjects: " + opaqueObjects.size()
				+ ", transparentObjects: " + transparentObjects.size() );
		
		if ( scene.getOverrideMaterial() != null ) 
		{			
			Material material = scene.getOverrideMaterial();
			
			setBlending( material.getBlending(), material.getBlendEquation(), material.getBlendSrc(), material.getBlendDst() );
			setDepthTest( material.isDepthTest() );
			setDepthWrite( material.isDepthWrite() );
			
			setPolygonOffset( material.isPolygonOffset(), material.getPolygonOffsetFactor(), material.getPolygonOffsetUnits() );

			renderObjects( opaqueObjects, camera, lights, fog, true, material );
			renderObjects( transparentObjects, camera, lights, fog, true, material );
			renderObjectsImmediate( _webglObjectsImmediate, null, camera, lights, fog, false, material );
		} 
		else 
		{		
			Material material = null;
			
			// opaque pass (front-to-back order)
			setBlending( Material.BLENDING.NO );

			renderObjects( opaqueObjects, camera, lights, fog, false, material );
			renderObjectsImmediate( _webglObjectsImmediate, false, camera, lights, fog, false, material );

			// transparent pass (back-to-front order)
			renderObjects( transparentObjects, camera, lights, fog, true, material );
			renderObjectsImmediate( _webglObjectsImmediate, true, camera, lights, fog, true, material );
		}

		// custom render plugins (post pass)
		renderPlugins( this.plugins, scene, camera, Plugin.TYPE.POST_RENDER );

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
	
	public void renderObjectsImmediate ( List<WebGLObject> renderList, Boolean isTransparentMaterial, Camera camera,
				List<Light> lights, AbstractFog fog, boolean useBlending, Material overrideMaterial ) {

		Material material = null;

		for ( int i = 0, il = renderList.size(); i < il; i ++ ) {

			WebGLObject webglObject = renderList.get( i );
			GeometryObject object = webglObject.object;

			if ( object.isVisible() ) {

				if ( overrideMaterial != null) {

					material = overrideMaterial;

				} else {

					if(isTransparentMaterial != null)
						material = isTransparentMaterial ? webglObject.transparent : webglObject.opaque;

					if ( material == null ) 
						continue;

					if ( useBlending )
						setBlending( material.getBlending(), material.getBlendEquation(), material.getBlendSrc(), material.getBlendDst() );

					setDepthTest( material.isDepthTest() );
					setDepthWrite( material.isDepthWrite() );
					setPolygonOffset( material.isPolygonOffset(), material.getPolygonOffsetFactor(), material.getPolygonOffsetUnits() );

				}

				renderImmediateObject( camera, lights, fog, material, object );

			}

		}

	}
	
	public void renderImmediateObject( Camera camera, List<Light> lights, AbstractFog fog, Material material, GeometryObject object ) {

		Shader program = setProgram( camera, lights, fog, material, object );

		this._currentGeometryGroupHash = - 1;

		setMaterialFaces( material );

//		if ( object.immediateRenderCallback ) {
//
//			object.immediateRenderCallback( program, _gl, _frustum );
//
//		} else {

//			object.render( function ( object ) { _this.renderBufferImmediate( object, program, material ); } );
		renderBufferImmediate( object, program, material );

//		}

	}

	private boolean renderPlugins( List<Plugin> plugins, Scene scene, Camera camera, Plugin.TYPE type ) 
	{		
		if ( plugins.size() == 0 ) 
			return false;

		boolean retval = false;
		for ( int i = 0, il = plugins.size(); i < il; i ++ ) 
		{
			Plugin plugin = plugins.get( i );

			if( ! plugin.isEnabled() 
					|| plugin.isRendering() 
					|| plugin.getType() != type 
					|| ( !(plugin.isMulty()) && !plugin.getScene().equals(scene)))
				continue;
			
			plugin.setRendering(true);
			Log.debug("Called renderPlugins(): " + plugin.getClass().getName());

			// reset state for plugin (to start from clean slate)
			this._currentProgram = null;
			this._currentCamera = null;

			this._oldBlending = null;
			this._oldDepthTest = null;
			this._oldDepthWrite = null;
			this.cache_oldMaterialSided = null;

			this._currentGeometryGroupHash = -1;
			this._currentMaterialId = -1;

			this._lightsNeedUpdate = true;

			plugin.render( camera, lights, _currentWidth, _currentHeight );

			// reset state after plugin (anything could have changed)

			this._currentProgram = null;
			this._currentCamera = null;

			this._oldBlending = null;
			this._oldDepthTest = null;
			this._oldDepthWrite = null;
			this.cache_oldMaterialSided = null;

			this._currentGeometryGroupHash = -1;
			this._currentMaterialId = -1;

			this._lightsNeedUpdate = true;
			
			plugin.setRendering(false);
			
			retval = true;
		}
		
		return retval;
	}

	private void renderObjects (List<WebGLObject> renderList, Camera camera, List<Light> lights, AbstractFog fog, boolean useBlending ) 
	{
		renderObjects ( renderList, camera, lights, fog, useBlending, null);
	}

	//renderList, camera, lights, fog, useBlending, overrideMaterial
	private void renderObjects (List<WebGLObject> renderList, Camera camera, List<Light> lights, AbstractFog fog, boolean useBlending, Material overrideMaterial ) 
	{		
		Material material = null;
		
		for ( int i = renderList.size() - 1; i != - 1; i -- ) {

			WebGLObject webglObject = renderList.get( i );

			GeometryObject object = webglObject.object;
			WebGLGeometry buffer = webglObject.buffer;

			setupMatrices( object, camera );

			if ( overrideMaterial != null) {

				material = overrideMaterial;

			} else {

				material = webglObject.material; //TODO: material = (isMaterialTransparent) ? webglObject.transparent : webglObject.opaque;

				if ( material == null ) continue;

				if ( useBlending ) 
					setBlending( material.getBlending(), material.getBlendEquation(), material.getBlendSrc(), material.getBlendDst() );

				setDepthTest( material.isDepthTest() );
				setDepthWrite( material.isDepthWrite() );
				setPolygonOffset( material.isPolygonOffset(), material.getPolygonOffsetFactor(), material.getPolygonOffsetUnits() );

			}

			setMaterialFaces( material );

			if ( buffer instanceof BufferGeometry ) {

				renderBufferDirect( camera, lights, fog, material, (BufferGeometry)buffer, object );

			} else {

				renderBuffer( camera, lights, fog, material, buffer, object );

			}

		}

	}
	
	/**
	 * Buffer rendering.
	 * Render GeometryObject with material.
	 */
	//camera, lights, fog, material, geometryGroup, object
	public void renderBuffer( Camera camera, List<Light> lights, AbstractFog fog, Material material, WebGLGeometry geometry, GeometryObject object ) 
	{
		if ( ! material.isVisible() ) 
			return;

		Shader program = setProgram( camera, lights, fog, material, object );

		Map<String, Integer> attributes = material.getShader().getAttributesLocations();

		boolean updateBuffers = false;
		int wireframeBit = material instanceof HasWireframe && ((HasWireframe)material).isWireframe() ? 1 : 0;

		int geometryGroupHash = ( geometry.getId() * 0xffffff ) + ( material.getShader().getId() * 2 ) + wireframeBit;

//		Log.error("--- renderBuffer() geometryGroupHash=" + geometryGroupHash 
//				+ ", _currentGeometryGroupHash=" +  this._currentGeometryGroupHash
//				+ ", program.id=" + program.getId()
////				+ ", geometryGroup.id=" + geometryBuffer.getId()
////				+ ", __webglLineCount=" + geometryBuffer.__webglLineCount
//				+ ", object.id=" + object.getId()
//				+ ", wireframeBit=" + wireframeBit);

		if ( geometryGroupHash != this._currentGeometryGroupHash ) 
		{
			this._currentGeometryGroupHash = geometryGroupHash;
			updateBuffers = true;
		}
		
		if ( updateBuffers ) {

			initAttributes();

		}

		// vertices
		if ( !(material instanceof HasSkinning && ((HasSkinning)material).isMorphTargets()) && attributes.get("position") >= 0 ) 
		{
			if ( updateBuffers ) 
			{
				getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, geometry.__webglVertexBuffer );
				enableAttribute( attributes.get("position") );
				getGL().vertexAttribPointer( attributes.get("position"), 3, DataType.FLOAT, false, 0, 0 );
			}

		} 
		else if ( object instanceof Mesh && ((Mesh)object).morphTargetBase != null  ) 
		{
				setupMorphTargets( material, geometry, (Mesh)object );
		}

		
		if ( updateBuffers ) 
		{
			// custom attributes

			// Use the per-geometryGroup custom attribute arrays which are setup in initMeshBuffers

			if ( geometry.__webglCustomAttributesList != null ) 
			{
				for ( int i = 0; i < geometry.__webglCustomAttributesList.size(); i ++ ) 
				{
					Attribute attribute = geometry.__webglCustomAttributesList.get( i );

					if( attributes.get( attribute.belongsToAttribute ) >= 0 ) 
					{
						getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, attribute.buffer );
						enableAttribute( attributes.get( attribute.belongsToAttribute ) );
						getGL().vertexAttribPointer( attributes.get( attribute.belongsToAttribute ), attribute.size, DataType.FLOAT, false, 0, 0 );
					}
				}
			}

			// colors
			if ( attributes.get("color") >= 0 ) 
			{
				if ( ((Geometry)object.getGeometry()).getColors().size() > 0 || ((Geometry)object.getGeometry()).getFaces().size() > 0 ) {

					getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, geometry.__webglColorBuffer );
					enableAttribute( attributes.get("color") );
					getGL().vertexAttribPointer( attributes.get("color"), 3, DataType.FLOAT, false, 0, 0 );

				} else {

					double defaultAttributeValues[] = new double[] {1.0,1.0,1.0};
					
					getGL().vertexAttrib3fv( attributes.get("color"), defaultAttributeValues);

				}
			}

			// normals
			if ( attributes.get("normal") >= 0 )
			{
				getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, geometry.__webglNormalBuffer );
				enableAttribute( attributes.get("normal") );
				getGL().vertexAttribPointer( attributes.get("normal"), 3, DataType.FLOAT, false, 0, 0 );
			}

			// tangents
			if ( attributes.get("tangent") >= 0 ) 
			{
				getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, geometry.__webglTangentBuffer );
				enableAttribute( attributes.get("tangent") );
				getGL().vertexAttribPointer( attributes.get("tangent"), 4, DataType.FLOAT, false, 0, 0 );
			}

			// uvs
			if ( attributes.get("uv") >= 0 )
			{
				if ( ((Geometry)object.getGeometry()).getFaceVertexUvs().get( 0 ) != null ) 
				{
					getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, geometry.__webglUVBuffer );
					enableAttribute( attributes.get("uv") );
					getGL().vertexAttribPointer( attributes.get("uv"), 2, DataType.FLOAT, false, 0, 0 );

				} else {
					
					double defaultAttributeValues[] = new double[] {0.0,0.0};
					getGL().vertexAttrib2fv( attributes.get("uv"), defaultAttributeValues );
				}
			}

			if ( attributes.get("uv2") >= 0 ) 
			{
				if ( ((Geometry)object.getGeometry()).getFaceVertexUvs().get( 1 ) != null ) 
				{
					getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, geometry.__webglUV2Buffer );
					enableAttribute( attributes.get("uv2") );
					getGL().vertexAttribPointer( attributes.get("uv2"), 2, DataType.FLOAT, false, 0, 0 );

				} else {
					
					double defaultAttributeValues[] = new double[] {0.0,0.0};
					
					getGL().vertexAttrib2fv( attributes.get("uv2"), defaultAttributeValues );
				}
			}

			if ( material instanceof HasSkinning && ((HasSkinning)material).isSkinning() &&
				 attributes.get("skinIndex") >= 0 && attributes.get("skinWeight") >= 0 ) 
			{
				getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, geometry.__webglSkinIndicesBuffer );
				enableAttribute( attributes.get("skinIndex") );
				getGL().vertexAttribPointer( attributes.get("skinIndex"), 4, DataType.FLOAT, false, 0, 0 );

				getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, geometry.__webglSkinWeightsBuffer );
				enableAttribute( attributes.get("skinWeight") );
				getGL().vertexAttribPointer( attributes.get("skinWeight"), 4, DataType.FLOAT, false, 0, 0 );
			}
			
			// line distances

			if ( attributes.get("lineDistance") != null && attributes.get("lineDistance") >= 0 ) {

				getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, geometry.__webglLineDistanceBuffer );
				enableAttribute( attributes.get("lineDistance") );
				getGL().vertexAttribPointer( attributes.get("lineDistance"), 1, DataType.FLOAT, false, 0, 0 );

			}

		}
		
		disableUnusedAttributes();

		Log.debug("  ----> renderBuffer() ID " + object.getId() + " (" + object.getClass().getSimpleName() + ")");

		// Render object's buffers
		object.renderBuffer(this, geometry, updateBuffers);
	}

	private void initMaterial ( Material material, List<Light> lights, AbstractFog fog, GeometryObject object ) 
	{
		Log.debug("Called initMaterial for material: " + material.getClass().getName() + " and object " + object.getClass().getName());

		// heuristics to create shader parameters according to lights in the scene
		// (not to blow over maxLights budget)
		Map<String, Integer> maxLightCount = allocateLights( lights );
		int maxShadows = allocateShadows( lights );

		ProgramParameters parameters = new ProgramParameters();
		
		parameters.gammaInput = this.isGammaInput();
		parameters.gammaOutput = this.isGammaOutput();
		
		parameters.precision = this._precision;

		parameters.supportsVertexTextures = this._supportsVertexTextures;
		
		if(fog != null) 
		{
			parameters.useFog  = true;
			parameters.useFog2 = (fog instanceof FogExp2);
		}
		
		parameters.logarithmicDepthBuffer = this._logarithmicDepthBuffer;
		
		parameters.maxBones = allocateBones( object );

		if(object instanceof SkinnedMesh)
		{
			parameters.useVertexTexture = this._supportsBoneTextures;// && ((SkinnedMesh)object).useVertexTexture;
		}

		parameters.maxMorphTargets = this.maxMorphTargets;
		parameters.maxMorphNormals = this.maxMorphNormals;

		parameters.maxDirLights   = maxLightCount.get("directional");
		parameters.maxPointLights = maxLightCount.get("point");
		parameters.maxSpotLights  = maxLightCount.get("spot");
		parameters.maxHemiLights  = maxLightCount.get("hemi");
		
		parameters.maxShadows = maxShadows;
		
		for(Plugin plugin: this.plugins)
		if(plugin instanceof ShadowMap && ((ShadowMap)plugin).isEnabled() && object.isReceiveShadow())
		{
			parameters.shadowMapEnabled = object.isReceiveShadow() && maxShadows > 0;
			parameters.shadowMapSoft    = ((ShadowMap)plugin).isSoft();
			parameters.shadowMapDebug   = ((ShadowMap)plugin).isDebugEnabled();
			parameters.shadowMapCascade = ((ShadowMap)plugin).isCascade();
			break;
		}

		material.updateProgramParameters(parameters);
		Log.debug("initMaterial() called new Program");

		String cashKey = material.getShader().getFragmentSource() 
				+ material.getShader().getVertexSource()
				+ parameters.toString();

		if(this._programs.containsKey(cashKey))
		{
			material.setShader( this._programs.get(cashKey) );
		}
		else
		{
			Shader shader = material.buildShader(getGL(), parameters);

			this._programs.put(cashKey, shader);

			this.getInfo().getMemory().programs = _programs.size();
		}
		
		Map<String, Integer> attributes = material.getShader().getAttributesLocations();

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
						numSupportedMorphNormals ++;
					}
				}

				((HasSkinning)material).setNumSupportedMorphNormals(numSupportedMorphNormals);
			}
		}
	}

	private Shader setProgram( Camera camera, List<Light> lights, AbstractFog fog, Material material, GeometryObject object ) 
	{
		// Use new material units for new shader
		this._usedTextureUnits = 0;
		
		if(material.isNeedsUpdate()) 
		{
			if(material.getShader() == null || material.getShader().getProgram() == null)
				material.deallocate(this);
			
			initMaterial( material, lights, fog, object );
			material.setNeedsUpdate(false);
		}

		if ( material instanceof HasSkinning && ((HasSkinning)material).isMorphTargets() ) 
		{
			if ( object instanceof Mesh && ((Mesh)object).__webglMorphTargetInfluences == null ) 
			{
				((Mesh)object).__webglMorphTargetInfluences = Float32Array.create( this.maxMorphTargets );
			}
		}

		boolean refreshProgram = false;
		boolean refreshMaterial = false;
		boolean refreshLights = false;

		Shader shader = material.getShader(); 
		WebGLProgram program = shader.getProgram();
		Map<String, Uniform> m_uniforms = shader.getUniforms();

		if ( !program.equals(_currentProgram) )
		{
			getGL().useProgram( program );
			this._currentProgram = program;

			refreshProgram = true;
			refreshMaterial = true;
			refreshLights = true;
		}

		if ( material.getId() != this._currentMaterialId ) 
		{
			if(_currentMaterialId == -1) refreshLights = true;
			
			this._currentMaterialId = material.getId();
			refreshMaterial = true;
		}

		if ( refreshProgram || !camera.equals( this._currentCamera) ) 
		{
			getGL().uniformMatrix4fv( m_uniforms.get("projectionMatrix").getLocation(), false, camera.getProjectionMatrix().getArray() );

			if ( _logarithmicDepthBuffer ) {

				gl.uniform1f( m_uniforms.get("logDepthBufFC").getLocation(), 2.0 / ( Math.log( ((HasNearFar)camera).getFar() + 1.0 ) / 0.6931471805599453 /*Math.LN2*/ ) );

			}

			if ( !camera.equals( this._currentCamera) ) 
				this._currentCamera = camera;
			
			// load material specific uniforms
			// (shader material also gets them for the sake of genericity)
			if ( material.getClass() == ShaderMaterial.class ||
				 material.getClass() == MeshPhongMaterial.class ||
				 material instanceof HasEnvMap && ((HasEnvMap)material).getEnvMap() != null 
			) {

				if ( m_uniforms.get("cameraPosition").getLocation() != null ) 
				{
					_vector3.setFromMatrixPosition( camera.getMatrixWorld() );
					getGL().uniform3f( m_uniforms.get("cameraPosition").getLocation(), _vector3.getX(), _vector3.getY(), _vector3.getZ() );
				}
			}

			if ( material.getClass() == MeshPhongMaterial.class ||
				 material.getClass() == MeshLambertMaterial.class ||
				 material.getClass() == MeshBasicMaterial.class ||
				 material.getClass() == ShaderMaterial.class ||
				 material instanceof HasSkinning && ((HasSkinning)material).isSkinning() 
			) {

				if ( m_uniforms.get("viewMatrix").getLocation() != null ) 
				{
					getGL().uniformMatrix4fv( m_uniforms.get("viewMatrix").getLocation(), false, camera.getMatrixWorldInverse().getArray() );
				}
			}
		}

		// skinning uniforms must be set even if material didn't change
		// auto-setting of texture unit for bone texture must go before other textures
		// not sure why, but otherwise weird things happen
		if ( material instanceof HasSkinning && ((HasSkinning)material).isSkinning() )
		{
			if ( object instanceof SkinnedMesh && ((SkinnedMesh)object).isUseVertexTexture() && this._supportsBoneTextures) 
			{
				if ( m_uniforms.get("boneTexture").getLocation() != null ) 
				{
					int textureUnit = getTextureUnit();

					getGL().uniform1i( m_uniforms.get("boneTexture").getLocation(), textureUnit );
					setTexture( ((SkinnedMesh)object).boneTexture, textureUnit );
				}
			} 
			else 
			{
				if ( m_uniforms.get("boneGlobalMatrices").getLocation() != null ) 
				{
					getGL().uniformMatrix4fv( m_uniforms.get("boneGlobalMatrices").getLocation(), false, ((SkinnedMesh)object).boneMatrices );
				}
			}
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

				if (this._lightsNeedUpdate ) 
				{
					refreshLights = true;
					this._lights.setupLights( lights, this.gammaInput );
					this._lightsNeedUpdate = false;
				}

				if ( refreshLights ) {
					this._lights.refreshUniformsLights( m_uniforms );
//					markUniformsLightsNeedsUpdate( m_uniforms, true );
				} else {
//					markUniformsLightsNeedsUpdate( m_uniforms, false );
				}
			}

			material.refreshUniforms(camera, this.gammaInput);

			if ( object.isReceiveShadow() && ! material.isShadowPass() )
				refreshUniformsShadow( m_uniforms, lights );

			// load common uniforms
			loadUniformsGeneric( m_uniforms );

		}

		loadUniformsMatrices( m_uniforms, object );

		if ( m_uniforms.get("modelMatrix").getLocation() != null )
			getGL().uniformMatrix4fv( m_uniforms.get("modelMatrix").getLocation(), false, object.getMatrixWorld().getArray() );

		return shader;
	}

	private void refreshUniformsShadow( Map<String, Uniform> uniforms, List<Light> lights ) 
	{
		if ( uniforms.containsKey("shadowMatrix") ) 
		{
			// Make them zero
			uniforms.get("shadowMap").setValue(new ArrayList<Texture>());
			uniforms.get("shadowMapSize").setValue(new ArrayList<Vector2>());
			uniforms.get("shadowMatrix").setValue(new ArrayList<Matrix4>());
			List<Texture> shadowMap = (List<Texture>)uniforms.get("shadowMap").getValue();
			List<Vector2> shadowMapSize = (List<Vector2>)uniforms.get("shadowMapSize").getValue();
			List<Matrix4> shadowMatrix = (List<Matrix4>)uniforms.get("shadowMatrix").getValue();
			
			int j = 0;
			for ( Light light: lights) 
			{
				if ( ! light.isCastShadow() ) continue;

				if ( light instanceof ShadowLight && ! ((ShadowLight)light).isShadowCascade() )  
				{
					ShadowLight shadowLight = (ShadowLight) light;

					shadowMap.add(shadowLight.getShadowMap() );
					shadowMapSize.add(shadowLight.getShadowMapSize() );
					shadowMatrix.add(shadowLight.getShadowMatrix() );

					((Float32Array)uniforms.get("shadowDarkness").getValue()).set( j, shadowLight.getShadowDarkness() );
					((Float32Array)uniforms.get("shadowBias").getValue()).set( j, shadowLight.getShadowBias() );
					j++;
				}
			}
		}
	}

	// Uniforms (load to GPU)

	private void loadUniformsMatrices ( Map<String, Uniform> uniforms, GeometryObject object ) 
	{
		GeometryObject objectImpl = (GeometryObject) object;
		getGL().uniformMatrix4fv( uniforms.get("modelViewMatrix").getLocation(), false, objectImpl._modelViewMatrix.getArray() );

		if ( uniforms.containsKey("normalMatrix") )
			getGL().uniformMatrix3fv( uniforms.get("normalMatrix").getLocation(), false, objectImpl._normalMatrix.getArray() );
	}

	@SuppressWarnings("unchecked")
	private void loadUniformsGeneric( Map<String, Uniform> materialUniforms ) 
	{
		WebGLRenderingContext gl = getGL();
		
		for ( Uniform uniform : materialUniforms.values() ) 
		{
//			for ( String key: materialUniforms.keySet() ) 
//			{
//				 Uniform uniform = materialUniforms.get(key);
			WebGLUniformLocation location = uniform.getLocation();
		
			if ( location == null ) continue;

			Object value = uniform.getValue();
			Uniform.TYPE type = uniform.getType();
			
			// Up textures also for undefined values
			if ( type != Uniform.TYPE.T && value == null ) continue;
		
			if(type == TYPE.I) // single integer
			{
				gl.uniform1i( location, (value instanceof Boolean) ? ((Boolean)value) ? 1 : 0 : (Integer) value );
			}
			else if(type == TYPE.F) // single double
			{
				gl.uniform1f( location, (Double)value );
			}
			else if(type == TYPE.V2) // single Vector2
			{ 
				gl.uniform2f( location, ((Vector2)value).getX(), ((Vector2)value).getX() );
			}
			else if(type == TYPE.V3) // single Vector3
			{ 
				gl.uniform3f( location, ((Vector3)value).getX(), ((Vector3)value).getY(), ((Vector3)value).getZ() );
			}
			else if(type == TYPE.V4) // single Vector4
			{
				gl.uniform4f( location, ((Vector4)value).getX(), ((Vector4)value).getY(), ((Vector4)value).getZ(), ((Vector4)value).getW() );
			}
			else if(type == TYPE.C) // single Color
			{
				gl.uniform3f( location, ((Color)value).getR(), ((Color)value).getG(), ((Color)value).getB() );
			}
			else if(type == TYPE.FV1) // flat array of floats (JS or typed array)
			{
				gl.uniform1fv( location, (Float32Array)value );
			}
			else if(type == TYPE.FV) // flat array of floats with 3 x N size (JS or typed array)
			{ 
				gl.uniform3fv( location, (Float32Array) value );
			}
			else if(type == TYPE.V2V) // List of Vector2
			{ 
				List<Vector2> listVector2f = (List<Vector2>) value;
				if ( uniform.getCacheArray() == null )
					uniform.setCacheArray( Float32Array.create( 2 * listVector2f.size() ) );

				for ( int i = 0, il = listVector2f.size(); i < il; i ++ ) 
				{
					int offset = i * 2;

					uniform.getCacheArray().set(offset, listVector2f.get(i).getX());
					uniform.getCacheArray().set(offset + 1, listVector2f.get(i).getY());
				}

				gl.uniform2fv( location, uniform.getCacheArray() );
			}
			else if(type == TYPE.V3V) // List of Vector3
			{
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

				gl.uniform3fv( location, uniform.getCacheArray() );
			}
			else if(type == TYPE.V4V) // List of Vector4
			{
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

				gl.uniform4fv( location, uniform.getCacheArray() );
			}
			else if(type == TYPE.M4) // single Matrix4
			{
				Matrix4 matrix4 = (Matrix4) value;
				if ( uniform.getCacheArray() == null )
					uniform.setCacheArray( Float32Array.create( 16 ) );

				matrix4.flattenToArrayOffset( uniform.getCacheArray() );
				gl.uniformMatrix4fv( location, false, uniform.getCacheArray() );
			}
			else if(type == TYPE.M4V) // List of Matrix4
			{
				List<Matrix4> listMatrix4f = (List<Matrix4>) value;
				if ( uniform.getCacheArray() == null )
					uniform.setCacheArray( Float32Array.create( 16 * listMatrix4f.size() ) );

				for ( int i = 0, il = listMatrix4f.size(); i < il; i ++ )
					listMatrix4f.get( i ).flattenToArrayOffset( uniform.getCacheArray(), i * 16 );

				gl.uniformMatrix4fv( location, false, uniform.getCacheArray() );
			}
			else if(type == TYPE.T) // single Texture (2d or cube)
			{
				Texture texture = (Texture)value;
				int textureUnit = getTextureUnit();

				gl.uniform1i( location, textureUnit );

				if ( texture != null )
				{
					if ( texture.getClass() == CubeTexture.class )
						setCubeTexture( (CubeTexture) texture, textureUnit );

					else if ( texture.getClass() == RenderTargetCubeTexture.class )
						setCubeTextureDynamic( (RenderTargetCubeTexture)texture, textureUnit );

					else
						setTexture( texture, textureUnit );
				}
			}
			else if(type == TYPE.TV) //List of Texture (2d)
			{
				List<Texture> textureList = (List<Texture>)value;
				int[] units = new int[textureList.size()];

				for( int i = 0, il = textureList.size(); i < il; i ++ ) 
				{
					units[ i ] = getTextureUnit();
				}

				gl.uniform1iv( location, units );

				for( int i = 0, il = textureList.size(); i < il; i ++ ) 
				{
					Texture texture = textureList.get( i );
					int textureUnit = units[ i ];

					if ( texture == null ) continue;

					setTexture( texture, textureUnit );
				}
			}			
		}
	}
	
	public int getTextureUnit() 
	{
		int textureUnit = this._usedTextureUnits ++;

		if ( textureUnit >= this._maxTextures ) 
		{
			Log.warn( "Trying to use " + textureUnit + " texture units while this GPU supports only " + this._maxTextures );
		}

		return textureUnit;
	}

	private void setupMatrices ( Object3D object, Camera camera ) 
	{
		object._modelViewMatrix.multiply( camera.getMatrixWorldInverse(), object.getMatrixWorld() );
		object._normalMatrix.getNormalMatrix( object._modelViewMatrix );
	}
	
	public void setMaterialFaces( Material material )
	{
		if ( this.cache_oldMaterialSided == null || this.cache_oldMaterialSided != material.getSides() ) 
		{
			if(material.getSides() == Material.SIDE.DOUBLE)
				getGL().disable( EnableCap.CULL_FACE );
			else
				getGL().enable( EnableCap.CULL_FACE );

			if ( material.getSides() == Material.SIDE.BACK ) 
				getGL().frontFace( FrontFaceDirection.CW );
			else
				getGL().frontFace( FrontFaceDirection.CCW );

			this.cache_oldMaterialSided = material.getSides();
		}
	}

	public void setDepthTest( boolean depthTest ) 
	{
		if ( this._oldDepthTest == null || this._oldDepthTest != depthTest ) 
		{
			if ( depthTest )
				getGL().enable( EnableCap.DEPTH_TEST );
			else 
				getGL().disable( EnableCap.DEPTH_TEST );

			this._oldDepthTest = depthTest;
		}
	}

	public void setDepthWrite(boolean depthWrite ) 
	{
		if ( this._oldDepthWrite == null || this._oldDepthWrite != depthWrite ) 
		{
			getGL().depthMask( depthWrite );
			_oldDepthWrite = depthWrite;
		}
	}

	private void setPolygonOffset( boolean polygonoffset, double factor, double units ) 
	{
		if ( this._oldPolygonOffset == null || this._oldPolygonOffset != polygonoffset ) 
		{
			if ( polygonoffset )
				getGL().enable( EnableCap.POLYGON_OFFSET_FILL );
			else
				getGL().disable( EnableCap.POLYGON_OFFSET_FILL );

			this._oldPolygonOffset = polygonoffset;
		}

		if ( polygonoffset && ( _oldPolygonOffsetFactor == null || 
				_oldPolygonOffsetUnits == null || 
				_oldPolygonOffsetFactor != factor || 
				_oldPolygonOffsetUnits != units ) 
		) {
			getGL().polygonOffset( factor, units );

			this._oldPolygonOffsetFactor = factor;
			this._oldPolygonOffsetUnits = units;
		}
	}

	public void setBlending( Material.BLENDING blending ) 
	{
		if ( blending != this._oldBlending ) 
		{
			if( blending == Material.BLENDING.NO) 
			{
				getGL().disable( EnableCap.BLEND );
				
			} 
			else if( blending == Material.BLENDING.ADDITIVE) 
			{
				
				getGL().enable( EnableCap.BLEND );
				getGL().blendEquation( BlendEquationMode.FUNC_ADD );
				getGL().blendFunc( BlendingFactorSrc.SRC_ALPHA, BlendingFactorDest.ONE );
				
			// TODO: Find blendFuncSeparate() combination
			} 
			else if( blending == Material.BLENDING.SUBTRACTIVE) 
			{
				getGL().enable( EnableCap.BLEND );
				getGL().blendEquation( BlendEquationMode.FUNC_ADD );
				getGL().blendFunc( BlendingFactorSrc.ZERO, BlendingFactorDest.ONE_MINUS_SRC_COLOR );

			// TODO: Find blendFuncSeparate() combination
			} 
			else if( blending == Material.BLENDING.MULTIPLY) 
			{
				getGL().enable( EnableCap.BLEND );
				getGL().blendEquation( BlendEquationMode.FUNC_ADD );
				getGL().blendFunc( BlendingFactorSrc.ZERO, BlendingFactorDest.SRC_COLOR );

			} 
			else if( blending == Material.BLENDING.CUSTOM) 
			{
				getGL().enable( EnableCap.BLEND );

			} 
			// NORMAL
			else 
			{
				getGL().enable( EnableCap.BLEND );
				getGL().blendEquationSeparate( BlendEquationMode.FUNC_ADD, BlendEquationMode.FUNC_ADD );
				getGL().blendFuncSeparate( BlendingFactorSrc.SRC_ALPHA, 
						BlendingFactorDest.ONE_MINUS_SRC_ALPHA, 
						BlendingFactorSrc.ONE, 
						BlendingFactorDest.ONE_MINUS_SRC_ALPHA );
			}

			this._oldBlending = blending;
		}
	}

	private void setBlending( Material.BLENDING blending, BlendEquationMode blendEquation, BlendingFactorSrc blendSrc, BlendingFactorDest blendDst ) 
	{
		setBlending(blending);

		if ( blending == Material.BLENDING.CUSTOM ) 
		{
			if ( blendEquation != this._oldBlendEquation ) 
			{
				getGL().blendEquation( blendEquation );
				this._oldBlendEquation = blendEquation;
			}

			if ( blendSrc != _oldBlendSrc || blendDst != _oldBlendDst ) 
			{
				getGL().blendFunc( blendSrc, blendDst);

				this._oldBlendSrc = blendSrc;
				this._oldBlendDst = blendDst;
			}
		} 
		else 
		{
			this._oldBlendEquation = null;
			this._oldBlendSrc = null;
			this._oldBlendDst = null;
		}
	}

	// Textures
	
	private void setCubeTextureDynamic(RenderTargetCubeTexture texture, int slot) 
	{
		getGL().activeTexture( TextureUnit.TEXTURE0, slot );
		getGL().bindTexture( TextureTarget.TEXTURE_CUBE_MAP, texture.getWebGlTexture() );
	}

	public void setTexture( Texture texture, int slot ) 
	{
		if ( texture.isNeedsUpdate()) 
		{
			if ( texture.getWebGlTexture() == null ) 
			{
				texture.setWebGlTexture( getGL().createTexture() );

				this.getInfo().getMemory().textures ++;
			}
			
			getGL().activeTexture( TextureUnit.TEXTURE0, slot );
			getGL().bindTexture( TextureTarget.TEXTURE_2D, texture.getWebGlTexture() );

			getGL().pixelStorei( PixelStoreParameter.UNPACK_FLIP_Y_WEBGL, texture.isFlipY() ? 1 : 0 );
			getGL().pixelStorei( PixelStoreParameter.UNPACK_PREMULTIPLY_ALPHA_WEBGL, texture.isPremultiplyAlpha() ? 1 : 0 );		
			getGL().pixelStorei( PixelStoreParameter.UNPACK_ALIGNMENT, texture.getUnpackAlignment() );

			Element image = texture.getImage();
			boolean isImagePowerOfTwo = Mathematics.isPowerOfTwo( image.getOffsetWidth() ) 
					&& Mathematics.isPowerOfTwo( image.getOffsetHeight() );

			texture.setTextureParameters( getGL(), getMaxAnisotropy(), TextureTarget.TEXTURE_2D, isImagePowerOfTwo );

			if ( texture instanceof CompressedTexture ) 
			{
				List<DataTexture> mipmaps = ((CompressedTexture) texture).getMipmaps();

				for( int i = 0, il = mipmaps.size(); i < il; i ++ ) 
				{
					DataTexture mipmap = mipmaps.get( i );
					getGL().compressedTexImage2D( TextureTarget.TEXTURE_2D, i, ((CompressedTexture) texture).getCompressedFormat(), 
							mipmap.getWidth(), mipmap.getHeight(), 0, mipmap.getData() );
				}
			}
			else if ( texture instanceof DataTexture ) 
			{
				getGL().texImage2D( TextureTarget.TEXTURE_2D, 0, 
						((DataTexture) texture).getWidth(),
						((DataTexture) texture).getHeight(), 
						0, 
						texture.getFormat(), 
						texture.getType(),
						((DataTexture) texture).getData() );
			} 
			else 
			{
				getGL().texImage2D( TextureTarget.TEXTURE_2D, 0, texture.getFormat(), texture.getType(), (ImageElement)image );
			}

			if ( texture.isGenerateMipmaps() && isImagePowerOfTwo ) 
				getGL().generateMipmap( TextureTarget.TEXTURE_2D );

			texture.setNeedsUpdate(false);
		} 
		// Needed to check webgl texture in case deferred loading
		else if(texture.getWebGlTexture() != null)
		{
			getGL().activeTexture( TextureUnit.TEXTURE0, slot );
			getGL().bindTexture( TextureTarget.TEXTURE_2D, texture.getWebGlTexture() );
		}
	}
	
	private CanvasElement createPowerOfTwoImage(Element image) 
	{
		int width = image.getOffsetWidth();
		int height = image.getOffsetHeight();
		
		CanvasElement canvas = Document.get().createElement("canvas").cast();
		
		// Scale up the texture to the next highest power of two dimensions.
		canvas.setWidth( Mathematics.getNextHighestPowerOfTwo( width ) );
		canvas.setHeight( Mathematics.getNextHighestPowerOfTwo( height ) );

		Context2d context = canvas.getContext2d();
		context.drawImage((ImageElement)image, 0, 0, width, height);
		
		return canvas;
	}

	/**
	 * Warning: Scaling through the canvas will only work with images that use
	 * premultiplied alpha.
	 * 
	 * @param image    the image element
	 * @param maxSize  the max size of absoluteWidth or absoluteHeight
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
		context.drawImage((ImageElement)image, 0, 0, imgWidth, imgHeight, 0, 0, newWidth, newHeight );

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

			getGL().activeTexture( TextureUnit.TEXTURE0, slot );
			getGL().bindTexture( TextureTarget.TEXTURE_CUBE_MAP, texture.getWebGlTexture() );
			getGL().pixelStorei( PixelStoreParameter.UNPACK_FLIP_Y_WEBGL, texture.isFlipY() ? 1 : 0 );

			List<Element> cubeImage = new ArrayList<Element>();

			for ( int i = 0; i < 6; i ++ ) 
			{
				if ( this.autoScaleCubemaps )
					cubeImage.add(clampToMaxSize( texture.getImage( i ), this._maxCubemapSize ));

				else
					cubeImage.add(texture.getImage( i ));
			}

			Element image = cubeImage.get( 0 );
			boolean isImagePowerOfTwo = Mathematics.isPowerOfTwo( image.getOffsetWidth() ) 
					&& Mathematics.isPowerOfTwo( image.getOffsetHeight() );

			texture.setTextureParameters( getGL(), getMaxAnisotropy(), TextureTarget.TEXTURE_CUBE_MAP, true /*power of two*/ );

			for ( int i = 0; i < 6; i ++ ) 
			{
				if(!isImagePowerOfTwo)
				{
					getGL().texImage2D( TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X, i, 0, 
							texture.getFormat(), texture.getType(), createPowerOfTwoImage( cubeImage.get( i ) ) );
				}
				else
				{
					getGL().texImage2D( TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X, i, 0, 
							texture.getFormat(), texture.getType(), (ImageElement)cubeImage.get( i ) );
				}
			}

			if ( texture.isGenerateMipmaps() )	
				getGL().generateMipmap( TextureTarget.TEXTURE_CUBE_MAP );

			texture.setNeedsUpdate(false);
		} 
		else 
		{
			getGL().activeTexture( TextureUnit.TEXTURE0, slot );
			getGL().bindTexture( TextureTarget.TEXTURE_CUBE_MAP, texture.getWebGlTexture() );
		}

	}

	/**
	 * Setup render target
	 * 
	 * @param renderTarget the render target
	 */
	public void setRenderTarget( RenderTargetTexture renderTarget ) 
	{
		Log.debug("  ----> Called setRenderTarget(params)");
		WebGLFramebuffer framebuffer = null;
		
		int width, height, vx, vy;
		
		if(renderTarget != null) 
		{
			renderTarget.setRenderTarget(getGL());
		    framebuffer = renderTarget.getWebGLFramebuffer();

		    width = renderTarget.getWidth();
		    height = renderTarget.getHeight();
		    
			vx = 0;
			vy = 0;

		} 
		else 
		{
			width = this._viewportWidth;
			height = this._viewportHeight;
			
			vx = _viewportX;
			vy = _viewportY;

		}

		if ( framebuffer != this._currentFramebuffer ) 
		{
			getGL().bindFramebuffer( framebuffer );
			getGL().viewport( vx, vy, width, height);

			this._currentFramebuffer = framebuffer;
		}

		_currentWidth = width;
		_currentHeight = height;
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
		if ( this._supportsBoneTextures && object instanceof SkinnedMesh && ((SkinnedMesh)object).isUseVertexTexture() ) 
		{
			return 1024;
		} 
		else 
		{
			// default for when object is not specified
			// ( for example when prebuilding shader
			//   to be used with multiple objects )
			//
			// 	- leave some extra space for other uniforms
			//  - limit here is ANGLE's 254 max uniform vectors
			//    (up to 54 should be safe)

			int nVertexUniforms = getGL().getParameteri( WebGLConstants.MAX_VERTEX_UNIFORM_VECTORS );
			int nVertexMatrices = (int) Math.floor( ( nVertexUniforms - 20 ) / 4 );

			int maxBones = nVertexMatrices;

			if ( object instanceof SkinnedMesh ) 
			{
				maxBones = Math.min( ((SkinnedMesh)object).getBones().size(), maxBones );

				if ( maxBones < ((SkinnedMesh)object).getBones().size() )
				{
					Log.warn( "WebGLRenderer: too many bones - " + ((SkinnedMesh)object).getBones().size() 
							+ ", this GPU supports just " + maxBones + " (try OpenGL instead of ANGLE)" );
				}
			}

			return maxBones;
		}
	}

	private Map<String, Integer> allocateLights ( List<Light> lights ) 
	{
		int dirLights = 0, pointLights = 0, spotLights = 0, hemiLights = 0;
				
		for(Light light: lights) 
		{
			if ( light instanceof ShadowLight && ((ShadowLight)light).isOnlyShadow() ) continue;

			if ( light instanceof DirectionalLight ) dirLights ++;
			if ( light instanceof PointLight ) pointLights ++;
			if ( light instanceof SpotLight ) spotLights ++;
			if ( light instanceof HemisphereLight ) hemiLights ++;
		}

		Map<String, Integer> retval = GWT.isScript() ? 
				new FastMap<Integer>() : new HashMap<String, Integer>();
		retval.put("directional", dirLights);
		retval.put("point", pointLights);
		retval.put("spot", spotLights);
		retval.put("hemi", hemiLights);

		return retval;
	}

	private int allocateShadows( List<Light> lights ) 
	{
		int maxShadows = 0;

		for (Light light: lights)
		{
			if ( light instanceof ShadowLight)
			{
				if( !((ShadowLight)light).isCastShadow() )
					continue;

				maxShadows ++;
			}
		}

		return maxShadows;
	}
}
