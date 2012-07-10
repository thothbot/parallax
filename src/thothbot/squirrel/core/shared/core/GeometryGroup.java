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

import java.util.ArrayList;
import java.util.List;

import thothbot.squirrel.core.client.gl2.WebGLBuffer;
import thothbot.squirrel.core.client.gl2.arrays.Float32Array;
import thothbot.squirrel.core.client.gl2.arrays.Uint16Array;


public class GeometryGroup extends GeometryBuffer
{
	public List<Integer> faces3;
	public List<Integer> faces4;
	
	public int materialIndex;

	public List<Integer> offsets;
	public int vertices;
	
	public WebGLBuffer vertexPositionBuffer;
	public WebGLBuffer vertexNormalBuffer;
	public WebGLBuffer vertexUvBuffer;
	public WebGLBuffer vertexColorBuffer;
	public WebGLBuffer vertexIndexBuffer;
	
	public Float32Array __normalArray;
	public Float32Array __tangentArray;
	public Float32Array __uvArray;
	public Float32Array __uv2Array;
	
	public Float32Array __skinVertexAArray;
	public Float32Array __skinVertexBArray;
	public Float32Array __skinIndexArray;
	public Float32Array __skinWeightArray;
	
	public Uint16Array __faceArray;
	public Uint16Array __lineArray;
	
	public List<Float32Array> __morphTargetsArrays;	
	public List<Float32Array> __morphNormalsArrays;
	
	public boolean __inittedArrays;
		
	public GeometryGroup(int materialIndex, int numMorphTargets, int numMorphNormals) 
	{
		super();
		
		this.faces3 = new ArrayList<Integer>();
		this.faces4 = new ArrayList<Integer>();
		this.materialIndex = materialIndex;
		this.vertices = 0;
		this.numMorphTargets = numMorphTargets;
		this.numMorphNormals = numMorphNormals;
	}

}
