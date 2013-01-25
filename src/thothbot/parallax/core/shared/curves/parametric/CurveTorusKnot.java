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

public final class CurveTorusKnot extends Curve 
{

	private double scale;
	
	public CurveTorusKnot()
	{
		this(10);
	}
	
	public CurveTorusKnot(double scale)
	{
		this.scale = scale;
	}

	@Override
	public Vector3 getPoint(double t) 
	{
		double p = 3, q = 4;
		t *= Math.PI * 2.0;
		double tx = (2.0 + Math.cos(q * t)) * Math.cos(p * t);
		double ty = (2.0 + Math.cos(q * t)) * Math.sin(p * t);
		double tz = Math.sin(q * t);

		return new Vector3(tx, ty, tz).multiply(this.scale);
	}

}
