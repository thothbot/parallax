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

import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.ShaderBasic;
import thothbot.parallax.core.shared.core.Color3f;

public final class LineBasicMaterial extends Material 
	implements HasFog, HasColor, HasVertexColors
{

	private boolean isFog;
	
	private Color3f color;
	
	private Material.COLORS vertexColors;
	
	private float linewidth;
	
	public LineBasicMaterial()
	{	
		setFog(true);
		
		setColor(new Color3f(0xffffff));
		
		setLinewidth(1.0f);
		
		setVertexColors(Material.COLORS.NO);
	}
	
	@Override
	public Shader getShaderId()
	{
		return new ShaderBasic();
	}

	public float getLinewidth() {
		return this.linewidth;
	}
	
	public void setLinewidth(float linewidth) {
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
	public Color3f getColor() {
		return color;
	}
	
	@Override
	public void setColor(Color3f color) {
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
}
