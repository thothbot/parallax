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
import thothbot.parallax.core.client.shaders.BasicShader;
import thothbot.parallax.core.client.shaders.Uniform;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.Color;

public final class LineBasicMaterial extends Material 
	implements HasFog, HasColor, HasVertexColors
{

	private boolean isFog;
	
	private Color color;
	
	private Material.COLORS vertexColors;
	
	private double linewidth;
	
	public LineBasicMaterial()
	{	
		setFog(true);
		
		setColor(new Color(0xffffff));
		
		setLinewidth(1.0);
		
		setVertexColors(Material.COLORS.NO);
	}
	
	@Override
	public Shader getAssociatedShader()
	{
		return new BasicShader();
	}

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
	
	@Override
	public void refreshUniforms(Canvas3d canvas, Camera camera, boolean isGammaInput) 
	{
		super.refreshUniforms(canvas, camera, isGammaInput);
		Map<String, Uniform> uniforms = getShader().getUniforms();
		
		uniforms.get("diffuse").setValue( getColor() );
		uniforms.get("opacity").setValue( getOpacity() );
	}
}
