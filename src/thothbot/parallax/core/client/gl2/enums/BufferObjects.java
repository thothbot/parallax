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

import thothbot.parallax.core.client.gl2.WebGLConstants;

public enum BufferObjects implements GLEnum 
{
	ARRAY_BUFFER(WebGLConstants.ARRAY_BUFFER),
	ELEMENT_ARRAY_BUFFER(WebGLConstants.ELEMENT_ARRAY_BUFFER),
	ARRAY_BUFFER_BINDING(WebGLConstants.ARRAY_BUFFER_BINDING),
	ELEMENT_ARRAY_BUFFER_BINDING(WebGLConstants.ELEMENT_ARRAY_BUFFER_BINDING),
	STREAM_DRAW(WebGLConstants.STREAM_DRAW),
	STATIC_DRAW(WebGLConstants.STATIC_DRAW),
	DYNAMIC_DRAW(WebGLConstants.DYNAMIC_DRAW),
	BUFFER_SIZE(WebGLConstants.BUFFER_SIZE),
	BUFFER_USAGE(WebGLConstants.BUFFER_USAGE),
	CURRENT_VERTEX_ATTRIB(WebGLConstants.CURRENT_VERTEX_ATTRIB);

	private final int value;

	private BufferObjects(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
