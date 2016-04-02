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
 * Spherical linear unit quaternion interpolant.
 *
 * @author tschw
 */
@ThreejsObject("THREE.QuaternionLinearInterpolant")
public class QuaternionLinearInterpolant extends Interpolant {

    public QuaternionLinearInterpolant(double[] parameterPositions, double[] sampleValues, int sampleSize, double[] resultBuffer)
    {
        super(parameterPositions, sampleValues, sampleSize, resultBuffer);
    }

    @Override
    protected double[] interpolate(int i1, double t0, double t, double t1)
    {
        double[] result = this.resultBuffer,
                values = this.sampleValues;
        int stride = this.valueSize,
            offset = i1 * stride;

        double alpha = ( t - t0 ) / ( t1 - t0 );

        for ( int end = offset + stride; offset != end; offset += 4 ) {

//            Quaternion.slerpFlat( result, 0,
//                    values, offset - stride, values, offset, alpha );

        }

        return result;
    }
}
