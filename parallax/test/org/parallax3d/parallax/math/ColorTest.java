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

package org.parallax3d.parallax.math;

import org.junit.Test;
import org.parallax3d.parallax.system.ThreejsTest;
import static org.junit.Assert.*;

@ThreejsTest("Color")
public class ColorTest {
	@Test
	public void testConstructor() {
		Color c = new Color();
		assertTrue( c.r == 1. );
		assertTrue( c.g == 1. );
		assertTrue( c.b == 1. );

	}

	@Test
	public void testRgb_constructor() {
		Color c = new Color(1, 1, 1);
		assertTrue( c.r == 1 );
		assertTrue( c.g == 1 );
		assertTrue( c.b == 1 );

	}

	@Test
	public void testCopyHex() {
		Color c = new Color();
		Color c2 = new Color(16121850);
		c.copy(c2);
		assertTrue( c.getHex() == c2.getHex() );

	}

	@Test
	public void testCopyColorString() {
		Color c = new Color();
		Color c2 = Colors.IVORY.getColor();
		c.copy(c2);
		assertTrue( c.getHex() == c2.getHex() );

	}

	@Test
	public void testSetRGB() {
		Color c = new Color();
		c.setRGB(1, 0.2, 0.1);
		assertTrue( c.r == 1 );
		assertTrue( c.g == 0.2 );
		assertTrue( c.b == 0.1 );

	}

	@Test
	public void testCopyGammaToLinear() {
		Color c = new Color();
		Color c2 = new Color();
		c2.setRGB(0.3, 0.5, 0.9);
		c.copyGammaToLinear(c2);
		assertTrue( c.r == 0.09 );
		assertTrue( c.g == 0.25 );
		assertTrue( c.b == 0.81 );

	}

	@Test
	public void testCopyLinearToGamma() {
		Color c = new Color();
		Color c2 = new Color();
		c2.setRGB(0.09, 0.25, 0.81);
		c.copyLinearToGamma(c2);
		assertTrue( c.r == 0.3 );
		assertTrue( c.g == 0.5 );
		assertTrue( c.b == 0.9 );

	}

	@Test
	public void testConvertGammaToLinear() {
		Color c = new Color();
		c.setRGB(0.3, 0.5, 0.9);
		c.convertGammaToLinear();
		assertTrue( c.r == 0.09 );
		assertTrue( c.g == 0.25 );
		assertTrue( c.b == 0.81 );

	}

	@Test
	public void testConvertLinearToGamma() {
		Color c = new Color();
		c.setRGB(4, 9, 16);
		c.convertLinearToGamma();
		assertTrue( c.r == 2 );
		assertTrue( c.g == 3 );
		assertTrue( c.b == 4 );

	}

	@Test
	public void testSetWithNum() {
		Color c = new Color();
		c.set(16711680);
		assertTrue( c.r == 1 );
		assertTrue( c.g == 0 );
		assertTrue( c.b == 0 );

	}

	@Test
	public void testSetWithString() {
		Color c = new Color();
		c.set( Colors.SILVER.getValue() );
		assertTrue( c.getHex() == 12632256 );

	}

	@Test
	public void testClone() {
		Color c = new Color( Colors.TEAL.getValue() );
		Color c2 = c.clone();
		assertTrue( c2.getHex() == 32896 );

	}

	@Test
	public void testLerp() {
		Color c = new Color();
		Color c2 = new Color();
		c.setRGB(0, 0, 0);
		c.lerp(c2, 0.2);
		assertTrue( c.r == 0.2 );
		assertTrue( c.g == 0.2 );
		assertTrue( c.b == 0.2 );

	}

	@Test
	public void testSetStyleRGBRed() {
		Color c = new Color();
		c.setStyle("rgb(255,0,0)");
		assertTrue( c.r == 1 );
		assertTrue( c.g == 0 );
		assertTrue( c.b == 0 );

	}

	@Test
	public void testSetStyleRGBARed() {
		Color c = new Color();
		c.setStyle("rgba(255,0,0,0.5)");
		assertTrue( c.r == 1 );
		assertTrue( c.g == 0 );
		assertTrue( c.b == 0 );

	}

	@Test
	public void testSetStyleRGBRedWithSpaces() {
		Color c = new Color();
		c.setStyle("rgb( 255 , 0,   0 )");
		assertTrue( c.r == 1 );
		assertTrue( c.g == 0 );
		assertTrue( c.b == 0 );

	}

	@Test
	public void testSetStyleRGBARedWithSpaces() {
		Color c = new Color();
		c.setStyle("rgba( 255,  0,  0  , 1 )");
		assertTrue( c.r == 1 );
		assertTrue( c.g == 0 );
		assertTrue( c.b == 0 );

	}

	@Test
	public void testSetStyleRGBPercent() {
		Color c = new Color();
		c.setStyle("rgb(100%,50%,10%)");
		assertTrue( c.r == 1 );
		assertTrue( c.g == 0.5 );
		assertTrue( c.b == 0.1 );

	}

	@Test
	public void testSetStyleRGBAPercent() {
		Color c = new Color();
		c.setStyle("rgba(100%,50%,10%, 0.5)");
		assertTrue( c.r == 1 );
		assertTrue( c.g == 0.5 );
		assertTrue( c.b == 0.1 );

	}

	@Test
	public void testSetStyleRGBPercentWithSpaces() {
		Color c = new Color();
		c.setStyle("rgb( 100% ,50%  , 10% )");
		assertTrue( c.r == 1 );
		assertTrue( c.g == 0.5 );
		assertTrue( c.b == 0.1 );

	}

	@Test
	public void testSetStyleRGBAPercentWithSpaces() {
		Color c = new Color();
		c.setStyle("rgba( 100% ,50%  ,  10%, 0.5 )");
		assertTrue( c.r == 1 );
		assertTrue( c.g == 0.5 );
		assertTrue( c.b == 0.1 );

	}

	@Test
	public void testSetStyleHSLRed() {
		Color c = new Color();
		c.setStyle("hsl(360,100%,50%)");
		assertTrue( c.r == 1 );
		assertTrue( c.g == 0 );
		assertTrue( c.b == 0 );

	}

	@Test
	public void testSetStyleHSLARed() {
		Color c = new Color();
		c.setStyle("hsla(360,100%,50%,0.5)");
		assertTrue( c.r == 1 );
		assertTrue( c.g == 0 );
		assertTrue( c.b == 0 );

	}

	@Test
	public void testSetStyleHSLRedWithSpaces() {
		Color c = new Color();
		c.setStyle("hsl(360,  100% , 50% )");
		assertTrue( c.r == 1 );
		assertTrue( c.g == 0 );
		assertTrue( c.b == 0 );

	}

	@Test
	public void testSetStyleHSLARedWithSpaces() {
		Color c = new Color();
		c.setStyle("hsla( 360,  100% , 50%,  0.5 )");
		assertTrue( c.r == 1 );
		assertTrue( c.g == 0 );
		assertTrue( c.b == 0 );

	}

	@Test
	public void testSetStyleHexSkyBlue() {
		Color c = new Color();
		c.setStyle("#87CEEB");
		assertTrue( c.getHex() == 8900331 );

	}

	@Test
	public void testSetStyleHexSkyBlueMixed() {
		Color c = new Color();
		c.setStyle("#87cEeB");
		assertTrue( c.getHex() == 8900331 );

	}

	@Test
	public void testSetStyleHex2Olive() {
		Color c = new Color();
		c.setStyle("#F00");
		assertTrue( c.getHex() == 16711680 );

	}

	@Test
	public void testSetStyleHex2OliveMixed() {
		Color c = new Color();
		c.setStyle("#f00");
		assertTrue( c.getHex() == 16711680 );

	}

	@Test
	public void testSetStyleColorName() {
		Color c = new Color();
		c.setStyle("powderblue");
		assertTrue( c.getHex() == 11591910 );

	}

	@Test
	public void testGetHex() {
		Color c = Colors.RED.getColor();
		int res = c.getHex();
		assertTrue( res == 16711680 );

	}

	@Test
	public void testSetHex() {
		Color c = new Color();
		c.setHex(16416882);
		assertTrue( c.getHex() == 16416882 );

	}

	@Test
	public void testGetHexString() {
		Color c = Colors.TOMATO.getColor();
		String res = c.getHexString();
		assertTrue( res.equals( "ff6347" ) );

	}

	@Test
	public void testGetStyle() {
		Color c = Colors.PLUM.getColor();
		String res = c.getStyle();
		assertTrue( res.equals( "rgb(221,160,221)" ) );

	}

	@Test
	public void testGetHSL() {
		Color c = new Color(8454143);
		Color.HSL hsl = c.getHSL();
		assertTrue( "hue: " + hsl.hue, hsl.hue == 0.5 );
		assertTrue( "saturation: " + hsl.saturation, hsl.saturation == 1.0 );
		assertTrue( "lightness: " + hsl.lightness, Math.round(hsl.lightness * 100) / 100. == 0.75 );

	}

	@Test
	public void testSetHSL() {
		Color c = new Color();
		c.setHSL(0.75, 1.0, 0.25);
		Color.HSL hsl = c.getHSL();
		assertTrue( hsl.hue == 0.75 );
		assertTrue( hsl.saturation == 1.0 );
		assertTrue( hsl.lightness == 0.25 );

	}

}
