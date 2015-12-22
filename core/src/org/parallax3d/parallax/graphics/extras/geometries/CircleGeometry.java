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

package org.parallax3d.parallax.graphics.extras.geometries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.parallax3d.parallax.system.ThreeJsObject;
import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.math.Sphere;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;

/**
 * <img src="http://thothbot.github.com/parallax/static/docs/circle.gif" />
 * 
 * <p>
 * Circle geometry
 * 
 * @author thothbot
 *
 */
@ThreeJsObject("THREE.CircleGeometry")
public final class CircleGeometry extends Geometry
{
	public CircleGeometry()
	{
		this(50, 8);
	}
	
	public CircleGeometry(float radius, int segments)
	{
		this(radius, segments, 0, (float)(Math.PI * 2.0));
	}

	public CircleGeometry(float radius, int segments, float thetaStart, float thetaLength)
	{
	    segments = Math.max( 3, segments );

   		Vector3 center = new Vector3();
   		Vector2 centerUV = new Vector2( 0.5f, 0.5f );
	    List<Vector2> uvs = new ArrayList<Vector2>();

	    this.getVertices().add(center);
	    uvs.add( centerUV );

	    for ( int i = 0; i <= segments; i ++ ) 
	    {
	    	Vector3 vertex = new Vector3();

	        vertex.setX(radius * (float)Math.cos( thetaStart + (float)i / segments * thetaLength ));
	        vertex.setY(radius * (float)Math.sin( thetaStart + (float)i / segments * thetaLength ));

	        this.getVertices().add( vertex );
	        uvs.add( new Vector2( ( vertex.getX() / radius + 1.0f ) / 2.0f, - ( vertex.getY() / radius + 1.0f ) / 2.0f + 1.0f ) );
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

	    this.computeFaceNormals();

	    setBoundingSphere( new Sphere(radius) );
	}
}