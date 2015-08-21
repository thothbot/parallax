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

package thothbot.parallax.core.client;

import thothbot.parallax.core.client.context.Canvas3d;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
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
	public static interface AnimationUpdateHandler
	{
		/**
		 * Called when {@link #onUpdate()} called.
		 */
		public void onUpdate(double duration);
	}

	private RenderingPanel renderingPanel;
	private Scene scene;
	private AnimationUpdateHandler animationUpdateHandler;

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
		return this.renderingPanel.getRenderer();
	}
	
	public Canvas3d getCanvas() 
	{
		return this.renderingPanel.getCanvas();
	}

	/**
	 * Initialize the scene.
	 * 
	 * @param renderingPanel
	 * @param animationUpdateHandler this parameter used for updating debug info. Can be null.
	 */
	public void init(RenderingPanel renderingPanel, AnimationUpdateHandler animationUpdateHandler)
	{
		this.renderingPanel = renderingPanel;
		this.scene = new Scene();

		this.animationUpdateHandler = animationUpdateHandler;		
	}

	protected abstract void onUpdate(double duration);
	
	@Override
	protected void onRefresh(double duration)
	{
		getRenderer().getInfo().getTimer().render = new Duration();
		onUpdate(duration);
		
		animationUpdateHandler.onUpdate(duration);
	}
}
