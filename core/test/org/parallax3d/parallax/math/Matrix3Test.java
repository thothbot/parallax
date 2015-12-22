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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class Matrix3Test
{


	@Test
	public void testMatrix3()
	{
		Matrix3 a = new Matrix3();
		assertEquals( a.determinant(), 1.0);

		Matrix3 b = new Matrix3( 0, 1, 2, 3, 4, 5, 6, 7, 8 );
		assertEquals( 0.0, b.getArray()[0] );
		assertEquals( 3.0, b.getArray()[1] );
		assertEquals( 6.0, b.getArray()[2] );
		assertEquals( 1.0, b.getArray()[3] );
		assertEquals( 4.0, b.getArray()[4] );
		assertEquals( 7.0, b.getArray()[5] );
		assertEquals( 2.0, b.getArray()[6] );
		assertEquals( 5.0, b.getArray()[7] );
		assertEquals( 8.0, b.getArray()[8] );

		assertTrue( ! matrixEquals3( a, b ) );

	}

	@Test
	public void testCopy()
	{
		Matrix3 a = new Matrix3( 0, 1, 2, 3, 4, 5, 6, 7, 8 );
		Matrix3 b = new Matrix3().copy( a );

		assertTrue( matrixEquals3( a, b ));

		// ensure that it is a true copy
		a.getArray()[0]= 2;
		assertTrue( ! matrixEquals3( a, b ));
	}

	@Test
	public void testSet()
	{
		Matrix3 b = new Matrix3();
		assertEquals( b.determinant(), 1.0);

		b.set( 0, 1, 2, 3, 4, 5, 6, 7, 8 );
		assertEquals( 0.0, b.getArray()[0] );
		assertEquals( 3.0, b.getArray()[1] );
		assertEquals( 6.0, b.getArray()[2] );
		assertEquals( 1.0, b.getArray()[3] );
		assertEquals( 4.0, b.getArray()[4] );
		assertEquals( 7.0, b.getArray()[5] );
		assertEquals( 2.0, b.getArray()[6] );
		assertEquals( 5.0, b.getArray()[7] );
		assertEquals( 8.0, b.getArray()[8] );
	}

	@Test
	public void testIdentity()
	{
		Matrix3 b = new Matrix3( 0, 1, 2, 3, 4, 5, 6, 7, 8 );
		assertEquals( 0.0, b.getArray()[0] );
		assertEquals( 3.0, b.getArray()[1] );
		assertEquals( 6.0, b.getArray()[2] );
		assertEquals( 1.0, b.getArray()[3] );
		assertEquals( 4.0, b.getArray()[4] );
		assertEquals( 7.0, b.getArray()[5] );
		assertEquals( 2.0, b.getArray()[6] );
		assertEquals( 5.0, b.getArray()[7] );
		assertEquals( 8.0, b.getArray()[8] );
		
		Matrix3 a = new Matrix3();
		assertTrue( ! matrixEquals3( a, b ));

		b.identity();
		assertTrue( matrixEquals3( a, b ));
	}

	@Test
	public void testMultiplyScalar()
	{
		Matrix3 b = new Matrix3( 0, 1, 2, 3, 4, 5, 6, 7, 8 );

		b.multiply( 2 );
		assertEquals( 0.0 * 2, b.getArray()[0] );
		assertEquals( 3.0 * 2, b.getArray()[1] );
		assertEquals( 6.0 * 2, b.getArray()[2] );
		assertEquals( 1.0 * 2, b.getArray()[3] );
		assertEquals( 4.0 * 2, b.getArray()[4] );
		assertEquals( 7.0 * 2, b.getArray()[5] );
		assertEquals( 2.0 * 2, b.getArray()[6] );
		assertEquals( 5.0 * 2, b.getArray()[7] );
		assertEquals( 8.0 * 2, b.getArray()[8] );
	};

	@Test
	public void testDeterminant()
	{
		Matrix3 a = new Matrix3();
		assertEquals( 1.0, a.determinant());

		a.getArray()[0] = 2;
		assertEquals( 2.0, a.determinant());

		a.getArray()[0] = 0;
		assertEquals( 0.0, a.determinant());

		// calculated via http://www.euclideanspace.com/maths/algebra/matrix/functions/determinant/threeD/index.htm
		a.set( 2, 3, 4, 5, 13, 7, 8, 9, 11 );
		assertEquals( -73.0, a.determinant());
	}

	@Test
	public void testGetInverse()
	{
		Matrix3 identity = new Matrix3();
		Matrix4 a = new Matrix4();
		Matrix3 b = new Matrix3( 0, 0, 0, 0, 0, 0, 0, 0, 0 );
		Matrix3 c = new Matrix3( 0, 0, 0, 0, 0, 0, 0, 0, 0 );

//		assertTrue( ! matrixEquals3( a, b ));
		b.getInverse( a );
		assertTrue( matrixEquals3( b, new Matrix3() ));

		List<Matrix4> testMatrices = Arrays.asList(
			new Matrix4().makeRotationX( 0.3f ),
			new Matrix4().makeRotationX( -0.3f ),
			new Matrix4().makeRotationY( 0.3f ),
			new Matrix4().makeRotationY( -0.3f ),
			new Matrix4().makeRotationZ( 0.3f ),
			new Matrix4().makeRotationZ( -0.3f ),
			new Matrix4().makeScale( 1, 2, 3 ),
			new Matrix4().makeScale( 1.0f/8, 1.0f/2, 1.0f/3 )
		);

		for( int i = 0, il = testMatrices.size(); i < il; i ++ ) 
		{
			Matrix4 m = testMatrices.get(i);
			Matrix3 mInverse3 = new Matrix3().getInverse( m );

			Matrix4 mInverse = toMatrix4( mInverse3 );

			// the determinant of the inverse should be the reciprocal
			assertTrue( Math.abs( m.determinant() * mInverse3.determinant() - 1 ) < 0.0001);
			assertTrue( Math.abs( m.determinant() * mInverse.determinant() - 1 ) < 0.0001);

			Matrix4 mProduct = new Matrix4().multiply( m, mInverse );
			assertTrue( Math.abs( mProduct.determinant() - 1 ) < 0.0001);
//			assertTrue( matrixEquals3( mProduct, identity ));
		}
	}

	@Test
	public void testTranspose()
	{
		Matrix3 a = new Matrix3();
		Matrix3 b = a.clone().transpose();
		assertTrue( matrixEquals3( a, b ));

		b = new Matrix3( 0, 1, 2, 3, 4, 5, 6, 7, 8 );
		Matrix3 c = b.clone().transpose();
		assertTrue( ! matrixEquals3( b, c )); 
		c.transpose();
		assertTrue( matrixEquals3( b, c )); 
	}

	@Test
	public void testClone()
	{
		Matrix3 a = new Matrix3( 0, 1, 2, 3, 4, 5, 6, 7, 8 );
		Matrix3 b = a.clone();

		assertTrue( matrixEquals3( a, b ));

		// ensure that it is a true copy
		a.getArray()[0] = 2;
		assertTrue( ! matrixEquals3( a, b ));
	}

	@Test
	private boolean matrixEquals3( Matrix3 a, Matrix3 b) 
	{
		double tolerance = 0.0001;
		if( a.getArray().length != b.getArray().length ) 
		{
			return false;
		}
		
		for( int i = 0, il = a.getArray().length; i < il; i ++ ) 
		{
			double delta = a.getArray()[i] - b.getArray()[i];
			if( delta > tolerance ) 
			{
				return false;
			}
		}
		return true;
	}

	@Test
	private Matrix4 toMatrix4( Matrix3 m3 ) 
	{
		Matrix4 result = new Matrix4();
		float[] re = result.getArray();
		float[] me = m3.getArray();
		re[0] = me[0];
		re[1] = me[1];
		re[2] = me[2];
		re[4] = me[3];
		re[5] = me[4];
		re[6] = me[5];
		re[8] = me[6];
		re[9] = me[7];
		re[10] = me[8];

		return result;
	}
}