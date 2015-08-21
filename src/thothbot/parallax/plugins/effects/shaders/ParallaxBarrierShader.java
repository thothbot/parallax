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

package thothbot.parallax.plugins.effects.shaders;

import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

public class ParallaxBarrierShader extends Shader 
{

	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);

		@Source("source/parallaxBarrier.vs")
		TextResource getVertexShader();

		@Source("source/parallaxBarrier.fs")
		TextResource getFragmentShader();
	}

	public ParallaxBarrierShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("mapLeft", new Uniform( Uniform.TYPE.T ));
		this.addUniform("mapRight",  new Uniform( Uniform.TYPE.T ));
	}
}
