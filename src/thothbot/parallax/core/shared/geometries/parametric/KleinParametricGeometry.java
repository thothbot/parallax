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

package thothbot.parallax.core.shared.geometries.parametric;

import thothbot.parallax.core.shared.geometries.ParametricGeometry;
import thothbot.parallax.core.shared.math.Vector3;

/**
 * <img src="http://thothbot.github.com/parallax/static/docs/klein_bottle.gif" />
 * <p>
 * Klein bottle geometry
 * 
 * @author thothbot
 *
 */
public class KleinParametricGeometry extends ParametricGeometry
{
	public KleinParametricGeometry(int slices, int stacks) 
	{
		super(new ParametricGeometry.ParametricFunction() 
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
