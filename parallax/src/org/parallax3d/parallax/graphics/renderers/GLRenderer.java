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

import java.util.*;

import com.sun.prism.RenderTarget;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.graphics.extras.objects.ImmediateRenderObject;
import org.parallax3d.parallax.graphics.materials.*;
import org.parallax3d.parallax.graphics.objects.*;
import org.parallax3d.parallax.graphics.renderers.gl.*;
import org.parallax3d.parallax.system.*;
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
import org.parallax3d.parallax.graphics.textures.DataTexture;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.graphics.cameras.HasNearFar;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.lights.HemisphereLight;
import org.parallax3d.parallax.graphics.lights.Light;
import org.parallax3d.parallax.graphics.lights.PointLight;
import org.parallax3d.parallax.graphics.lights.ShadowLight;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Matrix3;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector4;
import org.parallax3d.parallax.graphics.scenes.AbstractFog;
import org.parallax3d.parallax.graphics.scenes.FogExp2;
import org.parallax3d.parallax.graphics.scenes.Scene;

import org.parallax3d.parallax.graphics.textures.TextureData;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.GLES20Ext;
import org.parallax3d.parallax.system.gl.GLHelpers;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.TypeArray;
import org.parallax3d.parallax.system.gl.enums.*;

/**
 * The WebGL renderer displays your beautifully crafted {@link Scene}s using WebGL, if your device supports it.
 */
@ThreejsObject("THREE.WebGLRenderer")
public class GLRenderer extends Renderer
{
	GL20 gl;

	GLRendererInfo info;

	List<Plugin> plugins;

	GLCapabilities capabilities;

	GLState state;
	GLProperties properties;
	GLObjects objects;
	GLPrograms programCache;
	GLLights lightCache;

	GLBufferRenderer bufferRenderer;
	GLIndexedBufferRenderer indexedBufferRenderer;

	List<Light> lights = new ArrayList<Light>();

	List<RenderItem> opaqueObjects = new ArrayList<RenderItem>();
	int opaqueObjectsLastIndex = - 1;

	List<RenderItem> transparentObjects = new ArrayList<RenderItem>();
	int transparentObjectsLastIndex = - 1;

	Float32Array morphInfluences = Float32Array.create(8);

	boolean premultipliedAlpha = true;

	// clearing
	boolean autoClear = true;
	boolean autoClearColor = true;
	boolean autoClearDepth = true;
	boolean autoClearStencil = true;

	// scene graph
	boolean sortObjects = true;

	// physically based shading
	double gammaFactor = 2.0;	// for backwards compatibility
	boolean gammaInput = false;
	boolean gammaOutput = false;

	// morphs
	int maxMorphTargets = 8;
	int maxMorphNormals = 4;

	// flags
	boolean autoScaleCubemaps = true;

	// ---- Internal properties ----------------------------

	int _currentProgram = 0; //WebGLProgram
	RenderTarget _currentRenderTarget = null;
	int _currentFramebuffer = 0; //WebGLFramebuffer
	int _currentMaterialId = -1;
	String _currentGeometryProgram = "";
	Camera _currentCamera = null;

	Vector4 _currentScissor = new Vector4();
	boolean _currentScissorTest = false;

	Vector4 _currentViewport = new Vector4();

	int _usedTextureUnits = 0;

	Color _clearColor = new Color( 0x000000 );
	double _clearAlpha = 0.;

	int _width = 0;
	int _height = 0;

	double _pixelRatio = 1.0;

	Vector4 _scissor;
	boolean _scissorTest = false;

	Vector4 _viewport;

	// frustum
	public Frustum _frustum = new Frustum();

	public Matrix4 _projScreenMatrix = new Matrix4();

	public Vector3 _vector3 = new Vector3();

	/**
	 * The constructor will create renderer for the current EGL context.
	 *
	 * @param width  the viewport width
	 * @param height the viewport height
	 */
	public GLRenderer(GL20 gl, int width, int height)
	{
		this.gl = gl;

		_scissor = new Vector4( 0, 0, _width, _height );
		_viewport = new Vector4( 0, 0, _width, _height );

		// default org.parallax3d.plugins (order is important)
		this.plugins = new ArrayList<>();

		GLExtensions.check(gl, GLES20Ext.List.OES_texture_float);
		GLExtensions.check(gl, GLES20Ext.List.OES_texture_float_linear);
		GLExtensions.check(gl, GLES20Ext.List.OES_texture_half_float);
		GLExtensions.check(gl, GLES20Ext.List.OES_texture_half_float_linear);
		GLExtensions.check(gl, GLES20Ext.List.OES_standard_derivatives);
		GLExtensions.check(gl, GLES20Ext.List.ANGLE_instanced_arrays);

		if(GLExtensions.check(gl, GLES20Ext.List.OES_element_index_uint))
		{
			BufferGeometry.MaxIndex = 4294967296L;
		}

		this.info = new GLRendererInfo();

		this.capabilities = new GLCapabilities(gl);
		this.state = new GLState(gl);
		this.properties = new GLProperties();
		this.objects = new GLObjects(gl, this.properties);
		this.programCache = new GLPrograms(this, this.capabilities);
		this.lightCache = new GLLights();

		this.bufferRenderer = new GLBufferRenderer(gl, this.info);
		this.indexedBufferRenderer = new GLIndexedBufferRenderer(gl, this.info);

		setDefaultGLState();
	}

	public double getTargetPixelRatio() {

		return _currentRenderTarget == null ? _pixelRatio : 1.;

	}

	/**
	 * Sets the sizes and also sets {@link #setViewport(int, int, int, int)} size.
	 *
	 * @param width  the canvas width.
	 * @param height the canvas height.
	 */
	public void setSize(int width, int height)
	{
		this.setViewport( 0, 0, width, height );
	}

	/**
	 * Sets the viewport to render from (X, Y) to (X + absoluteWidth, Y + absoluteHeight).
	 * By default X and Y = 0.
	 * Also fires ViewportResize event.
	 */
	public void setViewport(int x, int y, int width, int height)
	{
		state.viewport( _viewport.set( x, y, width, height ) );
	}

	/**
	 * Sets the scissor area from (x, y) to (x + absoluteWidth, y + absoluteHeight).
	 */
	public void setScissor(int x, int y, int width, int height)
	{
		state.scissor( _scissor.set( x, y, width, height ) );
	}

	/**
	 * Enable the scissor test. When this is enabled, only the pixels
	 * within the defined scissor area will be affected by further
	 * renderer actions.
	 */
	public void setScissorTest(boolean enable)
	{
		state.setScissorTest( _scissorTest = enable );
	}

	public void setDefaultGLState()
	{
		state.init();
		state.scissor(_currentScissor.copy( _scissor ).multiply( _pixelRatio ));
		state.viewport( _currentViewport.copy( _viewport ).multiply( _pixelRatio ) );

		glClearColor( _clearColor.getR(), _clearColor.getG(), _clearColor.getB(), _clearAlpha );
	}

	public void glClearColor( double r, double g, double  b, double  a )
	{
		if ( premultipliedAlpha )
			r *= a; g *= a; b *= a;

		state.clearColor( r, g, b, a );
	}

	public int getMaxAnisotropy()
	{
		if (GLExtensions.check(this.gl, GLES20Ext.List.EXT_texture_filter_anisotropic)) {
			return GLHelpers.getParameter(gl, GLES20Ext.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
		} else {
			return 0;
		}
	}

	public Shader.PRECISION getPrecision() {
		return capabilities.getPrecision();
	}

	public double getPixelRatio() {
		return this._pixelRatio;
	}

	public void setPixelRatio(double value) {
		_pixelRatio = value;

		this.setSize( (int)_viewport.getZ(), (int)_viewport.getW() );
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

	// -- Clearing

	public Color getClearColor() {
		return this._clearColor;
	}


	public void setClearColor( Color color )
	{
		setClearColor( color, 1.0 );
	}

	public void setClearColor( Color color, double alpha )
	{
		_clearColor.set( color );

		_clearAlpha = alpha;

		glClearColor( _clearColor.getR(), _clearColor.getG(), _clearColor.getB(), _clearAlpha );
	}

	public double getClearAlpha() {

		return this._clearAlpha;

	}

	public void setClearAlpha( double alpha ) {

		_clearAlpha = alpha;

		glClearColor( _clearColor.getR(), _clearColor.getG(), _clearColor.getB(), _clearAlpha );

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

	// Reset

	public void resetGLState() {

		_currentProgram = 0;
		_currentCamera = null;

		_currentGeometryProgram = "";
		_currentMaterialId = - 1;

		state.reset();

	}

	/**
	 * Gets {@link #setAutoClear(boolean)} flag.
	 */
	public boolean isAutoClear() {
		return autoClear;
	}

	/**
	 * Defines whether the renderer should automatically clear its output before rendering.
	 * Default is true.
	 *
	 * @param isAutoClear false or true
	 */
	public void setAutoClear(boolean isAutoClear) {
		this.autoClear = isAutoClear;
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
	 * Gets {@link GLRendererInfo} instance with debug information.
	 *
	 * @return the {@link GLRendererInfo} instance
	 */
	public GLRendererInfo getInfo() {
		return info;
	}

	// --- Buffer rendering

	public void renderBufferImmediate( GeometryObject object, Shader program, Material material ) {

		state.initAttributes();

		var buffers = properties.get( object );

		if ( object.hasPositions && ! buffers.position ) buffers.position = _gl.createBuffer();
		if ( object.hasNormals && ! buffers.normal ) buffers.normal = _gl.createBuffer();
		if ( object.hasUvs && ! buffers.uv ) buffers.uv = _gl.createBuffer();
		if ( object.hasColors && ! buffers.color ) buffers.color = _gl.createBuffer();

		var attributes = program.getAttributes();

		if ( object.hasPositions ) {

			_gl.bindBuffer( _gl.ARRAY_BUFFER, buffers.position );
			_gl.bufferData( _gl.ARRAY_BUFFER, object.positionArray, _gl.DYNAMIC_DRAW );

			state.enableAttribute( attributes.position );
			_gl.vertexAttribPointer( attributes.position, 3, _gl.FLOAT, false, 0, 0 );

		}

		if ( object.hasNormals ) {

			_gl.bindBuffer( _gl.ARRAY_BUFFER, buffers.normal );

			if ( material.type !== 'MeshPhongMaterial' && material.type !== 'MeshStandardMaterial' && material.shading === THREE.FlatShading ) {

				for ( var i = 0, l = object.count * 3; i < l; i += 9 ) {

					var array = object.normalArray;

					var nx = ( array[ i + 0 ] + array[ i + 3 ] + array[ i + 6 ] ) / 3;
					var ny = ( array[ i + 1 ] + array[ i + 4 ] + array[ i + 7 ] ) / 3;
					var nz = ( array[ i + 2 ] + array[ i + 5 ] + array[ i + 8 ] ) / 3;

					array[ i + 0 ] = nx;
					array[ i + 1 ] = ny;
					array[ i + 2 ] = nz;

					array[ i + 3 ] = nx;
					array[ i + 4 ] = ny;
					array[ i + 5 ] = nz;

					array[ i + 6 ] = nx;
					array[ i + 7 ] = ny;
					array[ i + 8 ] = nz;

				}

			}

			_gl.bufferData( _gl.ARRAY_BUFFER, object.normalArray, _gl.DYNAMIC_DRAW );

			state.enableAttribute( attributes.normal );

			_gl.vertexAttribPointer( attributes.normal, 3, _gl.FLOAT, false, 0, 0 );

		}

		if ( object.hasUvs && material.map ) {

			_gl.bindBuffer( _gl.ARRAY_BUFFER, buffers.uv );
			_gl.bufferData( _gl.ARRAY_BUFFER, object.uvArray, _gl.DYNAMIC_DRAW );

			state.enableAttribute( attributes.uv );

			_gl.vertexAttribPointer( attributes.uv, 2, _gl.FLOAT, false, 0, 0 );

		}

		if ( object.hasColors && material.vertexColors !== THREE.NoColors ) {

			_gl.bindBuffer( _gl.ARRAY_BUFFER, buffers.color );
			_gl.bufferData( _gl.ARRAY_BUFFER, object.colorArray, _gl.DYNAMIC_DRAW );

			state.enableAttribute( attributes.color );

			_gl.vertexAttribPointer( attributes.color, 3, _gl.FLOAT, false, 0, 0 );

		}

		state.disableUnusedAttributes();

		_gl.drawArrays( _gl.TRIANGLES, 0, object.count );

		object.count = 0;

	}

	//camera, lights, fog, material, geometry, object
	public void renderBufferDirect( Camera camera, AbstractFog fog, BufferGeometry geometry,
									Material material, GeometryObject object )
	{
		setMaterial( material );

		Shader program = setProgram(camera, fog, material, object);

//		FastMap<Integer> attributes = material.getShader().getAttributesLocations();

		boolean updateBuffers = false;
		int wireframeBit = material instanceof HasWireframe &&
				((HasWireframe)material).isWireframe() ? 1 : 0;

		String geometryProgram = geometry.getId() + "_" + program.getId() + "_" + wireframeBit;

		if (!geometryProgram.equals(_currentGeometryProgram)) {

			_currentGeometryProgram = geometryProgram;
			updateBuffers = true;

		}

		// morph targets
		List<Double> morphTargetInfluences = object instanceof Mesh ? ((Mesh)object).morphTargetInfluences : null;

		class ActiveInfluences implements Comparable<ActiveInfluences> {
			public double influence;
			public int index;

			public  ActiveInfluences(double influence, int index) {
				this.influence = influence;
				this.index = index;
			}

			@Override
			public int compareTo(ActiveInfluences o) {
				return (int) (Math.abs( o.influence ) - Math.abs( this.influence ));
			}
		}
		// render mesh
		if ( morphTargetInfluences != null ) {

			List<ActiveInfluences> activeInfluences = new ArrayList<>();

			for ( int i = 0, l = morphTargetInfluences.size(); i < l; i ++ ) {

				double influence = morphTargetInfluences.get( i );
				activeInfluences.add( new ActiveInfluences( influence, i ) );

			}

			Collections.sort(activeInfluences);

			if ( activeInfluences.size() > 8 ) {

				activeInfluences = activeInfluences.subList(0, 7);

			}

			FastMap<List<BufferAttribute>> morphAttributes = geometry.getMorphAttributes();

			for ( int i = 0, l = activeInfluences.size(); i < l; i ++ ) {

				ActiveInfluences influence = activeInfluences.get( i );
				morphInfluences.set( i, influence.influence);

				if ( influence.influence != 0 ) {

					int index = influence.index;

					if ( material instanceof HasSkinning && ((HasSkinning)material).isMorphTargets() && morphAttributes.containsKey("position") )
						geometry.addAttribute( "morphTarget" + i, morphAttributes.get("position").get( index ) );
					if ( material instanceof HasSkinning && ((HasSkinning)material).isMorphNormals() && morphAttributes.containsKey("normal") )
						geometry.addAttribute( "morphNormal" + i, morphAttributes.get("normal").get( index ) );

				} else {

					if ( material instanceof HasSkinning && ((HasSkinning)material).isMorphTargets() )
						geometry.removeAttribute( "morphTarget" + i );
					if ( material instanceof HasSkinning && ((HasSkinning)material).isMorphNormals() )
						geometry.removeAttribute( "morphNormal" + i );
				}

			}

			FastMap<Uniform> uniforms = program.getUniforms();

			if ( uniforms.get("morphTargetInfluences").getLocation() != -1 ) {

				gl.glUniform1fv( uniforms.get("morphTargetInfluences").getLocation(), morphInfluences.getLength(), morphInfluences.getTypedBuffer() );

			}

			updateBuffers = true;

		}

		AttributeData index = geometry.getIndex();
		BufferAttribute position = geometry.getAttribute("position");

		if ( material instanceof HasWireframe && ((HasWireframe)material).isWireframe() ) {

			index = objects.getWireframeAttribute( geometry );

		}

		BufferRenderer renderer;

		if(index == null)
		{
			renderer = indexedBufferRenderer;
			((GLIndexedBufferRenderer)renderer).setIndex( index );
		}
		else
		{
			renderer = bufferRenderer;
		}

		if ( updateBuffers ) {

			setupVertexAttributes( material, program, geometry );

			if ( index != null ) {

				gl.glBindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER.getValue(), objects.getAttributeBuffer( index ) );

			}

		}

		int dataStart = 0;
		int dataCount = Integer.MAX_VALUE;

		if ( index != null ) {

			dataCount = index.count;

		} else if ( position != null ) {

			dataCount = position.count;

		}

		int rangeStart = geometry.getDrawRange().start;
		int rangeCount = geometry.getDrawRange().count;

		int groupStart = group != null ? group.start : 0;
		int groupCount = group != null ? group.count : Integer.MAX_VALUE;

		int drawStart = Math.max( Math.max( dataStart, rangeStart), groupStart );
		int drawEnd = Math.min( Math.min( dataStart + dataCount, rangeStart + rangeCount), groupStart + groupCount ) - 1;

		int drawCount = Math.max( 0, drawEnd - drawStart + 1 );

		if ( object instanceof Mesh )
		{
			if ( material instanceof HasWireframe && ((HasWireframe)material).isWireframe())
			{

				state.setLineWidth( ((HasWireframe)material).getWireframeLineWidth() * getTargetPixelRatio() );
				renderer.setMode( BeginMode.LINES );

			} else {

				renderer.setMode( ((Mesh)object).getDrawMode() );

			}

		}
		else if ( object instanceof Line )
		{

			double lineWidth = material instanceof LineBasicMaterial
					? ((LineBasicMaterial)material).getLinewidth() : 1.;

			state.setLineWidth( lineWidth * getTargetPixelRatio() );

			if ( object instanceof LineSegments) {

				renderer.setMode( BeginMode.LINES );

			} else {

				renderer.setMode( BeginMode.LINE_STRIP );

			}
		}
		else if ( object instanceof Points)
		{
			renderer.setMode( BeginMode.POINTS );
		}

		if ( geometry instanceof InstancedBufferGeometry && ((InstancedBufferGeometry)geometry).getMaxInstancedCount() > 0 ) {

			renderer.renderInstances((InstancedBufferGeometry) geometry, drawStart, drawCount );

		} else {

			renderer.render( drawStart, drawCount );

		}
	}

	private void setupVertexAttributes( Material material, Shader program, BufferGeometry geometry )
	{
		setupVertexAttributes(material, program, geometry, 0);
	}

	private void setupVertexAttributes( Material material, Shader program, BufferGeometry geometry, int startIndex )
	{

		if ( geometry instanceof InstancedBufferGeometry ) {

			if ( !GLExtensions.check(gl, GLES20Ext.List.ANGLE_instanced_arrays ) ) {

				Log.error( "GLRenderer.setupVertexAttributes: using InstancedBufferGeometry but hardware does not support extension ANGLE_instanced_arrays." );
				return;

			}

		}

		state.initAttributes();

		FastMap<BufferAttribute> geometryAttributes = geometry.getAttributes();

		FastMap<Integer> programAttributes = program.getAttributesLocations();

		FastMap<Float32Array> materialDefaultAttributeValues = material.DEFAULT_ATTRIBUTE_VALUES;

		for ( String name: programAttributes.keySet()) {

			Integer programAttribute = programAttributes.get(name);

			if ( programAttribute >= 0 ) {

				BufferAttribute geometryAttribute = geometryAttributes.get( name );

				if ( geometryAttribute != null ) {

					int size = geometryAttribute.getItemSize();
					int buffer = objects.getAttributeBuffer( geometryAttribute );

					if ( geometryAttribute instanceof InterleavedBufferAttribute ) {

						InterleavedBuffer data = ((InterleavedBufferAttribute) geometryAttribute).getData();
						int stride = data.getStride();
						int offset = ((InterleavedBufferAttribute)geometryAttribute).getOffset();

						if ( data instanceof InstancedInterleavedBuffer ) {

							state.enableAttributeAndDivisor( programAttribute, ((InstancedInterleavedBuffer) data).getMeshPerAttribute(), extension );

							if ( ((InstancedBufferGeometry)geometry).getMaxInstancedCount() == null ) {

								((InstancedBufferGeometry)geometry).setMaxInstancedCount( ((InstancedInterleavedBuffer) data).getMeshPerAttribute() * data.getCount() );

							}

						} else {

							state.enableAttribute( programAttribute );

						}

						this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(),buffer);

						this.gl.glVertexAttribPointer(programAttribute, size, DataType.FLOAT.getValue(), false,
							stride * data.getArray().getBytesPerElement(),
							( startIndex * stride + offset ) * data.getArray().getBytesPerElement()); // 4 bytes per Float32

					}
					else
					{
						if ( geometryAttribute instanceof InstancedBufferAttribute ) {

							state.enableAttributeAndDivisor( programAttribute, ((InstancedBufferAttribute) geometryAttribute).getMeshPerAttribute(), extension );

							if ( ((InstancedBufferGeometry)geometry).getMaxInstancedCount() == null ) {

								((InstancedBufferGeometry)geometry).setMaxInstancedCount(
										((InstancedBufferAttribute) geometryAttribute).getMeshPerAttribute() * geometryAttribute.getCount() );

							}

						} else {

							state.enableAttribute( programAttribute );

						}

						this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(),buffer);
						this.gl.glVertexAttribPointer(programAttribute, size, DataType.FLOAT.getValue(), false,
								0, startIndex * size * 4 ); // 4 bytes per Float32

					}

				}
				else
				{
					Float32Array value = materialDefaultAttributeValues.get( name );

					if ( value != null ) {

						switch ( value.getLength() ) {

							case 2:
								gl.glVertexAttrib2fv( programAttribute, value.getTypedBuffer() );
								break;

							case 3:
								gl.glVertexAttrib3fv( programAttribute, value.getTypedBuffer() );
								break;

							case 4:
								gl.glVertexAttrib4fv( programAttribute, value.getTypedBuffer() );
								break;

							default:
								gl.glVertexAttrib1fv( programAttribute, value.getTypedBuffer() );

						}

					}

				}

			}

		}

		state.disableUnusedAttributes();
	}

	// Rendering

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
		// Render basic org.parallax3d.plugins
		if(renderPlugins( this.plugins, scene, camera, Plugin.TYPE.BASIC_RENDER ))
			return;

		AbstractFog fog = scene.getFog();

		// reset caching for this frame
		_currentGeometryProgram = "";
		_currentMaterialId = -1;
		_currentCamera = null;

		// update scene graph

		if ( scene.isAutoUpdate() )
			scene.updateMatrixWorld();

		// update camera matrices and frustum

		if ( camera.getParent() == null )
			camera.updateMatrixWorld();

		camera.getMatrixWorldInverse().getInverse( camera.getMatrixWorld() );

		_projScreenMatrix.multiply( camera.getProjectionMatrix(),
				camera.getMatrixWorldInverse() );
		_frustum.setFromMatrix( _projScreenMatrix );

		lights = new ArrayList<>();
		opaqueObjectsLastIndex = - 1;
		transparentObjectsLastIndex = - 1;

		projectObject( scene, camera );

		opaqueObjects = new ArrayList<>();
		transparentObjects = new ArrayList<>();

		if ( this.isSortObjects() ) {

			Collections.sort(opaqueObjects, new RenderItem.PainterSortStable());

			Collections.sort(transparentObjects, new RenderItem.ReversePainterSortStable());

		}

		setupLights( lights, camera );

		// custom render org.parallax3d.plugins (pre pass)
		renderPlugins( this.plugins, scene, camera, Plugin.TYPE.PRE_RENDER );

		info.getRender().calls = 0;
		info.getRender().vertices = 0;
		info.getRender().faces = 0;
		info.getRender().points = 0;

		setRenderTarget( renderTarget );

		if ( isAutoClear() || forceClear )
		{
			clear( this.isAutoClearColor(), this.isAutoClearDepth(), this.isAutoClearStencil() );
		}

		if ( scene.getOverrideMaterial() != null )
		{
			Material overrideMaterial = scene.getOverrideMaterial();

			renderObjects( opaqueObjects, camera, fog, overrideMaterial );
			renderObjects( transparentObjects, camera, fog, overrideMaterial );
		}
		else
		{
			// opaque pass (front-to-back order)
			state.setBlending(Material.BLENDING.NO);
			renderObjects( opaqueObjects, camera, fog );

			// transparent pass (back-to-front order)
			renderObjects( transparentObjects, camera, fog );
		}

		// custom render org.parallax3d.plugins (post pass)
		renderPlugins( this.plugins, scene, camera, Plugin.TYPE.POST_RENDER );

		// Generate mipmap if we're using any kind of mipmap filtering
		if ( renderTarget != null && renderTarget.isGenerateMipmaps()
				&& renderTarget.getMinFilter() != TextureMinFilter.NEAREST
				&& renderTarget.getMinFilter() != TextureMinFilter.LINEAR)
		{
			updateRenderTargetMipmap( renderTarget );
		}

		// Ensure depth buffer writing is enabled so it can be cleared on next render

		state.setDepthTest( true );
		state.setDepthWrite( true );
		state.setColorWrite( true );

	}

	private void pushRenderItem( Object3D object, AbstractGeometry geometry, Material material, double z, Object group ) {

		List<RenderItem> array;
		int index = 0;

		// allocate the next position in the appropriate array

		if ( material.isTransparent() ) {

			array = transparentObjects;
			index = ++ transparentObjectsLastIndex;

		} else {

			array = opaqueObjects;
			index = ++ opaqueObjectsLastIndex;

		}

		// recycle existing render item or grow the array

		RenderItem renderItem = array.get( index );

		if ( renderItem != null )
		{
			renderItem.id = object.getId();
			renderItem.object = object;
			renderItem.geometry = geometry;
			renderItem.material = material;
			renderItem.z = _vector3.getZ();
			renderItem.group = group;
		}
		else
		{
			array.add( new RenderItem(object.getId(), object, geometry, material,_vector3.getZ(), group) );
		}

	}

	private void projectObject( Object3D object, Camera camera ) {

		if ( !object.isVisible() ) return;

		if ( object.getLayers().test( camera.getLayers() ) )
		{

			if ( object instanceof Light ) {

				lights.add((Light) object);

			} else if ( object instanceof Sprite ) {

				if ( !object.isFrustumCulled() || _frustum.isIntersectsObject((GeometryObject) object) ) {

					sprites.push( object );

				}

//			} else if ( object instanceof LensFlare ) {
//
//				lensFlares.push( object );

			} else if ( object instanceof ImmediateRenderObject) {

				if ( sortObjects ) {

					_vector3.setFromMatrixPosition( object.getMatrixWorld() );
					_vector3.applyProjection( _projScreenMatrix );

				}

				pushRenderItem( object, null, ((ImmediateRenderObject) object).getMaterial(), _vector3.getZ(), null );

			} else if ( object instanceof Mesh || object instanceof Line || object instanceof Points ) {

				if ( object instanceof SkinnedMesh ) {

					((SkinnedMesh) object).getSkeleton().update();

				}

				if ( object.frustumCulled == false || _frustum.isIntersectsObject((GeometryObject) object) ) {

					Material material = object.material;

					if ( material.isVisible() ) {

						if ( sortObjects ) {

							_vector3.setFromMatrixPosition( object.getMatrixWorld() );
							_vector3.applyProjection( _projScreenMatrix );

						}

						AbstractGeometry geometry = objects.update( object );

						if ( material instanceof MultiMaterial) {

							var groups = geometry.groups;
							var materials = material.materials;

							for ( int i = 0, l = groups.length; i < l; i ++ ) {

								var group = groups[ i ];
								Material groupMaterial = materials[ group.materialIndex ];

								if ( groupMaterial.isVisible() ) {

									pushRenderItem( object, geometry, groupMaterial, _vector3.getZ(), group );

								}

							}

						} else {

							pushRenderItem( object, geometry, material, _vector3.getZ(), null );

						}

					}

				}

			}

		}

		List<Object3D> children = object.getChildren();

		for ( int i = 0, l = children.size(); i < l; i ++ ) {

			projectObject( children.get( i ), camera );

		}
	}

	private void renderObjects (List<RenderItem> renderList, Camera camera, AbstractFog fog )
	{
		renderObjects ( renderList, camera, fog, null);
	}

	//renderList, camera, lights, fog, useBlending, overrideMaterial
	private void renderObjects (List<RenderItem> renderList, Camera camera, AbstractFog fog, Material overrideMaterial )
	{
		for ( int i = 0, l = renderList.size(); i < l; i ++ ) {

			RenderItem renderItem = renderList.get( i );

			Object3D object = renderItem.object;
			AbstractGeometry geometry = renderItem.geometry;
			Material material = overrideMaterial == null ? renderItem.material : overrideMaterial;
			Object group = renderItem.group;

			object.getModelViewMatrix().multiply( camera.getMatrixWorldInverse(), object.getMatrixWorld() );
			object.getNormalMatrix().getNormalMatrix( object.getModelViewMatrix() );

			if ( object instanceof ImmediateRenderObject ) {

				setMaterial( material );

				Shader program = setProgram( camera, fog, material, object );

				_currentGeometryProgram = "";

				object.render( function ( object ) {

					renderBufferImmediate( object, program, material );

				} );

			} else {

				renderBufferDirect( camera, fog, geometry, material, object, group );

			}

		}
	}

	private void initMaterial ( Material material, AbstractFog fog, GeometryObject object )
	{
		Log.debug("WebGlRender: Called initMaterial for material: " + material.getClass().getName() + " and object " + object.getClass().getName());

		FastMap<Object> materialProperties = properties.get( material );

		var parameters = programCache.getParameters( material, _lights, fog, object );
		var code = programCache.getProgramCode( material, parameters );

		var program = materialProperties.get("program");
		boolean programChange = true;

		if ( program == null ) {

			// new material
			material.deallocate(this);

		} else if ( program.code != code ) {

			// changed glsl or parameters
			releaseMaterialProgramReference( material );

		} else if ( parameters.shaderID != undefined ) {

			// same glsl and uniform list
			return;

		} else {

			// only rebuild uniform list
			programChange = false;

		}

		if ( programChange ) {

			if ( parameters.get("shaderID") ) {

				var shader = THREE.ShaderLib[ parameters.shaderID ];

				materialProperties.__webglShader = {
						name: material.type,
						uniforms: THREE.UniformsUtils.clone( shader.uniforms ),
						vertexShader: shader.vertexShader,
						fragmentShader: shader.fragmentShader
				};

			} else {

				materialProperties.__webglShader = {
						name: material.type,
						uniforms: material.uniforms,
						vertexShader: material.vertexShader,
						fragmentShader: material.fragmentShader
				};

			}

			material.__webglShader = materialProperties.__webglShader;

			program = programCache.acquireProgram( material, parameters, code );

			materialProperties.program = program;
			material.program = program;

		}

		FastMap<Integer> attributes = program.getAttributes();
//		FastMap<Integer> attributes = material.getShader().getAttributesLocations();

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

		materialProperties.uniformsList = [];

		var uniforms = materialProperties.__webglShader.uniforms,
				uniformLocations = materialProperties.program.getUniforms();

		for ( var u in uniforms ) {

			var location = uniformLocations[ u ];

			if ( location ) {

				materialProperties.uniformsList.push( [ materialProperties.__webglShader.uniforms[ u ], location ] );

			}

		}

		if ( material instanceof THREE.MeshPhongMaterial ||
				material instanceof THREE.MeshLambertMaterial ||
				material instanceof THREE.MeshStandardMaterial ||
				material.lights ) {

			// store the light setup it was created for

			materialProperties.lightsHash = _lights.hash;

			// wire up the material to this renderer's lighting state

			uniforms.ambientLightColor.value = _lights.ambient;
			uniforms.directionalLights.value = _lights.directional;
			uniforms.spotLights.value = _lights.spot;
			uniforms.pointLights.value = _lights.point;
			uniforms.hemisphereLights.value = _lights.hemi;

			uniforms.directionalShadowMap.value = _lights.directionalShadowMap;
			uniforms.directionalShadowMatrix.value = _lights.directionalShadowMatrix;
			uniforms.spotShadowMap.value = _lights.spotShadowMap;
			uniforms.spotShadowMatrix.value = _lights.spotShadowMatrix;
			uniforms.pointShadowMap.value = _lights.pointShadowMap;
			uniforms.pointShadowMatrix.value = _lights.pointShadowMatrix;

		}

	}

	private void setMaterial( Material material )
	{
		setMaterialFaces( material );

		if ( material.isTransparent())
		{
			state.setBlending( material.getBlending(), material.getBlendEquation(), material.getBlendSrc(), material.getBlendDst(),
					material.getBlendEquationAlpha(), material.getBlendSrcAlpha(), material.getBlendDstAlpha() );
		}
		else
		{
			state.setBlending( Material.BLENDING.NO );
		}

		state.setDepthFunc( material.getDepthFunc() );
		state.setDepthTest( material.isDepthTest() );
		state.setDepthWrite( material.isDepthWrite() );
		state.setColorWrite( material.isColorWrite() );
		state.setPolygonOffset( material.isPolygonOffset(), material.getPolygonOffsetFactor(), material.getPolygonOffsetUnits() );

	}

	private void setMaterialFaces( Material material )
	{
		if(material.getSides() != Material.SIDE.DOUBLE)
			state.enable( EnableCap.CULL_FACE );
		else
			state.disable( EnableCap.CULL_FACE );

		state.setFlipSided( material.getSides() == Material.SIDE.BACK );
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

		if ( object.morphTargetBase != - 1 && attributes.get("position") >= 0)
		{
			this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphTargetsBuffers.get(object.morphTargetBase));
			enableAttribute( attributes.get("position") );
			this.gl.glVertexAttribPointer(attributes.get("position"), 3, DataType.FLOAT.getValue(), false, 0, 0);

		}
		else if ( attributes.get("position") >= 0 )
		{
			this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometrybuffer.__webglVertexBuffer);
			enableAttribute( attributes.get("position") );
			this.gl.glVertexAttribPointer(attributes.get("position"), 3, DataType.FLOAT.getValue(), false, 0, 0);
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
					this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphTargetsBuffers.get(order.get(m)));
					enableAttribute( attributes.get("morphTarget" + m ) );
					this.gl.glVertexAttribPointer(attributes.get("morphTarget" + m), 3, DataType.FLOAT.getValue(), false, 0, 0);

				}

				if (  attributes.get("morphNormal" + m )  >= 0 &&
						material instanceof HasSkinning && ((HasSkinning)material).isMorphNormals())
				{
					this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphNormalsBuffers.get(order.get(m)));
					enableAttribute( attributes.get("morphNormal" + m ));
					this.gl.glVertexAttribPointer(attributes.get("morphNormal" + m), 3, DataType.FLOAT.getValue(), false, 0, 0);
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

						this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphTargetsBuffers.get(influenceIndex));
						enableAttribute( attributes.get( "morphTarget" + m ) );
						this.gl.glVertexAttribPointer(attributes.get("morphTarget" + m), 3, DataType.FLOAT.getValue(), false, 0, 0);

					}

					if ( attributes.get( "morphNormal" + m ) >= 0 &&
							((HasSkinning)material).isMorphNormals() ) {

						this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometrybuffer.__webglMorphNormalsBuffers.get(influenceIndex));
						enableAttribute( attributes.get( "morphNormal" + m ) );
						this.gl.glVertexAttribPointer(attributes.get("morphNormal" + m), 3, DataType.FLOAT.getValue(), false, 0, 0);

					}

					object.__webglMorphTargetInfluences.set( m, influences.get( influenceIndex ));

				} else {

					/*
					_gl.vertexAttribPointer( attributes[ "morphTarget" + m ], 3,
					_gl.FLOAT, false, 0, 0 );

					if ( material.morphNormals ) {

						_gl.vertexAttribPointer( attributes[ "morphNormal" + m ], 3,
						_gl.FLOAT, false, 0, 0 );

					}
					*/

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

	public List<GeometryGroup> makeGroups( Geometry geometry, boolean usesFaceMaterial ) {

		long maxVerticesInGroup = GLExtensions.check(this.gl, GLES20Ext.List.OES_element_index_uint) ? 4294967296L : 65535L;

		int numMorphTargets = geometry.getMorphTargets().size();
		int numMorphNormals = geometry.getMorphNormals().size();

		String groupHash;

		FastMap<Integer> hash_map = new FastMap<Integer>();

		FastMap<GeometryGroup> groups = new FastMap<GeometryGroup>();

		List<GeometryGroup> groupsList = new ArrayList<GeometryGroup>();

		for ( int f = 0, fl = geometry.getFaces().size(); f < fl; f ++ ) {

			Face3 face = geometry.getFaces().get(f);
			Integer materialIndex = usesFaceMaterial ? face.getMaterialIndex() : 0;

			if ( ! hash_map.containsKey(materialIndex) ) {

				hash_map.put(materialIndex.toString(), 0);

			}

			groupHash = materialIndex + "_" + hash_map.get( materialIndex );

			if ( ! groups.containsKey(groupHash) ) {

				GeometryGroup group = new GeometryGroup(materialIndex,
						numMorphTargets, numMorphNormals);

				groups.put(groupHash, group);
				groupsList.add( group );

			}

			if ( groups.get( groupHash ).getVertices() + 3 > maxVerticesInGroup ) {

				hash_map.put( materialIndex.toString(), hash_map.get( materialIndex ) + 1 );
				groupHash = materialIndex + "_" + hash_map.get( materialIndex );

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
		boolean addBuffers = false;

		if ( GeometryGroup.geometryGroups.get( geometry.getId() + "" ) == null ||
				geometry.isGroupsNeedUpdate() ) {

			this._webglObjects.put(object.getId() + "", new ArrayList<RenderItem>());

			GeometryGroup.geometryGroups.put( geometry.getId() + "",
					makeGroups( geometry, material instanceof MeshFaceMaterial ));

			geometry.setGroupsNeedUpdate( false );

		}

		List<GeometryGroup> geometryGroupsList =
				GeometryGroup.geometryGroups.get( geometry.getId() + "" );

		// create separate VBOs per geometry chunk
		for ( int i = 0, il = geometryGroupsList.size(); i < il; i ++ ) {

			GeometryGroup geometryGroup = geometryGroupsList.get( i );

			// initialise VBO on the first access
			if ( geometryGroup.__webglVertexBuffer == 0 )
			{
				((Mesh)object).createBuffers(this, geometryGroup);
				((Mesh)object).initBuffers(this.gl, geometryGroup);
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
//			geometry.addEventListener( 'dispose', onGeometryDispose );

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

			} else if ( object instanceof Points) {

				if ( geometry.__webglVertexBuffer == 0 ) {

					((Points)object).createBuffers(this);
					((Points)object).initBuffers(this.gl);

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

					List<GeometryGroup> geometryGroupsList =
							GeometryGroup.geometryGroups.get( geometry.getId() + "" );

					for ( int i = 0,l = geometryGroupsList.size(); i < l; i ++ ) {

						addBuffer( geometryGroupsList.get( i ), (GeometryObject) object );

					}

				}

			} else if ( object instanceof Line || object instanceof Points) {

				addBuffer( geometry, (GeometryObject) object );

			} /*else if ( object instanceof ImmediateRenderObject ||
                    object.immediateRenderCallback ) {

				addBufferImmediate( _webglObjectsImmediate, object );

			}*/

		}

	}

	private void addBuffer( GLGeometry buffer, GeometryObject object ) {

		int id = object.getId();
		List<RenderItem> list = _webglObjects.get(id + "");
		if(list == null) {
			list = new ArrayList<RenderItem>();
			_webglObjects.put(id + "", list);
		}

		RenderItem webGLObject = new RenderItem(buffer, object);
		webGLObject.id = id;
		list.add(webGLObject);
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
					GeometryGroup.geometryGroups.get( geometry.getId() + "" );

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


		} else if ( object instanceof Points) {

			material = getBufferMaterial( object, (Geometry)geometry );

			boolean customAttributesDirty = (material instanceof ShaderMaterial) &&
					material.getShader().areCustomAttributesDirty();

			if ( geometry.isVerticesNeedUpdate() || geometry.isColorsNeedUpdate() ||
					((Points)object).isSortParticles() || customAttributesDirty ) {

				((Points)object).setBuffers( this, BufferUsage.DYNAMIC_DRAW );

			}

			geometry.setVerticesNeedUpdate( false );
			geometry.setColorsNeedUpdate( false );

			if(material instanceof ShaderMaterial ) {
				material.getShader().clearCustomAttributes();
			}

		}

	}

	public void renderObjectsImmediate ( List<RenderItem> renderList,
										 Boolean isTransparentMaterial, Camera camera,
										 List<Light> lights, AbstractFog fog,
										 boolean useBlending, Material overrideMaterial ) {

		Material material = null;

		for ( int i = 0, il = renderList.size(); i < il; i ++ ) {

			RenderItem webglObject = renderList.get( i );
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
			//Log.debug("Called renderPlugins(): " + plugin.getClass().getName());

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

		Shader program = setProgram( camera, lights, fog, material, object );

		FastMap<Integer> attributes = material.getShader().getAttributesLocations();

		boolean updateBuffers = false;
		int wireframeBit = material instanceof HasWireframe &&
				((HasWireframe)material).isWireframe() ? 1 : 0;

		int geometryGroupHash = ( geometry.getId() * 0xffffff ) +
				(material.getShader().getId() * 2 ) + wireframeBit;

//		Log.e(TAG,"--- renderBuffer() geometryGroupHash=" + geometryGroupHash
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
		if ( !(material instanceof HasSkinning && ((HasSkinning)material).isMorphTargets()) &&
				attributes.get("position") >= 0 )
		{
			if ( updateBuffers )
			{
				this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglVertexBuffer);
				enableAttribute( attributes.get("position") );
				this.gl.glVertexAttribPointer(attributes.get("position"), 3, DataType.FLOAT.getValue(), false, 0, 0);
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
			if ( attributes.get("color") >= 0 )
			{
				if ( ((Geometry)object.getGeometry()).getColors().size() > 0 ||
						((Geometry)object.getGeometry()).getFaces().size() > 0 ) {

					this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglColorBuffer);
					enableAttribute( attributes.get("color") );
					this.gl.glVertexAttribPointer(attributes.get("color"), 3, DataType.FLOAT.getValue(), false, 0, 0);

				} else {

					this.gl.glVertexAttrib3f(attributes.get("color"), 1.0f, 1.0f, 1.0f);

				}
			}

			// normals
			if ( attributes.get("normal") >= 0 )
			{
				this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglNormalBuffer);
				enableAttribute( attributes.get("normal") );
				this.gl.glVertexAttribPointer(attributes.get("normal"), 3, DataType.FLOAT.getValue(), false, 0, 0);
			}

			// tangents
			if ( attributes.get("tangent") >= 0 )
			{
				this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglTangentBuffer);
				enableAttribute( attributes.get("tangent") );
				this.gl.glVertexAttribPointer(attributes.get("tangent"), 4, DataType.FLOAT.getValue(), false, 0, 0);
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
					attributes.get("skinIndex") >= 0 && attributes.get("skinWeight") >= 0 )
			{
				this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglSkinIndicesBuffer);
				enableAttribute( attributes.get("skinIndex") );
				this.gl.glVertexAttribPointer(attributes.get("skinIndex"), 4, DataType.FLOAT.getValue(), false, 0, 0);

				this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglSkinWeightsBuffer);
				enableAttribute( attributes.get("skinWeight") );
				this.gl.glVertexAttribPointer(attributes.get("skinWeight"), 4, DataType.FLOAT.getValue(), false, 0, 0);
			}

			// line distances

			if ( attributes.get("lineDistance") != null && attributes.get("lineDistance") >= 0 ) {

				this.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglLineDistanceBuffer);
				enableAttribute( attributes.get("lineDistance") );
				this.gl.glVertexAttribPointer(attributes.get("lineDistance"), 1, DataType.FLOAT.getValue(), false, 0, 0);

			}

		}

		disableUnusedAttributes();

		//Log.debug("  ----> renderBuffer() ID " + object.getId() + " (" +
		//        object.getClass().getSimpleName() + ")");

		// Render object's buffers
		object.renderBuffer(this, geometry, updateBuffers);
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
		int program = shader.getProgram();
		FastMap<Uniform> m_uniforms = shader.getUniforms();

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
			if(_currentMaterialId == -1) refreshLights = true;

			this._currentMaterialId = material.getId();
			refreshMaterial = true;
		}

		if ( refreshProgram || !camera.equals( this._currentCamera) )
		{
			this.gl.glUniformMatrix4fv(m_uniforms.get("projectionMatrix").getLocation(), 1, false, camera.getProjectionMatrix().getArray().getTypedBuffer());

			if ( _logarithmicDepthBuffer ) {

				this.gl.glUniform1f(m_uniforms.get("logDepthBufFC").getLocation(), (float) (2.0 / (Math.log(((HasNearFar) camera).getFar() + 1.0) / 0.6931471805599453 /*Math.LN2*/)));

			}

			if ( !camera.equals( this._currentCamera) )
				this._currentCamera = camera;

			// load material specific uniforms
			// (shader material also gets them for the sake of genericity)
			if ( material.getClass() == ShaderMaterial.class ||
					material.getClass() == MeshPhongMaterial.class ||
					material instanceof HasEnvMap && ((HasEnvMap)material).getEnvMap() != null
					) {

				if ( m_uniforms.get("cameraPosition").getLocation() != -1 )
				{
					_vector3.setFromMatrixPosition( camera.getMatrixWorld() );
					this.gl.glUniform3f(m_uniforms.get("cameraPosition").getLocation(), (float)_vector3.getX(), (float)_vector3.getY(), (float)_vector3.getZ());
				}
			}

			if ( material.getClass() == MeshPhongMaterial.class ||
					material.getClass() == MeshLambertMaterial.class ||
					material.getClass() == MeshBasicMaterial.class ||
					material.getClass() == ShaderMaterial.class ||
					material instanceof HasSkinning && ((HasSkinning)material).isSkinning()
					) {

				if ( m_uniforms.get("viewMatrix").getLocation() != -1 )
				{
					this.gl.glUniformMatrix4fv(m_uniforms.get("viewMatrix").getLocation(), 1, false, camera.getMatrixWorldInverse().getArray().getTypedBuffer());
				}
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
				if ( m_uniforms.get("boneTexture").getLocation() != -1 )
				{
					int textureUnit = getTextureUnit();

					this.gl.glUniform1i(m_uniforms.get("boneTexture").getLocation(), textureUnit);
					setTexture( ((SkinnedMesh)object).boneTexture, textureUnit );
				}
			}
			else
			{
				if ( m_uniforms.get("boneGlobalMatrices").getLocation() != -1 )
				{
					this.gl.glUniformMatrix4fv(m_uniforms.get("boneGlobalMatrices").getLocation(), 1, false, ((SkinnedMesh) object).boneMatrices.getTypedBuffer());
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

		if ( m_uniforms.get("modelMatrix").getLocation() != 0 )
			this.gl.glUniformMatrix4fv(m_uniforms.get("modelMatrix").getLocation(), 1, false, object.getMatrixWorld().getArray().getTypedBuffer());

		return shader;
	}

	private void refreshUniformsShadow( FastMap<Uniform> uniforms, List<Light> lights )
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
		GeometryObject objectImpl = (GeometryObject) object;
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

			if ( location == -1 ) continue;

			Object value = uniform.getValue();
			Uniform.TYPE type = uniform.getType();
			// Up textures also for undefined values
			if ( type != Uniform.TYPE.T && value == null ) continue;

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
			Log.debug("WebGlRenderer: Trying to use " + textureUnit + " texture units while this GPU supports only " + this._maxTextures);
		}

		return textureUnit;
	}

	private void setupMatrices ( Object3D object, Camera camera )
	{
		object._modelViewMatrix.multiply( camera.getMatrixWorldInverse(), object.getMatrixWorld() );
		object._normalMatrix.getNormalMatrix( object._modelViewMatrix );
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

            /*
			GLES20.glPixelStorei( PixelStoreParameter.UNPACK_FLIP_Y_WEBGL, texture.isFlipY() ? 1 : 0 );
			GLES20.glPixelStorei( PixelStoreParameter.UNPACK_PREMULTIPLY_ALPHA_WEBGL,
                    texture.isPremultiplyAlpha() ? 1 : 0 );
            */
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
			//GLES20.glPixelStorei( PixelStoreParameter.UNPACK_FLIP_Y_WEBGL,
			// texture.isFlipY() ? 1 : 0 );

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
//		App.app.debug("WebGlRenderer", "  ----> Called setRenderTarget(params)");
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
			int nVertexMatrices = (int) Math.floor( ( nVertexUniforms - 20 ) / 4 );

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

			if ( light instanceof DirectionalLight ) dirLights ++;
			if ( light instanceof PointLight ) pointLights ++;
			if ( light instanceof SpotLight ) spotLights ++;
			if ( light instanceof HemisphereLight ) hemiLights ++;
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
