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
import org.parallax3d.parallax.graphics.cameras.HasNearFar;
import org.parallax3d.parallax.graphics.renderers.shaders.DepthShader;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * A material for drawing geometry by depth. Depth is based off of the camera near and far plane. White is nearest, black is farthest.
 *
 */
@ThreejsObject("THREE.MeshDepthMaterial")
public class MeshDepthMaterial extends Material implements HasWireframe
{
	boolean morphTargets = false;
	boolean wireframe = false;
	double wireframeLineWidth = 1.0;

	@Override
	public Shader getAssociatedShader()
	{
		return new DepthShader();
	}

	@Override
	public boolean isWireframe() {
		return this.wireframe;
	}

	@Override
	public MeshDepthMaterial setWireframe(boolean wireframe) {
		this.wireframe = wireframe;
		return this;
	}

	@Override
	public double getWireframeLineWidth() {
		return this.wireframeLineWidth;
	}

	@Override
	public MeshDepthMaterial setWireframeLineWidth(double wireframeLineWidth) {
		this.wireframeLineWidth = wireframeLineWidth;
		return this;
	}

	@Override
	public MeshDepthMaterial clone() {
		return new MeshDepthMaterial().copy(this);
	}

	public MeshDepthMaterial copy (MeshDepthMaterial source)
	{
		super.copy( source );

		this.wireframe = source.wireframe;
		this.wireframeLineWidth = source.wireframeLineWidth;

		return this;
	}
}
