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

package thothbot.parallax.core.shared.geometries;

import java.util.Arrays;

import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Face4;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.UV;
import thothbot.parallax.core.shared.core.Vector3;

public class Parametric extends Geometry
{
	public static interface ParametricFunction 
	{
		Vector3 run(double u, double v);
	}

	public Parametric(final ParametricFunction function, int slices, int stacks)
	{
		this(function, slices, stacks, false);
	}
	
	public Parametric(final ParametricFunction function, int slices, int stacks, boolean useTris)
	{
		super();

		int stackCount = stacks + 1;
		int sliceCount = slices + 1;
		
		for ( int i = 0; i <= stacks; i ++ ) 
		{
			double v = i / stacks * 1.0;

			for ( int j = 0; j <= slices; j ++ ) 
			{
				double u = j / slices * 1.0;

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

				UV uva = new UV( i / slices * 1.0,                      j / stacks * 1.0 );
				UV uvb = new UV( i / slices * 1.0,            ( j + 1.0 ) / stacks * 1.0 );
				UV uvc = new UV( ( i + 1.0 ) / slices * 1.0,            j / stacks * 1.0 );
				UV uvd = new UV( ( i + 1.0 ) / slices * 1.0,  ( j + 1.0 ) / stacks * 1.0 );

				if ( useTris ) 
				{
					this.getFaces().add( new Face3( a, b, c ) );
					this.getFaces().add( new Face3( b, d, c ) );

					getFaceVertexUvs().get( 0 ).add( Arrays.asList( uva, uvb, uvc ) );
					getFaceVertexUvs().get( 0 ).add( Arrays.asList( uvb, uvd, uvc ) );

				} 
				else 
				{
					this.getFaces().add( new Face4( a, b, d, c ) );
					getFaceVertexUvs().get( 0 ).add( Arrays.asList( uva, uvb, uvc, uvd ) );
				}
			}
		}
		
		this.computeCentroids();
		this.computeFaceNormals(false);
		this.computeVertexNormals();
	}
}
