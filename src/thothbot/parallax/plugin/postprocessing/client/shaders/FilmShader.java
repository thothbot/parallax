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

package thothbot.parallax.plugin.postprocessing.client.shaders;

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
		this.addUniform("tDiffuse", new Uniform(Uniform.TYPE.T, 0));
		this.addUniform("time", new Uniform(Uniform.TYPE.F, 0.0));
		this.addUniform("nIntensity", new Uniform(Uniform.TYPE.F, 0.5));
		this.addUniform("sIntensity", new Uniform(Uniform.TYPE.F, 0.05));
		this.addUniform("sCount", new Uniform(Uniform.TYPE.F, 4096));
		this.addUniform("grayscale", new Uniform(Uniform.TYPE.I, 1));
	}
}
