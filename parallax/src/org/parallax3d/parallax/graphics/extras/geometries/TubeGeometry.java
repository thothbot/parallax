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

package org.parallax3d.parallax.graphics.extras.geometries;

import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.core.Curve;
import org.parallax3d.parallax.graphics.extras.core.FrenetFrames;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;

import java.util.Arrays;
import java.util.List;

/**
 * Creates a tube which extrudes along a 3d spline.
 * <p>
 * Uses parallel transport frames as described in 
 * <a href="http://www.cs.indiana.edu/pub/techreports/TR425.pdf">http://www.cs.indiana.edu</a>
 * <p>
 * Based on the js code.
 * 
 * @author thothbot
 */
@ThreejsObject("THREE.TubeGeometry")
public final class TubeGeometry extends Geometry
{
	public interface Taper {
		double execute( double u);
	};
	
	public static class NoTaper implements Taper {

		@Override
		public double execute(double u) {
			return 1;
		}
	}
	
	public static class SinusoidalTaper implements Taper {

		@Override
		public double execute(double u) {
			return Math.sin( Math.PI * u );
		}
	}

	private List<Vector3> tangents;
	private List<Vector3> normals;
	private List<Vector3> binormals;
	private Curve path;

	private List<List<Integer>> grid;

	public TubeGeometry( Curve path )
	{
		this(path, 64, 1.0, 8, false);
	}

	public TubeGeometry( Curve path, int segments, double radius, int radialSegments, boolean closed )
	{
		this(path, segments, radius, radialSegments, closed, new NoTaper());
	}

	public TubeGeometry( Curve path, int segments, double radius, int radialSegments, boolean closed, Taper taper )
	{
		Vector3 pos2 = new Vector3();

		int numpoints = segments + 1;

		FrenetFrames frames = new FrenetFrames( path, segments, closed );
		tangents = frames.getTangents();
		normals = frames.getNormals();
		binormals = frames.getBinormals();

		// construct the grid
		int [][] grid = new int[numpoints][];

		for ( int i = 0; i < numpoints; i ++ ) {

			grid[ i ] = new int[radialSegments];

			double u = i / ( numpoints - 1 );

			Vector2 pos = path.getPointAt( u );

			Vector3 Vetangent = tangents.get(i);
			Vector3 normal = normals.get(i);
			Vector3 binormal = binormals.get(i);

			double r = radius * taper.execute( u );

			for ( int j = 0; j < radialSegments; j ++ ) {

				double v = (double) j / radialSegments * 2. * Math.PI;

				double cx = - r * Math.cos( v ); // TODO: Hack: Negating it so it faces outside.
				double cy = r * Math.sin( v );

				pos2.copy( pos );
				pos2.addX( cx * normal.getX() + cy * binormal.getX() );
				pos2.addY( cx * normal.getY() + cy * binormal.getY() );
				pos2.addZ( cx * normal.getZ() + cy * binormal.getZ() );

				grid[ i ][ j ] = vert(pos2.getX(), pos2.getY(), pos2.getZ());

			}

		}


		// construct the mesh

		for ( int i = 0; i < segments; i ++ ) {

			for ( int j = 0; j < radialSegments; j ++ ) {

				int ip = ( closed ) ? ( i + 1 ) % segments : i + 1;
				int jp = ( j + 1 ) % radialSegments;

				int a = grid[ i ][ j ];		// *** NOT NECESSARILY PLANAR ! ***
				int b = grid[ ip ][ j ];
				int c = grid[ ip ][ jp ];
				int d = grid[ i ][ jp ];

				Vector2 uva = new Vector2( i / segments, j / radialSegments );
				Vector2 uvb = new Vector2( ( i + 1 ) / segments, j / radialSegments );
				Vector2 uvc = new Vector2( ( i + 1 ) / segments, ( j + 1 ) / radialSegments );
				Vector2 uvd = new Vector2( i / segments, ( j + 1 ) / radialSegments );

				this.getFaces().add( new Face3( a, b, d ) );
				this.getFaceVertexUvs().get(0).add( Arrays.asList( uva, uvb, uvd ) );

				this.getFaces().add( new Face3( b, c, d ) );
				this.getFaceVertexUvs().get(0).add( Arrays.asList( uvb.clone(), uvc, uvd.clone() ) );

			}

		}

		this.computeFaceNormals();
		this.computeVertexNormals();
	}

	public List<Vector3> getTangents()
	{
		return this.tangents;
	}

	public List<Vector3> getNormals()
	{
		return this.normals;
	}

	public List<Vector3> getBinormals()
	{
		return this.binormals;
	}

	public Curve getPath()
	{
		return this.path;
	}

	private int vert( double x, double y, double z ) 
	{
		getVertices().add( new Vector3( x, y, z ) );
		return getVertices().size() - 1;
	}

}
