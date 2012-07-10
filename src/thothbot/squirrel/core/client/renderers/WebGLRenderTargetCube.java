/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
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

package thothbot.squirrel.core.client.renderers;

import java.util.ArrayList;
import java.util.List;

import thothbot.squirrel.core.client.gl2.WebGLFramebuffer;
import thothbot.squirrel.core.client.gl2.WebGLRenderbuffer;
import thothbot.squirrel.core.client.gl2.WebGLRenderingContext;
import thothbot.squirrel.core.shared.core.Mathematics;


public class WebGLRenderTargetCube extends WebGLRenderTarget
{
	public int activeCubeFace = 0;

	public List<WebGLFramebuffer> __webglFramebuffer;
	public List<WebGLRenderbuffer> __webglRenderbuffer;

	public WebGLRenderTargetCube(int width, int height, WebGLRenderTarget.WebGLRenderTargetOptions options) 
	{
		super(width, height, options);
	}

	@Override
	public void deallocate(WebGLRenderingContext gl)
	{
		if (this.__webglTexture == null)
			return;
	
		gl.deleteTexture(this.__webglTexture);
		for (int i = 0; i < 6; i++) 
		{
			gl.deleteFramebuffer(this.__webglFramebuffer.get(i));
			gl.deleteRenderbuffer(this.__webglRenderbuffer.get(i));
		}
	}
	
	@Override
	public WebGLFramebuffer getWebGLFramebuffer() 
	{
		return this.__webglFramebuffer.get( this.activeCubeFace );
	}
	
	@Override
	public void setRenderTarget(WebGLRenderingContext gl)
	{
		if (this.__webglFramebuffer != null)
			return;
	
		if (this.depthBuffer)
			this.depthBuffer = true;
		if (this.stencilBuffer)
			this.stencilBuffer = true;

		this.__webglTexture = gl.createTexture();

		// Setup texture, create render and frame buffers

		boolean isTargetPowerOfTwo = Mathematics.isPowerOfTwo(this.width)
				&& Mathematics.isPowerOfTwo(this.height);
		int glFormat = this.format.getValue();
		int glType = this.type.getValue();

		this.__webglFramebuffer = new ArrayList<WebGLFramebuffer>();
		this.__webglRenderbuffer = new ArrayList<WebGLRenderbuffer>();

		gl.bindTexture( WebGLRenderingContext.TEXTURE_CUBE_MAP, this.__webglTexture );
		//TODO: FIX setTextureParameters
		//setTextureParameters( _gl, WebGLRenderingContext.TEXTURE_CUBE_MAP, renderTarget.__webglTexture, isTargetPowerOfTwo );

		for ( int i = 0; i < 6; i ++ ) 
		{
			this.__webglFramebuffer.set( i, gl.createFramebuffer());
			this.__webglRenderbuffer.set( i, gl.createRenderbuffer());

			gl.texImage2D( WebGLRenderingContext.TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, glFormat, this.width, this.height, 0, glFormat, glType, null );

			this.setupFrameBuffer(gl, this.__webglFramebuffer.get( i ), WebGLRenderingContext.TEXTURE_CUBE_MAP_POSITIVE_X + i);
			this.setupRenderBuffer(gl, this.__webglRenderbuffer.get( i ));
		}

		if ( isTargetPowerOfTwo ) gl.generateMipmap( WebGLRenderingContext.TEXTURE_CUBE_MAP );

		// Release everything
		gl.bindTexture( WebGLRenderingContext.TEXTURE_CUBE_MAP, null );
		gl.bindRenderbuffer(WebGLRenderingContext.RENDERBUFFER, null);
		gl.bindFramebuffer(WebGLRenderingContext.FRAMEBUFFER, null);
	}
	
	@Override
	public void updateRenderTargetMipmap(WebGLRenderingContext gl) 
	{	
		gl.bindTexture( WebGLRenderingContext.TEXTURE_CUBE_MAP, this.__webglTexture );
		gl.generateMipmap( WebGLRenderingContext.TEXTURE_CUBE_MAP );
		gl.bindTexture( WebGLRenderingContext.TEXTURE_CUBE_MAP, null );
	}
}
