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

import org.parallax3d.parallax.App;

/**
 * Source for all chunks.
 * 
 * @author thothbot
 *
 */
public class Chunks
{
	public static String getAlphamapFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/alphamap_fragment.glsl").readString();
	}
	
	public static String getAlphamapParsFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/alphamap_pars_fragment.glsl").readString();
	}

	public static String getAlphatestFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/alphatest_fragment.glsl").readString();
	}
	
	public static String getBumpmapParsFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/bumpmap_pars_fragment.glsl").readString();
	}

	public static String getColorFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/color_fragment.glsl").readString();
	}

	public static String getColorParsFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/color_pars_fragment.glsl").readString();
	}

	public static String getColorParsVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/color_pars_vertex.glsl").readString();
	}

	public static String getColorVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/color_vertex.glsl").readString();
	}

	public static String getDefaultNormalVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/defaultnormal_vertex.glsl").readString();
	}
		
	public static String getDefaultVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/default_vertex.glsl").readString();
	}

	public static String getEnvmapFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/envmap_fragment.glsl").readString();
	}

	public static String getEnvmapParsFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/envmap_pars_fragment.glsl").readString();
	}

	public static String getEnvmapParsVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/envmap_pars_vertex.glsl").readString();
	}

	public static String getEnvmapVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/envmap_vertex.glsl").readString();
	}

	public static String getFogFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/fog_fragment.glsl").readString();
	}

	public static String getFogParsFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/fog_pars_fragment.glsl").readString();
	}

	public static String getLightmapFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lightmap_fragment.glsl").readString();
	}

	public static String getLightmapParsFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lightmap_pars_fragment.glsl").readString();
	}

	public static String getLightmapParsVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lightmap_pars_vertex.glsl").readString();
	}

	public static String getLightmapVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lightmap_vertex.glsl").readString();
	}

	public static String getLightsLambertParsVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lights_lambert_pars_vertex.glsl").readString();
	}

	public static String getLightsLambertVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lights_lambert_vertex.glsl").readString();
	}

	public static String getLightsPhongFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lights_phong_fragment.glsl").readString();
	}

	public static String getLightsPhongParsFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lights_phong_pars_fragment.glsl").readString();
	}

	public static String getLightsPhongParsVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lights_phong_pars_vertex.glsl").readString();
	}

	public static String getLightsPhongVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/lights_phong_vertex.glsl").readString();
	}

	public static String getLinearToGammaFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/linear_to_gamma_fragment.glsl").readString();
	}
	
	public static String getLogdepthbufFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/logdepthbuf_fragment.glsl").readString();
	}
	
	public static String getLogdepthbufParFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/logdepthbuf_par_fragment.glsl").readString();
	}
	
	public static String getLogdepthbufParVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/logdepthbuf_par_vertex.glsl").readString();
	}
	
	public static String getLogdepthbufVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/logdepthbuf_vertex.glsl").readString();
	}

	public static String getMapFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/map_fragment.glsl").readString();
	}

	public static String getMapParsFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/map_pars_fragment.glsl").readString();
	}

	public static String getMapParsVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/map_pars_vertex.glsl").readString();
	}

	public static String getMapParticleFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/map_particle_fragment.glsl").readString();
	}

	public static String getMapParticleParsFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/map_particle_pars_fragment.glsl").readString();
	}

	public static String getMapVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/map_vertex.glsl").readString();
	}

	public static String getMorphnormalVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/morphnormal_vertex.glsl").readString();
	}

	public static String getMorphtargetParsVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/morphtarget_pars_vertex.glsl").readString();
	}

	public static String getMorphtargetVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/morphtarget_vertex.glsl").readString();
	}
	
	public static String getNormalmapParsFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/normalmap_pars_fragment.glsl").readString();
	}

	public static String getShadowmapFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/shadowmap_fragment.glsl").readString();
	}

	public static String getShadowmapParsFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/shadowmap_pars_fragment.glsl").readString();
	}

	public static String getShadowmapParsVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/shadowmap_pars_vertex.glsl").readString();
	}

	public static String getShadowmapVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/shadowmap_vertex.glsl").readString();
	}

	public static String getSkinningParsVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/skinning_pars_vertex.glsl").readString();
	}

	public static String getSkinningVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/skinning_vertex.glsl").readString();
	}
	
	public static String getSkinBaseVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/skinbase_vertex.glsl").readString();
	}
	
	public static String getSkinNormalVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/skinnormal_vertex.glsl").readString();
	}
	
	public static String getSpecularmapFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/specularmap_fragment.glsl").readString();
	}
	
	public static String getSpecularmapParsFragment(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/specularmap_pars_fragment.glsl").readString();
	}
	
	public static String getWorldposVertex(){
		return App.files.classpath("org/parallax3d/parallax/graphics/renderers/shaders/chunk/worldpos_vertex.glsl").readString();
	}
}
