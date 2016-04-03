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

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.parallax3d.parallax.math.Constants.*;

@ThreejsTest("Box3")
public class Box3Test {
    @Test
    public void testConstructor() {
        Box3 a = new Box3();
        assertTrue(a.min.equals(posInf3));
        assertTrue(a.max.equals(negInf3));
        a = new Box3(zero3.clone(), zero3.clone());
        assertTrue(a.min.equals(zero3));
        assertTrue(a.max.equals(zero3));
        a = new Box3(zero3.clone(), one3.clone());
        assertTrue(a.min.equals(zero3));
        assertTrue(a.max.equals(one3));

    }

    @Test
    public void testCopy() {
        Box3 a = new Box3(zero3.clone(), one3.clone());
        Box3 b = new Box3().copy(a);
        assertTrue(b.min.equals(zero3));
        assertTrue(b.max.equals(one3));
        a.min = zero3;
        a.max = one3;
        assertTrue(b.min.equals(zero3));
        assertTrue(b.max.equals(one3));

    }

    @Test
    public void testSet() {
        Box3 a = new Box3();
        a.set(zero3, one3);
        assertTrue(a.min.equals(zero3));
        assertTrue(a.max.equals(one3));

    }

    @Test
    public void testSetFromPoints() {
        Box3 a = new Box3();
        a.setFromPoints(Arrays.asList(zero3, one3, two3));
        assertTrue(a.min.equals(zero3));
        assertTrue(a.max.equals(two3));
        assertTrue(a.min.equals(one3));
        assertTrue(a.max.equals(one3));
        assertTrue(a.isEmpty());
    }

    @Test
    public void testEmpty_makeEmpty() {
        Box3 a = new Box3();
        assertTrue(a.isEmpty());
        a = new Box3(zero3.clone(), one3.clone());
        assertTrue(!a.isEmpty());
        assertTrue(a.isEmpty());

    }

    @Test
    public void testCenter() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        assertTrue(a.center().equals(zero3));
        a = new Box3(zero3.clone(), one3.clone());
        Vector3 midpoint = one3.clone().multiplyScalar(0.5);
        assertTrue(a.center().equals(midpoint));

    }

    @Test
    public void testSize() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        assertTrue(a.size().equals(zero3));
        a = new Box3(zero3.clone(), one3.clone());
        assertTrue(a.size().equals(one3));

    }

    @Test
    public void testExpandByPoint() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        a.expandByPoint(zero3);
        assertTrue(a.size().equals(zero3));
        assertTrue(a.size().equals(one3));
        assertTrue(a.size().equals(one3.clone().multiplyScalar(2)));
        assertTrue(a.center().equals(zero3));

    }

    @Test
    public void testExpandByVector() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        a.expandByVector(zero3);
        assertTrue(a.size().equals(zero3));
        assertTrue(a.size().equals(one3.clone().multiplyScalar(2)));
        assertTrue(a.center().equals(zero3));

    }

    @Test
    public void testExpandByScalar() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        a.expandByScalar(0);
        assertTrue(a.size().equals(zero3));
        assertTrue(a.size().equals(one3.clone().multiplyScalar(2)));
        assertTrue(a.center().equals(zero3));

    }

    @Test
    public void testContainsPoint() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        assertTrue(a.containsPoint(zero3));
        assertTrue(!a.containsPoint(one3));
        assertTrue(a.containsPoint(zero3));
        assertTrue(a.containsPoint(one3));
        assertTrue(a.containsPoint(one3.clone().negate()));

    }

    @Test
    public void testContainsBox() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        Box3 b = new Box3(zero3.clone(), one3.clone());
        Box3 c = new Box3(one3.clone().negate(), one3.clone());
        assertTrue(a.containsBox(a));
        assertTrue(!a.containsBox(b));
        assertTrue(!a.containsBox(c));
        assertTrue(b.containsBox(a));
        assertTrue(c.containsBox(a));
        assertTrue(!b.containsBox(c));

    }

    @Test
    public void testGetParameter() {
        Box3 a = new Box3(zero3.clone(), one3.clone());
        Box3 b = new Box3(one3.clone().negate(), one3.clone());
        assertTrue(a.getParameter(new Vector3(0, 0, 0)).equals(new Vector3(0, 0, 0)));
        assertTrue(a.getParameter(new Vector3(1, 1, 1)).equals(new Vector3(1, 1, 1)));
        assertTrue(b.getParameter(new Vector3(-1, -1, -1)).equals(new Vector3(0, 0, 0)));
        assertTrue(b.getParameter(new Vector3(0, 0, 0)).equals(new Vector3(0.5, 0.5, 0.5)));
        assertTrue(b.getParameter(new Vector3(1, 1, 1)).equals(new Vector3(1, 1, 1)));

    }

    @Test
    public void testClampPoint() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        Box3 b = new Box3(one3.clone().negate(), one3.clone());
        assertTrue(a.clampPoint(new Vector3(0, 0, 0)).equals(new Vector3(0, 0, 0)));
        assertTrue(a.clampPoint(new Vector3(1, 1, 1)).equals(new Vector3(0, 0, 0)));
        assertTrue(a.clampPoint(new Vector3(-1, -1, -1)).equals(new Vector3(0, 0, 0)));
        assertTrue(b.clampPoint(new Vector3(2, 2, 2)).equals(new Vector3(1, 1, 1)));
        assertTrue(b.clampPoint(new Vector3(1, 1, 1)).equals(new Vector3(1, 1, 1)));
        assertTrue(b.clampPoint(new Vector3(0, 0, 0)).equals(new Vector3(0, 0, 0)));
        assertTrue(b.clampPoint(new Vector3(-1, -1, -1)).equals(new Vector3(-1, -1, -1)));
        assertTrue(b.clampPoint(new Vector3(-2, -2, -2)).equals(new Vector3(-1, -1, -1)));

    }

    @Test
    public void testDistanceToPoint() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        Box3 b = new Box3(one3.clone().negate(), one3.clone());
        assertTrue(a.distanceToPoint(new Vector3(0, 0, 0)) == 0);
        assertTrue(a.distanceToPoint(new Vector3(1, 1, 1)) == Math.sqrt(3));
        assertTrue(a.distanceToPoint(new Vector3(-1, -1, -1)) == Math.sqrt(3));
        assertTrue(b.distanceToPoint(new Vector3(2, 2, 2)) == Math.sqrt(3));
        assertTrue(b.distanceToPoint(new Vector3(1, 1, 1)) == 0);
        assertTrue(b.distanceToPoint(new Vector3(0, 0, 0)) == 0);
        assertTrue(b.distanceToPoint(new Vector3(-1, -1, -1)) == 0);
        assertTrue(b.distanceToPoint(new Vector3(-2, -2, -2)) == Math.sqrt(3));

    }

    @Test
    public void testIntersectsBox() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        Box3 b = new Box3(zero3.clone(), one3.clone());
        Box3 c = new Box3(one3.clone().negate(), one3.clone());
        assertTrue(a.intersectsBox(a));
        assertTrue(a.intersectsBox(b));
        assertTrue(a.intersectsBox(c));
        assertTrue(b.intersectsBox(a));
        assertTrue(c.intersectsBox(a));
        assertTrue(b.intersectsBox(c));
        b.translate(new Vector3(2, 2, 2));
        assertTrue(!a.intersectsBox(b));
        assertTrue(!b.intersectsBox(a));
        assertTrue(!b.intersectsBox(c));

    }

    @Test
    public void testIntersectsSphere() {
        Box3 a = new Box3(zero3.clone(), one3.clone());
        Sphere b = new Sphere(zero3.clone(), 1);
        assertTrue(a.intersectsSphere(b));
        b.translate(new Vector3(2, 2, 2));
        assertTrue(!a.intersectsSphere(b));

    }

    @Test
    public void testIntersectsPlane() {
        Box3 a = new Box3(zero3.clone(), one3.clone());
        Plane b = new Plane(new Vector3(0, 1, 0), 1);
        Plane c = new Plane(new Vector3(0, 1, 0), 1.25);
        Plane d = new Plane(new Vector3(0, -1, 0), 1.25);
        assertTrue(a.intersectsPlane(b));
        assertTrue(!a.intersectsPlane(c));
        assertTrue(!a.intersectsPlane(d));

    }

    @Test
    public void testGetBoundingSphere() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        Box3 b = new Box3(zero3.clone(), one3.clone());
        Box3 c = new Box3(one3.clone().negate(), one3.clone());
        assertTrue(a.getBoundingSphere().equals(new Sphere(zero3, 0)));
        assertTrue(b.getBoundingSphere().equals(new Sphere(one3.clone().multiplyScalar(0.5), Math.sqrt(3) * 0.5)));
        assertTrue(c.getBoundingSphere().equals(new Sphere(zero3, Math.sqrt(12) * 0.5)));

    }

    @Test
    public void testIntersect() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        Box3 b = new Box3(zero3.clone(), one3.clone());
        Box3 c = new Box3(one3.clone().negate(), one3.clone());
        assertTrue((a.clone().intersect(a)).equals(a));
        assertTrue((a.clone().intersect(b)).equals(a));
        assertTrue((b.clone().intersect(b)).equals(b));
        assertTrue((a.clone().intersect(c)).equals(a));
        assertTrue((b.clone().intersect(c)).equals(b));
        assertTrue((c.clone().intersect(c)).equals(c));

    }

    @Test
    public void testUnion() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        Box3 b = new Box3(zero3.clone(), one3.clone());
        Box3 c = new Box3(one3.clone().negate(), one3.clone());
        assertTrue((a.clone().union(a)).equals(a));
        assertTrue((a.clone().union(b)).equals(b));
        assertTrue((a.clone().union(c)).equals(c));
        assertTrue((b.clone().union(c)).equals(c));

    }

    @Test
    public void testApplyMatrix4() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        Box3 b = new Box3(zero3.clone(), one3.clone());
        Box3 c = new Box3(one3.clone().negate(), one3.clone());
        Box3 d = new Box3(one3.clone().negate(), zero3.clone());
        Matrix4 m = new Matrix4().makeTranslation(1, -2, 1);
        Vector3 t1 = new Vector3(1, -2, 1);
        assertTrue(compareBox(a.clone().applyMatrix4(m), a.clone().translate(t1)));
        assertTrue(compareBox(b.clone().applyMatrix4(m), b.clone().translate(t1)));
        assertTrue(compareBox(c.clone().applyMatrix4(m), c.clone().translate(t1)));
        assertTrue(compareBox(d.clone().applyMatrix4(m), d.clone().translate(t1)));

    }

    @Test
    public void testTranslate() {
        Box3 a = new Box3(zero3.clone(), zero3.clone());
        Box3 b = new Box3(zero3.clone(), one3.clone());
        Box3 c = new Box3(one3.clone().negate(), one3.clone());
        Box3 d = new Box3(one3.clone().negate(), zero3.clone());
        assertTrue((a.clone().translate(one3)).equals(new Box3(one3, one3)));
        assertTrue(((a.clone().translate(one3)).translate(one3.clone().negate())).equals(a));
        assertTrue((d.clone().translate(one3)).equals(b));
        assertTrue((b.clone().translate(one3.clone().negate())).equals(d));

    }

    static boolean compareBox(Box3 a, Box3 b) {
        double threshold = 0.0001;
        return (a.min.distanceTo(b.min) < threshold &&
                a.max.distanceTo(b.max) < threshold);
    }
}
