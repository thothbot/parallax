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

import thothbot.squirrel.core.client.gl2.arrays.Float32Array;
import thothbot.squirrel.core.shared.objects.GeometryObject;
import thothbot.squirrel.core.shared.objects.Object3D;

/**
 * This class implements three-dimensional region which is visible on the screen.
 * It is the field of view of the notional camera which is implemented as frustum 
 * of a rectangular pyramid with 6 planes. 
 * 
 * @author krs
 */
public class Frustum
{
	/**
	 * Panes of the Frustum of a rectangular pyramid
	 */
	public List<Vector4f> planes;

	/**
	 * Default constructor will make Frustum of a rectangular pyramid 
	 * with 6 planes. 
	 */
	public Frustum() 
	{
		this.planes = new ArrayList<Vector4f>();
		for(int i = 0; i < 6; i++)
			this.planes.add(new Vector4f());
	}

	/**
	 * Setting planes of the Frustum from the projection matrix
	 * 
	 * @param m the projection matrix
	 */
	public void setFromMatrix(Matrix4f m)
	{
		Float32Array me = m.getArray();
		float me0 = me.get(0), me1 = me.get(1), me2 = me.get(2), me3 = me.get(3);
		float me4 = me.get(4), me5 = me.get(5), me6 = me.get(6), me7 = me.get(7);
		float me8 = me.get(8), me9 = me.get(9), me10 = me.get(10), me11 = me.get(11);
		float me12 = me.get(12), me13 = me.get(13), me14 = me.get(14), me15 = me.get(15);

		planes.get(0).set(me3 - me0, me7 - me4, me11 - me8, me15 - me12);
		planes.get(1).set(me3 + me0, me7 + me4, me11 + me8, me15 + me12);
		planes.get(2).set(me3 + me1, me7 + me5, me11 + me9, me15 + me13);
		planes.get(3).set(me3 - me1, me7 - me5, me11 - me9, me15 - me13);
		planes.get(4).set(me3 - me2, me7 - me6, me11 - me10, me15 - me14);
		planes.get(5).set(me3 + me2, me7 + me6, me11 + me10, me15 + me14);

		for (int i = 0; i < 6; i++) 
		{
			Vector4f plane = planes.get(i);
			plane.divide((float) Math.sqrt(plane.x * plane.x + plane.y * plane.y + plane.z
					* plane.z));
		}
	}

	/**
	 * Checking if the 3D object located inside Frustum.
	 * 
	 * @param object any of 3D object
	 */
	public boolean contains(GeometryObject object)
	{
		float distance;
		Matrix4f matrix = object.getMatrixWorld();
		Float32Array me = matrix.getArray();
		float radius = -object.getGeometry().boundingSphere.radius * matrix.getMaxScaleOnAxis();

		for (int i = 0; i < 6; i++) 
		{
			Vector4f plane = planes.get(i);
			distance = plane.getX() * me.get(12) 
					+ plane.getY()  * me.get(13) 
					+ plane.getZ()	* me.get(14) 
					+ plane.getW();
			
			if (distance <= radius)
				return false;
		}

		return true;
	}
}
