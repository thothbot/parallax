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

import java.util.Arrays;

import thothbot.parallax.core.shared.core.Face4;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.UV;
import thothbot.parallax.core.shared.core.Vector3;

/**
 * The Plane geometry
 * <p>
 * <img src="http://thothbot.github.com/parallax/static/docs/plane.gif" />
 * <p>
 * Based on the three.js code.
 * 
 * @author thothbot
 *
 */
public final class Plane extends Geometry
{
	public Plane(int width, int height) 
	{
		this(width, height, 1, 1);
	}

	public Plane(int width, int depth, int segmentsWidth, int segmentsDepth) 
	{
		super();

		double width_half = width / 2.0;
		double depth_half = depth / 2.0;
		int gridX = segmentsWidth;
		int gridZ = segmentsDepth;
		int gridX1 = gridX + 1;
		int gridZ1 = gridZ + 1;
		double segment_width = width / gridX;
		double segment_depth = depth / gridZ;

		Vector3 normal = new Vector3( 0, 1, 0 );

		for ( int iz = 0; iz < gridZ1; iz ++ ) 
		{
			for ( int ix = 0; ix < gridX1; ix ++ ) 
			{
				double x = ix * segment_width - width_half;
				double z =  iz * segment_depth - depth_half;

				getVertices().add( new Vector3( x, 0, z ) );
			}
		}

		for ( int iz = 0; iz < gridZ; iz ++ ) 
		{
			for ( int ix = 0; ix < gridX; ix ++ ) 
			{
				int a = ix + gridX1 * iz;
				int b = ix + gridX1 * ( iz + 1 );
				int c = ( ix + 1 ) + gridX1 * ( iz + 1 );
				int d = ( ix + 1 ) + gridX1 * iz;

				Face4 face = new Face4( a, b, c, d );
				face.getNormal().copy( normal );
				face.getVertexNormals().addAll( Arrays.asList(normal.clone(), normal.clone(), normal.clone(), normal.clone()) );

				getFaces().add( face );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList(
					new UV( ix / gridX * 1.0,                     iz / gridZ * 1.0 ),
					new UV( ix / gridX * 1.0,           ( iz + 1.0 ) / gridZ * 1.0 ),
					new UV( ( ix + 1.0 ) / gridX * 1.0, ( iz + 1.0 ) / gridZ * 1.0 ),
					new UV( ( ix + 1.0 ) / gridX * 1.0,           iz / gridZ * 1.0 )
				) );
			}
		}

		this.computeCentroids();
	}
}
