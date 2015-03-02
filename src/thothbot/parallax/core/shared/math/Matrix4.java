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
import thothbot.parallax.core.shared.Log;

/**
 * This class implements three-dimensional matrix. NxN, where N=4.
 * 
 * This matrix actually is array which is represented the following 
 * indexes:
 * 
 * <pre>{@code
 * 0 4  8 12
 * 1 5  9 13
 * 2 6 10 14
 * 3 7 11 15
 * }</pre>
 * 
 * @author thothbot
 *
 */
public class Matrix4
{
	private Float32Array elements;
	
	// Temporary variables
	static Vector3 _x = new Vector3();
	static Vector3 _y = new Vector3();
	static Vector3 _z = new Vector3();
	static Vector3 _v1 = new Vector3();
	static Vector3 _vector = new Vector3();
	static Matrix4 _matrix = new Matrix4();

	/**
	 * Default constructor will make identity four-dimensional matrix.
	 * 
	 * <pre>{@code
	 * 1 0 0 0
	 * 0 1 0 0
	 * 0 0 1 0
	 * 0 0 0 1
	 * }</pre>
	 */
	public Matrix4() 
	{
		this.elements = Float32Array.create(16);
		identity();
	}

	/**
	 * This constructor will create four-dimensional matrix. 
	 * This matrix uses input values n11-n44 and represented as 
	 * the following:
	 * 
	 * <pre>{@code
	 * n11 n12 n13 n14
	 * n21 n22 n23 n24
	 * n31 n32 n33 n34
	 * n41 n42 n43 n44
	 * }</pre>
	 */
	public Matrix4(double n11, double n12, double n13, double n14, 
			double n21, double n22, double n23, double n24, 
			double n31, double n32, double n33, double n34, 
			double n41, double n42, double n43, double n44) 
	{
		this.elements = Float32Array.create(16);
		set(
			n11, n12, n13, n14,
			n21, n22, n23, n24,
			n31, n32, n33, n34,
			n41, n42, n43, n44
		);
	}
	

	/**
	 * get the current Matrix which is represented 
	 * by Array[16] which the following indexes:
	 * 
	 * <pre>{@code
     * 0 4  8 12
     * 1 5  9 13
     * 2 6 10 14
     * 3 7 11 15
     * }</pre>
	 * 
	 * @return the Array
	 */
	public Float32Array getArray() 
	{
		return elements;
	}
	
	/**
	 * Setting input values n11-n44 to the current matrix.
	 * This matrix will be represented as the following:
	 * 
	 * <pre>{@code
	 * n11 n12 n13 n14
	 * n21 n22 n23 n24
	 * n31 n32 n33 n34
	 * n41 n42 n43 n44
	 * }</pre>
	 * 
	 * @return the current matrix.
	 */
	public Matrix4 set(
			double n11, double n12, double n13, double n14, 
			double n21, double n22, double n23, double n24, 
			double n31, double n32, double n33, double n34, 
			double n41, double n42, double n43, double n44)
	{
		this.getArray().set(0, n11);
		this.getArray().set(1, n21);
		this.getArray().set(2, n31);
		this.getArray().set(3, n41);
		
		this.getArray().set(4, n12);
		this.getArray().set(5, n22);
		this.getArray().set(6, n32);
		this.getArray().set(7, n42);
		
		this.getArray().set(8, n13);
		this.getArray().set(9, n23);
		this.getArray().set(10, n33);
		this.getArray().set(11, n43);
		
		this.getArray().set(12, n14);
		this.getArray().set(13, n24);
		this.getArray().set(14, n34);
		this.getArray().set(15, n44);
		
		return this;
	}

	/**
	 * Make  make identity four-dimensional matrix.
	 * 
	 * <pre>{@code
	 * 1 0 0 0
	 * 0 1 0 0
	 * 0 0 1 0
	 * 0 0 0 1
	 * }</pre>
	 */
	public Matrix4 identity()
	{
		set(
			1, 0, 0, 0, 
			0, 1, 0, 0,
			0, 0, 1, 0, 
			0, 0, 0, 1
		);
		
		return this;
	}

	/**
	 * Sets the value of this matrix to the values of input matrix.
	 *  
	 * @param m the matrix values which we wat to copy
	 * 
	 * @return the current matrix
	 */
	public Matrix4 copy(Matrix4 m)
	{
		Float32Array me = m.getArray();
		return set(
			me.get(0), me.get(4), me.get(8),  me.get(12), 
			me.get(1), me.get(5), me.get(9),  me.get(13), 
			me.get(2), me.get(6), me.get(10), me.get(14), 
			me.get(3), me.get(7), me.get(11), me.get(15)
		);
	}
	
	public Matrix4 copyPosition( Matrix4 m ) {

		Float32Array te = this.getArray();
		Float32Array me = m.getArray();

		te.set( 12 , me.get( 12 ));
		te.set( 13 , me.get( 13 ));
		te.set( 14 , me.get( 14 ));

		return this;
	}
	
	
	/**
	 * Setting rotation values to the rotation values of the input matrix.
	 * 
	 * @param m the input matrix
	 */
	public Matrix4 extractRotation(Matrix4 m)
	{
		Float32Array me = m.getArray();

		Vector3 v1 = new Vector3();

		double scaleX = 1.0 / v1.set(me.get(0), me.get(1), me.get(2)).length();
		double scaleY = 1.0 / v1.set(me.get(4), me.get(5), me.get(6)).length();
		double scaleZ = 1.0 / v1.set(me.get(8), me.get(9), me.get(10)).length();

		this.getArray().set(0, me.get(0) * scaleX);
		this.getArray().set(1, me.get(1) * scaleX);
		this.getArray().set(2, me.get(2) * scaleX);

		this.getArray().set(4, me.get(4) * scaleY);
		this.getArray().set(5, me.get(5) * scaleY);
		this.getArray().set(6, me.get(6) * scaleY);

		this.getArray().set(8, me.get(8) * scaleZ);
		this.getArray().set(9, me.get(9) * scaleZ);
		this.getArray().set(10, me.get(10) * scaleZ);
		
		return this;
	}
	
	public Matrix4 makeRotationFromEuler( Euler euler ) {

		Float32Array te = this.getArray();

		double x = euler.getX(), y = euler.getY(), z = euler.getZ();
		double a = Math.cos( x ), b = Math.sin( x );
		double c = Math.cos( y ), d = Math.sin( y );
		double e = Math.cos( z ), f = Math.sin( z );

		if ( euler.getOrder().equals("XYZ") ) {

			double ae = a * e, af = a * f, be = b * e, bf = b * f;

			te.set(0, c * e);
			te.set(4, - c * f);
			te.set(8, d);

			te.set(1, af + be * d);
			te.set(5, ae - bf * d);
			te.set(9, - b * c);

			te.set(2, bf - ae * d);
			te.set(6, be + af * d);
			te.set(10, a * c);

		} else if ( euler.getOrder().equals("YXZ") ) {

			double ce = c * e, cf = c * f, de = d * e, df = d * f;

			te.set(0, ce + df * b);
			te.set(4, de * b - cf);
			te.set(8, a * d);

			te.set(1, a * f);
			te.set(5, a * e);
			te.set(9, - b);

			te.set(2, cf * b - de);
			te.set(6, df + ce * b);
			te.set(10, a * c);

		} else if ( euler.getOrder().equals("ZXY") ) {

			double ce = c * e, cf = c * f, de = d * e, df = d * f;

			te.set(0, ce - df * b);
			te.set(4, - a * f);
			te.set(8, de + cf * b);

			te.set(1, cf + de * b);
			te.set(5, a * e);
			te.set(9, df - ce * b);

			te.set(2, - a * d);
			te.set(6, b);
			te.set(10, a * c);

		} else if ( euler.getOrder().equals("ZYX") ) {

			double ae = a * e, af = a * f, be = b * e, bf = b * f;

			te.set(0, c * e);
			te.set(4, be * d - af);
			te.set(8, ae * d + bf);

			te.set(1, c * f);
			te.set(5, bf * d + ae);
			te.set(9, af * d - be);

			te.set(2, - d);
			te.set(6, b * c);
			te.set(10, a * c);

		} else if ( euler.getOrder().equals("YZX") ) {

			double ac = a * c, ad = a * d, bc = b * c, bd = b * d;

			te.set(0, c * e);
			te.set(4, bd - ac * f);
			te.set(8, bc * f + ad);

			te.set(1, f);
			te.set(5, a * e);
			te.set(9, - b * e);

			te.set(2, - d * e);
			te.set(6, ad * f + bc);
			te.set(10, ac - bd * f);

		} else if ( euler.getOrder().equals("XZY") ) {

			double ac = a * c, ad = a * d, bc = b * c, bd = b * d;

			te.set(0, c * e);
			te.set(4, - f);
			te.set(8, d * e);

			te.set(1, ac * f + bd);
			te.set(5, a * e);
			te.set(9, ad * f - bc);

			te.set(2, bc * f - ad);
			te.set(6, b * e);
			te.set(10, bd * f + ac);

		}

		// last column
		te.set(3, 0);
		te.set(7, 0);
		te.set(11, 0);

		// bottom row
		te.set(12, 0);
		te.set(13, 0);
		te.set(14, 0);
		te.set(15, 1.0);

		return this;
	}

	public Matrix4 makeRotationFromQuaternion( Quaternion q ) {

		Float32Array te = this.elements;

		double x = q.getX(), y = q.getY(), z = q.getZ(), w = q.getW();
		double x2 = x + x, y2 = y + y, z2 = z + z;
		double xx = x * x2, xy = x * y2, xz = x * z2;
		double yy = y * y2, yz = y * z2, zz = z * z2;
		double wx = w * x2, wy = w * y2, wz = w * z2;

		te.set(0, 1.0 - ( yy + zz ));
		te.set(4, xy - wz);
		te.set(8, xz + wy);

		te.set(1, xy + wz);
		te.set(5, 1.0 - ( xx + zz ));
		te.set(9, yz - wx);

		te.set(2, xz - wy);
		te.set(6, yz + wx);
		te.set(10, 1.0 - ( xx + yy ));

		// last column
		te.set(3, 0);
		te.set(7, 0);
		te.set(11, 0);

		// bottom row
		te.set(12, 0);
		te.set(13, 0);
		te.set(14, 0);
		te.set(15, 1.0);

		return this;

	}
	
	/**
	 * Modifies the current matrix by looking at target on defined eye.
	 * 
	 * @param eye the Eye vector
	 * @param target the Target vector
	 * @param up the Up vector
	 * 
	 * @return the current matrix
	 */
	public Matrix4 lookAt(Vector3 eye, Vector3 target, Vector3 up)
	{
		Float32Array te = this.elements;

		_z.sub( eye, target ).normalize();

		if ( _z.length() == 0 ) {

			_z.z = 1.0;

		}

		_x.cross( up, _z ).normalize();

		if ( _x.length() == 0 ) {

			_z.x += 0.0001;
			_x.cross( up, _z ).normalize();

		}

		_y.cross( _z, _x );


		te.set(0,  _x.getX()); te.set(4,  _y.getX()); te.set(8,  _z.getX());
		te.set(1,  _x.getY()); te.set(5,  _y.getY()); te.set(9,  _z.getY());
		te.set(2,  _x.getZ()); te.set(6,  _y.getZ()); te.set(10,  _z.getZ());

		return this;

	}
	
	/**
	 * Sets the value of this matrix to the matrix multiplication of m1 and
	 * vector m2.
	 * 
	 * @param m1 the first matrix
	 * @param m2 the second matrix
	 * 
	 * @return the current matrix
	 */
	public Matrix4 multiply(Matrix4 m1, Matrix4 m2)
	{
		Float32Array ae = m1.getArray();
		Float32Array be = m2.getArray();

		double a11 = ae.get(0), a12 = ae.get(4), a13 = ae.get(8), a14 = ae.get(12);
		double a21 = ae.get(1), a22 = ae.get(5), a23 = ae.get(9), a24 = ae.get(13);
		double a31 = ae.get(2), a32 = ae.get(6), a33 = ae.get(10), a34 = ae.get(14);
		double a41 = ae.get(3), a42 = ae.get(7), a43 = ae.get(11), a44 = ae.get(15);

		double b11 = be.get(0), b12 = be.get(4), b13 = be.get(8), b14 = be.get(12);
		double b21 = be.get(1), b22 = be.get(5), b23 = be.get(9), b24 = be.get(13);
		double b31 = be.get(2), b32 = be.get(6), b33 = be.get(10), b34 = be.get(14);
		double b41 = be.get(3), b42 = be.get(7), b43 = be.get(11), b44 = be.get(15);

		this.getArray().set(0, (a11 * b11 + a12 * b21 + a13 * b31 + a14 * b41));
		this.getArray().set(4, (a11 * b12 + a12 * b22 + a13 * b32 + a14 * b42));
		this.getArray().set(8, (a11 * b13 + a12 * b23 + a13 * b33 + a14 * b43));
		this.getArray().set(12, (a11 * b14 + a12 * b24 + a13 * b34 + a14 * b44));

		this.getArray().set(1, (a21 * b11 + a22 * b21 + a23 * b31 + a24 * b41));
		this.getArray().set(5, (a21 * b12 + a22 * b22 + a23 * b32 + a24 * b42));
		this.getArray().set(9, (a21 * b13 + a22 * b23 + a23 * b33 + a24 * b43));
		this.getArray().set(13, (a21 * b14 + a22 * b24 + a23 * b34 + a24 * b44));

		this.getArray().set(2, (a31 * b11 + a32 * b21 + a33 * b31 + a34 * b41));
		this.getArray().set(6, (a31 * b12 + a32 * b22 + a33 * b32 + a34 * b42));
		this.getArray().set(10, (a31 * b13 + a32 * b23 + a33 * b33 + a34 * b43));
		this.getArray().set(14, (a31 * b14 + a32 * b24 + a33 * b34 + a34 * b44));

		this.getArray().set(3, (a41 * b11 + a42 * b21 + a43 * b31 + a44 * b41));
		this.getArray().set(7, (a41 * b12 + a42 * b22 + a43 * b32 + a44 * b42));
		this.getArray().set(11, (a41 * b13 + a42 * b23 + a43 * b33 + a44 * b43));
		this.getArray().set(15, (a41 * b14 + a42 * b24 + a43 * b34 + a44 * b44));
		
		return this;
	}

	/**
	 * Sets the value of this matrix to the matrix multiplication of itself and
	 * matrix m.
	 * {@code (this = this * m)}
	 * 
	 * @param m the other matrix
	 * 
	 * @return the current matrix
	 */
	public Matrix4 multiply(Matrix4 m)
	{
		return multiply(this, m);
	}

	/**
	 * Sets the value of this matrix to the scalar multiplication of the scale
	 * factor with this.
	 * 
	 * @param s the scalar value
	 * 
	 * @return the current matrix
	 */
	public Matrix4 multiply(double s)
	{
		this.getArray().set(0, (this.getArray().get(0) * s));
		this.getArray().set(4, (this.getArray().get(4) * s));
		this.getArray().set(8, (this.getArray().get(8) * s));
		this.getArray().set(12, (this.getArray().get(12) * s));
		this.getArray().set(1, (this.getArray().get(1) * s));
		this.getArray().set(5, (this.getArray().get(5) * s));
		this.getArray().set(9, (this.getArray().get(9) * s));
		this.getArray().set(13, (this.getArray().get(13) * s));
		this.getArray().set(2, (this.getArray().get(2) * s));
		this.getArray().set(6, (this.getArray().get(6) * s));
		this.getArray().set(10, (this.getArray().get(10) * s));
		this.getArray().set(14, (this.getArray().get(14) * s));
		this.getArray().set(3, (this.getArray().get(3) * s));
		this.getArray().set(7, (this.getArray().get(7) * s));
		this.getArray().set(11, (this.getArray().get(11) * s));
		this.getArray().set(15, (this.getArray().get(15) * s));
		
		return this;
	}
	
	public Float32Array applyToVector3Array (Float32Array array) 
	{
		return applyToVector3Array(array, 0, array.getLength());
	}
	
	public Float32Array applyToVector3Array (Float32Array array, int offset, int length) 
	{
		for ( int i = 0, j = offset, il; i < length; i += 3, j += 3 ) {

			_v1.x = array.get( j );
			_v1.y = array.get( j + 1 );
			_v1.z = array.get( j + 2 );

			_v1.apply( this );

			array.set( j , _v1.x);
			array.set( j + 1 , _v1.y);
			array.set( j + 2 , _v1.z);

		}

		return array;
	}

	/**
	 * get the current matrix determinant.
	 * 
	 * @return the matrix determinant
	 */
	public double determinant()
	{
		double n11 = this.getArray().get(0), n12 = this.getArray().get(4), n13 = this.getArray().get(8),  n14 = this.getArray().get(12);
		double n21 = this.getArray().get(1), n22 = this.getArray().get(5), n23 = this.getArray().get(9),  n24 = this.getArray().get(13);
		double n31 = this.getArray().get(2), n32 = this.getArray().get(6), n33 = this.getArray().get(10), n34 = this.getArray().get(14);
		double n41 = this.getArray().get(3), n42 = this.getArray().get(7), n43 = this.getArray().get(11), n44 = this.getArray().get(15);

		//TODO: make this more efficient
		//( based on http://www.euclideanspace.com/maths/algebra/matrix/functions/inverse/fourD/index.htm )

		return (
			n41 * (
				+ n14 * n23 * n32
				 - n13 * n24 * n32
				 - n14 * n22 * n33
				 + n12 * n24 * n33
				 + n13 * n22 * n34
				 - n12 * n23 * n34
			) +
			n42 * (
				+ n11 * n23 * n34
				 - n11 * n24 * n33
				 + n14 * n21 * n33
				 - n13 * n21 * n34
				 + n13 * n24 * n31
				 - n14 * n23 * n31
			) +
			n43 * (
				+ n11 * n24 * n32
				 - n11 * n22 * n34
				 - n14 * n21 * n32
				 + n12 * n21 * n34
				 + n14 * n22 * n31
				 - n12 * n24 * n31
			) +
			n44 * (
				- n13 * n22 * n31
				 - n11 * n23 * n32
				 + n11 * n22 * n33
				 + n13 * n21 * n32
				 - n12 * n21 * n33
				 + n12 * n23 * n31
			)

		);
	}

	/**
	 * Transpose the current matrix where its rows will be the 
	 * columns or its columns are the rows of the current matrix.
	 * 
	 * @return the current matrix
	 */
	public Matrix4 transpose()
	{
		Float32Array te = this.getArray();
		double tmp;

		tmp = te.get(1); te.set(1, te.get(4)); te.set(4, tmp);
		tmp = te.get(2); te.set(2, te.get(8)); te.set(8, tmp);
		tmp = te.get(6); te.set(6, te.get(9)); te.set(9, tmp);

		tmp = te.get(3); te.set(3, te.get(12)); te.set(12, tmp);
		tmp = te.get(7); te.set(7, te.get(13)); te.set(13, tmp);
		tmp = te.get(11); te.set(11, te.get(14)); te.set(14, tmp);

		return this;
	}

	/**
	 * Sets the value of input array to the values of the current matrix.
	 * The indexes in the array will be the following:
	 * 
	 * <pre>{@code
	 * 0 4  8 12
     * 1 5  9 13
     * 2 6 10 14
     * 3 7 11 15
     * }</pre>
     * 
	 * @param flat the array for storing matrix values
	 * 
	 * @return the modified input vector
	 */
	public Float32Array flattenToArrayOffset(Float32Array flat)
	{
		return flattenToArrayOffset(flat, 0);
	}

	/**
	 * Sets the value of input array to the values of the current matrix.
	 * The indexes in the array will be the following:
	 * 
	 * <pre>{@code
	 * 0 4  8 12
     * 1 5  9 13
     * 2 6 10 14
     * 3 7 11 15
     * }</pre>
     * 
	 * @param flat the array for storing matrix values
	 * @param offset the offset value
	 * 
	 * @return the modified input vector
	 */
	public Float32Array flattenToArrayOffset(Float32Array flat, int offset)
	{
		flat.set(offset, this.getArray().get(0));
		flat.set(offset + 1, this.getArray().get(1));
		flat.set(offset + 2, this.getArray().get(2));
		flat.set(offset + 3, this.getArray().get(3));

		flat.set(offset + 4, this.getArray().get(4));
		flat.set(offset + 5, this.getArray().get(5));
		flat.set(offset + 6, this.getArray().get(6));
		flat.set(offset + 7, this.getArray().get(7));

		flat.set(offset + 8, this.getArray().get(8));
		flat.set(offset + 9, this.getArray().get(9));
		flat.set(offset + 10, this.getArray().get(10));
		flat.set(offset + 11, this.getArray().get(11));

		flat.set(offset + 12, this.getArray().get(12));
		flat.set(offset + 13, this.getArray().get(13));
		flat.set(offset + 14, this.getArray().get(14));
		flat.set(offset + 15, this.getArray().get(15));

		return flat;
	}

	/**
	 * Setting position values of the current matrix to the values of
	 * input vector.
	 * 
	 * @param v the input vector
	 * 
	 * @return the current matrix
	 */
	public Matrix4 setPosition(Vector3 v)
	{
		this.getArray().set(12, v.getX());
		this.getArray().set(13, v.getY());
		this.getArray().set(14, v.getZ());

		return this;
	}

	/**
	 * Sets the value of this matrix to the matrix inverse of the passed matrix
	 * m.
	 * 
	 * Based on <a href="http://www.euclideanspace.com/maths/algebra/matrix/functions/inverse/fourD/index.htm">http://www.euclideanspace.com</a>
	 * 
	 * @param m the matrix to be inverted
	 */
	public Matrix4 getInverse(Matrix4 m)
	{
		Float32Array te = this.getArray();
		Float32Array me = m.getArray();

		double n11 = me.get(0), n12 = me.get(4), n13 = me.get(8),  n14 = me.get(12);
		double n21 = me.get(1), n22 = me.get(5), n23 = me.get(9),  n24 = me.get(13);
		double n31 = me.get(2), n32 = me.get(6), n33 = me.get(10), n34 = me.get(14);
		double n41 = me.get(3), n42 = me.get(7), n43 = me.get(11), n44 = me.get(15);

		te.set(0,  n23 * n34 * n42 - n24 * n33 * n42 + n24 * n32 * n43 - n22 * n34 * n43 - n23 * n32 * n44 + n22 * n33 * n44);
		te.set(4,  n14 * n33 * n42 - n13 * n34 * n42 - n14 * n32 * n43 + n12 * n34 * n43 + n13 * n32 * n44 - n12 * n33 * n44);
		te.set(8,  n13 * n24 * n42 - n14 * n23 * n42 + n14 * n22 * n43 - n12 * n24 * n43 - n13 * n22 * n44 + n12 * n23 * n44);
		te.set(12, n14 * n23 * n32 - n13 * n24 * n32 - n14 * n22 * n33 + n12 * n24 * n33 + n13 * n22 * n34 - n12 * n23 * n34);
		te.set(1,  n24 * n33 * n41 - n23 * n34 * n41 - n24 * n31 * n43 + n21 * n34 * n43 + n23 * n31 * n44 - n21 * n33 * n44);
		te.set(5,  n13 * n34 * n41 - n14 * n33 * n41 + n14 * n31 * n43 - n11 * n34 * n43 - n13 * n31 * n44 + n11 * n33 * n44);
		te.set(9,  n14 * n23 * n41 - n13 * n24 * n41 - n14 * n21 * n43 + n11 * n24 * n43 + n13 * n21 * n44 - n11 * n23 * n44);
		te.set(13, n13 * n24 * n31 - n14 * n23 * n31 + n14 * n21 * n33 - n11 * n24 * n33 - n13 * n21 * n34 + n11 * n23 * n34);
		te.set(2,  n22 * n34 * n41 - n24 * n32 * n41 + n24 * n31 * n42 - n21 * n34 * n42 - n22 * n31 * n44 + n21 * n32 * n44);
		te.set(6,  n14 * n32 * n41 - n12 * n34 * n41 - n14 * n31 * n42 + n11 * n34 * n42 + n12 * n31 * n44 - n11 * n32 * n44);
		te.set(10, n12 * n24 * n41 - n14 * n22 * n41 + n14 * n21 * n42 - n11 * n24 * n42 - n12 * n21 * n44 + n11 * n22 * n44);
		te.set(14, n14 * n22 * n31 - n12 * n24 * n31 - n14 * n21 * n32 + n11 * n24 * n32 + n12 * n21 * n34 - n11 * n22 * n34);
		te.set(3,  n23 * n32 * n41 - n22 * n33 * n41 - n23 * n31 * n42 + n21 * n33 * n42 + n22 * n31 * n43 - n21 * n32 * n43);
		te.set(7,  n12 * n33 * n41 - n13 * n32 * n41 + n13 * n31 * n42 - n11 * n33 * n42 - n12 * n31 * n43 + n11 * n32 * n43);
		te.set(11, n13 * n22 * n41 - n12 * n23 * n41 - n13 * n21 * n42 + n11 * n23 * n42 + n12 * n21 * n43 - n11 * n22 * n43);
		te.set(15, n12 * n23 * n31 - n13 * n22 * n31 + n13 * n21 * n32 - n11 * n23 * n32 - n12 * n21 * n33 + n11 * n22 * n33);
		
		double det = n11 * te.get( 0 ) + n21 * te.get( 4 ) + n31 * te.get( 8 ) + n41 * te.get( 12 );

		if ( det == 0 ) {

			String msg = "Matrix4.getInverse(): can't invert matrix, determinant is 0";

			Log.error(msg);
			
			this.identity();
			return this;
			
		}

		this.multiply( 1.0 / det );

		return this;

	}
	
	/**
	 * Scale the current matrix.
	 * 
	 * @param v the vector to scale the current matrix
	 */
	public void scale(Vector3 v)
	{
		double x = v.x, y = v.y, z = v.z;

		this.getArray().set(0,  (this.getArray().get(0)  * x));
		this.getArray().set(1,  (this.getArray().get(1)  * x));
		this.getArray().set(2,  (this.getArray().get(2)  * x));
		this.getArray().set(3,  (this.getArray().get(3)  * x));
		
		this.getArray().set(4,  (this.getArray().get(4)  * y));
		this.getArray().set(5,  (this.getArray().get(5)  * y));
		this.getArray().set(6,  (this.getArray().get(6)  * y));
		this.getArray().set(7,  (this.getArray().get(7)  * y));
		
		this.getArray().set(8,  (this.getArray().get(8)  * z));
		this.getArray().set(9,  (this.getArray().get(9)  * z));
		this.getArray().set(10, (this.getArray().get(10) * z));
		this.getArray().set(11, (this.getArray().get(11) * z));
	}
	
	public double getMaxScaleOnAxis()
	{
		double scaleXSq = this.getArray().get(0) * this.getArray().get(0) + this.getArray().get(1) * this.getArray().get(1) + this.getArray().get(2) * this.getArray().get(2);
		double scaleYSq = this.getArray().get(4) * this.getArray().get(4) + this.getArray().get(5) * this.getArray().get(5) + this.getArray().get(6) * this.getArray().get(6);
		double scaleZSq = this.getArray().get(8) * this.getArray().get(8) + this.getArray().get(9)	* this.getArray().get(9) + this.getArray().get(10) * this.getArray().get(10);

		return Math.sqrt( Math.max( scaleXSq, Math.max( scaleYSq, scaleZSq ) ) );
	}
	
	/**
	 * This method will make translation matrix using X, Y,Z coordinates 
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param z the Z coordinate
	 * 
	 * @return the current matrix
	 */
	public Matrix4 makeTranslation(double x, double y, double z)
	{
		this.set(
			1, 0, 0, x, 
			0, 1, 0, y, 
			0, 0, 1, z, 
			0, 0, 0, 1
		);
		
		return this;
	}

	/**
	 * The method will make rotation matrix on X-axis using defining angle theta.
	 * 
	 * @param theta the angle to make rotation matrix
	 * 
	 * @return the current matrix
	 */
	public Matrix4 makeRotationX(double theta)
	{
		double c = Math.cos(theta), s = Math.sin(theta);

		this.set(
			1, 0, 0, 0, 
			0, c,-s, 0, 
			0, s, c, 0, 
			0, 0, 0, 1
		);
		
		return this;
	}

	/**
	 * The method will make rotation matrix on Y-axis using defining angle theta.
	 * 
	 * @param theta the angle to make rotation matrix
	 * 
	 * @return the current matrix
	 */
	public Matrix4 makeRotationY(double theta)
	{
		double c = Math.cos(theta), s = Math.sin(theta);

		this.set(
			c,  0, s, 0, 
			0,  1, 0, 0, 
			-s, 0, c, 0, 
			 0, 0, 0, 1
		);
		
		return this;
	}

	/**
	 * The method will make rotation matrix on Z-axis using defining angle theta.
	 * 
	 * @param theta the angle to make rotation matrix
	 * 
	 * @return the current matrix
	 */
	public Matrix4 makeRotationZ(double theta)
	{
		double c = Math.cos(theta), s = Math.sin(theta);

		this.set(
			c, -s, 0, 0, 
			s,  c, 0, 0, 
			0,  0, 1, 0, 
			0,  0, 0, 1
		);

		return this;
	}

	/**
	 * The method will make rotation matrix on XYZ-axis using defining angle theta.
	 * 
	 * @param axis  the axis on which rotate the matrix
	 * @param angle the angle to make rotation matrix
	 * 
	 * @return the current matrix
	 */
	public Matrix4 makeRotationAxis(Vector3 axis, double angle)
	{
		// Based on http://www.gamedev.net/reference/articles/article1199.asp

		double c = Math.cos(angle);
		double s = Math.sin(angle);
		double t = 1.0 - c;
		double x = axis.getX(), y = axis.getY(), z = axis.getZ();
		double tx = t * x, ty = t * y;

		this.set(
			(tx * x + c),     (tx * y - s * z), (tx * z + s * y), 0, 
			(tx * y + s * z),     (ty * y + c), (ty * z - s * x), 0, 
			(tx * z - s * y), (ty * z + s * x),	(t * z * z + c),  0,
			               0,                0,               0,  1
		);
		
		return this;
	}

	/**
	 * Make a scaled matrix on the X, Y, Z coordinates.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * 
	 * @return the current matrix
	 */
	public Matrix4 makeScale(double x, double y, double z)
	{
		return this.set(
			x, 0, 0, 0, 
			0, y, 0, 0, 
			0, 0, z, 0, 
			0, 0, 0, 1
		);
	}

	public Matrix4 compose(Vector3 position, Quaternion quaternion, Vector3 scale)
	{
		this.makeRotationFromQuaternion( quaternion );
		this.scale( scale );
		this.setPosition( position );

		return this;
	}

	public Matrix4 decompose(Vector3 position, Quaternion quaternion, Vector3 scale)
	{
		Float32Array te = this.elements;

		double sx = _vector.set( te.get( 0 ), te.get( 1 ), te.get( 2 ) ).length();
		double sy = _vector.set( te.get( 4 ), te.get( 5 ), te.get( 6 ) ).length();
		double sz = _vector.set( te.get( 8 ), te.get( 9 ), te.get( 10 ) ).length();

		// if determine is negative, we need to invert one scale
		double det = this.determinant();
		if ( det < 0 ) {
			sx = - sx;
		}

		position.x = te.get( 12 );
		position.y = te.get( 13 );
		position.z = te.get( 14 );

		// scale the rotation part

		_matrix.elements.set( this.elements ); // at this point matrix is incomplete so we can't use .copy()

		double invSX = 1.0 / sx;
		double invSY = 1.0 / sy;
		double invSZ = 1.0 / sz;

		_matrix.elements.set( 0, _matrix.elements.get( 0 ) * invSX );
		_matrix.elements.set( 1, _matrix.elements.get( 1 ) * invSX );
		_matrix.elements.set( 2, _matrix.elements.get( 2 ) * invSX );

		_matrix.elements.set( 4, _matrix.elements.get( 4 ) * invSY );
		_matrix.elements.set( 5, _matrix.elements.get( 5 ) * invSY );
		_matrix.elements.set( 6, _matrix.elements.get( 6 ) * invSY );

		_matrix.elements.set( 8, _matrix.elements.get( 8 ) * invSZ );
		_matrix.elements.set( 9, _matrix.elements.get( 9 ) * invSZ );
		_matrix.elements.set( 10, _matrix.elements.get( 10 ) * invSZ );

		quaternion.setFromRotationMatrix( _matrix );

		scale.x = sx;
		scale.y = sy;
		scale.z = sz;

		return this;
	}

	/**
	 * Creates a frustum matrix.
	 */
	public Matrix4 makeFrustum(double left, double right, double bottom, double top, double near, double far)
	{
		Float32Array te = this.getArray();
		double x = 2.0 * near / ( right - left );
		double y = 2.0 * near / ( top - bottom );

		double a = ( right + left ) / ( right - left );
		double b = ( top + bottom ) / ( top - bottom );
		double c = - ( far + near ) / ( far - near );
		double d = - 2.0 * far * near / ( far - near );

		te.set(0, x);  te.set(4, 0);  te.set(8, a);    te.set(12, 0);
		te.set(1, 0);  te.set(5, y);   te.set(9, b);   te.set(13, 0);
		te.set(2, 0);  te.set(6, 0);  te.set(10, c);   te.set(14, d);
		te.set(3, 0);  te.set(7, 0);  te.set(11, - 1); te.set(15, 0);

		return this;
	}

	/**
	 * Making Perspective Projection Matrix
	 * 
	 * @param fov    the field Of View
	 * @param aspect the aspect ration
	 * @param near   the near value
	 * @param far    the far value
	 * 
	 * @return the current Projection Matrix
	 */
	public Matrix4 makePerspective(double fov, double aspect, double near, double far)
	{
		double ymax = near * Math.tan( Mathematics.degToRad( fov * 0.5 ) );
		double ymin = - ymax;
		double xmin = ymin * aspect;
		double xmax = ymax * aspect;

		return this.makeFrustum( xmin, xmax, ymin, ymax, near, far );
	}

	/**
	 * Making Orthographic Projection Matrix
	 * 
	 * @return the current Projection Matrix
	 */
	public Matrix4 makeOrthographic(double left, double right, double top, double bottom, double near, double far)
	{
		Float32Array te = this.elements;
		double w = right - left;
		double h = top - bottom;
		double p = far - near;

		double x = ( right + left ) / w;
		double y = ( top + bottom ) / h;
		double z = ( far + near )   / p;

		te.set(0, 2.0 / w); te.set(4, 0.0);     te.set(8, 0.0);       te.set(12, -x);
		te.set(1, 0.0);     te.set(5, 2.0 / h); te.set(9, 0.0);       te.set(13, -y);
		te.set(2, 0.0);     te.set(6, 0.0);     te.set(10, -2.0 / p); te.set(14, -z);
		te.set(3, 0.0);     te.set(7, 0.0);     te.set(11, 0.0);      te.set(15, 1.0);

		return this;
	}

	/**
	 * Clone the current matrix.
	 * {@code matrix.clone() != matrix}
	 * 
	 * @return the new instance of matrix
	 */
	public Matrix4 clone()
	{
		Float32Array te = this.getArray();

		return new Matrix4(
			te.get(0), te.get(4), te.get(8), te.get(12), 
			te.get(1), te.get(5), te.get(9), te.get(13), 
			te.get(2), te.get(6), te.get(10), te.get(14), 
			te.get(3), te.get(7), te.get(11), te.get(15)
		);

	}
	
	/**
	 * get matrix information by printing list of matrix's elements.
	 */
	public String toString() 
	{
		String retval = "[";
		
		for(int i = 0; i < this.getArray().getLength(); i++)
			retval += this.getArray().get(i) + ", ";
		
		return retval + "]";
	}
}
