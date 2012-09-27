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
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.shared.core.Color;

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

	public DataTexture( int width, int height )
	{
		this.width = width;
		this.height = height;
	}

	/**
	 * Constructor which can be used to generate random data texture.
	 * 
	 * @param width
	 * @param height
	 * @param color
	 */
	public DataTexture( int width, int height, Color color )
	{
		this.width = width;
		this.height = height;

		generateDataTexture(color);
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
	
	public void generateDataTexture( Color color ) 
	{
		int size = width * height;
		Uint8Array data = Uint8Array.create( 3 * size );

		int r = (int)Math.floor( color.getR() * 255 );
		int g = (int)Math.floor( color.getG() * 255 );
		int b = (int)Math.floor( color.getB() * 255 );

		for ( int i = 0; i < size; i ++ ) 
		{
			data.set( i * 3, r);
			data.set( i * 3 + 1, g);
			data.set( i * 3 + 2, b);

		}

		setData( data );
		setFormat( PixelFormat.RGB );
		setNeedsUpdate( true );
	}
}
