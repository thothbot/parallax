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

package org.parallax3d.parallax.tests.resources;

import java.util.Arrays;
import java.util.List;

import org.parallax3d.parallax.graphics.renderers.shaders.*;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;

/**
 * Dynamic terrain shader<br>
 * - Blinn-Phong<br>
 * - height + normal + diffuse1 + diffuse2 + specular + detail maps<br>
 * - point, directional and hemisphere lights (use with "lights: true" material option)<br>
 * - shadow maps receiving
 * <p>
 * Based on three.js code
 * 
 * @author thothbot
 *
 */
public final class TerrainShader extends Shader
{

	interface Resources extends DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);
		
		@Source("shaders/terrain.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("shaders/terrain.fs.glsl")
		SourceTextResource getFragmentShader();
	}

	public TerrainShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.setUniforms(UniformsLib.getFog());
		this.setUniforms(UniformsLib.getLights());
		this.setUniforms(UniformsLib.getShadowmap());
		this.addUniform("enableDiffuse1", new Uniform(Uniform.TYPE.I, false ));
		this.addUniform("enableDiffuse2", new Uniform(Uniform.TYPE.I, false ));
		this.addUniform("enableSpecular", new Uniform(Uniform.TYPE.I, false ));
		this.addUniform("enableReflection", new Uniform(Uniform.TYPE.I, false ));

		this.addUniform("tDiffuse1", new Uniform(Uniform.TYPE.T ));
		this.addUniform("tDiffuse2", new Uniform(Uniform.TYPE.T ));
		this.addUniform("tDetail", new Uniform(Uniform.TYPE.T ));
		this.addUniform("tNormal", new Uniform(Uniform.TYPE.T ));
		this.addUniform("tSpecular", new Uniform(Uniform.TYPE.T ));
		this.addUniform("tDisplacement", new Uniform(Uniform.TYPE.T ));
		
		this.addUniform("uNormalScale", new Uniform(Uniform.TYPE.F, 1.0 ));
		
		this.addUniform("uDisplacementBias", new Uniform(Uniform.TYPE.F, 0.0 ));
		this.addUniform("uDisplacementScale", new Uniform(Uniform.TYPE.F, 1.0 ));

		this.addUniform("diffuse", new Uniform(Uniform.TYPE.C, new Color(0xeeeeee) ));
		this.addUniform("specular", new Uniform(Uniform.TYPE.C, new Color(0x111111) ));
		this.addUniform("ambient", new Uniform(Uniform.TYPE.C, new Color(0x050505) ));
		this.addUniform("shininess", new Uniform(Uniform.TYPE.F, 30.0 ));
		this.addUniform("opacity", new Uniform(Uniform.TYPE.F, 1.0 ));
		
		this.addUniform("uRepeatBase", new Uniform(Uniform.TYPE.V2, new Vector2(1, 1) ));
		this.addUniform("uRepeatOverlay", new Uniform(Uniform.TYPE.V2, new Vector2(1, 1) ));

		this.addUniform("uOffset", new Uniform(Uniform.TYPE.V2, new Vector2(0, 0) ));
	}
	
	@Override
	protected void updateFragmentSource(String src)
	{
		List<String> vars = Arrays.asList(
				ChunksFragmentShader.SHADOWMAP_PARS,
				ChunksFragmentShader.FOG_PARS
		);
			
		List<String> main = Arrays.asList(
				ChunksFragmentShader.SHADOWMAP,
				ChunksFragmentShader.LINEAR_TO_GAMMA,
				ChunksFragmentShader.FOG
		);
					
		super.updateFragmentSource(Shader.updateShaderSource(src, vars, main));		
	}
	
	@Override
	protected void updateVertexSource(String src) 
	{
		List<String> vars = Arrays.asList(
				ChunksVertexShader.SHADOWMAP_PARS
		);
			
		List<String> main = Arrays.asList(
				ChunksVertexShader.SHADOWMAP
		);

		super.updateVertexSource(Shader.updateShaderSource(src, vars, main));
	}
}
