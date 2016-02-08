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

import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.renderers.RenderTargetTexture;
import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.extras.helpers.CameraHelper;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector2;

@ThreejsObject("THREE.LightShadow")
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
	public ShadowLight setTarget(Object3D target) {
		this.target = target;
		return this;
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
	public ShadowLight setIntensity(double intensity) {
		this.intensity = intensity;
		return this;
	}

	public boolean isOnlyShadow() {
		return onlyShadow;
	}

	/**
	 * If set to true light will only cast shadow but not contribute any lighting (as if intensity was 0 but cheaper to compute).
	 * <p>
	 * Default � false.
	 */
	public ShadowLight setOnlyShadow(boolean onlyShadow) {
		this.onlyShadow = onlyShadow;
		return this;
	}

	public boolean isShadowCascade() {
		return shadowCascade;
	}

	public ShadowLight setShadowCascade(boolean shadowCascade) {
		this.shadowCascade = shadowCascade;
		return this;
	}

	public int getShadowMapWidth() {
		return shadowMapWidth;
	}

	public ShadowLight setShadowMapWidth(int shadowMapWidth) {
		this.shadowMapWidth = shadowMapWidth;
		return this;
	}

	public int getShadowMapHeight() {
		return shadowMapHeight;
	}

	public ShadowLight setShadowMapHeight(int shadowMapHeight) {
		this.shadowMapHeight = shadowMapHeight;
		return this;
	}

	public Camera getShadowCamera() {
		return shadowCamera;
	}

	public ShadowLight setShadowCamera(Camera shadowCamera) {
		this.shadowCamera = shadowCamera;
		return this;
	}

	public double getShadowCameraNear() {
		return shadowCameraNear;
	}

	public ShadowLight setShadowCameraNear(double shadowCameraNear) {
		this.shadowCameraNear = shadowCameraNear;
		return this;
	}

	public double getShadowCameraFar() {
		return shadowCameraFar;
	}

	public ShadowLight setShadowCameraFar(double shadowCameraFar) {
		this.shadowCameraFar = shadowCameraFar;
		return this;
	}

	public boolean isShadowCameraVisible() {
		return shadowCameraVisible;
	}

	public ShadowLight setShadowCameraVisible(boolean shadowCameraVisible) {
		this.shadowCameraVisible = shadowCameraVisible;
		return this;
	}

	public double getShadowBias() {
		return shadowBias;
	}

	public ShadowLight setShadowBias(double shadowBias) {
		this.shadowBias = shadowBias;
		return this;
	}

	public double getShadowDarkness() {
		return shadowDarkness;
	}

	public ShadowLight setShadowDarkness(double shadowDarkness) {
		this.shadowDarkness = shadowDarkness;
		return this;
	}

	public boolean isAllocateShadows()
	{
		return isCastShadow() && !isShadowCascade();
	}

	public Matrix4 getShadowMatrix() {
		return shadowMatrix;
	}

	public ShadowLight setShadowMatrix(Matrix4 shadowMatrix) {
		this.shadowMatrix = shadowMatrix;
		return this;
	}

	public RenderTargetTexture getShadowMap() {
		return shadowMap;
	}

	public ShadowLight setShadowMap(RenderTargetTexture shadowMap) {
		this.shadowMap = shadowMap;
		return this;
	}

	public Vector2 getShadowMapSize() {
		return shadowMapSize;
	}

	public ShadowLight setShadowMapSize(Vector2 shadowMapSize) {
		this.shadowMapSize = shadowMapSize;
		return this;
	}

	public CameraHelper getCameraHelper() {
		return cameraHelper;
	}

	public ShadowLight setCameraHelper(CameraHelper cameraHelper) {
		this.cameraHelper = cameraHelper;
		return this;
	}
}
