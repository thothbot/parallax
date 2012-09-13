/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
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
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.plugin.postprocessing.client.shader;

import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

/**
 * Depth-of-field shader with bokeh
 * <p>
 * Based on three.js code<br>
 * Ported from GLSL shader by Martins Upitis <a href="http://artmartinsh.blogspot.com/2010/02/glsl-lens-blur-filter-with-bokeh.html">artmartinsh.blogspot.com</a>
 *
 * @author thothbot
 *
 */
public final class ShaderBokeh extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("source/defaultUv.vs")
		TextResource getVertexShader();

		@Source("source/bokeh.fs")
		TextResource getFragmentShader();
	}

	public ShaderBokeh() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tColor", new Uniform(Uniform.TYPE.T, 0));
		this.addUniform("tDepth", new Uniform(Uniform.TYPE.T, 1));
		this.addUniform("focus", new Uniform(Uniform.TYPE.F, 1.0));
		this.addUniform("aspect", new Uniform(Uniform.TYPE.F, 1.0));
		this.addUniform("aperture", new Uniform(Uniform.TYPE.F, 0.025));
		this.addUniform("maxblur", new Uniform(Uniform.TYPE.I, 1.0));
	}

}
