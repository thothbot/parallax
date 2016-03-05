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

package org.parallax3d.parallax.graphics.renderers;

import org.parallax3d.parallax.graphics.textures.EmptyTextureData;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.enums.*;

@ThreejsObject("THREE.WebGLRenderTarget")
public class RenderTargetTexture extends Texture
{
	private int width;
	private int height;

	private boolean isDepthBuffer = true;
	private boolean isStencilBuffer = true;

	private int webglFramebuffer; // WebGLFramebuffer
	private int webglRenderbuffer; //WebGLRenderbuffer

	public RenderTargetTexture shareDepthFrom;

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
		// Create a dummy Image so that WebGLRenderer can get size
		super();

		this.width = width;
		this.height = height;

		setWrapS( wrapS );
		setWrapT( wrapT );

		setMagFilter( magFilter );
		setMinFilter( minFilter );

		setFormat( format );
		setType( type );
	}

	public void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
		((EmptyTextureData) getImage()).setWidth(width);
		((EmptyTextureData) getImage()).setHeight(height);
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

	public int getWebGLFramebuffer() {
		return this.webglFramebuffer;
	}

	public void deallocate(GL20 gl)
	{
		if (this.getWebGlTexture() == 0)
			return;

		gl.glDeleteTexture(this.getWebGlTexture());
		this.setWebGlTexture(0);

		gl.glDeleteFramebuffer(this.webglFramebuffer);
		gl.glDeleteRenderbuffer(this.webglRenderbuffer);

		this.webglFramebuffer = 0;
		this.webglRenderbuffer = 0;
	}

	public RenderTargetTexture clone()
	{
		RenderTargetTexture tmp = new RenderTargetTexture(this.width, this.height);

		super.clone(tmp);

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

	public void setRenderTarget(GL20 gl)
	{
		if (this.webglFramebuffer > 0)
			return;

		this.deallocate(gl);
		this.setWebGlTexture(gl.glGenTexture());

		// Setup texture, create render and frame buffers

		boolean isTargetPowerOfTwo = Mathematics.isPowerOfTwo(this.width)
				&& Mathematics.isPowerOfTwo(this.height);

		this.webglFramebuffer = gl.glGenFramebuffer();

		if ( this.shareDepthFrom != null )
		{
			this.webglRenderbuffer = this.shareDepthFrom.webglRenderbuffer;
		}
		else
		{
			this.webglRenderbuffer = gl.glGenRenderbuffer();
		}

		gl.glBindTexture(TextureTarget.TEXTURE_2D.getValue(), this.getWebGlTexture());
		setTextureParameters(gl, TextureTarget.TEXTURE_2D, isTargetPowerOfTwo);

		gl.glTexImage2D(TextureTarget.TEXTURE_2D.getValue(), 0, getFormat().getValue(), this.width, this.height, 0,  getFormat().getValue(), getType().getValue(), null);

		setupFrameBuffer( gl, this.webglFramebuffer, TextureTarget.TEXTURE_2D);

		if ( this.shareDepthFrom != null )
		{

			if ( this.isDepthBuffer && ! this.isStencilBuffer ) {

				gl.glFramebufferRenderbuffer(GL20.GL_FRAMEBUFFER, FramebufferSlot.DEPTH_ATTACHMENT.getValue(), GL20.GL_RENDERBUFFER, this.webglRenderbuffer);

			} else if ( this.isDepthBuffer && this.isStencilBuffer ) {

				gl.glFramebufferRenderbuffer(GL20.GL_FRAMEBUFFER, FramebufferSlot.DEPTH_STENCIL_ATTACHMENT.getValue(), GL20.GL_RENDERBUFFER, this.webglRenderbuffer);

			}

		} else {

			setupRenderBuffer( gl, this.webglRenderbuffer );

		}

		if (isTargetPowerOfTwo)
			gl.glGenerateMipmap(TextureTarget.TEXTURE_2D.getValue());

		// Release everything
		gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, 0);
		gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0);
	}

	public void updateRenderTargetMipmap(GL20 gl)
	{
		gl.glBindTexture(TextureTarget.TEXTURE_2D.getValue(), this.getWebGlTexture());
		gl.glGenerateMipmap(TextureTarget.TEXTURE_2D.getValue());
		gl.glBindTexture(TextureTarget.TEXTURE_2D.getValue(), 0);
	}

	public void setupFrameBuffer( GL20 gl, int framebuffer, TextureTarget textureTarget)
	{
		gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebuffer);
		gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, FramebufferSlot.COLOR_ATTACHMENT0.getValue(), textureTarget.getValue(), this.getWebGlTexture(), 0);
	}

	public void setupRenderBuffer( GL20 gl, int renderbuffer )
	{
		gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, renderbuffer);

		if (this.isDepthBuffer && !this.isStencilBuffer)
		{
			gl.glRenderbufferStorage(GL20.GL_RENDERBUFFER, RenderbufferInternalFormat.DEPTH_COMPONENT16.getValue(), this.width, this.height);
			gl.glFramebufferRenderbuffer(GL20.GL_FRAMEBUFFER, FramebufferSlot.DEPTH_ATTACHMENT.getValue(), GL20.GL_RENDERBUFFER, renderbuffer);

			/*
			 * For some reason this is not working. Defaulting to RGBA4. } else
			 * if( ! this.depthBuffer && this.stencilBuffer ) {
			 *
			 * _App.gl.renderbufferStorage( WebGLConstants.RENDERBUFFER,
			 * WebGLConstants.STENCIL_INDEX8, this.width, this.height );
			 * _App.gl.framebufferRenderbuffer( WebGLConstants.FRAMEBUFFER,
			 * WebGLConstants.STENCIL_ATTACHMENT,
			 * WebGLConstants.RENDERBUFFER, renderbuffer );
			 */
		}
		else if (this.isDepthBuffer && this.isStencilBuffer)
		{
			gl.glRenderbufferStorage(GL20.GL_RENDERBUFFER, RenderbufferInternalFormat.DEPTH_STENCIL.getValue(), this.width, this.height);
			gl.glFramebufferRenderbuffer(GL20.GL_FRAMEBUFFER, FramebufferSlot.DEPTH_STENCIL_ATTACHMENT.getValue(), GL20.GL_RENDERBUFFER,renderbuffer);
		}
		else
		{
			gl.glRenderbufferStorage(GL20.GL_RENDERBUFFER, RenderbufferInternalFormat.RGBA4.getValue(), this.width, this.height);
		}
	}
}
