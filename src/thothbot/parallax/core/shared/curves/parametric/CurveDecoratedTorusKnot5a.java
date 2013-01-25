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

import thothbot.parallax.core.shared.math.Vector3;

public final class CurveDecoratedTorusKnot5a extends CurveDecoratedTorusKnot4a 
{
	@Override
	public Vector3 getPoint(double t) 
	{
		double fi = t * Math.PI * 2.0;
		double x = Math.cos(3.0 * fi) * (1.0 + 0.3 * Math.cos(5.0 * fi) + 0.5 * Math.cos(10.0 * fi));
		double y = Math.sin(3.0 * fi) * (1.0 + 0.3 * Math.cos(5.0 * fi) + 0.5 * Math.cos(10.0 * fi));
		double z = 0.2 * Math.sin(20.0 * fi);

		return new Vector3(x, y, z).multiply(this.scale);
	}
}
