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
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.math.Vector4;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;

/**
 * Simple skin shader
 * <p>
 * - per-pixel Blinn-Phong diffuse term mixed with half-Lambert wrap-around term (per color component)<br>
 * - physically based specular term (Kelemen/Szirmay-Kalos specular reflectance)<br>
 * - diffuse map<br>
 * - bump map<br>
 * - specular map<br>
 * - point, directional and hemisphere lights (use with "lights: true" material option)<br>
 * - fog (use with "fog: true" material option)<br>
 * - shadow maps
 * <p>
 * Based on three,js code.
 *  
 * @author thothbot
 *
 */
public final class SkinSimpleShader extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);
		
		@Source("shaders/skin_simple.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("shaders/skin_simple.fs.glsl")
		SourceTextResource getFragmentShader();
	}

	public SkinSimpleShader() 
	{
		super(Resources.INSTANCE);
	}
	
	@Override
	protected void initUniforms() 
	{
		this.setUniforms(UniformsLib.getFog());
		this.setUniforms(UniformsLib.getLights());
		this.setUniforms(UniformsLib.getShadowmap());
		
		this.addUniform("enableBump", new Uniform(Uniform.TYPE.I, 0 ));
		this.addUniform("enableSpecular", new Uniform(Uniform.TYPE.I, 0 ));
		
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T ));
		this.addUniform("tBeckmann", new Uniform(Uniform.TYPE.T ));

		this.addUniform("diffuse", new Uniform(Uniform.TYPE.C, new Color( 0xeeeeee ) ));
		this.addUniform("specular", new Uniform(Uniform.TYPE.C, new Color( 0x111111 ) ));
		this.addUniform("ambient", new Uniform(Uniform.TYPE.C, new Color( 0x050505 ) ));
		
		this.addUniform("opacity", new Uniform(Uniform.TYPE.F, 1.0 ));
		
		this.addUniform("uRoughness", new Uniform(Uniform.TYPE.F, 0.15 ));
		this.addUniform("uSpecularBrightness", new Uniform(Uniform.TYPE.F, 0.75 ));

		this.addUniform("bumpMap", new Uniform(Uniform.TYPE.T ));
		this.addUniform("bumpScale", new Uniform(Uniform.TYPE.F, 1.0 ));
		
		this.addUniform("specularMap", new Uniform(Uniform.TYPE.T ));
		
		this.addUniform("offsetRepeat", new Uniform(Uniform.TYPE.V4, new Vector4( 0, 0, 1, 1 ) ));
		this.addUniform("uWrapRGB", new Uniform(Uniform.TYPE.V3, new Vector3( 0.75, 0.375, 0.1875 ) ));
	}
	
	@Override
	protected void updateFragmentSource(String src)
	{
		List<String> vars = Arrays.asList(
				ChunksFragmentShader.SHADOWMAP_PARS,
				ChunksFragmentShader.FOG_PARS,
				ChunksFragmentShader.BUMPMAP_PARS
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
