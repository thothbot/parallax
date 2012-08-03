/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.client.renderers;

import thothbot.parallax.core.client.gl2.WebGLProgram;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.WebGLShader;
import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.scenes.Scene;

public abstract class Plugin 
{
	protected WebGLRenderer renderer;
	
	public abstract void init(WebGLRenderer webGLRenderer);
	
	public abstract void render( Scene scene, Camera camera, int currentWidth, int currentHeight );
	
	protected WebGLProgram createProgram ( Shader shader ) 
	{
		WebGLRenderingContext gl = this.renderer.getGL();
		WebGLProgram program = gl.createProgram();

		WebGLShader fragmentShader = gl.createShader( GLenum.FRAGMENT_SHADER.getValue() );
		WebGLShader vertexShader = gl.createShader( GLenum.VERTEX_SHADER.getValue() );

		gl.shaderSource( fragmentShader, shader.getFragmentSource() );
		gl.shaderSource( vertexShader, shader.getVertexSource() );

		gl.compileShader( fragmentShader );
		gl.compileShader( vertexShader );

		gl.attachShader( program, fragmentShader );
		gl.attachShader( program, vertexShader );

		gl.linkProgram( program );
		
		if (!gl.getProgramParameterb(program, GLenum.LINK_STATUS.getValue()))
			Log.error("Could not initialise shader\n"
					+ "GL error: " + gl.getProgramInfoLog(program)
					+ "\n-----\nVERTEX:\n" + shader.getVertexSource()
					+ "\n-----\nFRAGMENT:\n" + shader.getFragmentSource()
			);

		else
			Log.info("initProgram(): shaders has been initialised");

		return program;
	}
}
