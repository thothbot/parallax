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

package thothbot.parallax.core.client.gl2.enums;

/**
 * The texture minifying function is used whenever the pixel being textured 
 * maps to an area greater than one texture element. There are six defined 
 * minifying functions. Two of them use the nearest one or nearest four 
 * texture elements to compute the texture value. The other four use mipmaps.
 * 
 * @author thothbot
 *
 */
public enum TextureMinFilter 
{
	/**
	 * Returns the value of the texture element that is nearest 
	 * (in Manhattan distance) to the center of the pixel being textured.
	 */
	NEAREST(GLenum.NEAREST),
	/**
	 * Returns the weighted average of the four texture elements that 
	 * are closest to the center of the pixel being textured. 
	 */
	LINEAR(GLenum.LINEAR),
	/**
	 * Chooses the mipmap that most closely matches the size of the 
	 * pixel being textured and uses the {@link TextureMinFilter#NEAREST} criterion 
	 * (the texture element nearest to the center of the pixel) 
	 * to produce a texture value.
	 */
	NEAREST_MIPMAP_NEAREST(GLenum.NEAREST_MIPMAP_NEAREST),
	/**
	 * Chooses the mipmap that most closely matches the size of the pixel 
	 * being textured and uses the {@link TextureMinFilter#LINEAR} criterion 
	 * (a weighted average of the four texture elements that are closest to 
	 * the center of the pixel) to produce a texture value.
	 */
	LINEAR_MIPMAP_NEAREST(GLenum.LINEAR_MIPMAP_NEAREST),
	/**
	 * Chooses the two mipmaps that most closely match the size of the pixel 
	 * being textured and uses the {@link TextureMinFilter#NEAREST} criterion (the texture element
	 * nearest to the center of the pixel) to produce a texture value from 
	 * each mipmap. The final texture value is a weighted average of those two values.
	 */
	NEAREST_MIPMAP_LINEAR(GLenum.NEAREST_MIPMAP_LINEAR),
	/**
	 * Chooses the two mipmaps that most closely match the size of the pixel 
	 * being textured and uses the {@link TextureMinFilter#LINEAR} criterion 
	 * (a weighted average of the four texture elements that are closest to the 
	 * center of the pixel) to produce a texture value from each mipmap. 
	 * The final texture value is a weighted average of those two values.
	 */
	LINEAR_MIPMAP_LINEAR(GLenum.LINEAR_MIPMAP_LINEAR);

	private final GLenum value;

	private TextureMinFilter(GLenum GLenum) 
	{
		this.value = GLenum;
	}
	
	public GLenum getEnum()
	{
		return this.value;
	}

	/**
	 * Gets the enum's numerical value.
	 */
	public int getValue() 
	{
		return value.getValue();
	}
}
