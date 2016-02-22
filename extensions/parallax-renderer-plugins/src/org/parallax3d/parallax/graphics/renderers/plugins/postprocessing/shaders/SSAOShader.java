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

/**
 * Screen-space ambient occlusion shader
 * <p>
 * Ported from SSAO GLSL shader v1.2. 
 * Assembled by Martins Upitis (martinsh) (<a href="http://devlog-martinsh.blogspot.com">devlog-martinsh.blogspot.com</a>).
 * Original technique is made by ArKano22 (<a href="http://www.gamedev.net/topic/550699-ssao-no-halo-artifacts/">gamedev.net</a>).<br>
 * <p>
 * Based on three.js code
 * <p>
 * Modified to use RGBA packed depth texture (use clear color 1,1,1,1 for depth pass), 
 * made fog more compatible with linear fog
 * 
 * @author thothbot
 * 
 */
public final class SSAOShader extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);
		
		@Source("source/defaultUv.vs.glsl")
		SourceTextResource getVertexShader();

		@Source("source/ssao.fs.glsl")
		SourceTextResource getFragmentShader();
	}

	public SSAOShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T));
		this.addUniform("tDepth", new Uniform(Uniform.TYPE.T));
		this.addUniform("size", new Uniform(Uniform.TYPE.V2, new Vector2( 512, 512 )));
		this.addUniform("cameraNear", new Uniform(Uniform.TYPE.F, 1.0));
		this.addUniform("cameraFar", new Uniform(Uniform.TYPE.F, 100.0));
		this.addUniform("fogNear", new Uniform(Uniform.TYPE.F, 5.0));
		this.addUniform("fogFar", new Uniform(Uniform.TYPE.F, 100.0));
		this.addUniform("fogEnabled", new Uniform(Uniform.TYPE.I, false));
		this.addUniform("onlyAO", new Uniform(Uniform.TYPE.I, false));
		this.addUniform("aoClamp", new Uniform(Uniform.TYPE.F, 0.3));
		this.addUniform("lumInfluence", new Uniform(Uniform.TYPE.F, 0.9));
	}
}
