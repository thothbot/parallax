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

package thothbot.parallax.core.client.textures;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.gl2.WebGLFramebuffer;
import thothbot.parallax.core.client.gl2.WebGLRenderbuffer;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.enums.FramebufferSlot;
import thothbot.parallax.core.client.gl2.enums.TextureTarget;
import thothbot.parallax.core.shared.math.Mathematics;

public class RenderTargetCubeTexture extends RenderTargetTexture
{
	private int activeCubeFace = 0;

	private List<WebGLFramebuffer> webglFramebuffer;
	private List<WebGLRenderbuffer> webglRenderbuffer;

	public RenderTargetCubeTexture(int width, int height ) 
	{
		super(width, height);
	}
	
	public int getActiveCubeFace() {
		return this.activeCubeFace;
	}
	
	public void setActiveCubeFace(int activeCubeFace) {
		this.activeCubeFace = activeCubeFace;
	}

	@Override
	public void deallocate(WebGLRenderingContext gl)
	{
		if (this.getWebGlTexture() == null)
			return;
	
		gl.deleteTexture(this.getWebGlTexture());
		this.setWebGlTexture(null);
		
		for (int i = 0; i < 6; i++) 
		{
			gl.deleteFramebuffer(this.webglFramebuffer.get(i));
			gl.deleteRenderbuffer(this.webglRenderbuffer.get(i));
		}
		
		this.webglFramebuffer = null;
		this.webglRenderbuffer = null;
	}
	
	@Override
	public WebGLFramebuffer getWebGLFramebuffer() 
	{
		return this.webglFramebuffer.get( getActiveCubeFace() );
	}
	
	@Override
	public void setRenderTarget(WebGLRenderingContext gl)
	{
		if (this.webglFramebuffer != null)
			return;

		this.setWebGlTexture(gl.createTexture());

		// Setup texture, create render and frame buffers
		boolean isTargetPowerOfTwo = Mathematics.isPowerOfTwo(getWidth())
				&& Mathematics.isPowerOfTwo(getHeight());

		this.webglFramebuffer = new ArrayList<WebGLFramebuffer>();
		this.webglRenderbuffer = new ArrayList<WebGLRenderbuffer>();

		gl.bindTexture( TextureTarget.TEXTURE_CUBE_MAP, this.getWebGlTexture() );

		setTextureParameters( gl, TextureTarget.TEXTURE_CUBE_MAP, isTargetPowerOfTwo );

		for ( int i = 0; i < 6; i ++ ) 
		{
			this.webglFramebuffer.add( gl.createFramebuffer() );
			this.webglRenderbuffer.add( gl.createRenderbuffer() );

			gl.texImage2D( TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X, i, 0, getWidth(), getHeight(), 0, 
					getFormat(), getType(), null );

			this.setupFrameBuffer(gl, this.webglFramebuffer.get( i ), TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X, i);
			this.setupRenderBuffer(gl, this.webglRenderbuffer.get( i ));
		}

		if ( isTargetPowerOfTwo ) 
			gl.generateMipmap( TextureTarget.TEXTURE_CUBE_MAP );

		// Release everything
		gl.bindTexture( TextureTarget.TEXTURE_CUBE_MAP, null );
		gl.bindRenderbuffer(null);
		gl.bindFramebuffer(null);
	}
	
	public void setupFrameBuffer(WebGLRenderingContext gl, WebGLFramebuffer framebuffer, TextureTarget textureTarget, int slot)
	{	
		gl.bindFramebuffer(framebuffer);
		gl.framebufferTexture2D(FramebufferSlot.COLOR_ATTACHMENT0, textureTarget, slot, this.getWebGlTexture(), 0);
	}
	
	@Override
	public void updateRenderTargetMipmap(WebGLRenderingContext gl) 
	{	
		gl.bindTexture( TextureTarget.TEXTURE_CUBE_MAP, this.getWebGlTexture() );
		gl.generateMipmap( TextureTarget.TEXTURE_CUBE_MAP );
		gl.bindTexture( TextureTarget.TEXTURE_CUBE_MAP, null );
	}
}
