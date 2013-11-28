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

public class Line3Test extends GWTTestCase
{

	private static Vector3 zero3 = new Vector3();
	private static Vector3 one3 = new Vector3( 1, 1, 1 );
	private static Vector3 two3 = new Vector3( 2, 2, 2 );

	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}
	
	public void testLine3()
	{
		Line3 a = new Line3();
		assertTrue( a.getStart().equals( zero3 ));
		assertTrue( a.getEnd().equals( zero3 ));

		Line3 b = new Line3( two3.clone(), one3.clone() );
		assertTrue( b.getStart().equals( two3 ));
		assertTrue( b.getEnd().equals( one3 ));
	}

	public void testSet()
	{
		Line3 a = new Line3();

		a.set( one3, one3 );
		assertTrue( a.getStart().equals( one3 ));
		assertTrue( a.getEnd().equals( one3 ));
	}

	public void testCopy()
	{
		Line3 a = new Line3( zero3.clone(), one3.clone() );
		Line3 b = new Line3().copy( a );
		assertTrue( b.getStart().equals( zero3 ));
		assertTrue( b.getEnd().equals( one3 ));

		// ensure that it is a true copy
		a.setStart(zero3);
		a.setEnd(one3);
		assertTrue( b.getStart().equals( zero3 ));
		assertTrue( b.getEnd().equals( one3 ));
	}

	public void testAtDouble()
	{
		Line3 a = new Line3( one3.clone(), new Vector3( 1, 1, 2 ) );

		assertTrue( a.at( -1 ).distanceTo( new Vector3( 1, 1, 0 ) ) < 0.0001);
		assertTrue( a.at( 0 ).distanceTo( one3.clone() ) < 0.0001);
		assertTrue( a.at( 1 ).distanceTo( new Vector3( 1, 1, 2 ) ) < 0.0001);
		assertTrue( a.at( 2 ).distanceTo( new Vector3( 1, 1, 3 ) ) < 0.0001);
	}

	public void testClosestPointToPointParameterVector3()
	{
		Line3 a = new Line3( one3.clone(), new Vector3( 1, 1, 2 ) );

		// nearby the ray
		assertEquals( 0.0, a.closestPointToPointParameter( zero3.clone(), true ));
		Vector3 b1 = a.closestPointToPoint( zero3.clone(), true );
		assertTrue( b1.distanceTo( new Vector3( 1, 1, 1 ) ) < 0.0001);

		// nearby the ray
		assertEquals( -1.0, a.closestPointToPointParameter( zero3.clone(), false ));
		Vector3 b2 = a.closestPointToPoint( zero3.clone(), false );
		assertTrue( b2.distanceTo( new Vector3( 1, 1, 0 ) ) < 0.0001);

		// nearby the ray
		assertEquals( 1.0, a.closestPointToPointParameter( new Vector3( 1, 1, 5 ), true ));
		Vector3 b = a.closestPointToPoint( new Vector3( 1, 1, 5 ), true );
		assertTrue( b.distanceTo( new Vector3( 1, 1, 2 ) ) < 0.0001);

		// exactly on the ray
		assertEquals( 0.0, a.closestPointToPointParameter( one3.clone(), true ) );
		Vector3 c = a.closestPointToPoint( one3.clone(), true );
		assertTrue( c.distanceTo( one3.clone() ) < 0.0001);
	}
}
