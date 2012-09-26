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

public enum PixelType implements GLConstants
{
	UNSIGNED_BYTE(GLConstants.UNSIGNED_BYTE),
	UNSIGNED_SHORT_4_4_4_4(GLConstants.UNSIGNED_SHORT_4_4_4_4),
	UNSIGNED_SHORT_5_5_5_1(GLConstants.UNSIGNED_SHORT_5_5_5_1),
	UNSIGNED_SHORT_5_6_5(GLConstants.UNSIGNED_SHORT_5_6_5);

	private final int value;

	private PixelType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
