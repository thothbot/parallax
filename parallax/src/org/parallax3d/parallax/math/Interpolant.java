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

import org.parallax3d.parallax.system.ThreejsObject;

/**
 * Abstract base class of interpolants over parametric samples.
 *
 * The parameter domain is one dimensional, typically the time or a path
 * along a curve defined by the data.
 *
 * The sample values can have any dimensionality and derived classes may
 * apply special interpretations to the data.
 *
 * This class provides the interval seek in a Template Method, deferring
 * the actual interpolation to derived classes.
 *
 * Time complexity is O(1) for linear access crossing at most two points
 * and O(log N) for random access, where N is the number of positions.
 *
 * References:
 *
 * 		http://www.oodesign.com/template-method-pattern.html
 *
 * @author tschw
 */
@ThreejsObject("Interpolant")
public abstract class Interpolant {

    public enum EndingModes {
        ZeroCurvatureEnding,
        ZeroSlopeEnding,
        WrapAroundEnding;
    };

    // Interpolation

    public enum Interpolation {
        InterpolateDiscrete,
        InterpolateLinear,
        InterpolateSmooth;
    }

    public static class Settings
    {
        public EndingModes endingStart;
        public EndingModes endingEnd;

        public Settings() {}

        public Settings(EndingModes endingStart, EndingModes endingEnd) {
            this.endingStart = endingStart;
            this.endingEnd = endingEnd;
        }
    }

    protected Settings settings = new Settings();

    protected double[] parameterPositions;

    protected double[] sampleValues;

    protected int valueSize;

    protected double[] resultBuffer;

    int _cachedIndex = 0;

    public Interpolant(double[] parameterPositions, double[] sampleValues, int sampleSize, double[] resultBuffer)
    {
        this.parameterPositions = parameterPositions;
        this.resultBuffer = resultBuffer;
        this.sampleValues = sampleValues;
        this.valueSize = sampleSize;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public double[] getParameterPositions() {
        return parameterPositions;
    }

    public void setParameterPositions(double[] parameterPositions) {
        this.parameterPositions = parameterPositions;
    }

    public double[] getSampleValues() {
        return sampleValues;
    }

    public void setSampleValues(double[] sampleValues) {
        this.sampleValues = sampleValues;
    }

    public int getValueSize() {
        return valueSize;
    }

    public void setValueSize(int valueSize) {
        this.valueSize = valueSize;
    }

    public double[] getResultBuffer() {
        return resultBuffer;
    }

    public void setResultBuffer(double[] resultBuffer) {
        this.resultBuffer = resultBuffer;
    }

    public double[] evaluate(double t )
    {

        double[] pp = this.parameterPositions;
        int i1 = this._cachedIndex;

        Double t1 = pp[   i1   ];
        Double t0 = pp[ i1 - 1 ];

        validate_interval: {

            seek: {

                int right;

                linear_scan: {
//- See http://jsperf.com/comparison-to-undefined/3
//- slower code:
//-
//- 				if ( t >= t1 || t1 === undefined ) {
                    forward_scan: if ( ! ( t < t1 ) ) {

                        for ( int giveUpAt = i1 + 2; ;) {

                            if ( t1 == null ) {

                                if ( t < t0 ) break forward_scan;

                                // after end

                                i1 = pp.length;
                                this._cachedIndex = i1;
                                return this.afterEnd( i1 - 1, t, t0 );

                            }

                            if ( i1 == giveUpAt ) break; // this loop

                            t0 = t1;
                            t1 = pp[ ++ i1 ];

                            if ( t < t1 ) {

                                // we have arrived at the sought interval
                                break seek;

                            }

                        }

                        // prepare binary search on the right side of the index
                        right = pp.length;
                        break linear_scan;

                    }

//- slower code:
//-					if ( t < t0 || t0 === undefined ) {
                    if ( ! ( t >= t0 ) ) {

                        // looping?

                        double t1global = pp[ 1 ];

                        if ( t < t1global ) {

                            i1 = 2; // + 1, using the scan for the details
                            t0 = t1global;

                        }

                        // linear reverse scan

                        for ( int giveUpAt = i1 - 2; ;) {

                            if ( t0 == null ) {

                                // before start

                                this._cachedIndex = 0;
                                return this.beforeStart( 0, t, t1 );

                            }

                            if ( i1 == giveUpAt ) break; // this loop

                            t1 = t0;
                            t0 = pp[ -- i1 - 1 ];

                            if ( t >= t0 ) {

                                // we have arrived at the sought interval
                                break seek;

                            }

                        }

                        // prepare binary search on the left side of the index
                        right = i1;
                        i1 = 0;
                        break linear_scan;

                    }

                    // the interval is valid

                    break validate_interval;

                } // linear scan

                // binary search

                while ( i1 < right ) {

                    int mid = ( i1 + right ) >>> 1;

                    if ( t < pp[ mid ] ) {

                        right = mid;

                    } else {

                        i1 = mid + 1;

                    }

                }

                t1 = pp[   i1   ];
                t0 = pp[ i1 - 1 ];

                // check boundary cases, again

                if ( t0 == null ) {

                    this._cachedIndex = 0;
                    return this.beforeStart( 0, t, t1 );

                }

                if ( t1 == null ) {

                    i1 = pp.length;
                    this._cachedIndex = i1;
                    return this.afterEnd( i1 - 1, t0, t );

                }

            } // seek

            this._cachedIndex = i1;

            this.intervalChanged( i1, t0, t1 );

        } // validate_interval

        return this.interpolate( i1, t0, t, t1 );

    }

    protected double[] copySampleValue( int index ) {

        // copies a sample value to the result buffer

        double[] result = this.resultBuffer;
        double[] values = this.sampleValues;
        int stride = this.valueSize;
        int offset = index * stride;

        for ( int i = 0; i != stride; ++ i ) {

            result[ i ] = values[ offset + i ];

        }

        return result;

    }

    protected double[] afterEnd(int i, double t0, double t)
    {
        return copySampleValue( i );
    }

    protected double[] beforeStart(int i, double t0, double t)
    {
        return copySampleValue( i );
    }

    protected abstract double[] interpolate( int i1, double t0, double t, double t1 );

    protected void intervalChanged( int i1, double t0, double t1 ) {
        // empty
    }
}
