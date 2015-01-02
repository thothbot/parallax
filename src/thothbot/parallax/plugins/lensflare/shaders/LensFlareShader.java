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

package thothbot.parallax.plugins.lensflare.shaders;

import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

public class LensFlareShader extends Shader 
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);

		@Source("source/lensFlare.vs")
		TextResource getVertexShader();

		@Source("source/lensFlare.fs")
		TextResource getFragmentShader();
	}

	public LensFlareShader() 
	{
		this(Resources.INSTANCE);
	}
	
	public LensFlareShader(DefaultResources resource) 
	{
		super(resource);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("renderType", new Uniform(Uniform.TYPE.I,  1 ));
		this.addUniform("map", new Uniform(Uniform.TYPE.T ) );
		this.addUniform("occlusionMap", new Uniform(Uniform.TYPE.T ) );
		
		this.addUniform("opacity", new Uniform(Uniform.TYPE.F,  1.0 ));
		this.addUniform("color", new Uniform(Uniform.TYPE.C, new Color( 0xffffff ) ));
		this.addUniform("scale", new Uniform(Uniform.TYPE.V2, new Vector2( 1.0, 1.0 ) ));
		this.addUniform("rotation", new Uniform(Uniform.TYPE.F, 1.0 ));
		this.addUniform("screenPosition", new Uniform(Uniform.TYPE.V3,  new Vector3( 0.0, 0.0, 0.0 ) ));
	}
}
