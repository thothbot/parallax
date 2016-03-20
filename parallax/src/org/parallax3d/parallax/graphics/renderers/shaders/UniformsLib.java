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

import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector4;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import java.util.ArrayList;

/**
 * Some common uniforms used by shaders.
 * 
 * @author thothbot
 *
 */
public class UniformsLib
{
	public static FastMap<Uniform> common()
	{
		return new FastMap<Uniform>() {{

			put("diffuse", new Uniform(Uniform.TYPE.C, new Color( 0xeeeeee )));
			put("opacity", new Uniform(Uniform.TYPE.F,  1.0 ));

			put("map", new Uniform(Uniform.TYPE.T ));
			put("offsetRepeat", new Uniform(Uniform.TYPE.V4,  new Vector4( 0, 0, 1, 1 ) ));

			put("specularMap", new Uniform(Uniform.TYPE.T ));
			put("alphaMap", new Uniform(Uniform.TYPE.T ));

			put("envMap", new Uniform(Uniform.TYPE.T ));
			put("flipEnvMap", new Uniform(Uniform.TYPE.F,  -1.0 ));
			put("reflectivity", new Uniform(Uniform.TYPE.F,  1.0 ));
			put("refractionRatio", new Uniform(Uniform.TYPE.F,  0.98 ));
		}};

	}

	public static FastMap<Uniform> aomap()
	{
		return new FastMap<Uniform>() {{

			put("aoMap", new Uniform(Uniform.TYPE.T ));
			put("aoMapIntensity", new Uniform(Uniform.TYPE.F,  1.0 ));

		}};

	}

	public static FastMap<Uniform> lightmap()
	{
		return new FastMap<Uniform>() {{

			put("lightMap", new Uniform(Uniform.TYPE.T ));
			put("lightMapIntensity", new Uniform(Uniform.TYPE.F,  1.0 ));

		}};

	}

	public static FastMap<Uniform> emissivemap()
	{
		return new FastMap<Uniform>() {{

			put("emissiveMap", new Uniform(Uniform.TYPE.T ));

		}};

	}

	public static FastMap<Uniform> bumpmap ()
	{
		return new FastMap<Uniform>() {{

			put("bumpMap", new Uniform(Uniform.TYPE.T ));
			put("bumpScale", new Uniform(Uniform.TYPE.F, 1.0 ));

		}};

	}

	public static FastMap<Uniform> normalmap ()
	{
		return new FastMap<Uniform>() {{

			put("normalMap", new Uniform(Uniform.TYPE.T ));
			put("normalScale", new Uniform(Uniform.TYPE.V2, new Vector2( 1, 1 ) ));

		}};

	}

	public static FastMap<Uniform> displacementmap ()
	{
		return new FastMap<Uniform>() {{

			put("displacementMap", new Uniform(Uniform.TYPE.T ));
			put("displacementScale", new Uniform(Uniform.TYPE.F, 1.0 ));
			put("displacementBias", new Uniform(Uniform.TYPE.F, 0.0 ));

		}};

	}

	public static FastMap<Uniform> roughnessmap ()
	{
		return new FastMap<Uniform>() {{

			put("roughnessMap", new Uniform(Uniform.TYPE.T ));

		}};

	}

	public static FastMap<Uniform> metalnessmap ()
	{
		return new FastMap<Uniform>() {{

			put("metalnessMap", new Uniform(Uniform.TYPE.T ));

		}};

	}

	public static FastMap<Uniform> fog()
	{
		return new FastMap<Uniform>() {{

			put("fogDensity", new Uniform(Uniform.TYPE.F,  0.00025 ));
			put("fogNear", new Uniform(Uniform.TYPE.F,  1.0 ));
			put("fogFar", new Uniform(Uniform.TYPE.F,  2000.0 ));
			put("fogColor", new Uniform(Uniform.TYPE.C, new Color( 0xffffff )));

		}};

	}

	public static FastMap<Uniform> lights()
	{
		return new FastMap<Uniform>() {{

			put("ambientLightColor", new Uniform(Uniform.TYPE.FV,  Float32Array.createArray() ));

			put("directionalLights", new Uniform(Uniform.TYPE.SA,  new ArrayList<>(), new FastMap<Uniform>(){{

				put("direction", new Uniform(Uniform.TYPE.V3 ));
				put("color", new Uniform(Uniform.TYPE.C ));

				put("shadow", new Uniform(Uniform.TYPE.I ));
				put("shadowBias", new Uniform(Uniform.TYPE.F ));
				put("shadowRadius", new Uniform(Uniform.TYPE.F ));
				put("shadowMapSize", new Uniform(Uniform.TYPE.V2 ));

			}} ));

			put("directionalShadowMap", new Uniform(Uniform.TYPE.TV,   new ArrayList<Texture>() ));
			put("directionalShadowMatrix", new Uniform(Uniform.TYPE.M4V, new ArrayList<Matrix4>() ));

			put("spotLights", new Uniform(Uniform.TYPE.SA,  new ArrayList<>(), new FastMap<Uniform>(){{

				put("color", new Uniform(Uniform.TYPE.C ));
				put("position", new Uniform(Uniform.TYPE.V3 ));
				put("direction", new Uniform(Uniform.TYPE.V3 ));
				put("distance", new Uniform(Uniform.TYPE.F ));
				put("coneCos", new Uniform(Uniform.TYPE.F ));
				put("penumbraCos", new Uniform(Uniform.TYPE.F ));
				put("decay", new Uniform(Uniform.TYPE.F ));

				put("shadow", new Uniform(Uniform.TYPE.I ));
				put("shadowBias", new Uniform(Uniform.TYPE.F ));
				put("shadowRadius", new Uniform(Uniform.TYPE.F ));
				put("shadowMapSize", new Uniform(Uniform.TYPE.V2 ));

			}} ));

			put("spotShadowMap", new Uniform(Uniform.TYPE.TV,   new ArrayList<Texture>() ));
			put("spotShadowMatrix", new Uniform(Uniform.TYPE.M4V, new ArrayList<Matrix4>() ));

			put("pointLights", new Uniform(Uniform.TYPE.SA,  new ArrayList<>(), new FastMap<Uniform>(){{

				put("color", new Uniform(Uniform.TYPE.C ));
				put("position", new Uniform(Uniform.TYPE.V3 ));
				put("decay", new Uniform(Uniform.TYPE.F ));
				put("distance", new Uniform(Uniform.TYPE.F ));

				put("shadow", new Uniform(Uniform.TYPE.I ));
				put("shadowBias", new Uniform(Uniform.TYPE.F ));
				put("shadowRadius", new Uniform(Uniform.TYPE.F ));
				put("shadowMapSize", new Uniform(Uniform.TYPE.V2 ));

			}} ));

			put("pointShadowMap", new Uniform(Uniform.TYPE.TV,   new ArrayList<Texture>() ));
			put("pointShadowMatrix", new Uniform(Uniform.TYPE.M4V, new ArrayList<Matrix4>() ));

			put("hemisphereLights", new Uniform(Uniform.TYPE.SA,  new ArrayList<>(), new FastMap<Uniform>(){{

				put("direction", new Uniform(Uniform.TYPE.V3 ));
				put("skyColor", new Uniform(Uniform.TYPE.C ));
				put("groundColor", new Uniform(Uniform.TYPE.C ));

			}} ));

		}};

	}

	public static FastMap<Uniform> points()
	{
		return new FastMap<Uniform>() {{

			put("diffuse", new Uniform(Uniform.TYPE.C, new Color( 0xeeeeee )));
			put("opacity", new Uniform(Uniform.TYPE.F,  1.0 ));
			put("size",    new Uniform(Uniform.TYPE.F,  1.0 ));
			put("scale",   new Uniform(Uniform.TYPE.F,  1.0 ));
			put("map",     new Uniform(Uniform.TYPE.T ));
			put("offsetRepeat", new Uniform(Uniform.TYPE.V4, new Vector4( 0, 0, 1, 1 ) ));

		}};

	}
}
