/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package thothbot.parallax.core.shared.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.MeshFaceMaterial;
import thothbot.parallax.core.shared.objects.DimensionalObject;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.objects.Particle;

public class Ray 
{
	private Vector3 origin;
	private Vector3 direction;
	private double near;
	private double far;

	private double precision = 0.0001;
	
	// internal
	private Vector3 originCopy;

	private Vector3 localOriginCopy;
	private Vector3 localDirectionCopy;

	private Vector3 vector;
	private Vector3 normal;
	private Vector3 intersectPoint;

	private Matrix4 inverseMatrix;
	
	Vector3 v0 = new Vector3(), v1 = new Vector3(), v2 = new Vector3();

	public class Intersect implements Comparable<Intersect>
	{
		public double distance;
		public Vector3 point;
		public Face3 face;
		public int faceIndex;
		public DimensionalObject object;

		@Override
		public int compareTo(Intersect o) 
		{
			double result = o.distance - this.distance; 
			return (result == 0) ? 0 
					: (result > 0) ? 1 : -1;
		}
	}

	public Ray()
	{
		this(new Vector3(), new Vector3());
	}

	public Ray( Vector3 origin, Vector3 direction )
	{
		this(origin, direction, 0, Double.POSITIVE_INFINITY);
	}

	public Ray( Vector3 origin, Vector3 direction, double near, double far ) 
	{
		this.origin = origin;
		this.direction = direction;
		this.near = near;
		this.far = far;
		
		this.originCopy = new Vector3();

		this.localOriginCopy = new Vector3();
		this.localDirectionCopy = new Vector3();

		this.vector = new Vector3();
		this.normal = new Vector3();
		this.intersectPoint = new Vector3();

		this.inverseMatrix = new Matrix4();
	}
	
	public void set( Vector3 origin, Vector3 direction ) 
	{
		this.origin = origin;
		this.direction = direction;
	}
	
	public void setPrecision( double value ) 
	{
		this.precision = value;
	}

	private double distanceFromIntersection( Vector3 origin, Vector3 direction, Vector3 position ) 
	{	
		v0.sub( position, origin );

		double dot = v0.dot( direction );

		Vector3 intersect = v1.add( origin, v2.copy( direction ).multiply( dot ) );
		double distance = position.distanceTo( intersect );

		return distance;
	}

	/**
	 * <a href="http://www.blackpawn.com/texts/pointinpoly/default.html">www.blackpawn.com</a>
	 * @return  true or false
	 */
	public boolean pointInFace3( Vector3 p, Vector3 a, Vector3 b, Vector3 c ) 
	{
		v0.sub( c, a );
		v1.sub( b, a );
		v2.sub( p, a );

		double dot00 = v0.dot( v0 );
		double dot01 = v0.dot( v1 );
		double dot02 = v0.dot( v2 );
		double dot11 = v1.dot( v1 );
		double dot12 = v1.dot( v2 );

		double invDenom = 1 / ( dot00 * dot11 - dot01 * dot01 );
		double u = ( dot11 * dot02 - dot01 * dot12 ) * invDenom;
		double v = ( dot00 * dot12 - dot01 * dot02 ) * invDenom;

		return ( u >= 0 ) && ( v >= 0 ) && ( u + v < 1 );
	}
	var intersectObject = function ( object, ray, intersects ) {

		var distance,intersect;

		if ( object instanceof THREE.Particle ) {

			distance = distanceFromIntersection( ray.origin, ray.direction, object.matrixWorld.getPosition() );

			if ( distance > object.scale.x ) {

				return intersects;

			}

			intersect = {

				distance: distance,
				point: object.position,
				face: null,
				object: object

			};

			intersects.push( intersect );

		} else if ( object instanceof THREE.Mesh ) {

			// Checking boundingSphere

			var scaledRadius = object.geometry.boundingSphere.radius * object.matrixWorld.getMaxScaleOnAxis();

			// Checking distance to ray

			distance = distanceFromIntersection( ray.origin, ray.direction, object.matrixWorld.getPosition() );

			if ( distance > scaledRadius) {

				return intersects;

			}

			// Checking faces

			var f, fl, face, dot, scalar,
			geometry = object.geometry,
			vertices = geometry.vertices,
			objMatrix, geometryMaterials,
			isFaceMaterial, material, side, point;

			geometryMaterials = object.geometry.materials;
			isFaceMaterial = object.material instanceof THREE.MeshFaceMaterial;
			side = object.material.side;

			var a, b, c, d;
			var precision = ray.precision;

			object.matrixRotationWorld.extractRotation( object.matrixWorld );

			originCopy.copy( ray.origin );

			objMatrix = object.matrixWorld;
			inverseMatrix.getInverse( objMatrix );

			localOriginCopy.copy( originCopy );
			inverseMatrix.multiplyVector3( localOriginCopy );

			localDirectionCopy.copy( ray.direction );
			inverseMatrix.rotateAxis( localDirectionCopy ).normalize();

			for ( f = 0, fl = geometry.faces.length; f < fl; f ++ ) {

				face = geometry.faces[ f ];

				material = isFaceMaterial === true ? geometryMaterials[ face.materialIndex ] : object.material;
				if ( material === undefined ) continue;
				side = material.side;

				vector.sub( face.centroid, localOriginCopy );
				normal = face.normal;
				dot = localDirectionCopy.dot( normal );

				// bail if ray and plane are parallel

				if ( Math.abs( dot ) < precision ) continue;

				// calc distance to plane

				scalar = normal.dot( vector ) / dot;

				// if negative distance, then plane is behind ray

				if ( scalar < 0 ) continue;

				if ( side === THREE.DoubleSide || ( side === THREE.FrontSide ? dot < 0 : dot > 0 ) ) {

					intersectPoint.add( localOriginCopy, localDirectionCopy.multiplyScalar( scalar ) );

					if ( face instanceof THREE.Face3 ) {

						a = vertices[ face.a ];
						b = vertices[ face.b ];
						c = vertices[ face.c ];

						if ( pointInFace3( intersectPoint, a, b, c ) ) {

							point = object.matrixWorld.multiplyVector3( intersectPoint.clone() );
							distance = originCopy.distanceTo( point );

							if ( distance < ray.near || distance > ray.far ) continue;

							intersect = {

								distance: distance,
								point: point,
								face: face,
								faceIndex: f,
								object: object

							};

							intersects.push( intersect );

						}

					} else if ( face instanceof THREE.Face4 ) {

						a = vertices[ face.a ];
						b = vertices[ face.b ];
						c = vertices[ face.c ];
						d = vertices[ face.d ];

						if ( pointInFace3( intersectPoint, a, b, d ) || pointInFace3( intersectPoint, b, c, d ) ) {

							point = object.matrixWorld.multiplyVector3( intersectPoint.clone() );
							distance = originCopy.distanceTo( point );

							if ( distance < ray.near || distance > ray.far ) continue;

							intersect = {

								distance: distance,
								point: point,
								face: face,
								faceIndex: f,
								object: object

							};

							intersects.push( intersect );
						}
					}
				}
			}
		}
	}

	public void intersectDescendants( DimensionalObject object, Ray ray, List<Ray.Intersect> intersects ) 
	{
		var descendants = object.getDescendants();

		for ( int i = 0, l = descendants.length; i < l; i ++ ) 
		{
			intersectObject( descendants[ i ], ray, intersects );
		}
	}

	public List<Ray.Intersect> intersectObject( DimensionalObject object, boolean recursive ) 
	{

		List<Ray.Intersect> intersects = new ArrayList<Ray.Intersect>();

		if ( recursive == true ) 
		{
			intersectDescendants( object, this, intersects );
		}

		intersectObject( object, this, intersects );

		Collections.sort( intersects );

		return intersects;
	}

	public List<Ray.Intersect> intersectObjects(List<? extends DimensionalObject> objects, boolean recursive) 
	{
		List<Ray.Intersect> intersects = new ArrayList<Ray.Intersect>();

		for ( int i = 0, l = objects.size(); i < l; i ++ )
		{
			intersectObject( objects.get( i ), this, intersects );

			if ( recursive == true ) 
			{
				intersectDescendants( objects.get( i ), this, intersects );
			}
		}

		Collections.sort( intersects );

		return intersects;
	}
}
