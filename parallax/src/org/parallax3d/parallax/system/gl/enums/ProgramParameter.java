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

public enum ProgramParameter implements GLEnum
{
	/** 
	 * Returns boolean 
	 */
	DELETE_STATUS(GL20.GL_DELETE_STATUS),
	
	/** 
	 * Returns boolean
	 */
	LINK_STATUS(GL20.GL_LINK_STATUS),
	
	/** 
	 * Returns boolean 
	 */
	VALIDATE_STATUS(GL20.GL_VALIDATE_STATUS),
	
	/** 
	 * Returns int 
	 */
	INFO_LOG_LENGTH(GL20.GL_INFO_LOG_LENGTH),
	
	/** 
	 * Returns int 
	 */
	ATTACHED_SHADERS(GL20.GL_ATTACHED_SHADERS),
	
	/** 
	 * Returns int 
	 */
	ACTIVE_UNIFORMS(GL20.GL_ACTIVE_UNIFORMS),
	
	/** 
	 * Returns int 
	 */
	ACTIVE_UNIFORM_MAX_LENGTH(GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH),
	
	/** 
	 * Returns int 
	 */
	ACTIVE_ATTRIBUTES(GL20.GL_ACTIVE_ATTRIBUTES),
	
	/** 
	 * Returns int 
	 */
	ACTIVE_ATTRIBUTE_MAX_LENGTH(GL20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);

	private final int value;

	private ProgramParameter(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
