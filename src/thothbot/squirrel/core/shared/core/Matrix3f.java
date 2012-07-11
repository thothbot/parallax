/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.core;

import thothbot.squirrel.core.client.gl2.arrays.Float32Array;
import thothbot.squirrel.core.shared.Log;

/**
 * This class implements three-dimensional matrix. MxM, where M=3.
 * 
 * This matrix actually is array which is represented the following 
 * indexes:
 * 
 * 0 3 6
 * 1 4 7
 * 2 5 8
 * 
 * @author thothbot
 *
 */
public class Matrix3f
{
	private Float32Array elements;

	/**
	 * Default constructor will make empty three-dimensional matrix.
	 */
	public Matrix3f() 
	{
		this.elements = Float32Array.create(9);
	}
	

	/**
	 * Getting the current Matrix which is represented 
	 * by Array[9] which the following indexes:
	 * 
	 * 0 3 6
	 * 1 4 7
	 * 2 5 8
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
	public void getInverse(Matrix4f m)
	{
		// input: THREE.Matrix4
		// ( based on http://code.google.com/p/webgl-mjs/ )
		Float32Array me = m.getArray();

		float a11 = me.get(10) * me.get(5) - me.get(6) * me.get(9);
		float a21 = -me.get(10) * me.get(1) + me.get(2) * me.get(9);
		float a31 = me.get(6) * me.get(1) - me.get(2) * me.get(5);
		float a12 = -me.get(10) * me.get(4) + me.get(6) * me.get(8);
		float a22 = me.get(10) * me.get(0) - me.get(2) * me.get(8);
		float a32 = -me.get(6) * me.get(0) + me.get(2) * me.get(4);
		float a13 = me.get(9) * me.get(4) - me.get(5) * me.get(8);
		float a23 = -me.get(9) * me.get(0) + me.get(1) * me.get(8);
		float a33 = me.get(5) * me.get(0) - me.get(1) * me.get(4);

		float det = me.get(0) * a11 + me.get(1) * a12 + me.get(2) * a13;

		// no inverse

		if (det == 0)
			Log.error("Matrix3f.invert(): determinant == 0");

		float idet = 1.0f / det;

		this.getArray().set(0, idet * a11);
		this.getArray().set(1, idet * a21);
		this.getArray().set(2, idet * a31);
		this.getArray().set(3, idet * a12);
		this.getArray().set(4, idet * a22);
		this.getArray().set(5, idet * a32);
		this.getArray().set(6, idet * a13);
		this.getArray().set(7, idet * a23);
		this.getArray().set(8, idet * a33);
	}

	/**
	 * Transpose the current matrix where its rows will be the 
	 * columns or its columns are the rows of the current matrix.
	 */
	public void transpose()
	{
		float tmp;
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
	 * Getting information of the current Matrix 
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
