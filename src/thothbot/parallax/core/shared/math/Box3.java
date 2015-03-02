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

import java.util.List;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.shared.core.AbstractGeometry;
import thothbot.parallax.core.shared.core.BufferGeometry;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryObject;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.core.Object3D.Traverse;

public class Box3 
{
	private Vector3 min;
	private Vector3 max;
	
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
	
	public Box3 setFromObject(Object3D object) 
	{

		// Computes the world-axis-aligned bounding box of an object (including its children),
		// accounting for both the object's, and childrens', world transforms

		object.updateMatrixWorld( true );

		this.makeEmpty();
		
		object.traverse(new Traverse() {
			
			@Override
			public void callback(Object3D node) {
				
				AbstractGeometry geometry = ((GeometryObject)node).getGeometry();
				
				Vector3 v1 = new Vector3();


				if ( geometry != null ) {

					if ( geometry instanceof Geometry ) {

						List<Vector3> vertices = ((Geometry)geometry).getVertices();

						for ( int i = 0, il = vertices.size(); i < il; i ++ ) {

							v1.copy( vertices.get( i ) );

							v1.apply( node.getMatrixWorld() );

							expandByPoint( v1 );

						}

					} else if ( geometry instanceof BufferGeometry && ((BufferGeometry)geometry).getAttribute("position") != null ) {

						Float32Array positions = (Float32Array)((BufferGeometry)geometry).getAttribute("position").getArray();

						for ( int i = 0, il = positions.getLength(); i < il; i += 3 ) {

							v1.set( positions.get( i ), positions.get( i + 1 ), positions.get( i + 2 ) );

							v1.apply( node.getMatrixWorld() );

							expandByPoint( v1 );

						}
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

	public boolean isContainsPoint( Vector3 point ) 
	{
		if ( point.x < this.min.x || point.x > this.max.x ||
		     point.y < this.min.y || point.y > this.max.y ||
		     point.z < this.min.z || point.z > this.max.z ) {

			return false;

		}

		return true;
	}

	public boolean isContainsBox( Box3 box ) 
	{
		if ( ( this.min.x <= box.min.x ) && ( box.max.x <= this.max.x ) &&
			 ( this.min.y <= box.min.y ) && ( box.max.y <= this.max.y ) &&
			 ( this.min.z <= box.min.z ) && ( box.max.z <= this.max.z ) ) {

			return true;

		}

		return false;
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

	public boolean isIntersectionBox( Box3 box ) 
	{
		// using 6 splitting planes to rule out intersections.

		if ( box.max.x < this.min.x || box.min.x > this.max.x ||
		     box.max.y < this.min.y || box.min.y > this.max.y ||
		     box.max.z < this.min.z || box.min.z > this.max.z ) {

			return false;

		}

		return true;
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
		points[ 7 ].set( this.max.x, this.max.y, this.max.z ).apply( matrix );  // 111

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
