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
import java.util.List;

import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.math.Vector4;

/**
 * Face3 represent the triangular sides (A, B, C) of the geometric object.
 * 
 * <pre>
 * {
 * 	&#064;code
 * 	Vector3 normal = new Vector3(0, 1, 0);
 * 	Color color = new Color(0xffaa00);
 * 	Face3 face = new Face3(0, 1, 2, normal, color, 0);
 * }
 * </pre>
 * 
 * @author thothbot
 * 
 */
public class Face3 {
	protected int a;
	protected int b;
	protected int c;

	protected Vector3 normal ;
	protected List<Vector3> vertexNormals;
	protected Color color;
	protected List<Color> vertexColors;
	// protected List<Material> materials;
	protected int materialIndex = 0;
	protected List<Vector4> vertexTangents;
	
	// Special case used in Geometry.computeMorphNormals()
	public Vector3 __originalFaceNormal;
	public List<Vector3> __originalVertexNormals;

	public Face3(int a, int b, int c, Vector3 normal) {
		this(a, b, c);

		this.normal = normal;
	}

	
	/**
	 * Face3 constructor
	 * 
	 * @param a
	 *            the Vertex A index.
	 * @param b
	 *            the Vertex B index.
	 * @param c
	 *            the Vertex C index.
	 * @param normals
	 *            the List of vertex normals.
	 */
	public Face3(int a, int b, int c, List<Vector3> normals) {
		this(a, b, c);

		this.setVertexNormals(normals);
	}

	/**
	 * Face3 constructor
	 * 
	 * @param a
	 *            the Vertex A index.
	 * @param b
	 *            the Vertex B index.
	 * @param c
	 *            the Vertex C index.
	 * @param materialIndex
	 *            the Material index.
	 */
	public Face3(int a, int b, int c, int materialIndex) {
		this(a, b, c);

		this.materialIndex = materialIndex;
	}

	/**
	 * Face3 constructor
	 * 
	 * @param a
	 *            the Vertex A index.
	 * @param b
	 *            the Vertex B index.
	 * @param c
	 *            the Vertex C index.
	 */
	public Face3(int a, int b, int c) {
		this.setA(a);
		this.setB(b);
		this.setC(c);
		this.setNormal(new Vector3());
		this.vertexTangents = new ArrayList<Vector4>();
		this.vertexNormals = new ArrayList<Vector3>();
		this.color = new Color(0x000000);

		this.vertexColors = new ArrayList<Color>();
	}

	/**
	 * Face3 constructor
	 * 
	 * @param a
	 *            the Vertex A index.
	 * @param b
	 *            the Vertex B index.
	 * @param c
	 *            the Vertex C index.
	 * @param normals
	 *            the List of vertex normals.
	 * @param colors
	 *            the List of vertex colors.
	 * @param materialIndex
	 *            the Material index.
	 */
	public Face3(int a, int b, int c, List<Vector3> normals,
			List<Color> colors, int materialIndex) {
		this(a, b, c);

		this.setVertexNormals(normals);
		this.setVertexColors(new ArrayList<Color>());

		this.materialIndex = materialIndex;
	}

	/**
	 * Face3 constructor
	 * 
	 * @param a
	 *            the Vertex A index.
	 * @param b
	 *            the Vertex B index.
	 * @param c
	 *            the Vertex C index.
	 * @param normal
	 *            the Face normal.
	 * @param color
	 *            the Face color.
	 * @param materialIndex
	 *            the Material index.
	 */
	public Face3(int a, int b, int c, Vector3 normal, Color color,
			int materialIndex) {
		this(a, b, c);

		this.setNormal(normal instanceof Vector3 ? normal : new Vector3());
		this.setVertexNormals(new ArrayList<Vector3>());

		this.setColor(color);
		this.setVertexColors(new ArrayList<Color>());

		this.materialIndex = materialIndex;
	}

	/**
	 * Sets Vertex A index.
	 */
	public void setA(int a) {
		this.a = a;
	}

	/**
	 * Gets Vertex A index.
	 */
	public int getA() {
		return a;
	}

	/**
	 * Sets Vertex B index.
	 */
	public void setB(int b) {
		this.b = b;
	}

	/**
	 * Gets Vertex B index.
	 */
	public int getB() {
		return b;
	}

	/**
	 * Sets Vertex C index
	 */
	public void setC(int c) {
		this.c = c;
	}

	/**
	 * Gets Vertex C index
	 */
	public int getC() {
		return c;
	}

	public int[] getFlat() {
		int[] flat = new int[3];
		flat[0] = this.a;
		flat[1] = this.b;
		flat[2] = this.c;
		return flat;
	}

	/**
	 * Sets the Face normal.
	 */
	public void setNormal(Vector3 normal) {
		this.normal = normal;
	}

	/**
	 * Gets the Face normal.
	 */
	public Vector3 getNormal() {
		return normal;
	}

	/**
	 * Sets List of 3 vertex normals.
	 */
	public void setVertexNormals(List<Vector3> vertexNormals) {
		this.vertexNormals = vertexNormals;
	}

	/**
	 * Gets List of 3 vertex normals.
	 */
	public List<Vector3> getVertexNormals() {
		return vertexNormals;
	}

	/**
	 * Sets the Face color.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Gets the Face color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets List of 3 vertex colors.
	 */
	public void setVertexColors(List<Color> vertexColors) {
		this.vertexColors = vertexColors;
	}

	/**
	 * Gets List of 3 vertex colors.
	 */
	public List<Color> getVertexColors() {
		return vertexColors;
	}

	/**
	 * Sets List of 3 vertex tangets.
	 */
	public void setVertexTangents(List<Vector4> vertexTangents) {
		this.vertexTangents = vertexTangents;
	}

	/**
	 * Gets List of 3 vertex tangets.
	 */
	public List<Vector4> getVertexTangents() {
		return vertexTangents;
	}
//
//	/**
//	 * Sets the Face centroid.
//	 */
//	public void setCentroid(Vector3 centroid) {
//		this.centroid = centroid;
//	}
//
//	/**
//	 * Gets the Face centroid.
//	 */
//	public Vector3 getCentroid() {
//		return centroid;
//	}

	// public List<Material> getMaterials(){
	// return this.materials;
	// }

	/**
	 * Gets Material index.
	 */
	public int getMaterialIndex() {
		return this.materialIndex;
	}

	/**
	 * Sets Material index.
	 */
	public void setMaterialIndex(int index) {
		this.materialIndex = index;
	}

	public Face3 clone() {
		Face3 face = new Face3(this.a, this.b, this.c);

		face.normal.copy(this.normal);
		face.color.copy(this.color);
		face.materialIndex = this.materialIndex;

		for (int i = 0, il = this.vertexNormals.size(); i < il; i++)
			face.vertexNormals.add(this.vertexNormals.get(i).clone());

		for (int i = 0, il = this.vertexColors.size(); i < il; i++)
			face.vertexColors.add(this.vertexColors.get(i).clone());

		for (int i = 0, il = this.vertexTangents.size(); i < il; i++)
			face.vertexTangents.add(this.vertexTangents.get(i).clone());

		return face;
	}

	public String toString() {
		return "[" + this.a + ", " + this.b + ", " + this.c + "]";
	}
}
