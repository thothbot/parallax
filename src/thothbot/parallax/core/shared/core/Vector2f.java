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
 * This class is realization of (X, Y) vector. 
 * Where:
 * X - x coordinate of the vector.
 * Y - y coordinate of the vector.
 * 
 * @author thothbot
 */
public class Vector2f implements Vector
{
	/**
	 * The X-coordinate
	 */
	protected float x;
	
	/**
	 * The Y-coordinate
	 */
	protected float y;

	/**
	 * This default constructor will initialize vector (0, 0); 
	 */
	public Vector2f() 
	{
		this(0, 0);
	}

	/**
	 * This constructor will initialize vector (X, Y) from the specified 
	 * X, Y coordinates.
	 *  
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 */
	public Vector2f(float x, float y) 
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * get X coordinate from the vector
	 * 
	 * @return a X coordinate
	 */
	public float getX()
	{
		return x;
	}
	
	/**
	 * get Y coordinate from the vector
	 * 
	 * @return a Y coordinate
	 */
	public float getY()
	{
		return y;
	}
		
	/**
	 * This method will add specified value to X coordinate of the vector.
	 * In another words: x += value.
	 * 
	 * @param x the X coordinate
	 */
	public void addX(float x)
	{
		this.x += x;
	}
	
	/**
	 * This method will add specified value to Y coordinate of the vector.
	 * In another words: y += value.
	 * 
	 * @param y the Y coordinate
	 */
	public void addY(float y)
	{
		this.y += y;
	}

	/**
	 * This method sets X coordinate of the vector.
	 * 
	 * @param x the X coordinate
	 */
	public void setX(float x)
	{
		this.x = x;
	}

	/**
	 * This method sets Y coordinate of the vector.
	 * 
	 * @param y the Y coordinate
	 */
	public void setY(float y)
	{
		this.y = y;
	}

	/**
	 * Set value of the vector from another vector.
	 * 
	 * @param v the other vector
	 * 
	 * @return the current vector
	 */
	public Vector2f copy(Vector2f v)
	{
		this.set(v.getX(), v.getY());
		return this;
	}
	
	/**
	 * Set value of the vector to the specified (X, Y, Z) coordinates.
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 */
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public Vector2f add(Vector v)
	{
		return this.add(this, v);
	}
	
	@Override
	public Vector2f add(Vector v1, Vector v2)
	{
		this.setX(((Vector3f)v1).getX() + ((Vector3f)v2).getX());
		this.setY(((Vector3f)v1).getY() + ((Vector3f)v2).getY());
		return this;
	}
	
	@Override
	public Vector2f add(float s)
	{
		this.addX(s);
		this.addY(s);
		
		return this;
	}

	@Override
	public Vector2f sub(Vector v)
	{
		return this.sub(this, v);
	}
	
	@Override
	public Vector2f sub(Vector v1, Vector v2)
	{
		this.setX(((Vector3f)v1).getX() - ((Vector3f)v2).getX());
		this.setY(((Vector3f)v1).getY() - ((Vector3f)v2).getY());
		
		return this;
	}

	@Override
	public Vector2f multiply(Vector v)
	{
		return this.multiply(this, v);
	}
	
	@Override
	public Vector2f multiply(Vector v1, Vector v2)
	{
		this.setX(((Vector3f)v1).getX() * ((Vector3f)v2).getX());
		this.setY(((Vector3f)v1).getY() * ((Vector3f)v2).getY());

		return this;
	}

	@Override
	public Vector2f multiply(float s)
	{
		this.x *= s;
		this.y *= s;

		return this;
	}

	@Override
	public Vector2f divide(Vector v)
	{
		return this.divide(this, v);
	}
	
	@Override
	public Vector2f divide(Vector v1, Vector v2)
	{
		this.setX(((Vector3f)v1).getX() / ((Vector3f)v2).getX());
		this.setY(((Vector3f)v1).getY() / ((Vector3f)v2).getY());

		return this;
	}
	
	@Override
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

	/*
	 * (non-Javadoc)
	 * @see thothbot.parallax.core.shared.core.Vector#distanceToSquared(thothbot.parallax.core.shared.core.Vector)
	 */
	@Override
	public float distanceToSquared(Vector v)
	{
		float dx = this.getX() - ((Vector2f) v).getX();
		float dy = this.getY() - ((Vector2f) v).getY();
		return (dx * dx + dy * dy);
	}

	/*
	 * (non-Javadoc)
	 * @see thothbot.parallax.core.shared.core.Vector#distanceTo(thothbot.parallax.core.shared.core.Vector)
	 */
	@Override
	public float distanceTo(Vector v1)
	{
		return (float) Math.sqrt(distanceToSquared(v1));
	}

	public Vector2f setLength(float l)
	{
		normalize();
		return multiply(l);
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
	 * @param v1
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
