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

import java.util.Arrays;
import java.util.List;

import com.google.gwt.junit.client.GWTTestCase;

public class TriangleTest extends GWTTestCase 
{

	private static Vector3 zero3 = new Vector3();
	private static Vector3 one3 = new Vector3( 1, 1, 1 );
	private static Vector3 two3 = new Vector3( 2, 2, 2 );
	
	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}
	
	public void testTriangle()
	{
		Triangle a = new Triangle();
		assertTrue( a.getA().equals( zero3 ));
		assertTrue( a.getB().equals( zero3 ));
		assertTrue( a.getC().equals( zero3 ));

		a = new Triangle( one3.clone().negate(), one3, two3 );
		assertTrue( a.getA().equals( one3.clone().negate() ));
		assertTrue( a.getB().equals( one3 ));
		assertTrue( a.getC().equals( two3 ));
	}

	public void testSet()
	{
		Triangle a = new Triangle();

		a.set( one3.clone().negate(), one3, two3 );
		assertTrue( a.getA().equals( one3.clone().negate() ));
		assertTrue( a.getB().equals( one3 ));
		assertTrue( a.getC().equals( two3 ));
	}

	public void testSetFromPointsAndIndices()
	{
		Triangle a = new Triangle();

		List<Vector3> points = Arrays.asList( one3, one3.clone().negate(), two3 );
		a.setFromPointsAndIndices( points, 1, 0, 2 );
		assertTrue( a.getA().equals( one3.clone().negate() ));
		assertTrue( a.getB().equals( one3 ));
		assertTrue( a.getC().equals( two3 ));
	}

	public void testCopy()
	{
		Triangle a = new Triangle( one3.clone().negate(), one3, two3 );
		Triangle b = new Triangle().copy( a );
		assertTrue( b.getA().equals( one3.clone().negate() ));
		assertTrue( b.getB().equals( one3 ));
		assertTrue( b.getC().equals( two3 ));

		// ensure that it is a true copy
		a.setA( one3 );
		a.setB( zero3 );
		a.setC( zero3 );
		assertTrue( b.getA().equals( one3.clone().negate() ));
		assertTrue( b.getB().equals( one3 ));
		assertTrue( b.getC().equals( two3 ));
	}

	public void testArea()
	{
		Triangle a = new Triangle();

		assertEquals( 0.0, a.area() );

		a = new Triangle( new Vector3( 0, 0, 0 ), new Vector3( 1, 0, 0 ), new Vector3( 0, 1, 0 ) );
		assertEquals( 0.5, a.area() );

		a = new Triangle( new Vector3( 2, 0, 0 ), new Vector3( 0, 0, 0 ), new Vector3( 0, 0, 2 ) );
		assertEquals( 2.0, a.area() );

		// colinear triangle.
		a = new Triangle( new Vector3( 2, 0, 0 ), new Vector3( 0, 0, 0 ), new Vector3( 3, 0, 0 ) );
		assertEquals( 0.0, a.area() );
	}

	public void testMidpoint()
	{
		Triangle a = new Triangle();

		assertTrue( a.midpoint().equals( new Vector3( 0, 0, 0 ) ));

		a = new Triangle( new Vector3( 0, 0, 0 ), new Vector3( 1, 0, 0 ), new Vector3( 0, 1, 0 ) );
		assertTrue( a.midpoint().equals( new Vector3( 1.0/3, 1.0/3, 0 ) ));

		a = new Triangle( new Vector3( 2, 0, 0 ), new Vector3( 0, 0, 0 ), new Vector3( 0, 0, 2 ) );
		assertTrue( a.midpoint().equals( new Vector3( 2.0/3, 0, 2.0/3 ) ));
	}

	public void testNormal()
	{
		Triangle a = new Triangle();

		assertTrue( a.normal().equals( new Vector3( 0, 0, 0 ) ));

		a = new Triangle( new Vector3( 0, 0, 0 ), new Vector3( 1, 0, 0 ), new Vector3( 0, 1, 0 ) );
		assertTrue( a.normal().equals( new Vector3( 0, 0, 1 ) ));

		a = new Triangle( new Vector3( 2, 0, 0 ), new Vector3( 0, 0, 0 ), new Vector3( 0, 0, 2 ) );
		assertTrue( a.normal().equals( new Vector3( 0, 1, 0 ) ));
	}

	public void testPlane()
	{
		Triangle a = new Triangle();

		// artificial normal is created in this case.
		assertEquals( 0.0, a.plane().distanceToPoint( a.getA() ) );
		assertEquals( 0.0, a.plane().distanceToPoint( a.getB() ) );
		assertEquals( 0.0, a.plane().distanceToPoint( a.getC() ) );
		assertTrue( a.plane().getNormal().equals( a.normal() ));

		a = new Triangle( new Vector3( 0, 0, 0 ), new Vector3( 1, 0, 0 ), new Vector3( 0, 1, 0 ) );
		assertEquals( 0.0, a.plane().distanceToPoint( a.getA() ) );
		assertEquals( 0.0, a.plane().distanceToPoint( a.getB() ) );
		assertEquals( 0.0, a.plane().distanceToPoint( a.getC() ) );
		assertTrue( a.plane().getNormal().equals( a.normal() ));

		a = new Triangle( new Vector3( 2, 0, 0 ), new Vector3( 0, 0, 0 ), new Vector3( 0, 0, 2 ) );
		assertEquals( 0.0, a.plane().distanceToPoint( a.getA() ) );
		assertEquals( 0.0, a.plane().distanceToPoint( a.getB() ) );
		assertEquals( 0.0, a.plane().distanceToPoint( a.getC() ) );
		assertTrue( a.plane().getNormal().clone().normalize().equals( a.normal() ));
	}

	public void testBarycoordFromPointVector3Vector3()
	{
		Triangle a = new Triangle();

		Vector3 bad = new Vector3( -2, -1, -1 );

		assertTrue( a.barycoordFromPoint( a.getA() ).equals( bad ));
		assertTrue( a.barycoordFromPoint( a.getB() ).equals( bad ));
		assertTrue( a.barycoordFromPoint( a.getC() ).equals( bad ));

		a = new Triangle( new Vector3( 0, 0, 0 ), new Vector3( 1, 0, 0 ), new Vector3( 0, 1, 0 ) );
		assertTrue( a.barycoordFromPoint( a.getA() ).equals( new Vector3( 1, 0, 0 ) ));
		assertTrue( a.barycoordFromPoint( a.getB() ).equals( new Vector3( 0, 1, 0 ) ));
		assertTrue( a.barycoordFromPoint( a.getC() ).equals( new Vector3( 0, 0, 1 ) ));
		assertTrue( a.barycoordFromPoint( a.midpoint() ).distanceTo( new Vector3( 1.0/3, 1.0/3, 1.0/3 ) ) < 0.0001);

		a = new Triangle( new Vector3( 2, 0, 0 ), new Vector3( 0, 0, 0 ), new Vector3( 0, 0, 2 ) );
		assertTrue( a.barycoordFromPoint( a.getA() ).equals( new Vector3( 1, 0, 0 ) ));
		assertTrue( a.barycoordFromPoint( a.getB() ).equals( new Vector3( 0, 1, 0 ) ));
		assertTrue( a.barycoordFromPoint( a.getC() ).equals( new Vector3( 0, 0, 1 ) ));
		assertTrue( a.barycoordFromPoint( a.midpoint() ).distanceTo( new Vector3( 1.0/3, 1.0/3, 1.0/3 ) ) < 0.0001);
	}

	public void testContainsPoint()
	{
		Triangle a = new Triangle();

		assertTrue( ! a.containsPoint( a.getA() ));
		assertTrue( ! a.containsPoint( a.getB() ));
		assertTrue( ! a.containsPoint( a.getC() ));

		a = new Triangle( new Vector3( 0, 0, 0 ), new Vector3( 1, 0, 0 ), new Vector3( 0, 1, 0 ) );
		assertTrue( a.containsPoint( a.getA() ));
		assertTrue( a.containsPoint( a.getB() ));
		assertTrue( a.containsPoint( a.getC() ));
		assertTrue( a.containsPoint( a.midpoint() ));
		assertTrue( ! a.containsPoint( new Vector3( -1, -1, -1 ) ));

		a = new Triangle( new Vector3( 2, 0, 0 ), new Vector3( 0, 0, 0 ), new Vector3( 0, 0, 2 ) );
		assertTrue( a.containsPoint( a.getA() ));
		assertTrue( a.containsPoint( a.getB() ));
		assertTrue( a.containsPoint( a.getC() ));
		assertTrue( a.containsPoint( a.midpoint() ));
		assertTrue( ! a.containsPoint( new Vector3( -1, -1, -1 ) ));
	}
}
