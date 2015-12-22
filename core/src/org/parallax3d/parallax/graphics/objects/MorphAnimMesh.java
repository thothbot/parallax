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

package org.parallax3d.parallax.graphics.objects;

import java.util.List;
import java.util.Map;

import org.parallax3d.parallax.Parallax;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.system.ObjectMap;

public class MorphAnimMesh extends Mesh
{
	public class Animation {
		public int start;
		public int end;
	}
	
	private int duration = 1000; // milliseconds
	private boolean mirroredLoop = false;
	private int time = 0;

	private int lastKeyframe = 0;
	private int currentKeyframe = 0;

	private int direction = 1;
	private boolean directionBackwards = false;
	
	//
	private int startKeyframe;
	private int endKeyframe;
	private int length;
		
	private ObjectMap<String, Animation> animations;
	
	private String firstAnimation;
	
	public MorphAnimMesh(Geometry geometry, Material material)
	{			
		super(geometry, material);
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
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
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
//		Geometry geometry = (Geometry) this.getGeometry();
//
//		if ( this.animations == null) {
//			this.animations = new ObjectMap<String, Animation>();
//		}
//
//		String firstAnimation = null;
//
//		RegExp pattern = RegExp.compile("([a-z]+)(\\d+)");
//
//		for ( int i = 0, il = geometry.getMorphTargets().size(); i < il; i ++ )
//		{
//			Geometry.MorphTarget morph = geometry.getMorphTargets().get(i);
//
//			for (MatchResult result = pattern.exec(morph.name); result != null; result = pattern.exec(morph.name))
//			{
//			    String label = result.getGroup(1);
//			    String num = result.getGroup(2);
//
//				if ( ! this.animations.containsKey( label ) ) {
//					Animation animation = new Animation();
//					animation.start = Integer.MAX_VALUE;
//					animation.end = Integer.MIN_VALUE;
//					this.animations.put(label, animation);
//				}
//
//				Animation animation = this.animations.get( label );
//
//				if ( i < animation.start ) animation.start = i;
//				if ( i > animation.end ) animation.end = i;
//
//				if ( firstAnimation == null )
//					firstAnimation = label;
//
//			}
//		}
//
//		this.firstAnimation = firstAnimation;
//	}
	
	public void setAnimationLabel( String label, int start, int end ) 
	{
		if ( this.animations == null) {
			this.animations = new ObjectMap<String, Animation>();
		}
		
		Animation animation = new Animation();
		animation.start = start;
		animation.end = end;
		
		this.animations.put(label, animation);
	}

	public void playAnimation( String label, int fps ) 
	{
		Animation animation = this.animations.get( label );

		if ( animation != null ) 
		{
			this.setFrameRange( animation.start, animation.end );
			this.duration = 1000 * ( ( animation.end - animation.start ) / fps );
			this.time = 0;

		} 
		else 
		{
			Parallax.app.error("MorphanimMesh", "animation[" + label + "] undefined");
		}
	}

	public void updateAnimation( float delta ) 
	{
		if(this.getGeometry() == null)
			return;
		
		float frameTime = (float)this.duration / this.length;

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

		int keyframe = this.startKeyframe + (int) Mathematics.clamp(
				(int) Math.floor(this.time / frameTime), 0, this.length - 1);

		if ( keyframe != this.currentKeyframe ) 
		{
			this.morphTargetInfluences.set( this.lastKeyframe, 0.0f);
			this.morphTargetInfluences.set( this.currentKeyframe, 1.0f);

			this.morphTargetInfluences.set( keyframe, 0.0f );

			this.lastKeyframe = this.currentKeyframe;
			this.currentKeyframe = keyframe;
		}

		float mix = ( this.time % frameTime ) / frameTime;
		
		if ( this.directionBackwards )
			mix = 1 - mix;

		this.morphTargetInfluences.set( this.currentKeyframe, mix);
		this.morphTargetInfluences.set( this.lastKeyframe, 1.0f - mix);
	}
	
	public void interpolateTargets ( int a, int b, float t ) {

		List<Float> influences = this.morphTargetInfluences;

		for ( int i = 0, l = influences.size(); i < l; i ++ ) {

			influences.set( i, 0.0f );

		}

		if ( a > -1 ) influences.set( a, 1.0f - t);
		if ( b > -1 ) influences.set( b, t);

	}
	
	public MorphAnimMesh clone() {
		return clone(new MorphAnimMesh((Geometry) getGeometry(), getMaterial()));
	}
	
	public MorphAnimMesh clone( MorphAnimMesh object ) {

		super.clone(object);
		
		object.duration = this.duration;
		object.mirroredLoop = this.mirroredLoop;
		object.time = this.time;

		object.lastKeyframe = this.lastKeyframe;
		object.currentKeyframe = this.currentKeyframe;

		object.direction = this.direction;
		object.directionBackwards = this.directionBackwards;
		
		return object;
	}
	
	private void setFrameRange(int start, int end ) 
	{
		this.startKeyframe = start;
		this.endKeyframe = end;

		this.length = this.endKeyframe - this.startKeyframe + 1;
	}
}