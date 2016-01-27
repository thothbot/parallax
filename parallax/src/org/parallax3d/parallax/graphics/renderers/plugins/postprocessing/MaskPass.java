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

package org.parallax3d.parallax.graphics.renderers.plugins.postprocessing;

import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.system.gl.enums.EnableCap;
import org.parallax3d.parallax.system.gl.enums.StencilFunction;
import org.parallax3d.parallax.system.gl.enums.StencilOp;

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
	
	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}
			
	@Override
	public void render (Postprocessing ecffectComposer, double delta, boolean maskActive) 
	{
		// don't update color or depth
		ecffectComposer.getRenderer().gl.glColorMask( false, false, false, false );
		ecffectComposer.getRenderer().gl.glDepthMask( false );

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

		ecffectComposer.getRenderer().gl.glEnable( EnableCap.STENCIL_TEST.getValue() );
		ecffectComposer.getRenderer().gl.glStencilOp( StencilOp.REPLACE.getValue(), StencilOp.REPLACE.getValue(), StencilOp.REPLACE.getValue() );
		ecffectComposer.getRenderer().gl.glStencilFunc( StencilFunction.ALWAYS.getValue(), writeValue, 0xffffffff );
		ecffectComposer.getRenderer().gl.glClearStencil( clearValue );

		// draw into the stencil buffer
		ecffectComposer.getRenderer().render( this.scene, this.camera, ecffectComposer.getReadBuffer(), this.clear );
		ecffectComposer.getRenderer().render( this.scene, this.camera, ecffectComposer.getWriteBuffer(), this.clear );

		// re-enable update of color and depth
		ecffectComposer.getRenderer().gl.glColorMask( true, true, true, true );
		ecffectComposer.getRenderer().gl.glDepthMask( true );

		// only render where stencil is set to 1
		ecffectComposer.getRenderer().gl.glStencilFunc( StencilFunction.EQUAL.getValue(), 1, 0xffffffff );  // draw if == 1
		ecffectComposer.getRenderer().gl.glStencilOp( StencilOp.KEEP.getValue(), StencilOp.KEEP.getValue(), StencilOp.KEEP.getValue() );
	}
	
	@Override
	public boolean isMaskActive()
	{
		return true;
	}

}
