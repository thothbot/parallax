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

import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;

/**
 * <img src="http://thothbot.github.com/parallax/static/docs/torus.gif" />
 * <p>
 * Torus geometry
 * 
 * @author thothbot
 *
 */
public final class TorusGeometry extends Geometry
{
	
	public TorusGeometry() 
	{
		this(100, 40, 8, 6);
	}
	
	public TorusGeometry(double radius, double tube, int segmentsR, int segmentsT) 
	{
		this(radius, tube, segmentsR, segmentsT, Math.PI * 2.0);
	}
	
	public TorusGeometry(double radius, double tube, int segmentsR, int segmentsT, double arc) 
	{
		super();
		
		Vector3 center = new Vector3();
		List<Vector2> uvs = new ArrayList<Vector2>();
		List<Vector3> normals = new ArrayList<Vector3>();
		
		for ( int j = 0; j <= segmentsR; j ++ ) 
		{
			for ( int i = 0; i <= segmentsT; i ++ ) 
			{
				double u = i / (double)segmentsT * arc;
				double v = j / (double)segmentsR * Math.PI * 2.0;

				center.setX(radius * Math.cos( u ));
				center.setY(radius * Math.sin( u ));

				Vector3 vertex = new Vector3();
				vertex.setX(( radius + tube * Math.cos( v ) ) * Math.cos( u ));
				vertex.setY(( radius + tube * Math.cos( v ) ) * Math.sin( u ));
				vertex.setZ(tube * Math.sin( v ));

				getVertices().add( vertex );

				uvs.add( new Vector2( i / (double)segmentsT, j / (double)segmentsR ) );
				normals.add( vertex.clone().sub( center ).normalize() );
			}
		}

		for ( int j = 1; j <= segmentsR; j ++ ) 
		{
			for ( int i = 1; i <= segmentsT; i ++ ) 
			{
				int a = ( segmentsT + 1 ) * j + i - 1;
				int b = ( segmentsT + 1 ) * ( j - 1 ) + i - 1;
				int c = ( segmentsT + 1 ) * ( j - 1 ) + i;
				int d = ( segmentsT + 1 ) * j + i;

				Face3 face = new Face3( a, b, d, Arrays.asList( normals.get( a ).clone(), normals.get( b ).clone(), normals.get( d ).clone() ) );
				getFaces().add( face );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList( uvs.get( a ).clone(), uvs.get( b ).clone(), uvs.get( d ).clone() ) );

				face = new Face3( b, c, d, Arrays.asList( normals.get( b ).clone(), normals.get( c ).clone(), normals.get( d ).clone() ) );
				getFaces().add( face );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList( uvs.get( b ).clone(), uvs.get( c ).clone(), uvs.get( d ).clone() ) );
			}

		}

		this.computeFaceNormals();
	}
}
