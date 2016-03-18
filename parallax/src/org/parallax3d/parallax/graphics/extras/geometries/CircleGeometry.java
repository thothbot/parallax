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

import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.core.Geometry;

/**
 * <img src="http://thothbot.github.com/parallax/static/docs/circle.gif" />
 * 
 * <p>
 * Circle geometry
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.CircleGeometry")
public final class CircleGeometry extends Geometry
{
	public CircleGeometry()
	{
		this(50, 8);
	}

	public CircleGeometry(double radius, int segments)
	{
		this(radius, segments, 0, Math.PI * 2.0);
	}

	public CircleGeometry(double radius, int segments, double thetaStart, double thetaLength)
	{
		this.fromBufferGeometry( new CircleBufferGeometry( radius, segments, thetaStart, thetaLength ) );
	}
}
