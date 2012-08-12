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

	public void lerp(Vector3 v1, double alpha)
	{
		this.x += (v1.x - this.x) * alpha;
		this.y += (v1.y - this.y) * alpha;
		this.z += (v1.z - this.z) * alpha;
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
		this.setX( v1.y * v2.z - v1.z * v2.y);
		this.setY( v1.z * v2.x - v1.x * v2.z);
		this.setZ( v1.x * v2.y - v1.y * v2.x);
		return this;
	}

	public Vector3 cross(Vector3 v)
	{
		return this.cross(this, v);
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

	public void getPositionFromMatrix(Matrix4 m)
	{
		this.x = m.getArray().get(12);
		this.y = m.getArray().get(13);
		this.z = m.getArray().get(14);
	}

	public Vector3 getRotationFromMatrix(Matrix4 m)
	{
		return getRotationFromMatrix(m, new Vector3(1, 1, 1));
	}

	public Vector3 getRotationFromMatrix(Matrix4 m, Vector3 scale)
	{
		double sx = scale.x;
		double sy = scale.y;
		double sz = scale.z;

		double m11 = m.getArray().get(0) / sx, 
				m12 = m.getArray().get(4) / sy,
				m13 = m.getArray().get(8) / sz;
		double m21 = m.getArray().get(1) / sx, 
				m22 = m.getArray().get(5) / sy, 
				m23 = m.getArray().get(9) / sz;
		double m33 = m.getArray().get(10) / sz;

		this.y = Math.asin(m13);

		double cosY = Math.cos(this.y);

		if (Math.abs(cosY) > 0.00001) 
		{
			this.x = Math.atan2(-m23 / cosY, m33 / cosY);
			this.z = Math.atan2(-m12 / cosY, m11 / cosY);
		} 
		else 
		{
			this.x = 0.0;
			this.z = Math.atan2(m21, m22);
		}

		return this;
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

		this.x = sx;
		this.y = sy;
		this.z = sz;
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
		try 
		{
			return (this.x == v1.x && this.y == v1.y && this.z == v1.z);
		} 
		catch (NullPointerException e2) 
		{
			return false;
		}
	}

	public boolean isZero()
	{
		return (this.lengthSq() < 0.0001 /* almostZero */);
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
