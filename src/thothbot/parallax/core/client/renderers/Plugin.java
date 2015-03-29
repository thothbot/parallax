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

import java.util.List;

import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.lights.Light;
import thothbot.parallax.core.shared.scenes.Scene;

public abstract class Plugin 
{
	public enum TYPE {
		BASIC_RENDER,
		PRE_RENDER,
		POST_RENDER,
	};

	private boolean isEnabled = true;
	private boolean isRendering;
	
	protected WebGLRenderer renderer;
	protected Scene scene;

	public Plugin(WebGLRenderer renderer, Scene scene) 
	{
		this.renderer = renderer;
		this.scene = scene;
		renderer.addPlugin(this);
	}

	public WebGLRenderer getRenderer() {
		return this.renderer;
	}

	public Scene getScene() {
		return this.scene;
	}
	
	public boolean isMulty() {
		return false;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public boolean isRendering() {
		return this.isRendering;
	}
	
	public void setRendering(boolean isRendering) {
		this.isRendering = isRendering;
	}

	public abstract Plugin.TYPE getType();

	public abstract void render( Camera camera, List<Light> lights, int currentWidth, int currentHeight );

	public void deallocate() {
		
	}
}
