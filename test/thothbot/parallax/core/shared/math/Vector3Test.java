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

public class Vector3Test extends GWTTestCase
{

	private static double X = 2;
	private static double Y = 3;
	private static double Z = 4;
	
	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}

	@Test
	public void testVector3() 
	{
		Vector3 a = new Vector3();
		assertEquals( a.x, 0.0);
		assertEquals( a.y, 0.0);
		assertEquals( a.z, 0.0);

		a = new Vector3( X, Y, Z );
		assertEquals( a.x, X);
		assertEquals( a.y, Y);
		assertEquals( a.z, Z);
	}
	
	@Test
	public void testLength() 
	{
		Vector3 a = new Vector3( X, 0, 0 );
		Vector3 b = new Vector3( 0, -Y, 0 );
		Vector3 c = new Vector3( 0, 0, Z );
		Vector3 d = new Vector3();

		assertEquals( a.length(), X);
		assertEquals( a.lengthSq(), X*X);
		assertEquals( b.length(), Y);
		assertEquals( b.lengthSq(), Y*Y);
		assertEquals( c.length(), Z);
		assertEquals( c.lengthSq(), Z*Z);
		assertEquals( d.length(), 0.0);
		assertEquals( d.lengthSq(), 0.0);

		a.set( X, Y, Z );
		assertEquals( a.length(), Math.sqrt( X*X + Y*Y + Z*Z ));
		assertEquals( a.lengthSq(), ( X*X + Y*Y + Z*Z ));
	}

	@Test
	public void testDistanceTo() 
	{
		Vector3 a = new Vector3( X, 0, 0 );
		Vector3 b = new Vector3( 0, -Y, 0 );
		Vector3 c = new Vector3( 0, 0, Z );
		Vector3 d = new Vector3();

		assertEquals( a.distanceTo( d ), X);
		assertEquals( a.distanceToSquared( d ), X*X);

		assertEquals( b.distanceTo( d ), Y);
		assertEquals( b.distanceToSquared( d ), Y*Y);

		assertEquals( c.distanceTo( d ), Z);
		assertEquals( c.distanceToSquared( d ), Z*Z);
	}

	@Test
	public void testCopy() 
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3().copy( a );
		assertEquals( b.x, X);
		assertEquals( b.y, Y);
		assertEquals( b.z, Z);

		// ensure that it is a true copy
		a.x = 0;
		a.y = -1;
		a.z = -2;
		assertEquals( b.x, X);
		assertEquals( b.y, Y);
		assertEquals( b.z, Z);
	}

	@Test
	public void testSet() 
	{
		Vector3 a = new Vector3();
		assertEquals( a.x, 0.0);
		assertEquals( a.y, 0.0);
		assertEquals( a.z, 0.0);

		a.set( X, Y, Z );
		assertEquals( a.x, X);
		assertEquals( a.y, Y);
		assertEquals( a.z, Z);
	}

	@Test
	public void testAdd() 
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3( -X, -Y, -Z );

		a.add( b );
		assertEquals( a.x, 0.0);
		assertEquals( a.y, 0.0);
		assertEquals( a.z, 0.0);

		Vector3 c = new Vector3().add( b, b );
		assertEquals( c.x, -2*X);
		assertEquals( c.y, -2*Y);
		assertEquals( c.z, -2*Z);
	}

	@Test
	public void testSub() 
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3( -X, -Y, -Z );

		a.sub( b );
		assertEquals( a.x, 2*X);
		assertEquals( a.y, 2*Y);
		assertEquals( a.z, 2*Z);

		Vector3 c = new Vector3().sub( a, a );
		assertEquals( c.x, 0.0);
		assertEquals( c.y, 0.0);
		assertEquals( c.z, 0.0);
	}

	@Test
	public void testMultiplyDivide() 
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3( -X, -Y, -Z );

		a.multiply( -2 );
		assertEquals( a.x, X*-2);
		assertEquals( a.y, Y*-2);
		assertEquals( a.z, Z*-2);

		b.multiply( -2 );
		assertEquals( b.x, 2*X);
		assertEquals( b.y, 2*Y);
		assertEquals( b.z, 2*Z);

		a.divide( -2 );
		assertEquals( a.x, X);
		assertEquals( a.y, Y);
		assertEquals( a.z, Z);

		b.divide( -2 );
		assertEquals( b.x, -X);
		assertEquals( b.y, -Y);
		assertEquals( b.z, -Z);
	}

	@Test
	public void testNegate() 
	{
		Vector3 a = new Vector3( X, Y, Z );

		a.negate();
		assertEquals( a.x, -X);
		assertEquals( a.y, -Y);
		assertEquals( a.z, -Z);
	}

	@Test
	public void testDot() 
	{
		Vector3 a = new Vector3( X, Y, Z );
		Vector3 b = new Vector3( -X, -Y, -Z );
		Vector3 c = new Vector3();

		double result = a.dot( b );
		assertEquals( result, (-X*X-Y*Y-Z*Z));

		result = a.dot( c );
		assertEquals( result, 0.0);
	}

	@Test
	public void testNormalize() 
	{
		Vector3 a = new Vector3( X, 0, 0 );
		Vector3 b = new Vector3( 0, -Y, 0 );
		Vector3 c = new Vector3( 0, 0, Z );

		a.normalize();
		assertEquals( a.length(), 1.0);
		assertEquals( a.x, 1.0);

		b.normalize();
		assertEquals( b.length(), 1.0);
		assertEquals( b.y, -1.0);

		c.normalize();
		assertEquals( c.length(), 1.0);
		assertEquals( c.z, 1.0);
	}

	@Test
	public void testSetLength() 
	{
		Vector3 a = new Vector3( X, 0, 0 );

		assertEquals( a.length(), X);
		a.setLength( Y );
		assertEquals( a.length(), Y);

		a = new Vector3( 0, 0, 0 );
		assertEquals( a.length(), 0.0);
		a.setLength( Y );
		assertEquals( a.length(), 0.0);
	}

	@Test
	public void testLerpClone() 
	{
		Vector3 a = new Vector3( X, 0, Z );
		Vector3 b = new Vector3( 0, -Y, 0 );

		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 0.5 ) ));
		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 1 ) ));

		assertTrue( a.clone().lerp( b, 0 ).equals( a ));

		assertEquals( a.clone().lerp( b, 0.5 ).x, X*0.5);
		assertEquals( a.clone().lerp( b, 0.5 ).y, -Y*0.5);
		assertEquals( a.clone().lerp( b, 0.5 ).z, Z*0.5);

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
		assertEquals( a.x, b.x);
		assertEquals( a.y, b.y);
		assertEquals( a.z, b.z);

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
		assertEquals( c.x, -X);
		assertEquals( c.y, -Y);
		assertEquals( c.z, -Z);

		c.copy( a ).max( b );
		assertEquals( c.x, X);
		assertEquals( c.y, Y);
		assertEquals( c.z, Z);

		c.set( -2*X, 2*Y, -2*Z );
		c.clamp( b, a );
		assertEquals( c.x, -X);
		assertEquals( c.y, Y);
		assertEquals( c.z, -Z);
	}

	@Test
	public void testSetXYZ() 
	{
		Vector3 a = new Vector3();
		assertEquals( a.x, 0.0);
		assertEquals( a.y, 0.0);
		assertEquals( a.z, 0.0);

		a.setX( X );
		a.setY( Y );
		a.setZ( Z );

		assertEquals( a.x, X);
		assertEquals( a.y, Y);
		assertEquals( a.z, Z);
	}
}
