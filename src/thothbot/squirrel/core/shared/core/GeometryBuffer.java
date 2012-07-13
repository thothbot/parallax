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

package thothbot.squirrel.core.shared.core;

import java.util.List;

import thothbot.squirrel.core.client.gl2.WebGLBuffer;
import thothbot.squirrel.core.client.gl2.arrays.Float32Array;


public abstract class GeometryBuffer
{
	public static int Counter = 0;
	private int id = 0;

	private boolean isArrayInitialized;
	
	private Float32Array webGlColorArray;
	private Float32Array webGlVertexArray;
	
	public WebGLBuffer __webglVertexBuffer;
	public WebGLBuffer __webglNormalBuffer;
	public WebGLBuffer __webglTangentBuffer;
	public WebGLBuffer __webglColorBuffer;
	public WebGLBuffer __webglUVBuffer;
	public WebGLBuffer __webglUV2Buffer;
	
	public WebGLBuffer __webglSkinVertexABuffer;
	public WebGLBuffer __webglSkinVertexBBuffer;
	public WebGLBuffer __webglSkinIndicesBuffer;
	public WebGLBuffer __webglSkinWeightsBuffer;
	
	public WebGLBuffer __webglFaceBuffer;
	public WebGLBuffer __webglLineBuffer;
	
	public int numMorphTargets;
	public List<WebGLBuffer> __webglMorphTargetsBuffers;
	
	public int numMorphNormals;
	public List<WebGLBuffer> __webglMorphNormalsBuffers;
	
	public List<WebGLCustomAttribute> __webglCustomAttributesList;
	
	public int __webglParticleCount;
	public int __webglLineCount;
	public int __webglVertexCount;
	public int __webglFaceCount;
	
	public GeometryBuffer() 
	{
		this.id = GeometryBuffer.Counter++;
	}
		
	public void setId(int id) 
	{
		this.id = id;
	}

	public int getId() 
	{
		return id;
	}
	
	/**
	 * Check if WebGl arrays initialized
	 */
	public boolean isArrayInitialized() 
	{
		return isArrayInitialized;
	}

	public void setArrayInitialized(boolean isArrayInitialized) 
	{
		this.isArrayInitialized = isArrayInitialized;
	}

	public Float32Array getWebGlColorArray()
	{
		return this.webGlColorArray;
	}
	
	public Float32Array getWebGlVertexArray()
	{
		return this.webGlVertexArray;
	}

	public void setWebGlColorArray(Float32Array a)
	{
		this.webGlColorArray = a;
	}
	
	public void setWebGlVertexArray(Float32Array a)
	{
		this.webGlVertexArray = a;
	}
	
	protected void dispose() 
	{
		setArrayInitialized(false);
		
		setWebGlColorArray ( null );
		setWebGlVertexArray( null );
	}
}
