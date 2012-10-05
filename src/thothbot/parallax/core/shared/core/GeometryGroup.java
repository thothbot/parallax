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

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;

public class GeometryGroup extends GeometryBuffer
{
	public List<Integer> faces3;
	public List<Integer> faces4;

	public int materialIndex = -1;

	public int vertices;

	public List<Float32Array> __morphTargetsArrays;
	public List<Float32Array> __morphNormalsArrays;

	private Float32Array webGlSkinIndexArray;
	private Float32Array webGlSkinWeightArray;

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

	public Float32Array getWebGlSkinIndexArray() 
	{
		return webGlSkinIndexArray;
	}

	public Float32Array getWebGlSkinWeightArray() 
	{
		return webGlSkinWeightArray;
	}
	
	public void setWebGlSkinIndexArray(Float32Array a)
	{
		this.webGlSkinIndexArray = a;
	}
	
	public void setWebGlSkinWeightArray(Float32Array a)
	{
		this.webGlSkinWeightArray = a;
	}
	
	@Override
	public void dispose() 
	{
		super.dispose();
		
		setWebGlSkinIndexArray( null );
		setWebGlSkinWeightArray( null );
	}
}
