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
	NEVER(WebGLConstants.NEVER),
	
	/**
	 * Passes if the incoming alpha value is less than the reference value.
	 */
	LESS(WebGLConstants.LESS),
	
	/**
	 * Passes if the incoming alpha value is equal to the reference value.
	 */
	EQUAL(WebGLConstants.EQUAL),
	
	/**
	 * Passes if the incoming alpha value is less than or equal to the reference value.
	 */
	LEQUAL(WebGLConstants.LEQUAL),
	
	/**
	 * Passes if the incoming alpha value is greater than the reference value.
	 */
	GREATER(WebGLConstants.GREATER),
	
	/**
	 * Passes if the incoming alpha value is not equal to the reference value.
	 */
	NOTEQUAL(WebGLConstants.NOTEQUAL),
	
	/**
	 * Passes if the incoming alpha value is greater than or equal to the reference value.
	 */
	GEQUAL(WebGLConstants.GEQUAL),
	
	/**
	 * Always passes (initial value).
	 */
	ALWAYS(WebGLConstants.ALWAYS);

	private final int value;

	private AlphaFunction(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
