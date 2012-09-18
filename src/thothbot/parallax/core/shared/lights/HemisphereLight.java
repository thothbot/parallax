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

package thothbot.parallax.core.shared.lights;

import thothbot.parallax.core.shared.core.Color;
import thothbot.parallax.core.shared.core.Vector3;

public final class HemisphereLight extends Light 
{
	private Color groundColor;
	private double intensity;
	
	public HemisphereLight(int skyColorHex, int groundColorHex)
	{
		this(skyColorHex, groundColorHex, 1);
	}
	
	public HemisphereLight(int skyColorHex, int groundColorHex, double intensity)
	{
		super(skyColorHex);
		
		this.groundColor = new Color( groundColorHex );

		this.position = new Vector3( 0, 100, 0 );

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
}
