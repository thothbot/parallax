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
import java.util.Collections;
import java.util.List;

import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Face4;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.MeshFaceMaterial;
import thothbot.parallax.core.shared.objects.DimensionalObject;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.objects.Particle;

/**
 * Representation of a ray in space.
 * <p>
 * Pretty useful for detecting collisions between objects in a scene.
 * <p>
 * Based on three.js code.
 * 
 * @author thothbot
 *
 */
public class Ray 
{
	// Where does the ray start. Default is 0, 0, 0.
	private Vector3 origin;
	
	// A vector pointing in the direction the ray goes. Default is 0, 0, 0.
	private Vector3 direction;
	
	private static Vector3 __v1 = new Vector3();
	private static Vector3 __v2 = new Vector3();

	public Ray()
	{
		this(new Vector3(), new Vector3());
	}

	public Ray( Vector3 origin, Vector3 direction ) 
	{
		this.origin = origin;
		this.direction = direction;
	}
	
	public Ray set( Vector3 origin, Vector3 direction ) 
	{
		this.origin.copy( origin );
		this.direction.copy( direction );

		return this;
	}

	public Ray copy( Ray ray ) 
	{
		this.origin.copy( ray.origin );
		this.direction.copy( ray.direction );

		return this;
	}

	public Vector3 at( double t)
	{
		return at(t, new Vector3());
	}
	
	public Vector3 at( double t, Vector3 optionalTarget ) 
	{
		return optionalTarget.copy( this.direction ).multiply( t ).add( this.origin );
	}

	public Ray recast( double t ) 
	{
		this.origin.copy( this.at( t, Ray.__v1 ) );

		return this;
	}

	public Vector3 closestPointToPoint( Vector3 point)
	{
		return closestPointToPoint(point, new Vector3());
	}
	
	public Vector3 closestPointToPoint( Vector3 point, Vector3 optionalTarget ) 
	{
		optionalTarget.sub( point, this.origin );
		double directionDistance = optionalTarget.dot( this.direction );

		return optionalTarget.copy( this.direction ).multiply( directionDistance ).add( this.origin );
	}

	double distanceToPoint( Vector3 point ) 
	{
		double directionDistance = Ray.__v1.sub( point, this.origin ).dot( this.direction );
		Ray.__v1.copy( this.direction ).multiply( directionDistance ).add( this.origin );

		return Ray.__v1.distanceTo( point );
	}

	public boolean isIntersectionSphere( Sphere sphere ) 
	{
		return ( this.distanceToPoint( sphere.getCenter() ) <= sphere.getRadius() );
	}

	public boolean isIntersectionPlane( Plane plane ) 
	{
		// check if the line and plane are non-perpendicular, if they
		// eventually they will intersect.
		double denominator = plane.getNormal().dot( this.direction );
		if ( denominator != 0 ) 
		{
			return true;
		}

		// line is coplanar, return origin
		if( plane.distanceToPoint( this.origin ) == 0 ) 
		{
			return true;
		}

		return false;
	}

	public Double distanceToPlane( Plane plane ) 
	{
		double denominator = plane.getNormal().dot( this.direction );
		if ( denominator == 0 ) 
		{
			// line is coplanar, return origin
			if( plane.distanceToPoint( this.origin ) == 0 ) 
			{
				return 0.0;
			}

			// Unsure if this is the correct method to handle this case.
			return null;
		}

		double t = - ( this.origin.dot( plane.getNormal() ) + plane.getConstant() ) / denominator;

		return t;
	}

	public Vector3 intersectPlane( Plane plane)
	{
		return intersectPlane(plane, new Vector3());
	}

	public Vector3 intersectPlane( Plane plane, Vector3 optionalTarget ) 
	{
		Double t = this.distanceToPlane( plane );

		if( t == null ) 
		{
			return null;
		}

		return this.at( t, optionalTarget );
	}

	public Ray transform( Matrix4 matrix4 ) 
	{
		this.direction.add( this.origin ).apply( matrix4 );
		this.origin.apply( matrix4 );
		this.direction.sub( this.origin );

		return this;
	}

	public boolean equals( Ray ray ) 
	{
		return ray.origin.equals( this.origin ) && ray.direction.equals( this.direction );
	}

	public Ray clone() 
	{
		return new Ray().copy( this );
	}
}
