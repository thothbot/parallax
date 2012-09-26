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
public enum TextureUnit 
{
  TEXTURE0(GLEnum.TEXTURE0),
  TEXTURE1(GLEnum.TEXTURE1),
  TEXTURE2(GLEnum.TEXTURE2),
  TEXTURE3(GLEnum.TEXTURE3),
  TEXTURE4(GLEnum.TEXTURE4),
  TEXTURE5(GLEnum.TEXTURE5),
  TEXTURE6(GLEnum.TEXTURE6),
  TEXTURE7(GLEnum.TEXTURE7),
  TEXTURE8(GLEnum.TEXTURE8),
  TEXTURE9(GLEnum.TEXTURE9),
  TEXTURE10(GLEnum.TEXTURE10),
  TEXTURE11(GLEnum.TEXTURE11),
  TEXTURE12(GLEnum.TEXTURE12),
  TEXTURE13(GLEnum.TEXTURE13),
  TEXTURE14(GLEnum.TEXTURE14),
  TEXTURE15(GLEnum.TEXTURE15),
  TEXTURE16(GLEnum.TEXTURE16),
  TEXTURE17(GLEnum.TEXTURE17),
  TEXTURE18(GLEnum.TEXTURE18),
  TEXTURE19(GLEnum.TEXTURE19),
  TEXTURE20(GLEnum.TEXTURE20),
  TEXTURE21(GLEnum.TEXTURE21),
  TEXTURE22(GLEnum.TEXTURE22),
  TEXTURE23(GLEnum.TEXTURE23),
  TEXTURE24(GLEnum.TEXTURE24),
  TEXTURE25(GLEnum.TEXTURE25),
  TEXTURE26(GLEnum.TEXTURE26),
  TEXTURE27(GLEnum.TEXTURE27),
  TEXTURE28(GLEnum.TEXTURE28),
  TEXTURE29(GLEnum.TEXTURE29),
  TEXTURE30(GLEnum.TEXTURE30),
  TEXTURE31(GLEnum.TEXTURE31),
  ACTIVE_TEXTURE(GLEnum.ACTIVE_TEXTURE);

  private final int value;

  private TextureUnit(GLEnum GLEnum) 
  {
    this.value = GLEnum.getValue();
  }
  
  /**
   * Gets the enum's numerical value.
   */
  public int getValue() 
  {
    return value;
  }
}
