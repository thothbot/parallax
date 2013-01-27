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

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

public class Vector2Test extends GWTTestCase  
{

	private static double X = 2;
	private static double Y = 3;
	
	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}
	
	@Test
	public void testVector2() 
	{
		Vector2 a = new Vector2();
		assertEquals( a.x, 0.0);
		assertEquals( a.y, 0.0);

		a = new Vector2( X, Y );
		assertEquals( a.x, X);
		assertEquals( a.y, Y);
	}

	@Test
	public void testSetXSetY() 
	{
		Vector2 a = new Vector2();
		assertEquals( a.x, 0.0);
		assertEquals( a.y, 0.0);

		a.setX( X );
		a.setY( Y );
		assertEquals( a.x, X);
		assertEquals( a.y, Y);
	}

	@Test
	public void testCopy() 
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2().copy( a );
		assertEquals( b.x, X);
		assertEquals( b.y, Y);

		// ensure that it is a true copy
		a.x = 0;
		a.y = -1;
		assertEquals( b.x, X);
		assertEquals( b.y, Y);
	}

	@Test
	public void testSet() 
	{
		Vector2 a = new Vector2();
		assertEquals( a.x, 0.0);
		assertEquals( a.y, 0.0);

		a.set( X, Y );
		assertEquals( a.x, X);
		assertEquals( a.y, Y);
	}

	@Test
	public void testAdd() 
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );

		a.add( b );
		assertEquals( a.x, 0.0);
		assertEquals( a.y, 0.0);

		Vector2 c = new Vector2().add( b, b );
		assertEquals( c.x, -2.0*X);
		assertEquals( c.y, -2.0*Y);
	}

	@Test
	public void testSub() 
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );

		a.sub( b );
		assertEquals( a.x, 2.0 * X);
		assertEquals( a.y, 2.0 * Y);

		Vector2 c = new Vector2().sub( a, a );
		assertEquals( c.x, 0.0);
		assertEquals( c.y, 0.0);
	}

	@Test
	public void testMultiplyDivide() 
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );

		a.multiply( -2 );
		assertEquals( a.x, X *-2.0);
		assertEquals( a.y, Y *-2.0);

		b.multiply( -2 );
		assertEquals( b.x, 2.0 * X);
		assertEquals( b.y, 2.0 * Y);

		a.divide( -2 );
		assertEquals( a.x, X);
		assertEquals( a.y, Y);

		b.divide( -2 );
		assertEquals( b.x, -X);
		assertEquals( b.y, -Y);
	}

	@Test
	public void testNegate() 
	{
		Vector2 a = new Vector2( X, Y );

		a.negate();
		assertEquals( a.x, -X);
		assertEquals( a.y, -Y);
	}

	@Test
	public void testDot() 
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );
		Vector2 c = new Vector2();

		double result = a.dot( b );
		assertEquals( result, (-X * X - Y * Y));

		result = a.dot( c );
		assertEquals( result, 0);
	}

	@Test
	public void testLength() 
	{
		Vector2 a = new Vector2( X, 0 );
		Vector2 b = new Vector2( 0, -Y );
		Vector2 c = new Vector2();

		assertEquals( a.length(), X);
		assertEquals( a.lengthSq(), X*X);
		assertEquals( b.length(), Y);
		assertEquals( b.lengthSq(), Y*Y);
		assertEquals( c.length(), 0.0);
		assertEquals( c.lengthSq(), 0.0);

		a.set( X, Y );
		assertEquals( a.length(), Math.sqrt( X*X + Y*Y ));
		assertEquals( a.lengthSq(), ( X*X + Y*Y ));
	}

	@Test
	public void testNormalize() 
	{
		Vector2 a = new Vector2( X, 0 );
		Vector2 b = new Vector2( 0, -Y );

		a.normalize();
		assertEquals( a.length(), 1.0);
		assertEquals( a.x, 1.0);

		b.normalize();
		assertEquals( b.length(), 1.0);
		assertEquals( b.y, -1.0);
	}

	@Test
	public void testDistanceTo() 
	{
		Vector2 a = new Vector2( X, 0 );
		Vector2 b = new Vector2( 0, -Y );
		Vector2 c = new Vector2();

		assertEquals( a.distanceTo( c ), X);
		assertEquals( a.distanceToSquared( c ), X*X);

		assertEquals( b.distanceTo( c ), Y);
		assertEquals( b.distanceToSquared( c ), Y*Y);
	}

	@Test
	public void testSetLength() 
	{
		Vector2 a = new Vector2( X, 0 );

		assertEquals( a.length(), X);
		a.setLength( Y );
		assertEquals( a.length(), Y);

		a = new Vector2( 0, 0 );
		assertEquals( a.length(), 0.0);
		a.setLength( Y );
		assertEquals( a.length(), 0.0);
	}

	@Test
	public void testLerpClone() 
	{
		Vector2 a = new Vector2( X, 0 );
		Vector2 b = new Vector2( 0, -Y );

		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 0.5 ) ));
		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 1 ) ));

		assertTrue( a.clone().lerp( b, 0 ).equals( a ));

		assertEquals( a.clone().lerp( b, 0.5 ).x, X*0.5);
		assertEquals( a.clone().lerp( b, 0.5 ).y, -Y*0.5);

		assertTrue( a.clone().lerp( b, 1 ).equals( b ));
	}

	@Test
	public void testMinMaxClamp() 
	{
		Vector2 a = new Vector2( X, Y );
		Vector2 b = new Vector2( -X, -Y );
		Vector2 c = new Vector2();

		c.copy( a ).min( b );
		assertEquals( c.x, -X);
		assertEquals( c.y, -Y);

		c.copy( a ).max( b );
		assertEquals( c.x, X);
		assertEquals( c.y, Y);

		c.set( -2*X, 2*Y );
		c.clamp( b, a );
		assertEquals( c.x, -X);
		assertEquals( c.y, Y);
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
		assertEquals( a.x, b.x);
		assertEquals( a.y, b.y);

		assertTrue( a.equals( b ));
		assertTrue( b.equals( a ));
	}
}
