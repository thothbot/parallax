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

import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.system.BufferUtils;
import org.parallax3d.parallax.system.ThreeJsObject;

import java.nio.FloatBuffer;


/**
 * This class is realization of (X, Y, Z) vector. 
 * Where:
 * X - x coordinate of the vector.
 * Y - y coordinate of the vector.
 * Z - z coordinate of the vector.
 * 
 * @author thothbot
 */
@ThreeJsObject("THREE.Vector3")
public class Vector3 extends Vector2
{
	/**
	 * The Z-coordinate
	 */
	protected float z;
	
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
	public Vector3(float x, float y, float z) 
	{
		super(x, y);
		this.z = z;
	}

	/**
	 * get Z coordinate from the vector
	 * 
	 * @return a Z coordinate
	 */
	public float getZ()
	{
		return this.z;
	}
	
	/**
	 * This method will add specified value to Z coordinate of the vector.
	 * In another words: z += value.
	 * 
	 * @param z the Y coordinate
	 */
	public void addZ(float z)
	{
		this.z += z;
	}
	
	/**
	 * This method sets Z coordinate of the vector.
	 * 
	 * @param z the Z coordinate
	 */
	public void setZ(float z)
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
	public Vector3 set(float x, float y, float z)
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
	public Vector3 set(float a)
	{
		this.x = a;
		this.y = a;
		this.z = a;
		return this;
	}
	
	public void setComponent ( int index, float value ) {

		switch ( index ) {

			case 0: this.x = value; break;
			case 1: this.y = value; break;
			case 2: this.z = value; break;
			default: throw new Error( "index is out of range: " + index );

		}

	}

	public float getComponent( int index ) {

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
	public Vector3 add(float s)
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

	public Vector3 multiply(float s)
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
	
	public Vector3 applyAxisAngle(Vector3 axis, float angle) 
	{
		this.apply( _quaternion.setFromAxisAngle( axis, angle ) );

		return this;
	}
	
	public Vector3 apply( Matrix3 m ) 
	{
		float x = this.x;
		float y = this.y;
		float z = this.z;

		float[] e = m.getArray();

		this.x = e[0] * x + e[3] * y + e[6] * z;
		this.y = e[1] * x + e[4] * y + e[7] * z;
		this.z = e[2] * x + e[5] * y + e[8] * z;

		return this;
	}

	/**
	 * 
	 * @param m Matrix4 affine matrix
	 * @return
	 */
	public Vector3 apply( Matrix4 m ) 
	{
		float x = this.x, y = this.y, z = this.z;

		float[] e = m.getArray();

		this.x = e[0] * x + e[4] * y + e[8]  * z + e[12];
		this.y = e[1] * x + e[5] * y + e[9]  * z + e[13];
		this.z = e[2] * x + e[6] * y + e[10] * z + e[14];

		return this;
	}

	/**
	 * 
	 * @param m the Matrix4 projection matrix
	 * @return
	 */
	public Vector3 applyProjection( Matrix4 m ) 
	{
		float x = this.x, y = this.y, z = this.z;

		float[] e = m.getArray();
		float d = 1.0f / ( e[3] * x + e[7] * y + e[11] * z + e[15] ); // perspective divide

		this.x = ( e[0] * x + e[4] * y + e[8]  * z + e[12] ) * d;
		this.y = ( e[1] * x + e[5] * y + e[9]  * z + e[13] ) * d;
		this.z = ( e[2] * x + e[6] * y + e[10] * z + e[14] ) * d;

		return this;
	}
	
	public Vector3 apply( Quaternion q ) 
	{
		float x = this.x;
		float y = this.y;
		float z = this.z;

		float qx = q.x;
		float qy = q.y;
		float qz = q.z;
		float qw = q.w;

		// calculate quat * vector

		float ix =  qw * x + qy * z - qz * y;
		float iy =  qw * y + qz * x - qx * z;
		float iz =  qw * z + qx * y - qy * x;
		float iw = - qx * x - qy * y - qz * z;

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
		float x = this.x, y = this.y, z = this.z;

		float[] e = m.getArray();

		this.x = e[0] * x + e[4] * y + e[8]  * z;
		this.y = e[1] * x + e[5] * y + e[9]  * z;
		this.z = e[2] * x + e[6] * y + e[10] * z;

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
	public Vector3 divide(float scalar)
	{
		if ( scalar != 0 ) {

			float invScalar = 1.0f / scalar;

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

	public Vector3 clamp(float minVal, float maxVal) 
	{
		_min.set( minVal, minVal, minVal );
		_max.set( maxVal, maxVal, maxVal );

		return this.clamp( _min, _max );
	}

	public Vector3 floor() 
	{

		this.x = (float)Math.floor( this.x );
		this.y = (float)Math.floor( this.y );
		this.z = (float)Math.floor( this.z );

		return this;

	}

	public Vector3 ceil() 
	{

		this.x = (float)Math.ceil( this.x );
		this.y = (float)Math.ceil( this.y );
		this.z = (float)Math.ceil( this.z );

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

		this.x = ( this.x < 0 ) ? (float)Math.ceil( this.x ) : (float)Math.floor( this.x );
		this.y = ( this.y < 0 ) ? (float)Math.ceil( this.y ) : (float)Math.floor( this.y );
		this.z = ( this.z < 0 ) ? (float)Math.ceil( this.z ) : (float)Math.floor( this.z );

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
	public float dot(Vector3 v1)
	{
		return (this.x * v1.x + this.y * v1.y + this.z * v1.z);
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

	public float lengthManhattan()
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

	public Vector3 setLength(float l)
	{
		float oldLength = this.length();

		if ( oldLength != 0 && l != oldLength  ) {

			this.multiply( l / oldLength );
		}

		return this;
	}

	public Vector3 lerp(Vector3 v1, float alpha)
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
		float ax = a.x, ay = a.y, az = a.z;
		float bx = b.x, by = b.y, bz = b.z;

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

		float dot = this.dot( _v1 );

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
	
	public float angleTo( Vector3 v ) 
	{
		float theta = this.dot( v ) / ( this.length() * v.length() );

		// clamp, to handle numerical problems

		return (float)Math.acos( Mathematics.clamp( theta, - 1, 1 ) );
	}

	public float distanceTo(Vector3 v1)
	{
		return (float)Math.sqrt(distanceToSquared(v1));
	}

	public float distanceToSquared(Vector3 v1)
	{
		float dx = this.x - v1.x;
		float dy = this.y - v1.y;
		float dz = this.z - v1.z;
		return (dx * dx + dy * dy + dz * dz);
	}
	
	public Vector3 setFromMatrixPosition( Matrix4 m )
	{

		this.x = m.getArray()[ 12 ];
		this.y = m.getArray()[ 13 ];
		this.z = m.getArray()[ 14 ];

		return this;
	}
	
	public Vector3 setFromMatrixScale( Matrix4 m ) 
	{

		float[] el = m.getArray();

		float sx = this.set( el[ 0 ], el[ 1 ], el[ 2 ] ).length();
		float sy = this.set( el[ 4 ], el[ 5 ], el[ 6 ] ).length();
		float sz = this.set( el[ 8 ], el[ 9 ], el[ 10 ] ).length();

		this.x = sx;
		this.y = sy;
		this.z = sz;

		return this;
	}
	
	public Vector3 setFromMatrixColumn( int index, Matrix4 matrix ) 
	{

		int offset = index * 4;

		float[] me = matrix.getArray();

		this.x = me[ offset ];
		this.y = me[ offset + 1 ];
		this.z = me[ offset + 2 ];

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
	
	public Vector3 fromArray ( FloatBuffer array )
	{
		return fromArray(array, 0);
	}

	public Vector3 fromArray ( FloatBuffer array, int offset )
	{

		this.x = array.get( offset );
		this.y = array.get( offset + 1 );
		this.z = array.get( offset + 2 );

		return this;

	}
	
	public FloatBuffer toArray()
	{
		return toArray(BufferUtils.newFloatBuffer(3), 0);
	}

	public FloatBuffer toArray( FloatBuffer array, int offset )
	{

		array.put(offset, this.x);
		array.put(offset + 1, this.y);
		array.put( offset + 2 , this.z);

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
