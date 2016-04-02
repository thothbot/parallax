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
package org.parallax3d.parallax.animation;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.math.Interpolant;
import org.parallax3d.parallax.math.interpolants.CubicInterpolant;
import org.parallax3d.parallax.math.interpolants.DiscreteInterpolant;
import org.parallax3d.parallax.math.interpolants.LinearInterpolant;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import java.util.List;

import static org.parallax3d.parallax.math.Interpolant.Interpolation.InterpolateLinear;

/**
 *
 * A timed sequence of keyframes for a specific property.
 *
 *
 * @author Ben Houston / http://clara.io/
 * @author David Sarno / http://lighthaus.us/
 * @author tschw
 * @author thothbot
 */
@ThreejsObject("KeyframeTrack")
public class KeyframeTrack {

    public static final Interpolant.Interpolation DefaultInterpolation = InterpolateLinear;
    
    String name;
    Float32Array times;
    List<Object> values;
    Interpolant.Interpolation interpolation;

    public KeyframeTrack(String name, Float32Array times, List<Object> values) {

        this( name, times, values, DefaultInterpolation);
        
    }
    
    public KeyframeTrack(String name, Float32Array times, List<Object> values, Interpolant.Interpolation interpolation) 
    {
        this.name = name;
        this.times = times;
        this.values = values;
        this.interpolation = interpolation;
    }
    
    public Interpolant.Interpolation DefaultInterpolation() {
        
        return DefaultInterpolation;

    }

    public DiscreteInterpolant InterpolantFactoryMethodDiscrete( double[] result ) {

//        return new DiscreteInterpolant(
//                this.times, this.values, this.getValueSize(), result );
        return null;

    }

    public LinearInterpolant InterpolantFactoryMethodLinear( double[] result ) {

//        return new LinearInterpolant(
//                this.times, this.values, this.getValueSize(), result );

        return null;
    }

    public CubicInterpolant InterpolantFactoryMethodSmooth( double[] result ) {

//        return new CubicInterpolant(
//                this.times, this.values, this.getValueSize(), result );

        return null;
    }

//    setInterpolation( interpolation ) {
//
//        var factoryMethod = undefined;
//
//        switch ( interpolation ) {
//
//            case InterpolateDiscrete:
//
//                factoryMethod = this.InterpolantFactoryMethodDiscrete;
//
//                break;
//
//            case InterpolateLinear:
//
//                factoryMethod = this.InterpolantFactoryMethodLinear;
//
//                break;
//
//            case InterpolateSmooth:
//
//                factoryMethod = this.InterpolantFactoryMethodSmooth;
//
//                break;
//
//        }
//
//        if ( factoryMethod === undefined ) {
//
//            var message = "unsupported interpolation for " +
//                    this.ValueTypeName + " keyframe track named " + this.name;
//
//            if ( this.createInterpolant === undefined ) {
//
//                // fall back to default, unless the default itself is messed up
//                if ( interpolation !== this.DefaultInterpolation ) {
//
//                    this.setInterpolation( this.DefaultInterpolation );
//
//                } else {
//
//                    throw new Error( message ); // fatal, in this case
//
//                }
//
//            }
//
//            console.warn( message );
//            return;
//
//        }
//
//        this.createInterpolant = factoryMethod;
//
//    }
//
//    getInterpolation() {
//
//        switch ( this.createInterpolant ) {
//
//            case this.InterpolantFactoryMethodDiscrete:
//
//                return InterpolateDiscrete;
//
//            case this.InterpolantFactoryMethodLinear:
//
//                return InterpolateLinear;
//
//            case this.InterpolantFactoryMethodSmooth:
//
//                return InterpolateSmooth;
//
//        }
//
//    }

    public int getValueSize() {

        return this.values.size() / this.times.getLength();

    }

    // move all keyframes either forwards or backwards in time
    public KeyframeTrack shift( double timeOffset ) {

        if( timeOffset != 0.0 ) {

            for( int i = 0, n = this.times.getLength(); i != n; ++ i ) {

                times.set( i , times.get( i ) + timeOffset );

            }

        }

        return this;

    }

    // scale all keyframe times by a factor (useful for frame <-> seconds conversions)
    public KeyframeTrack scale(double timeScale ) {

        if( timeScale != 1.0 ) {

            for( int i = 0, n = this.times.getLength(); i != n; ++ i ) {

                times.set( i , times.get( i ) * timeScale );

            }

        }

        return this;

    }

    // removes keyframes before and after animation without changing any values within the range [startTime, endTime].
    // IMPORTANT: We do not shift around keys to the start of the track time, because for interpolated keys this will change their values
    public KeyframeTrack trim( double startTime, double endTime ) {

        Float32Array times = this.times;

        int nKeys = times.getLength(),
            from = 0,
            to = nKeys - 1;

        while ( from != nKeys && times.get( from ) < startTime ) ++ from;
        while ( to != -1 && times.get( to ) > endTime ) -- to;

        ++ to; // inclusive -> exclusive bound

        if( from != 0 || to != nKeys ) {

            // empty tracks are forbidden, so keep at least one keyframe
            if ( from >= to ) {
                to = Math.max(to, 1);
                from = to - 1;
            }

            int stride = this.getValueSize();
            this.times = AnimationUtils.arraySlice( times, from, to );
            this.values = AnimationUtils.arraySlice( this.values, from * stride, to * stride );

        }

        return this;

    }

    /**
     * ensure we do not get a GarbageInGarbageOut situation, make sure tracks are at least minimally viable
     * @return
     */
    public boolean validate() {

        boolean valid = true;

//        int valueSize = this.getValueSize();
//        if ( valueSize - Math.floor( valueSize ) != 0 ) {
//
//            Log.error( "invalid value size in track", this );
//            valid = false;
//
//        }

        int nKeys = times.getLength();

        if( nKeys == 0 ) {

            Log.error( "track is empty", this );
            valid = false;

        }

        double prevTime = 0;

        for( int i = 0; i != nKeys; i ++ ) {

            double currTime = times.get( i );

            if( prevTime > currTime ) {

                Log.error( "out of order keys", this, i, currTime, prevTime );
                valid = false;
                break;

            }

            prevTime = currTime;

        }

        return valid;

    }

    // removes equivalent sequential keys as common in morph target sequences
    // (0,0,0,0,1,1,1,0,0,0,0,0,0,0) --> (0,0,1,1,0,0)
    public KeyframeTrack optimize() {

        int stride = this.getValueSize(),
            writeIndex = 1;

        for( int i = 1, n = times.getLength() - 1; i <= n; ++ i ) {

            boolean keep = false;

            double time = times.get( i );
            double timeNext = times.get( i + 1 );

            // remove adjacent keyframes scheduled at the same time

//            if ( time != timeNext && ( i != 1 || time != time[ 0 ] ) ) {
//
//                // remove unnecessary keyframes same as their neighbors
//                int offset = i * stride,
//                        offsetP = offset - stride,
//                        offsetN = offset + stride;
//
//                for ( int j = 0; j != stride; ++ j ) {
//
//                    Object value = values.get( offset + j );
//
//                    if ( value != values.get(offsetP + j) ||
//                            value != values.get(offsetN + j)) {
//
//                        keep = true;
//                        break;
//
//                    }
//
//                }
//
//            }

            // in-place compaction

//            if ( keep ) {
//
//                if ( i != writeIndex ) {
//
//                    times[ writeIndex ] = times[ i ];
//
//                    int readOffset = i * stride,
//                            writeOffset = writeIndex * stride;
//
//                    for ( int j = 0; j != stride; ++ j ) {
//
//                        values[ writeOffset + j ] = values[ readOffset + j ];
//
//                    }
//
//
//                }
//
//                ++ writeIndex;
//
//            }

        }

        if ( writeIndex != times.getLength() ) {

            this.times = AnimationUtils.arraySlice( times, 0, writeIndex );
            this.values = AnimationUtils.arraySlice( values, 0, writeIndex * stride );

        }

        return this;

    }

    public Interpolant createInterpolant(Object o) {
        return null;
    }
}
