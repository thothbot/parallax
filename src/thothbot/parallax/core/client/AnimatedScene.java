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
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.client;

import thothbot.parallax.core.client.context.Canvas3d;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.cameras.PerspectiveCamera;
import thothbot.parallax.core.shared.scenes.Scene;

import com.google.gwt.core.client.Duration;

/**
 * The class to set up {@link Scene} for the {@link WebGLRenderer} 
 * in the {@link Canvas3d} context.
 * 
 * @author thothbot
 *
 */
public abstract class AnimatedScene extends Animation
{
	/**
	 * Basically use for the debugger. Check if needed.
	 * 
	 * @author thothbot
	 *
	 */
	public static interface AnimatedSceneCallback
	{
		/**
		 * Called when {@link #onUpdate()} called.
		 */
		public void onUpdate();
	}

	private WebGLRenderer renderer;
	private Scene scene;
	private AnimatedSceneCallback animatedSceneCallback;

	/**
	 * Gets the main {@link Scene} object.
	 * 
	 * @return the Scene object.
	 */
	public Scene getScene() 
	{
		return this.scene;
	}

	/**
	 * Gets {@link WebGLRenderer} associated with the AnimatedScene.
	 * 
	 * @return the {@link WebGLRenderer} instance.
	 */
	public WebGLRenderer getRenderer() 
	{
		return this.renderer;
	}

	/**
	 * Initialize the scene.
	 * 
	 * @param renderer the {@link WebGLRenderer} instance.
	 * @param animatedSceneCallback this parameter used for updating debug info. Can be null.
	 */
	public void init(WebGLRenderer renderer, AnimatedSceneCallback animatedSceneCallback)
	{
		if(getRenderer() != null)
			return;

		this.renderer = renderer;
		this.scene = new Scene();

		this.animatedSceneCallback = animatedSceneCallback;		
	}

	protected abstract void onUpdate(double duration);
	
	@Override
	protected void onRefresh(double duration)
	{
		getRenderer().getInfo().getTimer().render = new Duration();
		onUpdate(duration);
		
		animatedSceneCallback.onUpdate();
	}
	
	/**
	 * Called when size has been changed on {@link Canvas3d} element. 
	 * This method basically used for updating aspect ratio for {@link PerspectiveCamera}.
	 */
	protected void onResize() {
		// Empty
	}
}
