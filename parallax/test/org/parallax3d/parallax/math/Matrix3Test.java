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

package org.parallax3d.parallax.math;

import org.junit.Test;
import org.parallax3d.parallax.system.ThreejsTest;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import static org.junit.Assert.*;

@ThreejsTest("Matrix3")
public class Matrix3Test {

    static boolean matrixEquals3(Matrix4 a, Matrix4 b) {
        return matrixEquals3(new Matrix3().setFromMatrix4(a), new Matrix3().setFromMatrix4(b));
    }

    static boolean matrixEquals3(Matrix3 a, Matrix3 b) {
        double tolerance = 0.0001;
        if (a.elements.getLength() != b.elements.getLength()) {
            return false;
        }
        for (int i = 0, il = a.elements.getLength(); i < il; i++) {
            double delta = a.elements.get(i) - b.elements.get(i);
            if (delta > tolerance) {
                return false;
            }
        }
        return true;
    }

    static Matrix4 toMatrix4(Matrix3 m3) {
        Matrix4 result = new Matrix4();
        Float32Array re = result.elements;
        Float32Array me = m3.elements;
        re.set(0, me.get(0));
        re.set(1, me.get(1));
        re.set(2, me.get(2));
        re.set(4, me.get(3));
        re.set(5, me.get(4));
        re.set(6, me.get(5));
        re.set(8, me.get(6));
        re.set(9, me.get(7));
        re.set(10, me.get(8));

        return result;
    }

    ;

    @Test
    public void testConstructor() {
        Matrix3 a = new Matrix3();
        assertTrue(a.determinant() == 1);
        Matrix3 b = new Matrix3().set(0, 1, 2, 3, 4, 5, 6, 7, 8);
        assertTrue(b.elements.get(0) == 0);
        assertTrue(b.elements.get(1) == 3);
        assertTrue(b.elements.get(2) == 6);
        assertTrue(b.elements.get(3) == 1);
        assertTrue(b.elements.get(4) == 4);
        assertTrue(b.elements.get(5) == 7);
        assertTrue(b.elements.get(6) == 2);
        assertTrue(b.elements.get(7) == 5);
        assertTrue(b.elements.get(8) == 8);
        assertTrue(!matrixEquals3(a, b));

    }

    @Test
    public void testCopy() {
        Matrix3 a = new Matrix3().set(0, 1, 2, 3, 4, 5, 6, 7, 8);
        Matrix3 b = new Matrix3().copy(a);
        assertTrue(matrixEquals3(a, b));
        a.elements.set(0, 2);
        assertTrue(!matrixEquals3(a, b));

    }

    @Test
    public void testSet() {
        Matrix3 b = new Matrix3();
        assertTrue(b.determinant() == 1);
        b.set(0, 1, 2, 3, 4, 5, 6, 7, 8);
        assertTrue(b.elements.get(0) == 0);
        assertTrue(b.elements.get(1) == 3);
        assertTrue(b.elements.get(2) == 6);
        assertTrue(b.elements.get(3) == 1);
        assertTrue(b.elements.get(4) == 4);
        assertTrue(b.elements.get(5) == 7);
        assertTrue(b.elements.get(6) == 2);
        assertTrue(b.elements.get(7) == 5);
        assertTrue(b.elements.get(8) == 8);

    }

    @Test
    public void testIdentity() {
        Matrix3 b = new Matrix3().set(0, 1, 2, 3, 4, 5, 6, 7, 8);
        assertTrue(b.elements.get(0) == 0);
        assertTrue(b.elements.get(1) == 3);
        assertTrue(b.elements.get(2) == 6);
        assertTrue(b.elements.get(3) == 1);
        assertTrue(b.elements.get(4) == 4);
        assertTrue(b.elements.get(5) == 7);
        assertTrue(b.elements.get(6) == 2);
        assertTrue(b.elements.get(7) == 5);
        assertTrue(b.elements.get(8) == 8);
        Matrix3 a = new Matrix3();
        assertTrue(!matrixEquals3(a, b));
        assertTrue(matrixEquals3(a, b));

    }

    @Test
    public void testMultiplyScalar() {
        Matrix3 b = new Matrix3().set(0, 1, 2, 3, 4, 5, 6, 7, 8);
        assertTrue(b.elements.get(0) == 0);
        assertTrue(b.elements.get(1) == 3);
        assertTrue(b.elements.get(2) == 6);
        assertTrue(b.elements.get(3) == 1);
        assertTrue(b.elements.get(4) == 4);
        assertTrue(b.elements.get(5) == 7);
        assertTrue(b.elements.get(6) == 2);
        assertTrue(b.elements.get(7) == 5);
        assertTrue(b.elements.get(8) == 8);
        b.multiplyScalar(2);
        assertTrue(b.elements.get(0) == 0 * 2);
        assertTrue(b.elements.get(1) == 3 * 2);
        assertTrue(b.elements.get(2) == 6 * 2);
        assertTrue(b.elements.get(3) == 1 * 2);
        assertTrue(b.elements.get(4) == 4 * 2);
        assertTrue(b.elements.get(5) == 7 * 2);
        assertTrue(b.elements.get(6) == 2 * 2);
        assertTrue(b.elements.get(7) == 5 * 2);
        assertTrue(b.elements.get(8) == 8 * 2);

    }

    @Test
    public void testDeterminant() {
        Matrix3 a = new Matrix3();
        assertTrue(a.determinant() == 1);
        a.elements.set(0, 2);
        assertTrue(a.determinant() == 2);
        a.elements.set(0, 0);
        assertTrue(a.determinant() == 0);
        a.set(2, 3, 4, 5, 13, 7, 8, 9, 11);
        assertTrue(a.determinant() == -73);

    }

    @Test
    public void testGetInverse() {
        Matrix4 identity = new Matrix4();
        Matrix4 a = new Matrix4();
        Matrix3 b = new Matrix3().set(0, 0, 0, 0, 0, 0, 0, 0, 0);
        Matrix4 c = new Matrix4().set(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertTrue(!matrixEquals3(a, c));
        b.getInverse(a);
        assertTrue(matrixEquals3(b, new Matrix3()));

        Matrix4[] testMatrices = new Matrix4[]{
                new Matrix4().makeRotationX(0.3),
                new Matrix4().makeRotationX(-0.3),
                new Matrix4().makeRotationY(0.3),
                new Matrix4().makeRotationY(-0.3),
                new Matrix4().makeRotationZ(0.3),
                new Matrix4().makeRotationZ(-0.3),
                new Matrix4().makeScale(1, 2, 3),
                new Matrix4().makeScale(1 / 8, 1 / 2, 1 / 3)};

        for (int i = 0, il = testMatrices.length; i < il; i++) {
            Matrix4 m = testMatrices[i];
            Matrix3 mInverse3 = new Matrix3().getInverse(m);

            Matrix4 mInverse = toMatrix4(mInverse3);

            // the determinant of the inverse should be the reciprocal
            assertTrue(Math.abs(m.determinant() * mInverse3.determinant() - 1) < 0.0001);
            assertTrue(Math.abs(m.determinant() * mInverse.determinant() - 1) < 0.0001);

            Matrix4 mProduct = new Matrix4().multiplyMatrices(m, mInverse);
            assertTrue(Math.abs(mProduct.determinant() - 1) < 0.0001);
            assertTrue(matrixEquals3(mProduct, identity));
        }
    }

    @Test
    public void testTranspose() {
        Matrix3 a = new Matrix3();
        Matrix3 b = a.clone().transpose();
        assertTrue(matrixEquals3(a, b));
        b = new Matrix3().set(0, 1, 2, 3, 4, 5, 6, 7, 8);
        Matrix3 c = b.clone().transpose();
        assertTrue(!matrixEquals3(b, c));
        assertTrue(matrixEquals3(b, c));

    }

    @Test
    public void testClone() {
        Matrix3 a = new Matrix3().set(0, 1, 2, 3, 4, 5, 6, 7, 8);
        Matrix3 b = a.clone();
        assertTrue(matrixEquals3(a, b));
        a.elements.set(0, 2);
        assertTrue(!matrixEquals3(a, b));

    }

}
