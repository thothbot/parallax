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

package thothbot.parallax.loader.shared.json;

import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.Material.BLENDING;

public enum JsoBlending
{
	NormalBlending        { @Override public BLENDING getValue() { return Material.BLENDING.NORMAL; }},
	AdditiveBlending      { @Override public BLENDING getValue() { return Material.BLENDING.ADDITIVE; }},
	SubtractiveBlending   { @Override public BLENDING getValue() { return Material.BLENDING.SUBTRACTIVE; }},
	MultiplyBlending      { @Override public BLENDING getValue() { return Material.BLENDING.MULTIPLY; }},
	AdditiveAlphaBlending { @Override public BLENDING getValue() { return Material.BLENDING.ADDITIVE_ALPHA; }},
	CustomBlending        { @Override public BLENDING getValue() { return Material.BLENDING.CUSTOM; }};
	
	public abstract Material.BLENDING getValue();
}
