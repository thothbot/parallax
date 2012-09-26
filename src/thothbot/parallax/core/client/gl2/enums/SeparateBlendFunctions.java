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

public enum SeparateBlendFunctions 
{
	BLEND_DST_RGB(GLEnum.BLEND_DST_RGB),
	BLEND_SRC_RGB(GLEnum.BLEND_SRC_RGB),
	BLEND_DST_ALPHA(GLEnum.BLEND_DST_ALPHA),
	BLEND_SRC_ALPHA(GLEnum.BLEND_SRC_ALPHA),
	CONSTANT_COLOR(GLEnum.CONSTANT_COLOR),
	ONE_MINUS_CONSTANT_COLOR(GLEnum.ONE_MINUS_CONSTANT_COLOR),
	CONSTANT_ALPHA(GLEnum.CONSTANT_ALPHA),
	ONE_MINUS_CONSTANT_ALPHA(GLEnum.ONE_MINUS_CONSTANT_ALPHA),
	BLEND_COLOR(GLEnum.BLEND_COLOR);

	private final int value;

	private SeparateBlendFunctions(GLEnum glEnum) {
		this.value = glEnum.getValue();
	}

	/**
	 * Gets the enum's numerical value.
	 */
	public int getValue() {
		return value;
	}
}
