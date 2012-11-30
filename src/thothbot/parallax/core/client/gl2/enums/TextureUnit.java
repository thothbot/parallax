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
 * GL2 Texture Units flags.
 * 
 * @author thothbot
 *
 */
public enum TextureUnit implements GLEnum
{
	TEXTURE0(WebGLConstants.TEXTURE0),
	TEXTURE1(WebGLConstants.TEXTURE1),
	TEXTURE2(WebGLConstants.TEXTURE2),
	TEXTURE3(WebGLConstants.TEXTURE3),
	TEXTURE4(WebGLConstants.TEXTURE4),
	TEXTURE5(WebGLConstants.TEXTURE5),
	TEXTURE6(WebGLConstants.TEXTURE6),
	TEXTURE7(WebGLConstants.TEXTURE7),
	TEXTURE8(WebGLConstants.TEXTURE8),
	TEXTURE9(WebGLConstants.TEXTURE9),
	TEXTURE10(WebGLConstants.TEXTURE10),
	TEXTURE11(WebGLConstants.TEXTURE11),
	TEXTURE12(WebGLConstants.TEXTURE12),
	TEXTURE13(WebGLConstants.TEXTURE13),
	TEXTURE14(WebGLConstants.TEXTURE14),
	TEXTURE15(WebGLConstants.TEXTURE15),
	TEXTURE16(WebGLConstants.TEXTURE16),
	TEXTURE17(WebGLConstants.TEXTURE17),
	TEXTURE18(WebGLConstants.TEXTURE18),
	TEXTURE19(WebGLConstants.TEXTURE19),
	TEXTURE20(WebGLConstants.TEXTURE20),
	TEXTURE21(WebGLConstants.TEXTURE21),
	TEXTURE22(WebGLConstants.TEXTURE22),
	TEXTURE23(WebGLConstants.TEXTURE23),
	TEXTURE24(WebGLConstants.TEXTURE24),
	TEXTURE25(WebGLConstants.TEXTURE25),
	TEXTURE26(WebGLConstants.TEXTURE26),
	TEXTURE27(WebGLConstants.TEXTURE27),
	TEXTURE28(WebGLConstants.TEXTURE28),
	TEXTURE29(WebGLConstants.TEXTURE29),
	TEXTURE30(WebGLConstants.TEXTURE30),
	TEXTURE31(WebGLConstants.TEXTURE31),
	ACTIVE_TEXTURE(WebGLConstants.ACTIVE_TEXTURE);

	private final int value;

	private TextureUnit(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
