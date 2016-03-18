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

@ThreejsObject("THREE.BoxBufferGeometry")
public class BoxBufferGeometry extends BufferGeometry {

    // buffers
    Uint32Array indices;
    Float32Array vertices;
    Float32Array normals;
    Float32Array uvs;

    // offset variables
    int vertexBufferOffset = 0;
    int uvBufferOffset = 0;
    int indexBufferOffset = 0;
    int numberOfVertices = 0;

    // group variables
    int groupStart = 0;

    public BoxBufferGeometry(double width, double height, double depth)
    {
        this( width, height, depth, 1, 1, 1);
    }

    public BoxBufferGeometry(double width, double height, double depth, int widthSegments, int heightSegments, int depthSegments)
    {
        // these are used to calculate buffer length
        int vertexCount = calculateVertexCount( widthSegments, heightSegments, depthSegments );
        int indexCount = ( vertexCount / 4 ) * 6;

        // buffers
        indices = Uint32Array.create( indexCount );
        vertices = Float32Array.create(vertexCount * 3);
        normals = Float32Array.create( vertexCount * 3 );
        uvs = Float32Array.create( vertexCount * 2 );

        // build each side of the box geometry
        buildPlane( 0, 1, 0, - 1, - 1, depth, height,   width,  depthSegments, heightSegments, 0 ); // px
        buildPlane( 0, 1, 0,   1, - 1, depth, height, - width,  depthSegments, heightSegments, 1 ); // nx
        buildPlane( 0, 0, 1,   1,   1, width, depth,    height, widthSegments, depthSegments,  2 ); // py
        buildPlane( 0, 0, 1,   1, - 1, width, depth,  - height, widthSegments, depthSegments,  3 ); // ny
        buildPlane( 0, 1, 0,   1, - 1, width, height,   depth,  widthSegments, heightSegments, 4 ); // pz
        buildPlane( 0, 1, 0, - 1, - 1, width, height, - depth,  widthSegments, heightSegments, 5 ); // nz

        // build geometry
        this.setIndex( new BufferAttribute( indices, 1 ) );
        this.addAttribute( "position", new BufferAttribute( vertices, 3 ) );
        this.addAttribute( "normal", new BufferAttribute( normals, 3 ) );
        this.addAttribute( "uv", new BufferAttribute( uvs, 2 ) );

    }

    private int calculateVertexCount ( int w, int h, int d ) {

        int segments = 0;

        // calculate the amount of segments for each side
        segments += w * h * 2; // xy
        segments += w * d * 2; // xz
        segments += d * h * 2; // zy

        return segments * 4; // four vertices per segments

    }

    private void buildPlane ( int u, int v, int w, int udir, int vdir, double width, double height, double depth, int gridX, int gridY, int materialIndex ) {

        double segmentWidth	= width / gridX;
        double segmentHeight = height / gridY;

        double widthHalf = width / 2.;
        double heightHalf = height / 2.;
        double depthHalf = depth / 2.;

        int gridX1 = gridX + 1;
        int gridY1 = gridY + 1;

        int vertexCounter = 0;
        int groupCount = 0;

        Vector3 vector = new Vector3();

        // generate vertices, normals and uvs

        for ( int iy = 0; iy < gridY1; iy ++ ) {

            double y = iy * segmentHeight - heightHalf;

            for ( int ix = 0; ix < gridX1; ix ++ ) {

                double x = ix * segmentWidth - widthHalf;

                // set values to correct vector component
                vector.setComponent(u , x * udir)
                        .set( v , y * vdir )
                        .set( w , depthHalf );

                // now apply vector to vertex buffer
                vertices.set( vertexBufferOffset , vector.getX());
                vertices.set( vertexBufferOffset + 1 , vector.getY());
                vertices.set( vertexBufferOffset + 2 , vector.getZ());

                // set values to correct vector component
                vector.setComponent( u , 0. )
                        .setComponent( v , 0. )
                        .setComponent( w , depth > 0 ? 1. : - 1. );

                // now apply vector to normal buffer
                normals.set( vertexBufferOffset , vector.getX());
                normals.set( vertexBufferOffset + 1 , vector.getY());
                normals.set( vertexBufferOffset + 2 , vector.getZ());

                // uvs
                uvs.set( uvBufferOffset , ix / gridX);
                uvs.set( uvBufferOffset + 1 , 1 - ( iy / gridY ));

                // update offsets and counters
                vertexBufferOffset += 3;
                uvBufferOffset += 2;
                vertexCounter += 1;

            }

        }

        // 1. you need three indices to draw a single face
        // 2. a single segment consists of two faces
        // 3. so we need to generate six (2*3) indices per segment

        for ( int iy = 0; iy < gridY; iy ++ ) {

            for ( int ix = 0; ix < gridX; ix ++ ) {

                // indices
                int a = numberOfVertices + ix + gridX1 * iy;
                int b = numberOfVertices + ix + gridX1 * ( iy + 1 );
                int c = numberOfVertices + ( ix + 1 ) + gridX1 * ( iy + 1 );
                int d = numberOfVertices + ( ix + 1 ) + gridX1 * iy;

                // face one
                indices.set( indexBufferOffset , a );
                indices.set( indexBufferOffset + 1 , b );
                indices.set( indexBufferOffset + 2 , d );

                // face two
                indices.set( indexBufferOffset + 3 , b );
                indices.set( indexBufferOffset + 4 , c );
                indices.set( indexBufferOffset + 5 , d );

                // update offsets and counters
                indexBufferOffset += 6;
                groupCount += 6;

            }

        }

        // add a group to the geometry. this will ensure multi material support
        this.addGroup( groupStart, groupCount, materialIndex );

        // calculate new start value for groups
        groupStart += groupCount;

        // update total number of vertices
        numberOfVertices += vertexCounter;

    }
}
