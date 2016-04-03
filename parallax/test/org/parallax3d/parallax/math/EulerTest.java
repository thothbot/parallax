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

@ThreejsTest("Euler")
public class EulerTest {

    static final Euler eulerZero = new Euler( 0, 0, 0, "XYZ" );
    static final Euler eulerAxyz = new Euler( 1, 0, 0, "XYZ" );
    static final Euler eulerAzyx = new Euler( 0, 1, 0, "ZYX" );

    @Test
    public void testConstructor_equals() {
        Euler a = new Euler();
        assertTrue(a.equals(eulerZero));
        assertTrue(!a.equals(eulerAxyz));
        assertTrue(!a.equals(eulerAzyx));

    }

    @Test
    public void testClone_copy_equals() {
        Euler a = eulerAxyz.clone();
        assertTrue(a.equals(eulerAxyz));
        assertTrue(!a.equals(eulerZero));
        assertTrue(!a.equals(eulerAzyx));
        assertTrue(a.equals(eulerAzyx));
        assertTrue(!a.equals(eulerAxyz));
        assertTrue(!a.equals(eulerZero));

    }

    @Test
    public void testSet_setFromVector3_toVector3() {
        Euler a = new Euler();
        a.set(0, 1, 0, "ZYX");
        assertTrue(a.equals(eulerAzyx));
        assertTrue(!a.equals(eulerAxyz));
        assertTrue(!a.equals(eulerZero));
        Vector3 vec = new Vector3(0, 1, 0);
        Euler b = new Euler().setFromVector3(vec, "ZYX");
        assertTrue(a.equals(b));
        Vector3 c = b.toVector3();
        assertTrue(c.equals(vec));

    }

    @Test
    public void testQuaternion_setFromEuler_Euler_fromQuaternion() {
        Euler[] testValues = new Euler[]{eulerZero, eulerAxyz, eulerAzyx};
        for( int i = 0; i < testValues.length; i ++ ) {
            Euler v = testValues[i];
            Quaternion q = new Quaternion().setFromEuler( v );

            Euler v2 = new Euler().setFromQuaternion( q, v.order );
            Quaternion q2 = new Quaternion().setFromEuler( v2 );
            assertTrue( quatEquals( q, q2 ) );
        }

    }

    @Test
    public void testMatrix4_setFromEuler_Euler_fromRotationMatrix() {
        Euler[] testValues = new Euler[]{eulerZero, eulerAxyz, eulerAzyx};
        for( int i = 0; i < testValues.length; i ++ ) {
            Euler v = testValues[i];
            Matrix4 m = new Matrix4().makeRotationFromEuler( v );

            Euler v2 = new Euler().setFromRotationMatrix( m, v.order );
            Matrix4 m2 = new Matrix4().makeRotationFromEuler( v2 );
            assertTrue( matrixEquals4( m, m2 ) );
        }

    }

    @Test
    public void testReorder() {
        Euler[] testValues = new Euler[]{eulerZero, eulerAxyz, eulerAzyx};
        for( int i = 0; i < testValues.length; i ++ ) {
            Euler v = testValues[i];
            Quaternion q = new Quaternion().setFromEuler( v );

            v.reorder( "YZX" );
            Quaternion q2 = new Quaternion().setFromEuler( v );
            assertTrue( quatEquals( q, q2 ) );

            v.reorder( "ZXY" );
            Quaternion q3 = new Quaternion().setFromEuler( v );
            assertTrue( quatEquals( q, q3 ) );
        }
    }

    @Test
    public void testGimbalLocalQuat() {
        Quaternion q1 = new Quaternion(0.5207769385244341, -0.4783214164122354, 0.520776938524434, 0.47832141641223547);
        Quaternion q2 = new Quaternion(0.11284905712620674, 0.6980437630368944, -0.11284905712620674, 0.6980437630368944);
        String eulerOrder = "ZYX";
        Euler eViaQ1 = new Euler().setFromQuaternion(q1, eulerOrder);
        Matrix4 mViaQ1 = new Matrix4().makeRotationFromQuaternion(q1);
        Euler eViaMViaQ1 = new Euler().setFromRotationMatrix(mViaQ1, eulerOrder);
        assertTrue(eulerEquals(eViaQ1, eViaMViaQ1));

    }

    static boolean  matrixEquals4( Matrix4 a, Matrix4 b) {
        double tolerance = 0.0001;
        if( a.elements.getLength() != b.elements.getLength() ) {
            return false;
        }
        for( int i = 0, il = a.elements.getLength(); i < il; i ++ ) {
            double delta = a.elements.get(i) - b.elements.get(i);
            if( delta > tolerance ) {
                return false;
            }
        }
        return true;
    };

    static boolean eulerEquals( Euler a, Euler b ) {
        double tolerance = 0.0001;
        double diff = Math.abs( a.x - b.x ) + Math.abs( a.y - b.y ) + Math.abs( a.z - b.z );
        return ( diff < tolerance );
    }

    static boolean quatEquals( Quaternion a, Quaternion b) {
        double tolerance = 0.0001;
        double diff = Math.abs( a.x - b.x ) + Math.abs( a.y - b.y ) + Math.abs( a.z - b.z ) + Math.abs( a.w - b.w );
        return ( diff < tolerance );
    }
}
