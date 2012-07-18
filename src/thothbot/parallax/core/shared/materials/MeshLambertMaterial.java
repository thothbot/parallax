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
import thothbot.parallax.core.client.shader.ShaderLambert;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.core.Color3f;
import thothbot.parallax.core.shared.core.Vector3f;

import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.canvas.dom.client.Context2d.LineJoin;

public final class MeshLambertMaterial extends AbstractMapMaterial
{

	public static class MeshLambertMaterialOptions extends AbstractMapMaterial.AbstractMapMaterialOptions
	{
		public Color3f ambient = new Color3f( 0xffffff );
		public Color3f emissive = new Color3f( 0x000000 );
		public Texture.OPERATIONS combine = Texture.OPERATIONS.MULTIPLY;
		public Material.SHADING shading = Material.SHADING.SMOOTH;
		public LineCap wireframeLinecap = LineCap.ROUND;
		public LineJoin wireframeLinejoin = LineJoin.ROUND;
		public boolean skinning = false;
		public boolean morphTargets = false;
		public Vector3f wrapRGB = new Vector3f( 1f, 1f, 1f );
	}

	private Color3f ambient;
	private Color3f emissive;
	private Vector3f wrapRGB;
	private LineCap wireframeLinecap;
	private LineJoin wireframeLinejoin;
	private boolean skinning;
	private boolean morphTargets;

	public MeshLambertMaterial(MeshLambertMaterialOptions parameters) 
	{
		super(parameters);
		this.ambient = parameters.ambient;
		this.emissive = parameters.emissive;
		this.combine = parameters.combine;
		setShading(parameters.shading);
		this.wireframeLinecap = parameters.wireframeLinecap;
		this.wireframeLinejoin = parameters.wireframeLinejoin;
		this.skinning = parameters.skinning;
		this.morphTargets = parameters.morphTargets;
		this.wrapRGB = parameters.wrapRGB;
	}

	public Color3f getAmbient() 
	{
		return ambient;
	}
	
	public Color3f getEmissive() 
	{
		return this.emissive;
	}
	
	public Vector3f getWrapRGB() 
	{
		return this.wrapRGB;
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

	public void setSkinning(boolean skinning)
	{
		this.skinning = skinning;
	}

	public boolean isSkinning()
	{
		return skinning;
	}

	public void setMorphTargets(boolean morphTargets)
	{
		this.morphTargets = morphTargets;
	}

	public boolean isMorphTargets()
	{
		return morphTargets;
	}

	public Shader getShaderId()
	{
		return new ShaderLambert();
	}
}
