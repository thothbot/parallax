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
import thothbot.squirrel.core.client.shader.ShaderPhong;
import thothbot.squirrel.core.shared.core.Color3f;
import thothbot.squirrel.core.shared.core.Vector3f;
import thothbot.squirrel.core.shared.textures.Texture;

import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.canvas.dom.client.Context2d.LineJoin;

public final class MeshPhongMaterial extends AbstractMapMaterial 
{
	static class MeshPhongMaterialOptions extends AbstractMapMaterial.AbstractMapMaterialOptions 
	{
		public Color3f ambient = new Color3f(0x050505);
		public Color3f specular = new Color3f(0x111111);
		public int shininess = 30;
		public Texture.OPERATIONS combine = Texture.OPERATIONS.MULTIPLY;
		public Material.SHADING shading = Material.SHADING.SMOOTH;
		public LineCap wireframeLinecap = LineCap.ROUND;
		public LineJoin wireframeLinejoin = LineJoin.ROUND;
		public boolean skinning = false;
		public boolean morphTargets = false;
	}
	
	private Color3f ambient;
	private Color3f emissive;
	private Vector3f wrapRGB;
	private Color3f specular;
	private int shininess;
	private LineCap wireframeLinecap;
	private LineJoin wireframeLinejoin;
	private boolean skinning;
	private boolean morphTargets;

	public MeshPhongMaterial(MeshPhongMaterialOptions options){
		super(options);
		this.color = options.color;
		this.ambient = options.ambient;
		this.specular = options.specular;
		this.shininess = options.shininess;
		this.map = options.map;
		this.lightMap = options.lightMap;
		this.envMap = options.envMap;
		this.combine = options.combine;
		this.reflectivity = options.reflectivity;
		this.refractionRatio = options.refractionRatio;
		this.shading = options.shading;
		this.wireframeLinecap = options.wireframeLinecap;
		this.wireframeLinejoin = options.wireframeLinejoin;
		this.skinning = options.skinning;
		this.morphTargets = options.morphTargets;
	}

	public Boolean getSkinning(){
		return this.skinning;
	}

	public boolean isMorphTargets() {
		return morphTargets;
	}

	public LineCap getWireframeLinecap() {
		return wireframeLinecap;
	}

	public LineJoin getWireframeLinejoin() {
		return wireframeLinejoin;
	}

	public Color3f getAmbient() {
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

	public Color3f getSpecular() {
		return specular;
	}

	public int getShininess() {
		return shininess;
	}

	public Shader getShaderId()
	{
		return new ShaderPhong();
	}
}
