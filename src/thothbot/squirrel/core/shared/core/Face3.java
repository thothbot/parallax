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

public class Face3
{
	protected int a;
	protected int b;
	protected int c;

	protected Vector3f normal;
	protected List<Vector3f> vertexNormals;
	protected Color3f color;
	protected List<Color3f> vertexColors;
	// protected List<Material> materials;
	protected int materialIndex;
	protected List<Vector4f> vertexTangents;
	protected Vector3f centroid;

	public Face3(int a, int b, int c, List<Vector3f> normals)
	{
		this(a,b,c);

		this.setVertexNormals(normals);
	}

	public Face3(int a, int b, int c, int materialIndex)
	{
		this(a, b, c);
		
		this.materialIndex = materialIndex;
	}
	
	public Face3(int a, int b, int c, List<Vector3f> normals, List<Color3f> colors,	int materialIndex) 
	{
		this(a, b, c);
		
		this.setVertexNormals(normals);
		this.setVertexColors(new ArrayList<Color3f>());

		this.materialIndex = materialIndex;
	}

	public Face3(int a, int b, int c, Vector3f normal, Color3f color, int materialIndex) 
	{
		this(a, b, c);
		
		this.setNormal(normal instanceof Vector3f ? normal : new Vector3f());
		this.setVertexNormals(new ArrayList<Vector3f>());

		this.setColor(color);
		this.setVertexColors(new ArrayList<Color3f>());

		this.materialIndex = materialIndex;
	}

	public Face3(int a, int b, int c) 
	{
		this.setA(a);
		this.setB(b);
		this.setC(c);
		this.setNormal(new Vector3f());
		this.setVertexTangents(new ArrayList<Vector4f>());
		this.setVertexNormals(new ArrayList<Vector3f>());
		this.setCentroid(new Vector3f());
		this.setColor(new Color3f(0x000000));
		this.setVertexColors(new ArrayList<Color3f>());

		this.materialIndex = -1;
		this.vertexColors = new ArrayList<Color3f>();
		// this.materials = new ArrayList<Material>();
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

	public void setNormal(Vector3f normal)
	{
		this.normal = normal;
	}

	public Vector3f getNormal()
	{
		return normal;
	}

	public void setVertexNormals(List<Vector3f> vertexNormals)
	{
		this.vertexNormals = vertexNormals;
	}

	public List<Vector3f> getVertexNormals()
	{
		return vertexNormals;
	}

	public void setColor(Color3f color)
	{
		this.color = color;
	}

	public Color3f getColor()
	{
		return color;
	}

	public void setVertexColors(List<Color3f> vertexColors)
	{
		this.vertexColors = vertexColors;
	}

	public List<Color3f> getVertexColors()
	{
		return vertexColors;
	}

	public void setVertexTangents(List<Vector4f> vertexTangents)
	{
		this.vertexTangents = vertexTangents;
	}

	public List<Vector4f> getVertexTangents()
	{
		return vertexTangents;
	}

	public void setCentroid(Vector3f centroid)
	{
		this.centroid = centroid;
	}

	public Vector3f getCentroid()
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
