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

package org.parallax3d.parallax.graphics.core;

import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.objects.Points;

import java.util.List;

public abstract class GeometryObject extends Object3D
{
	protected AbstractGeometry geometry;
	protected Material material;

	public GeometryObject(AbstractGeometry geometry, Material material) {
		this.geometry = geometry;
		this.material = material;
	}

	public AbstractGeometry getGeometry()
	{
		return this.geometry;
	}

	public void setGeometry(AbstractGeometry geometry)
	{
		this.geometry = geometry;
	}

	public Material getMaterial()
	{
		return this.material;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}

	/**
	 * Abstract method to get intersections between a casted ray and this object.
	 * Subclasses such as {@link Mesh}, {@link Line}, and {@link Points} implement this method in order to participate in raycasting.
	 * @param raycaster
	 * @param intersects
	 */
	public abstract void raycast( Raycaster raycaster, List<Raycaster.Intersect> intersects);

}
