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

package org.parallax3d.parallax.graphics.extras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.parallax3d.parallax.Parallax;
import org.parallax3d.parallax.math.Vector2;

public class FontUtils
{
	public static final float EPSILON = 0.0000000001f;

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
				Parallax.app.error("FontUtils", "triangulate() - Warning, unable to triangulate polygon!");

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
	public static float TriangulateArea(List<Vector2> contour)
	{
		int n = contour.size();
		float a = 0.0f;

		for( int p = n - 1, q = 0; q < n; p = q++ )
			a += contour.get( p ).getX() * contour.get( q ).getY() - contour.get( q ).getX() * contour.get( p ).getY();

		return a * 0.5f;
	}

	/*
	 * Calculate area of the contour polygon
	 */
	private static float area( List<Vector2> contour ) 
	{

		int n = contour.size();
		float a = 0.0f;

		for( int p = n - 1, q = 0; q < n; p = q++ )
			a += contour.get( p ).getX() * contour.get( q ).getY() - contour.get( q ).getX() * contour.get( p ).getY();

		return a * 0.5f;
	}
	
	private static boolean snip( List<Vector2> contour, int u, int v, int w, int n, List<Integer> verts ) 
	{
		float ax = contour.get( verts.get( u ) ).getX();
		float ay = contour.get( verts.get( u ) ).getY();

		float bx = contour.get( verts.get( v ) ).getX();
		float by = contour.get( verts.get( v ) ).getY();

		float cx = contour.get( verts.get( w ) ).getX();
		float cy = contour.get( verts.get( w ) ).getY();

		if ( EPSILON > (((bx - ax) * (cy - ay)) - ((by - ay) * (cx - ax))) ) 
			return false;

		for ( int p = 0; p < n; p++ ) 
		{
			if( (p == u) || (p == v) || (p == w) ) 
				continue;

			float px = contour.get( verts.get( p ) ).getX();
			float py = contour.get( verts.get( p ) ).getY();

			if ( insideTriangle( ax, ay, bx, by, cx, cy, px, py ) ) 
				return false;
		}
		return true;
	}
	
	/*
	 * see if p is inside triangle abc
	 */
	public static boolean insideTriangle( float ax, float ay,
			float bx, float by,
			float cx, float cy,
			float px, float py 
	) {
		  float aX = cx - bx;  float aY = cy - by;
		  float bX = ax - cx;  float bY = ay - cy;
		  float cX = bx - ax;  float cY = by - ay;
		  float apx= px  -ax;  float apy= py - ay;
		  float bpx= px - bx;  float bpy= py - by;
		  float cpx= px - cx;  float cpy= py - cy;

		  float aCROSSbp = aX * bpy - aY * bpx;
		  float cCROSSap = cX * apy - cY * apx;
		  float bCROSScp = bX * cpy - bY * cpx;

		  return ( (aCROSSbp >= 0.0) && (bCROSScp >= 0.0) && (cCROSSap >= 0.0) );
	}
}
