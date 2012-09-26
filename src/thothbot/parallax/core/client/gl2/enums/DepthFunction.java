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

public enum DepthFunction implements GLConstants
{
	NEVER(GLConstants.NEVER),
	LESS(GLConstants.LESS),
	EQUAL(GLConstants.EQUAL),
	LEQUAL(GLConstants.LEQUAL),
	GREATER(GLConstants.GREATER),
	NOTEQUAL(GLConstants.NOTEQUAL),
	GEQUAL(GLConstants.GEQUAL),
	ALWAYS(GLConstants.ALWAYS);

	private final int value;

	private DepthFunction(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
