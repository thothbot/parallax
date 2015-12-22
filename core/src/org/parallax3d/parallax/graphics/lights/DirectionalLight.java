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
import java.util.List;
import java.util.Map;

import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.system.BufferUtils;
import org.parallax3d.parallax.system.ObjectMap;
import org.parallax3d.parallax.system.ThreeJsObject;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.renderers.RendererLights;

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
@ThreeJsObject("THREE.DirectionalLight")
public class DirectionalLight extends ShadowLight
{	
	public static class UniformDirectional implements UniformLight
	{
		public FloatBuffer colors;
		public FloatBuffer positions;

		@Override
		public void reset() 
		{
			this.colors    = BufferUtils.newFloatBuffer(3);
			this.positions = BufferUtils.newFloatBuffer(3);
			
		}

		@Override
		public void refreshUniform(ObjectMap<String, Uniform> uniforms)
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

	private float[] shadowCascadeBias = { 0.0f, 0.0f, 0.0f};
	private int[] shadowCascadeWidth = { 512, 512, 512 };
	private int[] shadowCascadeHeight = { 512, 512, 512 };

	private float[] shadowCascadeNearZ = { -1.000f, 0.990f, 0.998f };
	private float[] shadowCascadeFarZ = { 0.990f, 0.998f, 1.000f };

	private List<VirtualLight> shadowCascadeArray;

	public DirectionalLight(int hex) 
	{
		this(hex, 1.0f);
	}

	public DirectionalLight(int hex, float intensity)
	{		
		super(hex);

		setIntensity(intensity);

		this.shadowCascadeOffset = new Vector3(0, 0, -1000);
	}
		
	public Vector3 getShadowCascadeOffset() {
		return shadowCascadeOffset;
	}

	public void setShadowCascadeOffset(Vector3 shadowCascadeOffset) {
		this.shadowCascadeOffset = shadowCascadeOffset;
	}

	public int getShadowCascadeCount() {
		return shadowCascadeCount;
	}
	
	public void setShadowCascadeCount(int shadowCascadeCount) {
		this.shadowCascadeCount = shadowCascadeCount;
	}

	public float[] getShadowCascadeBias() {
		return shadowCascadeBias;
	}

	public void setShadowCascadeBias(float[] shadowCascadeBias) {
		this.shadowCascadeBias = shadowCascadeBias;
	}

	public int[] getShadowCascadeWidth() {
		return shadowCascadeWidth;
	}

	public void setShadowCascadeWidth(int[] shadowCascadeWidth) {
		this.shadowCascadeWidth = shadowCascadeWidth;
	}

	public int[] getShadowCascadeHeight() {
		return shadowCascadeHeight;
	}

	public void setShadowCascadeHeight(int[] shadowCascadeHeight) {
		this.shadowCascadeHeight = shadowCascadeHeight;
	}
	
	public float[] getShadowCascadeNearZ() {
		return shadowCascadeNearZ;
	}

	public void setShadowCascadeNearZ(float[] shadowCascadeNearZ) {
		this.shadowCascadeNearZ = shadowCascadeNearZ;
	}

	public float[] getShadowCascadeFarZ() {
		return shadowCascadeFarZ;
	}

	public void setShadowCascadeFarZ(float[] shadowCascadeFarZ) {
		this.shadowCascadeFarZ = shadowCascadeFarZ;
	}

	public List<VirtualLight> getShadowCascadeArray() {
		return shadowCascadeArray;
	}

	public void setShadowCascadeArray(List<VirtualLight> shadowCascadeArray) {
		this.shadowCascadeArray = shadowCascadeArray;
	}

	public int getShadowCameraLeft() {
		return shadowCameraLeft;
	}

	public void setShadowCameraLeft(int shadowCameraLeft) {
		this.shadowCameraLeft = shadowCameraLeft;
	}

	public int getShadowCameraRight() {
		return shadowCameraRight;
	}

	public void setShadowCameraRight(int shadowCameraRight) {
		this.shadowCameraRight = shadowCameraRight;
	}

	public int getShadowCameraTop() {
		return shadowCameraTop;
	}

	public void setShadowCameraTop(int shadowCameraTop) {
		this.shadowCameraTop = shadowCameraTop;
	}

	public int getShadowCameraBottom() {
		return shadowCameraBottom;
	}

	public void setShadowCameraBottom(int shadowCameraBottom) {
		this.shadowCameraBottom = shadowCameraBottom;
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
		FloatBuffer dirColors     = zlights.directional.colors;
		FloatBuffer dirPositions  = zlights.directional.positions;

		float intensity = getIntensity();

		int dirOffset = dirColors.array().length;

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

		dirPositions.put( dirOffset, position.getX());
		dirPositions.put(dirOffset + 1, position.getY());
		dirPositions.put(dirOffset + 2, position.getZ());
	}
}