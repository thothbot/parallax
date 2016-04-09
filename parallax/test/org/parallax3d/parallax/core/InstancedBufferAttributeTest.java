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
import org.parallax3d.parallax.graphics.core.InstancedBufferAttribute;
import org.parallax3d.parallax.system.ThreejsTest;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import static org.junit.Assert.assertTrue;

@ThreejsTest("InstancedBufferAttribute")
public class InstancedBufferAttributeTest {
    @Test
    public void testCan_be_created() {
        InstancedBufferAttribute instance = new InstancedBufferAttribute(Float32Array.create(10), 2);
        assertTrue(instance.getMeshPerAttribute() == 1);
        instance = new InstancedBufferAttribute(Float32Array.create(10), 2, 123);
        assertTrue(instance.getMeshPerAttribute() == 123);

    }

    @Test
    public void testCopy() {
        Float32Array array = Float32Array.create(new double[]{1, 2, 3, 7, 8, 9});
        InstancedBufferAttribute instance = new InstancedBufferAttribute(array, 2, 123);
        InstancedBufferAttribute copiedInstance = instance.copy(instance);
        assertTrue(copiedInstance instanceof InstancedBufferAttribute);
        assertTrue(copiedInstance.getItemSize() == 2);
        assertTrue(copiedInstance.getMeshPerAttribute() == 123);

    }

}
