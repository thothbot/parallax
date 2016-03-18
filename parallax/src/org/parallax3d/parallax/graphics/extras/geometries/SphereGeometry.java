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
 * <img src="http://thothbot.github.com/parallax/static/docs/sphere.gif" />
 * <p>
 * The Sphere geometry
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.SphereGeometry")
public final class SphereGeometry extends Geometry
{
	public SphereGeometry()
	{
		this(50);
	}

	public SphereGeometry(double radius)
	{
		this(radius, 8, 6);
	}

	public SphereGeometry(double radius, int segmentsWidth, int segmentsHeight)
	{
		this(radius, segmentsWidth, segmentsHeight, 0.0, Math.PI * 2.0 );
	}

	public SphereGeometry(double radius, int segmentsWidth, int segmentsHeight, double phiStart, double phiLength)
	{
		this(radius, segmentsWidth, segmentsHeight, phiStart, phiLength, 0.0, Math.PI);
	}

	public SphereGeometry(double radius, int widthSegments, int heightSegments, double phiStart, double phiLength, double thetaStart, double thetaLength)
	{
		this.fromBufferGeometry( new SphereBufferGeometry( radius, widthSegments, heightSegments, phiStart, phiLength, thetaStart, thetaLength ) );
	}
}
