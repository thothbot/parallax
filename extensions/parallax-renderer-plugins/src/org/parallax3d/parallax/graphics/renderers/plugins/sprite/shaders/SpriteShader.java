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

package org.parallax3d.parallax.graphics.renderers.plugins.sprite.shaders;

import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;

public final class SpriteShader extends Shader 
{
	interface Resources extends Shader.DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("source/sprite.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("source/sprite.fs.glsl")
		SourceTextResource getFragmentShader();
	}

	public SpriteShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("uvOffset", new Uniform(Uniform.TYPE.V2, new Vector2( 0.0, 0.0 ) ));
		this.addUniform("uvScale", new Uniform(Uniform.TYPE.V2, new Vector2( 1.0, 1.0 ) ));
		
		this.addUniform("rotation", new Uniform(Uniform.TYPE.F, 1.0 ));
		this.addUniform("scale", new Uniform(Uniform.TYPE.V2, new Vector2( 1.0, 1.0 ) ));
		
		this.addUniform("color", new Uniform(Uniform.TYPE.C, new Color( 0xffffff ) ));
		this.addUniform("map", new Uniform(Uniform.TYPE.T) );
		this.addUniform("opacity", new Uniform(Uniform.TYPE.F,  1.0 ));
		
		this.addUniform("fogType", new Uniform(Uniform.TYPE.I,  0 ));
		this.addUniform("fogDensity", new Uniform(Uniform.TYPE.F,  0.00025 ));
		this.addUniform("fogNear", new Uniform(Uniform.TYPE.F,  1.0 ));
		this.addUniform("fogFar", new Uniform(Uniform.TYPE.F,  2000.0 ));
		this.addUniform("fogColor", new Uniform(Uniform.TYPE.C, new Color( 0xffffff )));

		this.addUniform("alphaTest", new Uniform(Uniform.TYPE.F,  0 ));
	}
}
