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
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.system.ThreejsTest;

import static org.junit.Assert.assertTrue;

@ThreejsTest("PerspectiveCamera")
public class PerspectiveCameraTest {
    @Test
    public void testUpdateProjectionMatrix() {
        PerspectiveCamera cam = new PerspectiveCamera(75, 16 / 9, 0.1, 300.0);
        Matrix4 m = cam.getProjectionMatrix();
        Matrix4 reference = new Matrix4().set(0.7330642938613892, 0, 0, 0, 0, 1.3032253980636597, 0, 0, 0, 0, -1.000666856765747, -0.2000666856765747, 0, 0, -1, 0);

        System.out.println(m + " " + reference);
        assertTrue(reference.equals(m));

    }

    @Test
    public void testClone() {
        double near = 1;
        double far = 3;
        double bottom = -1;
        double top = 1;
        double aspect = 16 / 9;
        double fov = 90;
        PerspectiveCamera cam = new PerspectiveCamera(fov, aspect, near, far);
        PerspectiveCamera clonedCam = cam.clone();
        assertTrue(cam.getFov() == clonedCam.getFov());
        assertTrue(cam.getAspect() == clonedCam.getAspect());
        assertTrue(cam.getNear() == clonedCam.getNear());
        assertTrue(cam.getFar() == clonedCam.getFar());
        assertTrue(cam.getZoom() == clonedCam.getZoom());
        assertTrue(cam.getProjectionMatrix().equals(clonedCam.getProjectionMatrix()));

    }

}
