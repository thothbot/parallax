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

public enum FramebufferErrorCode 
{
	FRAMEBUFFER_COMPLETE(GLEnum.FRAMEBUFFER_COMPLETE),
	FRAMEBUFFER_INCOMPLETE_ATTACHMENT(GLEnum.FRAMEBUFFER_INCOMPLETE_ATTACHMENT),
	FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT(GLEnum.FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT),
	FRAMEBUFFER_INCOMPLETE_DIMENSIONS(GLEnum.FRAMEBUFFER_INCOMPLETE_DIMENSIONS),
	FRAMEBUFFER_UNSUPPORTED(GLEnum.FRAMEBUFFER_UNSUPPORTED);

	private final int value;

	private FramebufferErrorCode(GLEnum glEnum) {
		this.value = glEnum.getValue();
	}
	
	/**
	 * Gets the enum's numerical value.
	 */
	public int getValue() {
		return value;
	}
}
