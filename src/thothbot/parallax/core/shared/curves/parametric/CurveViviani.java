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
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.shared.curves.parametric;

import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.curves.Curve;

public final class CurveViviani extends Curve 
{

	private double radius;
	
	public CurveViviani(double radius)
	{
		this.radius = radius;
	}

	@Override
	public Vector3 getPoint(double t) 
	{
		t = t * 4.0 * Math.PI; // Normalized to 0..1
		double a = this.radius / 2.0;
		double tx = a * (1.0 + Math.cos(t));
		double ty = a * Math.sin(t);
		double tz = 2.0 * a * Math.sin(t / 2.0);

		return new Vector3(tx, ty, tz);
	}
}
