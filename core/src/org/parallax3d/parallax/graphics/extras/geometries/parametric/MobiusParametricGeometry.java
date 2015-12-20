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

package org.parallax3d.parallax.graphics.extras.geometries.parametric;

import org.parallax3d.parallax.graphics.extras.geometries.ParametricGeometry;
import org.parallax3d.parallax.math.Vector3;

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
		super(new ParametricFunction() {
			
			@Override
			public Vector3 run(float u, float t)
			{
				u = u - 0.5f;
				float v = (float)(2.0 * Math.PI * t);

				float a = 2.0f;
	
				float x = (float)(Math.cos(v) * (a + u * Math.cos(v/2.0)));
				float y = (float)(Math.sin(v) * (a + u * Math.cos(v/2.0)));
				float z = (float)(u * Math.sin(v/2.0));
				return new Vector3(x, y, z);
			}
		}, 
		slices, stacks);
	}
}
