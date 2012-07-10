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

// A texture coordinate.
public class UVf
{
	/**
	 * Horizontal coordinate.
	 */
	private float u = 0.0f;

	/**
	 * Vertical coordinate.
	 */
	private float v = 0.0f;

	public UVf() {
	}

	/**
	 * Constructs and initializes a UV
	 * 
	 * @param u
	 * @param v
	 */
	public UVf(float u, float v) {
		this.u = u;
		this.v = v;
	}

	public float getU()
	{
		return this.u;
	}

	public float getV()
	{
		return this.v;
	}

	/**
	 * Sets the value of this UV from the 2 values u and v.
	 * 
	 * @param u
	 * @param v
	 * 
	 */
	public UVf set(float u, float v)
	{
		this.u = u;
		this.v = v;
		return this;
	}

	public UVf copy(UVf uv)
	{
		return this.set(uv.u, uv.v);
	}

	public UVf lerp(UVf uv, float alpha)
	{
		this.u += (uv.u - this.u) * alpha;
		this.v += (uv.v - this.v) * alpha;
		return this;
	}
	
	public UVf clone() 
	{
		return new UVf( this.u, this.v );
	}
	
	public String toString() 
	{
		return "{" + this.u + ", " + this.v + "}";
	}
}
