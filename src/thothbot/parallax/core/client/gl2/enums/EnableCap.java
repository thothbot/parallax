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

import thothbot.parallax.core.client.gl2.WebGLConstants;

public enum EnableCap implements WebGLConstants
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
