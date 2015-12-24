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

import org.parallax3d.parallax.system.Image;
import org.parallax3d.parallax.system.ThreeJsObject;

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
	private List<Image> images;
	
	public CubeTexture(List<Image> images)
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
	 * Getting Image by its index
	 * 
	 * @param index the index of Image in the Cubic texture
	 * 
	 * @return the image
	 */
	public Image getImage(int index)
	{
		return this.images.get(index);
	}
}
