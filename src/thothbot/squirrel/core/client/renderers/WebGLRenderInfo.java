/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.client.renderers;

import com.google.gwt.core.client.Duration;

public class WebGLRenderInfo
{

	public class WebGLRenderTimer
	{
		public Duration render = new Duration();
	}

	public class WebGLRenderInfoRender
	{
		public int calls = 0;
		public int vertices = 0;
		public int faces = 0;
		public int points = 0;
	}

	public class WebGLRenderInfoMemory
	{
		public int programs = 0;
		public int geometries = 0;
		public int textures = 0;
	}

	private WebGLRenderInfoRender render;
	private WebGLRenderInfoMemory memory;
	private WebGLRenderTimer timer;
	
	public WebGLRenderInfo() 
	{
		this.render = new WebGLRenderInfoRender();
		this.memory = new WebGLRenderInfoMemory();
		this.timer = new WebGLRenderTimer();
	}

	public WebGLRenderInfoRender getRender()
	{
		return render;
	}

	public WebGLRenderInfoMemory getMemory()
	{
		return memory;
	}

	public WebGLRenderTimer getTimer()
	{
		return timer;
	}
}
