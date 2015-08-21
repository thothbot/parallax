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

package thothbot.parallax.core.client.renderers;

import thothbot.parallax.core.client.debugger.Debugger;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.scenes.Scene;

import com.google.gwt.core.client.Duration;

/**
 * Th class with a series of statistical information about the graphics board memory and 
 * the rendering process. Used id {@link Debugger}.
 *  
 * @author thothbot
 *
 */
public class WebGlRendererInfo
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
	 * Default constructor for {@link WebGlRendererInfo} initialization.
	 */
	public WebGlRendererInfo() 
	{
		this.render = new WebGLRenderInfoRender();
		this.memory = new WebGLRenderInfoMemory();
		this.timer = new WebGLRenderTimer();
	}

	/**
	 * Gets {@link WebGlRendererInfo.WebGLRenderInfoRender} information.
	 */
	public WebGLRenderInfoRender getRender()
	{
		return render;
	}

	/**
	 * Gets {@link WebGlRendererInfo.WebGLRenderInfoMemory} information.
	 */
	public WebGLRenderInfoMemory getMemory()
	{
		return memory;
	}

	/**
	 * Gets {@link WebGlRendererInfo.WebGLRenderTimer} information.
	 */
	public WebGLRenderTimer getTimer()
	{
		return timer;
	}
}
