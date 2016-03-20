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

import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;

import java.util.Arrays;
import java.util.List;

public class DashedShader extends Shader 
{

	interface Resources extends DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("source/linedashed_vert.glsl")
		SourceTextResource getVertexShader();

		@Source("source/linedashed_frag.glsl")
		SourceTextResource getFragmentShader();
	}

	public DashedShader()
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.setUniforms(UniformsLib.common());
		this.setUniforms(UniformsLib.fog());
		this.addUniform("scale",     new Uniform(Uniform.TYPE.F, 1.0 ));
		this.addUniform("dashSize",  new Uniform(Uniform.TYPE.F, 1.0 ));
		this.addUniform("totalSize", new Uniform(Uniform.TYPE.F, 2.0 ));
	}
}
