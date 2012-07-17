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

package thothbot.parallax.core.shared.scenes;

import java.util.Map;

import thothbot.parallax.core.client.shader.Uniform;

/**
 * This class implements fog with density.
 * 
 * @author thothbot
 *
 */
public final class FogExp2 extends Fog
{
	private float density;

	/**
	 * This default constructor will make fog with 
	 * density parameter 0.00025.
	 * 
	 * @param hex the color in HEX format
	 */
	public FogExp2(int hex) 
	{
		this(hex, 0.00025f);
	}

	/**
	 * This constructor will make fog with defined density.
	 * 
	 * @param hex     the color in HEX format
	 * @param density the density value in range <0.0, 1.0>
	 */
	public FogExp2(int hex, float density) 
	{
		super(hex);
		this.density = density;
	}

	/**
	 * Set density parameter
	 * 
	 * @param density the density value in range <0.0, 1.0>
	 */
	public void setDensity(float density)
	{
		this.density = density;
	}

	/**
	 * Get density parameter
	 * 
	 * @return the density value
	 */
	public float getDensity()
	{
		return this.density;
	}
	
	@Override
	public void refreshUniforms(Map<String, Uniform> uniforms) 
	{
		super.refreshUniforms(uniforms);
		uniforms.get("fogDensity").value = getDensity();
	}
}
