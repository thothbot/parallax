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

package org.parallax3d.parallax.graphics.materials;

import org.parallax3d.parallax.math.Color;

/**
 * Material has a color.
 * 
 * @author thothbot
 *
 */
public interface HasColor 
{
	/**
	 * Gets material color
	 * 
	 * @return the color
	 */
	Color getColor();
	
	/**
	 * Sets material color
	 * 
	 * @param color the color
	 */
	<T extends Material> T setColor(Color color);
	<T extends Material> T setColor(int color);
}
