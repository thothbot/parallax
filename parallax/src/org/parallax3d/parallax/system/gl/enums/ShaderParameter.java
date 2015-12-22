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

public enum ShaderParameter implements GLEnum
{
	/** Returns int */
	SHADER_TYPE(GL20.GL_SHADER_TYPE),
	/** Returns boolean */
	DELETE_STATUS(GL20.GL_DELETE_STATUS),
	/** Returns boolean */
	COMPILE_STATUS(GL20.GL_COMPILE_STATUS),
	/** Returns int */
	INFO_LOG_LENGTH(GL20.GL_INFO_LOG_LENGTH),
	/** Returns int */
	SHADER_SOURCE_LENGTH(GL20.GL_SHADER_SOURCE_LENGTH);

	private final int value;

	private ShaderParameter(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
