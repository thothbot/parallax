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

import static org.junit.Assert.assertTrue;
import static org.parallax3d.parallax.math.Constants.*;

@ThreejsTest("Quaternion")
public class QuaternionTest {

	static final String[] orders = new String[]{ "XYZ", "YXZ", "ZXY", "ZYX", "YZX", "XZY" };
	static final Euler eulerAngles = new Euler( 0.1, -0.3, 0.25 );

	static Quaternion qSub ( Quaternion a, Quaternion b ) {
		Quaternion result = new Quaternion();
		result.copy( a );

		result.x -= b.x;
		result.y -= b.y;
		result.z -= b.z;
		result.w -= b.w;

		return result;

	}

	@Test
	public void testConstructor() {
		Quaternion a = new Quaternion();
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		assertTrue( a.z == 0 );
		assertTrue( a.w == 1 );
		a = new Quaternion(x, y, z, w);
		assertTrue( a.x == x );
		assertTrue( a.y == y );
		assertTrue( a.z == z );
		assertTrue( a.w == w );

	}

	@Test
	public void testCopy() {
		Quaternion a = new Quaternion(x, y, z, w);
		Quaternion b = new Quaternion().copy(a);
		assertTrue( b.x == x );
		assertTrue( b.y == y );
		assertTrue( b.z == z );
		assertTrue( b.w == w );
		a.x = 0;
		a.y = -1;
		a.z = 0;
		a.w = -1;
		assertTrue( b.x == x );
		assertTrue( b.y == y );

	}

	@Test
	public void testSet() {
		Quaternion a = new Quaternion();
		assertTrue( a.x == 0 );
		assertTrue( a.y == 0 );
		assertTrue( a.z == 0 );
		assertTrue( a.w == 1 );
		a.set(x, y, z, w);
		assertTrue( a.x == x );
		assertTrue( a.y == y );
		assertTrue( a.z == z );
		assertTrue( a.w == w );

	}

	@Test
	public void testSetFromAxisAngle() {
		assertTrue( true );
		Quaternion zero = new Quaternion();

		Quaternion a = new Quaternion().setFromAxisAngle(new Vector3(1, 0, 0), 0);
		assertTrue( a.equals(zero) );
		a = new Quaternion().setFromAxisAngle(new Vector3(0, 1, 0), 0);
		assertTrue( a.equals(zero) );
		a = new Quaternion().setFromAxisAngle(new Vector3(0, 0, 1), 0);
		assertTrue( a.equals(zero) );

		Quaternion b1 = new Quaternion().setFromAxisAngle(new Vector3(1, 0, 0), Math.PI);
		assertTrue( !a.equals(b1) );
		Quaternion b2 = new Quaternion().setFromAxisAngle(new Vector3(1, 0, 0), -Math.PI);
		assertTrue( !a.equals(b2) );

        b1.multiply( b2 );
		assertTrue( a.equals(b1) );

	}

	@Test
	public void testSetFromEuler_setFromQuaternion() {
		Vector3[] angles = new Vector3[]{new Vector3(1, 0, 0), new Vector3(0, 1, 0), new Vector3(0, 0, 1)};
		
		// ensure euler conversion to/from Quaternion matches.
		for( int i = 0; i < orders.length; i ++ ) {
			for( int j = 0; j < angles.length; j ++ ) {
				Euler eulers2 = new Euler().setFromQuaternion( new Quaternion().setFromEuler( new Euler( angles[j].x, angles[j].y, angles[j].z, orders[i] ) ), orders[i] );
				Vector3 newAngle = new Vector3( eulers2.x, eulers2.y, eulers2.z );
				assertTrue( newAngle.distanceTo( angles[j] ) < 0.001);
			}
		}
	}

	@Test
	public void testSetFromEuler_setFromRotationMatrix() {
		// ensure euler conversion for Quaternion matches that of Matrix4
		for( int i = 0; i < orders.length; i ++ ) {
			Quaternion q = new Quaternion().setFromEuler( eulerAngles );
			Matrix4 m = new Matrix4().makeRotationFromEuler( eulerAngles );
			Quaternion q2 = new Quaternion().setFromRotationMatrix( m );

			assertTrue( qSub( q, q2 ).length() < 0.001 );
		}
	}

	@Test
	public void testNormalize_length_lengthSq() {
		Quaternion a = new Quaternion(x, y, z, w);
		Quaternion b = new Quaternion(-x, -y, -z, -w);

		assertTrue( a.length() != 1 );
		assertTrue( a.lengthSq() != 1 );
        a.normalize();
        assertTrue( a.length() == 1 );
		assertTrue( a.lengthSq() == 1 );

        a.set(0, 0, 0, 0);
		assertTrue( a.lengthSq() == 0 );
		assertTrue( a.length() == 0 );
        a.normalize();
		assertTrue( a.lengthSq() == 1 );
		assertTrue( a.length() == 1 );

	}

	@Test
	public void testInverse_conjugate() {
		Quaternion a = new Quaternion(x, y, z, w);
		Quaternion b = a.clone().conjugate();
		assertTrue( a.x == -b.x );
		assertTrue( a.y == -b.y );
		assertTrue( a.z == -b.z );
		assertTrue( a.w == b.w );

	}

	@Test
	public void testMultiplyQuaternions_multiply() {
		Euler[] angles = new Euler[]{new Euler(1, 0, 0), new Euler(0, 1, 0), new Euler(0, 0, 1)};
		Quaternion q1 = new Quaternion().setFromEuler(angles[0]);
		Quaternion q2 = new Quaternion().setFromEuler(angles[1]);
		Quaternion q3 = new Quaternion().setFromEuler(angles[2]);
		Quaternion q = new Quaternion().multiplyQuaternions(q1, q2).multiply(q3);
		Matrix4 m1 = new Matrix4().makeRotationFromEuler(angles[0]);
		Matrix4 m2 = new Matrix4().makeRotationFromEuler(angles[1]);
		Matrix4 m3 = new Matrix4().makeRotationFromEuler(angles[2]);
		Matrix4 m = new Matrix4().multiplyMatrices(m1, m2).multiply(m3);
		Quaternion qFromM = new Quaternion().setFromRotationMatrix(m);
		assertTrue( qSub(q, qFromM).length() < 0.001 );

	}

	@Test
	public void testMultiplyVector3() {
		Euler[] angles = new Euler[]{new Euler(1, 0, 0), new Euler(0, 1, 0), new Euler(0, 0, 1)};

		// ensure euler conversion for Quaternion matches that of Matrix4
		for( int i = 0; i < orders.length; i ++ ) {
			for( int j = 0; j < angles.length; j ++ ) {
				Quaternion q = new Quaternion().setFromEuler( angles[j] );
				Matrix4 m = new Matrix4().makeRotationFromEuler( angles[j] );

				Vector3 v0 = new Vector3(1, 0, 0);
				Vector3 qv = v0.clone().applyQuaternion( q );
				Vector3 mv = v0.clone().applyMatrix4( m );

				assertTrue( qv.distanceTo( mv ) < 0.001 );
			}
		}
	}

	@Test
	public void testEquals() {
		Quaternion a = new Quaternion(x, y, z, w);
		Quaternion b = new Quaternion(-x, -y, -z, -w);

		assertTrue( a.x != b.x );
		assertTrue( a.y != b.y );

		assertTrue( !a.equals(b) );
		assertTrue( !b.equals(a) );

        a.copy( b );
		assertTrue( a.x == b.x );
		assertTrue( a.y == b.y );

		assertTrue( a.equals(b) );
		assertTrue( b.equals(a) );

	}

	@Test
	public void testSlerp() {
		slerpTestSkeleton(new doSlerpObject(), Mathematics.EPSILON);

	}

	@Test
	public void testSlerpFlat() {
		slerpTestSkeleton(new doSlerpArray(), Mathematics.EPSILON);

	}

	class doSlerpObject implements Slerp {

		Quaternion a, b, c;

		@Override
		public doSlerpObject set(double[] aArr, double[] bArr, double t) {
			a = new Quaternion().fromArray( aArr );
					b = new Quaternion().fromArray( bArr );
					c = new Quaternion().fromArray( aArr );
			c.slerp( b, t );

			return this;
		}

		@Override
		public boolean equals(double x, double y, double z, double w, double maxError) {

			return 	Math.abs( x - c.x ) <= maxError &&
					Math.abs( y - c.y ) <= maxError &&
					Math.abs( z - c.z ) <= maxError &&
					Math.abs( w - c.w ) <= maxError;
		}

		@Override
		public double length() {
			return c.length();
		}

		@Override
		public double dotA() {
			return c.dot( a );
		}

		@Override
		public double dotB() {
			return c.dot( b );
		}
	}

	class doSlerpArray implements Slerp {

		double[] a;
		double[] b;
		double[] result = new double[]{0 ,0 ,0 ,0};

		@Override
		public doSlerpArray set(double[] a, double[] b, double t) {
			this.a = a;
			this.b = b;

			Quaternion.slerpFlat( result, 0, a, 0, b, 0, t );

			return this;
		}

		@Override
		public boolean equals(double x, double y, double z, double w, double maxError) {

			return 	Math.abs( x - result[ 0 ] ) <= maxError &&
					Math.abs( y - result[ 1 ] ) <= maxError &&
					Math.abs( z - result[ 2 ] ) <= maxError &&
					Math.abs( w - result[ 3 ] ) <= maxError;
		}

		@Override
		public double length() {
			return Math.sqrt( arrDot( result, result ) );
		}

		@Override
		public double dotA() {
			return arrDot( result, a );
		}

		@Override
		public double dotB() {
			return arrDot( result, b );
		}

		double arrDot( double[] a, double[] b ) {

			return 	a[ 0 ] * b[ 0 ] + a[ 1 ] * b[ 1 ] +
					a[ 2 ] * b[ 2 ] + a[ 3 ] * b[ 3 ];

		}
	}

	interface Slerp {
		Slerp set(double[] aArr, double[] bArr, double i);

		boolean equals( double x, double y, double z, double w, double maxError );
		double length();
		double dotA();
		double dotB();
	}


	static double maxNormError = 0;

	static boolean isNormal(Slerp result, double maxError ) {

		double normError = Math.abs( 1 - result.length() );
		maxNormError = Math.max( maxNormError, normError );
		return normError <= maxError;

	}

	static void slerpTestSkeleton( Slerp doSlerp, double maxError ) {

		double[] a = new double[]{
				0.6753410084407496,
				0.4087830051091744,
				0.32856700410659473,
				0.5185120064806223,
		};

		double[] b = new double[]{
				0.6602792107657797,
				0.43647413932562285,
				0.35119011210236006,
				0.5001871596632682
		};

		Slerp result = doSlerp.set( a, b, 0 );
		assertTrue( result.equals(
				a[ 0 ], a[ 1 ], a[ 2 ], a[ 3 ], 0 ));

		result = doSlerp.set( a, b, 1 );
		assertTrue( result.equals(
				b[ 0 ], b[ 1 ], b[ 2 ], b[ 3 ], 0 ));

		result = doSlerp.set( a, b, 0.5 );
		assertTrue( Math.abs( result.dotA() - result.dotB() ) <= Mathematics.EPSILON);
		assertTrue( isNormal( result, maxError ));

		result = doSlerp.set( a, b, 0.25 );
		assertTrue( result.dotA() > result.dotB());
		assertTrue( isNormal( result, maxError ));

		result = doSlerp.set( a, b, 0.75 );
		assertTrue( result.dotA() < result.dotB());
		assertTrue( isNormal( result, maxError ));

		double D = Mathematics.SQRT1_2;

		result = doSlerp.set( new double[]{1, 0, 0, 0}, new double[]{ 0, 0, 1, 0 }, 0.5 );
		assertTrue( result.equals( D, 0, D, 0, Mathematics.EPSILON));
		assertTrue( isNormal( result,maxError ));

		result = doSlerp.set( new double[]{ 0, D, 0, D }, new double[]{ 0, -D, 0, D }, 0.5 );
		assertTrue( result.equals( 0, 0, 0, 1, Mathematics.EPSILON ));
		assertTrue( isNormal( result,maxError ));
	}
}
