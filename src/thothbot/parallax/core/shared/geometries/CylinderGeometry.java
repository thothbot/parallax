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
 * <img src="http://thothbot.github.com/parallax/static/docs/cylinder.gif" />
 * <p>
 * Cylinder geometry
 * 
 * @author thothbot
 *
 */
public final class CylinderGeometry extends Geometry
{

	public CylinderGeometry() 
	{
		this(20, 20, 100, 8, 1);
	}
	
	public CylinderGeometry(double radiusTop, double radiusBottom, double height, int segmentsRadius, int segmentsHeight)
	{
		this(radiusTop, radiusBottom, height, segmentsRadius, segmentsHeight, false, 0, Math.PI * 2);
	}

	public CylinderGeometry(double radiusTop, double radiusBottom, double height, int radialSegments, int heightSegments, boolean openEnded, double thetaStart, double thetaLength) 
	{
		super();

		double heightHalf = height / 2.0;
//		int segmentsX = radialSegments;
//		int segmentsY = heightSegments;

		int x = 0, y = 0; 
		
		List<List<Integer>> vertices = new ArrayList<List<Integer>>();
		List<List<Vector2>> uvs = new ArrayList<List<Vector2>>();

		for ( y = 0; y <= heightSegments; y ++ ) 
		{
			List<Integer> verticesRow = new ArrayList<Integer>();
			List<Vector2> uvsRow = new ArrayList<Vector2>();

			double v = (double)y / (double)heightSegments;
			double radius = v * ( radiusBottom - radiusTop ) + radiusTop;

			for ( x = 0; x <= radialSegments; x ++ ) 
			{
				double u = (double)x / (double)radialSegments;

				Vector3 vertex = new Vector3();
				
				vertex.setX(radius * Math.sin( u * thetaLength + thetaStart ));
				vertex.setY(- v * height + heightHalf);
				vertex.setZ(radius * Math.cos( u * thetaLength + thetaStart ));

				getVertices().add( vertex );

				verticesRow.add( getVertices().size() - 1 );
				uvsRow.add( new Vector2( u, 1 - v ) );

			}

			vertices.add( verticesRow );
			uvs.add( uvsRow );
		}

		double tanTheta = ( radiusBottom - radiusTop ) / height;
		Vector3 na, nb;

		for ( x = 0; x < radialSegments; x ++ ) 
		{
			if ( radiusTop != 0 ) 
			{
				na = getVertices().get( vertices.get( 0 ).get( x ) ).clone();
				nb = getVertices().get( vertices.get( 0 ).get( x + 1 ) ).clone();
			} 
			else 
			{
				na = getVertices().get( vertices.get( 1 ).get( x ) ).clone();
				nb = getVertices().get( vertices.get( 1 ).get( x + 1 ) ).clone();
			}

			na.setY( Math.sqrt( na.getX() * na.getX() + na.getZ() * na.getZ() ) * tanTheta );
			na.normalize();
			nb.setY( Math.sqrt( nb.getX() * nb.getX() + nb.getZ() * nb.getZ() ) * tanTheta );
			nb.normalize();

			for ( y = 0; y < heightSegments; y ++ ) 
			{
				int v1 = vertices.get( y ).get( x );
				int v2 = vertices.get( y + 1 ).get( x );
				int v3 = vertices.get( y + 1 ).get( x + 1 );
				int v4 = vertices.get( y ).get( x + 1 );

				Vector3 n1 = na.clone();
				Vector3 n2 = na.clone();
				Vector3 n3 = nb.clone();
				Vector3 n4 = nb.clone();

				Vector2 uv1 = uvs.get( y ).get( x ).clone();
				Vector2 uv2 = uvs.get( y + 1 ).get( x ).clone();
				Vector2 uv3 = uvs.get( y + 1 ).get( x + 1 ).clone();
				Vector2 uv4 = uvs.get( y ).get( x + 1 ).clone();
				
				this.getFaces().add( new Face3( v1, v2, v4, Arrays.asList( n1, n2, n4 ) ) );
				this.getFaceVertexUvs().get( 0 ).add( Arrays.asList( uv1, uv2, uv4 ) );

				this.getFaces().add( new Face3( v2, v3, v4, Arrays.asList(  n2.clone(), n3, n4.clone() ) ) );
				this.getFaceVertexUvs().get( 0 ).add( Arrays.asList( uv2.clone(), uv3, uv4.clone() ) );


			}
		}

		// top cap

		if ( !openEnded && radiusTop > 0 ) 
		{
			getVertices().add( new Vector3( 0.0, heightHalf, 0.0 ) );

			for ( x = 0; x < radialSegments; x ++ ) 
			{
				int v1 = vertices.get( 0 ).get( x );
				int v2 = vertices.get( 0 ).get( x + 1 );
				int v3 = getVertices().size() - 1;

				Vector3 n1 = new Vector3( 0, 1, 0 );
				Vector3 n2 = new Vector3( 0, 1, 0 );
				Vector3 n3 = new Vector3( 0, 1, 0 );

				Vector2 uv1 = uvs.get( 0 ).get( x ).clone();
				Vector2 uv2 = uvs.get( 0 ).get( x + 1 ).clone();
				Vector2 uv3 = new Vector2( uv2.getX(), 0 );

				getFaces().add( new Face3( v1, v2, v3, Arrays.asList( n1, n2, n3 ) ) );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList( uv1, uv2, uv3 ) );
			}
		}

		// bottom cap

		if ( !openEnded && radiusBottom > 0 ) 
		{
			getVertices().add( new Vector3( 0, - heightHalf, 0 ) );

			for ( x = 0; x < radialSegments; x ++ ) 
			{
				int v1 = vertices.get( y ).get( x + 1 );
				int v2 = vertices.get( y ).get( x );
				int v3 = getVertices().size() - 1;

				Vector3 n1 = new Vector3( 0.0, - 1.0, 0.0 );
				Vector3 n2 = new Vector3( 0.0, - 1.0, 0.0 );
				Vector3 n3 = new Vector3( 0.0, - 1.0, 0.0 );

				Vector2 uv1 = uvs.get( y ).get( x + 1 ).clone();
				Vector2 uv2 = uvs.get( y ).get( x ).clone();
				Vector2 uv3 = new Vector2( uv2.getX(), 1 );

				getFaces().add( new Face3( v1, v2, v3, Arrays.asList( n1, n2, n3 ) ) );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList( uv1, uv2, uv3 ) );
			}
		}

		computeFaceNormals();
	}
}
