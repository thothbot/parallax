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

public class Vector2Test
{

	private static double DELTA = 0.00001	;

	private static double X = 2;
	private static double Y = 3;

	@Test
	public void testVector2()
	{
		Vector2 a = new Vector2();
		assertEquals( 0.0, a.x, DELTA);
		assertEquals( 0.0, a.y, DELTA);

		a = new Vector2( X, Y );
		assertEquals( X, a.x, DELTA);
		assertEquals( Y, a.y, DELTA);
	}

	@Test
	public void testSetXSetY()
	{
		Vector2 a = new Vector2();
		assertEquals( 0.0, a.x, DELTA);
		assertEquals( 0.0, a.y, DELTA);

		a.setX( X );
		a.setY( Y );
		assertEquals( X, a.x, DELTA);
		assertEquals( Y, a.y, DELTA);
	}

	@Test
	public void testCopy()
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2().copy( a );
		assertEquals( X, b.x, DELTA);
		assertEquals( Y, b.y, DELTA);

		// ensure that it is a true copy
		a.x = 0;
		a.y = -1;
		assertEquals( X, b.x, DELTA);
		assertEquals( Y, b.y, DELTA);
	}

	@Test
	public void testSet()
	{
		Vector2 a = new Vector2();
		assertEquals( 0.0, a.x, DELTA);
		assertEquals( 0.0, a.y, DELTA);

		a.set( X, Y );
		assertEquals( X, a.x, DELTA);
		assertEquals( Y, a.y, DELTA);
	}

	@Test
	public void testAdd()
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );

		a.add( b );
		assertEquals( 0.0, a.x, DELTA);
		assertEquals( 0.0, a.y, DELTA);

		Vector2 c = new Vector2().add( b, b );
		assertEquals( -2.0 * X, c.x, DELTA);
		assertEquals( -2.0 * Y, c.y, DELTA);
	}

	@Test
	public void testSub()
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );

		a.sub( b );
		assertEquals( 2.0 * X, a.x, DELTA);
		assertEquals( 2.0 * Y, a.y, DELTA);

		Vector2 c = new Vector2().sub( a, a );
		assertEquals( 0.0, c.x, DELTA);
		assertEquals( 0.0, c.y, DELTA);
	}

	@Test
	public void testMultiplyDivide()
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );

		a.multiply( -2 );
		assertEquals( X *-2.0, a.x, DELTA);
		assertEquals( Y *-2.0, a.y, DELTA);

		b.multiply( -2 );
		assertEquals( 2.0 * X, b.x, DELTA);
		assertEquals( 2.0 * Y, b.y, DELTA);

		a.divide( -2 );
		assertEquals( X, a.x, DELTA);
		assertEquals( Y, a.y, DELTA);

		b.divide( -2 );
		assertEquals( -X, b.x, DELTA);
		assertEquals( -Y, b.y, DELTA);
	}

	@Test
	public void testNegate()
	{
		Vector2 a = new Vector2( X, Y );

		a.negate();
		assertEquals( -X, a.x, DELTA);
		assertEquals( -Y, a.y, DELTA);
	}

	@Test
	public void testDot()
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );
		Vector2 c = new Vector2();

		double result = a.dot( b );
		assertEquals( (-X * X - Y * Y), result, DELTA);

		result = a.dot( c );
		assertEquals( 0.0, result, DELTA);
	}

	@Test
	public void testLength()
	{
		Vector2 a = new Vector2( X, 0 );
		Vector2 b = new Vector2( 0, -Y );
		Vector2 c = new Vector2();

		assertEquals( X, a.length(), DELTA );
		assertEquals( X * X, a.lengthSq(), DELTA );
		assertEquals( Y, b.length(), DELTA );
		assertEquals( Y * Y, b.lengthSq(), DELTA );
		assertEquals( 0.0, c.length(), DELTA );
		assertEquals( 0.0, c.lengthSq(), DELTA );

		a.set( X, Y );
		assertEquals( Math.sqrt( X*X + Y*Y ), a.length(), DELTA );
		assertEquals( ( X*X + Y*Y ), a.lengthSq(), DELTA );
	}

	@Test
	public void testNormalize()
	{
		Vector2 a = new Vector2( X, 0 );
		Vector2 b = new Vector2( 0, -Y );

		a.normalize();
		assertEquals( 1.0, a.length(), DELTA );
		assertEquals( 1.0, a.x, DELTA);

		b.normalize();
		assertEquals( 1.0, b.length(), DELTA );
		assertEquals( -1.0, b.y, DELTA);
	}

	@Test
	public void testDistanceTo()
	{
		Vector2 a = new Vector2( X, 0 );
		Vector2 b = new Vector2( 0, -Y );
		Vector2 c = new Vector2();

		assertEquals( X, a.distanceTo( c ), DELTA );
		assertEquals( X * X, a.distanceToSquared( c ), DELTA );

		assertEquals( Y, b.distanceTo( c ), DELTA );
		assertEquals( Y * Y, b.distanceToSquared( c ), DELTA );
	}

	@Test
	public void testSetLength()
	{
		Vector2 a = new Vector2( X, 0 );

		assertEquals( X, a.length(), DELTA );
		a.setLength( Y );
		assertEquals( Y, a.length(), DELTA );

		a = new Vector2( 0, 0 );
		assertEquals( 0.0, a.length(), DELTA );
		a.setLength( Y );
		assertEquals( 0.0, a.length(), DELTA );
	}

	@Test
	public void testLerpClone()
	{
		Vector2 a = new Vector2( X, 0 );
		Vector2 b = new Vector2( 0, -Y );

		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 0.5 ) ));
		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 1 ) ));

		assertTrue( a.clone().lerp( b, 0 ).equals( a ));

		assertEquals( X * 0.5, a.clone().lerp( b, 0.5 ).x, DELTA );
		assertEquals( -Y * 0.5, a.clone().lerp( b, 0.5 ).y, DELTA );

		assertTrue( a.clone().lerp( b, 1 ).equals( b ));
	}

	@Test
	public void testMinMaxClamp()
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );
		Vector2 c = new Vector2();

		c.copy( a ).min( b );
		assertEquals( -X, c.x, DELTA);
		assertEquals( -Y, c.y, DELTA);

		c.copy( a ).max( b );
		assertEquals( X, c.x, DELTA);
		assertEquals( Y, c.y, DELTA);

		c.set( -2*X, 2*Y );
		c.clamp( b, a );
		assertEquals( -X, c.x, DELTA);
		assertEquals( Y, c.y, DELTA);
	}

	@Test
	public void testEquals()
	{
		Vector2 a = new Vector2( X, 0 );
		Vector2 b = new Vector2( 0, -Y );

		assertTrue( a.x != b.x);
		assertTrue( a.y != b.y);

		assertTrue( ! a.equals( b ));
		assertTrue( ! b.equals( a ));

		a.copy( b );
		assertEquals( b.x, a.x, DELTA);
		assertEquals( b.y, a.y, DELTA);

		assertTrue( a.equals( b ));
		assertTrue( b.equals( a ));
	}
}
