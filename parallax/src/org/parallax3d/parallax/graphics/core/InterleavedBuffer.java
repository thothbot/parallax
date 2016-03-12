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

import org.parallax3d.parallax.graphics.renderers.gl.AttributeData;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.TypeArray;

/**
 * @author benaadams / https://twitter.com/ben_a_adams
 */
@ThreejsObject("THREE.InterleavedBuffer")
public class InterleavedBuffer extends AttributeData {

    int stride;

    public InterleavedBuffer(TypeArray array, int stride)
    {
        super(array);
        this.stride = stride;
    }

    public int getStride() {
        return stride;
    }

    public int getLength () {

        return this.array.getLength();

    }

    public int getCount () {

        return getLength() / this.stride;

    }

//    public InterleavedBuffer copy( InterleavedBuffer source ) {
//
//        this.array = new ArrayList<>(source.array);
//        this.stride = source.stride;
//        this.dynamic = source.dynamic;
//
//        return this;
//
//    }

    public InterleavedBuffer copyAt( int index1, InterleavedBuffer attribute, int index2 ) {

        index1 *= this.stride;
        index2 *= attribute.stride;

        for ( int i = 0, l = this.stride; i < l; i ++ ) {

            ((Float32Array)this.array).set( index1 + i, ((Float32Array)this.array).get( index2 + i ));

        }

        return this;

    }

    public InterleavedBuffer set(  TypeArray value  ) {
        return set(value, 0);
    }

    public InterleavedBuffer set( TypeArray value, int offset ) {

        this.array.set( value, offset );

        return this;

    }

    @Override
    protected InterleavedBuffer clone() {
        InterleavedBuffer instance = new InterleavedBuffer(this.array, this.stride);
        instance.setDynamic(this.isDynamic());

        return this;
    }
}
