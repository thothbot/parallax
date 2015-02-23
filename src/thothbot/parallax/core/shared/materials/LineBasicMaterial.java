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

import thothbot.parallax.core.client.shaders.BasicShader;
import thothbot.parallax.core.client.shaders.Shader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.math.Color;

/**
 * A material for drawing wireframe-style geometries.
 * 
 * @author thothbot
 *
 */
public class LineBasicMaterial extends Material 
	implements HasFog, HasColor, HasVertexColors
{

	private boolean isFog = true;
	
	private Color color;
	
	private Material.COLORS vertexColors;
	
	private double linewidth;
	
	public LineBasicMaterial()
	{	
		this.isFog = true;
		
		this.color = new Color(0xffffff);
		
		setLinewidth(1.0);
		
		setVertexColors(Material.COLORS.NO);
	}
	
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
	
	/**
	 * Line color in hexadecimal. Default is 0xffffff.
	 */
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
	
	public LineBasicMaterial clone() {

		LineBasicMaterial material = new LineBasicMaterial();
		super.clone(material);


		material.color.copy( this.color );

		material.linewidth = this.linewidth;

		material.vertexColors = this.vertexColors;

		material.isFog = this.isFog;

		return material;

	}
	
	@Override
	public void refreshUniforms(Camera camera, boolean isGammaInput) 
	{
		super.refreshUniforms(camera, isGammaInput);
		Map<String, Uniform> uniforms = getShader().getUniforms();
		
		uniforms.get("diffuse").setValue( getColor() );
		uniforms.get("opacity").setValue( getOpacity() );
	}
}
