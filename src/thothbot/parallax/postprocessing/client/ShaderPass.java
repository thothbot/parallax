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

package thothbot.parallax.postprocessing.client;

import java.util.Map;

import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.Uniform;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.utils.UniformsUtils;

public class ShaderPass extends Pass
{
	private String textureID;
	private Map<String, Uniform> uniforms;
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

		this.uniforms = UniformsUtils.clone( shader.getUniforms() );

		this.material = new ShaderMaterial();
		this.material.setUniforms(this.uniforms);
		this.material.setVertexShaderSource(shader.getVertexSource());
		this.material.setFragmentShaderSource(shader.getFragmentSource());
	}
	
	public Map<String, Uniform> getUniforms() {
		return this.uniforms;
	}
	
	public boolean isRenderToScreen() {
		return this.isRenderToScreen;
	}
	
	public void setRenderToScreen(boolean isRenderToScreen) {
		this.isRenderToScreen = isRenderToScreen;
	}
	
	@Override
	public void render( EffectComposer effectComposer, double delta, boolean maskActive) 
	{
		if ( this.uniforms.containsKey(this.textureID))
			this.uniforms.get( this.textureID ).setTexture( effectComposer.getReadBuffer() );

		effectComposer.getQuad().setMaterial(this.material);

		if ( this.isRenderToScreen )
			effectComposer.getRenderer().render( 
				effectComposer.getScene(), effectComposer.getCamera() );
		else
			effectComposer.getRenderer().render( 
				effectComposer.getScene(), effectComposer.getCamera(), effectComposer.getWriteBuffer(), this.isClear );
	}
}
