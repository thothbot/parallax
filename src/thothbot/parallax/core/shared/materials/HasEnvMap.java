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
	public double getReflectivity();
	
	/**
	 * Sets reflectivity
	 * 
	 * @param reflectivity the reflectivity value
	 */
	public void setReflectivity(double reflectivity);
	
	/**
	 * Gets Refraction Ratio
	 */
	public double getRefractionRatio();
	
	/**
	 * Sets Refraction Ratio
	 */
	public void setRefractionRatio(double refractionRatio);
}
