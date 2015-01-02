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

public final class CurveTrefoilPolynomialKnot extends Curve 
{

	private double scale;
	
	public CurveTrefoilPolynomialKnot()
	{
		this(10);
	}
	
	public CurveTrefoilPolynomialKnot(double scale)
	{
		this.scale = scale;
	}
	
	@Override
	public Vector3 getPoint(double t) 
	{
		t = t * 4.0 - 2.0;
		double tx = Math.pow(t, 3.0) - 3.0 * t;
		double	ty = Math.pow(t, 4.0) - 4.0 * t * t;
		double	tz = 1 / 5.0 * Math.pow(t, 5.0) - 2.0 * t;

		return new Vector3(tx, ty, tz).multiply(this.scale);
	}

}
