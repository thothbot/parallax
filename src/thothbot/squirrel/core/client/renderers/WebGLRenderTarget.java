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

import thothbot.squirrel.core.client.gl2.WebGLFramebuffer;
import thothbot.squirrel.core.client.gl2.WebGLRenderbuffer;
import thothbot.squirrel.core.client.gl2.WebGLRenderingContext;
import thothbot.squirrel.core.client.gl2.WebGLTexture;
import thothbot.squirrel.core.shared.core.Mathematics;
import thothbot.squirrel.core.shared.core.Vector2f;
import thothbot.squirrel.core.shared.textures.Texture;

public class WebGLRenderTarget
{
	public int width;
	public int height;

	public Vector2f offset;
	public Vector2f repeat;

	public boolean generateMipmaps = true;
	
	public int activeCubeFace;

	public Texture.WRAPPING_MODE wrapS;
	public Texture.WRAPPING_MODE wrapT;

	public Texture.FILTER magFilter;
	public Texture.FILTER minFilter;

	public Texture.FORMAT format;
	public Texture.TYPE type;

	public boolean depthBuffer;
	public boolean stencilBuffer;

	public WebGLTexture __webglTexture;
	public WebGLFramebuffer __webglFramebuffer;
	public WebGLRenderbuffer __webglRenderbuffer;
	
	public static class WebGLRenderTargetOptions
	{
		public Texture.WRAPPING_MODE wrapS = Texture.WRAPPING_MODE.CLAMP_TO_EDGE;
		public Texture.WRAPPING_MODE wrapT = Texture.WRAPPING_MODE.CLAMP_TO_EDGE;
		
		public Texture.FILTER magFilter = Texture.FILTER.LINEAR;
		public Texture.FILTER minFilter = Texture.FILTER.LINEAR_MIP_MAP_LINEAR;
		
		public Texture.FORMAT format = Texture.FORMAT.RGBA;
		public Texture.TYPE type = Texture.TYPE.UNSIGNED_BYTE;
		
		public boolean depthBuffer = true;
		public boolean stencilBuffer = true;
	}

	public WebGLRenderTarget(int width, int height) {

		this(width, height, new WebGLRenderTarget.WebGLRenderTargetOptions());
	}

	public WebGLRenderTarget(int width, int height, WebGLRenderTarget.WebGLRenderTargetOptions options) 
	{
		this.width = width;
		this.height = height;
		
		this.offset = new Vector2f(0, 0);
		this.repeat = new Vector2f(1, 1);
		
		this.wrapS = options.wrapS;
		this.wrapT = options.wrapT;
		
		this.magFilter = options.magFilter;
		this.minFilter = options.minFilter;
		
		this.format = options.format;
		this.type = options.type;
		
		this.depthBuffer = options.depthBuffer;
		this.stencilBuffer = options.stencilBuffer;
	}

	public WebGLFramebuffer getWebGLFramebuffer() {
		return this.__webglFramebuffer;
	}

	public void deallocate(WebGLRenderingContext gl)
	{
		if (this.__webglTexture == null)
			return;

		gl.deleteTexture(this.__webglTexture);
		gl.deleteFramebuffer(this.__webglFramebuffer);
		gl.deleteRenderbuffer(this.__webglRenderbuffer);
	}

	public WebGLRenderTarget clone()
	{
		WebGLRenderTarget tmp = new WebGLRenderTarget(this.width, this.height);

		tmp.wrapS = this.wrapS;
		tmp.wrapT = this.wrapT;

		tmp.magFilter = this.magFilter;
		tmp.minFilter = this.minFilter;

		tmp.offset.copy(this.offset);
		tmp.repeat.copy(this.repeat);

		tmp.format = this.format;
		tmp.type = this.type;

		tmp.depthBuffer = this.depthBuffer;
		tmp.stencilBuffer = this.stencilBuffer;

		return tmp;
	}
	
	public void setRenderTarget(WebGLRenderingContext gl)
	{
		if (this.__webglFramebuffer != null)
			return;

		this.__webglTexture = gl.createTexture();

		// Setup texture, create render and frame buffers

		boolean isTargetPowerOfTwo = Mathematics.isPowerOfTwo(this.width)
				&& Mathematics.isPowerOfTwo(this.height);
		int glFormat = this.format.getValue();
		int glType = this.type.getValue();

		this.__webglFramebuffer = gl.createFramebuffer();
		this.__webglRenderbuffer = gl.createRenderbuffer();

		gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, this.__webglTexture);
		// TODO: FIX setTextureParameters
		//Texture.setTextureParameters(_gl, WebGLRenderingContext.TEXTURE_2D, renderTarget, isTargetPowerOfTwo);

		gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, glFormat, this.width, this.height, 0,
				glFormat, glType, null);

		setupFrameBuffer(gl, this.__webglFramebuffer, WebGLRenderingContext.TEXTURE_2D);
		setupRenderBuffer(gl, this.__webglRenderbuffer);

		if (isTargetPowerOfTwo)
			gl.generateMipmap(WebGLRenderingContext.TEXTURE_2D);

		// Release everything
		gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, null);
		gl.bindRenderbuffer(WebGLRenderingContext.RENDERBUFFER, null);
		gl.bindFramebuffer(WebGLRenderingContext.FRAMEBUFFER, null);
	}

	public void updateRenderTargetMipmap(WebGLRenderingContext gl)
	{	
		gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, this.__webglTexture);
		gl.generateMipmap(WebGLRenderingContext.TEXTURE_2D);
		gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, null);
	}

	public void setupFrameBuffer(WebGLRenderingContext gl, WebGLFramebuffer framebuffer, int textureTarget)
	{	
		gl.bindFramebuffer(WebGLRenderingContext.FRAMEBUFFER, framebuffer);
		gl.framebufferTexture2D(WebGLRenderingContext.FRAMEBUFFER, WebGLRenderingContext.COLOR_ATTACHMENT0, textureTarget, this.__webglTexture, 0);
	}

	public void setupRenderBuffer(WebGLRenderingContext gl, WebGLRenderbuffer renderbuffer)
	{	
		gl.bindRenderbuffer(WebGLRenderingContext.RENDERBUFFER, renderbuffer);

		if (this.depthBuffer && !this.stencilBuffer) 
		{
			gl.renderbufferStorage(WebGLRenderingContext.RENDERBUFFER, WebGLRenderingContext.DEPTH_COMPONENT16, this.width, this.height);
			gl.framebufferRenderbuffer(WebGLRenderingContext.FRAMEBUFFER, WebGLRenderingContext.DEPTH_ATTACHMENT, WebGLRenderingContext.RENDERBUFFER, renderbuffer);

			/*
			 * For some reason this is not working. Defaulting to RGBA4. } else
			 * if( ! this.depthBuffer && this.stencilBuffer ) {
			 * 
			 * _gl.renderbufferStorage( WebGLRenderingContext.RENDERBUFFER,
			 * WebGLRenderingContext.STENCIL_INDEX8, this.width, this.height );
			 * _gl.framebufferRenderbuffer( WebGLRenderingContext.FRAMEBUFFER,
			 * WebGLRenderingContext.STENCIL_ATTACHMENT,
			 * WebGLRenderingContext.RENDERBUFFER, renderbuffer );
			 */
		} 
		else if (this.depthBuffer && this.stencilBuffer) 
		{
			gl.renderbufferStorage(WebGLRenderingContext.RENDERBUFFER,
					WebGLRenderingContext.DEPTH_STENCIL, this.width, this.height);
			gl.framebufferRenderbuffer(WebGLRenderingContext.FRAMEBUFFER,
					WebGLRenderingContext.DEPTH_STENCIL_ATTACHMENT,
					WebGLRenderingContext.RENDERBUFFER, renderbuffer);
		} 
		else 
		{
			gl.renderbufferStorage(WebGLRenderingContext.RENDERBUFFER,
					WebGLRenderingContext.RGBA4, this.width, this.height);
		}
	}
}
