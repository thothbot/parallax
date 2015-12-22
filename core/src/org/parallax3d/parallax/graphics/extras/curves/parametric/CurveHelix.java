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

import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.extras.core.Curve;

public final class CurveHelix extends Curve
{

	@Override
	public Vector3 getPoint(float t)
	{
		float a = 30; // radius
		float b = 150; //height
		float t2 = (float)(2.0 * Math.PI * t * b / 30.0);
		float tx = (float)(Math.cos(t2) * a);
		float ty = (float)(Math.sin(t2) * a);
		float tz = b * t;

		return new Vector3(tx, ty, tz);
	}

}
