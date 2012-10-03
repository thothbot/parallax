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

import thothbot.parallax.core.shared.core.Vector2;
import thothbot.parallax.core.shared.utils.CurveUtils;
import thothbot.parallax.core.shared.utils.ShapeUtils;

public class QuadraticBezierCurve extends Curve
{
	private Vector2 v0;
	private Vector2 v1;
	private Vector2 v2;
	
	public QuadraticBezierCurve(Vector2 v0, Vector2 v1, Vector2 v2) 
	{
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public Vector2 getPoint(double t)
	{
		double tx = ShapeUtils.b2( t, this.v0.getX(), this.v1.getX(), this.v2.getX() );
		double ty = ShapeUtils.b2( t, this.v0.getY(), this.v1.getY(), this.v2.getY() );

		return new Vector2( tx, ty );
	}

	@Override
	public Vector2 getTangent( double t ) 
	{
		double tx = CurveUtils.tangentQuadraticBezier( t, this.v0.getX(), this.v1.getX(), this.v2.getX() );
		double ty = CurveUtils.tangentQuadraticBezier( t, this.v0.getY(), this.v1.getY(), this.v2.getY() );

		// returns unit vector
		Vector2 tangent = new Vector2( tx, ty );
		tangent.normalize();

		return tangent;
	}
}
