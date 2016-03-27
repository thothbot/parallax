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

import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.GLES20Ext;
import org.parallax3d.parallax.system.gl.GLHelpers;
import org.parallax3d.parallax.system.gl.enums.ShaderPrecisionSpecifiedTypes;
import org.parallax3d.parallax.system.gl.enums.Shaders;

@ThreejsObject("THREE.WebGLCapabilities")
public class GLCapabilities {

    GL20 gl;

    Shader.PRECISION precision;

    boolean logarithmicDepthBuffer;

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

        this.maxTextures         = GLHelpers.getParameter(gl, GL20.GL_MAX_TEXTURE_IMAGE_UNITS );
        this.maxVertexTextures   = GLHelpers.getParameter(gl, GL20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS );
        this.maxTextureSize      = GLHelpers.getParameter(gl, GL20.GL_MAX_TEXTURE_SIZE );
        this.maxCubemapSize      = GLHelpers.getParameter(gl, GL20.GL_MAX_CUBE_MAP_TEXTURE_SIZE );

        this.maxAttributes       = GLHelpers.getParameter(gl, GL20.GL_MAX_VERTEX_ATTRIBS );
        this.maxVertexUniforms   = GLHelpers.getParameter(gl, GL20.GL_MAX_VERTEX_UNIFORM_VECTORS );
        this.maxVaryings         = GLHelpers.getParameter(gl, GL20.GL_MAX_VARYING_VECTORS );
        this.maxFragmentUniforms = GLHelpers.getParameter(gl, GL20.GL_MAX_FRAGMENT_UNIFORM_VECTORS );

        this.vertexTextures = this.maxVertexTextures > 0;
        this.floatFragmentTextures = GLExtensions.check(gl, GLES20Ext.List.OES_texture_float);
        this.floatVertexTextures = this.vertexTextures && this.floatFragmentTextures;

        this.logarithmicDepthBuffer = GLExtensions.check(gl, GLES20Ext.List.EXT_frag_depth);

        calcPrecision();
    }

    public Shader.PRECISION getPrecision() {
        return this.precision;
    }

    public void setPrecision(Shader.PRECISION precision) {
        this.precision = precision;
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

    private void calcPrecision() {
        GLShaderPrecisionFormat _vertexShaderPrecisionHighpFloat = new
                GLShaderPrecisionFormat(gl, Shaders.VERTEX_SHADER, ShaderPrecisionSpecifiedTypes.HIGH_FLOAT);
        GLShaderPrecisionFormat _vertexShaderPrecisionMediumpFloat =
                new GLShaderPrecisionFormat(gl, Shaders.VERTEX_SHADER, ShaderPrecisionSpecifiedTypes.MEDIUM_FLOAT);
        GLShaderPrecisionFormat _vertexShaderPrecisionLowpFloat = new
                GLShaderPrecisionFormat(gl, Shaders.VERTEX_SHADER, ShaderPrecisionSpecifiedTypes.LOW_FLOAT);

        GLShaderPrecisionFormat _fragmentShaderPrecisionHighpFloat = new
                GLShaderPrecisionFormat(gl, Shaders.FRAGMENT_SHADER, ShaderPrecisionSpecifiedTypes.HIGH_FLOAT);
        GLShaderPrecisionFormat _fragmentShaderPrecisionMediumpFloat =
                new GLShaderPrecisionFormat(gl, Shaders.FRAGMENT_SHADER, ShaderPrecisionSpecifiedTypes.MEDIUM_FLOAT);
        GLShaderPrecisionFormat _fragmentShaderPrecisionLowpFloat = new
                GLShaderPrecisionFormat(gl, Shaders.FRAGMENT_SHADER, ShaderPrecisionSpecifiedTypes.LOW_FLOAT);

        if(_vertexShaderPrecisionHighpFloat.getPrecision() > 0 &&
                _fragmentShaderPrecisionHighpFloat.getPrecision() > 0)
            this.precision = Shader.PRECISION.HIGHP;
        else if(_vertexShaderPrecisionMediumpFloat.getPrecision() > 0 &&
                _fragmentShaderPrecisionMediumpFloat.getPrecision() > 0)
            this.precision = Shader.PRECISION.MEDIUMP;
        else
            this.precision = Shader.PRECISION.LOWP;
    }
}
