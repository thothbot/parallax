/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.utils;

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
	public static float tangentQuadraticBezier(float t, float p0, float p1, float p2)
	{
		return 2.0f * (1.0f - t) * (p1 - p0) + 2.0f * t * (p2 - p1);
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
	public static float tangentCubicBezier(float t, float p0, float p1, float p2, float p3)
	{
		return   -3.0f * p0 * (1.0f - t) * (1.0f - t) 
				+ 3.0f * p1 * (1.0f - t) * (1.0f - t) 
				- 6.0f * t  * p1 * (1.0f - t) 
				+ 6.0f * t  * p2 * (1.0f - t) 
				- 3.0f * t  * t * p2 + 3.0f * t * t * p3;
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
	public static float tangentSpline(float t, float p0, float p1, float p2, float p3)
	{
		// To check if my formulas are correct
		float h00 =  6.0f * t * t - 6.0f * t; // derived from 2t^3 − 3t^2 + 1
		float h10 =  3.0f * t * t - 4.0f * t + 1.0f; // t^3 − 2t^2 + t
		float h01 = -6.0f * t * t + 6.0f * t; // − 2t3 + 3t2
		float h11 =  3.0f * t * t - 2.0f * t; // t3 − t2

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
	public static float interpolate(float p0, float p1, float p2, float p3, float t)
	{
		float v0 = (p2 - p0) * 0.5f;
		float v1 = (p3 - p1) * 0.5f;
		float t2 = t * t;
		float t3 = t * t2;
		
		return (2.0f * p1 - 2.0f * p2 + v0 + v1) * t3 + (-3.0f * p1 + 3.0f * p2 - 2.0f * v0 - v1) * t2 + v0 * t + p1;
	}
}
