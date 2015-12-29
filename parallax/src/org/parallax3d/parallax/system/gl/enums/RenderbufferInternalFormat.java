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
import org.parallax3d.parallax.system.gl.GLES20Ext;

public enum RenderbufferInternalFormat implements GLEnum
{
	RGBA4(GL20.GL_RGBA4),
	RGB5_A1(GL20.GL_RGB5_A1),
	RGB565(GL20.GL_RGB565),
	DEPTH_COMPONENT16(GL20.GL_DEPTH_COMPONENT16),
	STENCIL_INDEX8(GL20.GL_STENCIL_INDEX8),
	DEPTH_STENCIL(GLES20Ext.GL_DEPTH_STENCIL);

	private final int value;

	private RenderbufferInternalFormat(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
