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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thothbot.parallax.core.shared.core.BoundingSphere;
import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Face4;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.UVf;
import thothbot.parallax.core.shared.core.Vector3f;

/**
 * The Sphere geometry
 * <p>
 * <img src="http://thothbot.github.com/parallax/static/docs/sphere.gif" />
 * <p>
 * Based on the three.js code.
 * 
 * @author thothbot
 *
 */
public final class Sphere extends Geometry
{
	public Sphere() 
	{
		this(50, 8, 6);
	}

	public Sphere(int radius, int segmentsWidth, int segmentsHeight) 
	{
		this(radius, segmentsWidth, segmentsHeight, 0.0f, (float) (Math.PI * 2.0f), 0.0f, (float) Math.PI);
	}

	public Sphere(int radius, int segmentsWidth, int segmentsHeight, float phiStart, float phiLength, float thetaStart, float thetaLength) 
	{
		super();
		int segmentsX = Math.max( 3, segmentsWidth );
		int segmentsY = Math.max( 2, segmentsHeight );
		
		List<List<Integer>> vertices = new ArrayList<List<Integer>>();
		List<List<UVf>> uvs = new ArrayList<List<UVf>>();
		
		for (int y = 0; y <= segmentsY; y++) 
		{
			List<Integer> verticesRow = new ArrayList<Integer>();
			List<UVf> uvsRow = new ArrayList<UVf>();
			
			for (int x = 0; x <= segmentsX; x++) 
			{

				float u = (float) x / segmentsX;
				float v = (float) y / segmentsY;

				Vector3f vertex = new Vector3f();
				vertex.setX((float) (- radius * Math.cos( phiStart + u * phiLength ) * Math.sin( thetaStart + v * thetaLength )));
				vertex.setY((float) (radius * Math.cos( thetaStart + v * thetaLength )));
				vertex.setZ((float) (radius * Math.sin( phiStart + u * phiLength ) * Math.sin( thetaStart + v * thetaLength )));
				
				this.vertices.add( vertex );

				verticesRow.add( this.vertices.size() - 1 );
				uvsRow.add( new UVf( u, v ) );
			}
			
			vertices.add( verticesRow );
			uvs.add( uvsRow );
		}

		for ( int y = 0; y < segmentsY; y ++ ) {

			for ( int x = 0; x < segmentsX; x ++ ) {

				int v1 = vertices.get( y ).get( x + 1 );
				int v2 = vertices.get( y ).get( x );
				int v3 = vertices.get( y + 1 ).get( x );
				int v4 = vertices.get( y + 1 ).get( x + 1 );

				Vector3f n1 = this.vertices.get( v1 ).clone();
				n1.normalize();
				Vector3f n2 = this.vertices.get( v2 ).clone();
				n2.normalize();
				Vector3f n3 = this.vertices.get( v3 ).clone();
				n3.normalize();
				Vector3f n4 = this.vertices.get( v4 ).clone();
				n4.normalize();

				UVf uv1 = uvs.get( y ).get( x + 1 ).clone();
				UVf uv2 = uvs.get( y ).get( x ).clone();
				UVf uv3 = uvs.get( y + 1 ).get( x ).clone();
				UVf uv4 = uvs.get( y + 1 ).get( x + 1 ).clone();

				if ( Math.abs( this.vertices.get( v1 ).getY() ) == radius ) 
				{
					this.faces.add( new Face3( v1, v3, v4, Arrays.asList( n1, n3, n4 ) ) );
					this.faceVertexUvs.get( 0 ).add( Arrays.asList( uv1, uv3, uv4 ) );

				} else if ( Math.abs( this.vertices.get( v3 ).getY() ) ==  radius ) {

					this.faces.add( new Face3( v1, v2, v3, Arrays.asList( n1, n2, n3 ) ) );
					this.faceVertexUvs.get( 0 ).add( Arrays.asList( uv1, uv2, uv3 ) );

				} else {
					this.faces.add( new Face4( v1, v2, v3, v4, Arrays.asList( n1, n2, n3, n4 ) ) );
					this.faceVertexUvs.get( 0 ).add( Arrays.asList( uv1, uv2, uv3, uv4 ) );

				}

			}

		}

		this.computeCentroids();
		this.computeFaceNormals(false);

		this.boundingSphere = new BoundingSphere(radius);
	}
}
