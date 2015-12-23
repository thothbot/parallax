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

package org.parallax3d.parallax.math;

import org.junit.Test;
import static org.junit.Assert.*;

public class ColorTest
{
	private static double DELTA = 0.0;

	@Test
	public void testColor()
	{
		Color c = new Color();
	    assertEquals( 1.0, c.getR(), DELTA );
	    assertEquals( 1.0, c.getG(), DELTA );
	    assertEquals( 1.0, c.getB(), DELTA );
	}

	@Test
	public void testCopyHex()
	{
		Color c = new Color();
	    Color c2 = new Color(0xF5FFFA);
	    c.copy(c2);
	    assertEquals(c.getHex(), c2.getHex());
	}

	@Test
	public void testGetHex()
	{
		Color c = new Color(0xFF0000);
	    assertEquals(0xFF0000, c.getHex() );
	}

	@Test
	public void testSetRGB()
	{
		Color c = new Color();
	    c.setRGB(1.0, 0.25, 0.1);
	    assertEquals( 1.0, c.getR(), DELTA );
	    assertEquals( 0.25, c.getG(), DELTA );
	    assertEquals( 0.1, c.getB(), DELTA );
	}

	@Test
	public void testSetHex()
	{
		Color c = new Color();
	    c.setHex(0xFA8072);
	    assertEquals( 0xFA8072, c.getHex() );
	}

	@Test
	public void testCopyGammaToLinear()
	{
		Color c = new Color();
		Color c2 = new Color();
	    c2.setRGB(2, 4, 8);
	    c.copyGammaToLinear(c2);
	    assertEquals( 4.0, c.getR(), DELTA );
	    assertEquals( 16.0, c.getG(), DELTA );
	    assertEquals( 64.0, c.getB(), DELTA );
	}

	@Test
	public void testCopyLinearToGamma()
	{
		Color c = new Color();
		Color c2 = new Color();
	    c2.setRGB(4, 9, 16);
	    c.copyLinearToGamma(c2);
	    assertEquals( 2.0, c.getR(), DELTA );
	    assertEquals( 3.0, c.getG(), DELTA );
	    assertEquals( 4.0, c.getB(), DELTA );
	}

	@Test
	public void testConvertGammaToLinear()
	{
		Color c = new Color();
	    c.setRGB(2, 4, 8);
	    c.convertGammaToLinear();
	    assertEquals( 4.0, c.getR(), DELTA );
	    assertEquals( 16.0, c.getG(), DELTA );
	    assertEquals( 64.0, c.getB(), DELTA );
	}

	@Test
	public void testConvertLinearToGamma()
	{
		Color c = new Color();
	    c.setRGB(4, 9, 16);
	    c.convertLinearToGamma();
	    assertEquals( 2.0, c.getR(), DELTA );
	    assertEquals( 3.0, c.getG(), DELTA );
	    assertEquals( 4.0, c.getB(), DELTA );
	}

	@Test
	public void testSetWithNum()
	{
		Color c = new Color();
	    c.setHex(0xFF0000);
	    assertEquals( 1.0, c.getR(), DELTA );
	    assertEquals( 0.0, c.getG(), DELTA );
	    assertEquals( 0.0, c.getB(), DELTA );
	}

	@Test
	public void testClone()
	{
	    Color c = new Color(0xFF5544);
	    Color c2 = c.clone();
	    assertEquals(0xFF5544, c2.getHex());
	}

	@Test
	public void testLerp()
	{
	    Color c = new Color();
	    Color c2 = new Color();
	    c.setRGB(0, 0, 0);
	    c.lerp(c2, 2);
	    assertEquals( 2.0, c.getR(), DELTA );
	    assertEquals( 2.0, c.getG(), DELTA );
	    assertEquals( 2.0, c.getB(), DELTA );
	}
}
