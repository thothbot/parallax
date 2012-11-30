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

/**
 * GL2 Texture Target flags.
 * 
 * @author thothbot
 *
 */
public enum TextureTarget implements GLEnum
{
	TEXTURE_2D(WebGLConstants.TEXTURE_2D),
	TEXTURE(WebGLConstants.TEXTURE),
	TEXTURE_CUBE_MAP(WebGLConstants.TEXTURE_CUBE_MAP),
	TEXTURE_BINDING_CUBE_MAP(WebGLConstants.TEXTURE_BINDING_CUBE_MAP),
	TEXTURE_CUBE_MAP_POSITIVE_X(WebGLConstants.TEXTURE_CUBE_MAP_POSITIVE_X),
	TEXTURE_CUBE_MAP_NEGATIVE_X(WebGLConstants.TEXTURE_CUBE_MAP_NEGATIVE_X),
	TEXTURE_CUBE_MAP_POSITIVE_Y(WebGLConstants.TEXTURE_CUBE_MAP_POSITIVE_Y),
	TEXTURE_CUBE_MAP_NEGATIVE_Y(WebGLConstants.TEXTURE_CUBE_MAP_NEGATIVE_Y),
	TEXTURE_CUBE_MAP_POSITIVE_Z(WebGLConstants.TEXTURE_CUBE_MAP_POSITIVE_Z),
	TEXTURE_CUBE_MAP_NEGATIVE_Z(WebGLConstants.TEXTURE_CUBE_MAP_NEGATIVE_Z),
	MAX_CUBE_MAP_TEXTURE_SIZE(WebGLConstants.MAX_CUBE_MAP_TEXTURE_SIZE);

	private final int value;

	private TextureTarget(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
