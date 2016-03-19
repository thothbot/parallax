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

/**
 * @author Mugen87 / https://github.com/Mugen87
 *
 * see: http://www.blackpawn.com/texts/pqtorus/
 */
@ThreejsObject("THREE.TorusKnotBufferGeometry")
public class TorusKnotBufferGeometry extends BufferGeometry {

    public TorusKnotBufferGeometry()
    {
        this(100, 40, 64, 8);
    }

    public TorusKnotBufferGeometry(double radius)
    {
        this(radius, 40, 64, 8);
    }

    public TorusKnotBufferGeometry(double radius, double tube, int tubularSegments, int radialSegments )
    {
        this(radius, tube, tubularSegments, radialSegments, 2, 3);
    }

    public TorusKnotBufferGeometry(double radius, double tube, int tubularSegments, int radialSegments, int p, int q )
    {
        // used to calculate buffer length
        int vertexCount = ( ( radialSegments + 1 ) * ( tubularSegments + 1 ) );
        int indexCount = radialSegments * tubularSegments * 2 * 3;

        // buffers
        BufferAttribute indices = new BufferAttribute( Uint32Array.create( indexCount ) , 1 );
        BufferAttribute vertices = new BufferAttribute( Float32Array.create( vertexCount * 3 ), 3 );
        BufferAttribute normals = new BufferAttribute( Float32Array.create( vertexCount * 3 ), 3 );
        BufferAttribute uvs = new BufferAttribute( Float32Array.create( vertexCount * 2 ), 2 );

        // helper variables
        int index = 0, indexOffset = 0;

        Vector3 vertex = new Vector3();
        Vector3 normal = new Vector3();
        Vector2 uv = new Vector2();

        Vector3 P1 = new Vector3();
        Vector3 P2 = new Vector3();

        Vector3 B = new Vector3();
        Vector3 T = new Vector3();
        Vector3 N = new Vector3();

        // generate vertices, normals and uvs

        for ( int i = 0; i <= tubularSegments; ++ i ) {

            // the radian "u" is used to calculate the position on the torus curve of the current tubular segement

            double u = i / tubularSegments * p * Math.PI * 2.;

            // now we calculate two points. P1 is our current position on the curve, P2 is a little farther ahead.
            // these points are used to create a special "coordinate space", which is necessary to calculate the correct vertex positions

            calculatePositionOnCurve( u, p, q, radius, P1 );
            calculatePositionOnCurve( u + 0.01, p, q, radius, P2 );

            // calculate orthonormal basis

            T.sub( P2, P1 );
            N.add( P2, P1 );
            B.cross( T, N );
            N.cross( B, T );

            // normalize B, N. T can be ignored, we don't use it

            B.normalize();
            N.normalize();

            for ( int j = 0; j <= radialSegments; ++ j ) {

                // now calculate the vertices. they are nothing more than an extrusion of the torus curve.
                // because we extrude a shape in the xy-plane, there is no need to calculate a z-value.

                double v = (double)j / radialSegments * Math.PI * 2.;
                double cx = - tube * Math.cos( v );
                double cy = tube * Math.sin( v );

                // now calculate the final vertex position.
                // first we orient the extrusion with our basis vectos, then we add it to the current position on the curve

                vertex.setX(P1.getX() + (cx * N.getX() + cy * B.getX()));
                vertex.setY(P1.getY() + (cx * N.getY() + cy * B.getY()));
                vertex.setZ(P1.getZ() + (cx * N.getZ() + cy * B.getZ()));

                // vertex
                vertices.setXYZ( index, vertex.getX(), vertex.getY(), vertex.getZ());

                // normal (P1 is always the center/origin of the extrusion, thus we can use it to calculate the normal)
                normal.sub( vertex, P1 ).normalize();
                normals.setXYZ( index, normal.getX(), normal.getY(), normal.getZ());

                // uv
                uv.setX(i / tubularSegments);
                uv.setY(j / radialSegments);
                uvs.setXY( index, uv.getX(), uv.getY());

                // increase index
                index ++;

            }

        }

        // generate indices

        for ( int j = 1; j <= tubularSegments; j ++ ) {

            for ( int i = 1; i <= radialSegments; i ++ ) {

                // indices
                int a = ( radialSegments + 1 ) * ( j - 1 ) + ( i - 1 );
                int b = ( radialSegments + 1 ) * j + ( i - 1 );
                int c = ( radialSegments + 1 ) * j + i;
                int d = ( radialSegments + 1 ) * ( j - 1 ) + i;

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
        this.addAttribute( "normal", normals );
        this.addAttribute( "uv", uvs );

    }

    /**
     * this function calculates the current position on the torus curve
     * 
     * @param u
     * @param p
     * @param q
     * @param radius
     * @param position
     */
    private static void calculatePositionOnCurve(double u, double p, double q, double radius, Vector3 position ) {

        double cu = Math.cos( u );
        double su = Math.sin( u );
        double quOverP = q / p * u;
        double cs = Math.cos( quOverP );

        position.setX(radius * (2 + cs) * 0.5 * cu);
        position.setY(radius * (2 + cs) * su * 0.5);
        position.setZ(radius * Math.sin(quOverP) * 0.5);

    }
}
