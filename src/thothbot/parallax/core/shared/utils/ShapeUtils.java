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

package thothbot.parallax.core.shared.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.Vector2f;


/**
 * The class implements some 3D Shape related helper methods
 * 
 * The code is based on js-code written by zz85 
 * 
 * @author thothbot
 *
 */
public class ShapeUtils
{
	public static float shortest = Float.POSITIVE_INFINITY;
	
	/**
	 * Remove holes from the Shape
	 * 
	 * @param contour the list of contour
	 * @param holes   the list of holes
	 * @param shape   the list of shapes, where will be created shape without holes
	 * @param allpoints 
	 * @param verts   the list of vertices, where will be created isolated faces
     */
	public static void removeHoles( List<Vector2f> contour, List<List<Vector2f>> holes, 
			List<Vector2f> shape, List<Vector2f> allpoints, List<List<Vector2f>> verts ) 
	{
		/* For each isolated shape, find the closest points and break to the hole to allow triangulation */
		int holeIndex = -1;
		int shapeIndex = -1;
		for ( int h = 0; h < holes.size(); h ++ ) 
		{
			List<Vector2f> hole = holes.get( h );
			allpoints.addAll(hole);

			// Find the shortest pair of pts between shape and hole
			// Note: Actually, I'm not sure now if we could optimize this to be faster than O(m*n)
			// Using distanceToSquared() intead of distanceTo() should speed a little
			// since running square roots operations are reduced.

			for ( int h2 = 0; h2 < hole.size(); h2 ++ ) 
			{
				Vector2f pts1 = hole.get( h2 );
				List<Float> dist = new ArrayList<Float>();

				for ( int p = 0; p < contour.size(); p++ ) 
				{
					Vector2f pts2 = contour.get( p );
					float d = pts1.distanceToSquared( pts2 );
					dist.add( d );

					if ( d < shortest ) 
					{
						shortest = d;
						holeIndex = h2;
						shapeIndex = p;
					}
				}
			}

			int prevShapeVert = ( shapeIndex - 1 ) >= 0 
					? shapeIndex - 1 
							: contour.size() - 1;
			int prevHoleVert = ( holeIndex - 1 ) >= 0 
					? holeIndex - 1 
							: hole.size() - 1;

			List<Vector2f> areaapts = Arrays.asList(
				hole.get( holeIndex ),
				contour.get( shapeIndex ),
				contour.get( prevShapeVert )
			);

			double areaa = FontUtils.TriangulateArea( areaapts );

			List<Vector2f> areabpts = Arrays.asList(
				hole.get( holeIndex ),
				hole.get( prevHoleVert ),
				contour.get( shapeIndex )
			);

			double areab = FontUtils.TriangulateArea( areabpts );

			int shapeOffset = 1;
			int holeOffset = -1;

			int oldShapeIndex = shapeIndex, oldHoleIndex = holeIndex;
			shapeIndex += shapeOffset;
			holeIndex += holeOffset;

			if ( shapeIndex < 0 ) 
				shapeIndex += contour.size();
			shapeIndex %= contour.size();

			if ( holeIndex < 0 ) 
				holeIndex += hole.size();
			holeIndex %= hole.size();

			prevShapeVert = ( shapeIndex - 1 ) >= 0 
					? shapeIndex - 1 
							: contour.size() - 1;
			prevHoleVert = ( holeIndex - 1 ) >= 0 
					? holeIndex - 1 
							: hole.size() - 1;

			areaapts = Arrays.asList(
				hole.get( holeIndex ),
				contour.get( shapeIndex ),
				contour.get( prevShapeVert )
			);

			double areaa2 = FontUtils.TriangulateArea( areaapts );

			areabpts = Arrays.asList(
				hole.get( holeIndex ),
				hole.get( prevHoleVert ),
				contour.get( shapeIndex )
			);

			double areab2 = FontUtils.TriangulateArea( areabpts );

			if ( ( areaa + areab ) > ( areaa2 + areab2 ) ) 
			{
				shapeIndex = oldShapeIndex;
				holeIndex = oldHoleIndex ;

				if ( shapeIndex < 0 ) 
					shapeIndex += contour.size();
				shapeIndex %= contour.size();

				if ( holeIndex < 0 ) 
					holeIndex += hole.size();
				holeIndex %= hole.size();

				prevShapeVert = ( shapeIndex - 1 ) >= 0 
						? shapeIndex - 1 
								: contour.size() - 1;
				prevHoleVert = ( holeIndex - 1 ) >= 0 
						? holeIndex - 1 
								: hole.size() - 1;

			} 
			else 
			{
				Log.error("ShapeUtils: removeHoles() ERROR");
			}

			List<Vector2f> tmpShape1 = contour.subList(0, shapeIndex);
			List<Vector2f> tmpShape2 = contour.subList(shapeIndex, contour.size() - 1);

			List<Vector2f> tmpHole1 = hole.subList(holeIndex, hole.size() - 1);
			List<Vector2f> tmpHole2 = hole.subList(0, holeIndex);

			// Should check orders here again?

			List<Vector2f> trianglea = Arrays.asList(
				hole.get( holeIndex ),
				contour.get( shapeIndex ),
				contour.get( prevShapeVert )
			);

			List<Vector2f> triangleb = Arrays.asList(
				hole.get( holeIndex ),
				hole.get( prevHoleVert ),
				contour.get( shapeIndex )
			);

			verts = Arrays.asList(trianglea, triangleb);

			shape = new ArrayList<Vector2f>();
			shape.addAll(tmpShape1);
			shape.addAll(tmpHole1);
			shape.addAll(tmpHole2);
			shape.addAll(tmpShape2);
		}
	}

	/*
	 * @param contour List of {@link Vector2f}
	 * @param holes
	 * 
	 * @return List<List<Integer>>
	 */
	public static List<List<Integer>> triangulateShape ( List<Vector2f> contour, List<List<Vector2f>> holes ) 
	{
		List<Vector2f> shape = new ArrayList<Vector2f>();  
		List<Vector2f> allpoints = new ArrayList<Vector2f>();
		List<List<Vector2f>> isolatedPts = new ArrayList<List<Vector2f>>();
		
		ShapeUtils.removeHoles( contour, holes, shape, allpoints, isolatedPts);
Log.info("........" + shape + ", " + allpoints + ", " + isolatedPts);
		 // True returns indices for points of spooled shape
		List<List<Vector2f>> triangles = new ArrayList<List<Vector2f>>();
		List<List<Integer>> vertIndices = new ArrayList<List<Integer>>();
		FontUtils.triangulate( shape, triangles, vertIndices);

		// To maintain reference to old shape, one must match coordinates, 
		// or offset the indices from original arrays. It's probably easier 
		// to do the first.

		// prepare all points map
		Map<String, Integer> allPointsMap = new HashMap<String, Integer>();
		
		for ( int i = 0, il = allpoints.size(); i < il; i ++ ) 
		{
			String key = allpoints.get( i ).getX() + ":" + allpoints.get( i ).getY();

			if ( allPointsMap.containsKey(key))
				Log.warn( "ShapeUtils: triangulateShape() - Duplicate point " + key );

			allPointsMap.put(key, i);
		}

		// check all face vertices against all points map
		for ( int i = 0, il = triangles.size(); i < il; i ++ ) 
		{
			List<Vector2f> face = triangles.get( i );

			for ( int f = 0; f < 3; f ++ ) 
			{
				String key = face.get( f ).getX() + ":" + face.get( f ).getY();

//				if ( allPointsMap.containsKey(key) )
//					face.add( f, allPointsMap.get(key));
			}
		}

		// check isolated points vertices against all points map
		for ( int i = 0, il = isolatedPts.size(); i < il; i ++ ) 
		{
			List<Vector2f> face = isolatedPts.get( i );

			for ( int f = 0; f < 3; f ++ ) 
			{
				String key = face.get( f ).getX() + ":" + face.get( f ).getY();

//				if ( allPointsMap.containsKey(key) )
//					face.add( f, allPointsMap.get(key));
			}
		}

//		vertIndices.addAll( isolatedPts );
		return vertIndices;
	}


	public static boolean isClockWise( List<Vector2f> pts ) 
	{
		return FontUtils.TriangulateArea( pts ) < 0;
	}

	/*
	 * Quad Bezier Functions
	 * 
	 * Bezier Curves formulas obtained from
	 * http://en.wikipedia.org/wiki/B%C3%A9zier_curve 
	 */
	public static float b2( float t, float p0, float p1, float p2 ) 
	{
		return b2p0( t, p0 ) + b2p1( t, p1 ) + b2p2( t, p2 );
	}

	private static float b2p0( float t, float p ) 
	{

		float k = 1.0f - t;
		return k * k * p;

	}

	private static float b2p1( float t, float p ) 
	{

		return 2.0f * ( 1.0f - t ) * t * p;

	}

	private static float b2p2( float t, float p  ) 
	{
		return t * t * p;
	}

	/*
	 * Cubic Bezier Functions
	 * @param t
	 * @param p0 
	 * 		X or Y coordinate of vector 1
	 * @param p1
	 * 		X or Y coordinate of vector 2
	 * @param p2
	 * 		X or Y coordinate of vector 3
	 * @param p3
	 * 		X or Y coordinate of vector 4
	 */
	public static float  b3( float t, float p0, float p1, float p2, float p3 ) 
	{
		return b3p0( t, p0 ) + b3p1( t, p1 ) + b3p2( t, p2 ) +  b3p3( t, p3 );
	}

	private static float b3p0( float t, float p ) 
	{
		float k = 1.0f - t;
		return k * k * k * p;
	}

	private static float b3p1( float t, float p )
	{

		float k = 1.0f - t;
		return 3.0f * k * k * t * p;

	}

	private static Float b3p2( float t, float p ) 
	{
		float k = 1.0f - t;
		return 3.0f * k * t * t * p;
	}

	private static float b3p3( float t, float p ) 
	{
		return t * t * t * p;
	}
}
