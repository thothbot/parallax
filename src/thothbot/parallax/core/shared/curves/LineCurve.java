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

import thothbot.parallax.core.shared.math.Vector2;

public class LineCurve extends Curve
{

	private Vector2 v1;
	private Vector2 v2;

	public LineCurve(Vector2 v1, Vector2 v2) 
	{
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public Vector2 getPoint(double t)
	{
		Vector2 point = this.v2.clone();
		point.sub(this.v1);
		point.multiply(t);
		point.add(this.v1);

		return point;
	}

	/*
	 * Line curve is linear, so we can overwrite default getPointAt
	 */
	@Override
	public Vector2 getPointAt(double u)
	{
		return this.getPoint(u);
	}

	@Override
	public Vector2 getTangent(double t)
	{
		Vector2 tangent = this.v2.clone();
		tangent.sub(this.v1);
		tangent.normalize();
		return tangent;
	}
}
