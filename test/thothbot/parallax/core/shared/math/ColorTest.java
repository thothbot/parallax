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
	    assertEquals( 1.0, c.getR() );
	    assertEquals( 1.0, c.getG() );
	    assertEquals( 1.0, c.getB() );
	}

	@Test
	public void testGetHex() 
	{
		Color c = new Color(0xFF000);
	    int res = c.getHex();
	    assertEquals( 0xFF0000, res );
	}

	@Test
	public void testSetHex() 
	{
		Color c = new Color();
	    c.setHex(0xFA8072);
	    assertEquals( 0xFA8072, c.getHex() );
	}

	@Test
	public void testSetRGB() 
	{
		Color c = new Color();
	    c.setRGB(255, 2, 1);
	    assertEquals( 255, c.getR() );
	    assertEquals( 2, c.getG() );
	    assertEquals( 1, c.getB() );
	}

	@Test
	public void testConvertGammaToLinear() 
	{
		Color c = new Color();
	    c.setRGB(2, 4, 8);
	    c.convertGammaToLinear();
	    assertEquals( 4, c.getR() );
	    assertEquals( 16, c.getG() );
	    assertEquals( 64, c.getB() );
	}

	@Test
	public void testConvertLinearToGamma() 
	{
		Color c = new Color();
	    c.setRGB(4, 9, 16);
	    c.convertLinearToGamma();
	    assertEquals( 2, c.getR() );
	    assertEquals( 3, c.getG() );
	    assertEquals( 4, c.getB() );
	}
}
