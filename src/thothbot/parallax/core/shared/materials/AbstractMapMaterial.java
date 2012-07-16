/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
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
import thothbot.parallax.core.shared.core.Color3f;

public abstract class AbstractMapMaterial extends Material 
{
	public static class AbstractMapMaterialOptions extends Material.MaterialOptions
	{
		public Color3f color = new Color3f( 0xffffff );
		public Texture map = null;
		public Texture envMap = null;
		public Texture lightMap = null;
		public float reflectivity = 1.0f;
		public float refractionRatio = 0.98f;
		public boolean fog = true;
	}

	protected Color3f color;
	protected Texture map;
	protected Texture envMap;
	protected Texture lightMap;
	protected float reflectivity;
	protected float refractionRatio;
	
	protected Texture.OPERATIONS combine;
	
	public AbstractMapMaterial(AbstractMapMaterialOptions options)
	{
		super(options);
		this.color = options.color;
		this.map = options.map;
		this.envMap = options.envMap;
		this.lightMap = options.lightMap;
		this.reflectivity = options.reflectivity;
		this.refractionRatio = options.refractionRatio;
		this.fog = options.fog;
	}

	public Color3f getColor(){
		return this.color;
	}
	
	public void setColor(Color3f color)
	{
		this.color = color;
	}
	
	public Texture getMap(){
		return this.map;
	}
	
	public void setMap(Texture map)
	{
		this.map = map;
	}
	
	public Texture getLightMap(){
		return this.lightMap;
	}
	
	public void setLightMap(Texture lightMap)
	{
		this.lightMap = lightMap;
	}

	public Texture getEnvMap() 
	{
		return envMap;
	}
	
	public void setEnvMap(Texture envMap)
	{
		this.envMap = envMap;
	}
	
	public void setReflectivity(float reflectivity)
	{
		this.reflectivity = reflectivity;
	}

	public float getReflectivity()
	{
		return reflectivity;
	}
	
	public float getRefractionRatio()
	{
		return this.refractionRatio;
	}
	
	public void setRefractionRatio(float refractionRatio)
	{
		this.refractionRatio = refractionRatio;
	}
	
	public Texture.OPERATIONS getCombine()
	{
		return this.combine;
	}
	
	public void setCombine(Texture.OPERATIONS combine)
	{
		this.combine = combine;
	}

	public boolean bufferGuessUVType () 
	{
		if ( this.map != null || this.lightMap != null)
			return true;

		return false;
	}

}
