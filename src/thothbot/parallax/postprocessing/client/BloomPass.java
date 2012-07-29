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

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.Uniform;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.shared.core.Vector2f;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.utils.UniformsUtils;

import thothbot.parallax.postprocessing.client.shader.ShaderConvolution;
import thothbot.parallax.postprocessing.client.shader.ShaderScreen;

public class BloomPass extends Pass
{
	private static Vector2f blurX = new Vector2f( 0.001953125f, 0.0f );
	private static Vector2f blurY = new Vector2f( 0.0f, 0.001953125f );
	
	private RenderTargetTexture renderTargetX;
	private RenderTargetTexture renderTargetY;
	
	private Map<String, Uniform> screenUniforms;
	private Map<String, Uniform> convolutionUniforms;

	private ShaderMaterial materialScreen;
	private ShaderMaterial materialConvolution;
	
	private boolean clear = false;
	
	public BloomPass()
	{
		this(1, 25, 4.0f, 256);
	}

	public BloomPass( int strength, int kernelSize, float sigma, int resolution ) 
	{
		super();

		// render targets

		RenderTargetTexture.WebGLRenderTargetOptions pars = new RenderTargetTexture.WebGLRenderTargetOptions();
		pars.minFilter = Texture.FILTER.LINEAR;
		pars.magFilter = Texture.FILTER.LINEAR;
		pars.format = Texture.FORMAT.RGB;

		this.renderTargetX = new RenderTargetTexture( resolution, resolution, pars );
		this.renderTargetY = new RenderTargetTexture( resolution, resolution, pars );

		// screen material

		Shader screenShader = new ShaderScreen();

		this.screenUniforms = UniformsUtils.clone( screenShader.getUniforms() );

		this.screenUniforms.get("opacity").value = strength;
		
		ShaderMaterial.ShaderMaterialOptions shaderMaterialopt = new ShaderMaterial.ShaderMaterialOptions();
		shaderMaterialopt.uniforms =this.screenUniforms;
		shaderMaterialopt.vertexShader =screenShader.getVertexSource();
		shaderMaterialopt.fragmentShader =screenShader.getFragmentSource();
		shaderMaterialopt.blending = Material.BLENDING.ADDITIVE;
		shaderMaterialopt.transparent = true;

		this.materialScreen = new ShaderMaterial(shaderMaterialopt);

		// convolution material

		Shader convolutionShader = new ShaderConvolution();

		this.convolutionUniforms = UniformsUtils.clone( convolutionShader.getUniforms() );

		this.convolutionUniforms.get("uImageIncrement").value = BloomPass.blurX;
		this.convolutionUniforms.get("cKernel").value = Shader.buildKernel( sigma );

		ShaderMaterial.ShaderMaterialOptions shaderMaterialopt2 = new ShaderMaterial.ShaderMaterialOptions();
		shaderMaterialopt2.uniforms =this.convolutionUniforms;
		shaderMaterialopt2.vertexShader = "#define KERNEL_SIZE " + kernelSize + ".0\n" + convolutionShader.getVertexSource(); 
		shaderMaterialopt2.fragmentShader = "#define KERNEL_SIZE " + kernelSize + "\n"   + convolutionShader.getFragmentSource();
		
		this.materialConvolution = new ShaderMaterial(shaderMaterialopt2);
	}

	@Override
	public void render(RenderTargetTexture writeBuffer, RenderTargetTexture readBuffer, float delta, boolean maskActive)
	{
		if ( maskActive ) 
			getRenderer().getGL().disable( WebGLRenderingContext.STENCIL_TEST );

		// Render quad with blured scene into texture (convolution pass 1)

		EffectComposer.quad.setMaterial(this.materialConvolution);

		this.convolutionUniforms.get("tDiffuse" ).texture = readBuffer;
		this.convolutionUniforms.get("uImageIncrement").value = BloomPass.blurX;

		getRenderer().render( EffectComposer.scene, EffectComposer.camera, this.renderTargetX, true );


		// Render quad with blured scene into texture (convolution pass 2)

		this.convolutionUniforms.get("tDiffuse").texture = this.renderTargetX;
		this.convolutionUniforms.get("uImageIncrement").value = BloomPass.blurY;

		getRenderer().render( EffectComposer.scene, EffectComposer.camera, this.renderTargetY, true );

		// Render original scene with superimposed blur to texture

		EffectComposer.quad.setMaterial(this.materialScreen);

		this.screenUniforms.get("tDiffuse").texture = this.renderTargetY;

		if ( maskActive ) 
			getRenderer().getGL().enable( WebGLRenderingContext.STENCIL_TEST );

		getRenderer().render( EffectComposer.scene, EffectComposer.camera, readBuffer, this.clear );
	}

}
