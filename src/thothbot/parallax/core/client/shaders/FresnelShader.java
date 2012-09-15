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

package thothbot.parallax.core.client.shaders;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

/**
 * Fresnel shader.
 * <p>
 * based on Nvidia Cg tutorial and three.js code
 * 
 * @author thothbot
 *
 */
public final class FresnelShader extends Shader 
{

	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);

		@Source("source/fresnel.vs")
		TextResource getVertexShader();

		@Source("source/fresnel.fs")
		TextResource getFragmentShader();
	}

	public FresnelShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms() 
	{
		this.addUniform("mRefractionRatio", new Uniform(Uniform.TYPE.F, 1.02 ));
		this.addUniform("mFresnelBias", new Uniform(Uniform.TYPE.F, .1 ));
		this.addUniform("mFresnelPower", new Uniform(Uniform.TYPE.F, 2.0 ));
		this.addUniform("mFresnelScale", new Uniform(Uniform.TYPE.F, 1.0 ));
		this.addUniform("tCube", new Uniform(Uniform.TYPE.T, 1 ));
	}
}
