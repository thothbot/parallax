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

@ThreejsTest("Sphere")
public class SphereTest {
	@Test
	public void testConstructor() {
		Sphere a = new Sphere();
		assertTrue( a.center.equals(zero3) );
		assertTrue( a.radius == 0 );
		a = new Sphere(one3.clone(), 1);
		assertTrue( a.center.equals(one3) );
		assertTrue( a.radius == 1 );

	}

	@Test
	public void testCopy() {
		Sphere a = new Sphere(one3.clone(), 1);
		Sphere b = new Sphere().copy(a);
		assertTrue( b.center.equals(one3) );
		assertTrue( b.radius == 1 );
		a.center = zero3;
		a.radius = 0;
		assertTrue( b.center.equals(one3) );
		assertTrue( b.radius == 1 );

	}

	@Test
	public void testSet() {
		Sphere a = new Sphere();
		assertTrue( a.center.equals(zero3) );
		assertTrue( a.radius == 0 );
		a.set(one3, 1);
		assertTrue( a.center.equals(one3) );
		assertTrue( a.radius == 1 );

	}

	@Test
	public void testEmpty() {
		Sphere a = new Sphere();
		assertTrue( a.isEmpty() );
		a.set(one3, 1);
		assertTrue( !a.isEmpty() );

	}

	@Test
	public void testContainsPoint() {
		Sphere a = new Sphere(one3.clone(), 1);
		assertTrue( !a.containsPoint(zero3) );
		assertTrue( a.containsPoint(one3) );

	}

	@Test
	public void testDistanceToPoint() {
		Sphere a = new Sphere(one3.clone(), 1);
		assertTrue( a.distanceToPoint(zero3) - 0.732 < 0.001 );
		assertTrue( a.distanceToPoint(one3) == -1 );

	}

	@Test
	public void testIntersectsSphere() {
		Sphere a = new Sphere(one3.clone(), 1);
		Sphere b = new Sphere(zero3.clone(), 1);
		Sphere c = new Sphere(zero3.clone(), 0.25);
		assertTrue( a.intersectsSphere(b) );
		assertTrue( !a.intersectsSphere(c) );

	}

	@Test
	public void testIntersectsPlane() {
		Sphere a = new Sphere(zero3.clone(), 1);
		Plane b = new Plane(new Vector3(0, 1, 0), 1);
		Plane c = new Plane(new Vector3(0, 1, 0), 1.25);
		Plane d = new Plane(new Vector3(0, -1, 0), 1.25);
		assertTrue( a.intersectsPlane(b) );
		assertTrue( !a.intersectsPlane(c) );
		assertTrue( !a.intersectsPlane(d) );

	}

	@Test
	public void testClampPoint() {
		Sphere a = new Sphere(one3.clone(), 1);
		assertTrue( a.clampPoint(new Vector3(1, 1, 3)).equals(new Vector3(1, 1, 2)) );
		assertTrue( a.clampPoint(new Vector3(1, 1, -3)).equals(new Vector3(1, 1, 0)) );

	}

	@Test
	public void testGetBoundingBox() {
		Sphere a = new Sphere(one3.clone(), 1);
		assertTrue( a.getBoundingBox().equals(new Box3(zero3, two3)) );
		a.set(zero3, 0);
		assertTrue( a.getBoundingBox().equals(new Box3(zero3, zero3)) );

	}

	@Test
	public void testApplyMatrix4() {
		Sphere a = new Sphere(one3.clone(), 1);
		Matrix4 m = new Matrix4().makeTranslation(1, -2, 1);
		assertTrue( ((a.clone().applyMatrix4(m)).getBoundingBox()).equals(a.getBoundingBox().applyMatrix4(m)) );

	}

	@Test
	public void testTranslate() {
		Sphere a = new Sphere(one3.clone(), 1);
		a.translate(one3.clone().negate());
		assertTrue( a.center.equals(zero3) );

	}

}
