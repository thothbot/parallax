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
 * <img src="http://thothbot.github.com/parallax/static/docs/mobius_3d.gif" />
 * <p>
 * Mobius 3D geometry
 * 
 * @author thothbot
 *
 */
public class Mobius3dParametricGeometry extends ParametricGeometry
{

	public Mobius3dParametricGeometry(int slices, int stacks) {
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

				double x = a * Math.cos(t) * Math.cos(phi) - b * Math.sin(t) * Math.sin(phi);
				double z = a * Math.cos(t) * Math.sin(phi) + b * Math.sin(t) * Math.cos(phi);
				double y = (major + x) * Math.sin(u);
				x = (major + x) * Math.cos(u);
				return new Vector3(x, y, z);
			}
		}, slices, stacks);
		// TODO Auto-generated constructor stub
	}

}
