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

import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.core.GeometryGroup;
import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshFaceMaterial;
import org.parallax3d.parallax.graphics.core.BufferGeometry;

import java.util.Comparator;

@ThreejsObject("THREE.WebGLObjects")
public class GLObject implements Comparable<GLObject>
{
	public static class PainterSortStable implements Comparator<GLObject> {

		@Override
		public int compare(GLObject a, GLObject b) {
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

	public static class ReversePainterSortStable implements Comparator<GLObject> {

		@Override
		public int compare(GLObject a, GLObject b) {
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
	public GeometryObject object;
	public GLGeometry buffer;
	public boolean render;

	public Material material;

	public Material opaque;
	public Material transparent;
	// render depth
	public double z;

	public GLObject(GLGeometry buffer, GeometryObject object)
	{
		this(buffer, object, null, null);
	}

	public GLObject(GLGeometry buffer, GeometryObject object, Material opaque, Material transparent)
	{
		this.buffer = buffer;
		this.object = object;
		this.opaque = opaque;
		this.transparent = transparent;
	}

	public void unrollImmediateBufferMaterial() {
		Material material = object.getMaterial();

		if ( material.isTransparent() ) {

			this.transparent = material;
			this.opaque = null;

		} else {

			this.opaque = material;
			this.transparent = null;

		}
	}

	public void unrollBufferMaterial(GLRenderer renderer)
	{
		GeometryObject object = this.object;
		GLGeometry buffer = this.buffer;

		AbstractGeometry geometry = object.getGeometry();
		Material material = object.getMaterial();

		if ( material instanceof MeshFaceMaterial )
		{
			int materialIndex = geometry instanceof BufferGeometry ? 0 : ((GeometryGroup)buffer).getMaterialIndex();

			material = ((MeshFaceMaterial)material).getMaterials().get( materialIndex );

			this.material = material;

			if ( material.isTransparent() ) {

				renderer.transparentObjects.add( this );

			} else {

				renderer.opaqueObjects.add( this );

			}
		}
		else
		{

			this.material = material;

			if ( material != null)
			{
				if ( material.isTransparent() ) {

					renderer.transparentObjects.add( this );

				} else {

					renderer.opaqueObjects.add( this );

				}

			}
		}
	}

	@Override
	public int compareTo(GLObject o)
	{
		double result = o.z - this.z;
		return (result == 0) ? 0
				: (result > 0) ? 1 : -1;
	}

	public String toString() {
		return "{id: " + this.id
				+ ", material: " + (this.material != null ? this.material.getClass().getSimpleName() : "null")
				+ ", object: " + (this.object != null ? this.object.getClass().getSimpleName() : "null")
				+ ", render: " + this.render
				+ ", z: " + this.z + "}";
	}}
