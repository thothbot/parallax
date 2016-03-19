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

import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * <img src="http://thothbot.github.com/parallax/static/docs/torus_knot.gif" />
 * <p>
 * Torus knot geometry
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.TorusKnotGeometry")
public final class TorusKnotGeometry extends Geometry
{

	public TorusKnotGeometry()
	{
		this(100, 40, 64, 8);
	}

	public TorusKnotGeometry(double radius)
	{
		this(radius, 40, 64, 8);
	}

	public TorusKnotGeometry(double radius, double tube, int tubularSegments, int radialSegments)
	{
		this(radius, tube, tubularSegments, radialSegments, 2, 3);
	}

	public TorusKnotGeometry(double radius, double tube, int tubularSegments, int radialSegments, int p, int q )
	{
		this.fromBufferGeometry( new TorusKnotBufferGeometry( radius, tube, tubularSegments, radialSegments, p, q ) );
		this.mergeVertices();
	}
}
