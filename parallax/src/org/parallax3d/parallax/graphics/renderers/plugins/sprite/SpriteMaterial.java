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

package org.parallax3d.parallax.graphics.renderers.plugins.sprite;

import org.parallax3d.parallax.graphics.materials.HasColor;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;

public class SpriteMaterial extends Material implements HasColor {

	private Color color;
	
	private Texture map;

	private double rotation = 0;

	private boolean isFog = false;
	
	public SpriteMaterial() 
	{
		setColor( 0xffffff );
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
	public SpriteMaterial setColor(Color color) {
		this.color = color;
		return this;
	}

	@Override
	public SpriteMaterial setColor(int color) {
		this.color = new Color( color );
		return this;
	}
	
	public Texture getMap() {
		return map;
	}

	public SpriteMaterial setMap(Texture map) {
		this.map = map;
		return this;
	}

	public double getRotation() {
		return rotation;
	}

	public SpriteMaterial setRotation(double rotation) {
		this.rotation = rotation;
		return this;
	}

	public boolean isFog() {
		return isFog;
	}

	public SpriteMaterial setFog(boolean isFog) {
		this.isFog = isFog;
		return this;
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
