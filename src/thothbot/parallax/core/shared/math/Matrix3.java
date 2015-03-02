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
	
	// Temporary variables
	static Vector3 _v1 = new Vector3();

	/**
	 * Default constructor will make empty three-dimensional matrix.
	 */
	public Matrix3() 
	{
		this.elements = Float32Array.create(9);
		identity();
	}
	
	public Matrix3( double n11, double n12, double n13, double n21, double n22, double n23, double n31, double n32, double n33 ) 
	{
		this();
		set(n11, n12, n13, n21, n22, n23, n31, n32, n33 );
	}
	
	public Matrix3 set( double n11, double n12, double n13, double n21, double n22, double n23, double n31, double n32, double n33 ) 
	{
		Float32Array te = this.getArray();

		te.set(0, n11); te.set(3, n12); te.set(6, n13);
		te.set(1, n21); te.set(4, n22); te.set(7, n23);
		te.set(2, n31); te.set(5, n32); te.set(8, n33);

		return this;
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
		Float32Array me = m.getArray();

		this.set(
			me.get(0), me.get(3), me.get(6),
			me.get(1), me.get(4), me.get(7),
			me.get(2), me.get(5), me.get(8)
		);

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

			array.set( j , _v1.x );
			array.set( j + 1 , _v1.y );
			array.set( j + 2 , _v1.z );

		}

		return array;
	}

	public Matrix3 multiply( double s ) 
	{
		Float32Array te = this.getArray();

		te.set(0, te.get(0) * s); te.set(3, te.get(3) * s); te.set(6, te.get(6) * s);
		te.set(1, te.get(1) * s); te.set(4, te.get(4) * s); te.set(7, te.get(7) * s);
		te.set(2, te.get(2) * s); te.set(5, te.get(5) * s); te.set(8, te.get(8) * s);

		return this;
	}

	public double determinant() 
	{
		Float32Array te = this.getArray();

		double a = te.get(0), b = te.get(1), c = te.get(2),
			d = te.get(3), e = te.get(4), f = te.get(5),
			g = te.get(6), h = te.get(7), i = te.get(8);

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
		Float32Array me = m.getArray();
		Float32Array te = this.getArray();

		te.set(0, me.get(10) * me.get(5) - me.get(6) * me.get(9));
		te.set(1, -me.get(10) * me.get(1) + me.get(2) * me.get(9));
		te.set(2,  me.get(6) * me.get(1) - me.get(2) * me.get(5));
		te.set(3, -me.get(10) * me.get(4) + me.get(6) * me.get(8));
		te.set(4, me.get(10) * me.get(0) - me.get(2) * me.get(8));
		te.set(5, -me.get(6) * me.get(0) + me.get(2) * me.get(4));
		te.set(6, me.get(9) * me.get(4) - me.get(5) * me.get(8));
		te.set(7, -me.get(9) * me.get(0) + me.get(1) * me.get(8));
		te.set(8, me.get(5) * me.get(0) - me.get(1) * me.get(4));

		double det = me.get(0) * te.get(0) + me.get(1) * te.get(3) + me.get(2) * te.get(6);

		// no inverse

		if (det == 0)
		{
			Log.error("Matrix3.invert(): determinant == 0");
			this.identity();
		}
		else
		{
			this.multiply( 1.0 / det );	
		}

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
	
	public Matrix3 clone() 
	{
		Float32Array te = this.getArray();

		return new Matrix3(

			te.get(0), te.get(3), te.get(6),
			te.get(1), te.get(4), te.get(7),
			te.get(2), te.get(5), te.get(8)
		);
	}
}
