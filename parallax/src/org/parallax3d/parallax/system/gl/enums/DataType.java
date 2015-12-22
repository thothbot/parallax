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

/**
 * GL2 Data type flags.
 * 
 * @author thothbot
 *
 */
public enum DataType implements GLEnum
{
	BYTE(GL20.GL_BYTE),
	UNSIGNED_BYTE(GL20.GL_UNSIGNED_BYTE),
	SHORT(GL20.GL_SHORT),
	UNSIGNED_SHORT(GL20.GL_UNSIGNED_SHORT),
	INT(GL20.GL_INT),
	UNSIGNED_INT(GL20.GL_UNSIGNED_INT),
	FLOAT(GL20.GL_FLOAT);

	private final int value;

	private DataType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
