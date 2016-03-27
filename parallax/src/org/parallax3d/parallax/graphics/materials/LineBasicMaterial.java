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
import org.parallax3d.parallax.graphics.renderers.shaders.BasicShader;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.renderers.shaders.Uniform;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * A material for drawing wireframe-style geometries.
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.LineBasicMaterial")
public class LineBasicMaterial extends Material 
	implements HasFog, HasColor, HasVertexColors
{

	Color color = new Color(0xffffff);

	double linewidth = 1.;
	String linecap = "round";
	String linejoin = "round";

	boolean fog = true;

	Material.COLORS vertexColors = COLORS.NO;

	@Override
	public Shader getAssociatedShader()
	{
		return new BasicShader();
	}

	/**
	 * Line thickness. Default is 1.
	 * @return
	 */
	public double getLinewidth() {
		return this.linewidth;
	}

	/**
	 * Controls line thickness. Default is 1.
	 * <p>
	 * Due to limitations in the <a href="https://code.google.com/p/angleproject/">ANGLE layer</a>, on Windows platforms linewidth will always be 1 regardless of the set value.
	 * @param linewidth
	 */
	public LineBasicMaterial setLinewidth(double linewidth) {
		this.linewidth = linewidth;
		return this;
	}

	@Override
	public boolean isFog() {
		return this.fog;
	}

	@Override
	public LineBasicMaterial setFog(boolean fog) {
		this.fog = fog;
		return this;
	}

	/**
	 * Line color in hexadecimal. Default is 0xffffff.
	 */
	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public LineBasicMaterial setColor(Color color) {
		this.color = color;
		return this;
	}

	@Override
	public LineBasicMaterial setColor(int color) {
		this.color = new Color( color );
		return this;
	}

	@Override
	public Material.COLORS getVertexColors() {
		return this.vertexColors;
	}

	@Override
	public LineBasicMaterial setVertexColors(Material.COLORS vertexColors) {
		this.vertexColors = vertexColors;
		return this;
	}

	public LineBasicMaterial copy(LineBasicMaterial source)
	{
		super.copy( source );

		this.color.copy( source.color );

		this.linewidth = source.linewidth;
		this.linecap = source.linecap;
		this.linejoin = source.linejoin;

		this.vertexColors = source.vertexColors;

		this.fog = source.fog;

		return this;
	}

	@Override
	public LineBasicMaterial clone()
	{
		return new LineBasicMaterial().copy(this);
	}
}
