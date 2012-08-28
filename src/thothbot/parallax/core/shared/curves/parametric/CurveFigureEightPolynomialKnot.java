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

package thothbot.parallax.core.shared.curves.parametric;

import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.curves.Curve;

public final class CurveFigureEightPolynomialKnot extends Curve 
{

	private double scale;
	
	public CurveFigureEightPolynomialKnot()
	{
		this(1);
	}
	
	public CurveFigureEightPolynomialKnot(double scale)
	{
		this.scale = scale;
	}
	
	@Override
	public Vector3 getPoint(double t)
	{
		t = scaleTo(-4, 4, t);
		double tx = 2 / 5.0 * t * (t * t - 7.0) * (t * t - 10.0);
		double	ty = Math.pow(t, 4.0) - 13.0 * t * t;
		double	tz = 1 / 10.0 * t * (t * t - 4.0) * (t * t - 9.0) * (t * t - 12.0);

		return new Vector3(tx, ty, tz).multiply(this.scale);
	}
	
	private double scaleTo(double x, double y, double t) 
	{
		double r = y - x;
		return t * r + x;
	}

}
