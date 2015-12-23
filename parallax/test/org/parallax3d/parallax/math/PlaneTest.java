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

public class PlaneTest
{
	private static double DELTA = 0.0;

	private static Vector3 zero3 = new Vector3();
	private static Vector3 one3 = new Vector3( 1, 1, 1 );
	
	private static double X = 2;
	private static double Y = 3;
	private static double Z = 4;
	private static double W = 5;

	@Test
	public void testPlane()
	{
		Plane a = new Plane();
		assertEquals( 1.0, a.getNormal().x, DELTA );
		assertEquals( 0.0, a.getNormal().y, DELTA );
		assertEquals( 0.0, a.getNormal().z, DELTA );
		assertEquals( 0.0, a.getConstant(), DELTA );

		a = new Plane( one3.clone(), 0);
		assertEquals( 1.0, a.getNormal().x, DELTA );
		assertEquals( 1.0, a.getNormal().y, DELTA );
		assertEquals( 1.0, a.getNormal().z, DELTA );
		assertEquals( 0.0, a.getConstant(), DELTA );

		a = new Plane( one3.clone(), 1 );
		assertEquals( 1.0, a.getNormal().x, DELTA );
		assertEquals( 1.0, a.getNormal().y, DELTA );
		assertEquals( 1.0, a.getNormal().z, DELTA );
		assertEquals( 1.0, a.getConstant(), DELTA );
	}

	@Test
	public void testSet()
	{
		Plane a = new Plane();
		assertEquals( 1.0, a.getNormal().x, DELTA );
		assertEquals( 0.0, a.getNormal().y, DELTA );
		assertEquals( 0.0, a.getNormal().z, DELTA );
		assertEquals( 0.0, a.getConstant(), DELTA );

		Plane b = a.clone().set( new Vector3( X, Y, Z ), W );
		assertEquals( X, b.getNormal().x, DELTA );
		assertEquals( Y, b.getNormal().y, DELTA );
		assertEquals( Z, b.getNormal().z, DELTA );
		assertEquals( W, b.getConstant(), DELTA );
	}

	@Test
	public void testSetComponents()
	{
		Plane a = new Plane();
		assertEquals( 1.0, a.getNormal().x, DELTA );
		assertEquals( 0.0, a.getNormal().y, DELTA );
		assertEquals( 0.0, a.getNormal().z, DELTA );
		assertEquals( 0.0, a.getConstant(), DELTA );

		Plane b = a.clone().setComponents( X, Y, Z , W );
		assertEquals( X, b.getNormal().x, DELTA );
		assertEquals( Y, b.getNormal().y, DELTA );
		assertEquals( Z, b.getNormal().z, DELTA );
		assertEquals( W, b.getConstant(), DELTA );
	}

	@Test
	public void testSetFromNormalAndCoplanarPoint()
	{
		Vector3 normal = one3.clone().normalize();
		Plane a = new Plane().setFromNormalAndCoplanarPoint( normal, zero3 );
		
		assertTrue( a.getNormal().equals( normal ));
		assertTrue( a.getConstant() == 0.0);
	}

	@Test
	public void testCopy()
	{
		Plane a = new Plane( new Vector3( X, Y, Z ), W );
		Plane b = new Plane().copy( a );
		assertEquals( X, b.getNormal().x, DELTA );
		assertEquals( Y, b.getNormal().y, DELTA );
		assertEquals( Z, b.getNormal().z, DELTA );
		assertEquals( W, b.getConstant(), DELTA );

		// ensure that it is a true copy
		a.getNormal().x = 0;
		a.getNormal().y = -1;
		a.getNormal().z = -2;
		a.setConstant( -3 );
		assertEquals( X, b.getNormal().x, DELTA );
		assertEquals( Y, b.getNormal().y, DELTA );
		assertEquals( Z, b.getNormal().z, DELTA );
		assertEquals( W, b.getConstant(), DELTA );
	}

	@Test
	public void testNormalize()
	{
		Plane a = new Plane( new Vector3( 2, 0, 0 ), 2 );
		
		a.normalize();
		assertEquals( 1.0, a.getNormal().length(), DELTA );
		assertTrue( a.getNormal().equals( new Vector3( 1, 0, 0 ) ));
		assertEquals( 1.0, a.getConstant(), 1.0);
	}

	@Test
	public void testNegate()
	{
		Plane a = new Plane( new Vector3( 2, 0, 0 ), -2 );
		
		a.normalize();
		assertEquals( 3.0, a.distanceToPoint( new Vector3( 4, 0, 0 ) ), DELTA );
		assertEquals( 0.0, a.distanceToPoint( new Vector3( 1, 0, 0 ) ), DELTA );

		a.negate();
		assertEquals( -3.0, a.distanceToPoint( new Vector3( 4, 0, 0 ) ), DELTA );
		assertEquals( 0.0, a.distanceToPoint( new Vector3( 1, 0, 0 ) ), DELTA );
	}

	@Test
	public void testDistanceToPoint()
	{
		Plane a = new Plane( new Vector3( 2, 0, 0 ), -2 );
		
		a.normalize();
		assertEquals( 0.0, a.distanceToPoint( a.projectPoint( zero3.clone() ) ), DELTA );
		assertEquals( 3.0, a.distanceToPoint( new Vector3( 4, 0, 0 ) ), DELTA );
	}

	@Test
	public void testDistanceToSphere()
	{
		Plane a = new Plane( new Vector3( 1, 0, 0 ), 0 );

		Sphere b = new Sphere( new Vector3( 2, 0, 0 ), 1 );
		
		assertEquals( 1.0, a.distanceToSphere( b ), DELTA );

		a.set( new Vector3( 1, 0, 0 ), 2 );
		assertEquals( 3.0, a.distanceToSphere( b ), DELTA );
		a.set( new Vector3( 1, 0, 0 ), -2 );
		assertEquals( -1.0, a.distanceToSphere( b ), DELTA );
	}

	@Test
	public void testProjectPoint()
	{
		Plane a = new Plane( new Vector3( 1, 0, 0 ), 0 );

		assertTrue( a.projectPoint( new Vector3( 10, 0, 0 ) ).equals( zero3 ));
		assertTrue( a.projectPoint( new Vector3( -10, 0, 0 ) ).equals( zero3 ));

		Plane b = new Plane( new Vector3( 0, 1, 0 ), -1 );
		assertTrue( b.projectPoint( new Vector3( 0, 0, 0 ) ).equals( new Vector3( 0, 1, 0 ) ));
		assertTrue( b.projectPoint( new Vector3( 0, 1, 0 ) ).equals( new Vector3( 0, 1, 0 ) ));
	}

	@Test
	public void testOrthoPoint()
	{
		Plane a = new Plane( new Vector3( 1, 0, 0 ), 0 );

		assertTrue( a.orthoPoint( new Vector3( 10, 0, 0 ) ).equals( new Vector3( 10, 0, 0 ) ));
		assertTrue( a.orthoPoint( new Vector3( -10, 0, 0 ) ).equals( new Vector3( -10, 0, 0 ) ));
	}

	@Test
	public void testIntersectLine()
	{
		Plane a = new Plane( new Vector3( 1, 0, 0 ), 0 );

		assertTrue( a.isIntersectionLine( new Line3(new Vector3(-10, 0, 0), new Vector3(10, 0, 0))) );
		assertTrue(a.intersectLine(new Line3(new Vector3(-10, 0, 0), new Vector3(10, 0, 0))).equals(new Vector3(0, 0, 0)));

		Plane b = new Plane( new Vector3( 1, 0, 0 ), -3 );

		assertTrue( b.isIntersectionLine( new Line3(new Vector3( -10, 0, 0 ), new Vector3( 10, 0, 0 ) ) ) );
		assertTrue( b.intersectLine(new Line3(new Vector3(-10, 0, 0), new Vector3(10, 0, 0))).equals(new Vector3(3, 0, 0)));


		Plane c = new Plane( new Vector3( 1, 0, 0 ), -11 );

		assertTrue( ! c.isIntersectionLine( new Line3(new Vector3( -10, 0, 0 ), new Vector3( 10, 0, 0 ) )));
		assertNull( c.intersectLine( new Line3(new Vector3( -10, 0, 0 ), new Vector3( 10, 0, 0 ) ) ));
		
		Plane d = new Plane( new Vector3( 1, 0, 0 ), 11 );

		assertTrue( ! d.isIntersectionLine( new Line3(new Vector3( -10, 0, 0 ), new Vector3( 10, 0, 0 ) )));
		assertNull( d.intersectLine( new Line3(new Vector3( -10, 0, 0 ), new Vector3( 10, 0, 0 ) )));
	}

	@Test
	public void testCoplanarPoint()
	{
		Plane a = new Plane( new Vector3( 1, 0, 0 ), 0 );
		assertEquals( 0.0, a.distanceToPoint( a.coplanarPoint() ), DELTA );

		Plane b = new Plane( new Vector3( 0, 1, 0 ), -1 );
		assertEquals( 0.0, b.distanceToPoint( b.coplanarPoint() ), DELTA );
	}

	@Test
	public void testTransform()
	{
		Plane a = new Plane( new Vector3( 1, 0, 0 ), 0 );

		Matrix4 m = new Matrix4();
		m.makeRotationZ( (double)(Math.PI * 0.5) );

		assertTrue( comparePlane( a.clone().apply( m ), new Plane( new Vector3( 0, 1, 0 ), 0 ) ));

		a = new Plane( new Vector3( 0, 1, 0 ), -1 );
		assertTrue( comparePlane( a.clone().apply( m ), new Plane( new Vector3( -1, 0, 0 ), -1 ) ));

		m.makeTranslation( 1, 1, 1 );
		assertTrue( comparePlane( a.clone().apply( m ), a.clone().translate( new Vector3( 1, 1, 1 ) ) ));
	}

	private boolean comparePlane( Plane a, Plane b ) 
	{
		double threshold = 0.0001;
		return ( a.getNormal().distanceTo( b.getNormal() ) < threshold &&
				Math.abs( a.getConstant() - b.getConstant() ) < threshold );
	};

}
