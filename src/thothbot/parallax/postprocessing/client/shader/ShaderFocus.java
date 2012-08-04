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
 * Focus shader
 * <p>
 * Based on three.js code<br>
 * Based on PaintEffect postprocess from ro.me <a href="http://code.google.com/p/3-dreams-of-black/source/browse/deploy/js/effects/PaintEffect.js">code.google.com/p/3-dreams-of-black</a>
 * 
 * @author thothbot
 *
 */
public final class ShaderFocus extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("source/defaultUv.vs")
		TextResource getVertexShader();

		@Source("source/focus.fs")
		TextResource getFragmentShader();
	}

	public ShaderFocus()
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T, 0));
		this.addUniform("screenWidth", new Uniform(Uniform.TYPE.F, 1024));
		this.addUniform("screenHeight", new Uniform(Uniform.TYPE.F, 1024));
		this.addUniform("sampleDistance", new Uniform(Uniform.TYPE.F, 0.94));
		this.addUniform("waveFactor", new Uniform(Uniform.TYPE.F, 0.00125));
	}
}
