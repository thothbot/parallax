/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

import thothbot.parallax.core.shared.core.Color;
import thothbot.parallax.core.shared.core.Vector2;
import thothbot.parallax.core.shared.core.Vector3;

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
		
		this.addUniform("enableAO",         new Uniform(Uniform.TYPE.I, 0 ));
		this.addUniform("enableDiffuse",    new Uniform(Uniform.TYPE.I, 0 ));
		this.addUniform("enableSpecular",   new Uniform(Uniform.TYPE.I, 0 ));
		this.addUniform("enableReflection", new Uniform(Uniform.TYPE.I, 0 ));
		
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
		
		this.addUniform("uDiffuseColor",  new Uniform(Uniform.TYPE.C, new Color( 0xffffff ) ));
		this.addUniform("uSpecularColor", new Uniform(Uniform.TYPE.C, new Color( 0x111111 ) ));
		this.addUniform("uAmbientColor",  new Uniform(Uniform.TYPE.C, new Color( 0xffffff ) ));
		this.addUniform("uShininess",     new Uniform(Uniform.TYPE.F, 30.0  ));
		this.addUniform("uOpacity",       new Uniform(Uniform.TYPE.F, 1.0 ));
		
		this.addUniform("uReflectivity", new Uniform(Uniform.TYPE.F, 0.5 ));
		
		this.addUniform("uOffset", new Uniform(Uniform.TYPE.V2, new Vector2( 0.0, 0.0 ) ));
		this.addUniform("uRepeat", new Uniform(Uniform.TYPE.V2, new Vector2( 1.0, 1.0 ) ));
		
		this.addUniform("wrapRGB", new Uniform(Uniform.TYPE.V3, new Vector3( 1.0, 1.0, 1.0 ) ));
	}
	
	@Override
	protected void updateVertexSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksVertexShader.SKINNING_PARS,
			ChunksVertexShader.SHADOWMAP_PARS
		);
		
		List<String> main = Arrays.asList(
			ChunksVertexShader.SKINBASE,
			ChunksVertexShader.SKINNORMAL
		);

		super.updateVertexSource(Shader.updateShaderSource(src, vars, main));
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

}
