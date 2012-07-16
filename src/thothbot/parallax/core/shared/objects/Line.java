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
import thothbot.parallax.core.shared.core.Color3f;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryBuffer;
import thothbot.parallax.core.shared.core.Vector2f;
import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.core.Vector4f;
import thothbot.parallax.core.shared.core.WebGLCustomAttribute;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.materials.Material;

public class Line extends GeometryObject
{
	public static enum TYPE {
		STRIPS, PIECES,
	};

	private TYPE type;

	public Line(Geometry geometry, Material material) 
	{
		this(geometry, material, Line.TYPE.STRIPS);
	}

	public Line(Geometry geometry, Material material, Line.TYPE type) 
	{
		super();
		this.geometry = geometry;
		this.material = material;
		this.type = type;

		if (this.geometry != null)
			if (this.geometry.getBoundingSphere() != null)
				this.geometry.computeBoundingSphere();
	}

	private static LineBasicMaterial.LineBasicMaterialOptions defaultMaterialOptions = new LineBasicMaterial.LineBasicMaterialOptions();
	static {
		defaultMaterialOptions.color = new Color3f((int)Math.random() * 0xffffff);
	};

	public Line(Geometry geometry) 
	{
		this(geometry, new LineBasicMaterial(defaultMaterialOptions), Line.TYPE.STRIPS);
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
		
		int primitives = ( this.getType() == Line.TYPE.STRIPS) 
				? GLenum.LINE_STRIP.getValue() 
				: GLenum.LINES.getValue();

		setLineWidth( gl, material.linewidth );

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

			geometry.verticesNeedUpdate = true;
			geometry.colorsNeedUpdate = true;
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

		boolean customAttributesDirty = ((this.material.attributes != null) && this.material.areCustomAttributesDirty());

		if ( this.geometry.verticesNeedUpdate ||  this.geometry.colorsNeedUpdate || customAttributesDirty )
			this.setBuffers( gl, geometry, GLenum.DYNAMIC_DRAW.getValue() );

		this.geometry.verticesNeedUpdate = false;
		this.geometry.colorsNeedUpdate = false;

		this.material.clearCustomAttributes();
	}

	// setLineBuffers
	public void setBuffers(WebGLRenderingContext gl, Geometry geometry, int hint)
	{		
		List<Vector3f> vertices = geometry.getVertices();
		List<Color3f> colors = geometry.getColors();

		boolean dirtyVertices = geometry.verticesNeedUpdate;
		boolean dirtyColors = geometry.colorsNeedUpdate;

		List<WebGLCustomAttribute> customAttributes = geometry.__webglCustomAttributesList;

		if (dirtyVertices) 
		{
			for (int v = 0; v < vertices.size(); v++) 
			{
				Vector3f vertex = vertices.get(v);
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
				Color3f color = colors.get(c);
				int offset = c * 3;

				geometry.getWebGlColorArray().set(offset, color.getR());
				geometry.getWebGlColorArray().set(offset + 1, color.getG());
				geometry.getWebGlColorArray().set(offset + 2, color.getB());
			}

			gl.bindBuffer(GLenum.ARRAY_BUFFER.getValue(), geometry.__webglColorBuffer);
			gl.bufferData(GLenum.ARRAY_BUFFER.getValue(), geometry.getWebGlColorArray(), hint);
		}

		if (customAttributes != null) 
		{
			for (int i = 0; i < customAttributes.size(); i++) 
			{
				WebGLCustomAttribute customAttribute = customAttributes.get(i);

				if (customAttribute.needsUpdate
						&& (customAttribute.boundTo == null || customAttribute.boundTo
								.equals("vertices"))) {

					int offset = 0;

					if (customAttribute.size == 1) {
						for (int ca = 0; ca < customAttribute.getValue().size(); ca++)
							customAttribute.array.set(ca, (Float) customAttribute.getValue().get(ca));

					} else if (customAttribute.size == 2) {
						for (int ca = 0; ca < customAttribute.getValue().size(); ca++) {

							Vector2f value = (Vector2f) customAttribute.getValue().get(ca);

							customAttribute.array.set(offset, value.getX());
							customAttribute.array.set(offset + 1, value.getY());

							offset += 2;
						}

					} else if (customAttribute.size == 3) {

						if (customAttribute.type == WebGLCustomAttribute.TYPE.C) {

							for (int ca = 0; ca < customAttribute.getValue().size(); ca++) {

								Color3f value = (Color3f) customAttribute.getValue().get(ca);

								customAttribute.array.set(offset, value.getR());
								customAttribute.array.set(offset + 1, value.getG());
								customAttribute.array.set(offset + 2, value.getB());

								offset += 3;
							}

						} else {

							for (int ca = 0; ca < customAttribute.getValue().size(); ca++) {
								Vector3f value = (Vector3f) customAttribute.getValue().get(ca);

								customAttribute.array.set(offset, value.getX());
								customAttribute.array.set(offset + 1, value.getY());
								customAttribute.array.set(offset + 2, value.getZ());

								offset += 3;
							}
						}

					} else if (customAttribute.size == 4) {

						for (int ca = 0; ca < customAttribute.getValue().size(); ca++) {
							Vector4f value = (Vector4f) customAttribute.getValue().get(ca);

							customAttribute.array.set(offset, value.getX());
							customAttribute.array.set(offset + 1, value.getY());
							customAttribute.array.set(offset + 2, value.getZ());
							customAttribute.array.set(offset + 3, value.getW());

							offset += 4;
						}
					}

					gl.bindBuffer(GLenum.ARRAY_BUFFER.getValue(), customAttribute.buffer);
					gl.bufferData(GLenum.ARRAY_BUFFER.getValue(), customAttribute.array, hint);
				}
			}
		}
	}
}
