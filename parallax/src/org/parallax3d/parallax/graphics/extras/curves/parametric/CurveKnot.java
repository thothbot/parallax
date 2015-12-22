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

package org.parallax3d.parallax.graphics.extras.curves.parametric;

import org.parallax3d.parallax.graphics.extras.core.Curve;
import org.parallax3d.parallax.math.Vector3;

public final class CurveKnot extends Curve
{

	@Override
	public Vector3 getPoint(float t)
	{
		t *= 2.0 * Math.PI;

		float R = 10;
		float s = 50;
		float tx = (float)(s * Math.sin(t));
		float ty = (float)(Math.cos(t) * (R + s * Math.cos(t)));
		float tz = (float)(Math.sin(t) * (R + s * Math.cos(t)));

		return new Vector3(tx, ty, tz);
	}

}
