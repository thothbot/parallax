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

public enum BufferObjects implements GLEnum 
{
	ARRAY_BUFFER(GL20.GL_ARRAY_BUFFER),
	ELEMENT_ARRAY_BUFFER(GL20.GL_ELEMENT_ARRAY_BUFFER),
	ARRAY_BUFFER_BINDING(GL20.GL_ARRAY_BUFFER_BINDING),
	ELEMENT_ARRAY_BUFFER_BINDING(GL20.GL_ELEMENT_ARRAY_BUFFER_BINDING),
	STREAM_DRAW(GL20.GL_STREAM_DRAW),
	STATIC_DRAW(GL20.GL_STATIC_DRAW),
	DYNAMIC_DRAW(GL20.GL_DYNAMIC_DRAW),
	BUFFER_SIZE(GL20.GL_BUFFER_SIZE),
	BUFFER_USAGE(GL20.GL_BUFFER_USAGE),
	CURRENT_VERTEX_ATTRIB(GL20.GL_CURRENT_VERTEX_ATTRIB);

	private final int value;

	private BufferObjects(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
