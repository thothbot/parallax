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

package thothbot.parallax.postprocessing.client.shader;

import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.Uniform;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

/**
 * Simple fake tilt-shift effect, modulating two pass Gaussian blur (see above) by vertical position
 * <p>
 * Based on three.js code
 * <p>
 * 9 samples per pass<br>
 * standard deviation 2.7<br>
 * "h" and "v" parameters should be set to "1 / width" and "1 / height"<br>
 * "r" parameter control where "focused" horizontal line lies<br>
 * 
 * @author thothbot
 *  
 */
public final class ShaderHorizontalTiltShift extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("source/defaultUv.vs")
		TextResource getVertexShader();

		@Source("source/horizontalTiltShift.fs")
		TextResource getFragmentShader();
	}

	public ShaderHorizontalTiltShift() 
	{
		super(Resources.INSTANCE);
	}
	
	@Override
	protected void initUniforms()
	{
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T, 0));
		this.addUniform("h", new Uniform(Uniform.TYPE.F, 1.0f/512f));
		this.addUniform("r", new Uniform(Uniform.TYPE.F, 0.35f));
	}
}
