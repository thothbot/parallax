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
import thothbot.parallax.core.shared.core.UV;
import thothbot.parallax.core.shared.core.Vector3;

/**
 * The Cylinder geometry
 * <p>
 * <img src="http://thothbot.github.com/parallax/static/docs/cylinder.gif" />
 * <p>
 * Based on the three.js code.
 * 
 * @author thothbot
 *
 */
public final class Cylinder extends Geometry
{

	public Cylinder() 
	{
		this(20, 20, 100, 8, 1);
	}
	
	public Cylinder(double radiusTop, double radiusBottom, double height, int segmentsRadius, int segmentsHeight)
	{
		this(radiusTop, radiusBottom, height, segmentsRadius, segmentsHeight, false);
	}

	public Cylinder(double radiusTop, double radiusBottom, double height, int segmentsRadius, int segmentsHeight, boolean openEnded) 
	{
		super();

		double heightHalf = height / 2.0;
		int segmentsX = segmentsRadius;
		int segmentsY = segmentsHeight;

		int x = 0, y = 0; 
		
		List<List<Integer>> vertices = new ArrayList<List<Integer>>();
		List<List<UV>> uvs = new ArrayList<List<UV>>();

		for ( y = 0; y <= segmentsY; y ++ ) 
		{
			List<Integer> verticesRow = new ArrayList<Integer>();
			List<UV> uvsRow = new ArrayList<UV>();

			double v = y / segmentsY * 1.0;
			double radius = v * ( radiusBottom - radiusTop ) + radiusTop;

			for ( x = 0; x <= segmentsX; x ++ ) 
			{
				double u = x / segmentsX * 1.0;

				Vector3 vertex = new Vector3();
				
				vertex.setX(radius * Math.sin( u * Math.PI * 2.0 ));
				vertex.setY(- v * height + heightHalf);
				vertex.setZ(radius * Math.cos( u * Math.PI * 2.0 ));

				getVertices().add( vertex );

				verticesRow.add( getVertices().size() - 1 );
				uvsRow.add( new UV( u, v ) );

			}

			vertices.add( verticesRow );
			uvs.add( uvsRow );
		}

		double tanTheta = ( radiusBottom - radiusTop ) / height;
		Vector3 na, nb;

		for ( x = 0; x < segmentsX; x ++ ) 
		{
			if ( radiusTop != 0 ) 
			{
				na = getVertices().get( vertices.get( 0 ).get( x ) ).clone();
				nb = getVertices().get( vertices.get( 0 ).get( x + 1 ) ).clone();
			} 
			else 
			{
				na = getVertices().get( vertices.get( 1 ).get( x ) ).clone();
				nb = getVertices().get( vertices.get( 1 ).get( x + 1 ) ).clone();
			}

			na.setY( Math.sqrt( na.getX() * na.getX() + na.getZ() * na.getZ() ) * tanTheta );
			na.normalize();
			nb.setY( Math.sqrt( nb.getX() * nb.getX() + nb.getZ() * nb.getZ() ) * tanTheta );
			nb.normalize();

			for ( y = 0; y < segmentsY; y ++ ) 
			{
				int v1 = vertices.get( y ).get( x );
				int v2 = vertices.get( y + 1 ).get( x );
				int v3 = vertices.get( y + 1 ).get( x + 1 );
				int v4 = vertices.get( y ).get( x + 1 );

				Vector3 n1 = na.clone();
				Vector3 n2 = na.clone();
				Vector3 n3 = nb.clone();
				Vector3 n4 = nb.clone();

				UV uv1 = uvs.get( y ).get( x ).clone();
				UV uv2 = uvs.get( y + 1 ).get( x ).clone();
				UV uv3 = uvs.get( y + 1 ).get( x + 1 ).clone();
				UV uv4 = uvs.get( y ).get( x + 1 ).clone();

				getFaces().add( new Face4( v1, v2, v3, v4, Arrays.asList( n1, n2, n3, n4 ) ) );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList( uv1, uv2, uv3, uv4 ) );

			}
		}

		// top cap

		if ( !openEnded && radiusTop > 0 ) 
		{
			getVertices().add( new Vector3( 0.0, heightHalf, 0.0 ) );

			for ( x = 0; x < segmentsX; x ++ ) 
			{
				int v1 = vertices.get( 0 ).get( x );
				int v2 = vertices.get( 0 ).get( x + 1 );
				int v3 = getVertices().size() - 1;

				Vector3 n1 = new Vector3( 0, 1, 0 );
				Vector3 n2 = new Vector3( 0, 1, 0 );
				Vector3 n3 = new Vector3( 0, 1, 0 );

				UV uv1 = uvs.get( 0 ).get( x ).clone();
				UV uv2 = uvs.get( 0 ).get( x + 1 ).clone();
				UV uv3 = new UV( uv2.getU(), 0 );

				getFaces().add( new Face3( v1, v2, v3, Arrays.asList( n1, n2, n3 ) ) );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList( uv1, uv2, uv3 ) );
			}
		}

		// bottom cap

		if ( !openEnded && radiusBottom > 0 ) 
		{
			getVertices().add( new Vector3( 0, - heightHalf, 0 ) );

			for ( x = 0; x < segmentsX; x ++ ) 
			{
				int v1 = vertices.get( y ).get( x + 1 );
				int v2 = vertices.get( y ).get( x );
				int v3 = getVertices().size() - 1;

				Vector3 n1 = new Vector3( 0.0, - 1.0, 0.0 );
				Vector3 n2 = new Vector3( 0.0, - 1.0, 0.0 );
				Vector3 n3 = new Vector3( 0.0, - 1.0, 0.0 );

				UV uv1 = uvs.get( y ).get( x + 1 ).clone();
				UV uv2 = uvs.get( y ).get( x ).clone();
				UV uv3 = new UV( uv2.getU(), 1 );

				getFaces().add( new Face3( v1, v2, v3, Arrays.asList( n1, n2, n3 ) ) );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList( uv1, uv2, uv3 ) );
			}
		}

		computeCentroids();
		computeFaceNormals(false);
	}
}
