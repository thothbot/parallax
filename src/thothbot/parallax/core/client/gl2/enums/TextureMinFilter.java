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

package thothbot.parallax.core.client.gl2.enums;

import thothbot.parallax.core.client.gl2.WebGLConstants;

/**
 * The texture minifying function is used whenever the pixel being textured 
 * maps to an area greater than one texture element. There are six defined 
 * minifying functions. Two of them use the nearest one or nearest four 
 * texture elements to compute the texture value. The other four use mipmaps.
 * 
 * @author thothbot
 *
 */
public enum TextureMinFilter implements GLEnum
{
	/**
	 * Returns the value of the texture element that is nearest 
	 * (in Manhattan distance) to the center of the pixel being textured.
	 */
	NEAREST(WebGLConstants.NEAREST),
	/**
	 * Returns the weighted average of the four texture elements that 
	 * are closest to the center of the pixel being textured. 
	 */
	LINEAR(WebGLConstants.LINEAR),
	/**
	 * Chooses the mipmap that most closely matches the size of the 
	 * pixel being textured and uses the {@link TextureMinFilter#NEAREST} criterion 
	 * (the texture element nearest to the center of the pixel) 
	 * to produce a texture value.
	 */
	NEAREST_MIPMAP_NEAREST(WebGLConstants.NEAREST_MIPMAP_NEAREST),
	/**
	 * Chooses the mipmap that most closely matches the size of the pixel 
	 * being textured and uses the {@link TextureMinFilter#LINEAR} criterion 
	 * (a weighted average of the four texture elements that are closest to 
	 * the center of the pixel) to produce a texture value.
	 */
	LINEAR_MIPMAP_NEAREST(WebGLConstants.LINEAR_MIPMAP_NEAREST),
	/**
	 * Chooses the two mipmaps that most closely match the size of the pixel 
	 * being textured and uses the {@link TextureMinFilter#NEAREST} criterion (the texture element
	 * nearest to the center of the pixel) to produce a texture value from 
	 * each mipmap. The final texture value is a weighted average of those two values.
	 */
	NEAREST_MIPMAP_LINEAR(WebGLConstants.NEAREST_MIPMAP_LINEAR),
	/**
	 * Chooses the two mipmaps that most closely match the size of the pixel 
	 * being textured and uses the {@link TextureMinFilter#LINEAR} criterion 
	 * (a weighted average of the four texture elements that are closest to the 
	 * center of the pixel) to produce a texture value from each mipmap. 
	 * The final texture value is a weighted average of those two values.
	 */
	LINEAR_MIPMAP_LINEAR(WebGLConstants.LINEAR_MIPMAP_LINEAR);

	private final int value;

	private TextureMinFilter(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
