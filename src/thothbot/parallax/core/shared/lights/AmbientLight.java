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

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.renderers.RendererLights;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.math.Color;

/**
 * This light's color gets applied to all the objects in the scene globally.
 * 
 * <pre>
 * {@code
 * AmbientLight light = new AmbientLight( 0xff0000 ); 
 * getScene().add( light );
 * }
 * </pre>
 * 
 * @author thothbot
 *
 */
public final class AmbientLight extends Light
{
	public static class UniformAmbient implements Light.UniformLight 
	{
		public Float32Array colors;
		
		@Override
		public void reset() 
		{
			this.colors = (Float32Array) Float32Array.createArray();
			for(int i = 0; i < 3; i++)
				this.colors.set(i, 0.0);
			
		}

		@Override
		public void refreshUniform(Map<String, Uniform> uniforms) 
		{
			uniforms.get("ambientLightColor").setValue( colors );
		}
	}
	
	public AmbientLight(int hex) {
		super(hex);
	}
	
	public AmbientLight clone() {

		AmbientLight light = new AmbientLight(0x000000);
		
		super.clone(light);

		return light;

	}
	
	@Override
	public void setupRendererLights(RendererLights zlights, boolean isGammaInput) 
	{
		Float32Array colors = zlights.ambient.colors;
	
		Color color = getColor();
		double r = 0, g = 0, b = 0;
		if(colors.getLength() == 3)
		{
			r = colors.get(0);
			g = colors.get(1);
			b = colors.get(2);
		}
		
		if ( isGammaInput ) 
		{
			r += color.getR() * color.getR();
			g += color.getG() * color.getG();
			b += color.getB() * color.getB();
		} 
		else 
		{
			r += color.getR();
			g += color.getG();
			b += color.getB();
		}

		colors.set( 0, r );
		colors.set( 1, g );
		colors.set( 2, b ); 
	}

}
