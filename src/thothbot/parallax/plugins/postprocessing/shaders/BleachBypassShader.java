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

package thothbot.parallax.plugins.postprocessing.shaders;

import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

/**
 * Bleach bypass shader
 * <a href="http://en.wikipedia.org/wiki/Bleach_bypass">wikipedia.org</a>
 * <p>
 * Based on three.js code <br>
 * Based on Nvidia example <a href="http://developer.download.nvidia.com/shaderlibrary/webpages/shader_library.html#post_bleach_bypass">nvidia.com</a>
 * 
 * @author thothbot
 *
 */
public final class BleachBypassShader extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);

		@Source("source/defaultUv.vs")
		TextResource getVertexShader();

		@Source("source/bleachbypass.fs")
		TextResource getFragmentShader();
	}
	
	public BleachBypassShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T ));
		this.addUniform("opacity", new Uniform(Uniform.TYPE.F, 1.0));
	}

}
