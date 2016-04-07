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
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.math.*;
import org.parallax3d.parallax.system.ThreejsTest;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

@ThreejsTest("BufferGeometry")
public class BufferGeometryTest {

    static final double DegToRad = Math.PI / 180.;

    @Test
    public void testAdd_delete_Attribute() {
        BufferGeometry geometry = new BufferGeometry();
        String attributeName = "position";
        assertTrue(!geometry.getAttributes().containsKey(attributeName));

        geometry.addAttribute(attributeName, new BufferAttribute(Float32Array.create(new double[]{1, 2, 3}), 1));
        assertTrue(geometry.getAttributes().get(attributeName) != null);

        geometry.removeAttribute(attributeName);
        assertTrue(!geometry.getAttributes().containsKey(attributeName));

    }

    @Test
    public void testApplyMatrix() {
        BufferGeometry geometry = new BufferGeometry();
        geometry.addAttribute("position", new BufferAttribute(Float32Array.create(6), 3));
        Matrix4 matrix = new Matrix4().set(1, 0, 0, 1.5, 0, 1, 0, -2, 0, 0, 1, 3, 0, 0, 0, 1);
        geometry.applyMatrix(matrix);
        Float32Array position = (Float32Array) geometry.getAttributes().get("position").getArray();
        Float32Array m = matrix.getArray();
        assertTrue(position.get(0) == m.get(12) && position.get(1) == m.get(13) && position.get(2) == m.get(14));
        assertTrue(position.get(3) == m.get(12) && position.get(4) == m.get(13) && position.get(5) == m.get(14));
        assertTrue(geometry.getAttributes().get("position").getVersion() == 1);

    }

    @Test
    public void testRotateX_Y_Z() {
        BufferGeometry geometry = new BufferGeometry();
        geometry.addAttribute("position", new BufferAttribute(Float32Array.create(new double[]{1, 2, 3, 4, 5, 6}),3));
        Float32Array pos = (Float32Array) geometry.getAttributes().get("position").getArray();

        geometry.rotateX(180 * DegToRad);
        assertTrue(pos.get(0) == 1 && pos.get(1) == -2 && pos.get(2) == -3
                && pos.get(3) == 4 && pos.get(4) == -5 && pos.get(5) == -6);

        geometry.rotateY( 180 * DegToRad );
        assertTrue(pos.get(0) == -1 && pos.get(1) == -2 && pos.get(2) == 3
                && pos.get(3) == -4 && pos.get(4) == -5 && pos.get(5) == 6);

        geometry.rotateZ( 180 * DegToRad );
        assertTrue(pos.get(0) == 1 && pos.get(1) == 2 && pos.get(2) == 3
                && pos.get(3) == 4 && pos.get(4) == 5 && pos.get(5) == 6);

    }

    @Test
    public void testTranslate() {
        BufferGeometry geometry = new BufferGeometry();
        geometry.addAttribute("position", new BufferAttribute(Float32Array.create(new double[]{1, 2, 3, 4, 5, 6}), 3));
        Float32Array pos = (Float32Array) geometry.getAttributes().get("position").getArray();
        geometry.translate(10, 20, 30);
        assertTrue(pos.get(0) == 11 && pos.get(1) == 22 && pos.get(2) == 33 && pos.get(3) == 14 && pos.get(4) == 25 && pos.get(5) == 36);

    }

    @Test
    public void testScale() {
        BufferGeometry geometry = new BufferGeometry();
        geometry.addAttribute("position", new BufferAttribute(Float32Array.create(new double[]{-1, -1, -1, 2, 2, 2}), 3));
        Float32Array pos = (Float32Array) geometry.getAttributes().get("position").getArray();
        geometry.scale(1, 2, 3);
        assertTrue(pos.get(0) == -1 && pos.get(1) == -2 && pos.get(2) == -3 && pos.get(3) == 2 && pos.get(4) == 4 && pos.get(5) == 6);

    }

    @Test
    public void testCenter() {
        BufferGeometry geometry = new BufferGeometry();
        geometry.addAttribute("position", new BufferAttribute(Float32Array.create(new double[]{-1, -1, -1, 1, 1, 1, 4, 4, 4}), 3));
        geometry.center();
        Float32Array pos = (Float32Array) geometry.getAttributes().get("position").getArray();
        Box3 bb = geometry.getBoundingBox();
        assertTrue(pos.get(0) == -2.5 && pos.get(1) == -2.5 && pos.get(2) == -2.5 && pos.get(3) == -0.5 && pos.get(4) == -0.5 && pos.get(5) == -0.5 && pos.get(6) == 2.5 && pos.get(7) == 2.5 && pos.get(8) == 2.5);

    }

    @Test
    public void testSetFromObject() {
        Geometry lineGeo = new Geometry();
        lineGeo.getVertices().addAll(Arrays.asList(new Vector3(-10, 0, 0), new Vector3(0, 10, 0), new Vector3(10, 0, 0)));
        lineGeo.getColors().addAll(Arrays.asList(new Color(1, 0, 0), new Color(0, 1, 0), new Color(0, 0, 1)));
        Line line = new Line(lineGeo, null);
        BufferGeometry geometry = new BufferGeometry().setFromObject(line);
        Float32Array pos = (Float32Array) geometry.getAttributes().get("position").getArray();
        Float32Array col = (Float32Array) geometry.getAttributes().get("color").getArray();
        List<Vector3> v = lineGeo.getVertices();
        List<Color> c = lineGeo.getColors();
        assertTrue(pos != null && v.size() * 3 == pos.getLength()
                && geometry.getAttributes().get("position").getCount() == 3
                && pos.get(0) == v.get(0).getX() && pos.get(1) == v.get(0).getY() && pos.get(2) == v.get(0).getZ()
                && pos.get(3) == v.get(1).getX() && pos.get(4) == v.get(1).getY() && pos.get(5) == v.get(1).getZ()
                && pos.get(6) == v.get(2).getX() && pos.get(7) == v.get(2).getY() && pos.get(8) == v.get(2).getZ());
        assertTrue(col != null && c.size() * 3 == col.getLength()
                && geometry.getAttributes().get("color").getCount() == 3
                && col.get(0) == c.get(0).getR() && col.get(1) == c.get(0).getG()
                && col.get(2) == c.get(0).getB() && col.get(3) == c.get(1).getR()
                && col.get(4) == c.get(1).getG() && col.get(5) == c.get(1).getB()
                && col.get(6) == c.get(2).getR() && col.get(7) == c.get(2).getG()
                && col.get(8) == c.get(2).getB());

    }

    @Test
    public void testComputeBoundingBox() {
        Box3 bb = getBBForVertices(new double[]{-1, -2, -3, 13, -2, -3.5, -1, -20, 0, -4, 5, 6});
        assertTrue(bb.getMin().getX() == -4 && bb.getMin().getY() == -20 && bb.getMin().getZ() == -3.5);
        assertTrue(bb.getMax().getX() == 13 && bb.getMax().getY() == 5 && bb.getMax().getZ() == 6);

        bb = getBBForVertices( new double[]{} );
        assertTrue(bb.getMin().getX() == 0 && bb.getMin().getY() == 0 && bb.getMin().getZ() == 0);
        assertTrue(bb.getMax().getX() == 0 && bb.getMax().getY() == 0 && bb.getMax().getZ() == 0);

        bb = getBBForVertices(new double[]{-1, -1, -1});
        assertTrue(bb.getMin().getX() == bb.getMax().getX() && bb.getMin().getY() == bb.getMax().getY() && bb.getMin().getZ() == bb.getMax().getZ());
        assertTrue(bb.getMin().getX() == -1 && bb.getMin().getY() == -1 && bb.getMin().getZ() == -1);

    }

    @Test
    public void testComputeBoundingSphere() {
        Sphere bs = getBSForVertices(new double[]{-10, 0, 0, 10, 0, 0});
        assertTrue(bs.getRadius() == (10 + 10) / 2);
        assertTrue(bs.getCenter().getX() == 0 && bs.getCenter().getY() == 0 && bs.getCenter().getY() == 0);

        bs = getBSForVertices(new double[]{-5, 11, -3, 5, -11, 3});
        double radius = new Vector3(5, 11, 3).length();
        assertTrue(bs.getRadius() == radius);
        assertTrue(bs.getCenter().getX() == 0 && bs.getCenter().getY() == 0 && bs.getCenter().getY() == 0);

    }

    @Test
    public void testComputeVertexNormals() {
        Float32Array normals = getNormalsForVertices(new double[]{-1, 0, 0, 1, 0, 0, 0, 1, 0});
        assertTrue(normals.get( 0 ) == 0 && normals.get( 1 ) == 0 && normals.get( 2 ) == 1);
        assertTrue(normals.get( 3 ) == 0 && normals.get( 4 ) == 0 && normals.get( 5 ) == 1);
        assertTrue(normals.get( 6 ) == 0 && normals.get( 7 ) == 0 && normals.get( 8 ) == 1);

        normals = getNormalsForVertices(new double[]{1, 0, 0, -1, 0, 0, 0, 1, 0});
        assertTrue(normals.get( 0 ) == 0 && normals.get( 1 ) == 0 && normals.get( 2 ) == -1);
        assertTrue(normals.get( 3 ) == 0 && normals.get( 4 ) == 0 && normals.get( 5 ) == -1);
        assertTrue(normals.get( 6 ) == 0 && normals.get( 7 ) == 0 && normals.get( 8 ) == -1);

        normals = getNormalsForVertices(new double[]{0, 0, 1, 0, 0, -1, 1, 1, 0});
        Vector3 direction = new Vector3(1, 1, 0).normalize();

        double difference = direction.dot(new Vector3(normals.get( 0 ), normals.get( 1 ), normals.get( 2 )));
        assertTrue(difference < Mathematics.EPSILON);

        normals = getNormalsForVertices(new double[]{1, 0, 0, -1, 0, 0});
        for (int i = 0; i < normals.getLength(); i++) {
            assertTrue ( "normals can't be calculated which is good",  Double.isNaN(normals.get(i)));
        }
    }

    @Test
    public void testMerge() {
        BufferGeometry geometry1 = new BufferGeometry();
        geometry1.addAttribute("attrName", new BufferAttribute(Float32Array.create(new double[]{1, 2, 3, 0, 0, 0}),3));
        BufferGeometry geometry2 = new BufferGeometry();
        geometry2.addAttribute("attrName", new BufferAttribute(Float32Array.create(new double[]{4, 5, 6}),3));
        Float32Array attr = (Float32Array) geometry1.getAttributes().get("attrName").getArray();
        geometry1.merge(geometry2, 1);

        // merged array should be 1, 2, 3, 4, 5, 6
        for (int i = 0; i < attr.getLength(); i++) {
            assertTrue( attr.get(i) == i + 1);
        }

        geometry1.merge(geometry2);
        assertTrue(attr.get(0) == 4 && attr.get(1) == 5 && attr.get(2) == 6);

    }

    @Test
    public void testCopy() {
        BufferGeometry geometry = new BufferGeometry();
        geometry.addAttribute("attrName", new BufferAttribute(Float32Array.create(new double[]{1, 2, 3, 4, 5, 6}),3));
        geometry.addAttribute("attrName2", new BufferAttribute(Float32Array.create(new double[]{0, 1, 3, 5, 6}),1));
        BufferGeometry copy = new BufferGeometry().copy(geometry);
        assertTrue(copy != geometry && geometry.getId() != copy.getId());

    }

    private static Box3 getBBForVertices(double[] vertices) {
        BufferGeometry geometry = new BufferGeometry();

        geometry.addAttribute("position", new BufferAttribute(Float32Array.create(vertices), 3));
        geometry.computeBoundingBox();

        return geometry.getBoundingBox();
    }

    private static Sphere getBSForVertices(double[] vertices) {
        BufferGeometry geometry = new BufferGeometry();

        geometry.addAttribute("position", new BufferAttribute(Float32Array.create(vertices), 3));
        geometry.computeBoundingSphere();

        return geometry.getBoundingSphere();
    }

    private static Float32Array getNormalsForVertices(double[] vertices) {
        BufferGeometry geometry = new BufferGeometry();

        geometry.addAttribute( "position", new BufferAttribute( Float32Array.create(vertices), 3 ) );

        geometry.computeVertexNormals();

        assertTrue(geometry.getAttributes().containsKey("normal"));

        return (Float32Array) geometry.getAttributes().get("normal").getArray();
    }
}
