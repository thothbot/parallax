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

import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.helpers.CameraHelper;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;
public abstract class ShadowLight extends Light implements HasIntensity
{
	protected Object3D target;
	
	protected double intensity;

	protected boolean onlyShadow = false;
	
	private Camera shadowCamera;
	protected double shadowCameraNear = 50;
	protected double shadowCameraFar = 5000;
	protected boolean shadowCameraVisible = false;

	//
	
	protected double shadowBias = 0.0;
	protected double shadowDarkness = 0.5;

	protected int shadowMapWidth = 512;
	protected int shadowMapHeight = 512;

	protected boolean shadowCascade = false;
	
	private RenderTargetTexture shadowMap;
	private Vector2 shadowMapSize;
	private Matrix4 shadowMatrix;
			
	private CameraHelper cameraHelper;
	
	public ShadowLight(int hex) 
	{
		super(hex);
		
		this.position = new Vector3(0, 1.0, 0);
		this.target   = new Object3D();
	}

	/**
	 * Gets Target used for shadow camera orientation.
	 */
	public Object3D getTarget() {
		return this.target;
	}
	
	/**
	 * Sets Target used for shadow camera orientation.
	 */
	public void setTarget(Object3D target) {
		this.target = target;
	}
	

	/**
	 * Gets Light's intensity.
	 */
	public double getIntensity() {
		return this.intensity;
	}

	/**
	 * Sets Light's intensity.
	 */
	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}
	
	public boolean isOnlyShadow() {
		return onlyShadow;
	}

	/**
	 * If set to true light will only cast shadow but not contribute any lighting (as if intensity was 0 but cheaper to compute).
	 * <p>
	 * Default � false.
	 */
	public void setOnlyShadow(boolean onlyShadow) {
		this.onlyShadow = onlyShadow;
	}

	public boolean isShadowCascade() {
		return shadowCascade;
	}

	public void setShadowCascade(boolean shadowCascade) {
		this.shadowCascade = shadowCascade;
	}

	public int getShadowMapWidth() {
		return shadowMapWidth;
	}

	public void setShadowMapWidth(int shadowMapWidth) {
		this.shadowMapWidth = shadowMapWidth;
	}

	public int getShadowMapHeight() {
		return shadowMapHeight;
	}

	public void setShadowMapHeight(int shadowMapHeight) {
		this.shadowMapHeight = shadowMapHeight;
	}
	
	public Camera getShadowCamera() {
		return shadowCamera;
	}

	public void setShadowCamera(Camera shadowCamera) {
		this.shadowCamera = shadowCamera;
	}
	
	public double getShadowCameraNear() {
		return shadowCameraNear;
	}

	public void setShadowCameraNear(double shadowCameraNear) {
		this.shadowCameraNear = shadowCameraNear;
	}

	public double getShadowCameraFar() {
		return shadowCameraFar;
	}

	public void setShadowCameraFar(double shadowCameraFar) {
		this.shadowCameraFar = shadowCameraFar;
	}

	public boolean isShadowCameraVisible() {
		return shadowCameraVisible;
	}

	public void setShadowCameraVisible(boolean shadowCameraVisible) {
		this.shadowCameraVisible = shadowCameraVisible;
	}

	public double getShadowBias() {
		return shadowBias;
	}

	public void setShadowBias(double shadowBias) {
		this.shadowBias = shadowBias;
	}

	public double getShadowDarkness() {
		return shadowDarkness;
	}

	public void setShadowDarkness(double shadowDarkness) {
		this.shadowDarkness = shadowDarkness;
	}

	public boolean isAllocateShadows()
	{
		return isCastShadow() && !isShadowCascade();
	}
	
	public Matrix4 getShadowMatrix() {
		return shadowMatrix;
	}

	public void setShadowMatrix(Matrix4 shadowMatrix) {
		this.shadowMatrix = shadowMatrix;
	}
	
	public RenderTargetTexture getShadowMap() {
		return shadowMap;
	}

	public void setShadowMap(RenderTargetTexture shadowMap) {
		this.shadowMap = shadowMap;
	}

	public Vector2 getShadowMapSize() {
		return shadowMapSize;
	}

	public void setShadowMapSize(Vector2 shadowMapSize) {
		this.shadowMapSize = shadowMapSize;
	}
	
	public CameraHelper getCameraHelper() {
		return cameraHelper;
	}

	public void setCameraHelper(CameraHelper cameraHelper) {
		this.cameraHelper = cameraHelper;
	}
}
