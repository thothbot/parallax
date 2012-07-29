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

import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.Uniform;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.utils.UniformsUtils;

import thothbot.parallax.postprocessing.client.shader.ShaderFilm;

public class FilmPass extends Pass
{
	private Map<String, Uniform> uniforms;
	private ShaderMaterial material;
	private boolean renderToScreen = false;

	public FilmPass( int noiseIntensity, int scanlinesIntensity, int scanlinesCount, float grayscale ) 
	{
		Shader shader = new ShaderFilm();

		this.uniforms = UniformsUtils.clone( shader.getUniforms() );

		this.material = new ShaderMaterial();
		this.material.setUniforms(this.uniforms);
		this.material.setVertexShaderSource(shader.getVertexSource());
		this.material.setFragmentShaderSource(shader.getFragmentSource());

		this.uniforms.get("grayscale").value = grayscale;
		this.uniforms.get("nIntensity").value = noiseIntensity;
		this.uniforms.get("sIntensity").value = scanlinesIntensity;
		this.uniforms.get("sCount").value = scanlinesCount;

		this.setEnabled(true);
		this.setNeedsSwap(true);
	}
	
	@Override
	public void render(WebGLRenderer renderer, RenderTargetTexture writeBuffer, RenderTargetTexture readBuffer, float delta,
			boolean maskActive)
	{
		this.uniforms.get("tDiffuse").texture = readBuffer;
		this.uniforms.get( "time" ).value = (Float)this.uniforms.get( "time" ).value + delta;

		EffectComposer.quad.setMaterial(this.material);

		if ( this.renderToScreen )
			renderer.render( EffectComposer.scene, EffectComposer.camera );

		else
			renderer.render( EffectComposer.scene, EffectComposer.camera, writeBuffer, false );

	}
}
