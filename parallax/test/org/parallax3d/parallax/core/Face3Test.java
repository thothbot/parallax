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
import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

@ThreejsTest("Face3")
public class Face3Test {
    @Test
    public void testCopy1() {
        Face3 instance = new Face3(0, 1, 2, new Vector3(0, 1, 0), new Color(0.25, 0.5, 0.75), 2);
        Face3 copiedInstance = instance.copy(instance);
        checkCopy(copiedInstance);
        checkVertexAndColors(copiedInstance);

    }

    @Test
    public void testCopy2() {
        Face3 instance = new Face3(0, 1, 2, new ArrayList<>(Arrays.asList(new Vector3(0, 1, 0), new Vector3(1, 0, 1))),
                new ArrayList<>(Arrays.asList(new Color(0.25, 0.5, 0.75), new Color(1, 0, 0.4))), 2);
        Face3 copiedInstance = instance.copy(instance);
        checkCopy(copiedInstance);
        checkVertexAndColorArrays(copiedInstance);

    }

    @Test
    public void testClone() {
        Face3 instance = new Face3(0, 1, 2, new Vector3(0, 1, 0), new Color(0.25, 0.5, 0.75), 2);
        Face3 copiedInstance = instance.clone();
        checkCopy(copiedInstance);
        checkVertexAndColors(copiedInstance);

    }

    private static void checkCopy(Face3 copiedInstance) {
        assertTrue("copy created the correct type", copiedInstance instanceof Face3);
        assertTrue("properties where copied",
                copiedInstance.getA() == 0 &&
                        copiedInstance.getB() == 1 &&
                        copiedInstance.getC() == 2 &&
                        copiedInstance.getMaterialIndex() == 2);
    }

    private static void checkVertexAndColors(Face3 copiedInstance) {
        assertTrue("properties where copied",
                copiedInstance.getNormal().getX() == 0 && copiedInstance.getNormal().getY() == 1 && copiedInstance.getNormal().getZ() == 0 &&
                        copiedInstance.getColor().getR() == 0.25 && copiedInstance.getColor().getG() == 0.5 && copiedInstance.getColor().getB() == 0.75);
    }

    private static void checkVertexAndColorArrays(Face3 copiedInstance) {
        assertTrue(copiedInstance.getVertexNormals().get(0).getX() == 0 && copiedInstance.getVertexNormals().get(0).getY() == 1 && copiedInstance.getVertexNormals().get(0).getZ() == 0);
        System.out.println(copiedInstance.getVertexNormals().get(1));
        assertTrue(copiedInstance.getVertexNormals().get(1).getX() == 1 && copiedInstance.getVertexNormals().get(1).getY() == 0 && copiedInstance.getVertexNormals().get(1).getZ() == 1);
        assertTrue(copiedInstance.getVertexColors().get(0).getR() == 0.25 && copiedInstance.getVertexColors().get(0).getG() == 0.5 && copiedInstance.getVertexColors().get(0).getB() == 0.75);
        assertTrue(copiedInstance.getVertexColors().get(1).getR() == 1 && copiedInstance.getVertexColors().get(1).getG() == 0 && copiedInstance.getVertexColors().get(1).getB() == 0.4);
    }
}
