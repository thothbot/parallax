/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.geometries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thothbot.squirrel.core.shared.core.Face4;
import thothbot.squirrel.core.shared.core.Geometry;
import thothbot.squirrel.core.shared.core.UVf;
import thothbot.squirrel.core.shared.core.Vector3f;
import thothbot.squirrel.core.shared.curves.CurvePath;
import thothbot.squirrel.core.shared.curves.FrenetFrames;
import thothbot.squirrel.core.shared.helpers.ArrowHelper;
import thothbot.squirrel.core.shared.objects.Object3D;

/**
 * @author WestLangley / https://github.com/WestLangley
 * @author zz85 / https://github.com/zz85
 * @author miningold / https://github.com/miningold
 *
 * Modified from the TorusKnotGeometry by @oosmoxiecode
 *
 * Creates a tube which extrudes along a 3d spline
 *
 * Uses parallel transport frames as described in
 * http://www.cs.indiana.edu/pub/techreports/TR425.pdf
 */
public final class Tube extends Geometry
{
	private List<Vector3f> tangents;
	private List<Vector3f> normals;
	private List<Vector3f> binormals;
	
	private List<List<Integer>> grid;
	
	public Tube( CurvePath path )
	{
		this(path, 64, 1.0f, 8, false, true);
	}
	
	public Tube( CurvePath path, int segments, float radius, int segmentsRadius, boolean closed, boolean debug ) 
	{
		super();
		
		Object3D debugObject = new Object3D();

		this.grid = new ArrayList<List<Integer>>();

		int numpoints = segments + 1;

		FrenetFrames frames = new FrenetFrames(path, segments, closed);
		
		// proxy internals
		this.tangents = frames.getTangents();
		this.normals = frames.getNormals();
		this.binormals = frames.getBinormals();

		// consruct the grid

		for ( int i = 0; i < numpoints; i++ ) 
		{
			this.grid.add( i, new ArrayList<Integer>());

			float u = (float)i / ( numpoints - 1 );

			Vector3f pos = (Vector3f) path.getPointAt( u );

			if ( debug ) 
			{
				debugObject.addChild(new ArrowHelper(tangents.get( i ), pos, radius, 0x0000ff));	
				debugObject.addChild(new ArrowHelper(normals.get( i ), pos, radius, 0xff0000));
				debugObject.addChild(new ArrowHelper(binormals.get( i ), pos, radius, 0x00ff00));
			}

			for ( int j = 0; j < segmentsRadius; j++ ) 
			{
				double v = j / segmentsRadius * 2.0 * Math.PI;

				// TODO: Hack: Negating it so it faces outside.
				float cx = (float) ( - radius * Math.cos( v ) ); 
				float cy = (float) ( radius * Math.sin( v ) );

				Vector3f pos2 = new Vector3f();
	            pos2.copy( pos );
	            pos2.addX(cx * normals.get(i).getX() + cy * binormals.get(i).getX());
	            pos2.addY(cx * normals.get(i).getY() + cy * binormals.get(i).getY());
	            pos2.addZ(cx * normals.get(i).getZ() + cy * binormals.get(i).getZ());

	            this.grid.get( i ).add( j, vert( pos2.getX(), pos2.getY(), pos2.getZ() ));
			}
		}


		// construct the mesh

		for ( int i = 0; i < segments; i++ ) 
		{
			for ( int j = 0; j < segmentsRadius; j++ ) 
			{
				int ip = ( closed ) ? (i + 1) % segments : i + 1;
				int jp = (j + 1) % segmentsRadius;

				int a = this.grid.get( i ).get( j );		// *** NOT NECESSARILY PLANAR ! ***
				int b = this.grid.get( ip ).get( j );
				int c = this.grid.get( ip ).get( jp );
				int d = this.grid.get( i ).get( jp );

				UVf uva = new UVf( i / (float)segments,         j / (float)segmentsRadius );
				UVf uvb = new UVf( ( i + 1 ) / (float)segments, j / (float)segmentsRadius );
				UVf uvc = new UVf( ( i + 1 ) / (float)segments, ( j + 1 ) / (float)segmentsRadius );
				UVf uvd = new UVf( i / (float)segments,         ( j + 1 ) / (float)segmentsRadius );

				this.faces.add( new Face4( a, b, c, d ) );
				this.faceVertexUvs.get( 0 ).add( Arrays.asList( uva, uvb, uvc, uvd ) );

			}
		}

		this.computeCentroids();
		this.computeFaceNormals(false);
		this.computeVertexNormals();
	}
	
	private int vert( float x, float y, float z ) 
	{
		this.getVertices().add( new Vector3f( x, y, z ) );
		
		return  this.getVertices().size() - 1;
	}
}
