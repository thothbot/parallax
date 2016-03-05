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

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.extras.core.Curve;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.extras.CurveUtils;

@ThreejsObject("THREE.SplineCurve3")
public class SplineCurve3 extends Curve
{
	public List<Vector3> points;

	public SplineCurve3()
	{
		this.points = new ArrayList<Vector3>();
	}

	public SplineCurve3(List<Vector3> points)
	{
		this.points = points;
	}

	@Override
	public Vector3 getPoint(double t)
	{
		Vector3 v = new Vector3();

		double point = ( points.size() - 1.0 ) * t;
		int intPoint = (int) Math.floor( point );

		double weight = point - intPoint;

		int c0 = intPoint == 0 ? intPoint : intPoint - 1;
		int c1 = intPoint;
		int c2 = intPoint  > points.size() - 2 ? points.size() -1 : intPoint + 1;
		int c3 = intPoint  > points.size() - 3 ? points.size() -1 : intPoint + 2;

		v.setX( CurveUtils.interpolate( points.get(c0).getX(), points.get(c1).getX(), points.get(c2).getX(), points.get(c3).getX(), weight) );
		v.setY( CurveUtils.interpolate( points.get(c0).getY(), points.get(c1).getY(), points.get(c2).getY(), points.get(c3).getY(), weight) );
		v.setZ( CurveUtils.interpolate( points.get(c0).getZ(), points.get(c1).getZ(), points.get(c2).getZ(), points.get(c3).getZ(), weight) );

		return v;
	}
}
