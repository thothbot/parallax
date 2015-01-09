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

package thothbot.parallax.core.shared.scenes;

import java.util.Map;

import thothbot.parallax.core.client.shaders.Uniform;

/**
 * This class implements simple fog with near and far options.
 * 
 * @author thothbot
 *
 */
public final class Fog extends AbstractFog 
{
	private double near;
	private double far;

	/**
	 * This default constructor will make simple fog with 
	 * near parameter 1.0 and far 1000
	 * 
	 * @param hex the color in HEX format
	 */
	public Fog(int hex) 
	{
		this(hex, 1, 1000);
	}

	/**
	 * This constructor will make simple fog with defined parameters.
	 * 
	 * @param hex  the color in HEX format
	 * @param near the near scalar value
	 * @param far  the far scala value
	 */
	public Fog(int hex, double near, double far) 
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
	public void setNear(double near)
	{
		this.near = near;
	}

	/**
	 * Get near fog parameter
	 * 
	 * @return the near fog parameter
	 */
	public double getNear()
	{
		return near;
	}

	/**
	 * Set far fog parameter
	 * 
	 * @param far the far fog parameter
	 */
	public void setFar(double far)
	{
		this.far = far;
	}

	/**
	 * Get far fog parameter
	 * 
	 * @return the far fog parameter
	 */
	public double getFar()
	{
		return far;
	}
	
	public Fog clone() {
		Fog fog = new Fog(0x000000);
		super.clone(fog);
		
		fog.near = this.near;
		fog.far = this.far;
		
		return fog;
	}
	
	@Override
	public void refreshUniforms(Map<String, Uniform> uniforms) 
	{
		super.refreshUniforms(uniforms);
		
		uniforms.get("fogNear").setValue( getNear() );
		uniforms.get("fogFar").setValue( getFar() );
	}
}
