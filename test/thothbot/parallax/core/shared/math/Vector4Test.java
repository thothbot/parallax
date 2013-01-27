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

	@Test
	public void testVector4() 
	{
		Vector4 a = new Vector4();
		assertEquals( a.x, 0.0);
		assertEquals( a.y, 0.0);
		assertEquals( a.z, 0.0);
		assertEquals( a.w, 1.0);

		a = new Vector4( X, Y, Z, W );
		assertEquals( a.x, X);
		assertEquals( a.y, Y);
		assertEquals( a.z, Z);
		assertEquals( a.w, W);
	}
	
	@Test
	public void testLength() 
	{
		Vector4 a = new Vector4( X, 0, 0, 0 );
		Vector4 b = new Vector4( 0, -Y, 0, 0 );
		Vector4 c = new Vector4( 0, 0, Z, 0 );
		Vector4 d = new Vector4( 0, 0, 0, W );
		Vector4 e = new Vector4( 0, 0, 0, 0 );
		
		assertEquals( a.length(), X);
		assertEquals( a.lengthSq(), X*X);
		assertEquals( b.length(), Y);
		assertEquals( b.lengthSq(), Y*Y);
		assertEquals( c.length(), Z);
		assertEquals( c.lengthSq(), Z*Z);
		assertEquals( d.length(), W);
		assertEquals( d.lengthSq(), W*W);
		assertEquals( e.length(), 0.0);
		assertEquals( e.lengthSq(), 0.0);

		a.set( X, Y, Z, W );
		assertEquals( a.length(), Math.sqrt( X*X + Y*Y + Z*Z + W*W ));
		assertEquals( a.lengthSq(), ( X*X + Y*Y + Z*Z + W*W ));
	}

	@Test
	public void testDistanceTo() 
	{
		Vector4 a = new Vector4( X, 0, 0, 0 );
		Vector4 b = new Vector4( 0, -Y, 0, 0 );
		Vector4 c = new Vector4( 0, 0, Z, 0 );
		Vector4 d = new Vector4( 0, 0, 0, -W );
		Vector4 e = new Vector4();
		
		assertEquals( a.distanceTo( e ), X);
		assertEquals( a.distanceToSquared( e ), X*X);

		assertEquals( b.distanceTo( e ), Y);
		assertEquals( b.distanceToSquared( e ), Y*Y);

		assertEquals( c.distanceTo( e ), Z);
		assertEquals( c.distanceToSquared( e ), Z*Z);

		assertEquals( d.distanceTo( e ), W);
		assertEquals( d.distanceToSquared( e ), W*W);
	}

	@Test
	public void testSetXYZW() 
	{
		Vector4 a = new Vector4();
		assertEquals( a.x, 0.0);
		assertEquals( a.y, 0.0);
		assertEquals( a.z, 0.0);
		assertEquals( a.w, 1.0);

		a.setX( X );
		a.setY( Y );
		a.setZ( Z );
		a.setW( W );

		assertEquals( a.x, X);
		assertEquals( a.y, Y);
		assertEquals( a.z, Z);
		assertEquals( a.w, W);
	}

	@Test
	public void testCopy() 
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4().copy( a );
		assertEquals( b.x, X);
		assertEquals( b.y, Y);
		assertEquals( b.z, Z);
		assertEquals( b.w, W);

		// ensure that it is a true copy
		a.x = 0;
		a.y = -1;
		a.z = -2;
		a.w = -3;
		assertEquals( b.x, X);
		assertEquals( b.y, Y);
		assertEquals( b.z, Z);
		assertEquals( b.w, W);
	}

	@Test
	public void testSet() 
	{
		Vector4 a = new Vector4();
		assertEquals( a.x, 0.0);
		assertEquals( a.y, 0.0);
		assertEquals( a.z, 0.0);
		assertEquals( a.w, 1.0);

		a.set( X, Y, Z, W );
		assertEquals( a.x, X);
		assertEquals( a.y, Y);
		assertEquals( a.z, Z);
		assertEquals( a.w, W);
	}

	@Test
	public void testAdd() 
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );

		a.add( b );
		assertEquals( a.x, 0.0);
		assertEquals( a.y, 0.0);
		assertEquals( a.z, 0.0);
		assertEquals( a.w, 0.0);

		Vector4 c = new Vector4().add( b, b );
		assertEquals( c.x, -2*X);
		assertEquals( c.y, -2*Y);
		assertEquals( c.z, -2*Z);
		assertEquals( c.w, -2*W);
	}

	@Test
	public void testSub() 
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );

		a.sub( b );
		assertEquals( a.x, 2*X);
		assertEquals( a.y, 2*Y);
		assertEquals( a.z, 2*Z);
		assertEquals( a.w, 2*W);

		Vector4 c = new Vector4().sub( a, a );
		assertEquals( c.x, 0.0);
		assertEquals( c.y, 0.0);
		assertEquals( c.z, 0.0);
		assertEquals( c.w, 0.0);
	}

	@Test
	public void testMultiplyDivide() 
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );

		a.multiply( -2 );
		assertEquals( a.x, X*-2);
		assertEquals( a.y, Y*-2);
		assertEquals( a.z, Z*-2);
		assertEquals( a.w, W*-2);

		b.multiply( -2 );
		assertEquals( b.x, 2*X);
		assertEquals( b.y, 2*Y);	
		assertEquals( b.z, 2*Z);	
		assertEquals( b.w, 2*W);	

		a.divide( -2 );
		assertEquals( a.x, X);
		assertEquals( a.y, Y);
		assertEquals( a.z, Z);
		assertEquals( a.w, W);

		b.divide( -2 );
		assertEquals( b.x, -X);
		assertEquals( b.y, -Y);
		assertEquals( b.z, -Z);
		assertEquals( b.w, -W);
	}

	@Test
	public void testNegate() 
	{
		Vector4 a = new Vector4( X, Y, Z, W );

		a.negate();
		assertEquals( a.x, -X);
		assertEquals( a.y, -Y);
		assertEquals( a.z, -Z);
		assertEquals( a.w, -W);
	}

	@Test
	public void testDot() 
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );
		Vector4 c = new Vector4( 0, 0, 0, 0 );

		double result = a.dot( b );
		assertEquals( result, (-X*X-Y*Y-Z*Z-W*W));

		result = a.dot( c );
		assertEquals( result, 0.0);
	}

	@Test
	public void testNormalize() 
	{
		Vector4 a = new Vector4( X, 0, 0, 0 );
		Vector4 b = new Vector4( 0, -Y, 0, 0 );
		Vector4 c = new Vector4( 0, 0, Z, 0 );
		Vector4 d = new Vector4( 0, 0, 0, -W );
		
		a.normalize();
		assertEquals( a.length(), 1.0);
		assertEquals( a.x, 1.0);

		b.normalize();
		assertEquals( b.length(), 1.0);
		assertEquals( b.y, -1.0);

		c.normalize();
		assertEquals( c.length(), 1.0);
		assertEquals( c.z, 1.0);

		d.normalize();
		assertEquals( d.length(), 1.0);
		assertEquals( d.w, -1.0);
	}

	@Test
	public void testSetLength() 
	{
		Vector4 a = new Vector4( X, 0, 0, 0 );

		assertEquals( a.length(), X);
		a.setLength( Y );
		assertEquals( a.length(), Y);

		a = new Vector4( 0, 0, 0, 0 );
		assertEquals( a.length(), 0.0);
		a.setLength( Y );
		assertEquals( a.length(), 0.0);
	}

	@Test
	public void testLerpClone() 
	{
		Vector4 a = new Vector4( X, 0, Z, 0 );
		Vector4 b = new Vector4( 0, -Y, 0, -W );

		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 0.5 ) ));
		assertTrue( a.lerp( a, 0 ).equals( a.lerp( a, 1 ) ));

		assertTrue( a.clone().lerp( b, 0 ).equals( a ));

		assertEquals( a.clone().lerp( b, 0.5 ).x, X*0.5);
		assertEquals( a.clone().lerp( b, 0.5 ).y, -Y*0.5);
		assertEquals( a.clone().lerp( b, 0.5 ).z, Z*0.5);
		assertEquals( a.clone().lerp( b, 0.5 ).w, -W*0.5);

		assertTrue( a.clone().lerp( b, 1 ).equals( b ));
	}

	@Test
	public void testMinMaxClamp() 
	{
		Vector4 a = new Vector4( X, Y, Z, W );
		Vector4 b = new Vector4( -X, -Y, -Z, -W );
		Vector4 c = new Vector4();

		c.copy( a ).min( b );
		assertEquals( c.x, -X);
		assertEquals( c.y, -Y);
		assertEquals( c.z, -Z);
		assertEquals( c.w, -W);

		c.copy( a ).max( b );
		assertEquals( c.x, X);
		assertEquals( c.y, Y);
		assertEquals( c.z, Z);
		assertEquals( c.w, W);

		c.set( -2*X, 2*Y, -2*Z, 2*W );
		c.clamp( b, a );
		assertEquals( c.x, -X);
		assertEquals( c.y, Y);
		assertEquals( c.z, -Z);
		assertEquals( c.w, W);
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
		assertEquals( a.x, b.x);
		assertEquals( a.y, b.y);
		assertEquals( a.z, b.z);
		assertEquals( a.w, b.w);

		assertTrue( a.equals( b ));
		assertTrue( b.equals( a ));
	}
}
