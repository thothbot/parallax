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

package org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.shaders;

import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.math.Vector2;

import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

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
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);
		
		@Source("source/convolution.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("source/convolution.fs.glsl")
		SourceTextResource getFragmentShader();
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
