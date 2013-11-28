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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thothbot.parallax.core.shared.core.Face4;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;

public final class LatheGeometry extends Geometry
{
	
	public LatheGeometry ( List<Vector3> points) 
	{
		this(points, 12);
	}
	
	public LatheGeometry ( List<Vector3> points, int steps)
	{
		this(points, steps, 2.0 * Math.PI);
	}

	public LatheGeometry ( List<Vector3> points, int steps, double angle ) 
	{
		super();

		List<Vector3> newV = new ArrayList<Vector3>();

		Matrix4 matrix = new Matrix4().makeRotationZ( angle / steps );
		for ( int j = 0; j < points.size(); j ++ ) 
		{
			newV.add( j , points.get( j ).clone());
			getVertices().add( newV.get( j ) );
		}

		for ( int i = 0; i <= steps; i ++ ) 
		{
			for ( int j = 0; j < newV.size(); j ++ ) 
			{
				newV.set( j , (Vector3) matrix.multiplyVector3( newV.get( j ).clone() ));
				getVertices().add( newV.get( j ) );
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

				getFaces().add( new Face4( a, b, c, d ) );

				double stepsf = steps / 1.0;
				getFaceVertexUvs().get( 0 ).add( Arrays.asList(

					new Vector2( (1.0 - i / (double)stepsf),                     k / (double)kl ),
					new Vector2( (1.0 - ( i + 1.0 ) / (double)stepsf),           k / (double)kl ),
					new Vector2( (1.0 - ( i + 1.0 ) / (double)stepsf), ( k + 1.0 ) / (double)kl ),
					new Vector2( (1.0 - i / (double)stepsf),           ( k + 1.0 ) / (double)kl )
					
				) );
			}
		}

		this.computeCentroids();
		this.computeFaceNormals();
		this.computeVertexNormals();
	}

}
