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

package thothbot.parallax.core.shared.geometries;

public final class Icosahedron2Geometry extends PolyhedronGeometry 
{
	public Icosahedron2Geometry( double radius, int detail ) 
	{
		super(radius, detail );
	}	
	
	@Override
	protected double[][] getGeometryVertices() 
	{
		double a = 4 / Math.sqrt( 2 * (5 + Math.sqrt(5)) ) / 2;
		double b = Math.sqrt(1 - a*a);
		
		double[][] vertices = {
				{-a, 0.0, b}, {a, 0.0, b}, {-a, 0.0, -b}, {a, 0.0, -b},
				{0.0, b, a}, {0.0, b, -a}, {0.0, -b, a}, {0.0, -b, -a},
				{b, a, 0.0}, {-b, a, 0.0}, {b, -a, 0.0}, {-b, -a, 0.0}
		};
		
		return vertices;
	}

	@Override
	protected int[][] getGeometryFaces() 
	{
		int[][] faces = {
				{0,4,1}, {0,9,4}, {9,5,4},{4,5,8}, {4,8,1},
				{8,10,1}, {8,3,10},{5,3,8}, {5,2,3}, {2,7,3},
				{7,10,3}, {7,6,10}, {7,11,6}, {11,0,6}, {0,1,6},
				{6,1,10}, {9,0,11}, {9,11,2}, {9,2,5}, {7,2,11}
		};

		return faces;
	}

}
