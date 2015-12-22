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

package org.parallax3d.parallax.graphics.cameras;

public interface HasNearFar {


	/**
	 * Gets Camera frustum near plane.
	 */
	public float getNear();

	/**
	 * Sets Camera frustum near plane.
	 */
	public void setNear(float near);
	
	/**
	 * Gets Camera frustum far plane.
	 */
	public float getFar();

	/**
	 * Sets Camera frustum far plane.
	 */
	public void setFar(float far);
}