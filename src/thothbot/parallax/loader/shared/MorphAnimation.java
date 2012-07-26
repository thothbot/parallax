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

package thothbot.parallax.loader.shared;

import java.util.Map;

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Geometry.MorphTarget;
import thothbot.parallax.core.shared.core.Mathematics;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.objects.Mesh;

public class MorphAnimation
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
	
	private Geometry geometry;
	private Mesh mesh;
	
	public MorphAnimation() 
	{			
		setDuration(1000);
	}
	
	public void init(Mesh mesh, Geometry geometry)
	{
		this.mesh = mesh;
		this.geometry = geometry;

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
		if(this.mesh == null || this.geometry == null)
			return;
		
		delta = 17;

		float frameTime = this.duration / this.length;

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
			mesh.getMorphTargetInfluences().set( this.lastKeyframe, 0f);
			mesh.getMorphTargetInfluences().set( this.currentKeyframe, 1f);

			mesh.getMorphTargetInfluences().set( keyframe, 0f );

			this.lastKeyframe = this.currentKeyframe;
			this.currentKeyframe = keyframe;
		}

		float mix = ( this.time % frameTime ) / frameTime;

		if ( this.directionBackwards )
			mix = 1 - mix;

		mesh.getMorphTargetInfluences().set( this.currentKeyframe, mix);
		mesh.getMorphTargetInfluences().set( this.lastKeyframe, 1f - mix);

	}
	
	private void setFrameRange(int start, int end ) 
	{
		this.startKeyframe = start;
		this.endKeyframe = end;

		this.length = this.endKeyframe - this.startKeyframe + 1;
	}
}
