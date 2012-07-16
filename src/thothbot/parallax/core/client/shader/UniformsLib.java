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

package thothbot.parallax.core.client.shader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.shared.core.Color3f;
import thothbot.parallax.core.shared.core.Matrix4f;
import thothbot.parallax.core.shared.core.Vector2f;
import thothbot.parallax.core.shared.core.Vector4f;

/**
 * Some common uniforms used by shaders.
 * 
 * @author thothbot
 *
 */
public class UniformsLib
{
	public static final Map<String, Uniform> common = new HashMap<String, Uniform>() {
		{
			put("diffuse", new Uniform(Uniform.TYPE.C, new Color3f( 0xeeeeee )));
			put("opacity", new Uniform(Uniform.TYPE.F,  1.0f ));

			put("map", new Uniform(Uniform.TYPE.T,  0 ));
			put("offsetRepeat", new Uniform(Uniform.TYPE.V4,  new Vector4f( 0, 0, 1, 1 ) ));
			
			put("lightMap", new Uniform(Uniform.TYPE.T,  2 ));
			
			put("envMap", new Uniform(Uniform.TYPE.T,  1 ));
			put("flipEnvMap", new Uniform(Uniform.TYPE.F,  -1.0f ));
			put("useRefract", new Uniform(Uniform.TYPE.I,  1 ));
			put("reflectivity", new Uniform(Uniform.TYPE.F,  1.0f ));
			put("refractionRatio", new Uniform(Uniform.TYPE.F,  0.98f ));
			put("combine", new Uniform(Uniform.TYPE.I,  0 ));
			
			put("morphTargetInfluences", new Uniform(Uniform.TYPE.F,  0.0f ));
		}
	};
	
	public static final Map<String, Uniform> fog = new HashMap<String, Uniform>() {
		{
			put("fogDensity", new Uniform(Uniform.TYPE.F,  0.00025f ));
			put("fogNear", new Uniform(Uniform.TYPE.F,  1.0f ));
			put("fogFar", new Uniform(Uniform.TYPE.F,  2000.0f ));
			put("fogColor", new Uniform(Uniform.TYPE.C, new Color3f( 0xffffff )));
		}
	};
	
	public static final Map<String, Uniform> lights = new HashMap<String, Uniform>() {
		{
			put("ambientLightColor", new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
			
			put("directionalLightDirection", new Uniform(Uniform.TYPE.FV, Float32Array.createArray() ));
			put("directionalLightColor",     new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
			
			put("pointLightColor",    new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
			put("pointLightPosition", new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
			
			put("pointLightDistance", new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));
			
			put("spotLightColor",     new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
			put("spotLightPosition",  new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
			put("spotLightDirection", new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
			put("spotLightDistance",  new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));
			put("spotLightAngle",     new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));
			put("spotLightExponent",  new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));
		}
	};
	
	public static final Map<String, Uniform> particle = new HashMap<String, Uniform>() {
		{
			put("psColor", new Uniform(Uniform.TYPE.C, new Color3f( 0xeeeeee )));
			put("opacity", new Uniform(Uniform.TYPE.F,  1.0f ));
			put("size",    new Uniform(Uniform.TYPE.F,  1.0f ));
			put("scale",   new Uniform(Uniform.TYPE.F,  1.0f ));
			put("map",     new Uniform(Uniform.TYPE.T,  0 ));
			
			put("fogDensity", new Uniform(Uniform.TYPE.F,  0.00025f ));
			put("fogNear",    new Uniform(Uniform.TYPE.F,  1.0f ));
			put("fogFar",     new Uniform(Uniform.TYPE.F,  2000.0f ));
			put("fogColor",   new Uniform(Uniform.TYPE.C, new Color3f( 0xffffff )));
		}
	};
	
	public static final Map<String, Uniform> shadowmap = new HashMap<String, Uniform>() {
		{
			put("shadowMap",      new Uniform(Uniform.TYPE.TV, 6 ));
			put("shadowMapSize",  new Uniform(Uniform.TYPE.V2V, new ArrayList<Vector2f>() ));
			put("shadowBias",     new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));
			put("shadowDarkness", new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));
			
			put("shadowDarkness", new Uniform(Uniform.TYPE.M4V,  new ArrayList<Matrix4f>() ));
		}
	};	
}
