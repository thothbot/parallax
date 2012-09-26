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

public enum EnableCap 
{
	CULL_FACE(GLEnum.CULL_FACE),
	BLEND(GLEnum.BLEND),
	DITHER(GLEnum.DITHER),
	STENCIL_TEST(GLEnum.STENCIL_TEST),
	DEPTH_TEST(GLEnum.DEPTH_TEST),
	SCISSOR_TEST(GLEnum.SCISSOR_TEST),
	POLYGON_OFFSET_FILL(GLEnum.POLYGON_OFFSET_FILL),
	SAMPLE_ALPHA_TO_COVERAGE(GLEnum.SAMPLE_ALPHA_TO_COVERAGE),
	SAMPLE_COVERAGE(GLEnum.SAMPLE_COVERAGE);

	private final int value;

	private EnableCap(GLEnum glEnum) {
		this.value = glEnum.getValue();
	}

	/**
	 * Gets the enum's numerical value.
	 */
	public int getValue() {
		return value;
	}
}
