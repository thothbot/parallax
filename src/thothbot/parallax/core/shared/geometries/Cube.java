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
import thothbot.parallax.core.shared.core.UVf;
import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.materials.Material;

/**
 * The Cube geometry
 * <p>
 * <img src="http://thothbot.github.com/parallax/static/docs/cube.gif" />
 * <p>
 * Based on the three.js code.
 * 
 * @author thothbot
 *
 */
public final class Cube extends Geometry 
{
	public class Sides
	{
		public boolean px = true;
		public boolean nx = true;
		public boolean py = true;
		public boolean ny = true;
		public boolean pz = true;
		public boolean nz = true;
	}
	
	private Sides sides;

	private int segmentsWidth;
	private int segmentsHeight;
	private int segmentsDepth;
	
	public Cube() 
	{
		this(100f, 100f, 100f, 1, 1, 1);
	}

	public Cube( float width, float height, float depth) 
	{
		this(width, height, depth, 1, 1, 1);
	}
	
	public Cube( float width, float height, float depth, int segmentsWidth, int segmentsHeight, int segmentsDepth)
	{
		this(width, height, depth, segmentsWidth, segmentsHeight, segmentsDepth, null, null);
	}

	public Cube( float width, float height, float depth, int segmentsWidth, int segmentsHeight, int segmentsDepth, 
			List<Material> materials, Sides sides) 
	{
		super();
		
		this.segmentsWidth = segmentsWidth;
		this.segmentsHeight = segmentsHeight;
		this.segmentsDepth = segmentsDepth;

		float width_half = width / 2.0f;
		float height_half = height / 2.0f;
		float depth_half = depth / 2.0f;

		int mpx = 0;
		int mpy = 0;
		int mpz = 0;
		int mnx = 0;
		int mny = 0;
		int mnz = 0;

		setMaterials( new ArrayList<Material>() );
		
		if ( materials != null ) 
		{
			setMaterials( materials );
			mpx = 0; mnx = 1; mpy = 2; mny = 3; mpz = 4; mnz = 5;
		}

		if ( sides != null )
			this.sides = sides;
		else
			this.sides = new Sides();

		if(this.sides.px) 
			buildPlane( "z", "y", - 1, - 1, depth, height, width_half, mpx );
		if(this.sides.nx) 
			buildPlane( "z", "y",   1, - 1, depth, height, - width_half, mnx );
		if(this.sides.py) 
			buildPlane( "x", "z",   1,   1, width, depth, height_half, mpy );
		if(this.sides.ny) 
			buildPlane( "x", "z",   1, - 1, width, depth, - height_half, mny );
		if(this.sides.pz) 
			buildPlane( "x", "y",   1, - 1, width, height, depth_half, mpz );
		if(this.sides.nz) 
			buildPlane( "x", "y", - 1, - 1, width, height, - depth_half, mnz );

		this.computeCentroids();
		this.mergeVertices();
	}
	
	private void buildPlane( String u, String v, int udir, int vdir, float width, float height, float depth, int material ) 
	{
		int gridX = this.segmentsWidth;
		int gridY = this.segmentsHeight;
		float width_half = width / 2.0f;
		float height_half = height / 2.0f;
		
		int offset = getVertices().size();

		String w = "";

		if ( ( u.equals("x") && v.equals("y") ) || ( u.equals("y") && v.equals("x") ) ) {

			w = "z";

		} else if ( ( u.equals("x") && v.equals("z") ) || ( u.equals("z") && v.equals("x") ) ) {

			w = "y";
			gridY = this.segmentsDepth;

		} else if ( ( u.equals("z") && v.equals("y") ) || ( u.equals("y") && v.equals("z") ) ) {

			w = "x";
			gridX = this.segmentsDepth;

		}

		int gridX1 = gridX + 1;
		int gridY1 = gridY + 1;
		float segment_width = width / (float)gridX;
		float segment_height = height / (float)gridY;
		Vector3f normal = new Vector3f();

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
				Vector3f vector = new Vector3f();
				
				float u1 = ((float)( ix * segment_width - width_half ) * udir);
				if(u.equals("x"))
					vector.setX(u1);
				else if(u.equals("y"))
					vector.setY(u1);
				else if(u.equals("z"))
					vector.setZ(u1);

				float v1 = ((float)( iy * segment_height - height_half ) * vdir);
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
				
				Face4 face = new Face4( a + offset, b + offset, c + offset, d + offset );
				face.getNormal().copy( normal );
				face.setVertexNormals(Arrays.asList(normal.clone(), normal.clone(), normal.clone(), normal.clone()));
				face.setMaterialIndex(material);

				getFaces().add( face );
				getFaceVertexUvs().get( 0 ).add( Arrays.asList(
					new UVf( ((float)ix / gridX), ((float)iy / gridY) ),
					new UVf( ((float)ix / gridX), ((float)( iy + 1 ) / gridY) ),
					new UVf( ((float)( ix + 1 ) / gridX), ((float)( iy + 1 ) / gridY) ),
					new UVf( ((float)( ix + 1 ) / gridX), ((float)iy / gridY) )
				) );
			}
		}
	}
}
