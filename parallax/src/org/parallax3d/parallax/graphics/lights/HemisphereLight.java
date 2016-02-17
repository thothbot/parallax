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

import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.renderers.RendererLights;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

@ThreejsObject("THREE.HemisphereLight")
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
			this.skyColors    = Float32Array.createArray();
			this.groundColors = Float32Array.createArray();
			this.positions    = Float32Array.createArray();

		}

		@Override
		public void refreshUniform(FastMap<Uniform> uniforms)
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

	public HemisphereLight setGroundColor(Color groundColor) {
		this.groundColor = groundColor;
		return this;
	}

	public double getIntensity() {
		return intensity;
	}

	public HemisphereLight setIntensity(double intensity) {
		this.intensity = intensity;
		return this;
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

		hemiPositions.set(hemiOffset, _direction.getX());
		hemiPositions.set(hemiOffset + 1, _direction.getY());
		hemiPositions.set(hemiOffset + 2, _direction.getZ());
	}
}
