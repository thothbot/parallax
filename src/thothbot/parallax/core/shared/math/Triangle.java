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

import java.util.List;

public class Triangle 
{
	private Vector3 a;
	private Vector3 b;
	private Vector3 c;
	
	// Temporary variables
	static Vector3 _v0 = new Vector3();
	static Vector3 _v1 = new Vector3();
	static Vector3 _v2 = new Vector3();
	
	public Triangle()
	{
		this(new Vector3(), new Vector3(), new Vector3());
	}
	
	public Triangle( Vector3 a, Vector3 b, Vector3 c ) 
	{
		this.a = a;
		this.b = b;
		this.c = c;

	}
	
	public Vector3 getA() {
		return a;
	}

	public void setA(Vector3 a) {
		this.a = a;
	}

	public Vector3 getB() {
		return b;
	}

	public void setB(Vector3 b) {
		this.b = b;
	}

	public Vector3 getC() {
		return c;
	}

	public void setC(Vector3 c) {
		this.c = c;
	}

	public static Vector3 normal( Vector3 a, Vector3 b, Vector3 c) 
	{
		return Triangle.normal(a, b, c, new Vector3());
	}

	public static Vector3 normal( Vector3 a, Vector3 b, Vector3 c, Vector3 optionalTarget ) 
	{		
		optionalTarget.sub( c, b );
		_v0.sub( a, b );
		optionalTarget.cross( _v0 );

		double resultLengthSq = optionalTarget.lengthSq();
		if ( resultLengthSq > 0 ) {

			return optionalTarget.multiply( 1.0 / Math.sqrt( resultLengthSq ) );

		}

		return optionalTarget.set( 0, 0, 0 );

	}

	/**
	 * Static/instance method to calculate barycoordinates
	 * based on: http://www.blackpawn.com/texts/pointinpoly/default.html
	 */
	public static Vector3 barycoordFromPoint( Vector3 point, Vector3 a, Vector3 b, Vector3 c)
	{
		return Triangle.barycoordFromPoint(point, a, b, c, new Vector3());
	}
	
	public static Vector3 barycoordFromPoint( Vector3 point, Vector3 a, Vector3 b, Vector3 c, Vector3 optionalTarget ) 
	{
		_v0.sub( c, a );
		_v1.sub( b, a );
		_v2.sub( point, a );

		double dot00 = _v0.dot( _v0 );
		double dot01 = _v0.dot( _v1 );
		double dot02 = _v0.dot( _v2 );
		double dot11 = _v1.dot( _v1 );
		double dot12 = _v1.dot( _v2 );

		double denom = ( dot00 * dot11 - dot01 * dot01 );

		// colinear or singular triangle
		if ( denom == 0 ) {
			// arbitrary location outside of triangle?
			// not sure if this is the best idea, maybe should be returning undefined
			return optionalTarget.set( - 2.0, - 1.0, - 1.0 );
		}

		double invDenom = 1.0 / denom;
		double u = ( dot11 * dot02 - dot01 * dot12 ) * invDenom;
		double v = ( dot00 * dot12 - dot01 * dot02 ) * invDenom;

		// barycoordinates must always sum to 1
		return optionalTarget.set( 1.0 - u - v, v, u );

	}

	public static boolean containsPoint( Vector3 point, Vector3 a, Vector3 b, Vector3 c ) 
	{	
		Vector3 result = Triangle.barycoordFromPoint( point, a, b, c, _v1 );

		return ( result.x >= 0 ) && ( result.y >= 0 ) && ( ( result.x + result.y ) <= 1 );
	}

	public Triangle	set( Vector3 a, Vector3 b, Vector3 c ) 
	{
		this.a.copy( a );
		this.b.copy( b );
		this.c.copy( c );

		return this;
	}

	public Triangle setFromPointsAndIndices( List<Vector3> points, int i0, int i1, int i2 ) 
	{
		this.a.copy( points.get(i0) );
		this.b.copy( points.get(i1) );
		this.c.copy( points.get(i2) );

		return this;
	}

	public Triangle copy( Triangle triangle ) 
	{
		this.a.copy( triangle.a );
		this.b.copy( triangle.b );
		this.c.copy( triangle.c );

		return this;
	}

	public double area() 
	{
		_v0.sub( this.c, this.b );
		_v1.sub( this.a, this.b );

		return _v0.cross( _v1 ).length() * 0.5;
	}

	public Vector3 midpoint()
	{
		return midpoint(new Vector3());
	}
	
	public Vector3 midpoint( Vector3 optionalTarget ) 
	{
		return optionalTarget.add( this.a, this.b ).add( this.c ).multiply( 1.0 / 3 );

	}

	public Vector3 normal() 
	{
		return normal(new Vector3());
	}
	
	public Vector3 normal( Vector3 optionalTarget ) 
	{
		return Triangle.normal( this.a, this.b, this.c, optionalTarget );
	}

	public Plane plane()
	{
		return plane(new Plane());
	}
	public Plane plane( Plane optionalTarget ) 
	{
		return optionalTarget.setFromCoplanarPoints( this.a, this.b, this.c );
	}

	public Vector3 barycoordFromPoint( Vector3 point) 
	{
		return Triangle.barycoordFromPoint( point, this.a, this.b, this.c, new Vector3() );
	}
	
	public Vector3 barycoordFromPoint( Vector3 point, Vector3 optionalTarget ) 
	{
		return Triangle.barycoordFromPoint( point, this.a, this.b, this.c, optionalTarget );
	}

	public boolean containsPoint( Vector3 point ) 
	{
		return Triangle.containsPoint( point, this.a, this.b, this.c );
	}

	public boolean equals( Triangle triangle ) 
	{
		return triangle.a.equals( this.a ) && triangle.b.equals( this.b ) && triangle.c.equals( this.c );
	}

	public Triangle clone() 
	{
		return new Triangle().copy( this );
	}
}
