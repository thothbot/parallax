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

public enum UniformTypes implements GLEnum
{
	FLOAT_VEC2(GL20.GL_FLOAT_VEC2),
	FLOAT_VEC3(GL20.GL_FLOAT_VEC3),
	FLOAT_VEC4(GL20.GL_FLOAT_VEC4),
	INT_VEC2(GL20.GL_INT_VEC2),
	INT_VEC3(GL20.GL_INT_VEC3),
	INT_VEC4(GL20.GL_INT_VEC4),
	BOOL(GL20.GL_BOOL),
	BOOL_VEC2(GL20.GL_BOOL_VEC2),
	BOOL_VEC3(GL20.GL_BOOL_VEC3),
	BOOL_VEC4(GL20.GL_BOOL_VEC4),
	FLOAT_MAT2(GL20.GL_FLOAT_MAT2),
	FLOAT_MAT3(GL20.GL_FLOAT_MAT3),
	FLOAT_MAT4(GL20.GL_FLOAT_MAT4),
	SAMPLER_2D(GL20.GL_SAMPLER_2D),
	SAMPLER_CUBE(GL20.GL_SAMPLER_CUBE);

	private final int value;

	private UniformTypes(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
