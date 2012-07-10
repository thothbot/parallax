/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thothbot.squirrel.core.shared.Log;
import thothbot.squirrel.core.shared.core.Vector2f;


public class FontUtils
{
	public static double EPSILON = 0.0000000001;

	/*
	 * @param contour
	 * @param result 
	 * 			There will be generated List of List of {@link Vector2f}
	 * @param vertIndices 
	 * 			There will be generated List of List of integer
	 */
	public static void triangulate( List<Vector2f> contour, List<List<Vector2f>> result, List<List<Integer>> vertIndices ) 
	{
		int n = contour.size();

		if ( n < 3 ) 
			return;

		List<Integer> verts = new ArrayList<Integer>();
		
		if ( area( contour ) > 0.0 )
			for ( int v = 0; v < n; v++ ) 
				verts.add(v);

		else
			for ( int v = 0; v < n; v++ ) 
				verts.add(( n - 1 ) - v);

		/*  remove nv - 2 vertices, creating 1 triangle every time */
		int count = 2 * n;   /* error detection */
		int nv = n;
		for( int v = nv - 1; nv > 2; ) 
		{
			/* if we loop, it is probably a non-simple polygon */
			if ( ( count-- ) <= 0 ) 
			{
				//** Triangulate: ERROR - probable bad polygon!

				//throw ( "Warning, unable to triangulate polygon!" );
				//return null;
				// Sometimes warning is fine, especially polygons are triangulated in reverse.
				Log.warn("FaontUtils: triangulate() - Warning, unable to triangulate polygon!" );

				return;
			}

			/* three consecutive vertices in current polygon, <u,v,w> */

			int u = v; 	 	
			if ( nv <= u ) u = 0;     /* previous */
			
			v = u + 1;  
			if ( nv <= v ) v = 0;     /* new v    */
			
			int w = v + 1;  
			if ( nv <= w ) w = 0;     /* next     */

			if ( snip( contour, u, v, w, nv, verts ) ) 
			{
//				var a, b, c, s, t;

				/* true names of the vertices */
				int a = verts.get( u );
				int b = verts.get( v );
				int c = verts.get( w );

				/* output Triangle */

				/*
				result.push( contour[ a ] );
				result.push( contour[ b ] );
				result.push( contour[ c ] );
				*/
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
					verts.add( s, verts.get( t ));

				nv--;

				/* reset error detection counter */
				count = 2 * nv;
			}
		}
	}

	/*
	 * calculate area of the contour polygon
	 */
	public static double TriangulateArea(List<Vector2f> contour)
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
	private static double area( List<Vector2f> contour ) 
	{

		int n = contour.size();
		double a = 0.0;

		for( int p = n - 1, q = 0; q < n; p = q++ )
			a += contour.get( p ).getX() * contour.get( q ).getY() - contour.get( q ).getX() * contour.get( p ).getY();

		return a * 0.5;
	}
	
	private static boolean snip( List<Vector2f> contour, int u, int v, int w, int n, List<Integer> verts ) 
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

		  float aCROSSbp = aX*bpy - aY*bpx;
		  float cCROSSap = cX*apy - cY*apx;
		  float bCROSScp = bX*cpy - bY*cpx;

		  return ( (aCROSSbp >= 0.0f) && (bCROSScp >= 0.0f) && (cCROSSap >= 0.0f) );
	}
}
