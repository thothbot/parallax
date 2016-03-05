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
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.renderers.RendererLights;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

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
@ThreejsObject("THREE.SpotLight")
public class SpotLight extends ShadowLight
{
	public static class UniformSport implements Light.UniformLight
	{
		public Float32Array distances;
		public Float32Array colors;
		public Float32Array positions;

		public Float32Array directions;
		public Float32Array angles;
		public Float32Array exponents;

		@Override
		public void reset()
		{
			this.colors    =  Float32Array.createArray();
			this.distances =  Float32Array.createArray();
			this.positions =  Float32Array.createArray();

			this.directions =  Float32Array.createArray();
			this.angles     =  Float32Array.createArray();
			this.exponents  =  Float32Array.createArray();
		}

		@Override
		public void refreshUniform(FastMap<Uniform> uniforms)
		{
			uniforms.get("spotLightColor").setValue( colors );
			uniforms.get("spotLightPosition").setValue( positions );
			uniforms.get("spotLightDistance").setValue( distances );

			uniforms.get("spotLightDirection").setValue( directions );
			uniforms.get("spotLightAngleCos").setValue( angles );
			uniforms.get("spotLightExponent").setValue( exponents );
		}
	}

	private double distance;
	private double angle;
	private double exponent;

	private double shadowCameraFov = 50;

	public SpotLight(int hex)
	{
		this(hex, 1.0);
	}

	public SpotLight(int hex, double intensity)
	{
		this(hex, intensity, 0, Math.PI / 2.0, 10);
	}

	public SpotLight(int hex, double intensity, double distance, double angle, double exponent)
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
	public double getDistance() {
		return this.distance;
	}

	/**
	 * Sets the distance.
	 * <p>
	 * If non-zero, light will attenuate linearly from maximum intensity at light position down to zero at distance.
	 */
	public SpotLight setDistance(double distance) {
		this.distance = distance;
		return this;
	}

	public double getExponent() {
		return exponent;
	}

	public SpotLight setExponent(double exponent) {
		this.exponent = exponent;
		return this;
	}

	public double getAngle() {
		return angle;
	}

	public SpotLight setAngle(double angle) {
		this.angle = angle;
		return this;
	}

	public double getShadowCameraFov() {
		return shadowCameraFov;
	}

	public SpotLight setShadowCameraFov(double shadowCameraFov) {
		this.shadowCameraFov = shadowCameraFov;
		return this;
	}

	@Override
	public void setupRendererLights(RendererLights zlights, boolean isGammaInput)
	{
		Float32Array spotColors     = zlights.spot.colors;
		Float32Array spotPositions  = zlights.spot.positions;
		Float32Array spotDistances  = zlights.spot.distances;
		Float32Array spotDirections = zlights.spot.directions;
		Float32Array spotAngles     = zlights.spot.angles;
		Float32Array spotExponents  = zlights.spot.exponents;

		double intensity = getIntensity();
		double distance =  getDistance();

		int spotOffset = spotColors.getLength();

		if ( isGammaInput )
			setColorGamma( spotColors, spotOffset, getColor(), intensity );
		else
			setColorLinear( spotColors, spotOffset, getColor(), intensity );

		Vector3 position = new Vector3();
		position.setFromMatrixPosition( getMatrixWorld() );

		spotPositions.set(spotOffset, position.getX());
		spotPositions.set(spotOffset + 1, position.getY());
		spotPositions.set(spotOffset + 2, position.getZ());

		spotDistances.set(spotOffset / 3, distance);

		Vector3 vector3 = new Vector3();
		vector3.setFromMatrixPosition( getTarget().getMatrixWorld() );
		position.sub( vector3 );
		position.normalize();

		spotDirections.set(spotOffset, position.getX());
		spotDirections.set(spotOffset + 1, position.getY());
		spotDirections.set(spotOffset + 2, position.getZ());

		spotAngles.set(spotOffset / 3, Math.cos(getAngle()));
		spotExponents.set(spotOffset / 3, getExponent());
	}
}
