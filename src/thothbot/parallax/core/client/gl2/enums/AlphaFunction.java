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

/**
 * The alpha test function
 * 
 * @author thothbot
 *
 */
public enum AlphaFunction implements GLConstants
{
	/**
	 * Never passes.
	 */
	NEVER(GLConstants.NEVER),
	
	/**
	 * Passes if the incoming alpha value is less than the reference value.
	 */
	LESS(GLConstants.LESS),
	
	/**
	 * Passes if the incoming alpha value is equal to the reference value.
	 */
	EQUAL(GLConstants.EQUAL),
	
	/**
	 * Passes if the incoming alpha value is less than or equal to the reference value.
	 */
	LEQUAL(GLConstants.LEQUAL),
	
	/**
	 * Passes if the incoming alpha value is greater than the reference value.
	 */
	GREATER(GLConstants.GREATER),
	
	/**
	 * Passes if the incoming alpha value is not equal to the reference value.
	 */
	NOTEQUAL(GLConstants.NOTEQUAL),
	
	/**
	 * Passes if the incoming alpha value is greater than or equal to the reference value.
	 */
	GEQUAL(GLConstants.GEQUAL),
	
	/**
	 * Always passes (initial value).
	 */
	ALWAYS(GLConstants.ALWAYS);

	private final int value;

	private AlphaFunction(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
