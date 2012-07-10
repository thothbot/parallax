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

public class Vector4f implements Vector
{
	protected float x;
	protected float y;
	protected float z;
	protected float w;

	public Vector4f() 
	{
		this(0,0,0,1);
	}

	/**
	 * Constructs and initializes a Vector4f from the specified xyz coordinates.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 */
	public Vector4f(float x, float y, float z) 
	{
		this(x, y, z, 1);
	}

	/**
	 * Constructs and initializes a Vector4f from the specified xyzw
	 * coordinates.
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
	public Vector4f(float x, float y, float z, float w) 
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public float getX()
	{
		return this.x;
	}

	public void addX(float x)
	{
		this.x += x;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public float getY()
	{
		return this.y;
	}

	public void addY(float y)
	{
		this.y += y;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public float getZ()
	{
		return this.z;
	}

	public void addZ(float z)
	{
		this.z += z;
	}
	
	public void setZ(float z)
	{
		this.z = z;
	}
	
	public void setW(float w) 
	{
		this.w = w;
	}

	public float getW() 
	{
		return w;
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
	public Vector4f set(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	public Vector4f copy(Vector4f v)
	{
		return this.set(v.x, v.y, v.z, 1);
	}
	
	public Vector4f copy(Vector3f v){
		return this.set(v.x, v.y, v.z, 1);
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
	 * @param v1
	 *            the other vector
	 */
	@Override
	public Vector4f add(Vector v)
	{
		return this.add(this, (Vector4f) v);
	}

	public Vector4f addScalar(float s)
	{
		this.x += s;
		this.y += s;
		this.z += s;
		this.w += s;
		return this;
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
	 * this - v1).
	 * 
	 * @param v1
	 *            the other vector
	 */
	@Override
	public Vector4f sub(Vector v)
	{
		return this.sub(this, (Vector4f)v);
	}

	/**
	 * Sets the value of this vector to the scalar multiplication of the scale
	 * factor with this.
	 * 
	 * @param s
	 *            the scalar value
	 */
	@Override
	public Vector4f multiply(float s)
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
	
	public Vector4f multiply(Vector4f v)
	{
		return this.multiply(this, v);
	}

	public Vector4f divide(Vector4f v1, Vector4f v2)
	{
		this.x = v1.x / v2.x;
		this.y = v1.y / v2.y;
		this.z = v1.z / v2.z;
		this.w = v1.w / v2.w;
		return this;
	}
	
	public Vector4f divideSelf(Vector4f v)
	{
		return this.divide(this, v);
	}
	
	public Vector4f divide(float s)
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
	public float dot(Vector4f v1)
	{
		return (this.x * v1.x + this.y * v1.y + this.z * v1.z + this.w * v1.w);
	}

	/**
	 * Returns the length of this vector.
	 * 
	 * @return the length of this vector as a float
	 */
	public float length()
	{
		return (float) Math.sqrt(lengthSq());
	}

	/**
	 * Returns the squared length of this vector
	 * 
	 * @return the squared length of this vector as a float
	 */
	public float lengthSq()
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

	@Override
	public float distanceTo(Vector v)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public void setLength(float l)
	{
		this.normalize();
		multiply(l);
	}

	public void lerp(Vector4f v1, float alpha)
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
