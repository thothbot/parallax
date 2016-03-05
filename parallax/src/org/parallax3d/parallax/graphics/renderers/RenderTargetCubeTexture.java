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

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.enums.FramebufferSlot;
import org.parallax3d.parallax.system.gl.enums.TextureTarget;

@ThreejsObject("THREE.WebGLRenderTargetCube")
public class RenderTargetCubeTexture extends RenderTargetTexture
{
	private int activeCubeFace = 0;

	private List<Integer> webglFramebuffer; //WebGLFramebuffer
	private List<Integer> webglRenderbuffer; //WebGLRenderbuffer

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
	public void deallocate(GL20 gl)
	{
		if (this.getWebGlTexture() == 0)
			return;

		gl.glDeleteTexture(this.getWebGlTexture());
		this.setWebGlTexture(0);

		for (int i = 0; i < 6; i++)
		{
			gl.glDeleteFramebuffer(this.webglFramebuffer.get(i));
			gl.glDeleteRenderbuffer(this.webglRenderbuffer.get(i));
		}

		this.webglFramebuffer = null;
		this.webglRenderbuffer = null;
	}

	@Override
	public int getWebGLFramebuffer()
	{
		return this.webglFramebuffer.get( getActiveCubeFace() );
	}

	@Override
	public void setRenderTarget(GL20 gl)
	{
		if (this.webglFramebuffer != null)
			return;

		this.setWebGlTexture(gl.glGenTexture());

		// Setup texture, create render and frame buffers
		boolean isTargetPowerOfTwo = Mathematics.isPowerOfTwo(getWidth())
				&& Mathematics.isPowerOfTwo(getHeight());

		this.webglFramebuffer = new ArrayList<Integer>();
		this.webglRenderbuffer = new ArrayList<Integer>();

		gl.glBindTexture(TextureTarget.TEXTURE_CUBE_MAP.getValue(), this.getWebGlTexture() );

		setTextureParameters( gl, TextureTarget.TEXTURE_CUBE_MAP, isTargetPowerOfTwo );

		for ( int i = 0; i < 6; i ++ )
		{
			this.webglFramebuffer.add(gl.glGenFramebuffer());
			this.webglRenderbuffer.add(gl.glGenRenderbuffer());

			gl.glTexImage2D(TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X.getValue() + i, 0, getFormat().getValue(), getWidth(), getHeight(), 0,
					getFormat().getValue(), getType().getValue(), null);

			this.setupFrameBuffer(gl, this.webglFramebuffer.get( i ), TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X, i);
			this.setupRenderBuffer(gl, this.webglRenderbuffer.get( i ));
		}

		if ( isTargetPowerOfTwo )
			gl.glGenerateMipmap(TextureTarget.TEXTURE_CUBE_MAP.getValue());

		// Release everything
		gl.glBindTexture(TextureTarget.TEXTURE_CUBE_MAP.getValue(), 0);
		gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, 0);
		gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0);
	}

	public void setupFrameBuffer(GL20 gl, int /*WebGLFramebuffer*/ framebuffer, TextureTarget textureTarget, int slot)
	{
		gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebuffer);
		gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, FramebufferSlot.COLOR_ATTACHMENT0.getValue(), textureTarget.getValue() + slot, this.getWebGlTexture(), 0);
	}

	@Override
	public void updateRenderTargetMipmap(GL20 gl)
	{
		gl.glBindTexture(TextureTarget.TEXTURE_CUBE_MAP.getValue(), this.getWebGlTexture());
		gl.glGenerateMipmap(TextureTarget.TEXTURE_CUBE_MAP.getValue());
		gl.glBindTexture(TextureTarget.TEXTURE_CUBE_MAP.getValue(), 0);
	}
}
