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

package thothbot.squirrel.core.shared.objects;

import java.util.ArrayList;

import thothbot.squirrel.core.client.gl2.WebGLRenderingContext;
import thothbot.squirrel.core.client.gl2.arrays.Float32Array;
import thothbot.squirrel.core.client.renderers.WebGLRenderer;
import thothbot.squirrel.core.shared.Log;
import thothbot.squirrel.core.shared.core.Geometry;
import thothbot.squirrel.core.shared.core.GeometryBuffer;
import thothbot.squirrel.core.shared.core.WebGLCustomAttribute;
import thothbot.squirrel.core.shared.core.WebGLCustomAttribute.TYPE;
import thothbot.squirrel.core.shared.materials.Material;


public abstract class GeometryObject extends Object3D implements DimensionalObject
{
	protected Geometry geometry;
	protected Material material;

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
	
	public abstract void initBuffer(WebGLRenderer renderer);

	public abstract void setBuffer(WebGLRenderer renderer);
	
	public abstract void renderBuffer(WebGLRenderer renderer, GeometryBuffer geometryBuffer, boolean updateBuffers);
	
	protected void setLineWidth (WebGLRenderingContext gl, int width ) 
	{
		//if ( width != _oldLineWidth ) {
			gl.lineWidth( width );
//			this._oldLineWidth = width;
		//}
	}
	
	protected void initCustomAttributes (WebGLRenderingContext gl, Geometry geometry ) 
	{		
		int nvertices = geometry.getVertices().size();
		Material material = this.getMaterial();

		if ( material.attributes != null) 
		{
			if ( geometry.__webglCustomAttributesList == null ) 
				geometry.__webglCustomAttributesList = new ArrayList<WebGLCustomAttribute>();

			for ( String a : material.attributes.keySet() ) 
			{
				WebGLCustomAttribute attribute = material.attributes.get( a );
				if( ! attribute.__webglInitialized || attribute.createUniqueBuffers ) 
				{
					attribute.__webglInitialized = true;

					int size = 1;		// "f" and "i"

					if ( attribute.type == WebGLCustomAttribute.TYPE.V2 ) size = 2;
					else if ( attribute.type == WebGLCustomAttribute.TYPE.V3 ) size = 3;
					else if ( attribute.type == WebGLCustomAttribute.TYPE.V4 ) size = 4;
					else if ( attribute.type == WebGLCustomAttribute.TYPE.C  ) size = 3;

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
