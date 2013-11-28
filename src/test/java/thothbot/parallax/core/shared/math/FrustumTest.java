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

public class FrustumTest extends GWTTestCase 
{

	private static Vector3 unit3 = new Vector3( 1, 0, 0 );
	
	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}
	
	public void testFrustum()
	{
		Frustum a = new Frustum();

		assertEquals( a.getPlanes().size(), 6);

		Plane pDefault = new Plane();
		for( int i = 0; i < 6; i ++ ) {
			assertTrue( a.getPlanes().get(i).equals( pDefault ));
		}

		Plane p0 = new Plane( FrustumTest.unit3, -1 );
		Plane p1 = new Plane( FrustumTest.unit3, 1 );
		Plane p2 = new Plane( FrustumTest.unit3, 2 );
		Plane p3 = new Plane( FrustumTest.unit3, 3 );
		Plane p4 = new Plane( FrustumTest.unit3, 4 );
		Plane p5 = new Plane( FrustumTest.unit3, 5 );

		Frustum b = new Frustum( p0, p1, p2, p3, p4, p5 );
		assertTrue( b.getPlanes().get(0).equals( p0 ));
		assertTrue( b.getPlanes().get(1).equals( p1 ));
		assertTrue( b.getPlanes().get(2).equals( p2 ));
		assertTrue( b.getPlanes().get(3).equals( p3 ));
		assertTrue( b.getPlanes().get(4).equals( p4 ));
		assertTrue( b.getPlanes().get(5).equals( p5 ));
	}

	public void testCopy()
	{
		Plane p0 = new Plane( FrustumTest.unit3, -1 );
		Plane p1 = new Plane( FrustumTest.unit3, 1 );
		Plane p2 = new Plane( FrustumTest.unit3, 2 );
		Plane p3 = new Plane( FrustumTest.unit3, 3 );
		Plane p4 = new Plane( FrustumTest.unit3, 4 );
		Plane p5 = new Plane( FrustumTest.unit3, 5 );

		Frustum b = new Frustum( p0, p1, p2, p3, p4, p5 );
		Frustum a = new Frustum().copy( b );
		assertTrue( a.getPlanes().get(0).equals( p0 ));
		assertTrue( a.getPlanes().get(1).equals( p1 ));
		assertTrue( a.getPlanes().get(2).equals( p2 ));
		assertTrue( a.getPlanes().get(3).equals( p3 ));
		assertTrue( a.getPlanes().get(4).equals( p4 ));
		assertTrue( a.getPlanes().get(5).equals( p5 ));

		// ensure it is a true copy by modifying source
		b.getPlanes().set(0, p1);
		assertTrue( a.getPlanes().get(0).equals( p0 ));
	}

	public void testSetFromMatrixMakeOrthographicisContainsPoint()
	{
		Matrix4 m = new Matrix4().makeOrthographic( -1, 1, -1, 1, 1, 100 );
		Frustum a = new Frustum().setFromMatrix( m );

		assertTrue( ! a.isContainsPoint( new Vector3( 0, 0, 0 ) ));
		assertTrue( a.isContainsPoint( new Vector3( 0, 0, -50 ) ));
		assertTrue( a.isContainsPoint( new Vector3( 0, 0, -1.001 ) ));
		assertTrue( a.isContainsPoint( new Vector3( -1, -1, -1.001 ) ));
		assertTrue( ! a.isContainsPoint( new Vector3( -1.1, -1.1, -1.001 ) ));
		assertTrue( a.isContainsPoint( new Vector3( 1, 1, -1.001 ) ));
		assertTrue( ! a.isContainsPoint( new Vector3( 1.1, 1.1, -1.001 ) ));
		assertTrue( a.isContainsPoint( new Vector3( 0, 0, -100 ) ));
		assertTrue( a.isContainsPoint( new Vector3( -1, -1, -100 ) ));
		assertTrue( ! a.isContainsPoint( new Vector3( -1.1, -1.1, -100.1 ) ));
		assertTrue( a.isContainsPoint( new Vector3( 1, 1, -100 ) ));
		assertTrue( ! a.isContainsPoint( new Vector3( 1.1, 1.1, -100.1 ) ));
		assertTrue( ! a.isContainsPoint( new Vector3( 0, 0, -101 ) ));

	}
	
	public void testSetFromMatrixMakeFrustumisContainsPoint()
	{
		Matrix4 m = new Matrix4().makeFrustum( -1, 1, -1, 1, 1, 100 );
		Frustum a = new Frustum().setFromMatrix( m );

		assertTrue( ! a.isContainsPoint( new Vector3( 0, 0, 0 ) ));
		assertTrue( a.isContainsPoint( new Vector3( 0, 0, -50 ) ));
		assertTrue( a.isContainsPoint( new Vector3( 0, 0, -1.001 ) ));
		assertTrue( a.isContainsPoint( new Vector3( -1, -1, -1.001 ) ));
		assertTrue( ! a.isContainsPoint( new Vector3( -1.1, -1.1, -1.001 ) ));
		assertTrue( a.isContainsPoint( new Vector3( 1, 1, -1.001 ) ));
		assertTrue( ! a.isContainsPoint( new Vector3( 1.1, 1.1, -1.001 ) ));
		assertTrue( a.isContainsPoint( new Vector3( 0, 0, -99.999 ) ));
		assertTrue( a.isContainsPoint( new Vector3( -99.999, -99.999, -99.999 ) ));
		assertTrue( ! a.isContainsPoint( new Vector3( -100.1, -100.1, -100.1 ) ));
		assertTrue( a.isContainsPoint( new Vector3( 99.999, 99.999, -99.999 ) ));
		assertTrue( ! a.isContainsPoint( new Vector3( 100.1, 100.1, -100.1 ) ));
		assertTrue( ! a.isContainsPoint( new Vector3( 0, 0, -101 ) ));
	}
	
	public void testSetFromMatrixMakeFrustumisIntersectsSphere()
	{
		Matrix4 m = new Matrix4().makeFrustum( -1, 1, -1, 1, 1, 100 );
		Frustum a = new Frustum().setFromMatrix( m );

		assertTrue( ! a.isIntersectsSphere( new Sphere( new Vector3( 0, 0, 0 ), 0 ) ));
		assertTrue( ! a.isIntersectsSphere( new Sphere( new Vector3( 0, 0, 0 ), 0.9 ) ));
		assertTrue( a.isIntersectsSphere( new Sphere( new Vector3( 0, 0, 0 ), 1.1 ) ));
		assertTrue( a.isIntersectsSphere( new Sphere( new Vector3( 0, 0, -50 ), 0 ) ));
		assertTrue( a.isIntersectsSphere( new Sphere( new Vector3( 0, 0, -1.001 ), 0 ) ));
		assertTrue( a.isIntersectsSphere( new Sphere( new Vector3( -1, -1, -1.001 ), 0 ) ));
		assertTrue( ! a.isIntersectsSphere( new Sphere( new Vector3( -1.1, -1.1, -1.001 ), 0 ) ));
		assertTrue( a.isIntersectsSphere( new Sphere( new Vector3( -1.1, -1.1, -1.001 ), 0.5 ) ));
		assertTrue( a.isIntersectsSphere( new Sphere( new Vector3( 1, 1, -1.001 ), 0 ) ));
		assertTrue( ! a.isIntersectsSphere( new Sphere( new Vector3( 1.1, 1.1, -1.001 ), 0 ) ));
		assertTrue( a.isIntersectsSphere( new Sphere( new Vector3( 1.1, 1.1, -1.001 ), 0.5 ) ));
		assertTrue( a.isIntersectsSphere( new Sphere( new Vector3( 0, 0, -99.999 ), 0 ) ));
		assertTrue( a.isIntersectsSphere( new Sphere( new Vector3( -99.999, -99.999, -99.999 ), 0 ) ));
		assertTrue( ! a.isIntersectsSphere( new Sphere( new Vector3( -100.1, -100.1, -100.1 ), 0 ) ));
		assertTrue( a.isIntersectsSphere( new Sphere( new Vector3( -100.1, -100.1, -100.1 ), 0.5 ) ));
		assertTrue( a.isIntersectsSphere( new Sphere( new Vector3( 99.999, 99.999, -99.999 ), 0 ) ));
		assertTrue( ! a.isIntersectsSphere( new Sphere( new Vector3( 100.1, 100.1, -100.1 ), 0 ) ));
		assertTrue( a.isIntersectsSphere( new Sphere( new Vector3( 100.1, 100.1, -100.1 ), 0.2 ) ));
		assertTrue( ! a.isIntersectsSphere( new Sphere( new Vector3( 0, 0, -101 ), 0 ) ));
		assertTrue( a.isIntersectsSphere( new Sphere( new Vector3( 0, 0, -101 ), 1.1 ) ));
	}

	public void testClone()
	{
		Plane p0 = new Plane( FrustumTest.unit3, -1 );
		Plane p1 = new Plane( FrustumTest.unit3, 1 );
		Plane p2 = new Plane( FrustumTest.unit3, 2 );
		Plane p3 = new Plane( FrustumTest.unit3, 3 );
		Plane p4 = new Plane( FrustumTest.unit3, 4 );
		Plane p5 = new Plane( FrustumTest.unit3, 5 );

		Frustum b = new Frustum( p0, p1, p2, p3, p4, p5 );
		Frustum a = b.clone();
		assertTrue( a.getPlanes().get(0).equals( p0 ));
		assertTrue( a.getPlanes().get(1).equals( p1 ));
		assertTrue( a.getPlanes().get(2).equals( p2 ));
		assertTrue( a.getPlanes().get(3).equals( p3 ));
		assertTrue( a.getPlanes().get(4).equals( p4 ));
		assertTrue( a.getPlanes().get(5).equals( p5 ));

		// ensure it is a true copy by modifying source
		a.getPlanes().get(0).copy( p1 );
		assertTrue( b.getPlanes().get(0).equals( p0 ));
	}
}
