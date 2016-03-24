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
	boolean wireframe = false;
	double wireframeLineWidth = 1.0;

	Material.SHADING shading = SHADING.SMOOTH;

	boolean morphTargets = false;

	public Shader getAssociatedShader()
	{
		return new NormalShader();
	}

	@Override
	public boolean isWireframe() {
		return this.wireframe;
	}

	@Override
	public MeshNormalMaterial setWireframe(boolean wireframe) {
		this.wireframe = wireframe;
		return this;
	}

	@Override
	public double getWireframeLineWidth() {
		return this.wireframeLineWidth;
	}

	@Override
	public MeshNormalMaterial setWireframeLineWidth(double wireframeLineWidth) {
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
		return new MeshNormalMaterial().copy(this);
	}

	public MeshNormalMaterial copy(MeshNormalMaterial source )
	{
		super.copy( source );

		this.wireframe = source.wireframe;
		this.wireframeLineWidth = source.wireframeLineWidth;
		this.shading = source.shading;

		return this;

	}
}
