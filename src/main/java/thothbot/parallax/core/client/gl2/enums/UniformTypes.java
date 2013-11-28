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

public enum UniformTypes implements GLEnum
{
	FLOAT_VEC2(WebGLConstants.FLOAT_VEC2),
	FLOAT_VEC3(WebGLConstants.FLOAT_VEC3),
	FLOAT_VEC4(WebGLConstants.FLOAT_VEC4),
	INT_VEC2(WebGLConstants.INT_VEC2),
	INT_VEC3(WebGLConstants.INT_VEC3),
	INT_VEC4(WebGLConstants.INT_VEC4),
	BOOL(WebGLConstants.BOOL),
	BOOL_VEC2(WebGLConstants.BOOL_VEC2),
	BOOL_VEC3(WebGLConstants.BOOL_VEC3),
	BOOL_VEC4(WebGLConstants.BOOL_VEC4),
	FLOAT_MAT2(WebGLConstants.FLOAT_MAT2),
	FLOAT_MAT3(WebGLConstants.FLOAT_MAT3),
	FLOAT_MAT4(WebGLConstants.FLOAT_MAT4),
	SAMPLER_2D(WebGLConstants.SAMPLER_2D),
	SAMPLER_CUBE(WebGLConstants.SAMPLER_CUBE);

	private final int value;

	private UniformTypes(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
