/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.lights;

import thothbot.squirrel.core.shared.core.Object3D;
import thothbot.squirrel.core.shared.core.Vector3f;

public class DirectionalLight extends Light
{
	public float intensity;
	public float distance;

	private boolean castShadow = false;

	//

	public int shadowCameraNear = 50;
	public int shadowCameraFar = 5000;

	public int shadowCameraLeft = -500;
	public int shadowCameraRight = 500;
	public int shadowCameraTop = 500;
	public int shadowCameraBottom = -500;

	public boolean shadowCameraVisible = false;

	public float shadowBias = 0f;
	public float shadowDarkness = 0.5f;

	public int shadowMapWidth = 512;
	public int shadowMapHeight = 512;

	//

	public boolean shadowCascade = false;

	public Vector3f shadowCascadeOffset;
	public int shadowCascadeCount = 2;

	public float[] shadowCascadeBias = { 0f, 0f, 0f};
	public int[] shadowCascadeWidth = { 512, 512, 512 };
	public int[] shadowCascadeHeight = { 512, 512, 512 };

	public float[] shadowCascadeNearZ = { -1.000f, 0.990f, 0.998f };
	public float[] shadowCascadeFarZ = { 0.990f, 0.998f, 1.000f };

	public Object[] shadowCascadeArray;

	//

	public Object shadowMap;
	public Object shadowMapSize;
	public Object shadowCamera;
	public Object shadowMatrix;

	public DirectionalLight(int hex) {
		this(hex, 1f, 0f);
	}

	public DirectionalLight(int hex, float intensity, float distance) 
	{
		super(hex);

		this.intensity = intensity;
		this.distance = distance;

		this.position = new Vector3f(0f, 1f, 0f);
		this.target = new Object3D();
		this.shadowCascadeOffset = new Vector3f(0f, 0f, -1000f);

		this.onlyShadow = false;
	}

	public void setCastShadow(Boolean castShadow)
	{
		this.castShadow = castShadow;
	}

	public Boolean getCastShadow()
	{
		return castShadow;
	}

	public boolean isAllocateShadows()
	{
		return this.castShadow && !this.shadowCascade;
	}
}
