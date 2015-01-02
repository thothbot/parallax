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

public class Plane 
{
	private Vector3 normal;
	private double constant;
	
	private static Vector3 __v1 = new Vector3();
	private static Vector3 __v2 = new Vector3();

	public Plane()
	{
		this(new Vector3(1, 0, 0), 0);
	}

	public Plane ( Vector3 normal, double constant ) 
	{
		this.normal = normal;
		this.constant = constant;
	}
	
	public Vector3 getNormal() {
		return normal;
	}

	public void setNormal(Vector3 normal) {
		this.normal = normal;
	}

	public double getConstant() {
		return constant;
	}

	public void setConstant(double constant) {
		this.constant = constant;
	}
	
	public Plane set( Vector3 normal, double constant ) 
	{
		this.normal.copy( normal );
		this.constant = constant;

		return this;
	}

	public Plane setComponents( double x, double y, double z, double w ) 
	{
		this.normal.set( x, y, z );
		this.constant = w;

		return this;
	}

	public Plane  setFromNormalAndCoplanarPoint(Vector3 normal, Vector3 point ) 
	{
		this.normal.copy( normal );
		this.constant = - point.dot( this.normal );	// must be this.normal, not normal, as this.normal is normalized

		return this;
	}

	public Plane setFromCoplanarPoints( Vector3 a, Vector3 b, Vector3 c ) 
	{
		Vector3 normal = Plane.__v1.sub( c, b ).cross( Plane.__v2.sub( a, b ) ).normalize();

		// Q: should an error be thrown if normal is zero (e.g. degenerate plane)?

		this.setFromNormalAndCoplanarPoint( normal, a );

		return this;
	}

	public Plane copy( Plane plane ) 
	{
		this.normal.copy( plane.normal );
		this.constant = plane.constant;
		
		return this;
	}

	public Plane normalize() 
	{
		// Note: will lead to a divide by zero if the plane is invalid.

		double inverseNormalLength = 1.0 / this.normal.length();
		this.normal.multiply( inverseNormalLength );
		this.constant *= inverseNormalLength;

		return this;
	}

	public Plane negate() 
	{
		this.constant *= -1;
		this.normal.negate();

		return this;
	}

	public double distanceToPoint( Vector3 point ) 
	{
		return this.normal.dot( point ) + this.constant;
	}

	public double distanceToSphere( Sphere sphere ) 
	{
		return this.distanceToPoint( sphere.getCenter() ) - sphere.getRadius();
	}

	public Vector3 projectPoint( Vector3 point ) 
	{
		return this.orthoPoint( point ).sub( point ).negate();
	}
	
	public Vector3 projectPoint( Vector3 point, Vector3 optionalTarget ) 
	{
		return this.orthoPoint( point, optionalTarget ).sub( point ).negate();
	}

	public Vector3 orthoPoint( Vector3 point )
	{
		return orthoPoint(point, new Vector3());
	}
	
	public Vector3 orthoPoint( Vector3 point, Vector3 optionalTarget ) 
	{
		double perpendicularMagnitude = this.distanceToPoint( point );

		return optionalTarget.copy( this.normal ).multiply( perpendicularMagnitude );
	}

	/**
	 * Note: this tests if a line intersects the plane, not whether it (or its end-points) are coplanar with it.
	 */
	public boolean isIntersectionLine( Vector3 startPoint, Vector3 endPoint ) 
	{
		double startSign = this.distanceToPoint( startPoint );
		double endSign = this.distanceToPoint( endPoint );

		return ( startSign < 0 && endSign > 0 ) || ( endSign < 0 && startSign > 0 );
	}

	public Vector3 intersectLine( Vector3 startPoint, Vector3 endPoint)
	{
		return intersectLine(startPoint, endPoint, new Vector3());
	}
	
	public Vector3 intersectLine( Vector3 startPoint, Vector3 endPoint, Vector3 optionalTarget ) 
	{

		Vector3 direction = Plane.__v1.sub( endPoint, startPoint );

		double denominator = this.normal.dot( direction );

		if ( denominator == 0 ) 
		{
			// line is coplanar, return origin
			if( this.distanceToPoint( startPoint ) == 0 ) 
			{
				return optionalTarget.copy( startPoint );
			}

			// Unsure if this is the correct method to handle this case.
			return null;
		}

		double t = - ( startPoint.dot( this.normal ) + this.constant ) / denominator;

		if( t < 0 || t > 1 ) 
		{
			return null;
		}

		return optionalTarget.copy( direction ).multiply( t ).add( startPoint );
	}

	public Vector3 coplanarPoint()
	{
		return coplanarPoint(new Vector3());
	}
	
	public Vector3 coplanarPoint( Vector3 optionalTarget ) 
	{
		return optionalTarget.copy( this.normal ).multiply( - this.constant );
	}

	public Plane apply( Matrix4 matrix )
	{
		return apply(matrix, new Matrix3().getInverse( matrix ).transpose());
	}
	
	public Plane apply( Matrix4 matrix, Matrix3 optionalNormalMatrix ) 
	{
		// compute new normal based on theory here:
		// http://www.songho.ca/opengl/gl_normaltransform.html
		Vector3 newNormal = Plane.__v1.copy( this.normal ).apply( optionalNormalMatrix );

		Vector3 newCoplanarPoint = this.coplanarPoint( Plane.__v2 );
		newCoplanarPoint.apply( matrix );

		this.setFromNormalAndCoplanarPoint( newNormal, newCoplanarPoint );

		return this;
	}

	public Plane translate( Vector3 offset ) 
	{
		this.constant = this.constant - offset.dot( this.normal );

		return this;
	}

	public boolean equals( Plane plane ) 
	{
		return plane.normal.equals( this.normal ) && ( plane.constant == this.constant );
	}

	public Plane clone() 
	{
		return new Plane().copy( this );
	}
}
