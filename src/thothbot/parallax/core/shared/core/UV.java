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
 * Class implements UV texture mapping.
 * <p>
 * UV texturing permits polygons that make up a 3D object to be 
 * painted with color from an image.
 * <p>
 * Here are:
 * <ul>
 * <li>U - Horizontal coordinate.</li>
 * <li>V - Vertical coordinate.</li>
 * </ul>
 * 
 * UV coordinates should be applied per face. not per vertex. This means 
 * a shared vertex can have different UV coordinates in each of its 
 * triangles, so adjacent triangles can be cut apart and positioned 
 * on different areas of the texture map.
 * 
 * <pre>
 * {@code
 * UV uv = new UV( 0, 1 );
 * }
 * </pre>
 * 
 * @author thothbot
 *
 */
public class UV
{
	/**
	 * Horizontal coordinate.
	 */
	private double u;

	/**
	 * Vertical coordinate.
	 */
	private double v;

	/**
	 * This default Constructor will make UV object (0.0, 0.0) 
	 */
	public UV() 
	{
		this.u = 0.0;
		this.v = 0.0;
	}

	/**
	 * This constructor will initializes (U, V) texture mapping.
	 * 
	 * @param u the Horizontal coordinate.
	 * @param v the Vertical coordinate.
	 */
	public UV(double u, double v) 
	{
		this.u = u;
		this.v = v;
	}

	/**
	 * get Horizontal coordinate.
	 * 
	 * @return the Horizontal coordinate.
	 */
	public double getU()
	{
		return this.u;
	}

	/**
	 * get Vertical coordinate.
	 * 
	 * @return the Vertical coordinate.
	 */
	public double getV()
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
	public UV set(double u, double v)
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
	public UV copy(UV uv)
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
	public UV lerp(UV uv, double alpha)
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
	public UV clone() 
	{
		return new UV( this.u, this.v );
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
