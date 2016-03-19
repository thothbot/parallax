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
 * <img src="http://thothbot.github.com/parallax/static/docs/cube.gif" />
 * 
 * <p>
 * Cube geometry
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.BoxGeometry")
public final class BoxGeometry extends Geometry
{
	public BoxGeometry(double width, double height, double depth)
	{
		this( width, height, depth, 1, 1, 1);
	}

	public BoxGeometry(double width, double height, double depth, int widthSegments, int heightSegments, int depthSegments)
	{
		this.fromBufferGeometry( new BoxBufferGeometry( width, height, depth, widthSegments, heightSegments, depthSegments ) );
		this.mergeVertices();
	}
}
