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

package org.parallax3d.parallax.core;

import org.junit.Test;
import org.parallax3d.parallax.graphics.core.InterleavedBuffer;
import org.parallax3d.parallax.graphics.core.InterleavedBufferAttribute;
import org.parallax3d.parallax.system.ThreejsTest;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import static org.junit.Assert.assertTrue;

@ThreejsTest("InterleavedBufferAttribute")
public class InterleavedBufferAttributeTest {
    @Test
    public void testLength_and_count() {
        InterleavedBuffer buffer = new InterleavedBuffer(Float32Array.create(new double[]{1, 2, 3, 7, 8, 9}), 3);
        InterleavedBufferAttribute instance = new InterleavedBufferAttribute(buffer, 2, 0);
        assertTrue(instance.getCount() == 2);

    }

    @Test
    public void testSetX() {
        InterleavedBuffer buffer = new InterleavedBuffer(Float32Array.create(new double[]{1, 2, 3, 7, 8, 9}), 3);
        InterleavedBufferAttribute instance = new InterleavedBufferAttribute(buffer, 2, 0);
        instance.setX(0, 123);
        instance.setX(1, 321);
        assertTrue(((Float32Array) instance.getData().getArray()).get(0) == 123 && ((Float32Array) instance.getData().getArray()).get(3) == 321);
        buffer = new InterleavedBuffer(Float32Array.create(new double[]{1, 2, 3, 7, 8, 9}), 3);
        instance = new InterleavedBufferAttribute(buffer, 2, 1);
        instance.setX(0, 123);
        instance.setX(1, 321);
        assertTrue(((Float32Array) instance.getData().getArray()).get(1) == 123 && ((Float32Array) instance.getData().getArray()).get(4) == 321);

    }

}
