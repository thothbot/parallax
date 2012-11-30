/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package thothbot.parallax.core.shared.objects;

import java.util.Map;

import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Mathematics;
import thothbot.parallax.core.shared.materials.Material;

public class MorphAnimMesh extends Mesh
{
	public class Animation {
		public int start;
		public int end;
	}
	
	private int startKeyframe;
	private int endKeyframe;
	private int length;
	
	private int direction = 1;
	private boolean directionBackwards = false;
	
	
	private int duration; // milliseconds
	private boolean mirroredLoop = false;
	private int time = 0;

	private int lastKeyframe = 0;
	private int currentKeyframe = 0;
	
	private Map<String, Animation> animations;
	
	public MorphAnimMesh(Geometry geometry, Material material) 
	{			
		super(geometry, material);
		setDuration(1000);
		// internals
		this.setFrameRange( 0, geometry.getMorphTargets().size() - 1 );

	}
	
	/**
	 * Sets animation duration. Default: 1000ms.
	 * 
	 * @param duration the millisecond value.
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public int getDuration() {
		return this.duration;
	}
		
	public void setDirectionForward() 
	{
		this.direction = 1;
		this.directionBackwards = false;
	}
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public void setDirectionBackward() 
	{
		this.direction = -1;
		this.directionBackwards = true;
	}
	
//	public void parseAnimations() 
//	{
//		Geometry geometry = this.geometry;
//
//		if ( ! animations == null) 
//			geometry.animations = {};
//
//		var firstAnimation;
//		animations = geometry.animations;
//
//		var pattern = /([a-z]+)(\d+)/;
//
//		for ( int i = 0, il = geometry.getMorphTargets().size(); i < il; i ++ ) 
//		{
//			MorphTarget morph = geometry.getMorphTargets().get(i);
//			var parts = morph.name.match( pattern );
//
//			if ( parts && parts.length > 1 ) 
//			{
//				var label = parts[ 1 ];
//				var num = parts[ 2 ];
//
//				if ( ! animations[ label ] ) 
//					animations[ label ] = { start: Infinity, end: -Infinity };
//
//				var animation = animations[ label ];
//
//				if ( i < animation.start ) animation.start = i;
//				if ( i > animation.end ) animation.end = i;
//
//				if ( ! firstAnimation ) firstAnimation = label;
//			}
//		}
//
//		geometry.firstAnimation = firstAnimation;
//	}
//	
//	public void setAnimationLabel( String label, int start, int end ) 
//	{
//		if ( ! this.geometry.animations ) this.geometry.animations = {};
//
//		this.geometry.animations[ label ] = { start: start, end: end };
//	}
//
//	public void playAnimation( String label, int fps ) 
//	{
//		var animation = this.geometry.animations[ label ];
//
//		if ( animation ) 
//		{
//			this.setFrameRange( animation.start, animation.end );
//			this.duration = 1000 * ( ( animation.end - animation.start ) / fps );
//			this.time = 0;
//
//		} 
//		else 
//		{
//			Log.error( "animation[" + label + "] undefined" );
//		}
//	}

	public void updateAnimation( int delta ) 
	{
		if(this.geometry == null)
			return;
		
		delta = 8;

		double frameTime = (double)this.duration / this.length;

		this.time += this.direction * delta;

		if ( this.mirroredLoop ) 
		{
			if ( this.time > this.duration || this.time < 0 ) 
			{

				this.direction *= -1;

				if ( this.time > this.duration ) 
				{
					this.time = this.duration;
					this.directionBackwards = true;
				}

				if ( this.time < 0 ) 
				{
					this.time = 0;
					this.directionBackwards = false;
				}
			}

		} 
		else 
		{
			this.time = this.time % this.duration;

			if ( this.time < 0 ) 
				this.time += this.duration;
		}

		int keyframe = this.startKeyframe + (int)Mathematics.clamp( 
				(int)Math.floor( this.time / frameTime ), 0, this.length - 1 );

		if ( keyframe != this.currentKeyframe ) 
		{
			getMorphTargetInfluences().set( this.lastKeyframe, 0.0);
			getMorphTargetInfluences().set( this.currentKeyframe, 1.0);

			getMorphTargetInfluences().set( keyframe, 0.0 );

			this.lastKeyframe = this.currentKeyframe;
			this.currentKeyframe = keyframe;
		}

		double mix = ( this.time % frameTime ) / frameTime;
		
		if ( this.directionBackwards )
			mix = 1 - mix;

		getMorphTargetInfluences().set( this.currentKeyframe, mix);
		getMorphTargetInfluences().set( this.lastKeyframe, 1.0 - mix);
	}
	
	private void setFrameRange(int start, int end ) 
	{
		this.startKeyframe = start;
		this.endKeyframe = end;

		this.length = this.endKeyframe - this.startKeyframe + 1;
	}
}
