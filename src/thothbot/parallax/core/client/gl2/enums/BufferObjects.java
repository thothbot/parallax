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

public enum BufferObjects 
{
	ARRAY_BUFFER(GLEnum.ARRAY_BUFFER),
	ELEMENT_ARRAY_BUFFER(GLEnum.ELEMENT_ARRAY_BUFFER),
	ARRAY_BUFFER_BINDING(GLEnum.ARRAY_BUFFER_BINDING),
	ELEMENT_ARRAY_BUFFER_BINDING(GLEnum.ELEMENT_ARRAY_BUFFER_BINDING),
	STREAM_DRAW(GLEnum.STREAM_DRAW),
	STATIC_DRAW(GLEnum.STATIC_DRAW),
	DYNAMIC_DRAW(GLEnum.DYNAMIC_DRAW),
	BUFFER_SIZE(GLEnum.BUFFER_SIZE),
	BUFFER_USAGE(GLEnum.BUFFER_USAGE),
	CURRENT_VERTEX_ATTRIB(GLEnum.CURRENT_VERTEX_ATTRIB);

	private final int value;

	private BufferObjects(GLEnum glEnum) {
		this.value = glEnum.getValue();
	}

	/**
	 * Gets the enum's numerical value.
	 */
	public int getValue() {
		return value;
	}

}
