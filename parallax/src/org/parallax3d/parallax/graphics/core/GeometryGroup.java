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

package org.parallax3d.parallax.graphics.core;

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.parallax.graphics.renderers.GLGeometry;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint16Array;

public class GeometryGroup extends GLGeometry
{
	public static FastMap<List<GeometryGroup>> geometryGroups = new FastMap<List<GeometryGroup>>();

	private static int Counter = 0;

	private int id = 0;

	private List<Integer> faces3;

	private int materialIndex = -1;

	private int vertices;

	private int numMorphTargets;
	private int numMorphNormals;

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
		this.setFaces3(new ArrayList<Integer>());
		this.setMaterialIndex(materialIndex);
		this.setVertices(0);
		this.setNumMorphTargets(numMorphTargets);
		this.setNumMorphNormals(numMorphNormals);
	}

	public int getId() {
		return this.id;
	}


	/**
	 * @return the faces3
	 */
	public List<Integer> getFaces3() {
		return faces3;
	}

	/**
	 * @param faces3 the faces3 to set
	 */
	public void setFaces3(List<Integer> faces3) {
		this.faces3 = faces3;
	}

	/**
	 * @return the materialIndex
	 */
	public int getMaterialIndex() {
		return materialIndex;
	}

	/**
	 * @param materialIndex the materialIndex to set
	 */
	public void setMaterialIndex(int materialIndex) {
		this.materialIndex = materialIndex;
	}

	/**
	 * @return the vertices
	 */
	public int getVertices() {
		return vertices;
	}

	/**
	 * @param vertices the vertices to set
	 */
	public void setVertices(int vertices) {
		this.vertices = vertices;
	}

	/**
	 * @return the numMorphTargets
	 */
	public int getNumMorphTargets() {
		return numMorphTargets;
	}

	/**
	 * @param numMorphTargets the numMorphTargets to set
	 */
	public void setNumMorphTargets(int numMorphTargets) {
		this.numMorphTargets = numMorphTargets;
	}

	/**
	 * @return the numMorphNormals
	 */
	public int getNumMorphNormals() {
		return numMorphNormals;
	}

	/**
	 * @param numMorphNormals the numMorphNormals to set
	 */
	public void setNumMorphNormals(int numMorphNormals) {
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
