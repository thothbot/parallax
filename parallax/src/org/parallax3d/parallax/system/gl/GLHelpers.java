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
package org.parallax3d.parallax.system.gl;

import org.parallax3d.parallax.system.gl.arrays.Int32Array;

import java.nio.IntBuffer;

public class GLHelpers {

    public static int getParameter(GL20 gl, int param)
    {
        IntBuffer buffer = Int32Array.create(3).getTypedBuffer();
        gl.glGetIntegerv(param, buffer);
        return buffer.get(0);
    }

    public static boolean isShaderCompiled(GL20 gl, int shader)
    {
        IntBuffer buffer = Int32Array.create(3).getTypedBuffer();
        gl.glGetShaderiv( shader, GL20.GL_COMPILE_STATUS, buffer );
        return buffer.get(0) > 0;
    }

}
