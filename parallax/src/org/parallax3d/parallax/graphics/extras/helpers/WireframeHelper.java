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

package org.parallax3d.parallax.graphics.extras.helpers;

import java.util.Arrays;
import java.util.List;

import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.core.BufferGeometry.DrawCall;
import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint16Array;
import org.parallax3d.parallax.system.gl.arrays.Uint32Array;

@ThreejsObject("THREE.WireframeHelper")
public class WireframeHelper extends Line {

	private static final String POSITION = "position";

	public WireframeHelper(GeometryObject object)
	{
		this(object, new Color(0xffffff));
	}

	public WireframeHelper(GeometryObject object, Color color)
	{
		super(new BufferGeometry(), new LineBasicMaterial(), Line.MODE.PIECES);

		LineBasicMaterial material = (LineBasicMaterial) getMaterial();
		material.setColor( color );

		BufferGeometry geometry = (BufferGeometry) getGeometry();

		int[] edge = new int[]{ 0, 0 };
		FastMap<Boolean> hash = new FastMap<Boolean>();

		if(object.getGeometry() instanceof Geometry)
		{
			List<Vector3> vertices = ((Geometry)object.getGeometry()).getVertices();
			List<Face3> faces = ((Geometry)object.getGeometry()).getFaces();
			int numEdges = 0;

			// allocate maximal size
			Uint32Array edges = Uint32Array.create(6 * faces.size());

			for ( int i = 0, l = faces.size(); i < l; i ++ ) {

				Face3 face = faces.get( i );

				for ( int j = 0; j < 3; j ++ ) {

					edge[ 0 ] = face.getFlat()[j];
					edge[ 1 ] = face.getFlat()[( j + 1 ) % 3 ];
					Arrays.sort(edge);

					String key = Integer.toString(edge[0]) + Integer.toString( edge[1]);

					if ( !hash.containsKey(key) )
					{
						edges.set(2 * numEdges, edge[0]);
						edges.set(2 * numEdges + 1, edge[1]);
						hash.put(key, true);
						numEdges ++;
					}

				}

			}

			Float32Array coords = Float32Array.create(numEdges * 2 * 3);

			for ( int i = 0, l = numEdges; i < l; i ++ ) {

				for ( int j = 0; j < 2; j ++ ) {

					Vector3 vertex = vertices.get( edges.get( 2 * i + j ) );

					int index = 6 * i + 3 * j;
					coords.set(index + 0, vertex.getX());
					coords.set(index + 1, vertex.getY());
					coords.set(index + 2, vertex.getZ());

				}

			}

			geometry.addAttribute( POSITION, new BufferAttribute( coords, 3 ) );



		}
		else if ( object.getGeometry() instanceof BufferGeometry )
		{

			// Indexed BufferGeometry
			if ( ((BufferGeometry)object.getGeometry()).getAttribute("index") != null ) {

				Float32Array vertices = (Float32Array) ((BufferGeometry)object.getGeometry()).getAttribute(POSITION).getArray();
				Uint16Array indices = (Uint16Array) ((BufferGeometry)object.getGeometry()).getAttribute("index").getArray();
				List<DrawCall> drawcalls = ((BufferGeometry)object.getGeometry()).getDrawcalls();
				int numEdges = 0;

				if ( drawcalls.isEmpty() )
				{
					drawcalls = Arrays.asList( new DrawCall(0, indices.getLength(), 9) );
				}

				// allocate maximal size
				Uint32Array edges = Uint32Array.create(2 * indices.getLength());

				for ( int o = 0, ol = drawcalls.size(); o < ol; ++ o ) {

					int start = drawcalls.get( o ).start;
					int count = drawcalls.get( o ).count;
					int index = drawcalls.get( o ).index;

					for ( int i = start, il = start + count; i < il; i += 3 ) {

						for ( int j = 0; j < 3; j ++ ) {

							edge[ 0 ] = index + indices.get( i + j );
							edge[ 1 ] = index + indices.get( i + ( j + 1 ) % 3 );
							Arrays.sort(edge);

							String key = Integer.toString(edge[0]) + Integer.toString(edge[1]);

							if (  !hash.containsKey(key) )
							{
								edges.set(2 * numEdges, edge[0]);
								edges.set(2 * numEdges + 1, edge[1]);
								hash.put(key, true);
								numEdges ++;
							}

						}

					}

				}

				Float32Array coords = Float32Array.create(numEdges * 2 * 3);

				for ( int i = 0, l = numEdges; i < l; i ++ ) {

					for ( int j = 0; j < 2; j ++ ) {

						int index = 6 * i + 3 * j;
						int index2 = 3 * edges.get( 2 * i + j);
						coords.set(index + 0, vertices.get(index2));
						coords.set(index + 1, vertices.get(index2 + 1));
						coords.set(index + 2, vertices.get(index2 + 2));

					}

				}

				geometry.addAttribute( POSITION, new BufferAttribute( coords, 3 ) );

			}
			// non-indexed BufferGeometry
			else
			{

				Float32Array vertices = (Float32Array) ((BufferGeometry)object.getGeometry()).getAttribute(POSITION).getArray();
				int numEdges = vertices.getLength() / 3;
				int numTris = numEdges / 3;

				Float32Array coords = Float32Array.create(numEdges * 2 * 3);

				for ( int i = 0, l = numTris; i < l; i ++ ) {

					for ( int j = 0; j < 3; j ++ ) {

						int index = 18 * i + 6 * j;

						int index1 = 9 * i + 3 * j;
						coords.set(index + 0, vertices.get(index1));
						coords.set(index + 1, vertices.get(index1 + 1));
						coords.set(index + 2, vertices.get(index1 + 2));

						int index2 = 9 * i + 3 * ( ( j + 1 ) % 3 );
						coords.set(index + 3, vertices.get(index2));
						coords.set(index + 4, vertices.get(index2 + 1));
						coords.set(index + 5, vertices.get(index2 + 2));

					}

				}

				geometry.addAttribute( POSITION, new BufferAttribute( coords, 3 ) );

			}

		}

		this.setMatrix( object.getMatrixWorld() );
		this.setMatrixAutoUpdate( false );
	}

}
