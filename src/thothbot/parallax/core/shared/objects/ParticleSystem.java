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
import thothbot.parallax.core.client.gl2.enums.BeginMode;
import thothbot.parallax.core.client.gl2.enums.BufferTarget;
import thothbot.parallax.core.client.gl2.enums.BufferUsage;
import thothbot.parallax.core.client.renderers.WebGLRenderInfo;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.shaders.Attribute;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.Color;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryBuffer;
import thothbot.parallax.core.shared.core.Matrix4;
import thothbot.parallax.core.shared.core.Vector2;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.core.Vector4;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.ParticleBasicMaterial;

/**
 * A class for displaying particles in the form of variable size points. 
 * <p>
 * For example, the particles are displayed using GL_POINTS.
 * 
 * @author thothbot
 *
 */
public class ParticleSystem extends GeometryObject
{
	public boolean sortParticles = false;
	// Specifies whether the particle system will be culled if it's outside the camera's frustum. By default this is set to false.
	public boolean frustumCulled = false;
	
	// camera matrices cache
	private Matrix4 projScreenMatrixPS;

	private static ParticleBasicMaterial defaultMaterial = new ParticleBasicMaterial();
	static {
		defaultMaterial.setColor( new Color((int)Math.random() * 0xffffff) );
	};
	
	public ParticleSystem(Geometry geometry) 
	{
		this(geometry, defaultMaterial);
	}
	
	public ParticleSystem(GeometryBuffer geometry, Material material) 
	{
		this(material);
		this.geometryBuffer = geometry;
	}

	public ParticleSystem(Geometry geometry, Material material) 
	{
		this(material);
		
		this.geometry = geometry;
	
		if ( this.geometry != null ) 
		{
			// calc bound radius
			if( this.geometry.getBoundingSphere() == null)
				this.geometry.computeBoundingSphere();

			this.boundRadius = geometry.getBoundingSphere().radius;
		}
	}
	
	protected ParticleSystem(Material material)
	{
		this.material = material;
		
		this.projScreenMatrixPS = new Matrix4();
	}
	
	public void renderBuffer(WebGLRenderer renderer, GeometryBuffer geometryBuffer, boolean updateBuffers)
	{
		WebGLRenderingContext gl = renderer.getGL();
		WebGLRenderInfo info = renderer.getInfo();
		
		gl.drawArrays( BeginMode.POINTS, 0, geometryBuffer.__webglParticleCount );

		info.getRender().calls ++;
		info.getRender().points += geometryBuffer.__webglParticleCount;
	}

	public void initBuffer(WebGLRenderer renderer)
	{
		Geometry geometry = this.getGeometry();

		if(geometryBuffer != null)
		{
			createBuffers(renderer, geometryBuffer );
		}
		else if ( geometry.__webglVertexBuffer == null ) 
		{
			createBuffers( renderer, geometry );
			initBuffers( renderer.getGL(), geometry );

			geometry.setVerticesNeedUpdate(true);
			geometry.setColorsNeedUpdate(true);
		}
	}
	
	private void createBuffers (  WebGLRenderer renderer, GeometryBuffer geometry ) 
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

		boolean areCustomAttributesDirty = material.getShader().areCustomAttributesDirty();
		if ( geometryBuffer != null ) 
		{
			if(this.geometryBuffer.isVerticesNeedUpdate() 
					|| this.geometryBuffer.isColorsNeedUpdate() )
			{
				((GeometryBuffer)geometryBuffer).setDirectBuffers( renderer.getGL(), BufferUsage.DYNAMIC_DRAW, !geometryBuffer.isDynamic() );
			}

			this.getGeometryBuffer().setVerticesNeedUpdate(false);
			this.getGeometryBuffer().setColorsNeedUpdate(false);
		}
		else
		{
			if ( this.geometry.isVerticesNeedUpdate() 
					|| this.geometry.isColorsNeedUpdate() 
					|| this.sortParticles 
					|| areCustomAttributesDirty
					) {
				this.setBuffers( renderer, this.geometry, BufferUsage.DYNAMIC_DRAW);
				this.material.getShader().clearCustomAttributes();
			}

			this.getGeometry().setVerticesNeedUpdate(false);
			this.getGeometry().setColorsNeedUpdate(false);
		}
	}

	// setParticleBuffers
	public void setBuffers (WebGLRenderer renderer, Geometry geometry, BufferUsage hint) 
	{
		WebGLRenderingContext gl = renderer.getGL();
		List<Vector3> vertices = geometry.getVertices();

		List<Color> colors = geometry.getColors();

		List<List<Integer>> sortArray = geometry.sortArray;

		boolean dirtyVertices = geometry.isVerticesNeedUpdate();
		boolean dirtyElements = geometry.isElementsNeedUpdate();
		boolean dirtyColors   = geometry.isColorsNeedUpdate();

		List<Attribute> customAttributes = geometry.__webglCustomAttributesList;
		
		if ( this.sortParticles ) 
		{
			this.projScreenMatrixPS.copy( renderer.getCache_projScreenMatrix() );
			this.projScreenMatrixPS.multiply( this.getMatrixWorld() );

			for ( int v = 0; v < vertices.size(); v ++ ) {
				Vector3 vertex = vertices.get( v );

				renderer.getCache_vector3().copy( vertex );
				this.projScreenMatrixPS.multiplyVector4( renderer.getCache_vector3() );
				 
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

				Color color = colors.get( sortArray.get(c).get(1) );

				geometry.getWebGlColorArray().set( offset, color.getR());
				geometry.getWebGlColorArray().set( offset + 1, color.getG());
				geometry.getWebGlColorArray().set( offset + 2, color.getB());
			}

			if ( customAttributes != null ) {

				for ( int i = 0; i < customAttributes.size(); i ++ ) 
				{
					Attribute customAttribute = customAttributes.get( i );

					if ( ! ( customAttribute.getBoundTo() == null 
							|| customAttribute.getBoundTo() == Attribute.BOUND_TO.VERTICES ) ) 
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
						if ( customAttribute.type == Attribute.TYPE.C ) 
						{
							for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
							{
								int index = sortArray.get( ca ).get( 1 );
								Color value = (Color) customAttribute.getValue().get(index);

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

					Color color = colors.get( c );

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
					Attribute customAttribute = customAttributes.get( i );

					int offset = 0;

					if ( customAttribute.needsUpdate &&
						 ( customAttribute.getBoundTo() == null ||
						   customAttribute.getBoundTo() == Attribute.BOUND_TO.VERTICES ) )
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

							if ( customAttribute.type == Attribute.TYPE.C) 
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
			gl.bindBuffer( BufferTarget.ARRAY_BUFFER, geometry.__webglVertexBuffer );
			gl.bufferData( BufferTarget.ARRAY_BUFFER, geometry.getWebGlVertexArray(), hint );
		}

		if ( dirtyColors || this.sortParticles ) 
		{
			gl.bindBuffer( BufferTarget.ARRAY_BUFFER, geometry.__webglColorBuffer );
			gl.bufferData( BufferTarget.ARRAY_BUFFER, geometry.getWebGlColorArray(), hint );
		}

		if ( customAttributes != null ) 
		{
			for ( int i = 0; i < customAttributes.size(); i ++ ) 
			{
				Attribute customAttribute = customAttributes.get( i );

				if ( customAttribute.needsUpdate || this.sortParticles ) 
				{
					gl.bindBuffer( BufferTarget.ARRAY_BUFFER, customAttribute.buffer );
					gl.bufferData( BufferTarget.ARRAY_BUFFER, customAttribute.array, hint );
				}
			}
		}
	}
}
