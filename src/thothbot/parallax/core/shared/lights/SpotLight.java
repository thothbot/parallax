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
	public Object3D target;
	
	public Vector3 position;

	public double intensity;
	public double distance;
	
	public double angle;
	public double exponent;
	
	public boolean castShadow = false;
	
	public int shadowCameraNear = 50;
	public int shadowCameraFar = 5000;
	public int shadowCameraFov = 50;
	
	public boolean shadowCameraVisible = false;
	
	public double shadowBias = 0;
	public double shadowDarkness = 0.5;
	
	public int shadowMapWidth = 512;
	public int shadowMapHeight = 512;
	
	public Object shadowMap;
	public Object shadowMapSize;
	public Object shadowCamera;
	public Matrix4 shadowMatrix;
	
	public SpotLight(int hex) 
	{
		this(hex, 1, 0, Math.PI / 2.0, 10);
	}

	public SpotLight(int hex, double intensity, double distance, double angle, double exponent) 
	{
		super(hex);
		this.intensity = intensity;
		this.distance = distance;
		this.angle = angle;
		this.exponent = exponent;
		
		this.position = new Vector3( 0, 1, 0 );
		this.target = new Object3D();
	}
	
	public boolean isAllocateShadows() {
		return this.castShadow;
	}
}
