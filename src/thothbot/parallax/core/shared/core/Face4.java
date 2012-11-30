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

import java.util.List;

/**
 * Face4 represent the rectangular sides (A, B, C, D) of the geometric object.
 * <pre>
 * {@code
 * Vector3 normal = new Vector3( 0, 1, 0 ); 
 * Color color = new Color( 0xffaa00 ); 
 * Face4 face = new Face4( 0, 1, 2, 3, normal, color, 0 );
 * }
 * </pre>
 * @author thothbot
 *
 */
public class Face4 extends Face3
{
	protected int d;

	public Face4(int a, int b, int c, int d) 
	{
		super(a, b, c);
		this.setD(d);
	}
	
	public Face4(int a, int b, int c, int d, List<Vector3> normals) 
	{
		super(a, b, c, normals);
		this.setD(d);
	}
	
	public Face4(int a, int b, int c, int d, Vector3 normal, Color color, int materialIndex) 
	{
		super(a, b, c, normal, color, materialIndex);
		this.setD(d);
	}

	/**
	 * Sets Vertex D index.
	 */
	public void setD(int d)
	{
		this.d = d;
	}

	/**
	 * Gets Vertex D index.
	 */
	public int getD()
	{
		return d;
	}

	@Override
	public int[] getFlat()
	{
		int[] flat = new int[4];
		flat[0] = this.a;
		flat[1] = this.b;
		flat[2] = this.c;
		flat[3] = this.d;
		return flat;
	}

	// TODO: Material
	public Face4 clone()
	{

		Face4 face = new Face4(this.a, this.b, this.c, this.d);

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
		return "[" + this.a + ", " + this.b + ", " + this.c + ", " + this.d + "]";
	}
}
