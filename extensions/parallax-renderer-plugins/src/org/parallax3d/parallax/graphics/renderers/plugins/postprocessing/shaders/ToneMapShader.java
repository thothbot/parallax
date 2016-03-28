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
 * @author miibond
 *
 * Full-screen tone-mapping shader based on http://www.graphics.cornell.edu/~jaf/publications/sig02_paper.pdf
 */
@ThreejsObject("THREE.ToneMapShader")
public final class ToneMapShader extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("source/defaultUv.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("source/toneMap.fs.glsl")
		SourceTextResource getFragmentShader();
	}

	public ToneMapShader()
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T));
		this.addUniform("averageLuminance", new Uniform(Uniform.TYPE.F1, 1.0));
		this.addUniform("luminanceMap", new Uniform(Uniform.TYPE.T));
		this.addUniform("maxLuminance", new Uniform(Uniform.TYPE.F1, 16.0));
		this.addUniform("middleGrey", new Uniform(Uniform.TYPE.F1, 0.6));

	}
}
