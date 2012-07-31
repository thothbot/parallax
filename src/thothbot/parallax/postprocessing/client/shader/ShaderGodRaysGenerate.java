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
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.postprocessing.client.shader;

import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.Uniform;
import thothbot.parallax.core.shared.core.Vector2f;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

/**
 * The god-ray generation shader.
 * <p>
 * First pass:
 * <p>
 * The input is the depth map. I found that the output from the
 * THREE.MeshDepthMaterial material was directly suitable without
 * requiring any treatment whatsoever.
 * <p>
 * The depth map is blurred along radial lines towards the "sun". The
 * output is written to a temporary render target (I used a 1/4 sized
 * target).
 * <p>
 * Pass two & three:
 * <p>
 * The results of the previous pass are re-blurred, each time with a
 * decreased distance between samples.
 * <p>
 * The code from three.js project
 */
public final class ShaderGodRaysGenerate extends Shader 
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);

		@Source("source/defaultUv.vs")
		TextResource getVertexShader();

		@Source("source/godraysGenerate.fs")
		TextResource getFragmentShader();
	}
	
	public ShaderGodRaysGenerate() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tInput", new Uniform(Uniform.TYPE.T, 0));
		this.addUniform("fStepSize", new Uniform(Uniform.TYPE.F, 1.0f));
		this.addUniform("vSunPositionScreenSpace", new Uniform(Uniform.TYPE.V2, new Vector2f( 0.5f, 0.5f )));
	}
}
