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

import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

/**
 * This class is realization of (X, Y) vector. 
 * Where:
 * X - x coordinate of the vector.
 * Y - y coordinate of the vector.
 * 
 * @author thothbot
 */
@ThreejsObject("THREE.Vector2")
public class Vector2
{
	/**
	 * The X-coordinate
	 */
	protected double x;

	/**
	 * The Y-coordinate
	 */
	protected double y;

	/**
	 * This default constructor will initialize vector (0, 0);
	 */
	public Vector2()
	{
		this(0, 0);
	}

	/**
	 * This constructor will initialize vector (X, Y) from the specified
	 * X, Y coordinates.
	 *
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 */
	public Vector2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * get X coordinate from the vector
	 *
	 * @return a X coordinate
	 */
	public double getX()
	{
		return x;
	}

	/**
	 * get Y coordinate from the vector
	 *
	 * @return a Y coordinate
	 */
	public double getY()
	{
		return y;
	}

	/**
	 * This method will add specified value to X coordinate of the vector.
	 * In another words: x += value.
	 *
	 * @param x the X coordinate
	 */
	public Vector2 addX(double x)
	{
		this.x += x;
		return this;
	}

	/**
	 * This method will add specified value to Y coordinate of the vector.
	 * In another words: y += value.
	 *
	 * @param y the Y coordinate
	 */
	public Vector2 addY(double y)
	{
		this.y += y;
		return this;
	}

	/**
	 * This method sets X coordinate of the vector.
	 *
	 * @param x the X coordinate
	 */
	public Vector2 setX(double x)
	{
		this.x = x;
		return this;
	}

	/**
	 * This method sets Y coordinate of the vector.
	 *
	 * @param y the Y coordinate
	 */
	public Vector2 setY(double y)
	{
		this.y = y;
		return this;
	}

	/**
	 * Set value of the vector to the specified (X, Y, Z) coordinates.
	 *
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 */
	public Vector2 set(double x, double y)
	{
		this.x = x;
		this.y = y;

		return this;
	}

	public Vector2 set(double scalar)
	{
		this.x = scalar;
		this.y = scalar;

		return this;
	}

	public Vector2 setComponent( int index, double value ) {

		switch ( index ) {

			case 0: this.x = value; break;
			case 1: this.y = value; break;
			default: throw new Error( "index is out of range: " + index );

		}

		return this;
	}

	public double getComponent ( int index ) {

		switch ( index ) {

			case 0: return this.x;
			case 1: return this.y;
			default: throw new Error( "index is out of range: " + index );

		}

	}

	/**
	 * Set value of the vector from another vector.
	 *
	 * @param v the other vector
	 *
	 * @return the current vector
	 */
	public Vector2 copy(Vector2 v)
	{
		this.set(v.x, v.y);

		return this;
	}

	public Vector2 add(Vector2 v)
	{
		return this.add(this, v);
	}

	@Deprecated
	public Vector2 addVectors(Vector2 v1, Vector2 v2)
	{
		return this.add(v1, v2);
	}

	public Vector2 add(Vector2 v1, Vector2 v2)
	{
		this.x = v1.x + v2.x;
		this.y = v1.y + v2.y;

		return this;
	}

	public Vector2 add(Vector2 v1, double s)
	{
		this.x += v1.x * s;
		this.y += v1.y * s;

		return this;
	}

	public Vector2 add(double s)
	{
		this.addX(s);
		this.addY(s);

		return this;
	}

	public Vector2 sub(Vector2 v)
	{
		return this.sub(this, v);
	}

	@Deprecated
	public Vector2 subVectors(Vector2 v1, Vector2 v2)
	{
		return this.sub(v1, v2);
	}

	public Vector2 sub(Vector2 v1, Vector2 v2)
	{
		this.x = v1.x - v2.x;
		this.y = v1.y - v2.y;

		return this;
	}

	public Vector2 multiply(Vector2 v)
	{
		return this.multiply(this, v);
	}

	public Vector2 multiply(Vector2 v1, Vector2 v2)
	{
		this.x = v1.x * v2.x;
		this.y = v1.y * v2.y;

		return this;
	}

	@Deprecated
	public Vector2 multiplyScalar(double s)
	{
		return multiply( s );
	}

	public Vector2 multiply(double scalar)
	{
		if ( !Double.isInfinite( scalar ) ) {

			this.x *= scalar;
			this.y *= scalar;

		} else {

			this.x = 0;
			this.y = 0;

		}


		return this;
	}

	public Vector2 divide(Vector2 v)
	{
		return this.divide(this, v);
	}

	public Vector2 divide(Vector2 v1, Vector2 v2)
	{
		this.x = v1.x / v2.x;
		this.y = v1.y / v2.y;

		return this;
	}

	@Deprecated
	public Vector2 divideScalar(double s)
	{
		return divide(s);
	}

	public Vector2 divide(double s)
	{
		if (s != 0)
		{
			this.x /= s;
			this.y /= s;
		}
		else
		{
			this.set(0, 0);
		}

		return this;
	}

	public Vector2 min( Vector2 v )
	{
		this.x = Math.min( this.x, v.x );
		this.y = Math.min( this.y, v.y );

		return this;
	}

	public Vector2 max( Vector2 v )
	{
		this.x = Math.max( this.x, v.x );
		this.y = Math.max( this.y, v.y );

		return this;

	}

	/**
	 * This function assumes min &#60; max, if this assumption isn't true it will not operate correctly
	 *
	 */
	public Vector2 clamp( Vector2 min, Vector2 max )
	{
		this.x = Math.max( min.x, Math.min( max.x, this.x ) );
		this.y = Math.max( min.y, Math.min( max.y, this.y ) );

		return this;
	}

	@Deprecated
	public Vector2 clampScalar(double minVal, double maxVal)
	{
		return clamp(minVal, maxVal);
	}

	// Temporary variables
	private static final Vector2 _min = new Vector2();
	private static final Vector2 _max = new Vector2();
	public Vector2 clamp(double minVal, double maxVal)
	{
		_min.set( minVal, minVal );
		_max.set( maxVal, maxVal );

		return this.clamp( _min, _max );
	}

	public Vector2 clampLength( double min, double max )
	{
		double length = this.length();

		this.multiply( Math.max( min, Math.min( max, length ) ) / length );

		return this;
	}

	public Vector2 floor() {

		this.x = Math.floor( this.x );
		this.y = Math.floor( this.y );

		return this;

	}

	public Vector2 ceil() {

		this.x = Math.ceil( this.x );
		this.y = Math.ceil( this.y );

		return this;

	}

	public Vector2 round() {

		this.x = Math.round( this.x );
		this.y = Math.round( this.y );

		return this;

	}

	public Vector2 roundToZero() {

		this.x = ( this.x < 0 ) ? Math.ceil( this.x ) : Math.floor( this.x );
		this.y = ( this.y < 0 ) ? Math.ceil( this.y ) : Math.floor( this.y );

		return this;

	}

	/**
	 * Negates the value of this vector in place.
	 */
	public Vector2 negate()
	{
		this.x = - this.x;
		this.y = - this.y;

		return this;
	}

	/**
	 * Computes the dot product of the this vector and vector v1.
	 *
	 * @param v
	 *            the other vector
	 */
	public double dot(Vector2 v)
	{
		return (this.x * v.x + this.y * v.y);
	}

	/**
	 * Returns the squared length of this vector.
	 *
	 * @return the squared length of this vector
	 */
	public double lengthSq()
	{
		return dot(this);
	}

	/**
	 * Returns the length of this vector.
	 *
	 * @return the length of this vector
	 */
	public double length()
	{
		return Math.sqrt(lengthSq());
	}

	public double lengthManhattan()
	{
		return Math.abs( this.x ) + Math.abs( this.y );
	}

	/**
	 * Normalizes this vector in place.
	 */
	public Vector2 normalize()
	{
		this.divide(length());
		return this;
	}

	/**
	 * computes the angle in radians with respect to the positive x-axis
	 * @return
     */
	public double angle()
	{
		double angle = Math.atan2( this.y, this.x );

		if ( angle < 0 ) angle += 2 * Math.PI;

		return angle;

	}

	/*
	 * (non-Javadoc)
	 * @see thothbot.parallax.core.shared.core.Vector#distanceToSquared(thothbot.parallax.core.shared.core.Vector)
	 */
	public double distanceToSquared(Vector2 v)
	{
		double dx = this.x - v.x;
		double dy = this.y - v.y;
		return (dx * dx + dy * dy);
	}

	/*
	 * (non-Javadoc)
	 * @see thothbot.parallax.core.shared.core.Vector#distanceTo(thothbot.parallax.core.shared.core.Vector)
	 */
	public double distanceTo(Vector2 v1)
	{
		return Math.sqrt(distanceToSquared(v1));
	}

	public Vector2 setLength(double length)
	{
		return this.multiply( length / this.length() );
	}

	public Vector2 lerp(Vector2 v1, double alpha)
	{
		this.x += (v1.x - this.x) * alpha;
		this.y += (v1.y - this.y) * alpha;

		return this;
	}

	public Vector2 lerpVectors(Vector2 v1, Vector2 v2, double alpha)
	{
		this.sub( v2, v1 ).multiply( alpha ).add( v1 );

		return this;
	}

	public boolean isZero()
	{
		return (this.lengthSq() < 0.0001 /* almostZero */);
	}

	public Vector2 fromArray( Float32Array array) {
		return fromArray(array, 0);
	}

	public Vector2 fromArray( Float32Array array, int offset ) {

		this.x = array.get( offset );
		this.y = array.get( offset + 1 );

		return this;

	}

	public Float32Array toArray()
	{
		return toArray(Float32Array.create(2), 0);
	}

	public Float32Array toArray( Float32Array array, int offset )
	{

		array.set(offset, this.x);
		array.set(offset + 1, this.y);

		return array;
	}

	public Vector2 fromAttribute(BufferAttribute attribute, int index)
	{
		return fromAttribute(attribute, index, 0);
	}

	public Vector2 fromAttribute(BufferAttribute attribute, int index, int offset)
	{

		index = index * attribute.getItemSize() + offset;

		this.x = ((Float32Array)attribute.getArray()).get( index );
		this.y = ((Float32Array)attribute.getArray()).get( index + 1 );

		return this;
	}

	public Vector2 rotateAround( Vector2 center, double angle )
	{

		double c = Math.cos( angle ), s = Math.sin( angle );

		double x = this.x - center.x;
		double y = this.y - center.y;

		this.x = x * c - y * s + center.x;
		this.y = x * s + y * c + center.y;

		return this;

	}

	public Vector2 clone()
	{
		return new Vector2(this.x, this.y);
	}

	/**
	 * Returns true if all of the data members of Tuple2f t1 are equal to the
	 * corresponding data members in this Tuple2f.
	 *
	 * @param v		the vector with which the comparison is made
	 *
	 * @return true or false
	 */
	public boolean equals(Vector2 v)
	{
		return ( ( v.x == this.x ) && ( v.y == this.y ) );
	}

	@Override
	public String toString()
	{
		return "(" + this.x + ", " + this.y + ")";
	}
}
