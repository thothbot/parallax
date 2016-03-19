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

package org.parallax3d.parallax.graphics.extras.objects;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@ThreejsObject("THREE.MorphBlendMesh")
public class MorphBlendMesh extends Mesh
{
	public class Animation {
		public int start;
		public int end;

		public int length;
		public int fps;
		public int duration;

		public int lastFrame = 0;
		public int currentFrame = 0;

		public boolean active = false;

		public int time = 0;
		public int direction = 1;
		public int weight = 1;

		public boolean directionBackwards = false;
		public boolean mirroredLoop = false;
	}

	FastMap<Animation> animationsMap = new FastMap<>();
	List<Animation> animationsList = new ArrayList<>();

	public MorphBlendMesh(Geometry geometry, Material material)
	{
		super(geometry, material);

		// prepare default animation
		// (all frames played together in 1 second)

		int numFrames = geometry.getMorphTargets().size();

		String name = "__default";

		int startFrame = 0;
		int endFrame = numFrames - 1;

		int fps = numFrames / 1;

		this.createAnimation( name, startFrame, endFrame, fps );
		this.setAnimationWeight( name, 1 );


	}

	protected void createAnimation( String name, int start, int end, int fps ) {

		Animation animation = new Animation();
		animation.start = start;
		animation.end = end;
		animation.length = end - start + 1;
		animation.fps = fps;
		animation.duration = ( end - start ) / fps;

		this.animationsMap.put( name, animation );
		this.animationsList.add( animation );

	}

	public void setAnimationDirectionForward( String name ) {

		if ( this.animationsMap.containsKey( name ) ) {

			Animation animation = this.animationsMap.get( name );

			animation.direction = 1;
			animation.directionBackwards = false;

		}

	}

	public void setAnimationDirectionBackward( String name ) {

		if ( this.animationsMap.containsKey( name ) ) {

			Animation animation = this.animationsMap.get( name );

			animation.direction = - 1;
			animation.directionBackwards = true;

		}

	}

	public void setAnimationFPS( String name, int fps ) {

		if ( this.animationsMap.containsKey( name ) ) {

			Animation animation = this.animationsMap.get( name );

			animation.fps = fps;
			animation.duration = ( animation.end - animation.start ) / animation.fps;

		}

	}

	public void setAnimationDuration( String name, int duration ) {

		if ( this.animationsMap.containsKey( name ) ) {

			Animation animation = this.animationsMap.get( name );

			animation.duration = duration;
			animation.fps = ( animation.end - animation.start ) / animation.duration;

		}

	}

	public void setAnimationWeight( String name, int weight ) {

		if ( this.animationsMap.containsKey( name ) ) {

			Animation animation = this.animationsMap.get( name );

			animation.weight = weight;

		}

	}

	public void setAnimationTime( String name, int time ) {

		if ( this.animationsMap.containsKey( name ) ) {

			Animation animation = this.animationsMap.get( name );

			animation.time = time;

		}

	}

	public int getAnimationTime( String name ) {

		int  time = 0;

		if ( this.animationsMap.containsKey( name ) ) {

			Animation animation = this.animationsMap.get( name );
			time = animation.time;

		}

		return time;

	}

	public int getAnimationDuration( String name ) {

		int duration = - 1;

		if ( this.animationsMap.containsKey( name ) ) {

			Animation animation = this.animationsMap.get( name );

			duration = animation.duration;

		}

		return duration;

	}

	public void playAnimation( String name ) {

		if ( this.animationsMap.containsKey( name ) ) {

			Animation animation = this.animationsMap.get( name );

			animation.time = 0;
			animation.active = true;

		} else {

			Log.warn( "MorphBlendMesh: animation[" + name + "] undefined in .playAnimation()" );

		}

	}

	public void stopAnimation( String name ) {

		if ( this.animationsMap.containsKey( name ) ) {

			Animation animation = this.animationsMap.get( name );

			animation.active = false;

		}

	}

	public void update( double delta )
	{
		for ( int i = 0, il = this.animationsList.size(); i < il; i ++ ) {

			Animation animation = this.animationsList.get(i);

			if ( ! animation.active ) continue;

			double frameTime = animation.duration / animation.length;

			animation.time += animation.direction * delta;

			if ( animation.mirroredLoop ) {

				if ( animation.time > animation.duration || animation.time < 0 ) {

					animation.direction *= - 1;

					if ( animation.time > animation.duration ) {

						animation.time = animation.duration;
						animation.directionBackwards = true;

					}

					if ( animation.time < 0 ) {

						animation.time = 0;
						animation.directionBackwards = false;

					}

				}

			} else {

				animation.time = animation.time % animation.duration;

				if ( animation.time < 0 ) animation.time += animation.duration;

			}

			int keyframe = (int) (animation.start + Mathematics.clamp( Math.floor( animation.time / frameTime ), 0, animation.length - 1 ));
			int weight = animation.weight;

			if ( keyframe != animation.currentFrame ) {

				this.getMorphTargetInfluences().set(animation.lastFrame, 0.);
				this.getMorphTargetInfluences().set(animation.currentFrame, 1. * weight);

				this.getMorphTargetInfluences().set(keyframe, 0.);

				animation.lastFrame = animation.currentFrame;
				animation.currentFrame = keyframe;

			}

			double mix = ( animation.time % frameTime ) / frameTime;

			if ( animation.directionBackwards ) mix = 1 - mix;

			if ( animation.currentFrame != animation.lastFrame ) {

				this.getMorphTargetInfluences().set(animation.currentFrame, mix * weight);
				this.getMorphTargetInfluences().set(animation.lastFrame, (1 - mix) * weight);

			} else {

				this.getMorphTargetInfluences().set(animation.currentFrame, 1. * weight);

			}

		}
	}
}
