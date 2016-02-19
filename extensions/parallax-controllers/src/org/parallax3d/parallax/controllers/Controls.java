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

package org.parallax3d.parallax.controllers;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.core.Object3D;

/**
 * The abstract control implementation.
 * 
 * @author thothbot
 *
 */
public abstract class Controls
{
	private Object3D object;
	private RenderingContext context;
	
	/**
	 * The constructor will create a {@link Controls} instance.
	 * 
	 * @param object the {@link Object3D} which will be controlled. 
	 * 				For example {@link Camera} object.
	 */
	public Controls(Object3D object, RenderingContext context)
	{
		this.object = object;
		this.context = context;
	}
	
	/**
	 * Gets controlled instance.
	 * 
	 * @return the {@link Object3D}.
	 */
	public Object3D getObject()
	{
		return this.object;
	}

	public RenderingContext getContext() {
		return this.context;
	}
}
