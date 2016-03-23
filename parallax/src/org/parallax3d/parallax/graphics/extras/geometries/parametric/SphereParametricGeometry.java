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
import org.parallax3d.parallax.system.ThreejsObject;

@ThreejsObject("THREE.ParametricGeometries.SphereGeometry")
public class SphereParametricGeometry extends ParametricGeometry
{

	public SphereParametricGeometry(final double size, int slices, int stacks )
	{
		super(new ParametricFunction() {

			@Override
			public Vector3 run(double u, double v)
			{
				u *= Math.PI;
				v *= 2 * Math.PI;

				double x = size * Math.sin( u ) * Math.cos( v );
				double y = size * Math.sin( u ) * Math.sin( v );
				double z = size * Math.cos( u );


				return new Vector3( x, y, z );

			}
		}, slices, stacks);
	}

}
