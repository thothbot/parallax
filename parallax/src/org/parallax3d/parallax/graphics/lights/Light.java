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

package org.parallax3d.parallax.graphics.lights;

import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * Abstract base class for lights.
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.Light")
public abstract class Light extends Object3D
{
	Color color;
	double intensity;

	public Light()
	{
		this(0xffffff, 1.);
	}

	public Light(int hex)
	{
		this(hex, 1.);
	}

	public Light(int hex, double intensity)
	{
		this.color = new Color(hex);
		this.intensity = intensity;
	}

	public Light setColor(Color color) {
		this.color = color;
		return this;
	}

	public Color getColor() {
		return color;
	}

	public double getIntensity() {
		return intensity;
	}

	public Light setIntensity(double intensity) {
		this.intensity = intensity;
		return this;
	}

	public Light copy(Light source) {

		this.color.copy( source.color );
		this.intensity = source.intensity;

		return this;
	}

	public abstract Light clone();
}
