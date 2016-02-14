/*
 * Copyright 2015 Tony Houghton, h@realh.co.uk
 *
 * This file is part of the Android port of the Parallax project.
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

package org.parallax3d.parallax.system;

import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.graphics.textures.TextureData;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.system.gl.enums.PixelType;

/**
 * Certain types of texture need a dummy Image so that WebGlRenderer can
 * get width and height.
 */
public class DummyImage implements TextureData
{
	private int width;
	private int height;

	public DummyImage(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	@Override
	public void load(FileHandle file, TextureLoadHandler textureLoadHandler) {
		textureLoadHandler.onLoaded(true);
	}

	@Override
	public void glTexImage2D(GL20 gl, int target, PixelFormat pixelFormat, PixelType pixelType)
	{
		throw new RuntimeException("glTexImage2D not supported by DummyImage");
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public int getHeight()
	{
		return height;
	}

	@Override
	public DummyImage clampToMaxSize(int maxSize) {
		return this;
	}

	@Override
	public boolean isPowerOfTwo() {
		return false;
	}

	@Override
	public DummyImage toPowerOfTwo() {
		return this;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	@Override
	public void recycle()
	{ }
}
