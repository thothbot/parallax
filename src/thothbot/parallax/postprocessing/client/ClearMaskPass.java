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

import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.textures.RenderTargetTexture;

public class ClearMaskPass extends Pass 
{	
	public ClearMaskPass() 
	{
		super();
		this.setEnabled(true);
	}

	@Override
	public void render(WebGLRenderer renderer, RenderTargetTexture writeBuffer, RenderTargetTexture readBuffer, float delta, boolean maskActive ) 
	{
		renderer.getGL().disable( GLenum.STENCIL_TEST.getValue() );
	}
}