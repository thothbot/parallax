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

public class ArcCurve extends Curve
{
	
	public double aX;
	public double aY;

	public double aRadius;

	public double aStartAngle;
	public double aEndAngle;

	public boolean aClockwise;

	public ArcCurve(double aX, double aY, double aRadius, double aStartAngle, double aEndAngle,
			boolean aClockwise) 
	{
		this.aX = aX;
		this.aY = aY;
		this.aRadius = aRadius;
		this.aStartAngle = aStartAngle;
		this.aEndAngle = aEndAngle;
		this.aClockwise = aClockwise;
	}

	@Override
	public Vector2 getPoint(double t)
	{
		double deltaAngle = this.aEndAngle - this.aStartAngle;

		if ( !this.aClockwise )
			t = 1 - t;
		
		double angle = this.aStartAngle + t * deltaAngle;

		double tx = this.aX + this.aRadius * Math.cos( angle );
		double ty = this.aY + this.aRadius * Math.sin( angle );

		return new Vector2( tx, ty );
	}

}
