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

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.math.Vector2;

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
@ThreejsObject("THREE.Shape")
public class Shape extends Path
{
	private List<Path> holes = new ArrayList<>();

	public Shape()
	{
		super();
	}

	public Shape(List<Vector2> points)
	{
		super(points);
	}

	public List<Path> getHoles()
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
	 * Convenience method to return ShapeGeometry
	 */
	public ShapeGeometry makeGeometry ( final ShapeGeometry.ShapeGeometryParameters options )
	{
		return new ShapeGeometry( this, options );
	}
	
	/*
	 * Get points of holes
	 */
	public List<List<Vector2>> getPointsHoles()
	{
		return getPointsHoles(false);
	}

	public List<List<Vector2>> getPointsHoles( boolean closedPath) {
		int il = this.holes.size();
		List<List<Vector2>> holesPts = new ArrayList<List<Vector2>>();

		for ( int i = 0; i < il; i ++ )
			holesPts.add(this.holes.get( i ).getTransformedPoints( closedPath, getBends() ));

		return holesPts;
	}

	/*
	 * Get points of holes (spaced by regular distance)
	 */
	public List<List<Vector2>> getSpacedPointsHoles( boolean closedPath )
	{
		int il = this.holes.size();
		List<List<Vector2>> holesPts = new ArrayList<List<Vector2>>();

		for ( int i = 0; i < il; i ++ )
			holesPts.add(this.holes.get( i ).getTransformedSpacedPoints( closedPath, getBends() ));

		return holesPts;
	}
	
	@Override
	public String toString ()
	{
		return this.getActions().toString();
	}
}
