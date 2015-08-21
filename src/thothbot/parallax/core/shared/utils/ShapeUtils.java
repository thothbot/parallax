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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.FastMap;
import thothbot.parallax.core.shared.math.Vector2;

import com.google.gwt.core.client.GWT;


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
	
	/**
	 * Remove holes from the Shape
	 * 
	 * @param contour the list of contour
	 * @param holes   the list of holes
	 * @param shape   the list of shapes, where will be created shape without holes
	 * @param allpoints 
	 * @param verts   the list of vertices, where will be created isolated faces
     */
	public static void removeHoles( List<Vector2> contour, List<List<Vector2>> holes, 
			List<Vector2> shape, List<Vector2> allpoints, List<List<Vector2>> verts ) 
	{
		for(Vector2 vector: contour)
		{
			shape.add(vector.clone());
			allpoints.add(vector.clone());	
		}
		
		/* For each isolated shape, find the closest points and break to the hole to allow triangulation */
		int holeIndex = -1;
		int shapeIndex = -1;

		for ( int h = 0; h < holes.size(); h ++ ) 
		{
			List<Vector2> hole = holes.get( h );
			allpoints.addAll(hole);
			
			double shortest = Double.POSITIVE_INFINITY;

			// Find the shortest pair of pts between shape and hole
			// Note: Actually, I'm not sure now if we could optimize this to be faster than O(m*n)
			// Using distanceToSquared() intead of distanceTo() should speed a little
			// since running square roots operations are reduced.

			for ( int h2 = 0; h2 < hole.size(); h2 ++ ) 
			{
				Vector2 pts1 = hole.get( h2 );
				List<Double> dist = new ArrayList<Double>();

				for ( int p = 0; p < shape.size(); p++ ) 
				{
					Vector2 pts2 = shape.get( p );
					double d = pts1.distanceToSquared( pts2 );
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
							: shape.size() - 1;
			int prevHoleVert = ( holeIndex - 1 ) >= 0 
					? holeIndex - 1 
							: hole.size() - 1;

			List<Vector2> areaapts = Arrays.asList(
				hole.get( holeIndex ),
				shape.get( shapeIndex ),
				shape.get( prevShapeVert )
			);

			double areaa = FontUtils.TriangulateArea( areaapts );

			List<Vector2> areabpts = Arrays.asList(
				hole.get( holeIndex ),
				hole.get( prevHoleVert ),
				shape.get( shapeIndex )
			);

			double areab = FontUtils.TriangulateArea( areabpts );

			int shapeOffset = 1;
			int holeOffset = -1;

			int oldShapeIndex = shapeIndex, oldHoleIndex = holeIndex;
			shapeIndex += shapeOffset;
			holeIndex += holeOffset;

			if ( shapeIndex < 0 ) 
				shapeIndex += shape.size();
			shapeIndex %= shape.size();

			if ( holeIndex < 0 ) 
				holeIndex += hole.size();
			holeIndex %= hole.size();

			prevShapeVert = ( shapeIndex - 1 ) >= 0 
					? shapeIndex - 1 
							: shape.size() - 1;
			prevHoleVert = ( holeIndex - 1 ) >= 0 
					? holeIndex - 1 
							: hole.size() - 1;

			areaapts = Arrays.asList(
				hole.get( holeIndex ),
				shape.get( shapeIndex ),
				shape.get( prevShapeVert )
			);

			double areaa2 = FontUtils.TriangulateArea( areaapts );

			areabpts = Arrays.asList(
				hole.get( holeIndex ),
				hole.get( prevHoleVert ),
				shape.get( shapeIndex )
			);

			double areab2 = FontUtils.TriangulateArea( areabpts );

			if ( ( areaa + areab ) > ( areaa2 + areab2 ) ) 
			{
				shapeIndex = oldShapeIndex;
				holeIndex = oldHoleIndex ;

				if ( shapeIndex < 0 ) 
					shapeIndex += shape.size();
				shapeIndex %= shape.size();

				if ( holeIndex < 0 ) 
					holeIndex += hole.size();
				holeIndex %= hole.size();

				prevShapeVert = ( shapeIndex - 1 ) >= 0 
						? shapeIndex - 1 
								: shape.size() - 1;
				prevHoleVert = ( holeIndex - 1 ) >= 0 
						? holeIndex - 1 
								: hole.size() - 1;
			} 
			else 
			{
				Log.error("ShapeUtils: removeHoles() ERROR");
			}

			List<Vector2> tmpShape1 = new ArrayList<Vector2>(shape.subList(0, shapeIndex));
			List<Vector2> tmpShape2 = new ArrayList<Vector2>(shape.subList(shapeIndex, shape.size()));

			List<Vector2> tmpHole1 = new ArrayList<Vector2>(hole.subList(holeIndex, hole.size()));
			List<Vector2> tmpHole2 = new ArrayList<Vector2>(hole.subList(0, holeIndex));

			// Should check orders here again?

			List<Vector2> trianglea = Arrays.asList(
				hole.get( holeIndex ),
				shape.get( shapeIndex ),
				shape.get( prevShapeVert )
			);

			List<Vector2> triangleb = Arrays.asList(
				hole.get( holeIndex ),
				hole.get( prevHoleVert ),
				shape.get( shapeIndex )
			);

			verts.add(trianglea);
			verts.add(triangleb);

			shape.removeAll(shape);

			shape.addAll(tmpShape1);
			shape.addAll(tmpHole1);
			shape.addAll(tmpHole2);
			shape.addAll(tmpShape2);
		}
	}

	/*
	 * @param contour List of {@link Vector2}
	 * @param holes
	 * 
	 * @return List<List<Integer>>
	 */
	public static List<List<Integer>> triangulateShape ( List<Vector2> contour, List<List<Vector2>> holes ) 
	{
		List<Vector2> shape             = new ArrayList<Vector2>();  
		List<Vector2> allpoints         = new ArrayList<Vector2>();
		List<List<Vector2>> isolatedPts = new ArrayList<List<Vector2>>();
		
		ShapeUtils.removeHoles( contour, holes, shape, allpoints, isolatedPts);

		 // True returns indices for points of spooled shape
		List<List<Vector2>> triangles   = new ArrayList<List<Vector2>>();
		List<List<Integer>> vertIndices = new ArrayList<List<Integer>>();

		FontUtils.triangulate( shape, triangles, vertIndices);

		// To maintain reference to old shape, one must match coordinates, 
		// or offset the indices from original arrays. It's probably easier 
		// to do the first.

		// prepare all points map
		Map<String, Integer> allPointsMap = GWT.isScript() ? 
				new FastMap<Integer>() : new HashMap<String, Integer>();

		for ( int i = 0, il = allpoints.size(); i < il; i ++ ) 
		{
			String key = allpoints.get( i ).getX() + ":" + allpoints.get( i ).getY();

			if ( allPointsMap.containsKey(key))
				Log.warn( "ShapeUtils: triangulateShape() - Duplicate point " + key );

			allPointsMap.put(key, i);
		}

		// check all face vertices against all points map
		List<List<Integer>> trianglesIndixes = new ArrayList<List<Integer>>();
		for ( int i = 0, il = triangles.size(); i < il; i ++ ) 
		{
			List<Vector2> face = triangles.get( i );
			trianglesIndixes.add(new ArrayList<Integer>());
			for ( int f = 0; f < 3; f ++ ) 
			{
				String key = face.get( f ).getX() + ":" + face.get( f ).getY();

				if ( allPointsMap.containsKey(key) )
					trianglesIndixes.get(i).add( f, allPointsMap.get(key));
			}
		}

		// check isolated points vertices against all points map
		List<List<Integer>> isolatedPtsIndixes = new ArrayList<List<Integer>>();
		for ( int i = 0, il = isolatedPts.size(); i < il; i ++ ) 
		{
			List<Vector2> face = isolatedPts.get( i );
			isolatedPtsIndixes.add(new ArrayList<Integer>());

			for ( int f = 0; f < 3; f ++ ) 
			{
				String key = face.get( f ).getX() + ":" + face.get( f ).getY();

				if ( allPointsMap.containsKey(key) )
					isolatedPtsIndixes.get(i).add( f, allPointsMap.get(key));
			}
		}

		trianglesIndixes.addAll( isolatedPtsIndixes );
		return trianglesIndixes;
	}

	public static boolean isClockWise( List<Vector2> pts ) 
	{
		return FontUtils.TriangulateArea( pts ) < 0;
	}

	/*
	 * Quad Bezier Functions
	 * 
	 * Bezier Curves formulas obtained from
	 * http://en.wikipedia.org/wiki/B%C3%A9zier_curve 
	 */
	public static double b2( double t, double p0, double p1, double p2 ) 
	{
		return b2p0( t, p0 ) + b2p1( t, p1 ) + b2p2( t, p2 );
	}

	private static double b2p0( double t, double p ) 
	{
		double k = 1.0 - t;
		return k * k * p;
	}

	private static double b2p1( double t, double p ) 
	{

		return 2.0 * ( 1.0 - t ) * t * p;

	}

	private static double b2p2( double t, double p  ) 
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
	public static double  b3( double t, double p0, double p1, double p2, double p3 ) 
	{
		return b3p0( t, p0 ) + b3p1( t, p1 ) + b3p2( t, p2 ) +  b3p3( t, p3 );
	}

	private static double b3p0( double t, double p ) 
	{
		double k = 1.0 - t;
		return k * k * k * p;
	}

	private static double b3p1( double t, double p )
	{
		double k = 1.0 - t;
		return 3.0 * k * k * t * p;
	}

	private static double b3p2( double t, double p ) 
	{
		double k = 1.0 - t;
		return 3.0 * k * t * t * p;
	}

	private static double b3p3( double t, double p ) 
	{
		return t * t * t * p;
	}
}
