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

import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Face4;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.UVf;
import thothbot.parallax.core.shared.core.Vector3f;


public final class Cylinder extends Geometry
{

	public Cylinder() 
	{
		this(20f, 20f, 100f, 8, 1);
	}
	
	public Cylinder(float radiusTop, float radiusBottom, float height, int segmentsRadius, int segmentsHeight)
	{
		this(radiusTop, radiusBottom, height, segmentsRadius, segmentsHeight, false);
	}

	public Cylinder(float radiusTop, float radiusBottom, float height, int segmentsRadius, int segmentsHeight, boolean openEnded) 
	{
		super();

		float heightHalf = (float) height / 2.0f;
		int segmentsX = segmentsRadius;
		int segmentsY = segmentsHeight;

		int x = 0, y = 0; 
		
		List<List<Integer>> vertices = new ArrayList<List<Integer>>();
		List<List<UVf>> uvs = new ArrayList<List<UVf>>();

		for ( y = 0; y <= segmentsY; y ++ ) 
		{
			List<Integer> verticesRow = new ArrayList<Integer>();
			List<UVf> uvsRow = new ArrayList<UVf>();

			float v = (float) y / segmentsY;
			float radius = v * ( radiusBottom - radiusTop ) + radiusTop;

			for ( x = 0; x <= segmentsX; x ++ ) 
			{
				float u = (float) x / segmentsX;

				Vector3f vertex = new Vector3f();
				
				vertex.setX((float) (radius * Math.sin( u * Math.PI * 2 )));
				vertex.setY(- v * height + heightHalf);
				vertex.setZ((float) (radius * Math.cos( u * Math.PI * 2 )));

				this.vertices.add( vertex );

				verticesRow.add( this.vertices.size() - 1 );
				uvsRow.add( new UVf( u, v ) );

			}

			vertices.add( verticesRow );
			uvs.add( uvsRow );
		}

		float tanTheta = ( radiusBottom - radiusTop ) / height;
		Vector3f na, nb;

		for ( x = 0; x < segmentsX; x ++ ) 
		{

			if ( radiusTop != 0 ) {

				na = this.vertices.get( vertices.get( 0 ).get( x ) ).clone();
				nb = this.vertices.get( vertices.get( 0 ).get( x + 1 ) ).clone();

			} else {

				na = this.vertices.get( vertices.get( 1 ).get( x ) ).clone();
				nb = this.vertices.get( vertices.get( 1 ).get( x + 1 ) ).clone();

			}

			na.setY( (float) (Math.sqrt( na.getX() * na.getX() + na.getZ() * na.getZ() ) * tanTheta) );
			na.normalize();
			nb.setY( (float) (Math.sqrt( nb.getX() * nb.getX() + nb.getZ() * nb.getZ() ) * tanTheta) );
			nb.normalize();

			for ( y = 0; y < segmentsY; y ++ ) {

				int v1 = vertices.get( y ).get( x );
				int v2 = vertices.get( y + 1 ).get( x );
				int v3 = vertices.get( y + 1 ).get( x + 1 );
				int v4 = vertices.get( y ).get( x + 1 );

				Vector3f n1 = na.clone();
				Vector3f n2 = na.clone();
				Vector3f n3 = nb.clone();
				Vector3f n4 = nb.clone();

				UVf uv1 = uvs.get( y ).get( x ).clone();
				UVf uv2 = uvs.get( y + 1 ).get( x ).clone();
				UVf uv3 = uvs.get( y + 1 ).get( x + 1 ).clone();
				UVf uv4 = uvs.get( y ).get( x + 1 ).clone();

				this.faces.add( new Face4( v1, v2, v3, v4, Arrays.asList( n1, n2, n3, n4 ) ) );
				this.faceVertexUvs.get( 0 ).add( Arrays.asList( uv1, uv2, uv3, uv4 ) );

			}
		}

		// top cap

		if ( !openEnded && radiusTop > 0 ) {

			this.vertices.add( new Vector3f( 0.0f, heightHalf, 0.0f ) );

			for ( x = 0; x < segmentsX; x ++ ) {

				int v1 = vertices.get( 0 ).get( x );
				int v2 = vertices.get( 0 ).get( x + 1 );
				int v3 = this.vertices.size() - 1;

				Vector3f n1 = new Vector3f( 0, 1, 0 );
				Vector3f n2 = new Vector3f( 0, 1, 0 );
				Vector3f n3 = new Vector3f( 0, 1, 0 );

				UVf uv1 = uvs.get( 0 ).get( x ).clone();
				UVf uv2 = uvs.get( 0 ).get( x + 1 ).clone();
				UVf uv3 = new UVf( uv2.getU(), 0 );

				this.faces.add( new Face3( v1, v2, v3, Arrays.asList( n1, n2, n3 ) ) );
				this.faceVertexUvs.get( 0 ).add( Arrays.asList( uv1, uv2, uv3 ) );

			}

		}

		// bottom cap

		if ( !openEnded && radiusBottom > 0 ) {

			this.vertices.add( new Vector3f( 0, - heightHalf, 0 ) );

			for ( x = 0; x < segmentsX; x ++ ) {

				int v1 = vertices.get( y ).get( x + 1 );
				int v2 = vertices.get( y ).get( x );
				int v3 = this.vertices.size() - 1;

				Vector3f n1 = new Vector3f( 0.0f, - 1.0f, 0.0f );
				Vector3f n2 = new Vector3f( 0.0f, - 1.0f, 0.0f );
				Vector3f n3 = new Vector3f( 0.0f, - 1.0f, 0.0f );

				UVf uv1 = uvs.get( y ).get( x + 1 ).clone();
				UVf uv2 = uvs.get( y ).get( x ).clone();
				UVf uv3 = new UVf( uv2.getU(), 1 );

				this.faces.add( new Face3( v1, v2, v3, Arrays.asList( n1, n2, n3 ) ) );
				this.faceVertexUvs.get( 0 ).add( Arrays.asList( uv1, uv2, uv3 ) );

			}

		}

		computeCentroids();
		computeFaceNormals(false);
	}
}
