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

import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.math.Vector3;

public class AsteriskGeometry extends Geometry 
{

	public AsteriskGeometry(double innerRadius, double outerRadius )
	{
		double sd = innerRadius;
		double ed = outerRadius;

		double sd2 = 0.707 * sd;
		double ed2 = 0.707 * ed;

		double[][] rays = { { sd, 0, 0 }, { ed, 0, 0 }, { -sd, 0, 0 }, { -ed, 0, 0 },
					 { 0, sd, 0 }, { 0, ed, 0 }, { 0, -sd, 0 }, { 0, -ed, 0 },
					 { 0, 0, sd }, { 0, 0, ed }, { 0, 0, -sd }, { 0, 0, -ed },
					 { sd2, sd2, 0 }, { ed2, ed2, 0 }, { -sd2, -sd2, 0 }, { -ed2, -ed2, 0 },
					 { sd2, -sd2, 0 }, { ed2, -ed2, 0 }, { -sd2, sd2, 0 }, { -ed2, ed2, 0 },
					 { sd2, 0, sd2 }, { ed2, 0, ed2 }, { -sd2, 0, -sd2 }, { -ed2, 0, -ed2 },
					 { sd2, 0, -sd2 }, { ed2, 0, -ed2 }, { -sd2, 0, sd2 }, { -ed2, 0, ed2 },
					 { 0, sd2, sd2 }, { 0, ed2, ed2 }, { 0, -sd2, -sd2 }, { 0, -ed2, -ed2 },
					 { 0, sd2, -sd2 }, { 0, ed2, -ed2 }, { 0, -sd2, sd2 }, { 0, -ed2, ed2 }
		};

		for ( int i = 0, il = rays.length; i < il; i ++ ) 
		{
			double x = rays[ i ][ 0 ];
			double y = rays[ i ][ 1 ];
			double z = rays[ i ][ 2 ];

			this.getVertices().add( new Vector3( x, y, z ) );
		}
	}
}
