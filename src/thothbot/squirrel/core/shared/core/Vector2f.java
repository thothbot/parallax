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

public class Vector2f implements Vector
{
	protected float x;
	protected float y;

	public Vector2f() {
	}

	/**
	 * Constructs and initializes a Vector2f from the specified xy coordinates.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void addX(float x)
	{
		this.x += x;
	}

	public void setX(float x)
	{
		this.x = x;
	}

	public float getX()
	{
		return x;
	}

	public void addY(float y)
	{
		this.y += y;
	}

	public void setY(float y)
	{
		this.y = y;
	}

	public float getY()
	{
		return y;
	}

	/**
	 * Sets the value of this vector from the 2 values x and y.
	 * 
	 * @param x
	 * @param y
	 * 
	 */
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector2f copy(Vector2f v)
	{
		this.x = v.x;
		this.y = v.y;
		return this;
	}

	/**
	 * Sets the value of this vector to the vector sum of vectors v1 and v2.
	 * 
	 * @param v1
	 *            the first vector
	 * @param v2
	 *            the second vector
	 */
	public Vector2f add(Vector2f v1, Vector2f v2)
	{
		this.x = v1.x + v2.x;
		this.y = v1.y + v2.y;
		return this;
	}

	/**
	 * Sets the value of this vector to the vector sum of itself and vector v1.
	 * 
	 * @param v1
	 *            the other vector
	 */
	@Override
	public Vector2f add(Vector v)
	{
		return this.add(this, (Vector2f) v);
	}

	/**
	 * Sets the value of this vector to the vector difference of vectors v1 and
	 * v2 (this = v1 - v2).
	 * 
	 * @param v1
	 *            the first vector
	 * @param v2
	 *            the second vector
	 */
	public Vector2f sub(Vector2f v1, Vector2f v2)
	{
		this.x = v1.x - v2.x;
		this.y = v1.y - v2.y;
		return this;
	}

	/**
	 * Sets the value of this vector to the vector difference of itself and
	 * vector v1 (this = this - v1).
	 * 
	 * @param v1
	 *            the other vector
	 */
	@Override
	public Vector2f sub(Vector v)
	{
		return this.sub(this, (Vector2f)v);
	}

	/**
	 * Sets the value of this vector to the scalar multiplication of itself.
	 * 
	 * @param s
	 *            the scalar value
	 */
	@Override
	public Vector2f multiply(float s)
	{
		this.x *= s;
		this.y *= s;
		return this;
	}

	public Vector2f divide(float s)
	{
		if (s != 0) {

			this.x /= s;
			this.y /= s;

		} else {

			this.set(0, 0);

		}
		
		return this;
	}

	/**
	 * Negates the value of this vector in place.
	 */
	public Vector2f negate()
	{
		return this.multiply(-1);
	}

	/**
	 * Computes the dot product of the this vector and vector v1.
	 * 
	 * @param v1
	 *            the other vector
	 */
	public float dot(Vector2f v1)
	{
		return (this.x * v1.x + this.y * v1.y);
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

	/**
	 * Normalizes this vector in place.
	 */
	@Override
	public Vector2f normalize()
	{
		this.divide(length());
		return this;
	}

	public float distanceToSquared(Vector2f v)
	{
		float dx = this.x - v.x;
		float dy = this.y - v.y;
		return (dx * dx + dy * dy);
	}

	@Override
	public float distanceTo(Vector v1)
	{
		return (float) Math.sqrt(distanceToSquared((Vector2f)v1));
	}

	public void setLength(float l)
	{
		normalize();
		multiply(l);
	}

	public void lerp(Vector2f v1, float alpha)
	{
		this.x += (v1.x - this.x) * alpha;
		this.y += (v1.y - this.y) * alpha;
	}

	public boolean isZero()
	{
		return (this.lengthSq() < 0.0001f /* almostZero */);
	}

	@Override
	public Vector2f clone()
	{
		return new Vector2f(this.x, this.y);
	}

	/**
	 * Returns true if all of the data members of Tuple2f t1 are equal to the
	 * corresponding data members in this Tuple2f.
	 * 
	 * @param t1
	 *            the vector with which the comparison is made
	 * @return true or false
	 */
	public boolean equals(Vector2f v1)
	{
		try {
			return (this.x == v1.x && this.y == v1.y);
		} catch (NullPointerException e2) {
			return false;
		}
	}
	
	@Override
	public String toString() 
	{
		return "(" + this.x + ", " + this.y + ")";
	}
}
