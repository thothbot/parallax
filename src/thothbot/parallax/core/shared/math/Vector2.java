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

/**
 * This class is realization of (X, Y) vector. 
 * Where:
 * X - x coordinate of the vector.
 * Y - y coordinate of the vector.
 * 
 * @author thothbot
 */
public class Vector2 implements Vector
{
	/**
	 * The X-coordinate
	 */
	protected double x;
	
	/**
	 * The Y-coordinate
	 */
	protected double y;

	/**
	 * This default constructor will initialize vector (0, 0); 
	 */
	public Vector2() 
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
	public Vector2(double x, double y) 
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * get X coordinate from the vector
	 * 
	 * @return a X coordinate
	 */
	public double getX()
	{
		return x;
	}
	
	/**
	 * get Y coordinate from the vector
	 * 
	 * @return a Y coordinate
	 */
	public double getY()
	{
		return y;
	}
		
	/**
	 * This method will add specified value to X coordinate of the vector.
	 * In another words: x += value.
	 * 
	 * @param x the X coordinate
	 */
	public void addX(double x)
	{
		this.x += x;
	}
	
	/**
	 * This method will add specified value to Y coordinate of the vector.
	 * In another words: y += value.
	 * 
	 * @param y the Y coordinate
	 */
	public void addY(double y)
	{
		this.y += y;
	}

	/**
	 * This method sets X coordinate of the vector.
	 * 
	 * @param x the X coordinate
	 */
	public void setX(double x)
	{
		this.x = x;
	}

	/**
	 * This method sets Y coordinate of the vector.
	 * 
	 * @param y the Y coordinate
	 */
	public void setY(double y)
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
	public Vector2 copy(Vector2 v)
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
	public void set(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public Vector2 add(Vector v)
	{
		return this.add(this, v);
	}
	
	@Override
	public Vector2 add(Vector v1, Vector v2)
	{
		this.setX(((Vector2)v1).getX() + ((Vector2)v2).getX());
		this.setY(((Vector2)v1).getY() + ((Vector2)v2).getY());
		return this;
	}
	
	@Override
	public Vector2 add(double s)
	{
		this.addX(s);
		this.addY(s);
		
		return this;
	}

	@Override
	public Vector2 sub(Vector v)
	{
		return this.sub(this, v);
	}
	
	@Override
	public Vector2 sub(Vector v1, Vector v2)
	{
		this.setX(((Vector2)v1).getX() - ((Vector2)v2).getX());
		this.setY(((Vector2)v1).getY() - ((Vector2)v2).getY());
		
		return this;
	}

	@Override
	public Vector2 multiply(Vector v)
	{
		return this.multiply(this, v);
	}
	
	@Override
	public Vector2 multiply(Vector v1, Vector v2)
	{
		this.setX(((Vector2)v1).getX() * ((Vector2)v2).getX());
		this.setY(((Vector2)v1).getY() * ((Vector2)v2).getY());

		return this;
	}

	@Override
	public Vector2 multiply(double s)
	{
		this.x *= s;
		this.y *= s;

		return this;
	}

	@Override
	public Vector2 divide(Vector v)
	{
		return this.divide(this, v);
	}
	
	@Override
	public Vector2 divide(Vector v1, Vector v2)
	{
		this.setX(((Vector2)v1).getX() / ((Vector2)v2).getX());
		this.setY(((Vector2)v1).getY() / ((Vector2)v2).getY());

		return this;
	}
	
	@Override
	public Vector2 divide(double s)
	{
		if (s != 0) 
		{
			this.x /= s;
			this.y /= s;
		} 
		else
		{
			this.set(0, 0);
		}
		
		return this;
	}

	/**
	 * Negates the value of this vector in place.
	 */
	public Vector2 negate()
	{
		return this.multiply(-1);
	}

	/**
	 * Computes the dot product of the this vector and vector v1.
	 * 
	 * @param v1
	 *            the other vector
	 */
	public double dot(Vector2 v1)
	{
		return (this.x * v1.x + this.y * v1.y);
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

	/**
	 * Normalizes this vector in place.
	 */
	@Override
	public Vector2 normalize()
	{
		this.divide(length());
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see thothbot.parallax.core.shared.core.Vector#distanceToSquared(thothbot.parallax.core.shared.core.Vector)
	 */
	@Override
	public double distanceToSquared(Vector v)
	{
		double dx = this.getX() - ((Vector2) v).getX();
		double dy = this.getY() - ((Vector2) v).getY();
		return (dx * dx + dy * dy);
	}

	/*
	 * (non-Javadoc)
	 * @see thothbot.parallax.core.shared.core.Vector#distanceTo(thothbot.parallax.core.shared.core.Vector)
	 */
	@Override
	public double distanceTo(Vector v1)
	{
		return Math.sqrt(distanceToSquared(v1));
	}

	public Vector2 setLength(double l)
	{
		normalize();
		return multiply(l);
	}

	public void lerp(Vector2 v1, double alpha)
	{
		this.x += (v1.x - this.x) * alpha;
		this.y += (v1.y - this.y) * alpha;
	}

	public boolean isZero()
	{
		return (this.lengthSq() < 0.0001 /* almostZero */);
	}
	
	public Vector2 min( Vector2 v ) 
	{
		if ( this.x > v.x ) 
		{
			this.x = v.x;
		}

		if ( this.y > v.y ) 
		{
			this.y = v.y;
		}

		return this;
	}

	public Vector2 max( Vector2 v ) 
	{
		if ( this.x < v.x ) 
		{
			this.x = v.x;
		}

		if ( this.y < v.y ) 
		{
			this.y = v.y;
		}

		return this;
	}
	
	/**
	 * This function assumes min < max, if this assumption isn't true it will not operate correctly
	 * 
	 */
	public Vector2 clamp( Vector2 min, Vector2 max ) 
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

		return this;
	}

	@Override
	public Vector2 clone()
	{
		return new Vector2(this.x, this.y);
	}

	/**
	 * Returns true if all of the data members of Tuple2f t1 are equal to the
	 * corresponding data members in this Tuple2f.
	 * 
	 * @param v1
	 *            the vector with which the comparison is made
	 * @return true or false
	 */
	public boolean equals(Vector2 v1)
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
