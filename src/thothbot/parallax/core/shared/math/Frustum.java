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

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.shared.core.AbstractGeometry;
import thothbot.parallax.core.shared.core.GeometryObject;

/**
 * This class implements three-dimensional region which is visible on the screen.
 * It is the field of view of the notional camera which is implemented as frustum 
 * of a rectangular pyramid with 6 planes. 
 * 
 * @author thothbot
 */
public class Frustum
{
	/**
	 * Panes of the Frustum of a rectangular pyramid
	 */
	private List<Plane> planes;
	
	// Temporary variables
	static Sphere _sphere = new Sphere();
	static Vector3 _p1 = new Vector3();
	static Vector3 _p2 = new Vector3();

	/**
	 * Default constructor will make Frustum of a rectangular pyramid 
	 * with 6 planes. 
	 */
	public Frustum() 
	{
		this.planes = new ArrayList<Plane>();
		for(int i = 0; i < 6; i++)
			this.planes.add(new Plane());
	}
	
	public Frustum(Plane p0, Plane p1, Plane p2, Plane p3, Plane p4, Plane p5) 
	{
		this.planes = new ArrayList<Plane>();
		this.planes.add(p0);
		this.planes.add(p1);
		this.planes.add(p2);
		this.planes.add(p3);
		this.planes.add(p4);
		this.planes.add(p5);
	}

	public Frustum set( Plane p0, Plane p1, Plane p2, Plane p3, Plane p4, Plane p5 ) 
	{
		this.planes.get(0).copy( p0 );
		this.planes.get(1).copy( p1 );
		this.planes.get(2).copy( p2 );
		this.planes.get(3).copy( p3 );
		this.planes.get(4).copy( p4 );
		this.planes.get(5).copy( p5 );

		return this;
	}
	
	public List<Plane> getPlanes() {
		return planes;
	}

	public Frustum copy( Frustum frustum ) 
	{
		for(int i = 0; i < 6; i++)
			this.planes.get(i).copy(frustum.planes.get(i));

		return this;
	}

	public Frustum setFromMatrix( Matrix4 m ) 
	{
		Float32Array me = m.getArray();
		double me0 = me.get(0), me1 = me.get(1), me2 = me.get(2), me3 = me.get(3);
		double me4 = me.get(4), me5 = me.get(5), me6 = me.get(6), me7 = me.get(7);
		double me8 = me.get(8), me9 = me.get(9), me10 = me.get(10), me11 = me.get(11);
		double me12 = me.get(12), me13 = me.get(13), me14 = me.get(14), me15 = me.get(15);

		this.planes.get(0).setComponents( me3 - me0, me7 - me4, me11 - me8, me15 - me12 ).normalize();
		this.planes.get(1).setComponents( me3 + me0, me7 + me4, me11 + me8, me15 + me12 ).normalize();
		this.planes.get(2).setComponents( me3 + me1, me7 + me5, me11 + me9, me15 + me13 ).normalize();
		this.planes.get(3).setComponents( me3 - me1, me7 - me5, me11 - me9, me15 - me13 ).normalize();
		this.planes.get(4).setComponents( me3 - me2, me7 - me6, me11 - me10, me15 - me14 ).normalize();
		this.planes.get(5).setComponents( me3 + me2, me7 + me6, me11 + me10, me15 + me14 ).normalize();

		return this;
	}

	public boolean isIntersectsObject( GeometryObject object ) 
	{		
		AbstractGeometry geometry = object.getGeometry();

		if ( geometry.getBoundingSphere() == null ) 
			geometry.computeBoundingSphere();

		_sphere.copy( geometry.getBoundingSphere() );
		_sphere.apply( object.getMatrixWorld() );

		return this.isIntersectsSphere( _sphere );		
	}

	public boolean isIntersectsSphere( Sphere sphere ) 
	{
		Vector3 center = sphere.getCenter();
		double negRadius = -sphere.getRadius();

		for ( int i = 0; i < 6; i ++ ) 
		{
			double distance = planes.get( i ).distanceToPoint( center );

			if( distance < negRadius ) 
			{
				return false;
			}
		}

		return true;
	}
	
	public boolean isIntersectsBox(Box3 box) 
	{

		for ( int i = 0; i < 6 ; i ++ ) {

			Plane plane = planes.get( i );

			_p1.x = plane.getNormal().x > 0 ? box.getMin().x : box.getMax().x;
			_p2.x = plane.getNormal().x > 0 ? box.getMax().x : box.getMin().x;
			_p1.y = plane.getNormal().y > 0 ? box.getMin().y : box.getMax().y;
			_p2.y = plane.getNormal().y > 0 ? box.getMax().y : box.getMin().y;
			_p1.z = plane.getNormal().z > 0 ? box.getMin().z : box.getMax().z;
			_p2.z = plane.getNormal().z > 0 ? box.getMax().z : box.getMin().z;

			double d1 = plane.distanceToPoint( _p1 );
			double d2 = plane.distanceToPoint( _p2 );

			// if both outside plane, no intersection

			if ( d1 < 0 && d2 < 0 ) {

				return false;

			}
		}

		return true;
	}


	public boolean isContainsPoint( Vector3 point ) 
	{
		for ( int i = 0; i < 6; i ++ ) 
		{
			if( planes.get( i ).distanceToPoint( point ) < 0 ) 
			{
				return false;
			}
		}

		return true;
	}

	public Frustum clone() 
	{
		return new Frustum().copy( this );
	}
}
