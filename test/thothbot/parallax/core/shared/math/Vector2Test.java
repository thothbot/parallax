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

package thothbot.parallax.core.shared.math;

import com.google.gwt.junit.client.GWTTestCase;

public class Vector2Test extends GWTTestCase  
{

	private static double X = 2;
	private static double Y = 3;
	
	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}
	
	public void testVector2()
	{
		Vector2 a = new Vector2();
		assertEquals( 0.0, a.x );
		assertEquals( 0.0, a.y );

		a = new Vector2( X, Y );
		assertEquals( X, a.x );
		assertEquals( Y, a.y );
	}

	public void testSetXSetY()
	{
		Vector2 a = new Vector2();
		assertEquals( 0.0, a.x );
		assertEquals( 0.0, a.y );

		a.setX( X );
		a.setY( Y );
		assertEquals( X, a.x );
		assertEquals( Y, a.y );
	}

	public void testCopy()
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2().copy( a );
		assertEquals( X, b.x );
		assertEquals( Y, b.y );

		// ensure that it is a true copy
		a.x = 0;
		a.y = -1;
		assertEquals( X, b.x );
		assertEquals( Y, b.y );
	}

	public void testSet()
	{
		Vector2 a = new Vector2();
		assertEquals( 0.0, a.x );
		assertEquals( 0.0, a.y );

		a.set( X, Y );
		assertEquals( X, a.x );
		assertEquals( Y, a.y );
	}

	public void testAdd()
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );

		a.add( b );
		assertEquals( 0.0, a.x );
		assertEquals( 0.0, a.y );

		Vector2 c = new Vector2().add( b, b );
		assertEquals( -2.0 * X, c.x );
		assertEquals( -2.0 * Y, c.y );
	}

	public void testSub()
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );

		a.sub( b );
		assertEquals( 2.0 * X, a.x );
		assertEquals( 2.0 * Y, a.y );

		Vector2 c = new Vector2().sub( a, a );
		assertEquals( 0.0, c.x );
		assertEquals( 0.0, c.y );
	}

	public void testMultiplyDivide()
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );

		a.multiply( -2 );
		assertEquals( X *-2.0, a.x );
		assertEquals( Y *-2.0, a.y );

		b.multiply( -2 );
		assertEquals( 2.0 * X, b.x );
		assertEquals( 2.0 * Y, b.y );

		a.divide( -2 );
		assertEquals( X, a.x );
		assertEquals( Y, a.y );

		b.divide( -2 );
		assertEquals( -X, b.x );
		assertEquals( -Y, b.y );
	}

	public void testNegate()
	{
		Vector2 a = new Vector2( X, Y );

		a.negate();
		assertEquals( -X, a.x );
		assertEquals( -Y, a.y );
	}

	public void testDot()
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );
		Vector2 c = new Vector2();

		double result = a.dot( b );
		assertEquals( (-X * X - Y * Y), result );

		result = a.dot( c );
		assertEquals( 0.0, result );
	}

	public void testLength()
	{
		Vector2 a = new Vector2( X, 0 );
		Vector2 b = new Vector2( 0, -Y );
		Vector2 c = new Vector2();

		assertEquals( X, a.length() );
		assertEquals( X * X, a.lengthSq() );
		assertEquals( Y, b.length() );
		assertEquals( Y * Y, b.lengthSq() );
		assertEquals( 0.0, c.length() );
		assertEquals( 0.0, c.lengthSq() );

		a.set( X, Y );
		assertEquals( Math.sqrt( X*X + Y*Y ), a.length() );
		assertEquals( ( X*X + Y*Y ), a.lengthSq() );
	}

	public void testNormalize()
	{
		Vector2 a = new Vector2( X, 0 );
		Vector2 b = new Vector2( 0, -Y );

		a.normalize();
		assertEquals( 1.0, a.length() );
		assertEquals( 1.0, a.x );

		b.normalize();
		assertEquals( 1.0, b.length() );
		assertEquals( -1.0, b.y);
	}

	public void testDistanceTo()
	{
		Vector2 a = new Vector2( X, 0 );
		Vector2 b = new Vector2( 0, -Y );
		Vector2 c = new Vector2();

		assertEquals( X, a.distanceTo( c ) );
		assertEquals( X * X, a.distanceToSquared( c ) );

		assertEquals( Y, b.distanceTo( c ) );
		assertEquals( Y * Y, b.distanceToSquared( c ) );
	}

	public void testSetLength()
	{
		Vector2 a = new Vector2( X, 0 );

		assertEquals( X, a.length() );
		a.setLength( Y );
		assertEquals( Y, a.length() );

		a = new Vector2( 0, 0 );
		assertEquals( 0.0, a.length() );
		a.setLength( Y );
		assertEquals( 0.0, a.length() );
	}

	public void testLerpClone()
	{
		Vector2 a = new Vector2( X, 0 );
		Vector2 b = new Vector2( 0, -Y );

		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 0.5 ) ));
		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 1 ) ));

		assertTrue( a.clone().lerp( b, 0 ).equals( a ));

		assertEquals( X * 0.5, a.clone().lerp( b, 0.5 ).x );
		assertEquals( -Y * 0.5, a.clone().lerp( b, 0.5 ).y );

		assertTrue( a.clone().lerp( b, 1 ).equals( b ));
	}

	public void testMinMaxClamp()
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );
		Vector2 c = new Vector2();

		c.copy( a ).min( b );
		assertEquals( -X, c.x );
		assertEquals( -Y, c.y );

		c.copy( a ).max( b );
		assertEquals( X, c.x );
		assertEquals( Y, c.y );

		c.set( -2*X, 2*Y );
		c.clamp( b, a );
		assertEquals( -X, c.x);
		assertEquals( Y, c.y );
	}

	public void testEquals()
	{
		Vector2 a = new Vector2( X, 0 );
		Vector2 b = new Vector2( 0, -Y );

		assertTrue( a.x != b.x);
		assertTrue( a.y != b.y);

		assertTrue( ! a.equals( b ));
		assertTrue( ! b.equals( a ));

		a.copy( b );
		assertEquals( b.x, a.x );
		assertEquals( b.y, a.y );

		assertTrue( a.equals( b ));
		assertTrue( b.equals( a ));
	}
}
