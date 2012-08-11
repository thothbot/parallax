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
import thothbot.parallax.core.shared.core.UV;
import thothbot.parallax.core.shared.core.Vector3;

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
		this(50);
	}

	public Sphere(int radius) 
	{
		this(radius, 8, 6);
	}
	
	public Sphere(int radius, int segmentsWidth, int segmentsHeight) 
	{
		this(radius, segmentsWidth, segmentsHeight, 0.0, Math.PI * 2.0 );
	}
	
	public Sphere(int radius, int segmentsWidth, int segmentsHeight, double phiStart, double phiLength)
	{
		this(radius, segmentsWidth, segmentsHeight, phiStart, phiLength, 0.0, Math.PI);
	}

	public Sphere(int radius, int segmentsWidth, int segmentsHeight, double phiStart, double phiLength, double thetaStart, double thetaLength) 
	{
		super();
		int segmentsX = Math.max( 3, segmentsWidth );
		int segmentsY = Math.max( 2, segmentsHeight );
		
		List<List<Integer>> vertices = new ArrayList<List<Integer>>();
		List<List<UV>> uvs = new ArrayList<List<UV>>();
		
		for (int y = 0; y <= segmentsY; y++) 
		{
			List<Integer> verticesRow = new ArrayList<Integer>();
			List<UV> uvsRow = new ArrayList<UV>();
			
			for (int x = 0; x <= segmentsX; x++) 
			{

				double u = x / (double)segmentsX;
				double v = y / (double)segmentsY;

				Vector3 vertex = new Vector3();
				vertex.setX(- radius * Math.cos( phiStart + u * phiLength ) * Math.sin( thetaStart + v * thetaLength ));
				vertex.setY(radius * Math.cos( thetaStart + v * thetaLength ));
				vertex.setZ(radius * Math.sin( phiStart + u * phiLength ) * Math.sin( thetaStart + v * thetaLength ));
				
				getVertices().add( vertex );

				verticesRow.add( getVertices().size() - 1 );
				uvsRow.add( new UV( u, v ) );
			}
			
			vertices.add( verticesRow );
			uvs.add( uvsRow );
		}

		for ( int y = 0; y < segmentsY; y ++ ) 
		{
			for ( int x = 0; x < segmentsX; x ++ ) 
			{
				int v1 = vertices.get( y ).get( x + 1 );
				int v2 = vertices.get( y ).get( x );
				int v3 = vertices.get( y + 1 ).get( x );
				int v4 = vertices.get( y + 1 ).get( x + 1 );

				Vector3 n1 = getVertices().get( v1 ).clone();
				n1.normalize();
				Vector3 n2 = getVertices().get( v2 ).clone();
				n2.normalize();
				Vector3 n3 = getVertices().get( v3 ).clone();
				n3.normalize();
				Vector3 n4 = getVertices().get( v4 ).clone();
				n4.normalize();

				UV uv1 = uvs.get( y ).get( x + 1 ).clone();
				UV uv2 = uvs.get( y ).get( x ).clone();
				UV uv3 = uvs.get( y + 1 ).get( x ).clone();
				UV uv4 = uvs.get( y + 1 ).get( x + 1 ).clone();

				if ( Math.abs( getVertices().get( v1 ).getY() ) == radius ) 
				{
					getFaces().add( new Face3( v1, v3, v4, Arrays.asList( n1, n3, n4 ) ) );
					getFaceVertexUvs().get( 0 ).add( Arrays.asList( uv1, uv3, uv4 ) );
				} 
				else if ( Math.abs( getVertices().get( v3 ).getY() ) ==  radius ) 
				{
					getFaces().add( new Face3( v1, v2, v3, Arrays.asList( n1, n2, n3 ) ) );
					getFaceVertexUvs().get( 0 ).add( Arrays.asList( uv1, uv2, uv3 ) );
				} 
				else 
				{
					getFaces().add( new Face4( v1, v2, v3, v4, Arrays.asList( n1, n2, n3, n4 ) ) );
					getFaceVertexUvs().get( 0 ).add( Arrays.asList( uv1, uv2, uv3, uv4 ) );
				}
			}
		}

		this.computeCentroids();
		this.computeFaceNormals(false);

		setBoundingSphere( new BoundingSphere(radius) );
	}
}
