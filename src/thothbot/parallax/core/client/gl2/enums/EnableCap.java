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

public enum EnableCap implements GLEnum
{
	CULL_FACE(WebGLConstants.CULL_FACE),
	BLEND(WebGLConstants.BLEND),
	DITHER(WebGLConstants.DITHER),
	STENCIL_TEST(WebGLConstants.STENCIL_TEST),
	DEPTH_TEST(WebGLConstants.DEPTH_TEST),
	SCISSOR_TEST(WebGLConstants.SCISSOR_TEST),
	POLYGON_OFFSET_FILL(WebGLConstants.POLYGON_OFFSET_FILL),
	SAMPLE_ALPHA_TO_COVERAGE(WebGLConstants.SAMPLE_ALPHA_TO_COVERAGE),
	SAMPLE_COVERAGE(WebGLConstants.SAMPLE_COVERAGE);

	private final int value;

	private EnableCap(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
