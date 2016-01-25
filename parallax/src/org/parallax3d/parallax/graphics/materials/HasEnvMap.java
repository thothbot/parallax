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

package org.parallax3d.parallax.graphics.materials;

import org.parallax3d.parallax.graphics.textures.Texture;

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
	Texture getEnvMap();

	/**
	 * Sets environmental texture
	 *
	 * @param envMap the texture
	 */
	<T extends Material> T setEnvMap(Texture envMap);

	Texture.OPERATIONS getCombine();
	<T extends Material> T setCombine(Texture.OPERATIONS combine);

	/**
	 * Gets reflectivity
	 *
	 * @return the reflectivity value
	 */
	double getReflectivity();

	/**
	 * Sets reflectivity
	 *
	 * @param reflectivity the reflectivity value
	 */
	<T extends Material> T setReflectivity(double reflectivity);

	/**
	 * Gets Refraction Ratio
	 */
	double getRefractionRatio();

	/**
	 * Sets Refraction Ratio
	 */
	<T extends Material> T setRefractionRatio(double refractionRatio);
}
