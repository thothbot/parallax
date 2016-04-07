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
import static org.parallax3d.parallax.math.Constants.DELTA;
import static org.parallax3d.parallax.math.Constants.x;
import static org.parallax3d.parallax.math.Constants.y;

@ThreejsTest("Vector2")
public class Vector2Test {
	@Test
	public void testConstructor() {
		Vector2 a = new Vector2();
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		a = new Vector2(x, y);
		assertTrue( a.x == x );
		assertTrue( a.y == y );

	}

	@Test
	public void testCopy() {
		Vector2 a = new Vector2(x, y);
		Vector2 b = new Vector2().copy(a);
		assertTrue( b.x == x );
		assertTrue( b.y == y );
		a.x = 0;
		a.y = -1;
		assertTrue( b.x == x );
		assertTrue( b.y == y );

	}

	@Test
	public void testSet() {
		Vector2 a = new Vector2();
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		a.set(x, y);
		assertTrue( a.x == x );
		assertTrue( a.y == y );

	}

	@Test
	public void testSetX_setY() {
		Vector2 a = new Vector2();
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		assertTrue( a.x == x );
		assertTrue( a.y == y );

	}

	@Test
	public void testSetComponent_getComponent() {
		Vector2 a = new Vector2();
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		a.setComponent(0, 1);
		a.setComponent(1, 2);
		assertTrue( a.getComponent(0) == 1 );
		assertTrue( a.getComponent(1) == 2 );

	}

	@Test
	public void testAdd() {
		Vector2 a = new Vector2(x, y);
		Vector2 b = new Vector2(-x, -y);
		a.add(b);
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		Vector2 c = new Vector2().addVectors(b, b);
		assertTrue( c.x == (-2) * x );
		assertTrue( c.y == (-2) * y );

	}

	@Test
	public void testSub() {
		Vector2 a = new Vector2(x, y);
		Vector2 b = new Vector2(-x, -y);
		a.sub(b);
		assertTrue( a.x == 2 * x );
		assertTrue( a.y == 2 * y );
		Vector2 c = new Vector2().subVectors(a, a);
		assertTrue( c.x == 0 );
		assertTrue( c.y == 0 );

	}

	@Test
	public void testMultiply_divide() {
		Vector2 a = new Vector2(x, y);
		Vector2 b = new Vector2(-x, -y);
		a.multiplyScalar(-2);
		assertTrue( a.x == x * (-2) );
		assertTrue( a.y == y * (-2) );
		assertTrue( b.x == 2 * x );
		assertTrue( b.y == 2 * y );
		assertTrue( a.x == x );
		assertTrue( a.y == y );
		assertTrue( b.x == -x );
		assertTrue( b.y == -y );

	}

	@Test
	public void testMin_max_clamp() {
		Vector2 a = new Vector2(x, y);
		Vector2 b = new Vector2(-x, -y);
		Vector2 c = new Vector2();
		c.copy(a).min(b);
		assertTrue( c.x == -x );
		assertTrue( c.y == -y );
		assertTrue( c.x == x );
		assertTrue( c.y == y );
		c.set((-2) * x, 2 * y);
		c.clamp(b, a);
		assertTrue( c.x == -x );
		assertTrue( c.y == y );
		c.set((-2) * x, 2 * x);
		c.clampScalar(-x, x);
		assertEquals(c.x, -x, DELTA);
		assertEquals(c.y, x, DELTA);

	}

	@Test
	public void testRounding() {
		isEqual(new Vector2(-0.1, 0.1).floor(), new Vector2(-1, 0));
		isEqual(new Vector2(-0.5, 0.5).floor(), new Vector2(-1, 0));
		isEqual(new Vector2(-0.9, 0.9).floor(), new Vector2(-1, 0));
		isEqual(new Vector2(-0.1, 0.1).ceil(), new Vector2(0, 1));
		isEqual(new Vector2(-0.5, 0.5).ceil(), new Vector2(0, 1));
		isEqual(new Vector2(-0.9, 0.9).ceil(), new Vector2(0, 1));
		isEqual(new Vector2(-0.1, 0.1).round(), new Vector2(0, 0));
		isEqual(new Vector2(-0.5, 0.5).round(), new Vector2(0, 1));
		isEqual(new Vector2(-0.9, 0.9).round(), new Vector2(-1, 1));
		isEqual(new Vector2(-0.1, 0.1).roundToZero(), new Vector2(0, 0));
		isEqual(new Vector2(-0.5, 0.5).roundToZero(), new Vector2(0, 0));
		isEqual(new Vector2(-0.9, 0.9).roundToZero(), new Vector2(0, 0));
		isEqual(new Vector2(-1.1, 1.1).roundToZero(), new Vector2(-1, 1));
		isEqual(new Vector2(-1.5, 1.5).roundToZero(), new Vector2(-1, 1));
		isEqual(new Vector2(-1.9, 1.9).roundToZero(), new Vector2(-1, 1));

	}

	@Test
	public void testNegate() {
		Vector2 a = new Vector2(x, y);
		a.negate();
		assertTrue( a.x == -x );
		assertTrue( a.y == -y );

	}

	@Test
	public void testDot() {
		Vector2 a = new Vector2(x, y);
		Vector2 b = new Vector2(-x, -y);
		Vector2 c = new Vector2();
		double result = a.dot(b);
		assertTrue( result == (-x) * x - y * y );
		assertTrue( result == 0 );

	}

	@Test
	public void testLength_lengthSq() {
		Vector2 a = new Vector2(x, 0);
		Vector2 b = new Vector2(0, -y);
		Vector2 c = new Vector2();
		assertTrue( a.length() == x );
		assertTrue( a.lengthSq() == x * x );
		assertTrue( b.length() == y );
		assertTrue( b.lengthSq() == y * y );
		assertTrue( c.length() == 0 );
		assertTrue( c.lengthSq() == 0 );
		a.set(x, y);
		assertTrue( a.length() == Math.sqrt(x * x + y * y) );
		assertTrue( a.lengthSq() == x * x + y * y );

	}

	@Test
	public void testNormalize() {
		Vector2 a = new Vector2(x, 0);
		Vector2 b = new Vector2(0, -y);
		Vector2 c = new Vector2();
		a.normalize();
		assertTrue( a.length() == 1 );
		assertTrue( a.x == 1 );
		assertTrue( b.length() == 1 );
		assertTrue( b.y == -1 );

	}

	@Test
	public void testDistanceTo_distanceToSquared() {
		Vector2 a = new Vector2(x, 0);
		Vector2 b = new Vector2(0, -y);
		Vector2 c = new Vector2();
		assertTrue( a.distanceTo(c) == x );
		assertTrue( a.distanceToSquared(c) == x * x );
		assertTrue( b.distanceTo(c) == y );
		assertTrue( b.distanceToSquared(c) == y * y );

	}

	@Test
	public void testSetLength() {
		Vector2 a = new Vector2(x, 0);
		assertTrue( a.length() == x );
		assertTrue( a.length() == y );
		a = new Vector2(0, 0);
		assertTrue( a.length() == 0 );
		assertTrue( a.length() == 0 );

	}

	@Test
	public void testLerp_clone() {
		Vector2 a = new Vector2(x, 0);
		Vector2 b = new Vector2(0, -y);
		assertTrue( a.lerp(a, 0).equals(a.lerp(a, 0.5)) );
		assertTrue( a.lerp(a, 0).equals(a.lerp(a, 1)) );
		assertTrue( (a.clone().lerp(b, 0)).equals(a) );
		assertTrue( (a.clone().lerp(b, 0.5)).x == x * 0.5 );
		assertTrue( (a.clone().lerp(b, 0.5)).y == (-y) * 0.5 );
		assertTrue( (a.clone().lerp(b, 1)).equals(b) );

	}

	@Test
	public void testEquals() {
		Vector2 a = new Vector2(x, 0);
		Vector2 b = new Vector2(0, -y);
		assertTrue( a.x != b.x );
		assertTrue( a.y != b.y );
		assertTrue( !a.equals(b) );
		assertTrue( !b.equals(a) );
		assertTrue( a.x == b.x );
		assertTrue( a.y == b.y );
		assertTrue( a.equals(b) );
		assertTrue( b.equals(a) );

	}

	static void isEqual(Vector2 v1, Vector2 v2){
		assertTrue(v1.getX() == v2.getX());
		assertTrue(v1.getY() == v2.getY());
	}
}
