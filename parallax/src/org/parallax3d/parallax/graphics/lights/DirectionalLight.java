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

import java.util.List;

import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.renderers.RendererLights;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

/**
 * Affects objects using {@link MeshLambertMaterial} or {@link MeshPhongMaterial}.
 * 
 * <pre>
 * {@code
 * // White directional light at half intensity shining from the top. 
 * 
 * DirectionalLight light = new DirectionalLight( 0xffffff, 0.5 ); 
 * light.position.set( 0, 1, 0 ); 
 * getScene().add( light );
 * }
 * </pre>
 * @author thothbot
 *
 */
@ThreejsObject("THREE.DirectionalLight")
public class DirectionalLight extends ShadowLight
{
	public static class UniformDirectional implements Light.UniformLight
	{
		public Float32Array colors;
		public Float32Array positions;

		@Override
		public void reset()
		{
			this.colors    = Float32Array.createArray();
			this.positions = Float32Array.createArray();

		}

		@Override
		public void refreshUniform(FastMap<Uniform> uniforms)
		{
			uniforms.get("directionalLightColor").setValue( colors );
			uniforms.get("directionalLightDirection").setValue( positions );
		}
	}

	//

	private int shadowCameraLeft = -500;
	private int shadowCameraRight = 500;
	private int shadowCameraTop = 500;
	private int shadowCameraBottom = -500;

	//

	private Vector3 shadowCascadeOffset;
	private int shadowCascadeCount = 2;

	private double[] shadowCascadeBias = { 0.0, 0.0, 0.0};
	private int[] shadowCascadeWidth = { 512, 512, 512 };
	private int[] shadowCascadeHeight = { 512, 512, 512 };

	private double[] shadowCascadeNearZ = { -1.000, 0.990, 0.998 };
	private double[] shadowCascadeFarZ = { 0.990, 0.998, 1.000 };

	private List<VirtualLight> shadowCascadeArray;

	public DirectionalLight(int hex)
	{
		this(hex, 1.0);
	}

	public DirectionalLight(int hex, double intensity)
	{
		super(hex);

		setIntensity(intensity);

		this.shadowCascadeOffset = new Vector3(0, 0, -1000);
	}

	public Vector3 getShadowCascadeOffset() {
		return shadowCascadeOffset;
	}

	public DirectionalLight setShadowCascadeOffset(Vector3 shadowCascadeOffset) {
		this.shadowCascadeOffset = shadowCascadeOffset;
		return this;
	}

	public int getShadowCascadeCount() {
		return shadowCascadeCount;
	}

	public DirectionalLight setShadowCascadeCount(int shadowCascadeCount) {
		this.shadowCascadeCount = shadowCascadeCount;
		return this;
	}

	public double[] getShadowCascadeBias() {
		return shadowCascadeBias;
	}

	public DirectionalLight setShadowCascadeBias(double[] shadowCascadeBias) {
		this.shadowCascadeBias = shadowCascadeBias;
		return this;
	}

	public int[] getShadowCascadeWidth() {
		return shadowCascadeWidth;
	}

	public DirectionalLight setShadowCascadeWidth(int[] shadowCascadeWidth) {
		this.shadowCascadeWidth = shadowCascadeWidth;
		return this;
	}

	public int[] getShadowCascadeHeight() {
		return shadowCascadeHeight;
	}

	public DirectionalLight setShadowCascadeHeight(int[] shadowCascadeHeight) {
		this.shadowCascadeHeight = shadowCascadeHeight;
		return this;
	}

	public double[] getShadowCascadeNearZ() {
		return shadowCascadeNearZ;
	}

	public DirectionalLight setShadowCascadeNearZ(double[] shadowCascadeNearZ) {
		this.shadowCascadeNearZ = shadowCascadeNearZ;
		return this;
	}

	public double[] getShadowCascadeFarZ() {
		return shadowCascadeFarZ;
	}

	public DirectionalLight setShadowCascadeFarZ(double[] shadowCascadeFarZ) {
		this.shadowCascadeFarZ = shadowCascadeFarZ;
		return this;
	}

	public List<VirtualLight> getShadowCascadeArray() {
		return shadowCascadeArray;
	}

	public DirectionalLight setShadowCascadeArray(List<VirtualLight> shadowCascadeArray) {
		this.shadowCascadeArray = shadowCascadeArray;
		return this;
	}

	public int getShadowCameraLeft() {
		return shadowCameraLeft;
	}

	public DirectionalLight setShadowCameraLeft(int shadowCameraLeft) {
		this.shadowCameraLeft = shadowCameraLeft;
		return this;
	}

	public int getShadowCameraRight() {
		return shadowCameraRight;
	}

	public DirectionalLight setShadowCameraRight(int shadowCameraRight) {
		this.shadowCameraRight = shadowCameraRight;
		return this;
	}

	public int getShadowCameraTop() {
		return shadowCameraTop;
	}

	public DirectionalLight setShadowCameraTop(int shadowCameraTop) {
		this.shadowCameraTop = shadowCameraTop;
		return this;
	}

	public int getShadowCameraBottom() {
		return shadowCameraBottom;
	}

	public DirectionalLight setShadowCameraBottom(int shadowCameraBottom) {
		this.shadowCameraBottom = shadowCameraBottom;
		return this;
	}

	public DirectionalLight clone() {

		DirectionalLight light = new DirectionalLight(0x000000);

		super.clone(light);

		light.setTarget(  this.getTarget().clone() );

		light.setIntensity( this.getIntensity() );

		light.setCastShadow( this.isCastShadow() );
		light.onlyShadow =  this.onlyShadow;

		//

		light.shadowCameraNear = this.shadowCameraNear;
		light.shadowCameraFar = this.shadowCameraFar;

		light.shadowCameraLeft = this.shadowCameraLeft;
		light.shadowCameraRight = this.shadowCameraRight;
		light.shadowCameraTop = this.shadowCameraTop;
		light.shadowCameraBottom = this.shadowCameraBottom;

		light.shadowCameraVisible = this.shadowCameraVisible;

		light.shadowBias = this.shadowBias;
		light.shadowDarkness = this.shadowDarkness;

		light.shadowMapWidth = this.shadowMapWidth;
		light.shadowMapHeight = this.shadowMapHeight;

		//

		light.shadowCascade = this.shadowCascade;

		light.shadowCascadeOffset.copy( this.shadowCascadeOffset );
		light.shadowCascadeCount = this.shadowCascadeCount;

		light.shadowCascadeBias = this.shadowCascadeBias;
		light.shadowCascadeWidth = this.shadowCascadeWidth;
		light.shadowCascadeHeight = this.shadowCascadeHeight;

		light.shadowCascadeNearZ = this.shadowCascadeNearZ;
		light.shadowCascadeFarZ  = this.shadowCascadeFarZ;

		return light;

	}

	@Override
	public void setupRendererLights(RendererLights zlights, boolean isGammaInput)
	{
		Float32Array dirColors     = zlights.directional.colors;
		Float32Array dirPositions  = zlights.directional.positions;

		double intensity = getIntensity();

		int dirOffset = dirColors.getLength();

		if ( isGammaInput )
			setColorGamma( dirColors, dirOffset, getColor(), intensity );
		else
			setColorLinear( dirColors, dirOffset, getColor(), intensity );

		Vector3 position = new Vector3();
		position.setFromMatrixPosition(  getMatrixWorld());
		Vector3 _vector3 = new Vector3();
		_vector3.setFromMatrixPosition( getTarget().getMatrixWorld() );
		position.sub( _vector3 );
		position.normalize();

		dirPositions.set(dirOffset, position.getX());
		dirPositions.set(dirOffset + 1, position.getY());
		dirPositions.set(dirOffset + 2, position.getZ());
	}
}
