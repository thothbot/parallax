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
import java.util.Collections;
import java.util.List;

import com.sun.deploy.util.ArrayUtil;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.math.Vector2;

/**
 * The class implements some 3D Shape related helper methods
 * 
 * The code is based on js-code written by zz85 
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.ShapeUtils")
public class ShapeUtils {

	public static double area(List<Vector2> contour) {

		int n = contour.size();
		double a = 0.0;

		for (int p = n - 1, q = 0; q < n; p = q++)
			a += contour.get(p).getX() * contour.get(q).getY() - contour.get(q).getX() * contour.get(p).getY();

		return a * 0.5;

	}

	public static List<List<Vector2>> triangulate(List<Vector2> contour)
	{
		return triangulate(contour, new ArrayList<List<Integer>>());
	}

	/**
	 * This code is a quick port of code written in C++ which was submitted to
	 * flipcode.com by John W. Ratcliff  // July 22, 2000
	 * See original code and more information here:
	 * http://www.flipcode.com/archives/Efficient_Polygon_Triangulation.shtml
	 *
	 * ported to actionscript by Zevan Rosser
	 * www.actionsnippet.com
	 *
	 * ported to javascript by Joshua Koo
	 * http://www.lab4games.net/zz85/blog
	 *
	 */
	public static List<List<Vector2>> triangulate(List<Vector2> contour, List<List<Integer>> vertIndices)
	{
		int n = contour.size();

		if ( n < 3 ) return null;

		List<List<Vector2>> result = new ArrayList<>();
		List<Integer> verts = new ArrayList<>();

		/* we want a counter-clockwise polygon in verts */

		if ( ShapeUtils.area( contour ) > 0.0 ) {

			for ( int v = 0; v < n; v ++ ) verts.set(v, v);

		} else {

			for ( int v = 0; v < n; v ++ ) verts.set(v, (n - 1) - v);

		}

		int nv = n;

		/*  remove nv - 2 vertices, creating 1 triangle every time */

		int count = 2 * nv;   /* error detection */

		for ( int v = nv - 1; nv > 2; ) {

			/* if we loop, it is probably a non-simple polygon */

			if ( ( count -- ) <= 0 ) {

				//** Triangulate: ERROR - probable bad polygon!

				//throw ( "Warning, unable to triangulate polygon!" );
				//return null;
				// Sometimes warning is fine, especially polygons are triangulated in reverse.
				Log.warn( "ShapeUtils: Unable to triangulate polygon! in triangulate()" );

				return result;

			}

			/* three consecutive vertices in current polygon, <u,v,w> */

			int u = v; 	 	if ( nv <= u ) u = 0;     /* previous */
			v = u + 1;  if ( nv <= v ) v = 0;     /* new v    */
			int w = v + 1;  if ( nv <= w ) w = 0;     /* next     */

			if ( snip( contour, u, v, w, nv, verts ) ) {

				/* true names of the vertices */

				int a = verts.get(u);
				int b = verts.get(v);
				int c = verts.get(w);

				/* output Triangle */

				result.add( Arrays.asList(contour.get(a),
						contour.get(b),
						contour.get(c)) );


				vertIndices.add( Arrays.asList(verts.get(u), verts.get(v), verts.get(w)) );

					/* remove v from the remaining polygon */

				for ( int s = v, t = v + 1; t < nv; s ++, t ++ ) {

					verts.set(s, verts.get(t));

				}

				nv --;

					/* reset error detection counter */

				count = 2 * nv;

			}

		}

		return result;
	}

	public static List<Vector2> triangulateShape(List<List<Vector2>> contour, List<List<Vector2>> holes )
	{
		FastMap<Integer> allPointsMap = new FastMap();

		// To maintain reference to old shape, one must match coordinates, or offset the indices from original arrays. It's probably easier to do the first.

		List<Vector2> allpoints = new ArrayList<>();
		for(int i = 0, l = contour.size(); i < l; i++)
			allpoints.addAll(contour.get(i));

		for(int i = 0, l = holes.size(); i < l; i++)
			allpoints.addAll(holes.get(i));

		// prepare all points map

		String key;
		for (int i = 0, il = allpoints.size(); i < il; i ++ ) {

			key = allpoints.get(i).getX() + ":" + allpoints.get(i).getY();

			if ( allPointsMap.containsKey( key ))
			{
				Log.warn( "Shape: Duplicate point " + key );
			}

			allPointsMap.put( key , i );

		}

		// remove holes by cutting paths to holes and adding them to the shape
		List<Vector2> shapeWithoutHoles = removeHoles( contour, holes );

		List<List<Integer>> vertIndices = new ArrayList<>();
		List<List<Vector2>> triangles = ShapeUtils.triangulate( shapeWithoutHoles, vertIndices ); // True returns indices for points of spooled shape
		//console.log( "triangles",triangles, triangles.length );

		// check all face vertices against all points map

//		for ( int i = 0, il = triangles.size(); i < il; i ++ ) {
//
//			List<Vector2> face = triangles.get(i);
//
//			for ( int f = 0; f < 3; f ++ ) {
//
//				key = face.get(f).getX() + ":" + face.get(f).getY();
//
//				if ( allPointsMap.containsKey(key) ) {
//
//					Integer index = allPointsMap.get( key );
//
//					face.set(f, index);
//
//				}
//
//			}
//
//		}

		List<Vector2> retval = new ArrayList<>();
		for(int i = 0, l = triangles.size(); i < l; i++)
			retval.addAll(triangles.get(i));

		return retval;
	}

	private static List<Vector2> removeHoles( List<List<Vector2>> contour, List<List<Vector2>> holes )
	{
		List<Vector2> shape = new ArrayList<>();
		for(int i = 0, l = contour.size(); i < l; i++)
			shape.addAll(contour.get(i));

		List<Integer> indepHoles = new ArrayList<>();

		for ( int h = 0, hl = holes.size(); h < hl; h ++ ) {

			indepHoles.add( h );

		}

		FastMap<Boolean> failedCuts = new FastMap<>();

		int minShapeIndex = 0;
		int counter = indepHoles.size() * 2;
		while ( indepHoles.size() > 0 )
		{
			counter --;
			if ( counter < 0 ) {

				Log.error( "Infinite Loop! Holes left:" + indepHoles.size() + ", Probably Hole outside Shape!" );
				break;

			}

			// search for shape-vertex and hole-vertex,
			// which can be connected without intersections
			for ( int shapeIndex = minShapeIndex; shapeIndex < shape.size(); shapeIndex ++ ) {

				Vector2 shapePt = shape.get(shapeIndex);
				int holeIndex	= - 1;

				// search for hole which can be reached without intersections
				for ( int h = 0; h < indepHoles.size(); h ++ ) {

					int holeIdx = indepHoles.get(h);

					// prevent multiple checks
					String cutKey = shapePt.getX() + ":" + shapePt.getY() + ":" + holeIdx;
					if ( failedCuts.containsKey( cutKey ) )
						continue;

					List<Vector2> hole = holes.get(holeIdx);
					for ( int h2 = 0; h2 < hole.size(); h2 ++ ) {

						Vector2 holePt = hole.get(h2);
						if ( ! isCutLineInsideAngles( shape, hole, shapeIndex, h2 ) )    continue;
						if ( intersectsShapeEdge( shape, shapePt, holePt ) )		     continue;
						if ( intersectsHoleEdge( holes, indepHoles, shapePt, holePt ) )	 continue;

						holeIndex = h2;

						indepHoles.remove( h );

						List<Vector2> tmpShape1 = shape.subList( 0, shapeIndex + 1 );
						List<Vector2> tmpShape2 = shape.subList( shapeIndex, shape.size() );
						List<Vector2> tmpHole1  = hole.subList( holeIndex, hole.size() );
						List<Vector2> tmpHole2  = hole.subList( 0, holeIndex + 1 );

						shape = new ArrayList<>(tmpShape1);
						shape.addAll( tmpHole1 );
						shape.addAll( tmpHole2 );
						shape.addAll( tmpShape2 );

						minShapeIndex = shapeIndex;

						// Debug only, to show the selected cuts
						// glob_CutLines.push( [ shapePt, holePt ] );

						break;

					}
					if ( holeIndex >= 0 )	break;		// hole-vertex found

					failedCuts.put( cutKey, true);			// remember failure

				}

				if ( holeIndex >= 0 )	break;		// hole-vertex found

			}

		}

		return shape; 			/* shape with no holes */
	}

	private static boolean intersectsHoleEdge(List<List<Vector2>> holes , List<Integer> indepHoles, Vector2 inShapePt, Vector2 inHolePt ) {

		// checks for intersections with hole edges
		for ( int ihIdx = 0; ihIdx < indepHoles.size(); ihIdx ++ ) {

			List<Vector2> chkHole = holes.get(indepHoles.get(ihIdx));
			for ( int hIdx = 0; hIdx < chkHole.size(); hIdx ++ ) {

				int nextIdx = hIdx + 1; nextIdx %= chkHole.size();
				List<Vector2> intersection = intersect_segments_2D( inShapePt, inHolePt, chkHole.get(hIdx), chkHole.get(nextIdx), true );
				if ( intersection != null && intersection.size() > 0 )		return	true;

			}

		}
		return	false;

	}

	private static boolean intersectsShapeEdge( List<Vector2> shape, Vector2 inShapePt, Vector2 inHolePt ) {

		// checks for intersections with shape edges
		for ( int sIdx = 0; sIdx < shape.size(); sIdx ++ ) {

			int nextIdx = sIdx + 1; nextIdx %= shape.size();
			List<Vector2> intersection = intersect_segments_2D( inShapePt, inHolePt, shape.get(sIdx), shape.get(nextIdx), true );
			if ( intersection != null && intersection.size() > 0 )		return	true;

		}

		return	false;

	}

	private static boolean point_in_segment_2D_colin( Vector2 inSegPt1, Vector2 inSegPt2, Vector2 inOtherPt ) {

		// inOtherPt needs to be collinear to the inSegment
		if ( inSegPt1.getX() != inSegPt2.getX()) {

			if ( inSegPt1.getX() < inSegPt2.getX()) {

				return	( ( inSegPt1.getX() <= inOtherPt.getX()) && ( inOtherPt.getX() <= inSegPt2.getX()) );

			} else {

				return	( ( inSegPt2.getX() <= inOtherPt.getX()) && ( inOtherPt.getX() <= inSegPt1.getX()) );

			}

		} else {

			if ( inSegPt1.getY() < inSegPt2.getY()) {

				return	( ( inSegPt1.getY() <= inOtherPt.getY()) && ( inOtherPt.getY() <= inSegPt2.getY()) );

			} else {

				return	( ( inSegPt2.getY() <= inOtherPt.getY()) && ( inOtherPt.getY() <= inSegPt1.getY()) );

			}

		}

	}

	private static List<Vector2> intersect_segments_2D( Vector2 inSeg1Pt1, Vector2 inSeg1Pt2, Vector2 inSeg2Pt1, Vector2 inSeg2Pt2, boolean inExcludeAdjacentSegs )
	{

		double seg1dx = inSeg1Pt2.getX() - inSeg1Pt1.getX(),   seg1dy = inSeg1Pt2.getY() - inSeg1Pt1.getY();
		double seg2dx = inSeg2Pt2.getX() - inSeg2Pt1.getX(),   seg2dy = inSeg2Pt2.getY() - inSeg2Pt1.getY();

		double seg1seg2dx = inSeg1Pt1.getX() - inSeg2Pt1.getX();
		double seg1seg2dy = inSeg1Pt1.getY() - inSeg2Pt1.getY();

		double limit		= seg1dy * seg2dx - seg1dx * seg2dy;
		double perpSeg1	= seg1dy * seg1seg2dx - seg1dx * seg1seg2dy;

		if ( Math.abs( limit ) > Mathematics.EPSILON ) {

			// not parallel

			double perpSeg2;
			if ( limit > 0 ) {

				if ( ( perpSeg1 < 0 ) || ( perpSeg1 > limit ) ) 		return null;
				perpSeg2 = seg2dy * seg1seg2dx - seg2dx * seg1seg2dy;
				if ( ( perpSeg2 < 0 ) || ( perpSeg2 > limit ) ) 		return null;

			} else {

				if ( ( perpSeg1 > 0 ) || ( perpSeg1 < limit ) ) 		return null;
				perpSeg2 = seg2dy * seg1seg2dx - seg2dx * seg1seg2dy;
				if ( ( perpSeg2 > 0 ) || ( perpSeg2 < limit ) ) 		return null;

			}

			// i.e. to reduce rounding errors
			// intersection at endpoint of segment#1?
			if ( perpSeg2 == 0 ) {

				if ( ( inExcludeAdjacentSegs ) &&
						( ( perpSeg1 == 0 ) || ( perpSeg1 == limit ) ) )		return null;
				return Arrays.asList( inSeg1Pt1 );

			}
			if ( perpSeg2 == limit ) {

				if ( ( inExcludeAdjacentSegs ) &&
						( ( perpSeg1 == 0 ) || ( perpSeg1 == limit ) ) )		return null;
				return Arrays.asList( inSeg1Pt2 );

			}
			// intersection at endpoint of segment#2?
			if ( perpSeg1 == 0 )		return  Arrays.asList( inSeg2Pt1 );
			if ( perpSeg1 == limit )	return  Arrays.asList( inSeg2Pt2 );

			// return real intersection point
			double factorSeg1 = perpSeg2 / limit;
			return	 Arrays.asList( new Vector2(inSeg1Pt1.getX() + factorSeg1 * seg1dx,
					inSeg1Pt1.getY() + factorSeg1 * seg1dy ));

		} else {

			// parallel or collinear
			if ( ( perpSeg1 != 0 ) ||
					( seg2dy * seg1seg2dx != seg2dx * seg1seg2dy ) ) 			return null;

			// they are collinear or degenerate
			boolean seg1Pt = ( ( seg1dx == 0 ) && ( seg1dy == 0 ) );	// segment1 is just a point?
			boolean seg2Pt = ( ( seg2dx == 0 ) && ( seg2dy == 0 ) );	// segment2 is just a point?
			// both segments are points
			if ( seg1Pt && seg2Pt ) {

				if ( ( inSeg1Pt1.getX() != inSeg2Pt1.getX()) ||
						( inSeg1Pt1.getY() != inSeg2Pt1.getY()) )		return null;	// they are distinct  points
				return Arrays.asList( inSeg1Pt1 );                 						// they are the same point

			}
			// segment#1  is a single point
			if ( seg1Pt ) {

				if ( ! point_in_segment_2D_colin( inSeg2Pt1, inSeg2Pt2, inSeg1Pt1 ) )		return null;		// but not in segment#2
				return Arrays.asList( inSeg1Pt1 );

			}
			// segment#2  is a single point
			if ( seg2Pt ) {

				if ( ! point_in_segment_2D_colin( inSeg1Pt1, inSeg1Pt2, inSeg2Pt1 ) )		return  null;		// but not in segment#1
				return Arrays.asList( inSeg2Pt1 );

			}

			// they are collinear segments, which might overlap
			Vector2 seg1min, seg1max; double seg1minVal, seg1maxVal;
			Vector2 seg2min, seg2max; double seg2minVal, seg2maxVal;
			if ( seg1dx != 0 ) {

				// the segments are NOT on a vertical line
				if ( inSeg1Pt1.getX() < inSeg1Pt2.getX()) {

					seg1min = inSeg1Pt1; seg1minVal = inSeg1Pt1.getX();
					seg1max = inSeg1Pt2; seg1maxVal = inSeg1Pt2.getX();

				} else {

					seg1min = inSeg1Pt2; seg1minVal = inSeg1Pt2.getX();
					seg1max = inSeg1Pt1; seg1maxVal = inSeg1Pt1.getX();

				}
				if ( inSeg2Pt1.getX() < inSeg2Pt2.getX()) {

					seg2min = inSeg2Pt1; seg2minVal = inSeg2Pt1.getX();
					seg2max = inSeg2Pt2; seg2maxVal = inSeg2Pt2.getX();

				} else {

					seg2min = inSeg2Pt2; seg2minVal = inSeg2Pt2.getX();
					seg2max = inSeg2Pt1; seg2maxVal = inSeg2Pt1.getX();

				}

			} else {

				// the segments are on a vertical line
				if ( inSeg1Pt1.getY() < inSeg1Pt2.getY()) {

					seg1min = inSeg1Pt1; seg1minVal = inSeg1Pt1.getY();
					seg1max = inSeg1Pt2; seg1maxVal = inSeg1Pt2.getY();

				} else {

					seg1min = inSeg1Pt2; seg1minVal = inSeg1Pt2.getY();
					seg1max = inSeg1Pt1; seg1maxVal = inSeg1Pt1.getY();

				}
				if ( inSeg2Pt1.getY() < inSeg2Pt2.getY()) {

					seg2min = inSeg2Pt1; seg2minVal = inSeg2Pt1.getY();
					seg2max = inSeg2Pt2; seg2maxVal = inSeg2Pt2.getY();

				} else {

					seg2min = inSeg2Pt2; seg2minVal = inSeg2Pt2.getY();
					seg2max = inSeg2Pt1; seg2maxVal = inSeg2Pt1.getY();

				}

			}
			if ( seg1minVal <= seg2minVal ) {

				if ( seg1maxVal <  seg2minVal )	return null;
				if ( seg1maxVal == seg2minVal )	{

					if ( inExcludeAdjacentSegs )		return null;
					return Arrays.asList( seg2min );

				}
				if ( seg1maxVal <= seg2maxVal )	return Arrays.asList( seg2min, seg1max );
				return	Arrays.asList( seg2min, seg2max );

			} else {

				if ( seg1minVal >  seg2maxVal )	return null;
				if ( seg1minVal == seg2maxVal )	{

					if ( inExcludeAdjacentSegs )		return null;
					return Arrays.asList( seg1min );

				}
				if ( seg1maxVal <= seg2maxVal )	return Arrays.asList( seg1min, seg1max );
				return	Arrays.asList( seg1min, seg2max );

			}

		}

	}

	private static boolean isPointInsideAngle( Vector2 inVertex, Vector2 inLegFromPt, Vector2 inLegToPt, Vector2 inOtherPt )
	{
		// The order of legs is important

		// translation of all points, so that Vertex is at (0,0)
		double legFromPtX	= inLegFromPt.getX() - inVertex.getX(),  legFromPtY	= inLegFromPt.getY() - inVertex.getY();
		double legToPtX	= inLegToPt.getX() - inVertex.getX(),  legToPtY		= inLegToPt.getY() - inVertex.getY();
		double otherPtX	= inOtherPt.getX() - inVertex.getX(),  otherPtY		= inOtherPt.getY() - inVertex.getY();

		// main angle >0: < 180 deg.; 0: 180 deg.; <0: > 180 deg.
		double from2toAngle	= legFromPtX * legToPtY - legFromPtY * legToPtX;
		double from2otherAngle	= legFromPtX * otherPtY - legFromPtY * otherPtX;

		if ( Math.abs( from2toAngle ) > Mathematics.EPSILON ) {

			// angle != 180 deg.

			double other2toAngle		= otherPtX * legToPtY - otherPtY * legToPtX;
			// console.log( "from2to: " + from2toAngle + ", from2other: " + from2otherAngle + ", other2to: " + other2toAngle );

			if ( from2toAngle > 0 ) {

				// main angle < 180 deg.
				return	( ( from2otherAngle >= 0 ) && ( other2toAngle >= 0 ) );

			} else {

				// main angle > 180 deg.
				return	( ( from2otherAngle >= 0 ) || ( other2toAngle >= 0 ) );

			}

		} else {

			// angle == 180 deg.
			// console.log( "from2to: 180 deg., from2other: " + from2otherAngle  );
			return	( from2otherAngle > 0 );

		}

	}

	private static boolean isCutLineInsideAngles( List<Vector2> shape, List<Vector2> hole, int inShapeIdx, int inHoleIdx )
	{

		// Check if hole point lies within angle around shape point
		int lastShapeIdx = shape.size() - 1;

		int prevShapeIdx = inShapeIdx - 1;
		if ( prevShapeIdx < 0 )			prevShapeIdx = lastShapeIdx;

		int nextShapeIdx = inShapeIdx + 1;
		if ( nextShapeIdx > lastShapeIdx )	nextShapeIdx = 0;

		boolean insideAngle = isPointInsideAngle(shape.get(inShapeIdx), shape.get(prevShapeIdx), shape.get(nextShapeIdx), hole.get(inHoleIdx));
		if ( ! insideAngle ) {

			// console.log( "Vertex (Shape): " + inShapeIdx + ", Point: " + hole[inHoleIdx].x + "/" + hole[inHoleIdx].y );
			return	false;

		}

		// Check if shape point lies within angle around hole point
		int lastHoleIdx = hole.size() - 1;

		int prevHoleIdx = inHoleIdx - 1;
		if ( prevHoleIdx < 0 )			prevHoleIdx = lastHoleIdx;

		int nextHoleIdx = inHoleIdx + 1;
		if ( nextHoleIdx > lastHoleIdx )	nextHoleIdx = 0;

		return isPointInsideAngle(hole.get(inHoleIdx), hole.get(prevHoleIdx), hole.get(nextHoleIdx), shape.get(inShapeIdx));
	}

	private static boolean snip( List<Vector2> contour, int u, int v, int w, int n, List<Integer> verts )
	{
		double ax = contour.get(verts.get(u)).getX();
		double ay = contour.get(verts.get(u)).getY();

		double bx = contour.get(verts.get(v)).getX();
		double by = contour.get(verts.get(v)).getY();

		double cx = contour.get(verts.get(w)).getX();
		double cy = contour.get(verts.get(w)).getY();

		if ( Mathematics.EPSILON > ( ( ( bx - ax ) * ( cy - ay ) ) - ( ( by - ay ) * ( cx - ax ) ) ) )
			return false;

		double aX = cx - bx;  double aY = cy - by;
		double bX = ax - cx;  double bY = ay - cy;
		double cX = bx - ax;  double cY = by - ay;

		for ( int p = 0; p < n; p ++ ) {

			double px = contour.get(verts.get(p)).getX();
			double py = contour.get(verts.get(p)).getY();

			if ( ( ( px == ax ) && ( py == ay ) ) ||
					( ( px == bx ) && ( py == by ) ) ||
					( ( px == cx ) && ( py == cy ) ) )	continue;

			double apx = px - ax;  double apy = py - ay;
			double bpx = px - bx;  double bpy = py - by;
			double cpx = px - cx;  double cpy = py - cy;

			// see if p is inside triangle abc

			double aCROSSbp = aX * bpy - aY * bpx;
			double cCROSSap = cX * apy - cY * apx;
			double bCROSScp = bX * cpy - bY * cpx;

			if ( ( aCROSSbp >= - Mathematics.EPSILON ) && ( bCROSScp >= - Mathematics.EPSILON ) && ( cCROSSap >= - Mathematics.EPSILON ) ) return false;

		}

		return true;

	}

	public static boolean isClockWise( List<Vector2> pts ) {

		return ShapeUtils.area( pts ) < 0;

	}

	// Bezier Curves formulas obtained from
	// http://en.wikipedia.org/wiki/B%C3%A9zier_curve

	// Quad Bezier Functions

	private static double b2( double t, double p0, double p1, double p2 )
	{
		return b2p0( t, p0 ) + b2p1( t, p1 ) + b2p2( t, p2 );
	}

	private static double b2p0( double t, double p ) {

		double k = 1. - t;
		return k * k * p;

	}

	private static double b2p1( double t, double p ) {

		return 2. * ( 1. - t ) * t * p;

	}

	private static double b2p2( double t, double p ) {

		return t * t * p;

	}

	// Cubic Bezier Functions
	private static double b3( double t, double p0, double p1, double p2, double p3  )
	{
		return b3p0( t, p0 ) + b3p1( t, p1 ) + b3p2( t, p2 ) + b3p3( t, p3 );
	}

	private static double b3p0( double t, double p ) {

		double k = 1. - t;
		return k * k * k * p;

	}

	private static double b3p1( double t, double p ) {

		double k = 1. - t;
		return 3. * k * k * t * p;

	}

	private static double  b3p2( double t, double p ) {

		double k = 1. - t;
		return 3. * k * t * t * p;

	}

	private static double b3p3( double t, double p ) {

		return t * t * t * p;

	}
}
