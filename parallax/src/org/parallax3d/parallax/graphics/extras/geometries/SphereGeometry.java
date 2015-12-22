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
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.math.Sphere;
import org.parallax3d.parallax.math.Vector2;

/**
 * <img src="http://thothbot.github.com/parallax/static/docs/sphere.gif" />
 * <p>
 * The Sphere geometry
 * 
 * @author thothbot
 *
 */
@ThreeJsObject("THREE.SphereGeometry")
public final class SphereGeometry extends Geometry
{
	public SphereGeometry() 
	{
		this(50);
	}

	public SphereGeometry(float radius) 
	{
		this(radius, 8, 6);
	}
	
	public SphereGeometry(float radius, int segmentsWidth, int segmentsHeight) 
	{
		this(radius, segmentsWidth, segmentsHeight, 0.0f, (float)(Math.PI * 2.0) );
	}
	
	public SphereGeometry(float radius, int segmentsWidth, int segmentsHeight, float phiStart, float phiLength)
	{
		this(radius, segmentsWidth, segmentsHeight, phiStart, phiLength, 0.0f, (float)Math.PI);
	}

	public SphereGeometry(float radius, int widthSegments, int heightSegments, float phiStart, float phiLength, float thetaStart, float thetaLength) 
	{
		super();
		
		List<List<Integer>> vertices = new ArrayList<List<Integer>>();
		List<List<Vector2>> uvs = new ArrayList<List<Vector2>>();
		
		for (int y = 0; y <= heightSegments; y++) 
		{
			List<Integer> verticesRow = new ArrayList<Integer>();
			List<Vector2> uvsRow = new ArrayList<Vector2>();
			
			for (int x = 0; x <= widthSegments; x++) 
			{

				float u = x / (float)widthSegments;
				float v = y / (float)heightSegments;

				Vector3 vertex = new Vector3();
				vertex.setX(- radius * (float)Math.cos( phiStart + u * phiLength ) * (float)Math.sin( thetaStart + v * thetaLength ));
				vertex.setY(radius * (float)Math.cos( thetaStart + v * thetaLength ));
				vertex.setZ(radius * (float)Math.sin( phiStart + u * phiLength ) * (float)Math.sin( thetaStart + v * thetaLength ));
				
				getVertices().add( vertex );

				verticesRow.add( getVertices().size() - 1 );
				uvsRow.add( new Vector2( u, 1- v ) );
			}
			
			vertices.add( verticesRow );
			uvs.add( uvsRow );
		}

		for ( int y = 0; y < heightSegments; y ++ ) 
		{
			for ( int x = 0; x < widthSegments; x ++ ) 
			{
				int v1 = vertices.get( y ).get( x + 1 );
				int v2 = vertices.get( y ).get( x );
				int v3 = vertices.get( y + 1 ).get( x );
				int v4 = vertices.get( y + 1 ).get( x + 1 );

				Vector3 n1 = getVertices().get( v1 ).clone().normalize();
				Vector3 n2 = getVertices().get( v2 ).clone().normalize();
				Vector3 n3 = getVertices().get( v3 ).clone().normalize();
				Vector3 n4 = getVertices().get( v4 ).clone().normalize();

				Vector2 uv1 = uvs.get( y ).get( x + 1 ).clone();
				Vector2 uv2 = uvs.get( y ).get( x ).clone();
				Vector2 uv3 = uvs.get( y + 1 ).get( x ).clone();
				Vector2 uv4 = uvs.get( y + 1 ).get( x + 1 ).clone();

				if ( Math.abs( getVertices().get( v1 ).getY() ) == radius ) 
				{
					uv1.setX( ( uv1.getX() + uv2.getX() ) / 2.0f );
					getFaces().add( new Face3( v1, v3, v4, Arrays.asList( n1, n3, n4 ) ) );
					getFaceVertexUvs().get( 0 ).add( Arrays.asList( uv1, uv3, uv4 ) );
				} 
				else if ( Math.abs( getVertices().get( v3 ).getY() ) ==  radius ) 
				{
					uv3.setX( ( uv3.getX() + uv4.getX() ) / 2.0f );
					getFaces().add( new Face3( v1, v2, v3, Arrays.asList( n1, n2, n3 ) ) );
					getFaceVertexUvs().get( 0 ).add( Arrays.asList( uv1, uv2, uv3 ) );

				} 
				else 
				{
					getFaces().add( new Face3( v1, v2, v4,  Arrays.asList( n1, n2, n4 ) ) );
					getFaceVertexUvs().get( 0 ).add(  Arrays.asList( uv1, uv2, uv4 ) );

					getFaces().add( new Face3( v2, v3, v4,  Arrays.asList( n2.clone(), n3, n4.clone() ) ) );
					getFaceVertexUvs().get( 0 ).add(  Arrays.asList( uv2.clone(), uv3, uv4.clone() ) );

				}
			}
		}

		this.computeFaceNormals();

		setBoundingSphere( new Sphere(new Vector3(), radius) );
	}
}
