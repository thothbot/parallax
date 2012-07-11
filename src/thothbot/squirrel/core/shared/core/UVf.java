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

import java.lang.reflect.Constructor;

/**
 * Class implements UV texture mapping.
 * 
 * UV texturing permits polygons that make up a 3D object to be 
 * painted with color from an image.
 * 
 * Here are:
 * U - Horizontal coordinate.
 * V - Vertical coordinate.
 * 
 * UV coordinates should be applied per face. not per vertex. This means 
 * a shared vertex can have different UV coordinates in each of its 
 * triangles, so adjacent triangles can be cut apart and positioned 
 * on different areas of the texture map.
 * 
 * @author thothbot
 *
 */
public class UVf
{
	/**
	 * Horizontal coordinate.
	 */
	private float u;

	/**
	 * Vertical coordinate.
	 */
	private float v;

	/**
	 * This default Constructor will make UV object (0.0, 0.0) 
	 */
	public UVf() 
	{
		this.u = 0.0f;
		this.v = 0.0f;
	}

	/**
	 * This constructor will initializes (U, V) texture mapping.
	 * 
	 * @param u the Horizontal coordinate.
	 * @param v the Vertical coordinate.
	 */
	public UVf(float u, float v) 
	{
		this.u = u;
		this.v = v;
	}

	/**
	 * Getting Horizontal coordinate.
	 * 
	 * @return the Horizontal coordinate.
	 */
	public float getU()
	{
		return this.u;
	}

	/**
	 * Getting Vertical coordinate.
	 * 
	 * @return the Vertical coordinate.
	 */
	public float getV()
	{
		return this.v;
	}

	/**
	 * Sets the value of this UV from the two coordinates (U, V).
	 * 
	 * @param u the Horizontal coordinate.
	 * @param v the Vertical coordinate.
	 * 
	 */
	public UVf set(float u, float v)
	{
		this.u = u;
		this.v = v;
		return this;
	}

	/**
	 * Copy values of UV mapping object to the values of the current
	 * UV mapping object.
	 * 
	 * @param uv the UV mapping object
	 * 
	 * @return the current UV mapping object
	 */
	public UVf copy(UVf uv)
	{
		return this.set(uv.u, uv.v);
	}

	/**
	 * Linearly interpolates between the current UV object
	 * and input UV object.
	 * 
	 * @param uv the input UV object
	 * @param alpha the alpha value in range <0.0, 1.0>
	 * 
	 * @return the current UV object
	 */
	public UVf lerp(UVf uv, float alpha)
	{
		this.u += (uv.u - this.u) * alpha;
		this.v += (uv.v - this.v) * alpha;
		return this;
	}
	
	/**
	 * Clone the current UV mapping object.
	 * uv.clone() != uv
	 * 
	 * @return the new instance of UV mapping object
	 */
	public UVf clone() 
	{
		return new UVf( this.u, this.v );
	}
	
	/**
	 * Return information about this UV mapping object as list
	 * of Horizontal and Vertical coordinates.
	 */
	public String toString() 
	{
		return "(" + this.u + ", " + this.v + ")";
	}
}
