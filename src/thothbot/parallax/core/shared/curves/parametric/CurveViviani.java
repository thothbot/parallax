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

package thothbot.parallax.core.shared.curves.parametric;

import thothbot.parallax.core.shared.curves.Curve;
import thothbot.parallax.core.shared.math.Vector3;

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
