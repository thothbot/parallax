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

import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.renderers.RendererLights;

public class AreaLight extends Light implements HasIntensity 
{

	public Vector3 normal;
	public Vector3 right;

	private float intensity;

	public float width = 1.0f;
	public float height = 1.0f;

	public float constantAttenuation = 1.5f;
	public float linearAttenuation = 0.5f;
	public float quadraticAttenuation = 0.1f;
	
	public AreaLight ( int color) {
		this(color, 1.0f);
	}
	
	public AreaLight ( int color, float intensity ) {

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
	public float getIntensity() {
		return this.intensity;
	}

	/**
	 * Sets Light's intensity.
	 */
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
}
