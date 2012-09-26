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

package thothbot.parallax.core.client.gl2.extension;

import thothbot.parallax.core.client.gl2.WebGLExtension;

public class WebGLCompressedTextureS3tc extends WebGLExtension 
{
	public static final int COMPRESSED_RGB_S3TC_DXT1_EXT  = 0x83F0;
	public static final int COMPRESSED_RGBA_S3TC_DXT1_EXT = 0x83F1;
	public static final int COMPRESSED_RGBA_S3TC_DXT3_EXT = 0x83F2;
	public static final int COMPRESSED_RGBA_S3TC_DXT5_EXT = 0x83F3;
}
