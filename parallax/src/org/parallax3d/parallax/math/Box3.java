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

package org.parallax3d.parallax.math;

import java.util.List;

import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

@ThreejsObject("THREE.Box3")
public class Box3
{
	Vector3 min;
	Vector3 max;

	// Temporary variables
	static Vector3 _v1 = new Vector3();

	public Box3()
	{
		this(new Vector3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
				new Vector3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
	}

	public Box3(Vector3 min, Vector3 max)
	{
		this.min = min;
		this.max = max;
	}

	public Vector3 getMin() {
		return min;
	}

	public void setMin(Vector3 min) {
		this.min = min;
	}

	public Vector3 getMax() {
		return max;
	}

	public void setMax(Vector3 max) {
		this.max = max;
	}

	public Box3 set( Vector3 min, Vector3 max )
	{
		this.min.copy( min );
		this.max.copy( max );

		return this;
	}

	public Box3 setFromArray( Float32Array array )
	{
		this.makeEmpty();

		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double minZ = Double.POSITIVE_INFINITY;

		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double maxZ = Double.NEGATIVE_INFINITY;

		for ( int i = 0, il = array.getLength(); i < il; i += 3 ) {

			double x = array.get( i );
			double y = array.get( i + 1 );
			double z = array.get( i + 2 );

			if ( x < minX ) minX = x;
			if ( y < minY ) minY = y;
			if ( z < minZ ) minZ = z;

			if ( x > maxX ) maxX = x;
			if ( y > maxY ) maxY = y;
			if ( z > maxZ ) maxZ = z;

		}

		this.min.set( minX, minY, minZ );
		this.max.set( maxX, maxY, maxZ );

		return this;
	}

	public Box3 setFromPoints( List<Vector3> points )
	{
		return setFromPoints(points.toArray(new Vector3[points.size()]));
	}

	public Box3 setFromPoints( Vector3[] points )
	{
		this.makeEmpty();

		for ( int i = 0, il = points.length; i < il; i ++ )
		{
			this.expandByPoint( points[ i ] );
		}

		return this;
	}

	public Box3 setFromCenterAndSize( Vector3 center, Vector3 size )
	{
		Vector3 halfSize = _v1.copy( size ).multiply( 0.5 );

		this.min.copy( center ).sub( halfSize );
		this.max.copy( center ).add( halfSize );

		return this;

	}

	static final Box3 box = new Box3();
	/**
	 * Computes the world-axis-aligned bounding box of an object (including its children),
	 * accounting for both the object's, and childrens', world transforms
	 *
	 * @param object
	 * @return
     */
	public Box3 setFromObject(Object3D object)
	{

		this.makeEmpty();

		object.updateMatrixWorld( true );

		object.traverse(new Object3D.Traverse() {

			@Override
			public void callback(Object3D node) {

				AbstractGeometry geometry = ((GeometryObject)node).getGeometry();

				Vector3 v1 = new Vector3();

				if ( geometry != null ) {

					if ( geometry.getBoundingBox() == null )
					{
						geometry.computeBoundingBox();
					}

					if (!geometry.getBoundingBox().isEmpty())
					{
						box.copy(geometry.getBoundingBox());
						box.apply(node.getMatrixWorld());
						Box3.this.union(box);
					}
				}
			}
		});

		return this;
	}

	public Box3 copy( Box3 box )
	{
		this.min.copy( box.min );
		this.max.copy( box.max );

		return this;
	}

	public Box3 makeEmpty()
	{
		this.min.x = this.min.y = this.min.z = Double.POSITIVE_INFINITY;
		this.max.x = this.max.y = this.max.z = Double.NEGATIVE_INFINITY;

		return this;
	}

	public boolean isEmpty()
	{
		// this is a more robust check for empty than ( volume <= 0 ) because volume can get positive with two negative axes

		return ( this.max.x < this.min.x ) || ( this.max.y < this.min.y ) || ( this.max.z < this.min.z );
	}


	public Vector3 center()
	{
		return center(new Vector3());
	}

	public Vector3 center( Vector3 optionalTarget )
	{
		return optionalTarget.add( this.min, this.max ).multiply( 0.5 );
	}

	public Vector3 size()
	{
		return size(new Vector3());
	}

	public Vector3 size( Vector3 optionalTarget )
	{
		return optionalTarget.sub( this.max, this.min );
	}

	public Box3 expandByPoint( Vector3 point )
	{
		this.min.min( point );
		this.max.max( point );

		return this;
	}

	public Box3 expandByVector( Vector3 vector )
	{
		this.min.sub( vector );
		this.max.add( vector );

		return this;
	}

	public Box3 expandByScalar( double scalar )
	{
		this.min.add( -scalar );
		this.max.add( scalar );

		return this;
	}

	public boolean containsPoint( Vector3 point )
	{
		return !(point.x < this.min.x || point.x > this.max.x ||
				point.y < this.min.y || point.y > this.max.y ||
				point.z < this.min.z || point.z > this.max.z);

	}

	public boolean containsBox( Box3 box )
	{
		return (this.min.x <= box.min.x) && (box.max.x <= this.max.x) &&
				(this.min.y <= box.min.y) && (box.max.y <= this.max.y) &&
				(this.min.z <= box.min.z) && (box.max.z <= this.max.z);

	}

	public Vector3 getParameter( Vector3 point )
	{
		// This can potentially have a divide by zero if the box
		// has a size dimension of 0.

		return new Vector3(
				( point.x - this.min.x ) / ( this.max.x - this.min.x ),
				( point.y - this.min.y ) / ( this.max.y - this.min.y ),
				( point.z - this.min.z ) / ( this.max.z - this.min.z )
		);
	}

	public boolean intersectsBox(Box3 box )
	{
		// using 6 splitting planes to rule out intersections.

		return !(box.max.x < this.min.x || box.min.x > this.max.x ||
				box.max.y < this.min.y || box.min.y > this.max.y ||
				box.max.z < this.min.z || box.min.z > this.max.z);

	}

	public boolean intersectsSphere(Sphere sphere)
	{

		_v1.set(0,0,0);

		// Find the point on the AABB closest to the sphere center.
		this.clampPoint( sphere.getCenter(), _v1 );

		// If that point is inside the sphere, the AABB and sphere intersect.
		return _v1.distanceToSquared( sphere.getCenter() ) <= ( sphere.getRadius() * sphere.getRadius() );
	}

	/**
	 * We compute the minimum and maximum dot product values. If those values
	 * are on the same side (back or front) of the plane, then there is no intersection.
	 *
	 * @param plane
	 * @return
     */
	public boolean intersectsPlane( Plane plane )
	{

		double min, max;

		if ( plane.getNormal().getX() > 0 ) {

			min = plane.getNormal().getX() * this.min.x;
			max = plane.getNormal().getX() * this.max.x;

		} else {

			min = plane.getNormal().getX() * this.max.x;
			max = plane.getNormal().getX() * this.min.x;

		}

		if ( plane.getNormal().getY() > 0 ) {

			min += plane.getNormal().getY() * this.min.y;
			max += plane.getNormal().getY() * this.max.y;

		} else {

			min += plane.getNormal().getY() * this.max.y;
			max += plane.getNormal().getY() * this.min.y;

		}

		if ( plane.getNormal().getZ() > 0 ) {

			min += plane.getNormal().getZ() * this.min.z;
			max += plane.getNormal().getZ() * this.max.z;

		} else {

			min += plane.getNormal().getZ() * this.max.z;
			max += plane.getNormal().getZ() * this.min.z;

		}

		return ( min <= plane.getConstant() && max >= plane.getConstant() );

	}

	public Vector3 clampPoint( Vector3 point)
	{
		return clampPoint(point, new Vector3());
	}

	public Vector3 clampPoint( Vector3 point, Vector3 optionalTarget )
	{
		return optionalTarget.copy( point ).clamp( this.min, this.max );
	}

	public double distanceToPoint( Vector3 point )
	{
		Vector3 clampedPoint = _v1.copy( point ).clamp( this.min, this.max );
		return clampedPoint.sub( point ).length();
	}

	public Sphere getBoundingSphere()
	{
		return getBoundingSphere(new Sphere());
	}

	public Sphere getBoundingSphere( Sphere optionalTarget )
	{
		optionalTarget.setCenter( this.center() );
		optionalTarget.setRadius( this.size( _v1 ).length() * 0.5 );

		return optionalTarget;
	}

	public Box3 intersect( Box3 box )
	{
		this.min.max( box.min );
		this.max.min( box.max );

		return this;
	}

	public Box3 union( Box3 box )
	{
		this.min.min( box.min );
		this.max.max( box.max );

		return this;
	}

	public Box3 apply( Matrix4 matrix )
	{
		Vector3[] points = {
				new Vector3(),
				new Vector3(),
				new Vector3(),
				new Vector3(),
				new Vector3(),
				new Vector3(),
				new Vector3(),
				new Vector3()
		};

		// NOTE: I am using a binary pattern to specify all 2^3 combinations below
		points[ 0 ].set( this.min.x, this.min.y, this.min.z ).apply( matrix ); // 000
		points[ 1 ].set( this.min.x, this.min.y, this.max.z ).apply( matrix ); // 001
		points[ 2 ].set( this.min.x, this.max.y, this.min.z ).apply( matrix ); // 010
		points[ 3 ].set( this.min.x, this.max.y, this.max.z ).apply( matrix ); // 011
		points[ 4 ].set( this.max.x, this.min.y, this.min.z ).apply( matrix ); // 100
		points[ 5 ].set( this.max.x, this.min.y, this.max.z ).apply( matrix ); // 101
		points[ 6 ].set( this.max.x, this.max.y, this.min.z ).apply( matrix ); // 110
		points[ 7 ].set( this.max.x, this.max.y, this.max.z ).apply( matrix ); // 111

		this.makeEmpty();
		this.setFromPoints( points );

		return this;
	}

	public Box3 translate( Vector3 offset )
	{
		this.min.add( offset );
		this.max.add( offset );

		return this;
	}

	public boolean equals( Box3 box )
	{
		return box.min.equals( this.min ) && box.max.equals( this.max );
	}

	public Box3 clone()
	{
		return new Box3().copy( this );
	}

	public String toString()
	{
		return "{min:" + this.min.toString() + ", max:" + this.max.toString() + "}";
	}
}
