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

public class Tetrahedron extends Polyhedron
{
	public Tetrahedron( int radius, int detail ) 
	{
		super(
				Tetrahedron.getDefaultVertices(),
				Tetrahedron.getDefaultFaces(), 
				radius, 
				detail
		);
	};
	
	private static Integer[][] getDefaultFaces()
	{
		Integer[][] faces = {
			{ 2, 1, 0 }, { 0, 3, 2 }, { 1, 3, 0 }, { 2, 3, 1 }
		};
		return faces;
	}

	private static Float[][] getDefaultVertices()
	{
		Float[][] vertices = {
			{ 1f,  1f,  1f }, { -1f, -1f, 1f }, { -1f, 1f, -1f }, { 1f, -1f, -1f }
		};
		
		return vertices;
	}
}
