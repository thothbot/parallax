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

import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.math.Interpolant;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.events.AnimationActionFinishedEvent;
import org.parallax3d.parallax.system.events.AnimationActionLoopEvent;

import java.util.ArrayList;
import java.util.List;

@ThreejsObject("THREE.AnimationMixer._Action")
public class Action {

    // Loop styles for AnimationAction
    public enum Loop {
        LoopOnce,
        LoopRepeat,
        LoopPingPong;
    }

    AnimationMixer _mixer;
    AnimationClip _clip;
    GeometryObject _localRoot;

    Interpolant.Settings _interpolantSettings = new Interpolant.Settings(
            Interpolant.EndingModes.ZeroCurvatureEnding,
            Interpolant.EndingModes.ZeroCurvatureEnding);

    List<Interpolant> _interpolants;

    List<PropertyMixer> _propertyBindings;

    int _cacheIndex = -1;			// for the memory manager
    int _byClipCacheIndex = -1;		// for the memory manager

    Interpolant _timeScaleInterpolant;
    Interpolant _weightInterpolant;

    Loop loop = Loop.LoopRepeat;
    int _loopCount = -1;

    // global mixer time when the action is to be started
    // it's set back to '-1' upon start of the action
    double _startTime = -1;

    // scaled local time of the action
    // gets clamped or wrapped to 0..clip.duration according to loop
    double time = 0;

    double timeScale = 1.;
    double _effectiveTimeScale = 1.;
    double _currentTimeScale = 1.;

    double weight = 1.;
    double _effectiveWeight = 1.;

    long repetitions = Long.MAX_VALUE; 		// no. of repetitions when looping

    boolean paused = false;				// false -> zero effective time scale
    boolean enabled = true;				// true -> zero effective weight

    boolean clampWhenFinished 	= false;	// keep feeding the last frame?

    boolean zeroSlopeAtStart 	= true;		// for smooth interpolation w/o separate
    boolean zeroSlopeAtEnd		= true;		// clips for start, loop and end


    public Action(AnimationMixer mixer, AnimationClip clip) {
        this(mixer, clip, null);
    }

    public Action(AnimationMixer mixer, AnimationClip clip, GeometryObject localRoot) {

        this._mixer = mixer;
        this._clip = clip;
        this._localRoot = localRoot;


        List<KeyframeTrack> tracks = clip.tracks;
        int nTracks = tracks.size();

        _interpolants = new ArrayList<>( nTracks );

        for ( int i = 0; i != nTracks; ++ i ) {

            Interpolant interpolant = tracks.get(i).createInterpolant( null );
            _interpolants.set(i, interpolant);
            interpolant.setSettings(_interpolantSettings);

        }

        _propertyBindings = new ArrayList<>( nTracks );
    }

    public Action play() {

        this._mixer._activateAction( this );

        return this;

    }

    public Action stop() {

        this._mixer._deactivateAction( this );

        return this.reset();

    }

    public Action reset() {

        this.paused = false;
        this.enabled = true;

        this.time = 0;			// restart clip
        this._loopCount = -1;	// forget previous loops
        this._startTime = -1;	// forget scheduling

        return this.stopFading().stopWarping();

    }

    public boolean isRunning() {

        double start = this._startTime;

        return this.enabled && ! this.paused && this.timeScale != 0 &&
                this._startTime == -1 && this._mixer._isActiveAction( this );

    }

    // return true when play has been called
    public boolean isScheduled() {

        return this._mixer._isActiveAction( this );

    }

    public Action startAt( double time ) {

        this._startTime = time;

        return this;

    }

    public Action setLoop( Loop mode, long repetitions ) {

        this.loop = mode;
        this.repetitions = repetitions;

        return this;

    }

    // Weight

    /**
     * set the weight stopping any scheduled fading
     * although .enabled = false yields an effective weight of zero, this
     * method does *not* change .enabled, because it would be confusing
     */
    public Action setEffectiveWeight( double weight ) {

        this.weight = weight;

        // note: same logic as when updated at runtime
        this._effectiveWeight = this.enabled ? weight : 0;

        return this.stopFading();

    }

    /**
     * return the weight considering fading and .enabled
     */
    public double getEffectiveWeight() {

        return this._effectiveWeight;

    }

    public Action fadeIn( double duration ) {

        return this._scheduleFading( duration, 0, 1 );

    }

    public Action fadeOut( double duration ) {

        return this._scheduleFading( duration, 1, 0 );

    }

    public Action crossFadeFrom( Action fadeOutAction, double duration, boolean warp ) {

//        var mixer = this._mixer;

        fadeOutAction.fadeOut( duration );
        this.fadeIn( duration );

        if( warp ) {

            double fadeInDuration = this._clip.duration,
                    fadeOutDuration = fadeOutAction._clip.duration,

                    startEndRatio = fadeOutDuration / fadeInDuration,
                    endStartRatio = fadeInDuration / fadeOutDuration;

            fadeOutAction.warp( 1.0, startEndRatio, duration );
            this.warp( endStartRatio, 1.0, duration );

        }

        return this;

    }

    public Action crossFadeTo( Action fadeInAction, double duration, boolean warp ) {

        return fadeInAction.crossFadeFrom( this, duration, warp );

    }

    public Action stopFading() {

        Interpolant weightInterpolant = this._weightInterpolant;

        if ( weightInterpolant != null ) {

            this._weightInterpolant = null;
            this._mixer._takeBackControlInterpolant( weightInterpolant );

        }

        return this;

    }

    // Time Scale Control

    // set the weight stopping any scheduled warping
    // although .paused = true yields an effective time scale of zero, this
    // method does *not* change .paused, because it would be confusing
    public Action setEffectiveTimeScale( double timeScale ) {

        this.timeScale = timeScale;
        this._effectiveTimeScale = this.paused ? 0 :timeScale;

        return this.stopWarping();

    }

    // return the time scale considering warping and .paused
    public double getEffectiveTimeScale() {

        return this._effectiveTimeScale;

    }

    public Action setDuration( double duration ) {

        this.timeScale = this._clip.duration / duration;

        return this.stopWarping();

    }

    public Action syncWith( Action action ) {

        this.time = action.time;
        this.timeScale = action.timeScale;

        return this.stopWarping();

    }

    public Action halt( double duration ) {

        return this.warp( this._currentTimeScale, 0, duration );

    }

    public Action warp( double startTimeScale, double endTimeScale, double duration ) {

        AnimationMixer mixer = this._mixer;
        double now = mixer.time;
        Interpolant interpolant = this._timeScaleInterpolant;

        if ( interpolant == null ) {

            interpolant = mixer._lendControlInterpolant();
            this._timeScaleInterpolant = interpolant;

        }

        double[] times = interpolant.getParameterPositions(),
                values = interpolant.getSampleValues();

        times[ 0 ] = now;
        times[ 1 ] = now + duration;

        values[ 0 ] = startTimeScale / timeScale;
        values[ 1 ] = endTimeScale / timeScale;

        return this;

    }

    public Action stopWarping() {

        Interpolant timeScaleInterpolant = this._timeScaleInterpolant;

        if ( timeScaleInterpolant != null ) {

            this._timeScaleInterpolant = null;
            this._mixer._takeBackControlInterpolant( timeScaleInterpolant );

        }

        return this;

    }

    // Object Accessors

    public AnimationMixer getMixer() {

        return this._mixer;

    }

    public AnimationClip getClip() {

        return this._clip;

    }

    public GeometryObject getRoot() {

        if(this._localRoot != null)
            return this._localRoot;

        return  this._mixer._root;

    }

    public void _update( double time, double deltaTime, int timeDirection, int accuIndex ) {
        // called by the mixer

        double startTime = this._startTime;

        if ( startTime != -1 ) {

            // check for scheduled start of action

            double timeRunning = ( time - startTime ) * timeDirection;
            if ( timeRunning < 0 || timeDirection == 0 ) {

                return; // yet to come / don't decide when delta = 0

            }

            // start

            this._startTime = -1; // unschedule
            deltaTime = timeDirection * timeRunning;

        }

        // apply time scale and advance time

        deltaTime *= this._updateTimeScale( time );
        double clipTime = this._updateTime( deltaTime );

        // note: _updateTime may disable the action resulting in
        // an effective weight of 0

        double weight = this._updateWeight( time );

        if ( weight > 0 ) {

            List<Interpolant> interpolants = this._interpolants;
            List<PropertyMixer> propertyMixers = this._propertyBindings;

            for ( int j = 0, m = interpolants.size(); j != m; ++ j ) {

                interpolants.get(j).evaluate( clipTime );
                propertyMixers.get(j).accumulate( accuIndex, weight );

            }

        }

    }

    // Interna

    private double _updateWeight( double time ) {

        double weight = 0;

        if ( this.enabled ) {

            weight = this.weight;
            Interpolant interpolant = this._weightInterpolant;

            if ( interpolant != null ) {

                double interpolantValue = interpolant.evaluate( time )[ 0 ];

                weight *= interpolantValue;

                if ( time > interpolant.getParameterPositions()[ 1 ] ) {

                    this.stopFading();

                    if ( interpolantValue == 0 ) {

                        // faded out, disable
                        this.enabled = false;

                    }

                }

            }

        }

        this._effectiveWeight = weight;
        return weight;

    }

    private double _updateTimeScale( double time ) {

        double timeScale = 0;

        if ( ! this.paused ) {

            timeScale = this.timeScale;

            Interpolant interpolant = this._timeScaleInterpolant;

            if ( interpolant != null ) {

                double interpolantValue = interpolant.evaluate( time )[ 0 ];

                timeScale *= interpolantValue;

                if ( time > interpolant.getParameterPositions()[ 1 ] ) {

                    this.stopWarping();

                    if ( timeScale == 0 ) {

                        // motion has halted, pause
                        this.paused = true;

                    } else {

                        // warp done - apply final time scale
                        this.timeScale = timeScale;

                    }

                }

            }

        }

        this._effectiveTimeScale = timeScale;
        return timeScale;

    }

    private double _updateTime( double deltaTime ) {

        double time = this.time + deltaTime;

        if ( deltaTime == 0 ) return time;

        double duration = this._clip.duration;

                int loopCount = this._loopCount;

                boolean pingPong = false;

        switch ( loop ) {

            case LoopOnce:

                if ( loopCount == -1 ) {

                    // just started

                    this._loopCount = 0;
                    this._setEndings( true, true, false );

                }

                if ( time >= duration ) {

                    time = duration;

                } else if ( time < 0 ) {

                    time = 0;

                } else break;

                // reached the end

                if ( this.clampWhenFinished ) this.paused = true;
                else this.enabled = false;

                this._mixer.dispatchEvent( new AnimationActionFinishedEvent( this, deltaTime < 0 ? -1 : 1) );

                break;

            case LoopPingPong:

                pingPong = true;

            case LoopRepeat:

                if ( loopCount == -1 ) {

                    // just started

                    if ( deltaTime > 0 ) {

                        loopCount = 0;

                        this._setEndings(
                                true, this.repetitions == 0, pingPong );

                    } else {

                        // when looping in reverse direction, the initial
                        // transition through zero counts as a repetition,
                        // so leave loopCount at -1

                        this._setEndings( this.repetitions == 0, true, pingPong );

                    }

                }

                if ( time >= duration || time < 0 ) {

                    // wrap around

                    double loopDelta = Math.floor( time / duration ); // signed
                    time -= duration * loopDelta;

                    loopCount += Math.abs( loopDelta );

                    long pending = this.repetitions - loopCount;

                    if ( pending < 0 ) {

                        // stop (switch state, clamp time, fire event)

                        if ( this.clampWhenFinished ) this.paused = true;
                        else this.enabled = false;

                        time = deltaTime > 0 ? duration : 0;

                        this._mixer.dispatchEvent( new AnimationActionFinishedEvent( this, deltaTime > 0 ? 1 : -1) );

                        break;

                    } else if ( pending == 0 ) {

                        // transition to last round

                        boolean atStart = deltaTime < 0;
                        this._setEndings( atStart, ! atStart, pingPong );

                    } else {

                        this._setEndings( false, false, pingPong );

                    }

                    this._loopCount = loopCount;

                    this._mixer.dispatchEvent( new AnimationActionLoopEvent(this, loopDelta));

                }

                if ( loop == Loop.LoopPingPong && ( loopCount & 1 ) == 1 ) {

                    // invert time for the "pong round"

                    this.time = time;

                    return duration - time;

                }

                break;

        }

        this.time = time;

        return time;

    }

    private void _setEndings( boolean atStart, boolean atEnd, boolean pingPong ) {

        Interpolant.Settings settings = this._interpolantSettings;

        if ( pingPong ) {

            settings.endingStart 	= Interpolant.EndingModes.ZeroSlopeEnding;
            settings.endingEnd		= Interpolant.EndingModes.ZeroSlopeEnding;

        } else {

            // assuming for LoopOnce atStart == atEnd == true

            if ( atStart ) {

                settings.endingStart = this.zeroSlopeAtStart ?
                        Interpolant.EndingModes.ZeroSlopeEnding : Interpolant.EndingModes.ZeroCurvatureEnding;

            } else {

                settings.endingStart = Interpolant.EndingModes.WrapAroundEnding;

            }

            if ( atEnd ) {

                settings.endingEnd = this.zeroSlopeAtEnd ?
                        Interpolant.EndingModes.ZeroSlopeEnding : Interpolant.EndingModes.ZeroCurvatureEnding;

            } else {

                settings.endingEnd 	 = Interpolant.EndingModes.WrapAroundEnding;

            }

        }

    }

    private Action _scheduleFading( double duration, double weightNow, double weightThen ) {

        AnimationMixer mixer = this._mixer;
        double now = mixer.time;
        Interpolant interpolant = this._weightInterpolant;

        if ( interpolant == null ) {

            interpolant = mixer._lendControlInterpolant();
            this._weightInterpolant = interpolant;

        }

        double[] times = interpolant.getParameterPositions(),
                values = interpolant.getSampleValues();

        times[ 0 ] = now; 				values[ 0 ] = weightNow;
        times[ 1 ] = now + duration;	values[ 1 ] = weightThen;

        return this;

    }
}
