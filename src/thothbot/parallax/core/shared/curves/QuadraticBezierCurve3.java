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

package thothbot.parallax.core.shared.curves;

import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.utils.ShapeUtils;

public class QuadraticBezierCurve3 extends Curve
{
	private Vector3 v0;
	private Vector3 v1;
	private Vector3 v2;

	public QuadraticBezierCurve3(Vector3 v0, Vector3 v1, Vector3 v2) 
	{
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public Vector3 getPoint(double t)
	{
		double tx = ShapeUtils.b2(t, this.v0.getX(), this.v1.getX(), this.v2.getX());
		double ty = ShapeUtils.b2(t, this.v0.getY(), this.v1.getY(), this.v2.getY());
		double tz = ShapeUtils.b2(t, this.v0.getZ(), this.v1.getZ(), this.v2.getZ());

		return new Vector3(tx, ty, tz);
	}
}
