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

package thothbot.parallax.plugins.postprocessing.shaders;

import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

/**
 * Depth-of-field shader using mipmaps
 * <p>
 * requires power-of-2 sized render target with enabled mipmaps
 * <p>
 * Based on three.js code<br>
 * From Matt Handley \@applmak
 * 
 * @author thothbot
 *
 */
public final class DOFMipMapShader extends Shader
{
	public interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("source/defaultUv.vs")
		TextResource getVertexShader();

		@Source("source/dofmipmap.fs")
		TextResource getFragmentShader();
	}

	public DOFMipMapShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tColor", new Uniform(Uniform.TYPE.T ));
		this.addUniform("tDepth", new Uniform(Uniform.TYPE.T ));
		this.addUniform("focus", new Uniform(Uniform.TYPE.F, 1.0));
		this.addUniform("maxblur", new Uniform(Uniform.TYPE.I, true));
	}

}
