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

package thothbot.parallax.core.shared.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Face4;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.MeshFaceMaterial;
import thothbot.parallax.core.shared.objects.DimensionalObject;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.objects.Particle;

/**
 * Representation of a ray in space.
 * <p>
 * Pretty useful for detecting collisions between objects in a scene.
 * <p>
 * Based on three.js code.
 * 
 * @author thothbot
 *
 */
public class Ray 
{
	// Where does the ray start. Default is 0, 0, 0.
	private Vector3 origin;
	
	// A vector pointing in the direction the ray goes. Default is 0, 0, 0.
	private Vector3 direction;
	
	// Defines the closest distance that will be checked for intersections, starting from origin. Default is 0.
	private double near;
	
	// Defines the furthest distance that will be checked for intersections, starting from origin. Default is Infinity.
	// Hence, intersections will only be checked in the range (near, far).
	private double far;

	private double precision = 0.0001;
	
	// internals
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
		/**
		 * Distance between the origin and the intersected object,
		 */
		public double distance;
		/**
		 * Exact point where the intersection occurs
		 */
		public Vector3 point;
		/**
		 * Exact face that the ray intersected,
		 */
		public Face3 face;
		public int faceIndex;
		/**
		 * The intersected object
		 */
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
	
	/**
	 * Defines the maximum precision used when determining intersections. 
	 * This is specially important when checking collisions between almost parallel objects, 
	 * and can make the difference between reporting a collision or not.
	 * 
	 */
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
	
	public void intersectObject( DimensionalObject object, Ray ray, List<Ray.Intersect> intersects ) 
	{
		Matrix4 objMatrix = object.getMatrixWorld();
		
		if ( object instanceof Particle ) 
		{
			double distance = distanceFromIntersection( ray.origin, ray.direction, objMatrix.getPosition() );

			if ( distance > object.getScale().getX() ) 
			{
				return;
			}

			Ray.Intersect intersect = new Ray.Intersect();
			intersect.distance = distance;
			intersect.point = object.getPosition();
			intersect.object = object;
			
			intersects.add( intersect );
		} 
		else if ( object instanceof Mesh ) 
		{
			// Checking boundingSphere

			double scaledRadius = ((Mesh) object).getGeometry().getBoundingSphere().radius * object.getMatrixWorld().getMaxScaleOnAxis();

			// Checking distance to ray

			double distance = distanceFromIntersection( ray.origin, ray.direction, object.getMatrixWorld().getPosition() );

			if ( distance > scaledRadius) 
			{
				return;
			}

			// Checking faces

			Geometry geometry = ((Mesh) object).getGeometry();
			List<Vector3> vertices = geometry.getVertices();

			List<Material> geometryMaterials = geometry.getMaterials();
			boolean isFaceMaterial = ((Mesh) object).getMaterial() instanceof MeshFaceMaterial;
			Material.SIDE side = ((Mesh) object).getMaterial().getSides();
			
			object.getMatrixRotationWorld().extractRotation( objMatrix );

			originCopy.copy( ray.origin );

			inverseMatrix.getInverse( objMatrix );

			localOriginCopy.copy( originCopy );
			inverseMatrix.multiplyVector3( localOriginCopy );

			localDirectionCopy.copy( ray.direction );
			inverseMatrix.rotateAxis( localDirectionCopy ).normalize();

			for ( int f = 0, fl = geometry.getFaces().size(); f < fl; f ++ ) 
			{
				Face3 face = geometry.getFaces().get( f );

				Material material = ( isFaceMaterial == true ) ? geometryMaterials.get( face.getMaterialIndex() ) : ((Mesh) object).getMaterial();

				if ( material == null ) 
					continue;
				side = material.getSides();

				vector.sub( face.getCentroid(), localOriginCopy );
				normal = face.getNormal();
				double dot = localDirectionCopy.dot( normal );

				// bail if ray and plane are parallel

				if ( Math.abs( dot ) < precision ) 
					continue;

				// calc distance to plane

				double scalar = normal.dot( vector ) / dot;

				// if negative distance, then plane is behind ray

				if ( scalar < 0 ) continue;

				if ( side == Material.SIDE.DOUBLE || (side == Material.SIDE.FRONT ? dot < 0 : dot > 0 ) ) 
				{
					intersectPoint.add( localOriginCopy, localDirectionCopy.multiply( scalar ) );

					Vector3 a = vertices.get( face.getA() );
					Vector3 b = vertices.get( face.getB() );
					Vector3 c = vertices.get( face.getC() );
					
					if ( face.getClass() == Face3.class ) 
					{
						if ( pointInFace3( intersectPoint, a, b, c ) ) 
						{
							Vector3 point = objMatrix.multiplyVector3( intersectPoint.clone() );
							distance = originCopy.distanceTo( point );

							if ( distance < ray.near || distance > ray.far ) continue;
							
							Ray.Intersect intersect = new Ray.Intersect();
							intersect.distance = distance;
							intersect.point = point;
							intersect.face = face;
							intersect.faceIndex = f;
							intersect.object = object;

							intersects.add( intersect );
						}
					} 
					else if ( face.getClass() == Face4.class ) 
					{
						Vector3 d = vertices.get(((Face4)face).getD() );

						if ( pointInFace3( intersectPoint, a, b, d ) || pointInFace3( intersectPoint, b, c, d ) ) 
						{
							Vector3 point = objMatrix.multiplyVector3( intersectPoint.clone() );
							distance = originCopy.distanceTo( point );

							if ( distance < ray.near || distance > ray.far ) continue;
							
							Ray.Intersect intersect = new Ray.Intersect();
							intersect.distance = distance;
							intersect.point = point;
							intersect.face = face;
							intersect.faceIndex = f;
							intersect.object = object;

							intersects.add( intersect );
						}
					}
				}
			}
		}
	}

	public void intersectDescendants( DimensionalObject object, Ray ray, List<Ray.Intersect> intersects ) 
	{
		List<DimensionalObject> descendants = object.getDescendants();

		for ( int i = 0, l = descendants.size(); i < l; i ++ ) 
		{
			intersectObject( descendants.get( i ), ray, intersects );
		}
	}

	/**
	 * Determines whether the ray intersects object. The result is an array containing tuples of the form.
	 * <p>
	 * Note that even if we're checking only an object against a ray, 
	 * the ray can intersect the object in more than one point. 
	 * Hence, the return value is an array and not a single tuple.
	 */
	public List<Ray.Intersect> intersectObject( DimensionalObject object ) 
	{
		return intersectObject(object, false);
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

	/**
	 * Goes through the objects array, and determine whether they ray intersects them. 
	 * The result is an array that contains similar tuples to the ones returned by intersectObject
	 * <p>
	 * The entries in the returned array are sorted by ascending distance (i.e. closest objects first).
	 */
	public List<Ray.Intersect> intersectObjects(List<? extends DimensionalObject> objects)
	{
		return intersectObjects(objects, false);
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
