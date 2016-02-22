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

package org.parallax3d.parallax.graphics.renderers.shaders;

import java.util.Arrays;
import java.util.List;

import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;

/**
 * Simple depth shader.
 * <p>
 * Based on the three.js code.
 * 
 * @author thothbot
 *
 */
public final class DepthShader extends Shader
{

	interface Resources extends DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("source/depth.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("source/depth.fs.glsl")
		SourceTextResource getFragmentShader();

	}

	public DepthShader()
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("mNear", new Uniform(Uniform.TYPE.F, 1.0 ));
		this.addUniform("mFar", new Uniform(Uniform.TYPE.F, 2000.0 ));
		this.addUniform("opacity", new Uniform(Uniform.TYPE.F, 1.0 ));
	}
	
	@Override
	protected void updateVertexSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksVertexShader.MORPHTARGET_PARS,
			ChunksVertexShader.LOGDEPTHBUF_PAR
		);
		
		List<String> main1 = Arrays.asList(
			ChunksVertexShader.MORPHTARGET,
			ChunksVertexShader.DEFAULT,
			ChunksVertexShader.LOGDEPTHBUF
		);

		super.updateVertexSource(updateShaderSource(src, vars, main1));
	}
	
	@Override
	protected void updateFragmentSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksFragmentShader.LOGDEPTHBUF_PAR
		);
			
		List<String> main = Arrays.asList(
			ChunksFragmentShader.LOGDEPTHBUF
		);
			
		super.updateFragmentSource(updateShaderSource(src, vars, main));
	}
}
