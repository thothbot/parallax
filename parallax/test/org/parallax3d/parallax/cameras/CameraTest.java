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
import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsTest;

import static org.junit.Assert.assertTrue;

@ThreejsTest("Camera")
public class CameraTest {
    @Test
    public void testLookAt() {
        Camera cam = new Camera();
        cam.lookAt(new Vector3(0, 1, -1));
        assertTrue(cam.getRotation().getX() * (180 / Math.PI) == 45);

    }

    @Test
    public void testClone() {
        Camera cam = new Camera();

        cam.getMatrixWorldInverse().set(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        cam.getProjectionMatrix().set(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

        Camera clonedCam = cam.clone();

        assertTrue(cam.getMatrixWorldInverse().equals(clonedCam.getMatrixWorldInverse()));
        assertTrue(cam.getProjectionMatrix().equals(clonedCam.getProjectionMatrix()));

    }

}
