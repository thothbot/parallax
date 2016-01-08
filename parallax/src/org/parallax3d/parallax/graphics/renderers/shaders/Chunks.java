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
	public static Chunks INSTANCE = new Chunks();

	public TextResource getAlphamapFragment() {
		return TextResource.create( "chunk/alphamap_fragment.glsl" );
	};
	
	public TextResource getAlphamapParsFragment() {
		return TextResource.create( "chunk/alphamap_pars_fragment.glsl" );
	};

	public TextResource getAlphatestFragment() {
		return TextResource.create( "chunk/alphatest_fragment.glsl" );
	};
	
	public TextResource getBumpmapParsFragment() {
		return TextResource.create( "chunk/bumpmap_pars_fragment.glsl" );
	};

	public TextResource getColorFragment() {
		return TextResource.create( "chunk/color_fragment.glsl" );
	};

	public TextResource getColorParsFragment() {
		return TextResource.create( "chunk/color_pars_fragment.glsl" );
	};

	public TextResource getColorParsVertex() {
		return TextResource.create( "chunk/color_pars_vertex.glsl" );
	};

	public TextResource getColorVertex() {
		return TextResource.create( "chunk/color_vertex.glsl" );
	};

	public TextResource getDefaultNormalVertex() {
		return TextResource.create( "chunk/defaultnormal_vertex.glsl" );
	};
		
	public TextResource getDefaultVertex() {
		return TextResource.create( "chunk/default_vertex.glsl" );
	};

	public TextResource getEnvmapFragment() {
		return TextResource.create( "chunk/envmap_fragment.glsl" );
	};

	public TextResource getEnvmapParsFragment() {
		return TextResource.create( "chunk/envmap_pars_fragment.glsl" );
	};

	public TextResource getEnvmapParsVertex() {
		return TextResource.create( "chunk/envmap_pars_vertex.glsl" );
	};

	public TextResource getEnvmapVertex() {
		return TextResource.create( "chunk/envmap_vertex.glsl" );
	};

	public TextResource getFogFragment() {
		return TextResource.create( "chunk/fog_fragment.glsl" );
	};

	public TextResource getFogParsFragment() {
		return TextResource.create( "chunk/fog_pars_fragment.glsl" );
	};

	public TextResource getLightmapFragment() {
		return TextResource.create( "chunk/lightmap_fragment.glsl" );
	};

	public TextResource getLightmapParsFragment() {
		return TextResource.create( "chunk/lightmap_pars_fragment.glsl" );
	};

	public TextResource getLightmapParsVertex() {
		return TextResource.create( "chunk/lightmap_pars_vertex.glsl" );
	};

	public TextResource getLightmapVertex() {
		return TextResource.create( "chunk/lightmap_vertex.glsl" );
	};

	public TextResource getLightsLambertParsVertex() {
		return TextResource.create( "chunk/lights_lambert_pars_vertex.glsl" );
	};

	public TextResource getLightsLambertVertex() {
		return TextResource.create( "chunk/lights_lambert_vertex.glsl" );
	};

	public TextResource getLightsPhongFragment() {
		return TextResource.create( "chunk/lights_phong_fragment.glsl" );
	};

	public TextResource getLightsPhongParsFragment() {
		return TextResource.create( "chunk/lights_phong_pars_fragment.glsl" );
	};

	public TextResource getLightsPhongParsVertex() {
		return TextResource.create( "chunk/lights_phong_pars_vertex.glsl" );
	};

	public TextResource getLightsPhongVertex() {
		return TextResource.create( "chunk/lights_phong_vertex.glsl" );
	};

	public TextResource getLinearToGammaFragment() {
		return TextResource.create( "chunk/linear_to_gamma_fragment.glsl" );
	};
	
	public TextResource getLogdepthbufFragment() {
		return TextResource.create( "chunk/logdepthbuf_fragment.glsl" );
	};
	
	public TextResource getLogdepthbufParFragment() {
		return TextResource.create( "chunk/logdepthbuf_par_fragment.glsl" );
	};
	
	public TextResource getLogdepthbufParVertex() {
		return TextResource.create( "chunk/logdepthbuf_par_vertex.glsl" );
	};
	
	public TextResource getLogdepthbufVertex() {
		return TextResource.create( "chunk/logdepthbuf_vertex.glsl" );
	};

	public TextResource getMapFragment() {
		return TextResource.create( "chunk/map_fragment.glsl" );
	};

	public TextResource getMapParsFragment() {
		return TextResource.create( "chunk/map_pars_fragment.glsl" );
	};

	public TextResource getMapParsVertex() {
		return TextResource.create( "chunk/map_pars_vertex.glsl" );
	};

	public TextResource getMapParticleFragment() {
		return TextResource.create( "chunk/map_particle_fragment.glsl" );
	};

	public TextResource getMapParticleParsFragment() {
		return TextResource.create( "chunk/map_particle_pars_fragment.glsl" );
	};

	public TextResource getMapVertex() {
		return TextResource.create( "chunk/map_vertex.glsl" );
	};

	public TextResource getMorphnormalVertex() {
		return TextResource.create( "chunk/morphnormal_vertex.glsl" );
	};

	public TextResource getMorphtargetParsVertex() {
		return TextResource.create( "chunk/morphtarget_pars_vertex.glsl" );
	};

	public TextResource getMorphtargetVertex() {
		return TextResource.create( "chunk/morphtarget_vertex.glsl" );
	};
	
	public TextResource getNormalmapParsFragment() {
		return TextResource.create( "chunk/normalmap_pars_fragment.glsl" );
	};

	public TextResource getShadowmapFragment() {
		return TextResource.create( "chunk/shadowmap_fragment.glsl" );
	};

	public TextResource getShadowmapParsFragment() {
		return TextResource.create( "chunk/shadowmap_pars_fragment.glsl" );
	};

	public TextResource getShadowmapParsVertex() {
		return TextResource.create( "chunk/shadowmap_pars_vertex.glsl" );
	};

	public TextResource getShadowmapVertex() {
		return TextResource.create( "chunk/shadowmap_vertex.glsl" );
	};

	public TextResource getSkinningParsVertex() {
		return TextResource.create( "chunk/skinning_pars_vertex.glsl" );
	};

	public TextResource getSkinningVertex() {
		return TextResource.create( "chunk/skinning_vertex.glsl" );
	};
	
	public TextResource getSkinBaseVertex() {
		return TextResource.create( "chunk/skinbase_vertex.glsl" );
	};
	
	public TextResource getSkinNormalVertex() {
		return TextResource.create( "chunk/skinnormal_vertex.glsl" );
	};
	
	public TextResource getSpecularmapFragment() {
		return TextResource.create( "chunk/specularmap_fragment.glsl" );
	};
	
	public TextResource getSpecularmapParsFragment() {
		return TextResource.create( "chunk/specularmap_pars_fragment.glsl" );
	};
	
	public TextResource getWorldposVertex() {
		return TextResource.create( "chunk/worldpos_vertex.glsl" );
	};
}
