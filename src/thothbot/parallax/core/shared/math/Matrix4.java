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
import thothbot.parallax.core.shared.core.Euler;

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

	/**
	 * The first matrix column
	 */
	private static Vector3 __v1 = new Vector3();
	
	/**
	 * The second matrix column
	 */
	private static Vector3 __v2 = new Vector3();
	
	/**
	 * The third matrix column
	 */
	private static Vector3 __v3 = new Vector3();

	private static Matrix4 __m1 = new Matrix4();
	private static Matrix4 __m2 = new Matrix4();

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
	 * Returns the vector of the first matrix column. 
	 * 
	 * @return the vector
	 */
	public Vector3 getColumnX()
	{
		return Matrix4.__v1.set(this.getArray().get(0), this.getArray().get(1), this.getArray().get(2));
	}

	/**
	 * Returns the vector of the second matrix column. 
	 * 
	 * @return the vector
	 */
	public Vector3 getColumnY()
	{
		return Matrix4.__v1.set(this.getArray().get(4), this.getArray().get(5), this.getArray().get(6));
	}

	/**
	 * Returns the vector of the third matrix column. 
	 * 
	 * @return the vector
	 */
	public Vector3 getColumnZ()
	{
		return Matrix4.__v1.set(this.getArray().get(8), this.getArray().get(9), this.getArray().get(10));
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
	
	/**
	 * Sets the value of input vector to the matrix-vector multiplication of itself and
	 * vector v.
	 * {@code (this = this * v)}
	 * 
	 * @param v the input vector
	 * 
	 * @return the multiplication input vector
	 */
	public Vector3 multiplyVector3(Vector3 v)
	{
		Float32Array te = this.getArray();
	
		double vx = v.getX();
		double vy = v.getY();
		double vz = v.getZ();
		double d = 1.0 / ( te.get(3) * vx + te.get(7) * vy + te.get(11) * vz + te.get(15) );

		v.setX( ( te.get(0) * vx + te.get(4) * vy + te.get(8)  * vz + te.get(12) ) * d );
		v.setY( ( te.get(1) * vx + te.get(5) * vy + te.get(9)  * vz + te.get(13) ) * d );
		v.setZ( ( te.get(2) * vx + te.get(6) * vy + te.get(10) * vz + te.get(14) ) * d );

		return v;
	}

	/**
	 * Sets the value of input vector to the matrix-vector multiplication of itself and
	 * vector v.
	 * (this = this * v)
	 * 
	 * @param v the input vector
	 * 
	 * @return the multiplication input vector
	 */
	public Vector4 multiplyVector4(Vector4 v)
	{
		Float32Array te = this.getArray();
		double vx = v.x, vy = v.y, vz = v.z, vw = v.w;

		v.setX( te.get(0) * vx + te.get(4) * vy + te.get(8) * vz + te.get(12) * vw );
		v.setY( te.get(1) * vx + te.get(5) * vy + te.get(9) * vz + te.get(13) * vw );
		v.setZ( te.get(2) * vx + te.get(6) * vy + te.get(10) * vz + te.get(14) * vw );
		v.setW( te.get(3) * vx + te.get(7) * vy + te.get(11) * vz + te.get(15) * vw );

		return v;
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
		Float32Array te = this.getArray();

		Vector3 x = Matrix4.__v1;
		Vector3 y = Matrix4.__v2;
		Vector3 z = Matrix4.__v3;

		z.sub( eye, target ).normalize();

		if ( z.length() == 0 )
			z.setZ(1);

		x.cross( up, z ).normalize();

		if ( x.length() == 0 ) 
		{
			z.addX(0.0001);
			x.cross( up, z ).normalize();
		}

		y.cross( z, x );

		te.set(0, x.getX()); te.set(4, y.getX()); te.set(8,  z.getX());
		te.set(1, x.getY()); te.set(5, y.getY()); te.set(9,  z.getY());
		te.set(2, x.getZ()); te.set(6, y.getZ()); te.set(10, z.getZ());

		return this;
	}

	/**
	 * get input vector and rotate it by the current matrix.
	 *  
	 * @param v the input vector
	 * 
	 * @return the rotated vector
	 */
	public Vector3 rotateAxis(Vector3 v)
	{
		double vx = v.getX(), vy = v.getY(), vz = v.getZ();

		v.setX(this.getArray().get(0) * vx + this.getArray().get(4) * vy + this.getArray().get(8) * vz);
		v.setY(this.getArray().get(1) * vx + this.getArray().get(5) * vy + this.getArray().get(9) * vz);
		v.setZ(this.getArray().get(2) * vx + this.getArray().get(6) * vy + this.getArray().get(10) * vz);

		v.normalize();
		return v;
	}

	/**
	 * The cross product of vectors a and he current matrix
	 * 
	 * @param a the vector
	 * 
	 * @return the modified vector
	 */
	public Vector4 crossVector(Vector4 a)
	{
		Float32Array te = this.getArray();
		Vector4 v = new Vector4();

		v.x = te.get(0) * a.x + te.get(4) * a.y + te.get(8) * a.z + te.get(12) * a.w;
		v.y = te.get(1) * a.x + te.get(5) * a.y + te.get(9) * a.z + te.get(13) * a.w;
		v.z = te.get(2) * a.x + te.get(6) * a.y + te.get(10) * a.z + te.get(14) * a.w;

		v.w = ( a.w > 0 ) ? te.get(3) * a.x + te.get(7) * a.y + te.get(11) * a.z + te.get(15) * a.w : 1;

		return v;
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

		// cofactor exapaimsiom along first row

		return (
			n14 * n23 * n32 * n41-
			n13 * n24 * n32 * n41-
			n14 * n22 * n33 * n41+
			n12 * n24 * n33 * n41+

			n13 * n22 * n34 * n41-
			n12 * n23 * n34 * n41-
			n14 * n23 * n31 * n42+
			n13 * n24 * n31 * n42+

			n14 * n21 * n33 * n42-
			n11 * n24 * n33 * n42-
			n13 * n21 * n34 * n42+
			n11 * n23 * n34 * n42+

			n14 * n22 * n31 * n43-
			n12 * n24 * n31 * n43-
			n14 * n21 * n32 * n43+
			n11 * n24 * n32 * n43+

			n12 * n21 * n34 * n43-
			n11 * n22 * n34 * n43-
			n13 * n22 * n31 * n44+
			n12 * n23 * n31 * n44+

			n13 * n21 * n32 * n44-
			n11 * n23 * n32 * n44-
			n12 * n21 * n33 * n44+
			n11 * n22 * n33 * n44
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
	public Float32Array flattenToArray(Float32Array flat)
	{
		return flattenToArray(flat, 0);
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
	public Float32Array flattenToArray(Float32Array flat, int offset)
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
	 * get position vector from the current matrix.
	 * 
	 * @return the position vector 
	 */
	public Vector3 getPosition()
	{
		return Matrix4.__v1.set(this.getArray().get(12), this.getArray().get(13), this.getArray().get(14));
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

		te.set(0, n23*n34*n42 - n24*n33*n42 + n24*n32*n43 - n22*n34*n43 - n23*n32*n44 + n22*n33*n44);
		te.set(4, n14*n33*n42 - n13*n34*n42 - n14*n32*n43 + n12*n34*n43 + n13*n32*n44 - n12*n33*n44);
		te.set(8, n13*n24*n42 - n14*n23*n42 + n14*n22*n43 - n12*n24*n43 - n13*n22*n44 + n12*n23*n44);
		te.set(12, n14*n23*n32 - n13*n24*n32 - n14*n22*n33 + n12*n24*n33 + n13*n22*n34 - n12*n23*n34);
		te.set(1, n24*n33*n41 - n23*n34*n41 - n24*n31*n43 + n21*n34*n43 + n23*n31*n44 - n21*n33*n44);
		te.set(5, n13*n34*n41 - n14*n33*n41 + n14*n31*n43 - n11*n34*n43 - n13*n31*n44 + n11*n33*n44);
		te.set(9, n14*n23*n41 - n13*n24*n41 - n14*n21*n43 + n11*n24*n43 + n13*n21*n44 - n11*n23*n44);
		te.set(13, n13*n24*n31 - n14*n23*n31 + n14*n21*n33 - n11*n24*n33 - n13*n21*n34 + n11*n23*n34);
		te.set(2, n22*n34*n41 - n24*n32*n41 + n24*n31*n42 - n21*n34*n42 - n22*n31*n44 + n21*n32*n44);
		te.set(6, n14*n32*n41 - n12*n34*n41 - n14*n31*n42 + n11*n34*n42 + n12*n31*n44 - n11*n32*n44);
		te.set(10, n12*n24*n41 - n14*n22*n41 + n14*n21*n42 - n11*n24*n42 - n12*n21*n44 + n11*n22*n44);
		te.set(14, n14*n22*n31 - n12*n24*n31 - n14*n21*n32 + n11*n24*n32 + n12*n21*n34 - n11*n22*n34);
		te.set(3, n23*n32*n41 - n22*n33*n41 - n23*n31*n42 + n21*n33*n42 + n22*n31*n43 - n21*n32*n43);
		te.set(7, n12*n33*n41 - n13*n32*n41 + n13*n31*n42 - n11*n33*n42 - n12*n31*n43 + n11*n32*n43);
		te.set(11, n13*n22*n41 - n12*n23*n41 - n13*n21*n42 + n11*n23*n42 + n12*n21*n43 - n11*n22*n43);
		te.set(15, n12*n23*n31 - n13*n22*n31 + n13*n21*n32 - n11*n23*n32 - n12*n21*n33 + n11*n22*n33);
		this.multiply( 1.0 / m.determinant() );

		return this;
	}

	public Matrix4 setRotationFromEuler(Vector3 v)
	{
		return setRotationFromEuler(v, Euler.XYZ);
	}

	public Matrix4 setRotationFromEuler(Vector3 v, Euler order)
	{
		Float32Array te = this.elements;

		double x = v.x, y = v.y, z = v.z;
		double a = Math.cos( x ), b = Math.sin( x );
		double c = Math.cos( y ), d = Math.sin( y );
		double e = Math.cos( z ), f = Math.sin( z );

		if ( order == Euler.XYZ ) 
		{
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
		} 
		else if ( order == Euler.YXZ ) 
		{
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

		} 
		else if ( order == Euler.ZXY ) 
		{
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
		} 
		else if ( order == Euler.ZYX ) 
		{
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
		}
		else if ( order == Euler.YZX ) 
		{
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
		}
		else if ( order == Euler.XZY ) 
		{
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

		return this;
	}

	public void setRotationFromQuaternion(Quaternion q)
	{
		double x = q.x, y = q.y, z = q.z, w = q.w;
		double x2 = x + x, y2 = y + y, z2 = z + z;
		double xx = x * x2, xy = x * y2, xz = x * z2;
		double yy = y * y2, yz = y * z2, zz = z * z2;
		double wx = w * x2, wy = w * y2, wz = w * z2;

		this.getArray().set(0, 1 - (yy + zz));
		this.getArray().set(4, xy - wz);
		this.getArray().set(8, xz + wy);

		this.getArray().set(1, xy + wz);
		this.getArray().set(5, 1 - (xx + zz));
		this.getArray().set(9, yz - wx);

		this.getArray().set(2, xz - wy);
		this.getArray().set(6, yz + wx);
		this.getArray().set(10, 1 - (xx + yy));
	}

	public void compose(Vector3 translation, Quaternion rotation, Vector3 scale)
	{
		Matrix4 mRotation = __m1;
		Matrix4 mScale = __m2;

		mRotation.identity();
		mRotation.setRotationFromQuaternion(rotation);

		mScale.makeScale(scale.x, scale.y, scale.z);

		this.multiply(mRotation, mScale);

		this.getArray().set(12, translation.x);
		this.getArray().set(13, translation.y);
		this.getArray().set(14, translation.z);
	}

	public void decompose(Vector3 translation, Quaternion rotation, Vector3 scale)
	{
		// grab the axis vectors
		Vector3 x = __v1;
		Vector3 y = __v2;
		Vector3 z = __v3;

		x.set(this.getArray().get(0), this.getArray().get(1), this.getArray().get(2));
		y.set(this.getArray().get(4), this.getArray().get(5), this.getArray().get(6));
		z.set(this.getArray().get(8), this.getArray().get(9), this.getArray().get(10));

		scale.x = x.length();
		scale.y = y.length();
		scale.z = z.length();

		translation.x = this.getArray().get(12);
		translation.y = this.getArray().get(13);
		translation.z = this.getArray().get(14);

		// scale the rotation part

		Matrix4 matrix = __m1;

		matrix.copy(this);

		matrix.getArray().set(0, matrix.getArray().get(0) / scale.x);
		matrix.getArray().set(1, matrix.getArray().get(1) / scale.x);
		matrix.getArray().set(2, matrix.getArray().get(2) / scale.x);

		matrix.getArray().set(4, matrix.getArray().get(4) / scale.y);
		matrix.getArray().set(5, matrix.getArray().get(5) / scale.y);
		matrix.getArray().set(6, matrix.getArray().get(6) / scale.y);

		matrix.getArray().set(8, matrix.getArray().get(8) / scale.z);
		matrix.getArray().set(9, matrix.getArray().get(9) / scale.z);
		matrix.getArray().set(10, matrix.getArray().get(10) / scale.z);

		rotation.setFromRotationMatrix(matrix);
	}

	/**
	 * Setting position value to the position value of the input matrix.
	 * 
	 * @param m the input vector
	 * 
	 * @return the current matrix
	 */
	public Matrix4 extractPosition(Matrix4 m)
	{
		Float32Array me = m.getArray();

		this.getArray().set(12, me.get(12));
		this.getArray().set(13, me.get(13));
		this.getArray().set(14, me.get(14));
		
		return this;
	}

	@Deprecated
	public void extractRotation(Matrix4 m, Vector3 s)
	{
		double scaleX = 1.0 / s.getX(), scaleY = 1.0 / s.getY(), scaleZ = 1.0 / s.getZ();
		
		this.getArray().set(0, this.getArray().get(0) * scaleX);
		this.getArray().set(1, this.getArray().get(1) * scaleX);
		this.getArray().set(2, this.getArray().get(2) * scaleX);

		this.getArray().set(4, this.getArray().get(4) * scaleY);
		this.getArray().set(5, this.getArray().get(5) * scaleY);
		this.getArray().set(6, this.getArray().get(6) * scaleY);

		this.getArray().set(8, this.getArray().get(8) * scaleZ);
		this.getArray().set(9, this.getArray().get(9) * scaleZ);
		this.getArray().set(10, this.getArray().get(10) * scaleZ);
	}
	
	/**
	 * Setting rotation values to the rotation values of the input matrix.
	 * 
	 * @param m the input matrix
	 */
	public Matrix4 extractRotation(Matrix4 m)
	{
		Float32Array me = m.getArray();

		Vector3 vector = Matrix4.__v1;

		double scaleX = 1.0 / vector.set(me.get(0), me.get(1), me.get(2)).length();
		double scaleY = 1.0 / vector.set(me.get(4), me.get(5), me.get(6)).length();
		double scaleZ = 1.0 / vector.set(me.get(8), me.get(9), me.get(10)).length();

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

	/**
	 * Matrix translation: moves every point a constant distance 
	 * in a specified direction.
	 * 
	 * @param v the vector which define direction
	 */
	public void translate(Vector3 v)
	{
		double x = v.x, y = v.y, z = v.z;

		this.getArray().set(12, this.getArray().get(0) * x + this.getArray().get(4) * y + this.getArray().get(8) * z + this.getArray().get(12));
		this.getArray().set(13, this.getArray().get(1) * x + this.getArray().get(5) * y + this.getArray().get(9) * z + this.getArray().get(13));
		this.getArray().set(14, this.getArray().get(2) * x + this.getArray().get(6) * y + this.getArray().get(10) * z + this.getArray().get(14));
		this.getArray().set(15, this.getArray().get(3) * x + this.getArray().get(7) * y + this.getArray().get(11) * z + this.getArray().get(15));
	}

	/**
	 * Rotate the current matrix on X axis by defined angle.
	 * 
	 * @param angle the angle value
	 */
	public void rotateX(double angle)
	{
		double m12 = this.getArray().get(4);
		double m22 = this.getArray().get(5);
		double m32 = this.getArray().get(6);
		double m42 = this.getArray().get(7);
		double m13 = this.getArray().get(8);
		double m23 = this.getArray().get(9);
		double m33 = this.getArray().get(10);
		double m43 = this.getArray().get(11);

		double c = Math.cos(angle);
		double s = Math.sin(angle);

		this.getArray().set(4, c * m12 + s * m13);
		this.getArray().set(5, c * m22 + s * m23);
		this.getArray().set(6, c * m32 + s * m33);
		this.getArray().set(7, c * m42 + s * m43);

		this.getArray().set(8, c * m13 - s * m12);
		this.getArray().set(9, c * m23 - s * m22);
		this.getArray().set(10, c * m33 - s * m32);
		this.getArray().set(11, c * m43 - s * m42);
	}

	/**
	 * Rotate the current matrix on Y axis by defined angle.
	 * 
	 * @param angle the angle value
	 */
	public void rotateY(double angle)
	{
		double m11 = this.getArray().get(0);
		double m21 = this.getArray().get(1);
		double m31 = this.getArray().get(2);
		double m41 = this.getArray().get(3);
		double m13 = this.getArray().get(8);
		double m23 = this.getArray().get(9);
		double m33 = this.getArray().get(10);
		double m43 = this.getArray().get(11);

		double c = Math.cos(angle);
		double s = Math.sin(angle);

		this.getArray().set(0, c * m11 + s * m13);
		this.getArray().set(1, c * m21 + s * m23);
		this.getArray().set(2, c * m31 + s * m33);
		this.getArray().set(3, c * m41 + s * m43);

		this.getArray().set(8, c * m13 - s * m11);
		this.getArray().set(9, c * m23 - s * m21);
		this.getArray().set(10, c * m33 - s * m31);
		this.getArray().set(11, c * m43 - s * m41);
	}

	/**
	 * Rotate the current matrix on Z axis by defined angle.
	 * 
	 * @param angle the angle value
	 */
	public void rotateZ(double angle)
	{
		double m11 = this.getArray().get(0);
		double m21 = this.getArray().get(1);
		double m31 = this.getArray().get(2);
		double m41 = this.getArray().get(3);
		double m12 = this.getArray().get(4);
		double m22 = this.getArray().get(5);
		double m32 = this.getArray().get(6);
		double m42 = this.getArray().get(7);

		double c = Math.cos(angle);
		double s = Math.sin(angle);

		this.getArray().set(0, c * m11 + s * m12);
		this.getArray().set(1, c * m21 + s * m22);
		this.getArray().set(2, c * m31 + s * m32);
		this.getArray().set(3, c * m41 + s * m42);

		this.getArray().set(4, c * m12 - s * m11);
		this.getArray().set(5, c * m22 - s * m21);
		this.getArray().set(6, c * m32 - s * m31);
		this.getArray().set(7, c * m42 - s * m41);
	}

	/**
	 * Rotate the current matrix on axis by defined angle.
	 * 
	 * @param axis the axis on which rotate the matrix
	 * @param angle the angle value
	 */
	public void rotateByAxis(Vector3 axis, double angle)
	{
		double x = axis.getX(), y = axis.getY(), z = axis.getZ();

		// optimize by checking axis
		if (x == 1 && y == 0 && z == 0) 
		{
			this.rotateX(angle);
			return;
		} 
		else if (x == 0 && y == 1 && z == 0) 
		{
			this.rotateY(angle);
			return;
		} 
		else if (x == 0 && y == 0 && z == 1) 
		{
			this.rotateZ(angle);
			return;
		}

		double n = Math.sqrt(x * x + y * y + z * z);

		x /= n;
		y /= n;
		z /= n;

		double xx = x * x, yy = y * y, zz = z * z;
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		double oneMinusCosine = 1 - c;
		double xy = x * y * oneMinusCosine;
		double xz = x * z * oneMinusCosine;
		double yz = y * z * oneMinusCosine;
		double xs = x * s;
		double ys = y * s;
		double zs = z * s;

		double r11 = xx + (1 - xx) * c;
		double r21 = xy + zs;
		double r31 = xz - ys;
		double r12 = xy - zs;
		double r22 = yy + (1 - yy) * c;
		double r32 = yz + xs;
		double r13 = xz + ys;
		double r23 = yz - xs;
		double r33 = zz + (1 - zz) * c;

		double m11 = this.getArray().get(0), m21 = this.getArray().get(1), m31 = this.getArray().get(2), m41 = this.getArray().get(3);
		double m12 = this.getArray().get(4), m22 = this.getArray().get(5), m32 = this.getArray().get(6), m42 = this.getArray().get(7);
		double m13 = this.getArray().get(8), m23 = this.getArray().get(9), m33 = this.getArray().get(10), m43 = this.getArray().get(11);

		this.getArray().set(0, r11 * m11 + r21 * m12 + r31 * m13);
		this.getArray().set(1, r11 * m21 + r21 * m22 + r31 * m23);
		this.getArray().set(2, r11 * m31 + r21 * m32 + r31 * m33);
		this.getArray().set(3, r11 * m41 + r21 * m42 + r31 * m43);

		this.getArray().set(4, r12 * m11 + r22 * m12 + r32 * m13);
		this.getArray().set(5, r12 * m21 + r22 * m22 + r32 * m23);
		this.getArray().set(6, r12 * m31 + r22 * m32 + r32 * m33);
		this.getArray().set(7, r12 * m41 + r22 * m42 + r32 * m43);

		this.getArray().set(8, r13 * m11 + r23 * m12 + r33 * m13);
		this.getArray().set(9, r13 * m21 + r23 * m22 + r33 * m23);
		this.getArray().set(10, r13 * m31 + r23 * m32 + r33 * m33);
		this.getArray().set(11, r13 * m41 + r23 * m42 + r33 * m43);
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
		double ymax = near * Math.tan( fov * Math.PI / 360.0 );
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
