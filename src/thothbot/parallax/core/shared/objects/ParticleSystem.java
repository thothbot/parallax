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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.renderers.WebGLRenderInfo;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.core.Color3;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryBuffer;
import thothbot.parallax.core.shared.core.Matrix4;
import thothbot.parallax.core.shared.core.Vector2;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.core.Vector4;
import thothbot.parallax.core.shared.core.WebGLCustomAttribute;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.ParticleBasicMaterial;

public class ParticleSystem extends GeometryObject
{
	public boolean sortParticles = false;
	public boolean frustumCulled = false;
	
	// camera matrices cache
	private Matrix4 projScreenMatrixPS;

	private static ParticleBasicMaterial defaultMaterial = new ParticleBasicMaterial();
	static {
		defaultMaterial.setColor( new Color3((int)Math.random() * 0xffffff) );
	};
	
	public ParticleSystem(Geometry geometry) 
	{
		this(geometry, defaultMaterial);
	}

	public ParticleSystem(Geometry geometry, Material material) 
	{
		this.geometry = geometry;
		this.material = material;
		
		this.projScreenMatrixPS = new Matrix4();

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
		
		gl.drawArrays( GLenum.POINTS.getValue(), 0, geometryBuffer.__webglParticleCount );

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

		geometry.setWebGlVertexArray( Float32Array.create( nvertices * 3 ) );
		geometry.setWebGlColorArray( Float32Array.create( nvertices * 3 ) );

		geometry.sortArray = new ArrayList<List<Integer>>();
		geometry.__webglParticleCount = nvertices;

		initCustomAttributes ( gl, geometry );
	}
	
	@Override
	public void setBuffer(WebGLRenderer renderer)
	{		
		this.material = Material.getBufferMaterial( this, null );

		boolean customAttributesDirty = ((this.material.getAttributes() != null) && this.material.areCustomAttributesDirty());

		if ( this.geometry.verticesNeedUpdate || this.geometry.colorsNeedUpdate || this.sortParticles || customAttributesDirty )
			this.setBuffers( renderer, this.geometry, GLenum.DYNAMIC_DRAW.getValue());

		this.getGeometry().verticesNeedUpdate = false;
		this.getGeometry().colorsNeedUpdate = false;

		this.material.clearCustomAttributes();
	}

	// setParticleBuffers
	public void setBuffers (WebGLRenderer renderer, Geometry geometry, int hint) 
	{
		WebGLRenderingContext gl = renderer.getGL();
		List<Vector3> vertices = geometry.getVertices();

		List<Color3> colors = geometry.getColors();

		List<List<Integer>> sortArray = geometry.sortArray;

		boolean dirtyVertices = geometry.verticesNeedUpdate;
		boolean dirtyElements = geometry.elementsNeedUpdate;
		boolean dirtyColors   = geometry.colorsNeedUpdate;

		List<WebGLCustomAttribute> customAttributes = geometry.__webglCustomAttributesList;
		
		if ( this.sortParticles ) 
		{
			this.projScreenMatrixPS.copy( renderer.getCache_projScreenMatrix() );
			this.projScreenMatrixPS.multiply( this.getMatrixWorld() );

			for ( int v = 0; v < vertices.size(); v ++ ) {
				Vector3 vertex = vertices.get( v );

				renderer.getCache_vector3().copy( vertex );
				this.projScreenMatrixPS.multiplyVector3( renderer.getCache_vector3() );
				 
				sortArray.add(v, new ArrayList<Integer>(Arrays.asList((int)renderer.getCache_vector3().getZ(), v)));
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
				Vector3 vertex = vertices.get(sortArray.get(v).get(1) );
				int offset = v * 3;

				geometry.getWebGlVertexArray().set( offset , vertex.getX());
				geometry.getWebGlVertexArray().set( offset + 1, vertex.getY());
				geometry.getWebGlVertexArray().set( offset + 2, vertex.getZ());

			}

			for ( int c = 0; c < colors.size(); c ++ ) 
			{
				int offset = c * 3;

				Color3 color = colors.get( sortArray.get(c).get(1) );

				geometry.getWebGlColorArray().set( offset, color.getR());
				geometry.getWebGlColorArray().set( offset + 1, color.getG());
				geometry.getWebGlColorArray().set( offset + 2, color.getB());
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
							customAttribute.array.set(ca, (Double) customAttribute.getValue().get(index));
						}
					} 
					else if ( customAttribute.size == 2 ) 
					{
						for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
						{
							int index = sortArray.get( ca ).get( 1 );
							Vector2 value = (Vector2) customAttribute.getValue().get(index);

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
								Color3 value = (Color3) customAttribute.getValue().get(index);

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
								Vector3 value = (Vector3) customAttribute.getValue().get(index);

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
							Vector4 value = (Vector4) customAttribute.getValue().get(index);

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
					Vector3 vertex = vertices.get( v );

					int offset = v * 3;

					geometry.getWebGlVertexArray().set( offset, vertex.getX());
					geometry.getWebGlVertexArray().set( offset + 1,  vertex.getY());
					geometry.getWebGlVertexArray().set( offset + 2, vertex.getZ());
				}
			}

			if ( dirtyColors ) 
			{
				for ( int c = 0; c < colors.size(); c ++ ) 
				{

					Color3 color = colors.get( c );

					int offset = c * 3;

					geometry.getWebGlColorArray().set(offset, color.getR());
					geometry.getWebGlColorArray().set(offset + 1, color.getG());
					geometry.getWebGlColorArray().set(offset + 2,  color.getB());
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
								customAttribute.array.set(ca, (Double) customAttribute.getValue().get(ca));

						} 
						else if ( customAttribute.size == 2 ) 
						{
							for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
							{

								Vector2 value = (Vector2) customAttribute.getValue().get(ca);

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

									Color3 value = (Color3) customAttribute.getValue().get(ca);

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
						else if ( customAttribute.size == 4 ) 
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
					}
				}
			}
		}

		if ( dirtyVertices || this.sortParticles ) 
		{
			gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometry.__webglVertexBuffer );
			gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), geometry.getWebGlVertexArray(), hint );
		}

		if ( dirtyColors || this.sortParticles ) 
		{
			gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometry.__webglColorBuffer );
			gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), geometry.getWebGlColorArray(), hint );
		}

		if ( customAttributes != null ) 
		{
			for ( int i = 0; i < customAttributes.size(); i ++ ) 
			{
				WebGLCustomAttribute customAttribute = customAttributes.get( i );

				if ( customAttribute.needsUpdate || this.sortParticles ) 
				{
					gl.bindBuffer( GLenum.ARRAY_BUFFER.getValue(), customAttribute.buffer );
					gl.bufferData( GLenum.ARRAY_BUFFER.getValue(), customAttribute.array, hint );
				}
			}
		}
	}
}
