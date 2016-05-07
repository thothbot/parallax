/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * Copyright 2015 Tony Houghton, h@realh.co.uk
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

package org.parallax3d.parallax.graphics.renderers;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.system.ViewportResizeBus;
import org.parallax3d.parallax.graphics.renderers.shaders.Attribute;
import org.parallax3d.parallax.graphics.renderers.shaders.ProgramParameters;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.lights.SpotLight;
import org.parallax3d.parallax.graphics.textures.CompressedTexture;
import org.parallax3d.parallax.graphics.textures.CubeTexture;
import org.parallax3d.parallax.math.Frustum;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.objects.PointCloud;
import org.parallax3d.parallax.graphics.objects.SkinnedMesh;
import org.parallax3d.parallax.graphics.textures.DataTexture;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.graphics.cameras.HasNearFar;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.core.BufferGeometry.DrawCall;
import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.GeometryGroup;
import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.lights.HemisphereLight;
import org.parallax3d.parallax.graphics.lights.Light;
import org.parallax3d.parallax.graphics.lights.PointLight;
import org.parallax3d.parallax.graphics.lights.ShadowLight;
import org.parallax3d.parallax.graphics.materials.HasEnvMap;
import org.parallax3d.parallax.graphics.materials.HasFog;
import org.parallax3d.parallax.graphics.materials.HasSkinning;
import org.parallax3d.parallax.graphics.materials.HasWireframe;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.materials.MeshFaceMaterial;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Matrix3;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector4;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.AbstractFog;
import org.parallax3d.parallax.graphics.scenes.FogExp2;
import org.parallax3d.parallax.graphics.scenes.Scene;

import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.graphics.textures.TextureData;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.GLES20Ext;
import org.parallax3d.parallax.system.gl.WebGLShaderPrecisionFormat;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Int32Array;
import org.parallax3d.parallax.system.gl.arrays.TypeArray;
import org.parallax3d.parallax.system.gl.arrays.Uint8Array;
import org.parallax3d.parallax.system.gl.enums.*;

/**
 * The WebGL renderer displays your beautifully crafted {@link Scene}s using WebGL, if your device supports it.
 */
@ThreejsObject("THREE.WebGLRenderer")
public class GLRenderer extends Renderer
{
	private static final String POSITION = "position";
	private static final String MORPH_TARGET = "morphTarget";
	private static final String MORPH_NORMAL = "morphNormal";
	private static final String COLOR = "color";
	private static final String NORMAL = "normal";
	private static final String TANGENT = "tangent";
	private static final String SKIN_WEIGHT = "skinWeight";
	private static final String SKIN_INDEX = "skinIndex";
	private static final String LINE_DISTANCE = "lineDistance";
	private static final String SHADOW_MATRIX = "shadowMatrix";

	public GL20 gl;

	private GLRendererInfo info;

	private List<Light> lights = new ArrayList<Light>();

	public FastMap<List<GLObject>> _webglObjects =  new FastMap<List<GLObject>>();

	public List<GLObject> _webglObjectsImmediate  = new ArrayList<GLObject>();

	public List<GLObject> opaqueObjects = new ArrayList<GLObject>();
	public List<GLObject> transparentObjects = new ArrayList<GLObject>();

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

	// morphs
	private int maxMorphTargets = 8;
	private int maxMorphNormals = 4;

	// flags
	private boolean autoScaleCubemaps = true;

	// ---- Internal properties ----------------------------

	public FastMap<Shader> _programs;

	private int _currentProgram = 0; //WebGLProgram
	private int _currentFramebuffer = 0; //WebGLFramebuffer
	private int _currentMaterialId = -1;
	private int _currentGeometryGroupHash = -1;
	private Camera _currentCamera = null;

	private int _usedTextureUnits = 0;

	// GL state cache

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

	private boolean _lightsNeedUpdate = true;

	private RendererLights _lights;

	private List<Plugin> plugins;

	// GPU capabilities
	private int _maxTextures;
	private int _maxVertexTextures;
	private int _maxCubemapSize;

	private boolean _supportsVertexTextures;
	private boolean _supportsBoneTextures;

	private WebGLShaderPrecisionFormat _vertexShaderPrecisionHighpFloat;
	private WebGLShaderPrecisionFormat _vertexShaderPrecisionMediumpFloat;

	private WebGLShaderPrecisionFormat _fragmentShaderPrecisionHighpFloat;
	private WebGLShaderPrecisionFormat _fragmentShaderPrecisionMediumpFloat;

	// clamp precision to maximum available
	private boolean highpAvailable;
	private boolean mediumpAvailable;

	private boolean isAutoUpdateObjects = true;
	private boolean isAutoUpdateScene = true;

	/**
	 * The constructor will create renderer for the current EGL context.
	 *
	 * @param width  the viewport width
	 * @param height the viewport height
	 */
	public GLRenderer(GL20 gl, int width, int height)
	{
		this.gl = gl;

		this.setInfo(new GLRendererInfo());

		this._lights           = new RendererLights();
		this._programs         = new FastMap<Shader>();

		this._maxTextures       = this.getIntGlParam(GL20.GL_MAX_TEXTURE_IMAGE_UNITS);
		this._maxVertexTextures = this.getIntGlParam(GL20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS);
		this._maxCubemapSize    = this.getIntGlParam(GL20.GL_MAX_CUBE_MAP_TEXTURE_SIZE);

		this._supportsVertexTextures = this._maxVertexTextures > 0 ;
		this._supportsBoneTextures = this._supportsVertexTextures && GLExtensions.isSupported(gl, GLES20Ext.List.OES_texture_float);

		this._vertexShaderPrecisionHighpFloat = new
				WebGLShaderPrecisionFormat(gl, Shaders.VERTEX_SHADER, ShaderPrecisionSpecifiedTypes.HIGH_FLOAT);
		this._vertexShaderPrecisionMediumpFloat =
				new WebGLShaderPrecisionFormat(gl, Shaders.VERTEX_SHADER, ShaderPrecisionSpecifiedTypes.MEDIUM_FLOAT);

		this._fragmentShaderPrecisionHighpFloat = new
				WebGLShaderPrecisionFormat(gl, Shaders.FRAGMENT_SHADER, ShaderPrecisionSpecifiedTypes.HIGH_FLOAT);
		this._fragmentShaderPrecisionMediumpFloat =
				new WebGLShaderPrecisionFormat(gl, Shaders.FRAGMENT_SHADER, ShaderPrecisionSpecifiedTypes.MEDIUM_FLOAT);

		this.highpAvailable = _vertexShaderPrecisionHighpFloat.getPrecision() > 0 &&
				_fragmentShaderPrecisionHighpFloat.getPrecision() > 0;
		this.mediumpAvailable = _vertexShaderPrecisionMediumpFloat.getPrecision() > 0 &&
				_fragmentShaderPrecisionMediumpFloat.getPrecision() > 0;

		if ( this._precision == Shader.PRECISION.HIGHP && ! highpAvailable ) {

			if ( mediumpAvailable ) {

				this._precision = Shader.PRECISION.MEDIUMP;
				Log.warn("WebGLRenderer: highp not supported, using mediump.");

			} else {

				this._precision = Shader.PRECISION.LOWP;
				Log.warn("WebGLRenderer: highp and mediump not supported, using lowp.");

			}

		}

		if ( this._precision == Shader.PRECISION.MEDIUMP && ! mediumpAvailable ) {

			this._precision = Shader.PRECISION.LOWP;
			Log.warn("WebGLRenderer: mediump not supported, using lowp.");
		}

		GLExtensions.isSupported(gl, GLES20Ext.List.OES_texture_float);
		GLExtensions.isSupported(gl, GLES20Ext.List.OES_texture_float_linear);
		GLExtensions.isSupported(gl, GLES20Ext.List.OES_standard_derivatives);

		if ( _logarithmicDepthBuffer )
		{
			_logarithmicDepthBuffer = GLExtensions.isSupported(gl, GLES20Ext.List.EXT_frag_depth);
		}

		GLExtensions.isSupported(gl, GLES20Ext.List.WEBGL_compressed_texture_s3tc);

		setSize(width, height);
		setDefaultGLState();

		// default org.parallax3d.plugins (order is important)
		this.plugins = new ArrayList<>();
	}

	private int getIntGlParam(int param)
	{
		IntBuffer buffer = Int32Array.create(3).getTypedBuffer();
		this.gl.glGetIntegerv(param, buffer);
		return buffer.get(0);
	}

	public void addPlugin(Plugin plugin)
	{
		deletePlugin(plugin);
		this.plugins.add(plugin);
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
		return GLExtensions.isSupported(this.gl, GLES20Ext.List.OES_texture_float);
	}

	public boolean supportsStandardDerivatives()
	{
		return GLExtensions.isSupported(this.gl, GLES20Ext.List.OES_standard_derivatives);
	}

	public boolean supportsCompressedTextureS3TC()
	{
		return GLExtensions.isSupported(this.gl, GLES20Ext.List.WEBGL_compressed_texture_s3tc);
	}

	public boolean supportsCompressedTexturePVRTC()
	{
		return GLExtensions.isSupported(this.gl, GLES20Ext.List.WEBGL_compressed_texture_pvrtc);
	}

	public boolean supportsBlendMinMax()
	{
		return GLExtensions.isSupported(this.gl, GLES20Ext.List.EXT_blend_minmax);
	}

	public int getMaxAnisotropy()
	{
		if (GLExtensions.isSupported(this.gl, GLES20Ext.List.EXT_texture_filter_anisotropic)) {
			return this.getIntGlParam(GLES20Ext.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
		} else {
			return 0;
		}
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

	public boolean isLogarithmicDepthBufferEnabled(){
		return _logarithmicDepthBuffer;
	}

	public void setLogarithmicDepthBuffer(boolean enable){
		this._logarithmicDepthBuffer = enable;
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
	 * Gets {@link GLRendererInfo} instance with debug information.
	 *
	 * @return the {@link GLRendererInfo} instance
	 */
	public GLRendererInfo getInfo() {
		return info;
	}

	private void setInfo(GLRendererInfo info) {
		this.info = info;
	}

	public void setDefaultGLState()
	{
		setClearColor(0x00000, 1.0);
		this.gl.glClearStencil(0);

		this.gl.glEnable(EnableCap.DEPTH_TEST.getValue());
		this.gl.glDepthFunc(DepthFunction.LEQUAL.getValue());

		this.gl.glFrontFace(FrontFaceDirection.CCW.getValue());
		this.gl.glCullFace(CullFaceMode.BACK.getValue());
		this.gl.glEnable(EnableCap.CULL_FACE.getValue());

		this.gl.glEnable(EnableCap.BLEND.getValue());
		this.gl.glBlendEquation(BlendEquationMode.FUNC_ADD.getValue());
		this.gl.glBlendFunc(BlendingFactorSrc.SRC_ALPHA.getValue(), BlendingFactorDest.ONE_MINUS_SRC_ALPHA.getValue());

		this.gl.glViewport(_viewportX, _viewportY, _viewportWidth, _viewportHeight);
		this.gl.glClearColor((float)clearColor.getR(), (float)clearColor.getG(), (float)clearColor.getB(), (float)clearAlpha);
	}

	/**
	 * Sets the sizes and also sets {@link #setViewport(int, int, int, int)} size.
	 *
	 * @param width  the canvas width.
	 * @param height the canvas height.
	 */
	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height);

		setViewport(0, 0, width, height);
	}

	/**
	 * Sets the viewport to render from (X, Y) to (X + absoluteWidth, Y + absoluteHeight).
	 * By default X and Y = 0.
	 * Also fires ViewportResize event.
	 */
	public void setViewport(int x, int y, int width, int height)
	{
		this._viewportX = x;
		this._viewportY = y;

		this._viewportWidth = width;
		this._viewportHeight = height;

		this.gl.glViewport(this._viewportX, this._viewportY, this._viewportWidth, this._viewportHeight);

		fireViewportResizeEvent(width, height);
	}

	/**
	 * Sets the scissor area from (x, y) to (x + absoluteWidth, y + absoluteHeight).
	 */
	public void setScissor(int x, int y, int width, int height)
	{
		this.gl.glScissor(x, y, width, height);
	}

	/**
	 * Enable the scissor test. When this is enabled, only the pixels
	 * within the defined scissor area will be affected by further
	 * renderer actions.
	 */
	public void enableScissorTest(boolean enable)
	{
		if (enable)
			this.gl.glEnable(EnableCap.SCISSOR_TEST.getValue());
		else
			this.gl.glDisable(EnableCap.SCISSOR_TEST.getValue());
	}

	@Override
	public void setClearColor( Color color, double alpha )
	{
		this.clearColor.copy(color);
		this.clearAlpha = alpha;

		this.gl.glClearColor((float) this.clearColor.getR(), (float) this.clearColor.getG(), (float) this.clearColor.getB(), (float) this.clearAlpha);
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

		if ( color ) {
			bits |= ClearBufferMask.COLOR_BUFFER_BIT.getValue();
		}
		if ( depth ) {
			bits |= ClearBufferMask.DEPTH_BUFFER_BIT.getValue();
		}
		if ( stencil ) {
			bits |= ClearBufferMask.STENCIL_BUFFER_BIT.getValue();
		}

		this.gl.glClear(bits);
	}

	public void clearColor()
	{
		this.gl.glClear(ClearBufferMask.COLOR_BUFFER_BIT.getValue());
	}

	public void clearDepth()
	{
		this.gl.glClear(ClearBufferMask.DEPTH_BUFFER_BIT.getValue());
	}

	public void clearStencil()
	{
		this.gl.glClear(ClearBufferMask.STENCIL_BUFFER_BIT.getValue());
	}

	/**
	 * Clear {@link RenderTargetTexture} and GL buffers.
	 */
	public void clearTarget( RenderTargetTexture renderTarget,
							 boolean color, boolean depth, boolean stencil )
	{
		setRenderTarget( renderTarget );
		clear( color, depth, stencil );
	}

	public void resetGLState()
	{
		_currentProgram = 0;
		_currentCamera = null;

		_oldBlending = null;
		_oldDepthTest = null;
		_oldDepthWrite = null;
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

			this.gl.glEnableVertexAttribArray(attribute);
			_enabledAttributes.set(attribute, 1);

		}

	}

	private void  disableUnusedAttributes() {

		for ( int i = 0, l = _enabledAttributes.getLength(); i < l; i ++ ) {

			if ( _enabledAttributes.get( i ) != _newAttributes.get( i ) ) {

				this.gl.glDisableVertexAttribArray(i);
				_enabledAttributes.set(i, 0);

			}

		}
	}


	/**
	 * Morph Targets Buffer initialization
	 */
	private void setupMorphTargets ( Material material, GLGeometry geometrybuffer,
									 Mesh object )
	{

		// set base
		FastMap<Integer> attributes = material.getShader().getAttributesLocations();
		FastMap<Uniform> uniforms = material.getShader().getUniforms();

		if ( object.morphTargetBase != - 1 && attributes.get(POSITION) >= 0)
		{
			this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphTargetsBuffers.get(object.morphTargetBase));
			enableAttribute( attributes.get(POSITION) );
			this.gl.glVertexAttribPointer(attributes.get(POSITION), 3, DataType.FLOAT.getValue(), false, 0, 0);

		}
		else if ( attributes.get(POSITION) >= 0 )
		{
			this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometrybuffer.__webglVertexBuffer);
			enableAttribute( attributes.get(POSITION) );
			this.gl.glVertexAttribPointer(attributes.get(POSITION), 3, DataType.FLOAT.getValue(), false, 0, 0);
		}

		if ( !object.morphTargetForcedOrder.isEmpty() )
		{
			// set forced order

			int m = 0;
			List<Integer> order = object.morphTargetForcedOrder;
			List<Double> influences = object.morphTargetInfluences;

			while ( material instanceof HasSkinning
					&& m < ((HasSkinning)material).getNumSupportedMorphTargets()
					&& m < order.size()
					) {
				if ( attributes.get(MORPH_TARGET + m )  >= 0 )
				{
					this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphTargetsBuffers.get(order.get(m)));
					enableAttribute( attributes.get(MORPH_TARGET + m ) );
					this.gl.glVertexAttribPointer(attributes.get(MORPH_TARGET + m), 3, DataType.FLOAT.getValue(), false, 0, 0);

				}

				if (  attributes.get(MORPH_NORMAL + m )  >= 0 &&
						material instanceof HasSkinning && ((HasSkinning)material).isMorphNormals())
				{
					this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphNormalsBuffers.get(order.get(m)));
					enableAttribute( attributes.get(MORPH_NORMAL + m ));
					this.gl.glVertexAttribPointer(attributes.get(MORPH_NORMAL + m), 3, DataType.FLOAT.getValue(), false, 0, 0);
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

			} else if ( activeInfluenceIndices.isEmpty() ) {

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

					if ( attributes.get( MORPH_TARGET + m ) >= 0 ) {

						this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphTargetsBuffers.get(influenceIndex));
						enableAttribute( attributes.get( MORPH_TARGET + m ) );
						this.gl.glVertexAttribPointer(attributes.get(MORPH_TARGET + m), 3, DataType.FLOAT.getValue(), false, 0, 0);

					}

					if ( attributes.get( MORPH_NORMAL + m ) >= 0 &&
							((HasSkinning)material).isMorphNormals() ) {

						this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphNormalsBuffers.get(influenceIndex));
						enableAttribute( attributes.get( MORPH_NORMAL + m ) );
						this.gl.glVertexAttribPointer(attributes.get(MORPH_NORMAL + m), 3, DataType.FLOAT.getValue(), false, 0, 0);

					}

					object.__webglMorphTargetInfluences.set( m, influences.get( influenceIndex ));

				} else {
					object.__webglMorphTargetInfluences.set( m, 0);
				}

				m ++;
			}
		}

		// load updated influences uniform
		if( uniforms.get("morphTargetInfluences").getLocation() != -1 )
		{
			Float32Array vals = object.__webglMorphTargetInfluences;
			this.gl.glUniform1fv(uniforms.get("morphTargetInfluences").getLocation(), 1, vals.getTypedBuffer());
		}
	}

	public void renderBufferImmediate( GeometryObject object, Shader program, Material material ) {
		initAttributes();
	}

	private void setupVertexAttributes( Material material, Shader program, BufferGeometry geometry, int startIndex ) {

		FastMap<BufferAttribute> geometryAttributes = geometry.getAttributes();

		FastMap<Integer> programAttributes = program.getAttributesLocations();

		for ( String key: programAttributes.keySet()) {

			Integer programAttribute = programAttributes.get(key);

			if ( programAttribute >= 0 ) {

				BufferAttribute geometryAttribute = geometryAttributes.get( key );

				if ( geometryAttribute != null ) {

					int size = geometryAttribute.getItemSize();
					this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometryAttribute.getBuffer());

					enableAttribute( programAttribute );

					this.gl.glVertexAttribPointer(programAttribute, size, DataType.FLOAT.getValue(), false, 0, startIndex * size * 4); // 4 bytes per Float32

				}
			}

		}

		disableUnusedAttributes();
	}


	//camera, lights, fog, material, geometry, object
	public void renderBufferDirect( Camera camera, List<Light> lights, AbstractFog fog,
									Material material, BufferGeometry geometry, GeometryObject object )
	{
		if ( ! material.isVisible() )
			return;

		Shader program = setProgram(camera, lights, fog, material, object);

		material.getShader().getAttributesLocations();

		boolean updateBuffers = false;
		int wireframeBit = material instanceof HasWireframe &&
				((HasWireframe)material).isWireframe() ? 1 : 0;

		int geometryGroupHash = ( geometry.getId() * 0xffffff ) +
				( material.getShader().getId() * 2 ) + wireframeBit;

		if ( geometryGroupHash != this._currentGeometryGroupHash )
		{
			this._currentGeometryGroupHash = geometryGroupHash;
			updateBuffers = true;
		}

		if ( updateBuffers ) {

			initAttributes();

		}

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

				if ( offsets.isEmpty() ) {

					if ( updateBuffers ) {

						setupVertexAttributes( material, program, geometry, 0 );

						this.gl.glBindBuffer(BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), index.getBuffer());

					}

					this.gl.glDrawElements(mode.getValue(), index.getArray().getLength(), type.getValue(), 0);

					this.info.getRender().calls ++;
					this.info.getRender().vertices +=
							index.getArray().getLength(); // not really true, here vertices can be shared
					this.info.getRender().faces += index.getArray().getLength() / 3;

				} else {
					// if there is more than 1 chunk
					// must set attribute pointers to use new offsets for each chunk
					// even if geometry and materials didn't change

					for ( int i = 0, il = offsets.size(); i < il; ++ i )
					{

						int startIndex = offsets.get( i ).index;

						setupVertexAttributes( material, program, geometry, startIndex );
						this.gl.glBindBuffer(BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), index.getBuffer());

						this.gl.glDrawElements( mode.getValue(), offsets.get( i ).count, type.getValue(), offsets.get( i ).start * size  );

						getInfo().getRender().calls ++;
						getInfo().getRender().vertices +=
								offsets.get( i ).count; // not really true, here vertices can be shared
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

				BufferAttribute position = geometry.getAttribute(POSITION);

				// render non-indexed triangles

				this.gl.glDrawArrays( mode.getValue(), 0, position.getArray().getLength() / 3 );

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

			BufferAttribute position = geometry.getAttribute(POSITION);

			// render particles

			this.gl.glDrawArrays( BeginMode.POINTS.getValue(), 0, position.getArray().getLength() / 3 );

			this.info.getRender().calls ++;
			this.info.getRender().points += position.getArray().getLength() / 3;
		}
		else if ( object instanceof Line )
		{

			BeginMode mode = ( ((Line)object).getMode() == Line.MODE.STRIPS ) ? BeginMode.LINE_STRIP : BeginMode.LINES;
			object.setLineWidth(this.gl, ((LineBasicMaterial)material).getLinewidth());

			BufferAttribute index = geometry.getAttribute("index");

			if ( index != null ) {

				DrawElementsType type = DrawElementsType.UNSIGNED_SHORT;
				int size = 2;

				List<DrawCall> drawcalls = geometry.getDrawcalls();

				if ( drawcalls.isEmpty() ) {

					if ( updateBuffers ) {

						setupVertexAttributes( material, program, geometry, 0 );
						this.gl.glBindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), index.getBuffer() );

					}

					this.gl.glDrawElements( mode.getValue(), index.getArray().getLength(), type.getValue(), 0 ); // 2 bytes per Uint16Array

					this.info.getRender().calls ++;
					this.info.getRender().vertices +=
							index.getArray().getLength(); // not really true, here vertices can be shared

				} else {

					// if there is more than 1 chunk
					// must set attribute pointers to use new offsets for each chunk
					// even if geometry and materials didn't change

					if ( drawcalls.size() > 1 ) {
						updateBuffers = true;
					}

					for ( int i = 0, il = drawcalls.size(); i < il; i ++ ) {

						int startIndex = drawcalls.get( i ).index;

						if ( updateBuffers ) {

							setupVertexAttributes( material, program, geometry, startIndex );
							this.gl.glBindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), index.getBuffer() );

						}

						// render indexed lines

						this.gl.glDrawElements( mode.getValue(), drawcalls.get( i ).count, type.getValue(), drawcalls.get( i ).start * size ); // 2 bytes per Uint16Array

						this.info.getRender().calls ++;
						this.info.getRender().vertices +=
								drawcalls.get( i ).count; // not really true, here vertices can be shared

					}

				}

			} else {

				// non-indexed lines

				if ( updateBuffers ) {

					setupVertexAttributes( material, program, geometry, 0 );

				}

				BufferAttribute position = geometry.getAttribute(POSITION);

				this.gl.glDrawArrays( mode.getValue(), 0,  position.getArray().getLength() / 3 );

				this.info.getRender().calls ++;
				this.info.getRender().points += position.getArray().getLength() / 3;

			}
		}
	}

	public List<GeometryGroup> makeGroups( Geometry geometry, boolean usesFaceMaterial ) {

		long maxVerticesInGroup = GLExtensions.isSupported(this.gl, GLES20Ext.List.OES_element_index_uint) ? 4294967296L : 65535L;

		int numMorphTargets = geometry.getMorphTargets().size();
		int numMorphNormals = geometry.getMorphNormals().size();

		String groupHash;

		FastMap<Integer> hashMap = new FastMap<Integer>();

		FastMap<GeometryGroup> groups = new FastMap<GeometryGroup>();

		List<GeometryGroup> groupsList = new ArrayList<GeometryGroup>();

		for ( int f = 0, fl = geometry.getFaces().size(); f < fl; f ++ ) {

			Face3 face = geometry.getFaces().get(f);
			Integer materialIndex = usesFaceMaterial ? face.getMaterialIndex() : 0;

			if ( ! hashMap.containsKey(materialIndex) ) {

				hashMap.put(materialIndex.toString(), 0);

			}

			groupHash = materialIndex + "_" + hashMap.get( materialIndex );

			if ( ! groups.containsKey(groupHash) ) {

				GeometryGroup group = new GeometryGroup(materialIndex,
						numMorphTargets, numMorphNormals);

				groups.put(groupHash, group);
				groupsList.add( group );

			}

			if ( groups.get( groupHash ).getVertices() + 3 > maxVerticesInGroup ) {

				hashMap.put( materialIndex.toString(), hashMap.get( materialIndex ) + 1 );
				groupHash = materialIndex + "_" + hashMap.get( materialIndex );

				if ( ! groups.containsKey(groupHash) ) {

					GeometryGroup group = new GeometryGroup(materialIndex,
							numMorphTargets, numMorphNormals);
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
		boolean addBuffers;

		if ( GeometryGroup.geometryGroups.get( Integer.toString(geometry.getId()) ) == null ||
				geometry.isGroupsNeedUpdate() ) {

			this._webglObjects.put(Integer.toString(object.getId()), new ArrayList<GLObject>());

			GeometryGroup.geometryGroups.put( Integer.toString(geometry.getId()),
					makeGroups( geometry, material instanceof MeshFaceMaterial ));

			geometry.setGroupsNeedUpdate( false );

		}

		List<GeometryGroup> geometryGroupsList =
				GeometryGroup.geometryGroups.get( Integer.toString(geometry.getId()) );

		// create separate VBOs per geometry chunk
		for ( int i = 0, il = geometryGroupsList.size(); i < il; i ++ ) {

			GeometryGroup geometryGroup = geometryGroupsList.get( i );

			// initialise VBO on the first access
			if ( geometryGroup.__webglVertexBuffer == 0 )
			{
				object.createBuffers(this, geometryGroup);
				object.initBuffers(this.gl, geometryGroup);
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

		AbstractGeometry geometry = object instanceof GeometryObject ?
				((GeometryObject)object).getGeometry() : null;

		if ( geometry == null ) {

			// ImmediateRenderObject

		} else if ( ! geometry.__webglInit ) {

			geometry.__webglInit = true;

			if ( geometry instanceof BufferGeometry ) {

				//

			} else if ( object instanceof Mesh ) {

				initGeometryGroups( scene, (Mesh) object, (Geometry)geometry );

			} else if ( object instanceof Line ) {

				if ( geometry.__webglVertexBuffer == 0 ) {

					((Line)object).createBuffers(this);
					((Line)object).initBuffers(this.gl);

					geometry.setVerticesNeedUpdate( true );
					geometry.setColorsNeedUpdate( true );
					geometry.setLineDistancesNeedUpdate( true );

				}

			} else if ( object instanceof PointCloud && geometry.__webglVertexBuffer == 0 ) {

					((PointCloud)object).createBuffers(this);
					((PointCloud)object).initBuffers(this.gl);

					geometry.setVerticesNeedUpdate( true );
					geometry.setColorsNeedUpdate( true );

			}

		}

		if ( !object.__webglActive ) {

			object.__webglActive = true;

			if ( object instanceof Mesh ) {

				if ( geometry instanceof BufferGeometry ) {

					addBuffer( geometry, (GeometryObject) object );

				} else if ( geometry instanceof Geometry ) {

					List<GeometryGroup> geometryGroupsList =
							GeometryGroup.geometryGroups.get( Integer.toString(geometry.getId()) );

					for ( int i = 0,l = geometryGroupsList.size(); i < l; i ++ ) {

						addBuffer( geometryGroupsList.get( i ), (GeometryObject) object );

					}

				}

			} else if ( object instanceof Line || object instanceof PointCloud ) {
				addBuffer( geometry, (GeometryObject) object );
			}
		}

	}

	private void addBuffer( GLGeometry buffer, GeometryObject object ) {

		int id = object.getId();
		List<GLObject> list = _webglObjects.get(Integer.toString(id));
		if(list == null) {
			list = new ArrayList<GLObject>();
			_webglObjects.put(Integer.toString(id), list);
		}

		GLObject webGLObject = new GLObject(buffer, object);
		webGLObject.id = id;
		list.add(webGLObject);
	}

	private void projectObject( Object3D scene, Object3D object ) {

		if ( !object.isVisible() ) {
			return;
		}

		if ( object instanceof Scene /*|| object instanceof Group */) {

			// skip

		} else {

			if(!(object instanceof Light))
				initObject( object, scene );

			if ( object instanceof Light ) {

				lights.add( (Light) object );

			} else {

				List<GLObject> webglObjects = this._webglObjects.get( Integer.toString(object.getId()) );

				if ( webglObjects != null && ( !object.isFrustumCulled() ||
						_frustum.isIntersectsObject( (GeometryObject) object ) ) ) {

					updateObject( (GeometryObject) object, scene );

					for ( int i = 0, l = webglObjects.size(); i < l; i ++ ) {

						GLObject webglObject = webglObjects.get(i);

						webglObject.unrollBufferMaterial(this);

						webglObject.render = true;

						if ( this.sortObjects ) {

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

			((BufferGeometry)geometry).setDirectBuffers(this.gl);

		} else if ( object instanceof Mesh ) {

			// check all geometry groups

			if ( geometry.isGroupsNeedUpdate() ) {

				initGeometryGroups( scene, (Mesh)object, (Geometry)geometry );

			}

			List<GeometryGroup> geometryGroupsList =
					GeometryGroup.geometryGroups.get( Integer.toString(geometry.getId()) );

			for ( int i = 0, il = geometryGroupsList.size(); i < il; i ++ ) {

				GeometryGroup geometryGroup = geometryGroupsList.get( i );

				material = getBufferMaterial( object, geometryGroup );

				if ( geometry.isGroupsNeedUpdate() ) {

					((Mesh)object).initBuffers( this.gl, geometryGroup );

				}

				boolean customAttributesDirty = (material instanceof ShaderMaterial) &&
						((ShaderMaterial)material).getShader().areCustomAttributesDirty();

				if ( geometry.isVerticesNeedUpdate() || geometry.isMorphTargetsNeedUpdate() ||
						geometry.isElementsNeedUpdate() ||
						geometry.isUvsNeedUpdate() || geometry.isNormalsNeedUpdate() ||
						geometry.isColorsNeedUpdate() || geometry.isTangentsNeedUpdate() ||
						customAttributesDirty ) {

					((Mesh)object).setBuffers( this.gl, geometryGroup, BufferUsage.DYNAMIC_DRAW, ! ((Geometry)geometry).isDynamic(), material );

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

			boolean customAttributesDirty = (material instanceof ShaderMaterial) &&
					((ShaderMaterial)material).getShader().areCustomAttributesDirty();

			if ( geometry.isVerticesNeedUpdate() || geometry.isColorsNeedUpdate() ||
					geometry.isLineDistancesNeedUpdate() || customAttributesDirty ) {

				((Line)object).setBuffers( this.gl, BufferUsage.DYNAMIC_DRAW );

			}

			geometry.setVerticesNeedUpdate( false );
			geometry.setColorsNeedUpdate( false );
			geometry.setLineDistancesNeedUpdate( false );

			if(material instanceof ShaderMaterial ) {
				((ShaderMaterial)material).getShader().clearCustomAttributes();
			}


		} else if ( object instanceof PointCloud ) {

			material = getBufferMaterial( object, (Geometry)geometry );

			boolean customAttributesDirty = (material instanceof ShaderMaterial) &&
					material.getShader().areCustomAttributesDirty();

			if ( geometry.isVerticesNeedUpdate() || geometry.isColorsNeedUpdate() ||
					((PointCloud)object).isSortParticles() || customAttributesDirty ) {

				((PointCloud)object).setBuffers( this, BufferUsage.DYNAMIC_DRAW );

			}

			geometry.setVerticesNeedUpdate( false );
			geometry.setColorsNeedUpdate( false );

			if(material instanceof ShaderMaterial ) {
				material.getShader().clearCustomAttributes();
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
	public void render( Scene scene, Camera camera, RenderTargetTexture renderTarget,
						boolean forceClear )
	{
		// Render basic org.parallax3d.plugins
		if(renderPlugins( this.plugins, scene, camera, Plugin.TYPE.BASIC_RENDER ))
			return;

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

		_projScreenMatrix.multiply( camera.getProjectionMatrix(),
				camera.getMatrixWorldInverse() );
		_frustum.setFromMatrix( _projScreenMatrix );

		this.lights = new ArrayList<Light>();
		this.opaqueObjects = new ArrayList<GLObject>();
		this.transparentObjects = new ArrayList<GLObject>();

		projectObject( scene, scene );

		if ( this.isSortObjects() ) {

			Collections.sort(opaqueObjects, new Comparator<GLObject>() {

				@Override
				public int compare(GLObject a, GLObject b) {
					if ( a.z != b.z ) {

						return (int)(b.z - a.z);

					} else {

						return a.id - b.id;

					}

				}
			});

			Collections.sort(transparentObjects, new Comparator<GLObject>() {

				@Override
				public int compare(GLObject a, GLObject b) {
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

		// custom render org.parallax3d.plugins (pre pass)
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
			GLObject webglObject = this._webglObjectsImmediate.get( i );
			Object3D object = webglObject.object;

			if ( object.isVisible() ) {

				setupMatrices( object, camera );

				webglObject.unrollImmediateBufferMaterial();

			}

		}

		if ( scene.getOverrideMaterial() != null )
		{
			Material material = scene.getOverrideMaterial();

			setBlending( material.getBlending(), material.getBlendEquation(),
					material.getBlendSrc(), material.getBlendDst() );
			setDepthTest( material.isDepthTest() );
			setDepthWrite( material.isDepthWrite() );

			setPolygonOffset( material.isPolygonOffset(),
					material.getPolygonOffsetFactor(), material.getPolygonOffsetUnits() );

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

		// custom render org.parallax3d.plugins (post pass)
		renderPlugins( this.plugins, scene, camera, Plugin.TYPE.POST_RENDER );

		// Generate mipmap if we're using any kind of mipmap filtering
		if ( renderTarget != null && renderTarget.isGenerateMipmaps()
				&& renderTarget.getMinFilter() != TextureMinFilter.NEAREST
				&& renderTarget.getMinFilter() != TextureMinFilter.LINEAR)
		{
			renderTarget.updateRenderTargetMipmap(this.gl);
		}

		// Ensure depth buffer writing is enabled so it can be cleared on next render

		this.setDepthTest( true );
		this.setDepthWrite( true );

	}

	public void renderObjectsImmediate ( List<GLObject> renderList,
										 Boolean isTransparentMaterial, Camera camera,
										 List<Light> lights, AbstractFog fog,
										 boolean useBlending, Material overrideMaterial ) {

		Material material = null;

		for ( int i = 0, il = renderList.size(); i < il; i ++ ) {

			GLObject webglObject = renderList.get( i );
			GeometryObject object = webglObject.object;

			if ( object.isVisible() ) {

				if ( overrideMaterial != null) {

					material = overrideMaterial;

				} else {

					if(isTransparentMaterial != null)
						material = isTransparentMaterial ? webglObject.transparent :
								webglObject.opaque;

					if ( material == null )
						continue;

					if ( useBlending )
						setBlending( material.getBlending(), material.getBlendEquation(), material.getBlendSrc(), material.getBlendDst() );

					setDepthTest( material.isDepthTest() );
					setDepthWrite( material.isDepthWrite() );
					setPolygonOffset( material.isPolygonOffset(),
							material.getPolygonOffsetFactor(), material.getPolygonOffsetUnits() );

				}

				renderImmediateObject( camera, lights, fog, material, object );

			}

		}

	}

	public void renderImmediateObject( Camera camera, List<Light> lights,
									   AbstractFog fog, Material material,
									   GeometryObject object ) {

		Shader program = setProgram( camera, lights, fog, material, object );

		this._currentGeometryGroupHash = - 1;

		setMaterialFaces( material );

		renderBufferImmediate( object, program, material );
	}

	private boolean renderPlugins( List<Plugin> plugins, Scene scene, Camera camera, Plugin.TYPE type )
	{
		if ( plugins.isEmpty() )
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

			// reset state for plugin (to start from clean slate)
			this._currentProgram = 0;
			this._currentCamera = null;

			this._oldBlending = null;
			this._oldDepthTest = null;
			this._oldDepthWrite = null;
			this.cache_oldMaterialSided = null;

			this._currentGeometryGroupHash = -1;
			this._currentMaterialId = -1;

			this._lightsNeedUpdate = true;

			plugin.render( this.gl, camera, lights, _currentWidth, _currentHeight );

			// reset state after plugin (anything could have changed)

			this._currentProgram = 0;
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

	private void renderObjects (List<GLObject> renderList, Camera camera,
								List<Light> lights, AbstractFog fog, boolean useBlending )
	{
		renderObjects ( renderList, camera, lights, fog, useBlending, null);
	}

	//renderList, camera, lights, fog, useBlending, overrideMaterial
	private void renderObjects (List<GLObject> renderList, Camera camera,
								List<Light> lights, AbstractFog fog, boolean useBlending,
								Material overrideMaterial )
	{
		Material material;

		for ( int i = renderList.size() - 1; i != - 1; i -- ) {

			GLObject webglObject = renderList.get( i );

			GeometryObject object = webglObject.object;
			GLGeometry buffer = webglObject.buffer;

			setupMatrices( object, camera );

			if ( overrideMaterial != null) {

				material = overrideMaterial;

			} else {

				material = webglObject.material;
				//TODO: material = (isMaterialTransparent) ?
				// webglObject.transparent : webglObject.opaque;

				if ( material == null ) {
					continue;
				}

				if ( useBlending )
					setBlending( material.getBlending(), material.getBlendEquation(), material.getBlendSrc(), material.getBlendDst() );

				setDepthTest( material.isDepthTest() );
				setDepthWrite( material.isDepthWrite() );
				setPolygonOffset( material.isPolygonOffset(), material.getPolygonOffsetFactor(),
						material.getPolygonOffsetUnits() );

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
	public void renderBuffer( Camera camera, List<Light> lights, AbstractFog fog,
							  Material material, GLGeometry geometry, GeometryObject object )
	{
		if ( ! material.isVisible() )
			return;

		setProgram( camera, lights, fog, material, object );

		FastMap<Integer> attributes = material.getShader().getAttributesLocations();

		boolean updateBuffers = false;
		int wireframeBit = material instanceof HasWireframe &&
				((HasWireframe)material).isWireframe() ? 1 : 0;

		int geometryGroupHash = ( geometry.getId() * 0xffffff ) +
				(material.getShader().getId() * 2 ) + wireframeBit;

		if ( geometryGroupHash != this._currentGeometryGroupHash )
		{
			this._currentGeometryGroupHash = geometryGroupHash;
			updateBuffers = true;
		}

		if ( updateBuffers ) {

			initAttributes();

		}

		// vertices
		if ( !(material instanceof HasSkinning && ((HasSkinning)material).isMorphTargets()) &&
				attributes.get(POSITION) >= 0 )
		{
			if ( updateBuffers )
			{
				this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglVertexBuffer);
				enableAttribute( attributes.get(POSITION) );
				this.gl.glVertexAttribPointer(attributes.get(POSITION), 3, DataType.FLOAT.getValue(), false, 0, 0);
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
						this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), attribute.buffer);
						enableAttribute( attributes.get( attribute.belongsToAttribute ) );
						this.gl.glVertexAttribPointer(attributes.get(attribute.belongsToAttribute), attribute.size, DataType.FLOAT.getValue(), false, 0, 0);
					}
				}
			}

			// colors
			if ( attributes.get(COLOR) >= 0 )
			{
				if ( !((Geometry)object.getGeometry()).getColors().isEmpty() ||
						!((Geometry)object.getGeometry()).getFaces().isEmpty() ) {

					this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglColorBuffer);
					enableAttribute( attributes.get(COLOR) );
					this.gl.glVertexAttribPointer(attributes.get(COLOR), 3, DataType.FLOAT.getValue(), false, 0, 0);

				} else {

					this.gl.glVertexAttrib3f(attributes.get(COLOR), 1.0f, 1.0f, 1.0f);

				}
			}

			// normals
			if ( attributes.get(NORMAL) >= 0 )
			{
				this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglNormalBuffer);
				enableAttribute( attributes.get(NORMAL) );
				this.gl.glVertexAttribPointer(attributes.get(NORMAL), 3, DataType.FLOAT.getValue(), false, 0, 0);
			}

			// tangents
			if ( attributes.get(TANGENT) >= 0 )
			{
				this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglTangentBuffer);
				enableAttribute( attributes.get(TANGENT) );
				this.gl.glVertexAttribPointer(attributes.get(TANGENT), 4, DataType.FLOAT.getValue(), false, 0, 0);
			}

			// uvs
			if ( attributes.get("uv") >= 0 )
			{
				if ( ((Geometry)object.getGeometry()).getFaceVertexUvs().get( 0 ) != null )
				{
					this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglUVBuffer);
					enableAttribute( attributes.get("uv") );
					this.gl.glVertexAttribPointer(attributes.get("uv"), 2, DataType.FLOAT.getValue(), false, 0, 0);

				} else {

					this.gl.glVertexAttrib2f(attributes.get("uv"), 0.0f, 0.0f);
				}
			}

			if ( attributes.get("uv2") >= 0 )
			{
				if ( ((Geometry)object.getGeometry()).getFaceVertexUvs().get( 1 ) != null )
				{
					this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglUV2Buffer);
					enableAttribute( attributes.get("uv2") );
					this.gl.glVertexAttribPointer(attributes.get("uv2"), 2, DataType.FLOAT.getValue(), false, 0, 0);

				} else {

					this.gl.glVertexAttrib2f(attributes.get("uv2"), 0.0f, 0.0f);
				}
			}

			if ( material instanceof HasSkinning && ((HasSkinning)material).isSkinning() &&
					attributes.get(SKIN_INDEX) >= 0 && attributes.get(SKIN_WEIGHT) >= 0 )
			{
				this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglSkinIndicesBuffer);
				enableAttribute( attributes.get(SKIN_INDEX) );
				this.gl.glVertexAttribPointer(attributes.get(SKIN_INDEX), 4, DataType.FLOAT.getValue(), false, 0, 0);

				this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglSkinWeightsBuffer);
				enableAttribute( attributes.get(SKIN_WEIGHT) );
				this.gl.glVertexAttribPointer(attributes.get(SKIN_WEIGHT), 4, DataType.FLOAT.getValue(), false, 0, 0);
			}

			// line distances

			if ( attributes.get(LINE_DISTANCE) != null && attributes.get(LINE_DISTANCE) >= 0 ) {

				this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglLineDistanceBuffer);
				enableAttribute( attributes.get(LINE_DISTANCE) );
				this.gl.glVertexAttribPointer(attributes.get(LINE_DISTANCE), 1, DataType.FLOAT.getValue(), false, 0, 0);

			}

		}

		disableUnusedAttributes();

		// Render object's buffers
		object.renderBuffer(this, geometry, updateBuffers);
	}

	private void initMaterial ( Material material, List<Light> lights,
								AbstractFog fog, GeometryObject object )
	{
		Log.debug("WebGlRender: Called initMaterial for material: " + material.getClass().getName() + " and object " + object.getClass().getName());

		// heuristics to create shader parameters according to lights in the scene
		// (not to blow over maxLights budget)
		FastMap<Integer> maxLightCount = allocateLights( lights );
		int maxShadows = allocateShadows( lights );

		ProgramParameters parameters = new ProgramParameters();

		parameters.gammaInput = this.isGammaInput();
		parameters.gammaOutput = this.isGammaOutput();

		parameters.precision = this._precision;

		parameters.supportsVertexTextures = this._supportsVertexTextures;

		if(fog != null)
		{
			parameters.useFog  = true;
			parameters.useFog2 = fog instanceof FogExp2;
		}

		parameters.logarithmicDepthBuffer = this._logarithmicDepthBuffer;

		parameters.maxBones = allocateBones( object );

		if(object instanceof SkinnedMesh)
		{
			parameters.useVertexTexture = this._supportsBoneTextures;
		}

		parameters.maxMorphTargets = this.maxMorphTargets;
		parameters.maxMorphNormals = this.maxMorphNormals;

		parameters.maxDirLights   = maxLightCount.get("directional");
		parameters.maxPointLights = maxLightCount.get("point");
		parameters.maxSpotLights  = maxLightCount.get("spot");
		parameters.maxHemiLights  = maxLightCount.get("hemi");

		parameters.maxShadows = maxShadows;

		for(Plugin plugin: this.plugins) {
			if (plugin instanceof ShadowMap && plugin.isEnabled() &&
					object.isReceiveShadow()) {
				parameters.shadowMapEnabled = object.isReceiveShadow() && maxShadows > 0;
				parameters.shadowMapSoft = ((ShadowMap) plugin).isSoft();
				parameters.shadowMapDebug = ((ShadowMap) plugin).isDebugEnabled();
				parameters.shadowMapCascade = ((ShadowMap) plugin).isCascade();
				break;
			}
		}

		material.updateProgramParameters(parameters);
		Log.debug("WebGlRender: initMaterial() called new Program");

		String cashKey = material.getShader().getFragmentSource()
				+ material.getShader().getVertexSource()
				+ parameters.toString();

		if(this._programs.containsKey(cashKey))
		{
			material.setShader( this._programs.get(cashKey) );
		}
		else
		{
			Shader shader = material.buildShader(this.gl, parameters);

			this._programs.put(cashKey, shader);

			this.getInfo().getMemory().programs = _programs.size();
		}

		FastMap<Integer> attributes = material.getShader().getAttributesLocations();

		if(material instanceof HasSkinning)
		{
			if ( ((HasSkinning)material).isMorphTargets())
			{
				int numSupportedMorphTargets = 0;
				for ( int i = 0; i < this.maxMorphTargets; i ++ )
				{
					String id = MORPH_TARGET + i;

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
					String id = MORPH_NORMAL + i;

					if ( attributes.get( id ) >= 0 )
					{
						numSupportedMorphNormals ++;
					}
				}

				((HasSkinning)material).setNumSupportedMorphNormals(numSupportedMorphNormals);
			}
		}
	}

	private Shader setProgram( Camera camera, List<Light> lights, AbstractFog fog,
							   Material material, GeometryObject object )
	{
		// Use new material units for new shader
		this._usedTextureUnits = 0;

		if(material.isNeedsUpdate())
		{
			if(material.getShader() == null || material.getShader().getProgram() == 0)
				material.deallocate(this);

			initMaterial( material, lights, fog, object );
			material.setNeedsUpdate(false);
		}

		if ( material instanceof HasSkinning && ((HasSkinning)material).isMorphTargets() 
				&& object instanceof Mesh && ((Mesh)object).__webglMorphTargetInfluences == null )
		{
			((Mesh)object).__webglMorphTargetInfluences = Float32Array.create( this.maxMorphTargets );
		}

		boolean refreshProgram = false;
		boolean refreshMaterial = false;
		boolean refreshLights = false;

		Shader shader = material.getShader();
		int program = shader.getProgram();
		FastMap<Uniform> mUniforms = shader.getUniforms();

		if ( program != _currentProgram )
		{
			this.gl.glUseProgram(program);
			this._currentProgram = program;

			refreshProgram = true;
			refreshMaterial = true;
			refreshLights = true;
		}

		if ( material.getId() != this._currentMaterialId )
		{
			if(_currentMaterialId == -1) {
				refreshLights = true;
			}

			this._currentMaterialId = material.getId();
			refreshMaterial = true;
		}

		if ( refreshProgram || !camera.equals( this._currentCamera) )
		{
			this.gl.glUniformMatrix4fv(mUniforms.get("projectionMatrix").getLocation(), 1, false, camera.getProjectionMatrix().getArray().getTypedBuffer());

			if ( _logarithmicDepthBuffer ) {

				this.gl.glUniform1f(mUniforms.get("logDepthBufFC").getLocation(), (float) (2.0 / (Math.log(((HasNearFar) camera).getFar() + 1.0) / 0.6931471805599453 /*Math.LN2*/)));

			}

			if ( !camera.equals( this._currentCamera) )
				this._currentCamera = camera;

			// load material specific uniforms
			// (shader material also gets them for the sake of genericity)
			if ( (material.getClass() == ShaderMaterial.class ||
					material.getClass() == MeshPhongMaterial.class ||
					material instanceof HasEnvMap && ((HasEnvMap)material).getEnvMap() != null) &&
					mUniforms.get("cameraPosition").getLocation() != -1 ) {
				
				_vector3.setFromMatrixPosition( camera.getMatrixWorld() );
				this.gl.glUniform3f(mUniforms.get("cameraPosition").getLocation(), (float)_vector3.getX(), (float)_vector3.getY(), (float)_vector3.getZ());
			}

			if ( (material.getClass() == MeshPhongMaterial.class ||
					material.getClass() == MeshLambertMaterial.class ||
					material.getClass() == MeshBasicMaterial.class ||
					material.getClass() == ShaderMaterial.class ||
					material instanceof HasSkinning && 
					((HasSkinning)material).isSkinning()) &&
					mUniforms.get("viewMatrix").getLocation() != -1 ) {
				
				this.gl.glUniformMatrix4fv(mUniforms.get("viewMatrix").getLocation(), 1, false, camera.getMatrixWorldInverse().getArray().getTypedBuffer());
			}
		}

		// skinning uniforms must be set even if material didn't change
		// auto-setting of texture unit for bone texture must go before other textures
		// not sure why, but otherwise weird things happen
		if ( material instanceof HasSkinning && ((HasSkinning)material).isSkinning() )
		{
			if ( object instanceof SkinnedMesh && ((SkinnedMesh)object).isUseVertexTexture() &&
					this._supportsBoneTextures)
			{
				if ( mUniforms.get("boneTexture").getLocation() != -1 )
				{
					int textureUnit = getTextureUnit();

					this.gl.glUniform1i(mUniforms.get("boneTexture").getLocation(), textureUnit);
					setTexture( ((SkinnedMesh)object).boneTexture, textureUnit );
				}
			}
			else
			{
				if ( mUniforms.get("boneGlobalMatrices").getLocation() != -1 )
				{
					this.gl.glUniformMatrix4fv(mUniforms.get("boneGlobalMatrices").getLocation(), 1, false, ((SkinnedMesh) object).boneMatrices.getTypedBuffer());
				}
			}
		}

		if ( refreshMaterial )
		{
			// refresh uniforms common to several materials
			if ( fog != null && material instanceof HasFog && ((HasFog)material).isFog())
				fog.refreshUniforms( mUniforms );

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
					this._lights.refreshUniformsLights( mUniforms );
				}
			}

			material.refreshUniforms(camera, this.gammaInput);

			if ( object.isReceiveShadow() && ! material.isShadowPass() )
				refreshUniformsShadow( mUniforms, lights );

			// load common uniforms
			loadUniformsGeneric( mUniforms );

		}

		loadUniformsMatrices( mUniforms, object );

		if ( mUniforms.get("modelMatrix").getLocation() != 0 )
			this.gl.glUniformMatrix4fv(mUniforms.get("modelMatrix").getLocation(), 1, false, object.getMatrixWorld().getArray().getTypedBuffer());

		return shader;
	}

	private void refreshUniformsShadow( FastMap<Uniform> uniforms, List<Light> lights )
	{
		if ( uniforms.containsKey(SHADOW_MATRIX) )
		{
			// Make them zero
			uniforms.get("shadowMap").setValue(new ArrayList<Texture>());
			uniforms.get("shadowMapSize").setValue(new ArrayList<Vector2>());
			uniforms.get(SHADOW_MATRIX).setValue(new ArrayList<Matrix4>());
			List<Texture> shadowMap = (List<Texture>)uniforms.get("shadowMap").getValue();
			List<Vector2> shadowMapSize = (List<Vector2>)uniforms.get("shadowMapSize").getValue();
			List<Matrix4> shadowMatrix = (List<Matrix4>)uniforms.get(SHADOW_MATRIX).getValue();

			int j = 0;
			for ( Light light: lights)
			{
				if ( ! light.isCastShadow() ) {
					continue;
				}

				if ( light instanceof ShadowLight && ! ((ShadowLight)light).isShadowCascade() )
				{
					ShadowLight shadowLight = (ShadowLight) light;

					shadowMap.add(shadowLight.getShadowMap() );
					shadowMapSize.add(shadowLight.getShadowMapSize() );
					shadowMatrix.add(shadowLight.getShadowMatrix() );

					((Float32Array)uniforms.get("shadowDarkness").getValue()).set( j,
							shadowLight.getShadowDarkness() );
					((Float32Array)uniforms.get("shadowBias").getValue()).set( j,
							shadowLight.getShadowBias() );
					j++;
				}
			}
		}
	}

	// Uniforms (load to GPU)

	private void loadUniformsMatrices ( FastMap<Uniform> uniforms, GeometryObject object )
	{
		GeometryObject objectImpl = object;
		this.gl.glUniformMatrix4fv(uniforms.get("modelViewMatrix").getLocation(), 1, false, objectImpl._modelViewMatrix.getArray().getTypedBuffer());

		if ( uniforms.containsKey("normalMatrix") )
			this.gl.glUniformMatrix3fv(uniforms.get("normalMatrix").getLocation(), 1, false, objectImpl._normalMatrix.getArray().getTypedBuffer());
	}

	private void loadUniformsGeneric( FastMap<Uniform> materialUniforms )
	{
		for ( String key: materialUniforms.keySet() )
		{
			Uniform uniform = materialUniforms.get(key);
			int location = uniform.getLocation();

			if ( location == -1 ) {
				continue;
			}

			Object value = uniform.getValue();
			Uniform.TYPE type = uniform.getType();
			// Up textures also for undefined values
			if ( type != Uniform.TYPE.T && value == null ) {
				continue;
			}

			if(type == Uniform.TYPE.I) // single integer
			{
				this.gl.glUniform1i(location, (value instanceof Boolean) ? ((Boolean) value) ? 1 : 0 : (Integer) value);
			}
			else if(type == Uniform.TYPE.F) // single float
			{
				this.gl.glUniform1f(location, value instanceof Float ? (Float) value : ((Double)value).floatValue());
			}
			else if(type == Uniform.TYPE.V2) // single Vector2
			{
				this.gl.glUniform2f(location, (float)((Vector2) value).getX(), (float)((Vector2) value).getX());
			}
			else if(type == Uniform.TYPE.V3) // single Vector3
			{
				this.gl.glUniform3f(location, (float)((Vector3) value).getX(), (float)((Vector3) value).getY(), (float)((Vector3) value).getZ());
			}
			else if(type == Uniform.TYPE.V4) // single Vector4
			{
				this.gl.glUniform4f(location, (float)((Vector4) value).getX(), (float)((Vector4) value).getY(), (float)((Vector4) value).getZ(), (float)((Vector4) value).getW());
			}
			else if(type == Uniform.TYPE.C) // single Color
			{
				this.gl.glUniform3f(location, (float)((Color) value).getR(), (float)((Color) value).getG(), (float)((Color) value).getB());
			}
			else if(type == Uniform.TYPE.FV1) // flat array of floats (JS or typed array)
			{
				this.gl.glUniform1fv(location, ((Float32Array) value).getLength(), ((Float32Array) value).getTypedBuffer());
			}
			else if(type == Uniform.TYPE.FV) // flat array of floats with 3 x N size (JS or typed array)
			{
				this.gl.glUniform3fv(location, ((Float32Array) value).getLength() / 3, ((Float32Array) value).getTypedBuffer());
			}
			else if(type == Uniform.TYPE.V2V) // List of Vector2
			{
				List<Vector2> listVector2f = (List<Vector2>) value;
				Float32Array cacheArray = uniform.getCacheArray();
				if (cacheArray  == null )
					uniform.setCacheArray(cacheArray = Float32Array.create( 2 * listVector2f.size() ) );

				for ( int i = 0, il = listVector2f.size(); i < il; i ++ )
				{
					int offset = i * 2;

					cacheArray.set(offset, listVector2f.get(i).getX());
					cacheArray.set(offset + 1, listVector2f.get(i).getY());
				}

				this.gl.glUniform2fv(location, uniform.getCacheArray().getLength() / 2,
						cacheArray.getTypedBuffer());
			}
			else if(type == Uniform.TYPE.V3V) // List of Vector3
			{
				List<Vector3> listVector3f = (List<Vector3>) value;
				Float32Array cacheArray = uniform.getCacheArray();
				if ( cacheArray == null )
					uniform.setCacheArray(cacheArray = Float32Array.create( 3 * listVector3f.size() ) );

				for ( int i = 0, il = listVector3f.size(); i < il; i ++ )
				{
					int offset = i * 3;

					uniform.getCacheArray().set(offset, listVector3f.get( i ).getX());
					uniform.getCacheArray().set(offset + 1, listVector3f.get( i ).getY());
					uniform.getCacheArray().set(offset + 2 , listVector3f.get( i ).getZ());
				}

				this.gl.glUniform3fv(location, cacheArray.getLength() / 3,
						cacheArray.getTypedBuffer());
			}
			else if(type == Uniform.TYPE.V4V) // List of Vector4
			{
				List<Vector4> listVector4f = (List<Vector4>) value;
				Float32Array cacheArray = uniform.getCacheArray();
				if ( cacheArray == null)
					uniform.setCacheArray(cacheArray = Float32Array.create( 4 * listVector4f.size() ) );


				for ( int i = 0, il = listVector4f.size(); i < il; i ++ )
				{
					int offset = i * 4;

					uniform.getCacheArray().set(offset, listVector4f.get( i ).getX());
					uniform.getCacheArray().set(offset + 1, listVector4f.get( i ).getY());
					uniform.getCacheArray().set(offset + 2, listVector4f.get( i ).getZ());
					uniform.getCacheArray().set(offset + 3, listVector4f.get( i ).getW());
				}

				this.gl.glUniform4fv(location, cacheArray.getLength() / 4,
						cacheArray.getTypedBuffer() );
			}
			else if(type == Uniform.TYPE.M4) // single Matrix4
			{
				Matrix4 matrix4 = (Matrix4) value;
				Float32Array cacheArray = uniform.getCacheArray();
				if ( cacheArray == null )
					uniform.setCacheArray(cacheArray = Float32Array.create( 16 ) );

				matrix4.flattenToArrayOffset( cacheArray );
				this.gl.glUniformMatrix4fv(location, 1, false, cacheArray.getTypedBuffer());
			}
			else if(type == Uniform.TYPE.M4V) // List of Matrix4
			{
				List<Matrix4> listMatrix4f = (List<Matrix4>) value;
				Float32Array cacheArray = uniform.getCacheArray();
				if ( cacheArray == null )
					uniform.setCacheArray(cacheArray = Float32Array.create( 16 * listMatrix4f.size() ) );

				for ( int i = 0, il = listMatrix4f.size(); i < il; i ++ )
					listMatrix4f.get( i ).flattenToArrayOffset( cacheArray, i * 16 );

				this.gl.glUniformMatrix4fv(location, cacheArray.getLength() / 16,
						false, cacheArray.getTypedBuffer());
			}
			else if(type == Uniform.TYPE.T) // single Texture (2d or cube)
			{
				Texture texture = (Texture)value;
				int textureUnit = getTextureUnit();

				this.gl.glUniform1i(location, textureUnit);

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
			else if(type == Uniform.TYPE.TV) //List of Texture (2d)
			{
				List<Texture> textureList = (List<Texture>)value;
				int[] units = new int[textureList.size()];

				for( int i = 0, il = textureList.size(); i < il; i ++ )
				{
					units[ i ] = getTextureUnit();
				}

				this.gl.glUniform1iv(location, 1, units, 0);

				for( int i = 0, il = textureList.size(); i < il; i ++ )
				{
					Texture texture = textureList.get( i );
					int textureUnit = units[ i ];

					if ( texture == null ) {
						continue;
					}

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
			Log.debug("WebGlRenderer: Trying to use " + textureUnit + " texture units while this GPU supports only " + this._maxTextures);
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
				this.gl.glDisable(EnableCap.CULL_FACE.getValue());
			else
				this.gl.glEnable(EnableCap.CULL_FACE.getValue());

			if ( material.getSides() == Material.SIDE.BACK )
				this.gl.glFrontFace(FrontFaceDirection.CW.getValue());
			else
				this.gl.glFrontFace(FrontFaceDirection.CCW.getValue());

			this.cache_oldMaterialSided = material.getSides();
		}
	}

	public void setDepthTest( boolean depthTest )
	{
		if ( this._oldDepthTest == null || this._oldDepthTest != depthTest )
		{
			if ( depthTest )
				this.gl.glEnable(EnableCap.DEPTH_TEST.getValue());
			else
				this.gl.glDisable(EnableCap.DEPTH_TEST.getValue());

			this._oldDepthTest = depthTest;
		}
	}

	public void setDepthWrite(boolean depthWrite )
	{
		if ( this._oldDepthWrite == null || this._oldDepthWrite != depthWrite )
		{
			this.gl.glDepthMask(depthWrite);
			_oldDepthWrite = depthWrite;
		}
	}

	private void setPolygonOffset( boolean polygonoffset, double factor, double units )
	{
		if ( this._oldPolygonOffset == null || this._oldPolygonOffset != polygonoffset )
		{
			if ( polygonoffset )
				this.gl.glEnable(EnableCap.POLYGON_OFFSET_FILL.getValue());
			else
				this.gl.glDisable(EnableCap.POLYGON_OFFSET_FILL.getValue());

			this._oldPolygonOffset = polygonoffset;
		}

		if ( polygonoffset && ( _oldPolygonOffsetFactor == null ||
				_oldPolygonOffsetUnits == null ||
				_oldPolygonOffsetFactor != factor ||
				_oldPolygonOffsetUnits != units )
		) {
			this.gl.glPolygonOffset((float)factor, (float)units);

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
				this.gl.glDisable(EnableCap.BLEND.getValue());

			}
			else if( blending == Material.BLENDING.ADDITIVE)
			{

				this.gl.glEnable(EnableCap.BLEND.getValue());
				this.gl.glBlendEquation(BlendEquationMode.FUNC_ADD.getValue());
				this.gl.glBlendFunc(BlendingFactorSrc.SRC_ALPHA.getValue(), BlendingFactorDest.ONE.getValue());

				// TODO: Find blendFuncSeparate() combination
			}
			else if( blending == Material.BLENDING.SUBTRACTIVE)
			{
				this.gl.glEnable(EnableCap.BLEND.getValue());
				this.gl.glBlendEquation(BlendEquationMode.FUNC_ADD.getValue());
				this.gl.glBlendFunc(BlendingFactorSrc.ZERO.getValue(), BlendingFactorDest.ONE_MINUS_SRC_COLOR.getValue());

				// TODO: Find blendFuncSeparate() combination
			}
			else if( blending == Material.BLENDING.MULTIPLY)
			{
				this.gl.glEnable(EnableCap.BLEND.getValue());
				this.gl.glBlendEquation(BlendEquationMode.FUNC_ADD.getValue());
				this.gl.glBlendFunc(BlendingFactorSrc.ZERO.getValue(), BlendingFactorDest.SRC_COLOR.getValue());

			}
			else if( blending == Material.BLENDING.CUSTOM)
			{
				this.gl.glEnable(EnableCap.BLEND.getValue());

			}
			// NORMAL
			else
			{
				this.gl.glEnable(EnableCap.BLEND.getValue());
				this.gl.glBlendEquationSeparate(BlendEquationMode.FUNC_ADD.getValue(), BlendEquationMode.FUNC_ADD.getValue());
				this.gl.glBlendFuncSeparate(BlendingFactorSrc.SRC_ALPHA.getValue(),
						BlendingFactorDest.ONE_MINUS_SRC_ALPHA.getValue(),
						BlendingFactorSrc.ONE.getValue(),
						BlendingFactorDest.ONE_MINUS_SRC_ALPHA.getValue());
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
				this.gl.glBlendEquation(blendEquation.getValue());
				this._oldBlendEquation = blendEquation;
			}

			if ( blendSrc != _oldBlendSrc || blendDst != _oldBlendDst )
			{
				this.gl.glBlendFunc(blendSrc.getValue(), blendDst.getValue());

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
		this.gl.glActiveTexture(TextureUnit.TEXTURE0.getValue() + slot);
		this.gl.glBindTexture(TextureTarget.TEXTURE_CUBE_MAP.getValue(), texture.getWebGlTexture());
	}

	public void setTexture( Texture texture, int slot )
	{
		if ( texture.isNeedsUpdate())
		{
			if ( texture.getWebGlTexture() == 0 )
			{
				texture.setWebGlTexture( this.gl.glGenTexture() );

				this.getInfo().getMemory().textures ++;
			}

			this.gl.glActiveTexture(TextureUnit.TEXTURE0.getValue() + slot);
			this.gl.glBindTexture(TextureTarget.TEXTURE_2D.getValue(), texture.getWebGlTexture());

			this.gl.glPixelStorei( PixelStoreParameter.UNPACK_ALIGNMENT.getValue(), texture.getUnpackAlignment() );

			TextureData image = texture.getImage();

			texture.setTextureParameters( this.gl, getMaxAnisotropy(), TextureTarget.TEXTURE_2D, image.isPowerOfTwo() );

			if ( texture instanceof CompressedTexture )
			{
				List<DataTexture> mipmaps = ((CompressedTexture) texture).getMipmaps();

				for( int i = 0, il = mipmaps.size(); i < il; i ++ )
				{
					DataTexture mipmap = mipmaps.get( i );
					this.gl.glCompressedTexImage2D(TextureTarget.TEXTURE_2D.getValue(), i,
							((CompressedTexture) texture).getCompressedFormat(),
							mipmap.getWidth(), mipmap.getHeight(), 0,
							mipmap.getData().getByteLength(),
							mipmap.getData().getBuffer());
				}
			}
			else if ( texture instanceof DataTexture )
			{
				TypeArray texData = ((DataTexture) texture).getData();
				this.gl.glTexImage2D(TextureTarget.TEXTURE_2D.getValue(), 0,
						((DataTexture) texture).getWidth(),
						((DataTexture) texture).getHeight(),
						0,
						texture.getFormat().getValue(),
						texture.getType().getValue(),
						texData.getByteLength(),
						texData.getBuffer());
			}
			// glTexImage2D does not apply to render target textures
			else if (!(texture instanceof RenderTargetTexture))
			{
				image.glTexImage2D(this.gl, TextureTarget.TEXTURE_2D.getValue(), texture.getFormat(), texture.getType());
			}

			if ( texture.isGenerateMipmaps() && image.isPowerOfTwo() )
				this.gl.glGenerateMipmap(TextureTarget.TEXTURE_2D.getValue());

			texture.setNeedsUpdate(false);
		}
		// Needed to check webgl texture in case deferred loading
		else if (texture.getWebGlTexture() != 0)
		{
			this.gl.glActiveTexture(TextureUnit.TEXTURE0.getValue() + slot);
			this.gl.glBindTexture(TextureTarget.TEXTURE_2D.getValue(), texture.getWebGlTexture());
		}
	}

	private void setCubeTexture ( CubeTexture texture, int slot )
	{
		if ( !texture.isValid() )
			return;

		if ( texture.isNeedsUpdate() )
		{
			if ( texture.getWebGlTexture() == 0 )
			{
				texture.setWebGlTexture(this.gl.glGenTexture());
				this.getInfo().getMemory().textures += 6;
			}

			this.gl.glActiveTexture( TextureUnit.TEXTURE0.getValue() + slot );
			this.gl.glBindTexture(TextureTarget.TEXTURE_CUBE_MAP.getValue(), texture.getWebGlTexture());

			List<TextureData> cubeImage = new ArrayList<TextureData>();

			for ( int i = 0; i < 6; i ++ ) {
				cubeImage.add(this.autoScaleCubemaps
						? texture.getImage(i).clampToMaxSize(this._maxCubemapSize)
						: texture.getImage(i));
			}

			texture.setTextureParameters( this.gl, getMaxAnisotropy(), TextureTarget.TEXTURE_CUBE_MAP, true /*power of two*/ );

			for ( int i = 0; i < 6; i ++ )
			{
				TextureData img = cubeImage.get(i);
				img.toPowerOfTwo();
				img.glTexImage2D(this.gl, TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X.getValue() + i, texture.getFormat(), texture.getType());
			}

			if ( texture.isGenerateMipmaps() )
				this.gl.glGenerateMipmap( TextureTarget.TEXTURE_CUBE_MAP.getValue() );

			texture.setNeedsUpdate(false);
		}
		else
		{
			this.gl.glActiveTexture( TextureUnit.TEXTURE0.getValue() + slot );
			this.gl.glBindTexture(TextureTarget.TEXTURE_CUBE_MAP.getValue(), texture.getWebGlTexture());
		}

	}

	/**
	 * Setup render target
	 *
	 * @param renderTarget the render target
	 */
	public void setRenderTarget( RenderTargetTexture renderTarget) {
		int framebuffer = 0;

		int width, height, vx, vy;

		if(renderTarget != null)
		{
			renderTarget.setRenderTarget(this.gl);
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
			this.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebuffer);
			this.gl.glViewport(vx, vy, width, height);

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
		if ( this._supportsBoneTextures && object instanceof SkinnedMesh &&
				((SkinnedMesh)object).isUseVertexTexture() )
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

			int nVertexUniforms = this.getIntGlParam(GL20.GL_MAX_VERTEX_UNIFORM_VECTORS);
			int nVertexMatrices = ( nVertexUniforms - 20 ) / 4;

			int maxBones = nVertexMatrices;

			if ( object instanceof SkinnedMesh )
			{
				maxBones = Math.min( ((SkinnedMesh)object).getBones().size(), maxBones );

				if ( maxBones < ((SkinnedMesh)object).getBones().size() )
				{
					Log.warn("WebGLRenderer: too many bones - " + ((SkinnedMesh) object).getBones().size()
							+ ", this GPU supports just " + maxBones + " (try OpenGL instead of ANGLE)");
				}
			}

			return maxBones;
		}
	}

	private FastMap<Integer> allocateLights ( List<Light> lights )
	{
		int dirLights = 0, pointLights = 0, spotLights = 0, hemiLights = 0;

		for(Light light: lights)
		{
			if ( light instanceof ShadowLight && ((ShadowLight)light).isOnlyShadow() )
				continue;

			if ( light instanceof DirectionalLight ) {
				dirLights ++;
			}
			if ( light instanceof PointLight ) {
				pointLights ++;
			}
			if ( light instanceof SpotLight ) {
				spotLights ++;
			}
			if ( light instanceof HemisphereLight ) {
				hemiLights ++;
			}
		}

		FastMap<Integer> retval =  new FastMap<>();
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

	/**
	 * This should be called from Android's onSurfaceChanged() or equivalent
	 * unless you call one of the @link{#setViewport} methods.
	 * @param newWidth
	 * @param newHeight
	 */
	public void onViewportResize(int newWidth, int newHeight) {
		_viewportWidth = newWidth;
		_viewportHeight = newHeight;
		fireViewportResizeEvent(newWidth, newHeight);
	}

	private void fireViewportResizeEvent(int width, int height)
	{
		ViewportResizeBus.onViewportResize(width, height);
	}
}
