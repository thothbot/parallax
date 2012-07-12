/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.client.gl2.enums;

/**
 * GL2 Texture Units flags.
 * 
 * @author thothbot
 *
 */
public enum TextureUnit 
{
  TEXTURE0(GLenum.TEXTURE0),
  TEXTURE1(GLenum.TEXTURE1),
  TEXTURE2(GLenum.TEXTURE2),
  TEXTURE3(GLenum.TEXTURE3),
  TEXTURE4(GLenum.TEXTURE4),
  TEXTURE5(GLenum.TEXTURE5),
  TEXTURE6(GLenum.TEXTURE6),
  TEXTURE7(GLenum.TEXTURE7),
  TEXTURE8(GLenum.TEXTURE8),
  TEXTURE9(GLenum.TEXTURE9),
  TEXTURE10(GLenum.TEXTURE10),
  TEXTURE11(GLenum.TEXTURE11),
  TEXTURE12(GLenum.TEXTURE12),
  TEXTURE13(GLenum.TEXTURE13),
  TEXTURE14(GLenum.TEXTURE14),
  TEXTURE15(GLenum.TEXTURE15),
  TEXTURE16(GLenum.TEXTURE16),
  TEXTURE17(GLenum.TEXTURE17),
  TEXTURE18(GLenum.TEXTURE18),
  TEXTURE19(GLenum.TEXTURE19),
  TEXTURE20(GLenum.TEXTURE20),
  TEXTURE21(GLenum.TEXTURE21),
  TEXTURE22(GLenum.TEXTURE22),
  TEXTURE23(GLenum.TEXTURE23),
  TEXTURE24(GLenum.TEXTURE24),
  TEXTURE25(GLenum.TEXTURE25),
  TEXTURE26(GLenum.TEXTURE26),
  TEXTURE27(GLenum.TEXTURE27),
  TEXTURE28(GLenum.TEXTURE28),
  TEXTURE29(GLenum.TEXTURE29),
  TEXTURE30(GLenum.TEXTURE30),
  TEXTURE31(GLenum.TEXTURE31),
  ACTIVE_TEXTURE(GLenum.ACTIVE_TEXTURE);

  private final int value;

  private TextureUnit(GLenum GLenum) 
  {
    this.value = GLenum.getValue();
  }
  
  /**
   * Gets the enum's numerical value.
   */
  public int getValue() 
  {
    return value;
  }
}
