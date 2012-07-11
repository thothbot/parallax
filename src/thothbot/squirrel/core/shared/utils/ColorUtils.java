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

import thothbot.squirrel.core.shared.core.Color3f;
import thothbot.squirrel.core.shared.core.Mathematics;

/**
 * This class implements some color related helpers methods.
 * 
 * This code is based on js-code written by alteredq 
 * http://alteredqualia.com/
 * 
 * @author thothbot
 *
 */
public class ColorUtils
{
	/**
	 * HSV presentation of color
	 */
	public static class HSV 
	{
		float hue;
		float saturation;
		float value;
		
		public HSV() 
		{
			this.hue = 0f;
			this.saturation = 0f;
			this.value = 0f;
		}
	}
	
	/**
	 * This method adjusts color by defined values of H, S, V
	 * 
	 * @param color the Color instance
	 * @param h the hue
	 * @param s the saturation
	 * @param v the value
	 */
	public static void adjustHSV(Color3f color, float h, float s, float v ) 
	{
		ColorUtils.HSV hsv = ColorUtils.rgbToHsv(color);
		
		hsv.hue = Mathematics.clamp( hsv.hue + h, 0, 1 );
		hsv.saturation = Mathematics.clamp( hsv.saturation + s, 0, 1 );
		hsv.value = Mathematics.clamp( hsv.value + v, 0, 1 );

		color.setHSV( hsv.hue, hsv.saturation, hsv.value );
	}
	
	/**
	 * This method will make new HSV instance and sets color value from
	 * color instance
	 * 
	 * Based on: 
	 * MochiKit implementation by Bob Ippolito
	 * 
	 * @param color the color instance
	 * @return the new HSV instance
	 */
	public static ColorUtils.HSV rgbToHsv(Color3f color) 
	{
		float r = color.getR();
		float g = color.getG();
		float b = color.getB();

		float max = Math.max( Math.max( r, g ), b );
		float min = Math.min( Math.min( r, g ), b );

		float hue;
		float saturation;
		float value = max;

		if ( min == max )	
		{
			hue = 0;
			saturation = 0;

		} 
		else 
		{
			float delta = ( max - min );
			saturation = delta / max;

			if ( r == max )
				hue = ( g - b ) / delta;

			else if ( g == max )
				hue = 2 + ( ( b - r ) / delta );

			else
				hue = 4 + ( ( r - g ) / delta );

			hue /= 6;

			if ( hue < 0 ) 
				hue += 1;

			if ( hue > 1 )
				hue -= 1;
		}

		ColorUtils.HSV hsv = new ColorUtils.HSV();

		hsv.hue = hue;
		hsv.saturation = saturation;
		hsv.value = value;

		return hsv;
	}
}
