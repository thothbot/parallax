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

import org.parallax3d.parallax.Parallax;
import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.system.ThreejsObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of cubic texture.
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.CubeTexture")
public class CubeTexture extends Texture 
{
	private List<TextureData> images = new ArrayList<>();

	public CubeTexture(String url)
	{
		this(url, null);
	}

	public CubeTexture(String url, final ImageLoadHandler imageLoadHandler)
	{
		String[] parts = {"px", "nx", "py", "ny", "pz", "nz"};
		String urlStart = url.substring(0, url.indexOf("*"));
		String urlEnd = url.substring(url.indexOf("*") + 1, url.length());

		for(int i = 0, len = parts.length; i < len; i++)
		{
			final boolean last = i == len - 1;
			String part = parts[i];
			FileHandle file = Parallax.asset(urlStart + part + urlEnd);

			TextureData image = new PixmapTextureData();
			images.add(image);
			image.load(file, new PixmapTextureData.TextureLoadHandler() {

				@Override
				public void onLoaded(boolean success) {
					if(last)
					{
						setNeedsUpdate(true);
						if (imageLoadHandler != null)
							imageLoadHandler.onImageLoad(CubeTexture.this);
					}
				}
			});
		}

		setFlipY(false);
	}

	public CubeTexture(List<TextureData> images)
	{
		this.images = images;
		setFlipY(false);
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
}
