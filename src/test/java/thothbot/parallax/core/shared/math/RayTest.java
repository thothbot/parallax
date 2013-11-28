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

public class RayTest extends GWTTestCase  
{

	private static Vector3 zero3 = new Vector3();
	private static Vector3 one3 = new Vector3( 1, 1, 1 );
	private static Vector3 two3 = new Vector3( 2, 2, 2 );

	
	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}
	
	public void testRay()
	{
		Ray a = new Ray();
		assertTrue( a.getOrigin().equals( zero3 ));
		assertTrue( a.getDirection().equals( zero3 ));

		a = new Ray( two3.clone(), one3.clone() );
		assertTrue( a.getOrigin().equals( two3 ));
		assertTrue( a.getDirection().equals( one3 ));
	}

	public void testSet()
	{
		Ray a = new Ray();

		a.set( one3, one3 );
		assertTrue( a.getOrigin().equals( one3 ));
		assertTrue( a.getDirection().equals( one3 ));
	}

	public void testCopy()
	{
		Ray a = new Ray( zero3.clone(), one3.clone() );
		Ray b = new Ray().copy( a );
		assertTrue( b.getOrigin().equals( zero3 ));
		assertTrue( b.getDirection().equals( one3 ));

		// ensure that it is a true copy
		a.setOrigin( zero3 );
		a.setDirection( one3 );
		assertTrue( b.getOrigin().equals( zero3 ));
		assertTrue( b.getDirection().equals( one3 ));
	}

	public void testAt()
	{
		Ray a = new Ray( one3.clone(), new Vector3( 0, 0, 1 ) );

		assertTrue( a.at( 0 ).equals( one3 ));
		assertTrue( a.at( -1 ).equals( new Vector3( 1, 1, 0 ) ));
		assertTrue( a.at( 1 ).equals( new Vector3( 1, 1, 2 ) ));
	}

	public void testRecast()
	{
		Ray a = new Ray( one3.clone(), new Vector3( 0, 0, 1 ) );

		assertTrue( a.recast( 0 ).equals( a ));

		Ray b = a.clone();
		assertTrue( b.recast( -1 ).equals( new Ray( new Vector3( 1, 1, 0 ), new Vector3( 0, 0, 1 ) ) ));

		Ray c = a.clone();
		assertTrue( c.recast( 1 ).equals( new Ray( new Vector3( 1, 1, 2 ), new Vector3( 0, 0, 1 ) ) ));

		Ray d = a.clone();
		Ray e = d.clone().recast( 1 );
		assertTrue( d.equals( a ));
		assertTrue( ! e.equals( d ));
		assertTrue( e.equals( c ));
	}

	public void testClosestPointToPoint()
	{
		Ray a = new Ray( one3.clone(), new Vector3( 0, 0, 1 ) );

		// nearby the ray
		Vector3 b = a.closestPointToPoint( zero3 );
		assertTrue( b.equals( new Vector3( 1, 1, 0 ) ));

		// exactly on the ray
		Vector3 c = a.closestPointToPoint( one3 );
		assertTrue( c.equals( one3 ));
	}

	public void testDistanceToPoint()
	{
		Ray a = new Ray( one3.clone(), new Vector3( 0, 0, 1 ) );

		// nearby the ray
		double b = a.distanceToPoint( zero3 );
		assertEquals( Math.sqrt( 2 ), b );

		// exactly on the ray
		double c = a.distanceToPoint( one3 );
		assertEquals( 0.0, c );
	}

	public void testIsIntersectionSphere()
	{
		Ray a = new Ray( one3.clone(), new Vector3( 0, 0, 1 ) );
		Sphere b = new Sphere( zero3, 0.5 );
		Sphere c = new Sphere( zero3, 1.5 );
		Sphere d = new Sphere( one3, 0.1 );
		Sphere e = new Sphere( two3, 0.1 );
		Sphere f = new Sphere( two3, 1 );

		assertTrue( ! a.isIntersectionSphere( b ));
		assertTrue( a.isIntersectionSphere( c ));
		assertTrue( a.isIntersectionSphere( d ));
		assertTrue( ! a.isIntersectionSphere( e ));
		assertTrue( ! a.isIntersectionSphere( f ));
	}

	public void testIsIntersectionPlane()
	{
		Ray a = new Ray( one3.clone(), new Vector3( 0, 0, 1 ) );

		// parallel plane behind
		Plane b = new Plane().setFromNormalAndCoplanarPoint( new Vector3( 0, 0, 1 ), one3.clone().sub( new Vector3( 0, 0, -1 ) ) );
		assertTrue( a.isIntersectionPlane( b ));

		// parallel plane coincident with origin
		Plane c = new Plane().setFromNormalAndCoplanarPoint( new Vector3( 0, 0, 1 ), one3.clone().sub( new Vector3( 0, 0, 0 ) ) );
		assertTrue( a.isIntersectionPlane( c ));

		// parallel plane infront
		Plane d = new Plane().setFromNormalAndCoplanarPoint( new Vector3( 0, 0, 1 ), one3.clone().sub( new Vector3( 0, 0, 1 ) ) );
		assertTrue( a.isIntersectionPlane( d ));

		// perpendical ray that overlaps exactly
		Plane e = new Plane().setFromNormalAndCoplanarPoint( new Vector3( 1, 0, 0 ), one3 );
		assertTrue( a.isIntersectionPlane( e ));

		// perpendical ray that doesn't overlap
		Plane f = new Plane().setFromNormalAndCoplanarPoint( new Vector3( 1, 0, 0 ), zero3 );
		assertTrue( ! a.isIntersectionPlane( f ));
	}

	public void testIntersectPlane()
	{
		Ray a = new Ray( one3.clone(), new Vector3( 0, 0, 1 ) );

		// parallel plane behind
		Plane b = new Plane().setFromNormalAndCoplanarPoint( new Vector3( 0, 0, 1 ), new Vector3( 1, 1, -1 ) );
		assertTrue( a.intersectPlane( b ).equals( new Vector3( 1, 1, -1 ) ));

		// parallel plane coincident with origin
		Plane c = new Plane().setFromNormalAndCoplanarPoint( new Vector3( 0, 0, 1 ), new Vector3( 1, 1, 0 ) );
		assertTrue( a.intersectPlane( c ).equals( new Vector3( 1, 1, 0 ) ));

		// parallel plane infront
		Plane d = new Plane().setFromNormalAndCoplanarPoint( new Vector3( 0, 0, 1 ), new Vector3( 1, 1, 1 ) );
		assertTrue( a.intersectPlane( d ).equals( new Vector3( 1, 1, 1 ) ));

		// perpendical ray that overlaps exactly
		Plane e = new Plane().setFromNormalAndCoplanarPoint( new Vector3( 1, 0, 0 ), one3 );
		assertTrue( a.intersectPlane( e ).equals( a.getOrigin() ));

		// perpendical ray that doesn't overlap
		Plane f = new Plane().setFromNormalAndCoplanarPoint( new Vector3( 1, 0, 0 ), zero3 );
		assertNull( a.intersectPlane( f ) );
	}

	public void testTransform()
	{
		Ray a = new Ray( one3.clone(), new Vector3( 0, 0, 1 ) );
		Matrix4 m = new Matrix4().identity();

		assertTrue( a.clone().apply( m ).equals( a ));

		a = new Ray( zero3.clone(), new Vector3( 0, 0, 1 ) );
		m.rotateByAxis( new Vector3( 0, 0, 1 ), Math.PI );
		assertTrue( a.clone().apply( m ).equals( a ));

		m.identity().rotateX( Math.PI );
		Ray b = a.clone();
		b.getDirection().negate();
		Ray a2 = a.clone().apply( m );
		assertTrue( a2.getOrigin().distanceTo( b.getOrigin() ) < 0.0001);
		assertTrue( a2.getDirection().distanceTo( b.getDirection() ) < 0.0001);

		a.setOrigin( new Vector3( 0, 0, 1 ) );
		b.setOrigin( new Vector3( 0, 0, -1 ) );
		Ray a3 = a.clone().apply( m );
		assertTrue( a3.getOrigin().distanceTo( b.getOrigin() ) < 0.0001);
		assertTrue( a3.getDirection().distanceTo( b.getDirection() ) < 0.0001);
	}
}
