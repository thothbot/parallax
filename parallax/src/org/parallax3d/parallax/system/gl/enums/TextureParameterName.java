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
 * GL2 Texture Parameter name flags.
 * 
 * @author thothbot
 *
 */
public enum TextureParameterName implements GLEnum
{
	TEXTURE_MAG_FILTER(GL20.GL_TEXTURE_MAG_FILTER),
	TEXTURE_MIN_FILTER(GL20.GL_TEXTURE_MIN_FILTER),
	TEXTURE_WRAP_S(GL20.GL_TEXTURE_WRAP_S),
	TEXTURE_WRAP_T(GL20.GL_TEXTURE_WRAP_T),
	
	// Extensions
	TEXTURE_MAX_ANISOTROPY_EXT(GL20.GL_TEXTURE_MAX_ANISOTROPY_EXT);

	private final int value;
	
	private TextureParameterName(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
