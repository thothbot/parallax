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

public class Spline {

	private Vector3[] points;
	
	public static Spline initFromArray( double[][] a ) {

		Spline spline = new Spline();
		
		spline.points = new Vector3[ a.length ];

		for ( int i = 0; i < a.length; i ++ ) {

			spline.points[ i ] = new Vector3( a[ i ][ 0 ], a[ i ][ 1 ], a[ i ][ 2 ] );

		}
		
		return spline;
	}
	
	public Vector3 getPoint(int k) {
		int point = ( this.points.length - 1 ) * k;
		int intPoint = (int)Math.floor( point );
		int weight = point - intPoint;

		int[]c = new int[4];
		c[ 0 ] = intPoint == 0 ? intPoint : intPoint - 1;
		c[ 1 ] = intPoint;
		c[ 2 ] = intPoint  > this.points.length - 2 ? this.points.length - 1 : intPoint + 1;
		c[ 3 ] = intPoint  > this.points.length - 3 ? this.points.length - 1 : intPoint + 2;

		Vector3 pa = this.points[ c[ 0 ] ];
		Vector3 pb = this.points[ c[ 1 ] ];
		Vector3 pc = this.points[ c[ 2 ] ];
		Vector3 pd = this.points[ c[ 3 ] ];

		int w2 = weight * weight;
		int w3 = weight * w2;

		Vector3 v3 = new Vector3();
		v3.x = interpolate( pa.x, pb.x, pc.x, pd.x, weight, w2, w3 );
		v3.y = interpolate( pa.y, pb.y, pc.y, pd.y, weight, w2, w3 );
		v3.z = interpolate( pa.z, pb.z, pc.z, pd.z, weight, w2, w3 );

		return v3;

	}
	
	public Vector3[] getControlPointsArray() {

		int l = this.points.length;
		
		Vector3[] coords = new Vector3[l];

		for ( int i = 0; i < l; i ++ ) {

			Vector3 p = this.points[ i ];
			coords[ i ] = p.clone();

		}

		return coords;

	}
	
	private double interpolate( double p0, double p1, double p2, double p3, double t, double t2, double t3 ) {

		double v0 = ( p2 - p0 ) * 0.5,
			v1 = ( p3 - p1 ) * 0.5;

		return ( 2.0 * ( p1 - p2 ) + v0 + v1 ) * t3 + ( - 3.0 * ( p1 - p2 ) - 2.0 * v0 - v1 ) * t2 + v0 * t + p1;

	}
}
