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
 * Brightness and contrast adjustment
 * <a href="https://github.com/evanw/glfx.js">github.com</a>
 * <p>
 * Based on three.js code
 * 
 * <ul>
 * <li>brightness: -1 to 1 (-1 is solid black, 0 is no change, and 1 is solid white)</li>
 * <li>contrast: -1 to 1 (-1 is solid gray, 0 is no change, and 1 is maximum contrast)</li>
 * </ul>
 * 
 * @author thothbot
 * 
 */
public final class BrightnessContrastShader extends Shader 
{

	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("source/defaultUv.vs")
		TextResource getVertexShader();

		@Source("source/brightnessContrast.fs")
		TextResource getFragmentShader();
	}
	
	public BrightnessContrastShader() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T ));
		this.addUniform("brightness", new Uniform(Uniform.TYPE.F, 0.0));
		this.addUniform("contrast", new Uniform(Uniform.TYPE.F, 0.0));
	}

}
