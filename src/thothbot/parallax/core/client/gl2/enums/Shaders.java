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

public enum Shaders implements GLEnum
{
	FRAGMENT_SHADER(WebGLConstants.FRAGMENT_SHADER),
	VERTEX_SHADER(WebGLConstants.VERTEX_SHADER),
	MAX_VERTEX_ATTRIBS(WebGLConstants.MAX_VERTEX_ATTRIBS),
	MAX_VERTEX_UNIFORM_VECTORS(WebGLConstants.MAX_VERTEX_UNIFORM_VECTORS),
	MAX_VARYING_VECTORS(WebGLConstants.MAX_VARYING_VECTORS),
	MAX_COMBINED_TEXTURE_IMAGE_UNITS(WebGLConstants.MAX_COMBINED_TEXTURE_IMAGE_UNITS),
	MAX_VERTEX_TEXTURE_IMAGE_UNITS(WebGLConstants.MAX_VERTEX_TEXTURE_IMAGE_UNITS),
	MAX_TEXTURE_IMAGE_UNITS(WebGLConstants.MAX_TEXTURE_IMAGE_UNITS),
	MAX_FRAGMENT_UNIFORM_VECTORS(WebGLConstants.MAX_FRAGMENT_UNIFORM_VECTORS),
	SHADER_TYPE(WebGLConstants.SHADER_TYPE),
	DELETE_STATUS(WebGLConstants.DELETE_STATUS),
	LINK_STATUS(WebGLConstants.LINK_STATUS),
	VALIDATE_STATUS(WebGLConstants.VALIDATE_STATUS),
	ATTACHED_SHADERS(WebGLConstants.ATTACHED_SHADERS),
	ACTIVE_UNIFORMS(WebGLConstants.ACTIVE_UNIFORMS),
	ACTIVE_UNIFORM_MAX_LENGTH(WebGLConstants.ACTIVE_UNIFORM_MAX_LENGTH),
	ACTIVE_ATTRIBUTES(WebGLConstants.ACTIVE_ATTRIBUTES),
	ACTIVE_ATTRIBUTE_MAX_LENGTH(WebGLConstants.ACTIVE_ATTRIBUTE_MAX_LENGTH),
	SHADING_LANGUAGE_VERSION(WebGLConstants.SHADING_LANGUAGE_VERSION),
	CURRENT_PROGRAM(WebGLConstants.CURRENT_PROGRAM);

	private final int value;

	private Shaders(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
