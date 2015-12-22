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
 * GL2 Texture Units flags.
 * 
 * @author thothbot
 *
 */
public enum TextureUnit implements GLEnum
{
	TEXTURE0(GL20.GL_TEXTURE0),
	TEXTURE1(GL20.GL_TEXTURE1),
	TEXTURE2(GL20.GL_TEXTURE2),
	TEXTURE3(GL20.GL_TEXTURE3),
	TEXTURE4(GL20.GL_TEXTURE4),
	TEXTURE5(GL20.GL_TEXTURE5),
	TEXTURE6(GL20.GL_TEXTURE6),
	TEXTURE7(GL20.GL_TEXTURE7),
	TEXTURE8(GL20.GL_TEXTURE8),
	TEXTURE9(GL20.GL_TEXTURE9),
	TEXTURE10(GL20.GL_TEXTURE10),
	TEXTURE11(GL20.GL_TEXTURE11),
	TEXTURE12(GL20.GL_TEXTURE12),
	TEXTURE13(GL20.GL_TEXTURE13),
	TEXTURE14(GL20.GL_TEXTURE14),
	TEXTURE15(GL20.GL_TEXTURE15),
	TEXTURE16(GL20.GL_TEXTURE16),
	TEXTURE17(GL20.GL_TEXTURE17),
	TEXTURE18(GL20.GL_TEXTURE18),
	TEXTURE19(GL20.GL_TEXTURE19),
	TEXTURE20(GL20.GL_TEXTURE20),
	TEXTURE21(GL20.GL_TEXTURE21),
	TEXTURE22(GL20.GL_TEXTURE22),
	TEXTURE23(GL20.GL_TEXTURE23),
	TEXTURE24(GL20.GL_TEXTURE24),
	TEXTURE25(GL20.GL_TEXTURE25),
	TEXTURE26(GL20.GL_TEXTURE26),
	TEXTURE27(GL20.GL_TEXTURE27),
	TEXTURE28(GL20.GL_TEXTURE28),
	TEXTURE29(GL20.GL_TEXTURE29),
	TEXTURE30(GL20.GL_TEXTURE30),
	TEXTURE31(GL20.GL_TEXTURE31),
	ACTIVE_TEXTURE(GL20.GL_ACTIVE_TEXTURE);

	private final int value;

	private TextureUnit(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
