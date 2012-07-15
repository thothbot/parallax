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

package thothbot.squirrel.core.client;

import thothbot.squirrel.core.client.renderers.WebGLRenderer;
import thothbot.squirrel.core.shared.cameras.Camera;
import thothbot.squirrel.core.shared.cameras.PerspectiveCamera;
import thothbot.squirrel.core.shared.scenes.Scene;

import com.google.gwt.core.client.Duration;

public abstract class RenderingScene extends Rendering
{
	public static interface RenderingSceneCallback
	{
		public void onUpdate();
	}

	private WebGLRenderer renderer;
	private Scene scene;
	private Camera camera;
	private RenderingSceneCallback renderingSceneCallback;

	public WebGLRenderer getRenderer() 
	{
		return this.renderer;
	}
	
	/**
	 * Define the main {@link Scene} for the Rendering. 
	 */
	protected void loadScene()
	{
		setScene(new Scene());
	}

	/**
	 * Sets the main {@link Scene} object.
	 * 
	 * @param scene the Scene instance.
	 */
	protected void setScene(Scene scene)
	{
		this.scene = scene;
	}
	
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
	 * There should be defined default camera for the current {@link Scene},
	 */
	protected abstract void loadCamera();

	/**
	 * Sets default {@link Camera} to the current {@Scene}
	 * 
	 * @param camera the Camera instance.
	 */
	protected void setCamera(Camera camera)
	{
		this.camera = camera;
	}

	/**
	 * Gets default {@link Camera} object associated with the current {@link Scene}.
	 * 
	 * @return the Camera instance
	 */
	public Camera getCamera() 
	{
		return this.camera;
	}
	
	/**
	 * Initialize the scene.
	 * 
	 * @param renderer the renderer instance
	 * @param renderingSceneCallback this parameter used for updating debug info. Can be null.
	 */
	public void init(WebGLRenderer renderer, RenderingSceneCallback renderingSceneCallback)
	{
		if(this.renderer != null)
			return;

		this.renderer = renderer;

		loadScene();
		loadCamera();

		this.renderingSceneCallback = renderingSceneCallback;		
	}

	@Override
	protected void onUpdate(double duration)
	{
		this.renderer.getInfo().getTimer().render = new Duration();
		this.renderer.render(getScene(), getCamera());
		
		renderingSceneCallback.onUpdate();
	}
	
	/**
	 * Called when size has been changed on Canvas element. 
	 * This method basically used for updating aspect ratio for {@link PerspectiveCamera}.
	 */
	protected void onResize()
	{
		if(getCamera() != null && getCamera().getClass() == PerspectiveCamera.class )
		{
			((PerspectiveCamera)getCamera()).setAspectRatio(
					getRenderer().getCanvas().getAspectRation());			
		}
	}
}
