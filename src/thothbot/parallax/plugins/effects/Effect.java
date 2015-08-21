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

package thothbot.parallax.plugins.effects;

import thothbot.parallax.core.client.events.HasEventBus;
import thothbot.parallax.core.client.events.ViewportResizeEvent;
import thothbot.parallax.core.client.events.ViewportResizeHandler;
import thothbot.parallax.core.client.renderers.Plugin;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.scenes.Scene;

import com.google.gwt.event.shared.HandlerRegistration;

public abstract class Effect extends Plugin implements HasEventBus, ViewportResizeHandler {

	public Effect(WebGLRenderer renderer, Scene scene) {
		super(renderer, scene);
		
		addViewportResizeHandler(this);
	}

	@Override
	public TYPE getType() {
		return Plugin.TYPE.BASIC_RENDER;
	}
	
	public HandlerRegistration addViewportResizeHandler(ViewportResizeHandler handler) 
	{
		return EVENT_BUS.addHandler(ViewportResizeEvent.TYPE, handler); 
	}
	
	@Override
	public abstract void onResize(ViewportResizeEvent event);
}
