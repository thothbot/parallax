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
import static org.parallax3d.parallax.math.Constants.*;

@ThreejsTest("Vector3")
public class Vector3Test {
	@Test
	public void testConstructor() {
		Vector3 a = new Vector3();
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		assertTrue( a.z == 0 );
		a = new Vector3(x, y, z);
		assertTrue( a.x == x );
		assertTrue( a.y == y );
		assertTrue( a.z == z );

	}

	@Test
	public void testCopy() {
		Vector3 a = new Vector3(x, y, z);
		Vector3 b = new Vector3().copy(a);
		assertTrue( b.x == x );
		assertTrue( b.y == y );
		assertTrue( b.z == z );
		a.x = 0;
		a.y = -1;
		a.z = -2;
		assertTrue( b.x == x );
		assertTrue( b.y == y );
		assertTrue( b.z == z );

	}

	@Test
	public void testSet() {
		Vector3 a = new Vector3();
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		assertTrue( a.z == 0 );
		a.set(x, y, z);
		assertTrue( a.x == x );
		assertTrue( a.y == y );
		assertTrue( a.z == z );

	}

	@Test
	public void testSetX_setY_setZ() {
		Vector3 a = new Vector3();
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		assertTrue( a.z == 0 );

		a.setX( x );
		a.setY( y );
		a.setZ( z );

		assertTrue( a.x == x );
		assertTrue( a.y == y );
		assertTrue( a.z == z );

	}

	@Test
	public void testSetComponent_getComponent() {
		Vector3 a = new Vector3();
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		assertTrue( a.z == 0 );
		a.setComponent(0, 1);
		a.setComponent(1, 2);
		a.setComponent(2, 3);
		assertTrue( a.getComponent(0) == 1 );
		assertTrue( a.getComponent(1) == 2 );
		assertTrue( a.getComponent(2) == 3 );

	}

	@Test
	public void testAdd() {
		Vector3 a = new Vector3(x, y, z);
		Vector3 b = new Vector3(-x, -y, -z);
		a.add(b);
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		assertTrue( a.z == 0 );
		Vector3 c = new Vector3().addVectors(b, b);
		assertTrue( c.x == (-2) * x );
		assertTrue( c.y == (-2) * y );
		assertTrue( c.z == (-2) * z );

	}

	@Test
	public void testSub() {
		Vector3 a = new Vector3(x, y, z);
		Vector3 b = new Vector3(-x, -y, -z);
		a.sub(b);
		assertTrue( a.x == 2 * x );
		assertTrue( a.y == 2 * y );
		assertTrue( a.z == 2 * z );
		Vector3 c = new Vector3().subVectors(a, a);
		assertTrue( c.x == 0 );
		assertTrue( c.y == 0 );
		assertTrue( c.z == 0 );

	}

	@Test
	public void testMultiply_divide() {
		Vector3 a = new Vector3(x, y, z);
		Vector3 b = new Vector3(-x, -y, -z);
		a.multiplyScalar(-2);
		assertTrue( a.x == x * (-2) );
		assertTrue( a.y == y * (-2) );
		assertTrue( a.z == z * (-2) );

		b.multiplyScalar( -2 );
		assertTrue( b.x == 2 * x );
		assertTrue( b.y == 2 * y );
		assertTrue( b.z == 2 * z );

		a.divideScalar( -2 );
		assertTrue( a.x == x );
		assertTrue( a.y == y );
		assertTrue( a.z == z );

		b.divideScalar( -2 );
		assertTrue( b.x == -x );
		assertTrue( b.y == -y );
		assertTrue( b.z == -z );

	}

	@Test
	public void testMin_max_clamp() {
		Vector3 a = new Vector3(x, y, z);
		Vector3 b = new Vector3(-x, -y, -z);
		Vector3 c = new Vector3();

		c.copy(a).min(b);
		assertTrue( c.x == -x );
		assertTrue( c.y == -y );
		assertTrue( c.z == -z );

		c.copy( a ).max( b );
		assertTrue( c.x == x );
		assertTrue( c.y == y );
		assertTrue( c.z == z );

		c.set((-2) * x, 2 * y, (-2) * z);
		c.clamp(b, a);
		assertTrue( c.x == -x );
		assertTrue( c.y == y );
		assertTrue( c.z == -z );

	}

	@Test
	public void testNegate() {
		Vector3 a = new Vector3(x, y, z);
		a.negate();
		assertTrue( a.x == -x );
		assertTrue( a.y == -y );
		assertTrue( a.z == -z );

	}

	@Test
	public void testDot() {
		Vector3 a = new Vector3(x, y, z);
		Vector3 b = new Vector3(-x, -y, -z);
		Vector3 c = new Vector3();

		double result = a.dot(b);
		assertTrue( result == (-x) * x - y * y - z * z );

		result = a.dot( c );
		assertTrue( result == 0 );

	}

	@Test
	public void testLength_lengthSq() {
		Vector3 a = new Vector3(x, 0, 0);
		Vector3 b = new Vector3(0, -y, 0);
		Vector3 c = new Vector3(0, 0, z);
		Vector3 d = new Vector3();
		assertTrue( a.length() == x );
		assertTrue( a.lengthSq() == x * x );
		assertTrue( b.length() == y );
		assertTrue( b.lengthSq() == y * y );
		assertTrue( c.length() == z );
		assertTrue( c.lengthSq() == z * z );
		assertTrue( d.length() == 0 );
		assertTrue( d.lengthSq() == 0 );
		a.set(x, y, z);
		assertTrue( a.length() == Math.sqrt(x * x + y * y + z * z) );
		assertTrue( a.lengthSq() == x * x + y * y + z * z );

	}

	@Test
	public void testNormalize() {
		Vector3 a = new Vector3(x, 0, 0);
		Vector3 b = new Vector3(0, -y, 0);
		Vector3 c = new Vector3(0, 0, z);

		a.normalize();
		assertTrue( a.length() == 1 );
		assertTrue( a.x == 1 );

		b.normalize();
		assertTrue( b.length() == 1 );
		assertTrue( b.y == -1 );

		c.normalize();
		assertTrue( c.length() == 1 );
		assertTrue( c.z == 1 );

	}

	@Test
	public void testDistanceTo_distanceToSquared() {
		Vector3 a = new Vector3(x, 0, 0);
		Vector3 b = new Vector3(0, -y, 0);
		Vector3 c = new Vector3(0, 0, z);
		Vector3 d = new Vector3();
		assertTrue( a.distanceTo(d) == x );
		assertTrue( a.distanceToSquared(d) == x * x );
		assertTrue( b.distanceTo(d) == y );
		assertTrue( b.distanceToSquared(d) == y * y );
		assertTrue( c.distanceTo(d) == z );
		assertTrue( c.distanceToSquared(d) == z * z );

	}

	@Test
	public void testSetLength() {
		Vector3 a = new Vector3(x, 0, 0);

		assertTrue( a.length() == x );
		a.setLength( y );
		assertTrue( a.length() == y );

		a = new Vector3(0, 0, 0);
		assertTrue( a.length() == 0 );
		a.setLength( y );
		assertTrue( a.length() == 0 );

	}

	@Test
	public void testProjectOnVector() {
		Vector3 a = new Vector3(1, 0, 0);
		Vector3 b = new Vector3();
		Vector3 normal = new Vector3(10, 0, 0);
		assertTrue( (b.copy(a).projectOnVector(normal)).equals(new Vector3(1, 0, 0)) );
		a.set(0, 1, 0);
		assertTrue( (b.copy(a).projectOnVector(normal)).equals(new Vector3(0, 0, 0)) );
		a.set(0, 0, -1);
		assertTrue( (b.copy(a).projectOnVector(normal)).equals(new Vector3(0, 0, 0)) );
		a.set(-1, 0, 0);
		assertTrue( (b.copy(a).projectOnVector(normal)).equals(new Vector3(-1, 0, 0)) );

	}

	@Test
	public void testProjectOnPlane() {
		Vector3 a = new Vector3(1, 0, 0);
		Vector3 b = new Vector3();
		Vector3 normal = new Vector3(1, 0, 0);

		assertTrue( (b.copy(a).projectOnPlane(normal)).equals(new Vector3(0, 0, 0)) );

		a.set(0, 1, 0);
		assertTrue( (b.copy(a).projectOnPlane(normal)).equals(new Vector3(0, 1, 0)) );

		a.set(0, 0, -1);
		assertTrue( (b.copy(a).projectOnPlane(normal)).equals(new Vector3(0, 0, -1)) );

		a.set(-1, 0, 0);
		assertTrue( (b.copy(a).projectOnPlane(normal)).equals(new Vector3(0, 0, 0)) );

	}

	@Test
	public void testReflect() {
		Vector3 a = new Vector3();
		Vector3 normal = new Vector3(0, 1, 0);
		Vector3 b = new Vector3();
		a.set(0, -1, 0);
		assertTrue( (b.copy(a).reflect(normal)).equals(new Vector3(0, 1, 0)) );
		a.set(1, -1, 0);
		assertTrue( (b.copy(a).reflect(normal)).equals(new Vector3(1, 1, 0)) );
		a.set(1, -1, 0);
		normal.set(0, -1, 0);
		assertTrue( (b.copy(a).reflect(normal)).equals(new Vector3(1, 1, 0)) );

	}

	@Test
	public void testAngleTo() {
		Vector3 a = new Vector3(0, -0.18851655680720186, 0.9820700116639124);
		Vector3 b = new Vector3(0, 0.18851655680720186, -0.9820700116639124);

		assertEquals(a.angleTo(a), 0, DELTA);
		assertEquals(a.angleTo(b), Math.PI, DELTA);

		Vector3 x = new Vector3(1, 0, 0);
		Vector3 y = new Vector3(0, 1, 0);
		Vector3 z = new Vector3(0, 0, 1);

		assertEquals(x.angleTo(y), Math.PI / 2, DELTA);
		assertEquals(x.angleTo(z), Math.PI / 2, DELTA);
		assertEquals(z.angleTo(x), Math.PI / 2, DELTA);

		assertTrue( Math.abs( x.angleTo( new Vector3( 1, 1, 0 ) ) - ( Math.PI / 4 ) ) < 0.0000001 );

	}

	@Test
	public void testLerp_clone() {
		Vector3 a = new Vector3(x, 0, z);
		Vector3 b = new Vector3(0, -y, 0);
		assertTrue( a.lerp(a, 0).equals(a.lerp(a, 0.5)) );
		assertTrue( a.lerp(a, 0).equals(a.lerp(a, 1)) );
		assertTrue( (a.clone().lerp(b, 0)).equals(a) );
		assertTrue( (a.clone().lerp(b, 0.5)).x == x * 0.5 );
		assertTrue( (a.clone().lerp(b, 0.5)).y == (-y) * 0.5 );
		assertTrue( (a.clone().lerp(b, 0.5)).z == z * 0.5 );
		assertTrue( (a.clone().lerp(b, 1)).equals(b) );

	}

	@Test
	public void testEquals() {
		Vector3 a = new Vector3(x, 0, z);
		Vector3 b = new Vector3(0, -y, 0);

		assertTrue( a.x != b.x );
		assertTrue( a.y != b.y );
		assertTrue( a.z != b.z );

		assertTrue( !a.equals(b) );
		assertTrue( !b.equals(a) );

		a.copy( b );
		assertTrue( a.x == b.x );
		assertTrue( a.y == b.y );
		assertTrue( a.z == b.z );

		assertTrue( a.equals(b) );
		assertTrue( b.equals(a) );

	}

}
