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

/**
 * This class is realization of (X, Y, Z) vector. 
 * Where:
 * X - x coordinate of the vector.
 * Y - y coordinate of the vector.
 * Z - z coordinate of the vector.
 * 
 * @author thothbot
 */
public class Vector3f extends Vector2f implements Vector
{
	/**
	 * The Z-coordinate
	 */
	protected float z;

	/**
	 * This default constructor will initialize vector (0, 0, 0); 
	 */
	public Vector3f() 
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
	public Vector3f(float x, float y, float z) 
	{
		super(x, y);
		this.z = z;
	}

	/**
	 * Getting Z coordinate from the vector
	 * 
	 * @return a Z coordinate
	 */
	public float getZ()
	{
		return this.z;
	}
	
	/**
	 * This method will add specified value to Z coordinate of the vector.
	 * In another words: z += value.
	 * 
	 * @param z the Y coordinate
	 */
	public void addZ(float z)
	{
		this.z += z;
	}
	
	/**
	 * This method sets Z coordinate of the vector.
	 * 
	 * @param z the Z coordinate
	 */
	public void setZ(float z)
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
	public Vector3f copy(Vector3f v)
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
	public Vector3f set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	@Override
	public Vector3f add(Vector v)
	{
		return this.add(this, v);
	}
	
	@Override
	public Vector3f add(Vector v1, Vector v2)
	{
		this.setX(((Vector3f)v1).getX() + ((Vector3f)v2).getX());
		this.setY(((Vector3f)v1).getY() + ((Vector3f)v2).getY());
		this.setZ(((Vector3f)v1).getZ() + ((Vector3f)v2).getZ());
		return this;
	}

	@Override
	public Vector3f add(float s)
	{
		this.addX(s);
		this.addY(s);
		this.addZ(s);
		return this;
	}
	
	@Override
	public Vector3f sub(Vector v)
	{
		return this.sub(this, v);
	}
	
	@Override
	public Vector3f sub(Vector v1, Vector v2)
	{
		this.setX(((Vector3f)v1).getX() - ((Vector3f)v2).getX());
		this.setY(((Vector3f)v1).getY() - ((Vector3f)v2).getY());
		this.setZ(((Vector3f)v1).getZ() - ((Vector3f)v2).getZ());
		return this;
	}
	
	@Override
	public Vector3f multiply(Vector v)
	{
		return this.multiply(this, v);
	}
	
	@Override
	public Vector3f multiply(Vector v1, Vector v2)
	{
		this.setX(((Vector3f)v1).getX() * ((Vector3f)v2).getX());
		this.setY(((Vector3f)v1).getY() * ((Vector3f)v2).getY());
		this.setZ(((Vector3f)v1).getZ() * ((Vector3f)v2).getZ());
		return this;
	}

	@Override
	public Vector3f multiply(float s)
	{
		this.x *= s;
		this.y *= s;
		this.z *= s;
		return this;
	}
	
	@Override
	public Vector3f divide(Vector v)
	{
		return this.divide(this, v);
	}
	
	@Override
	public Vector3f divide(Vector v1, Vector v2)
	{
		this.setX(((Vector3f)v1).getX() / ((Vector3f)v2).getX());
		this.setY(((Vector3f)v1).getY() / ((Vector3f)v2).getY());
		this.setZ(((Vector3f)v1).getZ() / ((Vector3f)v2).getZ());
		return this;
	}

	@Override
	public Vector3f divide(float s)
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
	public Vector3f negate()
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
	public float dot(Vector3f v1)
	{
		return (this.x * v1.x + this.y * v1.y + this.z * v1.z);
	}

	/**
	 * Returns the squared length of this vector.
	 * 
	 * @return the squared length of this vector
	 */
	public float lengthSq()
	{
		return dot(this);
	}

	/**
	 * Returns the length of this vector.
	 * 
	 * @return the length of this vector
	 */
	public float length()
	{
		return (float) Math.sqrt(lengthSq());
	}

	public float lengthManhattan()
	{
		return (float) (Math.abs(this.x) + Math.abs(this.y) + Math.abs(this.z));
	}

	/**
	 * Normalizes this vector in place.
	 */
	@Override
	public Vector3f normalize()
	{
		float len = this.length();
		if (len > 0)
		{
			this.multiply(1.0f / len);
		} 
		else 
		{
			this.setX(0);
			this.setY(0);
			this.setZ(0);
		}

		return this;
	}

	public void setLength(float l)
	{
		this.normalize();
		multiply(l);
	}

	public void lerp(Vector3f v1, float alpha)
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
	public Vector3f cross(Vector3f v1, Vector3f v2)
	{
		this.setX( v1.y * v2.z - v1.z * v2.y);
		this.setY( v1.z * v2.x - v1.x * v2.z);
		this.setZ( v1.x * v2.y - v1.y * v2.x);
		return this;
	}

	public Vector3f cross(Vector3f v)
	{
		return this.cross(this, v);
	}

	@Override
	public float distanceToSquared(Vector v1)
	{
		float dx = this.getX() - ((Vector3f)v1).getX();
		float dy = this.getY() - ((Vector3f)v1).getY();
		float dz = this.getZ() - ((Vector3f)v1).getZ();
		return (dx * dx + dy * dy + dz * dz);
	}

	@Override
	public float distanceTo(Vector v1)
	{
		return (float) Math.sqrt(distanceToSquared(v1));
	}

	public void getPositionFromMatrix(Matrix4f m)
	{
		this.x = m.elements.get(12);
		this.y = m.elements.get(13);
		this.z = m.elements.get(14);
	}

	public Vector3f getRotationFromMatrix(Matrix4f m)
	{
		return getRotationFromMatrix(m, new Vector3f(1f, 1f, 1f));
	}

	public Vector3f getRotationFromMatrix(Matrix4f m, Vector3f scale)
	{
		float sx = scale.x;
		float sy = scale.y;
		float sz = scale.z;

		float m11 = m.elements.get(0) / sx, m12 = m.elements.get(4) / sy, m13 = m.elements.get(8)
				/ sz;
		float m21 = m.elements.get(1) / sx, m22 = m.elements.get(5) / sy, m23 = m.elements.get(9)
				/ sz;
		float m33 = m.elements.get(10) / sz;

		this.y = (float) Math.asin(m13);

		float cosY = (float) Math.cos(this.y);

		if (Math.abs(cosY) > 0.00001) {

			this.x = (float) Math.atan2(-m23 / cosY, m33 / cosY);
			this.z = (float) Math.atan2(-m12 / cosY, m11 / cosY);

		} else {

			this.x = 0f;
			this.z = (float) Math.atan2(m21, m22);
		}

		return this;
	}

	public void getScaleFromMatrix(Matrix4f m)
	{
		Vector3f tmp = new Vector3f();
		tmp.set(m.elements.get(0), m.elements.get(1), m.elements.get(2));
		float sx = tmp.length();

		tmp.set(m.elements.get(4), m.elements.get(5), m.elements.get(6));
		float sy = tmp.length();

		tmp.set(m.elements.get(8), m.elements.get(9), m.elements.get(10));
		float sz = tmp.length();

		this.x = sx;
		this.y = sy;
		this.z = sz;
	}

	/**
	 * Returns true if all of the data members of v1 are equal to the
	 * corresponding data members in this Vector3f.
	 * 
	 * @param v1
	 *            the vector with which the comparison is made
	 * @return true or false
	 */
	public boolean equals(Vector3f v1)
	{
		try {
			return (this.x == v1.x && this.y == v1.y && this.z == v1.z);
		} catch (NullPointerException e2) {
			return false;
		}
	}

	public boolean isZero()
	{
		return (this.lengthSq() < 0.0001 /* almostZero */);
	}

	@Override
	public Vector3f clone()
	{
		return new Vector3f(this.getX(), this.getY(), this.getZ());
	}
	
	@Override
	public String toString() 
	{
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
}
