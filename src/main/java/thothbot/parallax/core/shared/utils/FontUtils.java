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

package thothbot.parallax.core.shared.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.math.Vector2;


public class FontUtils
{
	public static final double EPSILON = 0.0000000001;

	/*
	 * @param contour
	 * @param result 
	 * 			There will be generated List of List of {@link Vector2}
	 * @param vertIndices 
	 * 			There will be generated List of List of integer
	 */
	public static void triangulate( List<Vector2> contour, List<List<Vector2>> result, List<List<Integer>> vertIndices ) 
	{
		int n = contour.size();

		if ( n < 3 ) 
			return;

		List<Integer> verts = new ArrayList<Integer>();

		if ( area( contour ) > 0.0 )
		{
			for ( int v = 0; v < n; v++ ) 
				verts.add(v);
		}
		else
		{
			for ( int v = 0; v < n; v++ ) 
				verts.add(( n - 1 ) - v);
		}

		int nv = n;
		/*  remove nv-2 Vertices, creating 1 triangle every time */
		int count = 2 * nv;   /* error detection */
		
		for( int v = nv-1; nv > 2; ) 
		{
			/* if we loop, it is probably a non-simple polygon */
			
			if ( 0 >= (count--) ) 
			{
				//** Triangulate: ERROR - probable bad polygon!

				//throw ( "Warning, unable to triangulate polygon!" );
				//return null;
				// Sometimes warning is fine, especially polygons are triangulated in reverse.
				Log.warn("FontUtils: triangulate() - Warning, unable to triangulate polygon!" );

				return;
			}

			/* three consecutive vertices in current polygon, <u,v,w> */

			int u = v; 	   if ( nv <= u ) u = 0;     /* previous */
			v = u + 1;     if ( nv <= v ) v = 0;     /* new v    */
			int w = v + 1; if ( nv <= w ) w = 0;     /* next     */

			if ( snip( contour, u, v, w, nv, verts ) ) 
			{
				/* true names of the vertices */
				int a = verts.get( u );
				int b = verts.get( v );
				int c = verts.get( w );

				/* output Triangle */

				result.add( Arrays.asList(
					contour.get( a ),
					contour.get( b ),
					contour.get( c ) ) );

				vertIndices.add( Arrays.asList(
						verts.get( u ), 
						verts.get( v ), 
						verts.get( w ) ) );

				/* remove v from the remaining polygon */

				for( int s = v, t = v + 1; t < nv; s++, t++ )
					verts.set( s, verts.get( t ));
				nv--;
				
				/* reset error detection counter */
				count = 2 * nv;
			}
		}
	}

	/*
	 * calculate area of the contour polygon
	 */
	public static double TriangulateArea(List<Vector2> contour)
	{
		int n = contour.size();
		double a = 0.0;

		for( int p = n - 1, q = 0; q < n; p = q++ )
			a += contour.get( p ).getX() * contour.get( q ).getY() - contour.get( q ).getX() * contour.get( p ).getY();

		return a * 0.5;
	}

	/*
	 * Calculate area of the contour polygon
	 */
	private static double area( List<Vector2> contour ) 
	{

		int n = contour.size();
		double a = 0.0;

		for( int p = n - 1, q = 0; q < n; p = q++ )
			a += contour.get( p ).getX() * contour.get( q ).getY() - contour.get( q ).getX() * contour.get( p ).getY();

		return a * 0.5;
	}
	
	private static boolean snip( List<Vector2> contour, int u, int v, int w, int n, List<Integer> verts ) 
	{
		double ax = contour.get( verts.get( u ) ).getX();
		double ay = contour.get( verts.get( u ) ).getY();

		double bx = contour.get( verts.get( v ) ).getX();
		double by = contour.get( verts.get( v ) ).getY();

		double cx = contour.get( verts.get( w ) ).getX();
		double cy = contour.get( verts.get( w ) ).getY();

		if ( EPSILON > (((bx - ax) * (cy - ay)) - ((by - ay) * (cx - ax))) ) 
			return false;

		for ( int p = 0; p < n; p++ ) 
		{
			if( (p == u) || (p == v) || (p == w) ) 
				continue;

			double px = contour.get( verts.get( p ) ).getX();
			double py = contour.get( verts.get( p ) ).getY();

			if ( insideTriangle( ax, ay, bx, by, cx, cy, px, py ) ) 
				return false;
		}
		return true;
	}
	
	/*
	 * see if p is inside triangle abc
	 */
	public static boolean insideTriangle( double ax, double ay,
			double bx, double by,
			double cx, double cy,
			double px, double py 
	) {
		  double aX = cx - bx;  double aY = cy - by;
		  double bX = ax - cx;  double bY = ay - cy;
		  double cX = bx - ax;  double cY = by - ay;
		  double apx= px  -ax;  double apy= py - ay;
		  double bpx= px - bx;  double bpy= py - by;
		  double cpx= px - cx;  double cpy= py - cy;

		  double aCROSSbp = aX * bpy - aY * bpx;
		  double cCROSSap = cX * apy - cY * apx;
		  double bCROSScp = bX * cpy - bY * cpx;

		  return ( (aCROSSbp >= 0.0) && (bCROSScp >= 0.0) && (cCROSSap >= 0.0) );
	}
}
