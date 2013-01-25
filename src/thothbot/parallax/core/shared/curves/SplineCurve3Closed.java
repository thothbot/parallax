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

import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.utils.CurveUtils;

public class SplineCurve3Closed extends Curve
{
	public List<Vector3> points;
	
	public SplineCurve3Closed() 
	{
		this.points = new ArrayList<Vector3>();
	}

	public SplineCurve3Closed(List<Vector3> points) 
	{
		this.points = points;
	}

	@Override
	public Vector3 getPoint(double t)
	{
		Vector3 v = new Vector3();
		
		 // This needs to be from 0-length +1
		double point = points.size() * t;
		int intPoint = (int) Math.floor( point );
		
		double weight = point - intPoint;
		intPoint += intPoint > 0 ? 0 : ( Math.floor( Math.abs( intPoint ) / points.size() ) + 1 ) * points.size();
		
		int c0 = ( intPoint - 1 ) % points.size();
		int c1 = ( intPoint ) % points.size();
		int c2 = ( intPoint + 1 ) % points.size();
		int c3 = ( intPoint + 2 ) % points.size();

		v.setX( CurveUtils.interpolate( points.get(c0).getX(), points.get(c1).getX(), points.get(c2).getX(), points.get(c3).getX(), weight) );
		v.setY( CurveUtils.interpolate( points.get(c0).getY(), points.get(c1).getY(), points.get(c2).getY(), points.get(c3).getY(), weight) );
		v.setZ( CurveUtils.interpolate( points.get(c0).getZ(), points.get(c1).getZ(), points.get(c2).getZ(), points.get(c3).getZ(), weight) );

		return v;
	}
}
