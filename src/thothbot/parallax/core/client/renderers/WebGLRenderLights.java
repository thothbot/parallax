/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package thothbot.parallax.core.client.renderers;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.shared.scenes.Scene;

/**
 * Lights used in the {@link Scene}.
 * 
 * @author thothbot
 *
 */
public class WebGLRenderLights
{

	public Float32Array ambient;
	public WebGLRenderLightsDirectional directional;
	public WebGLRenderLightsPoint point;
	public WebGLRenderLightsSpot spot;
	
	public WebGLRenderLights() 
	{
		this.ambient = (Float32Array) Float32Array.createArray();
		for(int i = 0; i < 3; i++)
			this.ambient.set(i, 0.0);
		
		this.directional = new WebGLRenderLightsDirectional();
		this.point       = new WebGLRenderLightsPoint();
		this.spot        = new WebGLRenderLightsSpot();
	}
	
	public class WebGLRenderLightsSpot extends WebGLRenderLightsPoint 
	{
		public Float32Array directions;
		public Float32Array angles;
		public Float32Array exponents;
		
		public WebGLRenderLightsSpot() 
		{
			super();
			this.directions = (Float32Array) Float32Array.createArray();
			this.angles     = (Float32Array) Float32Array.createArray();
			this.exponents  = (Float32Array) Float32Array.createArray();
		}
	}
	
	public class WebGLRenderLightsPoint extends WebGLRenderLightsDirectional 
	{
		public Float32Array distances;
		
		public WebGLRenderLightsPoint() 
		{
			super();
			this.distances = (Float32Array) Float32Array.createArray();
		}
	}

	public class WebGLRenderLightsDirectional 
	{
		public int length = 0;
		public Float32Array colors;
		public Float32Array positions;
		
		public WebGLRenderLightsDirectional() 
		{
			this.colors    = (Float32Array) Float32Array.createArray();
			this.positions = (Float32Array) Float32Array.createArray();
		}
	}
}
