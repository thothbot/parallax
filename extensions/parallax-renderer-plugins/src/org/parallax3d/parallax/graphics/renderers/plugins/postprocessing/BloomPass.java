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

package org.parallax3d.parallax.graphics.renderers.plugins.postprocessing;

import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.renderers.RenderTargetTexture;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.shaders.ConvolutionShader;
import org.parallax3d.parallax.graphics.renderers.plugins.postprocessing.shaders.CopyShader;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.gl.enums.EnableCap;
import org.parallax3d.parallax.system.gl.enums.PixelFormat;
import org.parallax3d.parallax.system.gl.enums.TextureMagFilter;
import org.parallax3d.parallax.system.gl.enums.TextureMinFilter;

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

		this.materialScreen = new ShaderMaterial(new CopyShader());
		this.materialScreen.getShader().getUniforms().get("opacity").setValue( strength );
		this.materialScreen.setBlending(Material.BLENDING.ADDITIVE);
		this.materialScreen.setTransparent(true);

		// convolution material
		Shader convolutionShader = new ConvolutionShader();

		this.materialConvolution = new ShaderMaterial(
				"#define KERNEL_SIZE " + kernelSize + ".0\n" + convolutionShader.getVertexSource(),
				"#define KERNEL_SIZE " + kernelSize + "\n"   + convolutionShader.getFragmentSource()
				);
		this.materialConvolution.getShader().setUniforms(convolutionShader.getUniforms());
		this.materialConvolution.getShader().getUniforms().get("uImageIncrement").setValue( BloomPass.blurX );
		this.materialConvolution.getShader().getUniforms().get("cKernel").setValue( Shader.buildKernel( sigma ) );
	}

	@Override
	public void render(Postprocessing postprocessing, double delta, boolean maskActive)
	{
		if ( maskActive ) 
			postprocessing.getRenderer().gl.glDisable( EnableCap.STENCIL_TEST.getValue() );

		// Render quad with blured scene into texture (convolution pass 1)
		postprocessing.getQuad().setMaterial(this.materialConvolution);

		this.materialConvolution.getShader().getUniforms().get("tDiffuse" ).setValue( postprocessing.getReadBuffer() );
		this.materialConvolution.getShader().getUniforms().get("uImageIncrement").setValue( BloomPass.blurX );

		postprocessing.getRenderer().render( 
				postprocessing.getScene(), postprocessing.getCamera(), this.renderTargetX, true );


		// Render quad with blured scene into texture (convolution pass 2)
		this.materialConvolution.getShader().getUniforms().get("tDiffuse").setValue( this.renderTargetX );
		this.materialConvolution.getShader().getUniforms().get("uImageIncrement").setValue( BloomPass.blurY );

		postprocessing.getRenderer().render( 
				postprocessing.getScene(), postprocessing.getCamera(), this.renderTargetY, true );

		// Render original scene with superimposed blur to texture
		postprocessing.getQuad().setMaterial(this.materialScreen);

		this.materialScreen.getShader().getUniforms().get("tDiffuse").setValue( this.renderTargetY );

		if ( maskActive ) 
			postprocessing.getRenderer().gl.glEnable( EnableCap.STENCIL_TEST.getValue() );

		postprocessing.getRenderer().render( 
				postprocessing.getScene(), postprocessing.getCamera(), postprocessing.getReadBuffer(), this.clear );
	}

}
