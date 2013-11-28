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

public class Box2Test extends GWTTestCase 
{

	private static Vector2 negInf2 = new Vector2( Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY );
	private static Vector2 posInf2 = new Vector2( Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY );

	private static Vector2 zero2 = new Vector2();
	private static Vector2 one2 = new Vector2( 1, 1 );

	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}

	public void testBox2()
	{
		Box2 a = new Box2();

		assertTrue( a.getMin().equals( Box2Test.posInf2 ));
		assertTrue( a.getMax().equals( Box2Test.negInf2 ));
		
		Box2 b = new Box2( Box2Test.zero2.clone(), Box2Test.one2.clone() );
		
		assertTrue( b.getMin().equals( Box2Test.zero2 ));
		assertTrue( b.getMax().equals( Box2Test.one2 ));
	}

	public void testSet()
	{
		Box2 a = new Box2();

		a.set( zero2, one2 );
		assertTrue( a.getMin().equals( zero2 ));
		assertTrue( a.getMax().equals( one2 ));
	}

	public void testCopy()
	{
		Box2 a = new Box2( zero2.clone(), one2.clone() );
		Box2 b = new Box2().copy( a );
		assertTrue( b.getMin().equals( zero2 ));
		assertTrue( b.getMax().equals( one2 ));

		// ensure that it is a true copy
		a.setMin( zero2 );
		a.setMax( one2 );
		assertTrue( b.getMin().equals( zero2 ));
		assertTrue( b.getMax().equals( one2 ));
	}

	public void testEmpty()
	{
		Box2 a = new Box2();

		assertTrue( a.isEmpty());

		Box2 b = new Box2( zero2.clone(), one2.clone() );
		assertTrue( ! b.isEmpty());

		b.makeEmpty();
		assertTrue( b.isEmpty());
	}

	public void testCenter()
	{
		Box2 a = new Box2( zero2.clone(), zero2.clone() );

		assertTrue( a.center().equals( zero2 ));

		Box2 b = new Box2( zero2, one2 );
		Vector2 midpoint = one2.clone().multiply( 0.5 );
		assertTrue( b.center().equals( midpoint ));
	}

	public void testSize()
	{
		Box2 a = new Box2( zero2.clone(), zero2.clone() );

		assertTrue( a.size().equals( zero2 ));

		Box2 b = new Box2( zero2.clone(), one2.clone() );
		assertTrue( b.size().equals( one2 ));
	}

	public void testExpandByPoint()
	{
		Box2 a = new Box2( zero2.clone(), zero2.clone() );

		a.expandByPoint( zero2 );
		assertTrue( a.size().equals( zero2 ));

		a.expandByPoint( one2 );
		assertTrue( a.size().equals( one2 ));

		a.expandByPoint( one2.clone().negate() );
		assertTrue( a.size().equals( one2.clone().multiply( 2 ) ));
		assertTrue( a.center().equals( zero2 ));
	}

	public void testExpandByVector()
	{
		Box2 a = new Box2( zero2.clone(), zero2.clone() );

		a.expandByVector( zero2 );
		assertTrue( a.size().equals( zero2 ));

		a.expandByVector( one2 );
		assertTrue( a.size().equals( one2.clone().multiply( 2 ) ));
		assertTrue( a.center().equals( zero2 ));
	}

	public void testExpandByScalar()
	{
		Box2 a = new Box2( zero2.clone(), zero2.clone() );

		a.expandByScalar( 0 );
		assertTrue( a.size().equals( zero2 ));

		a.expandByScalar( 1 );
		assertTrue( a.size().equals( one2.clone().multiply( 2 ) ));
		assertTrue( a.center().equals( zero2 ));
	}

	public void testIsContainsPoint()
	{
		Box2 a = new Box2( zero2.clone(), zero2.clone() );

		assertTrue( a.isContainsPoint( zero2 ));
		assertTrue( ! a.isContainsPoint( one2 ));

		a.expandByScalar( 1 );
		assertTrue( a.isContainsPoint( zero2 ));
		assertTrue( a.isContainsPoint( one2 ));
		assertTrue( a.isContainsPoint( one2.clone().negate() ));
	}

	public void testIsContainsBox()
	{
		Box2 a = new Box2( zero2.clone(), zero2.clone() );
		Box2 b = new Box2( zero2.clone(), one2.clone() );
		Box2 c = new Box2( one2.clone().negate(), one2.clone() );

		assertTrue( a.isContainsBox( a ));
		assertTrue( ! a.isContainsBox( b ));
		assertTrue( ! a.isContainsBox( c ));

		assertTrue( b.isContainsBox( a ));
		assertTrue( c.isContainsBox( a ));
		assertTrue( ! b.isContainsBox( c ));

	}

	public void testGetParameter()
	{
		Box2 a = new Box2( zero2.clone(), one2.clone() );
		Box2 b = new Box2( one2.clone().negate(), one2.clone() );

		assertTrue( a.getParameter( new Vector2( 0, 0 ) ).equals( new Vector2( 0, 0 ) ));
		assertTrue( a.getParameter( new Vector2( 1, 1 ) ).equals( new Vector2( 1, 1 ) ));

		assertTrue( b.getParameter( new Vector2( -1, -1 ) ).equals( new Vector2( 0, 0 ) ));
		assertTrue( b.getParameter( new Vector2( 0, 0 ) ).equals( new Vector2( 0.5, 0.5 ) ));
		assertTrue( b.getParameter( new Vector2( 1, 1 ) ).equals( new Vector2( 1, 1 ) ));
	}

	public void testIsIntersectionBox()
	{
		Box2 a = new Box2( zero2.clone(), zero2.clone() );
		Box2 b = new Box2( zero2.clone(), one2.clone() );
		Box2 c = new Box2( one2.clone().negate(), one2.clone() );

		assertTrue( a.isIntersectionBox( a ));
		assertTrue( a.isIntersectionBox( b ));
		assertTrue( a.isIntersectionBox( c ));

		assertTrue( b.isIntersectionBox( a ));
		assertTrue( c.isIntersectionBox( a ));
		assertTrue( b.isIntersectionBox( c ));

		b.translate( new Vector2( 2, 2 ) );
		assertTrue( ! a.isIntersectionBox( b ));
		assertTrue( ! b.isIntersectionBox( a ));
		assertTrue( ! b.isIntersectionBox( c ));
	}

	public void testClampPoint()
	{
		Box2 a = new Box2( zero2.clone(), zero2.clone() );
		Box2 b = new Box2( one2.clone().negate(), one2.clone() );

		assertTrue( a.clampPoint( new Vector2( 0, 0 ) ).equals( new Vector2( 0, 0 ) ));
		assertTrue( a.clampPoint( new Vector2( 1, 1 ) ).equals( new Vector2( 0, 0 ) ));
		assertTrue( a.clampPoint( new Vector2( -1, -1 ) ).equals( new Vector2( 0, 0 ) ));

		assertTrue( b.clampPoint( new Vector2( 2, 2 ) ).equals( new Vector2( 1, 1 ) ));
		assertTrue( b.clampPoint( new Vector2( 1, 1 ) ).equals( new Vector2( 1, 1 ) ));
		assertTrue( b.clampPoint( new Vector2( 0, 0 ) ).equals( new Vector2( 0, 0 ) ));
		assertTrue( b.clampPoint( new Vector2( -1, -1 ) ).equals( new Vector2( -1, -1 ) ));
		assertTrue( b.clampPoint( new Vector2( -2, -2 ) ).equals( new Vector2( -1, -1 ) ));
	}

	public void testDistanceToPoint()
	{
		Box2 a = new Box2( zero2.clone(), zero2.clone() );
		Box2 b = new Box2( one2.clone().negate(), one2.clone() );

		assertTrue( a.distanceToPoint( new Vector2( 0, 0 ) ) == 0);
		assertTrue( a.distanceToPoint( new Vector2( 1, 1 ) ) == Math.sqrt( 2 ));
		assertTrue( a.distanceToPoint( new Vector2( -1, -1 ) ) == Math.sqrt( 2 ));

		assertTrue( b.distanceToPoint( new Vector2( 2, 2 ) ) == Math.sqrt( 2 ));
		assertTrue( b.distanceToPoint( new Vector2( 1, 1 ) ) == 0);
		assertTrue( b.distanceToPoint( new Vector2( 0, 0 ) ) == 0);
		assertTrue( b.distanceToPoint( new Vector2( -1, -1 ) ) == 0);
		assertTrue( b.distanceToPoint( new Vector2( -2, -2 ) ) == Math.sqrt( 2 ));
	}

	public void testIntersect()
	{
		Box2 a = new Box2( zero2.clone(), zero2.clone() );
		Box2 b = new Box2( zero2.clone(), one2.clone() );
		Box2 c = new Box2( one2.clone().negate(), one2.clone() );

		assertTrue( a.clone().intersect( a ).equals( a ));
		assertTrue( a.clone().intersect( b ).equals( a ));
		assertTrue( b.clone().intersect( b ).equals( b ));
		assertTrue( a.clone().intersect( c ).equals( a ));
		assertTrue( b.clone().intersect( c ).equals( b ));
		assertTrue( c.clone().intersect( c ).equals( c ));
	}

	public void testUnion()
	{
		Box2 a = new Box2( zero2.clone(), zero2.clone() );
		Box2 b = new Box2( zero2.clone(), one2.clone() );
		Box2 c = new Box2( one2.clone().negate(), one2.clone() );

		assertTrue( a.clone().union( a ).equals( a ));
		assertTrue( a.clone().union( b ).equals( b ));
		assertTrue( a.clone().union( c ).equals( c ));
		assertTrue( b.clone().union( c ).equals( c ));
	}

	public void testTranslate()
	{
		Box2 a = new Box2( zero2.clone(), zero2.clone() );
		Box2 b = new Box2( zero2.clone(), one2.clone() );
		Box2 c = new Box2( one2.clone().negate(), one2.clone() );
		Box2 d = new Box2( one2.clone().negate(), zero2.clone() );

		assertTrue( a.clone().translate( one2 ).equals( new Box2( one2, one2 ) ));
		assertTrue( a.clone().translate( one2 ).translate( one2.clone().negate() ).equals( a ));
		assertTrue( d.clone().translate( one2 ).equals( b ));
		assertTrue( b.clone().translate( one2.clone().negate() ).equals( d ));
	}
}
