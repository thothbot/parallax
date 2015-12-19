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

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.system.ThreeJsObject;

/**
 * This class implements three-dimensional matrix. MxM, where M=3.
 * 
 * This matrix actually is array which is represented the following 
 * indexes:
 * 
 * <pre> {@code
 * 0 3 6
 * 1 4 7
 * 2 5 8
 * } </pre>
 * 
 * @author thothbot
 *
 */
@ThreeJsObject("THREE.Matrix3")
public class Matrix3
{
	final float[] elements = new float[9];

	// Temporary variables
	static Vector3 _v1 = new Vector3();

	/**
	 * Default constructor will make empty three-dimensional matrix.
	 */
	public Matrix3() 
	{
		identity();
	}
	
	public Matrix3( float n11, float n12, float n13, float n21, float n22, float n23, float n31, float n32, float n33 ) 
	{
		this();
		set(n11, n12, n13, n21, n22, n23, n31, n32, n33 );
	}
	
	public Matrix3 set( float n11, float n12, float n13, float n21, float n22, float n23, float n31, float n32, float n33 ) 
	{
		float[] val = this.elements;
		val[0] = n11;
		val[1] = n21;
		val[2] = n31;
		val[3] = n12;
		val[4] = n22;
		val[5] = n32;
		val[6] = n13;
		val[7] = n23;
		val[8] = n33;

		return this;
	}

	public float[] getArray()
	{
		return this.elements;
	}
	
	public Matrix3 identity() 
	{
		set(
			1, 0, 0,
			0, 1, 0,
			0, 0, 1
		);

		return this;
	}

	public Matrix3 copy( Matrix3 m ) 
	{

		float[] me = m.getArray();

		this.set(
			me[0], me[3], me[6],
			me[1], me[4], me[7],
			me[2], me[5], me[8]
		);

		return this;
	}
//
//	public Float32Array applyToVector3Array (Float32Array array)
//	{
//		return applyToVector3Array(array, 0, array.getLength());
//	}
//
//	public Float32Array applyToVector3Array (Float32Array array, int offset, int length)
//	{
//
//		for ( int i = 0, j = offset, il; i < length; i += 3, j += 3 ) {
//
//			_v1.x = array.get( j );
//			_v1.y = array.get( j + 1 );
//			_v1.z = array.get( j + 2 );
//
//			_v1.apply( this );
//
//			array.set( j , _v1.x );
//			array.set( j + 1 , _v1.y );
//			array.set( j + 2 , _v1.z );
//
//		}
//
//		return array;
//	}

	public Matrix3 multiply( float s ) 
	{
		float[] te = this.elements;

		te[0] *= s; te[3] *= s; te[6] *= s;
		te[1] *= s; te[4] *= s; te[7] *= s;
		te[2] *= s; te[5] *= s; te[8] *= s;

		return this;
	}

	public float determinant() 
	{
		float[] te = this.elements;

		float a = te[0], b = te[1], c = te[2],
			  d = te[3], e = te[4], f = te[5],
			  g = te[6], h = te[7], i = te[8];

		return a*e*i - a*f*h - b*d*i + b*f*g + c*d*h - c*e*g;
	}

	/**
	 * Sets the value of this matrix to the matrix inverse of the passed matrix
	 * m.
	 * 
	 * @param m the matrix to be inverted
	 */
	public Matrix3 getInverse(Matrix4 m)
	{
		// input: THREE.Matrix4
		// ( based on http://code.google.com/p/webgl-mjs/ )
		float[] me = m.getArray();
		float[] te = this.elements;

		te[0] = me[10] * me[5] - me[6] * me[9];
		te[1] = -me[10] * me[1] + me[2] * me[9];
		te[2] = me[6] * me[1] - me[2] * me[5];
		te[3] = -me[10] * me[4] + me[6] * me[8];
		te[4] = me[10] * me[0] - me[2] * me[8];
		te[5] = -me[6] * me[0] + me[2] * me[4];
		te[6] = me[9] * me[4] - me[5] * me[8];
		te[7] = -me[9] * me[0] + me[1] * me[8];
		te[8] = me[5] * me[0] - me[1] * me[4];

		float det = me[0] * te[0] + me[1] * te[3] + me[2] * te[6];

		// no inverse

		if (det == 0)
		{
			Log.error("Matrix3.invert(): determinant == 0");
			this.identity();
		}
		else
		{
			this.multiply( 1.0f / det );
		}

		return this;
	}

	/**
	 * Transpose the current matrix where its rows will be the 
	 * columns or its columns are the rows of the current matrix.
	 */
	public Matrix3 transpose()
	{
		float tmp;
		float[] m = this.elements;

		tmp = m[1];
		m[1] = m[3];
		m[3] = tmp;

		tmp = m[2];
		m[2] = m[6];
		m[6] = tmp;

		tmp = m[5];
		m[5] = m[7];
		m[7] = tmp;

		return this;
	}
	
	/**
	 * 
	 * @param m Matrix4
	 * @return
	 */
	public Matrix3 getNormalMatrix( Matrix4 m ) 
	{
		this.getInverse( m ).transpose();

		return this;
	}

	/**
	 * Transpose the current matrix into new Matrix which is represented 
	 * by Array[9] 
	 * 
	 * @return an array of new transposed matrix.
	 */
	public float[] transposeIntoArray()
	{
		float[] r = new float[9];
		float[] m = this.elements;

		r[0] = m[0];
		r[1] = m[3];
		r[2] = m[6];
		r[3] = m[1];
		r[4] = m[4];
		r[5] = m[7];
		r[6] = m[2];
		r[7] = m[5];
		r[8] = m[8];

		return r;
	}
	
	/**
	 * get information of the current Matrix 
	 * which is represented as list of it values.
	 */
	public String toString() 
	{
		String retval = "[";
		
		for(int i = 0; i < this.elements.length; i++)
			retval += this.elements[i] + ", ";
		
		return retval + "]";
	}
	
	public Matrix3 clone() 
	{
		float[] te = this.elements;

		return new Matrix3(
			te[0], te[3], te[6],
			te[1], te[4], te[7],
			te[2], te[5], te[8]
		);
	}
}
