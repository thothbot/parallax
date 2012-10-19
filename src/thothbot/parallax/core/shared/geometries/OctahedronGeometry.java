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

package thothbot.parallax.core.shared.geometries;

/**
 * <img src="http://thothbot.github.com/parallax/static/docs/octahedron.gif" />
 * <p>
 * Octahedron geometry
 * 
 * @author thothbot
 *
 */
public final class OctahedronGeometry extends PolyhedronGeometry
{
	public OctahedronGeometry( double radius, int detail ) 
	{
		super( radius, detail );
	}

	@Override
	protected double[][] getGeometryVertices() 
	{
		double[][] vertices = {
				{ 1.0, 0.0, 0.0 }, { -1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, -1.0, 0.0 }, { 0.0, 0.0, 1.0 }, { 0.0, 0.0, -1.0 }
		};

		return vertices;
	}

	@Override
	protected int[][] getGeometryFaces() 
	{
		int[][] faces = {
				{ 0, 2, 4 }, { 0, 4, 3 }, { 0, 3, 5 }, { 0, 5, 2 }, { 1, 2, 5 }, { 1, 5, 3 }, { 1, 3, 4 }, { 1, 4, 2 }
		};
		return faces;
	}
}
