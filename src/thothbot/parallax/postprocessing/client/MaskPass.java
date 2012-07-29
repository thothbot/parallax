/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
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

package thothbot.parallax.postprocessing.client;

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.renderers.WebGLRenderTarget;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.scenes.Scene;

public class MaskPass extends Pass
{
	private Scene scene;
	
	private Camera camera;
	private boolean clear = true;
	private boolean inverse = false;
	
	public MaskPass( Scene scene, Camera camera ) 
	{
		this.scene = scene;
		this.camera = camera;
		this.setEnabled(true);
	}
		
	@Override
	public void render (WebGLRenderTarget writeBuffer, WebGLRenderTarget readBuffer, float delta, boolean maskActive) 
	{
		// don't update color or depth

		getRenderer().getGL().colorMask( false, false, false, false );
		getRenderer().getGL().depthMask( false );

		// set up stencil

		int writeValue, clearValue;

		if ( this.inverse ) 
		{
			writeValue = 0;
			clearValue = 1;
		} 
		else 
		{
			writeValue = 1;
			clearValue = 0;
		}

		getRenderer().getGL().enable( WebGLRenderingContext.STENCIL_TEST );
		getRenderer().getGL().stencilOp( WebGLRenderingContext.REPLACE, WebGLRenderingContext.REPLACE, WebGLRenderingContext.REPLACE );
		getRenderer().getGL().stencilFunc( WebGLRenderingContext.ALWAYS, writeValue, 0xffffffff );
		getRenderer().getGL().clearStencil( clearValue );

		// draw into the stencil buffer

		getRenderer().render( this.scene, this.camera, readBuffer, this.clear );
		getRenderer().render( this.scene, this.camera, writeBuffer, this.clear );

		// re-enable update of color and depth

		getRenderer().getGL().colorMask( true, true, true, true );
		getRenderer().getGL().depthMask( true );

		// only render where stencil is set to 1

		getRenderer().getGL().stencilFunc( WebGLRenderingContext.EQUAL, 1, 0xffffffff );  // draw if == 1
		getRenderer().getGL().stencilOp( WebGLRenderingContext.KEEP, WebGLRenderingContext.KEEP, WebGLRenderingContext.KEEP );
	}
	
	@Override
	public boolean isMaskActive()
	{
		return true;
	}
}
