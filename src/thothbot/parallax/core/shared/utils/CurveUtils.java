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

package thothbot.parallax.core.shared.utils;

/**
 * This class implements some helpers methods for Curve instances
 * 
 * This code is based on js-code written by zz85
 * http://www.lab4games.net/zz85/blog
 * 
 * @author thothbot
 *
 */
public class CurveUtils
{
	/**
	 * This method calculates tangent of Quadratic Bezier Curve.
	 * 
	 * @param t  the value in range <0.0, 1.0>. The t in the 
	 * 			function for a linear Bezier curve can be 
	 * 			thought of as describing how far B(t) is from p0 to p2.
	 * @param p0 the p0 Quadratic Bezier Curve point.
	 * @param p1 the p1 Quadratic Bezier Curve point.
	 * @param p2 the p2 Quadratic Bezier Curve point.
	 * 
	 * @return the tangent of Quadratic Bezier Curve
	 */
	public static double tangentQuadraticBezier(double t, double p0, double p1, double p2)
	{
		return 2.0 * (1.0 - t) * (p1 - p0) + 2.0 * t * (p2 - p1);
	}

	/**
	 * This method calculates tangent of Cubic Bezier Curve.
	 * 
	 * Puay Bing, thanks for helping with this derivative!
	 * 
	 * @param t  the value in range <0.0, 1.0>. The t in the 
	 * 			function for a linear Bezier curve can be 
	 * 			thought of as describing how far B(t) is from p0 to p3.
	 * 
	 * @param p0 the p0 Cubic Bezier Curve point.
	 * @param p1 the p1 Cubic Bezier Curve point.
	 * @param p2 the p2 Cubic Bezier Curve point.
	 * @param p3 the p3 Cubic Bezier Curve point.
	 * 
	 * @return the tangent of Cubic Bezier Curve
	 */
	public static double tangentCubicBezier(double t, double p0, double p1, double p2, double p3)
	{
		return   -3.0 * p0 * (1.0 - t) * (1.0 - t) 
				+ 3.0 * p1 * (1.0 - t) * (1.0 - t) 
				- 6.0 * t  * p1 * (1.0 - t) 
				+ 6.0 * t  * p2 * (1.0 - t) 
				- 3.0 * t  * t * p2 + 3.0 * t * t * p3;
	}

	/**
	 * This method calculates tangent of Spline Curve.
	 * 
	 * @param t  the value in range <0.0, 1.0>. The t in the 
	 * 			function for a linear Bezier curve can be 
	 * 			thought of as describing how far B(t) is from p0 to p3.
	 * @param p0 the p0 Spline point.
	 * @param p1 the p1 Spline point.
	 * @param p2 the p2 Spline point.
	 * @param p3 the p3 Spline point.
	 * 
	 * @return the tangent of Spline Curve
	 */
	public static double tangentSpline(double t, double p0, double p1, double p2, double p3)
	{
		// To check if my formulas are correct
		double h00 =  6.0 * t * t - 6.0 * t; // derived from 2t^3 − 3t^2 + 1
		double h10 =  3.0 * t * t - 4.0 * t + 1.0; // t^3 − 2t^2 + t
		double h01 = -6.0 * t * t + 6.0 * t; // − 2t3 + 3t2
		double h11 =  3.0 * t * t - 2.0 * t; // t3 − t2

		return h00 + h10 + h01 + h11;
	}

	/**
	 * Interpolation of Catmull-Rom Spline
	 * 
	 * @param p0 the p0 Spline point.
	 * @param p1 the p1 Spline point.
	 * @param p2 the p2 Spline point.
	 * @param p3 the p3 Spline point.
	 * @param t the value in range <0.0, 1.0>. The t in the 
	 * 			function for a linear Bezier curve can be 
	 * 			thought of as describing how far B(t) is from p0 to p3.
	 * 
	 * @return the interpolated value. 
	 */
	public static double interpolate(double p0, double p1, double p2, double p3, double t)
	{
		double v0 = (p2 - p0) * 0.5;
		double v1 = (p3 - p1) * 0.5;
		double t2 = t * t;
		double t3 = t * t2;
		
		return ( 2.0 * p1 - 2.0 * p2 +       v0 + v1) * t3 
			 + (-3.0 * p1 + 3.0 * p2 - 2.0 * v0 - v1) * t2 
			 + v0 * t + p1;
	}
}
