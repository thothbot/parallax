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

import thothbot.parallax.core.client.renderers.RendererLights;
import thothbot.parallax.core.shared.math.Vector3;

public class AreaLight extends Light implements HasIntensity 
{

	public Vector3 normal;
	public Vector3 right;

	private double intensity;

	public double width = 1.0;
	public double height = 1.0;

	public double constantAttenuation = 1.5;
	public double linearAttenuation = 0.5;
	public double quadraticAttenuation = 0.1;
	
	public AreaLight ( int color) {
		this(color, 1.0);
	}
	
	public AreaLight ( int color, double intensity ) {

		super(color);

		this.normal = new Vector3( 0, - 1, 0 );
		this.right = new Vector3( 1, 0, 0 );

		this.intensity = intensity;
	}

	@Override
	public void setupRendererLights(RendererLights zlights, boolean isGammaInput) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Gets Light's intensity.
	 */
	public double getIntensity() {
		return this.intensity;
	}

	/**
	 * Sets Light's intensity.
	 */
	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}
}
