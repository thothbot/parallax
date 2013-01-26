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

import thothbot.parallax.core.shared.Log;

import com.google.gwt.junit.client.GWTTestCase;

public class PlaneTest extends GWTTestCase  
{

	private static Vector3 zero3 = new Vector3();
	private static Vector3 one3 = new Vector3( 1, 1, 1 );
	
	private static double X = 2;
	private static double Y = 3;
	private static double Z = 4;
	private static double W = 5;

	
	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}
	
	@Test
	public void testPlane() 
	{
		Plane a = new Plane();
		assertEquals( a.getNormal().x, 1.0);
		assertEquals( a.getNormal().y, 0.0);
		assertEquals( a.getNormal().z, 0.0);
		assertEquals( a.getConstant(), 0.0);

		a = new Plane( one3.clone(), 0);
		assertEquals( a.getNormal().x, 1.0);
		assertEquals( a.getNormal().y, 1.0);
		assertEquals( a.getNormal().z, 1.0);
		assertEquals( a.getConstant(), 0.0);

		a = new Plane( one3.clone(), 1 );
		assertEquals( a.getNormal().x, 1.0);
		assertEquals( a.getNormal().y, 1.0);
		assertEquals( a.getNormal().z, 1.0);
		assertEquals( a.getConstant(), 1.0);
	}

	@Test
	public void testSet() 
	{
		Plane a = new Plane();
		assertEquals( a.getNormal().x, 1.0);
		assertEquals( a.getNormal().y, 0.0);
		assertEquals( a.getNormal().z, 0.0);
		assertEquals( a.getConstant(), 0.0);

		Plane b = a.clone().set( new Vector3( X, Y, Z ), W );
		assertEquals( b.getNormal().x, X);
		assertEquals( b.getNormal().y, Y);
		assertEquals( b.getNormal().z, Z);
		assertEquals( b.getConstant(), W);
	}

	@Test
	public void testSetComponents() 
	{
		Plane a = new Plane();
		assertEquals( a.getNormal().x, 1.0);
		assertEquals( a.getNormal().y, 0.0);
		assertEquals( a.getNormal().z, 0.0);
		assertEquals( a.getConstant(), 0.0);

		Plane b = a.clone().setComponents( X, Y, Z , W );
		assertEquals( b.getNormal().x, X);
		assertEquals( b.getNormal().y, Y);
		assertEquals( b.getNormal().z, Z);
		assertEquals( b.getConstant(), W);
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
		assertEquals( b.getNormal().x, X);
		assertEquals( b.getNormal().y, Y);
		assertEquals( b.getNormal().z, Z);
		assertEquals( b.getConstant(), W);

		// ensure that it is a true copy
		a.getNormal().x = 0;
		a.getNormal().y = -1;
		a.getNormal().z = -2;
		a.setConstant( -3 );
		assertEquals( b.getNormal().x, X);
		assertEquals( b.getNormal().y, Y);
		assertEquals( b.getNormal().z, Z);
		assertEquals( b.getConstant(), W);
	}

	@Test
	public void testNormalize() 
	{
		Plane a = new Plane( new Vector3( 2, 0, 0 ), 2 );
		
		a.normalize();
		assertEquals( a.getNormal().length(), 1.0);
		assertTrue( a.getNormal().equals( new Vector3( 1, 0, 0 ) ));
		assertEquals( a.getConstant(), 1.0);
	}

	@Test
	public void testNegate() 
	{
		Plane a = new Plane( new Vector3( 2, 0, 0 ), -2 );
		
		a.normalize();
		assertEquals( a.distanceToPoint( new Vector3( 4, 0, 0 ) ), 3.0);
		assertEquals( a.distanceToPoint( new Vector3( 1, 0, 0 ) ), 0.0);

		a.negate();
		assertEquals( a.distanceToPoint( new Vector3( 4, 0, 0 ) ), -3.0);
		assertEquals( a.distanceToPoint( new Vector3( 1, 0, 0 ) ), 0.0);
	}

	@Test
	public void testDistanceToPoint() 
	{
		Plane a = new Plane( new Vector3( 2, 0, 0 ), -2 );
		
		a.normalize();
		assertEquals( a.distanceToPoint( a.projectPoint( zero3.clone() ) ), 0.0);
		assertEquals( a.distanceToPoint( new Vector3( 4, 0, 0 ) ), 3.0);
	}

	@Test
	public void testDistanceToSphere() 
	{
		Plane a = new Plane( new Vector3( 1, 0, 0 ), 0 );

		Sphere b = new Sphere( new Vector3( 2, 0, 0 ), 1 );
		
		assertEquals( a.distanceToSphere( b ), 1.0);

		a.set( new Vector3( 1, 0, 0 ), 2 );
		assertEquals( a.distanceToSphere( b ), 3.0);
		a.set( new Vector3( 1, 0, 0 ), -2 );
		assertEquals( a.distanceToSphere( b ), -1.0);
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

		assertTrue( a.isIntersectionLine( new Vector3( -10, 0, 0 ), new Vector3( 10, 0, 0 ) ));
		assertTrue( a.intersectLine( new Vector3( -10, 0, 0 ), new Vector3( 10, 0, 0 ) ).equals( new Vector3( 0, 0, 0 ) ));

		Plane b = new Plane( new Vector3( 1, 0, 0 ), -3 );

		assertTrue( b.isIntersectionLine( new Vector3( -10, 0, 0 ), new Vector3( 10, 0, 0 ) ));
		assertTrue( b.intersectLine( new Vector3( -10, 0, 0 ), new Vector3( 10, 0, 0 ) ).equals( new Vector3( 3, 0, 0 ) ));


		Plane c = new Plane( new Vector3( 1, 0, 0 ), -11 );

		assertTrue( ! c.isIntersectionLine( new Vector3( -10, 0, 0 ), new Vector3( 10, 0, 0 ) ));
		assertNull( c.intersectLine( new Vector3( -10, 0, 0 ), new Vector3( 10, 0, 0 ) ) );
		
		Plane d = new Plane( new Vector3( 1, 0, 0 ), 11 );

		assertTrue( ! d.isIntersectionLine( new Vector3( -10, 0, 0 ), new Vector3( 10, 0, 0 ) ));
		assertNull( d.intersectLine( new Vector3( -10, 0, 0 ), new Vector3( 10, 0, 0 ) ));
	}

	@Test
	public void testCoplanarPoint() 
	{
		Plane a = new Plane( new Vector3( 1, 0, 0 ), 0 );
		assertEquals( a.distanceToPoint( a.coplanarPoint() ), 0.0);

		Plane b = new Plane( new Vector3( 0, 1, 0 ), -1 );
		assertEquals( b.distanceToPoint( b.coplanarPoint() ), 0.0);
	}

	@Test
	public void testTransform() 
	{
		Plane a = new Plane( new Vector3( 1, 0, 0 ), 0 );

		Matrix4 m = new Matrix4();
		m.makeRotationZ( Math.PI * 0.5 );

		assertTrue( comparePlane( a.clone().transform( m ), new Plane( new Vector3( 0, 1, 0 ), 0 ) ));

		a = new Plane( new Vector3( 0, 1, 0 ), -1 );
		assertTrue( comparePlane( a.clone().transform( m ), new Plane( new Vector3( -1, 0, 0 ), -1 ) ));

		m.makeTranslation( 1, 1, 1 );
		assertTrue( comparePlane( a.clone().transform( m ), a.clone().translate( new Vector3( 1, 1, 1 ) ) ));
	}

	private boolean comparePlane( Plane a, Plane b ) 
	{
		double threshold = 0.0001;
		return ( a.getNormal().distanceTo( b.getNormal() ) < threshold &&
				Math.abs( a.getConstant() - b.getConstant() ) < threshold );
	};

}
