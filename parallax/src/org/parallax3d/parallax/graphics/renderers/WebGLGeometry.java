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

package org.parallax3d.parallax.graphics.renderers;

import java.util.List;

import org.parallax3d.parallax.system.ThreeJsObject;
import org.parallax3d.parallax.graphics.renderers.shaders.Attribute;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

@ThreeJsObject("THREE.WebGLGeometries")
public abstract class WebGLGeometry {

	public Float32Array __colorArray;
	public Float32Array __vertexArray;
	public Float32Array __normalArray;
	public Float32Array __tangentArray;
	public Float32Array __uvArray;
	public Float32Array __uv2Array;
	public Float32Array __lineDistanceArray;

	// WebGLBuffer
	public Integer __webglColorBuffer;
	public Integer __webglVertexBuffer;
	public Integer __webglNormalBuffer;
	public Integer __webglTangentBuffer;
	public Integer __webglUVBuffer;
	public Integer __webglUV2Buffer;
	public Integer __webglLineDistanceBuffer;

	public Integer __webglSkinIndicesBuffer;
	public Integer __webglSkinWeightsBuffer;

	public Integer __webglFaceBuffer;
	public Integer __webglLineBuffer;

	public List<Integer> __webglMorphTargetsBuffers; // WebGLBuffer

	public List<Integer> __webglMorphNormalsBuffers; // WebGLBuffer

	public List<Attribute> __webglCustomAttributesList;

	public int __webglParticleCount;
	public int __webglLineCount;
	public int __webglVertexCount;
	public int __webglFaceCount;

	public boolean __webglInit;

	public abstract int getId();

	public void dispose()
	{
		__colorArray = null;
		__normalArray = null;
		__tangentArray = null;
		__uvArray = null;
		__uv2Array = null;
		__vertexArray = null;
	}

	// static would be better for GC efficiency but worse for thread safety
	private final int[] tmpBufArray = { 0 };
}
