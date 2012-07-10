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

import com.google.gwt.core.client.GWT;

public class Matrix3f
{
	public Float32Array elements;

	public Matrix3f() {
		elements = Float32Array.create(9);
	}

	public void invert(Matrix3f m1)
	{
		
	}

	/**
	 * Sets the value of this matrix to the matrix inverse of the passed matrix
	 * m1.
	 * 
	 * @param m1
	 *            the matrix to be inverted
	 */
	public void getInverse(Matrix4f m1)
	{
		// input: THREE.Matrix4
		// ( based on http://code.google.com/p/webgl-mjs/ )
		Float32Array me = m1.elements;

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

		this.elements.set(0, idet * a11);
		this.elements.set(1, idet * a21);
		this.elements.set(2, idet * a31);
		this.elements.set(3, idet * a12);
		this.elements.set(4, idet * a22);
		this.elements.set(5, idet * a32);
		this.elements.set(6, idet * a13);
		this.elements.set(7, idet * a23);
		this.elements.set(8, idet * a33);
	}

	public void transpose()
	{
		float tmp;
		Float32Array m = this.elements;

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

	public Float32Array transposeIntoArray()
	{
		Float32Array r = Float32Array.create(9);
		Float32Array m = this.elements;

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
	
	public String toString() {
		String retval = "[";
		
		for(int i = 0; i < this.elements.getLength(); i++)
			retval += this.elements.get(i) + ", ";
		
		return retval + "]";
	}
}
