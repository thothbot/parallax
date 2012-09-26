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

import thothbot.parallax.core.client.gl2.WebGLFramebuffer;
import thothbot.parallax.core.client.gl2.WebGLRenderbuffer;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.enums.DataType;
import thothbot.parallax.core.client.gl2.enums.GLEnum;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.TextureMagFilter;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
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
				PixelFormat.RGBA,              DataType.UNSIGNED_BYTE);
	}

	public RenderTargetTexture(int width, int height, 
			TextureWrapMode wrapS,      TextureWrapMode wrapT, 
			TextureMagFilter magFilter, TextureMinFilter minFilter,
			PixelFormat format,         DataType type) 
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

		gl.bindTexture(GLEnum.TEXTURE_2D.getValue(), this.getWebGlTexture());

		setTextureParameters(gl, GLEnum.TEXTURE_2D.getValue(), isTargetPowerOfTwo);

		gl.texImage2D(GLEnum.TEXTURE_2D.getValue(), 0, getFormat().getValue(), this.width, this.height, 0,
				getFormat().getValue(), getType().getValue(), null);

		setupFrameBuffer(gl, this.webglFramebuffer, GLEnum.TEXTURE_2D.getValue());
		setupRenderBuffer(gl, this.webglRenderbuffer);

		if (isTargetPowerOfTwo)
			gl.generateMipmap(GLEnum.TEXTURE_2D.getValue());

		// Release everything
		gl.bindTexture(GLEnum.TEXTURE_2D.getValue(), null);
		gl.bindRenderbuffer(GLEnum.RENDERBUFFER.getValue(), null);
		gl.bindFramebuffer(GLEnum.FRAMEBUFFER.getValue(), null);
	}

	public void updateRenderTargetMipmap(WebGLRenderingContext gl)
	{	
		gl.bindTexture(GLEnum.TEXTURE_2D.getValue(), this.getWebGlTexture());
		gl.generateMipmap(GLEnum.TEXTURE_2D.getValue());
		gl.bindTexture(GLEnum.TEXTURE_2D.getValue(), null);
	}

	public void setupFrameBuffer(WebGLRenderingContext gl, WebGLFramebuffer framebuffer, int textureTarget)
	{	
		gl.bindFramebuffer(GLEnum.FRAMEBUFFER.getValue(), framebuffer);
		gl.framebufferTexture2D(GLEnum.FRAMEBUFFER.getValue(), GLEnum.COLOR_ATTACHMENT0.getValue(), textureTarget, this.getWebGlTexture(), 0);
	}

	public void setupRenderBuffer(WebGLRenderingContext gl, WebGLRenderbuffer renderbuffer)
	{	
		gl.bindRenderbuffer(GLEnum.RENDERBUFFER.getValue(), renderbuffer);

		if (this.isDepthBuffer && !this.isStencilBuffer) 
		{
			gl.renderbufferStorage(GLEnum.RENDERBUFFER.getValue(), GLEnum.DEPTH_COMPONENT16.getValue(), this.width, this.height);
			gl.framebufferRenderbuffer(GLEnum.FRAMEBUFFER.getValue(), GLEnum.DEPTH_ATTACHMENT.getValue(), GLEnum.RENDERBUFFER.getValue(), renderbuffer);

			/*
			 * For some reason this is not working. Defaulting to RGBA4. } else
			 * if( ! this.depthBuffer && this.stencilBuffer ) {
			 * 
			 * _gl.renderbufferStorage( GLEnum.RENDERBUFFER,
			 * GLEnum.STENCIL_INDEX8, this.width, this.height );
			 * _gl.framebufferRenderbuffer( GLEnum.FRAMEBUFFER,
			 * GLEnum.STENCIL_ATTACHMENT,
			 * GLEnum.RENDERBUFFER, renderbuffer );
			 */
		} 
		else if (this.isDepthBuffer && this.isStencilBuffer) 
		{
			gl.renderbufferStorage(GLEnum.RENDERBUFFER.getValue(),
					GLEnum.DEPTH_STENCIL.getValue(), this.width, this.height);
			gl.framebufferRenderbuffer(GLEnum.FRAMEBUFFER.getValue(),
					GLEnum.DEPTH_STENCIL_ATTACHMENT.getValue(),
					GLEnum.RENDERBUFFER.getValue(), renderbuffer);
		} 
		else 
		{
			gl.renderbufferStorage(GLEnum.RENDERBUFFER.getValue(),
					GLEnum.RGBA4.getValue(), this.width, this.height);
		}
	}
}
