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

package thothbot.parallax.core.client.textures;

import thothbot.parallax.core.client.gl2.arrays.Uint8Array;

/**
 * Implementation of data texture.
 * 
 * @author thothbot
 *
 */
public class DataTexture extends Texture
{
	private Uint8Array data;
	private int width;
	private int height;
	
	public DataTexture( Uint8Array data, int width, int height)
	{
		this.data = data;
		this.width = width;
		this.height = height;
	}
	
	public Uint8Array getData() {
		return data;
	}

	public void setData(Uint8Array data) {
		this.data = data;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
