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
import thothbot.parallax.core.shared.cameras.Camera;
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
public abstract class RenderingScene extends Rendering
{
	/**
	 * Basically use for the debugger. Check if needed.
	 * 
	 * @author thothbot
	 *
	 */
	public static interface RenderingSceneCallback
	{
		/**
		 * Called when {@link #onUpdate()} called.
		 */
		public void onUpdate();
	}

	private WebGLRenderer renderer;
	private Scene scene;
	private Camera camera;
	private RenderingSceneCallback renderingSceneCallback;

	/**
	 * Gets {@link WebGLRenderer} associated with the RenderingScene.
	 * 
	 * @return the {@link WebGLRenderer} instance.
	 */
	public WebGLRenderer getRenderer() 
	{
		return this.renderer;
	}
	
	/**
	 * Loads the main {@link Scene} for the Rendering. 
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
	 * Loads default camera for the current {@link Scene},
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
	 * @param renderer the {@link WebGLRenderer} instance.
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
	 * Called when size has been changed on {@link Canvas3d} element. 
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
