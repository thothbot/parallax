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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thothbot.parallax.core.shared.core.Face4;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Matrix4f;
import thothbot.parallax.core.shared.core.UVf;
import thothbot.parallax.core.shared.core.Vector3f;

public final class Lathe extends Geometry
{
	
	public Lathe ( List<Vector3f> points) 
	{
		this(points, 12);
	}
	
	public Lathe ( List<Vector3f> points, int steps)
	{
		this(points, steps, (float) (2f * Math.PI));
	}

	public Lathe ( List<Vector3f> points, int steps, float angle ) 
	{
		super();

		List<Vector3f> newV = new ArrayList<Vector3f>();

		Matrix4f matrix = new Matrix4f().makeRotationZ( angle / steps );
		for ( int j = 0; j < points.size(); j ++ ) 
		{
			newV.add( j , points.get( j ).clone());
			this.vertices.add( newV.get( j ) );
		}

		for ( int i = 0; i <= steps; i ++ ) 
		{
			for ( int j = 0; j < newV.size(); j ++ ) 
			{
				newV.set( j , (Vector3f) matrix.multiplyVector3( newV.get( j ).clone() ));
				this.vertices.add( newV.get( j ) );
			}
		}

		for ( int i = 0; i < steps; i ++ ) 
		{
			for ( int k = 0, kl = points.size(); k < kl - 1; k ++ ) 
			{
				int a = i * kl + k;
				int b = ( ( i + 1 ) % (steps + 1) ) * kl + k;
				int c = ( ( i + 1 ) % (steps + 1) ) * kl + ( k + 1 ) % kl;
				int d = i * kl + ( k + 1 ) % kl;

				this.faces.add( new Face4( a, b, c, d ) );

				float stepsf = (float)steps / 1.0f;
				this.faceVertexUvs.get( 0 ).add( Arrays.asList(

					new UVf( (1.0f - i / stepsf),          ((float)k / kl ) ),
					new UVf( (1.0f - ( i + 1.0f ) / stepsf), ((float)k / kl ) ),
					new UVf( (1.0f - ( i + 1.0f ) / stepsf), ((float)( k + 1.0f ) / kl ) ),
					new UVf( (1.0f - i / stepsf),          ((float)( k + 1.0f ) / kl ) )
					
				) );
			}
		}

		this.computeCentroids();
		this.computeFaceNormals(false);
		this.computeVertexNormals();
	}

}
