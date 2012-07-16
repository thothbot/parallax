/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.shared.objects;

import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.materials.Material;

public class MorphAnimMesh extends Mesh
{

	public int startKeyframe;
	public int endKeyframe;
	public int length;
	
	public int direction = 1;
	public boolean directionBackwards = false;
	
	
	public int duration = 1000; // milliseconds
	public boolean mirroredLoop = false;
	public int time = 0;

	private int lastKeyframe = 0;
	private int currentKeyframe = 0;

	public MorphAnimMesh(Geometry geometry, Material material) {
		super(geometry, material);
		
		// internals
		this.setFrameRange( 0, this.geometry.getMorphTargets().size() - 1 );
	}

	public void setFrameRange(int start, int end ) 
	{
		this.startKeyframe = start;
		this.endKeyframe = end;

		this.length = this.endKeyframe - this.startKeyframe + 1;
	}
	
	public void setDirectionForward() 
	{
		this.direction = 1;
		this.directionBackwards = false;
	}
	
	public void setDirectionBackward() 
	{
		this.direction = -1;
		this.directionBackwards = true;
	}
	
	// TODO: Fix
	public void parseAnimations() 
	{

//		Geometry geometry = this.geometry;
//
//		if ( ! geometry.animations ) geometry.animations = {};
//
//		var firstAnimation, animations = geometry.animations;
//
//		var pattern = /([a-z]+)(\d+)/;
//
//		for ( var i = 0, il = geometry.morphTargets.length; i < il; i ++ ) {
//
//			var morph = geometry.morphTargets[ i ];
//			var parts = morph.name.match( pattern );
//
//			if ( parts && parts.length > 1 ) {
//
//				var label = parts[ 1 ];
//				var num = parts[ 2 ];
//
//				if ( ! animations[ label ] ) animations[ label ] = { start: Infinity, end: -Infinity };
//
//				var animation = animations[ label ];
//
//				if ( i < animation.start ) animation.start = i;
//				if ( i > animation.end ) animation.end = i;
//
//				if ( ! firstAnimation ) firstAnimation = label;
//
//			}
//
//		}
//
//		geometry.firstAnimation = firstAnimation;

	}
	
//	THREE.MorphAnimMesh.prototype.setAnimationLabel = function ( label, start, end ) {
//
//		if ( ! this.geometry.animations ) this.geometry.animations = {};
//
//		this.geometry.animations[ label ] = { start: start, end: end };
//
//	};
//
//	THREE.MorphAnimMesh.prototype.playAnimation = function ( label, fps ) {
//
//		var animation = this.geometry.animations[ label ];
//
//		if ( animation ) {
//
//			this.setFrameRange( animation.start, animation.end );
//			this.duration = 1000 * ( ( animation.end - animation.start ) / fps );
//			this.time = 0;
//
//		} else {
//
//			console.warn( "animation[" + label + "] undefined" );
//
//		}
//
//	};
//
//	THREE.MorphAnimMesh.prototype.updateAnimation = function ( delta ) {
//
//		var frameTime = this.duration / this.length;
//
//		this.time += this.direction * delta;
//
//		if ( this.mirroredLoop ) {
//
//			if ( this.time > this.duration || this.time < 0 ) {
//
//				this.direction *= -1;
//
//				if ( this.time > this.duration ) {
//
//					this.time = this.duration;
//					this.directionBackwards = true;
//
//				}
//
//				if ( this.time < 0 ) {
//
//					this.time = 0;
//					this.directionBackwards = false;
//
//				}
//
//			}
//
//		} else {
//
//			this.time = this.time % this.duration;
//
//			if ( this.time < 0 ) this.time += this.duration;
//
//		}
//
//		var keyframe = this.startKeyframe + THREE.Math.clamp( Math.floor( this.time / frameTime ), 0, this.length - 1 );
//
//		if ( keyframe !== this.currentKeyframe ) {
//
//			this.morphTargetInfluences[ this.lastKeyframe ] = 0;
//			this.morphTargetInfluences[ this.currentKeyframe ] = 1;
//
//			this.morphTargetInfluences[ keyframe ] = 0;
//
//			this.lastKeyframe = this.currentKeyframe;
//			this.currentKeyframe = keyframe;
//
//		}
//
//		var mix = ( this.time % frameTime ) / frameTime;
//
//		if ( this.directionBackwards ) {
//
//			mix = 1 - mix;
//
//		}
//
//		this.morphTargetInfluences[ this.currentKeyframe ] = mix;
//		this.morphTargetInfluences[ this.lastKeyframe ] = 1 - mix;
//
//	}
}
