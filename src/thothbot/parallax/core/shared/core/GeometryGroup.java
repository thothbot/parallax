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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;

import thothbot.parallax.core.client.gl2.WebGLBuffer;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Uint16Array;
import thothbot.parallax.core.client.renderers.WebGLExtensions;
import thothbot.parallax.core.client.renderers.WebGLGeometry;
import thothbot.parallax.core.client.renderers.WebGLExtensions.Id;
import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.shared.materials.MeshFaceMaterial;
import thothbot.parallax.core.shared.scenes.Scene;

public class GeometryGroup extends WebGLGeometry
{
	public static Map<String, List<GeometryGroup>> geometryGroups = GWT.isScript() ? 
			new FastMap<List<GeometryGroup>>() : new HashMap<String, List<GeometryGroup>>(); 
	
	public static int Counter = 0;
	
	public int id = 0;
	
	public List<Integer> faces3;

	public int materialIndex = -1;

	public int vertices;
	
	public int numMorphTargets;
	public int numMorphNormals;

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

		this.id = GeometryGroup.Counter++;
		this.faces3 = new ArrayList<Integer>();
		this.materialIndex = materialIndex;
		this.vertices = 0;
		this.numMorphTargets = numMorphTargets;
		this.numMorphNormals = numMorphNormals;
	}
		
	@Override
	public void dispose() 
	{
		super.dispose();
		
		__inittedArrays = false;
		__faceArray = null;
		__lineArray = null;
		__skinIndexArray = null;
		__skinWeightArray = null;
	}
}
