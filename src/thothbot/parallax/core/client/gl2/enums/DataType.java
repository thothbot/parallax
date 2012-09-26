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
 * GL2 Data type flags.
 * 
 * @author thothbot
 *
 */
public enum DataType implements GLConstants
{
	BYTE(GLConstants.BYTE),
	UNSIGNED_BYTE(GLConstants.UNSIGNED_BYTE),
	SHORT(GLConstants.SHORT),
	UNSIGNED_SHORT(GLConstants.UNSIGNED_SHORT),
	INT(GLConstants.INT),
	UNSIGNED_INT(GLConstants.UNSIGNED_INT),
	FLOAT(GLConstants.FLOAT);

	private final int value;

	private DataType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
