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

package org.parallax3d.parallax.graphics.renderers;

import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.materials.Material;

import java.util.Comparator;

public class RenderItem implements Comparable<RenderItem>
{
	public static class PainterSortStable implements Comparator<RenderItem> {

		@Override
		public int compare(RenderItem a, RenderItem b) {
			if ( a.object.getRenderOrder() != b.object.getRenderOrder() ) {

				return a.object.getRenderOrder() - b.object.getRenderOrder();

			} else if ( a.material.getId() != b.material.getId() ) {

				return a.material.getId() - b.material.getId();

			} else if ( a.z != b.z ) {

				return (int) (a.z - b.z);

			} else {

				return a.id - b.id;

			}
		}
	}

	public static class ReversePainterSortStable implements Comparator<RenderItem> {

		@Override
		public int compare(RenderItem a, RenderItem b) {
			if ( a.object.getRenderOrder() != b.object.getRenderOrder() ) {

				return a.object.getRenderOrder() - b.object.getRenderOrder();

			} if ( a.z != b.z ) {

				return (int) (b.z - a.z);

			} else {

				return a.id - b.id;

			}
		}
	}

	public int id;
	public Object3D object;
	public AbstractGeometry geometry;
	public Object group;
	public Material material;

	public double z;

	public RenderItem(int id, Object3D object, AbstractGeometry geometry, Material material, double z, Object group)
	{
		this.id = id;
		this.object = object;
		this.geometry = geometry;
		this.material = material;
		this.z = z;
		this.group = group;
	}

	@Override
	public int compareTo(RenderItem o)
	{
		double result = o.z - this.z;
		return (result == 0) ? 0
				: (result > 0) ? 1 : -1;
	}
}
