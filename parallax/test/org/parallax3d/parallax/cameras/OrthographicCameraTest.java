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

package org.parallax3d.parallax.cameras;

import org.junit.Test;
import org.parallax3d.parallax.graphics.cameras.OrthographicCamera;
import org.parallax3d.parallax.system.ThreejsTest;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import static org.junit.Assert.assertTrue;

@ThreejsTest("OrthographicCamera")
public class OrthographicCameraTest {
    @Test
    public void testUpdateProjectionMatrix() {
        double left = -1;
        double right = 1;
        double top = 1;
        double bottom = -1;
        double near = 1;
        double far = 3;
        OrthographicCamera cam = new OrthographicCamera(left, right, top, bottom, near, far);
        Float32Array pMatrix = cam.getProjectionMatrix().getArray();
        assertTrue(pMatrix.get(0) == 2 / (right - left));
        assertTrue(pMatrix.get(5) == 2 / (top - bottom));
        assertTrue(pMatrix.get(10) == (-2) / (far - near));
        assertTrue(pMatrix.get(12) == -(right + left) / (right - left));
        assertTrue(pMatrix.get(13) == -(top + bottom) / (top - bottom));
        assertTrue(pMatrix.get(14) == -(far + near) / (far - near));

    }

    @Test
    public void testClone() {
        double left = -1.5;
        double right = 1.5;
        double top = 1;
        double bottom = -1;
        double near = 0.1;
        double far = 42;
        OrthographicCamera cam = new OrthographicCamera(left, right, top, bottom, near, far);
        OrthographicCamera clonedCam = cam.clone();
        assertTrue(cam.getLeft() == clonedCam.getLeft());
        assertTrue(cam.getRight() == clonedCam.getRight());
        assertTrue(cam.getTop() == clonedCam.getTop());
        assertTrue(cam.getBottom() == clonedCam.getBottom());
        assertTrue(cam.getNear() == clonedCam.getNear());
        assertTrue(cam.getFar() == clonedCam.getFar());
        assertTrue(cam.getZoom() == clonedCam.getZoom());

    }

}
