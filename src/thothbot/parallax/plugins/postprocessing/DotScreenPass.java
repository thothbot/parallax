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
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.plugins.postprocessing.shaders.DotScreenShader;

public class DotScreenPass extends Pass
{
	private ShaderMaterial material;
	private boolean isRenderToScreen = false;
	
	public DotScreenPass( Vector2 center, double angle, double scale ) 
	{
		this.material = new ShaderMaterial(new DotScreenShader());
		((Vector2) this.material.getShader().getUniforms().get("center").getValue()).copy( center );
		this.material.getShader().getUniforms().get("angle").setValue( angle );
		this.material.getShader().getUniforms().get("scale").setValue( scale );

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
		((Vector2) this.material.getShader().getUniforms().get("tSize").getValue()).set( 
				effectCocmposer.getReadBuffer().getWidth(), effectCocmposer.getReadBuffer().getHeight() );

		effectCocmposer.getQuad().setMaterial(this.material);

		if ( this.isRenderToScreen )
			effectCocmposer.getRenderer().render( 
					effectCocmposer.getScene(), effectCocmposer.getCamera() );

		else
			effectCocmposer.getRenderer().render( 
					effectCocmposer.getScene(), effectCocmposer.getCamera(), effectCocmposer.getWriteBuffer(), false );

	}

}
