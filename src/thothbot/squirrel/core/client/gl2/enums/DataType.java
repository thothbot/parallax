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
 * GL2 Data type flags.
 * 
 * @author thothbot
 *
 */
public enum DataType 
{
	BYTE(GLenum.BYTE),
	UNSIGNED_BYTE(GLenum.UNSIGNED_BYTE),
	SHORT(GLenum.SHORT),
	UNSIGNED_SHORT(GLenum.UNSIGNED_SHORT),
	INT(GLenum.INT),
	UNSIGNED_INT(GLenum.UNSIGNED_INT),
	FLOAT(GLenum.FLOAT);

	private final int value;

	private DataType(GLenum GLenum) 
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
