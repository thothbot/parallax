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

package thothbot.parallax.core.shared.lights;

import java.util.Map;

import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.math.Color;

/**
 * Abstract base class for lights.
 * 
 * @author thothbot
 *
 */
public class Light extends Object3D
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
	
	public Light clone() {
		return clone(new Light(0x000000));
	}
	
	public Light clone( Light light ) {
		
		super.clone(light);

		light.color.copy( this.color );

		return light;
	}
}
