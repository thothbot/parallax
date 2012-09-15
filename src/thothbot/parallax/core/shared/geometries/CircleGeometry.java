/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

import thothbot.parallax.core.shared.core.BoundingSphere;
import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.UV;
import thothbot.parallax.core.shared.core.Vector3;


public final class CircleGeometry extends Geometry 
{
	public CircleGeometry()
	{
		this(50, 8);
	}
	
	public CircleGeometry(double radius, int segments)
	{
		this(radius, segments, 0, Math.PI * 2.0);
	}

	public CircleGeometry(double radius, int segments, double thetaStart, double thetaLength)
	{
	    segments = Math.max( 3, segments );

   		Vector3 center = new Vector3();
	    UV centerUV = new UV( 0.5, 0.5 );
	    List<UV> uvs = new ArrayList<UV>();

	    this.getVertices().add(center);
	    uvs.add( centerUV );

	    for ( int i = 0; i <= segments; i ++ ) 
	    {
	    	Vector3 vertex = new Vector3();

	        vertex.setX(radius * Math.cos( thetaStart + (double)i / segments * thetaLength ));
	        vertex.setY(radius * Math.sin( thetaStart + (double)i / segments * thetaLength ));

	        this.getVertices().add( vertex );
	        uvs.add( new UV( ( vertex.getX() / radius + 1.0 ) / 2.0, - ( vertex.getY() / radius + 1.0 ) / 2.0 + 1.0 ) );
	    }

	    Vector3 n = new Vector3( 0, 0, -1 );

	    for ( int i = 1; i <= segments; i ++ ) 
	    {
	        int v1 = i;
	        int v2 = i + 1 ;
	        int v3 = 0;

	        this.getFaces().add( new Face3( v1, v2, v3, Arrays.asList( n, n, n ) ) );
	        this.getFaceVertexUvs().get( 0 ).add( Arrays.asList( uvs.get( i ), uvs.get( i + 1 ), centerUV ) );
	    }

	    this.computeCentroids();
	    this.computeFaceNormals();

	    setBoundingSphere( new BoundingSphere(radius) );
	}
}
