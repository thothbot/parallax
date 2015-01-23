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

import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;

/**
 * <img src="http://thothbot.github.com/parallax/static/docs/cube.gif" />
 * 
 * <p>
 * Cube geometry
 * 
 * @author thothbot
 *
 */
public final class BoxGeometry extends Geometry 
{
	private int widthSegments;
	private int heightSegments;
	private int depthSegments;
	
	public BoxGeometry() 
	{
		this(100, 100, 100, 1, 1, 1);
	}

	public BoxGeometry( double width, double height, double depth) 
	{
		this(width, height, depth, 1, 1, 1);
	}
	
	public BoxGeometry( double width, double height, double depth, int segmentsWidth, int segmentsHeight, int segmentsDepth) 
	{
		super();
		
		this.widthSegments = segmentsWidth;
		this.heightSegments = segmentsHeight;
		this.depthSegments = segmentsDepth;

		double width_half = width / 2.0;
		double height_half = height / 2.0;
		double depth_half = depth / 2.0;

		buildPlane( "z", "y", - 1, - 1, depth, height, width_half, 0 );   // px 
		buildPlane( "z", "y",   1, - 1, depth, height, - width_half, 1 ); // nx
		buildPlane( "x", "z",   1,   1, width, depth, height_half, 2 );   // py
		buildPlane( "x", "z",   1, - 1, width, depth, - height_half, 3 ); // ny
		buildPlane( "x", "y",   1, - 1, width, height, depth_half, 4 );   // pz
		buildPlane( "x", "y", - 1, - 1, width, height, - depth_half, 5 ); // nz

		this.mergeVertices();
	}
	
	private void buildPlane( String u, String v, int udir, int vdir, double width, double height, double depth, int materialIndex ) 
	{
		int gridX = this.widthSegments;
		int gridY = this.heightSegments;
		double width_half = width / 2.0;
		double height_half = height / 2.0;
		
		int offset = this.getVertices().size();

		String w = "";

		if ( ( u.equals("x") && v.equals("y") ) || ( u.equals("y") && v.equals("x") ) ) 
		{
			w = "z";
		} 
		else if ( ( u.equals("x") && v.equals("z") ) || ( u.equals("z") && v.equals("x") ) ) 
		{
			w = "y";
			gridY = this.depthSegments;
		} 
		else if ( ( u.equals("z") && v.equals("y") ) || ( u.equals("y") && v.equals("z") ) ) 
		{
			w = "x";
			gridX = this.depthSegments;

		}

		int gridX1 = gridX + 1;
		int gridY1 = gridY + 1;
		double segment_width = width / (double)gridX;
		double segment_height = height / (double)gridY;
		Vector3 normal = new Vector3();

		int normalValue = (depth > 0) ? 1 : - 1;
		if(w.equals("x"))
			normal.setX(normalValue);
		else if(w.equals("y"))
			normal.setY(normalValue);
		else if(w.equals("z"))
			normal.setZ(normalValue);

		for ( int iy = 0; iy < gridY1; iy ++ ) 
		{
			for ( int ix = 0; ix < gridX1; ix ++ ) 
			{
				Vector3 vector = new Vector3();
				
				double u1 = (double)( ix * segment_width - width_half ) * udir;
				if(u.equals("x"))
					vector.setX(u1);
				else if(u.equals("y"))
					vector.setY(u1);
				else if(u.equals("z"))
					vector.setZ(u1);

				double v1 = (double)( iy * segment_height - height_half ) * vdir;
				if(v.equals("x"))
					vector.setX(v1);
				else if(v.equals("y"))
					vector.setY(v1);
				else if(v.equals("z"))
					vector.setZ(v1);				

				if(w.equals("x"))
					vector.setX(depth);
				else if(w.equals("y"))
					vector.setY(depth);
				else if(w.equals("z"))
					vector.setZ(depth);	

				getVertices().add( vector );
			}
		}

		for ( int iy = 0; iy < gridY; iy++ ) 
		{
			for ( int ix = 0; ix < gridX; ix++ ) 
			{
				int a = ix + gridX1 * iy;
				int b = ix + gridX1 * ( iy + 1 );
				int c = ( ix + 1 ) + gridX1 * ( iy + 1 );
				int d = ( ix + 1 ) + gridX1 * iy;
				
				Vector2 uva = new Vector2( ix / (double)gridX, 1.0 - iy / (double)gridY );
				Vector2 uvb = new Vector2( ix / (double)gridX, 1.0 - ( iy + 1.0 ) / (double)gridY );
				Vector2 uvc = new Vector2( ( ix + 1.0 ) / (double)gridX, 1.0 - ( iy + 1.0 ) / (double)gridY );
				Vector2 uvd = new Vector2( ( ix + 1.0 ) / (double)gridX, 1.0 - iy / (double)gridY );

				Face3 face = new Face3( a + offset, b + offset, d + offset );
				face.getNormal().copy( normal );
				face.getVertexNormals().addAll( Arrays.asList( normal.clone(), normal.clone(), normal.clone() ) );
				face.setMaterialIndex( materialIndex );

				this.getFaces().add( face );
				this.getFaceVertexUvs().get( 0 ).add( Arrays.asList( uva, uvb, uvd ) );

				face = new Face3( b + offset, c + offset, d + offset );
				face.getNormal().copy( normal );
				face.getVertexNormals().addAll( Arrays.asList( normal.clone(), normal.clone(), normal.clone()) );
				face.setMaterialIndex( materialIndex );

				this.getFaces().add( face );
				this.getFaceVertexUvs().get( 0 ).add( Arrays.asList(  uvb.clone(), uvc, uvd.clone() ) );

			}
		}
	}
}
