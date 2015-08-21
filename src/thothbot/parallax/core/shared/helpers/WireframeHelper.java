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

package thothbot.parallax.core.shared.helpers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Uint16Array;
import thothbot.parallax.core.client.gl2.arrays.Uint32Array;
import thothbot.parallax.core.shared.core.BufferAttribute;
import thothbot.parallax.core.shared.core.BufferGeometry;
import thothbot.parallax.core.shared.core.BufferGeometry.DrawCall;
import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.FastMap;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryObject;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.objects.Line;

import com.google.gwt.core.client.GWT;

public class WireframeHelper extends Line {
	
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
		Map<String, Boolean> hash = GWT.isScript() ? 
				new FastMap<Boolean>() : new HashMap<String, Boolean>();

		if(object.getGeometry() instanceof Geometry) 
		{
			List<Vector3> vertices = ((Geometry)object.getGeometry()).getVertices();
			List<Face3> faces = ((Geometry)object.getGeometry()).getFaces();
			int numEdges = 0;
			
			// allocate maximal size
			Uint32Array edges = Uint32Array.create( 6 * faces.size() );
		
			for ( int i = 0, l = faces.size(); i < l; i ++ ) {

				Face3 face = faces.get( i );

				for ( int j = 0; j < 3; j ++ ) {

					edge[ 0 ] = face.getFlat()[j];
					edge[ 1 ] = face.getFlat()[( j + 1 ) % 3 ];
					Arrays.sort(edge);

					String key = "" + edge[0] + edge[1];

					if ( !hash.containsKey(key) ) 
					{
						edges.set( 2 * numEdges, edge[ 0 ]);
						edges.set( 2 * numEdges + 1, edge[ 1 ]);
						hash.put(key, true);
						numEdges ++;
					}

				}

			}
			
			Float32Array coords = Float32Array.create( numEdges * 2 * 3 );

			for ( int i = 0, l = numEdges; i < l; i ++ ) {

				for ( int j = 0; j < 2; j ++ ) {

					Vector3 vertex = vertices.get( edges.get( 2 * i + j ) );

					int index = 6 * i + 3 * j;
					coords.set( index + 0 , vertex.getX());
					coords.set( index + 1 , vertex.getY());
					coords.set( index + 2 , vertex.getZ());

				}

			}

			geometry.addAttribute( "position", new BufferAttribute( coords, 3 ) );



		} 
		else if ( object.getGeometry() instanceof BufferGeometry ) 
		{

			 // Indexed BufferGeometry
			if ( ((BufferGeometry)object.getGeometry()).getAttribute("index") != null ) {

				Float32Array vertices = (Float32Array) ((BufferGeometry)object.getGeometry()).getAttribute("position").getArray();
				Uint16Array indices = (Uint16Array) ((BufferGeometry)object.getGeometry()).getAttribute("index").getArray();
				List<DrawCall> drawcalls = ((BufferGeometry)object.getGeometry()).getDrawcalls();
				int numEdges = 0;

				if ( drawcalls.size() == 0 ) 
				{
					drawcalls = Arrays.asList( new DrawCall(0, indices.getLength(), 9) );
				}

				// allocate maximal size
				Uint32Array edges = Uint32Array.create( 2 * indices.getLength() );

				for ( int o = 0, ol = drawcalls.size(); o < ol; ++ o ) {

					int start = drawcalls.get( o ).start;
					int count = drawcalls.get( o ).count;
					int index = drawcalls.get( o ).index;

					for ( int i = start, il = start + count; i < il; i += 3 ) {

						for ( int j = 0; j < 3; j ++ ) {

							edge[ 0 ] = index + indices.get( i + j );
							edge[ 1 ] = index + indices.get( i + ( j + 1 ) % 3 );
							Arrays.sort(edge);

							String key = "" + edge[0] + edge[1];

							if (  !hash.containsKey(key) ) 
							{
								edges.set( 2 * numEdges, edge[ 0 ]);
								edges.set( 2 * numEdges + 1, edge[ 1 ]);
								hash.put(key, true);
								numEdges ++;
							}

						}

					}

				}

				Float32Array coords = Float32Array.create( numEdges * 2 * 3 );

				for ( int i = 0, l = numEdges; i < l; i ++ ) {

					for ( int j = 0; j < 2; j ++ ) {

						int index = 6 * i + 3 * j;
						int index2 = 3 * edges.get( 2 * i + j);
						coords.set( index + 0 , vertices.get( index2 ) );
						coords.set( index + 1 , vertices.get( index2 + 1 ) );
						coords.set( index + 2 , vertices.get( index2 + 2 ) );

					}

				}

				geometry.addAttribute( "position", new BufferAttribute( coords, 3 ) );

			}
			// non-indexed BufferGeometry
			else 
			{

				Float32Array vertices = (Float32Array) ((BufferGeometry)object.getGeometry()).getAttribute("position").getArray();
				int numEdges = vertices.getLength() / 3;
				int numTris = numEdges / 3;

				Float32Array coords = Float32Array.create( numEdges * 2 * 3 );

				for ( int i = 0, l = numTris; i < l; i ++ ) {

					for ( int j = 0; j < 3; j ++ ) {

						int index = 18 * i + 6 * j;

						int index1 = 9 * i + 3 * j;
						coords.set( index + 0 , vertices.get( index1 ) );
						coords.set( index + 1 , vertices.get( index1 + 1 ) );
						coords.set( index + 2 , vertices.get( index1 + 2 ) );

						int index2 = 9 * i + 3 * ( ( j + 1 ) % 3 );
						coords.set( index + 3 , vertices.get( index2 ) );
						coords.set( index + 4 , vertices.get( index2 + 1 ) );
						coords.set( index + 5 , vertices.get( index2 + 2 ) );

					}

				}

				geometry.addAttribute( "position", new BufferAttribute( coords, 3 ) );

			}

		}

		this.setMatrix( object.getMatrixWorld() );
		this.setMatrixAutoUpdate( false );
	}

}
