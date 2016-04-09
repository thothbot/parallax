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
import static org.parallax3d.parallax.extras.curves.CatmullRomCurve3Test.vectorsAreEqual;

@ThreejsTest("ClosedSplineCurve3")
public class ClosedSplineCurve3Test {

    @Test
    public void testBasic_check() {
        CatmullRomCurve3 closedSpline = new CatmullRomCurve3(Arrays.asList(new Vector3(-60, -100, 60),
                new Vector3(-60, 20, 60), new Vector3(-60, 120, 60),
                new Vector3(60, 20, -60), new Vector3(60, -100, -60)));
        closedSpline.setClosed(true);
        List<Vector3> closedSplinePoints = Arrays.asList(new Vector3(-60, -100, 60),
                new Vector3(-67.5, -46.25, 67.5), new Vector3(-60, 20, 60),
                new Vector3(-67.5, 83.75, 67.5), new Vector3(-60, 120, 60),
                new Vector3(0, 83.75, 0), new Vector3(60, 20, -60), new Vector3(75, -46.25, -75),
                new Vector3(60, -100, -60), new Vector3(0, -115, 0), new Vector3(-60, -100, 60));

        List<Vector2> getPoints = closedSpline.getPoints(10);
        vectorsAreEqual(getPoints, closedSplinePoints);
        assertTrue(getPoints.size() == 11);

    }

}
