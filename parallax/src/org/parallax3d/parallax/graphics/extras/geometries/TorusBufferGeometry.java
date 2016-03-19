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
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint32Array;

@ThreejsObject("THREE.TorusBufferGeometry")
public class TorusBufferGeometry extends BufferGeometry {

    public TorusBufferGeometry() {
        this(100, 40, 8, 6);
    }

    public TorusBufferGeometry(double radius, double tube, int segmentsR, int segmentsT) {
        this(radius, tube, segmentsR, segmentsT, Math.PI * 2.0);
    }

    public TorusBufferGeometry(double radius, double tube, int radialSegments, int tubularSegments, double arc) 
    {
        // used to calculate buffer length
        int vertexCount = ( ( radialSegments + 1 ) * ( tubularSegments + 1 ) );
        int indexCount = radialSegments * tubularSegments * 2 * 3;

        // buffers
        Uint32Array indices = Uint32Array.create(indexCount);
        Float32Array vertices = Float32Array.create(vertexCount * 3);
        Float32Array normals = Float32Array.create( vertexCount * 3 );
        Float32Array uvs = Float32Array.create( vertexCount * 2 );

        // offset variables
        int vertexBufferOffset = 0;
        int uvBufferOffset = 0;
        int indexBufferOffset = 0;

        // helper variables
        Vector3 center = new Vector3();
        Vector3 vertex = new Vector3();
        Vector3 normal = new Vector3();

        // generate vertices, normals and uvs

        for ( int j = 0; j <= radialSegments; j ++ ) {

            for ( int i = 0; i <= tubularSegments; i ++ ) {

                double u = i / tubularSegments * arc;
                double v = j / radialSegments * Math.PI * 2;

                // vertex
                vertex.setX((radius + tube * Math.cos(v)) * Math.cos(u));
                vertex.setY((radius + tube * Math.cos(v)) * Math.sin(u));
                vertex.setZ(tube * Math.sin(v));

                vertices.set( vertexBufferOffset , vertex.getX() );
                vertices.set( vertexBufferOffset + 1 , vertex.getY() );
                vertices.set( vertexBufferOffset + 2 , vertex.getZ() );

                // this vector is used to calculate the normal
                center.setX(radius * Math.cos(u));
                center.setY(radius * Math.sin(u));

                // normal
                normal.sub( vertex, center ).normalize();

                normals.set( vertexBufferOffset , normal.getX() );
                normals.set( vertexBufferOffset + 1 , normal.getY() );
                normals.set( vertexBufferOffset + 2 , normal.getZ() );

                // uv
                uvs.set( uvBufferOffset , i / tubularSegments );
                uvs.set( uvBufferOffset + 1 , j / radialSegments );

                // update offsets
                vertexBufferOffset += 3;
                uvBufferOffset += 2;

            }

        }

        // generate indices

        for ( int j = 1; j <= radialSegments; j ++ ) {

            for ( int i = 1; i <= tubularSegments; i ++ ) {

                // indices
                int a = ( tubularSegments + 1 ) * j + i - 1;
                int b = ( tubularSegments + 1 ) * ( j - 1 ) + i - 1;
                int c = ( tubularSegments + 1 ) * ( j - 1 ) + i;
                int d = ( tubularSegments + 1 ) * j + i;

                // face one
                indices.set( indexBufferOffset , a );
                indices.set( indexBufferOffset + 1 , b );
                indices.set( indexBufferOffset + 2 , d );

                // face two
                indices.set( indexBufferOffset + 3 , b );
                indices.set( indexBufferOffset + 4 , c );
                indices.set( indexBufferOffset + 5 , d );

                // update offset
                indexBufferOffset += 6;

            }

        }

        // build geometry
        this.setIndex( new BufferAttribute( indices, 1 ) );
        this.addAttribute( "position", new BufferAttribute( vertices, 3 ) );
        this.addAttribute( "normal", new BufferAttribute( normals, 3 ) );
        this.addAttribute( "uv", new BufferAttribute( uvs, 2 ) );

    }
}
