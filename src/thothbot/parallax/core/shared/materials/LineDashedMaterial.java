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

package thothbot.parallax.core.shared.materials;

import java.util.Map;

import thothbot.parallax.core.client.shaders.DashedShader;
import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.math.Color;

/**
 * A material for drawing wireframe-style geometries with dashed lines.
 *
 */
public class LineDashedMaterial extends Material implements 
	HasFog, HasColor, HasVertexColors
{

	private boolean isFog;
	
	private Color color;
	
	private Material.COLORS vertexColors;
	
	private double linewidth;
	
	private double scale;
	private double dashSize;
	private double gapSize;
	
	public LineDashedMaterial()
	{
		setFog(true);
		
		setColor(new Color(0xffffff));
		
		setLinewidth(1.0);
		
		setVertexColors(Material.COLORS.NO);
		
		setScale(1.0);
		setDashSize(3.0);
		setGapSize(1.0);
	}

	@Override
	protected Shader getAssociatedShader() 
	{
		return new DashedShader();
	}
	
	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * The size of the dash. This is both the gap with the stroke. Default is 3.
	 * @return
	 */
	public double getDashSize() {
		return dashSize;
	}

	public void setDashSize(double dashSize) {
		this.dashSize = dashSize;
	}

	/**
	 * The size of the gap. Default is 1.
	 * @return
	 */
	public double getGapSize() {
		return gapSize;
	}

	public void setGapSize(double gapSize) {
		this.gapSize = gapSize;
	}
	
	/**
	 * The size of the gap. Default is 1.
	 * @return
	 */
	public double getLinewidth() {
		return this.linewidth;
	}
	
	public void setLinewidth(double linewidth) {
		this.linewidth = linewidth;
	}

	@Override
	public boolean isFog() {
		return this.isFog;
	}

	@Override
	public void setFog(boolean fog) {
		this.isFog = fog;
	}
	
	@Override
	public Color getColor() {
		return color;
	}
	
	@Override
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public Material.COLORS isVertexColors() {
		return this.vertexColors;
	}

	@Override
	public void setVertexColors(Material.COLORS vertexColors) {
		this.vertexColors = vertexColors;
	}
	
	public LineDashedMaterial clone() {

		LineDashedMaterial material = new LineDashedMaterial();
		
		super.clone(material);

		material.color.copy( this.color );

		material.linewidth = this.linewidth;

		material.scale = this.scale;
		material.dashSize = this.dashSize;
		material.gapSize = this.gapSize;

		material.vertexColors = this.vertexColors;

		material.isFog = this.isFog;

		return material;

	}
	
	@Override
	public void refreshUniforms(Camera camera, boolean isGammaInput) 
	{
		super.refreshUniforms(camera, isGammaInput);
		Map<String, Uniform> uniforms = getShader().getUniforms();
		
		uniforms.get("dashSize").setValue( getDashSize() );
		uniforms.get("totalSize").setValue( getDashSize() + getGapSize() );
		uniforms.get("scale").setValue( getScale() );
	}

}

