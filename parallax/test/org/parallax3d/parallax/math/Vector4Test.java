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

public class Vector4Test
{

	private static double DELTA = 0.00001;

	private static double X = 2;
	private static double Y = 3;
	private static double Z = 4;
	private static double W = 5;

	@Test
	public void testVector4()
	{
		Vector4 a = new Vector4();
		assertEquals( 0.0, a.x, DELTA);
		assertEquals( 0.0, a.y, DELTA);
		assertEquals( 0.0, a.z, DELTA);
		assertEquals( 1.0, a.w, DELTA);

		a = new Vector4( X, Y, Z, W );
		assertEquals( X, a.x, DELTA);
		assertEquals( Y, a.y, DELTA);
		assertEquals( Z, a.z, DELTA);
		assertEquals( W, a.w, DELTA);
	}

	@Test
	public void testLength()
	{
		Vector4 a = new Vector4( X, 0, 0, 0 );
		Vector4 b = new Vector4( 0, -Y, 0, 0 );
		Vector4 c = new Vector4( 0, 0, Z, 0 );
		Vector4 d = new Vector4( 0, 0, 0, W );
		Vector4 e = new Vector4( 0, 0, 0, 0 );
		
		assertEquals( X, a.length(), DELTA );
		assertEquals( X * X, a.lengthSq(), DELTA );
		assertEquals( Y, b.length(), DELTA );
		assertEquals( Y * Y, b.lengthSq(), DELTA );
		assertEquals( Z, c.length(), DELTA );
		assertEquals( Z * Z, c.lengthSq(), DELTA );
		assertEquals( W, d.length(), DELTA );
		assertEquals( W * W, d.lengthSq(), DELTA );
		assertEquals( 0.0, e.length(), DELTA );
		assertEquals( 0.0, e.lengthSq(), DELTA );

		a.set( X, Y, Z, W );
		assertEquals( Math.sqrt( X*X + Y*Y + Z*Z + W*W ), a.length(), DELTA );
		assertEquals( ( X*X + Y*Y + Z*Z + W*W ), a.lengthSq(), DELTA );
	}

	@Test
	public void testSetXYZW()
	{
		Vector4 a = new Vector4();
		assertEquals( 0.0, a.x, DELTA);
		assertEquals( 0.0, a.y, DELTA);
		assertEquals( 0.0, a.z, DELTA);
		assertEquals( 1.0, a.w, DELTA);

		a.setX( X );
		a.setY( Y );
		a.setZ( Z );
		a.setW( W );

		assertEquals( X, a.x, DELTA);
		assertEquals( Y, a.y, DELTA);
		assertEquals( Z, a.z, DELTA);
		assertEquals( W, a.w, DELTA);
	}

	@Test
	public void testCopy()
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4().copy( a );
		assertEquals( X, b.x, DELTA);
		assertEquals( Y, b.y, DELTA);
		assertEquals( Z, b.z, DELTA);
		assertEquals( W, b.w, DELTA);

		// ensure that it is a true copy
		a.x = 0;
		a.y = -1;
		a.z = -2;
		a.w = -3;
		assertEquals( X, b.x, DELTA);
		assertEquals( Y, b.y, DELTA);
		assertEquals( Z, b.z, DELTA);
		assertEquals( W, b.w, DELTA);
	}

	@Test
	public void testSet()
	{
		Vector4 a = new Vector4();
		assertEquals( 0.0, a.x, DELTA);
		assertEquals( 0.0, a.y, DELTA);
		assertEquals( 0.0, a.z, DELTA);
		assertEquals( 1.0, a.w, DELTA);

		a.set( X, Y, Z, W );
		assertEquals( X, a.x, DELTA);
		assertEquals( Y, a.y, DELTA);
		assertEquals( Z, a.z, DELTA);
		assertEquals( W, a.w, DELTA);
	}

	@Test
	public void testAdd()
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );

		a.add( b );
		assertEquals( 0.0, a.x, DELTA);
		assertEquals( 0.0, a.y, DELTA);
		assertEquals( 0.0, a.z, DELTA);
		assertEquals( 0.0, a.w, DELTA);

		Vector4 c = new Vector4().add( b, b );
		assertEquals( -2 * X, c.x, DELTA);
		assertEquals( -2 * Y, c.y, DELTA);
		assertEquals( -2 * Z, c.z, DELTA);
		assertEquals( -2 * W, c.w, DELTA);
	}

	@Test
	public void testSub()
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );

		a.sub( b );
		assertEquals( 2 * X, a.x, DELTA);
		assertEquals( 2 * Y, a.y, DELTA);
		assertEquals( 2 * Z, a.z, DELTA);
		assertEquals( 2 * W, a.w, DELTA);

		Vector4 c = new Vector4().sub( a, a );
		assertEquals( 0.0, c.x, DELTA);
		assertEquals( 0.0, c.y, DELTA);
		assertEquals( 0.0, c.z, DELTA);
		assertEquals( 0.0, c.w, DELTA);
	}

	@Test
	public void testMultiplyDivide()
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );

		a.multiply( -2 );
		assertEquals( -2 * X, a.x, DELTA);
		assertEquals( -2 * Y, a.y, DELTA);
		assertEquals( -2 * Z, a.z, DELTA);
		assertEquals( -2 * W, a.w, DELTA);

		b.multiply( -2 );
		assertEquals( 2 * X, b.x, DELTA);
		assertEquals( 2 * Y, b.y, DELTA);
		assertEquals( 2 * Z, b.z, DELTA);
		assertEquals( 2 * W, b.w, DELTA);

		a.divide( -2 );
		assertEquals( X, a.x, DELTA);
		assertEquals( Y, a.y, DELTA);
		assertEquals( Z, a.z, DELTA);
		assertEquals( W, a.w, DELTA);

		b.divide( -2 );
		assertEquals( -X, b.x, DELTA);
		assertEquals( -Y, b.y, DELTA);
		assertEquals( -Z, b.z, DELTA);
		assertEquals( -W, b.w, DELTA);
	}

	@Test
	public void testNegate()
	{
		Vector4 a = new Vector4( X, Y, Z, W );

		a.negate();
		assertEquals( -X, a.x, DELTA);
		assertEquals( -Y, a.y, DELTA);
		assertEquals( -Z, a.z, DELTA);
		assertEquals( -W, a.w, DELTA);
	}

	@Test
	public void testDot()
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );
		Vector4 c = new Vector4( 0, 0, 0, 0 );

		double result = a.dot( b );
		assertEquals( (-X*X-Y*Y-Z*Z-W*W), result, DELTA );

		result = a.dot( c );
		assertEquals( 0.0, result, DELTA);
	}

	@Test
	public void testNormalize()
	{
		Vector4 a = new Vector4( X, 0, 0, 0 );
		Vector4 b = new Vector4( 0, -Y, 0, 0 );
		Vector4 c = new Vector4( 0, 0, Z, 0 );
		Vector4 d = new Vector4( 0, 0, 0, -W );
		
		a.normalize();
		assertEquals( 1.0, a.length(), DELTA );
		assertEquals( 1.0, a.x, DELTA);

		b.normalize();
		assertEquals( 1.0, b.length(), DELTA );
		assertEquals( -1.0, b.y, DELTA);

		c.normalize();
		assertEquals( 1.0, c.length(), DELTA );
		assertEquals( 1.0, c.z, DELTA);

		d.normalize();
		assertEquals( 1.0, d.length(), DELTA );
		assertEquals( -1.0, d.w, DELTA);
	}

	@Test
	public void testSetLength()
	{
		Vector4 a = new Vector4( X, 0, 0, 0 );

		assertEquals( X, a.length(), DELTA );
		a.setLength( Y );
		assertEquals( Y, a.length(), DELTA );

		a = new Vector4( 0, 0, 0, 0 );
		assertEquals( 0.0, a.length(), DELTA );
		a.setLength( Y );
		assertEquals( 0.0, a.length(), DELTA );
	}

	@Test
	public void testLerpClone()
	{
		Vector4 a = new Vector4( X, 0, Z, 0 );
		Vector4 b = new Vector4( 0, -Y, 0, -W );

		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 0.5 ) ));
		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 1 ) ));

		assertTrue( a.clone().lerp( b, 0 ).equals( a ));

		assertEquals( X * 0.5, a.clone().lerp( b, 0.5 ).x, DELTA );
		assertEquals( -Y * 0.5, a.clone().lerp( b, 0.5 ).y, DELTA );
		assertEquals( Z * 0.5, a.clone().lerp( b, 0.5 ).z, DELTA );
		assertEquals( -W * 0.5, a.clone().lerp( b, 0.5 ).w, DELTA );

		assertTrue( a.clone().lerp( b, 1 ).equals( b ));
	}

	@Test
	public void testMinMaxClamp()
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );
		Vector4 c = new Vector4();

		c.copy( a ).min( b );
		assertEquals( -X, c.x, DELTA);
		assertEquals( -Y, c.y, DELTA);
		assertEquals( -Z, c.z, DELTA);
		assertEquals( -W, c.w, DELTA);

		c.copy( a ).max( b );
		assertEquals( X, c.x, DELTA);
		assertEquals( Y, c.y, DELTA);
		assertEquals( Z, c.z, DELTA);
		assertEquals( W, c.w, DELTA);

		c.set( -2*X, 2*Y, -2*Z, 2*W );
		c.clamp( b, a );
		assertEquals( -X, c.x, DELTA);
		assertEquals( Y, c.y, DELTA);
		assertEquals( -Z, c.z, DELTA);
		assertEquals( W, c.w, DELTA);
	}

	@Test
	public void testEquals()
	{
		Vector4 a = new Vector4( X, 0, Z, 0 );
		Vector4 b = new Vector4( 0, -Y, 0, -W );

		assertTrue( a.x != b.x);
		assertTrue( a.y != b.y);
		assertTrue( a.z != b.z);
		assertTrue( a.w != b.w);

		assertTrue( ! a.equals( b ));
		assertTrue( ! b.equals( a ));

		a.copy( b );
		assertEquals( b.x, a.x, DELTA);
		assertEquals( b.y, a.y, DELTA);
		assertEquals( b.z, a.z, DELTA);
		assertEquals( b.w, a.w, DELTA);

		assertTrue( a.equals( b ));
		assertTrue( b.equals( a ));
	}
}
