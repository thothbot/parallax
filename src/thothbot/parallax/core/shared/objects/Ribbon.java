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

import java.util.List;

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.renderers.WebGLRenderInfo;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.core.Color;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryBuffer;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.materials.Material;

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
		if ( this.getGeometry().isVerticesNeedUpdate || this.getGeometry().isColorsNeedUpdate )
			this.setBuffers( renderer, geometry, GLenum.DYNAMIC_DRAW.getValue() );

		this.getGeometry().isVerticesNeedUpdate = false;
		this.getGeometry().isColorsNeedUpdate = false;
	}

	@Override
	public void renderBuffer(WebGLRenderer renderer, GeometryBuffer geometryBuffer, boolean updateBuffers)
	{
		WebGLRenderingContext gl = renderer.getGL();
		WebGLRenderInfo info = renderer.getInfo();
		
		gl.drawArrays( GLenum.TRIANGLE_STRIP.getValue(), 0, geometryBuffer.__webglVertexCount );

		info.getRender().calls ++;
	}
	
	public void initBuffer(WebGLRenderer renderer)
	{
		Geometry geometry = this.getGeometry();

		if( geometry.__webglVertexBuffer == null ) 
		{
			createBuffers( renderer, geometry );
			initBuffers( renderer.getGL(), geometry );

			geometry.isVerticesNeedUpdate = true;
			geometry.isColorsNeedUpdate = true;
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

		geometry.setWebGlVertexArray( Float32Array.create( nvertices * 3 ) );
		geometry.setWebGlVertexArray( Float32Array.create( nvertices * 3 ) );

		geometry.__webglVertexCount = nvertices;
	}

	// setRibbonBuffers
	public void setBuffers(WebGLRenderer renderer, Geometry geometry, int hint)
	{
		WebGLRenderingContext gl = renderer.getGL();
		
		List<Vector3> vertices = geometry.getVertices();
		List<Color> colors = geometry.getColors();

		boolean dirtyVertices = geometry.isVerticesNeedUpdate;
		boolean dirtyColors = geometry.isColorsNeedUpdate;

		if (dirtyVertices) 
		{
			for (int v = 0; v < vertices.size(); v++) 
			{
				Vector3 vertex = vertices.get(v);

				int offset = v * 3;

				geometry.getWebGlVertexArray().set(offset, vertex.getX());
				geometry.getWebGlVertexArray().set(offset + 1, vertex.getY());
				geometry.getWebGlVertexArray().set(offset + 2, vertex.getZ());
			}

			gl.bindBuffer(GLenum.ARRAY_BUFFER.getValue(), geometry.__webglVertexBuffer);
			gl.bufferData(GLenum.ARRAY_BUFFER.getValue(), geometry.getWebGlVertexArray(), hint);
		}

		if (dirtyColors) 
		{
			for (int c = 0; c < colors.size(); c++) 
			{

				Color color = colors.get(c);

				int offset = c * 3;

				geometry.getWebGlColorArray().set(offset, color.getR());
				geometry.getWebGlColorArray().set(offset + 1, color.getG());
				geometry.getWebGlColorArray().set(offset + 2, color.getB());

			}

			gl.bindBuffer(GLenum.ARRAY_BUFFER.getValue(), geometry.__webglColorBuffer);
			gl.bufferData(GLenum.ARRAY_BUFFER.getValue(), geometry.getWebGlColorArray(), hint);
		}
	}
}
