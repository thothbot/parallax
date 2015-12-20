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

import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.system.BufferUtils;
import org.parallax3d.parallax.system.ThreeJsObject;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.renderers.RendererLights;

@ThreeJsObject("THREE.HemisphereLight")
public final class HemisphereLight extends Light implements HasIntensity
{
	public static class UniformHemisphere implements UniformLight
	{
		public FloatBuffer skyColors;
		public FloatBuffer groundColors;
		public FloatBuffer positions;
		
		@Override
		public void reset() 
		{
			this.skyColors    = (FloatBuffer) BufferUtils.newFloatBuffer(3);
			this.groundColors = (FloatBuffer) BufferUtils.newFloatBuffer(3);
			this.positions = (FloatBuffer) BufferUtils.newFloatBuffer(3);
			
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
	private float intensity;
	
	public HemisphereLight(int skyColorHex, int groundColorHex)
	{
		this(skyColorHex, groundColorHex, 1.0f);
	}
	
	public HemisphereLight(int skyColorHex, int groundColorHex, float intensity)
	{
		super(skyColorHex);
		
		this.groundColor = new Color( groundColorHex );

		this.position = new Vector3( 0, 100.0f, 0 );

		this.intensity = intensity;
	}
	
	public Color getGroundColor() {
		return groundColor;
	}

	public void setGroundColor(Color groundColor) {
		this.groundColor = groundColor;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
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
		FloatBuffer hemiSkyColors    = zlights.hemi.skyColors;
		FloatBuffer hemiGroundColors = zlights.hemi.groundColors;
		FloatBuffer hemiPositions    = zlights.hemi.positions;
		
		Color skyColor = getColor();
		Color groundColor = getGroundColor();
		float intensity = getIntensity();

		int hemiOffset = hemiSkyColors.array().length * 3;

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

		hemiPositions.put( hemiOffset,     _direction.getX() );
		hemiPositions.put(hemiOffset + 1, _direction.getY());
		hemiPositions.put(hemiOffset + 2, _direction.getZ());
	}
}
