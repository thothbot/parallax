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

package thothbot.parallax.core.shared.core;

import thothbot.parallax.core.shared.math.Vector3;

/**
 * Bounding Box is a container which used for quickly and easily 
 * determination of the location of an object in space.
 * 
 * @author thothbot
 *
 */
public class BoundingBox
{
	public Vector3 min;
	public Vector3 max;
	public Vector3 centroid;
	
	public BoundingBox() 
	{
		this(new Vector3(), new Vector3());
		
		this.centroid = new Vector3();
	}
	
	public BoundingBox(Vector3 min, Vector3 max) 
	{
		this.min = min;
		this.max = max;
	}
}
