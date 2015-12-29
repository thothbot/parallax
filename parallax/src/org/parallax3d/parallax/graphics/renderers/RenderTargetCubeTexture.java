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

import org.parallax3d.parallax.App;
import org.parallax3d.parallax.system.ThreeJsObject;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.enums.FramebufferSlot;
import org.parallax3d.parallax.system.gl.enums.TextureTarget;

@ThreeJsObject("THREE.WebGLRenderTargetCube")
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
	public void deallocate()
	{
		if (this.getWebGlTexture() == null)
			return;

		App.gl.glDeleteTexture(this.getWebGlTexture());
		this.setWebGlTexture(null);

		for (int i = 0; i < 6; i++)
		{
			App.gl.glDeleteFramebuffer(this.webglFramebuffer.get(i));
			App.gl.glDeleteRenderbuffer(this.webglRenderbuffer.get(i));
		}

		this.webglFramebuffer = null;
		this.webglRenderbuffer = null;
	}

	@Override
	public Integer getWebGLFramebuffer()
	{
		return this.webglFramebuffer.get( getActiveCubeFace() );
	}

	@Override
	public void setRenderTarget()
	{
		if (this.webglFramebuffer != null)
			return;

		this.setWebGlTexture(App.gl.glGenTexture());

		// Setup texture, create render and frame buffers
		boolean isTargetPowerOfTwo = Mathematics.isPowerOfTwo(getWidth())
				&& Mathematics.isPowerOfTwo(getHeight());

		this.webglFramebuffer = new ArrayList<Integer>();
		this.webglRenderbuffer = new ArrayList<Integer>();

		App.gl.glBindTexture(TextureTarget.TEXTURE_CUBE_MAP.getValue(), this.getWebGlTexture() );

		setTextureParameters( TextureTarget.TEXTURE_CUBE_MAP.getValue(), isTargetPowerOfTwo );

		for ( int i = 0; i < 6; i ++ )
		{
			this.webglFramebuffer.add( App.gl.glGenFramebuffer() );
			this.webglRenderbuffer.add( App.gl.glGenRenderbuffer() );

			App.gl.glTexImage2D(TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X.getValue(), i, 0, getWidth(), getHeight(), 0,
					getFormat().getValue(), getType().getValue(), null);

			this.setupFrameBuffer(this.webglFramebuffer.get( i ), TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X.getValue(), i);
			this.setupRenderBuffer(this.webglRenderbuffer.get( i ));
		}

		if ( isTargetPowerOfTwo )
			App.gl.glGenerateMipmap(TextureTarget.TEXTURE_CUBE_MAP.getValue());

		// Release everything
		Integer nullval = null;
		App.gl.glBindTexture(TextureTarget.TEXTURE_CUBE_MAP.getValue(), nullval);
		App.gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, nullval);
		App.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, nullval);
	}

	public void setupFrameBuffer(Integer /*WebGLFramebuffer*/ framebuffer, Integer /*TextureTarget*/ textureTarget, int slot)
	{
		App.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebuffer);
		App.gl.glFramebufferTexture2D(FramebufferSlot.COLOR_ATTACHMENT0.getValue(), textureTarget, slot, this.getWebGlTexture(), 0);
	}

	@Override
	public void updateRenderTargetMipmap()
	{
		App.gl.glBindTexture(TextureTarget.TEXTURE_CUBE_MAP.getValue(), this.getWebGlTexture());
		App.gl.glGenerateMipmap(TextureTarget.TEXTURE_CUBE_MAP.getValue());
		Integer nullval = null;
		App.gl.glBindTexture(TextureTarget.TEXTURE_CUBE_MAP.getValue(), nullval);
	}
}
