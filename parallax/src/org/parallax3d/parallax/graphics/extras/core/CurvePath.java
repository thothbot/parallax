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

package org.parallax3d.parallax.graphics.extras.core;

import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.curves.LineCurve;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;

import java.util.ArrayList;
import java.util.List;

/*
 *	Curved Path - a curve path is simply a array of connected
 *  curves, but retains the api of a curve
 */
@ThreejsObject("THREE.CurvePath")
public class CurvePath extends Curve
{

	List<Curve> curves = new ArrayList<>();

	List<Double> cacheLengths;

	// Automatically closes the path
	boolean autoClose = false;

	public CurvePath() {
	}

	public List<Curve> getCurves()
	{
		return this.curves;
	}

	public void setCurves(List<Curve> curves) {
		this.curves = curves;
	}

	public boolean isAutoClose() {
		return autoClose;
	}

	public void setAutoClose(boolean autoClose) {
		this.autoClose = autoClose;
	}

	public void add(Curve curve )
	{
		this.curves.add( curve );
	}

	public void closePath()
	{
		// TODO Test
		// and verify for vector3 (needs to implement equals)
		// Add a line curve if start and end of lines are not connected
		Vector2 startPoint = getCurves().get(0).getPoint(0);
		Vector2 endPoint = getCurves().get(this.curves.size() - 1 ).getPoint( 1 );

		if (!startPoint.equals(endPoint))
			this.curves.add( new LineCurve(endPoint, startPoint) );
	}

	/**
	 * To get accurate point with reference to
	 * entire path distance at time t,
	 * To get accurate point with reference to
	 *
	 * following has to be done:
	 * 1. Length of each sub path have to be known
	 * 2. Locate and identify type of curve
	 * 3. Get t for the curve
	 * 4. Return curve.getPointAt(t')
	 */
	@Override
	public Vector2 getPoint(double t)
	{
		double d = t * this.getLength();
		List<Double> curveLengths = this.getCurveLengths();
		int i = 0;

		// To think about boundaries points.
		while ( i < curveLengths.size() )
		{
			if ( curveLengths.get( i ) >= d )
			{
				double diff = curveLengths.get( i ) - d;
				Curve curve = getCurves().get( i );

				double u = 1.0 - diff / curve.getLength();

				return curve.getPointAt( u );
			}

			i ++;
		}

		return null;

		// loop where sum != 0, sum > d , sum+1 <d
	}

	/**
	 * We cannot use the default Curve getPoint() with getLength() because in
	 * Curve, getLength() depends on getPoint() but in THREE.CurvePath
	 * getPoint() depends on getLength
	 */
	public double getLength()
	{
		List<Double> lens = this.getCurveLengths();
		return lens.get( lens.size() - 1 );
	}

	/*
	 * Compute lengths and cache them
	 * We cannot overwrite getLengths() because UtoT mapping uses it.
	 */
	public List<Double> getCurveLengths()
	{
		// We use cache values if curves and cache array are same length
		if ( this.cacheLengths != null && this.cacheLengths.size() == this.curves.size() )
			return this.cacheLengths;

		// Get length of subsurve
		// Push sums into cached array
		this.cacheLengths = new ArrayList<Double>();
		double sums = 0.0;
		for ( int i = 0; i < this.curves.size(); i ++ )
		{
			sums += this.curves.get( i ).getLength();
			this.cacheLengths.add( sums );
		}

		return this.cacheLengths;
	}

	public Geometry createPointsGeometry( ) {

		List<Vector2> pts = this.getPoints();
		return this.createGeometry( pts );

	}

	/**
	 * Generate geometry from path points (for Line or Points objects)
	 * @param divisions
     */
	public Geometry createPointsGeometry( int divisions ) {

		List<Vector2> pts = this.getPoints( divisions );
		return this.createGeometry( pts );

	}

	/**
	 * Generate geometry from equidistant sampling along the path
	 * @param divisions
     */
	public Geometry createSpacedPointsGeometry( int divisions ) {

		List<Vector2> pts = this.getSpacedPoints( divisions );
		return this.createGeometry( pts );

	}

	public Geometry createGeometry(List<Vector2> points ) {

		Geometry geometry = new Geometry();

		for ( int i = 0, l = points.size(); i < l; i ++ ) {

			Vector2 point = points.get(i);
			double z = point instanceof Vector3 ? ((Vector3) point).getZ() : 0;
			geometry.getVertices().add( new Vector3(point.getX(), point.getY(), z ) );

		}

		return geometry;

	}

}
