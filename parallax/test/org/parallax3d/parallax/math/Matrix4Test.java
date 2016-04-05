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

import static org.junit.Assert.assertTrue;
import static org.parallax3d.parallax.math.EulerTest.matrixEquals4;

@ThreejsTest("Matrix4")
public class Matrix4Test {
    @Test
    public void testConstructor() {
        Matrix4 a = new Matrix4();
        assertTrue(a.determinant() == 1);
        Matrix4 b = new Matrix4().set(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        assertTrue(b.elements.get(0) == 0);
        assertTrue(b.elements.get(1) == 4);
        assertTrue(b.elements.get(2) == 8);
        assertTrue(b.elements.get(3) == 12);
        assertTrue(b.elements.get(4) == 1);
        assertTrue(b.elements.get(5) == 5);
        assertTrue(b.elements.get(6) == 9);
        assertTrue(b.elements.get(7) == 13);
        assertTrue(b.elements.get(8) == 2);
        assertTrue(b.elements.get(9) == 6);
        assertTrue(b.elements.get(10) == 10);
        assertTrue(b.elements.get(11) == 14);
        assertTrue(b.elements.get(12) == 3);
        assertTrue(b.elements.get(13) == 7);
        assertTrue(b.elements.get(14) == 11);
        assertTrue(b.elements.get(15) == 15);
        assertTrue(!matrixEquals4(a, b));

    }

    @Test
    public void testCopy() {
        Matrix4 a = new Matrix4().set(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        Matrix4 b = new Matrix4().copy(a);
        assertTrue(matrixEquals4(a, b));
        a.elements.set(0, 2);
        assertTrue(!matrixEquals4(a, b));

    }

    @Test
    public void testSet() {
        Matrix4 b = new Matrix4();
        assertTrue(b.determinant() == 1);
        b.set(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        assertTrue(b.elements.get(0) == 0);
        assertTrue(b.elements.get(1) == 4);
        assertTrue(b.elements.get(2) == 8);
        assertTrue(b.elements.get(3) == 12);
        assertTrue(b.elements.get(4) == 1);
        assertTrue(b.elements.get(5) == 5);
        assertTrue(b.elements.get(6) == 9);
        assertTrue(b.elements.get(7) == 13);
        assertTrue(b.elements.get(8) == 2);
        assertTrue(b.elements.get(9) == 6);
        assertTrue(b.elements.get(10) == 10);
        assertTrue(b.elements.get(11) == 14);
        assertTrue(b.elements.get(12) == 3);
        assertTrue(b.elements.get(13) == 7);
        assertTrue(b.elements.get(14) == 11);
        assertTrue(b.elements.get(15) == 15);

    }

    @Test
    public void testIdentity() {
        Matrix4 b = new Matrix4().set(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        assertTrue(b.elements.get(0) == 0);
        assertTrue(b.elements.get(1) == 4);
        assertTrue(b.elements.get(2) == 8);
        assertTrue(b.elements.get(3) == 12);
        assertTrue(b.elements.get(4) == 1);
        assertTrue(b.elements.get(5) == 5);
        assertTrue(b.elements.get(6) == 9);
        assertTrue(b.elements.get(7) == 13);
        assertTrue(b.elements.get(8) == 2);
        assertTrue(b.elements.get(9) == 6);
        assertTrue(b.elements.get(10) == 10);
        assertTrue(b.elements.get(11) == 14);
        assertTrue(b.elements.get(12) == 3);
        assertTrue(b.elements.get(13) == 7);
        assertTrue(b.elements.get(14) == 11);
        assertTrue(b.elements.get(15) == 15);
        Matrix4 a = new Matrix4();
        assertTrue(!matrixEquals4(a, b));
        assertTrue(matrixEquals4(a, b));

    }

    @Test
    public void testMultiplyScalar() {
        Matrix4 b = new Matrix4().set(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        assertTrue(b.elements.get(0) == 0);
        assertTrue(b.elements.get(1) == 4);
        assertTrue(b.elements.get(2) == 8);
        assertTrue(b.elements.get(3) == 12);
        assertTrue(b.elements.get(4) == 1);
        assertTrue(b.elements.get(5) == 5);
        assertTrue(b.elements.get(6) == 9);
        assertTrue(b.elements.get(7) == 13);
        assertTrue(b.elements.get(8) == 2);
        assertTrue(b.elements.get(9) == 6);
        assertTrue(b.elements.get(10) == 10);
        assertTrue(b.elements.get(11) == 14);
        assertTrue(b.elements.get(12) == 3);
        assertTrue(b.elements.get(13) == 7);
        assertTrue(b.elements.get(14) == 11);
        assertTrue(b.elements.get(15) == 15);
        b.multiplyScalar(2);
        assertTrue(b.elements.get(0) == 0 * 2);
        assertTrue(b.elements.get(1) == 4 * 2);
        assertTrue(b.elements.get(2) == 8 * 2);
        assertTrue(b.elements.get(3) == 12 * 2);
        assertTrue(b.elements.get(4) == 1 * 2);
        assertTrue(b.elements.get(5) == 5 * 2);
        assertTrue(b.elements.get(6) == 9 * 2);
        assertTrue(b.elements.get(7) == 13 * 2);
        assertTrue(b.elements.get(8) == 2 * 2);
        assertTrue(b.elements.get(9) == 6 * 2);
        assertTrue(b.elements.get(10) == 10 * 2);
        assertTrue(b.elements.get(11) == 14 * 2);
        assertTrue(b.elements.get(12) == 3 * 2);
        assertTrue(b.elements.get(13) == 7 * 2);
        assertTrue(b.elements.get(14) == 11 * 2);
        assertTrue(b.elements.get(15) == 15 * 2);

    }

    @Test
    public void testDeterminant() {
        Matrix4 a = new Matrix4();
        assertTrue(a.determinant() == 1);
        a.elements.set(0, 2);
        assertTrue(a.determinant() == 2);
        a.elements.set(0, 0);
        assertTrue(a.determinant() == 0);
        a.set(2, 3, 4, 5, -1, -21, -3, -4, 6, 7, 8, 10, -8, -9, -10, -12);
        assertTrue(a.determinant() == 76);

    }

    @Test
    public void testGetInverse() {
        Matrix4 identity = new Matrix4();
        Matrix4 a = new Matrix4();
        Matrix4 b = new Matrix4().set(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        Matrix4 c = new Matrix4().set(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertTrue(!matrixEquals4(a, b));
        b.getInverse(a);
        assertTrue(matrixEquals4(b, new Matrix4()));

        Matrix4[] testMatrices = new Matrix4[]{
                new Matrix4().makeRotationX(0.3),
                new Matrix4().makeRotationX(-0.3),
                new Matrix4().makeRotationY(0.3)
                , new Matrix4().makeRotationY(-0.3),
                new Matrix4().makeRotationZ(0.3),
                new Matrix4().makeRotationZ(-0.3),
                new Matrix4().makeScale(1, 2, 3),
                new Matrix4().makeScale(1 / 8, 1 / 2, 1 / 3),
                new Matrix4().makeFrustum(-1, 1, -1, 1, 1, 1000),
                new Matrix4().makeFrustum(-16, 16, -9, 9, 0.1, 10000),
                new Matrix4().makeTranslation(1, 2, 3)};

        for (int i = 0, il = testMatrices.length; i < il; i++) {
            Matrix4 m = testMatrices[i];

            Matrix4 mInverse = new Matrix4().getInverse(m);
            Matrix4 mSelfInverse = m.clone();
            mSelfInverse.getInverse(mSelfInverse);


            // self-inverse should the same as inverse
            assertTrue(matrixEquals4(mSelfInverse, mInverse));

            // the determinant of the inverse should be the reciprocal
            assertTrue(Math.abs(m.determinant() * mInverse.determinant() - 1) < 0.0001);

            Matrix4 mProduct = new Matrix4().multiplyMatrices(m, mInverse);

            // the determinant of the identity matrix is 1
            assertTrue(Math.abs(mProduct.determinant() - 1) < 0.0001);
            assertTrue(matrixEquals4(mProduct, identity));
        }

    }

    @Test
    public void testMakeBasis_extractBasis() {
        Vector3[] identityBasis = new Vector3[]{new Vector3(1, 0, 0), new Vector3(0, 1, 0), new Vector3(0, 0, 1)};
        Matrix4 a = new Matrix4().makeBasis(identityBasis[0], identityBasis[1], identityBasis[2]);
        Matrix4 identity = new Matrix4();
        assertTrue(matrixEquals4(a, identity));
        Vector3[][] testBases = new Vector3[][]{{new Vector3(0, 1, 0), new Vector3(-1, 0, 0), new Vector3(0, 0, 1)}};
        for (int i = 0; i < testBases.length; i++) {
            Vector3[] testBasis = testBases[i];
            Matrix4 b = new Matrix4().makeBasis(testBasis[0], testBasis[1], testBasis[2]);
            Vector3[] outBasis = new Vector3[]{new Vector3(), new Vector3(), new Vector3()};
            b.extractBasis(outBasis[0], outBasis[1], outBasis[2]);
            // check what goes in, is what comes out.
            for (int j = 0; j < outBasis.length; j++) {
                assertTrue(outBasis[j].equals(testBasis[j]));
            }

            // get the basis out the hard war
            for (int j = 0; j < identityBasis.length; j++) {
                outBasis[j].copy(identityBasis[j]);
                outBasis[j].applyMatrix4(b);
            }
            // did the multiply method of basis extraction work?
            for (int j = 0; j < outBasis.length; j++) {
                assertTrue(outBasis[j].equals(testBasis[j]));
            }
        }
    }

    @Test
    public void testTranspose() {
        Matrix4 a = new Matrix4();
        Matrix4 b = a.clone().transpose();
        assertTrue(matrixEquals4(a, b));
        b = new Matrix4().set(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        Matrix4 c = b.clone().transpose();
        assertTrue(!matrixEquals4(b, c));
        assertTrue(matrixEquals4(b, c));

    }

    @Test
    public void testClone() {
        Matrix4 a = new Matrix4().set(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        Matrix4 b = a.clone();
        assertTrue(matrixEquals4(a, b));
        a.elements.set(0, 2);
        assertTrue(!matrixEquals4(a, b));

    }

    @Test
    public void testCompose_decompose() {
        Vector3[] tValues = new Vector3[]{
                new Vector3(),
                new Vector3(3, 0, 0),
                new Vector3(0, 4, 0),
                new Vector3(0, 0, 5),
                new Vector3(-6, 0, 0),
                new Vector3(0, -7, 0),
                new Vector3(0, 0, -8),
                new Vector3(-2, 5, -9),
                new Vector3(-2, -5, -9)};
        Vector3[] sValues = new Vector3[]{
                new Vector3(1, 1, 1),
                new Vector3(2, 2, 2),
                new Vector3(1, -1, 1),
                new Vector3(-1, 1, 1),
                new Vector3(1, 1, -1),
                new Vector3(2, -2, 1),
                new Vector3(-1, 2, -2),
                new Vector3(-1, -1, -1),
                new Vector3(-2, -2, -2)};
        Quaternion[] rValues = new Quaternion[]{
                new Quaternion(),
                new Quaternion().setFromEuler(new Euler(1, 1, 0)),
                new Quaternion().setFromEuler(new Euler(1, -1, 1)),
                new Quaternion(0, 0.9238795292366128, 0, 0.38268342717215614)};
        for (int ti = 0; ti < tValues.length; ti++) {
            for (int si = 0; si < sValues.length; si++) {
                for (int ri = 0; ri < rValues.length; ri++) {
                    Vector3 t = tValues[ti];
                    Vector3 s = sValues[si];
                    Quaternion r = rValues[ri];

                    Matrix4 m = new Matrix4().compose(t, r, s);
                    Vector3 t2 = new Vector3();
                    Quaternion r2 = new Quaternion();
                    Vector3 s2 = new Vector3();

                    m.decompose(t2, r2, s2);

                    Matrix4 m2 = new Matrix4().compose(t2, r2, s2);

                    assertTrue(matrixEquals4(m, m2));

                }
            }
        }
    }

}
