/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package thothbot.parallax.core.shared.materials;

import thothbot.parallax.core.shared.core.Color3;

/**
 * The Material has both an Ambient and Emissive colors.
 * 
 * @author thothbot
 *
 */
public interface HasAmbientEmissiveColor 
{
	/**
	 * Gets Ambient color
	 * 
	 * @return the color
	 */
	public Color3 getAmbient();
	
	/**
	 * Sets Ambient color
	 * 
	 * @param ambient the color.
	 */
	public void setAmbient(Color3 ambient);
	
	/**
	 * Gets Emissive color
	 * 
	 * @return the color
	 */
	public Color3 getEmissive();
	
	/**
	 * Sets Emissive color
	 * 
	 * @param emissive the color
	 */
	public void setEmissive(Color3 emissive);
}
