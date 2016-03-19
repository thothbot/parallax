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

package org.parallax3d.parallax.graphics.extras.curves;

import org.parallax3d.parallax.graphics.extras.core.Curve;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.ThreejsObject;

@ThreejsObject("THREE.EllipseCurve")
public class EllipseCurve extends Curve
{

	double aX;
	double aY;

	double xRadius;
	double yRadius;

	double aStartAngle;
	double aEndAngle;

	boolean aClockwise;

	double aRotation;

	public EllipseCurve( double aX, double aY, double xRadius, double yRadius,
						 double aStartAngle, double aEndAngle, boolean aClockwise)
	{
		this(aX, aY, xRadius, yRadius, aStartAngle, aEndAngle, aClockwise, 0.);
	}

	public EllipseCurve( double aX, double aY, double xRadius, double yRadius,
						 double aStartAngle, double aEndAngle, boolean aClockwise, double aRotation )
	{
		this.aX = aX;
		this.aY = aY;

		this.xRadius = xRadius;
		this.yRadius = yRadius;

		this.aStartAngle = aStartAngle;
		this.aEndAngle = aEndAngle;

		this.aClockwise = aClockwise;

		this.aRotation = aRotation;
	}

	@Override
	public Vector2 getPoint(double t)
	{
		double deltaAngle = this.aEndAngle - this.aStartAngle;

		if ( deltaAngle < 0 ) deltaAngle += Math.PI * 2;
		if ( deltaAngle > Math.PI * 2 ) deltaAngle -= Math.PI * 2;

		double angle;

		if (this.aClockwise) {

			angle = this.aEndAngle + ( 1 - t ) * ( Math.PI * 2 - deltaAngle );

		} else {

			angle = this.aStartAngle + t * deltaAngle;

		}

		double x = this.aX + this.xRadius * Math.cos( angle );
		double y = this.aY + this.yRadius * Math.sin( angle );

		if ( this.aRotation != 0 ) {

			double cos = Math.cos( this.aRotation );
			double sin = Math.sin( this.aRotation );

			double tx = x, ty = y;

			// Rotate the point about the center of the ellipse.
			x = ( tx - this.aX ) * cos - ( ty - this.aY ) * sin + this.aX;
			y = ( tx - this.aX ) * sin + ( ty - this.aY ) * cos + this.aY;

		}

		return new Vector2( x, y );
	}
}
