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
 * <img src="http://thothbot.github.com/parallax/static/docs/tetrahedron.gif" />
 * <p>
 * Tetrahedron geometry
 * 
 * @author thothbot
 *
 */
public class TetrahedronGeometry extends PolyhedronGeometry
{
	public TetrahedronGeometry( double radius, int detail ) 
	{
		super( radius, detail );
	}

	@Override
	protected double[][] getGeometryVertices() 
	{
		double[][] vertices = {
				{ 1.0,  1.0,  1.0 }, { -1.0, -1.0, 1.0 }, { -1.0, 1.0, -1.0 }, { 1.0, -1.0, -1.0 }
		};

		return vertices;
	}

	@Override
	protected int[][] getGeometryFaces() 
	{
		int[][] faces = {
				{ 2, 1, 0 }, { 0, 3, 2 }, { 1, 3, 0 }, { 2, 3, 1 }
		};
		return faces;
	}
}
