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

package thothbot.parallax.plugins.postprocessing.client;

import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.plugins.postprocessing.client.shaders.CopyShader;

public class TexturePass extends Pass
{
	private ShaderMaterial material;
	
	public TexturePass ( Texture texture )
	{
		this(texture, 1.0);
	}
	
	public TexturePass ( Texture texture, double opacity ) 
	{
		this.material = new ShaderMaterial(new CopyShader());
		this.material.getShader().getUniforms().get("opacity").setValue( opacity );
		this.material.getShader().getUniforms().get("tDiffuse").setValue( texture );
		
		this.setEnabled(true);
		this.setNeedsSwap(false);
	}
	
	public ShaderMaterial getMaterial() {
		return this.material;
	}

	@Override
	public void render(Postprocessing postprocessing, double delta, boolean maskActive)
	{
		postprocessing.getQuad().setMaterial(this.material);

		postprocessing.getRenderer().render( 
				postprocessing.getScene(), postprocessing.getCamera(), postprocessing.getReadBuffer(), false );
	}
}
