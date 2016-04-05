/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
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
import org.parallax3d.parallax.system.ThreejsTest;
import static org.junit.Assert.*;
import static org.parallax3d.parallax.math.Constants.one3;
import static org.parallax3d.parallax.math.Constants.two3;
import static org.parallax3d.parallax.math.Constants.zero3;

@ThreejsTest("Line3")
public class Line3Test {
	@Test
	public void testConstructor_equals() {
		Line3 a = new Line3();
		assertTrue( a.start.equals(zero3) );
		assertTrue( a.end.equals(zero3) );
		a = new Line3(two3.clone(), one3.clone());
		assertTrue( a.start.equals(two3) );
		assertTrue( a.end.equals(one3) );

	}

	@Test
	public void testCopy_equals() {
		Line3 a = new Line3(zero3.clone(), one3.clone());
		Line3 b = new Line3().copy(a);
		assertTrue( b.start.equals(zero3) );
		assertTrue( b.end.equals(one3) );
		a.start = zero3;
		a.end = one3;
		assertTrue( b.start.equals(zero3) );
		assertTrue( b.end.equals(one3) );

	}

	@Test
	public void testSet() {
		Line3 a = new Line3();
		a.set(one3, one3);
		assertTrue( a.start.equals(one3) );
		assertTrue( a.end.equals(one3) );

	}

	@Test
	public void testAt() {
		Line3 a = new Line3(one3.clone(), new Vector3(1, 1, 2));
		assertTrue( a.at(-1).distanceTo(new Vector3(1, 1, 0)) < 1.0E-4 );
		assertTrue( a.at(0).distanceTo(one3.clone()) < 1.0E-4 );
		assertTrue( a.at(1).distanceTo(new Vector3(1, 1, 2)) < 1.0E-4 );
		assertTrue( a.at(2).distanceTo(new Vector3(1, 1, 3)) < 1.0E-4 );

	}

	@Test
	public void testClosestPointToPoint_closestPointToPointParameter() {
		Line3 a = new Line3(one3.clone(), new Vector3(1, 1, 2));
		assertTrue( a.closestPointToPointParameter(zero3.clone(), true) == 0 );
		Vector3 b1 = a.closestPointToPoint(zero3.clone(), true);
		assertTrue( b1.distanceTo(new Vector3(1, 1, 1)) < 1.0E-4 );
		assertTrue( a.closestPointToPointParameter(zero3.clone(), false) == -1 );
		Vector3 b2 = a.closestPointToPoint(zero3.clone(), false);
		assertTrue( b2.distanceTo(new Vector3(1, 1, 0)) < 1.0E-4 );
		assertTrue( a.closestPointToPointParameter(new Vector3(1, 1, 5), true) == 1 );
		Vector3 b = a.closestPointToPoint(new Vector3(1, 1, 5), true);
		assertTrue( b.distanceTo(new Vector3(1, 1, 2)) < 1.0E-4 );
		assertTrue( a.closestPointToPointParameter(one3.clone(), true) == 0 );
		Vector3 c = a.closestPointToPoint(one3.clone(), true);
		assertTrue( c.distanceTo(one3.clone()) < 1.0E-4 );

	}

}
