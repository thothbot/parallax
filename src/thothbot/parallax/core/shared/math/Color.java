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
 * The Color class is used encapsulate colors in the default RGB color space.
 * This class doesn't work with alpha value. 
 * Each RGB value stored as values in range [0.0-1.0]. Where value of 1.0 is 255 means 
 * that the color is completely shown and an color value of 0 or 0.0 means that 
 * the color is completely hidden. 
 * 
 * @author thothbot
 */
public final class Color
{
	/**
	 * HSL presentation of color
	 */
	public static class HSL 
	{
		public double hue;
		public double saturation;
		public double lightness;
		
		public HSL() 
		{
			this.hue = 0.0;
			this.saturation = 0.0;
			this.lightness = 0.0;
		}
	}
	
	/**
	 * The R-component of the color. &#60;0.0-1.0&#62;
	 */
	private double r;

	/**
	 * The G-component of the color. &#60;0.0-1.0&#62;
	 */
	private double g;

	/**
	 * The B-component of the color. &#60;0.0-1.0&#62;
	 */
	private double b;

	/**
	 * This default constructor will create color where R=1.0, G=1.0, B=1.0
	 * ie (255, 255, 255) or 0xFFFFFF in HEX.
	 */
	public Color() 
	{		
		setHex(0xFFFFFF);
	}

	/**
	 * This constructor will create Color instance by defined HEX value.
	 * For example 0xFFFFFF will create a color in RGB(255, 255, 255), which
	 * means completely white. 
	 * 
	 * @param hex Color in HEX format
	 */
	public Color(int hex) 
	{
		setHex(hex);
	}

	/**
	 * get R-component of the color. 
	 * 
	 * @return a value in range &#60;0.0, 1.0&#62;
	 */
	public double getR()
	{
		return r;
	}

	/**
	 * get G-component of the color. 
	 * 
	 * @return a value in range &#60;0.0, 1.0&#62; 
	 */
	public double getG()
	{
		return g;
	}
	
	/**
	 * get B-component of the color. 
	 * 
	 * @return a value in range &#60;0.0, 1.0&#62;
	 */
	public double getB()
	{
		return b;
	}
	
	/**
	 * Setting R-component of the color.
	 * 
	 * @param r the value in range &#60;0.0, 1.0&#62; 
	 */
	public void setR(double r)
	{
		this.r = r;
	}

	/**
	 * Setting G-component of the color.
	 * 
	 * @param g the value in range &#60;0.0, 1.0&#62; 
	 */
	public void setG(double g)
	{
		this.g = g;
	}

	/**
	 * Setting B-component of the color.
	 * 
	 * @param b the value in range &#60;0.0, 1.0&#62; 
	 */
	public void setB(double b)
	{
		this.b = b;
	}
	
	/**
	 * Setting color in HEX format. For example 0xFFFFFF will create 
	 * a color in RGB(255, 255, 255), which means completely white.
	 * 
	 * @param hex Color in HEX format
	 */	
	public Color setHex( int hex ) 
	{
		hex = (~~hex) & 0xffffff;

		this.r = ( hex >> 16 & 255 ) / 255.0;
		this.g = ( hex >> 8 & 255 ) / 255.0;
		this.b = ( hex & 255 ) / 255.0;

		return this;
	}
	
	/**
	 * Setting color in RGB mode. Each of R, G, B should be in 
	 * range <0.0, 1.0>. 
	 * 
	 * @param r the R-component of Color.
	 * @param g the G-component of Color.
	 * @param b the B-component of Color.
	 * 
	 * @return a current color
	 */
	public Color setRGB( double r, double g, double b ) 
	{
		this.r = r;
		this.g = g;
		this.b = b;

		return this;
	}

	/**
	 * Setting color based on HSV color model. Each input values H, S, V
	 * should be in range <0.0, 1.0>.
	 * 
	 * This method based on MochiKit implementation by Bob Ippolito
	 * 
	 * @param h the hue
	 * @param s the saturation
	 * @param v the value
	 * 
	 * @return a current color
	 */
//	public Color setHSV( double h, double s, double v ) 
//	{
//		return this.setHSL(h,s*v/((h=(2-s)*v)<1?h:2-h),h/2); // https://gist.github.com/xpansive/1337890
//	}

	/**
	 * h,s,l ranges are in <0.0 - 1.0>
	 * 
	 * @param h Hue
	 * @param s Saturation
	 * @param l Lightness (Intensity)
	 * @return
	 */
	public Color setHSL( double h, double s, double l ) 
	{
		// h,s,l ranges are in 0.0 - 1.0

		if ( s == 0 ) {

			this.r = this.g = this.b = l;

		} else {

			double p = l <= 0.5 ? l * ( 1.0 + s ) : l + s - ( l * s );
			double q = ( 2.0 * l ) - p;

			this.r = hue2rgb( q, p, h + 1.0 / 3.0 );
			this.g = hue2rgb( q, p, h );
			this.b = hue2rgb( q, p, h - 1.0 / 3.0 );

		}

		return this;

	}
	
	/**
	 * Set value of the color from another color.
	 * 
	 * @param color the other color
	 */
	public Color copy( Color color ) 
	{
		this.r = color.r;
		this.g = color.g;
		this.b = color.b;

		return this;
	}

	/**
	 * Set value of color from gamma.
	 * 
	 * @param color the gamma
	 * 
	 * @return the current color
	 */
	public Color copyGammaToLinear ( Color color ) 
	{
		this.r = color.r * color.r;
		this.g = color.g * color.g;
		this.b = color.b * color.b;

		return this;
	}

	/**
	 * Set value of the gamma from color.
	 * 
	 * @param color the color
	 * 
	 * @return a gamma
	 */
	public Color copyLinearToGamma ( Color color ) 
	{
		this.r = Math.sqrt( color.r );
		this.g = Math.sqrt( color.g );
		this.b = Math.sqrt( color.b );

		return this;
	}

	/**
	 * This method will convert gamma to color
	 * 
	 * @return a current color
	 */
	public Color convertGammaToLinear() 
	{
		double r = this.r, g = this.g, b = this.b;

		this.r = r * r;
		this.g = g * g;
		this.b = b * b;

		return this;
	}

	/**
	 * This method will convert color to gamma
	 * 
	 * @return a current gamma
	 */
	public Color convertLinearToGamma() 
	{
		this.r = Math.sqrt( this.r );
		this.g = Math.sqrt( this.g );
		this.b = Math.sqrt( this.b );

		return this;
	}

	/**
	 * Gets HEX value.
	 */
	public int getHex() 
	{
		return ~~((int) Math.floor(this.r * 255)) << 16
				^ ~~((int) Math.floor(this.g * 255)) << 8 
				^ ~~((int) Math.floor(this.b * 255)) << 0;
		
//		return ( this.r * 255 ) << 16 ^ ( this.g * 255 ) << 8 ^ ( this.b * 255 ) << 0;
	}

	public String getHexString() 
	{
		String hexString = Integer.toHexString(this.getHex()).toUpperCase();
		while(hexString.length() < 6)
		    hexString = "0" + hexString;
		return hexString;
	}

	public Color.HSL getHSL() 
	{
		// h,s,l ranges are in 0.0 - 1.0

		double r = this.r, g = this.g, b = this.b;

		double max = Math.max(Math.max( r, g), b );
		double min = Math.min(Math.min( r, g), b );

		double hue = 0, saturation = 0;
		double lightness = ( min + max ) / 2.0;

		Color.HSL hsl = new Color.HSL();
		
		if ( min == max ) 
		{
			hue = 0;
			saturation = 0;
		} 
		else 
		{
			double delta = max - min;

			saturation = lightness <= 0.5 ? delta / ( max + min ) : delta / ( 2.0 - max - min );

			if( max == r) hue = ( g - b ) / delta + ( g < b ? 6 : 0 );
			else if(max == g) hue = ( b - r ) / delta + 2.0;
			else if(max == b) hue = ( r - g ) / delta + 4.0;
			
			hue /= 6.0;
		}

		hsl.hue = hue;
		hsl.saturation = saturation;
		hsl.lightness = lightness;

		return hsl;
	}

	public String getStyle() 
	{
		return "rgb(" + ( ( (int)(this.r * 255) ) | 0 ) 
				+ "," + ( ( (int)(this.g * 255) ) | 0 ) 
				+ "," + ( ( (int)(this.b * 255) | 0) ) + ")";
	}

	public Color offsetHSL( double h, double s, double l ) 
	{
		Color.HSL hsl = this.getHSL();

		hsl.hue += h; hsl.saturation += s; hsl.lightness += l;

		this.setHSL( hsl.hue, hsl.saturation, hsl.lightness );

		return this;
	}

	public Color add( Color color ) 
	{

		this.r += color.r;
		this.g += color.g;
		this.b += color.b;

		return this;
	}

	public Color add( Color color1, Color color2 ) 
	{
		this.r = color1.r + color2.r;
		this.g = color1.g + color2.g;
		this.b = color1.b + color2.b;

		return this;
	}

	public Color add( int s ) 
	{
		this.r += s;
		this.g += s;
		this.b += s;

		return this;
	}

	public Color multiply( Color color ) 
	{
		this.r *= color.r;
		this.g *= color.g;
		this.b *= color.b;

		return this;
	}

	public Color multiply( double s ) 
	{

		this.r *= s;
		this.g *= s;
		this.b *= s;

		return this;
	}

	/**
	 * Linearly interpolates between the current color and input color.
	 * 
	 * @param color the input color
	 * @param alpha the alpha value in range <0.0, 1.0>
	 */
	public Color lerp( Color color, double alpha ) 
	{
		this.r += ( color.r - this.r ) * alpha;
		this.g += ( color.g - this.g ) * alpha;
		this.b += ( color.b - this.b ) * alpha;

		return this;
	}
	
	public boolean equals( Color c ) {

		return ( c.r == this.r ) && ( c.g == this.g ) && ( c.b == this.b );

	}

	/**
	 * Clone the current color class.
	 * (color.clone() != color).
	 * 
	 * @return a new color instance, based on the current color class
	 */
	public Color clone() 
	{
		return new Color().setRGB( this.r, this.g, this.b );
	}
	
	
	/**
	 * get Color class description by multiplying each value by 255. 
	 * Please not this is not real used values. This is just for readability. 
	 */
	public String toString()
	{
		return "{r:" + this.r + ", g:" + this.g + ", b:" + this.b + "}";
	}

	private double hue2rgb( double p, double q, double t ) 
	{
		if ( t < 0 ) t += 1.0;
		if ( t > 1 ) t -= 1.0;
		if ( t < 1 / 6.0 ) return p + ( q - p ) * 6.0 * t;
		if ( t < 1 / 2.0 ) return q;
		if ( t < 2 / 3.0 ) return p + ( q - p ) * 6.0 * ( 2.0 / 3.0 - t );
		
		return p;
	}
}
