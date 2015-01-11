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

package thothbot.parallax.core.shared.core;

import java.util.List;

import thothbot.parallax.core.client.gl2.WebGLBuffer;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.shaders.Attribute;
import thothbot.parallax.core.shared.math.Box3;
import thothbot.parallax.core.shared.math.Sphere;

public abstract class AbstractGeometry 
{
	public static int Counter = 0;
	
	private int id = 0;
	
	private String name;
	
	// Bounding box.		
	protected Box3 boundingBox = null;

	// Bounding sphere.
	protected Sphere boundingSphere = null;
	
	public Float32Array __colorArray;
	public Float32Array __vertexArray;
	public Float32Array __normalArray;
	public Float32Array __tangentArray;
	public Float32Array __uvArray;
	public Float32Array __uv2Array;
	
	public Float32Array __lineDistanceArray;
	
	public WebGLBuffer __webglColorBuffer;
	public WebGLBuffer __webglVertexBuffer;
	public WebGLBuffer __webglNormalBuffer;
	public WebGLBuffer __webglTangentBuffer;
	public WebGLBuffer __webglUVBuffer;
	public WebGLBuffer __webglUV2Buffer;
	
	public WebGLBuffer __webglLineDistanceBuffer;
	
	public WebGLBuffer __webglSkinIndicesBuffer;
	public WebGLBuffer __webglSkinWeightsBuffer;
		
	public WebGLBuffer __webglFaceBuffer;
	public WebGLBuffer __webglLineBuffer;
	
	public int numMorphTargets;
	public List<WebGLBuffer> __webglMorphTargetsBuffers;
	
	public int numMorphNormals;
	public List<WebGLBuffer> __webglMorphNormalsBuffers;
	
	public List<Attribute> __webglCustomAttributesList;
	
	public int __webglParticleCount;
	public int __webglLineCount;
	public int __webglVertexCount;
	public int __webglFaceCount;
		
	public AbstractGeometry() {
		this.id = BufferGeometry.Counter++;
		
		this.name = "";
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	/**
	 * Gets the Unique number of this geometry instance
	 */
	public int getId() 
	{
		return id;
	}
	
	public Box3 getBoundingBox() {
		return this.boundingBox;
	}
	
	public void setBoundingBox(Box3 boundingBox) {
		this.boundingBox = boundingBox;
	}
	
	public Sphere getBoundingSphere() {
		return this.boundingSphere;
	}
	
	public void setBoundingSphere(Sphere boundingSphere) {
		this.boundingSphere = boundingSphere;
	}

	public abstract void computeBoundingBox();
	
	public abstract void computeBoundingSphere();
	
	public abstract void computeVertexNormals();
	
	public abstract void computeTangents();
}
