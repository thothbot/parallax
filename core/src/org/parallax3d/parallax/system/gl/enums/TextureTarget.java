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

/**
 * GL2 Texture Target flags.
 * 
 * @author thothbot
 *
 */
public enum TextureTarget implements GLEnum
{
	TEXTURE_2D(GL20.GL_TEXTURE_2D),
	TEXTURE(GL20.GL_TEXTURE),
	TEXTURE_CUBE_MAP(GL20.GL_TEXTURE_CUBE_MAP),
	TEXTURE_BINDING_CUBE_MAP(GL20.GL_TEXTURE_BINDING_CUBE_MAP),
	TEXTURE_CUBE_MAP_POSITIVE_X(GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_X),
	TEXTURE_CUBE_MAP_NEGATIVE_X(GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X),
	TEXTURE_CUBE_MAP_POSITIVE_Y(GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y),
	TEXTURE_CUBE_MAP_NEGATIVE_Y(GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y),
	TEXTURE_CUBE_MAP_POSITIVE_Z(GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z),
	TEXTURE_CUBE_MAP_NEGATIVE_Z(GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z),
	MAX_CUBE_MAP_TEXTURE_SIZE(GL20.GL_MAX_CUBE_MAP_TEXTURE_SIZE);

	private final int value;

	private TextureTarget(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
