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

package org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.shaders;

import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * @author felixturner / http://airtight.cc/
 *
 * RGB Shift Shader
 * Shifts red and blue channels from center in opposite directions
 * Ported from http://kriss.cx/tom/2009/05/rgb-shift/
 * by Tom Butterworth / http://kriss.cx/tom/
 *
 * amount: shift distance (1 is width of input)
 * angle: shift angle in radians
 */
@ThreejsObject("THREE.DigitalGlitch")
public final class DigitalGlitch extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("source/defaultUv.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("source/digitalGlitch.fs.glsl")
		SourceTextResource getFragmentShader();
	}

	public DigitalGlitch()
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		//diffuse texture
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T ));
		//displacement texture for digital glitch squares
		this.addUniform("tDisp", new Uniform(Uniform.TYPE.T ));
		//apply the glitch ?
		this.addUniform("byp", new Uniform(Uniform.TYPE.I1, 0 ));
		this.addUniform("amount", new Uniform(Uniform.TYPE.F1, .08));
		this.addUniform("angle", new Uniform(Uniform.TYPE.F1, .02));
		this.addUniform("seed", new Uniform(Uniform.TYPE.F1, .02));
		this.addUniform("seed_x", new Uniform(Uniform.TYPE.F1, .02));
		this.addUniform("seed_y", new Uniform(Uniform.TYPE.F1, .02));
		this.addUniform("distortion_x", new Uniform(Uniform.TYPE.F1, .5));
		this.addUniform("distortion_y", new Uniform(Uniform.TYPE.F1, .6));
		this.addUniform("col_s", new Uniform(Uniform.TYPE.F1, .05));
	}
}
