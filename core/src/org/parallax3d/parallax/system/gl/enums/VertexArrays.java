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

public enum VertexArrays implements GLEnum
{
	VERTEX_ATTRIB_ARRAY_ENABLED(GL20.GL_VERTEX_ATTRIB_ARRAY_ENABLED),
	VERTEX_ATTRIB_ARRAY_SIZE(GL20.GL_VERTEX_ATTRIB_ARRAY_SIZE),
	VERTEX_ATTRIB_ARRAY_STRIDE(GL20.GL_VERTEX_ATTRIB_ARRAY_STRIDE),
	VERTEX_ATTRIB_ARRAY_TYPE(GL20.GL_VERTEX_ATTRIB_ARRAY_TYPE),
	VERTEX_ATTRIB_ARRAY_NORMALIZED(GL20.GL_VERTEX_ATTRIB_ARRAY_NORMALIZED),
	VERTEX_ATTRIB_ARRAY_POINTER(GL20.GL_VERTEX_ATTRIB_ARRAY_POINTER),
	VERTEX_ATTRIB_ARRAY_BUFFER_BINDING(GL20.GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING);

	private final int value;

	private VertexArrays(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
