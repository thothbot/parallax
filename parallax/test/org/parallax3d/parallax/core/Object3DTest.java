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
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.parallax3d.parallax.math.Constants.DELTA;

@ThreejsTest("Object3D")
public class Object3DTest {

    static final double RadToDeg = 180 / Math.PI;

    static void checkIfPropsAreEqual(Vector3 reference, Vector3 obj) {
        assertTrue(obj.getX() == reference.getX());
        assertTrue(obj.getY() == reference.getY());
        assertTrue(obj.getZ() == reference.getZ());
    }

    // since float equal checking is a mess in js, one solution is to cut off
    // decimal places
    static void checkIfFloatsAreEqual(double f1, double f2) {
        double f1Rounded = (f1 * 1000) / 1000.;
        double f2Rounded = (f2 * 1000) / 1000.;

        assertEquals(f1Rounded, f2Rounded, DELTA);
    }

    @Test
    public void testRotateX() {
        Object3D obj = new Object3D();
        double angleInRad = 1.562;
        obj.rotateX(angleInRad);
        checkIfFloatsAreEqual(obj.getRotation().getX(), angleInRad);

    }

    @Test
    public void testRotateY() {
        Object3D obj = new Object3D();
        double angleInRad = -0.346;
        obj.rotateY(angleInRad);
        checkIfFloatsAreEqual(obj.getRotation().getY(), angleInRad);

    }

    @Test
    public void testRotateZ() {
        Object3D obj = new Object3D();
        double angleInRad = 1;
        obj.rotateZ(angleInRad);
        checkIfFloatsAreEqual(obj.getRotation().getZ(), angleInRad);

    }

    @Test
    public void testTranslateOnAxis() {
        Object3D obj = new Object3D();
        Vector3 reference = new Vector3(1, 1.23, -4.56);
        obj.translateOnAxis(new Vector3(1, 0, 0), 1);
        obj.translateOnAxis(new Vector3(0, 1, 0), 1.23);
        obj.translateOnAxis(new Vector3(0, 0, 1), -4.56);
        checkIfPropsAreEqual(reference, obj.getPosition());

    }

    @Test
    public void testTranslateX() {
        Object3D obj = new Object3D();
        obj.translateX(1.234);
        assertTrue(obj.getPosition().getX() == 1.234);

    }

    @Test
    public void testTranslateY() {
        Object3D obj = new Object3D();
        obj.translateY(1.234);
        assertTrue(obj.getPosition().getY() == 1.234);

    }

    @Test
    public void testTranslateZ() {
        Object3D obj = new Object3D();
        obj.translateZ(1.234);
        assertTrue(obj.getPosition().getZ() == 1.234);

    }

    @Test
    public void testLookAt() {
        Object3D obj = new Object3D();
        obj.lookAt(new Vector3(0, -1, 1));
        assertTrue(obj.getRotation().getX() * RadToDeg == 45);

    }

    @Test
    public void testGetWorldRotation() {
        Object3D obj = new Object3D();
        obj.lookAt(new Vector3(0, -1, 1));
        assertTrue(obj.getWorldRotation().getX() * RadToDeg == 45);
        obj.lookAt(new Vector3(1, 0, 0));
        assertTrue(obj.getWorldRotation().getY() * RadToDeg == 90);

    }

}
