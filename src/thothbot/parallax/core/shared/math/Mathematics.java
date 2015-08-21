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


/**
 * Class implements some mathematical helpers methods.
 * 
 * @author thothbot
 */
		
public class Mathematics
{
	/**
	 * The method clamps the scalar x to range &#60;a, b&#62;.
	 * 
	 * @param x the scalar value for clamping
	 * @param a the start value of the range
	 * @param b the end value of the range
	 * 
	 * @return a clamped scalar by range &#60;a, b&#62;
	 */
	public static double clamp(double x, double a, double b)
	{
		return ( x < a ) ? a : ( ( x > b ) ? b : x );
	}

	/**
	 * The method clamps the scalar x to range &#60;a, inf).
	 * 
	 * @param x the scalar value for clamping
	 * @param a the start value of the range
	 * 
	 * @return a clamped scalar by range &#60;a, inf).
	 */
	public static double clampBottom(double x, double a)
	{
		return x < a ? a : x;
	}
	
	/**
	 * Linear mapping the scalar x from range &#60;a1, a2&#62; to range &#60;b1, b&#62;
	 * 
	 * @param x  the scalar value for linear mapping
	 * @param a1 the start value of the first range
	 * @param a2 the end value of the first range
	 * @param b1 the start value of the last range
	 * @param b2 the end value of the last range
	 * 
	 * @return a mapped value
	 */
	public static double mapLinear(double x, double a1, double a2, double b1, double b2)
	{
		return b1 + ( x - a1 ) * ( b2 - b1 ) / ( a2 - a1 );
	}

	/**
	 * http://en.wikipedia.org/wiki/Smoothstep
	 * 
	 * @param x
	 * @param min
	 * @param max
	 * @return
	 */
	public static double smoothstep( double x, double min, double max ) 
	{
		if ( x <= min ) return 0.0;
		if ( x >= max ) return 1.0;

		x = ( x - min ) / ( max - min );

		return x * x * ( 3.0 - 2.0 * x );
	}

	public static double smootherstep( double x, double min, double max ) 
	{
		if ( x <= min ) return 0.0;
		if ( x >= max ) return 1.0;

		x = ( x - min ) / ( max - min );

		return x * x * x * ( x * ( x * 6.0 - 15.0 ) + 10.0 );
	}

	/**
	 * The method generates random double value in the range &#60;0, 1&#62; with 
	 * 16 bits of randomness (standard Math.random() creates repetitive 
	 * patterns when applied over larger space).
	 * 
	 * @return a random Float value.
	 */
	public static double random16()
	{

		return (65280.0 * Math.random() + 255.0 * Math.random()) / 65535.0;
	}

	/**
	 * The method generates random integer value in the interval &#60;low, high&#62;.  
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
	 * The method generates random double value in the range &#60;low, high&#62;.
	 * 
	 * @param low  the start value of the range.
	 * @param high the end value of the range
	 * 
	 * @return a random Float value.
	 */
	public static double randFloat(double low, double high)
	{
		return low + Math.random() * (high - low);
	}

	/**
	 * The method generates random double value in the interval 
	 * &#60;-range/2, range/2&#62;.
	 * 
	 * @param range the value used to build the interval
	 * 
	 * @return a random Float value.
	 */
	public static double randFloatSpread(double range)
	{
		return range * (0.5 - Math.random());
	}
	
	public static double degToRad ( double degrees ) 
	{
		return degrees * Math.PI / 180;
	}

	public static double radToDeg( double radians ) 
	{
		return radians * 180 / Math.PI;
	}
	
	/**
	 * The method checks if the value is power of two.
	 * 
	 * @param value the Integer value
	 */
	public static boolean isPowerOfTwo ( int value ) 
	{
		return ( value & ( value - 1 ) ) == 0;
	}
	 
	public static int getNextHighestPowerOfTwo( int x) 
	{
	    --x;
	    for (int i = 1; i < 32; i <<= 1) 
	    {
	        x = x | x >> i;
	    }
	    return x + 1;
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
	 * This method returns a sign of the scalar x.
	 * 
	 * @param x the scalar value for checking.
	 * 
	 * @return 1 or -1
	 */
	public static double sign(double x)
	{
		return (x < 0) ? -1.0 : ((x > 0) ? 1.0 : 0);
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
