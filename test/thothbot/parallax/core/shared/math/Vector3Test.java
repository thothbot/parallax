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

public class Vector3Test extends GWTTestCase
{

	private static double X = 2;
	private static double Y = 3;
	private static double Z = 4;
	
	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}

	public void testVector3()
	{
		Vector3 a = new Vector3();
		assertEquals( 0.0, a.x );
		assertEquals( 0.0, a.y );
		assertEquals( 0.0, a.z );

		a = new Vector3( X, Y, Z );
		assertEquals( X, a.x );
		assertEquals( Y, a.y );
		assertEquals( Z, a.z );
	}
	
	public void testLength()
	{
		Vector3 a = new Vector3( X, 0, 0 );
		Vector3 b = new Vector3( 0, -Y, 0 );
		Vector3 c = new Vector3( 0, 0, Z );
		Vector3 d = new Vector3();

		assertEquals( X, a.length() );
		assertEquals( X * X, a.lengthSq() );
		assertEquals( Y, b.length() );
		assertEquals( Y * Y, b.lengthSq() );
		assertEquals( Z, c.length() );
		assertEquals( Z * Z, c.lengthSq() );
		assertEquals( 0.0, d.length() );
		assertEquals( 0.0, d.lengthSq() );

		a.set( X, Y, Z );
		assertEquals( Math.sqrt( X*X + Y*Y + Z*Z ), a.length() );
		assertEquals( ( X*X + Y*Y + Z*Z ), a.lengthSq() );
	}

	public void testDistanceTo()
	{
		Vector3 a = new Vector3( X, 0, 0 );
		Vector3 b = new Vector3( 0, -Y, 0 );
		Vector3 c = new Vector3( 0, 0, Z );
		Vector3 d = new Vector3();

		assertEquals( X, a.distanceTo( d ) );
		assertEquals( X * X, a.distanceToSquared( d ) );

		assertEquals( Y, b.distanceTo( d ) );
		assertEquals( Y * Y, b.distanceToSquared( d ) );

		assertEquals( Z, c.distanceTo( d ) );
		assertEquals( Z * Z, c.distanceToSquared( d ) );
	}

	public void testCopy()
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3().copy( a );
		assertEquals( X, b.x );
		assertEquals( Y, b.y );
		assertEquals( Z, b.z );

		// ensure that it is a true copy
		a.x = 0;
		a.y = -1;
		a.z = -2;
		assertEquals( X, b.x );
		assertEquals( Y, b.y );
		assertEquals( Z, b.z );
	}

	public void testSet()
	{
		Vector3 a = new Vector3();
		assertEquals( 0.0, a.x );
		assertEquals( 0.0, a.y );
		assertEquals( 0.0, a.z );

		a.set( X, Y, Z );
		assertEquals( X, a.x );
		assertEquals( Y, a.y );
		assertEquals( Z, a.z );
	}

	public void testAdd()
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3( -X, -Y, -Z );

		a.add( b );
		assertEquals( 0.0, a.x, 0.0);
		assertEquals( 0.0, a.y, 0.0);
		assertEquals( 0.0, a.z, 0.0);

		Vector3 c = new Vector3().add( b, b );
		assertEquals( -2*X, c.x );
		assertEquals( -2*Y, c.y );
		assertEquals( -2*Z, c.z );
	}

	public void testSub()
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3( -X, -Y, -Z );

		a.sub( b );
		assertEquals( 2 * X, a.x );
		assertEquals( 2 * Y, a.y );
		assertEquals( 2 * Z, a.z );

		Vector3 c = new Vector3().sub( a, a );
		assertEquals( 0.0, c.x );
		assertEquals( 0.0, c.y );
		assertEquals( 0.0, c.z );
	}

	public void testMultiplyDivide()
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3( -X, -Y, -Z );

		a.multiply( -2 );
		assertEquals( X * -2, a.x );
		assertEquals( Y * -2, a.y );
		assertEquals( Z * -2, a.z );

		b.multiply( -2 );
		assertEquals( 2 * X, b.x );
		assertEquals( 2 * Y, b.y );
		assertEquals( 2 * Z, b.z );

		a.divide( -2 );
		assertEquals( X, a.x );
		assertEquals( Y, a.y );
		assertEquals( Z, a.z );

		b.divide( -2 );
		assertEquals( -X, b.x );
		assertEquals( -Y, b.y );
		assertEquals( -Z, b.z );
	}

	public void testNegate()
	{
		Vector3 a = new Vector3( X, Y, Z );

		a.negate();
		assertEquals( -X, a.x );
		assertEquals( -Y, a.y );
		assertEquals( -Z, a.z );
	}

	public void testDot()
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3( -X, -Y, -Z );
		Vector3 c = new Vector3();

		double result = a.dot( b );
		assertEquals( (-X*X-Y*Y-Z*Z), result );

		result = a.dot( c );
		assertEquals( 0.0, result );
	}

	public void testNormalize()
	{
		Vector3 a = new Vector3( X, 0, 0 );
		Vector3 b = new Vector3( 0, -Y, 0 );
		Vector3 c = new Vector3( 0, 0, Z );

		a.normalize();
		assertEquals( 1.0, a.length() );
		assertEquals( 1.0, a.x );

		b.normalize();
		assertEquals( 1.0, b.length() );
		assertEquals( -1.0, b.y );

		c.normalize();
		assertEquals( 1.0, c.length() );
		assertEquals( 1.0, c.z );
	}

	public void testSetLength()
	{
		Vector3 a = new Vector3( X, 0, 0 );

		assertEquals( X, a.length() );
		a.setLength( Y );
		assertEquals( Y, a.length() );

		a = new Vector3( 0, 0, 0 );
		assertEquals( 0.0, a.length() );
		a.setLength( Y );
		assertEquals( 0.0, a.length() );
	}

	public void testLerpClone()
	{
		Vector3 a = new Vector3( X, 0, Z );
		Vector3 b = new Vector3( 0, -Y, 0 );

		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 0.5 ) ));
		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 1 ) ));

		assertTrue( a.clone().lerp( b, 0 ).equals( a ));

		assertEquals( X * 0.5, a.clone().lerp( b, 0.5 ).x );
		assertEquals( -Y * 0.5, a.clone().lerp( b, 0.5 ).y );
		assertEquals( Z * 0.5, a.clone().lerp( b, 0.5 ).z );

		assertTrue( a.clone().lerp( b, 1 ).equals( b ));
	}

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
		assertEquals( b.x, a.x);
		assertEquals( b.y, a.y);
		assertEquals( b.z, a.z);

		assertTrue( a.equals( b ));
		assertTrue( b.equals( a ));
	}

	public void testMinMaxClamp()
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3( -X, -Y, -Z );
		Vector3 c = new Vector3();

		c.copy( a ).min( b );
		assertEquals( -X, c.x );
		assertEquals( -Y, c.y );
		assertEquals( -Z, c.z );

		c.copy( a ).max( b );
		assertEquals( X, c.x );
		assertEquals( Y, c.y );
		assertEquals( Z, c.z );

		c.set( -2*X, 2*Y, -2*Z );
		c.clamp( b, a );
		assertEquals( -X, c.x );
		assertEquals( Y, c.y );
		assertEquals( -Z, c.z );
	}

	public void testSetXYZ()
	{
		Vector3 a = new Vector3();
		assertEquals( 0.0, a.x );
		assertEquals( 0.0, a.y );
		assertEquals( 0.0, a.z );

		a.setX( X );
		a.setY( Y );
		a.setZ( Z );

		assertEquals( X, a.x );
		assertEquals( Y, a.y );
		assertEquals( Z, a.z );
	}
}
