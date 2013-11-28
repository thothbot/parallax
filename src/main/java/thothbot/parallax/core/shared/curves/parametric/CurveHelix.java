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

public final class CurveHelix extends Curve
{

	@Override
	public Vector3 getPoint(double t) 
	{
		double a = 30; // radius
		double b = 150; //height
		double t2 = 2.0 * Math.PI * t * b / 30.0;
		double tx = Math.cos(t2) * a;
		double	ty = Math.sin(t2) * a;
		double	tz = b * t;

		return new Vector3(tx, ty, tz);
	}

}
