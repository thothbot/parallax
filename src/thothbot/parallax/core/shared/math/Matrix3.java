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
public class Matrix3
{
	private Float32Array elements;

	/**
	 * Default constructor will make empty three-dimensional matrix.
	 */
	public Matrix3() 
	{
		this.elements = Float32Array.create(9);
	}
	

	/**
	 * get the current Matrix which is represented 
	 * by Array[9] which the following indexes:
	 * 
	 * <pre> {@code
	 * 0 3 6
	 * 1 4 7
	 * 2 5 8
	 * } </pre>
	 * 
	 * @return the Array
	 */
	public Float32Array getArray() 
	{
		return elements;
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
		Float32Array me = m.getArray();

		double a11 = me.get(10) * me.get(5) - me.get(6) * me.get(9);
		double a21 = -me.get(10) * me.get(1) + me.get(2) * me.get(9);
		double a31 = me.get(6) * me.get(1) - me.get(2) * me.get(5);
		double a12 = -me.get(10) * me.get(4) + me.get(6) * me.get(8);
		double a22 = me.get(10) * me.get(0) - me.get(2) * me.get(8);
		double a32 = -me.get(6) * me.get(0) + me.get(2) * me.get(4);
		double a13 = me.get(9) * me.get(4) - me.get(5) * me.get(8);
		double a23 = -me.get(9) * me.get(0) + me.get(1) * me.get(8);
		double a33 = me.get(5) * me.get(0) - me.get(1) * me.get(4);

		double det = me.get(0) * a11 + me.get(1) * a12 + me.get(2) * a13;

		// no inverse

		if (det == 0)
			Log.error("Matrix3.invert(): determinant == 0");

		double idet = 1.0 / det;

		this.getArray().set(0, idet * a11);
		this.getArray().set(1, idet * a21);
		this.getArray().set(2, idet * a31);
		this.getArray().set(3, idet * a12);
		this.getArray().set(4, idet * a22);
		this.getArray().set(5, idet * a32);
		this.getArray().set(6, idet * a13);
		this.getArray().set(7, idet * a23);
		this.getArray().set(8, idet * a33);
		
		return this;
	}

	/**
	 * Transpose the current matrix where its rows will be the 
	 * columns or its columns are the rows of the current matrix.
	 */
	public Matrix3 transpose()
	{
		double tmp;
		Float32Array m = this.getArray();

		tmp = m.get(1);
		m.set(1, m.get(3));
		m.set(3, tmp);

		tmp = m.get(2);
		m.set(2, m.get(6));
		m.set(6, tmp);

		tmp = m.get(5);
		m.set(5, m.get(7));
		m.set(7, tmp);
		
		return this;
	}

	/**
	 * Transpose the current matrix into new Matrix which is represented 
	 * by Array[9] 
	 * 
	 * @return an array of new transposed matrix.
	 */
	public Float32Array transposeIntoArray()
	{
		Float32Array r = Float32Array.create(9);
		Float32Array m = this.getArray();

		r.set(0, m.get(0));
		r.set(1, m.get(3));
		r.set(2, m.get(6));
		r.set(3, m.get(1));
		r.set(4, m.get(4));
		r.set(5, m.get(7));
		r.set(6, m.get(2));
		r.set(7, m.get(5));
		r.set(8, m.get(8));

		return r;
	}
	
	/**
	 * get information of the current Matrix 
	 * which is represented as list of it values.
	 */
	public String toString() 
	{
		String retval = "[";
		
		for(int i = 0; i < this.getArray().getLength(); i++)
			retval += this.getArray().get(i) + ", ";
		
		return retval + "]";
	}
}
