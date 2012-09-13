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

import java.util.Map;

import thothbot.parallax.core.client.context.Canvas3d;
import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.ShaderDepth;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.cameras.OrthographicCamera;

public class MeshDepthMaterial extends Material implements HasWireframe
{
	private boolean isWireframe;
	private int wireframeLineWidth;

	public MeshDepthMaterial()
	{	
		setWireframe(false);
		setWireframeLineWidth(1);
	}
		
	public Material.SHADING bufferGuessNormalType () 
	{
		// only MeshBasicMaterial and MeshDepthMaterial don't need normals
		return null;
	}
	
	@Override
	public Shader getAssociatedShader()
	{
		return new ShaderDepth();
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
		Map<String, Uniform> uniforms = getShader().getUniforms();
		
		if(camera.getClass() == OrthographicCamera.class) 
		{
			uniforms.get("mNear").setValue( ((OrthographicCamera) camera).getNear() );
			uniforms.get("mFar").setValue( ((OrthographicCamera) camera).getFar() );
		}
		
		uniforms.get("opacity").setValue( getOpacity() );
	}
}
