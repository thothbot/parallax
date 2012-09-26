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

public enum EnableCap implements GLConstants
{
	CULL_FACE(GLConstants.CULL_FACE),
	BLEND(GLConstants.BLEND),
	DITHER(GLConstants.DITHER),
	STENCIL_TEST(GLConstants.STENCIL_TEST),
	DEPTH_TEST(GLConstants.DEPTH_TEST),
	SCISSOR_TEST(GLConstants.SCISSOR_TEST),
	POLYGON_OFFSET_FILL(GLConstants.POLYGON_OFFSET_FILL),
	SAMPLE_ALPHA_TO_COVERAGE(GLConstants.SAMPLE_ALPHA_TO_COVERAGE),
	SAMPLE_COVERAGE(GLConstants.SAMPLE_COVERAGE);

	private final int value;

	private EnableCap(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
