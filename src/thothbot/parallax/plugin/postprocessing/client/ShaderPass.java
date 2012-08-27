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

package thothbot.parallax.plugin.postprocessing.client;

import java.util.Map;

import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.Uniform;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.utils.UniformsUtils;

public class ShaderPass extends Pass
{
	private String textureID;
	private ShaderMaterial material;
	
	private boolean isRenderToScreen = false;

	private boolean isClear = false;
	
	public ShaderPass( Shader shader) 
	{
		this(shader, "tDiffuse");
	}
	
	public ShaderPass( Shader shader, String textureID ) 
	{
		this.setNeedsSwap(true);
		this.textureID = textureID;
		this.material = new ShaderMaterial(shader);
	}

	public boolean isRenderToScreen() {
		return this.isRenderToScreen;
	}
	
	public void setRenderToScreen(boolean isRenderToScreen) {
		this.isRenderToScreen = isRenderToScreen;
	}
	
	public Map<String, Uniform> getUniforms() {
		return this.material.getShader().getUniforms();
	}
	
	@Override
	public void render( Postprocessing postprocessing, double delta, boolean maskActive) 
	{
		if ( getUniforms().containsKey(this.textureID))
			getUniforms().get( this.textureID ).setTexture( postprocessing.getReadBuffer() );

		postprocessing.getQuad().setMaterial(this.material);

		if ( this.isRenderToScreen )
			postprocessing.getRenderer().render( 
				postprocessing.getScene(), postprocessing.getCamera() );
		else
			postprocessing.getRenderer().render( 
				postprocessing.getScene(), postprocessing.getCamera(), postprocessing.getWriteBuffer(), this.isClear );
	}
}
