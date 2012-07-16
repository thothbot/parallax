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

import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.objects.Object3D;

public class SpotLight extends Light
{
	public Vector3f position;
	public Object3D target;

	public float intensity;
	public float distance;
	
	public float angle;
	public float exponent;
	
	public boolean castShadow = false;
	
	public int shadowCameraNear = 50;
	public int shadowCameraFar = 5000;
	public int shadowCameraFov = 50;
	
	public boolean shadowCameraVisible = false;
	
	public float shadowBias = 0f;
	public float shadowDarkness = 0.5f;
	
	public int shadowMapWidth = 512;
	public int shadowMapHeight = 512;
	
	public Object shadowMap;
	public Object shadowMapSize;
	public Object shadowCamera;
	public Object shadowMatrix;
	
	public SpotLight(int hex) {
		this(hex, 1f, 0f, (float) (Math.PI / 2), 10f);
	}

	public SpotLight(int hex, float intensity, float distance, float angle, float exponent) {
		super(hex);
		this.intensity = intensity;
		this.distance = distance;
		this.angle = angle;
		this.exponent = exponent;
		
		this.position = new Vector3f( 0f, 1f, 0f );
		this.target = new Object3D();

		this.onlyShadow = false;
	}
	
	public boolean isAllocateShadows() {
		return this.castShadow;
	}
}
