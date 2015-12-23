/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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
import static org.junit.Assert.*;

public class Vector3Test
{

	private static double DELTA = 0.00001;

	private static double X = 2;
	private static double Y = 3;
	private static double Z = 4;

	@Test
	public void testVector3()
	{
		Vector3 a = new Vector3();
		assertEquals( 0.0, a.x, DELTA);
		assertEquals( 0.0, a.y, DELTA);
		assertEquals( 0.0, a.z, DELTA);

		a = new Vector3( X, Y, Z );
		assertEquals( X, a.x, DELTA);
		assertEquals( Y, a.y, DELTA);
		assertEquals( Z, a.z, DELTA);
	}

	@Test
	public void testLength()
	{
		Vector3 a = new Vector3( X, 0, 0 );
		Vector3 b = new Vector3( 0, -Y, 0 );
		Vector3 c = new Vector3( 0, 0, Z );
		Vector3 d = new Vector3();

		assertEquals( X, a.length(), DELTA );
		assertEquals( X * X, a.lengthSq(), DELTA );
		assertEquals( Y, b.length(), DELTA );
		assertEquals( Y * Y, b.lengthSq(), DELTA );
		assertEquals( Z, c.length(), DELTA );
		assertEquals( Z * Z, c.lengthSq(), DELTA );
		assertEquals( 0.0, d.length(), DELTA );
		assertEquals( 0.0, d.lengthSq(), DELTA );

		a.set( X, Y, Z );
		assertEquals( Math.sqrt( X*X + Y*Y + Z*Z ), a.length(), DELTA );
		assertEquals( ( X*X + Y*Y + Z*Z ), a.lengthSq(), DELTA );
	}

	@Test
	public void testDistanceTo()
	{
		Vector3 a = new Vector3( X, 0, 0 );
		Vector3 b = new Vector3( 0, -Y, 0 );
		Vector3 c = new Vector3( 0, 0, Z );
		Vector3 d = new Vector3();

		assertEquals( X, a.distanceTo( d ), DELTA );
		assertEquals( X * X, a.distanceToSquared( d ), DELTA );

		assertEquals( Y, b.distanceTo( d ), DELTA );
		assertEquals( Y * Y, b.distanceToSquared( d ), DELTA );

		assertEquals( Z, c.distanceTo( d ), DELTA );
		assertEquals( Z * Z, c.distanceToSquared( d ), DELTA );
	}

	@Test
	public void testCopy()
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3().copy( a );
		assertEquals( X, b.x, DELTA);
		assertEquals( Y, b.y, DELTA);
		assertEquals( Z, b.z, DELTA);

		// ensure that it is a true copy
		a.x = 0;
		a.y = -1;
		a.z = -2;
		assertEquals( X, b.x, DELTA);
		assertEquals( Y, b.y, DELTA);
		assertEquals( Z, b.z, DELTA);
	}

	@Test
	public void testSet()
	{
		Vector3 a = new Vector3();
		assertEquals( 0.0, a.x, DELTA);
		assertEquals( 0.0, a.y, DELTA);
		assertEquals( 0.0, a.z, DELTA);

		a.set( X, Y, Z );
		assertEquals( X, a.x, DELTA);
		assertEquals( Y, a.y, DELTA);
		assertEquals( Z, a.z, DELTA);
	}

	@Test
	public void testAdd()
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3( -X, -Y, -Z );

		a.add( b );
		assertEquals( 0.0, a.x, 0.0);
		assertEquals( 0.0, a.y, 0.0);
		assertEquals( 0.0, a.z, 0.0);

		Vector3 c = new Vector3().add( b, b );
		assertEquals( -2*X, c.x, DELTA);
		assertEquals( -2*Y, c.y, DELTA);
		assertEquals( -2*Z, c.z, DELTA);
	}

	@Test
	public void testSub()
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3( -X, -Y, -Z );

		a.sub( b );
		assertEquals( 2 * X, a.x, DELTA);
		assertEquals( 2 * Y, a.y, DELTA);
		assertEquals( 2 * Z, a.z, DELTA);

		Vector3 c = new Vector3().sub( a, a );
		assertEquals( 0.0, c.x, DELTA);
		assertEquals( 0.0, c.y, DELTA);
		assertEquals( 0.0, c.z, DELTA);
	}

	@Test
	public void testMultiplyDivide()
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3( -X, -Y, -Z );

		a.multiply( -2 );
		assertEquals( X * -2, a.x, DELTA);
		assertEquals( Y * -2, a.y, DELTA);
		assertEquals( Z * -2, a.z, DELTA);

		b.multiply( -2 );
		assertEquals( 2 * X, b.x, DELTA);
		assertEquals( 2 * Y, b.y, DELTA);
		assertEquals( 2 * Z, b.z, DELTA);

		a.divide( -2 );
		assertEquals( X, a.x, DELTA);
		assertEquals( Y, a.y, DELTA);
		assertEquals( Z, a.z, DELTA);

		b.divide( -2 );
		assertEquals( -X, b.x, DELTA);
		assertEquals( -Y, b.y, DELTA);
		assertEquals( -Z, b.z, DELTA);
	}

	@Test
	public void testNegate()
	{
		Vector3 a = new Vector3( X, Y, Z );

		a.negate();
		assertEquals( -X, a.x, DELTA);
		assertEquals( -Y, a.y, DELTA);
		assertEquals( -Z, a.z, DELTA);
	}

	@Test
	public void testDot()
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3( -X, -Y, -Z );
		Vector3 c = new Vector3();

		double result = a.dot( b );
		assertEquals( (-X*X-Y*Y-Z*Z), result, DELTA);

		result = a.dot( c );
		assertEquals( 0.0, result, DELTA);
	}

	@Test
	public void testNormalize()
	{
		Vector3 a = new Vector3( X, 0, 0 );
		Vector3 b = new Vector3( 0, -Y, 0 );
		Vector3 c = new Vector3( 0, 0, Z );

		a.normalize();
		assertEquals( 1.0, a.length(), DELTA );
		assertEquals( 1.0, a.x, DELTA);

		b.normalize();
		assertEquals( 1.0, b.length(), DELTA );
		assertEquals( -1.0, b.y, DELTA);

		c.normalize();
		assertEquals( 1.0, c.length(), DELTA );
		assertEquals( 1.0, c.z, DELTA);
	}

	@Test
	public void testSetLength()
	{
		Vector3 a = new Vector3( X, 0, 0 );

		assertEquals( X, a.length(), DELTA );
		a.setLength( Y );
		assertEquals( Y, a.length(), DELTA );

		a = new Vector3( 0, 0, 0 );
		assertEquals( 0.0, a.length(), DELTA );
		a.setLength( Y );
		assertEquals( 0.0, a.length(), DELTA );
	}

	@Test
	public void testLerpClone()
	{
		Vector3 a = new Vector3( X, 0, Z );
		Vector3 b = new Vector3( 0, -Y, 0 );

		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 0.5 ) ));
		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 1 ) ));

		assertTrue( a.clone().lerp( b, 0 ).equals( a ));

		assertEquals( X * 0.5, a.clone().lerp( b, 0.5 ).x, DELTA );
		assertEquals( -Y * 0.5, a.clone().lerp( b, 0.5 ).y, DELTA );
		assertEquals( Z * 0.5, a.clone().lerp( b, 0.5 ).z, DELTA );

		assertTrue( a.clone().lerp( b, 1 ).equals( b ));
	}

	@Test
	public void testEquals()
	{
		Vector3 a = new Vector3( X, 0, Z );
		Vector3 b = new Vector3( 0, -Y, 0 );

		assertTrue( a.x != b.x);
		assertTrue( a.y != b.y);
		assertTrue( a.z != b.z);

		assertTrue( ! a.equals( b ));
		assertTrue( ! b.equals( a ));

		a.copy( b );
		assertEquals( b.x, a.x, DELTA);
		assertEquals( b.y, a.y, DELTA);
		assertEquals( b.z, a.z, DELTA);

		assertTrue( a.equals( b ));
		assertTrue( b.equals( a ));
	}

	@Test
	public void testMinMaxClamp()
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3( -X, -Y, -Z );
		Vector3 c = new Vector3();

		c.copy( a ).min( b );
		assertEquals( -X, c.x, DELTA);
		assertEquals( -Y, c.y, DELTA);
		assertEquals( -Z, c.z, DELTA);

		c.copy( a ).max( b );
		assertEquals( X, c.x, DELTA);
		assertEquals( Y, c.y, DELTA);
		assertEquals( Z, c.z, DELTA);

		c.set( -2*X, 2*Y, -2*Z );
		c.clamp( b, a );
		assertEquals( -X, c.x, DELTA);
		assertEquals( Y, c.y, DELTA);
		assertEquals( -Z, c.z, DELTA);
	}

	@Test
	public void testSetXYZ()
	{
		Vector3 a = new Vector3();
		assertEquals( 0.0, a.x, DELTA);
		assertEquals( 0.0, a.y, DELTA);
		assertEquals( 0.0, a.z, DELTA);

		a.setX( X );
		a.setY( Y );
		a.setZ( Z );

		assertEquals( X, a.x, DELTA);
		assertEquals( Y, a.y, DELTA);
		assertEquals( Z, a.z, DELTA);
	}
}
