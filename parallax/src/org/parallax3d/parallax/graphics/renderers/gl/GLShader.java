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
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.GLHelpers;

@ThreejsObject("THREE.WebGLShader")
public class GLShader {

    public static int getShader(GL20 gl, int type, String string) {

        int shader = gl.glCreateShader( type );

        gl.glShaderSource( shader, string );
        gl.glCompileShader( shader );

        if (!GLHelpers.isShaderCompiled(gl, shader)) {

            Log.error( "GLShader: Shader couldn\'t compile." );

        }

        if ( gl.glGetShaderInfoLog( shader ) != "" ) {

            Log.warn( "GLShader: gl.glGetShaderInfoLog(), " + (type == GL20.GL_VERTEX_SHADER ? "vertex" : "fragment") + ": "
                    + gl.glGetShaderInfoLog( shader ) + " = " + addLineNumbers( string ) );

        }

        return shader;
    }

    private static String addLineNumbers( String string ) {

        String[] lines = string.split( "\n" );

        for ( int i = 0; i < lines.length; i ++ ) {

            lines[ i ] = ( i + 1 ) + ": " + lines[ i ];

        }

        return String.join("\n", lines);
    }

}
