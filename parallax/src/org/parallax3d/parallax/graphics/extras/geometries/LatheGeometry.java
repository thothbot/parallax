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

package org.parallax3d.parallax.graphics.extras.geometries;

import java.util.List;

import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.math.Vector2;

@ThreejsObject("THREE.LatheGeometry")
public final class LatheGeometry extends Geometry
{
	public LatheGeometry ( List<Vector2> points)
	{
		this(points, 12);
	}

	public LatheGeometry ( List<Vector2> points, int steps)
	{
		this(points, steps, 0, 2.0 * Math.PI);
	}

	public LatheGeometry ( List<Vector2> points, int segments, double phiStart, double phiLength )
	{
		this.fromBufferGeometry( new LatheBufferGeometry( points, segments, phiStart, phiLength ) );
		this.mergeVertices();
	}

}
