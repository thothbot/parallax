/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.shared.core;

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
public class Vector4f extends Vector3f implements Vector
{
	/**
	 * The W-coordinate
	 */
	protected double w;

	/**
	 * This default constructor will initialize vector (0, 0, 0, 1); 
	 */
	public Vector4f() 
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
	public Vector4f(double x, double y, double z) 
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
	public Vector4f(double x, double y, double z, double w) 
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
	public Vector4f copy(Vector4f v)
	{
		return this.set(v.getX(), v.getY(), v.getZ(), 1.0f);
	}
	
	/**
	 * Set value of the vector from another vector.
	 * 
	 * @param v the other vector
	 * 
	 * @return the current vector
	 */
	public Vector4f copy(Vector3f v)
	{
		return this.set(v.getX(), v.getY(), v.getZ(), 1.0f);
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
	public Vector4f set(double x, double y, double z, double w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	public Vector4f add(Vector4f v)
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
	public Vector4f add(Vector4f v1, Vector4f v2)
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
	public Vector4f add(Vector v)
	{
		return this.add(this, (Vector4f) v);
	}

	public Vector4f addScalar(double s)
	{
		this.x += s;
		this.y += s;
		this.z += s;
		this.w += s;
		return this;
	}
	
	public Vector4f sub(Vector4f v)
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
	public Vector4f sub(Vector4f v1, Vector4f v2)
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
	public Vector4f sub(Vector v)
	{
		return this.sub(this, (Vector4f)v);
	}

	public Vector4f multiply(Vector4f v)
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
	public Vector4f multiply(double s)
	{
		this.x *= s;
		this.y *= s;
		this.z *= s;
		this.w *= s;
		return this;
	}
	
	public Vector4f multiply(Vector4f v1, Vector4f v2)
	{
		this.x = v1.x * v2.x;
		this.y = v1.y * v2.y;
		this.z = v1.z * v2.z;
		this.w = v1.w * v2.w;

		return this;
	}

	public Vector4f divide(Vector4f v1, Vector4f v2)
	{
		this.x = v1.x / v2.x;
		this.y = v1.y / v2.y;
		this.z = v1.z / v2.z;
		this.w = v1.w / v2.w;
		return this;
	}
	
	public Vector4f divide(Vector4f v)
	{
		return this.divide(this, v);
	}
	
	public Vector4f divide(double s)
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
	public Vector4f negate()
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
	public double dot(Vector4f v1)
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

	/**
	 * Normalizes this vector in place.
	 */
	@Override
	public Vector4f normalize()
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

	public Vector4f setLength(double l)
	{
		this.normalize();
		return multiply(l);
	}

	public void lerp(Vector4f v1, double alpha)
	{
		this.x += (v1.x - this.x) * alpha;
		this.y += (v1.y - this.y) * alpha;
		this.z += (v1.z - this.z) * alpha;
		this.w += (v1.w - this.w) * alpha;
	}
	
	@Override
	public Vector4f clone() 
	{
		return new Vector4f(this.x, this.y, this.z, this.w);
	}
	
	@Override
	public String toString() 
	{
		return "(" + this.x + ", " + this.y + ", " + this.z +  ", " + this.w + ")";
	}
}
