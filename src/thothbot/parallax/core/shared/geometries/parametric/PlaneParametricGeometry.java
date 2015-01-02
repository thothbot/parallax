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

public class PlaneParametricGeometry extends ParametricGeometry
{

	public PlaneParametricGeometry(final int width, final int height, int slices, int stacks)
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
