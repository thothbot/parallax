/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package thothbot.squirrel.core.client.gl2.enums;

/**
 * GL2 Texture Target flags.
 * 
 * @author thothbot
 *
 */
public enum TextureTarget 
{
	TEXTURE_2D(GLenum.TEXTURE_2D),
	TEXTURE(GLenum.TEXTURE),
	TEXTURE_CUBE_MAP(GLenum.TEXTURE_CUBE_MAP),
	TEXTURE_BINDING_CUBE_MAP(GLenum.TEXTURE_BINDING_CUBE_MAP),
	TEXTURE_CUBE_MAP_POSITIVE_X(GLenum.TEXTURE_CUBE_MAP_POSITIVE_X),
	TEXTURE_CUBE_MAP_NEGATIVE_X(GLenum.TEXTURE_CUBE_MAP_NEGATIVE_X),
	TEXTURE_CUBE_MAP_POSITIVE_Y(GLenum.TEXTURE_CUBE_MAP_POSITIVE_Y),
	TEXTURE_CUBE_MAP_NEGATIVE_Y(GLenum.TEXTURE_CUBE_MAP_NEGATIVE_Y),
	TEXTURE_CUBE_MAP_POSITIVE_Z(GLenum.TEXTURE_CUBE_MAP_POSITIVE_Z),
	TEXTURE_CUBE_MAP_NEGATIVE_Z(GLenum.TEXTURE_CUBE_MAP_NEGATIVE_Z),
	MAX_CUBE_MAP_TEXTURE_SIZE(GLenum.MAX_CUBE_MAP_TEXTURE_SIZE);

	private final int value;

	private TextureTarget(GLenum GLenum) 
	{
		this.value = GLenum.getValue();
	}
	
	/**
	 * Gets the enum's numerical value.
	 */
	public int getValue() 
	{
		return value;
	}
}
