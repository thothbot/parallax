/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution 
 * 3.0 Unported License. for more details.
 * 
 * You should have received a copy of the the Creative Commons Attribution 
 * 3.0 Unported License along with Parallax. 
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */

package thothbot.parallax.core.client.shaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector4;

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

		retval.put("map", new Uniform(Uniform.TYPE.T ));
		retval.put("offsetRepeat", new Uniform(Uniform.TYPE.V4,  new Vector4( 0, 0, 1, 1 ) ));
			
		retval.put("lightMap", new Uniform(Uniform.TYPE.T ));
		retval.put("specularMap", new Uniform(Uniform.TYPE.T ));
		retval.put("alphaMap", new Uniform(Uniform.TYPE.T ));
			
		retval.put("envMap", new Uniform(Uniform.TYPE.T ));
		retval.put("flipEnvMap", new Uniform(Uniform.TYPE.F,  -1.0 ));
		retval.put("useRefract", new Uniform(Uniform.TYPE.I,  0 ));
		retval.put("reflectivity", new Uniform(Uniform.TYPE.F,  1.0 ));
		retval.put("refractionRatio", new Uniform(Uniform.TYPE.F,  0.98 ));
		retval.put("combine", new Uniform(Uniform.TYPE.I,  0 ));
			
		retval.put("morphTargetInfluences", new Uniform(Uniform.TYPE.F,  0.0 ));
		
		return retval;
	}
	
	public static Map<String, Uniform> getBump ()
	{
		Map<String, Uniform> retval = new HashMap<String, Uniform>();
		
		retval.put("bumpMap", new Uniform(Uniform.TYPE.T ));
		retval.put("bumpScale", new Uniform(Uniform.TYPE.F, 1.0 ));
		
		return retval;
	}
	
	public static Map<String, Uniform> getNormalMap ()
	{
		Map<String, Uniform> retval = new HashMap<String, Uniform>();
		
		retval.put("normalMap", new Uniform(Uniform.TYPE.T ));
		retval.put("normalScale", new Uniform(Uniform.TYPE.V2, new Vector2( 1, 1 ) ));
		
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

		retval.put("hemisphereLightPosition",    new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
		retval.put("hemisphereLightSkyColor",    new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
		retval.put("hemisphereLightGroundColor", new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
		
		retval.put("pointLightColor",    new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
		retval.put("pointLightPosition", new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
		retval.put("pointLightDistance", new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));

		retval.put("spotLightColor",     new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
		retval.put("spotLightPosition",  new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
		retval.put("spotLightDirection", new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));
		retval.put("spotLightDistance",  new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));
		retval.put("spotLightAngleCos",  new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));
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
		retval.put("map",     new Uniform(Uniform.TYPE.T ));
			
		retval.put("fogDensity", new Uniform(Uniform.TYPE.F,  0.00025 ));
		retval.put("fogNear",    new Uniform(Uniform.TYPE.F,  1.0 ));
		retval.put("fogFar",     new Uniform(Uniform.TYPE.F,  2000.0 ));
		retval.put("fogColor",   new Uniform(Uniform.TYPE.C, new Color( 0xffffff )));

		return retval;
	}
	
	public static Map<String, Uniform> getShadowmap() 
	{
		
		Map<String, Uniform> retval = new HashMap<String, Uniform>();	

		retval.put("shadowMap",      new Uniform(Uniform.TYPE.TV, new ArrayList<Texture>() ));
		retval.put("shadowMapSize",  new Uniform(Uniform.TYPE.V2V, new ArrayList<Vector2>() ));
		retval.put("shadowBias",     new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));
		retval.put("shadowDarkness", new Uniform(Uniform.TYPE.FV1,  Float32Array.createArray() ));
			
		retval.put("shadowMatrix", new Uniform(Uniform.TYPE.M4V,  new ArrayList<Matrix4>() ));
		
		return retval;
	}	
}
