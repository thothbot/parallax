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

package thothbot.parallax.core.shared.lights;

import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Vector3;

public final class HemisphereLight extends Light 
{
	private Color groundColor;
	private double intensity;
	
	public HemisphereLight(int skyColorHex, int groundColorHex)
	{
		this(skyColorHex, groundColorHex, 1.0);
	}
	
	public HemisphereLight(int skyColorHex, int groundColorHex, double intensity)
	{
		super(skyColorHex);
		
		this.groundColor = new Color( groundColorHex );

		this.position = new Vector3( 0, 100.0, 0 );

		this.intensity = intensity;
	}
	
	public Color getGroundColor() {
		return groundColor;
	}

	public void setGroundColor(Color groundColor) {
		this.groundColor = groundColor;
	}

	public double getIntensity() {
		return intensity;
	}

	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}
	
	public HemisphereLight clone() {

		HemisphereLight light = new HemisphereLight(0x000000, 0x000000);
		
		super.clone(light);

		light.groundColor.copy( this.groundColor );
		light.intensity = this.intensity;

		return light;

	}
}
