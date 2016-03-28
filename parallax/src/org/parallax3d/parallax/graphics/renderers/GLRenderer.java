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
import org.parallax3d.parallax.graphics.core.geometry.GeometryGroup;
import org.parallax3d.parallax.graphics.extras.objects.ImmediateRenderObject;
import org.parallax3d.parallax.graphics.lights.*;
import org.parallax3d.parallax.graphics.materials.*;
import org.parallax3d.parallax.graphics.objects.*;
import org.parallax3d.parallax.graphics.renderers.gl.*;
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

    // Events

    public void dispose() {

        _canvas.removeEventListener( 'webglcontextlost', onContextLost, false );

    };

    private void onContextLost() {

        resetGLState();
        setDefaultGLState();

        properties.clear();

    }

    private void onTextureDispose( Texture texture ) {

        texture.removeEventListener( 'dispose', onTextureDispose );

        deallocateTexture( texture );

        _infoMemory.textures --;


    }

    private void onRenderTargetDispose( GLRenderTarget renderTarget ) {

        renderTarget.removeEventListener( 'dispose', onRenderTargetDispose );

        deallocateRenderTarget( renderTarget );

        _infoMemory.textures --;

    }

    private void onMaterialDispose( Material material ) {

        material.removeEventListener( 'dispose', onMaterialDispose );

        deallocateMaterial( material );

    }

    // Buffer deallocation

    private void deallocateTexture( Texture texture ) {

        var textureProperties = properties.get( texture );

        if ( texture.image && textureProperties.__image__webglTextureCube ) {

            // cube texture

            _gl.deleteTexture( textureProperties.__image__webglTextureCube );

        } else {

            // 2D texture

            if ( textureProperties.__webglInit === undefined ) return;

            _gl.deleteTexture( textureProperties.__webglTexture );

        }

        // remove all webgl properties
        properties.delete( texture );

    }

    private void deallocateRenderTarget( GLRenderTarget renderTarget ) {

        var renderTargetProperties = properties.get( renderTarget );
        var textureProperties = properties.get( renderTarget.texture );

        if ( ! renderTarget || textureProperties.__webglTexture === undefined ) return;

        _gl.deleteTexture( textureProperties.__webglTexture );

        if ( renderTarget instanceof THREE.WebGLRenderTargetCube ) {

            for ( var i = 0; i < 6; i ++ ) {

                _gl.deleteFramebuffer( renderTargetProperties.__webglFramebuffer[ i ] );
                _gl.deleteRenderbuffer( renderTargetProperties.__webglDepthbuffer[ i ] );

            }

        } else {

            _gl.deleteFramebuffer( renderTargetProperties.__webglFramebuffer );
            _gl.deleteRenderbuffer( renderTargetProperties.__webglDepthbuffer );

        }

        properties.delete( renderTarget.texture );
        properties.delete( renderTarget );

    }

    private void deallocateMaterial( Material material ) {

        releaseMaterialProgramReference( material );

        properties.delete( material );

    }


    private void releaseMaterialProgramReference( Material material ) {

        var programInfo = properties.get( material ).program;

        material.program = undefined;

        if ( programInfo !== undefined ) {

            programCache.releaseProgram( programInfo );

        }

    }

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

            uniforms.get( "ambientLightColor" ).setValue( _lights.ambient;
            uniforms.get( "directionalLights" ).setValue( _lights.directional;
            uniforms.get( "spotLights" ).setValue( _lights.spot;
            uniforms.get( "pointLights" ).setValue( _lights.point;
            uniforms.get( "hemisphereLights" ).setValue( _lights.hemi;

            uniforms.get( "directionalShadowMap" ).setValue( _lights.directionalShadowMap;
            uniforms.get( "directionalShadowMatrix" ).setValue( _lights.directionalShadowMatrix;
            uniforms.get( "spotShadowMap" ).setValue( _lights.spotShadowMap;
            uniforms.get( "spotShadowMatrix" ).setValue( _lights.spotShadowMatrix;
            uniforms.get( "pointShadowMap" ).setValue( _lights.pointShadowMap;
            uniforms.get( "pointShadowMatrix" ).setValue( _lights.pointShadowMatrix;

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

        Shader program = (Shader) materialProperties.get("program");
        FastMap<Uniform> p_uniforms = program.getUniforms();
        FastMap<Uniform> m_uniforms = materialProperties.get("__webglShader").uniforms;

        if ( program.getId() != _currentProgram ) {

            gl.glUseProgram(program.getProgram());
            _currentProgram = program.getId();

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

                m_uniforms.get( "mNear" ).setValue( camera.near;
                m_uniforms.get( "mFar" ).setValue( camera.far;
                m_uniforms.get( "opacity" ).setValue( material.opacity;

            } else if ( material instanceof MeshNormalMaterial ) {

                m_uniforms.get( "opacity" ).setValue( material.opacity;

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

    private void updateDynamicUniforms ( FastMap<Uniform> uniforms, GeometryObject object, Camera camera )
    {
        var dynamicUniforms = [];

        for ( int j = 0, jl = uniforms.length; j < jl; j ++ ) {

            var uniform = uniforms[ j ][ 0 ];
            var onUpdateCallback = uniform.onUpdateCallback;

            if ( onUpdateCallback != null ) {

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

        if ( material instanceof HasEmissiveColor)
        {
            ((Color)uniforms.get("emissive").getValue()).copy( ((HasEmissiveColor) material).getEmissive() ).multiply( material.emissiveIntensity );
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

                uvScaleMap = ((GLRenderTarget) uvScaleMap).getTexture();

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

        if (material.getLightMap() != null ) {

            uniforms.get("lightMap").setValue(material.getLightMap());
            uniforms.get("lightMapIntensity").setValue(material.getLightMapIntensity());

        }

        if ( material.getEmissiveMap() != null  ) {

            uniforms.get("emissiveMap").setValue( material.getEmissiveMap() );

        }

    }

    private void refreshUniformsPhong ( FastMap<Uniform> uniforms, MeshPhongMaterial material ) {

        uniforms.get( "specular" ).setValue(material.getSpecular() );
        uniforms.get( "shininess" ).setValue( Math.max(material.getShininess(), 1e-4 )); // to prevent pow( 0.0, 0.0 )

        if (material.getLightMap() != null) {

            uniforms.get( "lightMap" ).setValue(material.getLightMap());
            uniforms.get( "lightMapIntensity" ).setValue(material.getLightMapIntensity());

        }

        if (material.getEmissiveMap() != null) {

            uniforms.get( "emissiveMap" ).setValue(material.getEmissiveMap());

        }

        if (material.getBumpMap() != null) {

            uniforms.get( "bumpMap" ).setValue(material.getBumpMap());
            uniforms.get( "bumpScale" ).setValue(material.getBumpScale());

        }

        if (material.getNormalMap() != null) {

            uniforms.get( "normalMap" ).setValue(material.getNormalMap());
            ((Vector2)uniforms.get("normalScale").getValue()).copy(material.getNormalScale());

        }

        if (material.getDisplacementMap() != null) {

            uniforms.get( "displacementMap" ).setValue(material.getDisplacementMap());
            uniforms.get( "displacementScale" ).setValue(material.getDisplacementScale());
            uniforms.get( "displacementBias" ).setValue(material.getDisplacementBias());

        }

    }
    
    private void refreshUniformsStandard ( FastMap<Uniform> uniforms, MeshStandardMaterial material ) {

        uniforms.get( "roughness" ).setValue( material.getRoughness() );
        uniforms.get( "metalness" ).setValue( material.getMetalness() );

        if (material.getRoughnessMap() != null) {

            uniforms.get( "roughnessMap" ).setValue(material.getRoughnessMap());

        }

        if (material.getMetalnessMap() != null ) {

            uniforms.get( "metalnessMap" ).setValue(material.getMetalnessMap() );

        }

        if (material.getLightMap() != null ) {

            uniforms.get( "lightMap" ).setValue(material.getLightMap() );
            uniforms.get( "lightMapIntensity" ).setValue(material.getLightMapIntensity() );

        }

        if (material.getEmissiveMap() != null) {

            uniforms.get( "emissiveMap" ).setValue(material.getEmissiveMap());

        }

        if (material.getBumpMap() != null) {

            uniforms.get( "bumpMap" ).setValue(material.getBumpMap() );
            uniforms.get( "bumpScale" ).setValue(material.getBumpScale() );

        }

        if (material.getNormalMap() != null ) {

            uniforms.get( "normalMap" ).setValue(material.getNormalMap());
            ((Vector2)uniforms.get("normalScale").getValue()).copy(material.getNormalScale());

        }

        if (material.getDisplacementMap() != null) {

            uniforms.get( "displacementMap" ).setValue(material.getDisplacementMap() );
            uniforms.get( "displacementScale" ).setValue(material.getDisplacementScale());
            uniforms.get( "displacementBias" ).setValue(material.getDisplacementBias());

        }

        if (material.getEnvMap() != null) {

            //uniforms.get( "envMap" ).setValue( material.envMap ); // part of uniforms common
            uniforms.get( "envMapIntensity" ).setValue(material.getEnvMapIntensity() );

        }

    }

    /**
     * If uniforms are marked as clean, they don't need to be loaded to the GPU.
     * @return
     */
    private void markUniformsLightsNeedsUpdate( FastMap<Uniform> uniforms, boolean value )
    {
        uniforms.get("ambientLightColor").setNeedsUpdate(value);

        uniforms.get("directionalLights").setNeedsUpdate(value);
        uniforms.get("pointLights").setNeedsUpdate(value);
        uniforms.get("spotLights").setNeedsUpdate(value);
        uniforms.get("hemisphereLights").setNeedsUpdate(value);

    }

    // Uniforms (load to GPU)

    private void loadUniformsMatrices ( FastMap<Uniform> uniforms, GeometryObject object )
    {
        this.gl.glUniformMatrix4fv(uniforms.get("modelViewMatrix").getLocation(), 1, false, object.getModelViewMatrix().getArray().getTypedBuffer());

        if ( uniforms.containsKey("normalMatrix") )
            this.gl.glUniformMatrix3fv(uniforms.get("normalMatrix").getLocation(), 1, false, object.getNormalMatrix().getArray().getTypedBuffer());
    }

    private int getTextureUnit()
    {
        int textureUnit = _usedTextureUnits;

        if ( textureUnit >= capabilities.getMaxTextures()) {

            Log.warn( "GLRenderer: trying to use " + textureUnit + " texture units while this GPU supports only " + capabilities.getMaxTextures());

        }

        _usedTextureUnits += 1;

        return textureUnit;
    }

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
            var location = uniforms.get( " i ][ 1 ];
            var" ).setValue( uniform.value;

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
