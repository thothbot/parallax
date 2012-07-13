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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.squirrel.core.client.shader.Uniform;
import thothbot.squirrel.core.shared.core.WebGLCustomAttribute;
import thothbot.squirrel.core.shared.lights.Light;


public class ShaderMaterial extends Material
{
	public static class ShaderMaterialOptions extends MaterialOptions
	{
		public String fragmentShader = "void main() {}";
		public String vertexShader = "void main() {}";
		public Map<String, Uniform> uniforms = new HashMap<String, Uniform>();
		public Map<String, WebGLCustomAttribute> attributes = new HashMap<String, WebGLCustomAttribute>();
		public Material.SHADING shading = Material.SHADING.SMOOTH;
		public List<Light> lights;
		
		public boolean skinning = false;

		public boolean morphTargets = false;
		public boolean morphNormals = false;
	}
	
	public ShaderMaterial(ShaderMaterialOptions parameters) 
	{
		super(parameters);
		this.fragmentShader = parameters.fragmentShader;
		this.vertexShader = parameters.vertexShader;
		
		this.uniforms = parameters.uniforms;
		this.attributes = parameters.attributes;
		
		setShading( parameters.shading );
		
		this.lights = parameters.lights;
		
		this.skinning = parameters.skinning;
		
		this.morphTargets = parameters.morphTargets;
		this.morphNormals = parameters.morphNormals;
	}
	
	public boolean bufferGuessUVType () 
	{
		return true;
	}
}
