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

package thothbot.parallax.core.shared.geometries;

import java.util.Arrays;

import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;

/**
 * Parametric Surfaces Geometry.
 * <p>
 * Based on the article <a href="http://prideout.net/blog/?p=44">prideout.net</a>
 * 
 * @author thothbot
 *
 */
public class ParametricGeometry extends Geometry
{
	public static interface ParametricFunction 
	{
		Vector3 run(double u, double v);
	}

	public ParametricGeometry(final ParametricFunction function, int slices, int stacks)
	{
		super();

		int sliceCount = slices + 1;
		
		for ( int i = 0; i <= stacks; i ++ ) 
		{
			double v = i / (double)stacks;

			for ( int j = 0; j <= slices; j ++ ) 
			{
				double u = j / (double)slices;

				Vector3 p = function.run( u, v );
				this.getVertices().add( p );

			}
		}

		for ( int i = 0; i < stacks; i ++ ) 
		{
			for ( int j = 0; j < slices; j ++ ) 
			{
				int a = i * sliceCount + j;
				int b = i * sliceCount + j + 1;
				int c = (i + 1) * sliceCount + j;
				int d = (i + 1) * sliceCount + j + 1;

				Vector2 uva = new Vector2( i / (double)slices,                      j / (double)stacks );
				Vector2 uvb = new Vector2( i / (double)slices,            ( j + 1.0 ) / (double)stacks );
				Vector2 uvc = new Vector2( ( i + 1.0 ) / (double)slices,            j / (double)stacks );
				Vector2 uvd = new Vector2( ( i + 1.0 ) / (double)slices,  ( j + 1.0 ) / (double)stacks );

				this.getFaces().add( new Face3( a, b, c ) );
				this.getFaces().add( new Face3( b, d, c ) );

				getFaceVertexUvs().get( 0 ).add( Arrays.asList( uva, uvb, uvc ) );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList( uvb, uvd, uvc ) );

			}
		}
		
		this.computeFaceNormals();
		this.computeVertexNormals();
	}
}
