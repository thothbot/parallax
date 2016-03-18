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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Sphere;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;

@ThreejsObject("THREE.PolyhedronGeometry")
public abstract class PolyhedronGeometry extends Geometry
{
	private List<ContainerOfVector> containers;

	public class ContainerOfVector
	{
		public Vector3 vector;
		public int index;
		public Vector2 uv;

		public ContainerOfVector(Vector3 v)
		{
			this.vector = v;
		}

		public ContainerOfVector(double x, double y, double z)
		{
			this.vector = new Vector3(x, y, z);
		}
	}

	public PolyhedronGeometry()
	{
		this(1.0, 0);
	}

	public PolyhedronGeometry(double radius, int detail)
	{
		super();
		this.containers = new ArrayList<>();

		double[] vertices = vertexSet();
		for ( int i = 0, l = vertices.length; i < l; i += 3 )
			prepare( new ContainerOfVector( vertices[ i ], vertices[ i + 1 ], vertices[ i + 2 ] ) );

		int[] indices = indexSet();

		Face3[] faces = new Face3[indices.length];
		for ( int i = 0, j = 0, l = indices.length; i < l; i += 3, j ++ ) {

			ContainerOfVector v1 = containers.get(indices[i]);
			ContainerOfVector v2 = containers.get(indices[i + 1]);
			ContainerOfVector v3 = containers.get(indices[i + 2]);

			faces[j] = new Face3(v1.index, v2.index, v3.index, Arrays.asList( v1.vector.clone(), v2.vector.clone(), v3.vector.clone()));
			faces[j].setMaterialIndex(j);

		}

		for ( int i = 0, l = faces.length; i < l; i ++ ) {

			subdivide( faces[ i ], detail );

		}

		// Handle case when face straddles the seam

		for (int i = 0, l = this.getFaceVertexUvs().get(0).size(); i < l; i ++ ) {

			List<Vector2> uvs = this.getFaceVertexUvs().get(0).get(i);

			double x0 = uvs.get(0).getX();
			double x1 = uvs.get(1).getX();
			double x2 = uvs.get(2).getX();

			double max = Math.max( x0, Math.max( x1, x2 ));
			double min = Math.min( x0, Math.min( x1, x2 ));

			if ( max > 0.9 && min < 0.1 ) {

				// 0.9 is somewhat arbitrary

				if ( x0 < 0.2 ) uvs.get(0).setX(uvs.get(0).getX() + 1);
				if ( x1 < 0.2 ) uvs.get(1).setX(uvs.get(1).getX() + 1);
				if ( x2 < 0.2 ) uvs.get(2).setX(uvs.get(2).getX() + 1);

			}

		}

		// Apply radius

		for ( int i = 0, l = getVertices().size(); i < l; i ++ )
			getVertices().get( i ).multiply( radius );

		// Merge vertices

		this.mergeVertices();

		this.computeFaceNormals();

		this.boundingSphere = new Sphere( new Vector3(), radius );
	}

	/**
	 * Gets geometry vertices.
	 */
	protected abstract double[] vertexSet();

	/**
	 * Gets geometry faces.
	 */
	protected abstract int[] indexSet();

	/**
	 * Project vector onto sphere's surface
	 */
	protected ContainerOfVector prepare( ContainerOfVector container )
	{
		getVertices().add( container.vector.normalize().clone() );
		this.containers.add( container );

		container.index = getVertices().size() - 1;

		// Texture coords are equivalent to map coords, calculate angle and convert to fraction of a circle.
		double u = azimuth( container.vector ) / 2.0 / Math.PI + 0.5;
		double v = inclination( container.vector ) / Math.PI + 0.5;
		container.uv = new Vector2( u, v );

		return container;
	}

	/**
	 * Approximate a curved face with recursively sub-divided triangles.
	 */
	protected void make( ContainerOfVector c1, ContainerOfVector c2, ContainerOfVector c3, int materialIndex )
	{
		Face3 face = new Face3( c1.index, c2.index, c3.index, Arrays.asList(c1.vector.clone(), c2.vector.clone(), c3.vector.clone()) );
		face.setMaterialIndex(materialIndex);
		getFaces().add( face );

		Vector3 centroid = new Vector3();
		centroid.copy( c1.vector ).add( c2.vector ).add( c3.vector ).divide( 3.0 );
		double azi = azimuth( centroid );

		getFaceVertexUvs().get( 0 ).add( Arrays.asList(
				correctUV( c1.uv, c1.vector, azi ),
				correctUV( c2.uv, c2.vector, azi ),
				correctUV( c3.uv, c3.vector, azi )
		) );
	}

	/**
	 * Angle around the Y axis, counter-clockwise when looking from above.
	 */
	protected double azimuth( Vector3 vector )
	{
		return Math.atan2( vector.getZ(), -vector.getX() );
	}

	/**
	 * Angle above the XZ plane.
	 */
	protected double inclination( Vector3 vector )
	{
		return Math.atan2( -vector.getY(), Math.sqrt( ( vector.getX() * vector.getX() ) + ( vector.getZ() * vector.getZ() ) ) );
	}

	/**
	 * Texture fixing helper. Spheres have some odd behaviours.
	 */
	protected Vector2 correctUV( Vector2 uv, Vector3 vector, double azimuth )
	{
		if ( (azimuth < 0) && (uv.getX() == 1) )
			uv = new Vector2( uv.getX() - 1.0, uv.getY() );

		if ( (vector.getX() == 0) && (vector.getZ() == 0) )
			uv = new Vector2( azimuth / 2.0 / Math.PI + 0.5, uv.getY() );

		return uv.clone();
	}

	protected void subdivide( Face3 face, double detail )
	{

		int cols = (int) Math.pow( 2, detail );
		ContainerOfVector a = prepare( new ContainerOfVector( getVertices().get(face.getA()) ));
		ContainerOfVector b = prepare( new ContainerOfVector( getVertices().get(face.getB()) ));
		ContainerOfVector c = prepare( new ContainerOfVector( getVertices().get(face.getC()) ));

		ContainerOfVector[][] v = new ContainerOfVector[cols + 1][];

		int materialIndex = face.getMaterialIndex();

		// Construct all of the vertices for this subdivision.

		for ( int i = 0 ; i <= cols; i ++ ) {

			ContainerOfVector aj = prepare( new ContainerOfVector(a.vector.clone().lerp( c.vector, i / cols ) ));
			ContainerOfVector bj = prepare( new ContainerOfVector(b.vector.clone().lerp( c.vector, i / cols ) ));
			int rows = cols - i;

			v[ i ] = new ContainerOfVector[rows + 1];

			for ( int j = 0; j <= rows; j ++ ) {

				if ( j == 0 && i == cols ) {

					v[ i ][ j ] = aj;

				} else {

					v[ i ][ j ] = prepare( new ContainerOfVector(aj.vector.clone().lerp( bj.vector, j / rows ) ));

				}

			}

		}

		// Construct all of the faces.

		for ( int i = 0; i < cols ; i ++ ) {

			for ( int j = 0; j < 2 * ( cols - i ) - 1; j ++ ) {

				int k = (int) Math.floor( j / 2 );

				if ( j % 2 == 0 ) {

					make(
							v[ i ][ k + 1 ],
							v[ i + 1 ][ k ],
							v[ i ][ k ],
							materialIndex
					);

				} else {

					make(
							v[ i ][ k + 1 ],
							v[ i + 1 ][ k + 1 ],
							v[ i + 1 ][ k ],
							materialIndex
					);

				}

			}

		}

	}
}
