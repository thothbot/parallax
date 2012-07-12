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
import thothbot.squirrel.core.client.shader.ShaderNormal;

import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.canvas.dom.client.Context2d.LineJoin;

public final class MeshNormalMaterial extends Material 
{
	
	public static class MeshNormalMaterialOptions extends Material.MaterialOptions 
	{
		public Material.SHADING shading = Material.SHADING.SMOOTH;
		public LineCap wireframeLinecap = LineCap.ROUND;
		public LineJoin wireframeLinejoin = LineJoin.ROUND;
	}
	
	private LineCap wireframeLinecap;
	private LineJoin wireframeLinejoin;


	public MeshNormalMaterial(MeshNormalMaterialOptions options)
	{
		super(options);
		this.shading = options.shading;
		this.wireframeLinecap = options.wireframeLinecap;
		this.wireframeLinejoin = options.wireframeLinejoin;
	}

	public void setWireframeLinecap(LineCap wireframeLinecap) 
	{
		this.wireframeLinecap = wireframeLinecap;
	}

	public LineCap getWireframeLinecap() 
	{
		return wireframeLinecap;
	}

	public void setWireframeLinejoin(LineJoin wireframeLinejoin) 
	{
		this.wireframeLinejoin = wireframeLinejoin;
	}

	public LineJoin getWireframeLinejoin() 
	{
		return wireframeLinejoin;
	}

	public Shader getShaderId()
	{
		return new ShaderNormal();
	}
}
