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
 * Fast and simple cubic spline interpolant.
 *
 * It was derived from a Hermitian construction setting the first derivative
 * at each sample position to the linear slope between neighboring positions
 * over their parameter interval.
 *
 * @author tschw
 */
@ThreejsObject("THREE.CubicInterpolant")
public class CubicInterpolant extends Interpolant {

    double _weightPrev = -0.;
    int _offsetPrev = -0;
    double _weightNext = -0.;
    int _offsetNext = -0;

    public CubicInterpolant(double[] parameterPositions, double[] sampleValues, int sampleSize, double[] resultBuffer)
    {
        super(parameterPositions, sampleValues, sampleSize, resultBuffer);

        settings.endingStart = EndingModes.ZeroCurvatureEnding;
        settings.endingEnd = EndingModes.ZeroCurvatureEnding;
    }

    @Override
    protected double[] interpolate(int i1, double t0, double t, double t1)
    {
        double[] result = this.resultBuffer;
        double[] values = this.sampleValues;
        int stride = this.valueSize;

        int o1 = i1 * stride,		o0 = o1 - stride,
            oP = this._offsetPrev, 	oN = this._offsetNext;
        double wP = this._weightPrev,	wN = this._weightNext;

        double p = ( t - t0 ) / ( t1 - t0 ),
                pp = p * p,
                ppp = pp * p;

        // evaluate polynomials

        double sP =      - wP   * ppp   +         2. * wP    * pp    -          wP   * p;
        double s0 = ( 1. + wP ) * ppp   + (-1.5 - 2. * wP )  * pp    + ( -0.5 + wP ) * p     + 1.;
        double s1 = (-1. - wN ) * ppp   + ( 1.5 +      wN )  * pp    +    0.5        * p;
        double sN =        wN   * ppp   -              wN    * pp;

        // combine data linearly

        for ( int i = 0; i != stride; ++ i ) {

            result[ i ] =
                    sP * values[ oP + i ] +
                            s0 * values[ o0 + i ] +
                            s1 * values[ o1 + i ] +
                            sN * values[ oN + i ];

        }

        return result;
    }

    @Override
    protected void intervalChanged(int i1, double t0, double t1)
    {
        double[] pp = this.parameterPositions;
        int iPrev = i1 - 2,
            iNext = i1 + 1;

        Double tPrev = pp[ iPrev ],
            tNext = pp[ iNext ];

        if ( tPrev == null ) {

            switch ( this.settings.endingStart ) {

                case ZeroSlopeEnding:

                    // f'(t0) = 0
                    iPrev = i1;
                    tPrev = 2 * t0 - t1;

                    break;

                case WrapAroundEnding:

                    // use the other end of the curve
                    iPrev = pp.length - 2;
                    tPrev = t0 + pp[ iPrev ] - pp[ iPrev + 1 ];

                    break;

                default: // ZeroCurvatureEnding

                    // f''(t0) = 0 a.k.a. Natural Spline
                    iPrev = i1;
                    tPrev = t1;

            }

        }

        if ( tNext == null ) {

            switch ( this.settings.endingEnd ) {

                case ZeroSlopeEnding:

                    // f'(tN) = 0
                    iNext = i1;
                    tNext = 2 * t1 - t0;

                    break;

                case WrapAroundEnding:

                    // use the other end of the curve
                    iNext = 1;
                    tNext = t1 + pp[ 1 ] - pp[ 0 ];

                    break;

                default: // ZeroCurvatureEnding

                    // f''(tN) = 0, a.k.a. Natural Spline
                    iNext = i1 - 1;
                    tNext = t0;

            }

        }

        double halfDt = ( t1 - t0 ) * 0.5;
        int stride = this.valueSize;

        this._weightPrev = halfDt / ( t0 - tPrev );
        this._weightNext = halfDt / ( tNext - t1 );
        this._offsetPrev = iPrev * stride;
        this._offsetNext = iNext * stride;

    }
}
