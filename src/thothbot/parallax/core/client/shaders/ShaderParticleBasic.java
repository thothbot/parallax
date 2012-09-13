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

package thothbot.parallax.core.client.shaders;

import java.util.Arrays;
import java.util.List;

import thothbot.parallax.core.client.shaders.ShaderBasic.Resources;

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
public final class ShaderParticleBasic extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("chunk/particle_basic_vs.chunk")
		TextResource getVertexShader();

		@Source("chunk/particle_basic_fs.chunk")
		TextResource getFragmentShader();
	}
	
	public ShaderParticleBasic() 
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
			ChunksVertexShader.SHADOWMAP_PARS
		);
		
		List<String> main = Arrays.asList(
			ChunksVertexShader.COLOR
		);
		
		List<String> main2 = Arrays.asList(
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
			ChunksFragmentShader.SHADOWMAP_PARS
		);
		
		List<String> main = Arrays.asList(
			ChunksFragmentShader.MAP_PARTICLE,
			ChunksFragmentShader.ALPHA_TEST,
			ChunksFragmentShader.COLOR,
			ChunksFragmentShader.SHADOWMAP,
			ChunksFragmentShader.FOG
		);
		
		super.updateFragmentSource(Shader.updateShaderSource(src, vars, main));		
	}
}
