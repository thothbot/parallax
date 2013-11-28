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

public class Vector4Test extends GWTTestCase 
{

	private static double X = 2;
	private static double Y = 3;
	private static double Z = 4;
	private static double W = 5;

	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}

	public void testVector4()
	{
		Vector4 a = new Vector4();
		assertEquals( 0.0, a.x );
		assertEquals( 0.0, a.y );
		assertEquals( 0.0, a.z );
		assertEquals( 1.0, a.w );

		a = new Vector4( X, Y, Z, W );
		assertEquals( X, a.x );
		assertEquals( Y, a.y );
		assertEquals( Z, a.z );
		assertEquals( W, a.w );
	}
	
	public void testLength()
	{
		Vector4 a = new Vector4( X, 0, 0, 0 );
		Vector4 b = new Vector4( 0, -Y, 0, 0 );
		Vector4 c = new Vector4( 0, 0, Z, 0 );
		Vector4 d = new Vector4( 0, 0, 0, W );
		Vector4 e = new Vector4( 0, 0, 0, 0 );
		
		assertEquals( X, a.length() );
		assertEquals( X * X, a.lengthSq() );
		assertEquals( Y, b.length() );
		assertEquals( Y * Y, b.lengthSq() );
		assertEquals( Z, c.length() );
		assertEquals( Z * Z, c.lengthSq() );
		assertEquals( W, d.length() );
		assertEquals( W * W, d.lengthSq() );
		assertEquals( 0.0, e.length() );
		assertEquals( 0.0, e.lengthSq() );

		a.set( X, Y, Z, W );
		assertEquals( Math.sqrt( X*X + Y*Y + Z*Z + W*W ), a.length() );
		assertEquals( ( X*X + Y*Y + Z*Z + W*W ), a.lengthSq() );
	}

	public void testSetXYZW()
	{
		Vector4 a = new Vector4();
		assertEquals( 0.0, a.x );
		assertEquals( 0.0, a.y );
		assertEquals( 0.0, a.z );
		assertEquals( 1.0, a.w );

		a.setX( X );
		a.setY( Y );
		a.setZ( Z );
		a.setW( W );

		assertEquals( X, a.x );
		assertEquals( Y, a.y );
		assertEquals( Z, a.z );
		assertEquals( W, a.w );
	}

	public void testCopy()
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4().copy( a );
		assertEquals( X, b.x );
		assertEquals( Y, b.y );
		assertEquals( Z, b.z );
		assertEquals( W, b.w );

		// ensure that it is a true copy
		a.x = 0;
		a.y = -1;
		a.z = -2;
		a.w = -3;
		assertEquals( X, b.x );
		assertEquals( Y, b.y );
		assertEquals( Z, b.z );
		assertEquals( W, b.w );
	}

	public void testSet()
	{
		Vector4 a = new Vector4();
		assertEquals( 0.0, a.x );
		assertEquals( 0.0, a.y );
		assertEquals( 0.0, a.z );
		assertEquals( 1.0, a.w );

		a.set( X, Y, Z, W );
		assertEquals( X, a.x );
		assertEquals( Y, a.y );
		assertEquals( Z, a.z );
		assertEquals( W, a.w );
	}

	public void testAdd()
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );

		a.add( b );
		assertEquals( 0.0, a.x );
		assertEquals( 0.0, a.y );
		assertEquals( 0.0, a.z );
		assertEquals( 0.0, a.w );

		Vector4 c = new Vector4().add( b, b );
		assertEquals( -2 * X, c.x );
		assertEquals( -2 * Y, c.y );
		assertEquals( -2 * Z, c.z );
		assertEquals( -2 * W, c.w );
	}

	public void testSub()
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );

		a.sub( b );
		assertEquals( 2 * X, a.x );
		assertEquals( 2 * Y, a.y );
		assertEquals( 2 * Z, a.z );
		assertEquals( 2 * W, a.w );

		Vector4 c = new Vector4().sub( a, a );
		assertEquals( 0.0, c.x );
		assertEquals( 0.0, c.y );
		assertEquals( 0.0, c.z );
		assertEquals( 0.0, c.w );
	}

	public void testMultiplyDivide()
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );

		a.multiply( -2 );
		assertEquals( -2 * X, a.x );
		assertEquals( -2 * Y, a.y );
		assertEquals( -2 * Z, a.z );
		assertEquals( -2 * W, a.w );

		b.multiply( -2 );
		assertEquals( 2 * X, b.x );
		assertEquals( 2 * Y, b.y );
		assertEquals( 2 * Z, b.z );
		assertEquals( 2 * W, b.w );

		a.divide( -2 );
		assertEquals( X, a.x );
		assertEquals( Y, a.y );
		assertEquals( Z, a.z );
		assertEquals( W, a.w );

		b.divide( -2 );
		assertEquals( -X, b.x );
		assertEquals( -Y, b.y );
		assertEquals( -Z, b.z );
		assertEquals( -W, b.w );
	}

	public void testNegate()
	{
		Vector4 a = new Vector4( X, Y, Z, W );

		a.negate();
		assertEquals( -X, a.x );
		assertEquals( -Y, a.y );
		assertEquals( -Z, a.z );
		assertEquals( -W, a.w );
	}

	public void testDot()
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );
		Vector4 c = new Vector4( 0, 0, 0, 0 );

		double result = a.dot( b );
		assertEquals( (-X*X-Y*Y-Z*Z-W*W), result );

		result = a.dot( c );
		assertEquals( 0.0, result );
	}

	public void testNormalize()
	{
		Vector4 a = new Vector4( X, 0, 0, 0 );
		Vector4 b = new Vector4( 0, -Y, 0, 0 );
		Vector4 c = new Vector4( 0, 0, Z, 0 );
		Vector4 d = new Vector4( 0, 0, 0, -W );
		
		a.normalize();
		assertEquals( 1.0, a.length() );
		assertEquals( 1.0, a.x );

		b.normalize();
		assertEquals( 1.0, b.length() );
		assertEquals( -1.0, b.y);

		c.normalize();
		assertEquals( 1.0, c.length() );
		assertEquals( 1.0, c.z );

		d.normalize();
		assertEquals( 1.0, d.length() );
		assertEquals( -1.0, d.w );
	}

	public void testSetLength()
	{
		Vector4 a = new Vector4( X, 0, 0, 0 );

		assertEquals( X, a.length() );
		a.setLength( Y );
		assertEquals( Y, a.length() );

		a = new Vector4( 0, 0, 0, 0 );
		assertEquals( 0.0, a.length() );
		a.setLength( Y );
		assertEquals( 0.0, a.length() );
	}

	public void testLerpClone()
	{
		Vector4 a = new Vector4( X, 0, Z, 0 );
		Vector4 b = new Vector4( 0, -Y, 0, -W );

		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 0.5 ) ));
		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 1 ) ));

		assertTrue( a.clone().lerp( b, 0 ).equals( a ));

		assertEquals( X * 0.5, a.clone().lerp( b, 0.5 ).x );
		assertEquals( -Y * 0.5, a.clone().lerp( b, 0.5 ).y );
		assertEquals( Z * 0.5, a.clone().lerp( b, 0.5 ).z );
		assertEquals( -W * 0.5, a.clone().lerp( b, 0.5 ).w );

		assertTrue( a.clone().lerp( b, 1 ).equals( b ));
	}

	public void testMinMaxClamp()
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );
		Vector4 c = new Vector4();

		c.copy( a ).min( b );
		assertEquals( -X, c.x );
		assertEquals( -Y, c.y );
		assertEquals( -Z, c.z );
		assertEquals( -W, c.w );

		c.copy( a ).max( b );
		assertEquals( X, c.x );
		assertEquals( Y, c.y );
		assertEquals( Z, c.z );
		assertEquals( W, c.w );

		c.set( -2*X, 2*Y, -2*Z, 2*W );
		c.clamp( b, a );
		assertEquals( -X, c.x );
		assertEquals( Y, c.y );
		assertEquals( -Z, c.z );
		assertEquals( W, c.w );
	}

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
		assertEquals( b.x, a.x);
		assertEquals( b.y, a.y);
		assertEquals( b.z, a.z);
		assertEquals( b.w, a.w);

		assertTrue( a.equals( b ));
		assertTrue( b.equals( a ));
	}
}
