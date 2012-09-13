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

package thothbot.parallax.core.shared.scenes;

import java.util.Map;

import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.core.Color;

/**
 * Abstract realization of Fog. This class implements color
 * property only.
 * 
 * @author thothbot
 *
 */
public abstract class Fog
{
	private Color color;
	
	/**
	 * This default constructor will make abstract Fog with
	 * defined color
	 *  
	 * @param hex the color in HEX format
	 */
	public Fog(int hex)
	{
		this.color = new Color(hex);
	}

	/**
	 * Set color for the Fog
	 * 
	 * @param color the color instance
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 * Get color of the Fog
	 * 
	 * @return the color instance
	 */
	public Color getColor()
	{
		return color;
	}
	
	/**
	 * The method refreshes uniforms for the fog
	 * 
	 * @param uniforms the map of uniforms
	 */
	public void refreshUniforms(Map<String, Uniform> uniforms) 
	{
		uniforms.get("fogColor").setValue( getColor() );
	}
}
