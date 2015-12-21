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

package org.parallax3d.parallax.graphics.renderers.shaders;

import java.util.ArrayList;

import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector4;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.BufferUtils;
import org.parallax3d.parallax.system.ObjectMap;
import org.parallax3d.parallax.system.gl.enums.BufferUsage;

/**
 * Some common uniforms used by shaders.
 * 
 * @author thothbot
 *
 */
public class UniformsLib
{
	public static ObjectMap<String, Uniform>  getCommon ()
	{
		ObjectMap<String, Uniform>  retval = new ObjectMap<String, Uniform> ();
				
		retval.put("diffuse", new Uniform(Uniform.TYPE.C, new Color( 0xeeeeee )));
		retval.put("opacity", new Uniform(Uniform.TYPE.F,  1.0f ));

		retval.put("map", new Uniform(Uniform.TYPE.T ));
		retval.put("offsetRepeat", new Uniform(Uniform.TYPE.V4,  new Vector4( 0, 0, 1, 1 ) ));
			
		retval.put("lightMap", new Uniform(Uniform.TYPE.T ));
		retval.put("specularMap", new Uniform(Uniform.TYPE.T ));
		retval.put("alphaMap", new Uniform(Uniform.TYPE.T ));
			
		retval.put("envMap", new Uniform(Uniform.TYPE.T ));
		retval.put("flipEnvMap", new Uniform(Uniform.TYPE.F,  -1.0f ));
		retval.put("useRefract", new Uniform(Uniform.TYPE.I,  0 ));
		retval.put("reflectivity", new Uniform(Uniform.TYPE.F,  1.0f ));
		retval.put("refractionRatio", new Uniform(Uniform.TYPE.F,  0.98f ));
		retval.put("combine", new Uniform(Uniform.TYPE.I,  0 ));
			
		retval.put("morphTargetInfluences", new Uniform(Uniform.TYPE.F,  0.0f ));
		
		return retval;
	}
	
	public static ObjectMap<String, Uniform> getBump ()
	{
		ObjectMap<String, Uniform> retval = new ObjectMap<String, Uniform>();
		
		retval.put("bumpMap", new Uniform(Uniform.TYPE.T ));
		retval.put("bumpScale", new Uniform(Uniform.TYPE.F, 1.0f ));
		
		return retval;
	}
	
	public static ObjectMap<String, Uniform> getNormalMap ()
	{
		ObjectMap<String, Uniform> retval = new ObjectMap<String, Uniform>();
		
		retval.put("normalMap", new Uniform(Uniform.TYPE.T ));
		retval.put("normalScale", new Uniform(Uniform.TYPE.V2, new Vector2( 1, 1 ) ));
		
		return retval;
	}
	
	public static ObjectMap<String, Uniform> getFog()
	{
		ObjectMap<String, Uniform> retval = new ObjectMap<String, Uniform>();
		
		retval.put("fogDensity", new Uniform(Uniform.TYPE.F,  0.00025f ));
		retval.put("fogNear", new Uniform(Uniform.TYPE.F,  1.0f ));
		retval.put("fogFar", new Uniform(Uniform.TYPE.F,  2000.0f ));
		retval.put("fogColor", new Uniform(Uniform.TYPE.C, new Color( 0xffffff )));

		return retval;
	}
	
	public static ObjectMap<String, Uniform> getLights()
	{
		ObjectMap<String, Uniform> retval = new ObjectMap<String, Uniform>();

		retval.put("ambientLightColor", new Uniform(Uniform.TYPE.FV, BufferUtils.newFloatBuffer(16) ));

		retval.put("directionalLightDirection", new Uniform(Uniform.TYPE.FV, BufferUtils.newFloatBuffer(16) ));
		retval.put("directionalLightColor",     new Uniform(Uniform.TYPE.FV,  BufferUtils.newFloatBuffer(16) ));

		retval.put("hemisphereLightPosition",    new Uniform(Uniform.TYPE.FV,  BufferUtils.newFloatBuffer(16) ));
		retval.put("hemisphereLightSkyColor",    new Uniform(Uniform.TYPE.FV,  BufferUtils.newFloatBuffer(16) ));
		retval.put("hemisphereLightGroundColor", new Uniform(Uniform.TYPE.FV,  BufferUtils.newFloatBuffer(16) ));
		
		retval.put("pointLightColor",    new Uniform(Uniform.TYPE.FV,  BufferUtils.newFloatBuffer(16) ));
		retval.put("pointLightPosition", new Uniform(Uniform.TYPE.FV,  BufferUtils.newFloatBuffer(16) ));
		retval.put("pointLightDistance", new Uniform(Uniform.TYPE.FV1,  BufferUtils.newFloatBuffer(16) ));

		retval.put("spotLightColor",     new Uniform(Uniform.TYPE.FV,  BufferUtils.newFloatBuffer(16) ));
		retval.put("spotLightPosition",  new Uniform(Uniform.TYPE.FV,  BufferUtils.newFloatBuffer(16) ));
		retval.put("spotLightDirection", new Uniform(Uniform.TYPE.FV,  BufferUtils.newFloatBuffer(16) ));
		retval.put("spotLightDistance",  new Uniform(Uniform.TYPE.FV1,  BufferUtils.newFloatBuffer(16) ));
		retval.put("spotLightAngleCos",  new Uniform(Uniform.TYPE.FV1,  BufferUtils.newFloatBuffer(16) ));
		retval.put("spotLightExponent",  new Uniform(Uniform.TYPE.FV1,  BufferUtils.newFloatBuffer(16) ));
		
		return retval;
	}
	
	public static ObjectMap<String, Uniform> getParticle() 
	{
		ObjectMap<String, Uniform> retval = new ObjectMap<String, Uniform>();

		retval.put("psColor", new Uniform(Uniform.TYPE.C, new Color( 0xeeeeee )));
		retval.put("opacity", new Uniform(Uniform.TYPE.F,  1.0f ));
		retval.put("size",    new Uniform(Uniform.TYPE.F,  1.0f ));
		retval.put("scale",   new Uniform(Uniform.TYPE.F,  1.0f ));
		retval.put("map",     new Uniform(Uniform.TYPE.T ));
			
		retval.put("fogDensity", new Uniform(Uniform.TYPE.F,  0.00025f ));
		retval.put("fogNear",    new Uniform(Uniform.TYPE.F,  1.0f ));
		retval.put("fogFar",     new Uniform(Uniform.TYPE.F,  2000.0f ));
		retval.put("fogColor",   new Uniform(Uniform.TYPE.C, new Color( 0xffffff )));

		return retval;
	}
	
	public static ObjectMap<String, Uniform> getShadowmap() 
	{
		
		ObjectMap<String, Uniform> retval = new ObjectMap<String, Uniform>();	

		retval.put("shadowMap",      new Uniform(Uniform.TYPE.TV, new ArrayList<Texture>() ));
		retval.put("shadowMapSize",  new Uniform(Uniform.TYPE.V2V, new ArrayList<Vector2>() ));
		retval.put("shadowBias",     new Uniform(Uniform.TYPE.FV1,  BufferUtils.newFloatBuffer(16) ));
		retval.put("shadowDarkness", new Uniform(Uniform.TYPE.FV1,  BufferUtils.newFloatBuffer(16) ));
			
		retval.put("shadowMatrix", new Uniform(Uniform.TYPE.M4V,  new ArrayList<Matrix4>() ));
		
		return retval;
	}	
}
