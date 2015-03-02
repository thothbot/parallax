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

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.shared.cameras.Camera;


/**
 * This class is realization of (X, Y, Z) vector. 
 * Where:
 * X - x coordinate of the vector.
 * Y - y coordinate of the vector.
 * Z - z coordinate of the vector.
 * 
 * @author thothbot
 */
public class Vector3 extends Vector2
{
	/**
	 * The Z-coordinate
	 */
	protected double z;
	
	// Temporary variables
	static Quaternion _quaternion = new Quaternion();
	static Matrix4 _matrix = new Matrix4();
	static Vector3 _min = new Vector3();
	static Vector3 _max = new Vector3();
	static Vector3 _v1 = new Vector3();
	
	/**
	 * This default constructor will initialize vector (0, 0, 0); 
	 */
	public Vector3() 
	{
		this(0, 0, 0);
	}

	/**
	 * This constructor will initialize vector (X, Y, Z) from the specified 
	 * X, Y, Z coordinates.
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param z the Z coordinate
	 */
	public Vector3(double x, double y, double z) 
	{
		super(x, y);
		this.z = z;
	}

	/**
	 * get Z coordinate from the vector
	 * 
	 * @return a Z coordinate
	 */
	public double getZ()
	{
		return this.z;
	}
	
	/**
	 * This method will add specified value to Z coordinate of the vector.
	 * In another words: z += value.
	 * 
	 * @param z the Y coordinate
	 */
	public void addZ(double z)
	{
		this.z += z;
	}
	
	/**
	 * This method sets Z coordinate of the vector.
	 * 
	 * @param z the Z coordinate
	 */
	public void setZ(double z)
	{
		this.z = z;
	}
	
	/**
	 * Set value of the vector to the specified (X, Y, Z) coordinates.
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param z the Z coordinate
	 */
	public Vector3 set(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	/**
	 * Set value of the vector to the specified (A, A, A) coordinates.
	 * 
	 * @param a the X, Y and Z coordinate
	 */
	public Vector3 set(double a)
	{
		this.x = a;
		this.y = a;
		this.z = a;
		return this;
	}
	
	public void setComponent ( int index, double value ) {

		switch ( index ) {

			case 0: this.x = value; break;
			case 1: this.y = value; break;
			case 2: this.z = value; break;
			default: throw new Error( "index is out of range: " + index );

		}

	}

	public double getComponent( int index ) {

		switch ( index ) {

			case 0: return this.x;
			case 1: return this.y;
			case 2: return this.z;
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
	public Vector3 copy(Vector3 v)
	{
		this.set(v.getX(), v.getY(), v.getZ());

		return this;
	}
	
	public Vector3 add(Vector3 v)
	{
		return this.add(this, v);
	}
	
	public Vector3 add(Vector3 v1, Vector3 v2)
	{
		this.x = v1.x + v2.x;
		this.y = v1.y + v2.y;
		this.z = v1.z + v2.z;
		
		return this;
	}

	@Override
	public Vector3 add(double s)
	{
		this.addX(s);
		this.addY(s);
		this.addZ(s);

		return this;
	}
	
	public Vector3 sub(Vector3 v)
	{
		return this.sub(this, v);
	}
	
	public Vector3 sub(Vector3 v1, Vector3 v2)
	{
		this.x = v1.x - v2.x;
		this.y = v1.y - v2.y;
		this.z = v1.z - v2.z;

		return this;
	}
	
	public Vector3 multiply(Vector3 v)
	{
		return this.multiply(this, v);
	}
	
	public Vector3 multiply(Vector3 v1, Vector3 v2)
	{
		this.x = v1.x * v2.x;
		this.y = v1.y * v2.y;
		this.z = v1.z * v2.z;
		
		return this;
	}

	public Vector3 multiply(double s)
	{
		this.x *= s;
		this.y *= s;
		this.z *= s;
		return this;
	}
	
	public Vector3 applyEuler( Euler euler) 
	{		
		this.apply( _quaternion.setFromEuler( euler ) );

		return this;
	}
	
	public Vector3 applyAxisAngle(Vector3 axis, double angle) 
	{
		this.apply( _quaternion.setFromAxisAngle( axis, angle ) );

		return this;
	}
	
	public Vector3 apply( Matrix3 m ) 
	{
		double x = this.x;
		double y = this.y;
		double z = this.z;

		Float32Array e = m.getArray();

		this.x = e.get(0) * x + e.get(3) * y + e.get(6) * z;
		this.y = e.get(1) * x + e.get(4) * y + e.get(7) * z;
		this.z = e.get(2) * x + e.get(5) * y + e.get(8) * z;

		return this;
	}

	/**
	 * 
	 * @param m Matrix4 affine matrix
	 * @return
	 */
	public Vector3 apply( Matrix4 m ) 
	{
		double x = this.x, y = this.y, z = this.z;

		Float32Array e = m.getArray();

		this.x = e.get(0) * x + e.get(4) * y + e.get(8)  * z + e.get(12);
		this.y = e.get(1) * x + e.get(5) * y + e.get(9)  * z + e.get(13);
		this.z = e.get(2) * x + e.get(6) * y + e.get(10) * z + e.get(14);

		return this;
	}

	/**
	 * 
	 * @param m the Matrix4 projection matrix
	 * @return
	 */
	public Vector3 applyProjection( Matrix4 m ) 
	{
		double x = this.x, y = this.y, z = this.z;

		Float32Array e = m.getArray();
		double d = 1.0 / ( e.get(3) * x + e.get(7) * y + e.get(11) * z + e.get(15) ); // perspective divide

		this.x = ( e.get(0) * x + e.get(4) * y + e.get(8)  * z + e.get(12) ) * d;
		this.y = ( e.get(1) * x + e.get(5) * y + e.get(9)  * z + e.get(13) ) * d;
		this.z = ( e.get(2) * x + e.get(6) * y + e.get(10) * z + e.get(14) ) * d;

		return this;
	}
	
	public Vector3 apply( Quaternion q ) 
	{
		double x = this.x;
		double y = this.y;
		double z = this.z;

		double qx = q.x;
		double qy = q.y;
		double qz = q.z;
		double qw = q.w;

		// calculate quat * vector

		double ix =  qw * x + qy * z - qz * y;
		double iy =  qw * y + qz * x - qx * z;
		double iz =  qw * z + qx * y - qy * x;
		double iw = - qx * x - qy * y - qz * z;

		// calculate result * inverse quat

		this.x = ix * qw + iw * - qx + iy * - qz - iz * - qy;
		this.y = iy * qw + iw * - qy + iz * - qx - ix * - qz;
		this.z = iz * qw + iw * - qz + ix * - qy - iy * - qx;

		return this;

	}

	public Vector3 project(Camera camera) 
	{
		_matrix.multiply( camera.getProjectionMatrix(), _matrix.getInverse( camera.getMatrixWorld() ) );
		return this.applyProjection( _matrix );

	}
	
	public Vector3 unproject(Camera camera) 
	{
		_matrix.multiply( camera.getMatrixWorld(), _matrix.getInverse( camera.getProjectionMatrix() ) );
		return this.applyProjection( _matrix );
	}
	
	/**
	 * 
	 * @param m Matrix4 affine matrix vector interpreted as a direction
	 * @return
	 */
	public Vector3 transformDirection( Matrix4 m ) 
	{
		double x = this.x, y = this.y, z = this.z;

		Float32Array e = m.getArray();

		this.x = e.get(0) * x + e.get(4) * y + e.get(8)  * z;
		this.y = e.get(1) * x + e.get(5) * y + e.get(9)  * z;
		this.z = e.get(2) * x + e.get(6) * y + e.get(10) * z;

		this.normalize();

		return this;
	}
	
	public Vector3 divide(Vector3 v)
	{
		return this.divide(this, v);
	}
	
	public Vector3 divide(Vector3 v1, Vector3 v2)
	{
		this.x = v1.x / v2.x;
		this.y = v1.y / v2.y;
		this.z = v1.z / v2.z;

		return this;
	}

	@Override
	public Vector3 divide(double scalar)
	{
		if ( scalar != 0 ) {

			double invScalar = 1.0 / scalar;

			this.x *= invScalar;
			this.y *= invScalar;
			this.z *= invScalar;

		} else {

			this.x = 0;
			this.y = 0;
			this.z = 0;

		}

		return this;

	}
	
	public Vector3 min( Vector3 v ) 
	{
		if ( this.x > v.x ) 
		{
			this.x = v.x;
		}

		if ( this.y > v.y ) 
		{
			this.y = v.y;
		}

		if ( this.z > v.z ) 
		{
			this.z = v.z;
		}

		return this;
	}

	public Vector3 max( Vector3 v ) 
	{
		if ( this.x < v.x ) 
		{
			this.x = v.x;
		}

		if ( this.y < v.y ) 
		{
			this.y = v.y;
		}

		if ( this.z < v.z ) 
		{
			this.z = v.z;
		}

		return this;
	}
	
	/**
	 * This function assumes min &#60; max, if this assumption isn't true it will not operate correctly
	 */
	public Vector3 clamp( Vector3 min, Vector3 max ) 
	{
		// This function assumes min < max, if this assumption isn't true it will not operate correctly

		if ( this.x < min.x ) {

			this.x = min.x;

		} else if ( this.x > max.x ) {

			this.x = max.x;

		}

		if ( this.y < min.y ) {

			this.y = min.y;

		} else if ( this.y > max.y ) {

			this.y = max.y;

		}

		if ( this.z < min.z ) {

			this.z = min.z;

		} else if ( this.z > max.z ) {

			this.z = max.z;

		}

		return this;
	}

	public Vector3 clamp(double minVal, double maxVal) 
	{
		_min.set( minVal, minVal, minVal );
		_max.set( maxVal, maxVal, maxVal );

		return this.clamp( _min, _max );
	}

	public Vector3 floor() 
	{

		this.x = Math.floor( this.x );
		this.y = Math.floor( this.y );
		this.z = Math.floor( this.z );

		return this;

	}

	public Vector3 ceil() 
	{

		this.x = Math.ceil( this.x );
		this.y = Math.ceil( this.y );
		this.z = Math.ceil( this.z );

		return this;

	}

	public Vector3 round() 
	{

		this.x = Math.round( this.x );
		this.y = Math.round( this.y );
		this.z = Math.round( this.z );

		return this;

	}

	public Vector3 roundToZero() 
	{

		this.x = ( this.x < 0 ) ? Math.ceil( this.x ) : Math.floor( this.x );
		this.y = ( this.y < 0 ) ? Math.ceil( this.y ) : Math.floor( this.y );
		this.z = ( this.z < 0 ) ? Math.ceil( this.z ) : Math.floor( this.z );

		return this;

	}

	@Override
	public Vector3 negate()
	{
		this.x = - this.x;
		this.y = - this.y;
		this.z = - this.z;

		return this;
	}
	
	/**
	 * Computes the dot product of this vector and vector v1.
	 * 
	 * @param v1
	 *            the other vector
	 * @return the dot product of this vector and v1
	 */
	public double dot(Vector3 v1)
	{
		return (this.x * v1.x + this.y * v1.y + this.z * v1.z);
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
		return Math.abs(this.x) + Math.abs(this.y) + Math.abs(this.z);
	}

	/**
	 * Normalizes this vector in place.
	 */
	@Override
	public Vector3 normalize()
	{
		return this.divide( this.length() );
	}

	public Vector3 setLength(double l)
	{
		double oldLength = this.length();

		if ( oldLength != 0 && l != oldLength  ) {

			this.multiply( l / oldLength );
		}

		return this;
	}

	public Vector3 lerp(Vector3 v1, double alpha)
	{
		this.x += (v1.x - this.x) * alpha;
		this.y += (v1.y - this.y) * alpha;
		this.z += (v1.z - this.z) * alpha;
		
		return this;
	}

	/**
	 * Sets this vector to be the vector cross product of vectors v1 and v2.
	 * 
	 * @param a
	 *            the first vector
	 * @param b
	 *            the second vector
	 */
	public Vector3 cross(Vector3 a, Vector3 b)
	{
		double ax = a.x, ay = a.y, az = a.z;
		double bx = b.x, by = b.y, bz = b.z;

		this.x = ay * bz - az * by;
		this.y = az * bx - ax * bz;
		this.z = ax * by - ay * bx;

		return this;
	}
	
	public Vector3 cross(Vector3 v)
	{
		return cross(this, v);
	}
	
	public Vector3 projectOnVector(Vector3 vector) 
	{
		_v1.copy( vector ).normalize();

		double dot = this.dot( _v1 );

		return this.copy( _v1 ).multiply( dot );
	}

	public Vector3 projectOnPlane(Vector3 planeNormal) 
	{
		_v1.copy( this ).projectOnVector( planeNormal );

		return this.sub( _v1 );
	}
	

	/**
	 * reflect incident vector off plane orthogonal to normal
	 * normal is assumed to have unit length
	 * @param normal
	 * @return
	 */
	public Vector3 reflect(Vector3 normal) 
	{
		return this.sub( _v1.copy( normal ).multiply( 2 * this.dot( normal ) ) );
	}
	
	public double angleTo( Vector3 v ) 
	{
		double theta = this.dot( v ) / ( this.length() * v.length() );

		// clamp, to handle numerical problems

		return Math.acos( Mathematics.clamp( theta, - 1, 1 ) );
	}

	public double distanceTo(Vector3 v1)
	{
		return Math.sqrt(distanceToSquared(v1));
	}

	public double distanceToSquared(Vector3 v1)
	{
		double dx = this.x - v1.x;
		double dy = this.y - v1.y;
		double dz = this.z - v1.z;
		return (dx * dx + dy * dy + dz * dz);
	}
	
	public Vector3 setFromMatrixPosition( Matrix4 m )
	{

		this.x = m.getArray().get( 12 );
		this.y = m.getArray().get( 13 );
		this.z = m.getArray().get( 14 );

		return this;
	}
	
	public Vector3 setFromMatrixScale( Matrix4 m ) 
	{

		double sx = this.set( m.getArray().get( 0 ), m.getArray().get( 1 ), m.getArray().get(  2 ) ).length();
		double sy = this.set( m.getArray().get( 4 ), m.getArray().get( 5 ), m.getArray().get(  6 ) ).length();
		double sz = this.set( m.getArray().get( 8 ), m.getArray().get( 9 ), m.getArray().get( 10 ) ).length();

		this.x = sx;
		this.y = sy;
		this.z = sz;

		return this;
	}
	
	public Vector3 setFromMatrixColumn( int index, Matrix4 matrix ) 
	{

		int offset = index * 4;

		Float32Array me = matrix.getArray();

		this.x = me.get( offset );
		this.y = me.get( offset + 1 );
		this.z = me.get( offset + 2 );

		return this;

	}
	
	/**
	 * Returns true if all of the data members of v1 are equal to the
	 * corresponding data members in this Vector3.
	 * 
	 * @param v1
	 *            the vector with which the comparison is made
	 * @return true or false
	 */
	public boolean equals(Vector3 v1)
	{
		return (this.x == v1.x && this.y == v1.y && this.z == v1.z);
	}
	
	public Vector3 fromArray ( Float32Array array )
	{
		return fromArray(array, 0);
	}
	
	public Vector3 fromArray ( Float32Array array, int offset ) 
	{

		this.x = array.get( offset );
		this.y = array.get( offset + 1 );
		this.z = array.get( offset + 2 );

		return this;

	}
	
	public Float32Array toArray() 
	{
		return toArray(Float32Array.create(3), 0);
	}

	public Float32Array toArray( Float32Array array, int offset ) 
	{

		array.set( offset , this.x);
		array.set( offset + 1 , this.y);
		array.set( offset + 2 , this.z);

		return array;
	}
	
	public Vector3 clone()
	{
		return new Vector3(this.getX(), this.getY(), this.getZ());
	}
	
	public String toString() 
	{
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
}
