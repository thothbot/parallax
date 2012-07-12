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

package thothbot.squirrel.core.shared.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import thothbot.squirrel.core.shared.Log;
import thothbot.squirrel.core.shared.curves.Curve;
import thothbot.squirrel.core.shared.curves.CurvePath;
import thothbot.squirrel.core.shared.curves.FrenetFrames;
import thothbot.squirrel.core.shared.curves.Path;
import thothbot.squirrel.core.shared.curves.Shape;
import thothbot.squirrel.core.shared.utils.ShapeUtils;


/*
 * @author zz85 / http://www.lab4games.net/zz85/blog
 *
 * Creates extruded geometry from a path shape.
 */
public class ExtrudeGeometry extends Geometry
{
	public static class ExtrudeGeometryParameters
	{
		// size of the text
		public float size;
		// thickness to extrude text
		public float height;
		// number of points on the curves
		public int curveSegments = 12;
		// number of points for z-side extrusions / used for subdividing segements of extrude spline too
		public int steps = 1;
		// Amount
		public int amount = 100;

		// turn on bevel
		public boolean bevelEnabled = true;
		// how deep into text bevel goes
		public float bevelThickness = 6;
		// how far from text outline is bevel
		public float bevelSize = bevelThickness - 2;
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
	
	private static double RAD_TO_DEGREES = 180.0 / Math.PI;
	
	private static Vector2f __v1 = new Vector2f();
	private static Vector2f __v2 = new Vector2f();
	private static Vector2f __v3 = new Vector2f();
	private static Vector2f __v4 = new Vector2f();
	private static Vector2f __v5 = new Vector2f();
	private static Vector2f __v6 = new Vector2f();
	
	private BoundingBox shapebb;
	private List<List<Vector2f>> holes;
	private List<List<Integer>> localFaces;
	private ExtrudeGeometryParameters options;

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

		this.computeCentroids();
		this.computeFaceNormals(false);
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

		List<Vector> extrudePts = null;
		boolean extrudeByPath = false;
		
		Vector3f binormal = new Vector3f();
		Vector3f normal = new Vector3f();
		Vector3f position2 = new Vector3f();
		
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

		int shapesOffset = this.vertices.size();

		if ( options.bendPath != null )
			shape.addWrapPath( options.bendPath );

		List<Vector2f> vertices =  shape.getTransformedPoints();
		this.holes = shape.getPointsHoles();

		boolean reverse = ! ShapeUtils.isClockWise( vertices ) ;

		if ( reverse ) 
		{
			Collections.reverse(vertices);

			// Maybe we should also check if holes are in the opposite direction, just to be safe ...
			for ( int h = 0, hl = this.holes.size(); h < hl; h ++ ) 
			{
				List<Vector2f> ahole = this.holes.get( h );

				if ( ShapeUtils.isClockWise( ahole ) )
					Collections.reverse(ahole);
			}

			// If vertices are in order now, we shouldn't need to worry about them again (hopefully)!
			reverse = false; 
		}

		localFaces = ShapeUtils.triangulateShape ( vertices, holes );
		Log.error("===========" + localFaces);
		// Would it be better to move points after triangulation?
		// shapePoints = shape.extractAllPointsWithBend( curveSegments, bendPath );
		// 	vertices = shapePoints.shape;
		// 	holes = shapePoints.holes;
		////
		///   Handle Vertices
		////
		for ( int h = 0, hl = this.holes.size();  h < hl; h ++ )
			vertices.addAll( this.holes.get( h ) );

		//
		// Find directions for point movement
		//
		List<Vector2f> contourMovements = new ArrayList<Vector2f>();
		for ( int i = 0, il = this.vertices.size(), j = il - 1, k = i + 1; i < il; i ++, j ++, k ++ ) 
		{
			if ( j == il ) j = 0;
			if ( k == il ) k = 0;

			Vector3f pt_i = this.vertices.get( i );
			Vector3f pt_j = this.vertices.get( j );
			Vector3f pt_k = this.vertices.get( k );

			contourMovements.add(
					getBevelVec( this.vertices.get( i ), this.vertices.get( j ), this.vertices.get( k ) ));
		}

		List<List<Vector2f>> holesMovements = new ArrayList<List<Vector2f>>();
		List<Vector2f> verticesMovements = (List<Vector2f>) ((ArrayList) contourMovements).clone();
		for ( int h = 0, hl = holes.size(); h < hl; h ++ ) 
		{
			List<Vector2f> ahole = holes.get( h );

			List<Vector2f> oneHoleMovements = new ArrayList<Vector2f>();

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
			float t = (float)b / options.bevelSegments;
			float z = options.bevelThickness * ( 1 - t );

			float bs = (float) (options.bevelSize * ( Math.sin ( t * Math.PI/2 ) )) ; // curved
			//bs = bevelSize * t ; // linear

			// contract shape

			for ( int i = 0, il = this.vertices.size(); i < il; i ++ ) 
			{
				Vector2f vert = scalePt2( this.vertices.get( i ), contourMovements.get( i ), bs );
				//vert = scalePt( contour[ i ], contourCentroid, bs, false );
				v( vert.getX(), vert.getY(),  - z );
			}

			// expand holes
			for ( int h = 0, hl = holes.size(); h < hl; h++ ) 
			{
				List<Vector2f> ahole = holes.get( h );
				List<Vector2f> oneHoleMovements = holesMovements.get( h );

				for ( int i = 0, il = ahole.size(); i < il; i++ ) 
				{
					Vector2f vert = scalePt2( ahole.get( i ), oneHoleMovements.get( i ), bs );
					//vert = scalePt( ahole[ i ], holesCentroids[ h ], bs, true );
					v( vert.getX(), vert.getY(),  -z );
				}
			}
		}

//		bs = bevelSize;

		// Back facing vertices

		for ( int i = 0; i < this.vertices.size(); i ++ ) 
		{
			Vector2f vert = options.bevelEnabled 
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
				position2.copy((Vector3f)extrudePts.get(0)).add(normal).add(binormal);
				
				v(position2.getX(), position2.getY(), position2.getZ());
			}
		}

		// Add stepped vertices...
		// Including front facing vertices

//		var s;

		for ( int s = 1; s <= options.steps; s ++ ) 
		{
			for ( int i = 0; i < this.vertices.size(); i ++ ) 
			{
				Vector2f vert = options.bevelEnabled 
						? scalePt2( vertices.get( i ), verticesMovements.get( i ), options.bevelSize ) 
						: vertices.get( i );

				if ( !extrudeByPath ) 
				{
					v( vert.getX(), vert.getY(), (float)options.amount / options.steps * s );
				} 
				else 
				{
					// v( vert.x, vert.y + extrudePts[ s - 1 ].y, extrudePts[ s - 1 ].x );

					normal.copy(splineTube.getNormals().get(s)).multiply(vert.getX());
					binormal.copy(splineTube.getBinormals().get(s)).multiply(vert.getY());

					position2.copy((Vector3f)extrudePts.get(s)).add(normal).add(binormal);

					v(position2.getX(), position2.getY(), position2.getZ() );
				}
			}
		}


		// Add bevel segments planes
		for ( int b = options.bevelSegments - 1; b >= 0; b -- ) 
		{
			float t = (float)b / options.bevelSegments;
			float z = options.bevelThickness * ( 1 - t );

			float bs = (float) (options.bevelSize * Math.sin ( t * Math.PI/2.0 )) ;

			// contract shape
			for ( int i = 0, il = this.vertices.size(); i < il; i ++ ) 
			{
				Vector2f vert = scalePt2( this.vertices.get( i ), contourMovements.get( i ), bs );
				v( vert.getX(), vert.getY(),  options.amount + z );
			}

			// expand holes
			for ( int h = 0, hl = this.holes.size(); h < hl; h ++ ) 
			{
				List<Vector2f> ahole = this.holes.get( h );
				List<Vector2f> oneHoleMovements = holesMovements.get( h );

				for ( int i = 0, il = ahole.size(); i < il; i++ ) 
				{
					Vector2f vert = scalePt2( ahole.get( i ), oneHoleMovements.get( i ), bs );

					if ( !extrudeByPath )
						v( vert.getX(), vert.getY(),  options.amount + z );

					else
						v( vert.getX(), 
								vert.getY() + ((Vector3f)extrudePts.get( options.steps - 1 )).getY(), 
								((Vector3f)extrudePts.get( options.steps - 1 )).getX() + z );
				}
			}
		}

		//
		// Handle Faces
		//

		// Top and bottom faces
		buildLidFaces();

		// Sides faces
		buildSideFaces();
	}
	
	private Vector2f getBevelVec( Vector pt_i, Vector pt_j, Vector pt_k ) 
	{
		// Algorithm 2
		return getBevelVec2( (Vector2f)pt_i, (Vector2f)pt_j, (Vector2f)pt_k );
	}

	private Vector2f getBevelVec1( Vector2f pt_i, Vector2f pt_j, Vector2f pt_k ) 
	{
		double anglea = Math.atan2( pt_j.getY() - pt_i.getY(), pt_j.getX() - pt_i.getX() );
		double angleb = Math.atan2( pt_k.getY() - pt_i.getY(), pt_k.getX() - pt_i.getX() );

		if ( anglea > angleb )
			angleb += Math.PI * 2.0;

		double anglec = ( anglea + angleb ) / 2.0;

		float x = (float) - Math.cos( anglec );
		float y = (float) - Math.sin( anglec );

		return new Vector2f( x, y ); //.normalize();
	}
	
	private Vector2f scalePt2 ( Vector pt, Vector vec, float size ) 
	{
		return (Vector2f) vec.clone().multiply( size ).add( pt );
	}
	
	/*
	 * good reading for line-line intersection
	 * http://sputsoft.com/blog/2010/03/line-line-intersection.html
	 */
	private Vector2f getBevelVec2( Vector2f pt_i, Vector2f pt_j, Vector2f pt_k ) 
	{
		Vector2f a = ExtrudeGeometry.__v1;
		Vector2f b = ExtrudeGeometry.__v2;
		Vector2f v_hat = ExtrudeGeometry.__v3;
		Vector2f w_hat = ExtrudeGeometry.__v4;
		Vector2f p = ExtrudeGeometry.__v5;
		Vector2f q = ExtrudeGeometry.__v6;

		// define a as vector j->i
		// define b as vectot k->i
		a.set( pt_i.getX() - pt_j.getX(), pt_i.getY() - pt_j.getY() );
		b.set( pt_i.getX() - pt_k.getX(), pt_i.getY() - pt_k.getY() );

		// get unit vectors
		Vector2f v = a.normalize();
		Vector2f w = b.normalize();

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

		float v_dot_w_hat = v.dot( w_hat );
		float q_sub_p_dot_w_hat = q.sub( p ).dot( w_hat );

		// We should not reach these conditions

		if ( v_dot_w_hat == 0 ) 
		{
			Log.info( "getBevelVec2() Either infinite or no solutions!" );

			if ( q_sub_p_dot_w_hat == 0 )
				Log.info( "getBevelVec2() Its finite solutions." );

			else
				Log.error( "getBevelVec2() Too bad, no solutions." );
		}

		float s = q_sub_p_dot_w_hat / v_dot_w_hat;

		// in case of emergecy, revert to algorithm 1.
		if ( s < 0 )
			return getBevelVec1( pt_i, pt_j, pt_k );

		Vector2f intersection = v.multiply( s ).add( p );

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
			int offset = this.getVertices().size() * layer;

			// Bottom faces
			
			for ( int i = 0; i < flen; i ++ ) 
			{
				List<Integer> face = this.localFaces.get( i );
				f3( face.get(2) + offset, face.get(1) + offset, face.get(0) + offset, true );
			}

			layer = this.options.steps + this.options.bevelSegments * 2;
			offset = this.getVertices().size() * layer;

			// Top faces

			for ( int i = 0; i < flen; i ++ ) 
			{
				List<Integer> face = localFaces.get( i );
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
				f3( face.get(0) + this.getVertices().size() * this.options.steps, 
						face.get(1) + this.getVertices().size() * this.options.steps, 
						face.get(2) + this.getVertices().size() * this.options.steps, false );
			}
		}
	}

	// Create faces for the z-sides of the shape

	private void buildSideFaces() 
	{
		int layeroffset = 0;
		sidewalls( this.getVertices(), layeroffset );
		layeroffset += this.getVertices().size();

		for ( int h = 0, hl = this.holes.size();  h < hl; h ++ ) 
		{
			List<?> ahole = this.holes.get( h );
			sidewalls( (List<Vector3f>) ahole, layeroffset );

			//, true
			layeroffset += ahole.size();
		}
	}

	private void sidewalls( List<Vector3f> contour, int layeroffset ) 
	{
		int i = this.getVertices().size();

		while ( --i >= 0 ) 
		{
			int j = i;
			int k = i - 1;
			if ( k < 0 ) 
				k = contour.size() - 1;

			int sl = this.options.steps + this.options.bevelSegments * 2;

			for ( int s = 0; s < sl; s ++ ) 
			{
				int slen1 = this.getVertices().size() * s;
				int slen2 = this.getVertices().size() * ( s + 1 );
				int a = layeroffset + j + slen1;
				int b = layeroffset + k + slen1;
				int c = layeroffset + k + slen2;
				int d = layeroffset + j + slen2;

				f4( a, b, c, d);
			}
		}
	}


	private void v( float x, float y, float z ) 
	{
		this.vertices.add( new Vector3f( x, y, z ) );
	}

	private void f3( int a, int b, int c, boolean isBottom ) 
	{
		int size = this.vertices.size();
		a += size;
		b += size;
		c += size;

		// normal, color, material
		this.faces.add( new Face3( a, b, c, this.options.material ) );

		List<UVf> uvs = isBottom 
				? WorldUVGenerator.generateBottomUV( this, a, b, c)
		        : WorldUVGenerator.generateTopUV( this, a, b, c);

 		this.faceVertexUvs.get( 0 ).add(uvs);
	}

	private void f4( int a, int b, int c, int d) 
	{
		int size = this.vertices.size();
		a += size;
		b += size;
		c += size;
		d += size;

 		this.faces.add( new Face4( a, b, c, d, null, null, this.options.extrudeMaterial ) );
 
 		List<UVf> uvs = WorldUVGenerator.generateSideWallUV(this, a, b, c, d);
 		this.faceVertexUvs.get( 0 ).add(uvs);
	}
	
	public static class WorldUVGenerator
	{
		public static List<UVf> generateTopUV( Geometry geometry, int indexA, int indexB, int indexC ) 
		{
			float ax = geometry.getVertices().get( indexA ).getX();
			float ay = geometry.getVertices().get( indexA ).getY();

			float bx = geometry.getVertices().get( indexB ).getX();
			float by = geometry.getVertices().get( indexB ).getY();

			float cx = geometry.getVertices().get( indexC ).getX();
			float cy = geometry.getVertices().get( indexC ).getY();
				
			return Arrays.asList(
				new UVf( ax, 1 - ay ),
				new UVf( bx, 1 - by ),
				new UVf( cx, 1 - cy )
			);
		}

		public static List<UVf> generateBottomUV( Geometry geometry, int indexA, int indexB, int indexC) 
		{
			return generateTopUV( geometry, indexA, indexB, indexC );
		}

		public static List<UVf> generateSideWallUV( Geometry geometry, int indexA, int indexB, int indexC, int indexD)
		{
			float ax = geometry.getVertices().get( indexA ).getX();
			float ay = geometry.getVertices().get( indexA ).getY();
			float az = geometry.getVertices().get( indexA ).getZ();

			float bx = geometry.getVertices().get( indexB ).getX();
			float by = geometry.getVertices().get( indexB ).getY();
			float bz = geometry.getVertices().get( indexB ).getZ();

			float cx = geometry.getVertices().get( indexC ).getX();
			float cy = geometry.getVertices().get( indexC ).getY();
			float cz = geometry.getVertices().get( indexC ).getZ();

			float dx = geometry.getVertices().get( indexD ).getX();
			float dy = geometry.getVertices().get( indexD ).getY();
			float dz = geometry.getVertices().get( indexD ).getZ();
			
			if ( Math.abs( ay - by ) < 0.01f ) 
			{
				return Arrays.asList(
					new UVf( ax, az ),
					new UVf( bx, bz ),
					new UVf( cx, cz ),
					new UVf( dx, dz )
				);
			} 
			else 
			{
				return Arrays.asList(
					new UVf( ay, az ),
					new UVf( by, bz ),
					new UVf( cy, cz ),
					new UVf( dy, dz )
				);
			}
		}
	}
}
