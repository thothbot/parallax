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
import static org.junit.Assert.*;
import static org.parallax3d.parallax.math.Constants.one3;
import static org.parallax3d.parallax.math.Constants.two3;
import static org.parallax3d.parallax.math.Constants.zero3;

@ThreejsTest("Triangle")
public class TriangleTest {
	@Test
	public void testConstructor() {
		Triangle a = new Triangle();
		assertTrue( a.a.equals(zero3) );
		assertTrue( a.b.equals(zero3) );
		assertTrue( a.c.equals(zero3) );
		a = new Triangle(one3.clone().negate(), one3.clone(), two3.clone());
		assertTrue( a.a.equals(one3.clone().negate()) );
		assertTrue( a.b.equals(one3) );
		assertTrue( a.c.equals(two3) );

	}

	@Test
	public void testCopy() {
		Triangle a = new Triangle(one3.clone().negate(), one3.clone(), two3.clone());
		Triangle b = new Triangle().copy(a);
		assertTrue( b.a.equals(one3.clone().negate()) );
		assertTrue( b.b.equals(one3) );
		assertTrue( b.c.equals(two3) );
		a.a = one3;
		a.b = zero3;
		a.c = zero3;
		assertTrue( b.a.equals(one3.clone().negate()) );
		assertTrue( b.b.equals(one3) );
		assertTrue( b.c.equals(two3) );

	}

	@Test
	public void testSetFromPointsAndIndices() {
		Triangle a = new Triangle();
		Vector3[] points = new Vector3[]{one3, one3.clone().negate(), two3};
		a.setFromPointsAndIndices(points, 1, 0, 2);
		assertTrue( a.a.equals(one3.clone().negate()) );
		assertTrue( a.b.equals(one3) );
		assertTrue( a.c.equals(two3) );

	}

	@Test
	public void testSet() {
		Triangle a = new Triangle();
		a.set(one3.clone().negate(), one3, two3);
		assertTrue( a.a.equals(one3.clone().negate()) );
		assertTrue( a.b.equals(one3) );
		assertTrue( a.c.equals(two3) );

	}

	@Test
	public void testArea() {
		Triangle a = new Triangle();
		assertTrue( a.area() == 0 );
		a = new Triangle(new Vector3(0, 0, 0), new Vector3(1, 0, 0), new Vector3(0, 1, 0));
		assertTrue( a.area() == 0.5 );
		a = new Triangle(new Vector3(2, 0, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 2));
		assertTrue( a.area() == 2 );
		a = new Triangle(new Vector3(2, 0, 0), new Vector3(0, 0, 0), new Vector3(3, 0, 0));
		assertTrue( a.area() == 0 );

	}

	@Test
	public void testMidpoint() {
		Triangle a = new Triangle();
		assertTrue( a.midpoint().equals(new Vector3(0, 0, 0)) );
		a = new Triangle(new Vector3(0, 0, 0), new Vector3(1, 0, 0), new Vector3(0, 1, 0));
		assertTrue( a.midpoint().equals(new Vector3(1 / 3, 1 / 3, 0)) );
		a = new Triangle(new Vector3(2, 0, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 2));
		assertTrue( a.midpoint().equals(new Vector3(2 / 3, 0, 2 / 3)) );

	}

	@Test
	public void testNormal() {
		Triangle a = new Triangle();
		assertTrue( a.normal().equals(new Vector3(0, 0, 0)) );
		a = new Triangle(new Vector3(0, 0, 0), new Vector3(1, 0, 0), new Vector3(0, 1, 0));
		assertTrue( a.normal().equals(new Vector3(0, 0, 1)) );
		a = new Triangle(new Vector3(2, 0, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 2));
		assertTrue( a.normal().equals(new Vector3(0, 1, 0)) );

	}

	@Test
	public void testPlane() {
		Triangle a = new Triangle();
		assertTrue( a.plane().distanceToPoint(a.a) == 0 );
		assertTrue( a.plane().distanceToPoint(a.b) == 0 );
		assertTrue( a.plane().distanceToPoint(a.c) == 0 );
		assertTrue( a.plane().normal.equals(a.normal()) );
		a = new Triangle(new Vector3(0, 0, 0), new Vector3(1, 0, 0), new Vector3(0, 1, 0));
		assertTrue( a.plane().distanceToPoint(a.a) == 0 );
		assertTrue( a.plane().distanceToPoint(a.b) == 0 );
		assertTrue( a.plane().distanceToPoint(a.c) == 0 );
		assertTrue( a.plane().normal.equals(a.normal()) );
		a = new Triangle(new Vector3(2, 0, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 2));
		assertTrue( a.plane().distanceToPoint(a.a) == 0 );
		assertTrue( a.plane().distanceToPoint(a.b) == 0 );
		assertTrue( a.plane().distanceToPoint(a.c) == 0 );
		assertTrue( ((a.plane().normal.clone()).normalize()).equals(a.normal()) );

	}

	@Test
	public void testBarycoordFromPoint() {
		Triangle a = new Triangle();
		Vector3 bad = new Vector3(-2, -1, -1);
		assertTrue( a.barycoordFromPoint(a.a).equals(bad) );
		assertTrue( a.barycoordFromPoint(a.b).equals(bad) );
		assertTrue( a.barycoordFromPoint(a.c).equals(bad) );
		a = new Triangle(new Vector3(0, 0, 0), new Vector3(1, 0, 0), new Vector3(0, 1, 0));
		assertTrue( a.barycoordFromPoint(a.a).equals(new Vector3(1, 0, 0)) );
		assertTrue( a.barycoordFromPoint(a.b).equals(new Vector3(0, 1, 0)) );
		assertTrue( a.barycoordFromPoint(a.c).equals(new Vector3(0, 0, 1)) );
		assertTrue( a.barycoordFromPoint(a.midpoint()).distanceTo(new Vector3(1 / 3, 1 / 3, 1 / 3)) < 1.0E-4 );
		a = new Triangle(new Vector3(2, 0, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 2));
		assertTrue( a.barycoordFromPoint(a.a).equals(new Vector3(1, 0, 0)) );
		assertTrue( a.barycoordFromPoint(a.b).equals(new Vector3(0, 1, 0)) );
		assertTrue( a.barycoordFromPoint(a.c).equals(new Vector3(0, 0, 1)) );
		assertTrue( a.barycoordFromPoint(a.midpoint()).distanceTo(new Vector3(1 / 3, 1 / 3, 1 / 3)) < 1.0E-4 );

	}

	@Test
	public void testContainsPoint() {
		Triangle a = new Triangle();
		assertTrue( !a.containsPoint(a.a) );
		assertTrue( !a.containsPoint(a.b) );
		assertTrue( !a.containsPoint(a.c) );
		a = new Triangle(new Vector3(0, 0, 0), new Vector3(1, 0, 0), new Vector3(0, 1, 0));
		assertTrue( a.containsPoint(a.a) );
		assertTrue( a.containsPoint(a.b) );
		assertTrue( a.containsPoint(a.c) );
		assertTrue( a.containsPoint(a.midpoint()) );
		assertTrue( !a.containsPoint(new Vector3(-1, -1, -1)) );
		a = new Triangle(new Vector3(2, 0, 0), new Vector3(0, 0, 0), new Vector3(0, 0, 2));
		assertTrue( a.containsPoint(a.a) );
		assertTrue( a.containsPoint(a.b) );
		assertTrue( a.containsPoint(a.c) );
		assertTrue( a.containsPoint(a.midpoint()) );
		assertTrue( !a.containsPoint(new Vector3(-1, -1, -1)) );

	}

}
