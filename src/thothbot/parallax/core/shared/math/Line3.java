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

public class Line3 
{

	private Vector3 start;
	private Vector3 end;
	
	//Temporary variables
	static Vector3 _startP = new Vector3();
	static Vector3 _startEnd = new Vector3();
		
	public Line3()
	{
		this(new Vector3(), new Vector3());
	}
	
	public Line3(Vector3 start, Vector3 end)
	{
		this.start = start;
		this.end = end;
	}
	
	public Vector3 getStart() {
		return start;
	}

	public void setStart(Vector3 start) {
		this.start = start;
	}

	public Vector3 getEnd() {
		return end;
	}

	public void setEnd(Vector3 end) {
		this.end = end;
	}
	
	public Line3 set( Vector3 start, Vector3 end ) 
	{
		this.start.copy( start );
		this.end.copy( end );

		return this;
	}

	public Line3 copy( Line3 line ) 
	{
		this.start.copy( line.start );
		this.end.copy( line.end );

		return this;
	}

	public Vector3 center()
	{
		return center(new Vector3());
	}
	
	public Vector3 center( Vector3 optionalTarget ) 
	{
		return optionalTarget.add( this.start, this.end ).multiply( 0.5 );
	}

	public Vector3 delta()
	{
		return delta(new Vector3());
	}
	
	public Vector3 delta( Vector3 optionalTarget ) 
	{
		return optionalTarget.sub( this.end, this.start );
	}

	public double distanceSq() 
	{
		return this.start.distanceToSquared( this.end );
	}

	public double distance() 
	{
		return this.start.distanceTo( this.end );
	}

	public Vector3 at( double t )
	{
		return at(t, new Vector3());
	}
	
	public Vector3 at( double t, Vector3 optionalTarget ) 
	{
		return this.delta( optionalTarget ).multiply( t ).add( this.start );
	}

	public double closestPointToPointParameter( Vector3 point ) 
	{
		return closestPointToPointParameter(point, false);
	}
	
	public double closestPointToPointParameter( Vector3 point, boolean clampToLine ) 
	{
		_startP.sub( point, this.start );
		_startEnd.sub( this.end, this.start );

		double startEnd2 = _startEnd.dot( _startEnd );
		double startEnd_startP = _startEnd.dot( _startP );

		double t = startEnd_startP / startEnd2;

		if ( clampToLine ) {

			t = Mathematics.clamp( t, 0, 1 );

		}

		return t;
	}

	public Vector3 closestPointToPoint( Vector3 point, boolean clampToLine)
	{
		return closestPointToPoint(point, clampToLine, new Vector3());
	}
	
	public Vector3 closestPointToPoint( Vector3 point, boolean clampToLine, Vector3 optionalTarget ) 
	{
		double t = this.closestPointToPointParameter( point, clampToLine );

        return this.delta( optionalTarget ).multiply( t ).add( this.start );
	}

	public Line3 apply( Matrix4 matrix ) 
	{
		this.start.apply( matrix );
		this.end.apply( matrix );

		return this;
	}

	public boolean equals( Line3 line ) 
	{
		return line.start.equals( this.start ) && line.end.equals( this.end );
	}

	public Line3 clone() 
	{
		return new Line3().copy( this );
	}
}
