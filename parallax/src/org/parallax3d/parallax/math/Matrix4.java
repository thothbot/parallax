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

import org.parallax3d.parallax.Parallax;
import org.parallax3d.parallax.system.ThreeJsObject;

import java.nio.FloatBuffer;

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
@ThreeJsObject("THREE.Matrix4")
public class Matrix4
{
	final float elements[] = new float[16];
	
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
	public Matrix4(float n11, float n12, float n13, float n14, 
			float n21, float n22, float n23, float n24, 
			float n31, float n32, float n33, float n34, 
			float n41, float n42, float n43, float n44) 
	{
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
	public float[] getArray() 
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
			float n11, float n12, float n13, float n14, 
			float n21, float n22, float n23, float n24, 
			float n31, float n32, float n33, float n34, 
			float n41, float n42, float n43, float n44)
	{
		this.elements[0] = n11;
		this.elements[1] = n21;
		this.elements[2] = n31;
		this.elements[3] = n41;
		
		this.elements[4] = n12;
		this.elements[5] = n22;
		this.elements[6] = n32;
		this.elements[7] = n42;
		
		this.elements[8] = n13;
		this.elements[9] = n23;
		this.elements[10] = n33;
		this.elements[11] = n43;
		
		this.elements[12] = n14;
		this.elements[13] = n24;
		this.elements[14] = n34;
		this.elements[15] = n44;
		
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
		float[] me = m.getArray();
		return set(
			me[0], me[4], me[8],  me[12],
			me[1], me[5], me[9],  me[13],
			me[2], me[6], me[10], me[14],
			me[3], me[7], me[11], me[15]
		);
	}
	
	public Matrix4 copyPosition( Matrix4 m ) {

		float[] te = this.elements;
		float[] me = m.getArray();

		te[12] = me[12];
		te[13] = me[13];
		te[14] = me[14];

		return this;
	}
	
	
	/**
	 * Setting rotation values to the rotation values of the input matrix.
	 * 
	 * @param m the input matrix
	 */
	public Matrix4 extractRotation(Matrix4 m)
	{
		float[] me = m.getArray();

		Vector3 v1 = new Vector3();

		float scaleX = 1.0f / v1.set(me[0], me[1], me[2]).length();
		float scaleY = 1.0f / v1.set(me[4], me[5], me[6]).length();
		float scaleZ = 1.0f / v1.set(me[8], me[9], me[10]).length();

		this.elements[0] = me[0] * scaleX;
		this.elements[1] = me[1] * scaleX;
		this.elements[2] = me[2] * scaleX;

		this.elements[4] = me[4] * scaleY;
		this.elements[5] = me[5] * scaleY;
		this.elements[6] = me[6] * scaleY;

		this.elements[8] = me[8] * scaleZ;
		this.elements[9] = me[9] * scaleZ;
		this.elements[10] = me[10] * scaleZ;
		
		return this;
	}
	
	public Matrix4 makeRotationFromEuler( Euler euler ) {

		float[] te = this.elements;

		float x = euler.getX(), y = euler.getY(), z = euler.getZ();
		float a = (float)Math.cos( x ), b = (float)Math.sin( x );
		float c = (float)Math.cos( y ), d = (float)Math.sin( y );
		float e = (float)Math.cos( z ), f = (float)Math.sin( z );

		if ( euler.getOrder().equals("XYZ") ) {

			float ae = a * e, af = a * f, be = b * e, bf = b * f;

			te[0] =  c * e;
			te[4] =  - c * f;
			te[8] =  d;

			te[1] =  af + be * d;
			te[5] =  ae - bf * d;
			te[9] =  - b * c;

			te[2] =  bf - ae * d;
			te[6] =  be + af * d;
			te[10] =  a * c;

		} else if ( euler.getOrder().equals("YXZ") ) {

			float ce = c * e, cf = c * f, de = d * e, df = d * f;

			te[0] =  ce + df * b;
			te[4] =  de * b - cf;
			te[8] =  a * d;

			te[1] =  a * f;
			te[5] =  a * e;
			te[9] =  - b;

			te[2] =  cf * b - de;
			te[6] =  df + ce * b;
			te[10] =  a * c;

		} else if ( euler.getOrder().equals("ZXY") ) {

			float ce = c * e, cf = c * f, de = d * e, df = d * f;

			te[0] =  ce - df * b;
			te[4] =  - a * f;
			te[8] =  de + cf * b;

			te[1] =  cf + de * b;
			te[5] =  a * e;
			te[9] =  df - ce * b;

			te[2] =  - a * d;
			te[6] =  b;
			te[10] =  a * c;

		} else if ( euler.getOrder().equals("ZYX") ) {

			float ae = a * e, af = a * f, be = b * e, bf = b * f;

			te[0] =  c * e;
			te[4] =  be * d - af;
			te[8] =  ae * d + bf;

			te[1] =  c * f;
			te[5] =  bf * d + ae;
			te[9] =  af * d - be;

			te[2] =  - d;
			te[6] =  b * c;
			te[10] =  a * c;

		} else if ( euler.getOrder().equals("YZX") ) {

			float ac = a * c, ad = a * d, bc = b * c, bd = b * d;

			te[0] =  c * e;
			te[4] =  bd - ac * f;
			te[8] =  bc * f + ad;

			te[1] =  f;
			te[5] =  a * e;
			te[9] =  - b * e;

			te[2] =  - d * e;
			te[6] =  ad * f + bc;
			te[10] =  ac - bd * f;

		} else if ( euler.getOrder().equals("XZY") ) {

			float ac = a * c, ad = a * d, bc = b * c, bd = b * d;

			te[0] =  c * e;
			te[4] =  - f;
			te[8] =  d * e;

			te[1] =  ac * f + bd;
			te[5] =  a * e;
			te[9] =  ad * f - bc;

			te[2] =  bc * f - ad;
			te[6] =  b * e;
			te[10] =  bd * f + ac;

		}

		// last column
		te[3] =  0;
		te[7] =  0;
		te[11] =  0;

		// bottom row
		te[12] =  0;
		te[13] =  0;
		te[14] =  0;
		te[15] =  1.0f;

		return this;
	}

	public Matrix4 makeRotationFromQuaternion( Quaternion q ) {

		float[] te = this.elements;

		float x = q.getX(), y = q.getY(), z = q.getZ(), w = q.getW();
		float x2 = x + x, y2 = y + y, z2 = z + z;
		float xx = x * x2, xy = x * y2, xz = x * z2;
		float yy = y * y2, yz = y * z2, zz = z * z2;
		float wx = w * x2, wy = w * y2, wz = w * z2;

		te[0] =  1.0f - ( yy + zz );
		te[4] =  xy - wz;
		te[8] =  xz + wy;

		te[1] =  xy + wz;
		te[5] =  1.0f - ( xx + zz );
		te[9] =  yz - wx;

		te[2] =  xz - wy;
		te[6] =  yz + wx;
		te[10] =  1.0f - ( xx + yy );

		// last column
		te[3] =  0;
		te[7] =  0;
		te[11] =  0;

		// bottom row
		te[12] =  0;
		te[13] =  0;
		te[14] =  0;
		te[15] =  1.0f;

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
		float[] te = this.elements;

		_z.sub( eye, target ).normalize();

		if ( _z.length() == 0 ) {

			_z.z = 1.0f;

		}

		_x.cross( up, _z ).normalize();

		if ( _x.length() == 0 ) {

			_z.x += 0.0001;
			_x.cross( up, _z ).normalize();

		}

		_y.cross( _z, _x );


		te[0] = _x.getX(); te[4] = _y.getX(); te[8] = _z.getX();
		te[1] = _x.getY(); te[5] = _y.getY(); te[9] = _z.getY();
		te[2] = _x.getZ(); te[6] = _y.getZ(); te[10] = _z.getZ();

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
		float[] ae = m1.getArray();
		float[] be = m2.getArray();

		float a11 = ae[0], a12 = ae[4], a13 = ae[8], a14 = ae[12];
		float a21 = ae[1], a22 = ae[5], a23 = ae[9], a24 = ae[13];
		float a31 = ae[2], a32 = ae[6], a33 = ae[10], a34 = ae[14];
		float a41 = ae[3], a42 = ae[7], a43 = ae[11], a44 = ae[15];

		float b11 = be[0], b12 = be[4], b13 = be[8], b14 = be[12];
		float b21 = be[1], b22 = be[5], b23 = be[9], b24 = be[13];
		float b31 = be[2], b32 = be[6], b33 = be[10], b34 = be[14];
		float b41 = be[3], b42 = be[7], b43 = be[11], b44 = be[15];

		this.elements[0] = (a11 * b11 + a12 * b21 + a13 * b31 + a14 * b41);
		this.elements[4] = (a11 * b12 + a12 * b22 + a13 * b32 + a14 * b42);
		this.elements[8] = (a11 * b13 + a12 * b23 + a13 * b33 + a14 * b43);
		this.elements[12] = (a11 * b14 + a12 * b24 + a13 * b34 + a14 * b44);

		this.elements[1] = (a21 * b11 + a22 * b21 + a23 * b31 + a24 * b41);
		this.elements[5] = (a21 * b12 + a22 * b22 + a23 * b32 + a24 * b42);
		this.elements[9] = (a21 * b13 + a22 * b23 + a23 * b33 + a24 * b43);
		this.elements[13] = (a21 * b14 + a22 * b24 + a23 * b34 + a24 * b44);

		this.elements[2] = (a31 * b11 + a32 * b21 + a33 * b31 + a34 * b41);
		this.elements[6] = (a31 * b12 + a32 * b22 + a33 * b32 + a34 * b42);
		this.elements[10] = (a31 * b13 + a32 * b23 + a33 * b33 + a34 * b43);
		this.elements[14] = (a31 * b14 + a32 * b24 + a33 * b34 + a34 * b44);

		this.elements[3] = (a41 * b11 + a42 * b21 + a43 * b31 + a44 * b41);
		this.elements[7] = (a41 * b12 + a42 * b22 + a43 * b32 + a44 * b42);
		this.elements[11] = (a41 * b13 + a42 * b23 + a43 * b33 + a44 * b43);
		this.elements[15] = (a41 * b14 + a42 * b24 + a43 * b34 + a44 * b44);

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
	public Matrix4 multiply(float s)
	{
		this.elements[0] *= s;
		this.elements[4] *= s;
		this.elements[8] *= s;
		this.elements[12] *= s;
		this.elements[1] *= s;
		this.elements[5] *= s;
		this.elements[9] *= s;
		this.elements[13] *= s;
		this.elements[2] *= s;
		this.elements[6] *= s;
		this.elements[10] *= s;
		this.elements[14] *= s;
		this.elements[3] *= s;
		this.elements[7] *= s;
		this.elements[11] *= s;
		this.elements[15] *= s;
		
		return this;
	}
	
	public FloatBuffer applyToVector3Array (FloatBuffer array)
	{
		return applyToVector3Array(array, 0, array.array().length);
	}

	public FloatBuffer applyToVector3Array (FloatBuffer array, int offset, int length)
	{
		for ( int i = 0, j = offset, il; i < length; i += 3, j += 3 ) {

			_v1.x = array.get( j );
			_v1.y = array.get( j + 1 );
			_v1.z = array.get( j + 2 );

			_v1.apply( this );

			array.put( j , _v1.x);
			array.put( j + 1 , _v1.y);
			array.put( j + 2 , _v1.z);

		}

		return array;
	}

	/**
	 * get the current matrix determinant.
	 * 
	 * @return the matrix determinant
	 */
	public float determinant()
	{
		float n11 = this.elements[0], n12 = this.elements[4], n13 = this.elements[8],  n14 = this.elements[12];
		float n21 = this.elements[1], n22 = this.elements[5], n23 = this.elements[9],  n24 = this.elements[13];
		float n31 = this.elements[2], n32 = this.elements[6], n33 = this.elements[10], n34 = this.elements[14];
		float n41 = this.elements[3], n42 = this.elements[7], n43 = this.elements[11], n44 = this.elements[15];

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
		float[] te = this.elements;
		float tmp;

		tmp = te[1]; te[1] =  te[4]; te[4] =  tmp;
		tmp = te[2]; te[2] =  te[8]; te[8] =  tmp;
		tmp = te[6]; te[6] =  te[9]; te[9] =  tmp;

		tmp = te[3]; te[3] =  te[12]; te[12] =  tmp;
		tmp = te[7]; te[7] =  te[13]; te[13] =  tmp;
		tmp = te[11]; te[11] =  te[14]; te[14] =  tmp;

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
	public FloatBuffer flattenToArrayOffset(FloatBuffer flat)
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
	public FloatBuffer flattenToArrayOffset(FloatBuffer flat, int offset)
	{
		flat.put(offset, this.elements[0]);
		flat.put(offset + 1, this.elements[1]);
		flat.put(offset + 2, this.elements[2]);
		flat.put(offset + 3, this.elements[3]);

		flat.put(offset + 4, this.elements[4]);
		flat.put(offset + 5, this.elements[5]);
		flat.put(offset + 6, this.elements[6]);
		flat.put(offset + 7, this.elements[7]);

		flat.put(offset + 8, this.elements[8]);
		flat.put(offset + 9, this.elements[9]);
		flat.put(offset + 10, this.elements[10]);
		flat.put(offset + 11, this.elements[11]);

		flat.put(offset + 12, this.elements[12]);
		flat.put(offset + 13, this.elements[13]);
		flat.put(offset + 14, this.elements[14]);
		flat.put(offset + 15, this.elements[15]);

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
		this.elements[12] = v.getX();
		this.elements[13] = v.getY();
		this.elements[14] = v.getZ();

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
		float[] te = this.elements;
		float[] me = m.getArray();

		float n11 = me[0], n12 = me[4], n13 = me[8],  n14 = me[12];
		float n21 = me[1], n22 = me[5], n23 = me[9],  n24 = me[13];
		float n31 = me[2], n32 = me[6], n33 = me[10], n34 = me[14];
		float n41 = me[3], n42 = me[7], n43 = me[11], n44 = me[15];

		te[0] =   n23 * n34 * n42 - n24 * n33 * n42 + n24 * n32 * n43 - n22 * n34 * n43 - n23 * n32 * n44 + n22 * n33 * n44;
		te[4] =   n14 * n33 * n42 - n13 * n34 * n42 - n14 * n32 * n43 + n12 * n34 * n43 + n13 * n32 * n44 - n12 * n33 * n44;
		te[8] =   n13 * n24 * n42 - n14 * n23 * n42 + n14 * n22 * n43 - n12 * n24 * n43 - n13 * n22 * n44 + n12 * n23 * n44;
		te[12] =  n14 * n23 * n32 - n13 * n24 * n32 - n14 * n22 * n33 + n12 * n24 * n33 + n13 * n22 * n34 - n12 * n23 * n34;
		te[1] =   n24 * n33 * n41 - n23 * n34 * n41 - n24 * n31 * n43 + n21 * n34 * n43 + n23 * n31 * n44 - n21 * n33 * n44;
		te[5] =   n13 * n34 * n41 - n14 * n33 * n41 + n14 * n31 * n43 - n11 * n34 * n43 - n13 * n31 * n44 + n11 * n33 * n44;
		te[9] =   n14 * n23 * n41 - n13 * n24 * n41 - n14 * n21 * n43 + n11 * n24 * n43 + n13 * n21 * n44 - n11 * n23 * n44;
		te[13] =  n13 * n24 * n31 - n14 * n23 * n31 + n14 * n21 * n33 - n11 * n24 * n33 - n13 * n21 * n34 + n11 * n23 * n34;
		te[2] =   n22 * n34 * n41 - n24 * n32 * n41 + n24 * n31 * n42 - n21 * n34 * n42 - n22 * n31 * n44 + n21 * n32 * n44;
		te[6] =   n14 * n32 * n41 - n12 * n34 * n41 - n14 * n31 * n42 + n11 * n34 * n42 + n12 * n31 * n44 - n11 * n32 * n44;
		te[10] =  n12 * n24 * n41 - n14 * n22 * n41 + n14 * n21 * n42 - n11 * n24 * n42 - n12 * n21 * n44 + n11 * n22 * n44;
		te[14] =  n14 * n22 * n31 - n12 * n24 * n31 - n14 * n21 * n32 + n11 * n24 * n32 + n12 * n21 * n34 - n11 * n22 * n34;
		te[3] =   n23 * n32 * n41 - n22 * n33 * n41 - n23 * n31 * n42 + n21 * n33 * n42 + n22 * n31 * n43 - n21 * n32 * n43;
		te[7] =   n12 * n33 * n41 - n13 * n32 * n41 + n13 * n31 * n42 - n11 * n33 * n42 - n12 * n31 * n43 + n11 * n32 * n43;
		te[11] =  n13 * n22 * n41 - n12 * n23 * n41 - n13 * n21 * n42 + n11 * n23 * n42 + n12 * n21 * n43 - n11 * n22 * n43;
		te[15] =  n12 * n23 * n31 - n13 * n22 * n31 + n13 * n21 * n32 - n11 * n23 * n32 - n12 * n21 * n33 + n11 * n22 * n33;
		
		float det = n11 * te[ 0 ] + n21 * te[ 4 ] + n31 * te[ 8 ] + n41 * te[ 12 ];

		if ( det == 0 ) {

			Parallax.app.error("Matrix4", "Matrix4.getInverse(): can't invert matrix, determinant is 0");
			
			this.identity();
			return this;
			
		}

		this.multiply( 1.0f / det );

		return this;

	}
	
	/**
	 * Scale the current matrix.
	 * 
	 * @param v the vector to scale the current matrix
	 */
	public void scale(Vector3 v)
	{
		float x = v.x, y = v.y, z = v.z;

		this.elements[0]  *= x;
		this.elements[1]  *= x;
		this.elements[2]  *= x;
		this.elements[3]  *= x;
		
		this.elements[4]  *= y;
		this.elements[5]  *= y;
		this.elements[6]  *= y;
		this.elements[7]  *= y;
		
		this.elements[8]  *= z;
		this.elements[9]  *= z;
		this.elements[10] *= z;
		this.elements[11] *= z;
	}
	
	public float getMaxScaleOnAxis()
	{
		float scaleXSq = this.elements[0] * this.elements[0] + this.elements[1] * this.elements[1] + this.elements[2] * this.elements[2];
		float scaleYSq = this.elements[4] * this.elements[4] + this.elements[5] * this.elements[5] + this.elements[6] * this.elements[6];
		float scaleZSq = this.elements[8] * this.elements[8] + this.elements[9]	* this.elements[9] + this.elements[10] * this.elements[10];

		return (float)Math.sqrt( Math.max( scaleXSq, Math.max( scaleYSq, scaleZSq ) ) );
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
	public Matrix4 makeTranslation(float x, float y, float z)
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
	public Matrix4 makeRotationX(float theta)
	{
		float c = (float)Math.cos(theta), s = (float)Math.sin(theta);

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
	public Matrix4 makeRotationY(float theta)
	{
		float c = (float)Math.cos(theta), s = (float)Math.sin(theta);

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
	public Matrix4 makeRotationZ(float theta)
	{
		float c = (float)Math.cos(theta), s = (float)Math.sin(theta);

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
	public Matrix4 makeRotationAxis(Vector3 axis, float angle)
	{
		// Based on http://www.gamedev.net/reference/articles/article1199.asp

		float c = (float)Math.cos(angle);
		float s = (float)Math.sin(angle);
		float t = 1.0f - c;
		float x = axis.getX(), y = axis.getY(), z = axis.getZ();
		float tx = t * x, ty = t * y;

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
	public Matrix4 makeScale(float x, float y, float z)
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
		float[] te = this.elements;

		float sx = _vector.set( te[0], te[1], te[2] ).length();
		float sy = _vector.set( te[4], te[5], te[6] ).length();
		float sz = _vector.set( te[8], te[9], te[10] ).length();

		// if determine is negative, we need to invert one scale
		float det = this.determinant();
		if ( det < 0 ) {
			sx = - sx;
		}

		position.x = te[12];
		position.y = te[13];
		position.z = te[14];

		// scale the rotation part

		// at this point matrix is incomplete so we can't use .copy()
		for(int i = 0, l = this.elements.length; i < l; i++ )
			_matrix.elements[i] = this.elements[i];


		float invSX = 1.0f / sx;
		float invSY = 1.0f / sy;
		float invSZ = 1.0f / sz;

		_matrix.elements[ 0 ] *= invSX;
		_matrix.elements[ 1 ] *= invSX;
		_matrix.elements[ 2 ] *= invSX;

		_matrix.elements[ 4 ] *= invSY;
		_matrix.elements[ 5 ] *= invSY;
		_matrix.elements[ 6 ] *= invSY;

		_matrix.elements[ 8 ] *= invSZ;
		_matrix.elements[ 9 ] *= invSZ;
		_matrix.elements[ 10 ] *= invSZ;

		quaternion.setFromRotationMatrix( _matrix );

		scale.x = sx;
		scale.y = sy;
		scale.z = sz;

		return this;
	}

	/**
	 * Creates a frustum matrix.
	 */
	public Matrix4 makeFrustum(float left, float right, float bottom, float top, float near, float far)
	{
		float[] te = this.elements;
		float x = 2.0f * near / ( right - left );
		float y = 2.0f * near / ( top - bottom );

		float a = ( right + left ) / ( right - left );
		float b = ( top + bottom ) / ( top - bottom );
		float c = - ( far + near ) / ( far - near );
		float d = - 2.0f * far * near / ( far - near );

		te[0] =  x;  te[4] =  0;  te[8] =  a;    te[12] =  0;
		te[1] =  0;  te[5] =  y;  te[9] =  b;   te[13] =  0;
		te[2] =  0;  te[6] =  0;  te[10] =  c;   te[14] =  d;
		te[3] =  0;  te[7] =  0;  te[11] =  - 1; te[15] =  0;

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
	public Matrix4 makePerspective(float fov, float aspect, float near, float far)
	{
		float ymax = near * (float)Math.tan( Mathematics.degToRad( fov * 0.5f ) );
		float ymin = - ymax;
		float xmin = ymin * aspect;
		float xmax = ymax * aspect;

		return this.makeFrustum( xmin, xmax, ymin, ymax, near, far );
	}

	/**
	 * Making Orthographic Projection Matrix
	 * 
	 * @return the current Projection Matrix
	 */
	public Matrix4 makeOrthographic(float left, float right, float top, float bottom, float near, float far)
	{
		float[] te = this.elements;
		float w = right - left;
		float h = top - bottom;
		float p = far - near;

		float x = ( right + left ) / w;
		float y = ( top + bottom ) / h;
		float z = ( far + near )   / p;

		te[0] =  2.0f / w; te[4] =  0.0f;     te[8] =  0.0f;       te[12] =  -x;
		te[1] =  0.0f;     te[5] =  2.0f / h; te[9] =  0.0f;       te[13] =  -y;
		te[2] =  0.0f;     te[6] =  0.0f;     te[10] =  -2.0f / p; te[14] =  -z;
		te[3] =  0.0f;     te[7] =  0.0f;     te[11] =  0.0f;      te[15] =  1.0f;

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
		float[] te = this.elements;

		return new Matrix4(
			te[0], te[4], te[8], te[12],
			te[1], te[5], te[9], te[13],
			te[2], te[6], te[10], te[14],
			te[3], te[7], te[11], te[15]
		);

	}
	
	/**
	 * get matrix information by printing list of matrix's elements.
	 */
	public String toString() 
	{
		String retval = "[";
		
		for(int i = 0, l = this.elements.length; i < l; i++)
			retval += this.elements[i] + ", ";
		
		return retval + "]";
	}
}
