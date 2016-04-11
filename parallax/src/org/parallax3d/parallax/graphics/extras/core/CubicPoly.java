/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
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

/**
 * Based on an optimized c++ solution in
 * - http://stackoverflow.com/questions/9489736/catmull-rom-curve-with-no-cusps-and-no-self-intersections/
 * - http://ideone.com/NoEbVM
 *
 * This CubicPoly class could be used for reusing some variables and calculations,
 * but for three.js curve use, it could be possible inlined and flatten into a single function call
 * which can be placed in CurveUtils.
 */
public class CubicPoly {

    private double c0;
    private double c1;
    private double c2;
    private double c3;

    /**
	 * Compute coefficients for a cubic polynomial
	 *   p(s) = c0 + c1*s + c2*s^2 + c3*s^3
	 * such that
	 *   p(0) = x0, p(1) = x1
	 *  and
	 *   p'(0) = t0, p'(1) = t1.
	 */
    public void init( double x0, double x1, double t0, double t1 ) {

        this.c0 = x0;
        this.c1 = t0;
        this.c2 = - 3.0 * x0 + 3.0 * x1 - 2.0 * t0 - t1;
        this.c3 = 2.0 * x0 - 2.0 * x1 + t0 + t1;

    }

    public void initNonuniformCatmullRom( double x0, double x1, double x2, double x3, double dt0, double dt1, double dt2 ) {

        // compute tangents when parameterized in [t1,t2]
        double t1 = ( x1 - x0 ) / dt0 - ( x2 - x0 ) / ( dt0 + dt1 ) + ( x2 - x1 ) / dt1;
        double t2 = ( x2 - x1 ) / dt1 - ( x3 - x1 ) / ( dt1 + dt2 ) + ( x3 - x2 ) / dt2;

        // rescale tangents for parametrization in [0,1]
        t1 *= dt1;
        t2 *= dt1;

        // initCubicPoly
        this.init( x1, x2, t1, t2 );

    }

    /**
     * standard Catmull-Rom spline: interpolate between x1 and x2 with previous/following points x1/x4
     */
    public void initCatmullRom( double x0, double x1, double x2, double x3, double tension ) {

        this.init( x1, x2, tension * ( x2 - x0 ), tension * ( x3 - x1 ) );

    }

    public double calc( double t ) {

        double t2 = t * t;
        double t3 = t2 * t;
        return this.c0 + this.c1 * t + this.c2 * t2 + this.c3 * t3;

    }
}
