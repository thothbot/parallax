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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import thothbot.squirrel.core.client.gl2.WebGLRenderingContext;
import thothbot.squirrel.core.client.gl2.arrays.Float32Array;
import thothbot.squirrel.core.client.renderers.WebGLRenderInfo;
import thothbot.squirrel.core.client.renderers.WebGLRenderer;
import thothbot.squirrel.core.shared.core.Color3f;
import thothbot.squirrel.core.shared.core.Geometry;
import thothbot.squirrel.core.shared.core.GeometryBuffer;
import thothbot.squirrel.core.shared.core.Vector2f;
import thothbot.squirrel.core.shared.core.Vector3f;
import thothbot.squirrel.core.shared.core.Vector4f;
import thothbot.squirrel.core.shared.core.WebGLCustomAttribute;
import thothbot.squirrel.core.shared.materials.LineBasicMaterial;
import thothbot.squirrel.core.shared.materials.Material;

public class ParticleSystem extends GeometryObject
{
	public boolean sortParticles = false;
	public boolean frustumCulled = false;
	
	private static LineBasicMaterial.LineBasicMaterialOptions defaultMaterialOptions = new LineBasicMaterial.LineBasicMaterialOptions();
	static {
		defaultMaterialOptions.color = new Color3f((int)Math.random() * 0xffffff);
	};
	
	public ParticleSystem(Geometry geometry) 
	{
		this(geometry, new LineBasicMaterial(defaultMaterialOptions));
	}

	public ParticleSystem(Geometry geometry, Material material) 
	{
		this.geometry = geometry;
		this.material = material;

		if ( this.geometry != null ) 
		{
			// calc bound radius
			if( this.geometry.getBoundingSphere() == null)
				this.geometry.computeBoundingSphere();

			this.boundRadius = geometry.getBoundingSphere().radius;
		}
	}
	
	public void renderBuffer(WebGLRenderer renderer, GeometryBuffer geometryBuffer, boolean updateBuffers)
	{
		WebGLRenderingContext gl = renderer.getGL();
		WebGLRenderInfo info = renderer.getInfo();
		
		gl.drawArrays( WebGLRenderingContext.POINTS, 0, geometryBuffer.__webglParticleCount );

		info.getRender().calls ++;
		info.getRender().points += geometryBuffer.__webglParticleCount;
	}

	public void initBuffer(WebGLRenderer renderer)
	{
		Geometry geometry = this.getGeometry();

		if ( geometry.__webglVertexBuffer == null ) 
		{
			createBuffers( renderer, geometry );
			initBuffers( renderer.getGL(), geometry );

			geometry.verticesNeedUpdate = true;
			geometry.colorsNeedUpdate = true;
		}
	}
	
	private void createBuffers (  WebGLRenderer renderer, Geometry geometry ) 
	{
		WebGLRenderingContext gl = renderer.getGL();
		WebGLRenderInfo info = renderer.getInfo();
		
		geometry.__webglVertexBuffer = gl.createBuffer();
		geometry.__webglColorBuffer = gl.createBuffer();

		info.getMemory().geometries ++;
	}
	
	private void initBuffers ( WebGLRenderingContext gl, Geometry geometry ) 
	{
		int nvertices = geometry.getVertices().size();

		geometry.__vertexArray = Float32Array.create( nvertices * 3 );
		geometry.__colorArray = Float32Array.create( nvertices * 3 );

		geometry.__sortArray = new ArrayList<List<Integer>>();
		geometry.__webglParticleCount = nvertices;

		initCustomAttributes ( gl, geometry );
	}
	
	@Override
	public void setBuffer(WebGLRenderer renderer)
	{		
		this.material = Material.getBufferMaterial( this, null );

		boolean customAttributesDirty = ((this.material.attributes != null) && this.material.areCustomAttributesDirty());

		if ( this.geometry.verticesNeedUpdate || this.geometry.colorsNeedUpdate || this.sortParticles || customAttributesDirty )
			this.setBuffers( renderer, this.geometry, WebGLRenderingContext.DYNAMIC_DRAW);

		this.getGeometry().verticesNeedUpdate = false;
		this.getGeometry().colorsNeedUpdate = false;

		this.material.clearCustomAttributes();
	}

	// setParticleBuffers
	public void setBuffers (WebGLRenderer renderer, Geometry geometry, int hint) 
	{
		WebGLRenderingContext gl = renderer.getGL();
		List<Vector3f> vertices = geometry.getVertices();

		List<Color3f> colors = geometry.getColors();

		Float32Array vertexArray = geometry.__vertexArray;
		Float32Array colorArray  = geometry.__colorArray;

		List<List<Integer>> sortArray = geometry.__sortArray;

		boolean dirtyVertices = geometry.verticesNeedUpdate;
		boolean dirtyElements = geometry.elementsNeedUpdate;
		boolean dirtyColors   = geometry.colorsNeedUpdate;

		List<WebGLCustomAttribute> customAttributes = geometry.__webglCustomAttributesList;
		
		if ( this.sortParticles ) 
		{
			renderer._projScreenMatrixPS.copy( renderer._projScreenMatrix );
			renderer._projScreenMatrixPS.multiply( this.getMatrixWorld() );

			for ( int v = 0; v < vertices.size(); v ++ ) {
				Vector3f vertex = vertices.get( v );

				renderer._vector3.copy( vertex );
				renderer._projScreenMatrixPS.multiplyVector3( renderer._vector3 );
				 
				sortArray.add(v, new ArrayList<Integer>(Arrays.asList((int)renderer._vector3.getZ(), v)));
			}

			Collections.sort(sortArray, new Comparator<List<Integer>>() {

				@Override
				public int compare(List<Integer> o1, List<Integer> o2)
				{
					return o1.get(0).compareTo(o2.get(0));
				}
			});

			for ( int v = 0; v < geometry.getVertices().size(); v ++ ) 
			{
				Vector3f vertex = vertices.get(sortArray.get(v).get(1) );
				int offset = v * 3;

				vertexArray.set( offset , vertex.getX());
				vertexArray.set( offset + 1, vertex.getY());
				vertexArray.set( offset + 2, vertex.getZ());

			}

			for ( int c = 0; c < colors.size(); c ++ ) {
				int offset = c * 3;

				Color3f color = colors.get( sortArray.get(c).get(1) );

				colorArray.set( offset, color.getR());
				colorArray.set( offset + 1, color.getG());
				colorArray.set( offset + 2, color.getB());
			}

			if ( customAttributes != null ) {

				for ( int i = 0; i < customAttributes.size(); i ++ ) 
				{
					WebGLCustomAttribute customAttribute = customAttributes.get( i );

					if ( ! ( customAttribute.boundTo == null || customAttribute.boundTo.equals("vertices") ) ) 
						continue;

					int offset = 0;

					if ( customAttribute.size == 1 ) 
					{
						for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
						{
							int index = sortArray.get( ca ).get( 1 );
							customAttribute.array.set(ca, (Float) customAttribute.getValue().get(index));
						}
					} 
					else if ( customAttribute.size == 2 ) 
					{
						for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
						{
							int index = sortArray.get( ca ).get( 1 );
							Vector2f value = (Vector2f) customAttribute.getValue().get(index);

							customAttribute.array.set(offset, value.getX());
							customAttribute.array.set(offset + 1, value.getY());

							offset += 2;
						}
					} 
					else if ( customAttribute.size == 3 ) 
					{
						if ( customAttribute.type == WebGLCustomAttribute.TYPE.C ) 
						{
							for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
							{
								int index = sortArray.get( ca ).get( 1 );
								Color3f value = (Color3f) customAttribute.getValue().get(index);

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
								int index = sortArray.get( ca ).get( 1 );
								Vector3f value = (Vector3f) customAttribute.getValue().get(index);

								customAttribute.array.set(offset, value.getX());
								customAttribute.array.set(offset + 1, value.getY());
								customAttribute.array.set(offset + 2, value.getZ());

								offset += 3;
							}
						}
					} 
					else if ( customAttribute.size == 4 ) 
					{
						for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
						{
							int index = sortArray.get( ca ).get( 1 );
							Vector4f value = (Vector4f) customAttribute.getValue().get(index);

							customAttribute.array.set(offset, value.getX());
							customAttribute.array.set(offset + 1, value.getY());
							customAttribute.array.set(offset + 2, value.getZ());
							customAttribute.array.set(offset + 3, value.getW());

							offset += 4;
						}
					}
				}
			}

		} 
		else 
		{
			if ( dirtyVertices ) 
			{
				for ( int v = 0; v < vertices.size(); v ++ ) 
				{
					Vector3f vertex = vertices.get( v );

					int offset = v * 3;

					vertexArray.set( offset, vertex.getX());
					vertexArray.set( offset + 1,  vertex.getY());
					vertexArray.set( offset + 2, vertex.getZ());
				}
			}

			if ( dirtyColors ) 
			{
				for ( int c = 0; c < colors.size(); c ++ ) 
				{

					Color3f color = colors.get( c );

					int offset = c * 3;

					colorArray.set(offset, color.getR());
					colorArray.set(offset + 1, color.getG());
					colorArray.set(offset + 2,  color.getB());
				}
			}

			if ( customAttributes != null ) 
			{
				for ( int i = 0; i < customAttributes.size(); i ++ ) 
				{
					WebGLCustomAttribute customAttribute = customAttributes.get( i );

					int offset = 0;

					if ( customAttribute.needsUpdate &&
						 ( customAttribute.boundTo == null ||
						   customAttribute.boundTo.equals("vertices") ) )
					{

						if ( customAttribute.size == 1 ) 
						{
							for (int ca = 0; ca < customAttribute.getValue().size(); ca++)
								customAttribute.array.set(ca, (Float) customAttribute.getValue().get(ca));

						} 
						else if ( customAttribute.size == 2 ) 
						{
							for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
							{

								Vector2f value = (Vector2f) customAttribute.getValue().get(ca);

								customAttribute.array.set(offset, value.getX());
								customAttribute.array.set(offset + 1, value.getY());

								offset += 2;
							}

						} 
						else if ( customAttribute.size == 3 ) 
						{

							if ( customAttribute.type == WebGLCustomAttribute.TYPE.C) 
							{
								for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
								{

									Color3f value = (Color3f) customAttribute.getValue().get(ca);

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
									Vector3f value = (Vector3f) customAttribute.getValue().get(ca);

									customAttribute.array.set(offset, value.getX());
									customAttribute.array.set(offset + 1, value.getY());
									customAttribute.array.set(offset + 2, value.getZ());

									offset += 3;
								}
							}

						} 
						else if ( customAttribute.size == 4 ) 
						{
							for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
							{
								Vector4f value = (Vector4f) customAttribute.getValue().get(ca);

								customAttribute.array.set(offset, value.getX());
								customAttribute.array.set(offset + 1, value.getY());
								customAttribute.array.set(offset + 2, value.getZ());
								customAttribute.array.set(offset + 3, value.getW());

								offset += 4;
							}
						}
					}
				}
			}
		}

		if ( dirtyVertices || this.sortParticles ) 
		{
			gl.bindBuffer( WebGLRenderingContext.ARRAY_BUFFER, geometry.__webglVertexBuffer );
			gl.bufferData( WebGLRenderingContext.ARRAY_BUFFER, vertexArray, hint );
		}

		if ( dirtyColors || this.sortParticles ) 
		{
			gl.bindBuffer( WebGLRenderingContext.ARRAY_BUFFER, geometry.__webglColorBuffer );
			gl.bufferData( WebGLRenderingContext.ARRAY_BUFFER, colorArray, hint );
		}

		if ( customAttributes != null ) 
		{
			for ( int i = 0; i < customAttributes.size(); i ++ ) 
			{
				WebGLCustomAttribute customAttribute = customAttributes.get( i );

				if ( customAttribute.needsUpdate || this.sortParticles ) 
				{
					gl.bindBuffer( WebGLRenderingContext.ARRAY_BUFFER, customAttribute.buffer );
					gl.bufferData( WebGLRenderingContext.ARRAY_BUFFER, customAttribute.array, hint );
				}
			}
		}
	}
}
