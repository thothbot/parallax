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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.curves.Curve;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;

/**
 * Creates a tube which extrudes along a 3d spline.
 * <p>
 * Uses parallel transport frames as described in 
 * <a href="http://www.cs.indiana.edu/pub/techreports/TR425.pdf">http://www.cs.indiana.edu</a>
 * <p>
 * Based on the three.js code.
 * 
 * @author thothbot
 */
public final class TubeGeometry extends Geometry
{
	private List<Vector3> tangents;
	private List<Vector3> normals;
	private List<Vector3> binormals;
	private Curve path;
	
	private List<List<Integer>> grid;
	
	public TubeGeometry( Curve path )
	{
		this(path, 64, 1.0, 8, false, true);
	}
	
	public TubeGeometry( Curve path, int segments, double radius, int segmentsRadius, boolean closed, boolean debug ) 
	{
		super();
		
		this.path = path;
		
		this.grid = new ArrayList<List<Integer>>();

		int numpoints = segments + 1;

		FrenetFrames frames = new FrenetFrames(path, segments, closed);
		
		// proxy internals
		this.tangents  = frames.getTangents();
		this.normals   = frames.getNormals();
		this.binormals = frames.getBinormals();

		// consruct the grid

		for ( int i = 0; i < numpoints; i++ ) 
		{
			this.grid.add( i, new ArrayList<Integer>());

			double u = i / (double)( numpoints - 1 );

			Vector3 pos = (Vector3) path.getPointAt( u );
//
//			if ( debug ) 
//			{
//				getDebug().add( new ArrowHelper( tangents.get( i ),  pos, radius, 0x0000ff ) );	
//				getDebug().add( new ArrowHelper( normals.get( i ),   pos, radius, 0xff0000 ) );
//				getDebug().add( new ArrowHelper( binormals.get( i ), pos, radius, 0x00ff00 ) );
//			}

			for ( int j = 0; j < segmentsRadius; j++ ) 
			{
				double v = j / (double)segmentsRadius * 2.0 * Math.PI;

				// TODO: Hack: Negating it so it faces outside.
				double cx = - radius * Math.cos( v ); 
				double cy = radius * Math.sin( v );

				Vector3 pos2 = new Vector3();
	            pos2.copy( pos );
	            pos2.addX( cx * normals.get(i).getX() + cy * binormals.get(i).getX() );
	            pos2.addY( cx * normals.get(i).getY() + cy * binormals.get(i).getY() );
	            pos2.addZ( cx * normals.get(i).getZ() + cy * binormals.get(i).getZ() );

	            this.grid.get( i ).add( j, vert( pos2.getX(), pos2.getY(), pos2.getZ() ) );
			}
		}


		// construct the mesh

		for ( int i = 0; i < segments; i++ ) 
		{
			for ( int j = 0; j < segmentsRadius; j++ ) 
			{
				int ip = ( closed ) ? (i + 1) % segments : i + 1;
				int jp = (j + 1) % segmentsRadius;

				int a = this.grid.get( i ).get( j );		// *** NOT NECESSARILY PLANAR ! ***
				int b = this.grid.get( ip ).get( j );
				int c = this.grid.get( ip ).get( jp );
				int d = this.grid.get( i ).get( jp );

				Vector2 uva = new Vector2( i / (double)segments,                     j / (double)segmentsRadius );
				Vector2 uvb = new Vector2( ( i + 1.0 ) / (double)segments,           j / (double)segmentsRadius );
				Vector2 uvc = new Vector2( ( i + 1.0 ) / (double)segments, ( j + 1.0 ) / (double)segmentsRadius );
				Vector2 uvd = new Vector2( i / (double)segments,           ( j + 1.0 ) / (double)segmentsRadius );

				getFaces().add( new Face3( a, b, d ) );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList( uva, uvb, uvd ) );

				getFaces().add( new Face3( b, c, d ) );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList( uvb.clone(), uvc, uvd.clone() ) );
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
		this.getVertices().add( new Vector3( x, y, z ) );
		
		return  this.getVertices().size() - 1;
	}
}
