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

public enum ErrorCode 
{
	NO_ERROR(GLEnum.NO_ERROR),
	INVALID_ENUM(GLEnum.INVALID_ENUM),
	INVALID_VALUE(GLEnum.INVALID_VALUE),
	INVALID_OPERATION(GLEnum.INVALID_OPERATION),
	OUT_OF_MEMORY(GLEnum.OUT_OF_MEMORY);

	private final int value;

	private ErrorCode(GLEnum glEnum) {
		this.value = glEnum.getValue();
	}

	/**
	 * Gets the enum's numerical value.
	 */
	public int getValue() {
		return value;
	}
}
