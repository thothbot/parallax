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

package org.parallax3d.parallax.plugins.sprite;

import org.parallax3d.parallax.shaders.Shader;
import org.parallax3d.parallax.textures.Texture;
import org.parallax3d.parallax.materials.HasColor;
import org.parallax3d.parallax.materials.Material;
import org.parallax3d.parallax.math.Color;

public class SpriteMaterial extends Material implements HasColor {

	private Color color;
	
	private Texture map;

	private double rotation = 0;

	private boolean isFog = false;
	
	public SpriteMaterial() 
	{
		setColor(new Color(0xffffff));
	}
	
	@Override
	protected Shader getAssociatedShader()
	{
		return null;
	}
	
	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
		
	}
	
	public Texture getMap() {
		return map;
	}

	public void setMap(Texture map) {
		this.map = map;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public boolean isFog() {
		return isFog;
	}

	public void setFog(boolean isFog) {
		this.isFog = isFog;
	}

	@Override
	public SpriteMaterial clone() 
	{
		SpriteMaterial material = new SpriteMaterial();

		super.clone( material );

		material.color.copy( this.color );
		material.map = this.map;

		material.rotation = this.rotation;

		material.isFog = this.isFog;

		return material;
	}
}
