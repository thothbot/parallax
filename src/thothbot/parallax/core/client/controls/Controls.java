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

package thothbot.parallax.core.client.controls;

import thothbot.parallax.core.client.context.Canvas3d;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.Object3D;

import com.google.gwt.user.client.ui.Widget;

/**
 * The abstract control implementation.
 * 
 * @author thothbot
 *
 */
public abstract class Controls
{
	private Object3D object;
	private Widget widget;
	
	/**
	 * The constructor will create a {@link Controls} instance.
	 * 
	 * @param object the {@link Object3D} which will be controlled. 
	 * 				For example {@link Camera} object.
	 * @param widget the Widget where to listen control commands. 
	 * 				Basically {@link Canvas3d} widget.
	 */
	public Controls(Object3D object, Widget widget)
	{
		this.object = object;
		this.widget = widget;
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
	
	/**
	 * Gets widget.
	 * 
	 * @return the Widget.
	 */
	public Widget getWidget()
	{
		return this.widget;
	}
}
