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

import thothbot.squirrel.core.shared.core.Color3f;
import thothbot.squirrel.core.shared.core.Vector3f;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

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
public final class ShaderLambert extends Shader
{

	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("chunk/lambert_vs.chunk")
		TextResource getVertexShader();

		@Source("chunk/lambert_fs.chunk")
		TextResource getFragmentShader();
	}

	public ShaderLambert() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform(UniformsLib.common);
		this.addUniform(UniformsLib.fog);
		this.addUniform(UniformsLib.lights);
		this.addUniform(UniformsLib.shadowmap);
		this.addUniform("ambient", new Uniform(Uniform.TYPE.C, new Color3f( 0xffffff ) ));
		this.addUniform("emissive", new Uniform(Uniform.TYPE.C, new Color3f( 0x000000 ) ));
		this.addUniform("wrapRGB", new Uniform(Uniform.TYPE.V3, new Vector3f( 1, 1, 1 ) ));
	}
	
	@Override
	protected void setVertexSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksVertexShader.MAP_PARS,
			ChunksVertexShader.LIGHTMAP_PARS,
			ChunksVertexShader.ENVMAP_PARS,
			ChunksVertexShader.LIGHTS_LAMBERT_PARS,
			ChunksVertexShader.COLOR_PARS,
			ChunksVertexShader.SKINNING_PARS,
			ChunksVertexShader.MORPH_TARGET_PARS,
			ChunksVertexShader.SHADOWMAP_PARS
		);
		
		List<String> main = Arrays.asList(
			ChunksVertexShader.MAP,
			ChunksVertexShader.LIGHTMAP,
			ChunksVertexShader.ENVMAP,
			ChunksVertexShader.COLOR,
			ChunksVertexShader.MORPH_NORMAL
		);

		List<String> main2 = Arrays.asList(
			ChunksVertexShader.LIGHTS_LAMBERT,
			ChunksVertexShader.SKINNING,
			ChunksVertexShader.MORPH_TARGET,
			ChunksVertexShader.DEFAULT,
			ChunksVertexShader.SHADOWMAP
		);
		
		super.setVertexSource(Shader.updateShaderSource(src, vars, main, main2));
	}
	
	@Override
	protected void setFragmentSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksFragmentShader.COLOR_PARS,
			ChunksFragmentShader.MAP_PARS,
			ChunksFragmentShader.LIGHTMAP_PARS,
			ChunksFragmentShader.ENVMAP_PARS,
			ChunksFragmentShader.FOG_PARS,
			ChunksFragmentShader.SHADOWMAP_PARS
		);
		
		List<String> main = Arrays.asList(
			ChunksFragmentShader.MAP,
			ChunksFragmentShader.ALPHA_TEST
		);
		
		List<String> main2 = Arrays.asList(
			ChunksFragmentShader.LIGHTMAP,
			ChunksFragmentShader.COLOR,
			ChunksFragmentShader.ENVMAP,
			ChunksFragmentShader.SHADOWMAP,
			ChunksFragmentShader.LENEAR_TO_GAMMA,
			ChunksFragmentShader.FOG
		);
		
		super.setFragmentSource(Shader.updateShaderSource(src, vars, main, main2));		
	}

}
