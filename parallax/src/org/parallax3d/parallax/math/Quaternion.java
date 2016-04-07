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

import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Float64Array;

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
@ThreejsObject("THREE.Quaternion")
public class Quaternion
{
	public static interface QuaternionChangeHandler
	{
		void onChange(Quaternion quaternion);
	}

	/**
	 * The X coordinate.
	 */
	double x;

	/**
	 * The Y coordinate.
	 */
	double y;

	/**
	 * The Z coordinate.
	 */
	double z;

	/**
	 * The W coordinate.
	 */
	double w;

	QuaternionChangeHandler handler;

	/**
	 * Default constructor will make Quaternion (0.0, 0.0, 0.0, 1.0)
	 */
	public Quaternion()
	{
		this.x = 0.0;
		this.y = 0.0;
		this.z = 0.0;
		this.w = 1.0;
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
	public Quaternion(double x, double y, double z, double w)
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
	public Quaternion(double x, double y, double z)
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
	public double getX()
	{
		return this.x;
	}

	/**
	 * get Y coordinate from the Quaternion
	 *
	 * @return a Y coordinate
	 */
	public double getY()
	{
		return this.y;
	}

	/**
	 * get Z coordinate from the Quaternion
	 *
	 * @return a Z coordinate
	 */
	public double getZ()
	{
		return this.z;
	}

	/**
	 * get W coordinate from the Quaternion
	 *
	 * @return a W coordinate
	 */
	public double getW()
	{
		return this.w;
	}

	public Quaternion set(double x, double y, double z, double w)
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

		double c1 = Math.cos( euler.getX() / 2.0 );
		double c2 = Math.cos( euler.getY() / 2.0 );
		double c3 = Math.cos( euler.getZ() / 2.0 );
		double s1 = Math.sin( euler.getX() / 2.0 );
		double s2 = Math.sin( euler.getY() / 2.0 );
		double s3 = Math.sin( euler.getZ() / 2.0 );

		switch (euler.getOrder()) {
			case "XYZ":

				this.x = s1 * c2 * c3 + c1 * s2 * s3;
				this.y = c1 * s2 * c3 - s1 * c2 * s3;
				this.z = c1 * c2 * s3 + s1 * s2 * c3;
				this.w = c1 * c2 * c3 - s1 * s2 * s3;

				break;
			case "YXZ":

				this.x = s1 * c2 * c3 + c1 * s2 * s3;
				this.y = c1 * s2 * c3 - s1 * c2 * s3;
				this.z = c1 * c2 * s3 - s1 * s2 * c3;
				this.w = c1 * c2 * c3 + s1 * s2 * s3;

				break;
			case "ZXY":

				this.x = s1 * c2 * c3 - c1 * s2 * s3;
				this.y = c1 * s2 * c3 + s1 * c2 * s3;
				this.z = c1 * c2 * s3 + s1 * s2 * c3;
				this.w = c1 * c2 * c3 - s1 * s2 * s3;

				break;
			case "ZYX":

				this.x = s1 * c2 * c3 - c1 * s2 * s3;
				this.y = c1 * s2 * c3 + s1 * c2 * s3;
				this.z = c1 * c2 * s3 - s1 * s2 * c3;
				this.w = c1 * c2 * c3 + s1 * s2 * s3;

				break;
			case "YZX":

				this.x = s1 * c2 * c3 + c1 * s2 * s3;
				this.y = c1 * s2 * c3 + s1 * c2 * s3;
				this.z = c1 * c2 * s3 - s1 * s2 * c3;
				this.w = c1 * c2 * c3 - s1 * s2 * s3;

				break;
			case "XZY":

				this.x = s1 * c2 * c3 - c1 * s2 * s3;
				this.y = c1 * s2 * c3 - s1 * c2 * s3;
				this.z = c1 * c2 * s3 + s1 * s2 * c3;
				this.w = c1 * c2 * c3 + s1 * s2 * s3;

				break;
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
	public Quaternion setFromAxisAngle(Vector3 axis, double angle)
	{
		double halfAngle = angle / 2.0;
		double s = Math.sin(halfAngle);

		this.x = axis.x * s;
		this.y = axis.y * s;
		this.z = axis.z * s;
		this.w = Math.cos(halfAngle);

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

		Float32Array te = m.getArray();

		double m11 = te.get(0), m12 = te.get(4), m13 = te.get(8),
			m21 = te.get(1), m22 = te.get(5), m23 = te.get(9),
			m31 = te.get(2), m32 = te.get(6), m33 = te.get(10);

		double trace = m11 + m22 + m33;
		double s;

		if ( trace > 0 ) {

			s = 0.5 / Math.sqrt( trace + 1.0 );

			this.w = 0.25 / s;
			this.x = ( m32 - m23 ) * s;
			this.y = ( m13 - m31 ) * s;
			this.z = ( m21 - m12 ) * s;

		} else if ( m11 > m22 && m11 > m33 ) {

			s = 2.0 * Math.sqrt( 1.0 + m11 - m22 - m33 );

			this.w = ( m32 - m23 ) / s;
			this.x = 0.25 * s;
			this.y = ( m12 + m21 ) / s;
			this.z = ( m13 + m31 ) / s;

		} else if ( m22 > m33 ) {

			s = 2.0 * Math.sqrt( 1.0 + m22 - m11 - m33 );

			this.w = ( m13 - m31 ) / s;
			this.x = ( m12 + m21 ) / s;
			this.y = 0.25 * s;
			this.z = ( m23 + m32 ) / s;

		} else {

			s = 2.0 * Math.sqrt( 1.0 + m33 - m11 - m22 );

			this.w = ( m21 - m12 ) / s;
			this.x = ( m13 + m31 ) / s;
			this.y = ( m23 + m32 ) / s;
			this.z = 0.25 * s;

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
	// Temporary variables
	private static final Vector3 _v1  = new Vector3();
	public Quaternion setFromUnitVectors(Vector3 vFrom, Vector3 vTo )
	{
		double EPS = 0.000001;

		double r = vFrom.dot( vTo ) + 1;

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
		this.x *= -1.;
		this.y *= -1.;
		this.z *= -1.;

		this.onChange();

		return this;
	}

	public double dot( Quaternion v )
	{
		return this.x * v.x + this.y * v.y + this.z * v.z + this.w * v.w;
	}

	public double lengthSq()
	{
		return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
	}

	public double length()
	{
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
	}

	/**
	 * Normalize the current Quaternion
	 */
	public Quaternion normalize()
	{
		double l = this.length();

		if ( l == 0 ) {

			this.x = 0;
			this.y = 0;
			this.z = 0;
			this.w = 1;

		} else {

			l = 1. / l;

			this.x = this.x * l;
			this.y = this.y * l;
			this.z = this.z * l;
			this.w = this.w * l;

		}

		this.onChange();

		return this;
	}

	@Deprecated
	public Quaternion multiplyQuaternions(Quaternion a, Quaternion b)
	{
		return multiply(a, b);
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
		double qax = a.x, qay = a.y, qaz = a.z, qaw = a.w;
		double qbx = b.x, qby = b.y, qbz = b.z, qbw = b.w;

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
	@Deprecated
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
	public Quaternion slerp(Quaternion qb, double t)
	{
		if ( t == 0 ) return this;
		if ( t == 1 ) return this.copy( qb );

		double x = this.x, y = this.y, z = this.z, w = this.w;

		// http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/slerp/

		double cosHalfTheta = w * qb.w + x * qb.x + y * qb.y + z * qb.z;

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

		double halfTheta = Math.acos( cosHalfTheta );
		double sinHalfTheta = Math.sqrt( 1.0 - cosHalfTheta * cosHalfTheta );

		if ( Math.abs( sinHalfTheta ) < 0.001 ) {

			this.w = 0.5 * ( w + this.w );
			this.x = 0.5 * ( x + this.x );
			this.y = 0.5 * ( y + this.y );
			this.z = 0.5 * ( z + this.z );

			return this;

		}

		double ratioA = Math.sin( ( 1 - t ) * halfTheta ) / sinHalfTheta,
				ratioB = Math.sin( t * halfTheta ) / sinHalfTheta;

		this.w = ( w * ratioA + this.w * ratioB );
		this.x = ( x * ratioA + this.x * ratioB );
		this.y = ( y * ratioA + this.y * ratioB );
		this.z = ( z * ratioA + this.z * ratioB );

		this.onChange();

		return this;
	}

	public boolean equals( Quaternion quaternion )
	{
		return ( quaternion.x == this.x ) && ( quaternion.y == this.y ) && ( quaternion.z == this.z ) && ( quaternion.w == this.w );
	}

	public Quaternion fromArray ( double[] array )
	{
		int offset = 0;

		this.x = array[ offset ];
		this.y = array[ offset + 1 ];
		this.z = array[ offset + 2 ];
		this.w = array[ offset + 3 ];

		return this;
	}

	public Quaternion fromArray ( Float32Array array )
	{
		return fromArray(array, 0);
	}

	public Quaternion fromArray ( Float32Array array, int offset )
	{

		this.x = array.get( offset );
		this.y = array.get( offset + 1 );
		this.z = array.get( offset + 2 );
		this.w = array.get( offset + 3 );

		return this;

	}

	public Float32Array toArray()
	{
		return toArray(Float32Array.create(4), 0);
	}

	public Float32Array toArray( Float32Array array, int offset )
	{

		array.set(offset, this.x);
		array.set(offset + 1, this.y);
		array.set(offset + 2, this.z);
		array.set(offset + 3, this.w);

		return array;
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

	public static Quaternion slerp(Quaternion qa, Quaternion qb, Quaternion qm, double t )
	{
		return qm.copy( qa ).slerp( qb, t );
	}

	public static void slerpFlat(Float64Array dst, int dstOffset, Float64Array src0, int srcOffset0, Float64Array src1, int srcOffset1, double t )
	{

	}

	public static void slerpFlat(Float32Array dst, int dstOffset, Float32Array src0, int srcOffset0, Float32Array src1, int srcOffset1, double t )
	{

	}

	public static void slerpFlat(double[] dst, int dstOffset, double[] src0, int srcOffset0, double[] src1, int srcOffset1, double t )
	{

		// fuzz-free, array-based Quaternion SLERP operation

		double x0 = src0[ srcOffset0 + 0 ],
				y0 = src0[ srcOffset0 + 1 ],
				z0 = src0[ srcOffset0 + 2 ],
				w0 = src0[ srcOffset0 + 3 ],

				x1 = src1[ srcOffset1 + 0 ],
				y1 = src1[ srcOffset1 + 1 ],
				z1 = src1[ srcOffset1 + 2 ],
				w1 = src1[ srcOffset1 + 3 ];

		if ( w0 != w1 || x0 != x1 || y0 != y1 || z0 != z1 ) {

			double s = 1. - t,

					cos = x0 * x1 + y0 * y1 + z0 * z1 + w0 * w1,

					dir = ( cos >= 0 ? 1 : - 1 ),
					sqrSin = 1 - cos * cos;

			// Skip the Slerp for tiny steps to avoid numeric problems:
			if ( sqrSin > Mathematics.EPSILON ) {

				double sin = Math.sqrt( sqrSin ),
						len = Math.atan2( sin, cos * dir );

				s = Math.sin( s * len ) / sin;
				t = Math.sin( t * len ) / sin;

			}

			double tDir = t * dir;

			x0 = x0 * s + x1 * tDir;
			y0 = y0 * s + y1 * tDir;
			z0 = z0 * s + z1 * tDir;
			w0 = w0 * s + w1 * tDir;

			// Normalize in case we just did a lerp:
			if ( s == 1 - t ) {

				double f = 1. / Math.sqrt( x0 * x0 + y0 * y0 + z0 * z0 + w0 * w0 );

				x0 *= f;
				y0 *= f;
				z0 *= f;
				w0 *= f;

			}

		}

		dst[ dstOffset ] = x0 ;
		dst[ dstOffset + 1 ] = y0 ;
		dst[ dstOffset + 2 ] = z0 ;
		dst[ dstOffset + 3 ] = w0 ;

	}
}
