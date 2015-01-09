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

package thothbot.parallax.plugins.postprocessing;

import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.scenes.Scene;

public class RenderPass extends Pass
{
	private Scene scene;
	private Camera camera;
	private Material overrideMaterial;
	
	private Color clearColor;
	private double clearAlpha;
	
	private Color oldClearColor;
	private double oldClearAlpha;
	
	private boolean clear = true;
	
	public RenderPass ( Scene scene, Camera camera )
	{
		this(scene, camera, null, null);
	}

	public RenderPass ( Scene scene, Camera camera, Material overrideMaterial, Color clearColor )
	{
		this(scene, camera, overrideMaterial, clearColor, 1.0);
	}
	
	public RenderPass ( Scene scene, Camera camera, Material overrideMaterial, Color clearColor, double clearAlpha ) 
	{
		this.scene = scene;
		this.camera = camera;

		this.overrideMaterial = overrideMaterial;

		this.clearColor = clearColor;
		this.clearAlpha = clearAlpha;

		this.oldClearColor = new Color();
		this.oldClearAlpha = 1.0;

		this.setEnabled(true);
		this.setNeedsSwap(false);
	}
	
	public void setClear(boolean clear) {
		this.clear = clear;
	}

	@Override
	public void render(Postprocessing postprocessing, double delta, boolean maskActive)
	{
//		this.scene.overrideMaterial = this.overrideMaterial;

		if ( this.clearColor != null ) 
		{

			this.oldClearColor.copy( postprocessing.getRenderer().getClearColor() );
			this.oldClearAlpha = postprocessing.getRenderer().getClearAlpha();

			postprocessing.getRenderer().setClearColor( this.clearColor, this.clearAlpha );

		}

		postprocessing.getRenderer().render( this.scene, this.camera, postprocessing.getReadBuffer(), this.clear );

		if ( this.clearColor != null)
			postprocessing.getRenderer().setClearColor( this.oldClearColor, this.oldClearAlpha );

//		this.scene.overrideMaterial = null;
	}
}
