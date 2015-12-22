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

package org.parallax3d.parallax.graphics.extras.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.curves.*;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.extras.ShapeUtils;
import org.parallax3d.parallax.system.ThreeJsObject;

@ThreeJsObject("THREE.Path")
public class Path extends CurvePath
{
	public static enum PATH_ACTIONS {
		MOVE_TO,
		LINE_TO,
		QUADRATIC_CURVE_TO, // Bezier quadratic curve
		BEZIER_CURVE_TO, 	// Bezier cubic curve
		CSPLINE_THRU,		// Catmull-rom spline
		ARC,				// Circle
		ELLIPSE
	};
	
	public class Action 
	{
		public PATH_ACTIONS action;
		public List<Object> args;
		
		public Action(PATH_ACTIONS action, Object ...args)
		{
			this.action = action;
			this.args = Arrays.asList(args);
		}
		
		public String toString()
		{
			return "{action=" + this.action.name() + ", args=" + this.args + "}";
		}
	}
	
	private List<Action> actions;
	private boolean useSpacedPoints = false;

	public Path() 
	{
		super();
		this.actions = new ArrayList<Action>();
	}
	
	public Path( List<Vector2> points ) 
	{
		this();
		this.fromPoints( points );
	}
	
	public List<Action> getActions()
	{
		return this.actions;
	}
	
	// Create path using straight lines to connect all points
	// - vectors: array of Vector2

	public void fromPoints( List<Vector2> vectors ) 
	{
		moveTo( vectors.get( 0 ).getX(), vectors.get( 0 ).getY() );

		for ( int v = 1, vlen = vectors.size(); v < vlen; v ++ )
			lineTo( vectors.get( v ).getX(), vectors.get( v ).getY() );
	}
	
	public void moveTo( float x, float y ) 
	{
		this.actions.add( new Action(PATH_ACTIONS.MOVE_TO, x, y));
	}
	
	public void lineTo( float x, float y ) 
	{	
		List<Object> lastargs = this.actions.get( this.actions.size() - 1 ).args;

		float x0 = (Float) lastargs.get( lastargs.size() - 2 );
		float y0 = (Float) lastargs.get( lastargs.size() - 1 );

		LineCurve curve = new LineCurve( new Vector2( x0, y0 ), new Vector2( x, y ) );
		add(curve);

		this.actions.add( new Action( PATH_ACTIONS.LINE_TO, x, y ) );
	}
	
	public void quadraticCurveTo(float aCPx, float aCPy, float aX, float aY ) 
	{		
		List<Object> lastargs = this.actions.get( this.actions.size() - 1 ).args;
				
		float x0 = (Float) lastargs.get( lastargs.size() - 2 );
		float y0 = (Float) lastargs.get( lastargs.size() - 1 );

		QuadraticBezierCurve curve = new QuadraticBezierCurve(
				new Vector2( x0, y0 ),
				new Vector2( aCPx, aCPy ),
				new Vector2( aX, aY ) );
		add(curve);

		this.actions.add( new Action( PATH_ACTIONS.QUADRATIC_CURVE_TO, aCPx, aCPy, aX, aY ) );
	}
	
	public void bezierCurveTo( float aCP1x, float aCP1y, float aCP2x, float aCP2y, float aX, float aY ) 
	{

		List<Object> lastargs = this.actions.get( this.actions.size() - 1 ).args;

		float x0 = (Float) lastargs.get( lastargs.size() - 2 );
		float y0 = (Float) lastargs.get( lastargs.size() - 1 );

		CubicBezierCurve curve = new CubicBezierCurve( new Vector2( x0, y0 ),
				new Vector2( aCP1x, aCP1y ),
				new Vector2( aCP2x, aCP2y ),
				new Vector2( aX, aY ) );
		add( curve );

		this.actions.add( new Action( PATH_ACTIONS.BEZIER_CURVE_TO, aCP1x, aCP1y, aCP2x, aCP2y, aX, aY ) );
	}
	
	/*
	 * @param pts
	 * 		List<{@link Vector2}>
	 */
	public void splineThru( List<Vector2> pts) 
	{	
		List<Object> lastargs = this.actions.get( this.actions.size() - 1 ).args;

		float x0 = (Float) lastargs.get( lastargs.size() - 2 );
		float y0 = (Float) lastargs.get( lastargs.size() - 1 );
		
		//---
		List<Vector2> npts = new ArrayList<Vector2>();
		npts.add(new Vector3( x0, y0, 0 ));
		npts.addAll(pts);
	
		SplineCurve curve = new SplineCurve( npts );
		add( curve );

		this.actions.add( new Action(PATH_ACTIONS.CSPLINE_THRU, pts ) );
	}
	
	/*
	 * FUTURE: Change the API or follow canvas API?
	 */
	public void arc( float aX, float aY, float aRadius, 
			float aStartAngle, float aEndAngle, boolean aClockwise ) 
	{
		List<Object> lastargs = this.actions.get( this.actions.size() - 1 ).args;
		
		float x0 = (Float) lastargs.get( lastargs.size() - 2 );
		float y0 = (Float) lastargs.get( lastargs.size() - 1 );
		
		absarc(aX + x0, aY + y0, aRadius, aStartAngle, aEndAngle, aClockwise );
	}

	public void absarc( float aX, float aY, float aRadius, 
			float aStartAngle, float aEndAngle, boolean aClockwise ) 
	{		
		absellipse(aX, aY, aRadius, aRadius, aStartAngle, aEndAngle, aClockwise);
	}
	
	public void ellipse( float aX, float aY, float xRadius, float yRadius,
			float aStartAngle, float aEndAngle, boolean aClockwise ) 
	{
		List<Object> lastargs = this.actions.get( this.actions.size() - 1 ).args;
		float x0 = (Float) lastargs.get( lastargs.size() - 2 );
		float y0 = (Float) lastargs.get( lastargs.size() - 1 );

		absellipse(aX + x0, aY + y0, xRadius, yRadius, aStartAngle, aEndAngle, aClockwise );
	}
	
	public void absellipse(float aX, float aY, float xRadius, float yRadius,
			float aStartAngle, float aEndAngle, boolean aClockwise ) 
	{

		List<Object> lastargs = this.actions.get( this.actions.size() - 1 ).args;
				
		EllipseCurve curve = new EllipseCurve( aX, aY, xRadius, yRadius,
				aStartAngle, aEndAngle, aClockwise );
		add( curve );

		Vector2 lastPoint = curve.getPoint(aClockwise ? 1 : 0);

		this.actions.add( new Action(PATH_ACTIONS.ELLIPSE, aX, aY, xRadius, yRadius, aStartAngle, aEndAngle, aClockwise, lastPoint.getX(), lastPoint.getY() ) );
	}


	
	public List<Vector2> getSpacedPoints( boolean closedPath )
	{
		return getSpacedPoints(40, closedPath);
	}

	public List<Vector2> getSpacedPoints( int divisions, boolean closedPath ) 
	{
		List<Vector2> points = new ArrayList<Vector2>();

		for ( int i = 0; i < divisions; i ++ )
			points.add( this.getPoint( i / (float)divisions ) );

//		if ( closedPath )
//			points.add( points.get(0) );

		return points;
	}
	
	/* 
	 * @return an List of {@link Vector2} based on contour of the path
	 */
	public List<Vector2> getPoints( boolean closedPath ) 
	{
		return getPoints(12, closedPath);
	}

	public List<Vector2> getPoints( int divisions, boolean closedPath ) 
	{
		if (this.useSpacedPoints)
			return this.getSpacedPoints( divisions, closedPath );

		List<Vector2> points = new ArrayList<Vector2>();

		float cpx, cpy, cpx2, cpy2, cpx1, cpy1, cpx0, cpy0;

		for ( int i = 0, il = this.actions.size(); i < il; i ++ ) 
		{
			Action item = this.actions.get( i );

			PATH_ACTIONS action = item.action;
			List<Object> args = item.args;

			switch( action ) 
			{

			case MOVE_TO:

				points.add( new Vector2( (Float)args.get( 0 ), (Float)args.get( 1 ) ) );
				break;

			case LINE_TO:

				points.add( new Vector2( (Float)args.get( 0 ), (Float)args.get( 1 ) ) );
				break;

			case QUADRATIC_CURVE_TO:

				cpx  = (Float)args.get( 2 );
				cpy  = (Float)args.get( 3 );

				cpx1 = (Float)args.get( 0 );
				cpy1 = (Float)args.get( 1 );

				if ( points.size() > 0 ) 
				{
					Vector2 laste = points.get( points.size() - 1 );

					cpx0 = laste.getX();
					cpy0 = laste.getY();
				} 
				else 
				{
					List<Object> laste = this.actions.get( i - 1 ).args;

					cpx0 = (Float) laste.get( laste.size() - 2 );
					cpy0 = (Float) laste.get( laste.size() - 1 );
				}

				for ( int j = 1; j <= divisions; j ++ ) 
				{
					float t = j / (float)divisions;

					float tx = ShapeUtils.b2(t, cpx0, cpx1, cpx);
					float ty = ShapeUtils.b2( t, cpy0, cpy1, cpy );

					points.add( new Vector2( tx, ty ) );
			  	}

				break;

			case BEZIER_CURVE_TO:

				cpx  = (Float)args.get( 4 );
				cpy  = (Float)args.get( 5 );

				cpx1 = (Float)args.get( 0 );
				cpy1 = (Float)args.get( 1 );

				cpx2 = (Float)args.get( 2 );
				cpy2 = (Float)args.get( 3 );

				if ( points.size() > 0 ) 
				{
					Vector2 laste = points.get( points.size() - 1 );

					cpx0 = laste.getX();
					cpy0 = laste.getY();
				} 
				else 
				{
					List<Object> laste = this.actions.get( i - 1 ).args;

					cpx0 = (Float) laste.get( laste.size() - 2 );
					cpy0 = (Float) laste.get( laste.size() - 1 );
				}


				for ( int j = 1; j <= divisions; j ++ ) 
				{
					float t = j / (float)divisions;

					float tx = ShapeUtils.b3( t, cpx0, cpx1, cpx2, cpx );
					float ty = ShapeUtils.b3( t, cpy0, cpy1, cpy2, cpy );

					points.add( new Vector2( tx, ty ) );
				}

				break;

			case CSPLINE_THRU:

				List<Object> laste = this.actions.get( i - 1 ).args;

				Vector2 last = new Vector2( (Float)laste.get( laste.size() - 2 ), (Float)laste.get( laste.size() - 1 ) );
				List<Vector2> spts = new ArrayList<Vector2>();
				spts.add(last);

				List<Vector3> v = (List<Vector3>) args.get( 0 );
				float n = divisions * v.size();

				spts.addAll(v);

				SplineCurve spline = new SplineCurve( spts );

				for ( int j = 1; j <= n; j ++ )
					points.add( (Vector2) spline.getPointAt( j / n ) ) ;

				break;

			case ARC:

				float aX = (Float)args.get( 0 );
				float aY = (Float)args.get( 1 );
				float aRadius = (Float)args.get( 2 );
				float aStartAngle = (Float)args.get( 3 );
				float aEndAngle = (Float)args.get( 4 );
				boolean aClockwise = !!(Boolean)args.get( 5 );

				float deltaAngle = aEndAngle - aStartAngle;
				int tdivisions = divisions * 2;

				for ( int j = 1; j <= tdivisions; j ++ ) 
				{
					float t = j / (float)tdivisions;

					if ( !aClockwise ) 
					{
						t = 1.0f - t;
					}

					float angle = aStartAngle + t * deltaAngle;

					float tx = aX + aRadius * (float)Math.cos( angle );
					float ty = aY + aRadius * (float)Math.sin( angle );

					points.add( new Vector2( tx, ty ) );
				}

				//console.log(points);

			  break;
			  
			  
			case ELLIPSE:

				float aXE = (Float)args.get( 0 );
				float aYE = (Float)args.get( 1 );
				float xRadiusE = (Float)args.get( 2 );
				float yRadiusE = (Float)args.get( 3 );
				float aStartAngleE = (Float)args.get( 4 );
				float aEndAngleE = (Float)args.get( 5 );
				boolean aClockwiseE = !!(Boolean)args.get( 6 );
				
				float deltaAngleE = aEndAngleE - aStartAngleE;
				float tdivisionsE = divisions * 2;

				for ( int j = 1; j <= tdivisionsE; j ++ ) 
				{
					float t = j / (float)tdivisionsE;

					if ( ! aClockwiseE ) 
					{
						t = 1 - t;
					}

					float angle = aStartAngleE + t * deltaAngleE;

					float tx = aXE + xRadiusE * (float)Math.cos( angle );
					float ty = aYE + yRadiusE * (float)Math.sin( angle );

					points.add( new Vector2( tx, ty ) );

				}

				//console.log(points);

			  break;

			} // end switch

		}

		// Normalize to remove the closing point by default.
		Vector2 lastPoint = (Vector2) points.get( points.size() - 1);
		double EPSILON = 0.0000000001;
		if ( Math.abs(lastPoint.getX() - ((Vector2)points.get( 0 )).getX()) < EPSILON &&
	             Math.abs(lastPoint.getY() - ((Vector2)points.get( 0 )).getY()) < EPSILON)
			points.remove( points.size() - 1);
		
		if ( closedPath )
			points.add( points.get( 0 ) );

		return points;
	}
	
//	// Breaks path into shapes
//
//	public void toShapes() 
//	{
////		var i, il, item, action, args;
//
//		List<Path> subPaths = new ArrayList<Path>();
//				
//		Path lastPath = new Path();
//
//		for ( int i = 0, il = this.actions.size(); i < il; i ++ ) 
//		{
//			Action item = this.actions.get( i );
//
//			List<Object> args = item.args;
//			Path.PATH_ACTIONS action = item.action;
//
//			if ( action == Path.PATH_ACTIONS.MOVE_TO ) 
//			{
//				if ( lastPath.actions.size() > 0 ) 
//				{
//					subPaths.add( lastPath );
//					lastPath = new Path();
//				}
//			}
//
//			lastPath[ action ].apply( lastPath, args );
//
//		}
//
//		if ( lastPath.actions.size() != 0 )
//			subPaths.add( lastPath );
//
//		if ( subPaths.length == 0 ) 
//			return [];
//
//		var tmpPath, tmpShape, shapes = [];
//
//		var holesFirst = !THREE.Shape.Utils.isClockWise( subPaths[ 0 ].getPoints() );
//		// console.log("Holes first", holesFirst);
//
//		if ( subPaths.length == 1) 
//		{
//			tmpPath = subPaths[0];
//			tmpShape = new THREE.Shape();
//			tmpShape.actions = tmpPath.actions;
//			tmpShape.curves = tmpPath.curves;
//			shapes.push( tmpShape );
//
//			return shapes;
//		};
//
//		if ( holesFirst ) {
//
//			tmpShape = new THREE.Shape();
//
//			for ( i = 0, il = subPaths.length; i < il; i ++ ) {
//
//				tmpPath = subPaths[ i ];
//
//				if ( THREE.Shape.Utils.isClockWise( tmpPath.getPoints() ) ) {
//
//					tmpShape.actions = tmpPath.actions;
//					tmpShape.curves = tmpPath.curves;
//
//					shapes.push( tmpShape );
//					tmpShape = new THREE.Shape();
//
//					//console.log('cw', i);
//
//				} else {
//
//					tmpShape.holes.push( tmpPath );
//
//					//console.log('ccw', i);
//
//				}
//
//			}
//
//		} else {
//
//			// Shapes first
//
//			for ( i = 0, il = subPaths.length; i < il; i ++ ) {
//
//				tmpPath = subPaths[ i ];
//
//				if ( THREE.Shape.Utils.isClockWise( tmpPath.getPoints() ) ) {
//
//
//					if ( tmpShape ) shapes.push( tmpShape );
//
//					tmpShape = new THREE.Shape();
//					tmpShape.actions = tmpPath.actions;
//					tmpShape.curves = tmpPath.curves;
//
//				} else {
//
//					tmpShape.holes.push( tmpPath );
//
//				}
//
//			}
//
//			shapes.push( tmpShape );
//
//		}
//
//		//console.log("shape", shapes);
//
//		return shapes;
//	}
	
	public List<Vector2> getTransformedSpacedPoints( boolean closedPath ) 
	{
		return getTransformedSpacedPoints(closedPath, getBends());
	}

	public List<Vector2>  getTransformedSpacedPoints( boolean closedPath, List<CurvePath> bends ) 
	{
		List<Vector2> oldPts = (List<Vector2>)(List<?>)this.getSpacedPoints( closedPath );

		for ( int i = 0; i < bends.size(); i ++ )
			oldPts = getWrapPoints( oldPts, bends.get( i ) );

		return oldPts;
	}

	/*
	 * Generate geometry from path points (for Line or ParticleSystem objects)
	 */
	public Geometry createPointsGeometry()
	{
		return createGeometry( getPoints(true) );
	}
	
	public Geometry createPointsGeometry( int divisions )
	{
		return createGeometry( getPoints( divisions, true ) );
	}
	
	/*
	 * Generate geometry from equidistance sampling along the path
	 */
	public Geometry createSpacedPointsGeometry()
	{
		return createGeometry( getSpacedPoints(true) );
	}
	
	public Geometry createSpacedPointsGeometry( int divisions )
	{
		return createGeometry( getSpacedPoints( divisions, true ) );
	}

	private Geometry createGeometry(List<Vector2> points)
	{
		Geometry geometry = new Geometry();

		for ( int i = 0; i < points.size(); i ++ ) 
		{
			geometry.getVertices().add( new Vector3(
					((Vector2)points.get( i )).getX(), 
					((Vector2)points.get( i )).getY(), 0 ) );
		}

		return geometry;
	}
	
	public List<Vector2> getTransformedPoints() 
	{
		return getTransformedPoints(false, getBends());
	}
	
	public List<Vector2> getTransformedPoints( boolean closedPath )
	{
		return getTransformedPoints(closedPath, getBends());
	}

	public List<Vector2> getTransformedPoints( boolean closedPath, List<CurvePath> bends ) 
	{
		List<Vector2> oldPts = (List<Vector2>)(List<?>)this.getPoints( closedPath ); // getPoints getSpacedPoints

		for ( int i = 0; i < bends.size(); i ++ )
			oldPts = this.getWrapPoints( oldPts, bends.get( i ) );

		return oldPts;
	}
}
