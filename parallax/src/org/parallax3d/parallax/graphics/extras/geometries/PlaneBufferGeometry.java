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

package org.parallax3d.parallax.graphics.extras.geometries;

import org.parallax3d.parallax.system.BufferUtils;
import org.parallax3d.parallax.system.ThreeJsObject;
import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.BufferGeometry;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@ThreeJsObject("THREE.PlaneBufferGeometry")
public class PlaneBufferGeometry extends BufferGeometry {

	public PlaneBufferGeometry(float width, float height) {
		this(width, height, 1, 1);
	}
	
	public PlaneBufferGeometry(float width, float height, int widthSegments, int heightSegments) {
		
		float width_half = width / 2.0f;
		float height_half = height / 2.0f;

		int gridX = widthSegments;
		int gridY = heightSegments;

		int gridX1 = gridX + 1;
		int gridY1 = gridY + 1;

		int segment_width = (int) (width / gridX);
		int segment_height = (int) (height / gridY);

		FloatBuffer vertices = BufferUtils.newFloatBuffer(gridX1 * gridY1 * 3);
		FloatBuffer normals = BufferUtils.newFloatBuffer(gridX1 * gridY1 * 3);
		FloatBuffer uvs = BufferUtils.newFloatBuffer(gridX1 * gridY1 * 2);

		int offset = 0;
		int offset2 = 0;

		for ( int iy = 0; iy < gridY1; iy ++ ) {

			float y = iy * segment_height - height_half;

			for ( int ix = 0; ix < gridX1; ix ++ ) {

				float x = (float)(ix * segment_width) - width_half;

				vertices.put(offset, x);
				vertices.put(offset + 1, -y);
				
				normals.put(offset + 2, 1.0f);

				uvs.put(offset2, ix / (float) gridX);
				uvs.put(offset2 + 1, 1.0f - (iy / (float) gridY));

				offset += 3;
				offset2 += 2;

			}

		}

		offset = 0;

		IntBuffer indices = BufferUtils.newIntBuffer(gridX * gridY * 6);

		for ( int iy = 0; iy < gridY; iy ++ ) {

			for ( int ix = 0; ix < gridX; ix ++ ) {

				int a = ix + gridX1 * iy;
				int b = ix + gridX1 * ( iy + 1 );
				int c = ( ix + 1 ) + gridX1 * ( iy + 1 );
				int d = ( ix + 1 ) + gridX1 * iy;

				indices.put(offset, a);
				indices.put(offset + 1, b);
				indices.put(offset + 2, d);

				indices.put( offset + 3 , b );
				indices.put(offset + 4, c);
				indices.put(offset + 5, d);

				offset += 6;

			}

		}

		this.addAttribute( "index", new BufferAttribute( indices, 1 ) );
		this.addAttribute( "position", new BufferAttribute( vertices, 3 ) );
		this.addAttribute( "normal", new BufferAttribute( normals, 3 ) );
		this.addAttribute( "uv", new BufferAttribute( uvs, 2 ) );

	}
}
