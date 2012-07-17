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
import thothbot.parallax.core.shared.core.UVf;
import thothbot.parallax.core.shared.core.Vector3f;

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
	public Plane(int width, int height) {
		this(width, height, 1, 1);
	}

	public Plane(int width, int depth, int segmentsWidth, int segmentsDepth) {
		super();

		float width_half = (float)width / 2f;
		float depth_half = (float)depth / 2f;
		int gridX = segmentsWidth;
		int gridZ = segmentsDepth;
		int gridX1 = gridX + 1;
		int gridZ1 = gridZ + 1;
		float segment_width = width / gridX;
		float segment_depth = depth / gridZ;

		Vector3f normal = new Vector3f( 0, 1, 0 );

		for ( int iz = 0; iz < gridZ1; iz ++ ) 
		{
			for ( int ix = 0; ix < gridX1; ix ++ ) 
			{

				float x = (float)(ix * segment_width - width_half);
				float z = (float)(iz * segment_depth - depth_half);

				this.vertices.add( new Vector3f( x, 0, z ) );

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

				this.faces.add( face );
				this.faceVertexUvs.get( 0 ).add( Arrays.asList(
					new UVf( ix / (float)gridX,         iz / (float)gridZ ),
					new UVf( ix / (float)gridX,         ( iz + 1 ) / (float)gridZ ),
					new UVf( ( ix + 1 ) / (float)gridX, ( iz + 1 ) / (float)gridZ ),
					new UVf( ( ix + 1 ) / (float)gridX, iz / (float)gridZ )
				) );

			}

		}

		this.computeCentroids();
	}
}
