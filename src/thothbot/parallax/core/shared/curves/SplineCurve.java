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

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.utils.CurveUtils;


public class SplineCurve extends Curve
{

	public List<Vector2> points;
	
	public SplineCurve() 
	{
		this.points = new ArrayList<Vector2>();
	}

	public SplineCurve(List<Vector2> points) 
	{
		this.points = points;
	}

	@Override
	public Vector2 getPoint(double t)
	{
		Vector2 v = new Vector2();
		
		double point = ( points.size() - 1.0 ) * t;
		int intPoint = (int) Math.floor( point );
		
		double weight = point - intPoint;

		int c0 = intPoint == 0 ? intPoint : intPoint - 1;
		int c1 = intPoint;
		int c2 = intPoint  > points.size() - 2 ? points.size() -1 : intPoint + 1;
		int c3 = intPoint  > points.size() - 3 ? points.size() -1 : intPoint + 2;

		v.setX( CurveUtils.interpolate( points.get(c0).getX(), points.get(c1).getX(), points.get(c2).getX(), points.get(c3).getX(), weight ) );
		v.setY( CurveUtils.interpolate( points.get(c0).getY(), points.get(c1).getY(), points.get(c2).getY(), points.get(c3).getY(), weight ) );

		return v;
	}
}
