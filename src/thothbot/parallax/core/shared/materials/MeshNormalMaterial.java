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

package thothbot.parallax.core.shared.materials;

import thothbot.parallax.core.client.context.Canvas3d;
import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.ShaderNormal;
import thothbot.parallax.core.shared.cameras.Camera;

public final class MeshNormalMaterial extends Material implements HasWireframe
{
	private boolean isWireframe;
	private int wireframeLineWidth;
	
	public MeshNormalMaterial()
	{
		setWireframe(false);
		setWireframeLineWidth(1);
		
		setShading(Material.SHADING.FLAT);
	}

	public Shader getShaderId()
	{
		return new ShaderNormal();
	}
	
	@Override
	public boolean isWireframe() {
		return this.isWireframe;
	}

	@Override
	public void setWireframe(boolean wireframe) {
		this.isWireframe = wireframe;
	}

	@Override
	public int getWireframeLineWidth() {
		return this.wireframeLineWidth;
	}

	@Override
	public void setWireframeLineWidth(int wireframeLineWidth) {
		this.wireframeLineWidth = wireframeLineWidth;
	}
	
	@Override
	public void refreshUniforms(Canvas3d canvas, Camera camera, boolean isGammaInput) 
	{
		super.refreshUniforms(canvas, camera, isGammaInput);
		
		getShader().getUniforms().get("opacity").setValue( getOpacity() );
	}
}
