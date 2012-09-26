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

public enum ErrorCode implements WebGLConstants
{
	NO_ERROR(WebGLConstants.NO_ERROR),
	INVALID_ENUM(WebGLConstants.INVALID_ENUM),
	INVALID_VALUE(WebGLConstants.INVALID_VALUE),
	INVALID_OPERATION(WebGLConstants.INVALID_OPERATION),
	OUT_OF_MEMORY(WebGLConstants.OUT_OF_MEMORY);

	private static Map<Integer, ErrorCode> errorCodeMap;
	private final int value;

	private ErrorCode(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	/**
	 * Parses an integer error code to its corresponding ErrorCode enum.
	 * 
	 * @param errorCode
	 */
	public static ErrorCode parseErrorCode(int errorCode) {
		if (errorCodeMap == null) {
			errorCodeMap = new HashMap<Integer, ErrorCode>();
			for (ErrorCode v : values()) {
				errorCodeMap.put(v.getValue(), v);
			}
		}
		return errorCodeMap.get(errorCode);
	}
}
