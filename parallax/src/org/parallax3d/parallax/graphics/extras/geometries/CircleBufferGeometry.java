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
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint16Array;

public class CircleBufferGeometry extends BufferGeometry {

    public CircleBufferGeometry()
    {
        this(50., 8);
    }

    public CircleBufferGeometry(double radius, int segments)
    {
        this(radius, segments, 0, Math.PI * 2.0);
    }

    public CircleBufferGeometry(double radius, int segments, double thetaStart, double thetaLength )
    {
        segments = Math.max( 3, segments );

        int vertices = segments + 2;

        Float32Array positions = Float32Array.create( vertices * 3 );
        Float32Array normals = Float32Array.create( vertices * 3 );
        Float32Array uvs = Float32Array.create( vertices * 2 );

        // center data is already zero, but need to set a few extras
        normals.set( 2 , 1.0 );
        uvs.set( 0 , 0.5 );
        uvs.set( 1 , 0.5 );

        for ( int s = 0, i = 3, ii = 2 ; s <= segments; s ++, i += 3, ii += 2 ) {

            double segment = thetaStart + s / segments * thetaLength;

            positions.set( i , radius * Math.cos( segment ) );
            positions.set( i + 1 , radius * Math.sin( segment ) );

            normals.set( i + 2 , 1 ); // normal z

            uvs.set( ii , ( positions.get( i ) / radius + 1. ) / 2. );
            uvs.set( ii + 1 , ( positions.get( i + 1 ) / radius + 1. ) / 2. );

        }

        int[] indices = new int[ 3 * segments ];

        for ( int i = 1; i <= segments; i ++ ) {

            indices[ i * 3 - 3 ] = i;
            indices[ i * 3 - 2 ] = i + 1;
            indices[ i * 3 - 1 ] = 0;

        }

        this.setIndex( new BufferAttribute( Uint16Array.create( indices ), 1 ) );
        this.addAttribute( "position", new BufferAttribute( positions, 3 ) );
        this.addAttribute( "normal", new BufferAttribute( normals, 3 ) );
        this.addAttribute( "uv", new BufferAttribute( uvs, 2 ) );

        this.boundingSphere = new Sphere( new Vector3(), radius );
    }
}
