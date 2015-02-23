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

package thothbot.parallax.core.shared.geometries;

import thothbot.parallax.core.shared.core.Geometry;

/**
 * <img src="http://thothbot.github.com/parallax/static/docs/plane.gif" />
 * <p>
 * Plane geometry
 * 
 * @author thothbot
 *
 */
public final class PlaneGeometry extends Geometry
{
	public PlaneGeometry(double width, double height) 
	{
		this(width, height, 1, 1);
	}

	public PlaneGeometry(double width, double height, int widthSegments, int heightSegments) 
	{
		super();

		this.fromBufferGeometry( new PlaneBufferGeometry( width, height, widthSegments, heightSegments ) );
	}
}
