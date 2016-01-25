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

package org.parallax3d.parallax.graphics.materials;

import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.renderers.shaders.NormalShader;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * A material that maps the normal vectors to RGB colors.
 *
 */
@ThreejsObject("THREE.MeshNormalMaterial")
public final class MeshNormalMaterial extends Material implements HasWireframe, HasShading
{
	private boolean isWireframe;
	private int wireframeLineWidth;

	private Material.SHADING shading;

	public MeshNormalMaterial()
	{
		setWireframe(false);
		setWireframeLineWidth(1);

		setShading(Material.SHADING.FLAT);
	}

	public Shader getAssociatedShader()
	{
		return new NormalShader();
	}

	@Override
	public boolean isWireframe() {
		return this.isWireframe;
	}

	@Override
	public MeshNormalMaterial setWireframe(boolean wireframe) {
		this.isWireframe = wireframe;
		return this;
	}

	@Override
	public int getWireframeLineWidth() {
		return this.wireframeLineWidth;
	}

	@Override
	public MeshNormalMaterial setWireframeLineWidth(int wireframeLineWidth) {
		this.wireframeLineWidth = wireframeLineWidth;
		return this;
	}

	public Material.SHADING getShading() {
		return this.shading;
	}

	public MeshNormalMaterial setShading(Material.SHADING shading) {
		this.shading = shading;
		return this;
	}

	@Override
	public MeshNormalMaterial clone() {

		MeshNormalMaterial material = new MeshNormalMaterial();

		super.clone(material);

		material.shading = this.shading;

		material.isWireframe = this.isWireframe;
		material.wireframeLineWidth = this.wireframeLineWidth;

		return material;

	}

	@Override
	public void refreshUniforms(Camera camera, boolean isGammaInput)
	{
		super.refreshUniforms(camera, isGammaInput);

		getShader().getUniforms().get("opacity").setValue( getOpacity() );
	}
}
