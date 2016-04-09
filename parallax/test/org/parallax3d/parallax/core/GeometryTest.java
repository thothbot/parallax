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
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsTest;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

@ThreejsTest("Geometry")
public class GeometryTest {

    Geometry getGeometryByParams(double x1, double y1, double z1,
                                 double x2, double y2, double z2,
                                 double x3, double y3, double z3) {
        Geometry geometry = new Geometry();

        // a triangle
        geometry.setVertices(Arrays.asList(
                new Vector3(x1, y1, z1),
                new Vector3(x2, y2, z2),
                new Vector3(x3, y3, z3)
        ));

        return geometry;
    }

    Geometry getGeometry() {
        return getGeometryByParams(-0.5, 0, 0, 0.5, 0, 0, 0, 1, 0);
    }

    @Test
    public void testRotateX() {
        Geometry geometry = getGeometry();

        Matrix4 matrix = new Matrix4();
        matrix.makeRotationX(Math.PI / 2);

        geometry.applyMatrix(matrix);

        Vector3 v0 = geometry.getVertices().get(0);
        Vector3 v1 = geometry.getVertices().get(1);
        Vector3 v2 = geometry.getVertices().get(2);
        assertTrue(v0.getX() == -0.5 && v0.getY() == 0 && v0.getZ() == 0);
        assertTrue(v1.getX() == 0.5 && v1.getY() == 0 && v1.getZ() == 0);
        assertTrue(v2.getX() == 0 && v2.getY() < Mathematics.EPSILON && v2.getZ() == 1);

    }

    @Test
    public void testRotateY() {
        Geometry geometry = getGeometry();

        Matrix4 matrix = new Matrix4();
        matrix.makeRotationY(Math.PI);

        geometry.applyMatrix(matrix);

        Vector3 v0 = geometry.getVertices().get(0);
        Vector3 v1 = geometry.getVertices().get(1);
        Vector3 v2 = geometry.getVertices().get(2);
        assertTrue(v0.getX() == 0.5 && v0.getY() == 0 && v0.getZ() < Mathematics.EPSILON);
        assertTrue(v1.getX() == -0.5 && v1.getY() == 0 && v1.getZ() < Mathematics.EPSILON);
        assertTrue(v2.getX() == 0 && v2.getY() == 1 && v2.getZ() == 0);

    }

    @Test
    public void testRotateZ() {
        Geometry geometry = getGeometry();

        Matrix4 matrix = new Matrix4();
        matrix.makeRotationZ(Math.PI / 2 * 3);

        geometry.applyMatrix(matrix);

        Vector3 v0 = geometry.getVertices().get(0);
        Vector3 v1 = geometry.getVertices().get(1);
        Vector3 v2 = geometry.getVertices().get(2);
        assertTrue(v0.getX() < Mathematics.EPSILON && v0.getY() == 0.5 && v0.getZ() == 0);
        assertTrue(v1.getX() < Mathematics.EPSILON && v1.getY() == -0.5 && v1.getZ() == 0);
        assertTrue(v2.getX() == 1 && v2.getY() < Mathematics.EPSILON && v2.getZ() == 0);

    }

    @Test
    public void testFromBufferGeometry() {
        BufferGeometry bufferGeometry = new BufferGeometry();
        bufferGeometry.addAttribute("position", new BufferAttribute(Float32Array.create(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9}), 3));
        bufferGeometry.addAttribute("color", new BufferAttribute(Float32Array.create(new double[]{0, 0, 0, 0.5, 0.5, 0.5, 1, 1, 1}), 3));
        bufferGeometry.addAttribute("normal", new BufferAttribute(Float32Array.create(new double[]{0, 1, 0, 1, 0, 1, 1, 1, 0}), 3));
        bufferGeometry.addAttribute("uv", new BufferAttribute(Float32Array.create(new double[]{0, 0, 0, 1, 1, 1}), 2));
        bufferGeometry.addAttribute("uv2", new BufferAttribute(Float32Array.create(new double[]{0, 0, 0, 1, 1, 1}), 2));

        Geometry geometry = new Geometry().fromBufferGeometry(bufferGeometry);

        List<Color> colors = geometry.getColors();
        assertTrue(colors.get(0).getR() == 0 && colors.get(0).getG() == 0 && colors.get(0).getB() == 0
                && colors.get(1).getR() == 0.5 && colors.get(1).getG() == 0.5 && colors.get(1).getB() == 0.5
                && colors.get(2).getR() == 1 && colors.get(2).getG() == 1 && colors.get(2).getB() == 1);

        List<Vector3> vertices = geometry.getVertices();
        assertTrue(vertices.get(0).getX() == 1 && vertices.get(0).getY() == 2 && vertices.get(0).getZ() == 3
                && vertices.get(1).getX() == 4 && vertices.get(1).getY() == 5 && vertices.get(1).getZ() == 6
                && vertices.get(2).getX() == 7 && vertices.get(2).getY() == 8 && vertices.get(2).getZ() == 9);

        List<Vector3> vNormals = geometry.getFaces().get(0).getVertexNormals();
        assertTrue(vNormals.get(0).getX() == 0 && vNormals.get(0).getY() == 1 && vNormals.get(0).getZ() == 0
                && vNormals.get(1).getX() == 1 && vNormals.get(1).getY() == 0 && vNormals.get(1).getZ() == 1
                && vNormals.get(2).getX() == 1 && vNormals.get(2).getY() == 1 && vNormals.get(2).getZ() == 0);

    }

    @Test
    public void testNormalize() {
        Geometry geometry = getGeometry();
        geometry.computeLineDistances();

        List<Double> distances = geometry.getLineDistances();
        assertTrue(distances.get(0) == 0);
        assertTrue(distances.get(1) == 1 + distances.get(0));
        assertTrue(distances.get(2) == Math.sqrt(0.5 * 0.5 + 1) + distances.get(1));

    }

}
