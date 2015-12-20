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

package org.parallax3d.parallax.graphics.lights;

import java.nio.FloatBuffer;
import java.util.Map;

import org.parallax3d.parallax.graphics.renderers.RendererLights;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.system.ThreeJsObject;

/**
 * Abstract base class for lights.
 * 
 * @author thothbot
 *
 */
@ThreeJsObject("THREE.Light")
public abstract class Light extends Object3D
{
	public interface UniformLight 
	{
		public void reset();
		public void refreshUniform(Map<String, Uniform> uniforms);
	}
	
	private Color color;
	
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
		
	public Light clone( Light light ) {
		
		super.clone(light);

		light.color.copy( this.color );

		return light;
	}
	
	public abstract void setupRendererLights(RendererLights zlights, boolean isGammaInput);

	protected void setColorGamma( FloatBuffer array, int offset, Color color, float intensity )
	{
		array.put(offset, color.getR() * color.getR() * intensity * intensity);
		array.put(offset + 1, color.getG() * color.getG() * intensity * intensity);
		array.put(offset + 2, color.getB() * color.getB() * intensity * intensity);
	}

	protected void  setColorLinear( FloatBuffer array, int offset, Color color, float intensity )
	{
		array.put(offset, color.getR() * intensity);
		array.put(offset + 1, color.getG() * intensity);
		array.put( offset + 2, color.getB() * intensity);
	}
}
