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

import java.util.HashMap;
import java.util.Map;

import thothbot.parallax.core.client.gl2.WebGLConstants;

public enum ErrorCode implements GLEnum
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
