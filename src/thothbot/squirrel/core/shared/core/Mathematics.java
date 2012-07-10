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

public class Mathematics
{
	
	public static boolean isPowerOfTwo ( int value ) 
	{
		return ( value & ( value - 1 ) ) == 0;
	}

	public static boolean isEven(int value)
	{
		return (value % 2 == 1);
	}
	/*
	 * Clamp value to range <a, b>
	 */
	public static float clamp(float x, float a, float b)
	{
		return (x < a) ? a : ((x > b) ? b : x);
	}

	/*
	 * Clamp value to range <a, inf)
	 */
	public static float clampBottom(float x, float a)
	{
		return x < a ? a : x;
	}

	/*
	 * Linear mapping from range <a1, a2> to range <b1, b2>
	 */
	public static float mapLinear(float x, float a1, float a2, float b1, float b2)
	{
		return b1 + (x - a1) * (b2 - b1) / (a2 - a1);
	}

	/*
	 * Random float from <0, 1> with 16 bits of randomness (standard
	 * Math.random() creates repetitive patterns when applied over larger space)
	 */
	public static float random16()
	{

		return (float) (65280 * Math.random() + 255 * Math.random()) / 65535;
	}

	/*
	 * Random integer from <low, high> interval
	 */
	public static int randInt(int low, int high)
	{
		return (int) (low + Math.floor(Math.random() * (high - low + 1)));
	}

	/*
	 * Random float from <low, high> interval
	 */
	public static float randFloat(float low, float high)
	{
		return (float) (low + Math.random() * (high - low));
	}

	/*
	 * Random float from <-range/2, range/2> interval
	 */
	public static float randFloatSpread(float range)
	{
		return (float) (range * (0.5f - Math.random()));
	}

	public static int sign(float x)
	{
		return (x < 0) ? -1 : ((x > 0) ? 1 : 0);
	}
	
	public static double gauss( double x, double sigma ) 
	{
		return Math.exp( - ( x * x ) / ( 2.0 * sigma * sigma ) );
	}
}
