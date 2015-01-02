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

package thothbot.parallax.core.shared.materials;

import thothbot.parallax.core.shared.math.Color;

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
	public Color getAmbient();
	
	/**
	 * Sets Ambient color
	 * 
	 * @param ambient the color.
	 */
	public void setAmbient(Color ambient);
	
	/**
	 * Gets Emissive color
	 * 
	 * @return the color
	 */
	public Color getEmissive();
	
	/**
	 * Sets Emissive color
	 * 
	 * @param emissive the color
	 */
	public void setEmissive(Color emissive);
}
