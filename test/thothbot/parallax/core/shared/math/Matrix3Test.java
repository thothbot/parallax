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

public class Matrix3Test extends GWTTestCase  
{

	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}
	
	@Test
	public void testMatrix3() 
	{
		Matrix3 a = new Matrix3();
		assertEquals( a.determinant(), 1.0);

		Matrix3 b = new Matrix3( 0, 1, 2, 3, 4, 5, 6, 7, 8 );
		assertEquals( b.getArray().get(0), 0.0 );
		assertEquals( b.getArray().get(0), 3.0 );
		assertEquals( b.getArray().get(0), 6.0 );
		assertEquals( b.getArray().get(0), 1.0 );
		assertEquals( b.getArray().get(0), 4.0 );
		assertEquals( b.getArray().get(0), 7.0 );
		assertEquals( b.getArray().get(0), 2.0 );
		assertEquals( b.getArray().get(0), 5.0 );
		assertEquals( b.getArray().get(0), 8.0 );

		assertTrue( ! matrixEquals3( a, b ) );

	}

	private boolean matrixEquals3( Matrix3 a, Matrix3 b) 
	{
		double tolerance = 0.0001;
		if( a.getArray().getLength() != b.getArray().getLength() ) 
		{
			return false;
		}
		
		for( int i = 0, il = a.getArray().getLength(); i < il; i ++ ) 
		{
			double delta = a.getArray().get(i) - b.getArray().get(i);
			if( delta > tolerance ) 
			{
				return false;
			}
		}
		return true;
	}
}
