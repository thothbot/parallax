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

package thothbot.parallax.core.shared.objects;

import thothbot.parallax.core.shared.core.GeometryBuffer;
import thothbot.parallax.core.shared.materials.Material;

public class RendererObject implements Comparable<RendererObject>
{
	public GeometryObject object;
	public GeometryBuffer buffer;
	public boolean render;
	
	public Material opaque;
	public Material transparent;
	// render depth
	public double z;

	public RendererObject(GeometryBuffer buffer, GeometryObject object, Material opaque, Material transparent) 
	{
		this.buffer = buffer;
		this.object = object;
		this.opaque = opaque;
		this.transparent = transparent;
	}
	
	@Override
	public int compareTo(RendererObject o)
	{
		double result = o.z - this.z; 
		return (result == 0) ? 0 
				: (result > 0) ? 1 : -1;
	}
}
