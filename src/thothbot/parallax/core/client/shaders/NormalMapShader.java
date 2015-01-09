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
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

/**
 * Normal map shader<br>
 * - Blinn-Phong<br>
 * - normal + diffuse + specular + AO + displacement + reflection + shadow maps<br>
 * - point and directional lights (use with "lights: true" material option)
 * <p>
 * Based on three.js code.
 * 
 * @author thothbot
 *
 */
public final class NormalMapShader extends Shader 
{

	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);

		@Source("source/normalmap.vs")
		TextResource getVertexShader();

		@Source("source/normalmap.fs")
		TextResource getFragmentShader();
	}

	public NormalMapShader() 
	{
		super(Resources.INSTANCE);
	}
	
	@Override
	protected void initUniforms() 
	{
		this.setUniforms(UniformsLib.getFog());
		this.setUniforms(UniformsLib.getLights());
		this.setUniforms(UniformsLib.getShadowmap());
		
		this.addUniform("enableAO",         new Uniform(Uniform.TYPE.I, false ));
		this.addUniform("enableDiffuse",    new Uniform(Uniform.TYPE.I, false ));
		this.addUniform("enableSpecular",   new Uniform(Uniform.TYPE.I, false ));
		this.addUniform("enableReflection", new Uniform(Uniform.TYPE.I, false ));
		this.addUniform("enableDisplacement", new Uniform(Uniform.TYPE.I, false ));
		
		// must go first as this is vertex texture
		this.addUniform("tDisplacement", new Uniform(Uniform.TYPE.T ));
		this.addUniform("tDiffuse",      new Uniform(Uniform.TYPE.T ));
		this.addUniform("tCube",         new Uniform(Uniform.TYPE.T ));
		this.addUniform("tNormal",       new Uniform(Uniform.TYPE.T ));
		this.addUniform("tSpecular",     new Uniform(Uniform.TYPE.T ));
		this.addUniform("tAO",           new Uniform(Uniform.TYPE.T ));
		
		this.addUniform("uNormalScale", new Uniform(Uniform.TYPE.V2, new Vector2( 1, 1 ) ));
		
		this.addUniform("uDisplacementBias",  new Uniform(Uniform.TYPE.F, 0.0 ));
		this.addUniform("uDisplacementScale", new Uniform(Uniform.TYPE.F, 1.0 ));
		
		this.addUniform("diffuse",  new Uniform(Uniform.TYPE.C, new Color( 0xffffff ) ));
		this.addUniform("specular", new Uniform(Uniform.TYPE.C, new Color( 0x111111 ) ));
		this.addUniform("ambient",  new Uniform(Uniform.TYPE.C, new Color( 0xffffff ) ));
		this.addUniform("shininess",     new Uniform(Uniform.TYPE.F, 30.0  ));
		this.addUniform("opacity",       new Uniform(Uniform.TYPE.F, 1.0 ));
		
		this.addUniform("useRefract", new Uniform(Uniform.TYPE.I, false ));
		this.addUniform("refractionRatio", new Uniform(Uniform.TYPE.F, 0.98 ));
		this.addUniform("reflectivity", new Uniform(Uniform.TYPE.F, 0.5 ));
		
		this.addUniform("uOffset", new Uniform(Uniform.TYPE.V2, new Vector2( 0.0, 0.0 ) ));
		this.addUniform("uRepeat", new Uniform(Uniform.TYPE.V2, new Vector2( 1.0, 1.0 ) ));
		
		this.addUniform("wrapRGB", new Uniform(Uniform.TYPE.V3, new Vector3( 1.0, 1.0, 1.0 ) ));
	}
	
	@Override
	protected void updateVertexSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksVertexShader.SKINNING_PARS,
			ChunksVertexShader.SHADOWMAP_PARS,
			ChunksVertexShader.LOGDEPTHBUF_PAR
		);
		
		List<String> main1 = Arrays.asList(
			ChunksVertexShader.SKINBASE,
			ChunksVertexShader.SKINNORMAL
		);
		
		List<String> main2 = Arrays.asList(
			ChunksVertexShader.LOGDEPTHBUF
		);

		super.updateVertexSource(Shader.updateShaderSource(src, vars, main1, main2));
	}
	
	@Override
	protected void updateFragmentSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksFragmentShader.SHADOWMAP_PARS,
			ChunksFragmentShader.FOG_PARS,
			ChunksFragmentShader.LOGDEPTHBUF_PAR
		);
		
		List<String> main1 = Arrays.asList(
			ChunksFragmentShader.LOGDEPTHBUF
		);
		
		List<String> main2 = Arrays.asList(
			ChunksFragmentShader.ALPHA_TEST
		);
		
		List<String> main3 = Arrays.asList(
			ChunksFragmentShader.SHADOWMAP,
			ChunksFragmentShader.LINEAR_TO_GAMMA,
			ChunksFragmentShader.FOG
		);
		
		super.updateFragmentSource(Shader.updateShaderSource(src, vars, main1, main2, main3));		
	}

}
