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

package thothbot.parallax.core.client.shader;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;

/**
 * Depth encoding into RGBA texture.
 * <p>
 * Based on SpiderGL shadow map example @see <a href="http://spidergl.org/example.php?id=6">http://spidergl.org</a><br>
 * Originally from @see <a href="http://www.gamedev.net/topic/442138-packing-a-float-into-a-a8r8g8b8-texture-shader/page__whichpage__1%25EF%25BF%25BD">http://www.gamedev.net</a><br>
 * See also here @see <a href="http://aras-p.info/blog/2009/07/30/encoding-floats-to-rgba-the-final/">http://aras-p.info</a>
 * 
 * @author thothbot
 */
public final class ShaderDepthRGBA extends Shader
{
	interface Resources extends DefaultResources
	{
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("chunk/depthRGBA_vs.chunk")
		TextResource getVertexShader();

		@Source("chunk/depthRGBA_fs.chunk")
		TextResource getFragmentShader();
	}

	public ShaderDepthRGBA() 
	{
		super(Resources.INSTANCE);
	}

	@Override
	protected void initUniforms()
	{
	}
	
	@Override
	protected void updateVertexSource(String src)
	{
		List<String> vars = Arrays.asList(
			ChunksVertexShader.MORPH_TARGET_PARS
		);
		
		List<String> main = Arrays.asList(
			ChunksVertexShader.MORPH_TARGET,
			ChunksVertexShader.DEFAULT
		);

		super.updateVertexSource(Shader.updateShaderSource(src, vars, main));
	}

}
