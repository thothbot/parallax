/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.client.renderers;

import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.scenes.Scene;

public abstract class Plugin 
{
	public enum TYPE {
		PRE_RENDER,
		POST_RENDER,
	};

	private WebGLRenderer renderer;
	private Scene scene;

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

	public abstract Plugin.TYPE getType();

	public abstract void render( Scene scene, Camera camera, int currentWidth, int currentHeight );
}
