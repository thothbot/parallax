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
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector4;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

public class OculusRiftShader extends Shader 
{

	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);

		@Source("source/oculusRift.vs")
		TextResource getVertexShader();

		@Source("source/oculusRift.fs")
		TextResource getFragmentShader();
	}

	public OculusRiftShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("texid", new Uniform( Uniform.TYPE.T ));
		this.addUniform("scale",  new Uniform( Uniform.TYPE.V2, new Vector2(1.0,1.0) ));
		this.addUniform("scaleIn",  new Uniform( Uniform.TYPE.V2, new Vector2(1.0,1.0) ));
		this.addUniform("lensCenter",  new Uniform( Uniform.TYPE.V2, new Vector2(0.0,0.0) ));
		this.addUniform("hmdWarpParam",  new Uniform( Uniform.TYPE.V4, new Vector4(1.0,0.0,0.0,0.0) ));
		this.addUniform("chromAbParam",  new Uniform( Uniform.TYPE.V4, new Vector4(1.0,0.0,0.0,0.0) ));
	}
}

