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

public class Box2 
{
	private Vector2 min;
	private Vector2 max;
	
	private static Vector2 __v1 = new Vector2();
	
	public Box2()
	{
		this(new Vector2(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), 
				new Vector2(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
	}
	public Box2( Vector2 min, Vector2 max ) 
	{
		this.min = min;
		this.max = max;
	}

	public Vector2 getMin() {
		return min;
	}
	public Vector2 getMax() {
		return max;
	}
	
	public void setMin(Vector2 min) {
		this.min = min;
	}
	public void setMax(Vector2 max) {
		this.max = max;
	}

	public Box2 set( Vector2 min, Vector2 max ) 
	{
		this.min.copy( min );
		this.max.copy( max );

		return this;
	}

	public Box2 setFromPoints( List<Vector2>points ) 
	{
		if ( points.size() > 0 ) 
		{
			Vector2 point = points.get(0);

			this.min.copy( point );
			this.max.copy( point );

			for ( int i = 1, il = points.size(); i < il; i ++ ) 
			{
				point = points.get(i);

				if ( point.x < this.min.x ) 
				{
					this.min.x = point.x;
				} 
				else if ( point.x > this.max.x ) 
				{
					this.max.x = point.x;
				}

				if ( point.y < this.min.y ) 
				{
					this.min.y = point.y;
				}
				else if ( point.y > this.max.y ) 
				{
					this.max.y = point.y;
				}
			}
		} 
		else 
		{
			this.makeEmpty();
		}

		return this;
	}

	public Box2 setFromCenterAndSize( Vector2 center, Vector2 size ) 
	{
		Vector2 halfSize = Box2.__v1.copy( size ).multiply( 0.5 );
		this.min.copy( center ).sub( halfSize );
		this.max.copy( center ).add( halfSize );

		return this;
	}

	public Box2 copy( Box2 box ) 
	{
		this.min.copy( box.min );
		this.max.copy( box.max );

		return this;
	}

	public Box2 makeEmpty()
	{
		this.min.x = this.min.y = Double.POSITIVE_INFINITY;
		this.max.x = this.max.y = Double.NEGATIVE_INFINITY;

		return this;
	}

	public boolean isEmpty() 
	{
		// this is a more robust check for empty than ( volume <= 0 ) because volume can get positive with two negative axes
		return ( this.max.getX() < this.min.getX() ) || ( this.max.getY() < this.min.getY() );
	}

	public Vector2 center()
	{
		return center(new Vector2());
	}
	
	public Vector2 center( Vector2 optionalTarget ) 
	{
		return optionalTarget.add( this.min, this.max ).multiply( 0.5 );
	}

	public Vector2 size()
	{
		return size(new Vector2());
	}
	
	public Vector2 size( Vector2 optionalTarget ) 
	{
		return optionalTarget.sub( this.max, this.min );
	}

	public Box2 expandByPoint( Vector2 point ) 
	{
		this.min.min( point );
		this.max.max( point );

		return this;
	}

	public Box2 expandByVector( Vector2 vector ) 
	{
		this.min.sub( vector );
		this.max.add( vector );

		return this;
	}

	public Box2 expandByScalar( double scalar ) 
	{
		this.min.add( -scalar );
		this.max.add( scalar );

		return this;
	}

	public boolean isContainsPoint( Vector2 point ) 
	{
		if ( point.x < this.min.x || point.x > this.max.x ||
		     point.y < this.min.y || point.y > this.max.y ) {

			return false;

		}

		return true;
	}

	public boolean isContainsBox( Box2 box ) 
	{
		if ( ( this.min.x <= box.min.x ) && ( box.max.x <= this.max.x ) &&
		     ( this.min.y <= box.min.y ) && ( box.max.y <= this.max.y ) ) {

			return true;
		}

		return false;
	}

	public Vector2 getParameter( Vector2 point ) 
	{
		// This can potentially have a divide by zero if the box
		// has a size dimension of 0.

		return new Vector2(
			( point.x - this.min.x ) / ( this.max.x - this.min.x ),
			( point.y - this.min.y ) / ( this.max.y - this.min.y )
		);
	}

	public boolean isIntersectionBox( Box2 box ) 
	{
		// using 6 splitting planes to rule out intersections.

		if ( box.max.x < this.min.x || box.min.x > this.max.x ||
		     box.max.y < this.min.y || box.min.y > this.max.y ) 
		{
			return false;
		}

		return true;
	}

	public Vector2 clampPoint( Vector2 point)
	{
		return clampPoint(point, new Vector2());
	}
	
	public Vector2 clampPoint( Vector2 point, Vector2 optionalTarget ) 
	{
		return optionalTarget.copy( point ).clamp( this.min, this.max );
	}

	public double distanceToPoint( Vector2 point ) 
	{
		Vector2 clampedPoint = Box2.__v1.copy( point ).clamp( this.min, this.max );
		return clampedPoint.sub( point ).length();
	}

	public Box2 intersect( Box2 box ) 
	{
		this.min.max( box.min );
		this.max.min( box.max );

		return this;
	}

	public Box2 union( Box2 box ) 
	{
		this.min.min( box.min );
		this.max.max( box.max );

		return this;
	}

	public Box2 translate( Vector2 offset ) 
	{
		this.min.add( offset );
		this.max.add( offset );

		return this;
	}

	public boolean equals( Box2 box ) 
	{
		return box.min.equals( this.min ) && box.max.equals( this.max );
	}

	public Box2 clone()
	{
		return new Box2().copy( this );
	}
	
	public String toString()
	{
		return "{min:" + this.min.toString() + ", max:" + this.max.toString() + "}";
	}
}
