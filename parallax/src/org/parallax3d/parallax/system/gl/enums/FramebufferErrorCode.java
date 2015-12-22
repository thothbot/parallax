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

import java.util.HashMap;
import java.util.Map;

public enum FramebufferErrorCode implements GLEnum
{
	FRAMEBUFFER_COMPLETE(GL20.GL_FRAMEBUFFER_COMPLETE),
	FRAMEBUFFER_INCOMPLETE_ATTACHMENT(GL20.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT),
	FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT(GL20.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT),
	FRAMEBUFFER_INCOMPLETE_DIMENSIONS(GL20.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS),
	FRAMEBUFFER_UNSUPPORTED(GL20.GL_FRAMEBUFFER_UNSUPPORTED);

	private static Map<Integer, FramebufferErrorCode> errorCodeMap;
	private final int value;

	private FramebufferErrorCode(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	/**
	 * Parses an integer framebuffer error code to its corresponding 
	 * FramebufferErrorCode enum.
	 * 
	 * @param errorCode
	 */
	public static FramebufferErrorCode parseErrorCode(int errorCode) 
	{
		if (errorCodeMap == null) 
		{
			errorCodeMap = new HashMap<Integer, FramebufferErrorCode>();
			for (FramebufferErrorCode v : values()) {
				errorCodeMap.put(v.getValue(), v);
			}
		}
		return errorCodeMap.get(errorCode);
	}
}
