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

@ThreejsObject("THREE.CylinderBufferGeometry")
public class CylinderBufferGeometry extends BufferGeometry {

    double radiusTop;
    double radiusBottom;
    double height;

    int radialSegments;
    int heightSegments;
    boolean openEnded;

    double thetaStart;
    double thetaLength;

    BufferAttribute indices;
    BufferAttribute vertices;
    BufferAttribute normals;
    BufferAttribute uvs;

    int index = 0;
    int indexOffset = 0;

    double halfHeight;

    public CylinderBufferGeometry()
    {
        this(20, 20, 100, 8, 1);
    }

    public CylinderBufferGeometry(double radiusTop, double radiusBottom, double height, int segmentsRadius, int segmentsHeight)
    {
        this(radiusTop, radiusBottom, height, segmentsRadius, segmentsHeight, false, 0., Math.PI * 2.);
    }

    public CylinderBufferGeometry( double radiusTop, double radiusBottom, double height, int radialSegments, int heightSegments, boolean openEnded, double thetaStart, double thetaLength )
    {
        this.radiusBottom = radiusBottom;
        this.radiusTop = radiusTop;
        this.height = height;

        this.radialSegments = radialSegments;
        this.heightSegments = heightSegments;
        this.openEnded = openEnded;

        this.thetaStart = thetaStart;
        this.thetaLength = thetaLength;

        int vertexCount = calculateVertexCount();
        int indexCount = calculateIndexCount();

        // buffers

        BufferAttribute indices = new BufferAttribute(Uint32Array.create(indexCount), 1);
        BufferAttribute vertices = new BufferAttribute(Float32Array.create(vertexCount * 3), 3);
        BufferAttribute normals = new BufferAttribute(Float32Array.create(vertexCount * 3), 3);
        BufferAttribute uvs = new BufferAttribute(Float32Array.create(vertexCount * 2), 2);

        // helper variables

//        indexArray =[],
        halfHeight = height / 2;

        // generate geometry

        generateTorso();

        if (!openEnded) {

            if (radiusTop > 0) {

                generateCap(true);

            }

            if (radiusBottom > 0) {

                generateCap(false);

            }

        }

        // build geometry

        this.setIndex(indices);
        this.addAttribute("position", vertices);
        this.addAttribute("normal", normals);
        this.addAttribute("uv", uvs);

    }

    private int calculateVertexCount () {

        int count = ( radialSegments + 1 ) * ( heightSegments + 1 );

        if ( !openEnded ) {

            count += ( ( radialSegments + 1 ) * 2 ) + ( radialSegments * 2 );

        }

        return count;

    }

    private int calculateIndexCount () {

        int count = radialSegments * heightSegments * 2 * 3;

        if ( !openEnded ) {

            count += radialSegments * 2 * 3;

        }

        return count;

    }

    private void generateTorso () {

        int[][] indexArray = new int[ heightSegments + 1 ][];

        Vector3 normal = new Vector3();
        Vector3 vertex = new Vector3();

        // this will be used to calculate the normal
        double tanTheta = ( radiusBottom - radiusTop ) / height;

        // generate vertices, normals and uvs

        for ( int y = 0; y <= heightSegments; y ++ ) {

            int[] indexRow = new int[ radialSegments + 1 ];

            double v = y / heightSegments;

            // calculate the radius of the current row
            double radius = v * ( radiusBottom - radiusTop ) + radiusTop;

            for ( int x = 0; x <= radialSegments; x ++ ) {

                double u = x / radialSegments;

                // vertex
                vertex.setX(radius * Math.sin(u * thetaLength + thetaStart));
                vertex.setY(-v * height + halfHeight);
                vertex.setZ(radius * Math.cos(u * thetaLength + thetaStart));
                vertices.setXYZ( index, vertex.getX(), vertex.getY(), vertex.getZ());

                // normal
                normal.copy( vertex );

                // handle special case if radiusTop/radiusBottom is zero
                if( ( radiusTop == 0  && y == 0 ) || ( radiusBottom == 0  && y == heightSegments ) ) {

                    normal.setX(Math.sin(u * thetaLength + thetaStart));
                    normal.setZ(Math.cos(u * thetaLength + thetaStart));

                }

                normal.setY( Math.sqrt( normal.getX() * normal.getX() + normal.getZ() * normal.getZ()) * tanTheta ).normalize();
                normals.setXYZ( index, normal.getX(), normal.getY(), normal.getZ());

                // uv
                uvs.setXY( index, u, 1 - v );

                // save index of vertex in respective row
                indexRow[x] = index;

                // increase index
                index ++;

            }

            // now save vertices of the row in our index array
            indexArray[y] = indexRow;

        }

        // generate indices

        for ( int x = 0; x < radialSegments; x ++ ) {

            for ( int y = 0; y < heightSegments; y ++ ) {

                // we use the index array to access the correct indices
                int i1 = indexArray[ y ][ x ];
                int i2 = indexArray[ y + 1 ][ x ];
                int i3 = indexArray[ y + 1 ][ x + 1 ];
                int i4 = indexArray[ y ][ x + 1 ];

                // face one
                indices.setX( indexOffset, i1 ); indexOffset++;
                indices.setX( indexOffset, i2 ); indexOffset++;
                indices.setX( indexOffset, i4 ); indexOffset++;

                // face two
                indices.setX( indexOffset, i2 ); indexOffset++;
                indices.setX( indexOffset, i3 ); indexOffset++;
                indices.setX( indexOffset, i4 ); indexOffset++;

            }

        }

    }

    private void generateCap ( boolean top ) {

        Vector2 uv = new Vector2();
        Vector3 vertex = new Vector3();

        double radius = (top) ? radiusTop : radiusBottom;
        int sign = (top) ? 1 : - 1;

        // save the index of the first center vertex
        int centerIndexStart = index;

        // first we generate the center vertex data of the cap.
        // because the geometry needs one set of uvs per face,
        // we must generate a center vertex per face/segment

        for ( int x = 1; x <= radialSegments; x ++ ) {

            // vertex
            vertices.setXYZ( index, 0, halfHeight * sign, 0 );

            // normal
            normals.setXYZ( index, 0, sign, 0 );

            // uv
            if(top) {

                uv.setX(x / radialSegments);
                uv.setY(0);

            } else {

                uv.setX((x - 1) / radialSegments);
                uv.setY(1);

            }

            uvs.setXY( index, uv.getX(), uv.getY());

            // increase index
            index++;

        }

        // save the index of the last center vertex
        int centerIndexEnd = index;

        // now we generate the surrounding vertices, normals and uvs

        for ( int x = 0; x <= radialSegments; x ++ ) {

            double u = x / radialSegments;

            // vertex
            vertex.setX(radius * Math.sin(u * thetaLength + thetaStart));
            vertex.setY(halfHeight * sign);
            vertex.setZ(radius * Math.cos(u * thetaLength + thetaStart));
            vertices.setXYZ( index, vertex.getX(), vertex.getY(), vertex.getZ());

            // normal
            normals.setXYZ( index, 0, sign, 0 );

            // uv
            uvs.setXY( index, u, (top) ? 1 : 0 );

            // increase index
            index ++;

        }

        // generate indices

        for ( int x = 0; x < radialSegments; x ++ ) {

            int  c = centerIndexStart + x;
            int i = centerIndexEnd + x;

            if( top == true ) {

                // face top
                indices.setX( indexOffset, i ); indexOffset++;
                indices.setX( indexOffset, i + 1 ); indexOffset++;
                indices.setX( indexOffset, c ); indexOffset++;

            } else {

                // face bottom
                indices.setX( indexOffset, i + 1); indexOffset++;
                indices.setX( indexOffset, i ); indexOffset++;
                indices.setX( indexOffset, c ); indexOffset++;

            }

        }

    }

}
