/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.curves;

import java.util.ArrayList;
import java.util.List;

import thothbot.squirrel.core.shared.core.Vector2f;
import thothbot.squirrel.core.shared.utils.CurveUtils;


public class CurveSpline extends Curve
{

	public List<Vector2f> points;
	
	public CurveSpline() 
	{
		this.points = new ArrayList<Vector2f>();
	}

	public CurveSpline(List<Vector2f> points) 
	{
		this.points = points;
	}

	@Override
	public Vector2f getPoint(float t)
	{
		Vector2f v = new Vector2f();
		
		float point = ( points.size() - 1 ) * t;
		int intPoint = (int) Math.floor( point );
		
		float weight = point - intPoint;

		int c0 = intPoint == 0 ? intPoint : intPoint - 1;
		int c1 = intPoint;
		int c2 = intPoint  > points.size() - 2 ? points.size() -1 : intPoint + 1;
		int c3 = intPoint  > points.size() - 3 ? points.size() -1 : intPoint + 2;

		v.setX( CurveUtils.interpolate( points.get(c0).getX(), points.get(c1).getX(), points.get(c2).getX(), points.get(c3).getX(), weight ) );
		v.setY( CurveUtils.interpolate( points.get(c0).getY(), points.get(c1).getY(), points.get(c2).getY(), points.get(c3).getY(), weight ) );

		return v;
	}
}
