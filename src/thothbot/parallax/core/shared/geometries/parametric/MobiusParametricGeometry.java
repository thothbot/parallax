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
import thothbot.parallax.core.shared.geometries.ParametricGeometry;

/**
 * <img src="http://thothbot.github.com/parallax/static/docs/mobius_strip.gif" />
 * <p>
 * Flat Mobius strip geometry
 * 
 * @author thothbot
 *
 */
public class MobiusParametricGeometry extends ParametricGeometry
{

	public MobiusParametricGeometry(int slices, int stacks) 
	{
		super(new ParametricGeometry.ParametricFunction() {
			
			@Override
			public Vector3 run(double u, double t)
			{
				u = u - 0.5;
				double v = 2.0 * Math.PI * t;

				double a = 2.0;
	
				double x = Math.cos(v) * (a + u * Math.cos(v/2.0));
				double y = Math.sin(v) * (a + u * Math.cos(v/2.0));
				double z = u * Math.sin(v/2.0);
				return new Vector3(x, y, z);
			}
		}, 
		slices, stacks);
	}
}
