/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
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

package org.parallax3d.parallax.graphics.renderers.gl;

import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.math.Vector4;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.GLES20Ext;
import org.parallax3d.parallax.system.gl.GLHelpers;
import org.parallax3d.parallax.system.gl.arrays.Int32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint8Array;
import org.parallax3d.parallax.system.gl.enums.*;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

@ThreejsObject("THREE.WebGLState")
public class GLState
{
    GL20 gl;

    Vector4 color = new Vector4();

    Uint8Array newAttributes = Uint8Array.create( 16 );
    Uint8Array enabledAttributes = Uint8Array.create( 16 );
    Uint8Array attributeDivisors = Uint8Array.create( 16 );

    Map<Integer, Boolean> capabilities = new HashMap<>();

    int[] compressedTextureFormats = null;

    Material.BLENDING currentBlending = null;
    BlendEquationMode currentBlendEquation = null;
    BlendingFactorSrc currentBlendSrc = null;
    BlendingFactorDest currentBlendDst = null;
    BlendEquationMode currentBlendEquationAlpha = null;
    BlendingFactorSrc currentBlendSrcAlpha = null;
    BlendingFactorDest currentBlendDstAlpha = null;

    DepthFunction currentDepthFunc = null;
    Boolean currentDepthWrite = null;

    Boolean currentColorWrite = null;

    Integer currentStencilWrite = null;
    Integer currentStencilFunc = null;
    Integer currentStencilRef = null;
    Integer currentStencilMask = null;
    Integer currentStencilFail  = null;
    Integer currentStencilZFail = null;
    Integer currentStencilZPass = null;

    Boolean currentFlipSided = null;

    Double currentLineWidth = null;

    Double currentPolygonOffsetFactor = null;
    Double currentPolygonOffsetUnits = null;

    Boolean currentScissorTest = null;

    int maxTextures;

    Integer currentTextureSlot = null;

    private class BoundTexture {
        public TextureTarget type;
        public int texture;
    }

    Map<Integer, BoundTexture> currentBoundTextures = new HashMap<>();

    Vector4 currentClearColor = new Vector4();
    Double currentClearDepth = null;
    Integer currentClearStencil = null;

    Vector4 currentScissor = new Vector4();
    Vector4 currentViewport = new Vector4();

    public GLState(GL20 gl) {
        this.gl = gl;

        this.maxTextures = GLHelpers.getParameter(gl, GL20.GL_MAX_TEXTURE_IMAGE_UNITS);
    }

    public void init() {

        this.clearColor( 0, 0, 0, 1 );
        this.clearDepth( 1 );
        this.clearStencil( 0 );

        this.gl.glEnable(EnableCap.DEPTH_TEST.getValue());
        this.gl.glDepthFunc(DepthFunction.LEQUAL.getValue());

        this.gl.glFrontFace(FrontFaceDirection.CCW.getValue());
        this.gl.glCullFace(CullFaceMode.BACK.getValue());
        this.gl.glEnable(EnableCap.CULL_FACE.getValue());

        this.gl.glEnable(EnableCap.BLEND.getValue());
        this.gl.glBlendEquation(BlendEquationMode.FUNC_ADD.getValue());
        this.gl.glBlendFunc(BlendingFactorSrc.SRC_ALPHA.getValue(), BlendingFactorDest.ONE_MINUS_SRC_ALPHA.getValue());
    }

    public void initAttributes() {

        for ( int i = 0, l = newAttributes.getLength(); i < l; i ++ ) {

            newAttributes.set( i , 0 );

        }

    }

    public void enableAttribute( int attribute ) {

        newAttributes.set( attribute , 1);

        if ( enabledAttributes.get( attribute ) == 0 ) {

            this.gl.glEnableVertexAttribArray(attribute);
            enabledAttributes.set( attribute , 1);

        }

        if ( attributeDivisors.get( attribute ) != 0 ) {

//            var extension = extensions.get( 'ANGLE_instanced_arrays' );
//
//            extension.vertexAttribDivisorANGLE( attribute, 0 );
            attributeDivisors.set( attribute , 0);

        }

    }

    public void enableAttributeAndDivisor( int attribute, int meshPerAttribute ) {

        newAttributes.set( attribute , 1);

        if ( enabledAttributes.get( attribute ) == 0 ) {

            this.gl.glEnableVertexAttribArray(attribute);
            enabledAttributes.set( attribute , 1);

        }

        if ( attributeDivisors.get( attribute ) != meshPerAttribute ) {

//            extension.vertexAttribDivisorANGLE( attribute, meshPerAttribute );
            attributeDivisors.set( attribute , meshPerAttribute);

        }

    }

    public void disableUnusedAttributes() {

        for ( int i = 0, l = enabledAttributes.getLength(); i < l; i ++ ) {

            if ( enabledAttributes.get( i ) != newAttributes.get( i ) ) {

                this.gl.glDisableVertexAttribArray(i);
                enabledAttributes.set( i , 0 );

            }

        }

    }

    public void enable( EnableCap cap )
    {
        int id = cap.getValue();
        if ( !capabilities.containsKey(id) || !capabilities.get( id )) {

            this.gl.glEnable( id );
            capabilities.put( id , true);

        }

    }

    public void disable( EnableCap cap )
    {
        int id = cap.getValue();
        if ( capabilities.containsKey(id) && capabilities.get( id )) {

            this.gl.glDisable( id );
            capabilities.put( id , false);

        }

    }

    public int[] getCompressedTextureFormats() {

        if ( compressedTextureFormats == null ) {

            int formatCount = GLHelpers.getParameter(gl,GL20.GL_NUM_COMPRESSED_TEXTURE_FORMATS);
            compressedTextureFormats = new int[formatCount];

            if(GLExtensions.check(gl, GLES20Ext.List.WEBGL_compressed_texture_pvrtc)
                || GLExtensions.check(gl, GLES20Ext.List.WEBGL_compressed_texture_s3tc)
                || GLExtensions.check(gl, GLES20Ext.List.WEBGL_compressed_texture_etc1))
            {
                IntBuffer buffer = Int32Array.create(4 * formatCount).getTypedBuffer();
                gl.glGetIntegerv( gl.GL_COMPRESSED_TEXTURE_FORMATS, buffer );

                compressedTextureFormats = buffer.array();

            }

        }

        return compressedTextureFormats;

    }

    public void setBlending( Material.BLENDING blending)
    {
        setBlending(blending, null, null, null, null, null, null);
    }

    public void setBlending( Material.BLENDING blending, BlendEquationMode blendEquation, BlendingFactorSrc blendSrc,
                             BlendingFactorDest blendDst, BlendEquationMode blendEquationAlpha,
                             BlendingFactorSrc blendSrcAlpha, BlendingFactorDest blendDstAlpha ) {

        if ( blending == Material.BLENDING.NO ) {

            this.gl.glDisable(EnableCap.BLEND.getValue());

        } else {

            this.gl.glEnable(EnableCap.BLEND.getValue());

        }

        if ( blending != currentBlending ) {

            if ( blending == Material.BLENDING.ADDITIVE ) {

                this.gl.glBlendEquation(BlendEquationMode.FUNC_ADD.getValue());
                this.gl.glBlendFunc(BlendingFactorSrc.SRC_ALPHA.getValue(), BlendingFactorDest.ONE.getValue());

            } else if ( blending == Material.BLENDING.SUBTRACTIVE ) {

                // TODO: Find blendFuncSeparate() combination
                this.gl.glBlendEquation(BlendEquationMode.FUNC_ADD.getValue());
                this.gl.glBlendFunc(BlendingFactorSrc.ZERO.getValue(), BlendingFactorDest.ONE_MINUS_SRC_COLOR.getValue());

            } else if ( blending == Material.BLENDING.MULTIPLY ) {

                // TODO: Find blendFuncSeparate() combination

                this.gl.glBlendEquation(BlendEquationMode.FUNC_ADD.getValue());
                this.gl.glBlendFunc(BlendingFactorSrc.ZERO.getValue(), BlendingFactorDest.SRC_COLOR.getValue());

            } else {

                this.gl.glBlendEquationSeparate(BlendEquationMode.FUNC_ADD.getValue(), BlendEquationMode.FUNC_ADD.getValue());
                this.gl.glBlendFuncSeparate(BlendingFactorSrc.SRC_ALPHA.getValue(),
                        BlendingFactorDest.ONE_MINUS_SRC_ALPHA.getValue(),
                        BlendingFactorSrc.ONE.getValue(),
                        BlendingFactorDest.ONE_MINUS_SRC_ALPHA.getValue());

            }

            currentBlending = blending;

        }

        if ( blending == Material.BLENDING.CUSTOM ) {

            blendEquationAlpha = blendEquationAlpha != null ? blendEquationAlpha : blendEquation;
            blendSrcAlpha = blendSrcAlpha != null ? blendSrcAlpha : blendSrc;
            blendDstAlpha = blendDstAlpha != null ? blendDstAlpha : blendDst;

            if ( blendEquation != currentBlendEquation || blendEquationAlpha != currentBlendEquationAlpha ) {

                this.gl.glBlendEquationSeparate( blendEquation.getValue(), blendEquationAlpha.getValue());

                currentBlendEquation = blendEquation;
                currentBlendEquationAlpha = blendEquationAlpha;

            }

            if ( blendSrc != currentBlendSrc || blendDst != currentBlendDst || blendSrcAlpha != currentBlendSrcAlpha || blendDstAlpha != currentBlendDstAlpha ) {

                this.gl.glBlendFuncSeparate( blendSrc.getValue(),
                        blendDst.getValue(),
                        blendSrcAlpha.getValue(),
                        blendDstAlpha.getValue());

                currentBlendSrc = blendSrc;
                currentBlendDst = blendDst;
                currentBlendSrcAlpha = blendSrcAlpha;
                currentBlendDstAlpha = blendDstAlpha;

            }

        } else {

            currentBlendEquation = null;
            currentBlendSrc = null;
            currentBlendDst = null;
            currentBlendEquationAlpha = null;
            currentBlendSrcAlpha = null;
            currentBlendDstAlpha = null;

        }

    }

    public void setDepthFunc( DepthFunction depthFunc ) {

        if ( currentDepthFunc != depthFunc ) {

            if ( depthFunc != null ) {

                this.gl.glDepthFunc(depthFunc.getValue());

            } else {

                this.gl.glDepthFunc(DepthFunction.LEQUAL.getValue());

            }

            currentDepthFunc = depthFunc;

        }

    }

    public void setDepthTest( boolean depthTest ) {

        if ( depthTest )

            this.enable(EnableCap.DEPTH_TEST);

        else

            this.disable(EnableCap.DEPTH_TEST);

    }

    public void setDepthWrite( boolean depthWrite ) {

        // TODO: Rename to setDepthMask

        if ( currentDepthWrite != depthWrite ) {

            this.gl.glDepthMask(depthWrite);
            currentDepthWrite = depthWrite;

        }

    }

    public void setColorWrite( boolean colorWrite ) {

        // TODO: Rename to setColorMask

        if ( currentColorWrite != colorWrite ) {

            gl.glColorMask( colorWrite, colorWrite, colorWrite, colorWrite );
            currentColorWrite = colorWrite;

        }

    }

    public void setStencilFunc( int stencilFunc, int stencilRef, int stencilMask ) {

        if ( currentStencilFunc != stencilFunc ||
                currentStencilRef != stencilRef 	||
                currentStencilMask != stencilMask ) {

            gl.glStencilFunc( stencilFunc, stencilRef, stencilMask );

            currentStencilFunc = stencilFunc;
            currentStencilRef  = stencilRef;
            currentStencilMask = stencilMask;

        }

    }

    public void setStencilOp( int stencilFail, int stencilZFail, int stencilZPass ) {

        if ( currentStencilFail	 != stencilFail 	||
                currentStencilZFail != stencilZFail ||
                currentStencilZPass != stencilZPass ) {

            gl.glStencilOp( stencilFail,  stencilZFail, stencilZPass );

            currentStencilFail  = stencilFail;
            currentStencilZFail = stencilZFail;
            currentStencilZPass = stencilZPass;

        }

    }

    public void setStencilTest( boolean stencilTest ) {

        if ( stencilTest )

            this.enable(EnableCap.STENCIL_TEST);

        else

            this.disable(EnableCap.STENCIL_TEST);

    }

    public void setStencilWrite( int stencilWrite ) {

        // TODO: Rename to setStencilMask

        if ( currentStencilWrite != stencilWrite ) {

            gl.glStencilMask( stencilWrite );
            currentStencilWrite = stencilWrite;

        }

    }

    public void setFlipSided( boolean flipSided ) {

        if ( currentFlipSided != flipSided ) {

            if ( flipSided )
                this.gl.glFrontFace(FrontFaceDirection.CW.getValue());
            else
                this.gl.glFrontFace(FrontFaceDirection.CCW.getValue());

            currentFlipSided = flipSided;

        }

    }

    public void setLineWidth( double width ) {

        if ( width != currentLineWidth ) {

            gl.glLineWidth( (float)width );

            currentLineWidth = width;

        }

    }

    public void setPolygonOffset( boolean polygonOffset, double factor, double units ) {

        if ( polygonOffset )
            this.enable(EnableCap.POLYGON_OFFSET_FILL);
        else
            this.disable(EnableCap.POLYGON_OFFSET_FILL);

        if ( polygonOffset && ( currentPolygonOffsetFactor != factor || currentPolygonOffsetUnits != units ) ) {

            this.gl.glPolygonOffset((float)factor, (float)units);

            currentPolygonOffsetFactor = factor;
            currentPolygonOffsetUnits = units;

        }

    }

    public Boolean getScissorTest() {

        return currentScissorTest;

    }

    public void setScissorTest( boolean scissorTest ) {

        currentScissorTest = scissorTest;

        if ( scissorTest ) {

            this.enable( EnableCap.SCISSOR_TEST );

        } else {

            this.disable( EnableCap.SCISSOR_TEST );

        }

    }

    // texture

    public void activeTexture() {

        activeTexture(TextureUnit.TEXTURE0.getValue() + maxTextures - 1);

    }

    public void activeTexture( int webglSlot ) {

        if ( currentTextureSlot != webglSlot ) {

            this.gl.glActiveTexture( webglSlot );
            currentTextureSlot = webglSlot;

        }

    }

    public void bindTexture( TextureTarget webglType, int webglTexture ) {

        if ( currentTextureSlot == null ) {

            activeTexture();

        }

        if ( !currentBoundTextures.containsKey( currentTextureSlot ) ) {

            currentBoundTextures.put( currentTextureSlot , new BoundTexture());

        }

        BoundTexture boundTexture = currentBoundTextures.get( currentTextureSlot );

        if ( boundTexture.type != webglType || boundTexture.texture != webglTexture ) {

            gl.glBindTexture( webglType.getValue(), webglTexture );

            boundTexture.type = webglType;
            boundTexture.texture = webglTexture;

        }

    }

    // clear values

    public void clearColor( double r, double g, double b, double a ) {

        color.set( r, g, b, a );

        if ( !currentClearColor.equals( color )) {

            this.gl.glClearColor((float) r, (float) g, (float) b, (float) a);
            currentClearColor.copy( color );

        }

    }

    public void clearDepth( double depth ) {

        if ( currentClearDepth != depth ) {

            gl.glClearDepthf((float) depth );
            currentClearDepth = depth;

        }

    }

    public void clearStencil ( int stencil ) {

        if ( currentClearStencil != stencil ) {

            gl.glClearStencil( stencil );
            currentClearStencil = stencil;

        }

    }

    public void scissor( Vector4 scissor ) {

        if ( !currentScissor.equals( scissor )) {

            gl.glScissor( (int)scissor.getX(), (int)scissor.getY(), (int)scissor.getZ(), (int)scissor.getW() );
            currentScissor.copy( scissor );

        }

    }

    public void viewport( Vector4 viewport ) {

        if ( !currentViewport.equals( viewport )) {

            gl.glViewport( (int)viewport.getX(), (int)viewport.getY(), (int)viewport.getZ(), (int)viewport.getW() );
            currentViewport.copy( viewport );

        }

    }

    //

    public void reset() {

        for ( int i = 0; i < enabledAttributes.getLength(); i ++ ) {

            if ( enabledAttributes.get( i ) == 1 ) {

                gl.glDisableVertexAttribArray( i );
                enabledAttributes.set( i , 0);

            }

        }

        capabilities = new HashMap<>();

        compressedTextureFormats = null;

        currentBlending = null;

        currentColorWrite = null;
        currentDepthWrite = null;
        currentStencilWrite = null;

        currentFlipSided = null;

    }
}
