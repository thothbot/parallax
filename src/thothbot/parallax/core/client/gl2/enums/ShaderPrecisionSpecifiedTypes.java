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

import thothbot.parallax.core.client.gl2.WebGLConstants;

public enum ShaderPrecisionSpecifiedTypes implements GLEnum
{
	LOW_FLOAT(WebGLConstants.LOW_FLOAT),
	MEDIUM_FLOAT(WebGLConstants.MEDIUM_FLOAT),
	HIGH_FLOAT(WebGLConstants.HIGH_FLOAT),
	LOW_INT(WebGLConstants.LOW_INT),
	MEDIUM_INT(WebGLConstants.MEDIUM_INT),
	HIGH_INT(WebGLConstants.HIGH_INT);

	private final int value;

	private ShaderPrecisionSpecifiedTypes(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
