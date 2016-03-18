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
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint32Array;

import java.util.List;

@ThreejsObject("THREE.LatheBufferGeometry")
public class LatheBufferGeometry extends BufferGeometry {

    public LatheBufferGeometry(List<Vector2> points) {
        this(points, 12, 0, Math.PI * 2);
    }

    public LatheBufferGeometry(List<Vector2> points, int segments, double phiStart, double phiLength)
    {
        // clamp phiLength so it's in range of [ 0, 2PI ]
        phiLength = Mathematics.clamp( phiLength, 0, Math.PI * 2 );

        // these are used to calculate buffer length
        int vertexCount = ( segments + 1 ) * points.size();
        int indexCount = segments * points.size() * 2 * 3;

        // buffers
        BufferAttribute indices = new BufferAttribute( Uint32Array.create(indexCount) , 1 );
        BufferAttribute vertices = new BufferAttribute( Float32Array.create( vertexCount * 3 ), 3 );
        BufferAttribute uvs = new BufferAttribute( Float32Array.create( vertexCount * 2 ), 2 );

        // helper variables
        int index = 0, indexOffset = 0;
        double inversePointLength = 1.0 / ( points.size() - 1 );
        double inverseSegments = 1.0 / segments;
        Vector3 vertex = new Vector3();
        Vector2 uv = new Vector2();

        // generate vertices and uvs

        for ( int i = 0; i <= segments; i ++ ) {

            double phi = phiStart + i * inverseSegments * phiLength;

            double sin = Math.sin( phi );
            double cos = Math.cos( phi );

            for ( int j = 0; j <= ( points.size() - 1 ); j ++ ) {

                // vertex
                vertex.setX(points.get(j).getX() * sin);
                vertex.setY(points.get(j).getY());
                vertex.setZ(points.get(j).getX() * cos);
                vertices.setXYZ( index, vertex.getX(), vertex.getY(), vertex.getZ());

                // uv
                uv.setX(i / segments);
                uv.setY(j / (points.size() - 1));
                uvs.setXY( index, uv.getX(), uv.getY());

                // increase index
                index ++;

            }

        }

        // generate indices

        for ( int i = 0; i < segments; i ++ ) {

            for ( int j = 0; j < ( points.size() - 1 ); j ++ ) {

                int base = j + i * points.size();

                // indices
                int a = base;
                int b = base + points.size();
                int c = base + points.size() + 1;
                int d = base + 1;

                // face one
                indices.setX( indexOffset, a ); indexOffset++;
                indices.setX( indexOffset, b ); indexOffset++;
                indices.setX( indexOffset, d ); indexOffset++;

                // face two
                indices.setX( indexOffset, b ); indexOffset++;
                indices.setX( indexOffset, c ); indexOffset++;
                indices.setX( indexOffset, d ); indexOffset++;

            }

        }

        // build geometry

        this.setIndex( indices );
        this.addAttribute( "position", vertices );
        this.addAttribute( "uv", uvs );

        // generate normals

        this.computeVertexNormals();

        // if the geometry is closed, we need to average the normals along the seam.
        // because the corresponding vertices are identical (but still have different UVs).

        if( phiLength == Math.PI * 2 ) {

            Float32Array normals = (Float32Array) this.getAttributes().get("normal").getArray();
            Vector3 n1 = new Vector3();
            Vector3 n2 = new Vector3();
            Vector3 n = new Vector3();

            // this is the buffer offset for the last line of vertices
            int base = segments * points.size() * 3;

            for( int i = 0, j = 0; i < points.size(); i ++, j += 3 ) {

                // select the normal of the vertex in the first line
                n1.setX(normals.get(j + 0));
                n1.setY(normals.get(j + 1));
                n1.setZ(normals.get(j + 2));

                // select the normal of the vertex in the last line
                n2.setX(normals.get(base + j + 0));
                n2.setY(normals.get(base + j + 1));
                n2.setZ(normals.get(base + j + 2));

                // average normals
                n.add( n1, n2 ).normalize();

                // assign the new values to both normals

                normals.set( j + 0 , n.getX()); normals.set( base + j + 0 , n.getX());
                normals.set( j + 1 , n.getY()); normals.set( base + j + 1 , n.getY());
                normals.set( j + 2 , n.getZ()); normals.set( base + j + 2 , n.getZ());

            } // next row

        }

    }
}
