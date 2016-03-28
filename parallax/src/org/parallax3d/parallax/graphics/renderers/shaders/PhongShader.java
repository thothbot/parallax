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

import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;

/**
 * Phong shading - lighting model three-dimensional objects, 
 * including models and polygonal primitives.
 * <p>
 * Based on three.js code.
 * 
 * @author thothbot
 *
 */
public final class PhongShader extends Shader
{

	interface Resources extends DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("source/meshphong_vert.glsl")
		SourceTextResource getVertexShader();

		@Source("source/meshphong_frag.glsl")
		SourceTextResource getFragmentShader();
	}

	public PhongShader()
	{
		super(Resources.INSTANCE);
	}
	@Override
	protected void initUniforms()
	{
		this.setUniforms(UniformsLib.common());
		this.setUniforms(UniformsLib.aomap());
		this.setUniforms(UniformsLib.lightmap());
		this.setUniforms(UniformsLib.emissivemap());
		this.setUniforms(UniformsLib.bumpmap());
		this.setUniforms(UniformsLib.normalmap());
		this.setUniforms(UniformsLib.displacementmap());
		this.setUniforms(UniformsLib.fog());
		this.setUniforms(UniformsLib.lights());
		this.addUniform("emissive", new Uniform(Uniform.TYPE.C, new Color( 0x000000 ) ));
		this.addUniform("specular", new Uniform(Uniform.TYPE.C, new Color( 0x111111 ) ));
		this.addUniform("shininess", new Uniform(Uniform.TYPE.F1, 30.0 ));
	}
	
}
