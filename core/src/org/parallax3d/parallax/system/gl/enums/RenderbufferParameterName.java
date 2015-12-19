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

public enum RenderbufferParameterName implements GLEnum
{
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_WIDTH(GL20.GL_RENDERBUFFER_WIDTH),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_HEIGHT(GL20.GL_RENDERBUFFER_HEIGHT),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_INTERNAL_FORMAT(GL20.GL_RENDERBUFFER_INTERNAL_FORMAT),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_RED_SIZE(GL20.GL_RENDERBUFFER_RED_SIZE),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_GREEN_SIZE(GL20.GL_RENDERBUFFER_GREEN_SIZE),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_BLUE_SIZE(GL20.GL_RENDERBUFFER_BLUE_SIZE),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_ALPHA_SIZE(GL20.GL_RENDERBUFFER_ALPHA_SIZE),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_DEPTH_SIZE(GL20.GL_RENDERBUFFER_DEPTH_SIZE),
	
	/** 
	 * Returns int 
	 */
	RENDERBUFFER_STENCIL_SIZE(GL20.GL_RENDERBUFFER_STENCIL_SIZE);

	private final int value;

	private RenderbufferParameterName(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
