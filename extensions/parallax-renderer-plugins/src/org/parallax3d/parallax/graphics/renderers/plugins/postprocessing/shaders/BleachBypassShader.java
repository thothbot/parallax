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

import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;

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
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("source/defaultUv.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("source/bleachbypass.fs.glsl")
		SourceTextResource getFragmentShader();
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
