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
	
	//Temporary variables
	static Vector3 _v1 = new Vector3();
	static Vector3 _v2 = new Vector3();
	static Vector3 _diff = new Vector3();
	static Vector3 _edge1 = new Vector3();
	static Vector3 _edge2 = new Vector3();
	static Vector3 _normal = new Vector3();
	
	public Ray()
	{
		this(new Vector3(), new Vector3());
	}

	public Ray( Vector3 origin, Vector3 direction ) 
	{
		this.origin = origin;
		this.direction = direction;
	}
	
	public Vector3 getOrigin() {
		return origin;
	}

	public void setOrigin(Vector3 origin) {
		this.origin = origin;
	}

	public Vector3 getDirection() {
		return direction;
	}

	public void setDirection(Vector3 direction) {
		this.direction = direction;
	}
	
	public Ray set( Vector3 origin, Vector3 direction ) 
	{
		this.origin.copy( origin );
		this.direction.copy( direction );

		return this;
	}

	public Ray copy( Ray ray ) 
	{
		this.origin.copy( ray.origin );
		this.direction.copy( ray.direction );

		return this;
	}

	public Vector3 at( double t)
	{
		return at(t, new Vector3());
	}
	
	public Vector3 at( double t, Vector3 optionalTarget ) 
	{
		return optionalTarget.copy( this.direction ).multiply( t ).add( this.origin );
	}

	public Ray recast( double t ) 
	{
		this.origin.copy( this.at( t, _v1 ) );

		return this;
	}

	public Vector3 closestPointToPoint( Vector3 point)
	{
		return closestPointToPoint(point, new Vector3());
	}
	
	public Vector3 closestPointToPoint( Vector3 point, Vector3 optionalTarget ) 
	{
		optionalTarget.sub( point, this.origin );
		double directionDistance = optionalTarget.dot( this.direction );
		
		if ( directionDistance < 0 ) {

			return optionalTarget.copy( this.origin );

		}

		return optionalTarget.copy( this.direction ).multiply( directionDistance ).add( this.origin );
	}

	public double distanceToPoint( Vector3 point ) 
	{		
		double directionDistance = _v1.sub( point, this.origin ).dot( this.direction );

		// point behind the ray

		if ( directionDistance < 0 ) {

			return this.origin.distanceTo( point );

		}

		_v1.copy( this.direction ).multiply( directionDistance ).add( this.origin );

		return _v1.distanceTo( point );

	}
	
	public double distanceSqToSegment( Vector3 v0, Vector3 v1, Vector3 optionalPointOnRay, Vector3 optionalPointOnSegment ) 
	{
		// from http://www.geometrictools.com/LibMathematics/Distance/Wm5DistRay3Segment3.cpp
		// It returns the min distance between the ray and the segment
		// defined by v0 and v1
		// It can also set two optional targets :
		// - The closest point on the ray
		// - The closest point on the segment

		Vector3 segCenter = v0.clone().add( v1 ).multiply( 0.5 );
		Vector3 segDir = v1.clone().sub( v0 ).normalize();
		double segExtent = v0.distanceTo( v1 ) * 0.5;
		Vector3 diff = this.origin.clone().sub( segCenter );
		double a01 = - this.direction.dot( segDir );
		double b0 = diff.dot( this.direction );
		double b1 = - diff.dot( segDir );
		double c = diff.lengthSq();
		double det = Math.abs( 1 - a01 * a01 );
		double s0, s1, sqrDist, extDet;

		if ( det >= 0 ) {

			// The ray and segment are not parallel.

			s0 = a01 * b1 - b0;
			s1 = a01 * b0 - b1;
			extDet = segExtent * det;

			if ( s0 >= 0 ) {

				if ( s1 >= - extDet ) {

					if ( s1 <= extDet ) {

						// region 0
						// Minimum at interior points of ray and segment.

						double invDet = 1.0 / det;
						s0 *= invDet;
						s1 *= invDet;
						sqrDist = s0 * ( s0 + a01 * s1 + 2.0 * b0 ) + s1 * ( a01 * s0 + s1 + 2.0 * b1 ) + c;

					} else {

						// region 1

						s1 = segExtent;
						s0 = Math.max( 0, - ( a01 * s1 + b0 ) );
						sqrDist = - s0 * s0 + s1 * ( s1 + 2 * b1 ) + c;

					}

				} else {

					// region 5

					s1 = - segExtent;
					s0 = Math.max( 0, - ( a01 * s1 + b0 ) );
					sqrDist = - s0 * s0 + s1 * ( s1 + 2.0 * b1 ) + c;

				}

			} else {

				if ( s1 <= - extDet ) {

					// region 4

					s0 = Math.max( 0, - ( - a01 * segExtent + b0 ) );
					s1 = ( s0 > 0 ) ? - segExtent : Math.min( Math.max( - segExtent, - b1 ), segExtent );
					sqrDist = - s0 * s0 + s1 * ( s1 + 2 * b1 ) + c;

				} else if ( s1 <= extDet ) {

					// region 3

					s0 = 0;
					s1 = Math.min( Math.max( - segExtent, - b1 ), segExtent );
					sqrDist = s1 * ( s1 + 2.0 * b1 ) + c;

				} else {

					// region 2

					s0 = Math.max( 0, - ( a01 * segExtent + b0 ) );
					s1 = ( s0 > 0 ) ? segExtent : Math.min( Math.max( - segExtent, - b1 ), segExtent );
					sqrDist = - s0 * s0 + s1 * ( s1 + 2.0 * b1 ) + c;

				}

			}

		} else {

			// Ray and segment are parallel.

			s1 = ( a01 > 0 ) ? - segExtent : segExtent;
			s0 = Math.max( 0, - ( a01 * s1 + b0 ) );
			sqrDist = - s0 * s0 + s1 * ( s1 + 2.0 * b1 ) + c;

		}

		if ( optionalPointOnRay != null ) {

			optionalPointOnRay.copy( this.direction.clone().multiply( s0 ).add( this.origin ) );

		}

		if ( optionalPointOnSegment != null ) {

			optionalPointOnSegment.copy( segDir.clone().multiply( s1 ).add( segCenter ) );

		}

		return sqrDist;

	}

	public boolean isIntersectionSphere( Sphere sphere ) 
	{
		return ( this.distanceToPoint( sphere.getCenter() ) <= sphere.getRadius() );
	}
	
	public Vector3 intersectSphere( Sphere sphere ) 
	{
		return intersectSphere(sphere, null);
	}

	/**
	 * from http://www.scratchapixel.com/lessons/3d-basic-lessons/lesson-7-intersecting-simple-shapes/ray-sphere-intersection/
	 * @param sphere
	 * @param optionalTarget
	 * @return
	 */
	public Vector3 intersectSphere( Sphere sphere, Vector3 optionalTarget ) 
	{
		_v1.sub( sphere.getCenter(), this.getOrigin() );

		double tca = _v1.dot( this.direction );

		double d2 = _v1.dot( _v1 ) - tca * tca;

		double radius2 = sphere.getRadius() * sphere.getRadius();

		if ( d2 > radius2 ) return null;

		double thc = Math.sqrt( radius2 - d2 );

		// t0 = first intersect point - entrance on front of sphere
		double t0 = tca - thc;

		// t1 = second intersect point - exit point on back of sphere
		double t1 = tca + thc;

		// test to see if both t0 and t1 are behind the ray - if so, return null
		if ( t0 < 0 && t1 < 0 ) return null;

		// test to see if t0 is behind the ray:
		// if it is, the ray is inside the sphere, so return the second exit point scaled by t1,
		// in order to always return an intersect point that is in front of the ray.
		if ( t0 < 0 ) return this.at( t1, optionalTarget );

		// else t0 is in front of the ray, so return the first collision point scaled by t0 
		return this.at( t0, optionalTarget );
	}

	public boolean isIntersectionPlane( Plane plane ) 
	{
		// check if the ray lies on the plane first

		double distToPoint = plane.distanceToPoint( this.origin );

		if ( distToPoint == 0 ) {

			return true;

		}

		double denominator = plane.getNormal().dot( this.direction );

		if ( denominator * distToPoint < 0 ) {

			return true;

		}

		// ray origin is behind the plane (and is pointing behind it)

		return false;

	}

	public Double distanceToPlane( Plane plane ) 
	{
		double denominator = plane.getNormal().dot( this.direction );
		if ( denominator == 0 ) {

			// line is coplanar, return origin
			if ( plane.distanceToPoint( this.origin ) == 0 ) {

				return 0.0;

			}

			// Null is preferable to undefined since undefined means.... it is undefined

			return null;

		}

		double t = - ( this.origin.dot( plane.getNormal() ) + plane.getConstant() ) / denominator;

		// Return if the ray never intersects the plane

		return t >= 0 ? t :  null;

	}

	public Vector3 intersectPlane( Plane plane)
	{
		return intersectPlane(plane, new Vector3());
	}

	public Vector3 intersectPlane( Plane plane, Vector3 optionalTarget ) 
	{
		Double t = this.distanceToPlane( plane );

		if( t == null ) 
		{
			return null;
		}

		return this.at( t, optionalTarget );
	}
	
	public boolean isIntersectionBox(Box3 box) 
	{
		return this.intersectBox( box, _v2 ) != null;
	}

	/**
	 * http://www.scratchapixel.com/lessons/3d-basic-lessons/lesson-7-intersecting-simple-shapes/ray-box-intersection/
	 * @param box
	 * @param optionalTarget
	 * @return
	 */
	public Vector3 intersectBox( Box3 box, Vector3 optionalTarget ) 
	{
		double tmin,tmax,tymin,tymax,tzmin,tzmax;

		double invdirx = 1.0 / this.direction.x,
			invdiry = 1.0 / this.direction.y,
			invdirz = 1.0 / this.direction.z;

		if ( invdirx >= 0 ) {

			tmin = ( box.getMin().x - origin.x ) * invdirx;
			tmax = ( box.getMax().x - origin.x ) * invdirx;

		} else {

			tmin = ( box.getMax().x - origin.x ) * invdirx;
			tmax = ( box.getMin().x - origin.x ) * invdirx;
		}

		if ( invdiry >= 0 ) {

			tymin = ( box.getMin().y - origin.y ) * invdiry;
			tymax = ( box.getMax().y - origin.y ) * invdiry;

		} else {

			tymin = ( box.getMax().y - origin.y ) * invdiry;
			tymax = ( box.getMin().y - origin.y ) * invdiry;
		}

		if ( ( tmin > tymax ) || ( tymin > tmax ) ) return null;

		// These lines also handle the case where tmin or tmax is NaN
		// (result of 0 * Infinity). x !== x returns true if x is NaN

		if ( tymin > tmin || tmin != tmin ) tmin = tymin;

		if ( tymax < tmax || tmax != tmax ) tmax = tymax;

		if ( invdirz >= 0 ) {

			tzmin = ( box.getMin().z - origin.z ) * invdirz;
			tzmax = ( box.getMax().z - origin.z ) * invdirz;

		} else {

			tzmin = ( box.getMax().z - origin.z ) * invdirz;
			tzmax = ( box.getMin().z - origin.z ) * invdirz;
		}

		if ( ( tmin > tzmax ) || ( tzmin > tmax ) ) return null;

		if ( tzmin > tmin || tmin != tmin ) tmin = tzmin;

		if ( tzmax < tmax || tmax != tmax ) tmax = tzmax;

		//return point closest to the ray (positive side)

		if ( tmax < 0 ) return null;

		return this.at( tmin >= 0 ? tmin : tmax, optionalTarget );
	}
	
	public Vector3 intersectTriangle(Vector3 a, Vector3 b, Vector3 c, boolean backfaceCulling) 
	{
		return intersectTriangle(a,b,c, backfaceCulling, new Vector3());
	}
	
	/**
	 * from http://www.geometrictools.com/LibMathematics/Intersection/Wm5IntrRay3Triangle3.cpp
	 * @param a
	 * @param b
	 * @param c
	 * @param backfaceCulling
	 * @param optionalTarget
	 * @return
	 */
	public Vector3 intersectTriangle(Vector3 a, Vector3 b, Vector3 c, boolean backfaceCulling, Vector3 optionalTarget) {

		// Compute the offset origin, edges, and normal.

		_edge1.sub( b, a );
		_edge2.sub( c, a );
		_normal.cross( _edge1, _edge2 );

		// Solve Q + t*D = b1*E1 + b2*E2 (Q = kDiff, D = ray direction,
		// E1 = kEdge1, E2 = kEdge2, N = Cross(E1,E2)) by
		//   |Dot(D,N)|*b1 = sign(Dot(D,N))*Dot(D,Cross(Q,E2))
		//   |Dot(D,N)|*b2 = sign(Dot(D,N))*Dot(D,Cross(E1,Q))
		//   |Dot(D,N)|*t = -sign(Dot(D,N))*Dot(Q,N)
		double DdN = this.direction.dot( _normal );
		double sign;

		if ( DdN > 0 ) {

			if ( backfaceCulling ) return null;
			sign = 1.0;

		} else if ( DdN < 0 ) {

			sign = - 1.0;
			DdN = - DdN;

		} else {

			return null;

		}

		_diff.sub( this.origin, a );
		double DdQxE2 = sign * this.direction.dot( _edge2.cross( _diff, _edge2 ) );

		// b1 < 0, no intersection
		if ( DdQxE2 < 0 ) {

			return null;

		}

		double DdE1xQ = sign * this.direction.dot( _edge1.cross( _diff ) );

		// b2 < 0, no intersection
		if ( DdE1xQ < 0 ) {

			return null;

		}

		// b1+b2 > 1, no intersection
		if ( DdQxE2 + DdE1xQ > DdN ) {

			return null;

		}

		// Line intersects triangle, check if ray does.
		double QdN = - sign * _diff.dot( _normal );

		// t < 0, no intersection
		if ( QdN < 0 ) {

			return null;

		}

		// Ray intersects triangle.
		return this.at( QdN / DdN, optionalTarget );

	}

	public Ray apply( Matrix4 matrix4 ) 
	{
		this.direction.add( this.origin ).apply( matrix4 );
		this.origin.apply( matrix4 );
		this.direction.sub( this.origin );
		this.direction.normalize();

		return this;
	}

	public boolean equals( Ray ray ) 
	{
		return ray.origin.equals( this.origin ) && ray.direction.equals( this.direction );
	}

	public Ray clone() 
	{
		return new Ray().copy( this );
	}
}
