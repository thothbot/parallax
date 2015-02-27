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
import thothbot.parallax.core.shared.math.Vector3;

public final class HemisphereLight extends Light implements HasIntensity
{
	public static class UniformHemisphere implements Light.UniformLight
	{
		public Float32Array skyColors;
		public Float32Array groundColors;
		public Float32Array positions;
		
		@Override
		public void reset() 
		{
			this.skyColors    = (Float32Array) Float32Array.createArray();
			this.groundColors = (Float32Array) Float32Array.createArray();
			this.positions = (Float32Array) Float32Array.createArray();
			
		}

		@Override
		public void refreshUniform(Map<String, Uniform> uniforms) 
		{
			uniforms.get("hemisphereLightSkyColor").setValue( skyColors );
			uniforms.get("hemisphereLightGroundColor").setValue( groundColors );
			uniforms.get("hemisphereLightPosition").setValue( positions );
			
		}
	}
	
	private Color groundColor;
	private double intensity;
	
	public HemisphereLight(int skyColorHex, int groundColorHex)
	{
		this(skyColorHex, groundColorHex, 1.0);
	}
	
	public HemisphereLight(int skyColorHex, int groundColorHex, double intensity)
	{
		super(skyColorHex);
		
		this.groundColor = new Color( groundColorHex );

		this.position = new Vector3( 0, 100.0, 0 );

		this.intensity = intensity;
	}
	
	public Color getGroundColor() {
		return groundColor;
	}

	public void setGroundColor(Color groundColor) {
		this.groundColor = groundColor;
	}

	public double getIntensity() {
		return intensity;
	}

	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}
	
	public HemisphereLight clone() {

		HemisphereLight light = new HemisphereLight(0x000000, 0x000000);
		
		super.clone(light);

		light.groundColor.copy( this.groundColor );
		light.intensity = this.intensity;

		return light;

	}
	
	@Override
	public void setupRendererLights(RendererLights zlights, boolean isGammaInput) 
	{
		Float32Array hemiSkyColors    = zlights.hemi.skyColors;
		Float32Array hemiGroundColors = zlights.hemi.groundColors;
		Float32Array hemiPositions    = zlights.hemi.positions;
		
		Color skyColor = getColor();
		Color groundColor = getGroundColor();
		double intensity = getIntensity();

		int hemiOffset = hemiSkyColors.getLength() * 3;

		if (  isGammaInput ) 
		{
			setColorGamma( hemiSkyColors, hemiOffset, skyColor, intensity );
			setColorGamma( hemiGroundColors, hemiOffset, groundColor, intensity );
		} 
		else 
		{
			setColorLinear( hemiSkyColors, hemiOffset, skyColor, intensity );
			setColorLinear( hemiGroundColors, hemiOffset, groundColor, intensity );
		}
		
		Vector3 _direction = new Vector3();
		
		_direction.setFromMatrixPosition( getMatrixWorld() );
		_direction.normalize();

		hemiPositions.set( hemiOffset,     _direction.getX() );
		hemiPositions.set( hemiOffset + 1, _direction.getY() );
		hemiPositions.set( hemiOffset + 2, _direction.getZ() );
	}
}
