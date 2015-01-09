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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * Source for all chunks.
 * 
 * @author thothbot
 *
 */
public interface Chunks extends ClientBundle
{
	Chunks INSTANCE = GWT.create(Chunks.class);

	@Source("chunk/alphamap_fragment.glsl")
	TextResource getAlphamapFragment();
	
	@Source("chunk/alphamap_pars_fragment.glsl")
	TextResource getAlphamapParsFragment();

	@Source("chunk/alphatest_fragment.glsl")
	TextResource getAlphatestFragment();
	
	@Source("chunk/bumpmap_pars_fragment.glsl")
	TextResource getBumpmapParsFragment();

	@Source("chunk/color_fragment.glsl")
	TextResource getColorFragment();

	@Source("chunk/color_pars_fragment.glsl")
	TextResource getColorParsFragment();

	@Source("chunk/color_pars_vertex.glsl")
	TextResource getColorParsVertex();

	@Source("chunk/color_vertex.glsl")
	TextResource getColorVertex();

	@Source("chunk/defaultnormal_vertex.glsl")
	TextResource getDefaultNormalVertex();
		
	@Source("chunk/default_vertex.glsl")
	TextResource getDefaultVertex();

	@Source("chunk/envmap_fragment.glsl")
	TextResource getEnvmapFragment();

	@Source("chunk/envmap_pars_fragment.glsl")
	TextResource getEnvmapParsFragment();

	@Source("chunk/envmap_pars_vertex.glsl")
	TextResource getEnvmapParsVertex();

	@Source("chunk/envmap_vertex.glsl")
	TextResource getEnvmapVertex();

	@Source("chunk/fog_fragment.glsl")
	TextResource getFogFragment();

	@Source("chunk/fog_pars_fragment.glsl")
	TextResource getFogParsFragment();

	@Source("chunk/lightmap_fragment.glsl")
	TextResource getLightmapFragment();

	@Source("chunk/lightmap_pars_fragment.glsl")
	TextResource getLightmapParsFragment();

	@Source("chunk/lightmap_pars_vertex.glsl")
	TextResource getLightmapParsVertex();

	@Source("chunk/lightmap_vertex.glsl")
	TextResource getLightmapVertex();

	@Source("chunk/lights_lambert_pars_vertex.glsl")
	TextResource getLightsLambertParsVertex();

	@Source("chunk/lights_lambert_vertex.glsl")
	TextResource getLightsLambertVertex();

	@Source("chunk/lights_phong_fragment.glsl")
	TextResource getLightsPhongFragment();

	@Source("chunk/lights_phong_pars_fragment.glsl")
	TextResource getLightsPhongParsFragment();

	@Source("chunk/lights_phong_pars_vertex.glsl")
	TextResource getLightsPhongParsVertex();

	@Source("chunk/lights_phong_vertex.glsl")
	TextResource getLightsPhongVertex();

	@Source("chunk/linear_to_gamma_fragment.glsl")
	TextResource getLinearToGammaFragment();
	
	@Source("chunk/logdepthbuf_fragment.glsl")
	TextResource getLogdepthbufFragment();
	
	@Source("chunk/logdepthbuf_par_fragment.glsl")
	TextResource getLogdepthbufParFragment();
	
	@Source("chunk/logdepthbuf_par_vertex.glsl")
	TextResource getLogdepthbufParVertex();
	
	@Source("chunk/logdepthbuf_vertex.glsl")
	TextResource getLogdepthbufVertex();

	@Source("chunk/map_fragment.glsl")
	TextResource getMapFragment();

	@Source("chunk/map_pars_fragment.glsl")
	TextResource getMapParsFragment();

	@Source("chunk/map_pars_vertex.glsl")
	TextResource getMapParsVertex();

	@Source("chunk/map_particle_fragment.glsl")
	TextResource getMapParticleFragment();

	@Source("chunk/map_particle_pars_fragment.glsl")
	TextResource getMapParticleParsFragment();

	@Source("chunk/map_vertex.glsl")
	TextResource getMapVertex();

	@Source("chunk/morphnormal_vertex.glsl")
	TextResource getMorphnormalVertex();

	@Source("chunk/morphtarget_pars_vertex.glsl")
	TextResource getMorphtargetParsVertex();

	@Source("chunk/morphtarget_vertex.glsl")
	TextResource getMorphtargetVertex();
	
	@Source("chunk/normalmap_pars_fragment.glsl")
	TextResource getNormalmapParsFragment();

	@Source("chunk/shadowmap_fragment.glsl")
	TextResource getShadowmapFragment();

	@Source("chunk/shadowmap_pars_fragment.glsl")
	TextResource getShadowmapParsFragment();

	@Source("chunk/shadowmap_pars_vertex.glsl")
	TextResource getShadowmapParsVertex();

	@Source("chunk/shadowmap_vertex.glsl")
	TextResource getShadowmapVertex();

	@Source("chunk/skinning_pars_vertex.glsl")
	TextResource getSkinningParsVertex();

	@Source("chunk/skinning_vertex.glsl")
	TextResource getSkinningVertex();
	
	@Source("chunk/skinbase_vertex.glsl")
	TextResource getSkinBaseVertex();
	
	@Source("chunk/skinnormal_vertex.glsl")
	TextResource getSkinNormalVertex();
	
	@Source("chunk/specularmap_fragment.glsl")
	TextResource getSpecularmapFragment();
	
	@Source("chunk/specularmap_pars_fragment.glsl")
	TextResource getSpecularmapParsFragment();
	
	@Source("chunk/worldpos_vertex.glsl")
	TextResource getWorldposVertex();
}
