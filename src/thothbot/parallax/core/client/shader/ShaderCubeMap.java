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
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.client.shader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

/**
 * Cube map shader.
 * <p>
 * Based on three.js code.
 *  
 * @author thothbot
 *
 */
public final class ShaderCubeMap extends Shader 
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);

		@Source("source/cube_map.vs")
		TextResource getVertexShader();

		@Source("source/cube_map.fs")
		TextResource getFragmentShader();
	}

	public ShaderCubeMap() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tCube", new Uniform(Uniform.TYPE.T, 1 ));
		this.addUniform("tFlip", new Uniform(Uniform.TYPE.F, -1.0f ));
	}
}
