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
import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.math.Vector4;
import org.parallax3d.parallax.system.ThreejsTest;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint32Array;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

@ThreejsTest("BufferAttribute")
public class BufferAttributeTest {
    @Test
    public void testCount() {
        assertTrue(new BufferAttribute(Float32Array.create(new double[]{1, 2, 3, 4, 5, 6}),3).getCount() == 2);

    }

    @Test
    public void testCopy() {
        BufferAttribute attr = new BufferAttribute(Float32Array.create(new double[]{1, 2, 3, 4, 5, 6}),3);
        attr.setDynamic(true);
        attr.setNeedsUpdate( true );
        BufferAttribute attrCopy = new BufferAttribute(attr.getArray(), attr.getItemSize()).copy(attr);
        assertTrue(attr.getCount() == attrCopy.getCount());
        assertTrue(attr.getItemSize() == attrCopy.getItemSize());
        assertTrue(attr.isDynamic() == attrCopy.isDynamic());
        assertTrue(attr.getArray().getLength() == attrCopy.getArray().getLength());
        assertTrue(attr.getVersion() == 1 && attrCopy.getVersion() == 0);

    }

    @Test
    public void testCopyAt() {
        BufferAttribute attr = new BufferAttribute(Float32Array.create(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9}),3);
        BufferAttribute attr2 = new BufferAttribute(Float32Array.create(9), 3);
        attr2.copyAt(1, attr, 2);
        attr2.copyAt(0, attr, 1);
        attr2.copyAt(2, attr, 0);
        Float32Array i = (Float32Array) attr.getArray();
        Float32Array i2 = (Float32Array) attr2.getArray();
        assertTrue(i2.get( 0 ) == i.get( 3 ) && i2.get( 1 ) == i.get( 4 ) && i2.get( 2 ) == i.get( 5 ));
        assertTrue(i2.get( 3 ) == i.get( 6 ) && i2.get( 4 ) == i.get( 7 ) && i2.get( 5 ) == i.get( 8 ));
        assertTrue(i2.get( 6 ) == i.get( 0 ) && i2.get( 7 ) == i.get( 1 ) && i2.get( 8 ) == i.get( 2 ));

    }

    @Test
    public void testCopyColorsArray() {
        BufferAttribute attr = new BufferAttribute(Float32Array.create(6), 3);
        attr.copyColorsArray(Arrays.asList( new Color(0, 0.5, 1), new Color(0.25, 1, 0)));
        Float32Array i = (Float32Array) attr.getArray();
        assertTrue(i.get( 0 ) == 0 && i.get( 1 ) == 0.5 && i.get( 2 ) == 1);
        assertTrue(i.get( 3 ) == 0.25 && i.get( 4 ) == 1 && i.get( 5 ) == 0);

    }

    @Test
    public void testCopyIndicesArray() {
        BufferAttribute attr = new BufferAttribute(Uint32Array.create(6), 3);
        attr.copyIndicesArray(Arrays.asList( new Face3(1,2,3), new Face3(4,5,6)));
        Uint32Array i = (Uint32Array) attr.getArray();
        assertTrue(i.get( 0 ) == 1 && i.get( 1 ) == 2 && i.get( 2 ) == 3);
        assertTrue(i.get( 3 ) == 4 && i.get( 4 ) == 5 && i.get( 5 ) == 6);

    }

    @Test
    public void testCopyVector2sArray() {
        BufferAttribute attr = new BufferAttribute(Float32Array.create(4), 2);
        attr.copyVector2sArray(Arrays.asList( new Vector2(1, 2), new Vector2(4, 5) ));
        Float32Array i = (Float32Array) attr.getArray();
        assertTrue(i.get( 0 ) == 1 && i.get( 1 ) == 2);
        assertTrue(i.get( 2 ) == 4 && i.get( 3 ) == 5);

    }

    @Test
    public void testCopyVector3sArray() {
        BufferAttribute attr = new BufferAttribute(Float32Array.create(6), 2);
        attr.copyVector3sArray(Arrays.asList( new Vector3(1, 2, 3), new Vector3(10, 20, 30)));
        Float32Array i = (Float32Array) attr.getArray();
        assertTrue(i.get( 0 ) == 1 && i.get( 1 ) == 2 && i.get( 2 ) == 3);
        assertTrue(i.get( 3 ) == 10 && i.get( 4 ) == 20 && i.get( 5 ) == 30);

    }

    @Test
    public void testCopyVector4sArray() {
        BufferAttribute attr = new BufferAttribute(Float32Array.create(8), 2);
        attr.copyVector4sArray(Arrays.asList( new Vector4(1, 2, 3, 4), new Vector4(10, 20, 30, 40)));
        Float32Array i = (Float32Array) attr.getArray();
        assertTrue(i.get( 0 ) == 1 && i.get( 1 ) == 2 && i.get( 2 ) == 3 && i.get( 3 ) == 4);
        assertTrue(i.get( 4 ) == 10 && i.get( 5 ) == 20 && i.get( 6 ) == 30 && i.get( 7 ) == 40);

    }

    @Test
    public void testClone() {
        BufferAttribute attr = new BufferAttribute(Float32Array.create(new double[]{1, 2, 3, 4, 0.12, -12}),2);
        BufferAttribute attrCopy = attr.clone();
        assertTrue(attr.getArray().getLength() == attrCopy.getArray().getLength());

    }

}
