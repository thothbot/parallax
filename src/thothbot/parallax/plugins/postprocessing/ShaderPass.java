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

package thothbot.parallax.plugins.postprocessing;

import java.util.Map;

import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.materials.ShaderMaterial;

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
	
	public ShaderMaterial getMaterial() {
		return this.material;
	}
	
	@Override
	public void render( Postprocessing postprocessing, double delta, boolean maskActive) 
	{
		if ( getUniforms().containsKey(this.textureID))
			getUniforms().get( this.textureID ).setValue( postprocessing.getReadBuffer() );

		postprocessing.getQuad().setMaterial(this.material);

		if ( this.isRenderToScreen )
			postprocessing.getRenderer().render( 
				postprocessing.getScene(), postprocessing.getCamera() );
		else
			postprocessing.getRenderer().render( 
				postprocessing.getScene(), postprocessing.getCamera(), postprocessing.getWriteBuffer(), this.isClear );
	}
}
