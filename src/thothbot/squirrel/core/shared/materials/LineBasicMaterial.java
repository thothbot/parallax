/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.materials;

import thothbot.squirrel.core.client.shader.Shader;
import thothbot.squirrel.core.client.shader.ShaderBasic;
import thothbot.squirrel.core.shared.core.Color3f;

import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.canvas.dom.client.Context2d.LineJoin;

public final class LineBasicMaterial extends Material 
{

	public static class LineBasicMaterialOptions extends Material.MaterialOptions 
	{
		public Color3f color = new Color3f(0xffffff);
		public int linewidth = 1;
		public LineCap linecap = LineCap.ROUND;
		public LineJoin linejoin = LineJoin.ROUND;
	}
	
	private Color3f color;
	private LineCap linecap;
	private LineJoin linejoin;
	
	public LineBasicMaterial(LineBasicMaterialOptions options)
	{
		super(options);
		this.color = options.color;
		this.linewidth = options.linewidth;
		this.linecap = options.linecap;
		this.linejoin = options.linejoin;
	}

	public void setColor(Color3f color) 
	{
		this.color = color;
	}

	public Color3f getColor() 
	{
		return color;
	}

	public void setLinewidth(int linewidth) 
	{
		this.linewidth = linewidth;
	}

	public int getLinewidth() 
	{
		return linewidth;
	}

	public void setLinecap(LineCap linecap) 
	{
		this.linecap = linecap;
	}

	public LineCap getLinecap() 
	{
		return linecap;
	}

	public void setLinejoin(LineJoin linejoin) 
	{
		this.linejoin = linejoin;
	}

	public LineJoin getLinejoin() 
	{
		return linejoin;
	}
	
	public Shader getShaderId()
	{
		return new ShaderBasic();
	}
}
