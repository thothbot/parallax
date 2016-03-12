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

import org.parallax3d.parallax.graphics.renderers.GLRendererInfo;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.GLES20Ext;
import org.parallax3d.parallax.system.gl.arrays.Uint32Array;
import org.parallax3d.parallax.system.gl.enums.DataType;

public class GLIndexedBufferRenderer extends BufferRenderer {

    GL20 gl;
    GLRendererInfo info;

    DataType type;
    int size;

    public GLIndexedBufferRenderer(GL20 gl, GLRendererInfo info) {
        this.gl = gl;
        this.info = info;
    }

    public void setIndex( AttributeData index ) {

        if ( index.getArray() instanceof Uint32Array && GLExtensions.check( gl, GLES20Ext.List.OES_element_index_uint ) ) {

            type = DataType.UNSIGNED_INT;
            size = 4;

        } else {

            type = DataType.UNSIGNED_SHORT;
            size = 2;

        }

    }
}
