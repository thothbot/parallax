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

package thothbot.parallax.core.shared.geometries.parametric;

import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.geometries.Parametric;

public class Plane extends Parametric
{

	public Plane(final int width, final int height, int slices, int stacks)
	{
		super(new ParametricFunction() {
			
			@Override
			public Vector3 run(double u, double v)
			{
				double x = u * (double)width;
				double y = 0.0;
				double z = v * (double)height;

				return new Vector3(x, y, z);
			}
		}, slices, stacks);
	}

}
