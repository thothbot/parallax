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

package org.parallax3d.parallax.extras.curves;

import org.junit.Test;
import org.parallax3d.parallax.graphics.extras.curves.CatmullRomCurve3;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

@ThreejsTest("CatmullRomCurve3")
public class CatmullRomCurve3Test {

    static double threshold = 0.000001;

    static void vectorsAreEqual(List<Vector2> check, List<Vector3> that) {

        assertTrue(check.size() == that.size());

        for (int i = 0; i < check.size(); i++) {

            Vector3 a = (Vector3) check.get(i), b = that.get(i);
            assertTrue("Vector differs at index " + i +
                    ". Should be " + a +
                    " instead of " + b, a.distanceToSquared(b) < threshold);
        }

    }

    List<Vector3> positions = Arrays.asList(
            new Vector3(-60, -100, 60),
            new Vector3(-60, 20, 60),
            new Vector3(-60, 120, 60),
            new Vector3(60, 20, -60),
            new Vector3(60, -100, -60)
    );

    @Test
    public void testCatmullrom_check() {
        CatmullRomCurve3 curve = new CatmullRomCurve3(positions);
        curve.setType(CatmullRomCurve3.Type.catmullrom);
        List<Vector3> catmullPoints = Arrays.asList(new Vector3(-60, -100, 60),
                new Vector3(-60, -51.04, 60), new Vector3(-60, -2.7199999999999998, 60),
                new Vector3(-61.92, 44.48, 61.92), new Vector3(-68.64, 95.36000000000001, 68.64),
                new Vector3(-60, 120, 60),
                new Vector3(-14.880000000000017, 95.36000000000001, 14.880000000000017),
                new Vector3(41.75999999999997, 44.48000000000003, -41.75999999999997),
                new Vector3(67.68, -4.640000000000025, -67.68),
                new Vector3(65.75999999999999, -59.68000000000002, -65.75999999999999),
                new Vector3(60, -100, -60));

        List<Vector2> getPoints = curve.getPoints(10);
        vectorsAreEqual(getPoints, catmullPoints);
        assertTrue(getPoints.size() == 11);

    }

    @Test
    public void testChordal_basic_check() {
        CatmullRomCurve3 curve = new CatmullRomCurve3(positions);
        curve.setType(CatmullRomCurve3.Type.chordal);
        List<Vector3> chordalPoints = Arrays.asList(new Vector3(-60, -100, 60), new Vector3(-60, -52, 60),
                new Vector3(-60, -4, 60), new Vector3(-60.656435889910924, 41.62455386421379, 60.656435889910924),
                new Vector3(-62.95396150459915, 87.31049238896205, 62.95396150459915), new Vector3(-60, 120, 60),
                new Vector3(-16.302568199486444, 114.1500463116312, 16.302568199486444),
                new Vector3(42.998098664956586, 54.017050116427455, -42.998098664956586),
                new Vector3(63.542500175682434, -3.0571533975463856, -63.542500175682434),
                new Vector3(62.65687513176183, -58.49286504815978, -62.65687513176183),
                new Vector3(60.00000000000001, -100, -60.00000000000001));

        List<Vector2> getPoints = curve.getPoints(10);
        vectorsAreEqual(getPoints, chordalPoints);
        assertTrue(getPoints.size() == 11);

    }

    @Test
    public void testCentripetal_basic_check() {
        CatmullRomCurve3 curve = new CatmullRomCurve3(positions);
        curve.setType(CatmullRomCurve3.Type.centripetal);
        List<Vector3> centripetalPoints = Arrays.asList(new Vector3(-60, -100, 60),
                new Vector3(-60, -51.47527724919028, 60), new Vector3(-60, -3.300369665587032, 60),
                new Vector3(-61.13836565863938, 42.86306307781241, 61.13836565863938),
                new Vector3(-65.1226454638772, 90.69743905511538, 65.1226454638772),
                new Vector3(-60, 120, 60), new Vector3(-15.620412575504497, 103.10790870179872, 15.620412575504497),
                new Vector3(42.384384731047874, 48.35477686933143, -42.384384731047874),
                new Vector3(65.25545512241153, -3.5662509660683424, -65.25545512241153),
                new Vector3(63.94159134180865, -58.87468822455125, -63.94159134180865),
                new Vector3(59.99999999999999, -100, -59.99999999999999));
        List<Vector2> getPoints = curve.getPoints(10);
        vectorsAreEqual(getPoints, centripetalPoints);
        assertTrue(getPoints.size() == 11);

    }

    @Test
    public void testClosed_catmullrom_basic_check() {
        CatmullRomCurve3 curve = new CatmullRomCurve3(positions);
        curve.setType(CatmullRomCurve3.Type.catmullrom);
        curve.setClosed(true);
        List<Vector3> closedSplinePoints = Arrays.asList(new Vector3(-60, -100, 60),
                new Vector3(-67.5, -46.25, 67.5), new Vector3(-60, 20, 60),
                new Vector3(-67.5, 83.75, 67.5), new Vector3(-60, 120, 60),
                new Vector3(0, 83.75, 0), new Vector3(60, 20, -60),
                new Vector3(75, -46.25, -75), new Vector3(60, -100, -60),
                new Vector3(0, -115, 0), new Vector3(-60, -100, 60));
        List<Vector2> getPoints = curve.getPoints(10);
        vectorsAreEqual(getPoints, closedSplinePoints);
        assertTrue(getPoints.size() == 11);

    }

}
