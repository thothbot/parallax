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

/**
 * GL2 BlendingFactorSrc flags.
 * 
 * @author thothbot
 *
 */
public enum BlendingFactorSrc implements WebGLConstants 
{
	ZERO(WebGLConstants.ZERO),
	ONE(WebGLConstants.ONE),
	DST_COLOR(WebGLConstants.DST_COLOR),
	ONE_MINUS_DST_COLOR(WebGLConstants.ONE_MINUS_DST_COLOR),
	SRC_ALPHA_SATURATE(WebGLConstants.SRC_ALPHA_SATURATE),
	SRC_ALPHA(WebGLConstants.SRC_ALPHA),
	ONE_MINUS_SRC_ALPHA(WebGLConstants.ONE_MINUS_SRC_ALPHA),
	DST_ALPHA(WebGLConstants.DST_ALPHA),
	ONE_MINUS_DST_ALPHA(WebGLConstants.ONE_MINUS_DST_ALPHA);

	private final int value;

	private BlendingFactorSrc(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
