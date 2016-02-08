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

package org.parallax3d.parallax.graphics.textures;

import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.system.gl.arrays.Uint8Array;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;

/**
 * Implementation of data texture.
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.DataTexture")
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

	public DataTexture setData(Uint8Array data) {
		this.data = data;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public DataTexture setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public DataTexture setHeight(int height) {
		this.height = height;
		return this;
	}

	public DataTexture generateDataTexture( Color color )
	{
		int size = width * height;
		Uint8Array data = Uint8Array.create(3 * size);

		int r = (int)Math.floor( color.getR() * 255 );
		int g = (int)Math.floor( color.getG() * 255 );
		int b = (int)Math.floor( color.getB() * 255 );

		for ( int i = 0; i < size; i ++ )
		{
			data.set(i * 3, r);
			data.set(i * 3 + 1, g);
			data.set(i * 3 + 2, b);

		}

		setData( data );
		setFormat( PixelFormat.RGB );
		setNeedsUpdate( true );

		return this;
	}
}
