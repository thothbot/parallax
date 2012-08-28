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

public class CurveDecoratedTorusKnot4a extends Curve 
{

	protected double scale;
	
	public CurveDecoratedTorusKnot4a()
	{
		this(40);
	}
	
	public CurveDecoratedTorusKnot4a(double scale)
	{
		this.scale = scale;
	}
	
	@Override
	public Vector3 getPoint(double t) 
	{
		t *= Math.PI * 2.0;
		
		double x = Math.cos(2.0 * t) * (1 + 0.6 * (Math.cos(5.0 * t) + 0.75 * Math.cos(10.0 * t)));
		double y = Math.sin(2.0 * t) * (1 + 0.6 * (Math.cos(5.0 * t) + 0.75 * Math.cos(10.0 * t)));
		double z = 0.35 * Math.sin(5.0 * t);

		return new Vector3(x, y, z).multiply(this.scale);
	}
}
