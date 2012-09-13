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

package thothbot.parallax.plugin.postprocessing.client.shaders;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.core.Vector2;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

/**
 * Convolution shader
 * <p>
 * Based on three.js code<b>
 * Ported from o3d sample to WebGL / GLSL <a href="http://o3d.googlecode.com/svn/trunk/samples/convolution.html">o3d.googlecode.com</a>
 * 
 * @author thothbot
 *
 */
public final class ShaderConvolution extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("source/convolution.vs")
		TextResource getVertexShader();

		@Source("source/convolution.fs")
		TextResource getFragmentShader();
	}

	public ShaderConvolution() 
	{
		super(Resources.INSTANCE);
	}
	
	@Override
	protected void initUniforms()
	{
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T, 0));
		this.addUniform("uImageIncrement", new Uniform(Uniform.TYPE.V2, new Vector2(0.001953125, 0.0)));
		this.addUniform("cKernel", new Uniform(Uniform.TYPE.FV1, Float32Array.createArray()));
	}
}
