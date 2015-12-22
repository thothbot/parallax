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
 * The alpha test function
 * 
 * @author thothbot
 *
 */
public enum AlphaFunction implements GLEnum
{
	/**
	 * Never passes.
	 */
	NEVER(GL20.GL_NEVER),
	
	/**
	 * Passes if the incoming alpha value is less than the reference value.
	 */
	LESS(GL20.GL_LESS),
	
	/**
	 * Passes if the incoming alpha value is equal to the reference value.
	 */
	EQUAL(GL20.GL_EQUAL),
	
	/**
	 * Passes if the incoming alpha value is less than or equal to the reference value.
	 */
	LEQUAL(GL20.GL_LEQUAL),
	
	/**
	 * Passes if the incoming alpha value is greater than the reference value.
	 */
	GREATER(GL20.GL_GREATER),
	
	/**
	 * Passes if the incoming alpha value is not equal to the reference value.
	 */
	NOTEQUAL(GL20.GL_NOTEQUAL),
	
	/**
	 * Passes if the incoming alpha value is greater than or equal to the reference value.
	 */
	GEQUAL(GL20.GL_GEQUAL),
	
	/**
	 * Always passes (initial value).
	 */
	ALWAYS(GL20.GL_ALWAYS);

	private final int value;

	private AlphaFunction(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
