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

package org.parallax3d.parallax.graphics.extras.core;

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.parallax.system.ThreeJsObject;
import org.parallax3d.parallax.math.Vector2;

// Abstract Curve base class
@ThreeJsObject("THREE.Curve")
public abstract class Curve
{
	public int __arcLengthDivisions = 0;
	public List<Float> cacheArcLengths;
	public boolean needsUpdate;

	/*
	 * Virtual base class method to overwrite and implement in subclasses - t [0 .. 1]
	 */
	public abstract Vector2 getPoint(float t);

	/*
	 * Get point at relative position in curve according to arc length - u [0 .. 1]
	 */
	public Vector2 getPointAt(float u)
	{
		float t = getUtoTmapping(u);

		return getPoint(t);
	}

	public List<Vector2> getPoints()
	{
		return getPoints(5);
	}

	/*
	 * Get sequence of points using getPoint( t )
	 */
	public List<Vector2> getPoints(int divisions)
	{
		List<Vector2> pts = new ArrayList<Vector2>();
		for (int d = 0; d <= divisions; d++)
			pts.add(this.getPoint(d / (float)divisions));

		return pts;
	}

	public List<Vector2> getSpacedPoints()
	{
		return getSpacedPoints(5);
	}

	/*
	 * Get sequence of points using getPointAt( u )
	 */
	public List<Vector2> getSpacedPoints(int divisions)
	{
		List<Vector2> pts = new ArrayList<Vector2>();

		for (int d = 0; d <= divisions; d++)
			pts.add(this.getPointAt(d / (float)divisions));

		return pts;
	}

	/*
	 * Get total curve arc length
	 */
	public float getLength()
	{
		List<Float> lengths = this.getLengths();
		return lengths.get(lengths.size() - 1);
	}

	public List<Float> getLengths()
	{
		if (this.__arcLengthDivisions > 0)
			return getLengths(this.__arcLengthDivisions);

		return getLengths(200);
	}

	/*
	 * Get list of cumulative segment lengths
	 */
	public List<Float> getLengths(int divisions)
	{
		if (this.cacheArcLengths != null 
				&& (this.cacheArcLengths.size() == (divisions + 1))
				&& !this.needsUpdate
		){
			return this.cacheArcLengths;
		}

		this.needsUpdate = false;

		List<Float> cache = new ArrayList<Float>();
		cache.add(0.0f);

		Vector2 last = this.getPoint(0.0f);
		float sum = 0;
		for (int p = 1; p <= divisions; p++) 
		{
			Vector2 current = getPoint(p / (float)divisions);
			sum += current.distanceTo(last);

			last = current;
			cache.add(sum);
		}

		this.cacheArcLengths = cache;
		return cache;
	}

	public void updateArcLengths()
	{
		this.needsUpdate = true;
		this.getLengths();
	}

	/*
	 * Given u ( 0 .. 1 ), get a t to find p. This gives you points which are
	 * equi distance
	 */
	public float getUtoTmapping(float u)
	{
		List<Float> arcLengths = this.getLengths();
		return getUtoTmapping(u, u * arcLengths.get(arcLengths.size() - 1));
	}

	public float getUtoTmapping(float u, float distance)
	{
		List<Float> arcLengths = this.getLengths();

		// The targeted u distance value to get
		float targetArcLength = distance;

		// binary search for the index with largest value smaller than target u
		// distance
		int low = 0;
		int high = arcLengths.size() - 1;
		float comparison;

		while (low <= high) 
		{
			/*
			 * less likely to overflow, though probably not issue
			 * here, JS doesn't really have integers, all numbers are floats
			 */
			int i = (int) Math.floor(low + (high - low) / 2.0);

			comparison = arcLengths.get(i) - targetArcLength;

			if (comparison < 0) 
			{
				low = i + 1;
				continue;
			} 
			else if (comparison > 0) 
			{
				high = i - 1;
				continue;
			} 
			else 
			{
				high = i;
				break;
			}
		}
	
		if (arcLengths.get(high) == targetArcLength)
		{
			return high / (float)(arcLengths.size() - 1);
		}

		// we could get finer grain at lengths, or use simple interpolatation
		// between two points
		float lengthBefore = arcLengths.get(high);
		float lengthAfter  = arcLengths.get(high + 1);

		float segmentLength = lengthAfter - lengthBefore;

		// determine where we are between the 'before' and 'after' points
		float segmentFraction = (targetArcLength - lengthBefore) / (float)segmentLength;

		// add that fractional amount to t
		return (high + segmentFraction) / ((float)arcLengths.size() - 1.0f);
	}

	/*
	 * In 2D space, there are actually 2 normal vectors,
	 * and in 3D space, infinte
	 * TODO this should be depreciated.
	 */
	public Vector2 getNormalVector( float t )
	{
		Vector2 vec = (Vector2) this.getTangent( t );
		return new Vector2( -vec.getY() , vec.getX() );
	}

	
	/*
	 * Returns a unit vector tangent at t In case any sub curve does not
	 * implement its tangent / normal finding, we get 2 points with a small
	 * delta and find a gradient of the 2 points which seems to make a
	 * reasonable approximation
	 */
	public Vector2 getTangent(float t)
	{
		float delta = 0.0001f;
		float t1 = t - delta;
		float t2 = t + delta;

		// Capping in case of danger
		if (t1 < 0)
			t1 = 0;
		if (t2 > 1)
			t2 = 1;

		Vector2 pt1 = this.getPoint(t1);
		Vector2 pt2 = this.getPoint(t2);

		Vector2 vec = pt2.clone();
		vec.sub(pt1);
		vec.normalize();
		return vec;
	}

	public Vector2 getTangentAt(float u)
	{
		return this.getTangent(this.getUtoTmapping(u));
	}
}
