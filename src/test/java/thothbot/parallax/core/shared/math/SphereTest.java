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

public class SphereTest extends GWTTestCase 
{

	private static Vector3 zero3 = new Vector3();
	private static Vector3 one3 = new Vector3( 1, 1, 1 );
	private static Vector3 two3 = new Vector3( 2, 2, 2 );

	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}
	
	public void testSphere()
	{
		Sphere a = new Sphere();
		assertTrue( a.getCenter().equals( zero3 ));
		assertEquals( 0.0, a.getRadius() );

		Sphere b = new Sphere( one3, 1.0 );
		assertTrue( b.getCenter().equals( one3 ));
		assertEquals( 1.0, b.getRadius() );
	}

	public void testSet()
	{
		Sphere a = new Sphere();
		assertTrue( a.getCenter().equals( zero3 ));
		assertEquals( 0.0, a.getRadius() );

		a.set( one3, 1 );
		assertTrue( a.getCenter().equals( one3 ));
		assertEquals( 1.0, a.getRadius() );
	}

	public void testCopy()
	{
		Sphere a = new Sphere( one3, 1 );
		Sphere b = new Sphere().copy( a );

		assertTrue( b.getCenter().equals( one3 ));
		assertEquals( 1.0, b.getRadius() );

		// ensure that it is a true copy
		a.setCenter( zero3 );
		a.setRadius( 0 );
		assertTrue( b.getCenter().equals( one3 ));
		assertEquals( 1.0, b.getRadius() );
	}

	public void testIsEmpty()
	{
		Sphere a = new Sphere();
		assertTrue( a.isEmpty());

		a.set( one3, 1 );
		assertTrue( ! a.isEmpty());
	}

	public void testIsContainsPoint()
	{
		Sphere a = new Sphere( one3, 1 );

		assertTrue( ! a.isContainsPoint( zero3 ));
		assertTrue( a.isContainsPoint( one3 ));
	}

	public void testDistanceToPoint()
	{
		Sphere a = new Sphere( one3, 1 );

		assertTrue( ( a.distanceToPoint( zero3 ) - 0.7320 ) < 0.001);
		assertEquals( -1.0, a.distanceToPoint( one3 ) );
	}

	public void testIsIntersectsSphere()
	{
		Sphere a = new Sphere( one3, 1 );
		Sphere b = new Sphere( zero3, 1 );
		Sphere c = new Sphere( zero3, 0.25 );

		assertTrue( a.isIntersectsSphere( b ) );
		assertTrue( ! a.isIntersectsSphere( c ) );
	}

	public void testClampPoint()
	{
		Sphere a = new Sphere( one3, 1 );

		assertTrue( a.clampPoint( new Vector3( 1, 1, 3 ) ).equals( new Vector3( 1, 1, 2 ) ));
		assertTrue( a.clampPoint( new Vector3( 1, 1, -3 ) ).equals( new Vector3( 1, 1, 0 ) ));
	}

	public void testGetBoundingBox()
	{
		Sphere a = new Sphere( one3, 1 );

		assertTrue( a.getBoundingBox().equals( new Box3( zero3, two3 ) ));

		a.set( zero3, 0.0 );
		assertTrue( a.getBoundingBox().equals( new Box3( zero3, zero3 ) ));
	}

	public void testTransform()
	{
		Sphere a = new Sphere( one3, 1 );

		Matrix4 m = new Matrix4().makeTranslation( 1, -2, 1 );

		assertTrue( a.clone().apply( m ).getBoundingBox().equals( a.getBoundingBox().apply( m ) ));
	}

	public void testTranslate()
	{
		Sphere a = new Sphere( one3, 1 );

		a.translate( one3.clone().negate() );
		assertTrue( a.getCenter().equals( zero3 ));
	}
}
