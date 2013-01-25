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

public final class CurveKnot extends Curve 
{

	@Override
	public Vector3 getPoint(double t) 
	{
		t *= 2.0 * Math.PI;

		double R = 10;
		double s = 50;
		double tx = s * Math.sin(t);
		double ty = Math.cos(t) * (R + s * Math.cos(t));
		double tz = Math.sin(t) * (R + s * Math.cos(t));

		return new Vector3(tx, ty, tz);
	}

}
