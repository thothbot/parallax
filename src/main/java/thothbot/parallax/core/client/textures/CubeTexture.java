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

package thothbot.parallax.core.client.textures;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Image;

/**
 * Implementation of cubic texture.
 * 
 * @author thothbot
 *
 */
public class CubeTexture extends Texture 
{
	private List<Element> images;
	
	private int loadedCount;
	public CubeTexture(String url)
	{
		this(url, null);
	}

	public CubeTexture(String url, ImageLoadHandler imageLoadHandler)
	{
		this(getImagesFromUrl(url), imageLoadHandler);
	}

	public CubeTexture(List<Image> images, final ImageLoadHandler imageLoadHandler)
	{
		this(new ArrayList<Element>());
		setFlipY(false);
	
		loadedCount = 0;
		for(Image image: images)
		{
			loadImage(image, new Loader() {
				
				@Override
				public void onLoad() {
					if(++loadedCount == 6)
					{
						setNeedsUpdate(true);
						if (imageLoadHandler != null)
							imageLoadHandler.onImageLoad(CubeTexture.this);
					}
				}
			});
			this.images.add(image.getElement());
		}
	}
	
	public CubeTexture(List<Element> images)
	{
		this.images = images;
	}

	/**
	 * Checks if the CubeTexture is valid or not
	 */
	public boolean isValid()
	{
		return (images != null && images.size() == 6);
	}
	
	/**
	 * Getting Image Element by its index
	 * 
	 * @param index the index of Image element in the Cubic texture
	 * 
	 * @return the image Element
	 */
	public Element getImage(int index)
	{
		return this.images.get(index);
	}
	
	private static List<Image> getImagesFromUrl(String url)
	{
		List<Image> images = new ArrayList<Image>();
		
		String[] parts = {"px", "nx", "py", "ny", "pz", "nz"};
		String urlStart = url.substring(0, url.indexOf("*"));
		String urlEnd = url.substring(url.indexOf("*") + 1, url.length());
		
		for(String part: parts)
		{
			Image image = new Image();
			image.setUrl(urlStart + part + urlEnd);
			images.add(image);
		}

		return images;
	}
}
