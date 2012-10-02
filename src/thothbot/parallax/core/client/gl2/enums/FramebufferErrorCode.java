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

import java.util.HashMap;
import java.util.Map;

import thothbot.parallax.core.client.gl2.WebGLConstants;

public enum FramebufferErrorCode implements GLEnum
{
	FRAMEBUFFER_COMPLETE(WebGLConstants.FRAMEBUFFER_COMPLETE),
	FRAMEBUFFER_INCOMPLETE_ATTACHMENT(WebGLConstants.FRAMEBUFFER_INCOMPLETE_ATTACHMENT),
	FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT(WebGLConstants.FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT),
	FRAMEBUFFER_INCOMPLETE_DIMENSIONS(WebGLConstants.FRAMEBUFFER_INCOMPLETE_DIMENSIONS),
	FRAMEBUFFER_UNSUPPORTED(WebGLConstants.FRAMEBUFFER_UNSUPPORTED);

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