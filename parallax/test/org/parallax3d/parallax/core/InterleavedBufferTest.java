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
import org.parallax3d.parallax.graphics.core.AttributeData;
import org.parallax3d.parallax.graphics.core.InterleavedBuffer;
import org.parallax3d.parallax.system.ThreejsTest;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import static org.junit.Assert.assertTrue;

@ThreejsTest("InterleavedBuffer")
public class InterleavedBufferTest {
    @Test
    public void testLength_and_count() {
        InterleavedBuffer instance = new InterleavedBuffer(Float32Array.create(new double[]{1, 2, 3, 7, 8, 9}), 3);
        assertTrue(instance.getLength() == 6);
        assertTrue(instance.getCount() == 2);

    }

    @Test
    public void testCopy() {
        Float32Array array = Float32Array.create(new double[]{1, 2, 3, 7, 8, 9});
        InterleavedBuffer instance = new InterleavedBuffer(array, 3);
        instance.setDynamic(true);
        checkInstanceAgainstCopy(instance, instance.copy(instance));

    }

    @Test
    public void testClone() {
        Float32Array array = Float32Array.create(new double[]{1, 2, 3, 7, 8, 9});
        InterleavedBuffer instance = new InterleavedBuffer(array, 3);
        instance.setDynamic(true);
        checkInstanceAgainstCopy(instance, instance.clone());

    }

    @Test
    public void testSet() {
        InterleavedBuffer instance = new InterleavedBuffer(Float32Array.create(new double[]{1, 2, 3, 7, 8, 9}), 3);
        instance.set(Float32Array.create(new double[]{0, -1}));
        assertTrue(((Float32Array) instance.getArray()).get(0) == 0 && ((Float32Array) instance.getArray()).get(1) == -1);

    }

    static void checkInstanceAgainstCopy(AttributeData instance, InterleavedBuffer copiedInstance) {
        assertTrue("the clone has the correct type", copiedInstance instanceof InterleavedBuffer);

        for (int i = 0; i < instance.getArray().getLength(); i++) {
            assertTrue("array was copied", ((Float32Array) copiedInstance.getArray()).get(i) == ((Float32Array) instance.getArray()).get(i));
        }

        assertTrue("stride was copied", copiedInstance.getStride() == ((InterleavedBuffer) instance).getStride());
        assertTrue("dynamic was copied", copiedInstance.isDynamic() == true);
    }
}
