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

import thothbot.parallax.core.shared.core.Color3;
import thothbot.parallax.core.shared.objects.Object3D;

public class Light extends Object3D
{
	private Color3 color;
	private boolean isOnlyShadow = false;
	
	public Light(int hex) 
	{
		super();
		this.color = new Color3(hex);
	}
	
	public void setColor(Color3 color) {
		this.color = color;
	}

	public Color3 getColor() {
		return color;
	}
	
	public boolean isOnlyShadow() {
		return this.isOnlyShadow;
	}
	
	// TODO check
	public boolean isAllocateShadows() {
		return false;
	}
}
