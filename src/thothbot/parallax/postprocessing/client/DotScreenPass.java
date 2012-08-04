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
import thothbot.parallax.core.shared.core.Vector2;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.materials.ShaderMaterial;
import thothbot.parallax.core.shared.utils.UniformsUtils;
import thothbot.parallax.postprocessing.client.shader.ShaderDotscreen;

public class DotScreenPass extends Pass
{
	private Map<String, Uniform> uniforms;
	private ShaderMaterial material;
	private boolean renderToScreen = false;
	
	public DotScreenPass( Vector3 center, double angle, double scale ) 
	{
		Shader shader = new ShaderDotscreen();

		this.uniforms = UniformsUtils.clone( shader.getUniforms() );

		((Vector3) this.uniforms.get("center").getValue()).copy( center );
		this.uniforms.get("angle").setValue( angle );
		this.uniforms.get("scale").setValue( scale );

		this.material = new ShaderMaterial();
		this.material.setUniforms(this.uniforms);
		this.material.setVertexShaderSource(shader.getVertexSource());
		this.material.setFragmentShaderSource(shader.getFragmentSource());

		this.setEnabled(true);
		this.setNeedsSwap(true);
	}
	
	@Override
	public void render(EffectComposer effectCocmposer, double delta, boolean maskActive)
	{
		this.uniforms.get("tDiffuse").setTexture( effectCocmposer.getReadBuffer() );
		((Vector2) this.uniforms.get("tSize").getValue()).set( 
				effectCocmposer.getReadBuffer().getWidth(), effectCocmposer.getReadBuffer().getHeight() );

		effectCocmposer.getQuad().setMaterial(this.material);

		if ( this.renderToScreen )
			effectCocmposer.getRenderer().render( 
					effectCocmposer.getScene(), effectCocmposer.getCamera() );

		else
			effectCocmposer.getRenderer().render( 
					effectCocmposer.getScene(), effectCocmposer.getCamera(), effectCocmposer.getWriteBuffer(), false );

	}

}
