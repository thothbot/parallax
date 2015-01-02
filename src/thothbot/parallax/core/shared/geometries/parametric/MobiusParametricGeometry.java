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
