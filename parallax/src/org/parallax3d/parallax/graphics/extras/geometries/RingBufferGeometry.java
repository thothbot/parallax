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
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint32Array;

@ThreejsObject("THREE.RingBufferGeometry")
public class RingBufferGeometry extends BufferGeometry {

    public RingBufferGeometry() {
        this(20, 50);
    }
    
    public RingBufferGeometry(double innerRadius, double outerRadius) {
        this(innerRadius, outerRadius, 8, 8, 0, Math.PI * 2.0);
    }

    public RingBufferGeometry(double innerRadius, double outerRadius, int thetaSegments, 
                              int phiSegments, double thetaStart, double thetaLength ) 
    {
        thetaSegments = Math.max( 3, thetaSegments );
        phiSegments = Math.max( 1, phiSegments );

        // these are used to calculate buffer length
        int vertexCount = ( thetaSegments + 1 ) * ( phiSegments + 1 );
        int indexCount = thetaSegments * phiSegments * 2 * 3;

        // buffers
        BufferAttribute indices = new BufferAttribute(Uint32Array.create(indexCount) , 1 );
        BufferAttribute vertices = new BufferAttribute(Float32Array.create(vertexCount * 3), 3 );
        BufferAttribute normals = new BufferAttribute(Float32Array.create( vertexCount * 3 ), 3 );
        BufferAttribute uvs = new BufferAttribute(Float32Array.create( vertexCount * 2 ), 2 );

        // some helper variables
        int index = 0, indexOffset = 0;
        double radius = innerRadius;
        double radiusStep = ( ( outerRadius - innerRadius ) / phiSegments );
        Vector3 vertex = new Vector3();
        Vector2 uv = new Vector2();

        // generate vertices, normals and uvs

        // values are generate from the inside of the ring to the outside

        for ( int j = 0; j <= phiSegments; j ++ ) {

            for ( int i = 0; i <= thetaSegments; i ++ ) {

                double segment = thetaStart + i / thetaSegments * thetaLength;

                // vertex
                vertex.setX(radius * Math.cos(segment));
                vertex.setY(radius * Math.sin(segment));
                vertices.setXYZ( index, vertex.getX(), vertex.getY(), vertex.getZ());

                // normal
                normals.setXYZ( index, 0, 0, 1 );

                // uv
                uv.setX((vertex.getX() / outerRadius + 1) / 2);
                uv.setY((vertex.getY() / outerRadius + 1) / 2);
                uvs.setXY( index, uv.getX(), uv.getY());

                // increase index
                index++;

            }

            // increase the radius for next row of vertices
            radius += radiusStep;

        }

        // generate indices

        for ( int j = 0; j < phiSegments; j ++ ) {

            int thetaSegmentLevel = j * ( thetaSegments + 1 );

            for ( int i = 0; i < thetaSegments; i ++ ) {

                int segment = i + thetaSegmentLevel;

                // indices
                int a = segment;
                int b = segment + thetaSegments + 1;
                int c = segment + thetaSegments + 2;
                int d = segment + 1;

                // face one
                indices.setX( indexOffset, a ); indexOffset++;
                indices.setX( indexOffset, b ); indexOffset++;
                indices.setX( indexOffset, c ); indexOffset++;

                // face two
                indices.setX( indexOffset, a ); indexOffset++;
                indices.setX( indexOffset, c ); indexOffset++;
                indices.setX( indexOffset, d ); indexOffset++;

            }

        }

        // build geometry

        this.setIndex( indices );
        this.addAttribute( "position", vertices );
        this.addAttribute( "normal", normals );
        this.addAttribute( "uv", uvs );

    }
}
