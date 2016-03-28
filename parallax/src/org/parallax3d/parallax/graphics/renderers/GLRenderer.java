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

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.cameras.HasNearFar;
import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.graphics.extras.objects.ImmediateRenderObject;
import org.parallax3d.parallax.graphics.lights.*;
import org.parallax3d.parallax.graphics.materials.*;
import org.parallax3d.parallax.graphics.objects.*;
import org.parallax3d.parallax.graphics.renderers.gl.*;
import org.parallax3d.parallax.graphics.renderers.shaders.Attribute;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.scenes.AbstractFog;
import org.parallax3d.parallax.graphics.scenes.Fog;
import org.parallax3d.parallax.graphics.scenes.FogExp2;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.*;
import org.parallax3d.parallax.math.*;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.ViewportResizeBus;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.GLES20Ext;
import org.parallax3d.parallax.system.gl.GLHelpers;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Int32Array;
import org.parallax3d.parallax.system.gl.arrays.TypeArray;
import org.parallax3d.parallax.system.gl.enums.*;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
	GLRenderTarget _currentRenderTarget = null;
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

	public GL20 gl() {
		return gl;
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

	public void setClearColor( int color )
	{
		setClearColor( new Color(color), 1.0 );
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
	 * Clear {@link GLRenderTarget} and GL buffers.
	 */
	public void clearTarget( GLRenderTarget renderTarget,
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

		if ( object.hasPositions && ! buffers.position ) buffers.position = gl.createBuffer();
		if ( object.hasNormals && ! buffers.normal ) buffers.normal = gl.createBuffer();
		if ( object.hasUvs && ! buffers.uv ) buffers.uv = gl.createBuffer();
		if ( object.hasColors && ! buffers.color ) buffers.color = gl.createBuffer();

		var attributes = program.getAttributes();

		if ( object.hasPositions ) {

			gl.bindBuffer( gl.ARRAY_BUFFER, buffers.position );
			gl.bufferData( gl.ARRAY_BUFFER, object.positionArray, gl.DYNAMIC_DRAW );

			state.enableAttribute( attributes.position );
			gl.vertexAttribPointer( attributes.position, 3, gl.FLOAT, false, 0, 0 );

		}

		if ( object.hasNormals ) {

			gl.bindBuffer( gl.ARRAY_BUFFER, buffers.normal );

			if ( material.type !== 'MeshPhongMaterial' && material.type !== 'MeshStandardMaterial' && material.shading == THREE.FlatShading ) {

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

			gl.bufferData( gl.ARRAY_BUFFER, object.normalArray, gl.DYNAMIC_DRAW );

			state.enableAttribute( attributes.normal );

			gl.vertexAttribPointer( attributes.normal, 3, gl.FLOAT, false, 0, 0 );

		}

		if ( object.hasUvs && material.map ) {

			gl.bindBuffer( gl.ARRAY_BUFFER, buffers.uv );
			gl.bufferData( gl.ARRAY_BUFFER, object.uvArray, gl.DYNAMIC_DRAW );

			state.enableAttribute( attributes.uv );

			gl.vertexAttribPointer( attributes.uv, 2, gl.FLOAT, false, 0, 0 );

		}

		if ( object.hasColors && material.vertexColors !== THREE.NoColors ) {

			gl.bindBuffer( gl.ARRAY_BUFFER, buffers.color );
			gl.bufferData( gl.ARRAY_BUFFER, object.colorArray, gl.DYNAMIC_DRAW );

			state.enableAttribute( attributes.color );

			gl.vertexAttribPointer( attributes.color, 3, gl.FLOAT, false, 0, 0 );

		}

		state.disableUnusedAttributes();

		gl.drawArrays( gl.TRIANGLES, 0, object.count );

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

	public void render( Scene scene, Camera camera, GLRenderTarget renderTarget )
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
	public void render(Scene scene, Camera camera, GLRenderTarget renderTarget, boolean forceClear )
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

	private Shader setProgram( Camera camera, AbstractFog fog, Material material, GeometryObject object )
	{
		// Use new material units for new shader
		this._usedTextureUnits = 0;

		FastMap<Object> materialProperties = properties.get( material );

		if ( materialProperties.get("program") == null ) {

			material.setNeedsUpdate( true );

		}

		if ( materialProperties.get("lightsHash") != null &&
				materialProperties.get("lightsHash") != _lights.hash ) {

			material.setNeedsUpdate( true );

		}

		if ( material.isNeedsUpdate() ) {

			initMaterial( material, fog, object );
			material.setNeedsUpdate( false );

		}

		boolean refreshProgram = false;
		boolean refreshMaterial = false;
		boolean refreshLights = false;

		program = materialProperties.program;
		FastMap<Uniform> p_uniforms = program.getUniforms();
		FastMap<Uniform> m_uniforms = materialProperties.get("__webglShader").uniforms;

		if ( program.id != _currentProgram ) {

			gl.glUseProgram( program.program );
			_currentProgram = program.id;

			refreshProgram = true;
			refreshMaterial = true;
			refreshLights = true;

		}

		if ( material.getId() != _currentMaterialId ) {

			_currentMaterialId = material.getId();

			refreshMaterial = true;

		}

		if ( refreshProgram || !camera.equals( this._currentCamera) )
		{
			this.gl.glUniformMatrix4fv(p_uniforms.get("projectionMatrix"), 1, false, camera.getProjectionMatrix().getArray().getTypedBuffer());

			if ( capabilities.isLogarithmicDepthBuffer() ) {

				this.gl.glUniform1f(p_uniforms.get("logDepthBufFC"), (float) (2.0 / (Math.log(((HasNearFar) camera).getFar() + 1.0) / Mathematics.LN2)));

			}

			if ( !camera.equals( this._currentCamera) )
			{
				_currentCamera = camera;

				// lighting uniforms depend on the camera so enforce an update
				// now, in case this material supports lights - or later, when
				// the next material that does gets activated:

				refreshMaterial = true;		// set to true on material change
				refreshLights = true;		// remains set until update done
			}

			// load material specific uniforms
			// (shader material also gets them for the sake of genericity)
			if ( material instanceof ShaderMaterial ||
					material instanceof MeshPhongMaterial ||
					material instanceof MeshStandardMaterial ||
					material instanceof HasEnvMap && ((HasEnvMap)material).getEnvMap() != null
					) {

				if ( p_uniforms.get("cameraPosition") != -1 )
				{
					_vector3.setFromMatrixPosition( camera.getMatrixWorld() );
					this.gl.glUniform3f(p_uniforms.get("cameraPosition"), (float)_vector3.getX(), (float)_vector3.getY(), (float)_vector3.getZ());
				}
			}

			if ( material instanceof MeshPhongMaterial ||
					material instanceof MeshLambertMaterial ||
					material instanceof MeshBasicMaterial ||
					material instanceof MeshStandardMaterial ||
					material instanceof ShaderMaterial ||
					material instanceof HasSkinning && ((HasSkinning)material).isSkinning()
					) {

				if ( p_uniforms.get("viewMatrix") != -1 )
				{
					this.gl.glUniformMatrix4fv(p_uniforms.get("viewMatrix"), 1, false, camera.getMatrixWorldInverse().getArray().getTypedBuffer());
				}
			}
		}

		// skinning uniforms must be set even if material didn't change
		// auto-setting of texture unit for bone texture must go before other textures
		// not sure why, but otherwise weird things happen
		if ( material instanceof HasSkinning && ((HasSkinning)material).isSkinning() )
		{
			if ( object.bindMatrix && p_uniforms.bindMatrix !== undefined ) {

				gl.uniformMatrix4fv( p_uniforms.bindMatrix, false, object.bindMatrix.elements );

			}

			if ( object.bindMatrixInverse && p_uniforms.bindMatrixInverse !== undefined ) {

				gl.uniformMatrix4fv( p_uniforms.bindMatrixInverse, false, object.bindMatrixInverse.elements );

			}

			if ( capabilities.floatVertexTextures && object.skeleton && object.skeleton.useVertexTexture ) {

				if ( p_uniforms.boneTexture !== undefined ) {

					var textureUnit = getTextureUnit();

					gl.uniform1i( p_uniforms.boneTexture, textureUnit );
					_this.setTexture( object.skeleton.boneTexture, textureUnit );

				}

				if ( p_uniforms.boneTextureWidth !== undefined ) {

					gl.uniform1i( p_uniforms.boneTextureWidth, object.skeleton.boneTextureWidth );

				}

				if ( p_uniforms.boneTextureHeight !== undefined ) {

					gl.uniform1i( p_uniforms.boneTextureHeight, object.skeleton.boneTextureHeight );

				}

			} else if ( object.skeleton && object.skeleton.boneMatrices ) {

				if ( p_uniforms.boneGlobalMatrices !== undefined ) {

					gl.uniformMatrix4fv( p_uniforms.boneGlobalMatrices, false, object.skeleton.boneMatrices );

				}

			}
		}

		if ( refreshMaterial )
		{
			if ( material instanceof MeshPhongMaterial ||
					material instanceof MeshLambertMaterial ||
					material instanceof MeshStandardMaterial ||
					material.lights ) {

				// the current material requires lighting info

				// note: all lighting uniforms are always set correctly
				// they simply reference the renderer's state for their
				// values
				//
				// use the current material's .needsUpdate flags to set
				// the GL state when required

				markUniformsLightsNeedsUpdate( m_uniforms, refreshLights );

			}

			// refresh uniforms common to several materials

			if ( fog && material.fog ) {

				refreshUniformsFog( m_uniforms, fog );

			}

			if ( material instanceof MeshBasicMaterial ||
					material instanceof MeshLambertMaterial ||
					material instanceof MeshPhongMaterial ||
					material instanceof MeshStandardMaterial ) {

				refreshUniformsCommon( m_uniforms, material );

			}

			// refresh single material specific uniforms

			if ( material instanceof LineBasicMaterial ) {

				refreshUniformsLine( m_uniforms, material );

			} else if ( material instanceof LineDashedMaterial ) {

				refreshUniformsLine( m_uniforms, material );
				refreshUniformsDash( m_uniforms, material );

			} else if ( material instanceof PointsMaterial ) {

				refreshUniformsPoints( m_uniforms, material );

			} else if ( material instanceof MeshLambertMaterial ) {

				refreshUniformsLambert( m_uniforms, material );

			} else if ( material instanceof MeshPhongMaterial ) {

				refreshUniformsPhong( m_uniforms, material );

			} else if ( material instanceof MeshStandardMaterial ) {

				refreshUniformsStandard( m_uniforms, material );

			} else if ( material instanceof MeshDepthMaterial ) {

				m_uniforms.mNear.value = camera.near;
				m_uniforms.mFar.value = camera.far;
				m_uniforms.opacity.value = material.opacity;

			} else if ( material instanceof MeshNormalMaterial ) {

				m_uniforms.opacity.value = material.opacity;

			}

			// load common uniforms

			loadUniformsGeneric( materialProperties.uniformsList );

		}

		loadUniformsMatrices( p_uniforms, object );

		if ( p_uniforms.modelMatrix != null ) {

			gl.uniformMatrix4fv( p_uniforms.modelMatrix, false, object.matrixWorld.elements );

		}

		if ( materialProperties.hasDynamicUniforms == true ) {

			updateDynamicUniforms( materialProperties.uniformsList, object, camera );

		}

		return program;
	}

	private void updateDynamicUniforms ( uniforms, GeometryObject object, Camera camera )
	{
		var dynamicUniforms = [];

		for ( int j = 0, jl = uniforms.length; j < jl; j ++ ) {

			var uniform = uniforms[ j ][ 0 ];
			var onUpdateCallback = uniform.onUpdateCallback;

			if ( onUpdateCallback !== undefined ) {

				onUpdateCallback.bind( uniform )( object, camera );
				dynamicUniforms.push( uniforms[ j ] );

			}

		}

		loadUniformsGeneric( dynamicUniforms );

	}

	// Uniforms (refresh uniforms objects)

	private void refreshUniformsCommon ( FastMap<Uniform> uniforms, Material material )
	{
		uniforms.get("opacity").setValue( material.getOpacity() );

		if(material instanceof HasColor)
		{
			uniforms.get("diffuse").setValue(((HasColor) material).getColor());
		}

		if ( material instanceof HasAmbientEmissiveColor)
		{
			((Color)uniforms.get("emissive").getValue()).copy( ((HasAmbientEmissiveColor) material).getEmissive() ).multiply( material.emissiveIntensity );
		}

		if(this instanceof HasMap)
		{
			uniforms.get("map").setValue( ((HasMap) material).getMap() );
		}

		if(this instanceof HasSpecularMap)
		{
			uniforms.get("specularMap").setValue( ((HasSpecularMap)material).getSpecularMap() );
		}

		if(this instanceof HasAlphaMap)
		{
			uniforms.get("alphaMap").setValue( ((HasAlphaMap)material).getAlphaMap() );
		}


		if ( material instanceof  HasAoMap)
		{
			uniforms.get("aoMap").setValue( ((HasAoMap)material).getAoMap() );
			uniforms.get("aoMapIntensity").setValue( ((HasAoMap)material).getAoMapIntensity() );
		}

		// uv repeat and offset setting priorities
		// 1. color map
		// 2. specular map
		// 3. normal map
		// 4. bump map
		// 5. alpha map
		// 6. emissive map

		Texture uvScaleMap = null;

		if(this instanceof HasMap)
			uvScaleMap = ((HasMap) this).getMap();

		if(uvScaleMap == null && this instanceof HasSpecularMap)
			uvScaleMap = ((HasSpecularMap)this).getSpecularMap();

		if(uvScaleMap == null && this instanceof HasDisplacementMap)
			uvScaleMap = ((HasDisplacementMap)this).getDisplacementMap();

		if(uvScaleMap == null && this instanceof HasNormalMap)
			uvScaleMap = ((HasNormalMap)this).getNormalMap();

		if(uvScaleMap == null && this instanceof HasBumpMap)
			uvScaleMap = ((HasBumpMap)this).getBumpMap();

		if(uvScaleMap == null && this instanceof HasRoughnessMap)
			uvScaleMap = ((HasRoughnessMap)this).getRoughnessMap();

		if(uvScaleMap == null && this instanceof HasMetalnessMap)
			uvScaleMap = ((HasMetalnessMap)this).getMetalnessMap();

		if(uvScaleMap == null && this instanceof HasAlphaMap)
			uvScaleMap = ((HasAlphaMap)this).getAlphaMap();

		if(uvScaleMap == null && this instanceof HasEmissiveMap)
			uvScaleMap = ((HasEmissiveMap)this).getEmissiveMap();

		if ( uvScaleMap != null ) {

			if ( uvScaleMap instanceof GLRenderTarget ) {

				uvScaleMap = uvScaleMap.texture;

			}

			((Vector4)uniforms.get("offsetRepeat").getValue()).set(
					uvScaleMap.getOffset().getX(),
					uvScaleMap.getOffset().getY(),
					uvScaleMap.getRepeat().getX(),
					uvScaleMap.getRepeat().getY() );

		}

		if(this instanceof HasEnvMap)
		{
			HasEnvMap envMapMaterial = (HasEnvMap)this;

			uniforms.get("envMap").setValue( envMapMaterial.getEnvMap() );
			uniforms.get("flipEnvMap").setValue( ( envMapMaterial.getEnvMap() != null
					&& envMapMaterial.getEnvMap() instanceof GLRenderTargetCube) ? 1.0 : -1.0 );

			uniforms.get("reflectivity").setValue( envMapMaterial.getReflectivity() );
			uniforms.get("refractionRatio").setValue( envMapMaterial.getRefractionRatio() );
		}

	}

	private void refreshUniformsLine ( FastMap<Uniform> uniforms, LineBasicMaterial material ) {

		uniforms.get("diffuse").setValue(((HasColor) material).getColor());
		uniforms.get("opacity").setValue( material.getOpacity() );

	}

	private void refreshUniformsDash ( FastMap<Uniform> uniforms, LineDashedMaterial material ) {

		uniforms.get("dashSize").setValue(material.getDashSize());
		uniforms.get("totalSize").setValue(material.getDashSize() + material.getGapSize());
		uniforms.get("scale").setValue(material.getScale());

	}

	private void refreshUniformsPoints ( FastMap<Uniform> uniforms, PointsMaterial material )
	{
		uniforms.get("diffuse").setValue(material.getColor());
		uniforms.get("opacity").setValue(material.getOpacity());
		uniforms.get("size").setValue(material.getSize() * _pixelRatio);
		uniforms.get("scale").setValue( _height / 2.0); // TODO: Cache this.

		uniforms.get("map").setValue(material.getMap());

		if ( material.getMap() != null )
		{
			((Vector4)uniforms.get("offsetRepeat").getValue()).set(
					material.getMap().getOffset().getX(),
					material.getMap().getOffset().getY(),
					material.getMap().getRepeat().getX(),
					material.getMap().getRepeat().getY() );

		}

	}

	private void refreshUniformsFog ( FastMap<Uniform> uniforms, AbstractFog fog ) {

		uniforms.get("fogColor").setValue( fog.getColor() );

		if ( fog instanceof Fog) {

			uniforms.get("fogNear").setValue( ((Fog)fog).getNear() );
			uniforms.get("fogFar").setValue( ((Fog)fog).getFar() );

		} else if ( fog instanceof FogExp2 ) {

			uniforms.get("fogDensity").setValue( ((FogExp2)fog).getDensity() );

		}

	}

	private void refreshUniformsLambert ( FastMap<Uniform> uniforms, MeshLambertMaterial material ) {

		if ( material instanceof HasLightMap && ((HasLightMap)material).getLightMap() != null ) {

			uniforms.get("lightMap").setValue(((HasLightMap)material).getLightMap());
			uniforms.get("lightMapIntensity").setValue(((HasLightMap)material).getLightMapIntensity());

		}

		if ( material instanceof HasEmissiveMap && ((HasEmissiveMap)material).getEmissiveMap() != null  ) {

			uniforms.get("emissiveMap").setValue( ((HasEmissiveMap)material).getEmissiveMap() );

		}

	}

	private void refreshUniformsPhong ( FastMap<Uniform> uniforms, MeshPhongMaterial material ) {

		uniforms.specular.value = material.specular;
		uniforms.shininess.value = Math.max( material.shininess, 1e-4 ); // to prevent pow( 0.0, 0.0 )

		if ( material.lightMap ) {

			uniforms.lightMap.value = material.lightMap;
			uniforms.lightMapIntensity.value = material.lightMapIntensity;

		}

		if ( material.emissiveMap ) {

			uniforms.emissiveMap.value = material.emissiveMap;

		}

		if ( material.bumpMap ) {

			uniforms.bumpMap.value = material.bumpMap;
			uniforms.bumpScale.value = material.bumpScale;

		}

		if ( material.normalMap ) {

			uniforms.normalMap.value = material.normalMap;
			uniforms.normalScale.value.copy( material.normalScale );

		}

		if ( material.displacementMap ) {

			uniforms.displacementMap.value = material.displacementMap;
			uniforms.displacementScale.value = material.displacementScale;
			uniforms.displacementBias.value = material.displacementBias;

		}

	}

	private void refreshUniformsStandard ( FastMap<Uniform> uniforms, Material material ) {

		uniforms.roughness.value = material.roughness;
		uniforms.metalness.value = material.metalness;

		if ( material.roughnessMap ) {

			uniforms.roughnessMap.value = material.roughnessMap;

		}

		if ( material.metalnessMap ) {

			uniforms.metalnessMap.value = material.metalnessMap;

		}

		if ( material.lightMap ) {

			uniforms.lightMap.value = material.lightMap;
			uniforms.lightMapIntensity.value = material.lightMapIntensity;

		}

		if ( material.emissiveMap ) {

			uniforms.emissiveMap.value = material.emissiveMap;

		}

		if ( material.bumpMap ) {

			uniforms.bumpMap.value = material.bumpMap;
			uniforms.bumpScale.value = material.bumpScale;

		}

		if ( material.normalMap ) {

			uniforms.normalMap.value = material.normalMap;
			uniforms.normalScale.value.copy( material.normalScale );

		}

		if ( material.displacementMap ) {

			uniforms.displacementMap.value = material.displacementMap;
			uniforms.displacementScale.value = material.displacementScale;
			uniforms.displacementBias.value = material.displacementBias;

		}

		if ( material.envMap ) {

			//uniforms.envMap.value = material.envMap; // part of uniforms common
			uniforms.envMapIntensity.value = material.envMapIntensity;

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
					gl.vertexAttribPointer( attributes[ "morphTarget" + m ], 3,
					gl.FLOAT, false, 0, 0 );

					if ( material.morphNormals ) {

						gl.vertexAttribPointer( attributes[ "morphNormal" + m ], 3,
						gl.FLOAT, false, 0, 0 );

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

	private void setCubeTextureDynamic(GLRenderTargetCube texture, int slot)
	{
		this.gl.glActiveTexture(TextureUnit.TEXTURE0.getValue() + slot);
		this.gl.glBindTexture(TextureTarget.TEXTURE_CUBE_MAP.getValue(), texture.getWebGlTexture());
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

	// -----------------------------------------------------------------------------------------------------------------
    // NEW code

	private void loadUniform( Uniform uniform ) 
	{
		int location = uniform.getLocation();

		if ( location == -1 ) return;

		Uniform.TYPE type = uniform.getType();

		// Up textures also for undefined values
		if ( type != Uniform.TYPE.T && uniform.getValue() == null ) return;

		if ( type == Uniform.TYPE.I1 ) {

            int value = (uniform.getValue() instanceof Boolean)
                    ? ((Boolean) uniform.getValue()) ? 1 : 0 : (Integer) uniform.getValue();

            this.gl.glUniform1i(location, value);

		} else if ( type == Uniform.TYPE.F1 ) {

            float value = uniform.getValue() instanceof Float
                    ? (Float) uniform.getValue() : ((Double)uniform.getValue()).floatValue();

            this.gl.glUniform1f(location, value);

		} else if ( type == Uniform.TYPE.F2 ) {

            Double[] value = (Double[])uniform.getValue();

            this.gl.glUniform2f(location, value[ 0 ].floatValue(), value[ 1 ].floatValue());

		} else if ( type == Uniform.TYPE.F3 ) {

            Double[] value = (Double[])uniform.getValue();

            this.gl.glUniform3f(location, value[ 0 ].floatValue(), value[ 1 ].floatValue(), value[ 2 ].floatValue());

		} else if ( type == Uniform.TYPE.F4 ) {

            Double[] value = (Double[])uniform.getValue();

            this.gl.glUniform4f(location, value[ 0 ].floatValue(), value[ 1 ].floatValue(), value[ 2 ].floatValue(), value[ 3 ].floatValue());

		} else if ( type == Uniform.TYPE.IV1 ) {

            Int32Array value = (Int32Array )uniform.getValue();

            this.gl.glUniform1iv(location, value.getLength(), value.getTypedBuffer());

		} else if ( type == Uniform.TYPE.IV3 ) {

            Int32Array value = (Int32Array )uniform.getValue();

            this.gl.glUniform3iv(location, value.getLength() / 3, value.getTypedBuffer());

		} else if ( type == Uniform.TYPE.FV1 ) {

            Float32Array value = (Float32Array )uniform.getValue();

            this.gl.glUniform1fv(location, value.getLength(), value.getTypedBuffer());

		} else if ( type == Uniform.TYPE.FV2 ) {

            Float32Array value = (Float32Array )uniform.getValue();

            this.gl.glUniform2fv(location, value.getLength() / 2, value.getTypedBuffer());

		} else if ( type == Uniform.TYPE.FV3 ) {

            Float32Array value = (Float32Array )uniform.getValue();

            this.gl.glUniform3fv(location, value.getLength() / 3, value.getTypedBuffer());

		} else if ( type == Uniform.TYPE.FV4 ) {

            Float32Array value = (Float32Array )uniform.getValue();

            this.gl.glUniform4fv(location, value.getLength() / 4, value.getTypedBuffer());

		} else if ( type == Uniform.TYPE.Matrix2fv ) {

            Float32Array value = (Float32Array )uniform.getValue();

            this.gl.glUniformMatrix2fv(location, 1, false, value.getTypedBuffer());

		} else if ( type == Uniform.TYPE.Matrix3fv ) {

            Float32Array value = (Float32Array )uniform.getValue();

            this.gl.glUniformMatrix3fv(location, 1, false, value.getTypedBuffer());

		} else if ( type == Uniform.TYPE.Matrix4fv ) {

            Float32Array value = (Float32Array )uniform.getValue();

            this.gl.glUniformMatrix4fv(location, 1, false, value.getTypedBuffer());

			//

		} else if ( type == Uniform.TYPE.I ) {

			// single integer
            int value = (uniform.getValue() instanceof Boolean)
                    ? ((Boolean) uniform.getValue()) ? 1 : 0 : (Integer) uniform.getValue();

            this.gl.glUniform1i(location, value);

		} else if ( type == Uniform.TYPE.F ) {

			// single float
            float value = uniform.getValue() instanceof Float
                    ? (Float) uniform.getValue() : ((Double)uniform.getValue()).floatValue();

            this.gl.glUniform1f(location, value);

		} else if ( type == Uniform.TYPE.V2 ) {

			// single Vector2
            Vector2 value = (Vector2) uniform.getValue();

            this.gl.glUniform2f(location, (float)value.getX(), (float)value.getX());

		} else if ( type == Uniform.TYPE.V3 ) {

			// single Vector3
            Vector3 value = (Vector3) uniform.getValue();

            this.gl.glUniform3f(location, (float)value.getX(), (float)value.getY(), (float)value.getZ());

		} else if ( type == Uniform.TYPE.V4 ) {

			// single Vector4
            Vector4 value = (Vector4) uniform.getValue();

            this.gl.glUniform4f(location, (float)value.getX(), (float)value.getY(), (float)value.getZ(), (float)value.getW());

		} else if ( type == Uniform.TYPE.C ) {

			// single Color
            Color value = (Color) uniform.getValue();

            this.gl.glUniform3f(location, (float)value.getR(), (float)value.getG(), (float)value.getB());

		} else if ( type == Uniform.TYPE.S ) {

			// TODO: Optimize this

			FastMap<Uniform> properties = uniform.getProperties();

			for ( String name : properties.keySet() ) {

				Uniform property = properties.get( name );

				loadUniform( property );

			}

		} else if ( type == Uniform.TYPE.SA ) {

			// TODO: Optimize this

            FastMap<Uniform> properties = uniform.getProperties();

            for ( String name : properties.keySet() ) {

                Uniform property = properties.get( name );
                ArrayList<Object> value = (ArrayList<Object>) uniform.getValue();

                for ( int i = 0, l = value.size(); i < l; i ++ ) {

                    property.setValue(value.get(i));
                    loadUniform( property );
                }

            }

		} else if ( type == Uniform.TYPE.IV ) {

			// flat array of integers with 3 x N size (JS or typed array)
            Int32Array value = (Int32Array )uniform.getValue();

            this.gl.glUniform3iv(location, value.getLength() / 3, value.getTypedBuffer());

		} else if ( type == Uniform.TYPE.FV ) {

			// flat array of floats with 3 x N size (JS or typed array)
            Float32Array value = (Float32Array )uniform.getValue();

            this.gl.glUniform3fv(location, value.getLength() / 3, value.getTypedBuffer());

		} else if ( type == Uniform.TYPE.V2V ) {

			// array of Vector2

            List<Vector2> listVector2f = (List<Vector2>) uniform.getValue();
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

		} else if ( type == Uniform.TYPE.V3V ) {

			// array of Vector3

            List<Vector3> listVector3f = (List<Vector3>) uniform.getValue();
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

		} else if ( type == Uniform.TYPE.V4V ) {

			// array of Vector4

            List<Vector4> listVector4f = (List<Vector4>) uniform.getValue();
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

		} else if ( type == Uniform.TYPE.M3 ) {

			// single Matrix3
            Matrix3 value = (Matrix3) uniform.getValue();

            this.gl.glUniformMatrix4fv(location, 1, false, value.getArray().getTypedBuffer());

		} else if ( type == Uniform.TYPE.M3V) {

			// array of Matrix3
            List<Matrix3> listMatrix3f = (List<Matrix3>) uniform.getValue();
            Float32Array cacheArray = uniform.getCacheArray();
            if ( cacheArray == null )
                uniform.setCacheArray(cacheArray = Float32Array.create( 9 * listMatrix3f.size() ) );

            for ( int i = 0, il = listMatrix3f.size(); i < il; i ++ )
                listMatrix3f.get( i ).flattenToArrayOffset( cacheArray, i * 9 );

            this.gl.glUniformMatrix3fv(location, cacheArray.getLength() / 9,
                    false, cacheArray.getTypedBuffer());

		} else if ( type == Uniform.TYPE.M4 ) {

			// single Matrix4
            Matrix4 matrix4 = (Matrix4) uniform.getValue();

            this.gl.glUniformMatrix4fv(location, 1, false, matrix4.getArray().getTypedBuffer());

		} else if ( type == Uniform.TYPE.M4V ) {

			// array of Matrix4

            List<Matrix4> listMatrix4f = (List<Matrix4>) uniform.getValue();
            Float32Array cacheArray = uniform.getCacheArray();
            if ( cacheArray == null )
                uniform.setCacheArray(cacheArray = Float32Array.create( 16 * listMatrix4f.size() ) );

            for ( int i = 0, il = listMatrix4f.size(); i < il; i ++ )
                listMatrix4f.get( i ).flattenToArrayOffset( cacheArray, i * 16 );

            this.gl.glUniformMatrix4fv(location, cacheArray.getLength() / 16,
                    false, cacheArray.getTypedBuffer());

		} else if ( type == Uniform.TYPE.T ) {

			// single Texture (2d or cube)
            AbstractTexture texture = (AbstractTexture)uniform.getValue();
            int textureUnit = getTextureUnit();

            this.gl.glUniform1i(location, textureUnit);

            if ( texture == null ) return;

            if ( texture instanceof CubeTexture ) {

                // CompressedTexture can have Array in image :/

                setCubeTexture( (CubeTexture)texture, textureUnit );

            } else if ( texture instanceof GLRenderTargetCube ) {

                setCubeTextureDynamic( ((GLRenderTargetCube) texture).getTexture(), textureUnit );

            } else if ( texture instanceof GLRenderTarget ) {

                this.setTexture( ((GLRenderTarget) texture).getTexture(), textureUnit );

            } else {

                this.setTexture( (Texture) texture, textureUnit );

            }

		} else if ( type == Uniform.TYPE.TV ) {

			// array of Texture (2d or cube)
            List<AbstractTexture> textureList = (List<AbstractTexture>)uniform.getValue();
            int[] units = new int[textureList.size()];

            for( int i = 0, il = textureList.size(); i < il; i ++ )
            {
                units[ i ] = getTextureUnit();
            }

            this.gl.glUniform1iv(location, 1, units, 0);

            for( int i = 0, il = textureList.size(); i < il; i ++ )
            {
                AbstractTexture texture = textureList.get( i );
                int textureUnit = units[ i ];

                if ( texture == null ) continue;

                if ( texture instanceof CubeTexture ) {

                    // CompressedTexture can have Array in image :/

                    setCubeTexture( (CubeTexture) texture, textureUnit );

                } else if ( texture instanceof GLRenderTarget ) {

                    this.setTexture( ((GLRenderTarget) texture).getTexture(), textureUnit );

                } else if ( texture instanceof GLRenderTargetCube ) {

                    setCubeTextureDynamic( ((GLRenderTargetCube) texture).getTexture(), textureUnit );

                } else {

                    this.setTexture( (Texture) texture, textureUnit );

                }
            }

		} else {

			Log.warn( "GLRenderer: Unknown uniform type: " + type );

		}

	}
	
    private void loadUniformsGeneric( FastMap<Uniform> uniforms ) {

        for ( var i = 0, l = uniforms.length; i < l; i ++ ) {

            var uniform = uniforms[ i ][ 0 ];

            // needsUpdate property is not added to all uniforms.
            if ( uniform.needsUpdate == false ) continue;

            var type = uniform.type;
            var location = uniforms[ i ][ 1 ];
            var value = uniform.value;

            loadUniform( uniform, type, location, value );

        }

    }

    // GL state setting
    private void setupLights ( List<Light> lights, Camera camera ) {

        double r = 0, g = 0, b = 0;

        Matrix4 viewMatrix = camera.getMatrixWorldInverse();

        double directionalLength = 0,
            pointLength = 0,
            spotLength = 0,
            hemiLength = 0,

            shadowsLength = 0;

        _lights.shadowsPointLight = 0;

        for ( int l = 0, ll = lights.size(); l < ll; l ++ ) {

            Light light = lights.get(l);

            Color color = light.getColor();
            double intensity = light.getIntensity();

            if ( light instanceof AmbientLight) {

                r += color.getR() * intensity;
                g += color.getG() * intensity;
                b += color.getB() * intensity;

            } else if ( light instanceof DirectionalLight ) {

                FastMap<Uniform> uniforms = lightCache.get( light );

                ((Color)uniforms.get("color").getValue()).copy(light.getColor()).multiplyScalar(light.getIntensity());
                ((Vector3)uniforms.get("direction").getValue()).setFromMatrixPosition(light.getMatrixWorld());
                _vector3.setFromMatrixPosition(((DirectionalLight) light).getTarget().getMatrixWorld());
                ((Vector3)uniforms.get("direction").getValue()).sub( _vector3 );
                ((Vector3)uniforms.get("direction").getValue()).transformDirection( viewMatrix );

                uniforms.get("shadow").setValue( light.isCastShadow() );

                if (light.isCastShadow()) {

                    uniforms.get("shadowBias").setValue( ((DirectionalLight) light).getShadow().getBias() );
                    uniforms.get("shadowRadius").setValue( ((DirectionalLight) light).getShadow().getRadius() );
                    uniforms.get("shadowMapSize").setValue( ((DirectionalLight) light).getShadow().getMapSize() );

                    _lights.shadows[ shadowsLength ++ ] = light;

                }

                _lights.directionalShadowMap[ directionalLength ] = ((DirectionalLight) light).getShadow().getMap();
                _lights.directionalShadowMatrix[ directionalLength ] = ((DirectionalLight) light).getShadow().getMatrix();
                _lights.directional[ directionalLength ++ ] = uniforms;

            } else if ( light instanceof SpotLight ) {

                double distance = ((SpotLight) light).getDistance();

                var uniforms = lightCache.get( light );

                uniforms.position.setFromMatrixPosition(light.getMatrixWorld());
                uniforms.position.applyMatrix4( viewMatrix );

                uniforms.color.copy( color ).multiplyScalar( intensity );
                uniforms.distance = distance;

                uniforms.direction.setFromMatrixPosition(light.getMatrixWorld());
                _vector3.setFromMatrixPosition( light.target.matrixWorld );
                uniforms.direction.sub( _vector3 );
                uniforms.direction.transformDirection( viewMatrix );

                uniforms.coneCos = Math.cos( light.angle );
                uniforms.penumbraCos = Math.cos( light.angle * ( 1 - light.penumbra ) );
                uniforms.decay = ( light.distance == 0 ) ? 0.0 : light.decay;

                uniforms.shadow = light.castShadow;

                if ( light.castShadow ) {

                    uniforms.shadowBias = light.shadow.bias;
                    uniforms.shadowRadius = light.shadow.radius;
                    uniforms.shadowMapSize = light.shadow.mapSize;

                    _lights.shadows[ shadowsLength ++ ] = light;

                }

                _lights.spotShadowMap[ spotLength ] = light.shadow.map;
                _lights.spotShadowMatrix[ spotLength ] = light.shadow.matrix;
                _lights.spot[ spotLength ++ ] = uniforms;

            } else if ( light instanceof PointLight ) {

                double distance = ((PointLight) light).getDistance();

                var uniforms = lightCache.get( light );

                uniforms.position.setFromMatrixPosition( light.matrixWorld );
                uniforms.position.applyMatrix4( viewMatrix );

                uniforms.color.copy( light.color ).multiplyScalar( light.intensity );
                uniforms.distance = light.distance;
                uniforms.decay = ( light.distance == 0 ) ? 0.0 : light.decay;

                uniforms.shadow = light.castShadow;

                if ( light.castShadow ) {

                    uniforms.shadowBias = light.shadow.bias;
                    uniforms.shadowRadius = light.shadow.radius;
                    uniforms.shadowMapSize = light.shadow.mapSize;

                    _lights.shadows[ shadowsLength ++ ] = light;

                }

                _lights.pointShadowMap[ pointLength ] = light.shadow.map;

                if ( _lights.pointShadowMatrix[ pointLength ] == undefined ) {

                    _lights.pointShadowMatrix[ pointLength ] = new THREE.Matrix4();

                }

                // for point lights we set the shadow matrix to be a translation-only matrix
                // equal to inverse of the light's position
                _vector3.setFromMatrixPosition( light.matrixWorld ).negate();
                _lights.pointShadowMatrix[ pointLength ].identity().setPosition( _vector3 );

                _lights.point[ pointLength ++ ] = uniforms;

            } else if ( light instanceof HemisphereLight ) {

                var uniforms = lightCache.get( light );

                uniforms.direction.setFromMatrixPosition( light.matrixWorld );
                uniforms.direction.transformDirection( viewMatrix );
                uniforms.direction.normalize();

                uniforms.skyColor.copy( light.color ).multiplyScalar( intensity );
                uniforms.groundColor.copy( light.groundColor ).multiplyScalar( intensity );

                _lights.hemi[ hemiLength ++ ] = uniforms;

            }

        }

        _lights.ambient[ 0 ] = r;
        _lights.ambient[ 1 ] = g;
        _lights.ambient[ 2 ] = b;

        _lights.directional.length = directionalLength;
        _lights.spot.length = spotLength;
        _lights.point.length = pointLength;
        _lights.hemi.length = hemiLength;

        _lights.shadows.length = shadowsLength;

        _lights.hash = directionalLength + ',' + pointLength + ',' + spotLength + ',' + hemiLength + ',' + shadowsLength;

    }

    public void setFaceCulling( CullFaceMode cullFace, FrontFaceDirection frontFaceDirection ) {

        if (cullFace == null) {

            state.disable(EnableCap.CULL_FACE);

        } else {

            if (frontFaceDirection == FrontFaceDirection.CW) {

                gl.glFrontFace(FrontFaceDirection.CW.getValue());

            } else {

                gl.glFrontFace(FrontFaceDirection.CCW.getValue());

            }

            if (cullFace == CullFaceMode.BACK) {

                gl.glCullFace(CullFaceMode.BACK.getValue());

            } else if (cullFace == CullFaceMode.FRONT) {

                gl.glCullFace(CullFaceMode.FRONT.getValue());

            } else {

                gl.glCullFace(CullFaceMode.FRONT_AND_BACK.getValue());

            }

            state.enable(EnableCap.CULL_FACE);

        }

    }
    
    private void setTextureParameters ( TextureTarget textureType, Texture texture, boolean isPowerOfTwoImage ) {

        if ( isPowerOfTwoImage ) {

            gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_WRAP_S.getValue(), texture.getWrapS().getValue());
            gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_WRAP_T.getValue(), texture.getWrapT().getValue());
            
            gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_MAG_FILTER.getValue(), texture.getMagFilter().getValue() );
            gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_MIN_FILTER.getValue(), texture.getMinFilter().getValue());

        } else {

            gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_WRAP_S.getValue(), GL20.GL_CLAMP_TO_EDGE);
            gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_WRAP_T.getValue(), GL20.GL_CLAMP_TO_EDGE);
            
            if ( texture.getWrapS() != TextureWrapMode.CLAMP_TO_EDGE || texture.getWrapT() != TextureWrapMode.CLAMP_TO_EDGE ) {

                Log.warn( "GLRenderer: Texture is not power of two. Texture.wrapS and Texture.wrapT should be set to THREE.ClampToEdgeWrapping: " + texture );

            }

            gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_MAG_FILTER.getValue(), filterFallback(texture.getMagFilter().getValue()));
            gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_MIN_FILTER.getValue(), filterFallback(texture.getMinFilter().getValue()));
            

            if ( texture.getMinFilter() != TextureMinFilter.NEAREST && texture.getMinFilter() != TextureMinFilter.LINEAR ) {

                Log.warn( "GLRenderer: Texture is not power of two. Texture.minFilter should be set to TextureMinFilter.NEAREST or TextureMinFilter.LINEAR: " + texture );

            }

        }

        boolean extension = GLExtensions.check(gl, GLES20Ext.List.EXT_texture_filter_anisotropic );

        // TODO: Fix extensions
        if ( extension ) {

//            if ( texture.type == THREE.FloatType && extensions.get( 'OES_texture_float_linear' ) == null ) return;
//            if ( texture.type == THREE.HalfFloatType && extensions.get( 'OES_texture_half_float_linear' ) == null ) return;
//
//            if ( texture.getAnisotropy() > 1 || properties.get( texture ).__currentAnisotropy ) {
//
//                _gl.texParameterf( textureType, extension.TEXTURE_MAX_ANISOTROPY_EXT, Math.min( texture.anisotropy, _this.getMaxAnisotropy() ) );
//                properties.get( texture ).__currentAnisotropy = texture.anisotropy;
//
//            }

        }

    }
    
    private void uploadTexture( FastMap<Object> textureProperties, Texture texture, int slot ) {

        if ( textureProperties.__webglInit == undefined ) {

            textureProperties.__webglInit = true;

            texture.addEventListener( 'dispose', onTextureDispose );

            textureProperties.__webglTexture = _gl.createTexture();

            _infoMemory.textures ++;

        }

        state.activeTexture( _gl.TEXTURE0 + slot );
        state.bindTexture( _gl.TEXTURE_2D, textureProperties.__webglTexture );

        _gl.pixelStorei( _gl.UNPACK_FLIP_Y_WEBGL, texture.flipY );
        _gl.pixelStorei( _gl.UNPACK_PREMULTIPLY_ALPHA_WEBGL, texture.premultiplyAlpha );
        _gl.pixelStorei( _gl.UNPACK_ALIGNMENT, texture.unpackAlignment );

        var image = clampToMaxSize( texture.image, capabilities.maxTextureSize );

        if ( textureNeedsPowerOfTwo( texture ) && isPowerOfTwo( image ) == false ) {

            image = makePowerOfTwo( image );

        }

        var isPowerOfTwoImage = isPowerOfTwo( image ),
                glFormat = paramThreeToGL( texture.format ),
                glType = paramThreeToGL( texture.type );

        setTextureParameters( _gl.TEXTURE_2D, texture, isPowerOfTwoImage );

        var mipmap, mipmaps = texture.mipmaps;

        if ( texture instanceof THREE.DataTexture ) {

            // use manually created mipmaps if available
            // if there are no manual mipmaps
            // set 0 level mipmap and then use GL to generate other mipmap levels

            if ( mipmaps.length > 0 && isPowerOfTwoImage ) {

                for ( var i = 0, il = mipmaps.length; i < il; i ++ ) {

                    mipmap = mipmaps[ i ];
                    state.texImage2D( _gl.TEXTURE_2D, i, glFormat, mipmap.width, mipmap.height, 0, glFormat, glType, mipmap.data );

                }

                texture.generateMipmaps = false;

            } else {

                state.texImage2D( _gl.TEXTURE_2D, 0, glFormat, image.width, image.height, 0, glFormat, glType, image.data );

            }

        } else if ( texture instanceof THREE.CompressedTexture ) {

            for ( var i = 0, il = mipmaps.length; i < il; i ++ ) {

                mipmap = mipmaps[ i ];

                if ( texture.format !== THREE.RGBAFormat && texture.format !== THREE.RGBFormat ) {

                    if ( state.getCompressedTextureFormats().indexOf( glFormat ) > - 1 ) {

                        state.compressedTexImage2D( _gl.TEXTURE_2D, i, glFormat, mipmap.width, mipmap.height, 0, mipmap.data );

                    } else {

                        console.warn( "THREE.WebGLRenderer: Attempt to load unsupported compressed texture format in .uploadTexture()" );

                    }

                } else {

                    state.texImage2D( _gl.TEXTURE_2D, i, glFormat, mipmap.width, mipmap.height, 0, glFormat, glType, mipmap.data );

                }

            }

        } else {

            // regular Texture (image, video, canvas)

            // use manually created mipmaps if available
            // if there are no manual mipmaps
            // set 0 level mipmap and then use GL to generate other mipmap levels

            if ( mipmaps.length > 0 && isPowerOfTwoImage ) {

                for ( var i = 0, il = mipmaps.length; i < il; i ++ ) {

                    mipmap = mipmaps[ i ];
                    state.texImage2D( _gl.TEXTURE_2D, i, glFormat, glFormat, glType, mipmap );

                }

                texture.generateMipmaps = false;

            } else {

                state.texImage2D( _gl.TEXTURE_2D, 0, glFormat, glFormat, glType, image );

            }

        }

        if ( texture.generateMipmaps && isPowerOfTwoImage ) _gl.generateMipmap( _gl.TEXTURE_2D );

        textureProperties.__version = texture.version;

        if ( texture.onUpdate ) texture.onUpdate( texture );

    }

    public void setTexture( Texture texture, int slot ) {

        var textureProperties = properties.get(texture);

        if (texture.version > 0 && textureProperties.__version != = texture.version) {

            var image = texture.image;

            if (image == = undefined) {

                Log.warn( "GLRenderer: Texture marked for update but image is undefined: " + texture);

                return;

            }

            if (image.complete == = false) {

                Log.warn( "GLRenderer: Texture marked for update but image is incomplete: " + texture);
                return;

            }

            uploadTexture(textureProperties, texture, slot);

            return;

        }

        state.activeTexture(_gl.TEXTURE0 + slot);
        state.bindTexture(_gl.TEXTURE_2D, textureProperties.__webglTexture);

    }

    private void setCubeTexture ( CubeTexture texture, int slot ) {

        FastMap<Object> textureProperties = properties.get( texture );

        if ( texture.getImages().size() == 6 ) {

            if ( texture.version > 0 && textureProperties.__version != texture.version ) {

                if ( ! textureProperties.__image__webglTextureCube ) {

                    texture.addEventListener( 'dispose', onTextureDispose );

                    textureProperties.__image__webglTextureCube = _gl.createTexture();

                    info.getMemory().textures ++;

                }

                state.activeTexture( TextureUnit.TEXTURE0.getValue() + slot );
                state.bindTexture( _gl.TEXTURE_CUBE_MAP, textureProperties.__image__webglTextureCube );

                _gl.pixelStorei( _gl.UNPACK_FLIP_Y_WEBGL, texture.flipY );

                boolean isCompressed = texture instanceof CompressedTexture;
                boolean isDataTexture = texture.getImages().get( 0 ) instanceof DataTexture;

                var cubeImage = [];

                for ( int i = 0; i < 6; i ++ ) {

                    if ( _this.autoScaleCubemaps && ! isCompressed && ! isDataTexture ) {

                        cubeImage[ i ] = clampToMaxSize( texture.image[ i ], capabilities.maxCubemapSize );

                    } else {

                        cubeImage[ i ] = isDataTexture ? texture.image[ i ].image : texture.image[ i ];

                    }

                }

                var image = cubeImage[ 0 ];
                boolean isPowerOfTwoImage = isPowerOfTwo( image );
                PixelFormat glFormat = texture.getFormat();
                PixelType glType = texture.getType();

                setTextureParameters( TextureTarget.TEXTURE_CUBE_MAP, texture, isPowerOfTwoImage );

                for ( int i = 0; i < 6; i ++ ) {

                    if ( ! isCompressed ) {

                        if ( isDataTexture ) {

                            state.texImage2D( TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X.getValue() + i, 0, glFormat, cubeImage[ i ].width, cubeImage[ i ].height, 0, glFormat, glType, cubeImage[ i ].data );

                        } else {

                            state.texImage2D( TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X.getValue() + i, 0, glFormat, glFormat, glType, cubeImage[ i ] );

                        }

                    } else {

                        var mipmap, mipmaps = cubeImage[ i ].mipmaps;

                        for ( int j = 0, jl = mipmaps.length; j < jl; j ++ ) {

                            mipmap = mipmaps[ j ];

                            if ( texture.getFormat() != PixelFormat.RGBA && texture.getFormat() != PixelFormat.RGB ) {

                                if ( state.getCompressedTextureFormats().indexOf( glFormat ) > - 1 ) {

                                    state.compressedTexImage2D( TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X.getValue() + i, j, glFormat, mipmap.width, mipmap.height, 0, mipmap.data );

                                } else {

                                    Log.warn( "GLRenderer: Attempt to load unsupported compressed texture format in .setCubeTexture()" );

                                }

                            } else {

                                state.texImage2D( TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X.getValue() + i, j,
                                        glFormat, mipmap.width, mipmap.height, 0, glFormat, glType, mipmap.data );

                            }

                        }

                    }

                }

                if ( texture.isGenerateMipmaps() && isPowerOfTwoImage ) {

                    gl.glGenerateMipmap( TextureTarget.TEXTURE_CUBE_MAP.getValue() );

                }

                textureProperties.__version = texture.version;

                if ( texture.onUpdate ) texture.onUpdate( texture );

            } else {

                state.activeTexture( TextureUnit.TEXTURE0.getValue() + slot );
                state.bindTexture( TextureTarget.TEXTURE_CUBE_MAP, (int)textureProperties.get("__image__webglTextureCube"));

            }

        }

    }

    private void setCubeTextureDynamic ( Texture texture, int slot ) {

        state.activeTexture( TextureUnit.TEXTURE0.getValue() + slot );
        state.bindTexture( TextureTarget.TEXTURE_CUBE_MAP, (int)properties.get( texture ).get("__webglTexture"));

    }

    // Render targets

    /**
     * Setup storage for target texture and bind it to correct framebuffer
     */
    private  void setupFrameBufferTexture ( int framebuffer, GLRenderTarget renderTarget, int attachment, int textureTarget ) {

        PixelFormat glFormat = renderTarget.texture.getFormat();
        PixelType glType = renderTarget.texture.getType();
        state.texImage2D( textureTarget, 0, glFormat, renderTarget.width, renderTarget.height, 0, glFormat, glType, null );
        gl.glBindFramebuffer( GL20.GL_FRAMEBUFFER, framebuffer );
        gl.glFramebufferTexture2D( GL20.GL_FRAMEBUFFER, attachment, textureTarget, (int)properties.get( renderTarget.texture ).get("__webglTexture"), 0 );
        gl.glBindFramebuffer( GL20.GL_FRAMEBUFFER, 0 /*null*/ );

    }

    /**
     * Setup storage for internal depth/stencil buffers and bind to correct framebuffer
     */
    private void setupRenderBufferStorage ( int renderbuffer, GLRenderTarget renderTarget ) {

        gl.glBindRenderbuffer( GL20.GL_RENDERBUFFER, renderbuffer );

        if ( renderTarget.depthBuffer && ! renderTarget.stencilBuffer ) {

            gl.glRenderbufferStorage( GL20.GL_RENDERBUFFER, RenderbufferInternalFormat.DEPTH_COMPONENT16.getValue(), renderTarget.width, renderTarget.height );
            gl.glFramebufferRenderbuffer( GL20.GL_FRAMEBUFFER, FramebufferSlot.DEPTH_ATTACHMENT.getValue(), GL20.GL_RENDERBUFFER, renderbuffer );

        } else if ( renderTarget.depthBuffer && renderTarget.stencilBuffer ) {

            gl.glRenderbufferStorage( GL20.GL_RENDERBUFFER, RenderbufferInternalFormat.DEPTH_STENCIL.getValue(), renderTarget.width, renderTarget.height );
            gl.glFramebufferRenderbuffer( GL20.GL_FRAMEBUFFER, FramebufferSlot.DEPTH_STENCIL_ATTACHMENT.getValue(), GL20.GL_RENDERBUFFER, renderbuffer );

        } else {

            // FIXME: We don't support !depth !stencil
            gl.glRenderbufferStorage( GL20.GL_RENDERBUFFER, RenderbufferInternalFormat.RGBA4.getValue(), renderTarget.width, renderTarget.height );

        }

        gl.glBindRenderbuffer( GL20.GL_RENDERBUFFER, 0 /*null*/ );

    }
    
    /**
     * Setup GL resources for a non-texture depth buffer
     * @param renderTarget
     */
    private void setupDepthRenderbuffer( GLRenderTarget renderTarget ) {

        FastMap<Object> renderTargetProperties = properties.get( renderTarget );

        boolean isCube = ( renderTarget instanceof GLRenderTargetCube );

        if ( isCube ) {

            renderTargetProperties.put("__webglDepthbuffer" , new int[6]);

            for ( int i = 0; i < 6; i ++ ) {

                gl.glBindFramebuffer( GL20.GL_FRAMEBUFFER, ((int[])renderTargetProperties.get("__webglFramebuffer"))[ i ] );
                ((int[])renderTargetProperties.get("__webglDepthbuffer"))[ i ] = gl.glGenRenderbuffer();
                setupRenderBufferStorage( ((int[])renderTargetProperties.get("__webglDepthbuffer"))[ i ], renderTarget );

            }

        } else {

            gl.glBindFramebuffer( GL20.GL_FRAMEBUFFER, (int)renderTargetProperties.get("__webglFramebuffer") );
            renderTargetProperties.put("__webglDepthbuffer", gl.glGenRenderbuffer());
            setupRenderBufferStorage( (int)renderTargetProperties.get("__webglDepthbuffer"), renderTarget );

        }

        gl.glBindFramebuffer( GL20.GL_FRAMEBUFFER, 0 /*null*/ );

    }
    
    /**
     * Set up GL resources for the render target
     * @param renderTarget
     */
    private void setupRenderTarget( GLRenderTarget renderTarget )
    {
        FastMap<Object> renderTargetProperties = properties.get( renderTarget );
        FastMap<Object> textureProperties = properties.get( renderTarget.texture );

        // TODO: Fix dispose
//        renderTarget.addEventListener( 'dispose', onRenderTargetDispose );

        textureProperties.put("__webglTexture", gl.glGenTexture());

        info.getMemory().textures ++;

        boolean isCube = ( renderTarget instanceof GLRenderTargetCube );
        boolean isTargetPowerOfTwo = Mathematics.isPowerOfTwo( renderTarget.width ) && Mathematics.isPowerOfTwo( renderTarget.height );

        // Setup framebuffer

        if ( isCube ) {

            renderTargetProperties.put("__webglFramebuffer", new int[6]);

            for ( int i = 0; i < 6; i ++ ) {

                ((int[])renderTargetProperties.get("__webglFramebuffer"))[ i ] = gl.glGenFramebuffer();

            }

        } else {

            renderTargetProperties.put("__webglFramebuffer", gl.glGenFramebuffer());

        }

        // Setup color buffer

        if ( isCube ) {

            state.bindTexture( TextureTarget.TEXTURE_CUBE_MAP, (Integer) textureProperties.get("__webglTexture"));
            setTextureParameters( TextureTarget.TEXTURE_CUBE_MAP, renderTarget.texture, isTargetPowerOfTwo );

            for ( int i = 0; i < 6; i ++ ) {

                setupFrameBufferTexture( ((int[])renderTargetProperties.get("__webglFramebuffer"))[ i ], 
                        renderTarget, GL20.GL_COLOR_ATTACHMENT0, TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X.getValue() + i );

            }

            if ( renderTarget.texture.isGenerateMipmaps() && isTargetPowerOfTwo )
                gl.glGenerateMipmap( TextureTarget.TEXTURE_CUBE_MAP.getValue() );

            state.bindTexture( TextureTarget.TEXTURE_CUBE_MAP, 0 /*null*/ );

        } else {

            state.bindTexture( TextureTarget.TEXTURE_2D, (int)textureProperties.get("__webglTexture"));
            setTextureParameters( TextureTarget.TEXTURE_2D, renderTarget.texture, isTargetPowerOfTwo );
            setupFrameBufferTexture( (int)renderTargetProperties.get("__webglFramebuffer"), 
                    renderTarget, GL20.GL_COLOR_ATTACHMENT0, TextureTarget.TEXTURE_2D.getValue() );

            if ( renderTarget.texture.isGenerateMipmaps() && isTargetPowerOfTwo )
                gl.glGenerateMipmap( TextureTarget.TEXTURE_2D.getValue() );

            state.bindTexture( TextureTarget.TEXTURE_2D, 0 /*null*/ );

        }

        // Setup depth and stencil buffers

        if ( renderTarget.depthBuffer ) {

            setupDepthRenderbuffer( renderTarget );

        }

    }

    public GLRenderTarget getCurrentRenderTarget() {

        return _currentRenderTarget;

    }

    /**
     * Setup render target
     * @param renderTarget
     */
	public void setRenderTarget(GLRenderTarget renderTarget) {

		_currentRenderTarget = renderTarget;

		if (renderTarget != null && !properties.get(renderTarget).containsKey("__webglFramebuffer")) {

			setupRenderTarget(renderTarget);

		}

		boolean isCube = (renderTarget instanceof GLRenderTargetCube);
		int framebuffer;

		if (renderTarget != null ) {

			FastMap<Object> renderTargetProperties = properties.get(renderTarget);

			if (isCube) {

				framebuffer = ((Integer[]) renderTargetProperties.get("__webglFramebuffer"))[((GLRenderTargetCube)renderTarget).activeCubeFace];

			} else {

				framebuffer = (int) renderTargetProperties.get("__webglFramebuffer");

			}

			_currentScissor.copy(renderTarget.scissor);
			_currentScissorTest = renderTarget.scissorTest;

			_currentViewport.copy(renderTarget.viewport);

		} else {

			framebuffer = 0;

			_currentScissor.copy(_scissor).multiplyScalar(_pixelRatio);
			_currentScissorTest = _scissorTest;

			_currentViewport.copy(_viewport).multiplyScalar(_pixelRatio);

		}

		if (_currentFramebuffer != framebuffer) {

			gl.glBindFramebuffer( GL20.GL_FRAMEBUFFER, framebuffer);
			_currentFramebuffer = framebuffer;

		}

		state.scissor(_currentScissor);
		state.setScissorTest(_currentScissorTest);

		state.viewport(_currentViewport);

		if (isCube) {

			FastMap<Object> textureProperties = properties.get(renderTarget.texture);
			gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER,
                    GL20.GL_COLOR_ATTACHMENT0,
                    GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_X + ((GLRenderTargetCube)renderTarget).activeCubeFace,
                    (Integer) textureProperties.get("__webglTexture"),
                    ((GLRenderTargetCube)renderTarget).activeMipMapLevel);

		}

	}

	public void readRenderTargetPixels(GLRenderTarget renderTarget, int x, int y, int width, int height, Buffer buffer)
    {

		if (properties.get(renderTarget).containsKey("__webglFramebuffer")) {

			int framebuffer = (int) properties.get(renderTarget).get("__webglFramebuffer");

			boolean restore = false;

			if (framebuffer != _currentFramebuffer) {

				gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebuffer);

				restore = true;

			}

			try {

				Texture texture = renderTarget.texture;

				if (texture.getFormat() != PixelFormat.RGBA
						&& texture.getFormat().getValue() != GLHelpers.getParameter(gl, GL20.GL_IMPLEMENTATION_COLOR_READ_FORMAT)) {

					Log.error("GLRenderer.readRenderTargetPixels: renderTarget is not in RGBA or implementation defined format.");
					return;

				}

				// TODO: Fix Extensions
				if (texture.getType() != PixelType.UNSIGNED_BYTE
						&& PixelType.UNSIGNED_BYTE.getValue() != GLHelpers.getParameter(gl, GL20.GL_IMPLEMENTATION_COLOR_READ_TYPE))
//					&& ! ( texture.getType() == PixelType.FLOAT && extensions.get( 'WEBGL_color_buffer_float' ) )
//					&& ! ( texture.getType() == THREE.HalfFloatType && extensions.get( 'EXT_color_buffer_half_float' ) ) )
				{

					Log.error("GLRenderer.readRenderTargetPixels: renderTarget is not in UnsignedByteType or implementation defined type.");
					return;

				}

				if (gl.glCheckFramebufferStatus(GL20.GL_FRAMEBUFFER) == GL20.GL_FRAMEBUFFER_COMPLETE) {

					gl.glReadPixels(x, y, width, height, texture.getFormat().getValue(), texture.getType().getValue(), buffer);

				} else {

					Log.error("GLRenderer.readRenderTargetPixels: readPixels from renderTarget failed. Framebuffer not complete.");

				}

			} finally {

				if (restore) {

					gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, _currentFramebuffer);

				}

			}

		}

	}

	private void updateRenderTargetMipmap( GLRenderTarget renderTarget ) {

		TextureTarget target = renderTarget instanceof GLRenderTargetCube ? TextureTarget.TEXTURE_CUBE_MAP : TextureTarget.TEXTURE_2D;
		int texture = (int) properties.get( renderTarget.texture ).get("__webglTexture");

		state.bindTexture( target, texture );
		gl.glGenerateMipmap( target.getValue() );
		state.bindTexture( target, 0 );

	}

	/**
	 * Fallback filters for non-power-of-2 textures
	 * @param f
	 * @return
     */
	int filterFallback ( int f )
	{
		if(f == GL20.GL_NEAREST || f == GL20.GL_NEAREST_MIPMAP_NEAREST || f == GL20.GL_NEAREST_MIPMAP_LINEAR)
			return GL20.GL_NEAREST;

		return GL20.GL_LINEAR;
	}
}
