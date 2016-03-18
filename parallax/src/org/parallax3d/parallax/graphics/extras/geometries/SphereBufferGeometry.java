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

import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.math.Sphere;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ThreejsObject("SphereBufferGeometry")
public class SphereBufferGeometry extends BufferGeometry {

    public SphereBufferGeometry() {
        this(50);
    }

    public SphereBufferGeometry(double radius) {
        this(radius, 8, 6);
    }

    public SphereBufferGeometry(double radius, int segmentsWidth, int segmentsHeight) {
        this(radius, segmentsWidth, segmentsHeight, 0.0, Math.PI * 2.0);
    }

    public SphereBufferGeometry(double radius, int segmentsWidth, int segmentsHeight, 
                                double phiStart, double phiLength) {
        this(radius, segmentsWidth, segmentsHeight, phiStart, phiLength, 0.0, Math.PI);
    }

    public SphereBufferGeometry(double radius, int widthSegments, int heightSegments,
                                double phiStart, double phiLength, double thetaStart, double thetaLength) {

        widthSegments = Math.max( 3, widthSegments );
        heightSegments = Math.max( 2, heightSegments );

        double thetaEnd = thetaStart + thetaLength;

        int vertexCount = ( ( widthSegments + 1 ) * ( heightSegments + 1 ) );

        BufferAttribute positions = new BufferAttribute( Float32Array.create( vertexCount * 3 ), 3 );
        BufferAttribute normals = new BufferAttribute( Float32Array.create( vertexCount * 3 ), 3 );
        BufferAttribute uvs = new BufferAttribute( Float32Array.create( vertexCount * 2 ), 2 );

        int index = 0;
        int[][] vertices = new int[heightSegments + 1][];
        Vector3 normal = new Vector3();

        for ( int y = 0; y <= heightSegments; y ++ ) {

            int[] verticesRow = new int[widthSegments + 1];

            double v = y / heightSegments;

            for ( int x = 0; x <= widthSegments; x ++ ) {

                double u = x / widthSegments;

                double px = - radius * Math.cos( phiStart + u * phiLength ) * Math.sin( thetaStart + v * thetaLength );
                double py = radius * Math.cos( thetaStart + v * thetaLength );
                double pz = radius * Math.sin( phiStart + u * phiLength ) * Math.sin( thetaStart + v * thetaLength );

                normal.set( px, py, pz ).normalize();

                positions.setXYZ( index, px, py, pz );
                normals.setXYZ( index, normal.getX(), normal.getY(), normal.getZ());
                uvs.setXY( index, u, 1. - v );

                verticesRow[x] = index;

                index ++;

            }

            vertices[y] = verticesRow;

        }

        List<Integer> indices = new ArrayList<>();

        for ( int y = 0; y < heightSegments; y ++ ) {

            for ( int x = 0; x < widthSegments; x ++ ) {

                int v1 = vertices[ y ][ x + 1 ];
                int v2 = vertices[ y ][ x ];
                int v3 = vertices[ y + 1 ][ x ];
                int v4 = vertices[ y + 1 ][ x + 1 ];

                if ( y != 0 || thetaStart > 0 )
                    indices.addAll(Arrays.asList( v1, v2, v4) );
                if ( y != heightSegments - 1 || thetaEnd < Math.PI )
                    indices.addAll(Arrays.asList( v2, v3, v4) );
            }
        }

        this.setIndex( BufferAttribute.Uint32Attribute( indices.toArray(), 1 ) );
        this.addAttribute( "position", positions );
        this.addAttribute( "normal", normals );
        this.addAttribute( "uv", uvs );

        this.boundingSphere = new Sphere( new Vector3(), radius );

    }
}
