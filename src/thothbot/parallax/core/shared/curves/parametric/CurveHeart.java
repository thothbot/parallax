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

public final class CurveHeart extends Curve 
{

	private double scale;
	
	public CurveHeart()
	{
		this(5.0);
	}

	public CurveHeart(double scale)
	{
		this.scale = scale;
	}

	@Override
	public Vector3 getPoint(double t) 
	{
		t = 2.0 * Math.PI * t;

		double tx = 16.0 * Math.pow(Math.sin(t), 3.0);
		double ty = 13.0 * Math.cos(t) - 5.0 * Math.cos(2.0 * t) - 2.0 * Math.cos(3.0 * t) - Math.cos(4.0 * t);

		return new Vector3(tx, ty, 0).multiply(this.scale);
	}

}
