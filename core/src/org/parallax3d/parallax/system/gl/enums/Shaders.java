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

public enum Shaders implements GLEnum
{
	FRAGMENT_SHADER(GL20.GL_FRAGMENT_SHADER),
	VERTEX_SHADER(GL20.GL_VERTEX_SHADER),
	MAX_VERTEX_ATTRIBS(GL20.GL_MAX_VERTEX_ATTRIBS),
	MAX_VERTEX_UNIFORM_VECTORS(GL20.GL_MAX_VERTEX_UNIFORM_VECTORS),
	MAX_VARYING_VECTORS(GL20.GL_MAX_VARYING_VECTORS),
	MAX_COMBINED_TEXTURE_IMAGE_UNITS(GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS),
	MAX_VERTEX_TEXTURE_IMAGE_UNITS(GL20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS),
	MAX_TEXTURE_IMAGE_UNITS(GL20.GL_MAX_TEXTURE_IMAGE_UNITS),
	MAX_FRAGMENT_UNIFORM_VECTORS(GL20.GL_MAX_FRAGMENT_UNIFORM_VECTORS),
	SHADER_TYPE(GL20.GL_SHADER_TYPE),
	DELETE_STATUS(GL20.GL_DELETE_STATUS),
	LINK_STATUS(GL20.GL_LINK_STATUS),
	VALIDATE_STATUS(GL20.GL_VALIDATE_STATUS),
	ATTACHED_SHADERS(GL20.GL_ATTACHED_SHADERS),
	ACTIVE_UNIFORMS(GL20.GL_ACTIVE_UNIFORMS),
	ACTIVE_UNIFORM_MAX_LENGTH(GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH),
	ACTIVE_ATTRIBUTES(GL20.GL_ACTIVE_ATTRIBUTES),
	ACTIVE_ATTRIBUTE_MAX_LENGTH(GL20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH),
	SHADING_LANGUAGE_VERSION(GL20.GL_SHADING_LANGUAGE_VERSION),
	CURRENT_PROGRAM(GL20.GL_CURRENT_PROGRAM);

	private final int value;

	private Shaders(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
