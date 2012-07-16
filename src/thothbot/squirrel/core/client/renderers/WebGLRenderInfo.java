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

import thothbot.squirrel.core.client.widget.Debugger;

import com.google.gwt.core.client.Duration;

/**
 * Th class with a series of statistical information about the graphics board memory and 
 * the rendering process. Used id {@link Debugger}.
 *  
 * @author thothbot
 *
 */
public class WebGLRenderInfo
{

	/**
	 * Information about duration for some procedures.  
	 */
	public class WebGLRenderTimer
	{
		/**
		 * {@link WebGLRenderer#render(Scene, Camera)} duration.
		 */
		public Duration render = new Duration();
	}

	/**
	 * Information about rendering primitives. 
	 */
	public class WebGLRenderInfoRender
	{
		/**
		 * How many types {@link WebGLRenderer#render(Scene, Camera)} was called.
		 */
		public int calls = 0;
		/**
		 * How many vertices were rendered.
		 */
		public int vertices = 0;
		/**
		 * How many faces were rendered.
		 */
		public int faces = 0;
		/**
		 * How many points were rendered.
		 */
		public int points = 0;
	}

	/**
	 * Information about used memory in the graphic card. 
	 */
	public class WebGLRenderInfoMemory
	{
		/**
		 * How many programs were loaded to the graphic card.  
		 */
		public int programs = 0;
		/**
		 * How many geometries were loaded to the graphic card.  
		 */
		public int geometries = 0;
		/**
		 *  How many textures were loaded to the graphic card.  
		 */
		public int textures = 0;
	}

	private WebGLRenderInfoRender render;
	private WebGLRenderInfoMemory memory;
	private WebGLRenderTimer timer;
	
	/**
	 * Default constructor for {@link WebGLRenderInfo} initialization.
	 */
	public WebGLRenderInfo() 
	{
		this.render = new WebGLRenderInfoRender();
		this.memory = new WebGLRenderInfoMemory();
		this.timer = new WebGLRenderTimer();
	}

	/**
	 * Gets {@link WebGLRenderInfo.WebGLRenderInfoRender} information.
	 */
	public WebGLRenderInfoRender getRender()
	{
		return render;
	}

	/**
	 * Gets {@link WebGLRenderInfo.WebGLRenderInfoMemory} information.
	 */
	public WebGLRenderInfoMemory getMemory()
	{
		return memory;
	}

	/**
	 * Gets {@link WebGLRenderInfo.WebGLRenderTimer} information.
	 */
	public WebGLRenderTimer getTimer()
	{
		return timer;
	}
}
