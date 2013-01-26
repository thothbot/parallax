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

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

public class ColorTest extends GWTTestCase 
{
	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}
	
	@Test
	public void testColor() 
	{
		Color c = new Color();
	    assertEquals( c.getR(), 1.0);
	    assertEquals( c.getG(), 1.0);
	    assertEquals( c.getB(), 1.0);
	}

	@Test
	public void testGetHex() 
	{
		Color c = new Color(0xFF000);
	    int res = c.getHex();
	    assertEquals( res, 0xFF0000 );
	}

	@Test
	public void testSetHex() 
	{
		Color c = new Color();
	    c.setHex(0xFA8072);
	    assertEquals( c.getHex(), 0xFA8072 );
	}

	@Test
	public void testSetRGB() 
	{
		Color c = new Color();
	    c.setRGB(255, 2, 1);
	    assertEquals( c.getR(), 255);
	    assertEquals( c.getG(), 2);
	    assertEquals( c.getB(), 1);
	}

	@Test
	public void testConvertGammaToLinear() 
	{
		Color c = new Color();
	    c.setRGB(2, 4, 8);
	    c.convertGammaToLinear();
	    assertEquals( c.getR(), 4);
	    assertEquals( c.getG(), 16);
	    assertEquals( c.getB(), 64);
	}

	@Test
	public void testConvertLinearToGamma() 
	{
		Color c = new Color();
	    c.setRGB(4, 9, 16);
	    c.convertLinearToGamma();
	    assertEquals( c.getR(), 2);
	    assertEquals( c.getG(), 3);
	    assertEquals( c.getB(), 4);
	}
}
