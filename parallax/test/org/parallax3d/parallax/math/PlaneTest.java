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

import static org.junit.Assert.assertTrue;
import static org.parallax3d.parallax.math.Constants.*;

@ThreejsTest("Plane")
public class PlaneTest {

	static boolean comparePlane( Plane a, Plane b) {
		double threshold = 0.0001;
		return ( a.normal.distanceTo( b.normal ) < threshold &&
				Math.abs( a.constant - b.constant ) < threshold );
	}

	@Test
	public void testConstructor() {
		Plane a = new Plane();
		assertTrue( a.normal.x == 1 );
		assertTrue( a.normal.y == 0 );
		assertTrue( a.normal.z == 0 );
		assertTrue( a.constant == 0 );
		a = new Plane(one3.clone(), 0);
		assertTrue( a.normal.x == 1 );
		assertTrue( a.normal.y == 1 );
		assertTrue( a.normal.z == 1 );
		assertTrue( a.constant == 0 );
		a = new Plane(one3.clone(), 1);
		assertTrue( a.normal.x == 1 );
		assertTrue( a.normal.y == 1 );
		assertTrue( a.normal.z == 1 );
		assertTrue( a.constant == 1 );

	}

	@Test
	public void testCopy() {
		Plane a = new Plane(new Vector3(x, y, z), w);
		Plane b = new Plane().copy(a);
		assertTrue( b.normal.x == x );
		assertTrue( b.normal.y == y );
		assertTrue( b.normal.z == z );
		assertTrue( b.constant == w );
		a.normal.x = 0;
		a.normal.y = -1;
		a.normal.z = -2;
		a.constant = -3;
		assertTrue( b.normal.x == x );
		assertTrue( b.normal.y == y );
		assertTrue( b.normal.z == z );
		assertTrue( b.constant == w );

	}

	@Test
	public void testSet() {
		Plane a = new Plane();
		assertTrue( a.normal.x == 1 );
		assertTrue( a.normal.y == 0 );
		assertTrue( a.normal.z == 0 );
		assertTrue( a.constant == 0 );
		Plane b = a.clone().set(new Vector3(x, y, z), w);
		assertTrue( b.normal.x == x );
		assertTrue( b.normal.y == y );
		assertTrue( b.normal.z == z );
		assertTrue( b.constant == w );

	}

	@Test
	public void testSetComponents() {
		Plane a = new Plane();
		assertTrue( a.normal.x == 1 );
		assertTrue( a.normal.y == 0 );
		assertTrue( a.normal.z == 0 );
		assertTrue( a.constant == 0 );
		Plane b = a.clone().setComponents(x, y, z, w);
		assertTrue( b.normal.x == x );
		assertTrue( b.normal.y == y );
		assertTrue( b.normal.z == z );
		assertTrue( b.constant == w );

	}

	@Test
	public void testSetFromNormalAndCoplanarPoint() {
		Vector3 normal = one3.clone().normalize();
		Plane a = new Plane().setFromNormalAndCoplanarPoint(normal, zero3);
		assertTrue( a.normal.equals(normal) );
		assertTrue( a.constant == 0 );

	}

	@Test
	public void testNormalize() {
		Plane a = new Plane(new Vector3(2, 0, 0), 2);
		a.normalize();
		assertTrue( a.normal.length() == 1 );
		assertTrue( a.normal.equals(new Vector3(1, 0, 0)) );
		assertTrue( a.constant == 1 );

	}

	@Test
	public void testNegate_distanceToPoint() {
		Plane a = new Plane(new Vector3(2, 0, 0), -2);

		a.normalize();
		assertTrue( a.distanceToPoint(new Vector3(4, 0, 0)) == 3 );
		assertTrue( a.distanceToPoint(new Vector3(1, 0, 0)) == 0 );

		a.negate();
		assertTrue( a.distanceToPoint(new Vector3(4, 0, 0)) == -3 );
		assertTrue( a.distanceToPoint(new Vector3(1, 0, 0)) == 0 );

	}

	@Test
	public void testDistanceToPoint() {
		Plane a = new Plane(new Vector3(2, 0, 0), -2);
		a.normalize();
		assertTrue( a.distanceToPoint(a.projectPoint(zero3.clone())) == 0 );
		assertTrue( a.distanceToPoint(new Vector3(4, 0, 0)) == 3 );

	}

	@Test
	public void testDistanceToSphere() {
		Plane a = new Plane(new Vector3(1, 0, 0), 0);
		Sphere b = new Sphere(new Vector3(2, 0, 0), 1);
		assertTrue( a.distanceToSphere(b) == 1 );
		a.set(new Vector3(1, 0, 0), 2);
		assertTrue( a.distanceToSphere(b) == 3 );
		a.set(new Vector3(1, 0, 0), -2);
		assertTrue( a.distanceToSphere(b) == -1 );

	}

	@Test
	public void testIsInterestionLine_intersectLine() {
		Plane a = new Plane(new Vector3(1, 0, 0), 0);
		Line3 l1 = new Line3(new Vector3(-10, 0, 0), new Vector3(10, 0, 0));
		assertTrue( a.intersectsLine(l1) );
		assertTrue( a.intersectLine(l1).equals(new Vector3(0, 0, 0)) );
		a = new Plane(new Vector3(1, 0, 0), -3);
		assertTrue( a.intersectsLine(l1) );
		assertTrue( a.intersectLine(l1).equals(new Vector3(3, 0, 0)) );
		a = new Plane(new Vector3(1, 0, 0), -11);
		assertTrue( !a.intersectsLine(l1) );
		assertTrue( a.intersectLine(l1) == null );
		a = new Plane(new Vector3(1, 0, 0), 11);
		assertTrue( !a.intersectsLine(l1) );
		assertTrue( a.intersectLine(l1) == null );

	}

	@Test
	public void testProjectPoint() {
		Plane a = new Plane(new Vector3(1, 0, 0), 0);
		assertTrue( a.projectPoint(new Vector3(10, 0, 0)).equals(zero3) );
		assertTrue( a.projectPoint(new Vector3(-10, 0, 0)).equals(zero3) );
		a = new Plane(new Vector3(0, 1, 0), -1);
		assertTrue( a.projectPoint(new Vector3(0, 0, 0)).equals(new Vector3(0, 1, 0)) );
		assertTrue( a.projectPoint(new Vector3(0, 1, 0)).equals(new Vector3(0, 1, 0)) );

	}

	@Test
	public void testOrthoPoint() {
		Plane a = new Plane(new Vector3(1, 0, 0), 0);
		assertTrue( a.orthoPoint(new Vector3(10, 0, 0)).equals(new Vector3(10, 0, 0)) );
		assertTrue( a.orthoPoint(new Vector3(-10, 0, 0)).equals(new Vector3(-10, 0, 0)) );

	}

	@Test
	public void testCoplanarPoint() {
		Plane a = new Plane(new Vector3(1, 0, 0), 0);
		assertTrue( a.distanceToPoint(a.coplanarPoint()) == 0 );
		a = new Plane(new Vector3(0, 1, 0), -1);
		assertTrue( a.distanceToPoint(a.coplanarPoint()) == 0 );

	}

	@Test
	public void testApplyMatrix4_translate() {
		Plane a = new Plane(new Vector3(1, 0, 0), 0);
		Matrix4 m = new Matrix4();
		m.makeRotationZ(Math.PI * 0.5);
		assertTrue( comparePlane(a.clone().applyMatrix4(m), new Plane(new Vector3(0, 1, 0), 0)) );
		a = new Plane(new Vector3(0, 1, 0), -1);
		assertTrue( comparePlane(a.clone().applyMatrix4(m), new Plane(new Vector3(-1, 0, 0), -1)) );
		m.makeTranslation(1, 1, 1);
		assertTrue( comparePlane(a.clone().applyMatrix4(m), a.clone().translate(new Vector3(1, 1, 1))) );

	}

}
