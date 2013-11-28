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

import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.plugins.postprocessing.shaders.FilmShader;

public class FilmPass extends Pass
{
	private ShaderMaterial material;
	private boolean isRenderToScreen = false;

	public FilmPass( double noiseIntensity, double scanlinesIntensity, int scanlinesCount, boolean grayscale ) 
	{
		this.material = new ShaderMaterial(new FilmShader());

		this.material.getShader().getUniforms().get("grayscale").setValue( grayscale );
		this.material.getShader().getUniforms().get("nIntensity").setValue( noiseIntensity );
		this.material.getShader().getUniforms().get("sIntensity").setValue( scanlinesIntensity );
		this.material.getShader().getUniforms().get("sCount").setValue( scanlinesCount );

		this.setEnabled(true);
		this.setNeedsSwap(true);
	}
	
	public boolean isRenderToScreen() {
		return this.isRenderToScreen;
	}
	
	public void setRenderToScreen(boolean isRenderToScreen) {
		this.isRenderToScreen = isRenderToScreen;
	}
	
	@Override
	public void render(Postprocessing effectCocmposer, double delta, boolean maskActive)
	{
		this.material.getShader().getUniforms().get("tDiffuse").setValue( effectCocmposer.getReadBuffer() );
		this.material.getShader().getUniforms().get( "time" ).setValue( (Double)this.material.getShader().getUniforms().get( "time" ).getValue() + delta );

		effectCocmposer.getQuad().setMaterial(this.material);

		if ( this.isRenderToScreen )
			effectCocmposer.getRenderer().render( 
					effectCocmposer.getScene(), effectCocmposer.getCamera() );

		else
			effectCocmposer.getRenderer().render( 
					effectCocmposer.getScene(), effectCocmposer.getCamera(), effectCocmposer.getWriteBuffer(), false );

	}
}
