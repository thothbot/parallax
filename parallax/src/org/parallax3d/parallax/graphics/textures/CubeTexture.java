/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * Copyright 2015 Tony Houghton, h@realh.co.uk
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

import org.parallax3d.parallax.App;
import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.system.ThreeJsObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of cubic texture.
 * 
 * @author thothbot
 *
 */
@ThreeJsObject("THREE.CubeTexture")
public class CubeTexture extends Texture 
{
	private List<PixmapTextureData> images;

	private int loadedCount;

	public CubeTexture(String url)
	{
		this(getImagesFromUrl(url));
	}

	public CubeTexture(List<PixmapTextureData> images)
	{
		this.images = images;
		setFlipY(false);

		loadedCount = 0;
	}

	/**
	 * Checks if the CubeTexture is valid or not
	 */
	public boolean isValid()
	{
		return (images != null && images.size() == 6);
	}
	
	/**
	 * Getting Image by its index
	 * 
	 * @param index the index of Image in the Cubic texture
	 * 
	 * @return the image
	 */
	public TextureData getImage(int index)
	{
		return this.images.get(index);
	}

	private static List<PixmapTextureData> getImagesFromUrl(String url)
	{
		List<PixmapTextureData> images = new ArrayList<PixmapTextureData>();

		String[] parts = {"px", "nx", "py", "ny", "pz", "nz"};
		String urlStart = url.substring(0, url.indexOf("*"));
		String urlEnd = url.substring(url.indexOf("*") + 1, url.length());

		for(String part: parts)
		{
			FileHandle file = App.files.internal(urlStart + part + urlEnd);
			images.add(new PixmapTextureData(file));
		}

		return images;
	}
}
