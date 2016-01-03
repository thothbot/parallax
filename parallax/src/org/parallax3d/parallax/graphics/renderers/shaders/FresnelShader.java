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

package org.parallax3d.parallax.graphics.renderers.shaders;

import org.parallax3d.parallax.App;

/**
 * Fresnel shader.
 * <p>
 * based on Nvidia Cg tutorial and three.js code
 * 
 * @author thothbot
 *
 */
public final class FresnelShader extends Shader 
{

	public FresnelShader() 
	{
		super(App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/source/fresnel.vs").readString(),
				App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/source/fresnel.fs").readString());
	}

	@Override
	protected void initUniforms() 
	{
		this.addUniform("mRefractionRatio", new Uniform(Uniform.TYPE.F, 1.02f ));
		this.addUniform("mFresnelBias", new Uniform(Uniform.TYPE.F, .1f ));
		this.addUniform("mFresnelPower", new Uniform(Uniform.TYPE.F, 2.0f ));
		this.addUniform("mFresnelScale", new Uniform(Uniform.TYPE.F, 1.0f ));
		this.addUniform("tCube", new Uniform(Uniform.TYPE.T ));
	}
}
