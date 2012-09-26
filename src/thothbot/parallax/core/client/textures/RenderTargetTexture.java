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

package thothbot.parallax.core.client.textures;

import thothbot.parallax.core.client.gl2.WebGLConstants;
import thothbot.parallax.core.client.gl2.WebGLFramebuffer;
import thothbot.parallax.core.client.gl2.WebGLRenderbuffer;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.enums.DataType;
import thothbot.parallax.core.client.gl2.enums.FramebufferSlot;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.PixelType;
import thothbot.parallax.core.client.gl2.enums.RenderbufferInternalFormat;
import thothbot.parallax.core.client.gl2.enums.TextureMagFilter;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
import thothbot.parallax.core.client.gl2.enums.TextureTarget;
import thothbot.parallax.core.client.gl2.enums.TextureWrapMode;
import thothbot.parallax.core.shared.core.Mathematics;

public class RenderTargetTexture extends Texture
{
	private int width;
	private int height;

	private boolean isDepthBuffer = true;
	private boolean isStencilBuffer = true;

	private WebGLFramebuffer webglFramebuffer;
	private WebGLRenderbuffer webglRenderbuffer;

	public RenderTargetTexture(int width, int height) 
	{
		this(width, height, 
				TextureWrapMode.CLAMP_TO_EDGE, TextureWrapMode.CLAMP_TO_EDGE, 
				TextureMagFilter.LINEAR,       TextureMinFilter.LINEAR_MIPMAP_LINEAR,
				PixelFormat.RGBA,              PixelType.UNSIGNED_BYTE);
	}

	public RenderTargetTexture(int width, int height, 
			TextureWrapMode wrapS,      TextureWrapMode wrapT, 
			TextureMagFilter magFilter, TextureMinFilter minFilter,
			PixelFormat format,         PixelType type) 
	{
		super(); // call super Texture

		this.width = width;
		this.height = height;

		setWrapS( wrapS );
		setWrapT( wrapT );
		
		setMagFilter( magFilter );
		setMinFilter( minFilter );
		
		setFormat( format );
		setType( type );
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean getDepthBuffer() {
		return this.isDepthBuffer;
	}
	
	public void setDepthBuffer(boolean depthBuffer) {
		this.isDepthBuffer = depthBuffer;
	}
	
	public boolean getStencilBuffer() {
		return this.isStencilBuffer;
	}
	
	public void setStencilBuffer(boolean stencilBuffer) {
		this.isStencilBuffer = stencilBuffer;
	}
	
	public WebGLFramebuffer getWebGLFramebuffer() {
		return this.webglFramebuffer;
	}

	public void deallocate(WebGLRenderingContext gl)
	{
		if (this.getWebGlTexture() == null)
			return;

		gl.deleteTexture(this.getWebGlTexture());
		gl.deleteFramebuffer(this.webglFramebuffer);
		gl.deleteRenderbuffer(this.webglRenderbuffer);
	}

	public RenderTargetTexture clone()
	{
		RenderTargetTexture tmp = new RenderTargetTexture(this.width, this.height);

		tmp.setWrapS( getWrapS() );
		tmp.setWrapT( getWrapT() );

		tmp.setMagFilter( getMagFilter() );
		tmp.setMinFilter( getMinFilter() );

		tmp.getOffset().copy(getOffset());
		tmp.getRepeat().copy(getRepeat());

		tmp.setFormat( getFormat() );
		tmp.setType( getType() );

		tmp.isDepthBuffer = this.isDepthBuffer;
		tmp.isStencilBuffer = this.isStencilBuffer;

		return tmp;
	}
	
	public void setRenderTarget(WebGLRenderingContext gl)
	{
		if (this.webglFramebuffer != null)
			return;

		this.setWebGlTexture(gl.createTexture());

		// Setup texture, create render and frame buffers

		boolean isTargetPowerOfTwo = Mathematics.isPowerOfTwo(this.width)
				&& Mathematics.isPowerOfTwo(this.height);

		this.webglFramebuffer = gl.createFramebuffer();
		this.webglRenderbuffer = gl.createRenderbuffer();

		gl.bindTexture(TextureTarget.TEXTURE_2D, this.getWebGlTexture());

		setTextureParameters(gl, TextureTarget.TEXTURE_2D, isTargetPowerOfTwo);

		gl.texImage2D(TextureTarget.TEXTURE_2D, 0, this.width, this.height, 0, getFormat(), getType(), null);

		setupFrameBuffer(gl, this.webglFramebuffer, TextureTarget.TEXTURE_2D);
		setupRenderBuffer(gl, this.webglRenderbuffer);

		if (isTargetPowerOfTwo)
			gl.generateMipmap(TextureTarget.TEXTURE_2D);

		// Release everything
		gl.bindTexture(TextureTarget.TEXTURE_2D, null);
		gl.bindRenderbuffer(null);
		gl.bindFramebuffer(null);
	}

	public void updateRenderTargetMipmap(WebGLRenderingContext gl)
	{	
		gl.bindTexture(TextureTarget.TEXTURE_2D, this.getWebGlTexture());
		gl.generateMipmap(TextureTarget.TEXTURE_2D);
		gl.bindTexture(TextureTarget.TEXTURE_2D, null);
	}

	public void setupFrameBuffer(WebGLRenderingContext gl, WebGLFramebuffer framebuffer, TextureTarget textureTarget)
	{	
		gl.bindFramebuffer(framebuffer);
		gl.framebufferTexture2D(FramebufferSlot.COLOR_ATTACHMENT0, textureTarget, this.getWebGlTexture(), 0);
	}

	public void setupRenderBuffer(WebGLRenderingContext gl, WebGLRenderbuffer renderbuffer)
	{	
		gl.bindRenderbuffer(renderbuffer);

		if (this.isDepthBuffer && !this.isStencilBuffer) 
		{
			gl.renderbufferStorage(RenderbufferInternalFormat.DEPTH_COMPONENT16, this.width, this.height);
			gl.framebufferRenderbuffer(FramebufferSlot.DEPTH_ATTACHMENT, renderbuffer);

			/*
			 * For some reason this is not working. Defaulting to RGBA4. } else
			 * if( ! this.depthBuffer && this.stencilBuffer ) {
			 * 
			 * _gl.renderbufferStorage( WebGLConstants.RENDERBUFFER,
			 * WebGLConstants.STENCIL_INDEX8, this.width, this.height );
			 * _gl.framebufferRenderbuffer( WebGLConstants.FRAMEBUFFER,
			 * WebGLConstants.STENCIL_ATTACHMENT,
			 * WebGLConstants.RENDERBUFFER, renderbuffer );
			 */
		} 
		else if (this.isDepthBuffer && this.isStencilBuffer) 
		{
			gl.renderbufferStorage(RenderbufferInternalFormat.DEPTH_STENCIL, this.width, this.height);
			gl.framebufferRenderbuffer( FramebufferSlot.DEPTH_STENCIL_ATTACHMENT, renderbuffer);
		} 
		else 
		{
			gl.renderbufferStorage(RenderbufferInternalFormat.RGBA4, this.width, this.height);
		}
	}
}
