/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.geometries;

import java.util.Arrays;

import thothbot.squirrel.core.shared.core.Face3;
import thothbot.squirrel.core.shared.core.Face4;
import thothbot.squirrel.core.shared.core.Geometry;
import thothbot.squirrel.core.shared.core.UVf;
import thothbot.squirrel.core.shared.core.Vector3f;

public class Parametric extends Geometry
{
	public static interface ParametricFunction 
	{
		Vector3f run(double u, double v);
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
			float v = (float)i / stacks;

			for ( int j = 0; j <= slices; j ++ ) 
			{
				float u = (float)j / slices;

				Vector3f p = function.run( u, v );
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

				UVf uva = new UVf( i / (float)slices,            j / (float) stacks );
				UVf uvb = new UVf( i / (float)slices,            ( j + 1.0f ) /(float) stacks );
				UVf uvc = new UVf( ( i + 1.0f ) / (float)slices, j / (float) stacks );
				UVf uvd = new UVf( ( i + 1.0f ) / (float)slices, ( j + 1.0f ) / (float)stacks );

				if ( useTris ) 
				{
					this.getFaces().add( new Face3( a, b, c ) );
					this.getFaces().add( new Face3( b, d, c ) );

					this.faceVertexUvs.get( 0 ).add( Arrays.asList( uva, uvb, uvc ) );
					this.faceVertexUvs.get( 0 ).add( Arrays.asList( uvb, uvd, uvc ) );

				} else {

					this.getFaces().add( new Face4( a, b, d, c ) );
					this.faceVertexUvs.get( 0 ).add( Arrays.asList( uva, uvb, uvc, uvd ) );

				}

			}
			
		}
		
		this.computeCentroids();
		this.computeFaceNormals(false);
		this.computeVertexNormals();
	}
}
