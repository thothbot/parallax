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

@ThreejsTest("Box2")
public class Box2Test {
    @Test
    public void testConstructor() {
        Box2 a = new Box2();
        assertTrue(a.min.equals(posInf2));
        assertTrue(a.max.equals(negInf2));
        a = new Box2(zero2.clone(), zero2.clone());
        assertTrue(a.min.equals(zero2));
        assertTrue(a.max.equals(zero2));
        a = new Box2(zero2.clone(), one2.clone());
        assertTrue(a.min.equals(zero2));
        assertTrue(a.max.equals(one2));

    }

    @Test
    public void testCopy() {
        Box2 a = new Box2(zero2.clone(), one2.clone());
        Box2 b = new Box2().copy(a);
        assertTrue(b.min.equals(zero2));
        assertTrue(b.max.equals(one2));
        a.min = zero2;
        a.max = one2;
        assertTrue(b.min.equals(zero2));
        assertTrue(b.max.equals(one2));

    }

    @Test
    public void testSet() {
        Box2 a = new Box2();
        a.set(zero2, one2);
        assertTrue(a.min.equals(zero2));
        assertTrue(a.max.equals(one2));

    }

    @Test
    public void testSetFromPoints() {
        Box2 a = new Box2();
        a.setFromPoints(Arrays.asList( zero2, one2, two2 ));
        assertTrue(a.min.equals(zero2));
        assertTrue(a.max.equals(two2));
        assertTrue(a.min.equals(one2));
        assertTrue(a.max.equals(one2));
        assertTrue(a.isEmpty());

    }

    @Test
    public void testEmpty_makeEmpty() {
        Box2 a = new Box2();
        assertTrue(a.isEmpty());
        a = new Box2(zero2.clone(), one2.clone());
        assertTrue(!a.isEmpty());
        assertTrue(a.isEmpty());

    }

    @Test
    public void testCenter() {
        Box2 a = new Box2(zero2.clone(), zero2.clone());
        assertTrue(a.center().equals(zero2));
        a = new Box2(zero2, one2);
        Vector2 midpoint = one2.clone().multiplyScalar(0.5);
        assertTrue(a.center().equals(midpoint));

    }

    @Test
    public void testSize() {
        Box2 a = new Box2(zero2.clone(), zero2.clone());
        assertTrue(a.size().equals(zero2));
        a = new Box2(zero2.clone(), one2.clone());
        assertTrue(a.size().equals(one2));

    }

    @Test
    public void testExpandByPoint() {
        Box2 a = new Box2(zero2.clone(), zero2.clone());
        a.expandByPoint(zero2);
        assertTrue(a.size().equals(zero2));
        assertTrue(a.size().equals(one2));
        assertTrue(a.size().equals(one2.clone().multiplyScalar(2)));
        assertTrue(a.center().equals(zero2));

    }

    @Test
    public void testExpandByVector() {
        Box2 a = new Box2(zero2.clone(), zero2.clone());
        a.expandByVector(zero2);
        assertTrue(a.size().equals(zero2));
        assertTrue(a.size().equals(one2.clone().multiplyScalar(2)));
        assertTrue(a.center().equals(zero2));

    }

    @Test
    public void testExpandByScalar() {
        Box2 a = new Box2(zero2.clone(), zero2.clone());
        a.expandByScalar(0);
        assertTrue(a.size().equals(zero2));
        assertTrue(a.size().equals(one2.clone().multiplyScalar(2)));
        assertTrue(a.center().equals(zero2));

    }

    @Test
    public void testContainsPoint() {
        Box2 a = new Box2(zero2.clone(), zero2.clone());
        assertTrue(a.containsPoint(zero2));
        assertTrue(!a.containsPoint(one2));
        assertTrue(a.containsPoint(zero2));
        assertTrue(a.containsPoint(one2));
        assertTrue(a.containsPoint(one2.clone().negate()));

    }

    @Test
    public void testContainsBox() {
        Box2 a = new Box2(zero2.clone(), zero2.clone());
        Box2 b = new Box2(zero2.clone(), one2.clone());
        Box2 c = new Box2(one2.clone().negate(), one2.clone());
        assertTrue(a.containsBox(a));
        assertTrue(!a.containsBox(b));
        assertTrue(!a.containsBox(c));
        assertTrue(b.containsBox(a));
        assertTrue(c.containsBox(a));
        assertTrue(!b.containsBox(c));

    }

    @Test
    public void testGetParameter() {
        Box2 a = new Box2(zero2.clone(), one2.clone());
        Box2 b = new Box2(one2.clone().negate(), one2.clone());
        assertTrue(a.getParameter(new Vector2(0, 0)).equals(new Vector2(0, 0)));
        assertTrue(a.getParameter(new Vector2(1, 1)).equals(new Vector2(1, 1)));
        assertTrue(b.getParameter(new Vector2(-1, -1)).equals(new Vector2(0, 0)));
        assertTrue(b.getParameter(new Vector2(0, 0)).equals(new Vector2(0.5, 0.5)));
        assertTrue(b.getParameter(new Vector2(1, 1)).equals(new Vector2(1, 1)));

    }

    @Test
    public void testClampPoint() {
        Box2 a = new Box2(zero2.clone(), zero2.clone());
        Box2 b = new Box2(one2.clone().negate(), one2.clone());
        assertTrue(a.clampPoint(new Vector2(0, 0)).equals(new Vector2(0, 0)));
        assertTrue(a.clampPoint(new Vector2(1, 1)).equals(new Vector2(0, 0)));
        assertTrue(a.clampPoint(new Vector2(-1, -1)).equals(new Vector2(0, 0)));
        assertTrue(b.clampPoint(new Vector2(2, 2)).equals(new Vector2(1, 1)));
        assertTrue(b.clampPoint(new Vector2(1, 1)).equals(new Vector2(1, 1)));
        assertTrue(b.clampPoint(new Vector2(0, 0)).equals(new Vector2(0, 0)));
        assertTrue(b.clampPoint(new Vector2(-1, -1)).equals(new Vector2(-1, -1)));
        assertTrue(b.clampPoint(new Vector2(-2, -2)).equals(new Vector2(-1, -1)));

    }

    @Test
    public void testDistanceToPoint() {
        Box2 a = new Box2(zero2.clone(), zero2.clone());
        Box2 b = new Box2(one2.clone().negate(), one2.clone());
        assertTrue(a.distanceToPoint(new Vector2(0, 0)) == 0);
        assertTrue(a.distanceToPoint(new Vector2(1, 1)) == Math.sqrt(2));
        assertTrue(a.distanceToPoint(new Vector2(-1, -1)) == Math.sqrt(2));
        assertTrue(b.distanceToPoint(new Vector2(2, 2)) == Math.sqrt(2));
        assertTrue(b.distanceToPoint(new Vector2(1, 1)) == 0);
        assertTrue(b.distanceToPoint(new Vector2(0, 0)) == 0);
        assertTrue(b.distanceToPoint(new Vector2(-1, -1)) == 0);
        assertTrue(b.distanceToPoint(new Vector2(-2, -2)) == Math.sqrt(2));

    }

    @Test
    public void testIntersectsBox() {
        Box2 a = new Box2(zero2.clone(), zero2.clone());
        Box2 b = new Box2(zero2.clone(), one2.clone());
        Box2 c = new Box2(one2.clone().negate(), one2.clone());
        assertTrue(a.intersectsBox(a));
        assertTrue(a.intersectsBox(b));
        assertTrue(a.intersectsBox(c));
        assertTrue(b.intersectsBox(a));
        assertTrue(c.intersectsBox(a));
        assertTrue(b.intersectsBox(c));
        b.translate(new Vector2(2, 2));
        assertTrue(!a.intersectsBox(b));
        assertTrue(!b.intersectsBox(a));
        assertTrue(!b.intersectsBox(c));

    }

    @Test
    public void testIntersect() {
        Box2 a = new Box2(zero2.clone(), zero2.clone());
        Box2 b = new Box2(zero2.clone(), one2.clone());
        Box2 c = new Box2(one2.clone().negate(), one2.clone());
        assertTrue((a.clone().intersect(a)).equals(a));
        assertTrue((a.clone().intersect(b)).equals(a));
        assertTrue((b.clone().intersect(b)).equals(b));
        assertTrue((a.clone().intersect(c)).equals(a));
        assertTrue((b.clone().intersect(c)).equals(b));
        assertTrue((c.clone().intersect(c)).equals(c));

    }

    @Test
    public void testUnion() {
        Box2 a = new Box2(zero2.clone(), zero2.clone());
        Box2 b = new Box2(zero2.clone(), one2.clone());
        Box2 c = new Box2(one2.clone().negate(), one2.clone());
        assertTrue((a.clone().union(a)).equals(a));
        assertTrue((a.clone().union(b)).equals(b));
        assertTrue((a.clone().union(c)).equals(c));
        assertTrue((b.clone().union(c)).equals(c));

    }

    @Test
    public void testTranslate() {
        Box2 a = new Box2(zero2.clone(), zero2.clone());
        Box2 b = new Box2(zero2.clone(), one2.clone());
        Box2 c = new Box2(one2.clone().negate(), one2.clone());
        Box2 d = new Box2(one2.clone().negate(), zero2.clone());
        assertTrue((a.clone().translate(one2)).equals(new Box2(one2, one2)));
        assertTrue(((a.clone().translate(one2)).translate(one2.clone().negate())).equals(a));
        assertTrue((d.clone().translate(one2)).equals(b));
        assertTrue((b.clone().translate(one2.clone().negate())).equals(d));

    }

}
