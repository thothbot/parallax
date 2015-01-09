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

package thothbot.parallax.core.client.shaders;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

/**
 * Simple Particle shader.
 * <p>
 * Based on the three.js code.
 * 
 * @author thothbot
 *
 */
public final class ParticleBasicShader extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("source/particle_basic.vs")
		TextResource getVertexShader();

		@Source("source/particle_basic.fs")
		TextResource getFragmentShader();
	}
	
	public ParticleBasicShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.setUniforms(UniformsLib.getParticle());
		this.setUniforms(UniformsLib.getShadowmap());
	}
	
	@Override
	protected void updateVertexSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksVertexShader.COLOR_PARS,
			ChunksVertexShader.SHADOWMAP_PARS,
			ChunksVertexShader.LOGDEPTHBUF_PAR
		);
		
		List<String> main = Arrays.asList(
			ChunksVertexShader.COLOR
		);
		
		List<String> main2 = Arrays.asList(
			ChunksVertexShader.LOGDEPTHBUF,
			ChunksVertexShader.WORLDPOS,
			ChunksVertexShader.SHADOWMAP
		);

		super.updateVertexSource(Shader.updateShaderSource(src, vars, main, main2));
	}
	
	@Override
	protected void updateFragmentSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksFragmentShader.COLOR_PARS,
			ChunksFragmentShader.MAP_PARTICLE_PARS,
			ChunksFragmentShader.FOG_PARS,
			ChunksFragmentShader.SHADOWMAP_PARS,
			ChunksFragmentShader.LOGDEPTHBUF_PAR
		);
		
		List<String> main = Arrays.asList(
			ChunksFragmentShader.LOGDEPTHBUF,
			ChunksFragmentShader.MAP_PARTICLE,
			ChunksFragmentShader.ALPHA_TEST,
			ChunksFragmentShader.COLOR,
			ChunksFragmentShader.SHADOWMAP,
			ChunksFragmentShader.FOG
		);
		
		super.updateFragmentSource(Shader.updateShaderSource(src, vars, main));		
	}
}
