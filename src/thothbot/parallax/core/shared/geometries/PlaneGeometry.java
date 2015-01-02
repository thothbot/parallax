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

import thothbot.parallax.core.shared.core.Face4;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;

/**
 * <img src="http://thothbot.github.com/parallax/static/docs/plane.gif" />
 * <p>
 * Plane geometry
 * 
 * @author thothbot
 *
 */
public final class PlaneGeometry extends Geometry
{
	public PlaneGeometry(int width, int height) 
	{
		this(width, height, 1, 1);
	}

	public PlaneGeometry(int width, int height, int segmentsWidth, int segmentsHeight) 
	{
		super();

		double width_half = width / 2.0;
		double height_half = height / 2.0;
		int gridX = segmentsWidth;
		int gridZ = segmentsHeight;
		int gridX1 = gridX + 1;
		int gridZ1 = gridZ + 1;
		double segment_width = width / (double)gridX;
		double segment_height = height / (double)gridZ;

		Vector3 normal = new Vector3( 0, 0, 1 );

		for ( int iz = 0; iz < gridZ1; iz ++ ) 
		{
			for ( int ix = 0; ix < gridX1; ix ++ ) 
			{
				double x = ix * segment_width - width_half;
				double y =  iz * segment_height - height_half;

				getVertices().add( new Vector3( x, -y, 0 ) );
			}
		}

		for ( int iz = 0; iz < gridZ; iz ++ ) 
		{
			for ( int ix = 0; ix < gridX; ix ++ ) 
			{
				int a = ix + gridX1 * iz;
				int b = ix + gridX1 * ( iz + 1 );
				int c = ( ix + 1 ) + gridX1 * ( iz + 1 );
				int d = ( ix + 1 ) + gridX1 * iz;

				Face4 face = new Face4( a, b, c, d );
				face.getNormal().copy( normal );
				face.getVertexNormals().addAll( Arrays.asList(normal.clone(), normal.clone(), normal.clone(), normal.clone()) );

				getFaces().add( face );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList(
					new Vector2( ix / (double)gridX,                     1.0 - iz / (double)gridZ ),
					new Vector2( ix / (double)gridX,           1.0 - ( iz + 1.0 ) / (double)gridZ ),
					new Vector2( ( ix + 1.0 ) / (double)gridX, 1.0 - ( iz + 1.0 ) / (double)gridZ ),
					new Vector2( ( ix + 1.0 ) / (double)gridX,           1.0 - iz / (double)gridZ )
				) );
			}
		}

		this.computeCentroids();
	}
}
