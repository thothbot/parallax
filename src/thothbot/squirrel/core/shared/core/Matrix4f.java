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

public class Matrix4f
{
	public Float32Array elements;

	public static Vector3f __v1 = new Vector3f(); // x
	public static Vector3f __v2 = new Vector3f(); // y
	public static Vector3f __v3 = new Vector3f(); // z

	public static Matrix4f __m1 = new Matrix4f();
	public static Matrix4f __m2 = new Matrix4f();

	public Matrix4f() 
	{
		elements = Float32Array.create(16);
		identity();
	}

	public Matrix4f(float n11, float n12, float n13, float n14, 
			float n21, float n22, float n23, float n24, 
			float n31, float n32, float n33, float n34, 
			float n41, float n42, float n43, float n44) 
	{
		elements = Float32Array.create(16);
		set(
			n11, n12, n13, n14,
			n21, n22, n23, n24,
			n31, n32, n33, n34,
			n41, n42, n43, n44
		);
	}

	public Matrix4f set(
			float n11, float n12, float n13, float n14, 
			float n21, float n22, float n23, float n24, 
			float n31, float n32, float n33, float n34, 
			float n41, float n42, float n43, float n44)
	{
		this.elements.set(0, n11);
		this.elements.set(1, n21);
		this.elements.set(2, n31);
		this.elements.set(3, n41);
		
		this.elements.set(4, n12);
		this.elements.set(5, n22);
		this.elements.set(6, n32);
		this.elements.set(7, n42);
		
		this.elements.set(8, n13);
		this.elements.set(9, n23);
		this.elements.set(10, n33);
		this.elements.set(11, n43);
		
		this.elements.set(12, n14);
		this.elements.set(13, n24);
		this.elements.set(14, n34);
		this.elements.set(15, n44);
		
		return this;
	}

	public void identity()
	{
		set(
			1f, 0f, 0f, 0f, 
			0f,	1f, 0f, 0f,
			0f, 0f, 1f, 0f, 
			0f, 0f, 0f, 1f
		);
	}

	public Matrix4f copy(Matrix4f m)
	{
		Float32Array me = m.elements;
		return set(
			me.get(0), me.get(4), me.get(8),  me.get(12), 
			me.get(1), me.get(5), me.get(9),  me.get(13), 
			me.get(2), me.get(6), me.get(10), me.get(14), 
			me.get(3), me.get(7), me.get(11), me.get(15)
		);
	}

	public Matrix4f lookAt(Vector3f eye, Vector3f target, Vector3f up)
	{
		Float32Array te = this.elements;

		Vector3f x = Matrix4f.__v1;
		Vector3f y = Matrix4f.__v2;
		Vector3f z = Matrix4f.__v3;

		z.sub( eye, target ).normalize();

		if ( z.length() == 0 )
			z.setZ(1);

		x.cross( up, z ).normalize();

		if ( x.length() == 0 ) {
			z.addX(0.0001f);
			x.cross( up, z ).normalize();
		}

		y.cross( z, x );


		te.set(0, x.getX()); te.set(4, y.getX()); te.set(8, z.getX());
		te.set(1, x.getY()); te.set(5, y.getY()); te.set(9, z.getY());
		te.set(2, x.getZ()); te.set(6, y.getZ()); te.set(10, z.getZ());

		return this;
	}

	public Matrix4f multiply(Matrix4f m1, Matrix4f m2)
	{
		Float32Array ae = m1.elements;
		Float32Array be = m2.elements;

		float a11 = ae.get(0), a12 = ae.get(4), a13 = ae.get(8), a14 = ae.get(12);
		float a21 = ae.get(1), a22 = ae.get(5), a23 = ae.get(9), a24 = ae.get(13);
		float a31 = ae.get(2), a32 = ae.get(6), a33 = ae.get(10), a34 = ae.get(14);
		float a41 = ae.get(3), a42 = ae.get(7), a43 = ae.get(11), a44 = ae.get(15);

		float b11 = be.get(0), b12 = be.get(4), b13 = be.get(8), b14 = be.get(12);
		float b21 = be.get(1), b22 = be.get(5), b23 = be.get(9), b24 = be.get(13);
		float b31 = be.get(2), b32 = be.get(6), b33 = be.get(10), b34 = be.get(14);
		float b41 = be.get(3), b42 = be.get(7), b43 = be.get(11), b44 = be.get(15);

		this.elements.set(0, (a11 * b11 + a12 * b21 + a13 * b31 + a14 * b41));
		this.elements.set(4, (a11 * b12 + a12 * b22 + a13 * b32 + a14 * b42));
		this.elements.set(8, (a11 * b13 + a12 * b23 + a13 * b33 + a14 * b43));
		this.elements.set(12, (a11 * b14 + a12 * b24 + a13 * b34 + a14 * b44));

		this.elements.set(1, (a21 * b11 + a22 * b21 + a23 * b31 + a24 * b41));
		this.elements.set(5, (a21 * b12 + a22 * b22 + a23 * b32 + a24 * b42));
		this.elements.set(9, (a21 * b13 + a22 * b23 + a23 * b33 + a24 * b43));
		this.elements.set(13, (a21 * b14 + a22 * b24 + a23 * b34 + a24 * b44));

		this.elements.set(2, (a31 * b11 + a32 * b21 + a33 * b31 + a34 * b41));
		this.elements.set(6, (a31 * b12 + a32 * b22 + a33 * b32 + a34 * b42));
		this.elements.set(10, (a31 * b13 + a32 * b23 + a33 * b33 + a34 * b43));
		this.elements.set(14, (a31 * b14 + a32 * b24 + a33 * b34 + a34 * b44));

		this.elements.set(3, (a41 * b11 + a42 * b21 + a43 * b31 + a44 * b41));
		this.elements.set(7, (a41 * b12 + a42 * b22 + a43 * b32 + a44 * b42));
		this.elements.set(11, (a41 * b13 + a42 * b23 + a43 * b33 + a44 * b43));
		this.elements.set(15, (a41 * b14 + a42 * b24 + a43 * b34 + a44 * b44));
		
		return this;
	}

	public Matrix4f multiply(Matrix4f m1)
	{
		return multiply(this, m1);
	}

	public Matrix4f multiply(float s)
	{
		this.elements.set(0, (this.elements.get(0) * s));
		this.elements.set(4, (this.elements.get(4) * s));
		this.elements.set(8, (this.elements.get(8) * s));
		this.elements.set(12, (this.elements.get(12) * s));
		this.elements.set(1, (this.elements.get(1) * s));
		this.elements.set(5, (this.elements.get(5) * s));
		this.elements.set(9, (this.elements.get(9) * s));
		this.elements.set(13, (this.elements.get(13) * s));
		this.elements.set(2, (this.elements.get(2) * s));
		this.elements.set(6, (this.elements.get(6) * s));
		this.elements.set(10, (this.elements.get(10) * s));
		this.elements.set(14, (this.elements.get(14) * s));
		this.elements.set(3, (this.elements.get(3) * s));
		this.elements.set(7, (this.elements.get(7) * s));
		this.elements.set(11, (this.elements.get(11) * s));
		this.elements.set(15, (this.elements.get(15) * s));
		
		return this;
	}
	
	public Vector4f multiplyVector3(Vector4f v)
	{
		Float32Array te = this.elements;

		float vx = v.getX();
		float vy = v.getY();
		float vz = v.getZ();
		float d = 1.0f / ( te.get(3) * vx + te.get(7) * vy + te.get(11) * vz + te.get(15) );

		v.setX( ( te.get(0) * vx + te.get(4) * vy + te.get(8)  * vz + te.get(12) ) * d );
		v.setY( ( te.get(1) * vx + te.get(5) * vy + te.get(9)  * vz + te.get(13) ) * d );
		v.setZ( ( te.get(2) * vx + te.get(6) * vy + te.get(10) * vz + te.get(14) ) * d );

		return v;
	}
	
	public Vector3f multiplyVector3(Vector3f v)
	{
		Float32Array te = this.elements;
	
		float vx = v.getX();
		float vy = v.getY();
		float vz = v.getZ();
		float d = 1.0f / ( te.get(3) * vx + te.get(7) * vy + te.get(11) * vz + te.get(15) );

		v.setX( ( te.get(0) * vx + te.get(4) * vy + te.get(8)  * vz + te.get(12) ) * d );
		v.setY( ( te.get(1) * vx + te.get(5) * vy + te.get(9)  * vz + te.get(13) ) * d );
		v.setZ( ( te.get(2) * vx + te.get(6) * vy + te.get(10) * vz + te.get(14) ) * d );

		return v;
	}

	public Vector4f multiplyVector4(Vector4f v)
	{
		Float32Array te = this.elements;
		float vx = v.x, vy = v.y, vz = v.z, vw = v.w;

		v.x = te.get(0) * vx + te.get(4) * vy + te.get(8) * vz + te.get(12) * vw;
		v.y = te.get(1) * vx + te.get(5) * vy + te.get(9) * vz + te.get(13) * vw;
		v.z = te.get(2) * vx + te.get(6) * vy + te.get(10) * vz + te.get(14) * vw;
		v.w = te.get(3) * vx + te.get(7) * vy + te.get(11) * vz + te.get(15) * vw;

		return v;
	}

	public Vector3f rotateAxis(Vector3f v)
	{
		float vx = v.x, vy = v.y, vz = v.z;

		v.setX(this.elements.get(0) * vx + this.elements.get(4) * vy + this.elements.get(8) * vz);
		v.setY(this.elements.get(1) * vx + this.elements.get(5) * vy + this.elements.get(9) * vz);
		v.setZ(this.elements.get(2) * vx + this.elements.get(6) * vy + this.elements.get(10) * vz);

		v.normalize();
		return v;
	}

	public Vector4f crossVector(Vector4f a)
	{
		Float32Array te = this.elements;
		Vector4f v = new Vector4f();

		v.x = te.get(0) * a.x + te.get(4) * a.y + te.get(8) * a.z + te.get(12) * a.w;
		v.y = te.get(1) * a.x + te.get(5) * a.y + te.get(9) * a.z + te.get(13) * a.w;
		v.z = te.get(2) * a.x + te.get(6) * a.y + te.get(10) * a.z + te.get(14) * a.w;

		v.w = ( a.w > 0 ) ? te.get(3) * a.x + te.get(7) * a.y + te.get(11) * a.z + te.get(15) * a.w : 1;

		return v;
	}

	public double determinant()
	{
		double n11 = this.elements.get(0), n12 = this.elements.get(4), n13 = this.elements.get(8),  n14 = this.elements.get(12);
		double n21 = this.elements.get(1), n22 = this.elements.get(5), n23 = this.elements.get(9),  n24 = this.elements.get(13);
		double n31 = this.elements.get(2), n32 = this.elements.get(6), n33 = this.elements.get(10), n34 = this.elements.get(14);
		double n41 = this.elements.get(3), n42 = this.elements.get(7), n43 = this.elements.get(11), n44 = this.elements.get(15);

		double det;

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

	public Matrix4f transpose()
	{
		Float32Array te = this.elements;
		float tmp;

		tmp = te.get(1); te.set(1, te.get(4)); te.set(4, tmp);
		tmp = te.get(2); te.set(2, te.get(8)); te.set(8, tmp);
		tmp = te.get(6); te.set(6, te.get(9)); te.set(9, tmp);

		tmp = te.get(3); te.set(3, te.get(12)); te.set(12, tmp);
		tmp = te.get(7); te.set(7, te.get(13)); te.set(13, tmp);
		tmp = te.get(11); te.set(11, te.get(14)); te.set(14, tmp);

		return this;
	}

	public Float32Array flattenToArray(Float32Array flat)
	{
		return flattenToArray(flat, 0);
	}

	public Float32Array flattenToArray(Float32Array flat, int offset)
	{
		flat.set(offset, this.elements.get(0));
		flat.set(offset + 1, this.elements.get(1));
		flat.set(offset + 2, this.elements.get(2));
		flat.set(offset + 3, this.elements.get(3));

		flat.set(offset + 4, this.elements.get(4));
		flat.set(offset + 5, this.elements.get(5));
		flat.set(offset + 6, this.elements.get(6));
		flat.set(offset + 7, this.elements.get(7));

		flat.set(offset + 8, this.elements.get(8));
		flat.set(offset + 9, this.elements.get(9));
		flat.set(offset + 10, this.elements.get(10));
		flat.set(offset + 11, this.elements.get(11));

		flat.set(offset + 12, this.elements.get(12));
		flat.set(offset + 13, this.elements.get(13));
		flat.set(offset + 14, this.elements.get(14));
		flat.set(offset + 15, this.elements.get(15));

		return flat;
	}

	public Vector3f getPosition()
	{
		return Matrix4f.__v1.set(this.elements.get(12), this.elements.get(13), this.elements.get(14));
	}

	public Matrix4f setPosition(Vector3f v)
	{
		this.elements.set(12, v.getX());
		this.elements.set(13, v.getY());
		this.elements.set(14, v.getZ());

		return this;
	}

	public Vector3f getColumnX()
	{
		return Matrix4f.__v1.set(this.elements.get(0), this.elements.get(1), this.elements.get(2));
	}

	public Vector3f getColumnY()
	{
		return Matrix4f.__v1.set(this.elements.get(4), this.elements.get(5), this.elements.get(6));
	}

	public Vector3f getColumnZ()
	{
		return Matrix4f.__v1.set(this.elements.get(8), this.elements.get(9), this.elements.get(10));
	}

	public Matrix4f getInverse(Matrix4f m)
	{

		// based on
		// http://www.euclideanspace.com/maths/algebra/matrix/functions/inverse/fourD/index.htm
		Float32Array te = this.elements;
		Float32Array me = m.elements;

		float n11 = me.get(0), n12 = me.get(4), n13 = me.get(8),  n14 = me.get(12);
		float n21 = me.get(1), n22 = me.get(5), n23 = me.get(9),  n24 = me.get(13);
		float n31 = me.get(2), n32 = me.get(6), n33 = me.get(10), n34 = me.get(14);
		float n41 = me.get(3), n42 = me.get(7), n43 = me.get(11), n44 = me.get(15);

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
		this.multiply( (float)(1.0 / m.determinant()) );

		return this;
	}

	public void setRotationFromEuler(Vector3f v)
	{
		setRotationFromEuler(v, Euler.XYZ);
	}

	public void setRotationFromEuler(Vector3f v, Euler order)
	{

		float x = v.x, y = v.y, z = v.z;
		float a = (float) Math.cos(x), b = (float) Math.sin(x);
		float c = (float) Math.cos(y), d = (float) Math.sin(y);
		float e = (float) Math.cos(z), f = (float) Math.sin(z);

		float ce, cf, de, df;
		float ae, af, be, bf;
		float ac, ad, bc, bd;
		switch (order) {

		case YXZ:

			ce = c * e;
			cf = c * f;
			de = d * e;
			df = d * f;

			this.elements.set(0, ce + df * b);
			this.elements.set(4, de * b - cf);
			this.elements.set(8, a * d);

			this.elements.set(1, a * f);
			this.elements.set(5, a * e);
			this.elements.set(9, -b);

			this.elements.set(2, cf * b - de);
			this.elements.set(6, df + ce * b);
			this.elements.set(10, a * c);
			break;

		case ZXY:

			ce = c * e;
			cf = c * f;
			de = d * e;
			df = d * f;

			this.elements.set(0, ce - df * b);
			this.elements.set(4, -a * f);
			this.elements.set(8, de + cf * b);

			this.elements.set(1, cf + de * b);
			this.elements.set(5, a * e);
			this.elements.set(9, df - ce * b);

			this.elements.set(2, -a * d);
			this.elements.set(6, b);
			this.elements.set(10, a * c);
			break;

		case ZYX:

			ae = a * e;
			af = a * f;
			be = b * e;
			bf = b * f;

			this.elements.set(0, c * e);
			this.elements.set(4, be * d - af);
			this.elements.set(8, ae * d + bf);

			this.elements.set(1, c * f);
			this.elements.set(5, bf * d + ae);
			this.elements.set(9, af * d - be);

			this.elements.set(2, -d);
			this.elements.set(6, b * c);
			this.elements.set(10, a * c);
			break;

		case YZX:

			ac = a * c;
			ad = a * d;
			bc = b * c;
			bd = b * d;

			this.elements.set(0, c * e);
			this.elements.set(4, bd - ac * f);
			this.elements.set(8, bc * f + ad);

			this.elements.set(1, f);
			this.elements.set(5, a * e);
			this.elements.set(9, -b * e);

			this.elements.set(2, -d * e);
			this.elements.set(6, ad * f + bc);
			this.elements.set(10, ac - bd * f);
			break;

		case XZY:

			ac = a * c;
			ad = a * d;
			bc = b * c;
			bd = b * d;

			this.elements.set(0, c * e);
			this.elements.set(4, -f);
			this.elements.set(8, d * e);

			this.elements.set(1, ac * f + bd);
			this.elements.set(5, a * e);
			this.elements.set(9, ad * f - bc);

			this.elements.set(2, bc * f - ad);
			this.elements.set(6, b * e);
			this.elements.set(10, bd * f + ac);
			break;

		default: // 'XYZ'

			ae = a * e;
			af = a * f;
			be = b * e;
			bf = b * f;

			this.elements.set(0, c * e);
			this.elements.set(4, -c * f);
			this.elements.set(8, d);

			this.elements.set(1, af + be * d);
			this.elements.set(5, ae - bf * d);
			this.elements.set(9, -b * c);

			this.elements.set(2, bf - ae * d);
			this.elements.set(6, be + af * d);
			this.elements.set(10, a * c);
			break;

		}

	}

	public void setRotationFromQuaternion(Quaternion q)
	{
		float x = q.x, y = q.y, z = q.z, w = q.w;
		float x2 = x + x, y2 = y + y, z2 = z + z;
		float xx = x * x2, xy = x * y2, xz = x * z2;
		float yy = y * y2, yz = y * z2, zz = z * z2;
		float wx = w * x2, wy = w * y2, wz = w * z2;

		this.elements.set(0, 1 - (yy + zz));
		this.elements.set(4, xy - wz);
		this.elements.set(8, xz + wy);

		this.elements.set(1, xy + wz);
		this.elements.set(5, 1 - (xx + zz));
		this.elements.set(9, yz - wx);

		this.elements.set(2, xz - wy);
		this.elements.set(6, yz + wx);
		this.elements.set(10, 1 - (xx + yy));
	}

	public void compose(Vector3f translation, Quaternion rotation, Vector3f scale)
	{
		Matrix4f mRotation = __m1;
		Matrix4f mScale = __m2;

		mRotation.identity();
		mRotation.setRotationFromQuaternion(rotation);

		mScale.makeScale(scale.x, scale.y, scale.z);

		this.multiply(mRotation, mScale);

		this.elements.set(12, translation.x);
		this.elements.set(13, translation.y);
		this.elements.set(14, translation.z);
	}

	public void decompose()
	{
		decompose(new Vector3f(), new Quaternion(), new Vector3f());
	}

	public void decompose(Vector3f translation, Quaternion rotation, Vector3f scale)
	{
		// grab the axis vectors
		Vector3f x = __v1;
		Vector3f y = __v2;
		Vector3f z = __v3;

		x.set(this.elements.get(0), this.elements.get(1), this.elements.get(2));
		y.set(this.elements.get(4), this.elements.get(5), this.elements.get(6));
		z.set(this.elements.get(8), this.elements.get(9), this.elements.get(10));

		scale.x = x.length();
		scale.y = y.length();
		scale.z = z.length();

		translation.x = this.elements.get(12);
		translation.y = this.elements.get(13);
		translation.z = this.elements.get(14);

		// scale the rotation part

		Matrix4f matrix = __m1;

		matrix.copy(this);

		matrix.elements.set(0, matrix.elements.get(0) / scale.x);
		matrix.elements.set(1, matrix.elements.get(1) / scale.x);
		matrix.elements.set(2, matrix.elements.get(2) / scale.x);

		matrix.elements.set(4, matrix.elements.get(4) / scale.y);
		matrix.elements.set(5, matrix.elements.get(5) / scale.y);
		matrix.elements.set(6, matrix.elements.get(6) / scale.y);

		matrix.elements.set(8, matrix.elements.get(8) / scale.z);
		matrix.elements.set(9, matrix.elements.get(9) / scale.z);
		matrix.elements.set(10, matrix.elements.get(10) / scale.z);

		rotation.setFromRotationMatrix(matrix);

		// return [ translation, rotation, scale ];
	}

	public Matrix4f extractPosition(Matrix4f m)
	{
		Float32Array me = m.elements;

		this.elements.set(12, me.get(12));
		this.elements.set(13, me.get(13));
		this.elements.set(14, me.get(14));
		
		return this;
	}

	// todo check if needed
	public void extractRotation(Matrix4f m, Vector3f s)
	{
		float scaleX = 1 / s.x, scaleY = 1 / s.y, scaleZ = 1 / s.z;
		
		this.elements.set(0, this.elements.get(0) * scaleX);
		this.elements.set(1, this.elements.get(1) * scaleX);
		this.elements.set(2, this.elements.get(2) * scaleX);

		this.elements.set(4, this.elements.get(4) * scaleY);
		this.elements.set(5, this.elements.get(5) * scaleY);
		this.elements.set(6, this.elements.get(6) * scaleY);

		this.elements.set(8, this.elements.get(8) * scaleZ);
		this.elements.set(9, this.elements.get(9) * scaleZ);
		this.elements.set(10, this.elements.get(10) * scaleZ);
	}
	
	public void extractRotation(Matrix4f m)
	{
		Float32Array me = m.elements;

		Vector3f vector = Matrix4f.__v1;

		float scaleX = 1.0f / vector.set(me.get(0), me.get(1), me.get(2)).length();
		float scaleY = 1.0f / vector.set(me.get(4), me.get(5), me.get(6)).length();
		float scaleZ = 1.0f / vector.set(me.get(8), me.get(9), me.get(10)).length();

		this.elements.set(0, me.get(0) * scaleX);
		this.elements.set(1, me.get(1) * scaleX);
		this.elements.set(2, me.get(2) * scaleX);

		this.elements.set(4, me.get(4) * scaleY);
		this.elements.set(5, me.get(5) * scaleY);
		this.elements.set(6, me.get(6) * scaleY);

		this.elements.set(8, me.get(8) * scaleZ);
		this.elements.set(9, me.get(9) * scaleZ);
		this.elements.set(10, me.get(10) * scaleZ);
	}

	public void translate(Vector3f v)
	{
		float x = v.x, y = v.y, z = v.z;

		this.elements.set(12, this.elements.get(0) * x + this.elements.get(4) * y + this.elements.get(8) * z + this.elements.get(12));
		this.elements.set(13, this.elements.get(1) * x + this.elements.get(5) * y + this.elements.get(9) * z + this.elements.get(13));
		this.elements.set(14, this.elements.get(2) * x + this.elements.get(6) * y + this.elements.get(10) * z + this.elements.get(14));
		this.elements.set(15, this.elements.get(3) * x + this.elements.get(7) * y + this.elements.get(11) * z + this.elements.get(15));
	}

	public void rotateX(float angle)
	{
		float m12 = this.elements.get(4);
		float m22 = this.elements.get(5);
		float m32 = this.elements.get(6);
		float m42 = this.elements.get(7);
		float m13 = this.elements.get(8);
		float m23 = this.elements.get(9);
		float m33 = this.elements.get(10);
		float m43 = this.elements.get(11);

		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);

		this.elements.set(4, c * m12 + s * m13);
		this.elements.set(5, c * m22 + s * m23);
		this.elements.set(6, c * m32 + s * m33);
		this.elements.set(7, c * m42 + s * m43);

		this.elements.set(8, c * m13 - s * m12);
		this.elements.set(9, c * m23 - s * m22);
		this.elements.set(10, c * m33 - s * m32);
		this.elements.set(11, c * m43 - s * m42);
	}

	public void rotateY(float angle)
	{
		float m11 = this.elements.get(0);
		float m21 = this.elements.get(1);
		float m31 = this.elements.get(2);
		float m41 = this.elements.get(3);
		float m13 = this.elements.get(8);
		float m23 = this.elements.get(9);
		float m33 = this.elements.get(10);
		float m43 = this.elements.get(11);

		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);

		this.elements.set(0, c * m11 + s * m13);
		this.elements.set(1, c * m21 + s * m23);
		this.elements.set(2, c * m31 + s * m33);
		this.elements.set(3, c * m41 + s * m43);

		this.elements.set(8, c * m13 - s * m11);
		this.elements.set(9, c * m23 - s * m21);
		this.elements.set(10, c * m33 - s * m31);
		this.elements.set(11, c * m43 - s * m41);
	}

	public void rotateZ(float angle)
	{
		float m11 = this.elements.get(0);
		float m21 = this.elements.get(1);
		float m31 = this.elements.get(2);
		float m41 = this.elements.get(3);
		float m12 = this.elements.get(4);
		float m22 = this.elements.get(5);
		float m32 = this.elements.get(6);
		float m42 = this.elements.get(7);

		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);

		this.elements.set(0, c * m11 + s * m12);
		this.elements.set(1, c * m21 + s * m22);
		this.elements.set(2, c * m31 + s * m32);
		this.elements.set(3, c * m41 + s * m42);

		this.elements.set(4, c * m12 - s * m11);
		this.elements.set(5, c * m22 - s * m21);
		this.elements.set(6, c * m32 - s * m31);
		this.elements.set(7, c * m42 - s * m41);
	}

	public void rotateByAxis(Vector3f axis, float angle)
	{

		// optimize by checking axis

		if (axis.x == 1 && axis.y == 0 && axis.z == 0) {

			this.rotateX(angle);
			return;

		} else if (axis.x == 0 && axis.y == 1 && axis.z == 0) {

			this.rotateY(angle);
			return;

		} else if (axis.x == 0 && axis.y == 0 && axis.z == 1) {

			this.rotateZ(angle);
			return;
		}

		float x = axis.x, y = axis.y, z = axis.z;
		float n = (float) Math.sqrt(x * x + y * y + z * z);

		x /= n;
		y /= n;
		z /= n;

		float xx = x * x, yy = y * y, zz = z * z;
		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		float oneMinusCosine = 1 - c;
		float xy = x * y * oneMinusCosine;
		float xz = x * z * oneMinusCosine;
		float yz = y * z * oneMinusCosine;
		float xs = x * s;
		float ys = y * s;
		float zs = z * s;

		float r11 = xx + (1 - xx) * c;
		float r21 = xy + zs;
		float r31 = xz - ys;
		float r12 = xy - zs;
		float r22 = yy + (1 - yy) * c;
		float r32 = yz + xs;
		float r13 = xz + ys;
		float r23 = yz - xs;
		float r33 = zz + (1 - zz) * c;

		float m11 = this.elements.get(0), m21 = this.elements.get(1), m31 = this.elements.get(2), m41 = this.elements.get(3);
		float m12 = this.elements.get(4), m22 = this.elements.get(5), m32 = this.elements.get(6), m42 = this.elements.get(7);
		float m13 = this.elements.get(8), m23 = this.elements.get(9), m33 = this.elements.get(10), m43 = this.elements.get(11);

		this.elements.set(0, r11 * m11 + r21 * m12 + r31 * m13);
		this.elements.set(1, r11 * m21 + r21 * m22 + r31 * m23);
		this.elements.set(2, r11 * m31 + r21 * m32 + r31 * m33);
		this.elements.set(3, r11 * m41 + r21 * m42 + r31 * m43);

		this.elements.set(4, r12 * m11 + r22 * m12 + r32 * m13);
		this.elements.set(5, r12 * m21 + r22 * m22 + r32 * m23);
		this.elements.set(6, r12 * m31 + r22 * m32 + r32 * m33);
		this.elements.set(7, r12 * m41 + r22 * m42 + r32 * m43);

		this.elements.set(8, r13 * m11 + r23 * m12 + r33 * m13);
		this.elements.set(9, r13 * m21 + r23 * m22 + r33 * m23);
		this.elements.set(10, r13 * m31 + r23 * m32 + r33 * m33);
		this.elements.set(11, r13 * m41 + r23 * m42 + r33 * m43);
	}

	public void scale(Vector3f v)
	{
		float x = v.x, y = v.y, z = v.z;

		this.elements.set(0,  (this.elements.get(0)  * x));
		this.elements.set(1,  (this.elements.get(1)  * x));
		this.elements.set(2,  (this.elements.get(2)  * x));
		this.elements.set(3,  (this.elements.get(3)  * x));
		
		this.elements.set(4,  (this.elements.get(4)  * y));
		this.elements.set(5,  (this.elements.get(5)  * y));
		this.elements.set(6,  (this.elements.get(6)  * y));
		this.elements.set(7,  (this.elements.get(7)  * y));
		
		this.elements.set(8,  (this.elements.get(8)  * z));
		this.elements.set(9,  (this.elements.get(9)  * z));
		this.elements.set(10, (this.elements.get(10) * z));
		this.elements.set(11, (this.elements.get(11) * z));
	}

	public float getMaxScaleOnAxis()
	{
		float scaleXSq = this.elements.get(0) * this.elements.get(0) + this.elements.get(1) * this.elements.get(1) + this.elements.get(2) * this.elements.get(2);
		float scaleYSq = this.elements.get(4) * this.elements.get(4) + this.elements.get(5) * this.elements.get(5) + this.elements.get(6) * this.elements.get(6);
		float scaleZSq = this.elements.get(8) * this.elements.get(8) + this.elements.get(9)	* this.elements.get(9) + this.elements.get(10) * this.elements.get(10);

		return (float) Math.sqrt(Math.max(scaleXSq, Math.max(scaleYSq, scaleZSq)));
	}

	public Matrix4f makeTranslation(float x, float y, float z)
	{
		this.set(
			1f, 0f, 0f, x, 
			0f, 1f, 0f, y, 
			0f, 0f, 1f, z, 
			0f, 0f, 0f, 1f
		);
		
		return this;
	}

	public Matrix4f makeRotationX(float theta)
	{
		float c = (float) Math.cos(theta), s = (float) Math.sin(theta);

		this.set(
			1f, 0f, 0f, 0f, 
			0f, c,  -s, 0f, 
			0f, s,   c, 0f, 
			0f, 0f, 0f, 1f
		);
		
		return this;
	}

	public Matrix4f makeRotationY(float theta)
	{
		float c = (float) Math.cos(theta), s = (float) Math.sin(theta);

		this.set(
			c,  0f, s,  0f, 
			0f, 1f, 0f, 0f, 
			-s, 0f,  c, 0f, 
			0f, 0f, 0f, 1f
		);
		
		return this;
	}

	public Matrix4f makeRotationZ(float theta)
	{
		float c = (float) Math.cos(theta), s = (float) Math.sin(theta);

		this.set(
			c,  -s, 0f, 0f, 
			s,   c, 0f, 0f, 
			0f, 0f, 1f, 0f, 
			0f, 0f, 0f, 1f
		);

		return this;
	}

	public Matrix4f makeRotationAxis(Vector3f axis, float angle)
	{
		// Based on http://www.gamedev.net/reference/articles/article1199.asp

		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		float t = 1.0f - c;
		float x = axis.getX(), y = axis.getY(), z = axis.getZ();
		float tx = t * x, ty = t * y;

		this.set(
			(tx * x + c),     (tx * y - s * z), (tx * z + s * y), 0f, 
			(tx * y + s * z),     (ty * y + c), (ty * z - s * x), 0f, 
			(tx * z - s * y), (ty * z + s * x),	(t * z * z + c),  0f,
			              0f,               0f,               0f, 1f
		);
		
		return this;
	}

	public Matrix4f makeScale(float x, float y, float z)
	{
		return this.set(
			x, 0f, 0f, 0f, 
			0f, y, 0f, 0f, 
			0f, 0f, z, 0f, 
			0f, 0f, 0f, 1f
		);
	}

	public Matrix4f makeFrustum(float left, float right, float bottom, float top, float near, float far)
	{
		Float32Array te = this.elements;
		float x = 2.0f * near / ( right - left );
		float y = 2.0f * near / ( top - bottom );

		float a = ( right + left ) / ( right - left );
		float b = ( top + bottom ) / ( top - bottom );
		float c = - ( far + near ) / ( far - near );
		float d = - 2.0f * far * near / ( far - near );

		te.set(0, x);   te.set(4, 0f);  te.set(8, a);     te.set(12, 0f);
		te.set(1, 0f);  te.set(5, y);   te.set(9, b);     te.set(13, 0f);
		te.set(2, 0f);  te.set(6, 0f);  te.set(10, c);    te.set(14, d);
		te.set(3, 0f);  te.set(7, 0f);  te.set(11, - 1f); te.set(15, 0f);

		return this;
	}

	public Matrix4f makePerspective(float fov, float aspect, float near, float far)
	{

		float ymax = (float) (near * Math.tan( fov * Math.PI / 360.0 ));
		float ymin = - ymax;
		float xmin = ymin * aspect;
		float xmax = ymax * aspect;

		return this.makeFrustum( xmin, xmax, ymin, ymax, near, far );
	}

	public void makeOrthographic(float left, float right, float bottom, float top, float near,
			float far)
	{
		float w = right - left;
		float h = top - bottom;
		float p = far - near;

		float x = (right + left) / w;
		float y = (top + bottom) / h;
		float z = (far + near) / p;

		set(
			(2.0f / w), 0f, 0f, 0f, 
			0f, (2.0f / h), 0f, 0f, 
			0f, 0f, (-2.0f / p), 0f, 
			-x, -y, -z, 1f
		);
	}

	public Matrix4f clone()
	{

		Float32Array te = this.elements;

		return new Matrix4f(
			te.get(0), te.get(4), te.get(8), te.get(12), 
			te.get(1), te.get(5), te.get(9), te.get(13), 
			te.get(2), te.get(6), te.get(10), te.get(14), 
			te.get(3), te.get(7), te.get(11), te.get(15)
		);

	}
	
	public String toString() {
		String retval = "[";
		
		for(int i = 0; i < this.elements.getLength(); i++)
			retval += this.elements.get(i) + ", ";
		
		return retval + "]";
	}
}
