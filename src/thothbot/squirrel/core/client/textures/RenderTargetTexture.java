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

package thothbot.squirrel.core.client.textures;

import thothbot.squirrel.core.client.gl2.WebGLFramebuffer;
import thothbot.squirrel.core.client.gl2.WebGLRenderbuffer;
import thothbot.squirrel.core.client.gl2.WebGLRenderingContext;
import thothbot.squirrel.core.client.gl2.enums.DataType;
import thothbot.squirrel.core.client.gl2.enums.GLenum;
import thothbot.squirrel.core.client.gl2.enums.PixelFormat;
import thothbot.squirrel.core.client.gl2.enums.TextureMagFilter;
import thothbot.squirrel.core.client.gl2.enums.TextureMinFilter;
import thothbot.squirrel.core.client.gl2.enums.TextureWrapMode;
import thothbot.squirrel.core.shared.core.Mathematics;
import thothbot.squirrel.core.shared.core.Vector2f;

public class RenderTargetTexture extends Texture
{
	private int width;
	private int height;

	public int activeCubeFace;

	public boolean depthBuffer = true;
	public boolean stencilBuffer = true;

	public WebGLFramebuffer __webglFramebuffer;
	public WebGLRenderbuffer __webglRenderbuffer;

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

		this.__webglFramebuffer = gl.createFramebuffer();
		this.__webglRenderbuffer = gl.createRenderbuffer();

		gl.bindTexture(GLenum.TEXTURE_2D.getValue(), this.__webglTexture);
		// TODO: FIX setTextureParameters
		//Texture.setTextureParameters(_gl, GLenum.TEXTURE_2D, renderTarget, isTargetPowerOfTwo);

		gl.texImage2D(GLenum.TEXTURE_2D.getValue(), 0, getFormat().getValue(), this.width, this.height, 0,
				getFormat().getValue(), getType().getValue(), null);

		setupFrameBuffer(gl, this.__webglFramebuffer, GLenum.TEXTURE_2D.getValue());
		setupRenderBuffer(gl, this.__webglRenderbuffer);

		if (isTargetPowerOfTwo)
			gl.generateMipmap(GLenum.TEXTURE_2D.getValue());

		// Release everything
		gl.bindTexture(GLenum.TEXTURE_2D.getValue(), null);
		gl.bindRenderbuffer(GLenum.RENDERBUFFER.getValue(), null);
		gl.bindFramebuffer(GLenum.FRAMEBUFFER.getValue(), null);
	}

	public void updateRenderTargetMipmap(WebGLRenderingContext gl)
	{	
		gl.bindTexture(GLenum.TEXTURE_2D.getValue(), this.__webglTexture);
		gl.generateMipmap(GLenum.TEXTURE_2D.getValue());
		gl.bindTexture(GLenum.TEXTURE_2D.getValue(), null);
	}

	public void setupFrameBuffer(WebGLRenderingContext gl, WebGLFramebuffer framebuffer, int textureTarget)
	{	
		gl.bindFramebuffer(GLenum.FRAMEBUFFER.getValue(), framebuffer);
		gl.framebufferTexture2D(GLenum.FRAMEBUFFER.getValue(), GLenum.COLOR_ATTACHMENT0.getValue(), textureTarget, this.__webglTexture, 0);
	}

	public void setupRenderBuffer(WebGLRenderingContext gl, WebGLRenderbuffer renderbuffer)
	{	
		gl.bindRenderbuffer(GLenum.RENDERBUFFER.getValue(), renderbuffer);

		if (this.depthBuffer && !this.stencilBuffer) 
		{
			gl.renderbufferStorage(GLenum.RENDERBUFFER.getValue(), GLenum.DEPTH_COMPONENT16.getValue(), this.width, this.height);
			gl.framebufferRenderbuffer(GLenum.FRAMEBUFFER.getValue(), GLenum.DEPTH_ATTACHMENT.getValue(), GLenum.RENDERBUFFER.getValue(), renderbuffer);

			/*
			 * For some reason this is not working. Defaulting to RGBA4. } else
			 * if( ! this.depthBuffer && this.stencilBuffer ) {
			 * 
			 * _gl.renderbufferStorage( GLenum.RENDERBUFFER,
			 * GLenum.STENCIL_INDEX8, this.width, this.height );
			 * _gl.framebufferRenderbuffer( GLenum.FRAMEBUFFER,
			 * GLenum.STENCIL_ATTACHMENT,
			 * GLenum.RENDERBUFFER, renderbuffer );
			 */
		} 
		else if (this.depthBuffer && this.stencilBuffer) 
		{
			gl.renderbufferStorage(GLenum.RENDERBUFFER.getValue(),
					GLenum.DEPTH_STENCIL.getValue(), this.width, this.height);
			gl.framebufferRenderbuffer(GLenum.FRAMEBUFFER.getValue(),
					GLenum.DEPTH_STENCIL_ATTACHMENT.getValue(),
					GLenum.RENDERBUFFER.getValue(), renderbuffer);
		} 
		else 
		{
			gl.renderbufferStorage(GLenum.RENDERBUFFER.getValue(),
					GLenum.RGBA4.getValue(), this.width, this.height);
		}
	}
}
