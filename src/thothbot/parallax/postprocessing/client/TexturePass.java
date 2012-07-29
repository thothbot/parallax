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

import thothbot.parallax.core.client.renderers.WebGLRenderTarget;
import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.Uniform;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.textures.Texture;
import thothbot.parallax.core.shared.utils.UniformsUtils;

import thothbot.parallax.postprocessing.client.shader.ShaderScreen;

public class TexturePass extends Pass
{

	private Map<String, Uniform> uniforms;
	private ShaderMaterial material;
	
	public TexturePass ( Texture texture )
	{
		this(texture, 1.0f);
	}
	
	public TexturePass ( Texture texture, float opacity ) 
	{
		Shader shader = new ShaderScreen();

		this.uniforms = UniformsUtils.clone( shader.getUniforms() );

		this.uniforms.get("opacity").value = opacity;
		this.uniforms.get("tDiffuse").texture = texture;

		ShaderMaterial.ShaderMaterialOptions shaderMaterialopt = new ShaderMaterial.ShaderMaterialOptions();
		shaderMaterialopt.uniforms = this.uniforms;
		shaderMaterialopt.vertexShader = shader.getVertexSource();
		shaderMaterialopt.fragmentShader = shader.getFragmentSource();
		
		this.material = new ShaderMaterial(shaderMaterialopt);
		
		this.setEnabled(true);
		this.setNeedsSwap(false);

	}
	@Override
	public void render(WebGLRenderTarget writeBuffer, WebGLRenderTarget readBuffer, float delta,
			boolean maskActive)
	{
		EffectComposer.quad.setMaterial(this.material);

		getRenderer().render( EffectComposer.scene, EffectComposer.camera, readBuffer, false );
	}

}
