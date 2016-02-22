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

/**
 * Normal map shader
 * <p>
 * Based on three.js code
 * <p>
 * compute normals from heightmap
 * 
 * @author thothbot
 *
 */
public final class NormalMapShader extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);
		
		@Source("source/defaultUv.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("source/normalmap.fs.glsl")
		SourceTextResource getFragmentShader();
	}

	public NormalMapShader()
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("heightMap", new Uniform(Uniform.TYPE.T));
		this.addUniform("resolution", new Uniform(Uniform.TYPE.V2, new Vector2( 512, 512 )));
		this.addUniform("scale", new Uniform(Uniform.TYPE.V2, new Vector2( 1, 1 )));
		this.addUniform("height", new Uniform(Uniform.TYPE.F, 0.05));
	}
}
