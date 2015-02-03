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

import java.util.List;

import thothbot.parallax.core.client.renderers.Plugin;
import thothbot.parallax.core.client.renderers.Plugin.TYPE;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.lights.Light;
import thothbot.parallax.core.shared.scenes.Scene;

public abstract class Effect extends Plugin {

	public Effect(WebGLRenderer renderer, Scene scene) {
		super(renderer, scene);
	}

	@Override
	public TYPE getType() {
		return Plugin.TYPE.POST_RENDER;
	}
}
