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
 * This class is realization of (X, Y, Z) vector. 
 * Where:
 * X - x coordinate of the vector.
 * Y - y coordinate of the vector.
 * Z - z coordinate of the vector.
 * 
 * @author thothbot
 */
public class Vector3 extends Vector2 implements Vector
{
	/**
	 * The Z-coordinate
	 */
	protected double z;

	/**
	 * This default constructor will initialize vector (0, 0, 0); 
	 */
	public Vector3() 
	{
		this(0, 0, 0);
	}

	/**
	 * This constructor will initialize vector (X, Y, Z) from the specified 
	 * X, Y, Z coordinates.
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param z the Z coordinate
	 */
	public Vector3(double x, double y, double z) 
	{
		super(x, y);
		this.z = z;
	}

	/**
	 * get Z coordinate from the vector
	 * 
	 * @return a Z coordinate
	 */
	public double getZ()
	{
		return this.z;
	}
	
	/**
	 * This method will add specified value to Z coordinate of the vector.
	 * In another words: z += value.
	 * 
	 * @param z the Y coordinate
	 */
	public void addZ(double z)
	{
		this.z += z;
	}
	
	/**
	 * This method sets Z coordinate of the vector.
	 * 
	 * @param z the Z coordinate
	 */
	public void setZ(double z)
	{
		this.z = z;
	}
	
	/**
	 * Set value of the vector from another vector.
	 * 
	 * @param v the other vector
	 * 
	 * @return the current vector
	 */
	public Vector3 copy(Vector3 v)
	{
		this.set(v.getX(), v.getY(), v.getZ());
		return this;
	}
	
	/**
	 * Set value of the vector to the specified (X, Y, Z) coordinates.
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param z the Z coordinate
	 */
	public Vector3 set(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	/**
	 * Set value of the vector to the specified (A, A, A) coordinates.
	 * 
	 * @param a the X, Y and Z coordinate
	 */
	public Vector3 set(double a)
	{
		this.x = a;
		this.y = a;
		this.z = a;
		return this;
	}

	@Override
	public Vector3 add(Vector v)
	{
		return this.add(this, v);
	}
	
	@Override
	public Vector3 add(Vector v1, Vector v2)
	{
		this.setX(((Vector3)v1).getX() + ((Vector3)v2).getX());
		this.setY(((Vector3)v1).getY() + ((Vector3)v2).getY());
		this.setZ(((Vector3)v1).getZ() + ((Vector3)v2).getZ());
		return this;
	}

	@Override
	public Vector3 add(double s)
	{
		this.addX(s);
		this.addY(s);
		this.addZ(s);
		return this;
	}
	
	@Override
	public Vector3 sub(Vector v)
	{
		return this.sub(this, v);
	}
	
	@Override
	public Vector3 sub(Vector v1, Vector v2)
	{
		this.setX(((Vector3)v1).getX() - ((Vector3)v2).getX());
		this.setY(((Vector3)v1).getY() - ((Vector3)v2).getY());
		this.setZ(((Vector3)v1).getZ() - ((Vector3)v2).getZ());
		return this;
	}
	
	@Override
	public Vector3 multiply(Vector v)
	{
		return this.multiply(this, v);
	}
	
	@Override
	public Vector3 multiply(Vector v1, Vector v2)
	{
		this.setX(((Vector3)v1).getX() * ((Vector3)v2).getX());
		this.setY(((Vector3)v1).getY() * ((Vector3)v2).getY());
		this.setZ(((Vector3)v1).getZ() * ((Vector3)v2).getZ());
		return this;
	}

	@Override
	public Vector3 multiply(double s)
	{
		this.x *= s;
		this.y *= s;
		this.z *= s;
		return this;
	}
	
	public Vector3 apply( Matrix3 m ) 
	{
		double x = this.x;
		double y = this.y;
		double z = this.z;

		Float32Array e = m.getArray();

		this.x = e.get(0) * x + e.get(3) * y + e.get(6) * z;
		this.y = e.get(1) * x + e.get(4) * y + e.get(7) * z;
		this.z = e.get(2) * x + e.get(5) * y + e.get(8) * z;

		return this;
	}

	public Vector3 apply( Matrix4 m ) 
	{

		// input: THREE.Matrix4 affine matrix

		double x = this.x, y = this.y, z = this.z;

		Float32Array e = m.getArray();

		this.x = e.get(0) * x + e.get(4) * y + e.get(8)  * z + e.get(12);
		this.y = e.get(1) * x + e.get(5) * y + e.get(9)  * z + e.get(13);
		this.z = e.get(2) * x + e.get(6) * y + e.get(10) * z + e.get(14);

		return this;
	}

//	public Vector3 applyProjection: function ( m ) {
//
//		// input: THREE.Matrix4 projection matrix
//
//		var x = this.x, y = this.y, z = this.z;
//
//		var e = m.elements;
//		var d = 1 / ( e[3] * x + e[7] * y + e[11] * z + e[15] ); // perspective divide
//
//		this.x = ( e[0] * x + e[4] * y + e[8]  * z + e[12] ) * d;
//		this.y = ( e[1] * x + e[5] * y + e[9]  * z + e[13] ) * d;
//		this.z = ( e[2] * x + e[6] * y + e[10] * z + e[14] ) * d;
//
//		return this;
//
//	},

	public Vector3 apply( Quaternion q ) 
	{
		double x = this.x;
		double y = this.y;
		double z = this.z;

		double qx = q.x;
		double qy = q.y;
		double qz = q.z;
		double qw = q.w;

		// calculate quat * vector

		double ix =  qw * x + qy * z - qz * y;
		double iy =  qw * y + qz * x - qx * z;
		double iz =  qw * z + qx * y - qy * x;
		double iw = -qx * x - qy * y - qz * z;

		// calculate result * inverse quat

		this.x = ix * qw + iw * -qx + iy * -qz - iz * -qy;
		this.y = iy * qw + iw * -qy + iz * -qx - ix * -qz;
		this.z = iz * qw + iw * -qz + ix * -qy - iy * -qx;

		return this;
	}

//	applyEuler: function ( v, eulerOrder ) {
//
//		var quaternion = THREE.Vector3.__q1.setFromEuler( v, eulerOrder );
//
//		this.applyQuaternion( quaternion );
//
//		return this;
//
//	},

//	applyAxisAngle: function ( axis, angle ) {
//
//		var quaternion = THREE.Vector3.__q1.setFromAxisAngle( axis, angle );
//
//		this.applyQuaternion( quaternion );
//
//		return this;
//
//	},

	
	@Override
	public Vector3 divide(Vector v)
	{
		return this.divide(this, v);
	}
	
	@Override
	public Vector3 divide(Vector v1, Vector v2)
	{
		this.setX(((Vector3)v1).getX() / ((Vector3)v2).getX());
		this.setY(((Vector3)v1).getY() / ((Vector3)v2).getY());
		this.setZ(((Vector3)v1).getZ() / ((Vector3)v2).getZ());
		return this;
	}

	@Override
	public Vector3 divide(double s)
	{
		if (s != 0) 
		{
			this.x /= s;
			this.y /= s;
			this.z /= s;
		} 
		else 
		{
			this.set(0, 0, 1);
		}

		return this;
	}

	@Override
	public Vector3 negate()
	{
		return this.multiply(-1);
	}
	
	/**
	 * Computes the dot product of this vector and vector v1.
	 * 
	 * @param v1
	 *            the other vector
	 * @return the dot product of this vector and v1
	 */
	public double dot(Vector3 v1)
	{
		return (this.x * v1.x + this.y * v1.y + this.z * v1.z);
	}

	/**
	 * Returns the squared length of this vector.
	 * 
	 * @return the squared length of this vector
	 */
	public double lengthSq()
	{
		return dot(this);
	}

	/**
	 * Returns the length of this vector.
	 * 
	 * @return the length of this vector
	 */
	public double length()
	{
		return Math.sqrt(lengthSq());
	}

	public double lengthManhattan()
	{
		return Math.abs(this.x) + Math.abs(this.y) + Math.abs(this.z);
	}

	/**
	 * Normalizes this vector in place.
	 */
	@Override
	public Vector3 normalize()
	{
		double len = this.length();
		if (len > 0)
		{
			this.multiply(1.0 / len);
		} 
		else 
		{
			this.setX(0);
			this.setY(0);
			this.setZ(0);
		}

		return this;
	}

	public Vector3 setLength(double l)
	{
		normalize();
		return multiply(l);
	}

	public Vector3 lerp(Vector3 v1, double alpha)
	{
		this.x += (v1.x - this.x) * alpha;
		this.y += (v1.y - this.y) * alpha;
		this.z += (v1.z - this.z) * alpha;
		
		return this;
	}

	/**
	 * Sets this vector to be the vector cross product of vectors v1 and v2.
	 * 
	 * @param v1
	 *            the first vector
	 * @param v2
	 *            the second vector
	 */
	public Vector3 cross(Vector3 v1, Vector3 v2)
	{
		double x = v1.y * v2.z - v1.z * v2.y;
		double y = v1.z * v2.x - v1.x * v2.z;
		double z = v1.x * v2.y - v1.y * v2.x;
		this.set(x, y, z);
		return this;
	}
	
	public Vector3 cross(Vector3 v)
	{
		return cross(this, v);
	}

	@Override
	public double distanceToSquared(Vector v1)
	{
		double dx = this.getX() - ((Vector3)v1).getX();
		double dy = this.getY() - ((Vector3)v1).getY();
		double dz = this.getZ() - ((Vector3)v1).getZ();
		return (dx * dx + dy * dy + dz * dz);
	}

	@Override
	public double distanceTo(Vector v1)
	{
		return Math.sqrt(distanceToSquared(v1));
	}
	
	public double angleTo( Vector3 v ) 
	{
		return Math.acos( this.dot( v ) / this.length() / v.length() );
	}

	public void getPositionFromMatrix(Matrix4 m)
	{
		this.x = m.getArray().get(12);
		this.y = m.getArray().get(13);
		this.z = m.getArray().get(14);
	}

	public void getScaleFromMatrix(Matrix4 m)
	{
		Vector3 tmp = new Vector3();
		tmp.set(m.getArray().get(0), m.getArray().get(1), m.getArray().get(2));
		double sx = tmp.length();

		tmp.set(m.getArray().get(4), m.getArray().get(5), m.getArray().get(6));
		double sy = tmp.length();

		tmp.set(m.getArray().get(8), m.getArray().get(9), m.getArray().get(10));
		double sz = tmp.length();

		set(sx, sy, sz);
	}
	
	/**
	 * Assumes the upper 3x3 of m is a pure rotation matrix (i.e, unscaled)
	 */
	public Vector3 setEulerFromRotationMatrix( Matrix4 m ) 
	{
		return setEulerFromRotationMatrix(m, Euler.XYZ);
	}

	public Vector3 setEulerFromRotationMatrix( Matrix4 m, Euler order ) 
	{
		Float32Array te = m.getArray();
		double m11 = te.get(0), m12 = te.get(4), m13 = te.get(8);
		double m21 = te.get(1), m22 = te.get(5), m23 = te.get(9);
		double m31 = te.get(2), m32 = te.get(6), m33 = te.get(10);

		if ( order == Euler.XYZ ) 
		{
			setY( Math.asin( Mathematics.clamp( m13, -1, 1 ) ));
		
			if ( Math.abs( m13 ) < 0.99999 ) 
			{
				setX( Math.atan2( - m23, m33 ) );
				setZ( Math.atan2( - m12, m11 ) );
			} 
			else 
			{
				setX( Math.atan2( m32, m22 ) );
				setZ( 0 );
			}

		} 
		else if ( order == Euler.YXZ )
		{
			setX( Math.asin( - Mathematics.clamp( m23, -1, 1 ) ) );
		
			if ( Math.abs( m23 ) < 0.99999 ) 
			{
				setY( Math.atan2( m13, m33 ) );
				setZ( Math.atan2( m21, m22 ) );	
			} 
			else 
			{
				setY( Math.atan2( - m31, m11 ) );
				setZ( 0 );	
			}
		} 
		else if ( order == Euler.ZXY ) 
		{
			setX( Math.asin( Mathematics.clamp( m32, -1, 1 ) ) );
		
			if ( Math.abs( m32 ) < 0.99999 ) 
			{
				setY( Math.atan2( - m31, m33 ) );
				setZ( Math.atan2( - m12, m22 ) );	
			} 
			else 
			{	
				setY( 0 );
				setZ( Math.atan2( m21, m11 ) );	
			}
		} 
		else if ( order == Euler.ZYX ) 
		{
			setY( Math.asin( - Mathematics.clamp( m31, -1, 1 ) ) );
			
			if ( Math.abs( m31 ) < 0.99999 ) 
			{
				setX( Math.atan2( m32, m33 ) );
				setZ( Math.atan2( m21, m11 ) );
			}
			else 
			{
				setX( 0 );
				setZ( Math.atan2( - m12, m22 ) );
			}
		} 
		else if ( order == Euler.YZX ) 
		{
			setZ( Math.asin( Mathematics.clamp( m21, -1, 1 ) ) );
		
			if ( Math.abs( m21 ) < 0.99999 ) 
			{
				setX( Math.atan2( - m23, m22 ) );
				setY( Math.atan2( - m31, m11 ) );
			} 
			else 
			{
				setX( 0 );
				setY( Math.atan2( m13, m33 ) );
			}	
		} 
		else if ( order == Euler.XZY ) 
		{
			setZ( Math.asin( - Mathematics.clamp( m12, -1, 1 ) ) );
		
			if ( Math.abs( m12 ) < 0.99999 ) 
			{
				setX( Math.atan2( m32, m22 ) );
				setY( Math.atan2( m13, m11 ) );
			}
			else 
			{
				setX( Math.atan2( - m23, m33 ) );
				setY( 0 );
			}	
		}
		
		return this;
	}
	
	/**
	 * q is assumed to be normalized
	 * <p>
	 * <a href="http://www.mathworks.com/matlabcentral/fileexchange/20696-function-to-convert-between-dcm-euler-angles-quaternions-and-euler-vectors/content/SpinCalc.m">www.mathworks.com</a> 
	 */
	public Vector3 setEulerFromQuaternion ( Quaternion q )
	{
		return setEulerFromQuaternion(q, Euler.XYZ);
	}

	public Vector3 setEulerFromQuaternion ( Quaternion q, Euler order ) 
	{
		double sqx = q.x * q.x;
		double sqy = q.y * q.y;
		double sqz = q.z * q.z;
		double sqw = q.w * q.w;

		if ( order == Euler.XYZ) 
		{
			setX( Math.atan2( 2 * ( q.x * q.w - q.y * q.z ), ( sqw - sqx - sqy + sqz ) ) );
			setY( Math.asin(  Mathematics.clamp( 2 * ( q.x * q.z + q.y * q.w ), -1, 1 ) ) );
			setZ( Math.atan2( 2 * ( q.z * q.w - q.x * q.y ), ( sqw + sqx - sqy - sqz ) ) );
		} 
		else if ( order == Euler.YXZ ) 
		{
			setX( Math.asin(  Mathematics.clamp( 2 * ( q.x * q.w - q.y * q.z ), -1, 1 ) ) );
			setY( Math.atan2( 2 * ( q.x * q.z + q.y * q.w ), ( sqw - sqx - sqy + sqz ) ) );
			setZ( Math.atan2( 2 * ( q.x * q.y + q.z * q.w ), ( sqw - sqx + sqy - sqz ) ) );
		} 
		else if ( order == Euler.ZXY ) 
		{	
			setX( Math.asin(  Mathematics.clamp( 2 * ( q.x * q.w + q.y * q.z ), -1, 1 ) ) );
			setY( Math.atan2( 2 * ( q.y * q.w - q.z * q.x ), ( sqw - sqx - sqy + sqz ) ) );
			setZ( Math.atan2( 2 * ( q.z * q.w - q.x * q.y ), ( sqw - sqx + sqy - sqz ) ) );
		} 
		else if ( order == Euler.ZYX ) 
		{
			setX( Math.atan2( 2 * ( q.x * q.w + q.z * q.y ), ( sqw - sqx - sqy + sqz ) ) );
			setY( Math.asin(  Mathematics.clamp( 2 * ( q.y * q.w - q.x * q.z ), -1, 1 ) ) );
			setZ( Math.atan2( 2 * ( q.x * q.y + q.z * q.w ), ( sqw + sqx - sqy - sqz ) ) );
		} 
		else if ( order == Euler.YZX ) 
		{
			setX( Math.atan2( 2 * ( q.x * q.w - q.z * q.y ), ( sqw - sqx + sqy - sqz ) ) );
			setY( Math.atan2( 2 * ( q.y * q.w - q.x * q.z ), ( sqw + sqx - sqy - sqz ) ) );
			setZ( Math.asin(  Mathematics.clamp( 2 * ( q.x * q.y + q.z * q.w ), -1, 1 ) ) );	
		} 
		else if ( order == Euler.XZY ) 
		{
			setX( Math.atan2( 2 * ( q.x * q.w + q.y * q.z ), ( sqw - sqx + sqy - sqz ) ) );
			setY( Math.atan2( 2 * ( q.x * q.z + q.y * q.w ), ( sqw + sqx - sqy - sqz ) ) );
			setZ( Math.asin(  Mathematics.clamp( 2 * ( q.z * q.w - q.x * q.y ), -1, 1 ) ) );
		}

		return this;
	}


	/**
	 * Returns true if all of the data members of v1 are equal to the
	 * corresponding data members in this Vector3.
	 * 
	 * @param v1
	 *            the vector with which the comparison is made
	 * @return true or false
	 */
	public boolean equals(Vector3 v1)
	{
		return (this.x == v1.x && this.y == v1.y && this.z == v1.z);
	}

	public boolean isZero()
	{
		return (this.lengthSq() < 0.0001 /* almostZero */);
	}
	
	public Vector3 min( Vector3 v ) 
	{
		if ( this.x > v.x ) 
		{
			this.x = v.x;
		}

		if ( this.y > v.y ) 
		{
			this.y = v.y;
		}

		if ( this.z > v.z ) 
		{
			this.z = v.z;
		}

		return this;
	}

	public Vector3 max( Vector3 v ) 
	{
		if ( this.x < v.x ) 
		{
			this.x = v.x;
		}

		if ( this.y < v.y ) 
		{
			this.y = v.y;
		}

		if ( this.z < v.z ) 
		{
			this.z = v.z;
		}

		return this;
	}
	
	/**
	 * This function assumes min < max, if this assumption isn't true it will not operate correctly
	 */
	public Vector3 clamp( Vector3 min, Vector3 max ) 
	{
		if ( this.x < min.x )
		{
			this.x = min.x;
		} 
		else if ( this.x > max.x ) 
		{
			this.x = max.x;
		}

		if ( this.y < min.y ) 
		{
			this.y = min.y;
		} 
		else if ( this.y > max.y ) 
		{
			this.y = max.y;
		}

		if ( this.z < min.z ) 
		{
			this.z = min.z;
		} 
		else if ( this.z > max.z ) 
		{
			this.z = max.z;
		}

		return this;
	}

	@Override
	public Vector3 clone()
	{
		return new Vector3(this.getX(), this.getY(), this.getZ());
	}
	
	@Override
	public String toString() 
	{
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
}
