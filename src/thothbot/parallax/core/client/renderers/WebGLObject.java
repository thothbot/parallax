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

package thothbot.parallax.core.client.renderers;

import thothbot.parallax.core.shared.core.AbstractGeometry;
import thothbot.parallax.core.shared.core.BufferGeometry;
import thothbot.parallax.core.shared.core.GeometryGroup;
import thothbot.parallax.core.shared.core.GeometryObject;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.MeshFaceMaterial;

public class WebGLObject implements Comparable<WebGLObject>
{
	public int id;
	public GeometryObject object;
	public WebGLGeometry buffer;
	public boolean render;
	
	public Material material;
	
	public Material opaque;
	public Material transparent;
	// render depth
	public double z;

	public WebGLObject(WebGLGeometry buffer, GeometryObject object)
	{
		this(buffer, object, null, null);
	}
	
	public WebGLObject(WebGLGeometry buffer, GeometryObject object, Material opaque, Material transparent) 
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
	
	public void unrollBufferMaterial(WebGLRenderer renderer) 
	{
		GeometryObject object = this.object;
		WebGLGeometry buffer = this.buffer;

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
	public int compareTo(WebGLObject o)
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
	}
}
