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

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.core.InterleavedBufferAttribute;
import org.parallax3d.parallax.graphics.renderers.GLRendererInfo;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.enums.BeginMode;

public class GLBufferRenderer extends BufferRenderer {

    public GLBufferRenderer(GL20 gl, GLRendererInfo info) {
        super(gl, info);
    }

    @Override
    public void render( int start, int count ) {

        _gl.glDrawArrays( mode.getValue(), start, count );

        _infoRender.calls ++;
        _infoRender.vertices += count;
        if ( mode == BeginMode.TRIANGLES ) _infoRender.faces += count / 3;

    }

    public void renderInstances( BufferGeometry geometry ) {

        var extension = extensions.get( "ANGLE_instanced_arrays" );

        if ( extension == null ) {

            Log.error( "GLBufferRenderer: using InstancedBufferGeometry but hardware does not support extension ANGLE_instanced_arrays." );
            return;

        }

        BufferAttribute position = geometry.getAttributes().get("position");

        int count = 0;

        if ( position instanceof InterleavedBufferAttribute) {

            count = position.data.count;

            extension.drawArraysInstancedANGLE( mode, 0, count, geometry.maxInstancedCount );

        } else {

            count = position.count;

            extension.drawArraysInstancedANGLE( mode, 0, count, geometry.maxInstancedCount );

        }

        _infoRender.calls ++;
        _infoRender.vertices += count * geometry.maxInstancedCount;
        if ( mode == BeginMode.TRIANGLES ) _infoRender.faces += geometry.maxInstancedCount * count / 3;

    }
}
