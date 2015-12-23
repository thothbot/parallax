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

import org.parallax3d.parallax.Parallax;

/**
 * Source for all chunks.
 * 
 * @author thothbot
 *
 */
public class Chunks
{
	public static String getAlphamapFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/alphamap_fragment.glsl").readString();
	}
	
	public static String getAlphamapParsFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/alphamap_pars_fragment.glsl").readString();
	}

	public static String getAlphatestFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/alphatest_fragment.glsl").readString();
	}
	
	public static String getBumpmapParsFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/bumpmap_pars_fragment.glsl").readString();
	}

	public static String getColorFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/color_fragment.glsl").readString();
	}

	public static String getColorParsFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/color_pars_fragment.glsl").readString();
	}

	public static String getColorParsVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/color_pars_vertex.glsl").readString();
	}

	public static String getColorVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/color_vertex.glsl").readString();
	}

	public static String getDefaultNormalVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/defaultnormal_vertex.glsl").readString();
	}
		
	public static String getDefaultVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/default_vertex.glsl").readString();
	}

	public static String getEnvmapFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/envmap_fragment.glsl").readString();
	}

	public static String getEnvmapParsFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/envmap_pars_fragment.glsl").readString();
	}

	public static String getEnvmapParsVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/envmap_pars_vertex.glsl").readString();
	}

	public static String getEnvmapVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/envmap_vertex.glsl").readString();
	}

	public static String getFogFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/fog_fragment.glsl").readString();
	}

	public static String getFogParsFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/fog_pars_fragment.glsl").readString();
	}

	public static String getLightmapFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lightmap_fragment.glsl").readString();
	}

	public static String getLightmapParsFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lightmap_pars_fragment.glsl").readString();
	}

	public static String getLightmapParsVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lightmap_pars_vertex.glsl").readString();
	}

	public static String getLightmapVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lightmap_vertex.glsl").readString();
	}

	public static String getLightsLambertParsVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lights_lambert_pars_vertex.glsl").readString();
	}

	public static String getLightsLambertVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lights_lambert_vertex.glsl").readString();
	}

	public static String getLightsPhongFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lights_phong_fragment.glsl").readString();
	}

	public static String getLightsPhongParsFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lights_phong_pars_fragment.glsl").readString();
	}

	public static String getLightsPhongParsVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lights_phong_pars_vertex.glsl").readString();
	}

	public static String getLightsPhongVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lights_phong_vertex.glsl").readString();
	}

	public static String getLinearToGammaFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/linear_to_gamma_fragment.glsl").readString();
	}
	
	public static String getLogdepthbufFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/logdepthbuf_fragment.glsl").readString();
	}
	
	public static String getLogdepthbufParFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/logdepthbuf_par_fragment.glsl").readString();
	}
	
	public static String getLogdepthbufParVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/logdepthbuf_par_vertex.glsl").readString();
	}
	
	public static String getLogdepthbufVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/logdepthbuf_vertex.glsl").readString();
	}

	public static String getMapFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/map_fragment.glsl").readString();
	}

	public static String getMapParsFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/map_pars_fragment.glsl").readString();
	}

	public static String getMapParsVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/map_pars_vertex.glsl").readString();
	}

	public static String getMapParticleFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/map_particle_fragment.glsl").readString();
	}

	public static String getMapParticleParsFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/map_particle_pars_fragment.glsl").readString();
	}

	public static String getMapVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/map_vertex.glsl").readString();
	}

	public static String getMorphnormalVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/morphnormal_vertex.glsl").readString();
	}

	public static String getMorphtargetParsVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/morphtarget_pars_vertex.glsl").readString();
	}

	public static String getMorphtargetVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/morphtarget_vertex.glsl").readString();
	}
	
	public static String getNormalmapParsFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/normalmap_pars_fragment.glsl").readString();
	}

	public static String getShadowmapFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/shadowmap_fragment.glsl").readString();
	}

	public static String getShadowmapParsFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/shadowmap_pars_fragment.glsl").readString();
	}

	public static String getShadowmapParsVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/shadowmap_pars_vertex.glsl").readString();
	}

	public static String getShadowmapVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/shadowmap_vertex.glsl").readString();
	}

	public static String getSkinningParsVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/skinning_pars_vertex.glsl").readString();
	}

	public static String getSkinningVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/skinning_vertex.glsl").readString();
	}
	
	public static String getSkinBaseVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/skinbase_vertex.glsl").readString();
	}
	
	public static String getSkinNormalVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/skinnormal_vertex.glsl").readString();
	}
	
	public static String getSpecularmapFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/specularmap_fragment.glsl").readString();
	}
	
	public static String getSpecularmapParsFragment(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/specularmap_pars_fragment.glsl").readString();
	}
	
	public static String getWorldposVertex(){
		return Parallax.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/worldpos_vertex.glsl").readString();
	}
}
