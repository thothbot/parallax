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
import thothbot.squirrel.core.client.textures.Texture;

import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.canvas.dom.client.Context2d.LineJoin;

public class MeshBasicMaterial extends AbstractMapMaterial
{
	public static class MeshBasicMaterialOptions extends AbstractMapMaterial.AbstractMapMaterialOptions 
	{
		public Texture.OPERATIONS combine = Texture.OPERATIONS.MULTIPLY;
		public Material.SHADING shading = Material.SHADING.SMOOTH;
		public LineCap wireframeLinecap = LineCap.ROUND;
		public LineJoin wireframeLinejoin = LineJoin.ROUND;
		public boolean skinning = false;
		public boolean morphTargets = false;
	}

	private LineCap wireframeLinecap;
	private LineJoin wireframeLinejoin;
	private boolean skinning;
	private boolean morphTargets;

	public MeshBasicMaterial(MeshBasicMaterialOptions options)
	{
		super(options);
		this.combine = options.combine;
		setShading(options.shading);
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

	public Shader getShaderId() 
	{
		return new ShaderBasic();
	}
	
	public Material.SHADING bufferGuessNormalType () 
	{
		// only MeshBasicMaterial and MeshDepthMaterial don't need normals
		if (this.envMap == null)
			return null;

		return super.bufferGuessNormalType();
	}
}
