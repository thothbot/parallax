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

package org.parallax3d.parallax.graphics.renderers.shaders;

import org.parallax3d.parallax.system.SourceBundleProxy;
import org.parallax3d.parallax.system.SourceTextResource;

import java.util.Arrays;
import java.util.List;

/**
 * Depth encoding into RGBA texture.
 * <p>
 * Based on SpiderGL shadow map example @see <a href="http://spidergl.org/example.php?id=6">http://spidergl.org</a><br>
 * Originally from @see <a href="http://www.gamedev.net/topic/442138-packing-a-float-into-a-a8r8g8b8-texture-shader/page__whichpage__1%25EF%25BF%25BD">http://www.gamedev.net</a><br>
 * See also here @see <a href="http://aras-p.info/blog/2009/07/30/encoding-floats-to-rgba-the-final/">http://aras-p.info</a>
 * 
 * @author thothbot
 */
public final class DepthRGBAShader extends Shader
{

	interface Resources extends DefaultResources
	{
		Resources INSTANCE = SourceBundleProxy.create(Resources.class);

		@Source("source/depthRGBA_vert.glsl")
		SourceTextResource getVertexShader();

		@Source("source/depthRGBA_frag.glsl")
		SourceTextResource getFragmentShader();
	}

	public DepthRGBAShader()
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
	}
	
}
