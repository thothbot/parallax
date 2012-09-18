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

import thothbot.parallax.core.shared.core.Vector3;

public class PointLight extends Light
{

	private Vector3 position;
	private double intensity;
	private double distance;
	
	public PointLight(int hex) 
	{
		this(hex, 1, 0);
	}
	
	public PointLight(int hex, double intensity, double distance ) 
	{
		super(hex);
		this.intensity = intensity;
		this.distance = distance;
		this.position = new Vector3(0, 0, 0);
	}
	
	public Vector3 getPosition() {
		return this.position;
	}
	
	public double getIntensity() {
		return this.intensity;
	}

	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}
}
