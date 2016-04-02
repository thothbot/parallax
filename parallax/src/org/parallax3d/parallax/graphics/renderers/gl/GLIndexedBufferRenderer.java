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

import org.parallax3d.parallax.graphics.core.AttributeData;
import org.parallax3d.parallax.graphics.core.InstancedBufferGeometry;
import org.parallax3d.parallax.graphics.renderers.GLRendererInfo;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.GLES20Ext;
import org.parallax3d.parallax.system.gl.arrays.Uint32Array;
import org.parallax3d.parallax.system.gl.enums.BeginMode;
import org.parallax3d.parallax.system.gl.enums.DataType;

public class GLIndexedBufferRenderer extends BufferRenderer {

    DataType type;
    int size;

    public GLIndexedBufferRenderer(GL20 gl, GLRendererInfo info)
    {
        super(gl, info);
    }

    public void setIndex( AttributeData index ) {

        if ( index.getArray() instanceof Uint32Array && GLExtensions.check( this._gl, GLES20Ext.List.OES_element_index_uint ) ) {

            type = DataType.UNSIGNED_INT;
            size = 4;

        } else {

            type = DataType.UNSIGNED_SHORT;
            size = 2;

        }

    }

    public void render( int start, int count ) {

        _gl.glDrawElements( mode.getValue(), count, type.getValue(), start * size );

        _infoRender.calls ++;
        _infoRender.vertices += count;
        if ( mode == BeginMode.TRIANGLES ) _infoRender.faces += count / 3;

    }

    public void renderInstances( InstancedBufferGeometry geometry, int start, int count)
    {
//        var extension = extensions.get( 'ANGLE_instanced_arrays' );
//
//        if ( extension == null ) {
//
//            Log.error( "GLBufferRenderer: using InstancedBufferGeometry but hardware does not support extension ANGLE_instanced_arrays." );
//            return;
//
//        }
//
//        extension.drawElementsInstancedANGLE( mode, count, type, start * size, geometry.getMaxInstancedCount());
//
//        _infoRender.calls ++;
//        _infoRender.vertices += count * geometry.getMaxInstancedCount();
//        if ( mode == BeginMode.TRIANGLES ) _infoRender.faces += geometry.getMaxInstancedCount() * count / 3;
    }
}
