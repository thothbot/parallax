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
 * Interface of the vector.
 */
public interface Vector
{
	/**
	 * Sets the value of this vector to the vector sum of itself and vector v.
	 * 
	 * @param v the other vector
	 * 
	 * @return the current vector
	 */
	public Vector add(Vector v);
	
	/**
	 * Sets the value of this vector to the vector sum of vectors v1 and v2.
	 * 
	 * @param v1 the first vector
	 * @param v2 the second vector
	 * 
	 * @return the current vector
	 */
	public Vector add(Vector v1, Vector v2);
	
	/**
	 * Sets the value of this vector to the vector sum of itself and scalar s.
	 * (this = this + s)
	 * 
	 * @param s the scalar value
	 * 
	 * @return the current vector
	 */
	public Vector add(double s);
	
	/**
	 * Sets the value of this vector to the vector difference of itself and
	 * vector v.
	 * (this = this - v)
	 * 
	 * @param v the other vector
	 * 
	 * @return the current vector
	 */
	public Vector sub(Vector v);
	
	/**
	 * Sets the value of this vector to the vector difference of v1 and
	 * vector v2.
	 * 
	 * @param v1 the first vector
	 * @param v2 the second vector
	 * 
	 * @return the current vector
	 */
	public Vector sub(Vector v1, Vector v2);
	
	/**
	 * Sets the value of this vector to the vector multiplication of itself and
	 * vector v.
	 * (this = this * v)
	 * 
	 * @param v the other vector
	 * 
	 * @return the current vector
	 */
	public Vector multiply(Vector v);
	
	/**
	 * Sets the value of this vector to the vector multiplication of v1 and
	 * vector v2.
	 * 
	 * @param v1 the first vector
	 * @param v2 the second vector
	 * 
	 * @return the current vector
	 */
	public Vector multiply(Vector v1, Vector v2);
	
	/**
	 * Sets the value of this vector to the scalar multiplication of the scale
	 * factor with this.
	 * 
	 * @param s the scalar value
	 * 
	 * @return the current vector
	 */
	public Vector multiply(double s);
		
	/**
	 * Sets the value of this vector to the vector division of itself and
	 * vector v.
	 * (this = this / v)
	 * 
	 * @param v the other vector
	 * 
	 * @return the current vector
	 */
	public Vector divide(Vector v);
	
	/**
	 * Sets the value of this vector to the scalar division of the scale
	 * factor with this.
	 * If the scalar is 0 then will be vector where last coordinate is 1.
	 * 
	 * @param s the scalar value
	 * 
	 * @return the current vector
	 */
	public Vector divide(double s);
	
	/**
	 * Sets the value of this vector to the vector division of v1 and
	 * vector v2.
	 * 
	 * @param v1 the first vector
	 * @param v2 the second vector
	 * 
	 * @return the current vector
	 */
	public Vector divide(Vector v1, Vector v2);
	
	/**
	 * Negates the value of this vector in place.
	 * 
	 * @return the current vector
	 */
	public Vector negate();
	
	/**
	 * get distance between two vectors.
	 * 
	 * @param v1 
	 * 			second vector
	 * @return a distance between two vectors
	 */
	public double distanceTo(Vector v1);
	
	/**
	 * get squared distance between two vectors.
	 * 
	 * @param v 
	 * 			second vector
	 * @return a squared distance between two vectors.
	 */
	public double distanceToSquared(Vector v);
	
	
	public Vector normalize();
	
	public Vector clone();
	public String toString();
}
