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

public enum RenderbufferParameterName implements GLConstants
{
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_WIDTH(GLConstants.RENDERBUFFER_WIDTH),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_HEIGHT(GLConstants.RENDERBUFFER_HEIGHT),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_INTERNAL_FORMAT(GLConstants.RENDERBUFFER_INTERNAL_FORMAT),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_RED_SIZE(GLConstants.RENDERBUFFER_RED_SIZE),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_GREEN_SIZE(GLConstants.RENDERBUFFER_GREEN_SIZE),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_BLUE_SIZE(GLConstants.RENDERBUFFER_BLUE_SIZE),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_ALPHA_SIZE(GLConstants.RENDERBUFFER_ALPHA_SIZE),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_DEPTH_SIZE(GLConstants.RENDERBUFFER_DEPTH_SIZE),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_STENCIL_SIZE(GLConstants.RENDERBUFFER_STENCIL_SIZE);

	private final int value;

	private RenderbufferParameterName(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
