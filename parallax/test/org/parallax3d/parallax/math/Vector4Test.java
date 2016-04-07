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

@ThreejsTest("Vector4")
public class Vector4Test {
	@Test
	public void testConstructor() {
		Vector4 a = new Vector4();
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		assertTrue( a.z == 0 );
		assertTrue( a.w == 1 );
		a = new Vector4(x, y, z, w);
		assertTrue( a.x == x );
		assertTrue( a.y == y );
		assertTrue( a.z == z );
		assertTrue( a.w == w );

	}

	@Test
	public void testCopy() {
		Vector4 a = new Vector4(x, y, z, w);
		Vector4 b = new Vector4().copy(a);
		assertTrue( b.x == x );
		assertTrue( b.y == y );
		assertTrue( b.z == z );
		assertTrue( b.w == w );
		a.x = 0;
		a.y = -1;
		a.z = -2;
		a.w = -3;
		assertTrue( b.x == x );
		assertTrue( b.y == y );
		assertTrue( b.z == z );
		assertTrue( b.w == w );

	}

	@Test
	public void testSet() {
		Vector4 a = new Vector4();
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		assertTrue( a.z == 0 );
		assertTrue( a.w == 1 );
		a.set(x, y, z, w);
		assertTrue( a.x == x );
		assertTrue( a.y == y );
		assertTrue( a.z == z );
		assertTrue( a.w == w );

	}

	@Test
	public void testSetX_setY_setZ_setW() {
		Vector4 a = new Vector4();
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		assertTrue( a.z == 0 );
		assertTrue( a.w == 1 );

		a.setX( x );
		a.setY( y );
		a.setZ( z );
		a.setW( w );

		assertTrue( a.x == x );
		assertTrue( a.y == y );
		assertTrue( a.z == z );
		assertTrue( a.w == w );

	}

	@Test
	public void testSetComponent_getComponent() {
		Vector4 a = new Vector4();
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		assertTrue( a.z == 0 );
		assertTrue( a.w == 1 );
		a.setComponent(0, 1);
		a.setComponent(1, 2);
		a.setComponent(2, 3);
		a.setComponent(3, 4);
		assertTrue( a.getComponent(0) == 1 );
		assertTrue( a.getComponent(1) == 2 );
		assertTrue( a.getComponent(2) == 3 );
		assertTrue( a.getComponent(3) == 4 );

	}

	@Test
	public void testAdd() {
		Vector4 a = new Vector4(x, y, z, w);
		Vector4 b = new Vector4(-x, -y, -z, -w);
		a.add(b);
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		assertTrue( a.z == 0 );
		assertTrue( a.w == 0 );
		Vector4 c = new Vector4().addVectors(b, b);
		assertTrue( c.x == (-2) * x );
		assertTrue( c.y == (-2) * y );
		assertTrue( c.z == (-2) * z );
		assertTrue( c.w == (-2) * w );

	}

	@Test
	public void testSub() {
		Vector4 a = new Vector4(x, y, z, w);
		Vector4 b = new Vector4(-x, -y, -z, -w);
		a.sub(b);
		assertTrue( a.x == 2 * x );
		assertTrue( a.y == 2 * y );
		assertTrue( a.z == 2 * z );
		assertTrue( a.w == 2 * w );
		Vector4 c = new Vector4().subVectors(a, a);
		assertTrue( c.x == 0 );
		assertTrue( c.y == 0 );
		assertTrue( c.z == 0 );
		assertTrue( c.w == 0 );

	}

	@Test
	public void testMultiply_divide() {
		Vector4 a = new Vector4(x, y, z, w);
		Vector4 b = new Vector4(-x, -y, -z, -w);

		a.multiplyScalar(-2);
		assertTrue( a.x == x * (-2) );
		assertTrue( a.y == y * (-2) );
		assertTrue( a.z == z * (-2) );
		assertTrue( a.w == w * (-2) );

		b.multiplyScalar( -2 );
		assertTrue( b.x == 2 * x );
		assertTrue( b.y == 2 * y );
		assertTrue( b.z == 2 * z );
		assertTrue( b.w == 2 * w );

		a.divideScalar( -2 );
		assertTrue( a.x == x );
		assertTrue( a.y == y );
		assertTrue( a.z == z );
		assertTrue( a.w == w );

		b.divideScalar( -2 );
		assertTrue( b.x == -x );
		assertTrue( b.y == -y );
		assertTrue( b.z == -z );
		assertTrue( b.w == -w );

	}

	@Test
	public void testMin_max_clamp() {
		Vector4 a = new Vector4(x, y, z, w);
		Vector4 b = new Vector4(-x, -y, -z, -w);
		Vector4 c = new Vector4();

		c.copy(a).min(b);
		assertTrue( c.x == -x );
		assertTrue( c.y == -y );
		assertTrue( c.z == -z );
		assertTrue( c.w == -w );

		c.copy( a ).max( b );
		assertTrue( c.x == x );
		assertTrue( c.y == y );
		assertTrue( c.z == z );
		assertTrue( c.w == w );

		c.set((-2) * x, 2 * y, (-2) * z, 2 * w);
		c.clamp(b, a);
		assertTrue( c.x == -x );
		assertTrue( c.y == y );
		assertTrue( c.z == -z );
		assertTrue( c.w == w );

	}

	@Test
	public void testNegate() {
		Vector4 a = new Vector4(x, y, z, w);
		a.negate();
		assertTrue( a.x == -x );
		assertTrue( a.y == -y );
		assertTrue( a.z == -z );
		assertTrue( a.w == -w );

	}

	@Test
	public void testDot() {
		Vector4 a = new Vector4(x, y, z, w);
		Vector4 b = new Vector4(-x, -y, -z, -w);
		Vector4 c = new Vector4(0, 0, 0, 0);

		double result = a.dot(b);
		assertTrue( result == (-x) * x - y * y - z * z - w * w );

		result = a.dot( c );
		assertTrue( result == 0 );

	}

	@Test
	public void testLength_lengthSq() {
		Vector4 a = new Vector4(x, 0, 0, 0);
		Vector4 b = new Vector4(0, -y, 0, 0);
		Vector4 c = new Vector4(0, 0, z, 0);
		Vector4 d = new Vector4(0, 0, 0, w);
		Vector4 e = new Vector4(0, 0, 0, 0);
		assertTrue( a.length() == x );
		assertTrue( a.lengthSq() == x * x );
		assertTrue( b.length() == y );
		assertTrue( b.lengthSq() == y * y );
		assertTrue( c.length() == z );
		assertTrue( c.lengthSq() == z * z );
		assertTrue( d.length() == w );
		assertTrue( d.lengthSq() == w * w );
		assertTrue( e.length() == 0 );
		assertTrue( e.lengthSq() == 0 );
		a.set(x, y, z, w);
		assertTrue( a.length() == Math.sqrt(x * x + y * y + z * z + w * w) );
		assertTrue( a.lengthSq() == x * x + y * y + z * z + w * w );

	}

	@Test
	public void testNormalize() {
		Vector4 a = new Vector4(x, 0, 0, 0);
		Vector4 b = new Vector4(0, -y, 0, 0);
		Vector4 c = new Vector4(0, 0, z, 0);
		Vector4 d = new Vector4(0, 0, 0, -w);

		a.normalize();
		assertTrue( a.length() == 1 );
		assertTrue( a.x == 1 );

		b.normalize();
		assertTrue( b.length() == 1 );
		assertTrue( b.y == -1 );

		c.normalize();
		assertTrue( c.length() == 1 );
		assertTrue( c.z == 1 );

		d.normalize();
		assertTrue( d.length() == 1 );
		assertTrue( d.w == -1 );

	}

	@Test
	public void testSetLength() {
		Vector4 a = new Vector4(x, 0, 0, 0);

		assertTrue( a.length() == x );
		a.setLength( y );
		assertTrue( a.length() == y );

		a = new Vector4(0, 0, 0, 0);
		assertTrue( a.length() == 0 );
		a.setLength( y );
		assertTrue( a.length() == 0 );

	}

	@Test
	public void testLerp_clone() {
		Vector4 a = new Vector4(x, 0, z, 0);
		Vector4 b = new Vector4(0, -y, 0, -w);
		assertTrue( a.lerp(a, 0).equals(a.lerp(a, 0.5)) );
		assertTrue( a.lerp(a, 0).equals(a.lerp(a, 1)) );
		assertTrue( (a.clone().lerp(b, 0)).equals(a) );
		assertTrue( (a.clone().lerp(b, 0.5)).x == x * 0.5 );
		assertTrue( (a.clone().lerp(b, 0.5)).y == (-y) * 0.5 );
		assertTrue( (a.clone().lerp(b, 0.5)).z == z * 0.5 );
		assertTrue( (a.clone().lerp(b, 0.5)).w == (-w) * 0.5 );
		assertTrue( (a.clone().lerp(b, 1)).equals(b) );

	}

	@Test
	public void testEquals() {
		Vector4 a = new Vector4(x, 0, z, 0);
		Vector4 b = new Vector4(0, -y, 0, -w);

		assertTrue( a.x != b.x );
		assertTrue( a.y != b.y );
		assertTrue( a.z != b.z );
		assertTrue( a.w != b.w );

		assertTrue( !a.equals(b) );
		assertTrue( !b.equals(a) );

		a.copy( b );
		assertTrue( a.x == b.x );
		assertTrue( a.y == b.y );
		assertTrue( a.z == b.z );
		assertTrue( a.w == b.w );

		assertTrue( a.equals(b) );
		assertTrue( b.equals(a) );

	}

}
