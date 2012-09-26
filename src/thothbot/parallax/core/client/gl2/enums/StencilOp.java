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

public enum StencilOp implements GLConstants
{
	ZERO(GLConstants.ZERO),
	KEEP(GLConstants.KEEP),
	REPLACE(GLConstants.REPLACE),
	INCR(GLConstants.INCR),
	DECR(GLConstants.DECR),
	INVERT(GLConstants.INVERT),
	INCR_WRAP(GLConstants.INCR_WRAP),
	DECR_WRAP(GLConstants.DECR_WRAP);

	private final int value;

	private StencilOp(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
