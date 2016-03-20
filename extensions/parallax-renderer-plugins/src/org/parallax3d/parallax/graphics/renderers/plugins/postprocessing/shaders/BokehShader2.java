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
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * @author zz85 / https://github.com/zz85 | twitter.com/blurspline
 *
 * Depth-of-field shader with bokeh
 * ported from GLSL shader by Martins Upitis
 * http://blenderartists.org/forum/showthread.php?237488-GLSL-depth-of-field-with-bokeh-v2-4-(update)
 *
 * Requires #define RINGS and SAMPLES integers
 */
@ThreejsObject("THREE.BokehShader2")
public final class BokehShader2 extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("source/defaultUv.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("source/bokeh2.fs.glsl")
		SourceTextResource getFragmentShader();
	}

	public BokehShader2()
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("textureWidth", new Uniform(Uniform.TYPE.F, 1. ));
		this.addUniform("textureHeight", new Uniform(Uniform.TYPE.F, 1. ));

		this.addUniform("focalDepth", new Uniform(Uniform.TYPE.F, 1. ));
		this.addUniform("focalLength", new Uniform(Uniform.TYPE.F, 24. ));
		this.addUniform("fstop", new Uniform(Uniform.TYPE.F, .9 ));

		this.addUniform("tColor", new Uniform(Uniform.TYPE.T ));
		this.addUniform("tDepth", new Uniform(Uniform.TYPE.T ));

		this.addUniform("maxblur", new Uniform(Uniform.TYPE.F, 1.0));

		this.addUniform("showFocus", new Uniform(Uniform.TYPE.I, 0));
		this.addUniform("manualdof", new Uniform(Uniform.TYPE.I, 0));
		this.addUniform("vignetting", new Uniform(Uniform.TYPE.I, 0));
		this.addUniform("depthblur", new Uniform(Uniform.TYPE.I, 0));

		this.addUniform("threshold", new Uniform(Uniform.TYPE.F, .5));
		this.addUniform("gain", new Uniform(Uniform.TYPE.F, 2.));
		this.addUniform("bias", new Uniform(Uniform.TYPE.F, .5));
		this.addUniform("fringe", new Uniform(Uniform.TYPE.F, .7));

		this.addUniform("znear", new Uniform(Uniform.TYPE.F, .1));
		this.addUniform("zfar", new Uniform(Uniform.TYPE.F, 100.));

		this.addUniform("noise", new Uniform(Uniform.TYPE.I, 1));
		this.addUniform("dithering", new Uniform(Uniform.TYPE.F, 0.0001));
		this.addUniform("pentagon", new Uniform(Uniform.TYPE.I, 0));

		this.addUniform("shaderFocus", new Uniform(Uniform.TYPE.I, 1));
		this.addUniform("focusCoords", new Uniform(Uniform.TYPE.V2, new Vector2()));
	}

}
