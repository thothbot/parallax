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
 * <img src="http://thothbot.github.com/parallax/static/docs/cylinder.gif" />
 * <p>
 * Cylinder geometry
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.CylinderGeometry")
public final class CylinderGeometry extends Geometry
{

	public CylinderGeometry()
	{
		this(20, 20, 100, 8, 1);
	}

	public CylinderGeometry(double radiusTop, double radiusBottom, double height, int segmentsRadius, int segmentsHeight)
	{
		this(radiusTop, radiusBottom, height, segmentsRadius, segmentsHeight, false, 0, Math.PI * 2);
	}

	public CylinderGeometry(double radiusTop, double radiusBottom, double height, int radialSegments, int heightSegments, boolean openEnded, double thetaStart, double thetaLength)
	{
		this.fromBufferGeometry( new CylinderBufferGeometry( radiusTop, radiusBottom, height, radialSegments, heightSegments, openEnded, thetaStart, thetaLength ) );
		this.mergeVertices();
	}
}
