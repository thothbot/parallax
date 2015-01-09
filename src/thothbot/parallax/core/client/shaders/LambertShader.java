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

import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Vector3;

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
public final class LambertShader extends Shader
{

	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("source/lambert.vs")
		TextResource getVertexShader();

		@Source("source/lambert.fs")
		TextResource getFragmentShader();
	}

	public LambertShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.setUniforms(UniformsLib.getCommon());
		this.setUniforms(UniformsLib.getFog());
		this.setUniforms(UniformsLib.getLights());
		this.setUniforms(UniformsLib.getShadowmap());
		this.addUniform("ambient", new Uniform(Uniform.TYPE.C, new Color( 0xffffff ) ));
		this.addUniform("emissive", new Uniform(Uniform.TYPE.C, new Color( 0x000000 ) ));
		this.addUniform("wrapRGB", new Uniform(Uniform.TYPE.V3, new Vector3( 1.0, 1.0, 1.0 ) ));
	}
	
	@Override
	protected void updateVertexSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksVertexShader.MAP_PARS,
			ChunksVertexShader.LIGHTMAP_PARS,
			ChunksVertexShader.ENVMAP_PARS,
			ChunksVertexShader.LIGHTS_LAMBERT_PARS,
			ChunksVertexShader.COLOR_PARS,
			ChunksVertexShader.MORPHTARGET_PARS,
			ChunksVertexShader.SKINNING_PARS,
			ChunksVertexShader.SHADOWMAP_PARS,
			ChunksVertexShader.LOGDEPTHBUF_PAR
		);
		
		List<String> main = Arrays.asList(
			ChunksVertexShader.MAP,
			ChunksVertexShader.LIGHTMAP,
			ChunksVertexShader.COLOR,
			ChunksVertexShader.MORPHNORMAL,
			ChunksVertexShader.SKINBASE,
			ChunksVertexShader.SKINNORMAL,
			ChunksVertexShader.DEFAULTNORMAL,
			ChunksVertexShader.MORPHTARGET,
			ChunksVertexShader.SKINNING,
			ChunksVertexShader.DEFAULT,
			ChunksVertexShader.LOGDEPTHBUF,
			
			ChunksVertexShader.WORLDPOS,
			ChunksVertexShader.ENVMAP,
			ChunksVertexShader.LIGHTS_LAMBERT,
			ChunksVertexShader.SHADOWMAP
		);

		super.updateVertexSource(Shader.updateShaderSource(src, vars, main));
	}
	
	@Override
	protected void updateFragmentSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksFragmentShader.COLOR_PARS,
			ChunksFragmentShader.MAP_PARS,
			ChunksFragmentShader.ALPHAMAP_PARS,
			ChunksFragmentShader.LIGHTMAP_PARS,
			ChunksFragmentShader.ENVMAP_PARS,
			ChunksFragmentShader.FOG_PARS,
			ChunksFragmentShader.SHADOWMAP_PARS,
			ChunksFragmentShader.SPECULARMAP_PARS,
			ChunksFragmentShader.LOGDEPTHBUF_PAR
		);
		
		List<String> main = Arrays.asList(
			ChunksFragmentShader.LOGDEPTHBUF,
			ChunksFragmentShader.MAP,
			ChunksFragmentShader.ALPHAMAP,
			ChunksFragmentShader.ALPHA_TEST,
			ChunksFragmentShader.SPECULARMAP
		);
		
		List<String> main2 = Arrays.asList(
			ChunksFragmentShader.LIGHTMAP,
			ChunksFragmentShader.COLOR,
			ChunksFragmentShader.ENVMAP,
			ChunksFragmentShader.SHADOWMAP,
			
			ChunksFragmentShader.LINEAR_TO_GAMMA,
			
			ChunksFragmentShader.FOG
		);
		
		super.updateFragmentSource(Shader.updateShaderSource(src, vars, main, main2));		
	}

}
