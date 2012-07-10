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

package thothbot.squirrel.core.shared.scenes;

import thothbot.squirrel.core.shared.core.Color3f;

public final class Fog
{
	private Color3f color;
	private float near;
	private float far;

	public Fog(int hex) {
		this(hex, 1f, 1000f);
	}

	public Fog(int hex, float near, float far) {
		this.color = new Color3f(hex);
		this.near = near;
		this.far = far;
	}

	public void setColor(Color3f color)
	{
		this.color = color;
	}

	public Color3f getColor()
	{
		return color;
	}

	public void setNear(float near)
	{
		this.near = near;
	}

	public double getNear()
	{
		return near;
	}

	public void setFar(float far)
	{
		this.far = far;
	}

	public double getFar()
	{
		return far;
	}
}
