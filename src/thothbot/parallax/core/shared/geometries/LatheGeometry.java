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

import java.util.Arrays;
import java.util.List;

import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;

public final class LatheGeometry extends Geometry
{
	
	public LatheGeometry ( List<Vector3> points) 
	{
		this(points, 12);
	}
	
	public LatheGeometry ( List<Vector3> points, int steps)
	{
		this(points, steps, 0, 2.0 * Math.PI);
	}

	public LatheGeometry ( List<Vector3> points, int segments, double phiStart, double phiLength ) 
	{
		super();

		double inversePointLength = 1.0 / ( points.size() - 1 );
		double inverseSegments = 1.0 / segments;

		for ( int i = 0; i <= segments; i ++ ) 
		{
			double phi = phiStart + i * inverseSegments * phiLength;

			double c = Math.cos( phi ),
				s = Math.sin( phi );

			for ( int j = 0; j < points.size(); j ++ ) 
			{
				Vector3 pt = points.get( j );

				Vector3 vertex = new Vector3();

				vertex.setX( c * pt.getX() - s * pt.getY() );
				vertex.setY( s * pt.getX() + c * pt.getY() );
				vertex.setZ( pt.getZ() );

				getVertices().add( vertex );
			}
		}
		
		int np = points.size();

		for ( int i = 0; i < segments; i ++ ) 
		{
			for ( int j = 0, jl = points.size(); j < jl - 1; j ++ ) 
			{
				int base = j + np * i;
				int a = base;
				int b = base + np;
				int c = base + 1 + np;
				int d = base + 1;
				
				double u0 = i * inverseSegments;
				double v0 = j * inversePointLength;
				double u1 = u0 + inverseSegments;
				double v1 = v0 + inversePointLength;

				getFaces().add( new Face3( a, b, d ) );

				getFaceVertexUvs().get( 0 ).add( Arrays.asList(

						new Vector2( u0, v0 ),
				        new Vector2( u1, v0 ),
				        new Vector2( u0, v1 )

				) );

				getFaces().add( new Face3( b, c, d ) );

				getFaceVertexUvs().get( 0 ).add( Arrays.asList(

						new Vector2( u1, v0 ),
				        new Vector2( u1, v1 ),
				        new Vector2( u0, v1 )

				) );
			}
		}

		this.mergeVertices();
		this.computeFaceNormals();
		this.computeVertexNormals();
	}

}
