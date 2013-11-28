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

import java.util.Arrays;
import java.util.List;

import com.google.gwt.junit.client.GWTTestCase;

public class Matrix4Test extends GWTTestCase 
{

	@Override
	public String getModuleName() {
		return "thothbot.parallax.core.Core";
	}

	public void testMatrix4()
	{
		Matrix4 a = new Matrix4();
		assertEquals( a.determinant(), 1.0);

		Matrix4 b = new Matrix4( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 );
		assertEquals( 0.0, b.getArray().get(0) );
		assertEquals( 4.0, b.getArray().get(1) );
		assertEquals( 8.0, b.getArray().get(2) );
		assertEquals( 12.0, b.getArray().get(3) );
		assertEquals( 1.0, b.getArray().get(4) );
		assertEquals( 5.0, b.getArray().get(5) );
		assertEquals( 9.0, b.getArray().get(6) );
		assertEquals( 13.0, b.getArray().get(7) );
		assertEquals( 2.0, b.getArray().get(8) );
		assertEquals( 6.0, b.getArray().get(9) );
		assertEquals( 10.0, b.getArray().get(10) );
		assertEquals( 14.0, b.getArray().get(11) );
		assertEquals( 3.0, b.getArray().get(12) );
		assertEquals( 7.0, b.getArray().get(13) );
		assertEquals( 11.0, b.getArray().get(14) );
		assertEquals( 15.0, b.getArray().get(15) );

		assertTrue( ! matrixEquals4( a, b ));
	}

	public void testSet()
	{
		Matrix4 b = new Matrix4();
		assertEquals( b.determinant(), 1.0);

		b.set( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 );
		assertEquals( 0.0, b.getArray().get(0) );
		assertEquals( 4.0, b.getArray().get(1) );
		assertEquals( 8.0, b.getArray().get(2) );
		assertEquals( 12.0, b.getArray().get(3) );
		assertEquals( 1.0, b.getArray().get(4) );
		assertEquals( 5.0, b.getArray().get(5) );
		assertEquals( 9.0, b.getArray().get(6) );
		assertEquals( 13.0, b.getArray().get(7) );
		assertEquals( 2.0, b.getArray().get(8) );
		assertEquals( 6.0, b.getArray().get(9) );
		assertEquals( 10.0, b.getArray().get(10) );
		assertEquals( 14.0, b.getArray().get(11) );
		assertEquals( 3.0, b.getArray().get(12) );
		assertEquals( 7.0, b.getArray().get(13) );
		assertEquals( 11.0, b.getArray().get(14) );
		assertEquals( 15.0, b.getArray().get(15) );		
	}

	public void testIdentity()
	{
		Matrix4 b = new Matrix4( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 );
		assertEquals( 0.0, b.getArray().get(0) );
		assertEquals( 4.0, b.getArray().get(1) );
		assertEquals( 8.0, b.getArray().get(2) );
		assertEquals( 12.0, b.getArray().get(3) );
		assertEquals( 1.0, b.getArray().get(4) );
		assertEquals( 5.0, b.getArray().get(5) );
		assertEquals( 9.0, b.getArray().get(6) );
		assertEquals( 13.0, b.getArray().get(7) );
		assertEquals( 2.0, b.getArray().get(8) );
		assertEquals( 6.0, b.getArray().get(9) );
		assertEquals( 10.0, b.getArray().get(10) );
		assertEquals( 14.0, b.getArray().get(11) );
		assertEquals( 3.0, b.getArray().get(12) );
		assertEquals( 7.0, b.getArray().get(13) );
		assertEquals( 11.0, b.getArray().get(14) );
		assertEquals( 15.0, b.getArray().get(15) );

		Matrix4 a = new Matrix4();
		assertTrue( ! matrixEquals4( a, b ));

		b.identity();
		assertTrue( matrixEquals4( a, b ));
	}

	public void testCopy()
	{
		Matrix4 a = new Matrix4( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 );
		Matrix4 b = new Matrix4().copy( a );

		assertTrue( matrixEquals4( a, b ));

		// ensure that it is a true copy
		a.getArray().set(0, 2);
		assertTrue( ! matrixEquals4( a, b ));
	}

	public void testMultiply()
	{
		Matrix4 b = new Matrix4( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 );
		assertEquals( 0.0, b.getArray().get(0) );
		assertEquals( 4.0, b.getArray().get(1) );
		assertEquals( 8.0, b.getArray().get(2) );
		assertEquals( 12.0, b.getArray().get(3) );
		assertEquals( 1.0, b.getArray().get(4) );
		assertEquals( 5.0, b.getArray().get(5) );
		assertEquals( 9.0, b.getArray().get(6) );
		assertEquals( 13.0, b.getArray().get(7) );
		assertEquals( 2.0, b.getArray().get(8) );
		assertEquals( 6.0, b.getArray().get(9) );
		assertEquals( 10.0, b.getArray().get(10) );
		assertEquals( 14.0, b.getArray().get(11) );
		assertEquals( 3.0, b.getArray().get(12) );
		assertEquals( 7.0, b.getArray().get(13) );
		assertEquals( 11.0, b.getArray().get(14) );
		assertEquals( 15.0, b.getArray().get(15) );
		

		b.multiply( 2 );
		assertEquals( 0.0 * 2, b.getArray().get(0) );
		assertEquals( 4.0 * 2, b.getArray().get(1) );
		assertEquals( 8.0 * 2, b.getArray().get(2) );
		assertEquals( 12.0 * 2, b.getArray().get(3) );
		assertEquals( 1.0 * 2, b.getArray().get(4) );
		assertEquals( 5.0 * 2, b.getArray().get(5) );
		assertEquals( 9.0 * 2, b.getArray().get(6) );
		assertEquals( 13.0 * 2, b.getArray().get(7) );
		assertEquals( 2.0 * 2, b.getArray().get(8) );
		assertEquals( 6.0 * 2, b.getArray().get(9) );
		assertEquals( 10.0 * 2, b.getArray().get(10) );
		assertEquals( 14.0 * 2, b.getArray().get(11) );
		assertEquals( 3.0 * 2, b.getArray().get(12) );
		assertEquals( 7.0 * 2, b.getArray().get(13) );
		assertEquals( 11.0 * 2, b.getArray().get(14) );
		assertEquals( 15.0 * 2, b.getArray().get(15) );		
	}

	public void testDeterminant()
	{
		Matrix4 a = new Matrix4();
		assertEquals( 1.0, a.determinant());

		a.getArray().set(0, 2);
		assertEquals( 2.0, a.determinant());

		a.getArray().set(0, 0);
		assertEquals( 0.0, a.determinant());

		// calculated via http://www.euclideanspace.com/maths/algebra/matrix/functions/determinant/fourD/index.htm
		a.set( 2, 3, 4, 5, -1, -21, -3, -4, 6, 7, 8, 10, -8, -9, -10, -12 );
		assertEquals( 76.0, a.determinant() );
	}

	public void testTranspose()
	{
		Matrix4 a = new Matrix4();
		Matrix4 b = a.clone().transpose();
		assertTrue( matrixEquals4( a, b ));

		b = new Matrix4( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 );
		Matrix4 c = b.clone().transpose();
		assertTrue( ! matrixEquals4( b, c )); 
		c.transpose();
		assertTrue( matrixEquals4( b, c )); 
	}

	public void testGetInverse()
	{
		Matrix4 identity = new Matrix4();

		Matrix4 a = new Matrix4();
		Matrix4 b = new Matrix4( 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 );
		Matrix4 c = new Matrix4( 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 );

		assertTrue( ! matrixEquals4( a, b ));
		b.getInverse( a );
		assertTrue( matrixEquals4( b, new Matrix4() ));

		List<Matrix4> testMatrices = Arrays.asList(
			new Matrix4().makeRotationX( 0.3 ),
			new Matrix4().makeRotationX( -0.3 ),
			new Matrix4().makeRotationY( 0.3 ),
			new Matrix4().makeRotationY( -0.3 ),
			new Matrix4().makeRotationZ( 0.3 ),
			new Matrix4().makeRotationZ( -0.3 ),
			new Matrix4().makeScale( 1, 2, 3 ),
			new Matrix4().makeScale( 1.0/8, 1.0/2, 1.0/3 ),
			new Matrix4().makeFrustum( -1, 1, -1, 1, 1, 1000 ),
			new Matrix4().makeFrustum( -16, 16, -9, 9, 0.1, 10000 ),
			new Matrix4().makeTranslation( 1, 2, 3 )
		);

		for( int i = 0, il = testMatrices.size(); i < il; i ++ ) 
		{
			Matrix4 m = testMatrices.get(i);

			Matrix4 mInverse = new Matrix4().getInverse( m );

			// the determinant of the inverse should be the reciprocal
			assertTrue( Math.abs( m.determinant() * mInverse.determinant() - 1  ) < 0.0001);

			Matrix4 mProduct = new Matrix4().multiply( m, mInverse );

			// the determinant of the identity matrix is 1
			assertTrue( Math.abs( mProduct.determinant() - 1 ) < 0.0001);
			assertTrue( matrixEquals4( mProduct, identity ));
		}
	}

	public void testClone()
	{
		Matrix4 a = new Matrix4( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 );
		Matrix4 b = a.clone();

		assertTrue( matrixEquals4( a, b ));

		// ensure that it is a true copy
		a.getArray().set(0, 2);
		assertTrue( ! matrixEquals4( a, b ));
	}
	
	private boolean matrixEquals4( Matrix4 a, Matrix4 b) 
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
