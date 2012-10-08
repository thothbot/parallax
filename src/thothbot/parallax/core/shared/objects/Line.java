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
import thothbot.parallax.core.client.gl2.enums.BeginMode;
import thothbot.parallax.core.client.gl2.enums.BufferTarget;
import thothbot.parallax.core.client.gl2.enums.BufferUsage;
import thothbot.parallax.core.client.renderers.WebGLRenderInfo;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.shaders.Attribute;
import thothbot.parallax.core.shared.core.Color;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryBuffer;
import thothbot.parallax.core.shared.core.Vector2;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.core.Vector4;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.materials.Material;

/**
 * A line or a series of lines.
 * 
 * @author thothbot
 *
 */
public class Line extends GeometryObject
{
	/**
	 * In OpenGL terms, LineStrip is the classic GL_LINE_STRIP and LinePieces is the equivalent to GL_LINES. 
	 */
	public static enum TYPE 
	{
		/**
		 * Will draw a series of segments connecting each point 
		 * (first connected to the second, the second connected 
		 * to the third, and so on and so forth)
		 */
		STRIPS, 
		/**
		 * Will draw a series of pairs of segments (first connected 
		 * to the second, the third connected to the fourth, 
		 * and so on and so forth).
		 */
		PIECES
	};

	private TYPE type;
	
	private static LineBasicMaterial defaultMaterial = new LineBasicMaterial();
	static {
		defaultMaterial.setColor( new Color((int)Math.random() * 0xffffff) );
	};

	public Line(Geometry geometry) 
	{
		this(geometry, Line.defaultMaterial, Line.TYPE.STRIPS);
	}

	public Line(Geometry geometry, LineBasicMaterial material) 
	{
		this(geometry, material, Line.TYPE.STRIPS);
	}

	public Line(Geometry geometry, LineBasicMaterial material, Line.TYPE type) 
	{
		super();
		this.geometry = geometry;
		this.material = material;
		this.type = type;

		if (this.geometry != null)
			if (this.geometry.getBoundingSphere() != null)
				this.geometry.computeBoundingSphere();
	}

	public void setType(Line.TYPE type)
	{
		this.type = type;
	}

	public Line.TYPE getType()
	{
		return type;
	}
	
	@Override
	public void renderBuffer(WebGLRenderer renderer, GeometryBuffer geometryBuffer, boolean updateBuffers)
	{
		WebGLRenderingContext gl = renderer.getGL();
		WebGLRenderInfo info = renderer.getInfo();
		
		BeginMode primitives = ( this.getType() == Line.TYPE.STRIPS) 
				? BeginMode.LINE_STRIP 
				: BeginMode.LINES;

		setLineWidth( gl, ((LineBasicMaterial)material).getLinewidth() );

		gl.drawArrays( primitives, 0, geometryBuffer.__webglLineCount );

		info.getRender().calls ++;
	}
	
	@Override
	public void initBuffer(WebGLRenderer renderer)
	{
		Geometry geometry = this.getGeometry();

		if( geometry.__webglVertexBuffer == null ) 
		{
			createBuffers(renderer, geometry );
			initBuffers(renderer.getGL(), geometry );

			geometry.setVerticesNeedUpdate(true);
			geometry.setColorsNeedUpdate(true);
		}
	}
	
	private void createBuffers ( WebGLRenderer renderer, Geometry geometry ) 
	{
		WebGLRenderingContext gl = renderer.getGL();
		WebGLRenderInfo info = renderer.getInfo();
		
		geometry.__webglVertexBuffer = gl.createBuffer();
		geometry.__webglColorBuffer = gl.createBuffer();

		info.getMemory().geometries ++;
	}

	private void initBuffers (WebGLRenderingContext gl, Geometry geometry) 
	{
		int nvertices = geometry.getVertices().size();

		geometry.setWebGlVertexArray( Float32Array.create( nvertices * 3 ) );
		geometry.setWebGlColorArray( Float32Array.create( nvertices * 3 ) );

		geometry.__webglLineCount = nvertices;

		initCustomAttributes ( gl, geometry );
	}

	@Override
	public void setBuffer(WebGLRenderer renderer)
	{
		WebGLRenderingContext gl = renderer.getGL();

		this.material = Material.getBufferMaterial( this, null );

		boolean areCustomAttributesDirty = material.getShader().areCustomAttributesDirty();
		if ( this.geometry.isVerticesNeedUpdate() 
				|| this.geometry.isColorsNeedUpdate() 
				|| areCustomAttributesDirty 
		) {
			this.setBuffers( gl, geometry, BufferUsage.DYNAMIC_DRAW );

			this.material.getShader().clearCustomAttributes();
		}

		this.geometry.setVerticesNeedUpdate(false);
		this.geometry.setColorsNeedUpdate(false);
	}

	// setLineBuffers
	public void setBuffers(WebGLRenderingContext gl, Geometry geometry, BufferUsage hint)
	{		
		List<Vector3> vertices = geometry.getVertices();
		List<Color> colors = geometry.getColors();

		boolean dirtyVertices = geometry.isVerticesNeedUpdate();
		boolean dirtyColors = geometry.isColorsNeedUpdate();

		List<Attribute> customAttributes = geometry.__webglCustomAttributesList;

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

			gl.bindBuffer(BufferTarget.ARRAY_BUFFER, geometry.__webglVertexBuffer);
			gl.bufferData(BufferTarget.ARRAY_BUFFER, geometry.getWebGlVertexArray(), hint);
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

			gl.bindBuffer(BufferTarget.ARRAY_BUFFER, geometry.__webglColorBuffer);
			gl.bufferData(BufferTarget.ARRAY_BUFFER, geometry.getWebGlColorArray(), hint);
		}

		if (customAttributes != null) 
		{
			for (int i = 0; i < customAttributes.size(); i++) 
			{
				Attribute customAttribute = customAttributes.get(i);

				if (customAttribute.needsUpdate
						&& (customAttribute.getBoundTo() == null 
						|| customAttribute.getBoundTo() == Attribute.BOUND_TO.VERTICES)) 
				{

					int offset = 0;

					if (customAttribute.size == 1) 
					{
						for (int ca = 0; ca < customAttribute.getValue().size(); ca++)
							customAttribute.array.set(ca, (Double)customAttribute.getValue().get(ca));

					}
					else if (customAttribute.size == 2) 
					{
						for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
						{

							Vector2 value = (Vector2) customAttribute.getValue().get(ca);

							customAttribute.array.set(offset, value.getX());
							customAttribute.array.set(offset + 1, value.getY());

							offset += 2;
						}

					} 
					else if (customAttribute.size == 3) 
					{
						if (customAttribute.type == Attribute.TYPE.C) 
						{
							for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
							{

								Color value = (Color) customAttribute.getValue().get(ca);

								customAttribute.array.set(offset, value.getR());
								customAttribute.array.set(offset + 1, value.getG());
								customAttribute.array.set(offset + 2, value.getB());

								offset += 3;
							}

						} 
						else 
						{
							for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
							{
								Vector3 value = (Vector3) customAttribute.getValue().get(ca);

								customAttribute.array.set(offset, value.getX());
								customAttribute.array.set(offset + 1, value.getY());
								customAttribute.array.set(offset + 2, value.getZ());

								offset += 3;
							}
						}

					} 
					else if (customAttribute.size == 4) 
					{
						for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
						{
							Vector4 value = (Vector4) customAttribute.getValue().get(ca);

							customAttribute.array.set(offset, value.getX());
							customAttribute.array.set(offset + 1, value.getY());
							customAttribute.array.set(offset + 2, value.getZ());
							customAttribute.array.set(offset + 3, value.getW());

							offset += 4;
						}
					}

					gl.bindBuffer(BufferTarget.ARRAY_BUFFER, customAttribute.buffer);
					gl.bufferData(BufferTarget.ARRAY_BUFFER, customAttribute.array, hint);
				}
			}
		}
	}
}
