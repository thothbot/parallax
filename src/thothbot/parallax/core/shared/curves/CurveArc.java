/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
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

package thothbot.parallax.core.shared.curves;

import thothbot.parallax.core.shared.core.Vector2f;

public class CurveArc extends Curve
{
	
	public float aX;
	public float aY;

	public float aRadius;

	public float aStartAngle;
	public float aEndAngle;

	public boolean aClockwise;

	public CurveArc(float aX, float aY, float aRadius, float aStartAngle, float aEndAngle,
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
	public Vector2f getPoint(float t)
	{
		float deltaAngle = this.aEndAngle - this.aStartAngle;

		if ( !this.aClockwise )
			t = 1 - t;
		
		float angle = this.aStartAngle + t * deltaAngle;

		float tx = (float) (this.aX + this.aRadius * Math.cos( angle ));
		float ty = (float) (this.aY + this.aRadius * Math.sin( angle ));

		return new Vector2f( tx, ty );
	}

}
