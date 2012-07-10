/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.client.shader;

import java.util.Arrays;
import java.util.List;

import thothbot.squirrel.core.client.shader.ShaderBasic.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

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
		this.addUniform(UniformsLib.particle);
		this.addUniform(UniformsLib.shadowmap);
	}
	
	@Override
	protected void setVertexSource(String src)
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

		super.setVertexSource(Shader.updateShaderSource(src, vars, main, main2));
	}
	
	@Override
	protected void setFragmentSource(String src)
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
		
		super.setFragmentSource(Shader.updateShaderSource(src, vars, main));		
	}
}
