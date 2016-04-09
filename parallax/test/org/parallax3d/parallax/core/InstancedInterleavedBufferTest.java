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
import org.parallax3d.parallax.graphics.core.InstancedInterleavedBuffer;
import org.parallax3d.parallax.system.ThreejsTest;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import static org.junit.Assert.assertTrue;

@ThreejsTest("InstancedInterleavedBuffer")
public class InstancedInterleavedBufferTest {
    @Test
    public void testCan_be_created() {
        Float32Array array = Float32Array.create(new double[]{1, 2, 3, 7, 8, 9});
        InstancedInterleavedBuffer instance = new InstancedInterleavedBuffer(array, 3);
        assertTrue(instance.getMeshPerAttribute() == 1);

    }

    @Test
    public void testCopy() {
        Float32Array array = Float32Array.create(new double[]{1, 2, 3, 7, 8, 9});
        InstancedInterleavedBuffer instance = new InstancedInterleavedBuffer(array, 3);
        InstancedInterleavedBuffer copiedInstance = instance.copy(instance);
        assertTrue(copiedInstance.getMeshPerAttribute() == 1);

    }

}
