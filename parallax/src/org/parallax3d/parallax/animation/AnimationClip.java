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

import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.system.ThreejsObject;

import java.util.List;

/**
 *
 * Reusable set of Tracks that represent an animation.
 *
 * @author Ben Houston / http://clara.io/
 * @author David Sarno / http://lighthaus.us/
 * @author thothbot
 */
@ThreejsObject("AnimationClip")
public class AnimationClip {

    String name; //ok

    List<KeyframeTrack> tracks;

    double duration = -1; //ok

    public AnimationClip( List<KeyframeTrack> tracks ) {

        this(Mathematics.generateUUID(), tracks, -1);

    }

    public AnimationClip(String name, List<KeyframeTrack> tracks, double duration)
    {
        this.name = name;
        this.tracks = tracks;
        this.duration = duration;

        // this means it should figure out its duration by scanning the tracks
        if ( this.duration < 0 ) {

            this.resetDuration();

        }

        // maybe only do these on demand, as doing them here could potentially slow down loading
        // but leaving these here during development as this ensures a lot of testing of these functions
        this.trim();
        this.optimize();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<KeyframeTrack> getTracks() {
        return tracks;
    }

    public void setTracks(List<KeyframeTrack> tracks) {
        this.tracks = tracks;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void resetDuration() {

        List<KeyframeTrack> tracks = this.tracks;
        double duration = .0;

        for ( int i = 0, n = tracks.size(); i != n; ++ i ) {

            KeyframeTrack track = this.tracks.get(i);

            duration = Math.max(
                    duration, track.times.get( track.times.getLength() - 1 ) );

        }

        this.duration = duration;

    }

    public AnimationClip trim() {

        for ( int i = 0; i < this.tracks.size(); i ++ ) {

            this.tracks.get(i).trim( 0, this.duration );

        }

        return this;

    }

    public AnimationClip optimize() {

        for ( int i = 0; i < this.tracks.size(); i ++ ) {

            this.tracks.get(i).optimize();

        }

        return this;

    }

    public static AnimationClip CreateFromMorphTargetSequence( String name, List morphTargetSequence, double fps ) {

//        var numMorphTargets = morphTargetSequence.length;
//        var tracks = [];
//
//        for ( var i = 0; i < numMorphTargets; i ++ ) {
//
//            var times = [];
//            var values = [];
//
//            times.push(
//                    ( i + numMorphTargets - 1 ) % numMorphTargets,
//                    i,
//                    ( i + 1 ) % numMorphTargets );
//
//            values.push( 0, 1, 0 );
//
//            var order = AnimationUtils.getKeyframeOrder( times );
//            times = AnimationUtils.sortedArray( times, 1, order );
//            values = AnimationUtils.sortedArray( values, 1, order );
//
//            // if there is a key at the first frame, duplicate it as the
//            // last frame as well for perfect loop.
//            if ( times[ 0 ] == 0 ) {
//
//                times.push( numMorphTargets );
//                values.push( values[ 0 ] );
//
//            }
//
//            tracks.push(
//                    new NumberKeyframeTrack(
//                            '.morphTargetInfluences[' + morphTargetSequence[ i ].name + ']',
//                            times, values
//                    ).scale( 1.0 / fps ) );
//        }
//
//        return new AnimationClip( name, -1, tracks );
        return null;

    }

    public static AnimationClip findByName( List<AnimationClip> clipArray, String name ) {

        for ( int i = 0; i < clipArray.size(); i ++ ) {

            if ( clipArray.get(i).name.equals( name ) ) {

                return clipArray.get(i);

            }
        }

        return null;

    }

//    public static void CreateClipsFromMorphTargetSequences( morphTargets, fps ) {
//
//        var animationToMorphTargets = {};
//
//        // tested with https://regex101.com/ on trick sequences
//        // such flamingo_flyA_003, flamingo_run1_003, crdeath0059
//        var pattern = /^([\w-]*?)([\d]+)$/;
//
//        // sort morph target names into animation groups based
//        // patterns like Walk_001, Walk_002, Run_001, Run_002
//        for ( var i = 0, il = morphTargets.length; i < il; i ++ ) {
//
//            var morphTarget = morphTargets[ i ];
//            var parts = morphTarget.name.match( pattern );
//
//            if ( parts && parts.length > 1 ) {
//
//                var name = parts[ 1 ];
//
//                var animationMorphTargets = animationToMorphTargets[ name ];
//                if ( ! animationMorphTargets ) {
//
//                    animationToMorphTargets[ name ] = animationMorphTargets = [];
//
//                }
//
//                animationMorphTargets.push( morphTarget );
//
//            }
//
//        }
//
//        var clips = [];
//
//        for ( var name in animationToMorphTargets ) {
//
//            clips.push( AnimationClip.CreateFromMorphTargetSequence( name, animationToMorphTargets[ name ], fps ) );
//
//        }
//
//        return clips;
//
//    }
//
//    // parse the animation.hierarchy format
//    public static void parseAnimation( animation, bones, nodeName ) {
//
//        if ( ! animation ) {
//
//            console.error( "  no animation in JSONLoader data" );
//            return null;
//
//        }
//
//        var addNonemptyTrack = function(
//                trackType, trackName, animationKeys, propertyName, destTracks ) {
//
//            // only return track if there are actually keys.
//            if ( animationKeys.length !== 0 ) {
//
//                var times = [];
//                var values = [];
//
//                AnimationUtils.flattenJSON(
//                        animationKeys, times, values, propertyName );
//
//                // empty keys are filtered out, so check again
//                if ( times.length !== 0 ) {
//
//                    destTracks.push( new trackType( trackName, times, values ) );
//
//                }
//
//            }
//
//        };
//
//        var tracks = [];
//
//        var clipName = animation.name || 'default';
//        // automatic length determination in AnimationClip.
//        var duration = animation.length || -1;
//        var fps = animation.fps || 30;
//
//        var hierarchyTracks = animation.hierarchy || [];
//
//        for ( var h = 0; h < hierarchyTracks.length; h ++ ) {
//
//            var animationKeys = hierarchyTracks[ h ].keys;
//
//            // skip empty tracks
//            if ( ! animationKeys || animationKeys.length == 0 ) continue;
//
//            // process morph targets in a way exactly compatible
//            // with AnimationHandler.init( animation )
//            if ( animationKeys[0].morphTargets ) {
//
//                // figure out all morph targets used in this track
//                var morphTargetNames = {};
//                for ( var k = 0; k < animationKeys.length; k ++ ) {
//
//                    if ( animationKeys[k].morphTargets ) {
//
//                        for ( var m = 0; m < animationKeys[k].morphTargets.length; m ++ ) {
//
//                            morphTargetNames[ animationKeys[k].morphTargets[m] ] = -1;
//                        }
//
//                    }
//
//                }
//
//                // create a track for each morph target with all zero
//                // morphTargetInfluences except for the keys in which
//                // the morphTarget is named.
//                for ( var morphTargetName in morphTargetNames ) {
//
//                    var times = [];
//                    var values = [];
//
//                    for ( var m = 0;
//                          m !== animationKeys[k].morphTargets.length; ++ m ) {
//
//                        var animationKey = animationKeys[k];
//
//                        times.push( animationKey.time );
//                        values.push( ( animationKey.morphTarget === morphTargetName ) ? 1 : 0 )
//
//                    }
//
//                    tracks.push( new NumberKeyframeTrack(
//                            '.morphTargetInfluence[' + morphTargetName + ']', times, values ) );
//
//                }
//
//                duration = morphTargetNames.length * ( fps || 1.0 );
//
//            } else {
//                // ...assume skeletal animation
//
//                var boneName = '.bones[' + bones[ h ].name + ']';
//
//                addNonemptyTrack(
//                        VectorKeyframeTrack, boneName + '.position',
//                        animationKeys, 'pos', tracks );
//
//                addNonemptyTrack(
//                        QuaternionKeyframeTrack, boneName + '.quaternion',
//                        animationKeys, 'rot', tracks );
//
//                addNonemptyTrack(
//                        VectorKeyframeTrack, boneName + '.scale',
//                        animationKeys, 'scl', tracks );
//
//            }
//
//        }
//
//        if ( tracks.length === 0 ) {
//
//            return null;
//
//        }
//
//        var clip = new AnimationClip( clipName, duration, tracks );
//
//        return clip;
//
//    }
}
