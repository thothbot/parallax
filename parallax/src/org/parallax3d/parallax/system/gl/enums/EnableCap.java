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

package org.parallax3d.parallax.system.gl.enums;

import org.parallax3d.parallax.system.gl.GL20;

public enum EnableCap implements GLEnum
{
	CULL_FACE(GL20.GL_CULL_FACE),
	BLEND(GL20.GL_BLEND),
	DITHER(GL20.GL_DITHER),
	STENCIL_TEST(GL20.GL_STENCIL_TEST),
	DEPTH_TEST(GL20.GL_DEPTH_TEST),
	SCISSOR_TEST(GL20.GL_SCISSOR_TEST),
	POLYGON_OFFSET_FILL(GL20.GL_POLYGON_OFFSET_FILL),
	SAMPLE_ALPHA_TO_COVERAGE(GL20.GL_SAMPLE_ALPHA_TO_COVERAGE),
	SAMPLE_COVERAGE(GL20.GL_SAMPLE_COVERAGE);

	private final int value;

	private EnableCap(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
