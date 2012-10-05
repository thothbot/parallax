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

package thothbot.parallax.core.shared.core;

import java.util.List;

import thothbot.parallax.core.client.gl2.WebGLBuffer;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Int16Array;
import thothbot.parallax.core.client.gl2.arrays.Uint16Array;
import thothbot.parallax.core.client.shaders.Attribute;


public class GeometryBuffer
{
	public class Offset 
	{
		public int start;
		public int count;
		public int index;	
	}

	public static int Counter = 0;
	private int id = 0;

	public List<GeometryBuffer.Offset> offsets;
	
	private boolean isArrayInitialized;
	
	private Int16Array webGlIndexArray;
	private Uint16Array webGlFaceArray;
	private Uint16Array webGlLineArray;
	
	private Float32Array webGlPositionArray;
	private Float32Array webGlColorArray;
	private Float32Array webGlVertexArray;
	private Float32Array webGlNormalArray;
	private Float32Array webGlTangentArray;
	private Float32Array webGlUvArray;
	private Float32Array webGlUv2Array;
	
	public WebGLBuffer __webglIndexBuffer;
	public WebGLBuffer __webglFaceBuffer;
	public WebGLBuffer __webglLineBuffer;
	
	public WebGLBuffer __webglPositionBuffer;
	public WebGLBuffer __webglColorBuffer;
	public WebGLBuffer __webglVertexBuffer;
	public WebGLBuffer __webglNormalBuffer;
	public WebGLBuffer __webglTangentBuffer;
	public WebGLBuffer __webglUVBuffer;
	public WebGLBuffer __webglUV2Buffer;
	
	public WebGLBuffer __webglSkinIndicesBuffer;
	public WebGLBuffer __webglSkinWeightsBuffer;
		
	public int numMorphTargets;
	public List<WebGLBuffer> __webglMorphTargetsBuffers;
	
	public int numMorphNormals;
	public List<WebGLBuffer> __webglMorphNormalsBuffers;
	
	public List<Attribute> __webglCustomAttributesList;
	
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

	/**
	 * Gets the Unique number of this geometry instance
	 */
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
	
	public Float32Array getWebGlVertexArray()
	{
		return this.webGlVertexArray;
	}

	public Float32Array getWebGlPositionArray()
	{
		return this.webGlPositionArray;
	}
	
	public void setWebGlPositionArray(Float32Array a)
	{
		this.webGlPositionArray = a;
	}
	
	public Float32Array getWebGlColorArray()
	{
		return this.webGlColorArray;
	}
	
	public void setWebGlColorArray(Float32Array a)
	{
		this.webGlColorArray = a;
	}
	
	public void setWebGlVertexArray(Float32Array a)
	{
		this.webGlVertexArray = a;
	}
	
	public void setWebGlNormalArray(Float32Array a)
	{
		this.webGlNormalArray = a;
	}
	
	public void setWebGlTangentArray(Float32Array a)
	{
		this.webGlTangentArray = a;
	}
	
	public void setWebGlUvArray(Float32Array a)
	{
		this.webGlUvArray = a;
	}
	
	public void setWebGlUv2Array(Float32Array a)
	{
		this.webGlUv2Array = a;
	}
	
	public Float32Array getWebGlNormalArray() 
	{
		return webGlNormalArray;
	}

	public Float32Array getWebGlTangentArray() 
	{
		return webGlTangentArray;
	}

	public Float32Array getWebGlUvArray() 
	{
		return webGlUvArray;
	}

	public Float32Array getWebGlUv2Array() 
	{
		return webGlUv2Array;
	}
	
	public Int16Array getWebGlIndexArray() 
	{
		return webGlIndexArray;
	}
	
	public void setWebGlIndexArray(Int16Array a)
	{
		this.webGlIndexArray = a;
	}
	
	public Uint16Array getWebGlFaceArray() 
	{
		return webGlFaceArray;
	}

	public Uint16Array getWebGlLineArray() 
	{
		return webGlLineArray;
	}
	
	public void setWebGlFaceArray(Uint16Array a)
	{
		this.webGlFaceArray = a;
	}
	
	public void setWebGlLineArray(Uint16Array a)
	{
		this.webGlLineArray = a;
	}
	
	protected void dispose() 
	{
		setArrayInitialized(false);
		
		setWebGlIndexArray( null );
		setWebGlFaceArray( null );
		setWebGlLineArray( null );
		
		setWebGlPositionArray( null );
		setWebGlColorArray ( null );
		setWebGlVertexArray( null );

		setWebGlNormalArray( null );
		setWebGlTangentArray( null );
		setWebGlUvArray( null );
		setWebGlUv2Array( null );		
	}
}
