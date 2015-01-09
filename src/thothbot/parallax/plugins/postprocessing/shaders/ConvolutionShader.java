/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package thothbot.parallax.plugins.postprocessing.shaders;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.math.Vector2;

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
public final class ConvolutionShader extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("source/convolution.vs")
		TextResource getVertexShader();

		@Source("source/convolution.fs")
		TextResource getFragmentShader();
	}

	public ConvolutionShader() 
	{
		super(Resources.INSTANCE);
	}
	
	@Override
	protected void initUniforms()
	{
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T ));
		this.addUniform("uImageIncrement", new Uniform(Uniform.TYPE.V2, new Vector2(0.001953125, 0.0)));
		this.addUniform("cKernel", new Uniform(Uniform.TYPE.FV1, Float32Array.createArray()));
	}
}
