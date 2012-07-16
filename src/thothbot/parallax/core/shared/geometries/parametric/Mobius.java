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

import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.geometries.Parametric;

/*
 * flat mobius strip
 * http://www.wolframalpha.com/input/?i=M%C3%B6bius+strip+parametric+equations&lk=1&a=ClashPrefs_*Surface.MoebiusStrip.SurfaceProperty.ParametricEquations-
 */
public class Mobius extends Parametric
{

	public Mobius(int slices, int stacks) 
	{
		super(new Parametric.ParametricFunction() {
			
			@Override
			public Vector3f run(double u, double t)
			{
				u = u - 0.5;
				double v = 2.0 * Math.PI * t;

				double a = 2.0;
	
				float x = (float) (Math.cos(v) * (a + u * Math.cos(v/2.0)));
				float y = (float) (Math.sin(v) * (a + u * Math.cos(v/2.0)));
				float z = (float) (u * Math.sin(v/2.0));
				return new Vector3f(x, y, z);
			}
		}, 
		slices, stacks);
	}
}
