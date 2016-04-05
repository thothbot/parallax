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

import static jdk.nashorn.internal.objects.Global.undefined;
import static org.junit.Assert.*;

@ThreejsTest("Frustum")
public class FrustumTest {

	final static Vector3 unit3 = new Vector3( 1, 0, 0 );

	@Test
	public void testConstructor() {
		Frustum a = new Frustum();
		assertTrue( a.planes != undefined );
		assertTrue( a.planes.size() == 6 );
		Plane p0 = new Plane(unit3, -1);
		Plane p1 = new Plane(unit3, 1);
		Plane p2 = new Plane(unit3, 2);
		Plane p3 = new Plane(unit3, 3);
		Plane p4 = new Plane(unit3, 4);
		Plane p5 = new Plane(unit3, 5);
		a = new Frustum(p0, p1, p2, p3, p4, p5);
		assertTrue( a.planes.get(0).equals(p0) );
		assertTrue( a.planes.get(1).equals(p1) );
		assertTrue( a.planes.get(2).equals(p2) );
		assertTrue( a.planes.get(3).equals(p3) );
		assertTrue( a.planes.get(4).equals(p4) );
		assertTrue( a.planes.get(5).equals(p5) );

	}

	@Test
	public void testCopy() {
		Plane p0 = new Plane(unit3, -1);
		Plane p1 = new Plane(unit3, 1);
		Plane p2 = new Plane(unit3, 2);
		Plane p3 = new Plane(unit3, 3);
		Plane p4 = new Plane(unit3, 4);
		Plane p5 = new Plane(unit3, 5);
		Frustum b = new Frustum(p0, p1, p2, p3, p4, p5);
		Frustum a = new Frustum().copy(b);
		assertTrue( a.planes.get(0).equals(p0) );
		assertTrue( a.planes.get(1).equals(p1) );
		assertTrue( a.planes.get(2).equals(p2) );
		assertTrue( a.planes.get(3).equals(p3) );
		assertTrue( a.planes.get(4).equals(p4) );
		assertTrue( a.planes.get(5).equals(p5) );
		b.planes.set(0, p1);
		assertTrue( a.planes.get(0).equals(p0) );

	}

	@Test
	public void testSetFromMatrix_makeOrthographic_containsPoint() {
		Matrix4 m = new Matrix4().makeOrthographic(-1, 1, -1, 1, 1, 100);
		Frustum a = new Frustum().setFromMatrix(m);
		assertTrue( !a.containsPoint(new Vector3(0, 0, 0)) );
		assertTrue( a.containsPoint(new Vector3(0, 0, -50)) );
		assertTrue( a.containsPoint(new Vector3(0, 0, -1.001)) );
		assertTrue( a.containsPoint(new Vector3(-1, -1, -1.001)) );
		assertTrue( !a.containsPoint(new Vector3(-1.1, -1.1, -1.001)) );
		assertTrue( a.containsPoint(new Vector3(1, 1, -1.001)) );
		assertTrue( !a.containsPoint(new Vector3(1.1, 1.1, -1.001)) );
		assertTrue( a.containsPoint(new Vector3(0, 0, -100)) );
		assertTrue( a.containsPoint(new Vector3(-1, -1, -100)) );
		assertTrue( !a.containsPoint(new Vector3(-1.1, -1.1, -100.1)) );
		assertTrue( a.containsPoint(new Vector3(1, 1, -100)) );
		assertTrue( !a.containsPoint(new Vector3(1.1, 1.1, -100.1)) );
		assertTrue( !a.containsPoint(new Vector3(0, 0, -101)) );

	}

	@Test
	public void testSetFromMatrix_makeFrustum_containsPoint() {
		Matrix4 m = new Matrix4().makeFrustum(-1, 1, -1, 1, 1, 100);
		Frustum a = new Frustum().setFromMatrix(m);
		assertTrue( !a.containsPoint(new Vector3(0, 0, 0)) );
		assertTrue( a.containsPoint(new Vector3(0, 0, -50)) );
		assertTrue( a.containsPoint(new Vector3(0, 0, -1.001)) );
		assertTrue( a.containsPoint(new Vector3(-1, -1, -1.001)) );
		assertTrue( !a.containsPoint(new Vector3(-1.1, -1.1, -1.001)) );
		assertTrue( a.containsPoint(new Vector3(1, 1, -1.001)) );
		assertTrue( !a.containsPoint(new Vector3(1.1, 1.1, -1.001)) );
		assertTrue( a.containsPoint(new Vector3(0, 0, -99.999)) );
		assertTrue( a.containsPoint(new Vector3(-99.999, -99.999, -99.999)) );
		assertTrue( !a.containsPoint(new Vector3(-100.1, -100.1, -100.1)) );
		assertTrue( a.containsPoint(new Vector3(99.999, 99.999, -99.999)) );
		assertTrue( !a.containsPoint(new Vector3(100.1, 100.1, -100.1)) );
		assertTrue( !a.containsPoint(new Vector3(0, 0, -101)) );

	}

	@Test
	public void testSetFromMatrix_makeFrustum_intersectsSphere() {
		Matrix4 m = new Matrix4().makeFrustum(-1, 1, -1, 1, 1, 100);
		Frustum a = new Frustum().setFromMatrix(m);
		assertTrue( !a.intersectsSphere(new Sphere(new Vector3(0, 0, 0), 0)) );
		assertTrue( !a.intersectsSphere(new Sphere(new Vector3(0, 0, 0), 0.9)) );
		assertTrue( a.intersectsSphere(new Sphere(new Vector3(0, 0, 0), 1.1)) );
		assertTrue( a.intersectsSphere(new Sphere(new Vector3(0, 0, -50), 0)) );
		assertTrue( a.intersectsSphere(new Sphere(new Vector3(0, 0, -1.001), 0)) );
		assertTrue( a.intersectsSphere(new Sphere(new Vector3(-1, -1, -1.001), 0)) );
		assertTrue( !a.intersectsSphere(new Sphere(new Vector3(-1.1, -1.1, -1.001), 0)) );
		assertTrue( a.intersectsSphere(new Sphere(new Vector3(-1.1, -1.1, -1.001), 0.5)) );
		assertTrue( a.intersectsSphere(new Sphere(new Vector3(1, 1, -1.001), 0)) );
		assertTrue( !a.intersectsSphere(new Sphere(new Vector3(1.1, 1.1, -1.001), 0)) );
		assertTrue( a.intersectsSphere(new Sphere(new Vector3(1.1, 1.1, -1.001), 0.5)) );
		assertTrue( a.intersectsSphere(new Sphere(new Vector3(0, 0, -99.999), 0)) );
		assertTrue( a.intersectsSphere(new Sphere(new Vector3(-99.999, -99.999, -99.999), 0)) );
		assertTrue( !a.intersectsSphere(new Sphere(new Vector3(-100.1, -100.1, -100.1), 0)) );
		assertTrue( a.intersectsSphere(new Sphere(new Vector3(-100.1, -100.1, -100.1), 0.5)) );
		assertTrue( a.intersectsSphere(new Sphere(new Vector3(99.999, 99.999, -99.999), 0)) );
		assertTrue( !a.intersectsSphere(new Sphere(new Vector3(100.1, 100.1, -100.1), 0)) );
		assertTrue( a.intersectsSphere(new Sphere(new Vector3(100.1, 100.1, -100.1), 0.2)) );
		assertTrue( !a.intersectsSphere(new Sphere(new Vector3(0, 0, -101), 0)) );
		assertTrue( a.intersectsSphere(new Sphere(new Vector3(0, 0, -101), 1.1)) );

	}

	@Test
	public void testClone() {
		Plane p0 = new Plane(unit3, -1);
		Plane p1 = new Plane(unit3, 1);
		Plane p2 = new Plane(unit3, 2);
		Plane p3 = new Plane(unit3, 3);
		Plane p4 = new Plane(unit3, 4);
		Plane p5 = new Plane(unit3, 5);
		Frustum b = new Frustum(p0, p1, p2, p3, p4, p5);
		Frustum a = b.clone();
		assertTrue( a.planes.get(0).equals(p0) );
		assertTrue( a.planes.get(1).equals(p1) );
		assertTrue( a.planes.get(2).equals(p2) );
		assertTrue( a.planes.get(3).equals(p3) );
		assertTrue( a.planes.get(4).equals(p4) );
		assertTrue( a.planes.get(5).equals(p5) );
		assertTrue( b.planes.get(0).equals(p0) );

	}

}
