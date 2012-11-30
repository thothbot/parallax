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

package thothbot.parallax.core.shared.objects;

import java.util.ArrayList;
import java.util.Map;

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.shaders.Attribute;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryBuffer;
import thothbot.parallax.core.shared.materials.Material;

public abstract class GeometryObject extends Object3D implements DimensionalObject
{
	protected Geometry geometry;
	protected GeometryBuffer geometryBuffer;
	protected Material material;
	protected Material customDepthMaterial;
	
	private double cache_oldLineWidth = -1;

	public GeometryBuffer getGeometryBuffer()
	{
		return this.geometryBuffer;
	}

	public Geometry getGeometry()
	{
		return this.geometry;
	}

	public void setGeometry(Geometry geometry)
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
	
	public Material getCustomDepthMaterial() {
		return customDepthMaterial;
	}

	public void setCustomDepthMaterial(Material customDepthMaterial) {
		this.customDepthMaterial = customDepthMaterial;
	}
	
	public abstract void initBuffer(WebGLRenderer renderer);

	public abstract void setBuffer(WebGLRenderer renderer);
	
	public abstract void renderBuffer(WebGLRenderer renderer, GeometryBuffer geometryBuffer, boolean updateBuffers);
	
	/**
	 * object — an instance of Object3D
	 * Removes an object from the GL context and releases all the data (geometry, matrices...) 
	 * that the GL context keeps about the object, but it doesn't release textures or affect any 
	 * JavaScript data.
	 */
	public void deallocate(WebGLRenderer renderer) 
	{
		if ( ! isWebglInit ) return;

		isWebglInit = false;

		_modelViewMatrix = null;
		_normalMatrix = null;

		_normalMatrixArray = null;
		_modelViewMatrixArray = null;
		_modelMatrixArray = null;

		deleteBuffers(renderer);
	}
	
	public void deleteBuffers(WebGLRenderer renderer) 
	{
		renderer.getGL().deleteBuffer( geometry.__webglVertexBuffer );
		renderer.getGL().deleteBuffer( geometry.__webglColorBuffer );

		renderer.getInfo().getMemory().geometries --;
	}

	protected void setLineWidth (WebGLRenderingContext gl, double width ) 
	{
		if ( width != this.cache_oldLineWidth ) 
		{
			gl.lineWidth( width );
			this.cache_oldLineWidth = width;
		}
	}
		
	protected void initCustomAttributes (WebGLRenderingContext gl, Geometry geometry ) 
	{		
		int nvertices = geometry.getVertices().size();
		Material material = this.getMaterial();

		Map<String, Attribute> attributes = material.getShader().getAttributes();
		
		if ( attributes != null) 
		{
			if ( geometry.__webglCustomAttributesList == null ) 
				geometry.__webglCustomAttributesList = new ArrayList<Attribute>();

			for ( String a : attributes.keySet() ) 
			{
				Attribute attribute = attributes.get( a );
				if( ! attribute.__webglInitialized || attribute.createUniqueBuffers ) 
				{
					attribute.__webglInitialized = true;

					int size = 1;		// "f" and "i"

					if ( attribute.type == Attribute.TYPE.V2 ) size = 2;
					else if ( attribute.type == Attribute.TYPE.V3 ) size = 3;
					else if ( attribute.type == Attribute.TYPE.V4 ) size = 4;
					else if ( attribute.type == Attribute.TYPE.C  ) size = 3;

					attribute.size = size;

					attribute.array = Float32Array.create( nvertices * size );

					attribute.buffer = gl.createBuffer();
					attribute.belongsToAttribute = a;

					attribute.needsUpdate = true;
				}

				geometry.__webglCustomAttributesList.add( attribute );
			}
		}
	}
}
