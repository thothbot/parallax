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

	/*
	 * get {@link WebGLRenderer} object
	 */
	protected WebGLRenderer getRenderer() 
	{
		return this.renderer;
	}
	
	/*
	 * Load default {@link Scene} for this Rendering Scene
	 */
	protected void loadScene()
	{
		setScene(new Scene());
	}

	/*
	 * Set {@link Scene}
	 * 
	 * @param scene
	 * 			{@link Scene} object
	 */
	protected void setScene(Scene scene)
	{
		this.scene = scene;
	}
	
	/*
	 * get {@link Scene} object
	 */
	public Scene getScene() 
	{
		return this.scene;
	}

	/*
	 * Load default {@link Camera} for this Rendering Scene
	 */
	protected abstract void loadCamera();
	
	/*
	 * Set {@link Camera}
	 * 
	 * @param camera
	 * 			{@link Camera} object
	 */
	protected void setCamera(Camera camera)
	{
		this.camera = camera;
	}

	/*
	 * get {@link Camera} object
	 */
	public Camera getCamera() 
	{
		return this.camera;
	}
	
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
}
