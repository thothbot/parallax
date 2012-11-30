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

/**
 * <img src="http://thothbot.github.com/parallax/static/docs/icosahedron.gif" />
 * <p>
 * Icosahedron geometry
 * 
 * @author thothbot
 *
 */
public final class IcosahedronGeometry extends PolyhedronGeometry
{
	public IcosahedronGeometry( double radius, int detail ) 
	{
		super(radius, detail );
	}	

	@Override
	protected double[][] getGeometryVertices() 
	{
		double t = ( 1.0 + Math.sqrt( 5 ) ) / 2.0;

		double[][] vertices = {
				{ -1.0,  t,  0.0 }, {  1.0, t, 0.0 }, { -1.0, -t,  0.0 }, {  1.0, -t,  0.0 },
				{  0.0, -1.0,  t }, {  0.0, 1.0, t }, {  0.0, -1.0, -t }, {  0.0,  1.0, -t },
				{  t,  0.0, -1.0 }, {  t, 0.0, 1.0 }, { -t,  0.0, -1.0 }, { -t,  0.0,  1.0 }
		};

		return vertices;
	}

	@Override
	protected int[][] getGeometryFaces() 
	{
		int[][] faces = {
				{ 0, 11,  5 }, { 0,  5,  1 }, {  0,  1,  7 }, {  0,  7, 10 }, {  0, 10, 11 },
				{ 1,  5,  9 }, { 5, 11,  4 }, { 11, 10,  2 }, { 10,  7,  6 }, {  7,  1,  8 },
				{ 3,  9,  4 }, { 3,  4,  2 }, {  3,  2,  6 }, {  3,  6,  8 }, {  3,  8,  9 },
				{ 4,  9,  5 }, { 2,  4, 11 }, {  6,  2, 10 }, {  8,  6,  7 }, {  9,  8,  1 }
		};

		return faces;
	}
}
