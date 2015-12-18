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

import org.parallax3d.parallax.ThreeJsObject;

/**
 * Implementation of Quaternion which provide a convenient mathematical notation 
 * for representing orientations and rotations of objects in three dimensions.
 * <p>
 * Quaternion represented by four coordinates: X, Y, Z, W
 * 
 * <pre>
 * {@code
 * Quaternion q = new Quaternion(); 
 * q.setFromAxisAngle( new Vector3( 0, 1, 0 ), Math.PI / 2 );  
 * 
 * Vector3 v = new Vector3( 1, 0, 0 ); 
 * q.multiplyVector3( v );
 * }
 * </pre>
 * @author thothbot
 *
 */
@ThreeJsObject("THREE.Quaternion")
public class Quaternion
{
	public static interface QuaternionChangeHandler 
	{
		void onChange(Quaternion quaternion);
	}

	/**
	 * The X coordinate.
	 */
	public float x;

	/**
	 * The Y coordinate.
	 */
	public float y;

	/**
	 * The Z coordinate.
	 */
	public float z;

	/**
	 * The W coordinate.
	 */
	public float w;
	
	private QuaternionChangeHandler handler;
	
	// Temporary variables
	static Vector3 _v1  = new Vector3();

	/**
	 * Default constructor will make Quaternion (0.0, 0.0, 0.0, 1.0)
	 */
	public Quaternion() 
	{
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
		this.w = 1.0f;
	}

	/**
	 * Constructs and initializes a Quaternion from the specified X, Y, Z, W
	 * coordinates.
	 * 
	 * Will make Quaternion (X, Y, Z, W)
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param z the Z coordinate
	 * @param w the W coordinate
	 */
	public Quaternion(float x, float y, float z, float w) 
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Constructs and initializes a Quaternion from the specified X, Y, Z coordinates.
	 * Will make Quaternion (X, Y, Z, 1.0)
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public Quaternion(float x, float y, float z) 
	{
		this();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setHandler(QuaternionChangeHandler handler) 
	{
		this.handler = handler;
	}
	
	private void onChange() 
	{
		if(this.handler != null)
			this.handler.onChange(Quaternion.this);
	}

	/**
	 * get X coordinate from the Quaternion
	 * 
	 * @return a X coordinate
	 */
	public float getX()
	{
		return this.x;
	}
	
	/**
	 * get Y coordinate from the Quaternion
	 * 
	 * @return a Y coordinate
	 */
	public float getY()
	{
		return this.y;
	}
	
	/**
	 * get Z coordinate from the Quaternion
	 * 
	 * @return a Z coordinate
	 */
	public float getZ()
	{
		return this.z;
	}
	
	/**
	 * get W coordinate from the Quaternion
	 * 
	 * @return a W coordinate
	 */
	public float getW()
	{
		return this.w;
	}
	
	public Quaternion set(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		
		this.onChange();
		
		return this;
	}
	
	/**
	 * Copy values from input Quaternion to the values of current Quaternion.
	 * 
	 * @param c1 the input Quaternion
	 */
	public Quaternion copy(Quaternion c1)
	{
		this.x = c1.x;
		this.y = c1.y;
		this.z = c1.z;
		this.w = c1.w;
		
		this.onChange();
		
		return this;
	}
	
	public Quaternion setFromEuler( Euler euler ) 
	{
		return setFromEuler(euler, false);
	}
	
	public Quaternion setFromEuler( Euler euler, boolean update ) 
	{

		// http://www.mathworks.com/matlabcentral/fileexchange/
		// 	20696-function-to-convert-between-dcm-euler-angles-quaternions-and-euler-vectors/
		//	content/SpinCalc.m

		float c1 = (float)Math.cos( euler.getX() / 2.0 );
		float c2 = (float)Math.cos( euler.getY() / 2.0 );
		float c3 = (float)Math.cos( euler.getZ() / 2.0 );
		float s1 = (float)Math.sin( euler.getX() / 2.0 );
		float s2 = (float)Math.sin( euler.getY() / 2.0 );
		float s3 = (float)Math.sin( euler.getZ() / 2.0 );

		if ( euler.getOrder().equals("XYZ") ) {

			this.x = s1 * c2 * c3 + c1 * s2 * s3;
			this.y = c1 * s2 * c3 - s1 * c2 * s3;
			this.z = c1 * c2 * s3 + s1 * s2 * c3;
			this.w = c1 * c2 * c3 - s1 * s2 * s3;

		} else if ( euler.getOrder().equals("YXZ") ) {

			this.x = s1 * c2 * c3 + c1 * s2 * s3;
			this.y = c1 * s2 * c3 - s1 * c2 * s3;
			this.z = c1 * c2 * s3 - s1 * s2 * c3;
			this.w = c1 * c2 * c3 + s1 * s2 * s3;

		} else if ( euler.getOrder().equals("ZXY") ) {

			this.x = s1 * c2 * c3 - c1 * s2 * s3;
			this.y = c1 * s2 * c3 + s1 * c2 * s3;
			this.z = c1 * c2 * s3 + s1 * s2 * c3;
			this.w = c1 * c2 * c3 - s1 * s2 * s3;

		} else if ( euler.getOrder().equals("ZYX") ) {

			this.x = s1 * c2 * c3 - c1 * s2 * s3;
			this.y = c1 * s2 * c3 + s1 * c2 * s3;
			this.z = c1 * c2 * s3 - s1 * s2 * c3;
			this.w = c1 * c2 * c3 + s1 * s2 * s3;

		} else if ( euler.getOrder().equals("YZX") ) {

			this.x = s1 * c2 * c3 + c1 * s2 * s3;
			this.y = c1 * s2 * c3 + s1 * c2 * s3;
			this.z = c1 * c2 * s3 - s1 * s2 * c3;
			this.w = c1 * c2 * c3 - s1 * s2 * s3;

		} else if ( euler.getOrder().equals("XZY") ) {

			this.x = s1 * c2 * c3 - c1 * s2 * s3;
			this.y = c1 * s2 * c3 - s1 * c2 * s3;
			this.z = c1 * c2 * s3 + s1 * s2 * c3;
			this.w = c1 * c2 * c3 + s1 * s2 * s3;

		}
		
		if(update) this.onChange();

		return this;
	}

	/**
	 * from
	 * <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/angleToQuaternion/index.htm">www.euclideanspace.com</a>		
	 * @param axis the axis have to be normalized
	 * @param angle the angle
	 */
	public Quaternion setFromAxisAngle(Vector3 axis, float angle)
	{
		float halfAngle = angle / 2.0f;
		float s = (float)Math.sin(halfAngle);

		this.x = axis.x * s;
		this.y = axis.y * s;
		this.z = axis.z * s;
		this.w = (float)Math.cos(halfAngle);
		
		this.onChange();
		
		return this;
	}

	/**
	 * http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/index.htm
	 * assumes the upper 3x3 of m is a pure rotation matrix (i.e, unscaled)
	 * 
	 * @param m
	 * @return
	 */
	public Quaternion setFromRotationMatrix( Matrix4 m ) 
	{

		float[] te = m.getArray();

		float m11 = te[0], m12 = te[4], m13 = te[8],
			m21 = te[1], m22 = te[5], m23 = te[9],
			m31 = te[2], m32 = te[6], m33 = te[10];

		float trace = m11 + m22 + m33;
		float s;

		if ( trace > 0 ) {

			s = 0.5f / (float)Math.sqrt( trace + 1.0 );

			this.w = 0.25f / s;
			this.x = ( m32 - m23 ) * s;
			this.y = ( m13 - m31 ) * s;
			this.z = ( m21 - m12 ) * s;

		} else if ( m11 > m22 && m11 > m33 ) {

			s = 2.0f * (float)Math.sqrt( 1.0 + m11 - m22 - m33 );

			this.w = ( m32 - m23 ) / s;
			this.x = 0.25f * s;
			this.y = ( m12 + m21 ) / s;
			this.z = ( m13 + m31 ) / s;

		} else if ( m22 > m33 ) {

			s = 2.0f * (float)Math.sqrt( 1.0 + m22 - m11 - m33 );

			this.w = ( m13 - m31 ) / s;
			this.x = ( m12 + m21 ) / s;
			this.y = 0.25f * s;
			this.z = ( m23 + m32 ) / s;

		} else {

			s = 2.0f * (float)Math.sqrt( 1.0 + m33 - m11 - m22 );

			this.w = ( m21 - m12 ) / s;
			this.x = ( m13 + m31 ) / s;
			this.y = ( m23 + m32 ) / s;
			this.z = 0.25f * s;

		}
		
		this.onChange();

		return this;

	}

	/**
	 * http://lolengine.net/blog/2014/02/24/quaternion-from-two-vectors-final
	 * assumes direction vectors vFrom and vTo are normalized
	 * 
	 * @param vFrom
	 * @param vTo
	 * @return
	 */
	public Quaternion setFromUnitVectors(Vector3 vFrom, Vector3 vTo ) 
	{
		float EPS = 0.000001f;

		float r = vFrom.dot( vTo ) + 1;

		if ( r < EPS ) {

			r = 0;

			if ( Math.abs( vFrom.x ) > Math.abs( vFrom.z ) ) {

				_v1.set( - vFrom.y, vFrom.x, 0 );

			} else {

				_v1.set( 0, - vFrom.z, vFrom.y );

			}

		} else {

			_v1.cross( vFrom, vTo );

		}

		this.x = _v1.x;
		this.y = _v1.y;
		this.z = _v1.z;
		this.w = r;

		this.normalize();

		return this;

	}


	/**
	 * Negates the value of this Quaternion in place.
	 */
	public Quaternion inverse()
	{
		this.conjugate().normalize();

		return this;
	}

	public Quaternion conjugate() 
	{
		this.x *= -1;
		this.y *= -1;
		this.z *= -1;
		
		this.onChange();

		return this;
	}
	
	public float dot( Quaternion v ) 
	{
		return this.x * v.x + this.y * v.y + this.z * v.z + this.w * v.w;
	}

	public float lengthSq() 
	{
		return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
	}
	
	public float length()
	{
		return (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
	}

	/**
	 * Normalize the current Quaternion
	 */
	public Quaternion normalize()
	{
		float l = this.length();

		if ( l == 0 ) {

			this.x = 0;
			this.y = 0;
			this.z = 0;
			this.w = 1;

		} else {

			l = 1 / l;

			this.x = this.x * l;
			this.y = this.y * l;
			this.z = this.z * l;
			this.w = this.w * l;

		}
		
		this.onChange();

		return this;
	}

	/**
	 * Sets the value of this Quaternion to the vector multiplication of Quaternion a and
	 * Quaternion v2.
	 * 
	 * Based on <a href="http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/code/index.htm">http://www.euclideanspace.com</a>
	 * 
	 * @param a the first Quaternion
	 * @param b the second Quaternion
	 * 
	 */
	public Quaternion multiply(Quaternion a, Quaternion b)
	{
		float qax = a.x, qay = a.y, qaz = a.z, qaw = a.w;
		float qbx = b.x, qby = b.y, qbz = b.z, qbw = b.w;

		this.x = qax * qbw + qaw * qbx + qay * qbz - qaz * qby;
		this.y = qay * qbw + qaw * qby + qaz * qbx - qax * qbz;
		this.z = qaz * qbw + qaw * qbz + qax * qby - qay * qbx;
		this.w = qaw * qbw - qax * qbx - qay * qby - qaz * qbz;

		this.onChange();
		
		return this;
	}

	/**
	 * Sets the value of this Quaternion to the vector multiplication of itself and
	 * Quaternion b.
	 * (this = this * b)
	 * 
	 * @param b the other Quaternion
	 * 
	 */
	public Quaternion multiply(Quaternion b)
	{
		return this.multiply( this, b );
	}

	/**
	 * Sets the value of the input vector to the vector multiplication of input vector and
	 * the current Quaternion.
	 * 
	 * @param vector the input vector
	 * 
	 * @return the modified input vector
	 */
	public Vector3 multiplyVector3(Vector3 vector)
	{
		return vector.apply( this );
	}

	/**
	 * Quaternion Interpolation
	 * 
	 * Based on <a href="http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/slerp/">http://www.euclideanspace.com</a>
	 * 
	 * @param qb  the quaternion a (first quaternion to be interpolated between)
	 * @param t   a scalar between 0.0 (at qa) and 1.0 (at qb)
	 * 
	 * @return the interpolated quaternion
	 */
	public Quaternion slerp(Quaternion qb, float t)
	{
		if ( t == 0 ) return this;
		if ( t == 1 ) return this.copy( qb );

		float x = this.x, y = this.y, z = this.z, w = this.w;

		// http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/slerp/

		float cosHalfTheta = w * qb.w + x * qb.x + y * qb.y + z * qb.z;

		if ( cosHalfTheta < 0 ) {

			this.w = - qb.w;
			this.x = - qb.x;
			this.y = - qb.y;
			this.z = - qb.z;

			cosHalfTheta = - cosHalfTheta;

		} else {

			this.copy( qb );

		}

		if ( cosHalfTheta >= 1.0 ) {

			this.w = w;
			this.x = x;
			this.y = y;
			this.z = z;

			return this;

		}

		float halfTheta = (float)Math.acos( cosHalfTheta );
		float sinHalfTheta = (float)Math.sqrt( 1.0 - cosHalfTheta * cosHalfTheta );

		if ( Math.abs( sinHalfTheta ) < 0.001 ) {

			this.w = 0.5f * ( w + this.w );
			this.x = 0.5f * ( x + this.x );
			this.y = 0.5f * ( y + this.y );
			this.z = 0.5f * ( z + this.z );

			return this;

		}

		float ratioA = (float)Math.sin( ( 1 - t ) * halfTheta ) / sinHalfTheta,
		ratioB = (float)Math.sin( t * halfTheta ) / sinHalfTheta;

		this.w = ( w * ratioA + this.w * ratioB );
		this.x = ( x * ratioA + this.x * ratioB );
		this.y = ( y * ratioA + this.y * ratioB );
		this.z = ( z * ratioA + this.z * ratioB );

		this.onChange();
		
		return this;
	}
	
	public static Quaternion slerp(Quaternion qa, Quaternion qb, Quaternion qm, float t ) 
	{
		return qm.copy( qa ).slerp( qb, t );
	}

	public boolean equals( Quaternion quaternion ) 
	{
		return ( quaternion.x == this.x ) && ( quaternion.y == this.y ) && ( quaternion.z == this.z ) && ( quaternion.w == this.w );
	}
	
	/**
	 * Clone the current Quaternion
	 * quaternion.clone() != quaternion;
	 * 
	 * @return the new instance of Quaternion
	 */
	public Quaternion clone()
	{
		return new Quaternion( this.x, this.y, this.z, this.w );
	}
	
	@Override
	public String toString() 
	{
		return "(" + this.x + ", " + this.y + ", " + this.z +  ", " + this.w + ")";
	}
}
