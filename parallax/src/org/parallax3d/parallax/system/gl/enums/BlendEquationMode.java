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
 * GL2 BlendEquationMode flags.
 * 
 * @author thothbot
 *
 */
public enum BlendEquationMode implements GLEnum
{
	FUNC_ADD(GL20.GL_FUNC_ADD),
	FUNC_REVERSE_SUBTRACT(GL20.GL_FUNC_REVERSE_SUBTRACT),
	FUNC_SUBTRACT(GL20.GL_FUNC_SUBTRACT);

	private final int value;

	private BlendEquationMode(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
