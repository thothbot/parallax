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

package thothbot.parallax.core.shared.scenes;

import java.util.Map;

import thothbot.parallax.core.client.shader.Uniform;

/**
 * This class implements simple fog with near and far options.
 * 
 * @author thothbot
 *
 */
public final class FogSimple extends Fog 
{
	private float near;
	private float far;

	/**
	 * This default constructor will make simple fog with 
	 * near parameter 1.0 and far 1000
	 * 
	 * @param hex the color in HEX format
	 */
	public FogSimple(int hex) 
	{
		this(hex, 1f, 1000f);
	}

	/**
	 * This constructor will make simple fog with defined parameters.
	 * 
	 * @param hex  the color in HEX format
	 * @param near the near scalar value
	 * @param far  the far scala value
	 */
	public FogSimple(int hex, float near, float far) 
	{
		super(hex);
		this.near = near;
		this.far = far;
	}

	/**
	 * Set near fog parameter
	 * 
	 * @param near the near scalar value
	 */
	public void setNear(float near)
	{
		this.near = near;
	}

	/**
	 * Get near fog parameter
	 * 
	 * @return the near fog parameter
	 */
	public float getNear()
	{
		return near;
	}

	/**
	 * Set far fog parameter
	 * 
	 * @param far the far fog parameter
	 */
	public void setFar(float far)
	{
		this.far = far;
	}

	/**
	 * Get far fog parameter
	 * 
	 * @return the far fog parameter
	 */
	public float getFar()
	{
		return far;
	}
	
	@Override
	public void refreshUniforms(Map<String, Uniform> uniforms) 
	{
		super.refreshUniforms(uniforms);
		
		uniforms.get("fogNear").value = getNear();
		uniforms.get("fogFar").value = getFar();
	}
}
