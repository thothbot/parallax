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

import java.util.List;

import com.google.gwt.dom.client.Element;

/**
 * Implementation of cubic texture
 * 
 * @author thothbot
 *
 */
public class CubeTexture extends Texture 
{
	private List<Element> images;
	
	public CubeTexture(List<Element> images, Texture.MAPPING_MODE mapping)
	{
		this.images = images;
		setMapping( mapping );
	}

	/**
	 * Checks if the CubeTexture is valid or not
	 */
	public boolean isValid()
	{
		return (images.size() == 6);
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
}
