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

package org.parallax3d.parallax.loaders.json;

import org.parallax3d.parallax.graphics.materials.Material;

public enum JsoBlending
{
	NormalBlending        { @Override public Material.BLENDING getValue() { return Material.BLENDING.NORMAL; }},
	AdditiveBlending      { @Override public Material.BLENDING getValue() { return Material.BLENDING.ADDITIVE; }},
	SubtractiveBlending   { @Override public Material.BLENDING getValue() { return Material.BLENDING.SUBTRACTIVE; }},
	MultiplyBlending      { @Override public Material.BLENDING getValue() { return Material.BLENDING.MULTIPLY; }},
	AdditiveAlphaBlending { @Override public Material.BLENDING getValue() { return Material.BLENDING.ADDITIVE_ALPHA; }},
	CustomBlending        { @Override public Material.BLENDING getValue() { return Material.BLENDING.CUSTOM; }};

	public abstract Material.BLENDING getValue();
}
