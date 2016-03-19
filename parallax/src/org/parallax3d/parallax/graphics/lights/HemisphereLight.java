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

import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.system.ThreejsObject;

@ThreejsObject("THREE.HemisphereLight")
public final class HemisphereLight extends Light
{
	private Color groundColor;

	public HemisphereLight(int skyColorHex, int groundColorHex)
	{
		this(skyColorHex, groundColorHex, 1.0);
	}

	public HemisphereLight(int skyColorHex, int groundColorHex, double intensity)
	{
		super(skyColorHex, intensity);

		this.groundColor = new Color( groundColorHex );
		this.updateMatrix();

		getPosition().set( 0 ,1 ,0);
	}

	public Color getGroundColor() {
		return groundColor;
	}

	public HemisphereLight setGroundColor(Color groundColor) {
		this.groundColor = groundColor;
		return this;
	}

	public HemisphereLight copy(HemisphereLight source) {

		super.copy( source );

		this.groundColor.copy( source.groundColor );

		return this;

	}

	@Override
	public HemisphereLight clone() {
		return new HemisphereLight(0x000000, 0x000000).copy(this);
	}
}
