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
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;

import java.util.Arrays;
import java.util.List;

/**
 * Lambert shader. This is the simplest model of light - a pure diffuse lighting. 
 * It is believed that the incident light  is scattered in all direction. 
 * Thus, the illumination is determined by the light density at the surface 
 * only and it depends linearly on the cosine of the angle of incidence.
 * <p>
 * Based on the three.js code.
 * 
 * @author thothbot
 *
 */
public final class LambertShader extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("source/meshlambert_vert.glsl")
		SourceTextResource getVertexShader();

		@Source("source/meshlambert_frag.glsl")
		SourceTextResource getFragmentShader();
	}

	public LambertShader()
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
		this.setUniforms(UniformsLib.fog());
		this.setUniforms(UniformsLib.lights());
		this.addUniform("emissive", new Uniform(Uniform.TYPE.C, new Color( 0x000000 ) ));
	}

}
