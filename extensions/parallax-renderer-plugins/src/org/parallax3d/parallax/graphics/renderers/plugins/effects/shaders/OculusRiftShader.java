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

package org.parallax3d.parallax.graphics.renderers.plugins.effects.shaders;

import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector4;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;

public class OculusRiftShader extends Shader
{

	interface Resources extends Shader.DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("source/oculusRift.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("source/oculusRift.fs.glsl")
		SourceTextResource getFragmentShader();
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

