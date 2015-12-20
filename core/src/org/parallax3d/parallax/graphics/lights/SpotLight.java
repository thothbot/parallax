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
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.renderers.RendererLights;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;

/**
 * A point light that can cast shadow in one direction.
 * <p>
 * Affects objects using {@link MeshLambertMaterial} or {@link MeshPhongMaterial}.
 * 
 * <pre>
 * {@code
 * // white spotlight shining from the side, casting shadow 
 * 
 * SpotLight spotLight = new SpotLight( 0xffffff ); 
 * spotLight.getPosition().set( 100, 1000, 100 );  
 * spotLight.setCastShadow( true );  
 * spotLight.setShadowMapWidth( 1024 ); 
 * spotLight.setShadowMapHeight( 1024 );  
 * spotLight.setShadowCameraNear( 500 ); 
 * spotLight.setShadowCameraFar( 4000 ); 
 * spotLight.setShadowCameraFov( 30 );  
 * 
 * getScene().add( spotLight );
 * }
 * </pre>
 * 
 * @author thothbot
 *
 */
@ThreeJsObject("THREE.SpotLight")
public class SpotLight extends ShadowLight
{
	public static class UniformSport implements UniformLight
	{
		public FloatBuffer distances;
		public FloatBuffer colors;
		public FloatBuffer positions;
		
		public FloatBuffer directions;
		public FloatBuffer angles;
		public FloatBuffer exponents;
		
		@Override
		public void reset() 
		{
			this.colors    = BufferUtils.newFloatBuffer(3);
			this.distances = BufferUtils.newFloatBuffer(3);
			this.positions = BufferUtils.newFloatBuffer(3);
			
			this.directions = BufferUtils.newFloatBuffer(3);
			this.angles     = BufferUtils.newFloatBuffer(3);
			this.exponents  = BufferUtils.newFloatBuffer(3);
		}

		@Override
		public void refreshUniform(Map<String, Uniform> uniforms)
		{
			uniforms.get("spotLightColor").setValue( colors );
			uniforms.get("spotLightPosition").setValue( positions );
			uniforms.get("spotLightDistance").setValue( distances );

			uniforms.get("spotLightDirection").setValue( directions );
			uniforms.get("spotLightAngleCos").setValue( angles );
			uniforms.get("spotLightExponent").setValue( exponents );
		}
	}
	
	private float distance;
	private float angle;
	private float exponent;

	private float shadowCameraFov = 50;
	
	public SpotLight(int hex) 
	{
		this(hex, 1.0f);
	}

	public SpotLight(int hex, float intensity)
	{
		this(hex, intensity, 0, (float)Math.PI / 2.0f, 10);
	}

	public SpotLight(int hex, float intensity, float distance, float angle, float exponent) 
	{
		super(hex);
		this.exponent = exponent;
		this.angle = angle;
		
		setIntensity(intensity);
		this.distance = distance;
		this.angle = angle;
		this.exponent = exponent;
	}
	
	/**
	 * Gets the distance. Default � 0.0.
	 */
	public float getDistance() {
		return this.distance;
	}

	/**
	 * Sets the distance.
	 * <p>
	 * If non-zero, light will attenuate linearly from maximum intensity at light position down to zero at distance.
	 */
	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	public float getExponent() {
		return exponent;
	}

	public void setExponent(float exponent) {
		this.exponent = exponent;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getShadowCameraFov() {
		return shadowCameraFov;
	}

	public void setShadowCameraFov(float shadowCameraFov) {
		this.shadowCameraFov = shadowCameraFov;
	}
	
	@Override
	public void setupRendererLights(RendererLights zlights, boolean isGammaInput) 
	{
		FloatBuffer spotColors     = zlights.spot.colors;
		FloatBuffer spotPositions  = zlights.spot.positions;
		FloatBuffer spotDistances  = zlights.spot.distances;
		FloatBuffer spotDirections = zlights.spot.directions;
		FloatBuffer spotAngles     = zlights.spot.angles;
		FloatBuffer spotExponents  = zlights.spot.exponents;
		
		float intensity = getIntensity();
		float distance =  getDistance();

		int spotOffset = spotColors.arrayOffset();

		if ( isGammaInput ) 
			setColorGamma( spotColors, spotOffset, getColor(), intensity ); 
		else 
			setColorLinear( spotColors, spotOffset, getColor(), intensity );

		Vector3 position = new Vector3();
		position.setFromMatrixPosition( getMatrixWorld() );

		spotPositions.put(spotOffset,     position.getX());
		spotPositions.put(spotOffset + 1, position.getY());
		spotPositions.put(spotOffset + 2, position.getZ());

		spotDistances.put(spotOffset / 3, distance);

		Vector3 vector3 = new Vector3();
		vector3.setFromMatrixPosition( getTarget().getMatrixWorld() );
		position.sub( vector3 );
		position.normalize();

		spotDirections.put(spotOffset,    position.getX());
		spotDirections.put(spotOffset + 1, position.getY());
		spotDirections.put(spotOffset + 2, position.getZ());

		spotAngles.put(spotOffset / 3, (float)Math.cos( getAngle() ));
		spotExponents.put( spotOffset / 3, getExponent());
	}
}
