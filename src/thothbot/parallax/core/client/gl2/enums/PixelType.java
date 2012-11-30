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

public enum PixelType implements GLEnum
{
	UNSIGNED_BYTE(WebGLConstants.UNSIGNED_BYTE),
	UNSIGNED_SHORT_4_4_4_4(WebGLConstants.UNSIGNED_SHORT_4_4_4_4),
	UNSIGNED_SHORT_5_5_5_1(WebGLConstants.UNSIGNED_SHORT_5_5_5_1),
	UNSIGNED_SHORT_5_6_5(WebGLConstants.UNSIGNED_SHORT_5_6_5);

	private final int value;

	private PixelType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
