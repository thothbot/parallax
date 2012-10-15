/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
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
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.plugins.postprocessing.client.shaders;

import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.core.Vector2;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

/**
 * Screen-space ambient occlusion shader
 * <p>
 * Based on three.js code<br>
 * 
 * Ported from SSAO GLSL shader v1.2 
 * assembled by Martins Upitis (martinsh) (<a href="http://devlog-martinsh.blogspot.com">devlog-martinsh.blogspot.com</a>).
 * Original technique is made by ArKano22 (<a href="http://www.gamedev.net/topic/550699-ssao-no-halo-artifacts/">gamedev.net</a>).<br>
 * 
 * Modified to use RGBA packed depth texture (use clear color 1,1,1,1 for depth pass), 
 * made fog more compatible with three.js linear fog
 * 
 * @author thothbot
 *
 */
public final class SSAOShader extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("source/defaultUv.vs")
		TextResource getVertexShader();

		@Source("source/ssao.fs")
		TextResource getFragmentShader();
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
		this.addUniform("cameraFar", new Uniform(Uniform.TYPE.F, 100));
		this.addUniform("fogNear", new Uniform(Uniform.TYPE.F, 5.0));
		this.addUniform("fogFar", new Uniform(Uniform.TYPE.F, 100));
		this.addUniform("fogEnabled", new Uniform(Uniform.TYPE.I, 0));
		this.addUniform("onlyAO", new Uniform(Uniform.TYPE.I, 0));
		this.addUniform("aoClamp", new Uniform(Uniform.TYPE.F, 0.3));
		this.addUniform("lumInfluence", new Uniform(Uniform.TYPE.F,0.9));
	}
}
