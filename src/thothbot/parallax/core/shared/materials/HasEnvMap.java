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

package thothbot.parallax.core.shared.materials;

import thothbot.parallax.core.client.textures.Texture;

/**
 * Material has environmental texture.
 * 
 * @author thothbot
 *
 */
public interface HasEnvMap 
{
	/**
	 * Gets environmental texture
	 * 
	 * @return the texture
	 */
	public Texture getEnvMap();
	
	/**
	 * Sets environmental texture
	 * 
	 * @param envMap the texture
	 */
	public void setEnvMap(Texture envMap);
	
	public Texture.OPERATIONS getCombine();
	public void setCombine(Texture.OPERATIONS combine);
	
	/**
	 * Gets reflectivity
	 * 
	 * @return the reflectivity value
	 */
	public float getReflectivity();
	
	/**
	 * Sets reflectivity
	 * 
	 * @param reflectivity the reflectivity value
	 */
	public void setReflectivity(float reflectivity);
	
	/**
	 * Gets Refraction Ratio
	 */
	public float getRefractionRatio();
	
	/**
	 * Sets Refraction Ratio
	 */
	public void setRefractionRatio(float refractionRatio);
}
