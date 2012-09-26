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

public enum Shaders implements GLConstants
{
	FRAGMENT_SHADER(GLConstants.FRAGMENT_SHADER),
	VERTEX_SHADER(GLConstants.VERTEX_SHADER),
	MAX_VERTEX_ATTRIBS(GLConstants.MAX_VERTEX_ATTRIBS),
	MAX_VERTEX_UNIFORM_VECTORS(GLConstants.MAX_VERTEX_UNIFORM_VECTORS),
	MAX_VARYING_VECTORS(GLConstants.MAX_VARYING_VECTORS),
	MAX_COMBINED_TEXTURE_IMAGE_UNITS(GLConstants.MAX_COMBINED_TEXTURE_IMAGE_UNITS),
	MAX_VERTEX_TEXTURE_IMAGE_UNITS(GLConstants.MAX_VERTEX_TEXTURE_IMAGE_UNITS),
	MAX_TEXTURE_IMAGE_UNITS(GLConstants.MAX_TEXTURE_IMAGE_UNITS),
	MAX_FRAGMENT_UNIFORM_VECTORS(GLConstants.MAX_FRAGMENT_UNIFORM_VECTORS),
	SHADER_TYPE(GLConstants.SHADER_TYPE),
	DELETE_STATUS(GLConstants.DELETE_STATUS),
	LINK_STATUS(GLConstants.LINK_STATUS),
	VALIDATE_STATUS(GLConstants.VALIDATE_STATUS),
	ATTACHED_SHADERS(GLConstants.ATTACHED_SHADERS),
	ACTIVE_UNIFORMS(GLConstants.ACTIVE_UNIFORMS),
	ACTIVE_UNIFORM_MAX_LENGTH(GLConstants.ACTIVE_UNIFORM_MAX_LENGTH),
	ACTIVE_ATTRIBUTES(GLConstants.ACTIVE_ATTRIBUTES),
	ACTIVE_ATTRIBUTE_MAX_LENGTH(GLConstants.ACTIVE_ATTRIBUTE_MAX_LENGTH),
	SHADING_LANGUAGE_VERSION(GLConstants.SHADING_LANGUAGE_VERSION),
	CURRENT_PROGRAM(GLConstants.CURRENT_PROGRAM);

	private final int value;

	private Shaders(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
