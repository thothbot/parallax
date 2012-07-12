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

import java.util.List;

import thothbot.squirrel.core.client.gl2.WebGLRenderingContext;
import thothbot.squirrel.core.client.gl2.arrays.Float32Array;
import thothbot.squirrel.core.client.renderers.WebGLRenderInfo;
import thothbot.squirrel.core.client.renderers.WebGLRenderer;
import thothbot.squirrel.core.shared.core.Color3f;
import thothbot.squirrel.core.shared.core.Geometry;
import thothbot.squirrel.core.shared.core.GeometryBuffer;
import thothbot.squirrel.core.shared.core.Vector3f;
import thothbot.squirrel.core.shared.materials.Material;

public class Ribbon extends GeometryObject
{
	public Ribbon(Geometry geometry, Material material) 
	{
		this.geometry = geometry;
		this.material = material;
	}

	@Override
	public void setBuffer(WebGLRenderer renderer)
	{
		if ( this.getGeometry().verticesNeedUpdate || this.getGeometry().colorsNeedUpdate )
			this.setBuffers( renderer, geometry, WebGLRenderingContext.DYNAMIC_DRAW );

		this.getGeometry().verticesNeedUpdate = false;
		this.getGeometry().colorsNeedUpdate = false;
	}

	@Override
	public void renderBuffer(WebGLRenderer renderer, GeometryBuffer geometryBuffer, boolean updateBuffers)
	{
		WebGLRenderingContext gl = renderer.getGL();
		WebGLRenderInfo info = renderer.getInfo();
		
		gl.drawArrays( WebGLRenderingContext.TRIANGLE_STRIP, 0, geometryBuffer.__webglVertexCount );

		info.getRender().calls ++;
	}
	
	public void initBuffer(WebGLRenderer renderer)
	{
		Geometry geometry = this.getGeometry();

		if( geometry.__webglVertexBuffer == null ) 
		{
			createBuffers( renderer, geometry );
			initBuffers( renderer.getGL(), geometry );

			geometry.verticesNeedUpdate = true;
			geometry.colorsNeedUpdate = true;
		}
	}
	
	private void createBuffers(WebGLRenderer renderer, Geometry geometry)
	{
		WebGLRenderingContext gl = renderer.getGL();
		WebGLRenderInfo info = renderer.getInfo();
		
		geometry.__webglVertexBuffer =  gl.createBuffer();
		geometry.__webglColorBuffer =  gl.createBuffer();
		
		info.getMemory().geometries ++;
	}
	
	private void initBuffers(WebGLRenderingContext gl, Geometry geometry)
	{
		int nvertices = geometry.getVertices().size();

		geometry.__vertexArray = Float32Array.create( nvertices * 3 );
		geometry.__colorArray = Float32Array.create( nvertices * 3 );

		geometry.__webglVertexCount = nvertices;
	}

	// setRibbonBuffers
	public void setBuffers(WebGLRenderer renderer, Geometry geometry, int hint)
	{
		WebGLRenderingContext gl = renderer.getGL();
		
		List<Vector3f> vertices = geometry.getVertices();
		List<Color3f> colors = geometry.getColors();

		Float32Array vertexArray = geometry.__vertexArray;
		Float32Array colorArray = geometry.__colorArray;

		boolean dirtyVertices = geometry.verticesNeedUpdate;
		boolean dirtyColors = geometry.colorsNeedUpdate;

		if (dirtyVertices) 
		{
			for (int v = 0; v < vertices.size(); v++) 
			{
				Vector3f vertex = vertices.get(v);

				int offset = v * 3;

				vertexArray.set(offset, vertex.getX());
				vertexArray.set(offset + 1, vertex.getY());
				vertexArray.set(offset + 2, vertex.getZ());
			}

			gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, geometry.__webglVertexBuffer);
			gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, vertexArray, hint);
		}

		if (dirtyColors) 
		{
			for (int c = 0; c < colors.size(); c++) 
			{

				Color3f color = colors.get(c);

				int offset = c * 3;

				colorArray.set(offset, color.getR());
				colorArray.set(offset + 1, color.getG());
				colorArray.set(offset + 2, color.getB());

			}

			gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, geometry.__webglColorBuffer);
			gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, colorArray, hint);
		}
	}
}
