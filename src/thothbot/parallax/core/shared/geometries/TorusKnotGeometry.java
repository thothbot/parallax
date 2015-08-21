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

import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;

/**
 * <img src="http://thothbot.github.com/parallax/static/docs/torus_knot.gif" />
 * <p>
 * Torus knot geometry
 * 
 * @author thothbot
 *
 */
public final class TorusKnotGeometry extends Geometry
{

	public TorusKnotGeometry() 
	{
		this(200, 40, 64, 8);
	}
	
	public TorusKnotGeometry(int radius)
	{
		this(radius, 40, 64, 8);
	}
	
	public TorusKnotGeometry(int radius, int tube, int segmentsR, int segmentsT)
	{
		this(radius, tube, segmentsR, segmentsT, 2, 3, 1);
	}
			
	public TorusKnotGeometry(int radius, int tube, int radialSegments, int tubularSegments, int p, int q, int heightScale ) 
	{
		super();
		
		int[][] grid = new int[radialSegments][];

		Vector3 tang = new Vector3();
		Vector3 n = new Vector3();
		Vector3 bitan = new Vector3();

		for ( int i = 0; i < radialSegments; ++ i )  	
		{
			grid[ i ] = new int[tubularSegments];
			double u = i / (double)radialSegments * 2.0 * p * Math.PI;
			Vector3 p1 = getPos( u,        q, p, radius, heightScale );
			Vector3 p2 = getPos( u + 0.01, q, p, radius, heightScale );
			tang.sub( p2, p1 );
			n.add( p2, p1 );
			bitan.cross( tang, n );
			n.cross( bitan, tang );
			bitan.normalize();
			n.normalize();
			
			for ( int j = 0; j < tubularSegments; ++ j ) 
			{
				double v = j / (double)tubularSegments * 2.0 * Math.PI;
				double cx = -tube * Math.cos( v ); // TODO: Hack: Negating it so it faces outside.
				double cy = tube * Math.sin( v );

				Vector3 pos = new Vector3();
				pos.setX( p1.getX() + cx * n.getX() + cy * bitan.getX() );
				pos.setY( p1.getY() + cx * n.getY() + cy * bitan.getY() );
				pos.setZ( p1.getZ() + cx * n.getZ() + cy * bitan.getZ() );

				grid[ i ][ j ] = vert( pos );
			}
		}
		
		for ( int i = 0; i < radialSegments; ++ i ) 
		{
			for ( int j = 0; j < tubularSegments; ++ j ) 
			{
				int ip = ( i + 1 ) % radialSegments;
				int jp = ( j + 1 ) % tubularSegments;

				int a = grid[ i ][ j ];
				int b = grid[ ip ][ j ];
				int c = grid[ ip ][ jp ];
				int d = grid[ i ][ jp ];

				Vector2 uva = new Vector2(           i / (double)radialSegments,           j / (double)tubularSegments );
				Vector2 uvb = new Vector2( ( i + 1.0 ) / (double)radialSegments,           j / (double)tubularSegments );
				Vector2 uvc = new Vector2( ( i + 1.0 ) / (double)radialSegments, ( j + 1.0 ) / (double)tubularSegments );
				Vector2 uvd = new Vector2(           i / (double)radialSegments, ( j + 1.0 ) / (double)tubularSegments );
		
				getFaces().add( new Face3( a, b, d ) );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList( uva, uvb, uvd ) );

				getFaces().add( new Face3( b, c, d ) );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList( uvb.clone(), uvc, uvd.clone() ) );

			}
		}

		this.computeFaceNormals();
		this.computeVertexNormals();
	}
	
	private int vert( Vector3 pos ) 
	{
		getVertices().add( pos );
		return getVertices().size() - 1;
	}

	private Vector3 getPos( double u, int in_q, int in_p, int radius, int heightScale ) 
	{
		double cu = Math.cos( u );
		double su = Math.sin( u );
		double quOverP = in_q / (double)in_p * u;
		double cs = Math.cos( quOverP );

		return new Vector3( 
			radius * ( 2.0 + cs ) * cu * 0.5,
			radius * ( 2.0 + cs ) * su * 0.5,
			heightScale * radius * Math.sin( quOverP ) * 0.5
		);
	}
}
