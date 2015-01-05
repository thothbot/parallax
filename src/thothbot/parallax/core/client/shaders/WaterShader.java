/*
 * Copyright 2014 Alex Usachev, thothbot@gmail.com
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

import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Vector3;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

public class WaterShader extends Shader 
{

	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);

		@Source("source/water.vs")
		TextResource getVertexShader();

		@Source("source/water.fs")
		TextResource getFragmentShader();
	}

	public WaterShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("normalSampler", new Uniform(Uniform.TYPE.T, null ));
		this.addUniform("mirrorSampler",  new Uniform(Uniform.TYPE.T, null ));
		this.addUniform("alpha", new Uniform(Uniform.TYPE.F, 1.0 ));
		this.addUniform("time", new Uniform(Uniform.TYPE.F, 0.0 ));
		this.addUniform("distortionScale", new Uniform(Uniform.TYPE.F, 20.0 ));
		this.addUniform("textureMatrix", new Uniform(Uniform.TYPE.M4, new Matrix4() ));
		this.addUniform("sunColor", new Uniform(Uniform.TYPE.C, new Color( 0x7F7F7F ) ));
		this.addUniform("sunDirection", new Uniform(Uniform.TYPE.V3, new Vector3( 0.70707, 0.70707, 0 ) ));
		this.addUniform("eye", new Uniform(Uniform.TYPE.V3, new Vector3( 0, 0, 0 ) ));
		this.addUniform("waterColor", new Uniform(Uniform.TYPE.C, new Color( 0x555555 ) ));
	}
}
