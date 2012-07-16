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
 * GL2 Texture MinFilter flags.
 * 
 * @author thothbot
 *
 */
public enum TextureMinFilter 
{
	NEAREST(GLenum.NEAREST),
	LINEAR(GLenum.LINEAR),
	NEAREST_MIPMAP_NEAREST(GLenum.NEAREST_MIPMAP_NEAREST),
	LINEAR_MIPMAP_NEAREST(GLenum.LINEAR_MIPMAP_NEAREST),
	NEAREST_MIPMAP_LINEAR(GLenum.NEAREST_MIPMAP_LINEAR),
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
