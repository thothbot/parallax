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

package thothbot.parallax.core.shared.geometries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.shared.core.BoundingSphere;
import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.UVf;
import thothbot.parallax.core.shared.core.Vector3f;

public class Polyhedron extends Geometry
{
	private Map<Integer, Map<Integer, ContainerOfVector>> midpoints;
	private List<ContainerOfVector> containers;
	
	public class ContainerOfVector
	{
		public Vector3f vector;
		public int index;
		public UVf uv;
		
		public ContainerOfVector(float x, float y, float z)
		{
			this.vector = new Vector3f(x, y, z);
		}
	}

	public Polyhedron(Float[][] vertices, Integer[][] faces) 
	{
		this(vertices, faces, 1, 0);
	}
	
	public Polyhedron(Float[][] vertices, Integer[][] faces, int radius, int detail) 
	{
		super();
		this.containers = new ArrayList<ContainerOfVector>();
		this.midpoints = new HashMap<Integer, Map<Integer,ContainerOfVector>>();

		for ( int i = 0, l = vertices.length; i < l; i ++ )
			prepare( new ContainerOfVector( vertices[ i ][ 0 ], vertices[ i ][ 1 ], vertices[ i ][2 ] ) );

		for ( int i = 0, l = faces.length; i < l; i ++ )
			make( this.containers.get( faces[ i ][ 0 ] ), 
					this.containers.get( faces[ i ][ 1 ] ),
					this.containers.get( faces[ i ][ 2 ] ), detail );

		this.mergeVertices();

		// Apply radius

		for ( int i = 0, l = this.vertices.size(); i < l; i ++ )
			this.vertices.get( i ).multiply( radius );
		
		this.computeCentroids();

		this.boundingSphere = new BoundingSphere(radius); 
	}
	
	/**
	 * Project vector onto sphere's surface
	 */
	protected ContainerOfVector prepare( ContainerOfVector container ) 
	{
		this.vertices.add( container.vector.normalize().clone() );
		this.containers.add( container );

		container.index = this.vertices.size() - 1;

		// Texture coords are equivalent to map coords, calculate angle and convert to fraction of a circle.
		float u = (float) (azimuth( container.vector ) / 2f / Math.PI + 0.5f);
		float v = (float) (inclination( container.vector ) / Math.PI + 0.5f);
		container.uv = new UVf( u, v );

		return container;
	}

	/**
	 * Approximate a curved face with recursively sub-divided triangles.
	 */
	protected void make( ContainerOfVector c1, ContainerOfVector c2, ContainerOfVector c3, int detail ) 
	{
		if ( detail < 1 ) 
		{
			Face3 face = new Face3( c1.index, c2.index, c3.index, Arrays.asList(c1.vector.clone(), c2.vector.clone(), c3.vector.clone()) );
			
			face.getCentroid().add( c1.vector ).add( c2.vector ).add( c3.vector ).divide( 3 );
			face.setNormal(face.getCentroid().clone().normalize());
			
			this.faces.add( face );

			double azi = azimuth( face.getCentroid() );
			this.faceVertexUvs.get( 0 ).add( Arrays.asList( 
				correctUV( c1.uv, c1.vector, azi ),
				correctUV( c2.uv, c2.vector, azi ),
				correctUV( c3.uv, c3.vector, azi )
			) );
		}
		else {

			detail -= 1;
			// split triangle into 4 smaller triangles
			make( c1, midpoint( c1, c2 ), midpoint( c1, c3 ), detail ); // top quadrant
			make( midpoint( c1, c2 ), c2, midpoint( c2, c3 ), detail ); // left quadrant
			make( midpoint( c1, c3 ), midpoint( c2, c3 ), c3, detail ); // right quadrant
			make( midpoint( c1, c2 ), midpoint( c2, c3 ), midpoint( c1, c3 ), detail ); // center quadrant

		}

	}

	protected ContainerOfVector midpoint( ContainerOfVector c1, ContainerOfVector c2 ) 
	{
		if ( !midpoints.containsKey(c1.index )) {
			midpoints.put( c1.index, new HashMap<Integer, ContainerOfVector>());
		}

		if ( !midpoints.containsKey(c2.index )) {
			midpoints.put( c2.index, new HashMap<Integer, ContainerOfVector>());
		}

		ContainerOfVector mid = midpoints.get( c1.index ).get( c2.index );
		if ( mid == null ) 
		{
			// generate mean point and project to surface with prepare()
			ContainerOfVector con = new ContainerOfVector(0,0,0);
			con.vector.add( c1.vector, c2.vector ).divide( 2 );
		
			mid = prepare(con);
			midpoints.get( c1.index ).put( c2.index, mid); 
			midpoints.get( c2.index ).put( c1.index,  mid);
		}
		return mid;
	}

	/**
	 * Angle around the Y axis, counter-clockwise when looking from above.
	 */
	protected double azimuth( Vector3f vector ) 
	{
		return Math.atan2( vector.getZ(), -vector.getX() );
	}

	/**
	 * Angle above the XZ plane.
	 */
	protected double inclination( Vector3f vector ) 
	{
		return Math.atan2( -vector.getY(), Math.sqrt( ( vector.getX() * vector.getX() ) + ( vector.getZ() * vector.getZ() ) ) );
	}

	/**
	 * Texture fixing helper. Spheres have some odd behaviours.
	 */
	protected UVf correctUV( UVf uv, Vector3f vector, double azimuth ) 
	{
		if ( (azimuth < 0) && (uv.getU() == 1) ) 
			uv = new UVf( uv.getU() - 1.0f, uv.getV() );
		
		if ( (vector.getX() == 0) && (vector.getZ() == 0) ) 
			uv = new UVf( (float) (azimuth / 2f / Math.PI + 0.5), uv.getV() );
		
		return uv;
	}
}
