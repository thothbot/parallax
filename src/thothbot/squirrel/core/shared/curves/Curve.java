/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.curves;

import java.util.ArrayList;
import java.util.List;

import thothbot.squirrel.core.shared.core.Vector;
import thothbot.squirrel.core.shared.core.Vector2f;

// Abstract Curve base class
public abstract class Curve
{

	public int __arcLengthDivisions = 0;
	public List<Float> cacheArcLengths;
	public boolean needsUpdate;

	/*
	 * Virtual base class method to overwrite and implement in subclasses - t [0 .. 1]
	 */
	public abstract Vector getPoint(float t);

	/*
	 * Get point at relative position in curve according to arc length - u [0 .. 1]
	 */
	public Vector getPointAt(float u)
	{
		float t = getUtoTmapping(u);
		return getPoint(t);
	}

	public List<Vector> getPoints()
	{
		return getPoints(5);
	}

	/*
	 * Get sequence of points using getPoint( t )
	 */
	public List<Vector> getPoints(int divisions)
	{

		List<Vector> pts = new ArrayList<Vector>();
		for (int d = 0; d <= divisions; d++)
			pts.add(this.getPoint((float)d / divisions));

		return pts;
	}

	public List<Vector> getSpacedPoints()
	{
		return getSpacedPoints(5);
	}

	/*
	 * Get sequence of points using getPointAt( u )
	 */
	public List<Vector> getSpacedPoints(int divisions)
	{
		List<Vector> pts = new ArrayList<Vector>();

		for (int d = 0; d <= divisions; d++)
			pts.add(this.getPointAt((float)d / divisions));

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

		Vector last = this.getPoint(0.0f);
		float sum = 0;
		for (int p = 1; p <= divisions; p++) 
		{
			Vector current = getPoint((float)p / divisions);
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
			return (float)high / (arcLengths.size() - 1.0f);

		// we could get finer grain at lengths, or use simple interpolatation
		// between two points
		float lengthBefore = arcLengths.get(high);
		float lengthAfter = arcLengths.get(high + 1);

		float segmentLength = lengthAfter - lengthBefore;

		// determine where we are between the 'before' and 'after' points
		float segmentFraction = (targetArcLength - lengthBefore) / segmentLength;

		// add that fractional amount to t
		return ((float)high + segmentFraction) / (arcLengths.size() - 1.0f);
	}

	/*
	 * In 2D space, there are actually 2 normal vectors,
	 * and in 3D space, infinte
	 * TODO this should be depreciated.
	 */
	public Vector2f getNormalVector( float t ) 
	{
		Vector2f vec = (Vector2f) this.getTangent( t );
		return new Vector2f( -vec.getY() , vec.getX() );
	}

	
	/*
	 * Returns a unit vector tangent at t In case any sub curve does not
	 * implement its tangent / normal finding, we get 2 points with a small
	 * delta and find a gradient of the 2 points which seems to make a
	 * reasonable approximation
	 */
	public Vector getTangent(float t)
	{
		float delta = 0.0001f;
		float t1 = t - delta;
		float t2 = t + delta;

		// Capping in case of danger
		if (t1 < 0)
			t1 = 0;
		if (t2 > 1)
			t2 = 1;

		Vector pt1 = this.getPoint(t1);
		Vector pt2 = this.getPoint(t2);

		Vector vec = pt2.clone();
		vec.sub(pt1);
		vec.normalize();
		return vec;
	}

	public Vector getTangentAt(float u)
	{
		return this.getTangent(this.getUtoTmapping(u));
	}
}
