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

import org.parallax3d.parallax.graphics.renderers.shaders.DashedShader;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.cameras.Camera;

/**
 * A material for drawing wireframe-style geometries with dashed lines.
 *
 */
@ThreejsObject("THREE.LineDashedMaterial")
public class LineDashedMaterial extends LineBasicMaterial
{
	private double scale;
	private double dashSize;
	private double gapSize;

	public LineDashedMaterial()
	{
		super();

		setScale(1.0);
		setDashSize(3.0);
		setGapSize(1.0);
	}

	@Override
	public Shader getAssociatedShader()
	{
		return new DashedShader();
	}

	public double getScale() {
		return scale;
	}

	public LineDashedMaterial setScale(double scale) {
		this.scale = scale;
		return this;
	}

	/**
	 * The size of the dash. This is both the gap with the stroke. Default is 3.
	 * @return
	 */
	public double getDashSize() {
		return dashSize;
	}

	public LineDashedMaterial setDashSize(double dashSize) {
		this.dashSize = dashSize;
		return this;
	}

	/**
	 * The size of the gap. Default is 1.
	 * @return
	 */
	public double getGapSize() {
		return gapSize;
	}

	public LineDashedMaterial setGapSize(double gapSize) {
		this.gapSize = gapSize;
		return this;
	}

	@Override
	public LineDashedMaterial clone() {

		LineDashedMaterial material = new LineDashedMaterial();

		super.clone(material);

		material.scale = this.scale;
		material.dashSize = this.dashSize;
		material.gapSize = this.gapSize;

		return material;

	}

	@Override
	public void refreshUniforms(Camera camera, boolean isGammaInput)
	{
		super.refreshUniforms(camera, isGammaInput);
		FastMap<Uniform> uniforms = getShader().getUniforms();

		uniforms.get("dashSize").setValue( getDashSize() );
		uniforms.get("totalSize").setValue( getDashSize() + getGapSize() );
		uniforms.get("scale").setValue( getScale() );
	}

}

