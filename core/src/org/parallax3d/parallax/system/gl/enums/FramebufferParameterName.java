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

public enum FramebufferParameterName implements GLEnum
{
	FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE(GL20.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE),
	FRAMEBUFFER_ATTACHMENT_OBJECT_NAME(GL20.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME),
	FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL(GL20.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL),
	FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE(GL20.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE);

	private final int value;

	private FramebufferParameterName(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
