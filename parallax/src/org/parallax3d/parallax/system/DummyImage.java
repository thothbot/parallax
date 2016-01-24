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

import org.parallax3d.parallax.graphics.textures.TextureData;

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
	public void glTexImage2D(int target)
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

	public void setWidth(int width)
	{
		this.width = width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	@Override
	public TextureData createScaledCopy(int width, int height)
	{
		return new DummyImage(width, height);
	}

	@Override
	public void recycle()
	{ }
}
