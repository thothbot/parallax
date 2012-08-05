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

/**
 * Face3 represent the triangular sides (A, B, C) of the geometric object.
 * 
 * @author thothbot
 *
 */
public class Face3
{
	protected int a;
	protected int b;
	protected int c;

	protected Vector3 normal;
	protected List<Vector3> vertexNormals;
	protected Color color;
	protected List<Color> vertexColors;
	// protected List<Material> materials;
	protected int materialIndex;
	protected List<Vector4> vertexTangents;
	protected Vector3 centroid;
	
	protected Vector3 originalNormal;
	protected List<Vector3> originalVertexNormals;

	public Face3(int a, int b, int c, List<Vector3> normals)
	{
		this(a,b,c);

		this.setVertexNormals(normals);
	}

	public Face3(int a, int b, int c, int materialIndex)
	{
		this(a, b, c);
		
		this.materialIndex = materialIndex;
	}
	
	public Face3(int a, int b, int c, List<Vector3> normals, List<Color> colors,	int materialIndex) 
	{
		this(a, b, c);
		
		this.setVertexNormals(normals);
		this.setVertexColors(new ArrayList<Color>());

		this.materialIndex = materialIndex;
	}

	public Face3(int a, int b, int c, Vector3 normal, Color color, int materialIndex) 
	{
		this(a, b, c);
		
		this.setNormal(normal instanceof Vector3 ? normal : new Vector3());
		this.setVertexNormals(new ArrayList<Vector3>());

		this.setColor(color);
		this.setVertexColors(new ArrayList<Color>());

		this.materialIndex = materialIndex;
	}

	public Face3(int a, int b, int c) 
	{
		this.setA(a);
		this.setB(b);
		this.setC(c);
		this.setNormal(new Vector3());
		this.setVertexTangents(new ArrayList<Vector4>());
		this.setVertexNormals(new ArrayList<Vector3>());
		this.setCentroid(new Vector3());
		this.setColor(new Color(0x000000));
		this.setVertexColors(new ArrayList<Color>());

		this.materialIndex = -1;
		this.vertexColors = new ArrayList<Color>();
		// this.materials = new ArrayList<Material>();
		
		this.originalNormal = new Vector3();
		this.originalVertexNormals = new ArrayList<Vector3>();
	}

	public void setA(int a)
	{
		this.a = a;
	}

	public int getA()
	{
		return a;
	}

	public void setB(int b)
	{
		this.b = b;
	}

	public int getB()
	{
		return b;
	}

	public void setC(int c)
	{
		this.c = c;
	}

	public int getC()
	{
		return c;
	}

	public int[] getFlat()
	{
		int[] flat = new int[3];
		flat[0] = this.a;
		flat[1] = this.b;
		flat[2] = this.c;
		return flat;
	}

	public void setNormal(Vector3 normal)
	{
		this.normal = normal;
	}

	public Vector3 getNormal()
	{
		return normal;
	}

	public void setVertexNormals(List<Vector3> vertexNormals)
	{
		this.vertexNormals = vertexNormals;
	}

	public List<Vector3> getVertexNormals()
	{
		return vertexNormals;
	}
	
	public Vector3 getOriginalNormal() {
		return this.originalNormal;
	}
	
	public List<Vector3> getOriginalVertexNormals() {
		return this.originalVertexNormals;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public Color getColor()
	{
		return color;
	}

	public void setVertexColors(List<Color> vertexColors)
	{
		this.vertexColors = vertexColors;
	}

	public List<Color> getVertexColors()
	{
		return vertexColors;
	}

	public void setVertexTangents(List<Vector4> vertexTangents)
	{
		this.vertexTangents = vertexTangents;
	}

	public List<Vector4> getVertexTangents()
	{
		return vertexTangents;
	}

	public void setCentroid(Vector3 centroid)
	{
		this.centroid = centroid;
	}

	public Vector3 getCentroid()
	{
		return centroid;
	}

	// public List<Material> getMaterials(){
	// return this.materials;
	// }

	public int getMaterialIndex()
	{
		return this.materialIndex;
	}

	public void setMaterialIndex(int index)
	{
		this.materialIndex = index;
	}

	public Face3 clone()
	{
		Face3 face = new Face3(this.a, this.b, this.c);

		face.normal.copy(this.normal);
		face.color.copy(this.color);
		face.centroid.copy(this.centroid);
		face.materialIndex = this.materialIndex;

		for (int i = 0, il = this.vertexNormals.size(); i < il; i++)
			face.vertexNormals.set(i, this.vertexNormals.get(i).clone());

		for (int i = 0, il = this.vertexColors.size(); i < il; i++)
			face.vertexColors.set(i, this.vertexColors.get(i).clone());

		for (int i = 0, il = this.vertexTangents.size(); i < il; i++)
			face.vertexTangents.set(i, this.vertexTangents.get(i).clone());

		return face;
	}
	
	public String toString()
	{
		return "[" + this.a + ", " + this.b + ", " + this.c + "]";
	}
}
