/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
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

import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EdgesGeometry extends BufferGeometry {

    public EdgesGeometry(AbstractGeometry geometry) {
        this(geometry, 1.);
    }

    public EdgesGeometry(AbstractGeometry geometry, double thresholdAngle) {
        double thresholdDot = Math.cos( Mathematics.degToRad( thresholdAngle ) );

        int[] edge = new int[]{ 0, 0 };
        class Hash {
            public int vert1;
            public int vert2;
            public int face1;
            public int face2;

            public Hash(int vert1, int vert2, int face1, int face2) {
                this.vert1 = vert1;
                this.vert2 = vert2;
                this.face1 = face1;
                this.face2 = face2;
            }
        }
        FastMap<Hash> hash = new FastMap();

        Geometry geometry2;

        if ( geometry instanceof BufferGeometry ) {

            geometry2 = new Geometry();
            geometry2.fromBufferGeometry((BufferGeometry) geometry);

        } else {

            geometry2 = ((Geometry) geometry).clone();

        }

        geometry2.mergeVertices();
        geometry2.computeFaceNormals();

        List<Vector3> vertices = geometry2.getVertices();
        List<Face3> faces = geometry2.getFaces();

        for ( int i = 0, l = faces.size(); i < l; i ++ ) {

            Face3 face = faces.get(i);

            for ( int j = 0; j < 3; j ++ ) {

                edge[ 0 ] = face.getFlat()[ j ];
                edge[ 1 ] = face.getFlat()[ ( j + 1 ) % 3  ];
                Arrays.sort(edge);

                String key = edge.toString();

                if ( !hash.containsKey( key )) {

                    hash.put(key, new Hash( edge[ 0 ],edge[ 1 ], i, -1));

                } else {

                    hash.get( key ).face2 = i;

                }

            }

        }

        List<Double> coords = new ArrayList<>();

        for (String key : hash.keySet() ) {

            Hash h = hash.get( key );

            if ( h.face2 == -1 || faces.get(h.face1).getNormal().dot(faces.get(h.face2).getNormal()) <= thresholdDot ) {

                Vector3 vertex = vertices.get(h.vert1);
                coords.add(vertex.getX());
                coords.add(vertex.getY());
                coords.add(vertex.getZ());

                Vector3 vertex2 = vertices.get(h.vert2);
                coords.add(vertex2.getX());
                coords.add(vertex2.getY());
                coords.add(vertex2.getZ());

            }
        }

        this.addAttribute( "position", new BufferAttribute( Float32Array.create( coords ), 3 ) );

    }
}
