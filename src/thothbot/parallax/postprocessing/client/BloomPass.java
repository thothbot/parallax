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

import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.TextureMagFilter;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.textures.RenderTargetTexture;
import thothbot.parallax.core.shared.core.Vector2;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.postprocessing.client.shader.ShaderConvolution;
import thothbot.parallax.postprocessing.client.shader.ShaderScreen;

public class BloomPass extends Pass
{
	private static Vector2 blurX = new Vector2( 0.001953125, 0.0 );
	private static Vector2 blurY = new Vector2( 0.0, 0.001953125 );
	
	private RenderTargetTexture renderTargetX;
	private RenderTargetTexture renderTargetY;
	
	private ShaderMaterial materialScreen;
	private ShaderMaterial materialConvolution;
	
	private boolean clear = false;
	
	public BloomPass()
	{
		this(1);
	}
	
	public BloomPass( double strength )
	{
		this(strength, 25, 4.0, 256);
	}

	public BloomPass( double strength, int kernelSize, double sigma, int resolution ) 
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

		
		this.materialScreen = new ShaderMaterial(new ShaderScreen());
		this.materialScreen.getShader().getUniforms().get("opacity").setValue( strength );
		this.materialScreen.setBlending(Material.BLENDING.ADDITIVE);
		this.materialScreen.setTransparent(true);

		// convolution material
		Shader convolutionShader = new ShaderConvolution();

		this.materialConvolution = new ShaderMaterial(
				"#define KERNEL_SIZE " + kernelSize + ".0\n" + convolutionShader.getVertexSource(),
				"#define KERNEL_SIZE " + kernelSize + "\n"   + convolutionShader.getFragmentSource()
				);
		this.materialConvolution.getShader().setUniforms(convolutionShader.getUniforms());
		this.materialConvolution.getShader().getUniforms().get("uImageIncrement").setValue( BloomPass.blurX );
		this.materialConvolution.getShader().getUniforms().get("cKernel").setValue( Shader.buildKernel( sigma ) );
	}

	@Override
	public void render(EffectComposer effectComposer, double delta, boolean maskActive)
	{
		if ( maskActive ) 
			effectComposer.getRenderer().getGL().disable( GLenum.STENCIL_TEST.getValue() );

		// Render quad with blured scene into texture (convolution pass 1)
		effectComposer.getQuad().setMaterial(this.materialConvolution);

		this.materialConvolution.getShader().getUniforms().get("tDiffuse" ).setTexture( effectComposer.getReadBuffer() );
		this.materialConvolution.getShader().getUniforms().get("uImageIncrement").setValue( BloomPass.blurX );

		effectComposer.getRenderer().render( 
				effectComposer.getScene(), effectComposer.getCamera(), this.renderTargetX, true );


		// Render quad with blured scene into texture (convolution pass 2)
		this.materialConvolution.getShader().getUniforms().get("tDiffuse").setTexture( this.renderTargetX );
		this.materialConvolution.getShader().getUniforms().get("uImageIncrement").setValue( BloomPass.blurY );

		effectComposer.getRenderer().render( 
				effectComposer.getScene(), effectComposer.getCamera(), this.renderTargetY, true );

		// Render original scene with superimposed blur to texture
		effectComposer.getQuad().setMaterial(this.materialScreen);

		this.materialScreen.getShader().getUniforms().get("tDiffuse").setTexture( this.renderTargetY );

		if ( maskActive ) 
			effectComposer.getRenderer().getGL().enable( GLenum.STENCIL_TEST.getValue() );

		effectComposer.getRenderer().render( 
				effectComposer.getScene(), effectComposer.getCamera(), effectComposer.getReadBuffer(), this.clear );
	}

}
