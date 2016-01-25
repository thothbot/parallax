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

package org.parallax3d.parallax.graphics.scenes;

import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * This class implements fog with density.
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.FogExp2")
public final class FogExp2 extends AbstractFog
{
	private double density;

	/**
	 * This default constructor will make fog with
	 * density parameter 0.00025.
	 *
	 * @param hex the color in HEX format
	 */
	public FogExp2(int hex)
	{
		this(hex, 0.00025);
	}

	/**
	 * This constructor will make fog with defined density.
	 *
	 * @param hex     the color in HEX format
	 * @param density the density value in range <0.0, 1.0>
	 */
	public FogExp2(int hex, double density)
	{
		super(hex);
		this.density = density;
	}

	/**
	 * Set density parameter
	 *
	 * @param density the density value in range <0.0, 1.0>
	 */
	public void setDensity(double density)
	{
		this.density = density;
	}

	/**
	 * Get density parameter
	 *
	 * @return the density value
	 */
	public double getDensity()
	{
		return this.density;
	}

	public FogExp2 clone() {
		FogExp2 fog = new FogExp2(0x000000);
		super.clone(fog);

		fog.density = this.density;

		return fog;
	}

	@Override
	public void refreshUniforms(FastMap<Uniform> uniforms)
	{
		super.refreshUniforms(uniforms);
		uniforms.get("fogDensity").setValue( getDensity() );
	}
}
