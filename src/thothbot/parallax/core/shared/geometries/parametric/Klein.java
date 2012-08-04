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

/**
 * The Klein bottle geometry
 * <p>
 * <img src="http://thothbot.github.com/parallax/static/docs/klein_bottle.gif" />
 * <p>
 * Based on the three.js code.
 * 
 * @author thothbot
 *
 */
public class Klein extends Parametric
{
	public Klein(int slices, int stacks) 
	{
		super(new Parametric.ParametricFunction() 
		{	
			@Override
			public Vector3 run(double u, double v)
			{
				u *= Math.PI;
				v *= 2.0 * Math.PI;

				u = u * 2.0;
				double x,y,z;

				if (u < Math.PI) 
				{
					x = 3.0 * Math.cos(u) * (1.0 + Math.sin(u)) + (2.0 * (1.0 - Math.cos(u) / 2.0)) * Math.cos(u) * Math.cos(v);
					z = -8.0 * Math.sin(u) - 2.0 * (1.0 - Math.cos(u) / 2.0) * Math.sin(u) * Math.cos(v);
				} 
				else 
				{
					x = 3.0 * Math.cos(u) * (1.0 + Math.sin(u)) + (2.0 * (1.0 - Math.cos(u) / 2.0)) * Math.cos(v + Math.PI);
					z = -8.0 * Math.sin(u);
				}

				y = -2.0 * (1.0 - Math.cos(u) / 2.0) * Math.sin(v);
				
				return new Vector3(x, y, z);
			}
		}, slices, stacks);
	}

}
