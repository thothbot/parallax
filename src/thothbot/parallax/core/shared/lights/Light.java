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

package thothbot.parallax.core.shared.lights;

import java.util.Map;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.core.Color;
import thothbot.parallax.core.shared.objects.Object3D;

/**
 * Abstract base class for lights.
 * 
 * @author thothbot
 *
 */
public abstract class Light extends Object3D
{
	public interface UniformLight 
	{
		public void reset();
		public void refreshUniform(Map<String, Uniform> uniforms);
	}

	private Color color;
	private boolean isOnlyShadow = false;
	
	public Light(int hex) 
	{
		super();
		this.color = new Color(hex);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public boolean isOnlyShadow() {
		return this.isOnlyShadow;
	}

	public boolean isAllocateShadows() {
		return false;
	}
	
	public abstract void setupRendererLights(RendererLights zlights, boolean isGammaInput);

	protected void setColorGamma( Float32Array array, int offset, Color color, double intensity ) 
	{
		array.set( offset,     color.getR() * color.getR() * intensity * intensity);
		array.set( offset + 1, color.getG() * color.getG() * intensity * intensity);
		array.set( offset + 2, color.getB() * color.getB() * intensity * intensity);
	}

	protected void  setColorLinear( Float32Array array, int offset, Color color, double intensity ) 
	{
		array.set( offset,     color.getR() * intensity);
		array.set( offset + 1, color.getG() * intensity);
		array.set( offset + 2, color.getB() * intensity);
	}
}
