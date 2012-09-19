/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

	@Source("chunk/alphatest_fragment.chunk")
	TextResource getAlphatestFragment();
	
	@Source("chunk/bumpmap_pars_fragment.chunk")
	TextResource getBumpmapParsFragment();

	@Source("chunk/color_fragment.chunk")
	TextResource getColorFragment();

	@Source("chunk/color_pars_fragment.chunk")
	TextResource getColorParsFragment();

	@Source("chunk/color_pars_vertex.chunk")
	TextResource getColorParsVertex();

	@Source("chunk/color_vertex.chunk")
	TextResource getColorVertex();

	@Source("chunk/defaultnormal_vertex.chunk")
	TextResource getDefaultNormalVertex();
	
	@Source("chunk/default_pars_vertex.chunk")
	TextResource getDefaultParsVertex();
	
	@Source("chunk/default_pars_fragment.chunk")
	TextResource getDefaultParsFragment();
	
	@Source("chunk/default_vertex.chunk")
	TextResource getDefaultVertex();

	@Source("chunk/envmap_fragment.chunk")
	TextResource getEnvmapFragment();

	@Source("chunk/envmap_pars_fragment.chunk")
	TextResource getEnvmapParsFragment();

	@Source("chunk/envmap_pars_vertex.chunk")
	TextResource getEnvmapParsVertex();

	@Source("chunk/envmap_vertex.chunk")
	TextResource getEnvmapVertex();

	@Source("chunk/fog_fragment.chunk")
	TextResource getFogFragment();

	@Source("chunk/fog_pars_fragment.chunk")
	TextResource getFogParsFragment();

	@Source("chunk/lightmap_fragment.chunk")
	TextResource getLightmapFragment();

	@Source("chunk/lightmap_pars_fragment.chunk")
	TextResource getLightmapParsFragment();

	@Source("chunk/lightmap_pars_vertex.chunk")
	TextResource getLightmapParsVertex();

	@Source("chunk/lightmap_vertex.chunk")
	TextResource getLightmapVertex();

	@Source("chunk/lights_lambert_pars_vertex.chunk")
	TextResource getLightsLambertParsVertex();

	@Source("chunk/lights_lambert_vertex.chunk")
	TextResource getLightsLambertVertex();

	@Source("chunk/lights_phong_fragment.chunk")
	TextResource getLightsPhongFragment();

	@Source("chunk/lights_phong_pars_fragment.chunk")
	TextResource getLightsPhongParsFragment();

	@Source("chunk/lights_phong_pars_vertex.chunk")
	TextResource getLightsPhongParsVertex();

	@Source("chunk/lights_phong_vertex.chunk")
	TextResource getLightsPhongVertex();

	@Source("chunk/linear_to_gamma_fragment.chunk")
	TextResource getLinearToGammaFragment();

	@Source("chunk/map_fragment.chunk")
	TextResource getMapFragment();

	@Source("chunk/map_pars_fragment.chunk")
	TextResource getMapParsFragment();

	@Source("chunk/map_pars_vertex.chunk")
	TextResource getMapParsVertex();

	@Source("chunk/map_particle_fragment.chunk")
	TextResource getMapParticleFragment();

	@Source("chunk/map_particle_pars_fragment.chunk")
	TextResource getMapParticleParsFragment();

	@Source("chunk/map_vertex.chunk")
	TextResource getMapVertex();

	@Source("chunk/morphnormal_vertex.chunk")
	TextResource getMorphnormalVertex();

	@Source("chunk/morphtarget_pars_vertex.chunk")
	TextResource getMorphtargetParsVertex();

	@Source("chunk/morphtarget_vertex.chunk")
	TextResource getMorphtargetVertex();

	@Source("chunk/shadowmap_fragment.chunk")
	TextResource getShadowmapFragment();

	@Source("chunk/shadowmap_pars_fragment.chunk")
	TextResource getShadowmapParsFragment();

	@Source("chunk/shadowmap_pars_vertex.chunk")
	TextResource getShadowmapParsVertex();

	@Source("chunk/shadowmap_vertex.chunk")
	TextResource getShadowmapVertex();

	@Source("chunk/skinning_pars_vertex.chunk")
	TextResource getSkinningParsVertex();

	@Source("chunk/skinning_vertex.chunk")
	TextResource getSkinningVertex();
	
	@Source("chunk/skinbase_vertex.chunk")
	TextResource getSkinBaseVertex();
	
	@Source("chunk/skinnormal_vertex.chunk")
	TextResource getSkinNormalVertex();
	
	@Source("chunk/specularmap_fragment.chunk")
	TextResource getSpecularmapFragment();
	
	@Source("chunk/specularmap_pars_fragment.chunk")
	TextResource getSpecularmapParsFragment();
	
	@Source("chunk/worldpos_vertex.chunk")
	TextResource getWorldposVertex();
}
