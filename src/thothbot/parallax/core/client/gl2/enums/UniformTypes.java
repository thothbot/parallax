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

public enum UniformTypes implements GLConstants
{
	FLOAT_VEC2(GLConstants.FLOAT_VEC2),
	FLOAT_VEC3(GLConstants.FLOAT_VEC3),
	FLOAT_VEC4(GLConstants.FLOAT_VEC4),
	INT_VEC2(GLConstants.INT_VEC2),
	INT_VEC3(GLConstants.INT_VEC3),
	INT_VEC4(GLConstants.INT_VEC4),
	BOOL(GLConstants.BOOL),
	BOOL_VEC2(GLConstants.BOOL_VEC2),
	BOOL_VEC3(GLConstants.BOOL_VEC3),
	BOOL_VEC4(GLConstants.BOOL_VEC4),
	FLOAT_MAT2(GLConstants.FLOAT_MAT2),
	FLOAT_MAT3(GLConstants.FLOAT_MAT3),
	FLOAT_MAT4(GLConstants.FLOAT_MAT4),
	SAMPLER_2D(GLConstants.SAMPLER_2D),
	SAMPLER_CUBE(GLConstants.SAMPLER_CUBE);

	private final int value;

	private UniformTypes(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
