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

/**
 * GL2 Texture Units flags.
 * 
 * @author thothbot
 *
 */
public enum TextureUnit implements GLConstants
{
	TEXTURE0(GLConstants.TEXTURE0),
	TEXTURE1(GLConstants.TEXTURE1),
	TEXTURE2(GLConstants.TEXTURE2),
	TEXTURE3(GLConstants.TEXTURE3),
	TEXTURE4(GLConstants.TEXTURE4),
	TEXTURE5(GLConstants.TEXTURE5),
	TEXTURE6(GLConstants.TEXTURE6),
	TEXTURE7(GLConstants.TEXTURE7),
	TEXTURE8(GLConstants.TEXTURE8),
	TEXTURE9(GLConstants.TEXTURE9),
	TEXTURE10(GLConstants.TEXTURE10),
	TEXTURE11(GLConstants.TEXTURE11),
	TEXTURE12(GLConstants.TEXTURE12),
	TEXTURE13(GLConstants.TEXTURE13),
	TEXTURE14(GLConstants.TEXTURE14),
	TEXTURE15(GLConstants.TEXTURE15),
	TEXTURE16(GLConstants.TEXTURE16),
	TEXTURE17(GLConstants.TEXTURE17),
	TEXTURE18(GLConstants.TEXTURE18),
	TEXTURE19(GLConstants.TEXTURE19),
	TEXTURE20(GLConstants.TEXTURE20),
	TEXTURE21(GLConstants.TEXTURE21),
	TEXTURE22(GLConstants.TEXTURE22),
	TEXTURE23(GLConstants.TEXTURE23),
	TEXTURE24(GLConstants.TEXTURE24),
	TEXTURE25(GLConstants.TEXTURE25),
	TEXTURE26(GLConstants.TEXTURE26),
	TEXTURE27(GLConstants.TEXTURE27),
	TEXTURE28(GLConstants.TEXTURE28),
	TEXTURE29(GLConstants.TEXTURE29),
	TEXTURE30(GLConstants.TEXTURE30),
	TEXTURE31(GLConstants.TEXTURE31),
	ACTIVE_TEXTURE(GLConstants.ACTIVE_TEXTURE);

	private final int value;

	private TextureUnit(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
