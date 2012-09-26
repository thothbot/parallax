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

package thothbot.parallax.plugin.postprocessing.client;

import thothbot.parallax.core.client.gl2.enums.EnableCap;
import thothbot.parallax.core.client.gl2.enums.GLConstants;
import thothbot.parallax.core.client.gl2.enums.StencilFunction;
import thothbot.parallax.core.client.gl2.enums.StencilOp;
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
	public void render (Postprocessing ecffectComposer, double delta, boolean maskActive) 
	{
		// don't update color or depth
		ecffectComposer.getRenderer().getGL().colorMask( false, false, false, false );
		ecffectComposer.getRenderer().getGL().depthMask( false );

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

		ecffectComposer.getRenderer().getGL().enable( EnableCap.STENCIL_TEST );
		ecffectComposer.getRenderer().getGL().stencilOp( StencilOp.REPLACE, StencilOp.REPLACE, StencilOp.REPLACE );
		ecffectComposer.getRenderer().getGL().stencilFunc( StencilFunction.ALWAYS, writeValue, 0xffffffff );
		ecffectComposer.getRenderer().getGL().clearStencil( clearValue );

		// draw into the stencil buffer
		ecffectComposer.getRenderer().render( this.scene, this.camera, ecffectComposer.getReadBuffer(), this.clear );
		ecffectComposer.getRenderer().render( this.scene, this.camera, ecffectComposer.getWriteBuffer(), this.clear );

		// re-enable update of color and depth
		ecffectComposer.getRenderer().getGL().colorMask( true, true, true, true );
		ecffectComposer.getRenderer().getGL().depthMask( true );

		// only render where stencil is set to 1
		ecffectComposer.getRenderer().getGL().stencilFunc( StencilFunction.EQUAL, 1, 0xffffffff );  // draw if == 1
		ecffectComposer.getRenderer().getGL().stencilOp( StencilOp.KEEP, StencilOp.KEEP, StencilOp.KEEP );
	}
	
	@Override
	public boolean isMaskActive()
	{
		return true;
	}
}
