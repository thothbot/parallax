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
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.graphics.extras.helpers.HasRaytracingPhysicalAttenuation;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.BufferUtils;
import org.parallax3d.parallax.system.ThreeJsObject;

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
@ThreeJsObject("THREE.PointLight")
public class PointLight extends Light implements HasRaytracingPhysicalAttenuation, HasIntensity
{
	public static class UniformPoint implements UniformLight
	{
		public FloatBuffer distances;
		public FloatBuffer colors;
		public FloatBuffer positions;
		
		@Override
		public void reset() 
		{
			this.distances = BufferUtils.newFloatBuffer(3);
			this.colors    = BufferUtils.newFloatBuffer(3);
			this.positions = BufferUtils.newFloatBuffer(3);
			
		}

		@Override
		public void refreshUniform(Map<String, Uniform> uniforms)
		{
			uniforms.get("pointLightColor").setValue( colors );
			uniforms.get("pointLightPosition").setValue( positions );
			uniforms.get("pointLightDistance").setValue( distances );
			
		}
	}
	
	private float intensity;
	private float distance;
	
	private boolean isPhysicalAttenuation;
	
	public PointLight(int hex) 
	{
		this(hex, 1.0f, 0.0f);
	}
	
	public PointLight(int hex, float intensity) 
	{
		this(hex, intensity, 0.0f);
	}
	
	public PointLight(int hex, float intensity, float distance ) 
	{
		super(hex);
		this.intensity = intensity;
		this.distance = distance;
	}

	public float getIntensity() {
		return this.intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getDistance() {
		return distance;
	}

	@Override
	public boolean isPhysicalAttenuation() {
		return isPhysicalAttenuation;
	}

	@Override
	public void setPhysicalAttenuation(boolean isPhysicalAttenuation) {
		this.isPhysicalAttenuation = isPhysicalAttenuation;
	}
	
	public PointLight clone() {

		PointLight light = new PointLight(0x000000);
		
		super.clone(light);

		light.intensity = this.intensity;
		light.distance = this.distance;

		return light;

	}
	
	@Override
	public void setupRendererLights(RendererLights zlights, boolean isGammaInput)
	{
		FloatBuffer pointColors     = zlights.point.colors;
		FloatBuffer pointPositions  = zlights.point.positions;
		FloatBuffer pointDistances  = zlights.point.distances;
		
		float intensity = getIntensity();
		float distance = getDistance();
		int pointOffset = pointColors.arrayOffset();

		if ( isGammaInput ) 
			setColorGamma( pointColors, pointOffset, getColor(), intensity ); 
		else 
			setColorLinear( pointColors, pointOffset, getColor(), intensity );

		Vector3 position = new Vector3();
		position.setFromMatrixPosition( getMatrixWorld() );

		pointPositions.put(pointOffset, position.getX());
		pointPositions.put(pointOffset + 1, position.getY());
		pointPositions.put(pointOffset + 2, position.getZ());

		pointDistances.put( pointOffset / 3, distance );
	}

}
