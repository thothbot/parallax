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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

public class MirrorShader extends Shader 
{

	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);

		@Source("source/mirror.vs")
		TextResource getVertexShader();

		@Source("source/mirror.fs")
		TextResource getFragmentShader();
	}

	public MirrorShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("mirrorColor", new Uniform(Uniform.TYPE.C, new Color(0x7F7F7F)));
		this.addUniform("mirrorSampler",  new Uniform(Uniform.TYPE.T, null ));
		this.addUniform("textureMatrix", new Uniform(Uniform.TYPE.M4, new Matrix4() ));
	}
}
