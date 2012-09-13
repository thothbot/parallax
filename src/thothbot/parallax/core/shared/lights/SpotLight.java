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

import thothbot.parallax.core.shared.core.Matrix4;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.objects.Object3D;

public class SpotLight extends Light
{
	private Object3D target;
	
	private double intensity;
	public double distance;
	
	private double angle;
	public double exponent;
	
	private boolean castShadow = false;
	
	private double shadowCameraNear = 50;
	private double shadowCameraFar = 5000;
	private double shadowCameraFov = 50;
	
	private boolean shadowCameraVisible = false;
	
	private double shadowBias = 0;
	private double shadowDarkness = 0.5;
	
	private int shadowMapWidth = 512;
	private int shadowMapHeight = 512;
	
	private Object shadowMap;
	private Object shadowMapSize;
	private Object shadowCamera;
	private Matrix4 shadowMatrix;
	
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
		this.setIntensity(intensity);
		this.distance = distance;
		this.setAngle(angle);
		this.exponent = exponent;
		
		this.position = new Vector3( 0, 1, 0 );
		this.setTarget(new Object3D());
	}
	
	public boolean isAllocateShadows() {
		return this.isCastShadow();
	}

	public Object3D getTarget() {
		return target;
	}

	public void setTarget(Object3D target) {
		this.target = target;
	}

	public double getIntensity() {
		return intensity;
	}

	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public boolean isCastShadow() {
		return castShadow;
	}

	public void setCastShadow(boolean castShadow) {
		this.castShadow = castShadow;
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

	public double getShadowCameraFov() {
		return shadowCameraFov;
	}

	public void setShadowCameraFov(double shadowCameraFov) {
		this.shadowCameraFov = shadowCameraFov;
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

	public Object getShadowMap() {
		return shadowMap;
	}

	public void setShadowMap(Object shadowMap) {
		this.shadowMap = shadowMap;
	}

	public Object getShadowMapSize() {
		return shadowMapSize;
	}

	public void setShadowMapSize(Object shadowMapSize) {
		this.shadowMapSize = shadowMapSize;
	}

	public Object getShadowCamera() {
		return shadowCamera;
	}

	public void setShadowCamera(Object shadowCamera) {
		this.shadowCamera = shadowCamera;
	}

	public Matrix4 getShadowMatrix() {
		return shadowMatrix;
	}

	public void setShadowMatrix(Matrix4 shadowMatrix) {
		this.shadowMatrix = shadowMatrix;
	}
	
	
}
