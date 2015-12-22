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

import org.parallax3d.parallax.system.BufferUtils;
import org.parallax3d.parallax.system.ThreeJsObject;

import java.nio.FloatBuffer;

/**
 * This class is realization of (X, Y) vector. 
 * Where:
 * X - x coordinate of the vector.
 * Y - y coordinate of the vector.
 * 
 * @author thothbot
 */
@ThreeJsObject("THREE.Vector2")
public class Vector2
{
	/**
	 * The X-coordinate
	 */
	protected float x;
	
	/**
	 * The Y-coordinate
	 */
	protected float y;
	
	// Temporary variables
	static Vector2 _min = new Vector2();
	static Vector2 _max = new Vector2();

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
	public Vector2(float x, float y) 
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * get X coordinate from the vector
	 * 
	 * @return a X coordinate
	 */
	public float getX()
	{
		return x;
	}
	
	/**
	 * get Y coordinate from the vector
	 * 
	 * @return a Y coordinate
	 */
	public float getY()
	{
		return y;
	}
		
	/**
	 * This method will add specified value to X coordinate of the vector.
	 * In another words: x += value.
	 * 
	 * @param x the X coordinate
	 */
	public void addX(float x)
	{
		this.x += x;
	}
	
	/**
	 * This method will add specified value to Y coordinate of the vector.
	 * In another words: y += value.
	 * 
	 * @param y the Y coordinate
	 */
	public void addY(float y)
	{
		this.y += y;
	}

	/**
	 * This method sets X coordinate of the vector.
	 * 
	 * @param x the X coordinate
	 */
	public void setX(float x)
	{
		this.x = x;
	}

	/**
	 * This method sets Y coordinate of the vector.
	 * 
	 * @param y the Y coordinate
	 */
	public void setY(float y)
	{
		this.y = y;
	}

	/**
	 * Set value of the vector to the specified (X, Y, Z) coordinates.
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 */
	public Vector2 set(float x, float y)
	{
		this.x = x;
		this.y = y;
		
		return this;
	}
	
	public void setComponent( int index, float value ) {

		switch ( index ) {

			case 0: this.x = value; break;
			case 1: this.y = value; break;
			default: throw new Error( "index is out of range: " + index );

		}

	}

	public float getComponent ( int index ) {

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
	
	public Vector2 add(Vector2 v1, Vector2 v2)
	{
		this.x = v1.x + v2.x;
		this.y = v1.y + v2.y;

		return this;
	}
	
	public Vector2 add(float s)
	{
		this.addX(s);
		this.addY(s);
		
		return this;
	}

	public Vector2 sub(Vector2 v)
	{
		return this.sub(this, v);
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

	public Vector2 multiply(float s)
	{
		this.x *= s;
		this.y *= s;

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
	
	public Vector2 divide(float s)
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
		if ( this.x > v.x ) 
		{
			this.x = v.x;
		}

		if ( this.y > v.y ) 
		{
			this.y = v.y;
		}

		return this;
	}

	public Vector2 max( Vector2 v ) 
	{
		if ( this.x < v.x ) 
		{
			this.x = v.x;
		}

		if ( this.y < v.y ) 
		{
			this.y = v.y;
		}

		return this;
	}
	
	/**
	 * This function assumes min &#60; max, if this assumption isn't true it will not operate correctly
	 * 
	 */
	public Vector2 clamp( Vector2 min, Vector2 max ) 
	{
		if ( this.x < min.x ) 
		{
			this.x = min.x;
		} 
		else if ( this.x > max.x ) 
		{
			this.x = max.x;
		}

		if ( this.y < min.y ) 
		{
			this.y = min.y;
		} 
		else if ( this.y > max.y ) 
		{
			this.y = max.y;
		}

		return this;
	}

	public Vector2 clamp(float minVal, float maxVal) 
	{
		_min.set( minVal, minVal );
		_max.set( maxVal, maxVal );

		return this.clamp( _min, _max );
	} 
	
	public Vector2 floor() {

		this.x = (float)Math.floor( this.x );
		this.y = (float)Math.floor( this.y );

		return this;

	}
	
	public Vector2 ceil() {

		this.x = (float)Math.ceil( this.x );
		this.y = (float)Math.ceil( this.y );

		return this;

	}
	
	public Vector2 round() {

		this.x = Math.round( this.x );
		this.y = Math.round( this.y );

		return this;

	}

	public Vector2 roundToZero() {

		this.x = ( this.x < 0 ) ? (float)Math.ceil( this.x ) : (float)Math.floor( this.x );
		this.y = ( this.y < 0 ) ? (float)Math.ceil( this.y ) : (float)Math.floor( this.y );

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
	public float dot(Vector2 v)
	{
		return (this.x * v.x + this.y * v.y);
	}

	/**
	 * Returns the squared length of this vector.
	 * 
	 * @return the squared length of this vector
	 */
	public float lengthSq()
	{
		return dot(this);
	}

	/**
	 * Returns the length of this vector.
	 * 
	 * @return the length of this vector
	 */
	public float length()
	{
		return (float)Math.sqrt(lengthSq());
	}

	/**
	 * Normalizes this vector in place.
	 */
	public Vector2 normalize()
	{
		this.divide(length());
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see thothbot.parallax.core.shared.core.Vector#distanceToSquared(thothbot.parallax.core.shared.core.Vector)
	 */
	public float distanceToSquared(Vector2 v)
	{
		float dx = this.x - v.x;
		float dy = this.y - v.y;
		return (dx * dx + dy * dy);
	}

	/*
	 * (non-Javadoc)
	 * @see thothbot.parallax.core.shared.core.Vector#distanceTo(thothbot.parallax.core.shared.core.Vector)
	 */
	public float distanceTo(Vector2 v1)
	{
		return (float)Math.sqrt(distanceToSquared(v1));
	}

	public Vector2 setLength(float l)
	{
		float oldLength = this.length();

		if ( oldLength != 0 && l != oldLength ) 
		{
			this.multiply( l / oldLength );
		}

		return this;
	}

	public Vector2 lerp(Vector2 v1, float alpha)
	{
		this.x += (v1.x - this.x) * alpha;
		this.y += (v1.y - this.y) * alpha;
		
		return this;
	}

	public boolean isZero()
	{
		return (this.lengthSq() < 0.0001 /* almostZero */);
	}
	
	public Vector2 fromArray( FloatBuffer array) {
		return fromArray(array, 0);
	}

	public Vector2 fromArray( FloatBuffer array, int offset ) {

		this.x = array.get( offset );
		this.y = array.get( offset + 1 );

		return this;

	}

	public FloatBuffer toArray()
	{
		return toArray(BufferUtils.newFloatBuffer(2), 0);
	}

	public FloatBuffer toArray( FloatBuffer array, int offset )
	{
		array.put( offset , this.x);
		array.put( offset + 1 , this.y);

		return array;
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