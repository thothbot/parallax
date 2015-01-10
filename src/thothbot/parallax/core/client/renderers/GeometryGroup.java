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

package thothbot.parallax.core.client.renderers;

import java.util.ArrayList;
import java.util.List;

public class GeometryGroup
{
	public static int Counter = 0;
	
	public int id = 0;
	
	public List<Integer> faces3;

	public int materialIndex = -1;

	public int vertices;
	
	public int numMorphTargets;
	public int numMorphNormals;

//	public List<Float32Array> __morphTargetsArrays;
//	public List<Float32Array> __morphNormalsArrays;
//
//	private Float32Array webGlSkinIndexArray;
//	private Float32Array webGlSkinWeightArray;

	public GeometryGroup(int materialIndex, int numMorphTargets, int numMorphNormals) 
	{
		super();

		this.faces3 = new ArrayList<Integer>();
		this.materialIndex = materialIndex;
		this.vertices = 0;
		this.numMorphTargets = numMorphTargets;
		this.numMorphNormals = numMorphNormals;
	}

//	public Float32Array getWebGlSkinIndexArray() 
//	{
//		return webGlSkinIndexArray;
//	}
//
//	public Float32Array getWebGlSkinWeightArray() 
//	{
//		return webGlSkinWeightArray;
//	}
//	
//	public void setWebGlSkinIndexArray(Float32Array a)
//	{
//		this.webGlSkinIndexArray = a;
//	}
//	
//	public void setWebGlSkinWeightArray(Float32Array a)
//	{
//		this.webGlSkinWeightArray = a;
//	}
	
//	@Override
//	public void dispose() 
//	{
//		super.dispose();
//		
//		setWebGlSkinIndexArray( null );
//		setWebGlSkinWeightArray( null );
//	}
}
