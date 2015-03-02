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
	
	// Temporary variables
	static Vector3 _v1 = new Vector3();
	static Vector3 _v2 = new Vector3();
	static Matrix3 _m1 = new Matrix3();
	
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
	
	public Plane setComponents( double x, double y, double z, double w ) {

		this.normal.set( x, y, z );
		this.constant = w;

		return this;
	}
	
	public Plane setFromNormalAndCoplanarPoint(Vector3 normal, Vector3 point ) 
	{
		this.normal.copy( normal );
		this.constant = - point.dot( this.normal );	// must be this.normal, not normal, as this.normal is normalized

		return this;
	}
	
	public Plane setFromCoplanarPoints( Vector3 a, Vector3 b, Vector3 c ) 
	{
		Vector3 normal = _v1.sub( c, b ).cross( _v2.sub( a, b ) ).normalize();

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
		this.constant *= - 1;
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
	public boolean isIntersectionLine( Line3 line ) 
	{
		// Note: this tests if a line intersects the plane, not whether it (or its end-points) are coplanar with it.

		double startSign = this.distanceToPoint( line.getStart() );
		double endSign = this.distanceToPoint( line.getEnd() );

		return ( startSign < 0 && endSign > 0 ) || ( endSign < 0 && startSign > 0 );
	}

	public Vector3 intersectLine( Line3 line )
	{
		return intersectLine(line, new Vector3());
	}
	
	public Vector3 intersectLine( Line3 line, Vector3 optionalTarget ) 
	{
		Vector3 direction = line.delta( _v1 );

		double denominator = this.normal.dot( direction );

		if ( denominator == 0 ) {

			// line is coplanar, return origin
			if ( this.distanceToPoint( line.getStart() ) == 0 ) {

				return optionalTarget.copy( line.getStart() );

			}

			// Unsure if this is the correct method to handle this case.
			return null;

		}

		double t = - ( line.getStart().dot( this.normal ) + this.constant ) / denominator;

		if ( t < 0 || t > 1 ) {

			return null;

		}

		return optionalTarget.copy( direction ).multiply( t ).add( line.getStart() );

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
		return apply(matrix, _m1.getNormalMatrix( matrix ));
	}
	
	public Plane apply( Matrix4 matrix, Matrix3 normalMatrix ) 
	{
		// compute new normal based on theory here:
		// http://www.songho.ca/opengl/gl_normaltransform.html
		Vector3 newNormal = _v1.copy( this.normal ).apply( normalMatrix );

		Vector3 newCoplanarPoint = this.coplanarPoint( _v2 );
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
