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

public enum StencilOp implements GLEnum
{
	ZERO(GL20.GL_ZERO),
	KEEP(GL20.GL_KEEP),
	REPLACE(GL20.GL_REPLACE),
	INCR(GL20.GL_INCR),
	DECR(GL20.GL_DECR),
	INVERT(GL20.GL_INVERT),
	INCR_WRAP(GL20.GL_INCR_WRAP),
	DECR_WRAP(GL20.GL_DECR_WRAP);

	private final int value;

	private StencilOp(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
