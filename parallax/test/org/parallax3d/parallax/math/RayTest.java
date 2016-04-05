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

@ThreejsTest("Ray")
public class RayTest {
	@Test
	public void testConstructor_equals() {
		Ray a = new Ray();
		assertTrue( a.origin.equals(zero3) );
		assertTrue( a.direction.equals(zero3) );
		a = new Ray(two3.clone(), one3.clone());
		assertTrue( a.origin.equals(two3) );
		assertTrue( a.direction.equals(one3) );

	}

	@Test
	public void testCopy_equals() {
		Ray a = new Ray(zero3.clone(), one3.clone());
		Ray b = new Ray().copy(a);
		assertTrue( b.origin.equals(zero3) );
		assertTrue( b.direction.equals(one3) );
		a.origin = zero3;
		a.direction = one3;
		assertTrue( b.origin.equals(zero3) );
		assertTrue( b.direction.equals(one3) );

	}

	@Test
	public void testSet() {
		Ray a = new Ray();
		a.set(one3, one3);
		assertTrue( a.origin.equals(one3) );
		assertTrue( a.direction.equals(one3) );

	}

	@Test
	public void testAt() {
		Ray a = new Ray(one3.clone(), new Vector3(0, 0, 1));
		assertTrue( a.at(0).equals(one3) );
		assertTrue( a.at(-1).equals(new Vector3(1, 1, 0)) );
		assertTrue( a.at(1).equals(new Vector3(1, 1, 2)) );

	}

	@Test
	public void testRecast_clone() {
		Ray a = new Ray(one3.clone(), new Vector3(0, 0, 1));
		assertTrue( a.recast(0).equals(a) );
		Ray b = a.clone();
		assertTrue( b.recast(-1).equals(new Ray(new Vector3(1, 1, 0), new Vector3(0, 0, 1))) );
		Ray c = a.clone();
		assertTrue( c.recast(1).equals(new Ray(new Vector3(1, 1, 2), new Vector3(0, 0, 1))) );
		Ray d = a.clone();
		Ray e = d.clone().recast( 1 );
		assertTrue( d.equals(a) );
		assertTrue( !e.equals(d) );
		assertTrue( e.equals(c) );

	}

	@Test
	public void testClosestPointToPoint() {
		Ray a = new Ray(one3.clone(), new Vector3(0, 0, 1));
		Vector3 b = a.closestPointToPoint(zero3);
		assertTrue( b.equals(one3) );
		Vector3 c = a.closestPointToPoint(new Vector3(0, 0, 50));
		assertTrue( c.equals(new Vector3(1, 1, 50)) );
		Vector3 d = a.closestPointToPoint( one3 );
		assertTrue( d.equals(one3) );

	}

	@Test
	public void testDistanceToPoint() {
		Ray a = new Ray(one3.clone(), new Vector3(0, 0, 1));
		double b = a.distanceToPoint(zero3);
		assertTrue( b == Math.sqrt(3) );
		double c = a.distanceToPoint(new Vector3(0, 0, 50));
		assertTrue( c == Math.sqrt(2) );
		// exactly on the ray
		double d = a.distanceToPoint( one3 );
		assertTrue( d == 0 );

	}

	@Test
	public void testDistanceSqToPoint() {
		Ray a = new Ray(one3.clone(), new Vector3(0, 0, 1));
		double b = a.distanceSqToPoint(zero3);
		assertTrue( b == 3 );
		double c = a.distanceSqToPoint(new Vector3(0, 0, 50));
		assertTrue( c == 2 );
		// exactly on the ray
		double d = a.distanceSqToPoint( one3 );
		assertTrue( d == 0 );

	}

	@Test
	public void testIntersectsSphere() {
		Ray a = new Ray(one3.clone(), new Vector3(0, 0, 1));
		Sphere b = new Sphere(zero3, 0.5);
		Sphere c = new Sphere(zero3, 1.5);
		Sphere d = new Sphere(one3, 0.1);
		Sphere e = new Sphere(two3, 0.1);
		Sphere f = new Sphere(two3, 1);
		assertTrue( !a.intersectsSphere(b) );
		assertTrue( !a.intersectsSphere(c) );
		assertTrue( a.intersectsSphere(d) );
		assertTrue( !a.intersectsSphere(e) );
		assertTrue( !a.intersectsSphere(f) );

	}

	@Test
	public void testIntersectSphere() {
		double TOL = 1.0E-4;
		Ray a0 = new Ray(zero3.clone(), new Vector3(0, 0, -1));
		Ray a1 = new Ray(one3.clone(), new Vector3(-1, 0, 0));
		Sphere b = new Sphere(new Vector3(0, 0, 3), 2);
		assertTrue( a0.intersectSphere(b) == null );
		b = new Sphere(new Vector3(3, 0, -1), 2);
		assertTrue( a0.intersectSphere(b) == null );
		b = new Sphere(new Vector3(1, -2, 1), 2);
		assertTrue( a1.intersectSphere(b) == null );
		b = new Sphere(new Vector3(-1, 1, 1), 1);
		assertTrue( a1.intersectSphere(b).distanceTo(new Vector3(0, 1, 1)) < TOL );
		b = new Sphere(new Vector3(0, 0, -2), 1);
		assertTrue( a0.intersectSphere(b).distanceTo(new Vector3(0, 0, -1)) < TOL );
		b = new Sphere(new Vector3(2, 0, -1), 2);
		assertTrue( a0.intersectSphere(b).distanceTo(new Vector3(0, 0, -1)) < TOL );
		b = new Sphere(new Vector3(2.01, 0, -1), 2);
		assertTrue( a0.intersectSphere(b) == null );
		b = new Sphere(zero3.clone(), 1);
		assertTrue( a0.intersectSphere(b).distanceTo(new Vector3(0, 0, -1)) < TOL );
		b = new Sphere(new Vector3(0, 0, 1), 4);
		assertTrue( a0.intersectSphere(b).distanceTo(new Vector3(0, 0, -3)) < TOL );
		b = new Sphere(new Vector3(0, 0, -1), 4);
		assertTrue( a0.intersectSphere(b).distanceTo(new Vector3(0, 0, -5)) < TOL );

	}

	@Test
	public void testIntersectsPlane() {
		Ray a = new Ray(one3.clone(), new Vector3(0, 0, 1));
		Plane b = new Plane().setFromNormalAndCoplanarPoint(new Vector3(0, 0, 1), one3.clone().sub(new Vector3(0, 0, -1)));
		assertTrue( a.intersectsPlane(b) );
		Plane c = new Plane().setFromNormalAndCoplanarPoint(new Vector3(0, 0, 1), one3.clone().sub(new Vector3(0, 0, 0)));
		assertTrue( a.intersectsPlane(c) );
		Plane d = new Plane().setFromNormalAndCoplanarPoint(new Vector3(0, 0, 1), one3.clone().sub(new Vector3(0, 0, 1)));
		assertTrue( !a.intersectsPlane(d) );
		Plane e = new Plane().setFromNormalAndCoplanarPoint(new Vector3(1, 0, 0), one3);
		assertTrue( a.intersectsPlane(e) );
		Plane f = new Plane().setFromNormalAndCoplanarPoint(new Vector3(1, 0, 0), zero3);
		assertTrue( !a.intersectsPlane(f) );

	}

	@Test
	public void testIntersectPlane() {
		Ray a = new Ray(one3.clone(), new Vector3(0, 0, 1));
		Plane b = new Plane().setFromNormalAndCoplanarPoint(new Vector3(0, 0, 1), new Vector3(1, 1, -1));
		assertTrue( a.intersectPlane(b) == null );
		Plane c = new Plane().setFromNormalAndCoplanarPoint(new Vector3(0, 0, 1), new Vector3(1, 1, 0));
		assertTrue( a.intersectPlane(c) == null );
		Plane d = new Plane().setFromNormalAndCoplanarPoint(new Vector3(0, 0, 1), new Vector3(1, 1, 1));
		assertTrue( a.intersectPlane(d).equals(a.origin) );
		Plane e = new Plane().setFromNormalAndCoplanarPoint(new Vector3(1, 0, 0), one3);
		assertTrue( a.intersectPlane(e).equals(a.origin) );
		Plane f = new Plane().setFromNormalAndCoplanarPoint(new Vector3(1, 0, 0), zero3);
		assertTrue( a.intersectPlane(f) == null );

	}

	@Test
	public void testApplyMatrix4() {
		Ray a = new Ray(one3.clone(), new Vector3(0, 0, 1));
		Matrix4 m = new Matrix4();
		assertTrue( (a.clone().applyMatrix4(m)).equals(a) );
		a = new Ray(zero3.clone(), new Vector3(0, 0, 1));
		m.makeRotationZ(Math.PI);
		assertTrue( (a.clone().applyMatrix4(m)).equals(a) );

		m.makeRotationX( Math.PI );
		Ray b = a.clone();
		b.direction.negate();
		Ray a2 = a.clone().applyMatrix4( m );
		assertTrue( a2.origin.distanceTo(b.origin) < 1.0E-4 );
		assertTrue( a2.direction.distanceTo(b.direction) < 1.0E-4 );

		a.origin = new Vector3(0, 0, 1);
		b.origin = new Vector3(0, 0, -1);
		a2 = a.clone().applyMatrix4(m);
		assertTrue( a2.origin.distanceTo(b.origin) < 1.0E-4 );
		assertTrue( a2.direction.distanceTo(b.direction) < 1.0E-4 );

	}

	@Test
	public void testDistanceSqToSegment() {
		Ray a = new Ray(one3.clone(), new Vector3(0, 0, 1));
		Vector3 ptOnLine = new Vector3();
		Vector3 ptOnSegment = new Vector3();
		Vector3 v0 = new Vector3(3, 5, 50);
		Vector3 v1 = new Vector3(50, 50, 50);
		double distSqr = a.distanceSqToSegment(v0, v1, ptOnLine, ptOnSegment);
		assertTrue( ptOnSegment.distanceTo(v0) < 1.0E-4 );
		assertTrue( ptOnLine.distanceTo(new Vector3(1, 1, 50)) < 1.0E-4 );
		assertTrue( Math.abs(distSqr - 20) < 1.0E-4 );
		v0 = new Vector3(-50, -50, -50);
		v1 = new Vector3(-3, -5, -4);
		distSqr = a.distanceSqToSegment(v0, v1, ptOnLine, ptOnSegment);
		assertTrue( ptOnSegment.distanceTo(v1) < 1.0E-4 );
		assertTrue( ptOnLine.distanceTo(one3) < 1.0E-4 );
		assertTrue( Math.abs(distSqr - 77) < 1.0E-4 );
		v0 = new Vector3(-50, -50, -50);
		v1 = new Vector3(50, 50, 50);
		distSqr = a.distanceSqToSegment(v0, v1, ptOnLine, ptOnSegment);
		assertTrue( ptOnSegment.distanceTo(one3) < 1.0E-4 );
		assertTrue( ptOnLine.distanceTo(one3) < 1.0E-4 );
		assertTrue( distSqr < 1.0E-4 );

	}

	@Test
	public void testIntersectBox() {
		double TOL = 1.0E-4;
		Box3 box = new Box3(new Vector3(-1, -1, -1), new Vector3(1, 1, 1));
		Ray a = new Ray(new Vector3(-2, 0, 0), new Vector3(1, 0, 0));
		assertTrue( a.intersectsBox(box) == true );
		assertTrue( a.intersectBox(box).distanceTo(new Vector3(-1, 0, 0)) < TOL );
		Ray b = new Ray(new Vector3(-2, 0, 0), new Vector3(-1, 0, 0));
		assertTrue( b.intersectsBox(box) == false );
		assertTrue( b.intersectBox(box) == null );
		Ray c = new Ray(new Vector3(0, 0, 0), new Vector3(1, 0, 0));
		assertTrue( c.intersectsBox(box) == true );
		assertTrue( c.intersectBox(box).distanceTo(new Vector3(1, 0, 0)) < TOL );
		Ray d = new Ray(new Vector3(0, 2, 1), new Vector3(0, -1, -1).normalize());
		assertTrue( d.intersectsBox(box) == true );
		assertTrue( d.intersectBox(box).distanceTo(new Vector3(0, 1, 0)) < TOL );
		Ray e = new Ray(new Vector3(1, -2, 1), new Vector3(0, 1, 0).normalize());
		assertTrue( e.intersectsBox(box) == true );
		assertTrue( e.intersectBox(box).distanceTo(new Vector3(1, -1, 1)) < TOL );
		Ray f = new Ray(new Vector3(1, -2, 0), new Vector3(0, -1, 0).normalize());
		assertTrue( f.intersectsBox(box) == false );
		assertTrue( f.intersectBox(box) == null );

	}

}
