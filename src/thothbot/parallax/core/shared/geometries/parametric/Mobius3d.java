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

public class Mobius3d extends Parametric
{

	public Mobius3d(int slices, int stacks) {
		super(new ParametricFunction() {
			
			@Override
			public Vector3 run(double u, double t)
			{
				// volumetric mobius strip
				u *= Math.PI;
				t *= 2.0 * Math.PI;

				u = u * 2.0;
				double phi = u / 2.0;
				double  major = 2.25, a = 0.125, b = 0.65;

				float x = (float) (a * Math.cos(t) * Math.cos(phi) - b * Math.sin(t) * Math.sin(phi));
				float z = (float) (a * Math.cos(t) * Math.sin(phi) + b * Math.sin(t) * Math.cos(phi));
				float y = (float) ((major + x) * Math.sin(u));
				x = (float) ((major + x) * Math.cos(u));
				return new Vector3(x, y, z);
			}
		}, slices, stacks);
		// TODO Auto-generated constructor stub
	}

}
