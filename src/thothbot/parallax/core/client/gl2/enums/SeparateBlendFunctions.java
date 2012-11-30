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

public enum SeparateBlendFunctions implements GLEnum
{
	BLEND_DST_RGB(WebGLConstants.BLEND_DST_RGB),
	BLEND_SRC_RGB(WebGLConstants.BLEND_SRC_RGB),
	BLEND_DST_ALPHA(WebGLConstants.BLEND_DST_ALPHA),
	BLEND_SRC_ALPHA(WebGLConstants.BLEND_SRC_ALPHA),
	CONSTANT_COLOR(WebGLConstants.CONSTANT_COLOR),
	ONE_MINUS_CONSTANT_COLOR(WebGLConstants.ONE_MINUS_CONSTANT_COLOR),
	CONSTANT_ALPHA(WebGLConstants.CONSTANT_ALPHA),
	ONE_MINUS_CONSTANT_ALPHA(WebGLConstants.ONE_MINUS_CONSTANT_ALPHA),
	BLEND_COLOR(WebGLConstants.BLEND_COLOR);

	private final int value;

	private SeparateBlendFunctions(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
