/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.shared.materials;

import thothbot.parallax.core.client.textures.Texture;

public interface HasEnvMap 
{
	public Texture getEnvMap();
	public void setEnvMap(Texture envMap);
	
	public Texture.OPERATIONS getCombine();
	public void setCombine(Texture.OPERATIONS combine);
	
	public float getReflectivity();
	public void setReflectivity(float reflectivity);
	
	public float getRefractionRatio();
	public void setRefractionRatio(float refractionRatio);
}
