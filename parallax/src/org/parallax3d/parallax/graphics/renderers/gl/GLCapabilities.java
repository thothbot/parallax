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

import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.arrays.Int32Array;

import java.nio.IntBuffer;

@ThreejsObject("THREE.WebGLCapabilities")
public class GLCapabilities {

    GL20 gl;

    boolean logarithmicDepthBuffer = false;

    int maxTextures;
    int maxVertexTextures;
    int maxTextureSize;
    int maxCubemapSize;

    int maxAttributes;
    int maxVertexUniforms;
    int maxVaryings;
    int maxFragmentUniforms;

    boolean vertexTextures;
    boolean floatFragmentTextures;
    boolean floatVertexTextures;

    public GLCapabilities(GL20 gl) {
        this.gl = gl;

        this.maxTextures = getIntGlParam( GL20.GL_MAX_TEXTURE_IMAGE_UNITS );
        this.maxVertexTextures = getIntGlParam( GL20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS );
        this.maxTextureSize = getIntGlParam( GL20.GL_MAX_TEXTURE_SIZE );
        this.maxCubemapSize = getIntGlParam( GL20.GL_MAX_CUBE_MAP_TEXTURE_SIZE );

        this.maxAttributes = getIntGlParam( GL20.GL_MAX_VERTEX_ATTRIBS );
        this.maxVertexUniforms = getIntGlParam( GL20.GL_MAX_VERTEX_UNIFORM_VECTORS );
        this.maxVaryings = getIntGlParam( GL20.GL_MAX_VARYING_VECTORS );
        this.maxFragmentUniforms = getIntGlParam( GL20.GL_MAX_FRAGMENT_UNIFORM_VECTORS );

        this.vertexTextures = this.maxVertexTextures > 0;
        this.floatFragmentTextures = !! extensions.get( 'OES_texture_float' );
        this.floatVertexTextures = this.vertexTextures && this.floatFragmentTextures;

    }

    public boolean isLogarithmicDepthBuffer() {
        return logarithmicDepthBuffer;
    }

    public int getMaxTextures() {
        return maxTextures;
    }

    public int getMaxVertexTextures() {
        return maxVertexTextures;
    }

    public int getMaxTextureSize() {
        return maxTextureSize;
    }

    public int getMaxCubemapSize() {
        return maxCubemapSize;
    }

    public int getMaxAttributes() {
        return maxAttributes;
    }

    public int getMaxVertexUniforms() {
        return maxVertexUniforms;
    }

    public int getMaxVaryings() {
        return maxVaryings;
    }

    public int getMaxFragmentUniforms() {
        return maxFragmentUniforms;
    }

    public boolean isVertexTextures() {
        return vertexTextures;
    }

    public boolean isFloatFragmentTextures() {
        return floatFragmentTextures;
    }

    public boolean isFloatVertexTextures() {
        return floatVertexTextures;
    }

    private int getIntGlParam(int param)
    {
        IntBuffer buffer = Int32Array.create(3).getTypedBuffer();
        this.gl.glGetIntegerv(param, buffer);
        return buffer.get(0);
    }
}
