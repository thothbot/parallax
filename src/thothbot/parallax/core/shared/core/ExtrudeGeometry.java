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

package thothbot.parallax.core.shared.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.curves.Curve;
import thothbot.parallax.core.shared.curves.CurvePath;
import thothbot.parallax.core.shared.curves.FrenetFrames;
import thothbot.parallax.core.shared.curves.Shape;
import thothbot.parallax.core.shared.math.Box3;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.utils.ShapeUtils;


/**
 * Creates extruded geometry from a path shape.
 * <p>
 * Based on the three.js code.
 */
public class ExtrudeGeometry extends Geometry
{
	public static class ExtrudeGeometryParameters
	{
		// size of the text
		public double size;
		// thickness to extrude text
		public double height;
		// number of points on the curves
		public int curveSegments = 12;
		// number of points for z-side extrusions / used for subdividing segements of extrude spline too
		public int steps = 1;
		// Amount
		public int amount = 100;

		// turn on bevel
		public boolean bevelEnabled = true;
		// how deep into text bevel goes
		public double bevelThickness = 6;
		// how far from text outline is bevel
		public double bevelSize = bevelThickness - 2;
		// number of bevel layers
		public int bevelSegments = 3;

		// 2d/3d spline path to extrude shape orthogonality to
		public Curve extrudePath;
		// 2d path for bend the shape around x/y plane
		public CurvePath bendPath;
		 
		// material index for front and back faces
		public int material;
		// material index for extrusion and beveled faces
		public int extrudeMaterial;
	}
	
	private static final double RAD_TO_DEGREES = 180.0 / Math.PI;
	
	private static Vector2 __v1 = new Vector2();
	private static Vector2 __v2 = new Vector2();
	private static Vector2 __v3 = new Vector2();
	private static Vector2 __v4 = new Vector2();
	private static Vector2 __v5 = new Vector2();
	private static Vector2 __v6 = new Vector2();
	
	private Box3 shapebb;
	private List<List<Vector2>> holes;
	private List<List<Integer>> localFaces;
	private ExtrudeGeometryParameters options;
	
	private int shapesOffset;
	private int verticesCount;

	public ExtrudeGeometry(ExtrudeGeometryParameters options)
	{
		this(new ArrayList<Shape>(), options);
	}

	public ExtrudeGeometry(Shape shape, ExtrudeGeometryParameters options)
	{
		this(Arrays.asList(shape), options);
	}
	
	public ExtrudeGeometry(List<Shape> shapes, ExtrudeGeometryParameters options)
	{
		super();
		
		this.shapebb = shapes.get( shapes.size() - 1 ).getBoundingBox();
		this.options = options;

		this.addShape( shapes, options );

		this.computeFaceNormals();
	}
	
	public void addShape(List<Shape> shapes, ExtrudeGeometryParameters options) 
	{
		int sl = shapes.size();
		
		for ( int s = 0; s < sl; s ++ ) 
			this.addShape( shapes.get( s ), options );
	}
	
	public void addShape( Shape shape, ExtrudeGeometryParameters options ) 
	{
		Log.debug("ExtrudeGeometry: Called addShape() shape=" + shape);

		List<Vector2> extrudePts = null;
		boolean extrudeByPath = false;
		
		Vector3 binormal  = new Vector3();
		Vector3 normal    = new Vector3();
		Vector3 position2 = new Vector3();
		
		FrenetFrames splineTube = null;

		if ( options.extrudePath != null) 
		{
			extrudePts = options.extrudePath.getSpacedPoints( options.steps );

			extrudeByPath = true;
			// bevels not supported for path extrusion
			options.bevelEnabled = false;

			// SETUP TNB variables

			// Reuse TNB from TubeGeomtry for now.
			// TODO - have a .isClosed in spline?
			splineTube = new FrenetFrames(options.extrudePath, options.steps, false);
		}

		// Safeguards if bevels are not enabled

		if ( !options.bevelEnabled ) 
		{
			options.bevelSegments = 0;
			options.bevelThickness = 0;
			options.bevelSize = 0;
		}

		this.shapesOffset = getVertices().size();

		if ( options.bendPath != null )
			shape.addWrapPath( options.bendPath );

		List<Vector2> vertices = shape.getTransformedPoints();

		this.holes = shape.getPointsHoles();

		boolean reverse = ! ShapeUtils.isClockWise( vertices ) ;

		if ( reverse ) 
		{
			Collections.reverse(vertices);

			// Maybe we should also check if holes are in the opposite direction, just to be safe ...
			for ( int h = 0, hl = this.holes.size(); h < hl; h ++ ) 
			{
				List<Vector2> ahole = this.holes.get( h );

				if ( ShapeUtils.isClockWise( ahole ) )
					Collections.reverse(ahole);
			}

			// If vertices are in order now, we shouldn't need to worry about them again (hopefully)!
			reverse = false; 
		}

		localFaces = ShapeUtils.triangulateShape ( vertices, holes );

		// Would it be better to move points after triangulation?
		// shapePoints = shape.extractAllPointsWithBend( curveSegments, bendPath );
		// 	vertices = shapePoints.shape;
		// 	holes = shapePoints.holes;
		////
		///   Handle Vertices
		////
		
		// vertices has all points but contour has only points of circumference
		List<Vector2> contour = new ArrayList<Vector2>(vertices); 
		
		for ( int h = 0, hl = this.holes.size();  h < hl; h ++ )
			vertices.addAll( this.holes.get( h ) );
		verticesCount = vertices.size();
		//
		// Find directions for point movement
		//
		List<Vector2> contourMovements = new ArrayList<Vector2>();
		for ( int i = 0, il = contour.size(), j = il - 1, k = i + 1; i < il; i ++, j ++, k ++ ) 
		{
			if ( j == il ) j = 0;
			if ( k == il ) k = 0;

			contourMovements.add(
					getBevelVec( contour.get( i ), contour.get( j ), contour.get( k ) ));
		}

		List<List<Vector2>> holesMovements = new ArrayList<List<Vector2>>();
		List<Vector2> verticesMovements = (List<Vector2>) ((ArrayList) contourMovements).clone();

		for ( int h = 0, hl = holes.size(); h < hl; h ++ ) 
		{
			List<Vector2> ahole = holes.get( h );

			List<Vector2> oneHoleMovements = new ArrayList<Vector2>();

			for ( int i = 0, il = ahole.size(), j = il - 1, k = i + 1; i < il; i ++, j ++, k ++ ) 
			{
				if ( j == il ) j = 0;
				if ( k == il ) k = 0;

				//  (j)---(i)---(k)
				oneHoleMovements.add(getBevelVec( ahole.get( i ), ahole.get( j ), ahole.get( k ) ));
			}

			holesMovements.add( oneHoleMovements );
			verticesMovements.addAll( oneHoleMovements );
		}


		// Loop bevelSegments, 1 for the front, 1 for the back
		for ( int b = 0; b < options.bevelSegments; b ++ ) 
		{
			double t = b / (double)options.bevelSegments;
			double z = options.bevelThickness * ( 1.0 - t );

			double bs = options.bevelSize * ( Math.sin ( t * Math.PI / 2.0 ) ) ; // curved
			//bs = bevelSize * t ; // linear

			// contract shape

			for ( int i = 0, il = contour.size(); i < il; i ++ ) 
			{
				Vector2 vert = scalePt2( contour.get( i ), contourMovements.get( i ), bs );
				//vert = scalePt( contour[ i ], contourCentroid, bs, false );
				v( vert.getX(), vert.getY(),  - z );
			}

			// expand holes
			for ( int h = 0, hl = holes.size(); h < hl; h++ ) 
			{
				List<Vector2> ahole = holes.get( h );
				List<Vector2> oneHoleMovements = holesMovements.get( h );

				for ( int i = 0, il = ahole.size(); i < il; i++ ) 
				{
					Vector2 vert = scalePt2( ahole.get( i ), oneHoleMovements.get( i ), bs );
					//vert = scalePt( ahole[ i ], holesCentroids[ h ], bs, true );
					v( vert.getX(), vert.getY(),  -z );
				}
			}
		}

//		bs = bevelSize;

		// Back facing vertices

		for ( int i = 0; i < vertices.size(); i ++ ) 
		{
			Vector2 vert = options.bevelEnabled 
					? scalePt2( vertices.get( i ), verticesMovements.get( i ), options.bevelSize ) 
					: vertices.get( i );

			if ( !extrudeByPath ) 
			{
				v( vert.getX(), vert.getY(), 0 );
			} 
			else 
			{
				// v( vert.x, vert.y + extrudePts[ 0 ].y, extrudePts[ 0 ].x );
				normal.copy(splineTube.getNormals().get(0)).multiply(vert.getX());
				binormal.copy(splineTube.getBinormals().get(0)).multiply(vert.getY());
				position2.copy((Vector3)extrudePts.get(0)).add(normal).add(binormal);
				
				v(position2.getX(), position2.getY(), position2.getZ());
			}
		}

		// Add stepped vertices...
		// Including front facing vertices

		for ( int s = 1; s <= options.steps; s ++ ) 
		{
			for ( int i = 0; i < vertices.size(); i ++ ) 
			{
				Vector2 vert = options.bevelEnabled 
						? scalePt2( vertices.get( i ), verticesMovements.get( i ), options.bevelSize ) 
						: vertices.get( i );

				if ( !extrudeByPath ) 
				{
					v( vert.getX(), vert.getY(), (double)options.amount / options.steps * s );
				} 
				else 
				{
					// v( vert.x, vert.y + extrudePts[ s - 1 ].y, extrudePts[ s - 1 ].x );

					normal.copy(splineTube.getNormals().get(s)).multiply(vert.getX());
					binormal.copy(splineTube.getBinormals().get(s)).multiply(vert.getY());

					position2.copy((Vector3)extrudePts.get(s)).add(normal).add(binormal);

					v(position2.getX(), position2.getY(), position2.getZ() );
				}
			}
		}


		// Add bevel segments planes
		for ( int b = options.bevelSegments - 1; b >= 0; b -- ) 
		{
			double t = (double)b / options.bevelSegments;
			double z = options.bevelThickness * ( 1 - t );

			double bs = options.bevelSize * Math.sin ( t * Math.PI/2.0 ) ;

			// contract shape
			for ( int i = 0, il = contour.size(); i < il; i ++ ) 
			{
				Vector2 vert = scalePt2( contour.get( i ), contourMovements.get( i ), bs );
				v( vert.getX(), vert.getY(),  options.amount + z );
			}

			// expand holes
			for ( int h = 0, hl = this.holes.size(); h < hl; h ++ ) 
			{
				List<Vector2> ahole = this.holes.get( h );
				List<Vector2> oneHoleMovements = holesMovements.get( h );

				for ( int i = 0, il = ahole.size(); i < il; i++ ) 
				{
					Vector2 vert = scalePt2( ahole.get( i ), oneHoleMovements.get( i ), bs );

					if ( !extrudeByPath )
						v( vert.getX(), vert.getY(),  options.amount + z );

					else
						v( vert.getX(), 
								vert.getY() + ((Vector3)extrudePts.get( options.steps - 1 )).getY(), 
								((Vector3)extrudePts.get( options.steps - 1 )).getX() + z );
				}
			}
		}

		//
		// Handle Faces
		//

		// Top and bottom faces
		buildLidFaces();

		// Sides faces
		buildSideFaces(contour);
	}
	
	private Vector2 getBevelVec( Vector2 pt_i, Vector2 pt_j, Vector2 pt_k ) 
	{
		// Algorithm 2
		return getBevelVec2( pt_i, pt_j, pt_k );
	}

	private Vector2 getBevelVec1( Vector2 pt_i, Vector2 pt_j, Vector2 pt_k ) 
	{
		double anglea = Math.atan2( pt_j.getY() - pt_i.getY(), pt_j.getX() - pt_i.getX() );
		double angleb = Math.atan2( pt_k.getY() - pt_i.getY(), pt_k.getX() - pt_i.getX() );

		if ( anglea > angleb )
			angleb += Math.PI * 2.0;

		double anglec = ( anglea + angleb ) / 2.0;

		double x = - Math.cos( anglec );
		double y = - Math.sin( anglec );

		return new Vector2( x, y ); //.normalize();
	}
	
	private Vector2 scalePt2 ( Vector2 pt, Vector2 vec, double size ) 
	{
		return (Vector2) vec.clone().multiply( size ).add( pt );
	}
	
	/*
	 * good reading for line-line intersection
	 * http://sputsoft.com/blog/2010/03/line-line-intersection.html
	 */
	private Vector2 getBevelVec2( Vector2 pt_i, Vector2 pt_j, Vector2 pt_k ) 
	{
		Vector2 a = ExtrudeGeometry.__v1;
		Vector2 b = ExtrudeGeometry.__v2;
		Vector2 v_hat = ExtrudeGeometry.__v3;
		Vector2 w_hat = ExtrudeGeometry.__v4;
		Vector2 p = ExtrudeGeometry.__v5;
		Vector2 q = ExtrudeGeometry.__v6;

		// define a as vector j->i
		// define b as vectot k->i
		a.set( pt_i.getX() - pt_j.getX(), pt_i.getY() - pt_j.getY() );
		b.set( pt_i.getX() - pt_k.getX(), pt_i.getY() - pt_k.getY() );

		// get unit vectors
		Vector2 v = a.normalize();
		Vector2 w = b.normalize();

		// normals from pt i
		v_hat.set( -v.getY(), v.getX() );
		w_hat.set( w.getY(), -w.getX() );

		// pts from i
		p.copy( pt_i ).add( v_hat );
		q.copy( pt_i ).add( w_hat );

		if ( p.equals( q ) )
			return w_hat.clone();

		// Points from j, k. helps prevents points cross overover most of the time
		p.copy( pt_j ).add( v_hat );
		q.copy( pt_k ).add( w_hat );

		double v_dot_w_hat = v.dot( w_hat );
		double q_sub_p_dot_w_hat = q.sub( p ).dot( w_hat );

		// We should not reach these conditions

		if ( v_dot_w_hat == 0 ) 
		{
			Log.info( "getBevelVec2() Either infinite or no solutions!" );

			if ( q_sub_p_dot_w_hat == 0 )
				Log.info( "getBevelVec2() Its finite solutions." );

			else
				Log.error( "getBevelVec2() Too bad, no solutions." );
		}

		double s = q_sub_p_dot_w_hat / v_dot_w_hat;

		// in case of emergecy, revert to algorithm 1.
		if ( s < 0 )
			return getBevelVec1( pt_i, pt_j, pt_k );

		Vector2 intersection = v.multiply( s ).add( p );

		// Don't normalize!, otherwise sharp corners become ugly
		return intersection.sub( pt_i ).clone(); 
	}

	private void buildLidFaces() 
	{
		int flen = this.localFaces.size();
		Log.debug("ExtrudeGeometry: buildLidFaces() faces=" + flen);

		if ( this.options.bevelEnabled ) 
		{
			int layer = 0 ; // steps + 1
			int offset = this.shapesOffset * layer;

			// Bottom faces

			for ( int i = 0; i < flen; i ++ ) 
			{
				List<Integer> face = this.localFaces.get( i );
				f3( face.get(2) + offset, face.get(1) + offset, face.get(0) + offset, true );
			}

			layer = this.options.steps + this.options.bevelSegments * 2;
			offset = verticesCount * layer;

			// Top faces

			for ( int i = 0; i < flen; i ++ ) 
			{
				List<Integer> face = this.localFaces.get( i );
				f3( face.get(0) + offset, face.get(1) + offset, face.get(2) + offset, false );
			}
		} 
		else 
		{
			// Bottom faces

			for ( int i = 0; i < flen; i++ ) 
			{
				List<Integer> face = localFaces.get( i );
				f3( face.get(2), face.get(1), face.get(0), true );
			}

			// Top faces

			for ( int i = 0; i < flen; i ++ ) 
			{
				List<Integer> face = localFaces.get( i );
				f3( face.get(0) + verticesCount * this.options.steps, 
					face.get(1) + verticesCount * this.options.steps, 
					face.get(2) + verticesCount * this.options.steps, false );
			}
		}
	}

	// Create faces for the z-sides of the shape

	private void buildSideFaces(List<Vector2> contour) 
	{
		int layeroffset = 0;
		sidewalls( contour, layeroffset );
		layeroffset += contour.size();

		for ( int h = 0, hl = this.holes.size();  h < hl; h ++ ) 
		{
			List<Vector2> ahole = this.holes.get( h );
			sidewalls( ahole, layeroffset );

			//, true
			layeroffset += ahole.size();
		}
	}

	private void sidewalls( List<Vector2> contour, int layeroffset ) 
	{
		int i = contour.size();

		while ( --i >= 0 ) 
		{
			int j = i;
			int k = i - 1;
			if ( k < 0 ) 
				k = contour.size() - 1;

			int sl = this.options.steps + this.options.bevelSegments * 2;

			for ( int s = 0; s < sl; s ++ ) 
			{
				int slen1 = this.verticesCount * s;
				int slen2 = this.verticesCount * ( s + 1 );
				int a = layeroffset + j + slen1;
				int b = layeroffset + k + slen1;
				int c = layeroffset + k + slen2;
				int d = layeroffset + j + slen2;

				f4( a, b, c, d);
			}
		}
	}


	private void v( double x, double y, double z ) 
	{
		getVertices().add( new Vector3( x, y, z ) );
	}

	private void f3( int a, int b, int c, boolean isBottom ) 
	{
		a += this.shapesOffset;
		b += this.shapesOffset;
		c += this.shapesOffset;

		// normal, color, material
		getFaces().add( new Face3( a, b, c, this.options.material ) );

		List<Vector2> uvs = isBottom 
				? WorldUVGenerator.generateBottomUV( this, a, b, c)
		        : WorldUVGenerator.generateTopUV( this, a, b, c);

 		getFaceVertexUvs().get( 0 ).add(uvs);
	}

	private void f4( int a, int b, int c, int d) 
	{
		a += this.shapesOffset;
		b += this.shapesOffset;
		c += this.shapesOffset;
		d += this.shapesOffset;

		List<Color> colors = new ArrayList<Color>();
		List<Vector3> normals = new ArrayList<Vector3>();
 		getFaces().add( new Face3( a, b, d, normals, colors, this.options.extrudeMaterial ) );
 		List<Color> colors2 = new ArrayList<Color>();
		List<Vector3> normals2 = new ArrayList<Vector3>();
 		getFaces().add( new Face3( b, c, d, normals2, colors2, this.options.extrudeMaterial ) );

  		List<Vector2> uvs = WorldUVGenerator.generateSideWallUV(this, a, b, c, d);
 		getFaceVertexUvs().get( 0 ).add( Arrays.asList( uvs.get( 0 ), uvs.get( 1 ), uvs.get( 3 ) ) );
 		getFaceVertexUvs().get( 0 ).add( Arrays.asList( uvs.get( 1 ), uvs.get( 2 ), uvs.get( 3 ) ) );

	}
	
	public static class WorldUVGenerator
	{
		public static List<Vector2> generateTopUV( Geometry geometry, int indexA, int indexB, int indexC ) 
		{
			double ax = geometry.getVertices().get( indexA ).getX();
			double ay = geometry.getVertices().get( indexA ).getY();

			double bx = geometry.getVertices().get( indexB ).getX();
			double by = geometry.getVertices().get( indexB ).getY();

			double cx = geometry.getVertices().get( indexC ).getX();
			double cy = geometry.getVertices().get( indexC ).getY();
				
			return Arrays.asList(
				new Vector2( ax, 1 - ay ),
				new Vector2( bx, 1 - by ),
				new Vector2( cx, 1 - cy )
			);
		}

		public static List<Vector2> generateBottomUV( ExtrudeGeometry geometry, int indexA, int indexB, int indexC) 
		{
			return generateTopUV( geometry, indexA, indexB, indexC );
		}

		public static List<Vector2> generateSideWallUV( Geometry geometry, int indexA, int indexB, int indexC, int indexD)
		{
			double ax = geometry.getVertices().get( indexA ).getX();
			double ay = geometry.getVertices().get( indexA ).getY();
			double az = geometry.getVertices().get( indexA ).getZ();

			double bx = geometry.getVertices().get( indexB ).getX();
			double by = geometry.getVertices().get( indexB ).getY();
			double bz = geometry.getVertices().get( indexB ).getZ();

			double cx = geometry.getVertices().get( indexC ).getX();
			double cy = geometry.getVertices().get( indexC ).getY();
			double cz = geometry.getVertices().get( indexC ).getZ();

			double dx = geometry.getVertices().get( indexD ).getX();
			double dy = geometry.getVertices().get( indexD ).getY();
			double dz = geometry.getVertices().get( indexD ).getZ();
			
			if ( Math.abs( ay - by ) < 0.01 ) 
			{
				return Arrays.asList(
					new Vector2( ax, az ),
					new Vector2( bx, bz ),
					new Vector2( cx, cz ),
					new Vector2( dx, dz )
				);
			} 
			else 
			{
				return Arrays.asList(
					new Vector2( ay, az ),
					new Vector2( by, bz ),
					new Vector2( cy, cz ),
					new Vector2( dy, dz )
				);
			}
		}
	}
}