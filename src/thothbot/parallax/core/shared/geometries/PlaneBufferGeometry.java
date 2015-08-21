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

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Uint16Array;
import thothbot.parallax.core.shared.core.BufferAttribute;
import thothbot.parallax.core.shared.core.BufferGeometry;

public class PlaneBufferGeometry extends BufferGeometry {

	public PlaneBufferGeometry(double width, double height) {
		this(width, height, 1, 1);
	}
	
	public PlaneBufferGeometry(double width, double height, int widthSegments, int heightSegments) {
		
		double width_half = width / 2.0;
		double height_half = height / 2.0;

		int gridX = widthSegments;
		int gridY = heightSegments;

		int gridX1 = gridX + 1;
		int gridY1 = gridY + 1;

		int segment_width = (int) (width / gridX);
		int segment_height = (int) (height / gridY);

		Float32Array vertices = Float32Array.create( gridX1 * gridY1 * 3 );
		Float32Array normals = Float32Array.create( gridX1 * gridY1 * 3 );
		Float32Array uvs = Float32Array.create( gridX1 * gridY1 * 2 );

		int offset = 0;
		int offset2 = 0;

		for ( int iy = 0; iy < gridY1; iy ++ ) {

			double y = iy * segment_height - height_half;

			for ( int ix = 0; ix < gridX1; ix ++ ) {

				double x = (double)(ix * segment_width) - width_half;

				vertices.set( offset,       x);
				vertices.set( offset + 1, - y);
				
				normals.set( offset + 2, 1.0);

				uvs.set( offset2, ix / (double)gridX);
				uvs.set( offset2 + 1, 1.0 - ( iy / (double)gridY ));

				offset += 3;
				offset2 += 2;

			}

		}

		offset = 0;

		Uint16Array indices = Uint16Array.create( gridX * gridY * 6 );

		for ( int iy = 0; iy < gridY; iy ++ ) {

			for ( int ix = 0; ix < gridX; ix ++ ) {

				int a = ix + gridX1 * iy;
				int b = ix + gridX1 * ( iy + 1 );
				int c = ( ix + 1 ) + gridX1 * ( iy + 1 );
				int d = ( ix + 1 ) + gridX1 * iy;

				indices.set( offset     , a );
				indices.set( offset + 1 , b );
				indices.set( offset + 2 , d );

				indices.set( offset + 3 , b );
				indices.set( offset + 4 , c );
				indices.set( offset + 5 , d );

				offset += 6;

			}

		}

		this.addAttribute( "index", new BufferAttribute( indices, 1 ) );
		this.addAttribute( "position", new BufferAttribute( vertices, 3 ) );
		this.addAttribute( "normal", new BufferAttribute( normals, 3 ) );
		this.addAttribute( "uv", new BufferAttribute( uvs, 2 ) );

	}
}
