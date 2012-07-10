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

import thothbot.squirrel.core.shared.Log;
import thothbot.squirrel.core.shared.core.ExtrudeGeometry;
import thothbot.squirrel.core.shared.core.Vector2f;

/**
 * @author zz85 / http://www.lab4games.net/zz85/blog
 * Defines a 2d shape plane using paths.
 * 
 * STEP 1 Create a path.
 * STEP 2 Turn path into shape.
 * STEP 3 ExtrudeGeometry takes in Shape/Shapes
 * STEP 3a - Extract points from each shape, turn to vertices
 * STEP 3b - Triangulate each shape, add faces.
 **/
public class Shape extends Path
{
	private List<CurvePath> holes = new ArrayList<CurvePath>();
	
	public Shape() 
	{
		super();
	}
	
	public Shape(List<Vector2f> points) 
	{
		super(points);		
	}
	
	public List<CurvePath> getHoles()
	{
		return this.holes;
	}

	/*
	 * Convenience method to return ExtrudeGeometry
	 */
	public ExtrudeGeometry extrude( ExtrudeGeometry.ExtrudeGeometryParameters options ) 
	{
		return new ExtrudeGeometry( this, options );
	}

	/*
	 * Get points of holes
	 */
	public List<List<Vector2f>> getPointsHoles()
	{
		return getPointsHoles(false);
	}

	public List<List<Vector2f>> getPointsHoles(  boolean closedPath ) 
	{
		int il = this.holes.size();
		List<List<Vector2f>> holesPts = new ArrayList<List<Vector2f>>();

//		for ( int i = 0; i < il; i ++ )
//			holesPts.add(this.holes.get( i ).getTransformedPoints( closedPath, getBends() ));

		return holesPts;
	}

	/*
	 * Get points of holes (spaced by regular distance)
	 */
	public List<List<Vector2f>> getSpacedPointsHoles( boolean closedPath ) 
	{
		int il = this.holes.size();
		List<List<Vector2f>> holesPts = new ArrayList<List<Vector2f>>();

//		for ( int i = 0; i < il; i ++ )
//			holesPts.add(this.holes.get( i ).getTransformedSpacedPoints( closedPath, getBends() ));

		return holesPts;
	}
	
	public List<Vector2f> getTransformedPoints() 
	{
		return getTransformedPoints(false, getBends());
	}
	
	public List<Vector2f> getTransformedPoints( boolean closedPath ) 
	{
		return getTransformedPoints(closedPath, getBends());
	}

	public List<Vector2f> getTransformedPoints( boolean closedPath, List<CurvePath> bends ) 
	{
		List<Vector2f> oldPts = this.getPoints( closedPath ); // getPoints getSpacedPoints
Log.info(".............." + this.getClass().getName()  + ", " + bends);
		for ( int i = 0; i < bends.size(); i ++ )
			oldPts = this.getWrapPoints( oldPts, bends.get( i ) );

		return oldPts;
	}
	
	public String toString()
	{
		return this.getActions().toString();
	}
}
