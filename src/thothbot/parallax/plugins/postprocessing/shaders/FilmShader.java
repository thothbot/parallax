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
 * Film grain & scanlines shader
 * <p>
 * Based on three.js code<br>
 * Ported from HLSL to WebGL / GLSL <a href="http://www.truevision3d.com/forums/showcase/staticnoise_colorblackwhite_scanline_shaders-t18698.0.html">truevision3d.com</a>
 * <p>
 * Screen Space Static Postprocessor
 * <p>
 * Produces an analogue noise overlay similar to a film grain / TV static
 * <p>
 * Original implementation and noise algorithm Pat 'Hawthorne' Shearon<br>
 * Optimized scanlines + noise version with intensity scaling Georg 'Leviathan' Steinrohder
 * 
 * @author thothbot
 *
 */
public final class FilmShader extends Shader
{
	public interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("source/defaultUv.vs")
		TextResource getVertexShader();

		@Source("source/film.fs")
		TextResource getFragmentShader();
	}

	public FilmShader()
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T ));
		this.addUniform("time", new Uniform(Uniform.TYPE.F, 0.0));
		this.addUniform("nIntensity", new Uniform(Uniform.TYPE.F, 0.5));
		this.addUniform("sIntensity", new Uniform(Uniform.TYPE.F, 0.05));
		this.addUniform("sCount", new Uniform(Uniform.TYPE.I, 4096));
		this.addUniform("grayscale", new Uniform(Uniform.TYPE.I, true));
	}
}
