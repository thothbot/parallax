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

import org.parallax3d.parallax.system.ThreeJsObject;
import org.parallax3d.parallax.graphics.extras.core.Curve;
import org.parallax3d.parallax.math.Vector2;

@ThreeJsObject("THREE.EllipseCurve")
public final class EllipseCurve extends Curve
{

	private float aX;
	private float aY;

	private float xRadius;
	private float yRadius;

	private float aStartAngle;
	private float aEndAngle;

	private boolean aClockwise;

	public EllipseCurve( float aX, float aY, float xRadius, float yRadius,
			float aStartAngle, float aEndAngle, boolean aClockwise ) 
	{
		this.aX = aX;
		this.aY = aY;

		this.xRadius = xRadius;
		this.yRadius = yRadius;

		this.aStartAngle = aStartAngle;
		this.aEndAngle = aEndAngle;

		this.aClockwise = aClockwise;
	}

	@Override
	public Vector2 getPoint(float t)
	{
		float deltaAngle = this.aEndAngle - this.aStartAngle;

		if ( !this.aClockwise ) 
		{
			t = 1 - t;
		}

		float angle = this.aStartAngle + t * deltaAngle;

		float tx = this.aX + this.xRadius * (float)Math.cos( angle );
		float ty = this.aY + this.yRadius * (float)Math.sin( angle );

		return new Vector2( tx, ty );
	}
}
