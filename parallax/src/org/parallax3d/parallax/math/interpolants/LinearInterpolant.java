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
package org.parallax3d.parallax.math.interpolants;

import org.parallax3d.parallax.math.Interpolant;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * @author tschw
 */
@ThreejsObject("THREE.LinearInterpolant")
public class LinearInterpolant extends Interpolant {

    public LinearInterpolant(double[] parameterPositions, double[] sampleValues, int sampleSize, double[] resultBuffer)
    {
        super(parameterPositions, sampleValues, sampleSize, resultBuffer);
    }

    @Override
    protected double[] interpolate(int i1, double t0, double t, double t1)
    {
        double[] result = this.resultBuffer,
                values = this.sampleValues;
        int stride = this.valueSize,
            offset1 = i1 * stride,
            offset0 = offset1 - stride;

        double weight1 = ( t - t0 ) / ( t1 - t0 ),
            weight0 = 1. - weight1;

        for ( int i = 0; i != stride; ++ i ) {

            result[ i ] =
                    values[ offset0 + i ] * weight0 +
                            values[ offset1 + i ] * weight1;

        }

        return result;
    }
}
