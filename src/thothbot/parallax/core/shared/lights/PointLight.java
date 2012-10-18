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
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.materials.MeshLambertMaterial;
import thothbot.parallax.core.shared.materials.MeshPhongMaterial;

/**
 * Affects objects using {@link MeshLambertMaterial} or {@link MeshPhongMaterial}.
 * 
 * <pre>
 * {@code
 * PointLight light = new PointLight( 0xff0000, 1, 100 ); 
 * light.getPosition().set( 50, 50, 50 ); 
 * getScene().add( light );
 * }
 * </pre>
 * 
 * @author thothbot
 *
 */
public class PointLight extends Light
{
	public static class UniformPoint implements Light.UniformLight 
	{
		public Float32Array distances;
		public Float32Array colors;
		public Float32Array positions;
		
		@Override
		public void reset() 
		{
			this.distances = (Float32Array) Float32Array.createArray();
			this.colors    = (Float32Array) Float32Array.createArray();
			this.positions = (Float32Array) Float32Array.createArray();
			
		}

		@Override
		public void refreshUniform(Map<String, Uniform> uniforms) 
		{
			uniforms.get("pointLightColor").setValue( colors );
			uniforms.get("pointLightPosition").setValue( positions );
			uniforms.get("pointLightDistance").setValue( distances );
			
		}
	}

	private double intensity;
	private double distance;
	
	public PointLight(int hex) 
	{
		this(hex, 1, 0);
	}
	
	public PointLight(int hex, double intensity, double distance ) 
	{
		super(hex);
		this.intensity = intensity;
		this.distance = distance;
		this.position = new Vector3(0, 0, 0);
	}

	public double getIntensity() {
		return this.intensity;
	}

	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}
	
	@Override
	public void setupRendererLights(RendererLights zlights, boolean isGammaInput) 
	{
		Float32Array pointColors     = zlights.point.colors;
		Float32Array pointPositions  = zlights.point.positions;
		Float32Array pointDistances  = zlights.point.distances;
		
		double intensity = getIntensity();
		double distance = getDistance();
		int pointOffset = pointColors.getLength();

		if ( isGammaInput ) 
			setColorGamma( pointColors, pointOffset, getColor(), intensity ); 
		else 
			setColorLinear( pointColors, pointOffset, getColor(), intensity );

		Vector3 position = getMatrixWorld().getPosition();

		pointPositions.set(  pointOffset,     position.getX() );
		pointPositions.set(  pointOffset + 1, position.getY() );
		pointPositions.set(  pointOffset + 2, position.getZ() );

		pointDistances.set( pointOffset / 3, distance );
	}
}
