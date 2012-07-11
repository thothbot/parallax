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

package thothbot.squirrel.core.shared.core;

/**
 * Class implements some mathematical helpers methods.
 * 
 * @author thothbot
 */
		
public class Mathematics
{
	/**
	 * The method checks if the value is power of two.
	 * 
	 * @param value the Integer value
	 */
	public static boolean isPowerOfTwo ( int value ) 
	{
		return ( value & ( value - 1 ) ) == 0;
	}

	/**
	 * The method checks id the value is even.
	 * 
	 * @param value the Integer value
	 */
	public static boolean isEven(int value)
	{
		return (value % 2 == 1);
	}
	
	/**
	 * The method clamps the scalar x to range <a, b>.
	 * 
	 * @param x the scalar value for clamping
	 * @param a the start value of the range
	 * @param b the end value of the range
	 * 
	 * @return a clamped scalar by range <a, b>
	 */
	public static float clamp(float x, float a, float b)
	{
		return (x < a) ? a : ((x > b) ? b : x);
	}

	/**
	 * The method clamps the scalar x to range <a, inf).
	 * 
	 * @param x the scalar value for clamping
	 * @param a the start value of the range
	 * 
	 * @return a clamped scalar by range <a, inf).
	 */
	public static float clampBottom(float x, float a)
	{
		return x < a ? a : x;
	}

	/**
	 * Linear mapping the scalar x from range <a1, a2> to range <b1, b2>
	 * 
	 * @param x  the scalar value for linear mapping
	 * @param a1 the start value of the first range
	 * @param a2 the end value of the first range
	 * @param b1 the start value of the last range
	 * @param b2 the end value of the last range
	 * 
	 * @return a mapped value
	 */
	public static float mapLinear(float x, float a1, float a2, float b1, float b2)
	{
		return b1 + (x - a1) * (b2 - b1) / (a2 - a1);
	}

	/**
	 * The method generates random float value in the range <0, 1> with 
	 * 16 bits of randomness (standard Math.random() creates repetitive 
	 * patterns when applied over larger space).
	 * 
	 * @return a random Float value.
	 */
	public static float random16()
	{

		return (float) (65280 * Math.random() + 255 * Math.random()) / 65535;
	}

	/**
	 * The method generates random integer value in the interval <low, high>.  
	 * 
	 * @param low  the start value of the interval.
	 * @param high the end value of the interval
	 * 
	 * @return a random Integer value
	 */
	public static int randInt(int low, int high)
	{
		return (int) (low + Math.floor(Math.random() * (high - low + 1)));
	}

	/**
	 * The method generates random float value in the range <low, high>.
	 * 
	 * @param low  the start value of the range.
	 * @param high the end value of the range
	 * 
	 * @return a random Float value.
	 */
	public static float randFloat(float low, float high)
	{
		return (float) (low + Math.random() * (high - low));
	}

	/**
	 * The method generates random float value in the interval 
	 * <-range/2, range/2>.
	 * 
	 * @param range the value used to build the interval
	 * 
	 * @return a random Float value.
	 */
	public static float randFloatSpread(float range)
	{
		return (float) (range * (0.5f - Math.random()));
	}

	/**
	 * This method returns a sign of the scalar x.
	 * 
	 * @param x the scalar value for checking.
	 * 
	 * @return 1 or -1
	 */
	public static int sign(float x)
	{
		return (x < 0) ? -1 : ((x > 0) ? 1 : 0);
	}
	
	/**
	 * Method computes the Gaussian function.
	 *  
	 * @param x the input scalar
	 * @param sigma the Gaussian sigma parameter.
	 * 
	 * @return a Gaussian function value
	 */
	public static double gauss( double x, double sigma ) 
	{
		return Math.exp( - ( x * x ) / ( 2.0 * sigma * sigma ) );
	}
}
