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

package org.parallax3d.parallax.graphics.core;

import org.parallax3d.parallax.system.UuidObject;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

/**
 * @author benaadams / https://twitter.com/ben_a_adams
 */
@ThreejsObject("THREE.InterleavedBufferAttribute")
public class InterleavedBufferAttribute extends UuidObject {

    InterleavedBuffer data;
    int itemSize;
    int offset;

    public InterleavedBufferAttribute( InterleavedBuffer interleavedBuffer, int itemSize, int offset) {
        this.data = interleavedBuffer;
        this.itemSize = itemSize;
        this.offset = offset;
    }

    public InterleavedBuffer getData() {
        return this.data;
    }

    public int getCount() {

        return this.data.getCount();

    }

    public InterleavedBufferAttribute setX( int index, double x ) {

        ((Float32Array)this.data.getArray()).set( index * this.data.stride + this.offset, x);

        return this;

    }

    public InterleavedBufferAttribute setY( int index, double y ) {

        ((Float32Array)this.data.getArray()).set( index * this.data.stride + this.offset + 1 , y );

        return this;

    }

    public InterleavedBufferAttribute setZ( int index, double z ) {

        ((Float32Array)this.data.getArray()).set( index * this.data.stride + this.offset + 2 , z);

        return this;

    }

    public InterleavedBufferAttribute setW( int index, double w ) {

        ((Float32Array)this.data.getArray()).set( index * this.data.stride + this.offset + 3 , w);

        return this;

    }

    public double getX( int index ) {

        return ((Float32Array)this.data.getArray()).get( index * this.data.stride + this.offset );

    }

    public double getY( int index ) {

        return ((Float32Array)this.data.getArray()).get( index * this.data.stride + this.offset + 1 );

    }

    public double getZ( int index ) {

        return ((Float32Array)this.data.getArray()).get( index * this.data.stride + this.offset + 2 );

    }

    public double getW( int index ) {

        return ((Float32Array)this.data.getArray()).get( index * this.data.stride + this.offset + 3 );

    }

    public InterleavedBufferAttribute setXY( int index, double x, double y ) {

        index = index * this.data.stride + this.offset;

        ((Float32Array)this.data.getArray()).set( index + 0 , x );
        ((Float32Array)this.data.getArray()).set( index + 1 , y );

        return this;

    }

    public InterleavedBufferAttribute setXYZ( int index, double x, double y, double z ) {

        index = index * this.data.stride + this.offset;

        ((Float32Array)this.data.getArray()).set( index + 0 , x );
        ((Float32Array)this.data.getArray()).set( index + 1 , y );
        ((Float32Array)this.data.getArray()).set( index + 2 , z );

        return this;

    }

    public InterleavedBufferAttribute setXYZW( int index, double x, double y, double z, double w ) {

        index = index * this.data.stride + this.offset;

        ((Float32Array)this.data.getArray()).set( index + 0 , x );
        ((Float32Array)this.data.getArray()).set( index + 1 , y );
        ((Float32Array)this.data.getArray()).set( index + 2 , z );
        ((Float32Array)this.data.getArray()).set( index + 3 , w );

        return this;

    }

}
