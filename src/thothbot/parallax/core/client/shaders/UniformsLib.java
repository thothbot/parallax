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

package thothbot.parallax.core.client.shaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.shared.core.Color;
import thothbot.parallax.core.shared.core.Matrix4;
import thothbot.parallax.core.shared.core.Vector2;
import thothbot.parallax.core.shared.core.Vector4;

/**
 * Some common uniforms used by shaders.
 * 
 * @author thothbot
 *
 */
public class UniformsLib
{
	public static Map<String, Uniform> getCommon ()
	{
		Map<String, Uniform> retval = new HashMap<String, Uniform>();
		
		retval.put("diffuse", new Uniform(Uniform.TYPE.C, new Color( 0xeeeeee )));
		retval.put("opacity", new Uniform(Uniform.TYPE.F,  1.0 ));

		retval.put("map", new Uniform(Uniform.TYPE.T,  0 ));
		retval.put("offsetRepeat", new Uniform(Uniform.TYPE.V4,  new Vector4( 0, 0, 1, 1 ) ));
			
		retval.put("lightMap", new Uniform(Uniform.TYPE.T,  2 ));
		retval.put("specularMap", new Uniform(Uniform.TYPE.T,  3 ));
			
		retval.put("envMap", new Uniform(Uniform.TYPE.T,  1 ));
		retval.put("flipEnvMap", new Uniform(Uniform.TYPE.F,  -1.0 ));
		retval.put("useRefract", new Uniform(Uniform.TYPE.I,  1 ));
		retval.put("reflectivity", new Uniform(Uniform.TYPE.F,  1.0 ));
		retval.put("refractionRatio", new Uniform(Uniform.TYPE.F,  0.98 ));
		retval.put("combine", new Uniform(Uniform.TYPE.I,  0 ));
			
		retval.put("morphTargetInfluences", new Uniform(Uniform.TYPE.F,  0.0 ));
		
		return retval;
	}
	
	public static Map<String, Uniform> getBump ()
	{
		Map<String, Uniform> retval = new HashMap<String, Uniform>();
		
		retval.put("bumpMap", new Uniform(Uniform.TYPE.T,  4 ));
		retval.put("bumpScale", new Uniform(Uniform.TYPE.F, 1.0 ));
		
		return retval;
	}
	
	public static Map<String, Uniform> getFog()
	{
		Map<String, Uniform> retval = new HashMap<String, Uniform>();
		
		retval.put("fogDensity", new Uniform(Uniform.TYPE.F,  0.00025 ));
		retval.put("fogNear", new Uniform(Uniform.TYPE.F,  1.0 ));
		retval.put("fogFar", new Uniform(Uniform.TYPE.F,  2000.0 ));
		retval.put("fogColor", new Uniform(Uniform.TYPE.C, new Color( 0xffffff )));

		return retval;
	}
	
	public static Map<String, Uniform> getLights()
	{
		Map<String, Uniform> retval = new HashMap<String, Uniform>();

		retval.put("ambientLightColor", new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));

		retval.put("directionalLightDirection", new Uniform(Uniform.TYPE.FV, Float32Array.createArray() ));
		retval.put("directionalLightColor",     new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));

		retval.put("pointLightColor",    new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
		retval.put("pointLightPosition", new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));

		retval.put("pointLightDistance", new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));

		retval.put("spotLightColor",     new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
		retval.put("spotLightPosition",  new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
		retval.put("spotLightDirection", new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
		retval.put("spotLightDistance",  new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));
		retval.put("spotLightAngle",     new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));
		retval.put("spotLightExponent",  new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));

		return retval;
	}
	
	public static Map<String, Uniform> getParticle() 
	{
		Map<String, Uniform> retval = new HashMap<String, Uniform>();

		retval.put("psColor", new Uniform(Uniform.TYPE.C, new Color( 0xeeeeee )));
		retval.put("opacity", new Uniform(Uniform.TYPE.F,  1.0 ));
		retval.put("size",    new Uniform(Uniform.TYPE.F,  1.0 ));
		retval.put("scale",   new Uniform(Uniform.TYPE.F,  1.0 ));
		retval.put("map",     new Uniform(Uniform.TYPE.T,  0 ));
			
		retval.put("fogDensity", new Uniform(Uniform.TYPE.F,  0.00025 ));
		retval.put("fogNear",    new Uniform(Uniform.TYPE.F,  1.0 ));
		retval.put("fogFar",     new Uniform(Uniform.TYPE.F,  2000.0 ));
		retval.put("fogColor",   new Uniform(Uniform.TYPE.C, new Color( 0xffffff )));

		return retval;
	}
	
	public static Map<String, Uniform> getShadowmap() 
	{
		
		Map<String, Uniform> retval = new HashMap<String, Uniform>();	

		retval.put("shadowMap",      new Uniform(Uniform.TYPE.TV, 6 ));
		retval.put("shadowMapSize",  new Uniform(Uniform.TYPE.V2V, new ArrayList<Vector2>() ));
		retval.put("shadowBias",     new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));
		retval.put("shadowDarkness", new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));
			
		retval.put("shadowMatrix", new Uniform(Uniform.TYPE.M4V,  new ArrayList<Matrix4>() ));
		
		return retval;
	}	
}
