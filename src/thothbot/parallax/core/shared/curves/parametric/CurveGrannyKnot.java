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
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.shared.curves.parametric;

import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.curves.Curve;

public final class CurveGrannyKnot extends Curve 
{

	@Override
	public Vector3 getPoint(double t) 
	{
		t = 2.0 * Math.PI * t;

		double x = -0.22 * Math.cos(t) - 1.28 * Math.sin(t) - 0.44 * Math.cos(3.0 * t) - 0.78 * Math.sin(3.0 * t);
		double y = -0.1 * Math.cos(2.0 * t) - 0.27 * Math.sin(2.0 * t) + 0.38 * Math.cos(4.0 * t) + 0.46 * Math.sin(4.0 * t);
		double z = 0.7 * Math.cos(3.0 * t) - 0.4 * Math.sin(3.0 * t);
		return new Vector3(x, y, z).multiply(20);
	}

}
