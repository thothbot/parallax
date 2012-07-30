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

import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.TextureMagFilter;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
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
		this(1);
	}
	
	public BloomPass( float strength )
	{
		this(strength, 25, 4.0f, 256);
	}

	public BloomPass( float strength, int kernelSize, float sigma, int resolution ) 
	{
		super();

		// render targets
		this.renderTargetX = new RenderTargetTexture( resolution, resolution );
		this.renderTargetX.setMinFilter(TextureMinFilter.LINEAR);
		this.renderTargetX.setMagFilter(TextureMagFilter.LINEAR);
		this.renderTargetX.setFormat(PixelFormat.RGB);
		
		this.renderTargetY = new RenderTargetTexture( resolution, resolution );
		this.renderTargetY.setMinFilter(TextureMinFilter.LINEAR);
		this.renderTargetY.setMagFilter(TextureMagFilter.LINEAR);
		this.renderTargetY.setFormat(PixelFormat.RGB);

		// screen material
		Shader screenShader = new ShaderScreen();

		this.screenUniforms = UniformsUtils.clone( screenShader.getUniforms() );
		this.screenUniforms.get("opacity").value = strength;
		
		this.materialScreen = new ShaderMaterial();
		this.materialScreen.setUniforms(this.screenUniforms);
		this.materialScreen.setVertexShaderSource(screenShader.getVertexSource());
		this.materialScreen.setFragmentShaderSource(screenShader.getFragmentSource());
		this.materialScreen.setBlending(Material.BLENDING.ADDITIVE);
		this.materialScreen.setTransparent(true);

		// convolution material
		Shader convolutionShader = new ShaderConvolution();

		this.convolutionUniforms = UniformsUtils.clone( convolutionShader.getUniforms() );
		this.convolutionUniforms.get("uImageIncrement").value = BloomPass.blurX;
		this.convolutionUniforms.get("cKernel").value = Shader.buildKernel( sigma );

		this.materialConvolution = new ShaderMaterial();
		this.materialConvolution.setUniforms(this.convolutionUniforms);
		this.materialConvolution.setVertexShaderSource(
				"#define KERNEL_SIZE " + kernelSize + ".0\n" + convolutionShader.getVertexSource());
		this.materialConvolution.setFragmentShaderSource(
				"#define KERNEL_SIZE " + kernelSize + "\n"   + convolutionShader.getFragmentSource());
	}

	@Override
	public void render(EffectComposer effectComposer, float delta, boolean maskActive)
	{
		if ( maskActive ) 
			effectComposer.getRenderer().getGL().disable( GLenum.STENCIL_TEST.getValue() );

		// Render quad with blured scene into texture (convolution pass 1)
		effectComposer.getQuad().setMaterial(this.materialConvolution);

		this.convolutionUniforms.get("tDiffuse" ).texture = effectComposer.getReadBuffer();
		this.convolutionUniforms.get("uImageIncrement").value = BloomPass.blurX;

		effectComposer.getRenderer().render( 
				effectComposer.getScene(), effectComposer.getCamera(), this.renderTargetX, true );


		// Render quad with blured scene into texture (convolution pass 2)
		this.convolutionUniforms.get("tDiffuse").texture = this.renderTargetX;
		this.convolutionUniforms.get("uImageIncrement").value = BloomPass.blurY;

		effectComposer.getRenderer().render( 
				effectComposer.getScene(), effectComposer.getCamera(), this.renderTargetY, true );

		// Render original scene with superimposed blur to texture
		effectComposer.getQuad().setMaterial(this.materialScreen);

		this.screenUniforms.get("tDiffuse").texture = this.renderTargetY;

		if ( maskActive ) 
			effectComposer.getRenderer().getGL().enable( GLenum.STENCIL_TEST.getValue() );

		effectComposer.getRenderer().render( 
				effectComposer.getScene(), effectComposer.getCamera(), effectComposer.getReadBuffer(), this.clear );
	}

}
