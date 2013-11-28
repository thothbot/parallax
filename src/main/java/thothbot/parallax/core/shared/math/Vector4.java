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

/**
 * This class is realization of (X, Y, Z, W) vector. 
 * Where:
 * X - x coordinate of the vector.
 * Y - y coordinate of the vector.
 * Z - z coordinate of the vector.
 * W - w coordinate of the vector.
 * 
 * @author thothbot
 */
public class Vector4 extends Vector3 implements Vector
{
	/**
	 * The W-coordinate
	 */
	protected double w;

	/**
	 * This default constructor will initialize vector (0, 0, 0, 1); 
	 */
	public Vector4() 
	{
		this(0, 0, 0, 1.0);
	}

	/**
	 * This constructor will initialize vector (X, Y, Z, 1) from the specified 
	 * X, Y, Z coordinates.
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param z the Z coordinate
	 */
	public Vector4(double x, double y, double z) 
	{
		this(x, y, z, 1.0);
	}

	/**
	 * This constructor will initialize vector (X, Y, Z, W) from the specified 
	 * X, Y, Z, W coordinates.
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param z the Z coordinate
	 * @param w the W coordinate
	 */
	public Vector4(double x, double y, double z, double w) 
	{
		super(x, y, z);
		this.w = w;
	}

	/**
	 * get W coordinate from the vector
	 * 
	 * @return a W coordinate
	 */
	public double getW() 
	{
		return w;
	}
	
	/**
	 * This method will add specified value to W coordinate of the vector.
	 * In another words: w += value.
	 * 
	 * @param w the W coordinate
	 */
	public void addW(double w)
	{
		this.w += w;
	}
	
	/**
	 * This method sets W coordinate of the vector.
	 * 
	 * @param w the W coordinate
	 */
	public void setW(double w) 
	{
		this.w = w;
	}

	/**
	 * Set value of the vector from another vector.
	 * 
	 * @param v the other vector
	 * 
	 * @return the current vector
	 */
	public Vector4 copy(Vector4 v)
	{
		return this.set(v.getX(), v.getY(), v.getZ(), v.getW());
	}
	
	/**
	 * Set value of the vector from another vector.
	 * 
	 * @param v the other vector
	 * 
	 * @return the current vector
	 */
	public Vector4 copy(Vector3 v)
	{
		return this.set(v.getX(), v.getY(), v.getZ(), 1.0);
	}
	
	/**
	 * Sets the value of this vector to the specified xyzw coordinates.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 * @param w
	 *            the w coordinate
	 */
	public Vector4 set(double x, double y, double z, double w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	public Vector4 add(Vector4 v)
	{
		return this.add(this, v);
	}
	
	/**
	 * Sets the value of this vector to the sum of vectors v1 and v2.
	 * 
	 * @param v1
	 *            the first vector
	 * @param v2
	 *            the second vector
	 */
	public Vector4 add(Vector4 v1, Vector4 v2)
	{
		this.x = v1.x + v2.x;
		this.y = v1.y + v2.y;
		this.z = v1.z + v2.z;
		this.w = v1.w + v2.w;
		return this;
	}

	/**
	 * Sets the value of this vector to the sum of itself and v1.
	 * 
	 * @param v
	 *            the other vector
	 */
	@Override
	public Vector4 add(Vector v)
	{
		return this.add(this, (Vector4) v);
	}

	public Vector4 addScalar(double s)
	{
		this.x += s;
		this.y += s;
		this.z += s;
		this.w += s;
		return this;
	}
	
	public Vector4 sub(Vector4 v)
	{
		return this.sub(this, v);
	}
	
	/**
	 * Sets the value of this vector to the difference of vectors v1 and v2
	 * (this = v1 - v2).
	 * 
	 * @param v1
	 *            the first vector
	 * @param v2
	 *            the second vector
	 */
	public Vector4 sub(Vector4 v1, Vector4 v2)
	{
		this.x = v1.x - v2.x;
		this.y = v1.y - v2.y;
		this.z = v1.z - v2.z;
		this.w = v1.w - v2.w;
		return this;
	}

	/**
	 * Sets the value of this vector to the difference of itself and v1 (this =
	 * this - v).
	 * 
	 * @param v
	 *            the other vector
	 */
	@Override
	public Vector4 sub(Vector v)
	{
		return this.sub(this, (Vector4)v);
	}

	public Vector4 multiply(Vector4 v)
	{
		return this.multiply(this, v);
	}
	
	/**
	 * Sets the value of this vector to the scalar multiplication of the scale
	 * factor with this.
	 * 
	 * @param s
	 *            the scalar value
	 */
	@Override
	public Vector4 multiply(double s)
	{
		this.x *= s;
		this.y *= s;
		this.z *= s;
		this.w *= s;
		return this;
	}
	
	public Vector4 multiply(Vector4 v1, Vector4 v2)
	{
		this.x = v1.x * v2.x;
		this.y = v1.y * v2.y;
		this.z = v1.z * v2.z;
		this.w = v1.w * v2.w;

		return this;
	}

	public Vector4 divide(Vector4 v1, Vector4 v2)
	{
		this.x = v1.x / v2.x;
		this.y = v1.y / v2.y;
		this.z = v1.z / v2.z;
		this.w = v1.w / v2.w;
		return this;
	}
	
	public Vector4 divide(Vector4 v)
	{
		return this.divide(this, v);
	}
	
	public Vector4 divide(double s)
	{
		if (s != 0) 
		{

			this.x /= s;
			this.y /= s;
			this.z /= s;
			this.w /= s;

		} 
		else 
		{
			set(0, 0, 0, 1);
		}
		return this;
	}

	/**
	 * Negates the value of this vector in place.
	 */
	public Vector4 negate()
	{
		return this.multiply(-1);
	}

	/**
	 * returns the dot product of this vector and v1
	 * 
	 * @param v1
	 *            the other vector
	 * @return the dot product of this vector and v1
	 */
	public double dot(Vector4 v1)
	{
		return (this.x * v1.x + this.y * v1.y + this.z * v1.z + this.w * v1.w);
	}

	/**
	 * Returns the length of this vector.
	 * 
	 * @return the length of this vector as a double
	 */
	public double length()
	{
		return Math.sqrt(lengthSq());
	}

	/**
	 * Returns the squared length of this vector
	 * 
	 * @return the squared length of this vector as a double
	 */
	public double lengthSq()
	{
		return dot(this);
	}
	
	public double lengthManhattan() 
	{
		return Math.abs( getX() ) + Math.abs( getY() ) + Math.abs( getZ() ) + Math.abs( getW() );
	}

	/**
	 * Normalizes this vector in place.
	 */
	@Override
	public Vector4 normalize()
	{
		divide(length());
		return this;
	}

	/**
	 * This method is not implemented yet.
	 */
	@Override
	public double distanceToSquared(Vector v1)
	{
		return 0;
	}

	/**
	 * This method is not implemented yet.
	 */
	@Override
	public double distanceTo(Vector v)
	{
		return 0;
	}

	public Vector4 setLength(double l)
	{
		this.normalize();
		return multiply(l);
	}

	public Vector4 lerp(Vector4 v1, double alpha)
	{
		this.x += (v1.x - this.x) * alpha;
		this.y += (v1.y - this.y) * alpha;
		this.z += (v1.z - this.z) * alpha;
		this.w += (v1.w - this.w) * alpha;
		
		return this;
	}
	
	/**
	 * <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToAngle/index.htm">www.euclideanspace.com</a>
	 * 
	 * @param q is assumed to be normalized
	 */
	public Vector4 setAxisAngleFromQuaternion( Quaternion q ) 
	{
		this.w = 2 * Math.acos( q.w );

		double s = Math.sqrt( 1 - q.w * q.w );

		if ( s < 0.0001 ) 
		{
			 this.x = 1;
			 this.y = 0;
			 this.z = 0;
		} 
		else
		{
			 this.x = q.x / s;
			 this.y = q.y / s;
			 this.z = q.z / s;
		}

		return this;
	}

	/**
	 * <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToAngle/index.htm">www.euclideanspace.com</a>
	 * 
	 * @param m assumes the upper 3x3 of m is a pure rotation matrix (i.e, unscaled)
	 */
	public Vector4 setAxisAngleFromRotationMatrix( Matrix4 m ) 
	{
		// variables for result
		double angle, x, y, z;
		// margin to allow for rounding errors
		double epsilon = 0.01;	
		// margin to distinguish between 0 and 180 degrees
		double epsilon2 = 0.1;		

		Float32Array te = m.getArray();

		double m11 = te.get(0), m12 = te.get(4), m13 = te.get(8);
		double m21 = te.get(1), m22 = te.get(5), m23 = te.get(9);
		double m31 = te.get(2), m32 = te.get(6), m33 = te.get(10);

		if ( ( Math.abs( m12 - m21 ) < epsilon )
		  && ( Math.abs( m13 - m31 ) < epsilon )
		  && ( Math.abs( m23 - m32 ) < epsilon ) 
		  ) {

			// singularity found
			// first check for identity matrix which must have +1 for all terms
			// in leading diagonal and zero in other terms

			if ( ( Math.abs( m12 + m21 ) < epsilon2 )
			  && ( Math.abs( m13 + m31 ) < epsilon2 )
			  && ( Math.abs( m23 + m32 ) < epsilon2 )
			  && ( Math.abs( m11 + m22 + m33 - 3 ) < epsilon2 ) 
			  ) {

				// this singularity is identity matrix so angle = 0

				this.set( 1, 0, 0, 0 );

				return this; // zero angle, arbitrary axis

			}

			// otherwise this singularity is angle = 180

			angle = Math.PI;

			double xx = ( m11 + 1 ) / 2;
			double yy = ( m22 + 1 ) / 2;
			double zz = ( m33 + 1 ) / 2;
			double xy = ( m12 + m21 ) / 4;
			double xz = ( m13 + m31 ) / 4;
			double yz = ( m23 + m32 ) / 4;

			// m11 is the largest diagonal term
			if ( ( xx > yy ) && ( xx > zz ) ) 
			{ 
				if ( xx < epsilon ) 
				{
					x = 0;
					y = 0.707106781;
					z = 0.707106781;
				} 
				else 
				{
					x = Math.sqrt( xx );
					y = xy / x;
					z = xz / x;
				}
			} 
			// m22 is the largest diagonal term
			else if ( yy > zz ) 
			{ 
				if ( yy < epsilon ) 
				{
					x = 0.707106781;
					y = 0;
					z = 0.707106781;
				} 
				else 
				{
					y = Math.sqrt( yy );
					x = xy / y;
					z = yz / y;
				}
			} 
			// m33 is the largest diagonal term so base result on this
			else 
			{ 
				if ( zz < epsilon ) 
				{
					x = 0.707106781;
					y = 0.707106781;
					z = 0;
				} 
				else 
				{
					z = Math.sqrt( zz );
					x = xz / z;
					y = yz / z;
				}
			}

			this.set( x, y, z, angle );

			return this; // return 180 deg rotation
		}

		// as we have reached here there are no singularities so we can handle normally

		double s = Math.sqrt( ( m32 - m23 ) * ( m32 - m23 )
						 + ( m13 - m31 ) * ( m13 - m31 )
						 + ( m21 - m12 ) * ( m21 - m12 ) ); // used to normalize

		if ( Math.abs( s ) < 0.001 ) s = 1; 

		// prevent divide by zero, should not happen if matrix is orthogonal and should be
		// caught by singularity test above, but I've left it in just in case

		this.x = ( m32 - m23 ) / s;
		this.y = ( m13 - m31 ) / s;
		this.z = ( m21 - m12 ) / s;
		this.w = Math.acos( ( m11 + m22 + m33 - 1 ) / 2 );

		return this;
	}
	
	public Vector4 min( Vector4 v ) 
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

		if ( this.w > v.w ) 
		{
			this.w = v.w;
		}

		return this;
	}
	
	public Vector4 max( Vector4 v ) 
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

		if ( this.w < v.w ) 
		{
			this.w = v.w;
		}

		return this;
	}
	
	/**
	 * This function assumes min < max, if this assumption isn't true it will not operate correctly
	 */
	public Vector4 clamp( Vector4 min, Vector4 max ) 
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

		if ( this.w < min.w ) 
		{
			this.w = min.w;
		} 
		else if ( this.w > max.w ) 
		{
			this.w = max.w;
		}

		return this;
	}

	public boolean equals( Vector4 v ) 
	{
		return ( ( v.x == this.x ) && ( v.y == this.y ) && ( v.z == this.z ) && ( v.w == this.w ) );
	}

	@Override
	public Vector4 clone() 
	{
		return new Vector4(this.x, this.y, this.z, this.w);
	}
	
	@Override
	public String toString() 
	{
		return "(" + this.x + ", " + this.y + ", " + this.z +  ", " + this.w + ")";
	}
}
