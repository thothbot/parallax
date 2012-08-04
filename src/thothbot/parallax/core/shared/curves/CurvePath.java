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

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.shared.core.BoundingBox;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Vector2;
import thothbot.parallax.core.shared.core.Vector3;


/*
 *	Curved Path - a curve path is simply a array of connected
 *  curves, but retains the api of a curve
 */
public class CurvePath extends Curve
{

	private List<Curve> curves;
	private List<CurvePath> bends;
	
	private List<Double> cacheLengths;
	
	// Automatically closes the path
	public boolean autoClose = false; 
	
	public CurvePath() 
	{
		this.curves = new ArrayList<Curve>();
		this.bends = new ArrayList<CurvePath>();
	}
	
	public List<CurvePath> getBends()
	{
		return this.bends;
	}
	
	public List<Curve> getCurves()
	{
		return this.curves;
	}

	public void add( Curve curve ) 
	{
		this.curves.add( curve );
	}

	/*
	 * TODO:
	 * If the ending of curve is not connected to the starting
	 * or the next curve, then, this is not a real path
	 */
	public void checkConnection() 
	{
	}
	
	public void closePath() 
	{
		// TODO Test
		// and verify for vector3 (needs to implement equals)
		// Add a line curve if start and end of lines are not connected
		Vector2 startPoint = (Vector2) getCurves().get(0).getPoint(0);
		Vector2 endPoint = (Vector2) getCurves().get(this.curves.size() - 1 ).getPoint(1);
		
		if (!startPoint.equals(endPoint))
			this.curves.add( new CurveLine(endPoint, startPoint) );
	}
	
	/*
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

				return (Vector2) curve.getPointAt( u );
			}

			i ++;
		}

		return null;

		// loop where sum != 0, sum > d , sum+1 <d
	}
	
	/*
	 * We cannot use the default THREE.Curve getPoint() with getLength() because in
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
	
	/*
	 * Returns min and max coordinates, as well as centroid
	 */
	public BoundingBox getBoundingBox() 
	{
		List<Vector2> points = (ArrayList)this.getPoints();

		double maxX, maxY;
		double minX, minY;

		maxX = maxY = Double.NEGATIVE_INFINITY;
		minX = minY = Double.POSITIVE_INFINITY;

		Vector2 sum = new Vector2();
		int il = points.size();

		for ( int i = 0; i < il; i ++ ) 
		{
			Vector2 p = points.get( i );

			if ( p.getX() > maxX ) 
				maxX = p.getX();
			else if ( p.getX() < minX ) 
				minX = p.getX();

			if ( p.getY() > maxY ) 
				maxY = p.getY();
			else if ( p.getY() < maxY ) 
				minY = p.getY();

			sum.add( p );
		}

		BoundingBox boundingBox = new BoundingBox();
		boundingBox.min.set(minX, minY, 0);
		boundingBox.max.set(maxX, maxY, 0);
		Vector2 centroid = sum.divide( il );
		boundingBox.centroid.set(centroid.getX(), centroid.getY(), 0);
		
		return boundingBox;
	}
	
	/**************************************************************
	 *	Create Geometries Helpers
	 **************************************************************/

	/*
	 * Generate geometry from path points (for Line or ParticleSystem objects)
	 */
	private Geometry createPointsGeometry( int divisions ) 
	{
		return CurvePath.createGeometry( (ArrayList)getPoints( divisions ) );
	}
	
	/*
	 * Generate geometry from equidistance sampling along the path
	 */
	private Geometry createSpacedPointsGeometry( int divisions ) 
	{
		return CurvePath.createGeometry( (ArrayList)getSpacedPoints( divisions ) );
	}

	private static Geometry createGeometry(List<Vector2> points) 
	{
		Geometry geometry = new Geometry();

		for ( int i = 0; i < points.size(); i ++ )
			geometry.getVertices().add( new Vector3( points.get( i ).getX(), points.get( i ).getY(), 0 ) );

		return geometry;
	}
	
	/**************************************************************
	 *	Bend / Wrap Helper Methods
	 **************************************************************/

	/*
	 * Wrap path / Bend modifiers?
	 */
	public void addWrapPath( CurvePath bendpath ) 
	{
		this.bends.add( bendpath );
	}
		
	/*
	 * This returns getPoints() bend/wrapped around the contour of a path.
	 * Read http://www.planetclegg.com/projects/WarpingTextToSplines.html
	 */
	protected List<Vector2> getWrapPoints(  List<Vector2> oldPts, CurvePath path ) 
	{
		BoundingBox bounds = this.getBoundingBox();

		for ( int i = 0, il = oldPts.size(); i < il; i ++ ) 
		{
			Vector2 p = oldPts.get( i );

			double oldX = p.getX();
			double oldY = p.getY();

			double xNorm = oldX / bounds.max.getX();

			// If using actual distance, for length > path, requires line extrusions
			//xNorm = path.getUtoTmapping(xNorm, oldX); // 3 styles. 1) wrap stretched. 2) wrap stretch by arc length 3) warp by actual distance

			xNorm = path.getUtoTmapping( xNorm, oldX );

			// check for out of bounds?

			Vector2 pathPt = path.getPoint( xNorm );
			Vector2 normal = path.getNormalVector( xNorm ).multiply( oldY );

			p.setX(pathPt.getX() + normal.getY());
			p.setY(pathPt.getX() + normal.getY());
		}

		return oldPts;
	}
}
