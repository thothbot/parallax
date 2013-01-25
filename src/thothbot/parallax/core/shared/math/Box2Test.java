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
	
	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}

	public void testBox2() 
	{
		Box2 a = new Box2();

		assertTrue( a.getMin().equals( new Vector2( Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY ) ));
		assertTrue( a.getMax().equals( new Vector2( Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY ) ));
	}

	public void testBox2Vector2Vector2() 
	{
		Vector2 v1 = new Vector2(0,0), v2 = new Vector2(1,1);
		Box2 b = new Box2( v1.clone(), v2.clone() );
		
		assertTrue( b.getMin().equals( v1 ));
		assertTrue( b.getMax().equals( v2 ));
	}

	public void testSet() {
		fail("Not yet implemented");
	}

	public void testCopy() {
		fail("Not yet implemented");
	}

	public void testMakeEmpty() {
		fail("Not yet implemented");
	}

	public void testIsEmpty() {
		fail("Not yet implemented");
	}

	public void testCenter() {
		fail("Not yet implemented");
	}

	public void testSize() {
		fail("Not yet implemented");
	}

	public void testExpandByPoint() {
		fail("Not yet implemented");
	}

	public void testExpandByVector() {
		fail("Not yet implemented");
	}

	public void testExpandByScalar() {
		fail("Not yet implemented");
	}

	public void testIsContainsPoint() {
		fail("Not yet implemented");
	}

	public void testIsContainsBox() {
		fail("Not yet implemented");
	}

	public void testGetParameter() {
		fail("Not yet implemented");
	}

	public void testIsIntersectionBox() {
		fail("Not yet implemented");
	}

	public void testClampPointVector2() {
		fail("Not yet implemented");
	}

	public void testDistanceToPoint() {
		fail("Not yet implemented");
	}

	public void testIntersect() {
		fail("Not yet implemented");
	}

	public void testUnion() {
		fail("Not yet implemented");
	}

	public void testTranslate() {
		fail("Not yet implemented");
	}
}
