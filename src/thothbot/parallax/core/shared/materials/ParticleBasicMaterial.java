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

import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.ShaderParticleBasic;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.core.Color3f;

public final class ParticleBasicMaterial extends Material 
{
	
	public static class ParticleBasicMaterialOptions extends Material.MaterialOptions 
	{
		public Color3f color = new Color3f(0xffffff);
		public Texture map = null;
		public float size = 1.0f;
		public boolean sizeAttenuation = true;
		public boolean fog = true;
	}
	
	private Color3f color;
	private Texture map;
	private float size;
	private boolean sizeAttenuation;

	public ParticleBasicMaterial(ParticleBasicMaterialOptions options)
	{
		super(options);
		this.color = options.color;
		this.map = options.map;
		this.size = options.size;
		this.sizeAttenuation = options.sizeAttenuation;
		this.fog = true;
	}

	public Color3f getColor()
	{
		return this.color;
	}
	
	public Texture getMap()
	{
		return this.map;
	}

	public float getSize() 
	{
		return size;
	}

	public boolean isSizeAttenuation() 
	{
		return sizeAttenuation;
	}
	
	public Shader getShaderId() 
	{
		return new ShaderParticleBasic();
	}

}
