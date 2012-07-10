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

import thothbot.squirrel.core.shared.core.Vector3f;

public class PointLight extends Light
{

	public Vector3f position;
	private int intensity;
	private int distance;
	
	public PointLight(int hex) {
		this(hex, 1, 0);
	}
	
	public PointLight(int hex, int intensity, int distance ) {
		super(hex);
		this.intensity = intensity;
		this.distance = distance;
		this.position = new Vector3f(0f, 0f, 0f);
	}
	
	public int getIntensity(){
		return this.intensity;
	}
	public void setIntensity(int intensity){
		this.intensity = intensity;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}
}
